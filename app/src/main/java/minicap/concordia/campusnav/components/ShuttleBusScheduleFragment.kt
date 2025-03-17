package minicap.concordia.campusnav.components.placeholder


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import minicap.concordia.campusnav.databinding.ShuttleBusScheduleBinding


class ShuttleBusScheduleFragment : BottomSheetDialogFragment() {


    private var _binding: ShuttleBusScheduleBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ShuttleBusScheduleBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
