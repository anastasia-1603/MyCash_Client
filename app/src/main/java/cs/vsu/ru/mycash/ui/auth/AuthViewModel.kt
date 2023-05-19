package cs.vsu.ru.mycash.ui.auth

import androidx.lifecycle.MutableLiveData
import cs.vsu.ru.mycash.api.ApiResponse
import cs.vsu.ru.mycash.data.AccountInit
import cs.vsu.ru.mycash.data.TokenResponse
import cs.vsu.ru.mycash.repository.AuthRepository
import cs.vsu.ru.mycash.ui.BaseViewModel
import cs.vsu.ru.mycash.ui.CoroutinesErrorHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
): BaseViewModel() {

    private val _tokenResponse = MutableLiveData<ApiResponse<TokenResponse>>()
    val tokenResponse = _tokenResponse

    fun init(accountInit: AccountInit, coroutinesErrorHandler: CoroutinesErrorHandler) = baseRequest(
        _tokenResponse,
        coroutinesErrorHandler
    ) {
        authRepository.init(accountInit)
    }
}