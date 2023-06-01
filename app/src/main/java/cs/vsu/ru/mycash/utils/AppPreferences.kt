package cs.vsu.ru.mycash.utils

import android.content.SharedPreferences
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import cs.vsu.ru.mycash.ui.login.SignInFragment
import cs.vsu.ru.mycash.ui.main.MainScreenActivity


class AppPreferences(context: FragmentActivity) {
    private val PREFS_NAME = "my_app_prefs"
    private val IS_FIRST_TIME_LAUNCH = "is_first_time_launch"
    private val IS_AUTH = "is_auth"
    private val TOKEN = "TOKEN"

    private val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, 0)

    var isFirstTimeLaunch: Boolean
        get() = prefs.getBoolean(IS_FIRST_TIME_LAUNCH, true)
        set(value) = prefs.edit().putBoolean(IS_FIRST_TIME_LAUNCH, value).apply()

    var isAuth: Boolean
        get() = prefs.getBoolean(IS_AUTH, false)
        set(value) = prefs.edit().putBoolean(IS_AUTH, value).apply()

    var token: String?
        get() = prefs.getString(TOKEN, "token")
        set(value) = prefs.edit().putString(TOKEN, value).apply()


}