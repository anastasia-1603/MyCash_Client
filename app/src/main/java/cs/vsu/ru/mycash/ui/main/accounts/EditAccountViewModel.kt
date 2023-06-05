package cs.vsu.ru.mycash.ui.main.accounts

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import cs.vsu.ru.mycash.data.Account
import cs.vsu.ru.mycash.data.AccountDto

class EditAccountViewModel : ViewModel() {

    private val _account: MutableLiveData<Account> by lazy {
        MutableLiveData<Account>()
    }
    val account : LiveData<Account> = _account

    fun setAccount(account: Account)
    {
        _account.value = account
    }
}