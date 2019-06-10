package net.bradball.android.drivingrange.data.models

import java.lang.Exception


enum class LOCATION_ERROR_TYPE {
    LOCATION_PERMISSIONS,
    GOOGLE_PLAY_SERVICES,
    LOCATION_SETTINGS
}

class LocationCheckError(val type: LOCATION_ERROR_TYPE,
                         val playServicesError: Int? = null,
                         val exception: Exception? = null) {

    companion object {

    }




}