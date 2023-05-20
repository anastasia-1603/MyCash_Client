package cs.vsu.ru.mycash.ui.onboarding

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import cs.vsu.ru.mycash.ui.auth.AuthViewModel
import cs.vsu.ru.mycash.ui.auth.TokenViewModel

import cs.vsu.ru.mycash.databinding.ActivityStartBinding
import androidx.activity.viewModels
import cs.vsu.ru.mycash.api.ApiResponse
import cs.vsu.ru.mycash.data.AccountInit
import cs.vsu.ru.mycash.ui.CoroutinesErrorHandler

class StartActivity : AppCompatActivity() {

    private lateinit var binding : ActivityStartBinding
    private val authViewModel: AuthViewModel by viewModels()
    private val tokenViewModel: TokenViewModel by viewModels()


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


                authViewModel.tokenResponse.observe(this) {
                    when(it) {
                        is ApiResponse.Failure ->
                        {
                            Toast.makeText(applicationContext, it.errorMessage, Toast.LENGTH_SHORT).show()
                        }
                        is ApiResponse.Loading -> Toast.makeText(applicationContext, "Loading", Toast.LENGTH_SHORT).show()
                        is ApiResponse.Success -> {
                            tokenViewModel.saveToken(it.data.token)
                            Toast.makeText(applicationContext, "Success", Toast.LENGTH_SHORT).show()
                        }

                    }
                }

                authViewModel.init(
                    AccountInit(balance.text.toString(), accountName.text.toString()),
                    object: CoroutinesErrorHandler {
                        override fun onError(message: String) {
                            Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
                        }
                    }
                )
            }

        }

    }

}