package cs.vsu.ru.mycash.ui.onboarding

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import cs.vsu.ru.mycash.ui.auth.AuthViewModel
import cs.vsu.ru.mycash.ui.auth.TokenViewModel

import dagger.hilt.android.AndroidEntryPoint
import cs.vsu.ru.mycash.api.TokenApiService
import cs.vsu.ru.mycash.databinding.ActivityStartBinding
import cs.vsu.ru.mycash.ui.main.MainScreenActivity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.activity.viewModels
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StartActivity : AppCompatActivity() {

    private lateinit var binding : ActivityStartBinding
    private val viewModel: AuthViewModel by viewModels()
    private val tokenViewModel: TokenViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStartBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.btnContinue.setOnClickListener {


//            val tokenApiService = ApiClient.getClient(token).create(TokenApiService::class.java)
//            val call = tokenApiService.getToken()
//
//            call.enqueue(object : Callback<String> {
//                override fun onResponse(call: Call<String>, response: Response<String>) {
//                }
//
//                override fun onFailure(call: Call<String>, t: Throwable) {
//                    // Обработка ошибки при отправке запроса
//                }
//            })

            val intent = Intent(this@StartActivity, MainScreenActivity::class.java)
            intent.putExtra("accountName", binding.editTextAccountName.text.toString())
            intent.putExtra("balance", binding.editTextBalance.text.toString())
            startActivity(intent)
        }

    }
}