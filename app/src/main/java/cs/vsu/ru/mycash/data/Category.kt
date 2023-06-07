package cs.vsu.ru.mycash.data

import android.graphics.Color
import android.graphics.drawable.ColorDrawable

data class Category (
    var name: String,
    var color: Int,
    val type: CategoryType,
    var isLimited: Boolean,
    var spendingLimit: Double
)