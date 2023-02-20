package retrofit.adaptor

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mujika.R
import retrofit.model.MenuItemStuff

class MenuAdapter (private var list: ArrayList<MenuItemStuff>): RecyclerView.Adapter<MenuAdapter.MenuViewHolder>(){
    inner class MenuViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        fun bind(menu: MenuItemStuff) {
            val menuName = itemView.findViewById<TextView>(R.id.menuName)
            val price = itemView.findViewById<TextView>(R.id.price)
            val amountSold = itemView.findViewById<TextView>(R.id.amountSold)
            val amount = itemView.findViewById<TextView>(R.id.amount)
            val description = itemView.findViewById<TextView>(R.id.description)
            with(itemView){
                menuName.text= "${menu.name}"
                price.text= "${menu.price} ${menu.currency}"
                amountSold.text= "${menu.sold} Terjual"
                amount.text= "0"
                description.text= "${menu.description}"

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : MenuViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.menu_list, parent, false)
        return MenuViewHolder(view)
    }

    override fun getItemCount(): Int = list.size;

    override fun onBindViewHolder(holder:MenuViewHolder, position: Int) {
        holder.bind(list[position])
    }

    fun filtering(list: ArrayList<MenuItemStuff>) {
        this.list = list
        notifyDataSetChanged()
    }

}