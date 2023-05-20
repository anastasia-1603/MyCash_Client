package cs.vsu.ru.mycash.data

data class Account(
    val id: Long,
    val name: String,
    val balance: Double,
    val target: Double,
    val limit: Double

)
