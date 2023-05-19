package cs.vsu.ru.mycash.data

import com.google.gson.annotations.SerializedName

data class TokenResponse(
    @SerializedName("access_token")
    val token: String
)
