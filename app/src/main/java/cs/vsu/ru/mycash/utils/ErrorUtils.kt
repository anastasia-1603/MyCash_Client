package cs.vsu.ru.mycash.utils

import cs.vsu.ru.mycash.api.ApiClient
import cs.vsu.ru.mycash.data.ApiError
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Response
import java.io.IOException


object ErrorUtils {
    fun parseError(response: Response<*>): ApiError {
        val converter: Converter<ResponseBody, ApiError> = ApiClient.retrofit!!
            .responseBodyConverter(ApiError::class.java, arrayOfNulls<Annotation>(0))
        return try {
            converter.convert(response.errorBody()!!)
        } catch (e: IOException) {
            ApiError(0, "Unknown error")
        }
    }
}