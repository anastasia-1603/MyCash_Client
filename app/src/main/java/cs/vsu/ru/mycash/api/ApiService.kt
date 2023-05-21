package cs.vsu.ru.mycash.api


import android.service.autofill.UserData
import cs.vsu.ru.mycash.api.Constants.INIT_URL
import cs.vsu.ru.mycash.api.Constants.TEST
import cs.vsu.ru.mycash.data.AccountInit
import cs.vsu.ru.mycash.data.TokenResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST

interface ApiService {

//    @POST(Constants.LOGIN_URL)
//    fun saveUser() : Call<String>

    @POST(INIT_URL)
    fun init(@Body accountInit: AccountInit): Call<TokenResponse>

    @GET(TEST)
    fun test(): Call<String>
}