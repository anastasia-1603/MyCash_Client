package cs.vsu.ru.mycash.ui.onboarding

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import cs.vsu.ru.mycash.api.ApiClient
import cs.vsu.ru.mycash.api.ApiService
import cs.vsu.ru.mycash.databinding.ActivityWelcomeBinding
import retrofit2.Call
import retrofit2.Callback;
import retrofit2.Response
import java.util.*

class WelcomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWelcomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.button.setOnClickListener {
            val token = UUID.randomUUID().toString()
            val apiService = ApiClient.getClient(token).create(ApiService::class.java)
            val call = apiService.saveUser()

            call.enqueue(object : Callback<String> {
                override fun onResponse(call: Call<String>, response: Response<String>) {
                }

                override fun onFailure(call: Call<String>, t: Throwable) {
                    // Обработка ошибки при отправке запроса
                }
            })

            val intent = Intent(this@WelcomeActivity, StartActivity::class.java)
            startActivity(intent)
        }
    }
}


