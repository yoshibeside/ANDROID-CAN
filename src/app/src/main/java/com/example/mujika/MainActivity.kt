package com.example.mujika

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.FrameLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.mujika.databinding.ActivityMainBinding
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        changeFragment("Twibbon")
        binding.bottomNavigation.setOnItemSelectedListener {
                when(it.itemId){
                R.id.menu -> changeFragment("Menu")
                R.id.cabangrestoran -> changeFragment("Cabang Restoran")
                R.id.twibbon -> changeFragment("Twibbon")
                R.id.keranjang -> changeFragment("Keranjang")
                    R.id.pembayaran -> changeFragment("Pembayaran")
            }
            true
        }
    }
    public fun changeFragment(tag: String){

        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        var fragment = fragmentManager.findFragmentByTag(tag)

        setSupportActionBar(findViewById(R.id.Toolbar))
        val tv1: TextView = findViewById(R.id.toolbar_title)
        tv1.text = tag
        if (tag == "Menu") {
            tv1.text = ""
        }

        if (fragment == null) {
            // create new instance according to the tag given
            val newFragment = when (tag) {
                "Twibbon" -> TwibbonFragment()
                "Menu" -> MenuFragment()
                "Cabang Restoran" -> CabangRestoranFragment()
                "Keranjang" -> KeranjangFragment()
                "Payment Success" -> PaymentSuccessFragment()
                "Pembayaran" -> PembayaranFragment()
                else -> throw IllegalArgumentException("Fragment type undefined")
            }

            fragmentTransaction.add(R.id.container, newFragment, tag)
            Log.d("TAG", "New $tag created")
        } else {
            // existing instance of fragment found
            fragmentTransaction.replace(R.id.container, fragment, tag)
            Log.d("TAG", "Reusing existing $tag")
        }
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }

}