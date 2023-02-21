package com.example.mujika

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit.adaptor.CabangRestoranAdapter
import retrofit.api.RetrofitClient
import retrofit.model.CabangRestoranModel
import retrofit.model.ListCabangRestoran
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class CabangRestoranFragment : Fragment() {

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        (activity as AppCompatActivity).supportActionBar?.title = ""
        val view = inflater.inflate(R.layout.fragment_cabang_restoran, container, false)
        val restoranRV = view.findViewById<RecyclerView>(R.id.idRestoranRV)
        val errorMsg = view.findViewById<TextView>(R.id.error_msg)

        var restoranModelArrayList = ArrayList<CabangRestoranModel>()

        restoranRV.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)

        RetrofitClient.instance.getRestoran().enqueue(object: Callback<ListCabangRestoran> {
            override fun onFailure(call: Call<ListCabangRestoran>, t: Throwable) {
                errorMsg.visibility = VISIBLE
                restoranRV.visibility = INVISIBLE
            }

            override fun onResponse(call: Call<ListCabangRestoran>, response: Response<ListCabangRestoran>) {
                errorMsg.visibility = INVISIBLE
                val listResponse = response.body()?.data
                listResponse?.let {restoranModelArrayList.addAll(it)}
                restoranModelArrayList.sortBy { CabangRestoranModel -> CabangRestoranModel.name }
                restoranRV.adapter = CabangRestoranAdapter(restoranModelArrayList)
            }
        })

        return view;
    }
}