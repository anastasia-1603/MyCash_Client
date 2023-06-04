package cs.vsu.ru.mycash.ui.main.accounts

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.adapter.FragmentStateAdapter
import cs.vsu.ru.mycash.R
import cs.vsu.ru.mycash.api.ApiClient
import cs.vsu.ru.mycash.api.ApiService
import cs.vsu.ru.mycash.data.Account
import cs.vsu.ru.mycash.data.Category
import cs.vsu.ru.mycash.data.CategoryType
import cs.vsu.ru.mycash.databinding.FragmentAccountsBinding
import cs.vsu.ru.mycash.ui.main.categories.CategoriesAdapter
import cs.vsu.ru.mycash.ui.main.categories.CategoriesExpensesFragment
import cs.vsu.ru.mycash.ui.main.categories.CategoriesIncomeFragment
import cs.vsu.ru.mycash.utils.AppPreferences
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AccountsFragment : Fragment() {
    private var _binding: FragmentAccountsBinding? = null
    private val binding get() = _binding!!
    private val accountsViewModel: AccountsViewModel by activityViewModels()
    private lateinit var apiService: ApiService
    private lateinit var adapter: AccountsAdapter

    private lateinit var appPrefs: AppPreferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        appPrefs = activity?.let { AppPreferences(it) }!!
        _binding = FragmentAccountsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        loadAccounts()
        val manager = LinearLayoutManager(context)
        adapter = AccountsAdapter(AccountsAdapter.OnClickListener{ category ->
            findNavController().navigate(R.id.editAccountFragment)
            //todo передать категорию
        })

        adapter.data = accountsViewModel.accountList.value ?: emptyList()

        binding.recyclerViewAccounts.layoutManager = manager
        binding.recyclerViewAccounts.adapter = adapter


        accountsViewModel.accountList.observe(viewLifecycleOwner) {
            adapter.data = it
        }

        return root
    }

    private fun loadAccounts() {
        val token = appPrefs.token.toString()
        apiService = ApiClient.getClient(token)
        apiService.getAccounts().enqueue(object : Callback<List<Account>> {
            override fun onResponse(
                call: Call<List<Account>>,
                response: Response<List<Account>>
            ) {
                response.body()?.let {
                    accountsViewModel.setAccountsList(it)
                }
            }

            override fun onFailure(call: Call<List<Account>>, t: Throwable) {
                t.message?.let { Log.e("t", it) }
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}