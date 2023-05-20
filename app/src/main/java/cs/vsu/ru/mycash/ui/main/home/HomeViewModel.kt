package cs.vsu.ru.mycash.ui.main.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import cs.vsu.ru.mycash.data.Operation

class HomeViewModel : ViewModel() {

    private val _accountName: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
    private val _balance: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
    val accountName: LiveData<String> = _accountName
    val balance: LiveData<String> = _balance

    private val _operationList: MutableLiveData<List<Operation>> by lazy {
        MutableLiveData<List<Operation>>()
    }

    val operationList: LiveData<List<Operation>> = _operationList


    fun setAccountName(accountName: String) {
        _accountName.value = accountName
    }

    fun setBalance(balance: String) {
        _balance.value = balance
    }
}