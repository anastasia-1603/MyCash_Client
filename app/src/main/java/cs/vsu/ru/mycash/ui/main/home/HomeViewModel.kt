package cs.vsu.ru.mycash.ui.main.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {


    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text
//    private val _accountName: MutableLiveData<String> by lazy {
//        MutableLiveData<String>()
//    }
//    private val _balance: MutableLiveData<String> by lazy {
//        MutableLiveData<String>()
//    }
//    val accountName: LiveData<String> = _accountName
//    val balance: LiveData<String> = _balance
//
//
//    fun setAccountName(accountName: String) {
//        _accountName.value = accountName
//    }
//
//    fun setBalance(balance: String) {
//        _balance.value = balance
//    }
}