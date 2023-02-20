package com.example.mujika

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil.setContentView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.CodeScannerView
import com.budiyev.android.codescanner.DecodeCallback
import com.google.gson.Gson
import com.google.gson.JsonObject
import okhttp3.ResponseBody
import retrofit.api.RetrofitClient
import retrofit2.Call
import retrofit2.Callback

class PembayaranFragment : Fragment() {

    private lateinit var codeScanner: CodeScanner

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_pembayaran, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val scannerView = view.findViewById<CodeScannerView>(R.id.scanner_view)
        val activity = requireActivity()
        codeScanner = CodeScanner(activity, scannerView)
        codeScanner.decodeCallback = DecodeCallback {
            activity.runOnUiThread {
                // TODO
                // read the id and show whether payment is successful or not
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
                        val status = gson.fromJson(response.body()?.string(), JsonObject::class.java).get("status").toString().replace("\"","")
                        if (status == "SUCCESS") {
                            val transaction = requireActivity().supportFragmentManager.beginTransaction()
                            transaction.replace(R.id.container, PaymentSuccessFragment())
                            transaction.remove(this@PembayaranFragment)
                            transaction.commit()
                        }
                        else if (status == "FAILED") {
                            Toast.makeText(context, "Wrong Code", Toast.LENGTH_SHORT).show()
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