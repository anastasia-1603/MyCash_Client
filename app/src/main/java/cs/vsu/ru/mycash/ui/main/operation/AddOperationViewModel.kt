package cs.vsu.ru.mycash.ui.main.operation

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import cs.vsu.ru.mycash.data.Account
import cs.vsu.ru.mycash.data.Category
import java.util.*

class AddOperationViewModel : ViewModel() {
    private val _accountName: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
    val accountName: LiveData<String> = _accountName

    private val _value: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
    val value: LiveData<String> = _value

    private val _accounts : MutableLiveData<List<Account>> by lazy {
        MutableLiveData<List<Account>>()
    }
    val accounts : LiveData<List<Account>> = _accounts

    fun setAccounts(accounts: List<Account>) {
        _accounts.value = accounts
    }

    private val _categoryName : MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
    val categoryName : LiveData<String> = _categoryName

    fun setCategoryName(categoryName: String) {
        _categoryName.value = categoryName
    }

    private val _categories : MutableLiveData<List<Category>> by lazy {
        MutableLiveData<List<Category>>()
    }
    val categories : LiveData<List<Category>> = _categories

    fun setCategories(categories: List<Category>) {
        _categories.value = categories
    }


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

    fun setValue(value: String) {
        _value.value = value
    }

    fun setMode(mode: Mode) {
        _mode.value = mode
    }

    private val _imageUri: MutableLiveData<Uri?> by lazy {
        MutableLiveData<Uri?>(null)
    }
    val imageUri: LiveData<Uri?> = _imageUri

    fun setImageUri(imageUri: Uri?) {
        _imageUri.value = imageUri
    }

    enum class Mode {
        INCOME,
        EXPENSES
    }
}