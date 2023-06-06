package cs.vsu.ru.mycash.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LoginViewModel() : ViewModel() {

    private val _username: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
    val username: LiveData<String> = _username

    private val _password: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    val password: LiveData<String> = _password
}