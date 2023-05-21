package cs.vsu.ru.mycash.api

//import cs.vsu.ru.mycash.data.AccountInit
//import cs.vsu.ru.mycash.data.TokenResponse
//import retrofit2.Response
//import retrofit2.http.Body
//import retrofit2.http.GET
//import retrofit2.http.Header
//import retrofit2.http.POST
//
//interface TokenApiService {
//
//    @GET(Constants.TOKEN_URL)
//    suspend fun refreshToken(
//        @Header("Authorization") token: String,
//    ): Response<TokenResponse>
//
//    @POST(Constants.INIT_URL)
//    suspend fun init(
//        @Body accountInit: AccountInit,
//    ): Response<TokenResponse>
//}