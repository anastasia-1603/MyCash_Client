package cs.vsu.ru.mycash.ui.onboarding

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AccountInitViewModel : ViewModel() {

    private val _accountName: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
    val accountName: LiveData<String> = _accountName

    fun setAccountName(accountName: String) {
        _accountName.value = accountName
    }

    private val _balance: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
    val balance: LiveData<String> = _balance

    fun setBalance(balance: String) {
        _balance.value = balance + "₽"
    }

}