package cs.vsu.ru.mycash.ui.main.operation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.*

class AddOperationViewModel : ViewModel() {
    private val _accountName: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
    val accountName: LiveData<String> = _accountName

    private val _sum: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
    val sum: LiveData<String> = _sum

    private val _date: MutableLiveData<Calendar> by lazy {
        MutableLiveData<Calendar>(Calendar.getInstance())
    }
    val date: LiveData<Calendar> = _date

    private val _mode: MutableLiveData<Mode> by lazy {
        MutableLiveData<Mode>(Mode.INCOME)
    }
    val mode: LiveData<Mode> = _mode

    private val _comment: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
    val comment: LiveData<String> = _comment

    fun setComment(comment: String) {
        _comment.value = comment
    }
    fun setDate(date: Calendar) {
        _date.value = date
    }

    fun setAccountName(accountName: String) {
        _accountName.value = accountName
    }

    fun setSum(sum: String) {
        _sum.value = sum + "₽"
    }

    fun setMode(mode: Mode) {
        _mode.value = mode
    }

    enum class Mode {
        INCOME,
        EXPENSES
    }
}