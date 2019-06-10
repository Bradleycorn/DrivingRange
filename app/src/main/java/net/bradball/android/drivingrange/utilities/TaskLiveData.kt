package net.bradball.android.drivingrange.utilities

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.SettingsClient

private val TAG = "LocationLiveData"

class LocationLiveData: MediatorLiveData<Location>() {

    fun onLocationPermissionsChanged(result: Int?) {

    }

    fun onLocationSettingsChanged(result: Int) {

    }

    private var permissionLiveData: LocationPermissionLiveData? = null
    private var playServicesLiveData: GooglePlayServicesLiveData? = null
    private var settingsLiveData: LocationSettingsLiveData? = null


    override fun onActive() {
        super.onActive()

        addSource(LocationPermissionLiveData()) { permissionResult ->
            if (permissionResult.getContent() == true) {
                addSource(GooglePlayServicesLiveData()) { playServicesResult ->
                    if (playServicesResult.getContent() == true) {
                        addSource(LocationSettingsLiveData()) { locationSettingsResult ->

                            // TODO Start Location Updates

                        }
                    }
                }
            }

        }
    }
}


class LocationPermissionLiveData(private val context: Context): MutableLiveData<ObservableEvent<Boolean>>() {
    override fun onActive() {
        super.onActive()
        Log.d(TAG, "Checking Location Permissions...")
        val locationPermission = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
        val hasPermission = locationPermission == PackageManager.PERMISSION_GRANTED

        Log.d(TAG, "Location Permissions Granted: $hasPermission")
        value = ObservableEvent(hasPermission)
    }

    fun onUserPermissionsResult(result: Int?) {
        if (hasActiveObservers() && result == PackageManager.PERMISSION_GRANTED) {
            value = ObservableEvent(true)
        }
    }
}

class GooglePlayServicesLiveData(private val context: Context): MutableLiveData<ObservableEvent<Boolean>>() {

    var reason: Int? = null
        private set

    override fun onActive() {
        super.onActive()

        val google = GoogleApiAvailability.getInstance()

        reason = google.isGooglePlayServicesAvailable(context)
        val hasGooglePlay = when (reason) {
            ConnectionResult.SERVICE_MISSING,
            ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED,
            ConnectionResult.SERVICE_DISABLED -> false
            else -> true
        }

        Log.d(TAG, "Has Google Play Services: $hasGooglePlay")
        value = ObservableEvent(hasGooglePlay)
    }
}

class LocationSettingsLiveData(
    private val locationSettingsClient: SettingsClient,
    private val locationRequest: LocationRequest): MutableLiveData<ObservableEvent<Boolean>>() {

    var exception: ApiException? = null
        private set

    override fun onActive() {
        super.onActive()

        val locationSettingsRequest = LocationSettingsRequest.Builder().addLocationRequest(locationRequest).build()

        Log.d(TAG, "Checking Location settings ... ")
        locationSettingsClient.checkLocationSettings(locationSettingsRequest)
            .addOnSuccessListener() {
                Log.d(TAG,"Location enabled.")
                value = ObservableEvent(true)
            }
            .addOnFailureListener() { e ->
                exception = (e as? ApiException)
                value = ObservableEvent(false)
            }
    }

    fun onSettingsUpdated(result: Int) {
        if (hasActiveObservers() && result == Activity.RESULT_OK) {
            value = ObservableEvent(true)
        }
    }
}