package cs.vsu.ru.mycash.ui.onboarding

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import cs.vsu.ru.mycash.api.ApiClient
import cs.vsu.ru.mycash.api.ApiService
import cs.vsu.ru.mycash.data.AccountInit
import cs.vsu.ru.mycash.data.TokenResponse
import cs.vsu.ru.mycash.databinding.ActivityStartBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StartActivity : AppCompatActivity() {

    private lateinit var binding : ActivityStartBinding
//    private lateinit var authViewModel: AuthViewModel
//    private lateinit var tokenViewModel: TokenViewModel
//    private lateinit var tokenManager: TokenManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStartBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val balance = binding.editTextBalance
        val accountName = binding.editTextAccountName
        binding.btnContinue.setOnClickListener {
            var valid = true
            if (balance.text.trim().isEmpty())
            {
                balance.error = "Введите остаток"
                valid = false
            }
            if (accountName.text.trim().isEmpty())
            {
                accountName.error = "Введите название"
                valid = false
            }
            if (valid)
            {

                val apiService = ApiClient.initClient().create(ApiService::class.java)
                val call = apiService.test()

                call.enqueue(object : Callback<String> {
                    override fun onResponse(call: Call<String>, response: Response<String>) {

                        Toast.makeText(applicationContext, response.body().toString(), Toast.LENGTH_LONG).show()

                    }

                    override fun onFailure(call: Call<String>, t: Throwable) {
                        Toast.makeText(applicationContext, t.message, Toast.LENGTH_SHORT).show()
                    }

                })

//                val call = apiService.init(AccountInit(
//                    accountName.text.toString(),
//                    balance.text.toString()))
//
//                call.enqueue(object : Callback<TokenResponse> {
//                    override fun onResponse(call: Call<TokenResponse>, response: Response<TokenResponse>) {
//                        val preferences: SharedPreferences = getSharedPreferences("MY_APP", Context.MODE_PRIVATE)
//                        val token = response.body()?.token
//                        preferences.edit().putString("TOKEN", token).apply()
//                        Toast.makeText(applicationContext, token, Toast.LENGTH_SHORT).show()
//
//                    }
//
//                    override fun onFailure(call: Call<TokenResponse>, t: Throwable) {
//                        Toast.makeText(applicationContext, t.message, Toast.LENGTH_SHORT).show()
//                    }
//
//                })


//                authViewModel.tokenResponse.observe(this) {
//                    when(it) {
//                        is ApiResponse.Failure ->
//                        {
//                            Toast.makeText(applicationContext, it.errorMessage, Toast.LENGTH_SHORT).show()
//                        }
//                        is ApiResponse.Loading -> Toast.makeText(applicationContext, "Loading", Toast.LENGTH_SHORT).show()
//                        is ApiResponse.Success -> {
//                            tokenViewModel.saveToken(it.data.token)
//                            Toast.makeText(applicationContext, "Success", Toast.LENGTH_SHORT).show()
//                        }
//
//                    }
//                }
//
//                authViewModel.init(
//                    AccountInit(balance.text.toString(), accountName.text.toString()),
//                    object: CoroutinesErrorHandler {
//                        override fun onError(message: String) {
//                            Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
//                        }
//                    }
//                )
            }

        }

    }
}