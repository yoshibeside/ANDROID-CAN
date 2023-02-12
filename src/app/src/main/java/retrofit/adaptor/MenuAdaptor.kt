package retrofit.adaptor

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mujika.R
import kotlinx.android.synthetic.main.menu_list.view.*
import retrofit.model.Menu

class MenuAdapter (private val list: ArrayList<Menu>): RecyclerView.Adapter<MenuAdapter.MenuViewHolder>(){
    inner class MenuViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        fun bind(menu: Menu) {
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

}