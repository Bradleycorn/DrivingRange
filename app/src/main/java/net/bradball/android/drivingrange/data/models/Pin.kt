package net.bradball.android.drivingrange.data.models

import android.location.Location

data class Pin(val name: String,
               val location: Location,
               val sortOrder: Int)