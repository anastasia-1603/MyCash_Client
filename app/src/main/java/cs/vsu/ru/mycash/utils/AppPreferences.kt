package cs.vsu.ru.mycash.utils

import android.content.Context
import android.content.SharedPreferences
import retrofit2.http.GET

class AppPreferences(context: Context) {

    private val PREFS_NAME = "my_app_prefs"
    private val IS_FIRST_TIME_LAUNCH = "is_first_time_launch"
    private val IS_AUTH = "is_auth"
    private val TOKEN = "TOKEN"
    private val USERNAME = "USERNAME"
    private val GET_NOTIFICATIONS = "get_notifications"

    val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    var isFirstTimeLaunch: Boolean
        get() = prefs.getBoolean(IS_FIRST_TIME_LAUNCH, true)
        set(value) = prefs.edit().putBoolean(IS_FIRST_TIME_LAUNCH, value).apply()

    var isAuth: Boolean
        get() = prefs.getBoolean(IS_AUTH, false)
        set(value) = prefs.edit().putBoolean(IS_AUTH, value).apply()

    var token: String?
        get() = prefs.getString(TOKEN, "token")
        set(value) = prefs.edit().putString(TOKEN, value).apply()

    var username: String?
        get() = prefs.getString(USERNAME, "username_default")
        set(value) = prefs.edit().putString(USERNAME, value).apply()

    var getNotifications: Boolean
        get() = prefs.getBoolean(GET_NOTIFICATIONS, true)
        set(value) = prefs.edit().putBoolean(GET_NOTIFICATIONS, value).apply()


}