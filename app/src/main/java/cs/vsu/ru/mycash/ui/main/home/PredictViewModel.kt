package cs.vsu.ru.mycash.ui.main.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import cs.vsu.ru.mycash.data.Account
import cs.vsu.ru.mycash.data.AccountDto
import cs.vsu.ru.mycash.data.Category
import java.util.*

class PredictViewModel : ViewModel() {

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

    private val _accountList: MutableLiveData<List<AccountDto>> by lazy {
        MutableLiveData<List<AccountDto>>()
    }
    val accountList : LiveData<List<AccountDto>> = _accountList

    fun setAccountsList(accounts: List<AccountDto>)
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


    fun setDate(date: Calendar) {
        _date.value = date
    }

    fun setAccountName(accountName: String) {
        _accountName.value = accountName
    }

    fun setBalance(balance: Double) {
        _balance.value = balance.toString() + "₽"
    }

    private val _predictInc: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
    val predictInc: LiveData<String> = _predictInc


    fun setPredictInc(predictInc: Double) {
        _predictInc.value = predictInc.toString()
    }

    private val _predictExp: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
    val predictExp: LiveData<String> = _predictExp

    fun setPredictExp(predictExp: Double) {
        _predictExp.value = predictExp.toString()
    }

    private val _category: MutableLiveData<Category> by lazy {
        MutableLiveData<Category>()
    }
    val category: LiveData<Category> = _category


    fun setCategory(category: Category) {
        _category.value = category
    }

    private val _predictSum: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
    val predictSum: LiveData<String> = _predictSum

    fun setPredictSum(predictSum: Double) {
        _predictSum.value = predictSum.toString()
    }

}