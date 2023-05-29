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
        val category = Category(
            "Транспорт",
            170,
            CategoryType.EXPENSE,
            false, 0.0)
        val category2 = Category(
            "Еда",
            170,
            CategoryType.EXPENSE,
            false, 0.0)
        val category3 = Category(
            "Развлечения",
            170,
            CategoryType.INCOME,
            false, 0.0)
        val account = Account( "Новый счет", 1000.0, 10.0, 10000.0, true)
//        val account2 = Account(2, "Второй счет", 1000.0, 10.0, 10000.0)

        val categories = listOf(category, category2, category3)

        operations = (1..10).map {
            Operation(
                account = account,
                category = categories[Random.nextInt(categories.size)],
                date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm")),
                value = Random.nextInt(10000).toDouble()
            )
        }.toMutableList()
    }
}