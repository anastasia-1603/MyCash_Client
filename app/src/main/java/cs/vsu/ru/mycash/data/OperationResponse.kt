package cs.vsu.ru.mycash.data

data class OperationResponse(
    val type: LimitType
)

enum class LimitType {
    NONE,
    CATEGORY,
    ACCOUNT

}