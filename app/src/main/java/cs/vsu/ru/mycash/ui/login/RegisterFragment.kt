package cs.vsu.ru.mycash.ui.login

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import cs.vsu.ru.mycash.R
import cs.vsu.ru.mycash.api.ApiClient
import cs.vsu.ru.mycash.data.ApiError
import cs.vsu.ru.mycash.data.RegisterRequest
import cs.vsu.ru.mycash.data.TokenResponse
import cs.vsu.ru.mycash.databinding.FragmentRegisterBinding
import cs.vsu.ru.mycash.utils.AppPreferences
import cs.vsu.ru.mycash.utils.ErrorUtils
import org.json.JSONObject
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
            val username = binding.username.text.toString()
            val password = binding.password.text.toString()
            binding.loading.visibility = View.VISIBLE
            apiService.register(RegisterRequest(
                username,
                password)
            ).enqueue(object : Callback<TokenResponse> {
                override fun onResponse(
                    call: Call<TokenResponse>,
                    response: Response<TokenResponse>
                ) {
                    if (response.body() != null)
                    {
                        if (response.isSuccessful) {
                            val alertDialogBuilder = AlertDialog.Builder(requireContext())
                            alertDialogBuilder.setMessage("Вы успешно зарегистрированы!")
                            val alertDialog = alertDialogBuilder.create()
                            alertDialog.show()
                            val tokenResponse = response.body()?.token.toString()

                            appPrefs.token = tokenResponse
                            ApiClient.updateClient(tokenResponse)
                            appPrefs.isAuth = true
                            appPrefs.username = username
                            findNavController().apply {
                                popBackStack()
                                popBackStack()
                            }

                            alertDialog.dismiss()

                        }
                    }
                    else {
                        if (response.code() == 409) {
                            Toast.makeText(context, "Пользователь с таким логином уже существует", Toast.LENGTH_LONG).show()
                        }

                    }
                    binding.loading.visibility = View.GONE
                }

                override fun onFailure(call: Call<TokenResponse>, t: Throwable) {
                    Toast.makeText(context, t.message, Toast.LENGTH_SHORT).show()
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