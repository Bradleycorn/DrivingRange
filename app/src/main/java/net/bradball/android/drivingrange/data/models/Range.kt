package net.bradball.android.drivingrange.data.models

import android.location.Location

data class Range(
    val name: String,
    val city: String,
    val state: String,
    val location: Location,
    val pins: List<Pin>)
