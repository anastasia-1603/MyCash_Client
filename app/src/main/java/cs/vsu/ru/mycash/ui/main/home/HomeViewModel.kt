package cs.vsu.ru.mycash.ui.main.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import cs.vsu.ru.mycash.data.AccountDto
import java.util.*

class HomeViewModel : ViewModel() {

    private val _accountIndex: MutableLiveData<Int> by lazy {
        MutableLiveData<Int>(0)
    }
    val accountIndex: LiveData<Int> = _accountIndex

    fun setAccountIndex(index: Int) {
        _accountIndex.value = index
    }

    fun decrementAccountIndex() {
        if (_accountIndex.value == 0)
        {
            _accountIndex.value = (_accountList.value?.size ?: 1) -1
        }
        else
        {
            _accountIndex.value = _accountIndex.value!! - 1
        }

    }
    fun incrementAccountIndex() {
        if (_accountIndex.value == _accountList.value?.size?.minus(1))
        {
            _accountIndex.value = 0
        }
        else
        {
            _accountIndex.value = _accountIndex.value!! + 1
        }
    }

    private val _accountName: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    val accountName: LiveData<String> = _accountName

    private val _accountList: MutableLiveData<List<AccountDto>> by lazy {
        MutableLiveData<List<AccountDto>>()
    }
    val accountList: LiveData<List<AccountDto>> = _accountList

    fun setAccountsList(accounts: List<AccountDto>) {
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
        _balance.value = balance
    }

    fun setMode(mode: Mode) {
        _mode.value = mode
    }

    enum class Mode {
        DAY,
        MONTH
    }

}