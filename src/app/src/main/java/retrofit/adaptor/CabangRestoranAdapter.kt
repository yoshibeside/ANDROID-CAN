package retrofit.adaptor

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import retrofit.model.CabangRestoranModel
import com.example.mujika.R

class CabangRestoranAdapter(private val cabangModelList: ArrayList<CabangRestoranModel>) :
    RecyclerView.Adapter<CabangRestoranAdapter.ViewHolder>() {
    private val cabangModelArrayList: ArrayList<CabangRestoranModel> = cabangModelList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // to inflate the layout for each item of recycler view.
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.card_restoran, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // to set data to textview and imageview of each card layout
        val model: CabangRestoranModel = cabangModelArrayList[position]
        holder.namaResto.text = model.name
        holder.alamatResto.text = model.address
        holder.telpResto.text = model.phone_number
    }

    override fun getItemCount(): Int {
        // this method is used for showing number of card items in recycler view.
        return cabangModelArrayList.size
    }

    // View holder class for initializing of your views such as TextView and Imageview.
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val namaResto : TextView
        val alamatResto : TextView
        val telpResto : TextView
        init {
            namaResto = itemView.findViewById(R.id.idCabangRestoran)
            alamatResto = itemView.findViewById(R.id.idAlamat)
            telpResto = itemView.findViewById(R.id.idTelpRestoran)
        }
    }

}
