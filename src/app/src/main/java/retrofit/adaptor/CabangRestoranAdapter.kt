package retrofit.adaptor

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
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

        holder.launchMapBtn.setOnClickListener(View.OnClickListener {
            val longitude = model.longitude
            val latitude = model.latitude
            val label = model.name
            val intent = Intent(Intent.ACTION_VIEW)
            intent.setData(Uri.parse("geo:$latitude,$longitude?q=$latitude,$longitude($label)"))
            intent.setPackage("com.google.android.apps.maps")
            holder.view.getContext().startActivity(intent)
        })
    }

    override fun getItemCount(): Int {
        // this method is used for showing number of card items in recycler view.
        return cabangModelArrayList.size
    }
    
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val view = itemView
        val namaResto : TextView
        val alamatResto : TextView
        val telpResto : TextView
        val launchMapBtn : ImageButton
        init {
            namaResto = itemView.findViewById(R.id.idCabangRestoran)
            alamatResto = itemView.findViewById(R.id.idAlamat)
            telpResto = itemView.findViewById(R.id.idTelpRestoran)
            launchMapBtn = itemView.findViewById(R.id.launchMapBtn)
        }
    }

}
