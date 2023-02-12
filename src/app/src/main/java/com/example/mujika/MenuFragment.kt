package com.example.mujika

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager

import kotlinx.android.synthetic.main.fragment_menu.*
import retrofit.adaptor.MenuAdapter
import retrofit.api.RetrofitClient
import retrofit.model.ListMenu
import retrofit.model.Menu
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MenuFragment : Fragment() {

    private var list = ArrayList<Menu>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        (activity as AppCompatActivity).supportActionBar?.title = "Menu"
        return inflater.inflate(R.layout.fragment_menu, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        RetrofitClient.instance.getMenu().enqueue(object: Callback<ListMenu> {
            override fun onFailure(call: Call<ListMenu>, t: Throwable) {
                println("tidak berhasil people")
                message_test.text = "terjadi kesalahan fetch dan tidak berhasil"
                println(t.message.toString())
            }

            override fun onResponse(call: Call<ListMenu>, response: Response<ListMenu>) {
                println(response.code().toString())
                val listResponse = response.body()?.data
                listResponse?.let {list.addAll(it)}
                val adapter = MenuAdapter(list)
                recycler_view.layoutManager = LinearLayoutManager(activity)
                recycler_view.adapter = adapter;
            }
        })
    }

}