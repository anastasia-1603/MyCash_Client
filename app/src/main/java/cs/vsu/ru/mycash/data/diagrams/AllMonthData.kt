package cs.vsu.ru.mycash.data.diagrams

data class AllMonthData(
    val accountLimit: Double,
    val target: Double,
    val balanceMonths: List<Double>,
    val incomes: List<Double>,
    val expenses: List<Double>
)