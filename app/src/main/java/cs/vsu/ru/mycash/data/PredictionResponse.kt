package cs.vsu.ru.mycash.data

data class PredictionResponse(
    val expensePrediction: Double,
    val incomePrediction: Double,
    val topCategory: Category,
    val topCategoryPrediction: Double
)