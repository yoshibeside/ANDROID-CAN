package com.example.mujika

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.TextView
import android.widget.SearchView
import android.widget.Toast
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import kotlinx.coroutines.launch
import retrofit.adaptor.MenuAdapter
import retrofit.api.RetrofitClient
import retrofit.model.ListMenu
import retrofit.model.MenuItemStuff
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import roomdb.AppDatabase
import roomdb.MenuDatabase
import roomdb.TypeMenu

class MenuFragment : Fragment() {

    private var listMakanan = ArrayList<MenuItemStuff>()
    private var listMinuman = ArrayList<MenuItemStuff>()
    private var list = ArrayList<MenuItemStuff>()
    private lateinit var appDb : AppDatabase

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        appDb = AppDatabase.getDatabase(requireContext())
        (activity as AppCompatActivity).supportActionBar?.title = "Menu"
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_menu, container, false)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.clear()
        inflater.inflate(R.menu.menu_option_bar, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_main_setting -> Toast.makeText(requireContext(), "Food selected", Toast.LENGTH_SHORT).show()
            R.id.menu_main_setting2 -> Toast.makeText(requireContext(), "Drink Selected", Toast.LENGTH_SHORT).show()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view)
        val messageTest = view.findViewById<TextView>(R.id.message_test)
        val searchView = view.findViewById<SearchView>(R.id.search_view_menu)
        super.onViewCreated(view, savedInstanceState)

        RetrofitClient.instance.getMenu().enqueue(object: Callback<ListMenu> {
            override fun onFailure(call: Call<ListMenu>, t: Throwable) {
                println("tidak berhasil people")
                val message = "terjadi kesalahan fetch dan tidak berhasil untuk masuk database"
                messageTest.text = message
                println(t.message.toString())
            }

            override fun onResponse(call: Call<ListMenu>, response: Response<ListMenu>) {
                println(response.code().toString())

                val listResponse = response.body()?.data
                listResponse?.let {list.addAll(it)}

                list.forEach {
                    var type = it?.let {
                        val converting = TypeMenu.valueOf(it.type!!)
                        converting
                    }
                    var new_menu = MenuDatabase(
                        id_cart_menu = null,
                        name_menu = it.name,
                        type_menu = type,
                        description = it.description,
                        price = it.price,
                        sold = it.sold,
                        currencies = it.currency
                    )
                    writeData(new_menu)
                    if (it.type == "Food") {
                        listMakanan.add(it)
                    }
                    else {
                        listMinuman.add(it)
                    }
                }
                listMakanan?.addAll(listMinuman)
                val adapter = MenuAdapter(listMakanan)

                searchView.clearFocus()
                searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        return false
                    }
                    override fun onQueryTextChange(newText: String?): Boolean {
                        filterList(newText, adapter)
                        return true
                    }
                })

                recyclerView.layoutManager = LinearLayoutManager(activity)
                recyclerView.adapter = adapter;
            }
        })

    }

    private fun writeData(data: MenuDatabase) {
        lifecycleScope.launch() {
            appDb.keranjangDao().insert(data)
        }
    }

    fun filterList(query : String?, adapter: MenuAdapter) {
        if (query != null) {
            val filteredMenu = ArrayList<MenuItemStuff>()
            list.forEach {
                if (it.name!!.toLowerCase().contains(query.toLowerCase())) {
                    filteredMenu.add(it)
                }
            }
            if (filteredMenu.isEmpty()) {
                Toast.makeText(activity, "No DataFound", Toast.LENGTH_SHORT).show()
            } else {
                adapter.filtering(filteredMenu)
            }
        }
    }

}