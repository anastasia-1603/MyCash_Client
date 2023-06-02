package cs.vsu.ru.mycash.ui.login

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import cs.vsu.ru.mycash.api.ApiAuthClient
import cs.vsu.ru.mycash.api.ApiService
import cs.vsu.ru.mycash.data.RegisterRequest
import cs.vsu.ru.mycash.data.TokenResponse
import cs.vsu.ru.mycash.databinding.FragmentRegisterBinding
import cs.vsu.ru.mycash.utils.AppPreferences
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterFragment : Fragment() {
    private var _binding: FragmentRegisterBinding? = null
    private lateinit var appPrefs: AppPreferences
    private val binding get() = _binding!!
    private lateinit var apiService: ApiService

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        binding.continueBtn.setOnClickListener {
            register()
        }
        return binding.root
    }

    private fun register() {
        appPrefs = AppPreferences(requireActivity())
        val token = appPrefs.token.toString()
        apiService = ApiAuthClient.getClient(token).create(ApiService::class.java)
        Log.e("token register", token)
        Log.e("token register prefs", appPrefs.token.toString())

        if (checkValid()) {
            val apiService = token.let { it1 ->
                ApiAuthClient.getClient(it1).create(ApiService::class.java)
            }

            apiService?.register(
                RegisterRequest(
                    binding.username.text.toString(),
                    binding.password.text.toString()
                )
            )?.enqueue(object : Callback<TokenResponse> {
                override fun onResponse(
                    call: Call<TokenResponse>,
                    response: Response<TokenResponse>
                ) {
                    Log.e("response", response.body().toString())
                    val tokenResponse = response.body()?.token ?: "-1"
                    Log.e("token response", tokenResponse)
                    appPrefs.token = tokenResponse
                    Log.e("token response prefs", appPrefs.token.toString())

                }

                override fun onFailure(call: Call<TokenResponse>, t: Throwable) {
                    t.message?.let { Log.e("failure register", it) }
                }

            })
        }

    }

    private fun checkValid(): Boolean {
        val username = binding.username
        val password = binding.password
        var valid = true
        if (username.text.trim().isEmpty()) {
            username.error = "Введите логин"
            valid = false
        }
        if (password.text.trim().isEmpty()) {
            password.error = "Введите пароль"
        }
        return valid
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}