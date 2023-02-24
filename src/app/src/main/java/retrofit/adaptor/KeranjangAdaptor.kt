package retrofit.adaptor


import android.view.LayoutInflater
import retrofit.model.MenuItemStuff
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mujika.R
import roomdb.KeranjangDao
import roomdb.MenuDatabase

class KeranjangAdaptor (private var list: ArrayList<MenuDatabase>, private val keranjangDao: KeranjangDao, private var onDataUpdateListener: OnDataUpdateListener): RecyclerView.Adapter<KeranjangAdaptor.KeranjangViewHolder>(){
    inner class KeranjangViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        fun bind(menu: MenuDatabase) {
            val menuName = itemView.findViewById<TextView>(R.id.menu_keranjang_name)
            val price = itemView.findViewById<TextView>(R.id.fullprice)
            val amount = itemView.findViewById<TextView>(R.id.amount_keranjang)
            val calculationResult = (menu.amount!! * menu.price!!)

            with(itemView){
                val textForPrice = String.format("Rp %d", calculationResult);
                println("diatas ini jawabannya. kenapa ngga mau bind ya?")
                menuName.text= menu.name_menu
                price.text= textForPrice
                amount.text= menu.amount.toString()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : KeranjangViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_keranjang, parent, false)
        return KeranjangViewHolder(view)
    }

    override fun getItemCount(): Int = list.size;

    override fun onBindViewHolder(holder:KeranjangViewHolder, position: Int) {

        holder.bind(list[position])

        holder.itemView.findViewById<Button>(R.id.plus_keranjang).setOnClickListener{
            val id_updating = list.get(position).id_cart_menu!!
            val id_menu_updating = list.get(position).id_cart_menu
            val updated_amount = (holder.itemView.findViewById<TextView>(R.id.amount_keranjang).text.toString()).toInt() + 1
            val string_price = String.format("Rp %d", updated_amount* list.get(position).price!!)
            holder.itemView.findViewById<TextView>(R.id.amount_keranjang).text = updated_amount.toString()
            holder.itemView.findViewById<TextView>(R.id.fullprice).text = string_price
            keranjangDao.update(id_updating, updated_amount)
            onDataUpdateListener.onDataUpdate()
        }

        holder.itemView.findViewById<Button>(R.id.minus_keranjang).setOnClickListener{
            val id_updating = list.get(position).id_cart_menu!!
            val updated_amount =  (holder.itemView.findViewById<TextView>(R.id.amount_keranjang).text.toString()).toInt() - 1
            val string_price = String.format("Rp %d", updated_amount* list.get(position).price!!)
            println("updated amountnya adalah segini " + updated_amount)
            if (updated_amount ==  0) {
                list.removeAt(position)
                notifyItemRemoved(position)
                notifyItemRangeChanged(position, list.size)
            } else {
                holder.itemView.findViewById<TextView>(R.id.amount_keranjang).text = updated_amount.toString()
                holder.itemView.findViewById<TextView>(R.id.fullprice).text = string_price
            }
            keranjangDao.update(id_updating, updated_amount)
            onDataUpdateListener.onDataUpdate()
        }
        onDataUpdateListener.onDataUpdate()
    }

    interface OnDataUpdateListener {
        fun onDataUpdate()
    }

}