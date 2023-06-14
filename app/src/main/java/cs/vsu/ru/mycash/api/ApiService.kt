package cs.vsu.ru.mycash.api


import cs.vsu.ru.mycash.api.Constants.ACCOUNT
import cs.vsu.ru.mycash.api.Constants.ACCOUNTS
import cs.vsu.ru.mycash.api.Constants.ACCOUNT_DELETE
import cs.vsu.ru.mycash.api.Constants.ACCOUNT_UPDATE
import cs.vsu.ru.mycash.api.Constants.ANALYTICS
import cs.vsu.ru.mycash.api.Constants.CATEGORY_UPDATE
import cs.vsu.ru.mycash.api.Constants.CATEGORY_URL
import cs.vsu.ru.mycash.api.Constants.DATA_DAY
import cs.vsu.ru.mycash.api.Constants.DATA_MONTH
import cs.vsu.ru.mycash.api.Constants.DELETE_USER
import cs.vsu.ru.mycash.api.Constants.INIT_URL
import cs.vsu.ru.mycash.api.Constants.LOGIN
import cs.vsu.ru.mycash.api.Constants.OPERATION_ADD
import cs.vsu.ru.mycash.api.Constants.OPERATION_DELETE
import cs.vsu.ru.mycash.api.Constants.OPERATION_UPDATE
import cs.vsu.ru.mycash.api.Constants.PREDICT
import cs.vsu.ru.mycash.api.Constants.REGISTER
import cs.vsu.ru.mycash.data.*
import cs.vsu.ru.mycash.data.diagrams.AnalyticsResponse
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @POST(INIT_URL)
    fun init(@Body accountInit: AccountInit): Call<TokenResponse>

    @GET(DATA_DAY)
    fun getDataByDay(@Path("year") year: Int,
                     @Path("month") month: Int,
                     @Path("day") day: Int) :
            Call<List<MainScreenAccountResponse>>

    @GET(DATA_MONTH)
    fun getDataByMonth(@Path("year") year: Int,
                       @Path("month") month: Int) :
            Call<List<MainScreenAccountResponse>>

    @POST(REGISTER)
    fun register(@Body registerRequest: RegisterRequest) : Call<TokenResponse>

    @POST(LOGIN)
    fun signIn(@Body registerRequest: RegisterRequest) : Call<TokenResponse>

    @DELETE(DELETE_USER)
    fun delete() : Call<Void>

    @GET(CATEGORY_URL)
    fun getCategories() : Call<List<Category>>

    @GET(ACCOUNTS)
    fun getAccounts(): Call<List<Account>>
    @POST(OPERATION_ADD)
    fun addOperation(@Body operation: Operation) : Call<OperationResponse>

    @GET(PREDICT)
    fun getPredict(@Path("accountName") accountName: String,
                   @Path("year") year: Int, @Path("month") month: Int) : Call<PredictionResponse>

    @POST(ACCOUNT)
    fun addAccount(@Body account: Account) : Call<Void>

    @POST(ACCOUNT_UPDATE)
    fun updateAccount(@Path("accountName") accountName: String, @Body account: Account) : Call<Void>

    @POST(CATEGORY_UPDATE)
    fun updateCategory(@Body category: Category) : Call<Void>

    @POST(ACCOUNT_DELETE)
    fun deleteAccount(@Path("accountName") accountName: String) : Call<Void>

    @DELETE(OPERATION_DELETE)
    fun deleteOperation(@Path("id") id: Long?) : Call<Void>

    @POST(OPERATION_UPDATE)
    fun updateOperation() : Call<OperationResponse>

    @GET(ANALYTICS)
    fun getAnalytics(@Path("accountName") accountName: String,
                     @Path("year") year: String,
                     @Path("month") month: String) : Call<AnalyticsResponse>

}