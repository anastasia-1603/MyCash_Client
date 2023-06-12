package cs.vsu.ru.mycash.data.diagrams

import cs.vsu.ru.mycash.data.Category

data class DayCategoriesSum(
    val day: Int,
    val data: List<Pair<Category, Double>>
)