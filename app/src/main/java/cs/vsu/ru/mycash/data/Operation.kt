package cs.vsu.ru.mycash.data

data class Operation (
    val id: Long,
    val category: Category,
    val accountName: String,
    val value: Double,
    val dateTime: String,
    val comment: String
)