package cs.vsu.ru.mycash.data

data class Account(
    var name: String,
    var balance: Double,
    var target: Double?,
    var spendingLimit: Double?,
    var isLimited: Boolean
)
