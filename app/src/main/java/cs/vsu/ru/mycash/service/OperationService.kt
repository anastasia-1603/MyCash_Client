package cs.vsu.ru.mycash.service

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import com.github.javafaker.Faker
import cs.vsu.ru.mycash.data.Operation
import java.time.LocalDate
import java.time.LocalDateTime
import kotlin.random.Random

@SuppressLint("NewApi")
class OperationService {

    private var operations = mutableListOf<Operation>()

    init {
        val categories = listOf("Транспорт", "Еда", "Одежда")
        val names = listOf("Новый счет", "Сбережения", "Отпуск")

        operations = (1..10).map {
            Operation(
                id = it.toLong(),
                account = names[Random.nextInt(names.size)],
                category = categories[Random.nextInt(categories.size)],
                date = LocalDateTime.now(),
                value = Random.nextDouble()
            )
        }.toMutableList()
    }
}