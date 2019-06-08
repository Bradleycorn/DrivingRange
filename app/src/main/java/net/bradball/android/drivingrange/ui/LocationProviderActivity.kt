package net.bradball.android.drivingrange.ui

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import android.location.Location
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.common.api.ApiException
import android.os.Looper
import android.view.View
import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.location.*
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import dagger.android.DaggerActivity
import dagger.android.support.DaggerAppCompatActivity
import net.bradball.android.drivingrange.R
import net.bradball.android.drivingrange.utilities.ObservableEvent


abstract class LocationProviderActivity: DaggerAppCompatActivity() {
    companion object {
        private val TAG = LocationProviderActivity::class.java.simpleName

        private const val PLAY_SERVICES_REQUEST = 1
        private const val GPS_PERMISSION_REQUEST = 2
        private const val GPS_SETTINGS_REQUEST = 3

        private const val LOCATION_UPDATE_INTERVAL_SECONDS = 1
        private const val LOCATION_FAST_INTERVAL_SECONDS = .5
        private const val MINIMUM_DISPLACEMENT_METERS = 1F

        enum class LOCATION_ERROR(@StringRes val message: Int, @StringRes val action: Int?) {
            PERMISSION_DENIED(
                R.string.location_generic_error,
                R.string.button_ok
            ),
            PLAY_SERVICES(
                R.string.location_generic_error,
                R.string.button_ok
            ),
            LOCATION_SETTINGS(
                R.string.location_generic_error,
                R.string.button_ok
            ),
            LOCATION_DISABLED(
                R.string.location_generic_error,
                R.string.button_ok
            )
        }
    }

    abstract fun getSnackbarView(): View

    private val locationRequest: LocationRequest by lazy {
        LocationRequest.create().apply {
            interval = (LOCATION_UPDATE_INTERVAL_SECONDS * 1000).toLong()
            fastestInterval = (LOCATION_FAST_INTERVAL_SECONDS * 1000).toLong()
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            smallestDisplacement =
                MINIMUM_DISPLACEMENT_METERS
        }
    }

    private val locationSettingsRequest: LocationSettingsRequest by lazy {
        LocationSettingsRequest.Builder().addLocationRequest(locationRequest).build()
    }


    private val mSettingsClient: SettingsClient by lazy {
        LocationServices.getSettingsClient(this)
    }

    private val mFusedLocationClient: FusedLocationProviderClient by lazy {
        LocationServices.getFusedLocationProviderClient(this)
    }


    private val _locationLiveData: MutableLiveData<Location> = object: MutableLiveData<Location>() {
        override fun onActive() {
            super.onActive()
            Log.d(TAG, "Location LiveData is Active")
            if (checkLocationPermissions() && checkGooglePlayServices()) {
                Log.d(TAG, "All Systems Go!")
                startLocationUpdates()
            }
        }

        override fun onInactive() {
            super.onInactive()
            Log.d(TAG, "Location LiveData is InActive")
            mFusedLocationClient.removeLocationUpdates(locationCallback)
        }
    }
    val locationLiveData: LiveData<Location> = _locationLiveData

    private val _locationErrors: MutableLiveData<ObservableEvent<LOCATION_ERROR>> = MutableLiveData()
    val locationErrors: LiveData<ObservableEvent<LOCATION_ERROR>> = _locationErrors


    private val locationCallback = object: LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            Log.d(TAG, "Location Received: $locationResult")
            _locationLiveData.value = locationResult.lastLocation
        }
    }


    private fun showErrorSnackbar(error: LOCATION_ERROR, onAction: ((View)->Unit)? = null) {
        val snackbar = Snackbar.make(getSnackbarView(), error.message, Snackbar.LENGTH_INDEFINITE)
        error.action?.let { actionText ->
            snackbar.setAction(actionText) {
                //snackbar.dismiss()
                onAction?.invoke(it)
            }
        }
        snackbar.addCallback(object: BaseTransientBottomBar.BaseCallback<Snackbar>() {
            override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                when (event) {
                    DISMISS_EVENT_CONSECUTIVE,
                    DISMISS_EVENT_MANUAL,
                    DISMISS_EVENT_SWIPE,
                    DISMISS_EVENT_TIMEOUT -> {}
                    else -> {}
                }
            }
        })
        snackbar.show()
    }

    private fun watchLocation() {
        if (_locationLiveData.hasActiveObservers()) {
            Log.d(TAG, "Here we go! Requesting location updates.")
            mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper())
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            GPS_SETTINGS_REQUEST -> onGpsSettingsResult(resultCode)
        }
    }


    private fun checkLocationPermissions(): Boolean {
        Log.d(TAG, "Checking Location Permissions...")
        val locationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
        val hasPermission = locationPermission == PackageManager.PERMISSION_GRANTED

        if (!hasPermission) {
            val shouldProvideRationale =
                ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)

            // Provide an additional rationale to the user. This would happen if the user denied the
            // request previously, but didn't check the "Don't ask again" checkbox.
            if (shouldProvideRationale) {
                // TODO: Show a Snackbar or Dialog, and then ...
                showPermissionsRequest()
            } else {
                showPermissionsRequest()
            }
        }

        Log.d(TAG, "Location Permissions Granted: $hasPermission")
        return hasPermission
    }

    private fun showPermissionsRequest() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            GPS_PERMISSION_REQUEST
        )
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            GPS_PERMISSION_REQUEST -> onGpsPermissionsResult(grantResults)
        }
    }

    private fun onGpsPermissionsResult(results: IntArray) {
        Log.d(TAG, "Checking Permissions Request Result...")
        if (results.getOrNull(0) == PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "Permissions Result: Granted.")
            if (_locationLiveData.hasActiveObservers() && checkGooglePlayServices()) {
                startLocationUpdates()
            }
        } else {
            showErrorSnackbar(Companion.LOCATION_ERROR.PERMISSION_DENIED)
            _locationErrors.value = ObservableEvent(Companion.LOCATION_ERROR.PERMISSION_DENIED)
            Log.d(TAG, "Permissions Result: Denied.")
        }
    }

    private fun checkGooglePlayServices(): Boolean {
        val google = GoogleApiAvailability.getInstance()

        val hasGooglePlay = when (val services = google.isGooglePlayServicesAvailable(this)) {
            ConnectionResult.SERVICE_MISSING,
            ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED,
            ConnectionResult.SERVICE_DISABLED -> {
                val dialog = google.getErrorDialog(this, services,
                    PLAY_SERVICES_REQUEST
                ) {
                    showErrorSnackbar(Companion.LOCATION_ERROR.PLAY_SERVICES)
                   _locationErrors.value = ObservableEvent(Companion.LOCATION_ERROR.PLAY_SERVICES)
                }
                dialog.show()
                false
            }
            else -> true
        }

        Log.d(TAG, "Has Google Play Services: $hasGooglePlay")
        return hasGooglePlay
    }

    private fun startLocationUpdates() {
        Log.d(TAG, "Checking Location settings ... ")
        mSettingsClient.checkLocationSettings(locationSettingsRequest)
            .addOnSuccessListener(this) {
                Log.d(TAG,"Location enabled.")
                watchLocation()
            }
            .addOnFailureListener(this) { e ->
                val statusCode = (e as ApiException).statusCode
                when (statusCode) {
                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> {
                        Log.d(TAG, "Location not enabled. Trying to show user a dialog to enable them...")
                        try {
                            // Show the dialog by calling startResolutionForResult(), and check the
                            // result in onActivityResult().
                            (e as? ResolvableApiException)?.startResolutionForResult(this,
                                GPS_SETTINGS_REQUEST
                            )
                            Log.d(TAG, "Dialog shown. Waiting for user response.")
                        } catch (e: Exception) {
                            Log.i(TAG, "Could not start activity to resolve location settings.")
                            showErrorSnackbar(Companion.LOCATION_ERROR.LOCATION_SETTINGS)
                            _locationErrors.value = ObservableEvent(Companion.LOCATION_ERROR.LOCATION_SETTINGS)
                        }

                    }
                    LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                        val errorMessage = "Location settings are inadequate, and cannot be fixed here. Fix in Settings."
                        Log.e(TAG, "Can't fix Location Settings automatically. User has to manually go to Settings app.")
                        showErrorSnackbar(Companion.LOCATION_ERROR.LOCATION_SETTINGS)
                        _locationErrors.value = ObservableEvent(Companion.LOCATION_ERROR.LOCATION_SETTINGS)
                    }
                }
            }
    }

    private fun onGpsSettingsResult(resultCode: Int) {
        when (resultCode) {
            Activity.RESULT_OK -> {
                Log.d(TAG, "Settings Enabled. Good To go.")
                watchLocation()
            }
            Activity.RESULT_CANCELED -> {
                Log.d(TAG, "Settings not enabled. Need to stop.")
                showErrorSnackbar(Companion.LOCATION_ERROR.LOCATION_DISABLED)
                _locationErrors.value = ObservableEvent(Companion.LOCATION_ERROR.LOCATION_DISABLED)
            }
        }
    }

}