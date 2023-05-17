package cs.vsu.ru.mycash.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import cs.vsu.ru.mycash.R
import cs.vsu.ru.mycash.data.Operation
import cs.vsu.ru.mycash.databinding.ItemOperationBinding

class OperationAdapter : RecyclerView.Adapter<OperationAdapter.OperationViewHolder>() {

    var data: List<Operation> = emptyList()
    set(newValue) {
        field = newValue
        notifyDataSetChanged()
    }

    class OperationViewHolder(val binding: ItemOperationBinding) : RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): OperationAdapter.OperationViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemOperationBinding.inflate(inflater, parent, false)

        return OperationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OperationAdapter.OperationViewHolder, position: Int) {
        val operation = data[position]
        val context = holder.itemView.context
        val colors = mapOf("Транспорт" to R.color.purple_500,
            "Еда" to R.color.teal_700, "Одежда" to R.color.red)

        with (holder.binding) {
            val color = colors[operation.category]

            categoryName.text = operation.category
            value.text = operation.value.toString()
            time.text = operation.date.toString()
            color?.let { ContextCompat.getColor(context, it) }?.let {
                imageView.setColorFilter(
                    it,
                    android.graphics.PorterDuff.Mode.SRC_IN
                )
            }
        }
    }

    override fun getItemCount(): Int = data.size
}