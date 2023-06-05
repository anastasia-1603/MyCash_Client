package cs.vsu.ru.mycash.ui.main.accounts

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import cs.vsu.ru.mycash.R
import cs.vsu.ru.mycash.api.ApiClient
import cs.vsu.ru.mycash.api.ApiService
import cs.vsu.ru.mycash.data.Account
import cs.vsu.ru.mycash.data.RegisterRequest
import cs.vsu.ru.mycash.databinding.FragmentAccountAddBinding
import cs.vsu.ru.mycash.databinding.FragmentAddOperationBinding
import cs.vsu.ru.mycash.ui.main.operation.AddOperationViewModel
import cs.vsu.ru.mycash.utils.AppPreferences
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AccountAddFragment : Fragment() {

    private var _binding: FragmentAccountAddBinding? = null
    private val binding get() = _binding!!
    private lateinit var apiService: ApiService
    private lateinit var appPrefs: AppPreferences
    private lateinit var accountAddViewModel: AccountAddViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        accountAddViewModel = ViewModelProvider(this)[AccountAddViewModel::class.java]
        appPrefs = activity?.let { AppPreferences(it) }!!
        _binding = FragmentAccountAddBinding.inflate(inflater, container, false)

        val menu = requireActivity().findViewById<BottomNavigationView>(R.id.nav_view).menu
        menu.findItem(R.id.accountsFragment).isChecked = true

        binding.cancelButton.setOnClickListener {
            findNavController().navigateUp()
        }

        val accountName = binding.accountName


        binding.saveButton.setOnClickListener {
            if (accountName.text.trim().isEmpty())
            {
                accountName.error = "Введите название счета"
            }
            else
            {
                postAccount(accountName.text.toString())
            }
        }
        return binding.root
    }

//    val name: String,
//    val balance: Double,
//    val target: Double,
//    val spendingLimit: Double,
//    val isLimited: Boolean
    private fun postAccount(accountName: String)
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
        apiService.addAccount(account).enqueue(object: Callback<Void> {
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