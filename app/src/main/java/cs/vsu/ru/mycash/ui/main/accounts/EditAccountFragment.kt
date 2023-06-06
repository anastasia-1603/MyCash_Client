package cs.vsu.ru.mycash.ui.main.accounts

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import cs.vsu.ru.mycash.R
import cs.vsu.ru.mycash.api.ApiClient
import cs.vsu.ru.mycash.api.ApiService
import cs.vsu.ru.mycash.data.Account
import cs.vsu.ru.mycash.databinding.FragmentEditAccountBinding
import cs.vsu.ru.mycash.utils.AppPreferences
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditAccountFragment : Fragment() {

    private var _binding: FragmentEditAccountBinding? = null
    private val binding get() = _binding!!
    private lateinit var apiService: ApiService
    private lateinit var appPrefs: AppPreferences
    private val editAccountViewModel: EditAccountViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        appPrefs = activity?.let { AppPreferences(it) }!!
        _binding = FragmentEditAccountBinding.inflate(inflater, container, false)

        val menu = requireActivity().findViewById<BottomNavigationView>(R.id.nav_view).menu
        menu.findItem(R.id.accountsFragment).isChecked = true

        binding.cancelButton.setOnClickListener {
            findNavController().navigateUp()
        }

        val account = editAccountViewModel.account.value
        if (account != null)
        {
            binding.accountName.setText(account.name)
            binding.goal.setText(account.target.toString())
            binding.limit.setText(account.target.toString())
        }

        val accountName = binding.accountName

        binding.saveButton.setOnClickListener {
            if (accountName.text.trim().isEmpty())
            {
                accountName.error = "Введите название счета"
            }
            else
            {
                updateAccount(accountName.text.toString())
            }
        }
        return binding.root
    }

    //    val name: String,
//    val balance: Double,
//    val target: Double,
//    val spendingLimit: Double,
//    val isLimited: Boolean
    private fun updateAccount(accountName: String)
    {
        apiService = ApiClient.getClient(appPrefs.token.toString())
        val goal = binding.goal
        val target = if (goal.text.trim().isEmpty()) { 0.0 } else {
            goal.text.toString().toDouble()
        }
        val limit = binding.limit
        val isLimited = limit.text.trim().isNotEmpty()
        val account = Account(accountName,
            0.0,
            target, 0.0, isLimited)
        apiService.updateAccount(account).enqueue(object: Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                findNavController().navigateUp()
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(context, t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}