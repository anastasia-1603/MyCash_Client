package cs.vsu.ru.mycash.data

import java.time.LocalDateTime

data class Operation (
    val category: Category,
    val account: Account,
    val value: Double,
    val date: String
)