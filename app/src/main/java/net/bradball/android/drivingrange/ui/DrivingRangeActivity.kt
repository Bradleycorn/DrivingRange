package net.bradball.android.drivingrange.ui

import android.os.Bundle
import android.view.View
import net.bradball.android.drivingrange.R

class DrivingRangeActivity : LocationProviderActivity() {
    companion object {
        private const val PLAY_SERVICES_REQUEST = 1
        private const val GPS_PERMISSION_REQUEST = 2
        private const val GPS_SETTINGS_REQUEST = 3
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun getSnackbarView(): View = findViewById(R.id.main_content_host)


//    override fun onResume() {
//        super.onResume()
//        checkLocationPermissions()
//    }
//
//    private fun checkLocationPermissions() {
//        val locationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
//
//        if (locationPermission != PackageManager.PERMISSION_GRANTED) {
//            requestPermissions()
//        } else {
//            checkGooglePlayServices()
//        }
//    }
//
//
//
//
//
//
//
//
//
//
//
//    private fun requestPermissions() {
//
//        val shouldProvideRationale = ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)
//
//        // Provide an additional rationale to the user. This would happen if the user denied the
//        // request previously, but didn't check the "Don't ask again" checkbox.
//        if (shouldProvideRationale) {
//            TODO("Show a reason for needing location")
//        } else {
//            showPermissionsRequest()
//        }
//    }
//
//    private fun showPermissionsRequest() {
//        ActivityCompat.requestPermissions(
//            this,
//            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
//            GPS_PERMISSION_REQUEST
//        )
//    }
//
//    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
//        when (requestCode) {
//            GPS_PERMISSION_REQUEST -> {
//                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)                  {
//                    checkGooglePlayServices()
//                } else {
//                    // If we can't use GPS, there's no reason to continue.
//                    finish()
//                }
//            }
//            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        }
//    }
//
//
//
//    private fun checkGooglePlayServices() {
//        val google = GoogleApiAvailability.getInstance()
//
//        when (val services = google.isGooglePlayServicesAvailable(this)) {
//            ConnectionResult.SERVICE_MISSING,
//            ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED,
//            ConnectionResult.SERVICE_DISABLED -> {
//                val dialog = google.getErrorDialog(this, services, PLAY_SERVICES_REQUEST)
//                dialog.show()
//            }
//        }
//    }
//
//
//    private fun onSettingsUpdated(settingsUpdated: Boolean) {
//       if (settingsUpdated) {
//           startLocationTracking()
//       }
//    }
//
//    private fun startLocationTracking() {
//        settingsClient.checkLocationSettings(locationSettingsRequst).apply {
//            addOnSuccessListener {
//                locationClient.requestLocationUpdates(locationRequest,
//                    locationCallback,
//                    null /* Looper */)
//            }
//
//            addOnFailureListener { ex ->
//                try {
//                    (ex as ResolvableApiException).startResolutionForResult(this@DrivingRangeActivity, GPS_SETTINGS_REQUEST)
//                } catch (ex: Exception){
//                    //Show a message about manually changing settings
//                }
//            }
//        }
//    }
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//
//        when (requestCode) {
//            GPS_SETTINGS_REQUEST -> onSettingsUpdated(resultCode == Activity.RESULT_OK)
//        }
//    }


}
