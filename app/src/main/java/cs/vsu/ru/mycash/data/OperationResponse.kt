package cs.vsu.ru.mycash.data

data class OperationResponse(
    val overLimitType: OverLimitType
)

enum class OverLimitType {
    NONE,
    CATEGORY,
    ACCOUNT

}