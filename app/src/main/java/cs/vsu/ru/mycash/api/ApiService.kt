package cs.vsu.ru.mycash.api


import cs.vsu.ru.mycash.api.Constants.ACCOUNT_URL
import cs.vsu.ru.mycash.api.Constants.CATEGORY_URL
import cs.vsu.ru.mycash.api.Constants.INFO
import cs.vsu.ru.mycash.api.Constants.DATA_DAY
import cs.vsu.ru.mycash.api.Constants.DATA_MONTH
import cs.vsu.ru.mycash.api.Constants.INIT_URL
import cs.vsu.ru.mycash.api.Constants.LOGIN
import cs.vsu.ru.mycash.api.Constants.OPERATION_ADD
import cs.vsu.ru.mycash.api.Constants.PREDICT
import cs.vsu.ru.mycash.api.Constants.REGISTER
import cs.vsu.ru.mycash.data.*
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @POST(INIT_URL)
    fun init(@Body accountInit: AccountInit): Call<TokenResponse>

    @GET(DATA_DAY)
    fun getDataByDay(@Path("year") year: Int,
                     @Path("month") month: Int,
                     @Path("day") day: Int) :
            Call<Map<String, List<Operation>>>

    @GET(DATA_MONTH)
    fun getDataByMonth(@Path("year") year: Int,
                       @Path("month") month: Int) :
            Call<Map<String, List<Operation>>>

    @POST(REGISTER)
    fun register(@Body registerRequest: RegisterRequest) : Call<TokenResponse>

    @POST(LOGIN)
    fun signIn(@Body registerRequest: RegisterRequest) : Call<TokenResponse>

    @GET(CATEGORY_URL)
    fun getCategories() : Call<List<Category>>

    @GET(ACCOUNT_URL)
    fun getAccounts(): Call<List<Account>>
    @POST(OPERATION_ADD)
    fun addOperation(@Body operation: Operation) : Call<OperationResponse>

    @GET(PREDICT)
    fun getPredict(@Path("accountName") accountName: String,
                   @Path("year") year: Int, @Path("month") month: Int) : Call<PredictionResponse>
}