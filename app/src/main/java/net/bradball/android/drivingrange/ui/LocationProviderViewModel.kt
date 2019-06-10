package net.bradball.android.drivingrange.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import net.bradball.android.drivingrange.data.repositories.LocationRepository
import javax.inject.Inject

class LocationProviderViewModel @Inject constructor(
    app: Application,
    private val locationRepository: LocationRepository): AndroidViewModel(app) {

    fun getLocationErrors() = locationRepository.locationErrors

    fun onLocationPermissionResult(result: Int?) {
        locationRepository.onLocationPermissionsResult(result)
    }

    fun onLocationSettingsResult(result: Int) {
        locationRepository.onLocationSettingsResult(result)
    }
}