package com.example.mujika

import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.TextView
import android.widget.SearchView
import android.widget.Toast
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
import roomdb.KeranjangDao
import roomdb.MenuDatabase
import roomdb.TypeMenu

class MenuFragment : Fragment() {

    private var listMakanan = ArrayList<MenuDatabase>()
    private var listMinuman = ArrayList<MenuDatabase>()
    private var orderedMakananMinuman = ArrayList<MenuDatabase>()
    private var list = ArrayList<MenuItemStuff>()
    private lateinit var keranjangDao : KeranjangDao

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val appDb = AppDatabase.getDatabase(requireContext())
        keranjangDao = appDb.keranjangDao()
        (activity as AppCompatActivity).supportActionBar?.title = "Menu"
        setHasOptionsMenu(true)

        val view = inflater.inflate(R.layout.fragment_menu, container, false)

        val messageTest = view.findViewById<TextView>(R.id.message_test)
        val searchView = view.findViewById<SearchView>(R.id.search_view_menu)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view)

        super.onViewCreated(view, savedInstanceState)
        println("dari onViewCreated tiap kali ngetikkah?")
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
                    val type = it.let {
                        val converting = TypeMenu.valueOf(it.type!!)
                        converting
                    }
                    val new_menu = MenuDatabase(
                        id_cart_menu = null,
                        name_menu = it.name,
                        type_menu = type,
                        description = it.description,
                        price = it.price,
                        sold = it.sold,
                        currencies = it.currency,
                        amount = it.getAmount()
                    )
                    writeData(new_menu)
                }
                lifecycleScope.launch() {
                    val tempMakanan = keranjangDao.findByType(TypeMenu.Food)
                    val tempMinuman = keranjangDao.findByType(TypeMenu.Drink)
                    listMakanan = ArrayList(tempMakanan)
                    listMinuman = ArrayList(tempMinuman)
                }
                orderedMakananMinuman.addAll(listMakanan)
                orderedMakananMinuman.addAll(listMinuman)
                val adapter = MenuAdapter(orderedMakananMinuman, keranjangDao)

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

                recyclerView?.layoutManager = LinearLayoutManager(activity)
                recyclerView?.adapter = adapter
            }
        })
        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.clear()
        inflater.inflate(R.menu.menu_option_bar, menu)
        menu?.let {
            val menuItem1 = it.findItem(R.id.menu_main_setting)
            val title1 = SpannableString(menuItem1.title)
            title1.setSpan(ForegroundColorSpan(Color.BLACK), 0, title1.length, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
            menuItem1.title = title1

            val menuItem2 = it.findItem(R.id.menu_main_setting2)
            val title2 = SpannableString(menuItem2.title)
            title2.setSpan(ForegroundColorSpan(Color.BLACK), 0, title2.length, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
            menuItem2.title = title2
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val recyclerView = view?.findViewById<RecyclerView>(R.id.recycler_view)
        val searchView = view?.findViewById<SearchView>(R.id.search_view_menu)
        when (item.itemId) {
            R.id.menu_main_setting -> {
                val adapter = MenuAdapter(listMakanan, keranjangDao)
                recyclerView?.adapter = adapter
                searchView?.clearFocus()
                searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        return false
                    }
                    override fun onQueryTextChange(newText: String?): Boolean {
                        filterList(newText, adapter)
                        return true
                    }
                })
            }
            R.id.menu_main_setting2 -> {
                val adapter = MenuAdapter(listMinuman, keranjangDao)
                recyclerView?.adapter = adapter
                searchView?.clearFocus()
                searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        return false
                    }
                    override fun onQueryTextChange(newText: String?): Boolean {
                        filterList(newText, adapter)
                        return true
                    }
                })

            }
        }
        return super.onOptionsItemSelected(item)
    }


    private fun writeData(data: MenuDatabase) {
        lifecycleScope.launch() {
            keranjangDao.insert(data)
        }
    }

    fun filterList(query : String?, adapter: MenuAdapter) {
        if (query != null) {
            val filteredMenu = ArrayList<MenuDatabase>()
            orderedMakananMinuman.forEach {
                if (it.name_menu?.lowercase()!!.contains(query.lowercase())) {
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