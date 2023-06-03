package cs.vsu.ru.mycash.data

data class OperationResponse(
    val isOverLimit: Boolean,
    val type: LimitType
)

enum class LimitType {
    CATEGORY,
    ACCOUNT
}