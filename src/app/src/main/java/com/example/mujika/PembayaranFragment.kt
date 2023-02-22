package com.example.mujika

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.view.ViewParent
import android.widget.FrameLayout
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil.setContentView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.CodeScannerView
import com.budiyev.android.codescanner.DecodeCallback
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.gson.Gson
import com.google.gson.JsonObject
import okhttp3.ResponseBody
import retrofit.api.RetrofitClient
import retrofit2.Call
import retrofit2.Callback

class PembayaranFragment : Fragment() {

    private lateinit var codeScanner: CodeScanner
    private var appBar: AppBarLayout? = null
    private var bottomNav: BottomNavigationView? = null

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_pembayaran, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val parentView = (view.parent as? FrameLayout)?.parent as? RelativeLayout
        appBar = parentView?.findViewById<AppBarLayout>(R.id.app_bar)
        bottomNav = parentView?.findViewById<BottomNavigationView>(R.id.bottom_navigation)

        appBar?.visibility = INVISIBLE
        bottomNav?.visibility = INVISIBLE

        val scannerView = view.findViewById<CodeScannerView>(R.id.scanner_view)
        val activity = requireActivity()
        codeScanner = CodeScanner(activity, scannerView)
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
                                val transaction = requireActivity().supportFragmentManager.beginTransaction()
                                transaction.replace(R.id.container, PaymentSuccessFragment())
                                transaction.commit()
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
    }

    override fun onPause() {
        codeScanner.releaseResources()
        super.onPause()
    }


}