package cs.vsu.ru.mycash.data

data class Operation (
    val category: Category,
    val account: Account,
    val value: Double,
    val created: String
)