package cs.vsu.ru.mycash.service

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import androidx.annotation.RequiresApi
import com.github.javafaker.Faker
import cs.vsu.ru.mycash.R
import cs.vsu.ru.mycash.data.Account
import cs.vsu.ru.mycash.data.Category
import cs.vsu.ru.mycash.data.CategoryType
import cs.vsu.ru.mycash.data.Operation
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.random.Random

@SuppressLint("NewApi")
class OperationService {

    var operations = mutableListOf<Operation>()


    init {
        val category = Category(1,
            "Транспорт",
            Color.GREEN,
            CategoryType.ALL,
            false, 0.0)
        val category2 = Category(2,
            "Еда",
            Color.CYAN,
            CategoryType.ALL,
            false, 0.0)
        val category3 = Category(3,
            "Развлечения",
            Color.MAGENTA,
            CategoryType.ALL,
            false, 0.0)
        val account = Account(1, "Новый счет", 1000.0, 10.0, 10000.0)
//        val account2 = Account(2, "Второй счет", 1000.0, 10.0, 10000.0)

        val categories = listOf(category, category2, category3)

        operations = (1..10).map {
            Operation(
                id = it.toLong(),
                account = account,
                category = categories[Random.nextInt(categories.size)],
                date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm")),
                value = Random.nextInt(10000).toDouble()
            )
        }.toMutableList()
    }
}