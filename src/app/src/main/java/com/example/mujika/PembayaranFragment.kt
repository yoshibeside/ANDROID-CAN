package com.example.mujika

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.fragment.app.Fragment
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.CodeScannerView
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ScanMode
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.gson.Gson
import com.google.gson.JsonObject
import okhttp3.ResponseBody
import retrofit.adaptor.KeranjangAdaptor
import retrofit.api.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import roomdb.AppDatabase
import roomdb.KeranjangDao
import java.text.NumberFormat
import java.util.*

class PembayaranFragment : Fragment() {

    private lateinit var codeScanner: CodeScanner
    private var appBar: AppBarLayout? = null
    private var bottomNav: BottomNavigationView? = null
    private lateinit var keranjangDao : KeranjangDao

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val appDb = AppDatabase.getDatabase(requireContext())
        keranjangDao = appDb.keranjangDao()
        return inflater.inflate(R.layout.fragment_pembayaran, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val parentView = (view.parent as? FrameLayout)?.parent as? RelativeLayout
        appBar = parentView?.findViewById(R.id.app_bar)
        bottomNav = parentView?.findViewById(R.id.bottom_navigation)
        val formator = NumberFormat.getInstance(Locale.ENGLISH)
        val updateTotalPrice = keranjangDao.getTotalPrice()
        val textTotalPrice = String.format("Rp %s", formator.format(updateTotalPrice))
        view?.findViewById<TextView>(R.id.total_amount)?.text = textTotalPrice

        val scannerView = view.findViewById<CodeScannerView>(R.id.scanner_view)
        val activity = requireActivity()
        codeScanner = CodeScanner(activity, scannerView)
        codeScanner.formats = CodeScanner.TWO_DIMENSIONAL_FORMATS
        codeScanner.scanMode = ScanMode.CONTINUOUS
        codeScanner.decodeCallback = DecodeCallback {
            activity.runOnUiThread {
                val id = it.text
                Log.d("TAG", id)
                RetrofitClient.instance.postPayment(id).enqueue(object: Callback<ResponseBody> {
                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        Log.e("TAG", "Failed to scan")
                    }

                    override fun onResponse(
                        call: Call<ResponseBody>,
                        response: retrofit2.Response<ResponseBody>
                    ) {
                        val gson = Gson()
                        val status = gson.fromJson(response.body()?.string(), JsonObject::class.java)?.get("status").toString().replace("\"","")
                        status?.let {
                            if (status == "SUCCESS") {
                                val activity = activity as? MainActivity
                                activity?.changeFragment("Payment Success")
                            }
                            else {
                                Toast.makeText(context, "Payment failed", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                })

            }
        }
        scannerView.setOnClickListener {
            codeScanner.startPreview()
        }
    }

    override fun onResume() {
        super.onResume()
        codeScanner.startPreview()
        appBar?.visibility = INVISIBLE
        bottomNav?.visibility = INVISIBLE
    }


    override fun onPause() {
        codeScanner.releaseResources()
        super.onPause()
        appBar?.visibility = VISIBLE
        bottomNav?.visibility = VISIBLE
    }

}