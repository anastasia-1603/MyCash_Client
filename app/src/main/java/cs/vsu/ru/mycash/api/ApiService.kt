package cs.vsu.ru.mycash.api


import android.service.autofill.UserData
import cs.vsu.ru.mycash.api.Constants.ACCOUNT_URL
import cs.vsu.ru.mycash.api.Constants.CATEGORY_URL
import cs.vsu.ru.mycash.api.Constants.INFO
import cs.vsu.ru.mycash.api.Constants.INIT_URL
import cs.vsu.ru.mycash.api.Constants.LOGIN
import cs.vsu.ru.mycash.api.Constants.REGISTER
import cs.vsu.ru.mycash.data.*
import retrofit2.Call
import retrofit2.http.*
import java.time.LocalDateTime

interface ApiService {
    @POST(INIT_URL)
    fun init(@Body accountInit: AccountInit): Call<TokenResponse>

    @GET(INFO)
    fun getAccountInfo(@Path("account") account: String,
                       @Path("year") year: Int,
                       @Path("month") month: Int,
                       @Path("day") day: Int) :
            Call<List<Operation>>

    @POST(REGISTER)
    fun register(@Body registerRequest: RegisterRequest) : Call<TokenResponse>

    @POST(LOGIN)
    fun signIn(@Body registerRequest: RegisterRequest) : Call<TokenResponse>

    @GET(CATEGORY_URL)
    fun getCategories() : Call<List<Category>>

    @GET(ACCOUNT_URL)
    fun getAccounts(): Call<List<Account>>
}