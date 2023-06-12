package cs.vsu.ru.mycash.ui.main.accounts

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.yandex.metrica.impl.ob.ol
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

        val account = editAccountViewModel.account.value

        binding.cancelButton.isVisible = editAccountViewModel.accountsNum.value!! > 1
        binding.cancelButton.setOnClickListener {
            val alertDialogBuilder = AlertDialog.Builder(requireContext())
            alertDialogBuilder.setMessage("Вы уверены?")

            alertDialogBuilder.setPositiveButton("Да") { dialog, _ ->
                deleteAccount()
                dialog.dismiss()
                findNavController().navigateUp()
            }
            alertDialogBuilder.setNegativeButton("Нет") { dialog, _ ->
                dialog.dismiss()
            }
            val alertDialog = alertDialogBuilder.create()
            alertDialog.show()

        }

        if (account != null)
        {
            binding.accountName.setText(account.name)
            if (account.target != null)
            {
                binding.goal.setText(account.target.toString())
            }
            else
            {
                binding.goal.setText("")
            }
            if (account.isLimited)
            {
                if (account.spendingLimit != null)
                {
                    binding.limit.setText(account.spendingLimit.toString())
                }
                else
                {
                    binding.limit.setText("")
                }
            }
            else
            {
                binding.limit.setText("")
            }
        }


        binding.saveButton.setOnClickListener {
            val accountName = binding.accountName
            if (accountName.text.trim().isEmpty())
            {
                accountName.error = "Введите название счета"
            }
            else
            {
                if (account != null)
                {
                    val oldAccountName = account.name.toString()
                    account.name = accountName.text.toString()
                    val limit = binding.limit.text.toString()
                    if (limit.trim().isNotEmpty()) {
                        account.spendingLimit = limit.toDouble()
                        account.isLimited = true
                    }
                    val target = binding.goal.text.toString()
                    if (target.trim().isNotEmpty()) {
                        account.target = target.toDouble()
                    }
                    updateAccount(account, oldAccountName)
                }

            }
        }
        return binding.root
    }

    private fun updateAccount(account: Account, oldAccountName: String)
    {
        apiService = ApiClient.getClient(appPrefs.token.toString())

        apiService.updateAccount(oldAccountName, account).enqueue(object: Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(context, t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun deleteAccount() {
        apiService = ApiClient.getClient(appPrefs.token.toString())
        val account = editAccountViewModel.account.value
        if (account != null) {
            apiService.deleteAccount(account.name)
                .enqueue(object : Callback<Void> {
                    override fun onResponse(call: Call<Void>, response: Response<Void>) {
                        findNavController().navigateUp()
                    }

                    override fun onFailure(call: Call<Void>, t: Throwable) {
                        Toast.makeText(context, t.message, Toast.LENGTH_SHORT).show()
                    }

                })

        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}