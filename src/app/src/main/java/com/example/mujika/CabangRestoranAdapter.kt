package com.example.mujika

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CabangRestoranAdapter(private val courseModelList: ArrayList<CabangRestoranModel>) :
    RecyclerView.Adapter<CabangRestoranAdapter.ViewHolder>() {
    private val courseModelArrayList: ArrayList<CabangRestoranModel>
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CabangRestoranAdapter.ViewHolder {
        // to inflate the layout for each item of recycler view.
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.card_restoran, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: CabangRestoranAdapter.ViewHolder, position: Int) {
        // to set data to textview and imageview of each card layout
        val model: CabangRestoranModel = courseModelArrayList[position]
        holder.namaResto.text = model.getNamaRestoran()
        holder.alamatResto.text = model.getAlamatRestoran()
        holder.telpResto.text = model.getTelpRestoran()
    }

    override fun getItemCount(): Int {
        // this method is used for showing number of card items in recycler view.
        return courseModelArrayList.size
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

    // Constructor
    init {
        this.courseModelArrayList = courseModelList
    }
}
