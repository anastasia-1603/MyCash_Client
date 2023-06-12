package cs.vsu.ru.mycash.ui.main.profile

import android.app.AlertDialog
import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import cs.vsu.ru.mycash.R
import cs.vsu.ru.mycash.databinding.FragmentProfileBinding
import cs.vsu.ru.mycash.utils.AppPreferences

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private lateinit var appPrefs: AppPreferences
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val profileViewModel = ViewModelProvider(this)[ProfileViewModel::class.java]
        appPrefs = activity?.let { AppPreferences(it) }!!
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val navController = findNavController()
        binding.buttonCustomize.setOnClickListener {
            navController.navigate(R.id.profileEditFragment)
        }

        binding.buttonOut.setOnClickListener {
            val alertDialogBuilder = AlertDialog.Builder(requireContext())
            alertDialogBuilder.setMessage("Вы уверены, что хотите выйти?")

            alertDialogBuilder.setPositiveButton("Да") { dialog, _ ->
                appPrefs.isAuth = false
                dialog.dismiss()
                navController.navigate(R.id.profileFragment)

            }
            alertDialogBuilder.setNegativeButton("Нет") { dialog, _ ->
                dialog.dismiss()
            }
            val alertDialog = alertDialogBuilder.create()
            alertDialog.show()
        }

        binding.buttonDelete.setOnClickListener {
            val alertDialogBuilder = AlertDialog.Builder(requireContext())
            alertDialogBuilder.setMessage("Вы уверены, что хотите удалить аккаунт?")

            alertDialogBuilder.setPositiveButton("Да") { dialog, _ ->
                appPrefs.isAuth = false
                dialog.dismiss()
                navController.navigate(R.id.profileFragment)

            }
            alertDialogBuilder.setNegativeButton("Нет") { dialog, _ ->
                dialog.dismiss()
            }
            val alertDialog = alertDialogBuilder.create()
            alertDialog.show()
        }
        return root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}