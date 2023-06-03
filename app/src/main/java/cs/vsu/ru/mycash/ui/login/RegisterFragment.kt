package cs.vsu.ru.mycash.ui.login

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import cs.vsu.ru.mycash.R
import cs.vsu.ru.mycash.api.ApiClient
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

        appPrefs = activity?.let { AppPreferences(it) }!!

        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        binding.continueBtn.setOnClickListener {
            register()
        }
        return binding.root
    }
    private fun register() {

        val apiService = ApiClient.getClient(appPrefs.token.toString())
        if (checkValid()) {
            apiService.register(RegisterRequest(
                binding.username.text.toString(),
                binding.password.text.toString())
            ).enqueue(object : Callback<TokenResponse> {
                override fun onResponse(
                    call: Call<TokenResponse>,
                    response: Response<TokenResponse>
                ) {
                    val tokenResponse = response.body()?.token.toString()
                    appPrefs.token = tokenResponse
                    ApiClient.updateClient(tokenResponse)
                    val navController = findNavController()
                    val alertDialogBuilder = AlertDialog.Builder(requireContext())
                    alertDialogBuilder.setMessage("Вы успешно зарегистрированы")

                    alertDialogBuilder.setPositiveButton("OK") { dialog, _ ->
                        appPrefs.isAuth = true
                        dialog.dismiss()
                        navController.navigate(R.id.profileFragment)
                    }

                    val alertDialog = alertDialogBuilder.create()
                    alertDialog.show()

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