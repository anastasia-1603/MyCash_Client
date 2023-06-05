package cs.vsu.ru.mycash.api

object Constants {
    const val BASE_URL = "http://45.140.168.75:4080/api/"
    const val INIT_URL = "auth/new"
    const val LOGIN = "auth/login"
    const val REGISTER = "auth/register"
    const val CATEGORY_URL = "category/get_all"
    const val INFO = "main/get/{account}/{year}/{month}/{day}"
    const val DATA_DAY = "main/get/{year}/{month}/{day}"
    const val ACCOUNT_URL = "account/get_all"
    const val DATA_MONTH = "main/get/{year}/{month}"
    const val OPERATION_ADD = "operation/add"
    const val PREDICT = "predict/{accountName}/{year}/{month}"

}