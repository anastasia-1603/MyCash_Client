package cs.vsu.ru.mycash.ui.main.accounts

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.adapter.FragmentStateAdapter
import cs.vsu.ru.mycash.R
import cs.vsu.ru.mycash.api.ApiService
import cs.vsu.ru.mycash.databinding.FragmentAccountsBinding
import cs.vsu.ru.mycash.ui.main.home.TabAllFragment
import cs.vsu.ru.mycash.ui.main.home.TabExpensesFragment
import cs.vsu.ru.mycash.ui.main.home.TabIncomeFragment
import cs.vsu.ru.mycash.utils.AppPreferences
import cs.vsu.ru.mycash.utils.OperationAdapter

class AccountsFragment : Fragment() {
    private var _binding: FragmentAccountsBinding? = null

    private val binding get() = _binding!!

    private lateinit var apiService: ApiService
    private lateinit var appPrefs: AppPreferences
    private lateinit var adapter: AccountsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        appPrefs = activity?.let { AppPreferences(it) }!!
        val accountsViewModel =
            ViewModelProvider(this)[AccountsViewModel::class.java]
        _binding = FragmentAccountsBinding.inflate(inflater, container, false)
        val root: View = binding.root


        val addBtn = binding.floatingActionButton
        addBtn.setOnClickListener{
            if (!appPrefs.isAuth){
                findNavController().navigate(R.id.profileUnauthFragment)
            }
            else
            {
                findNavController().navigate(R.id.accountAddFragment)
            }
        }

        val manager = LinearLayoutManager(context)
        adapter = AccountsAdapter(AccountsAdapter.OnClickListener { _ ->
            findNavController().navigate(R.id.editAccountFragment)
        })

//        val operationViewModel = ViewModelProvider(requireActivity())[OperationViewModel::class.java]


        val accountsList = accountsViewModel.accountList.value
        adapter.data = accountsList ?: emptyList()
        binding.recyclerViewAccounts.layoutManager = manager
        binding.recyclerViewAccounts.adapter = adapter

        accountsViewModel.accountList.observe(viewLifecycleOwner) {
            adapter.data = it
            adapter.notifyDataSetChanged()
        }


        return root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}