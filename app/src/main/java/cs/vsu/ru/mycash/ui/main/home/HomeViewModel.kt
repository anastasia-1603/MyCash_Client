package cs.vsu.ru.mycash.ui.main.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import cs.vsu.ru.mycash.data.CategoryType
import cs.vsu.ru.mycash.data.Operation
import java.util.Calendar

class HomeViewModel : ViewModel() {


    private val _accountName: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
    val accountName: LiveData<String> = _accountName

    private val _balance: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
    val balance: LiveData<String> = _balance

    private val _operationList: MutableLiveData<List<Operation>> by lazy {
        MutableLiveData<List<Operation>>()
    }
    val operationList: LiveData<List<Operation>> = _operationList


    private val _filteredOperationsList: MutableLiveData<List<Operation>> by lazy {
        MutableLiveData<List<Operation>>()
    }
    val filteredOperationsList: LiveData<List<Operation>> = _filteredOperationsList

    fun filterOperationsByCategory(categoryType: CategoryType) {
        val filteredList = operationList.value?.filter { it.category.type == categoryType }
        _filteredOperationsList.value = filteredList
    }

    private val _date: MutableLiveData<Calendar> by lazy {
        MutableLiveData<Calendar>()
    }
    val date: LiveData<Calendar> = _date

    fun setDate(date: Calendar) {
        _date.value = date
    }

    fun setAccountName(accountName: String) {
        _accountName.value = accountName
    }

    fun setBalance(balance: String) {
        _balance.value = balance + "₽"
    }

    fun setOperationList(operationList: List<Operation>) {
        _operationList.value = operationList
    }
}