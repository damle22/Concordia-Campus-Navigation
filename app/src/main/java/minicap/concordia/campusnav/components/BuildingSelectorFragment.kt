package minicap.concordia.ca

import android.animation.ValueAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import minicap.concordia.campusnav.R
import minicap.concordia.campusnav.buildingmanager.ConcordiaBuildingManager
import minicap.concordia.campusnav.buildingmanager.entities.Building
import minicap.concordia.campusnav.buildingmanager.enumerations.CampusName
import minicap.concordia.campusnav.components.BuildingAdapter
import minicap.concordia.campusnav.databinding.FragmentBuildingSelectorBinding

class BuildingSelectorFragment : Fragment() {

    private var isExpanded = false // tracks whether the layout is expanded or collapsed
    private var _binding: FragmentBuildingSelectorBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBuildingSelectorBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Animate margin on click of the root view
        binding.buildingSelectorRoot.setOnClickListener {
            animateMargin(binding.buildingSelectorRoot)
        }

        // Set up RecyclerViews with GridLayoutManager (2 columns)
        binding.sgwRecyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.loyRecyclerView.layoutManager = GridLayoutManager(requireContext(), 2)

        // Fetch building data and update RecyclerViews
        fetchBuildings()
    }

    /**
     * Fetches building data from ConcordiaBuildingManager by campus.
     */
    private fun fetchBuildings() {
        val buildingManager = ConcordiaBuildingManager.getInstance()

        // Retrieve the list of buildings for each campus
        val sgwBuildings: MutableList<Building> = buildingManager.getBuildingsForCampus(CampusName.SGW)
        val loyBuildings: MutableList<Building> = buildingManager.getBuildingsForCampus(CampusName.LOYOLA)

        // Update the RecyclerViews with the building lists
        binding.sgwRecyclerView.adapter = BuildingAdapter(sgwBuildings)
        binding.loyRecyclerView.adapter = BuildingAdapter(loyBuildings)
    }

    private fun animateMargin(rootView: View) {
        val currentMarginPx = getTopMargin(rootView)
        val targetMarginPx = if (!isExpanded) dpToPx(100) else dpToPx(450)
        isExpanded = !isExpanded

        val animator = ValueAnimator.ofInt(currentMarginPx, targetMarginPx)
        animator.duration = 400
        animator.addUpdateListener { valueAnimator ->
            val animatedValue = valueAnimator.animatedValue as Int
            val params = rootView.layoutParams as MarginLayoutParams
            params.topMargin = animatedValue
            rootView.layoutParams = params
        }
        animator.start()
    }

    private fun getTopMargin(view: View): Int {
        val params = view.layoutParams as MarginLayoutParams
        return params.topMargin
    }

    private fun dpToPx(dp: Int): Int {
        return (dp * resources.displayMetrics.density).toInt()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
