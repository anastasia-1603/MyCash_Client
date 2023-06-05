package cs.vsu.ru.mycash.ui.main.accounts

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import cs.vsu.ru.mycash.data.Account
import cs.vsu.ru.mycash.data.Category
import cs.vsu.ru.mycash.databinding.ItemAccountBinding
import cs.vsu.ru.mycash.databinding.ItemCategoryBinding
import cs.vsu.ru.mycash.ui.main.categories.CategoriesAdapter

class AccountsAdapter(private val onClickListener: OnClickListener)
    : RecyclerView.Adapter<AccountsAdapter.AccountsViewHolder>() {

    var data: List<Account> = emptyList()
        set(newValue) {
            field = newValue
            notifyDataSetChanged()
        }

    class AccountsViewHolder(val binding: ItemAccountBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AccountsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemAccountBinding.inflate(inflater, parent, false)

        return AccountsViewHolder(binding)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: AccountsViewHolder, position: Int) {
        val account = data[position]

        with (holder.binding) {

            accountName.text = account.name.toString()

            holder.itemView.setOnClickListener {
                onClickListener.onClick(account)
            }
        }
    }

    override fun getItemCount(): Int = data.size

    class OnClickListener(val clickListener: (account: Account) -> Unit) {
        fun onClick(account: Account) = clickListener(account)
    }

}