package minicap.concordia.ca

import android.animation.ValueAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import androidx.fragment.app.Fragment
import minicap.concordia.campusnav.R

class BuildingSelectorFragment : Fragment() {

    private var isExpanded = false // track whether the layout is “expanded” or “collapsed”

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the building selector layout
        return inflater.inflate(R.layout.fragment_building_selector, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 1) Find the root ConstraintLayout by its ID
        val buildingSelectorRoot = view.findViewById<View>(R.id.buildingSelectorRoot)

        // 2) Set a click listener to animate the margin
        buildingSelectorRoot.setOnClickListener {
            animateMargin(buildingSelectorRoot)
        }
    }

    private fun animateMargin(buildingSelectorRoot: View) {
        // Get the current top margin
        val currentMarginPx = getTopMargin(buildingSelectorRoot)
        // Decide the target margin (toggle between 450dp and 100dp)
        val endMarginPx = if (!isExpanded) dpToPx(100) else dpToPx(450)
        isExpanded = !isExpanded

        // Create a ValueAnimator to smoothly animate from currentMarginPx to endMarginPx
        val animator = ValueAnimator.ofInt(currentMarginPx, endMarginPx)
        animator.duration = 400 // 400ms animation
        animator.addUpdateListener { valueAnimator ->
            val animatedValue = valueAnimator.animatedValue as Int
            val params = buildingSelectorRoot.layoutParams as MarginLayoutParams
            params.topMargin = animatedValue
            buildingSelectorRoot.layoutParams = params
        }
        animator.start()
    }

    // Helper to read the current top margin
    private fun getTopMargin(view: View): Int {
        val params = view.layoutParams as MarginLayoutParams
        return params.topMargin
    }

    // Helper to convert dp -> px
    private fun dpToPx(dp: Int): Int {
        return (dp * resources.displayMetrics.density).toInt()
    }
}
