package cs.vsu.ru.mycash.ui.main.operation

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import cs.vsu.ru.mycash.data.Operation
import cs.vsu.ru.mycash.databinding.ItemOperationBinding
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class OperationAdapter(private val onClickListener: OnClickListener) :
    RecyclerView.Adapter<OperationAdapter.OperationViewHolder>() {

    var data: List<Operation> = emptyList()
        set(newValue) {
            field = newValue
            notifyDataSetChanged()
        }

    class OperationViewHolder(val binding: ItemOperationBinding) :
        RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): OperationViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemOperationBinding.inflate(inflater, parent, false)

        return OperationViewHolder(binding)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: OperationViewHolder, position: Int) {
        val operation = data[position]

        with(holder.binding) {
            categoryName.text = operation.category.name
            value.text = operation.value.toString()

            val date = LocalDateTime.parse(operation.dateTime)
            val formattedDateTime = DateTimeFormatter.ofPattern("dd.MM HH:mm").format(date)

            time.text = formattedDateTime

            imageView.setColorFilter(operation.category.color)

            holder.itemView.setOnClickListener {
                onClickListener.onClick(operation)
            }
        }
    }

    override fun getItemCount(): Int = data.size

    class OnClickListener(val clickListener: (operation: Operation) -> Unit) {
        fun onClick(operation: Operation) = clickListener(operation)
    }

}