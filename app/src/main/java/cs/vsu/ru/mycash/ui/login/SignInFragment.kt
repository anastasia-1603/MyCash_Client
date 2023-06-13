package cs.vsu.ru.mycash.ui.login

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import cs.vsu.ru.mycash.R
import cs.vsu.ru.mycash.api.ApiClient
import cs.vsu.ru.mycash.api.ApiService
import cs.vsu.ru.mycash.data.ApiError
import cs.vsu.ru.mycash.data.RegisterRequest
import cs.vsu.ru.mycash.data.TokenResponse
import cs.vsu.ru.mycash.databinding.FragmentSignInBinding
import cs.vsu.ru.mycash.utils.AppPreferences
import cs.vsu.ru.mycash.utils.ErrorUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignInFragment : Fragment() {
    private var _binding: FragmentSignInBinding? = null
    private lateinit var appPrefs: AppPreferences
    private val binding get() = _binding!!
    private val loginViewModel: LoginViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        appPrefs = activity?.let { AppPreferences(it) }!!
        _binding = FragmentSignInBinding.inflate(inflater, container, false)

        val username = binding.username
        val password = binding.password

        binding.continueBtn.setOnClickListener {
            binding.loading.visibility = View.VISIBLE
            var valid = true
            if (username.text.trim().isEmpty()) {
                username.error = "Введите логин"
                valid = false
            }
            if (password.text.trim().isEmpty()) {
                password.error = "Введите пароль"
                valid = false
            }

            if (valid) {
                val apiService = ApiClient.getClient(appPrefs.token.toString())
                val call = apiService.signIn(
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
                        if (response.isSuccessful) {
                            val tokenResponse = response.body()?.token.toString()
                            appPrefs.token = tokenResponse
                            ApiClient.updateClient(tokenResponse)
                            appPrefs.isAuth = true
                            appPrefs.username = username.text.toString()
                            binding.loading.visibility = View.GONE
                            findNavController().navigate(R.id.profileFragment)
                        }
                        else {
                            val error : ApiError = ErrorUtils.parseError(response)
                            Log.e("login error message", error.message)
                        }

                    }

                    override fun onFailure(call: Call<TokenResponse>, t: Throwable) {
                        Toast.makeText(context, t.message, Toast.LENGTH_SHORT).show()
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