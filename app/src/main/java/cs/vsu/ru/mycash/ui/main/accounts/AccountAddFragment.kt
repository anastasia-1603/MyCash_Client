package cs.vsu.ru.mycash.ui.main.accounts

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cs.vsu.ru.mycash.R

class AccountAddFragment : Fragment() {

    companion object {
        fun newInstance() = AccountAddFragment()
    }

    private lateinit var viewModel: AccountAddViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_account_add, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(AccountAddViewModel::class.java)
        // TODO: Use the ViewModel
    }

}