package com.example.mujika

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class CabangRestoranFragment : Fragment() {

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        (activity as AppCompatActivity).supportActionBar?.title = "Cabang Restoran"

        val view = inflater.inflate(R.layout.fragment_cabang_restoran, container, false)
        val restoranRV = view.findViewById<RecyclerView>(R.id.idRestoranRV)

        // Here, we have created new array list and added data to it
        val restoranModelArrayList: ArrayList<CabangRestoranModel> = ArrayList<CabangRestoranModel>()
        restoranModelArrayList.add(CabangRestoranModel(
            "Cabang Restoran 1",
            "Jalan Ganesa No.10, Lb. Siliwangi, Kecamatan Coblong, Bandung, Jawa Barat, 40132",
            "14045"))
        restoranModelArrayList.add(CabangRestoranModel(
            "Cabang Restoran 2",
            "Jalan Ganesa No.10, Lb. Siliwangi, Kecamatan Coblong, Bandung, Jawa Barat, 40132",
            "14045"))
        restoranModelArrayList.add(CabangRestoranModel(
            "Cabang Restoran 3",
            "Jalan Ganesa No.10, Lb. Siliwangi, Kecamatan Coblong, Bandung, Jawa Barat, 40132",
            "14045"))
        restoranModelArrayList.add(CabangRestoranModel(
            "Cabang Restoran 4",
            "Jalan Ganesa No.10, Lb. Siliwangi, Kecamatan Coblong, Bandung, Jawa Barat, 40132",
            "14045"))
        restoranModelArrayList.add(CabangRestoranModel(
            "Cabang Restoran 5",
            "Jalan Ganesa No.10, Lb. Siliwangi, Kecamatan Coblong, Bandung, Jawa Barat, 40132",
            "14045"))
        restoranModelArrayList.add(CabangRestoranModel(
            "Cabang Restoran 6",
            "Jalan Ganesa No.10, Lb. Siliwangi, Kecamatan Coblong, Bandung, Jawa Barat, 40132",
            "14045"))
        restoranModelArrayList.add(CabangRestoranModel(
            "Cabang Restoran 7",
            "Jalan Ganesa No.10, Lb. Siliwangi, Kecamatan Coblong, Bandung, Jawa Barat, 40132",
            "14045"))
        restoranModelArrayList.add(CabangRestoranModel(
            "Cabang Restoran 8",
            "Jalan Ganesa No.10, Lb. Siliwangi, Kecamatan Coblong, Bandung, Jawa Barat, 40132",
            "14045"))

        val restoranAdapter = CabangRestoranAdapter(restoranModelArrayList)

        val linearLayoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)

        restoranRV.layoutManager = linearLayoutManager
        restoranRV.adapter = restoranAdapter

        return view
    }
}