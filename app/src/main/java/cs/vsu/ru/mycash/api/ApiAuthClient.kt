package cs.vsu.ru.mycash.api

import android.util.Log
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiAuthClient {
    private var retrofit: Retrofit? = null

    fun getClient(token: String): Retrofit {
        val client = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .addHeader("Authorization", "Bearer $token")
                    .addHeader("ngrok-skip-browser-warning", true.toString())
                    .addHeader("User-Agent", "MyCash")
                    .build()
                Log.e("t", request.header("Authorization").toString())
                chain.proceed(request)
            }
            .build()


        if (retrofit == null) {
            retrofit = Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
        }

        return retrofit!!
    }

}