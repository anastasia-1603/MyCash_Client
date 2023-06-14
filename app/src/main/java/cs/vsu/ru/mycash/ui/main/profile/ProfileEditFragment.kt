package cs.vsu.ru.mycash.ui.main.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import cs.vsu.ru.mycash.databinding.FragmentProfileBinding
import cs.vsu.ru.mycash.databinding.FragmentProfileEditBinding
import cs.vsu.ru.mycash.utils.AppPreferences

class ProfileEditFragment : Fragment() {
    private var _binding: FragmentProfileEditBinding? = null
    private lateinit var appPrefs: AppPreferences
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val profileViewModel = ViewModelProvider(this)[ProfileEditViewModel::class.java]
        appPrefs = activity?.let { AppPreferences(it) }!!
        _binding = FragmentProfileEditBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.buttonSave.setOnClickListener {
            appPrefs.getNotifications = binding.checkBox.isChecked
            findNavController().navigateUp()
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}