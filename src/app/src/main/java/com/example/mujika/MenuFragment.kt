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
    private var tempMakananFilter = ArrayList<MenuDatabase>()
    private var tempMinumanFilter = ArrayList<MenuDatabase>()
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


        RetrofitClient.instance.getMenu().enqueue(object: Callback<ListMenu> {
            override fun onFailure(call: Call<ListMenu>, t: Throwable) {
                val message = "terjadi kesalahan fetch dan tidak berhasil untuk masuk database"
                println(message)
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
                val searchView = view?.findViewById<SearchView>(R.id.search_view_menu)
                val recyclerViewMinuman = view?.findViewById<RecyclerView>(R.id.recycler_view_minuman)

                lifecycleScope.launch() {
                    val tempMakanan = keranjangDao.findByType(TypeMenu.Food)
                    val tempMinuman = keranjangDao.findByType(TypeMenu.Drink)
                    tempMakananFilter = ArrayList(keranjangDao.findByType(TypeMenu.Food))
                    tempMinumanFilter = ArrayList(keranjangDao.findByType(TypeMenu.Drink))
                    listMakanan = ArrayList(tempMakanan)
                    listMinuman = ArrayList(tempMinuman)
                }
                val adapterAll = MenuAdapter("Makanan","Minuman", listMakanan, listMinuman, keranjangDao)

                searchView?.clearFocus()
                searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        return false
                    }

                    override fun onQueryTextChange(newText: String?): Boolean {
                        filterList(newText, adapterAll)
                        return true
                    }
                })
                println("Saat on response jumlahnya adalah seabgai beriut ")
                recyclerViewMinuman?.layoutManager = LinearLayoutManager(activity)
                recyclerViewMinuman?.adapter = adapterAll
            }
        })

        return view
    }

    override fun onResume() {
        super.onResume()

        println("THIS SHOULD HAVE SOMETHING INSIDE")
        if (!list.isNullOrEmpty()) {
            println("JUmlahnya adalah sebagai berikut ")
            val searchView = view?.findViewById<SearchView>(R.id.search_view_menu)
            val recyclerViewMinuman = view?.findViewById<RecyclerView>(R.id.recycler_view_minuman)

            lifecycleScope.launch() {
                val tempMakanan = keranjangDao.findByType(TypeMenu.Food)
                val tempMinuman = keranjangDao.findByType(TypeMenu.Drink)
                listMakanan = ArrayList(tempMakanan)
                listMinuman = ArrayList(tempMinuman)
            }
            val adapterAll = MenuAdapter("Makanan","Minuman", listMakanan, listMinuman, keranjangDao)

            searchView?.clearFocus()
            searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    filterList(newText, adapterAll)
                    return true
                }
            })

            recyclerViewMinuman?.layoutManager = LinearLayoutManager(activity)
            recyclerViewMinuman?.adapter = adapterAll
        }
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
        val recyclerView = view?.findViewById<RecyclerView>(R.id.recycler_view_minuman)
        val searchView = view?.findViewById<SearchView>(R.id.search_view_menu)
        val temp = ArrayList<MenuDatabase>()
        when (item.itemId) {
            R.id.menu_main_setting -> {
                val adapter = MenuAdapter("Makanan", "", listMakanan,temp, keranjangDao)
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
                val adapter = MenuAdapter("Minuman", "", listMinuman,temp, keranjangDao)
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
            var filteredMenuMakanan = ArrayList<MenuDatabase>()
            var filteredMenuMinuman = ArrayList<MenuDatabase>()

            tempMakananFilter.forEach {
                if (it.name_menu?.lowercase()!!.contains(query.lowercase())) {
                    filteredMenuMakanan.add(it)
                }
            }
            tempMinumanFilter.forEach {
                if (it.name_menu?.lowercase()!!.contains(query.lowercase())) {
                    filteredMenuMinuman.add(it)
                }
            }
            if (filteredMenuMakanan.isEmpty() && filteredMenuMinuman.isEmpty()) {
                Toast.makeText(activity, "No DataFound", Toast.LENGTH_SHORT).show()
            } else {
                adapter.filtering(filteredMenuMakanan, filteredMenuMinuman)
            }
        }
    }
}