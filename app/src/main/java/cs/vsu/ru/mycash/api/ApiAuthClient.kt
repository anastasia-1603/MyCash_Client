package cs.vsu.ru.mycash.api

import android.content.Context
import android.util.Log
import cs.vsu.ru.mycash.utils.AppPreferences
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiAuthClient {
    private var retrofit: Retrofit? = null
    private lateinit var appPrefs: AppPreferences

    fun getClient(token: String): Retrofit {
        val client = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .addHeader("Authorization", "Bearer $token")
                    .addHeader("ngrok-skip-browser-warning", true.toString())
                    .addHeader("User-Agent", "MyCash")
                    .build()
                chain.proceed(request)
            }
            .cache(null)
            .build()

//    fun getClient(context: Context): Retrofit {
//        appPrefs = AppPreferences(context)
//        val token = appPrefs.token
//        val client = OkHttpClient.Builder()
//            .addInterceptor { chain ->
//                val request = chain.request().newBuilder()
//                    .addHeader("Authorization", "Bearer $token")
//                    .addHeader("ngrok-skip-browser-warning", true.toString())
//                    .addHeader("User-Agent", "MyCash")
//                    .build()
//                chain.proceed(request)
//            }
//            .cache(null)
//            .build()


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