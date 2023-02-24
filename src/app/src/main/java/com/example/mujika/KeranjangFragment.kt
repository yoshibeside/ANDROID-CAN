package com.example.mujika

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch
import retrofit.adaptor.KeranjangAdaptor
import roomdb.AppDatabase
import roomdb.KeranjangDao
import roomdb.MenuDatabase
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

class KeranjangFragment : Fragment() {

    private lateinit var keranjangDao : KeranjangDao
    var list_menu_keranjang = ArrayList<MenuDatabase>()
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bayarBtn = view.findViewById<Button>(R.id.bayar)
        lifecycleScope.launch() {
            val tempKeranjang = keranjangDao.findMenu()
            list_menu_keranjang = ArrayList(tempKeranjang)
        }
        if (!list_menu_keranjang.isNullOrEmpty()) {
            bayarBtn.setOnClickListener {
                val activity = activity as? MainActivity
                activity?.changeFragment("Pembayaran")
            }
        }
    }

    override fun onResume() {
        super.onResume()
        val recyclerView = view?.findViewById<RecyclerView>(R.id.recycler_view_keranjang)

        lifecycleScope.launch() {
            val tempKeranjang = keranjangDao.findMenu()
            list_menu_keranjang = ArrayList(tempKeranjang)
        }
        if (!list_menu_keranjang.isNullOrEmpty()) {
            view?.findViewById<LinearLayout>(R.id.layout_pembayaran)?.visibility = VISIBLE
            val formator = NumberFormat.getInstance(Locale.ENGLISH)
            val adapter = KeranjangAdaptor(list_menu_keranjang, keranjangDao, object: KeranjangAdaptor.OnDataUpdateListener {
                override fun onDataUpdate() {
                    val updateTotalPrice = keranjangDao.getTotalPrice()
                    val textTotalPrice = String.format("Rp %s", formator.format(updateTotalPrice))
                    if (updateTotalPrice == 0) {
                        view?.findViewById<LinearLayout>(R.id.layout_pembayaran)?.visibility = INVISIBLE
                    } else {
                        view?.findViewById<TextView>(R.id.harga_total_pembayaran)?.text =
                            textTotalPrice
                    }
                }
            })
            recyclerView?.layoutManager = LinearLayoutManager(activity)
            recyclerView?.adapter = adapter
        } else {
            view?.findViewById<LinearLayout>(R.id.layout_pembayaran)?.visibility = INVISIBLE
        }
    }
}