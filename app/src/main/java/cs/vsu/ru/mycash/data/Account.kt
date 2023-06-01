package cs.vsu.ru.mycash.data

data class Account(
    val name: String,
    val balance: Double,
    val target: Double,
    val spendingLimit: Double,
    val isLimited: Boolean
)
