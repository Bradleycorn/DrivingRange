package net.bradball.android.drivingrange.data.repositories

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import net.bradball.android.drivingrange.data.models.LOCATION_ERROR_TYPE
import net.bradball.android.drivingrange.data.models.LocationCheckError
import net.bradball.android.drivingrange.utilities.ObservableEvent
import javax.inject.Inject

class LocationRepository @Inject constructor(
    private val locationSettingsClient: SettingsClient,
    private val locationClient: FusedLocationProviderClient
) {

    private val TAG = LocationRepository::class.java.simpleName

    companion object {
        private const val LOCATION_UPDATE_INTERVAL_SECONDS = 1
        private const val LOCATION_FAST_INTERVAL_SECONDS = .5
        private const val PIN_MINIMUM_DISPLACEMENT_METERS = 1F
        private const val RANGE_MINIMUM_DISPLACEMENT_METERS = 402.336F

        enum class LOCATION_TYPE(val displacement: Float) {
            RANGE(RANGE_MINIMUM_DISPLACEMENT_METERS),
            PIN(PIN_MINIMUM_DISPLACEMENT_METERS)
        }
    }

    private fun getLocationRequest(locationType: LOCATION_TYPE): LocationRequest {
        return LocationRequest.create().apply {
                interval = (LOCATION_UPDATE_INTERVAL_SECONDS * 1000).toLong()
                fastestInterval = (LOCATION_FAST_INTERVAL_SECONDS * 1000).toLong()
                priority = LocationRequest.PRIORITY_HIGH_ACCURACY
                smallestDisplacement = locationType.displacement
            }
    }

    private fun getLocationSettingsRequest(locationRequest: LocationRequest): LocationSettingsRequest {
        return LocationSettingsRequest.Builder().addLocationRequest(locationRequest).build()
    }

    private val locationCallback = object: LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            Log.d(TAG, "Location Received: $locationResult")
            //TODO: Set LiveData Value
            //_locationLiveData.value = locationResult.lastLocation
        }
    }

    private val _locationErrors = MutableLiveData<ObservableEvent<LocationCheckError>>()
    val locationErrors: LiveData<ObservableEvent<LocationCheckError>> = _locationErrors

    fun trackLocation(context: Context, locationType: LOCATION_TYPE): LiveData<Location> {
        return object: MediatorLiveData<Location>() {
            override fun onActive() {
                super.onActive()
                Log.d(TAG, "Location LiveData is Active")
                if (checkLocationPermissions(context) && checkGooglePlayServices(context)) {
                    Log.d(TAG, "All Systems Go!")
                    startLocationUpdates()
                }
            }

            override fun onInactive() {
                super.onInactive()
                Log.d(TAG, "Location LiveData is InActive")
                locationClient.removeLocationUpdates(locationCallback)
            }
        }
    }

    fun onLocationPermissionsResult(result: Int?) {
        if (result == PackageManager.PERMISSION_GRANTED) {
            // TODO ... we have permissions, keep going ...
        } else {
            // TODO ... Consumer should show an error, all we need to do here is stop.
        }
    }

    fun onLocationSettingsResult(result: Int) {
        if (result == Activity.RESULT_OK) {
            // TODO ... Start up the GPS radio!
        } else {
            // TODO ... Consumer should show an error, all we need to do here is stop.
        }
    }

    private fun checkLocationPermissions(context: Context): Boolean {
        Log.d(TAG, "Checking Location Permissions...")
        val locationPermission = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
        val hasPermission = locationPermission == PackageManager.PERMISSION_GRANTED

        if (!hasPermission) {
            _locationErrors.value = ObservableEvent(LocationCheckError(LOCATION_ERROR_TYPE.LOCATION_PERMISSIONS))
        }

        Log.d(TAG, "Location Permissions Granted: $hasPermission")
        return hasPermission
    }

    private fun checkGooglePlayServices(context: Context): Boolean {
        val google = GoogleApiAvailability.getInstance()

        val hasGooglePlay = when (val services = google.isGooglePlayServicesAvailable(context)) {
            ConnectionResult.SERVICE_MISSING,
            ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED,
            ConnectionResult.SERVICE_DISABLED -> {
                _locationErrors.value = ObservableEvent(LocationCheckError(LOCATION_ERROR_TYPE.GOOGLE_PLAY_SERVICES, playServicesError = services))
                false
            }
            else -> true
        }

        Log.d(TAG, "Has Google Play Services: $hasGooglePlay")
        return hasGooglePlay
    }


    private fun checkLocationSettings(locationType: LOCATION_TYPE) {
        val locationRequest = getLocationRequest(locationType)
        val locationSettingsRequest = getLocationSettingsRequest(locationRequest)

        Log.d(TAG, "Checking Location settings ... ")
        locationSettingsClient.checkLocationSettings(locationSettingsRequest)
            .addOnSuccessListener() {
                Log.d(TAG,"Location enabled.")
                watchLocation()
            }
            .addOnFailureListener() { e ->
                _locationErrors.value = ObservableEvent(LocationCheckError(LOCATION_ERROR_TYPE.LOCATION_SETTINGS, exception = e))
            }
    }

}