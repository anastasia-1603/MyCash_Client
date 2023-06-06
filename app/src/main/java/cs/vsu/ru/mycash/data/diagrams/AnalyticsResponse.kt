package cs.vsu.ru.mycash.data.diagrams

data class AnalyticsResponse(
    val incomes: List<DayCategoriesSum>,
    val expenses: List<DayCategoriesSum>,
    val all: AllMonthData
)