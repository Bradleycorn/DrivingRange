package net.bradball.android.drivingrange.ui.rangeList

import android.app.Application
import android.location.Location
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import net.bradball.android.drivingrange.data.models.Range
import net.bradball.android.drivingrange.data.repositories.RangeRepository
import javax.inject.Inject

class RangeListViewModel @Inject constructor(app: Application, private val rangeRepository: RangeRepository) : AndroidViewModel(app) {

    fun getRangeList(locationLiveData: LiveData<Location>): LiveData<List<Range>> {
        return Transformations.map(locationLiveData) {
            rangeRepository.getRangesNearLocation(it, 1F)
        }
    }


}
