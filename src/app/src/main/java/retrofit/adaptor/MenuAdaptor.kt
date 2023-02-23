package retrofit.adaptor

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mujika.R
import retrofit.model.MenuItemStuff
import roomdb.KeranjangDao
import roomdb.MenuDatabase

class MenuAdapter (private var list: ArrayList<MenuDatabase>, private val keranjangDao: KeranjangDao ): RecyclerView.Adapter<MenuAdapter.MenuViewHolder>(){
    inner class MenuViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        fun bind(menu: MenuDatabase) {
            val menuName = itemView.findViewById<TextView>(R.id.menuName)
            val price = itemView.findViewById<TextView>(R.id.price)
            val amountSold = itemView.findViewById<TextView>(R.id.amountSold)
            val amount = itemView.findViewById<TextView>(R.id.amount)
            val description = itemView.findViewById<TextView>(R.id.description)
            with(itemView){
                menuName.text= "${menu.name_menu}"
                price.text= "${menu.price} ${menu.currencies}"
                amountSold.text= "${menu.sold} Terjual"
                amount.text= menu.amount.toString()
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

        holder.itemView.findViewById<Button>(R.id.plus).setOnClickListener{
            val view_amount  = holder.itemView.findViewById<TextView>(R.id.amount)
            val updated_amount = view_amount.text.toString().toInt() + 1
            view_amount.text = updated_amount.toString()
            keranjangDao.update(list.get(position).name_menu!!, updated_amount)
        }

        holder.itemView.findViewById<Button>(R.id.minus).setOnClickListener{
            val view_amount  = holder.itemView.findViewById<TextView>(R.id.amount)
            val current_amount = view_amount.text.toString().toInt()
            var updated_amount = 0
            if (current_amount > 0) {
                updated_amount = (current_amount-1)
            }
            view_amount.text = updated_amount.toString()
            keranjangDao.update(list.get(position).name_menu!!, updated_amount)
        }
    }

    fun filtering(list: ArrayList<MenuDatabase>) {
        this.list = list
        notifyDataSetChanged()
    }

}