package cs.vsu.ru.mycash.api

import cs.vsu.ru.mycash.api.Constants.BASE_URL
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    private var retrofit: Retrofit? = null

    private lateinit var client: OkHttpClient

    fun getClient(token: String): ApiService {
        if (retrofit == null) {
            updateClient(token)
        }
        return retrofit!!.create(ApiService::class.java)
    }

    fun updateClient(token: String) {
        client = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .addHeader("ngrok-skip-browser-warning", true.toString())
                    .addHeader("User-Agent", "MyCash")
                    .addHeader("Authorization", "Bearer $token")
                    .build()
                chain.proceed(request)
            }
            .build()
        retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }


}
//object ApiClient {
//
//    private var retrofit: Retrofit? = null
//
//    fun initClient(): Retrofit {
//        val client = OkHttpClient.Builder()
//            .addInterceptor { chain ->
//                val request = chain.request().newBuilder()
//                    .addHeader("ngrok-skip-browser-warning", true.toString())
//                    .addHeader("User-Agent", "MyCash")
//                    .build()
//                chain.proceed(request)
//            }
//            .build()
//
//        if (retrofit == null) {
//            retrofit = Retrofit.Builder()
//                .baseUrl(BASE_URL)
//                .addConverterFactory(GsonConverterFactory.create())
//                .client(client)
//                .build()
//        }
//
//        return retrofit!!
//    }
//}