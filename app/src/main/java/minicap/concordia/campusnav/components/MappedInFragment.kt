package minicap.concordia.campusnav.components

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mappedin.sdk.MPIMapView
import com.mappedin.sdk.listeners.MPIMapClickListener
import com.mappedin.sdk.listeners.MPIMapViewListener
import com.mappedin.sdk.models.MPIHeader
import com.mappedin.sdk.web.MPIOptions
import minicap.concordia.campusnav.R


/**
 * A simple [Fragment] subclass.
 * Use the [MappedInFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MappedInFragment : Fragment() {

    lateinit var viewListener: MPIMapViewListener
    lateinit var clickListener: MPIMapClickListener
    lateinit var map: MPIMapView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_mapped_in, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val map = view.findViewById<MPIMapView>(R.id.mapped_in_map);

        map.loadVenue(MPIOptions.Init("5eab30aa91b055001a68e996", "RJyRXKcryCMy4erZqqCbuB1NbR66QTGNXVE0x3Pg6oCIlUR1",
            "mappedin-demo-mall", headers = listOf(MPIHeader("testName", "testValue"))),
            MPIOptions.ShowVenue(labelAllLocationsOnInit = true, backgroundColor = "#CDCDCD")) {}

        this.map = map
        map.listener = this.viewListener
        map.mapClickListener = this.clickListener

    }

    fun getMapView() = map;

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment MappedInFragment.
         */
        @JvmStatic
        fun newInstance(viewListener: MPIMapViewListener, clickListener: MPIMapClickListener): MappedInFragment {
            val map = MappedInFragment()
            map.viewListener = viewListener
            map.clickListener = clickListener
            return map
        }
    }
}