package cs.vsu.ru.mycash.utils

import android.content.Context
import android.content.SharedPreferences



class AppPreferences(context: Context) {

    private val PREFS_NAME = "my_app_prefs"
    private val IS_FIRST_TIME_LAUNCH = "is_first_time_launch"
    private val IS_AUTH = "is_auth"
    private val TOKEN = "TOKEN"
    private val NEW_TOKEN = "NEW_TOKEN"

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

    var newToken: String?
        get() = prefs.getString(NEW_TOKEN, "newToken")
        set(value) = prefs.edit().putString(NEW_TOKEN, value).apply()

//    inline fun SharedPreferences.editMe(operation: (SharedPreferences.Editor) -> Unit) {
//        val editMe = edit()
//        operation(editMe)
//        editMe.apply()
//    }
//
//    var SharedPreferences.token
//        get() = getString(TOKEN, "token")
//        set(value) {
//            editMe {
//                it.putString(TOKEN, value)
//            }
//        }
//
//    var SharedPreferences.isAuth
//        get() = getBoolean(IS_AUTH, false)
//        set(value) {
//            editMe {
//                it.putBoolean(IS_AUTH, value)
//            }
//        }
//
//    var SharedPreferences.isFirstTimeLaunch
//        get() = getBoolean(IS_FIRST_TIME_LAUNCH, true)
//        set(value) {
//            editMe {
//                it.putBoolean(IS_FIRST_TIME_LAUNCH, value)
//            }
//        }
//
//    var SharedPreferences.clearValues
//        get() = { }
//        set(value) {
//            editMe {
//                it.clear()
//            }
//        }
}