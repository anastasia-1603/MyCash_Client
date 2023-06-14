package cs.vsu.ru.mycash.ui.main.diagrams

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import cs.vsu.ru.mycash.data.Account
import cs.vsu.ru.mycash.data.AccountDto
import cs.vsu.ru.mycash.data.diagrams.AllMonthData
import cs.vsu.ru.mycash.data.diagrams.AnalyticsResponse
import cs.vsu.ru.mycash.data.diagrams.DayCategoriesSum
import java.util.*

class DiagramsViewModel : ViewModel() {

    private val _accountList: MutableLiveData<List<Account>> by lazy {
        MutableLiveData<List<Account>>()
    }

    val accountList : LiveData<List<Account>> = _accountList

    fun setAccountList(accounts: List<Account>)
    {
        _accountList.value = accounts
        val ind = accountIndex.value
        if (ind != null) {
            accountList.value?.get(ind)?.let { setAccountName(it.name) }
        }
    }

    private val _accountIndex: MutableLiveData<Int> by lazy {
        MutableLiveData<Int>(0)
    }
    val accountIndex: LiveData<Int> = _accountIndex

    fun setAccountIndex(index: Int) {
        _accountIndex.value = index
        accountList.value?.get(index)?.let { setAccountName(it.name) }
    }

    fun decrementAccountIndex() {
        if (_accountIndex.value == 0)
        {
            setAccountIndex((_accountList.value?.size ?: 1) -1)
        }
        else
        {
            setAccountIndex(_accountIndex.value!! - 1)
        }

    }
    fun incrementAccountIndex() {
        Log.e("_accountList.value?.size", _accountList.value?.size.toString())
        if (_accountIndex.value == _accountList.value?.size?.minus(1))
        {

            setAccountIndex(0)
        }
        else
        {
            setAccountIndex(_accountIndex.value!! + 1)
        }
    }

    private val _accountName: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    val accountName: LiveData<String> = _accountName
    fun setAccountName(accountName: String) {
        _accountName.value = accountName
    }

    private val _date: MutableLiveData<Calendar> by lazy {
        MutableLiveData<Calendar>(Calendar.getInstance())
    }
    val date: LiveData<Calendar> = _date

    fun setDate(date: Calendar) {
        _date.value = date
    }

    private val _data: MutableLiveData<AnalyticsResponse> by lazy {
        MutableLiveData<AnalyticsResponse>()
    }

    val data : LiveData<AnalyticsResponse> = _data

    fun setData(data: AnalyticsResponse)
    {
        _data.value = data
    }
}