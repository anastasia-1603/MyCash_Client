package cs.vsu.ru.mycash.ui.main.accounts

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import cs.vsu.ru.mycash.data.Account

class AccountsViewModel : ViewModel() {
    private val _accountList: MutableLiveData<List<Account>> by lazy {
        MutableLiveData<List<Account>>()
    }
    val accountList : LiveData<List<Account>> = _accountList

    fun setAccountsList(accounts: List<Account>)
    {
        _accountList.value = accounts
    }
}
