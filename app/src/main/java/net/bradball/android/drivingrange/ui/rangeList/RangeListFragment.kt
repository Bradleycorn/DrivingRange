package net.bradball.android.drivingrange.ui.rangeList

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import dagger.android.support.DaggerFragment

import net.bradball.android.drivingrange.R
import net.bradball.android.drivingrange.data.models.Range
import net.bradball.android.drivingrange.di.ViewModelFactory
import net.bradball.android.drivingrange.ui.LocationProviderActivity
import javax.inject.Inject

class RangeListFragment : DaggerFragment() {

    @Inject
    protected lateinit var viewModelFactory: ViewModelFactory

    private val viewModel by viewModels<RangeListViewModel> { viewModelFactory }

    private val rangeListAdapter = RangeListAdapter()
    private lateinit var rangeList: RecyclerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val viewContainer = inflater.inflate(R.layout.fragment_range_list, container, false)

        rangeList = viewContainer.findViewById(R.id.range_list)

        setupRangeList()

        return viewContainer
    }

    private fun setupRangeList() {
        rangeList.adapter = rangeListAdapter
        rangeList.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)

        val locationProvider = (activity as? LocationProviderActivity)?.locationLiveData ?: throw IllegalStateException("Activity is not a Location Provider")

        viewModel.getRangeList(locationProvider).observe(viewLifecycleOwner, Observer { rangeList ->
            rangeListAdapter.submitList(rangeList)
        })
    }
}


class RangeListViewHolder(itemView: ConstraintLayout): RecyclerView.ViewHolder(itemView) {

    private val nameView: TextView = itemView.findViewById(R.id.range_list_name)
    private val locationView: TextView = itemView.findViewById(R.id.range_list_location)
    private val distanceView: TextView = itemView.findViewById(R.id.range_list_distance)

    fun bindRange(range: Range) {
        nameView.text = range.name
        locationView.text = "${range.city}, ${range.state}"
        distanceView.text = "1.2 miles" //TODO: Set distance properly
    }
}

class RangeListAdapter: ListAdapter<Range, RangeListViewHolder>(rangeDiffer) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RangeListViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.view_holder_range_list, parent, false)
        return RangeListViewHolder(itemView as ConstraintLayout)
    }

    override fun onBindViewHolder(holder: RangeListViewHolder, position: Int) {
        holder.bindRange(getItem(position))
    }

    companion object {
        val rangeDiffer = object: DiffUtil.ItemCallback<Range>() {
            override fun areItemsTheSame(oldItem: Range, newItem: Range): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Range, newItem: Range): Boolean {
                return (
                    oldItem.name == newItem.name &&
                    oldItem.city == newItem.city &&
                    oldItem.state == newItem.state &&
                    oldItem.location.latitude == newItem.location.latitude &&
                    oldItem.location.longitude == newItem.location.longitude
                )
            }
        }
    }
}