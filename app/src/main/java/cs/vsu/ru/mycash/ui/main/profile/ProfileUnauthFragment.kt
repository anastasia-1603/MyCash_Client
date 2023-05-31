package cs.vsu.ru.mycash.ui.main.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import cs.vsu.ru.mycash.R
import cs.vsu.ru.mycash.databinding.FragmentProfileBinding
import cs.vsu.ru.mycash.databinding.FragmentProfileUnauthBinding

class ProfileUnauthFragment : Fragment() {
    private var _binding: FragmentProfileUnauthBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileUnauthBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val registerBtn = binding.registerBtn
        registerBtn.setOnClickListener{
            findNavController().navigate(R.id.registerFragment)
        }

        val signInBtn = binding.sigInBtn
        signInBtn.setOnClickListener {
            findNavController().navigate(R.id.signInFragment)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}