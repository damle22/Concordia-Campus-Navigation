package minicap.concordia.campusnav.components

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ScrollView
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import minicap.concordia.campusnav.buildingmanager.ConcordiaBuildingManager
import minicap.concordia.campusnav.buildingmanager.entities.Building
import minicap.concordia.campusnav.buildingmanager.enumerations.BuildingName
import minicap.concordia.campusnav.buildingmanager.enumerations.CampusName
import minicap.concordia.campusnav.components.BuildingAdapter
import minicap.concordia.campusnav.components.BuildingInfoBottomSheetFragment
import minicap.concordia.campusnav.databinding.FragmentBuildingSelectorBinding

class BuildingSelectorFragment : BottomSheetDialogFragment() {

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

        val bottomSheet = dialog?.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
        bottomSheet?.let {
            val behavior = BottomSheetBehavior.from(it)
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
            behavior.skipCollapsed = true
            behavior.isHideable = true

            // Set initial height to 90% of screen
            val displayMetrics = resources.displayMetrics
            val screenHeight = displayMetrics.heightPixels
            behavior.peekHeight = (screenHeight * 0.9).toInt()
            behavior.maxHeight = screenHeight

            behavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                private var lastSlideOffset = 0f
                private var dragStartOffset = 0f
                private var isDraggingDown = false
                private val dismissThreshold = 0.4f // 40% threshold

                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    // Handle state changes
                }

                override fun onSlide(bottomSheet: View, slideOffset: Float) {
                    val scrollView = binding.root as ScrollView
                    val canScrollUp = scrollView.canScrollVertically(-1)

                    when {
                        // User starts dragging down
                        slideOffset < lastSlideOffset && !isDraggingDown -> {
                            isDraggingDown = true
                            dragStartOffset = slideOffset
                        }

                        // User is scrolling up
                        slideOffset > lastSlideOffset -> {
                            isDraggingDown = false
                        }

                        // Handle dragging down
                        isDraggingDown -> {
                            if (!canScrollUp) { // Only allow dismiss when at top of content
                                val dragDistance = dragStartOffset - slideOffset
                                if (dragDistance >= dismissThreshold) {
                                    behavior.state = BottomSheetBehavior.STATE_HIDDEN
                                }
                            } else {
                                // If content can still scroll up, prevent dismiss
                                behavior.state = BottomSheetBehavior.STATE_EXPANDED
                            }
                        }
                    }

                    lastSlideOffset = slideOffset
                }
            })
        }

        // Set nested scrolling for RecyclerViews
        binding.sgwRecyclerView.isNestedScrollingEnabled = true
        binding.loyRecyclerView.isNestedScrollingEnabled = true

        // Set up RecyclerViews
        binding.sgwRecyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.loyRecyclerView.layoutManager = GridLayoutManager(requireContext(), 2)

        fetchBuildings()
    }

    /**
     * Fetches building data from ConcordiaBuildingManager by campus and attaches click listeners.
     */
    private fun fetchBuildings() {
        val buildingManager = ConcordiaBuildingManager.getInstance()

        // Retrieve the list of buildings for each campus
        val sgwBuildings: MutableList<Building> = buildingManager.getBuildingsForCampus(CampusName.SGW)
        val loyBuildings: MutableList<Building> = buildingManager.getBuildingsForCampus(CampusName.LOYOLA)

        // Create adapter for SGW buildings and set its click listener
        val sgwAdapter = BuildingAdapter(sgwBuildings)
        sgwAdapter.setOnBuildingClickListener { building ->
            showBuildingInformation(building)
        }
        binding.sgwRecyclerView.adapter = sgwAdapter

        // Create adapter for Loyola buildings and set its click listener
        val loyAdapter = BuildingAdapter(loyBuildings)
        loyAdapter.setOnBuildingClickListener { building ->
            showBuildingInformation(building)
        }
        binding.loyRecyclerView.adapter = loyAdapter
    }

    /**
     * Shows an alert dialog with the building details.
     */
    private fun showBuildingInformation(building: Building) {

        val bottomSheet = BuildingInfoBottomSheetFragment.newInstance(building.buildingIdentifier)
        bottomSheet.show(parentFragmentManager, "BuildingInfoBottomSheet")
        dismiss()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
