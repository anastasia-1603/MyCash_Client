package cs.vsu.ru.mycash.data

data class MainScreenAccountResponse (
    val accountName: String,
    val balance: Double,
    val operations: List<Operation>
)