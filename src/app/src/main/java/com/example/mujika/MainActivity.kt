package com.example.mujika

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.mujika.databinding.ActivityMainBinding
import roomdb.AppDatabase

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        changeFragment(TwibbonFragment(), "Twibbon")
        binding.bottomNavigation.setOnItemSelectedListener {
                when(it.itemId){
                R.id.menu -> changeFragment(MenuFragment(), "Menu")
                R.id.cabangrestoran -> changeFragment(CabangRestoranFragment(), "Cabang Restoran")
                R.id.twibbon -> changeFragment(TwibbonFragment(), "Twibbon")
                R.id.keranjang -> changeFragment(KeranjangFragment(), "Keranjang")
            }
            true
        }
    }
    private fun changeFragment(fragment : Fragment, string: String){
        setSupportActionBar(findViewById(R.id.Toolbar))
        val tv1: TextView = findViewById(R.id.toolbar_title)
        tv1.text = string
        if (string == "Menu") {
            tv1.text = ""
        }
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.container,fragment)
        fragmentTransaction.commit()
    }
}