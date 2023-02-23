package com.example.mujika

import android.os.Bundle
import android.view.*
import android.widget.SearchView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch
import retrofit.adaptor.KeranjangAdaptor
import retrofit.adaptor.MenuAdapter
import retrofit.api.RetrofitClient
import retrofit.model.ListMenu
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import roomdb.AppDatabase
import roomdb.KeranjangDao
import roomdb.MenuDatabase
import roomdb.TypeMenu

class KeranjangFragment : Fragment() {

    private lateinit var keranjangDao : KeranjangDao
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_keranjang, container, false)
        // Inflate the layout for this fragment
        val appDb = AppDatabase.getDatabase(requireContext())
        keranjangDao = appDb.keranjangDao()
        setHasOptionsMenu(false)
        (activity as AppCompatActivity).supportActionBar?.title = ""
        return view
    }

    override fun onResume() {
        super.onResume()
        val recyclerView = view?.findViewById<RecyclerView>(R.id.recycler_view_keranjang)
        var list_menu_keranjang = ArrayList<MenuDatabase>()

        lifecycleScope.launch() {
            val tempKeranjang = keranjangDao.findMenu()
            list_menu_keranjang = ArrayList(tempKeranjang)
        }
        if (!list_menu_keranjang.isNullOrEmpty()) {
            println(list_menu_keranjang)
            val adapter = KeranjangAdaptor(list_menu_keranjang, keranjangDao, object: KeranjangAdaptor.OnDataUpdateListener {
                override fun onDataUpdate() {
                    val updateTotalPrice = keranjangDao.getTotalPrice()
                    val textTotalPrice = String.format("Rp %d", updateTotalPrice)
                    view?.findViewById<TextView>(R.id.harga_total_pembayaran)?.text = textTotalPrice
                }
            })
            recyclerView?.layoutManager = LinearLayoutManager(activity)
            recyclerView?.adapter = adapter
        }
    }
}