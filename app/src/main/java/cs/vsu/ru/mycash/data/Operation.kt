package cs.vsu.ru.mycash.data

import java.time.LocalDateTime

data class Operation (
    val id: Long,
    val category: String, //потом поменять на классы
    val account: String,
    val value: Double,
    val date: LocalDateTime
)