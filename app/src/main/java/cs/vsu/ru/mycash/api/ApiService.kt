package cs.vsu.ru.mycash.api


import android.service.autofill.UserData
import cs.vsu.ru.mycash.api.Constants.INIT_URL
import cs.vsu.ru.mycash.api.Constants.TEST
import cs.vsu.ru.mycash.data.*
import retrofit2.Call
import retrofit2.http.*
import java.time.LocalDateTime

interface ApiService {

//    @POST(Constants.LOGIN_URL)
//    fun saveUser() : Call<String>

    @POST(INIT_URL)
    fun init(@Body accountInit: AccountInit): Call<TokenResponse>

    @GET(TEST)
    fun test(): Call<String>

    @GET("main/get/{account}/{year}/{month}/{day}")
    fun getAccountInfo(@Path("account") account: String,
                       @Path("year") year: Int,
                       @Path("month") month: Int,
                       @Path("day") day: Int) :
            Call<List<Operation>>

    @POST("/register")
    fun register(@Body registerRequest: RegisterRequest) : Call<TokenResponse>

    @POST("/login")
    fun signIn(@Body registerRequest: RegisterRequest) : Call<TokenResponse>

}