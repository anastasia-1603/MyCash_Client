package cs.vsu.ru.mycash.ui.main.home

import android.util.Log
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import cs.vsu.ru.mycash.api.ApiService
import cs.vsu.ru.mycash.data.Account
import cs.vsu.ru.mycash.data.Operation
import java.util.Calendar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import androidx.fragment.app.activityViewModels

class HomeViewModel : ViewModel() {

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

    private val _accountList: MutableLiveData<List<Account>> by lazy {
        MutableLiveData<List<Account>>()
    }
    val accountList : LiveData<List<Account>> = _accountList

    fun setAccountsList(accounts: List<Account>)
    {
        _accountList.value = accounts
    }

    private val _balance: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
    val balance: LiveData<String> = _balance

    private val _date: MutableLiveData<Calendar> by lazy {
        MutableLiveData<Calendar>(Calendar.getInstance())
    }
    val date: LiveData<Calendar> = _date

    private val _mode: MutableLiveData<Mode> by lazy {
        MutableLiveData<Mode>(Mode.DAY)
    }
    val mode: LiveData<Mode> = _mode

    fun setDate(date: Calendar) {
        _date.value = date
    }

    fun setAccountName(accountName: String) {
        _accountName.value = accountName
    }

    fun setBalance(balance: String) {
        _balance.value = balance + "₽"
    }

    fun setMode(mode: Mode) {
        _mode.value = mode
    }

    enum class Mode {
        DAY,
        MONTH
    }

}