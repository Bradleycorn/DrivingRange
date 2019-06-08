package net.bradball.android.drivingrange.ui.range


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import net.bradball.android.drivingrange.R
import net.bradball.android.drivingrange.ui.LocationProviderActivity

/**
 * A simple [Fragment] subclass.
 */
class PersimmonFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_persimmon, container, false)
    }

    override fun onResume() {
        super.onResume()
        val locationActivity = (activity as? LocationProviderActivity)
        if (locationActivity != null) {
            locationActivity.locationLiveData.observe(viewLifecycleOwner, Observer {
                Log.d(TAG, "Location Received: $it")
            })

            locationActivity.locationErrors.observe(viewLifecycleOwner, Observer {
                val navController = findNavController()
                //navController.popBackStack()
            })
        }
    }

    companion object {
        private val TAG = PersimmonFragment::class.java.simpleName
    }


}
