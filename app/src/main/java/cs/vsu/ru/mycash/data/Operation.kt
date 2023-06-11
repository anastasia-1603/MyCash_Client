package cs.vsu.ru.mycash.data



data class Operation(
    val id: Long?,
    var category: Category,
    var accountName: String,
    var value: Double,
    var dateTime: String,
    var comment: String
)