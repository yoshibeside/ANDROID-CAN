package com.example.mujika

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.mujika.databinding.ActivityMainBinding

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
    private fun changeFragment(newFragment : Fragment?, tag: String){

        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        var fragment = fragmentManager.findFragmentByTag(tag)

        setSupportActionBar(findViewById(R.id.Toolbar))
        val tv1: TextView = findViewById(R.id.toolbar_title)
        tv1.text = tag
        if (tag == "Menu") {
            tv1.text = ""
        }

        if (fragment == null && newFragment != null) {
            // create new instance
            fragmentTransaction.add(R.id.container, newFragment, tag)
            Log.d("TAG", "New $tag created")
        } else {
            if (fragment != null) {
                fragmentTransaction.replace(R.id.container, fragment, tag)
            }
            Log.d("TAG", "Reusing existing $tag")
        }
        fragmentTransaction.commit()
    }
}