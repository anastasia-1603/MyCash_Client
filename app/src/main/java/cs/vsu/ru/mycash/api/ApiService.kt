package cs.vsu.ru.mycash.api

import android.service.autofill.UserData
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface ApiService {

    @POST(Constants.LOGIN_URL)
    fun saveUser() : Call<String>
}