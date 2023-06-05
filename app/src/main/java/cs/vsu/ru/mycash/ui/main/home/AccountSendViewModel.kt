package cs.vsu.ru.mycash.ui.main.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import cs.vsu.ru.mycash.data.AccountDto

class AccountSendViewModel : ViewModel() {

    private val _accountIndex: MutableLiveData<Int> by lazy {
        MutableLiveData<Int>(0)
    }
    val accountIndex: LiveData<Int> = _accountIndex

    fun setAccountIndex(index: Int) {
        _accountIndex.value = index
    }

    fun decrementAccountIndex() {
        _accountIndex.value = _accountIndex.value!! - 1
    }

    fun incrementAccountIndex() {
        _accountIndex.value = _accountIndex.value!! + 1
    }

    private val _accountName: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    val accountName: LiveData<String> = _accountName

    fun setAccountName(accountName: String) {
        _accountName.value = accountName
    }

    private val _accountList: MutableLiveData<List<AccountDto>> by lazy {
        MutableLiveData<List<AccountDto>>()
    }

    val accountList : LiveData<List<AccountDto>> = _accountList

    fun setAccountsList(accounts: List<AccountDto>)
    {
        _accountList.value = accounts
    }

    private val _balance: MutableLiveData<Double> by lazy {
        MutableLiveData<Double>()
    }
    val balance: LiveData<Double> = _balance

    fun setBalance(balance: Double) {
        _balance.value = balance
    }



}