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

//    private val _operationList: MutableLiveData<List<Operation>> by lazy {
//        MutableLiveData<List<Operation>>()
//    }
//    val operationList: LiveData<List<Operation>> = _operationList
//
//    private val _incomeOperations: MutableLiveData<List<Operation>> by lazy {
//        MutableLiveData<List<Operation>>()
//    }
//    val incomeOperations: LiveData<List<Operation>> = _incomeOperations
//
//    private val _expenseOperations: MutableLiveData<List<Operation>> by lazy {
//        MutableLiveData<List<Operation>>()
//    }
//    val expenseOperations: LiveData<List<Operation>> = _expenseOperations

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

//    fun setOperationList(operations: List<Operation>) {
//        _operationList.value = operations
//    }
//
//    fun setIncomeList(operations: List<Operation>) {
//        _incomeOperations.value = operations
//    }
//
//    fun setExpenseList(operations: List<Operation>) {
//        _expenseOperations.value = operations
//    }

//
//    init {
//        _operationList.observeForever { operations ->
//            val incomeList = operations.filter { it.category.type == CategoryType.INCOME }
//            _incomeOperations.value = incomeList
//            val expenseList = operations.filter { it.category.type == CategoryType.EXPENSE }
//            _expenseOperations.value = expenseList
//        }
//    }

}