package cs.vsu.ru.mycash.ui.login

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import cs.vsu.ru.mycash.api.ApiClient
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val viewModel = ViewModelProvider(this)[LoginViewModel::class.java]

        appPrefs = activity?.let { AppPreferences(it) }!!
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)

        val username = binding.username
        val password = binding.password

        binding.continueBtn.setOnClickListener {
            var valid = true
            if (username.text.trim().isEmpty()) {
                username.error = "Введите логин"
                valid = false
            }
            if (password.text.trim().isEmpty()) {
                password.error = "Введите пароль"
            }

            if (valid) {
                val apiService = ApiClient.initClient().create(ApiService::class.java)
                val call = apiService.register(
                    RegisterRequest(
                        username.text.toString(),
                        password.text.toString()
                    )
                )

                call.enqueue(object : Callback<TokenResponse> {
                    override fun onResponse(
                        call: Call<TokenResponse>,
                        response: Response<TokenResponse>
                    ) {
                        val preferences =
                            activity?.getSharedPreferences("MY_APP", Context.MODE_PRIVATE)
                        val token = response.body()?.token ?: "-1"
                        preferences?.edit()?.putString("TOKEN", token)?.apply()
                        appPrefs.isAuth = true
                    }

                    override fun onFailure(call: Call<TokenResponse>, t: Throwable) {
                        t.message?.let { Log.e("failure register", it) }
                    }

                })
            }
        }



        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}