package cs.vsu.ru.mycash.service

import android.os.Build
import androidx.annotation.RequiresApi
import com.github.javafaker.Faker
import cs.vsu.ru.mycash.data.Operation
import java.time.LocalDate
import java.time.LocalDateTime

@RequiresApi(Build.VERSION_CODES.O)
class OperationService {

    private var operations = mutableListOf<Operation>()

    init {
        val faker = Faker.instance()
        operations = (1..10).map {
            Operation(
                id = it.toLong(),
                account = faker.finance().creditCard(),
                category = faker.business().creditCardType(),
                date = LocalDateTime.now(),
                value = faker.commerce().price()
            )
        }.toMutableList()
    }
}