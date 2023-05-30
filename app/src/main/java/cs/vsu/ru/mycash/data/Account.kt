package cs.vsu.ru.mycash.data

data class Account(
    val name: String,
    val balance: Double,
    val target: Double,
    val limit: Double,
    val isLimited: Boolean
)
