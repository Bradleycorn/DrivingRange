package net.bradball.android.drivingrange.data.repositories

import android.location.Location
import net.bradball.android.drivingrange.data.models.Range
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RangeRepository @Inject constructor() {
    // For now, just hard code a list of Ranges...
    private val ranges: List<Range> = listOf(
        Range("Persimmon Ridge", "Louisville", "KY", getLocation(38.288457, -85.437437), listOf()),
        Range("Persimmon Ridge - Golf Academy", "Louisville", "KY", getLocation(38.287448, -85.434191), listOf()),
        Range("Polo Fields", "Louisville", "KY", getLocation(38.256344, -85.436854), listOf()),
        Range("Different Strokes", "Louisville", "KY", getLocation(38.287038, -85.683608), listOf())
    )

    private fun getLocation(lat: Double, lng: Double) = Location("DrivingRange").apply {
        latitude = lat
        longitude = lng
    }



    fun getRangesNearLocation(location: Location, distance: Float): List<Range> {
        //TODO: filter list of ranges by location
        return ranges
    }


}