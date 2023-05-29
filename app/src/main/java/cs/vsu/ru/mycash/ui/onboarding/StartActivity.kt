package cs.vsu.ru.mycash.ui.onboarding

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import cs.vsu.ru.mycash.api.ApiClient
import cs.vsu.ru.mycash.api.ApiService
import cs.vsu.ru.mycash.data.AccountInit
import cs.vsu.ru.mycash.data.TokenResponse
import cs.vsu.ru.mycash.databinding.ActivityStartBinding
import cs.vsu.ru.mycash.ui.main.MainScreenActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StartActivity : AppCompatActivity() {

    private lateinit var binding : ActivityStartBinding
    private val viewModel: AccountInitViewModel by viewModels()

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
                val call = apiService.init(AccountInit(
                    accountName.text.toString(),
                    balance.text.toString().toDouble()))

                call.enqueue(object : Callback<TokenResponse> {
                    override fun onResponse(call: Call<TokenResponse>, response: Response<TokenResponse>) {
                        val preferences: SharedPreferences = getSharedPreferences("MY_APP", Context.MODE_PRIVATE)
                        val token = response.body()?.token ?: "-1"

                        preferences.edit().putString("TOKEN", token).apply()
                        Log.e("token1", token)
//                        Toast.makeText(applicationContext, token.toString(), Toast.LENGTH_SHORT).show()
//                        binding.editTextAccountName.setText(token)

                        val intent = Intent(this@StartActivity, MainScreenActivity::class.java)
                        startActivity(intent)
                    }

                    override fun onFailure(call: Call<TokenResponse>, t: Throwable) {
                        Toast.makeText(applicationContext, t.message, Toast.LENGTH_SHORT).show()
                    }

                })


            }

        }

    }
}