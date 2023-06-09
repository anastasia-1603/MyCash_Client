package cs.vsu.ru.mycash.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import cs.vsu.ru.mycash.R
import cs.vsu.ru.mycash.api.ApiClient
import cs.vsu.ru.mycash.data.RegisterRequest
import cs.vsu.ru.mycash.data.TokenResponse
import cs.vsu.ru.mycash.databinding.FragmentSignInBinding
import cs.vsu.ru.mycash.utils.AppPreferences
import org.json.JSONObject
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
                        if (response.body() != null) {
                            val tokenResponse = response.body()?.token.toString()
                            appPrefs.token = tokenResponse
                            ApiClient.updateClient(tokenResponse)
                            appPrefs.isAuth = true
                            appPrefs.username = username.text.toString()
                            binding.loading.visibility = View.GONE
                            findNavController().navigate(R.id.profileFragment)
                        }
                        else {
                            if (response.code() == 401)
                            {
                                Toast.makeText(context, "Неправильный логин или пароль", Toast.LENGTH_SHORT).show()
                            }
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