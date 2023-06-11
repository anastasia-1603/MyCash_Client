package cs.vsu.ru.mycash.ui.main.categories

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import cs.vsu.ru.mycash.data.Category
import cs.vsu.ru.mycash.databinding.ItemCategoryBinding

class CategoriesAdapter(private val onClickListener: CategoriesAdapter.OnClickListener)
    : RecyclerView.Adapter<CategoriesAdapter.CategoriesViewHolder>() {

    var data: List<Category> = emptyList()
        set(newValue) {
            field = newValue
            notifyDataSetChanged()
        }

    class CategoriesViewHolder(val binding: ItemCategoryBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CategoriesViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemCategoryBinding.inflate(inflater, parent, false)

        return CategoriesViewHolder(binding)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: CategoriesViewHolder, position: Int) {
        val category = data[position]

        with (holder.binding) {

            categoryName.text = category.name
            if (category.color > 0)
            {
                imageView.setColorFilter(-category.color)
            }
            else {
                imageView.setColorFilter(category.color)
            }

            holder.itemView.setOnClickListener {
                onClickListener.onClick(category)
            }
        }
    }

    override fun getItemCount(): Int = data.size

    class OnClickListener(val clickListener: (category: Category) -> Unit) {
        fun onClick(category: Category) = clickListener(category)
    }


}