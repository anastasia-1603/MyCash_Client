package cs.vsu.ru.mycash.utils

import android.content.Context
import android.content.SharedPreferences


class AppPreferences(context: Context) {
    private val PREFS_NAME = "my_app_prefs"
    private val IS_FIRST_TIME_LAUNCH = "is_first_time_launch"
    private val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, 0)

    var isFirstTimeLaunch: Boolean
        get() = prefs.getBoolean(IS_FIRST_TIME_LAUNCH, true)
        set(value) = prefs.edit().putBoolean(IS_FIRST_TIME_LAUNCH, value).apply()
}