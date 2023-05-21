package cs.vsu.ru.mycash.data

import android.graphics.Color
import android.graphics.drawable.ColorDrawable

data class Category (
    val id: Long,
    val name: String,
    val color: Int,
    val type: CategoryType,
    val isLimited: Boolean,
    val spendingLimit: Double
)