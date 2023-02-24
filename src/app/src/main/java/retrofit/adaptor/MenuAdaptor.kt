package retrofit.adaptor

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mujika.R
import roomdb.KeranjangDao
import roomdb.MenuDatabase
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

class MenuAdapter (private var heading1: String, private var heading2: String, private var list1: ArrayList<MenuDatabase>, private var list2: ArrayList<MenuDatabase>, private val keranjangDao: KeranjangDao ): RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    private val VIEW_TYPE_HEADER1 = 0
    private val VIEW_TYPE_HEADER2 = 1
    private val VIEW_TYPE_MENUITEM1 = 2
    private val VIEW_TYPE_MENUITEM2 = 3

    inner class Header1(itemView: View):RecyclerView.ViewHolder(itemView) {
        val header1 = itemView.findViewById<TextView>(R.id.message_test_makanan)
        fun bind(type: String) {
            header1.text = type
        }
    }

    inner class Header2(itemView: View):RecyclerView.ViewHolder(itemView) {
        val header2 = itemView.findViewById<TextView>(R.id.message_test_makanan)
        fun bind(type: String) {
            header2.text = type
        }
    }

    inner class MenuViewHolder1(itemView: View):RecyclerView.ViewHolder(itemView) {
        fun bind(menu: MenuDatabase) {
            val menuName = itemView.findViewById<TextView>(R.id.menuName)
            val price = itemView.findViewById<TextView>(R.id.price)
            val amountSold = itemView.findViewById<TextView>(R.id.amountSold)
            val amount = itemView.findViewById<TextView>(R.id.amount)
            val description = itemView.findViewById<TextView>(R.id.description)
            with(itemView){
                val formator = NumberFormat.getInstance(Locale.ENGLISH)
                menuName.text= "${menu.name_menu}"
                price.text= "Rp ${formator.format(menu.price)}"
                amountSold.text= "${formator.format(menu.sold)} Terjual"
                amount.text= menu.amount.toString()
                description.text= "${menu.description}"
            }
        }
    }

    inner class MenuViewHolder2(itemView: View):RecyclerView.ViewHolder(itemView) {
        fun bind(menu: MenuDatabase) {
            val menuName = itemView.findViewById<TextView>(R.id.menuName)
            val price = itemView.findViewById<TextView>(R.id.price)
            val amountSold = itemView.findViewById<TextView>(R.id.amountSold)
            val amount = itemView.findViewById<TextView>(R.id.amount)
            val description = itemView.findViewById<TextView>(R.id.description)
            with(itemView){
                val formator = NumberFormat.getInstance(Locale.ENGLISH)
                menuName.text= "${menu.name_menu}"
                price.text= "Rp ${formator.format(menu.price)}"
                amountSold.text= "${formator.format(menu.sold)} Terjual"
                amount.text= menu.amount.toString()
                description.text= "${menu.description}"
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (position == 0) {
            return VIEW_TYPE_HEADER1
        } else if (position <= list1.size) {
            return VIEW_TYPE_MENUITEM1
        } else if (position == (list1.size + 1) ) {
            return VIEW_TYPE_HEADER2
        } else {
            return VIEW_TYPE_MENUITEM2
        }
    }

    // Create view holder based on view type
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_HEADER1 -> Header1(LayoutInflater.from(parent.context).inflate(R.layout.header_section, parent, false))
            VIEW_TYPE_HEADER2 -> Header2(LayoutInflater.from(parent.context).inflate(R.layout.header_section, parent, false))
            VIEW_TYPE_MENUITEM1 -> MenuViewHolder1(LayoutInflater.from(parent.context).inflate(R.layout.menu_list, parent, false))
            VIEW_TYPE_MENUITEM2 -> MenuViewHolder2(LayoutInflater.from(parent.context).inflate(R.layout.menu_list, parent, false))
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun getItemCount(): Int{
        return (2 + list1.size + list2.size);
    }


    override fun onBindViewHolder(holder:RecyclerView.ViewHolder, position: Int) {

        when (holder.itemViewType) {
            VIEW_TYPE_HEADER1 -> {
                val headerViewHolder = holder as Header1
                headerViewHolder.bind(heading1)
            }
            VIEW_TYPE_HEADER2 -> {
                val headerViewHolder = holder as Header2
                headerViewHolder.bind(heading2)
            }
            VIEW_TYPE_MENUITEM1 -> {
                val currentPosition = position - 1
                val itemViewHolder = holder as MenuViewHolder1
                val listItem = list1[currentPosition]
                itemViewHolder.bind(listItem)
                itemViewHolder.itemView.findViewById<Button>(R.id.plus).setOnClickListener {
                    val view_amount = holder.itemView.findViewById<TextView>(R.id.amount)
                    val updated_amount = view_amount.text.toString().toInt() + 1
                    view_amount.text = updated_amount.toString()
                    list1.get(currentPosition).amount = updated_amount
                    keranjangDao.update(list1.get(currentPosition).id_cart_menu!!, updated_amount)
                }
                itemViewHolder.itemView.findViewById<Button>(R.id.minus).setOnClickListener{
                    val view_amount  = holder.itemView.findViewById<TextView>(R.id.amount)
                    val current_amount = view_amount.text.toString().toInt()
                    var updated_amount = 0
                    if (current_amount > 0) {
                        updated_amount = (current_amount-1)
                    }
                    view_amount.text = updated_amount.toString()
                    list1.get(currentPosition).amount = updated_amount
                    keranjangDao.update(list1.get(currentPosition).id_cart_menu!!, updated_amount)
                }
            }
            VIEW_TYPE_MENUITEM2 -> {
                val currentPosition = (position - list1.size -2)
                val itemViewHolder = holder as MenuViewHolder2
                val listItem = list2[currentPosition]
                itemViewHolder.bind(listItem)
                itemViewHolder.itemView.findViewById<Button>(R.id.plus).setOnClickListener {
                    val view_amount = holder.itemView.findViewById<TextView>(R.id.amount)
                    val updated_amount = view_amount.text.toString().toInt() + 1
                    view_amount.text = updated_amount.toString()
                    list2.get(currentPosition).amount = updated_amount
                    keranjangDao.update(list2.get(currentPosition).id_cart_menu!!, updated_amount)
                }
                itemViewHolder.itemView.findViewById<Button>(R.id.minus).setOnClickListener{
                    val view_amount  = holder.itemView.findViewById<TextView>(R.id.amount)
                    val current_amount = view_amount.text.toString().toInt()
                    var updated_amount = 0
                    if (current_amount > 0) {
                        updated_amount = (current_amount-1)
                    }
                    view_amount.text = updated_amount.toString()
                    list2.get(currentPosition).amount = updated_amount
                    keranjangDao.update(list2.get(currentPosition).id_cart_menu!!, updated_amount)
                }
            }
        }
    }

    fun filtering(list1filter: ArrayList<MenuDatabase>, list2filter: ArrayList<MenuDatabase>) {
        this.list1 = list1filter
        this.list2 = list2filter
        notifyDataSetChanged()
    }
}