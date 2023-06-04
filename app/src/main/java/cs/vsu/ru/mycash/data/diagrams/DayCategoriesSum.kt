package cs.vsu.ru.mycash.data.diagrams

import cs.vsu.ru.mycash.data.Category

data class DayCategoriesSum(
    val category: Category,
    val data: List<Pair<Int, Double>>
)