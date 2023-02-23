package com.example.mujika

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.Menu
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import androidx.lifecycle.ViewModelProvider
import com.example.mujika.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    private var menu: Menu? = null
    private lateinit var viewModel: ViewModelHolder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(findViewById(R.id.Toolbar))
        binding.bottomNavigation.setOnItemSelectedListener {
            when(it.itemId){
                R.id.menu -> changeFragment("Menu")
                R.id.cabangrestoran -> changeFragment("Cabang Restoran")
                R.id.twibbon -> changeFragment("Twibbon")
                R.id.keranjang -> changeFragment("Keranjang")
            }
            true
        }

        viewModel = ViewModelProvider(this).get(ViewModelHolder::class.java)

        if (savedInstanceState == null) {
            supportActionBar?.title = ""
            changeFragment("Twibbon")
        } else {
            val fragmentTags = viewModel.fragmentStack
            for (tag in fragmentTags) {
                supportFragmentManager.findFragmentByTag(tag)?.let {
                    supportFragmentManager
                        .beginTransaction()
                        .attach(it)
                        .commit()
                }
            }

            viewModel.currentFragmentTag?.let {
                changeFragment(it)
            }

        }
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)

        val fragmentTags = viewModel.fragmentStack
        fragmentTags.clear()
        for (i in 0 until supportFragmentManager.backStackEntryCount) {
            fragmentTags.add(supportFragmentManager.getBackStackEntryAt(i).name!!)
        }
        viewModel.currentFragmentTag = supportFragmentManager.findFragmentById(R.id.container)?.tag
    }
    @SuppressLint("RestrictedApi", "ResourceType")
    public fun changeFragment(tag: String){

        viewModel.currentFragmentTag = tag

        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        var fragment = fragmentManager.findFragmentByTag(tag)

        changeToolbar(tag)

        if (fragment == null) {
            // create new instance according to the tag given
            fragment = when (tag) {
                "Twibbon" -> TwibbonFragment()
                "Menu" -> MenuFragment()
                "Cabang Restoran" -> CabangRestoranFragment()
                "Keranjang" -> KeranjangFragment()
                "Payment Success" -> PaymentSuccessFragment()
                "Pembayaran" -> PembayaranFragment()
                else -> throw IllegalArgumentException("Fragment type undefined")
            }

            fragmentTransaction.add(R.id.container, fragment, tag)
            Log.d("TAG", "New $tag created")
        } else {
            // existing instance of fragment found
            fragmentTransaction.replace(R.id.container, fragment, tag)
            Log.d("TAG", "Reusing existing $tag")
        }

        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()

    }

    private fun changeToolbar(tag: String) {
        val toolbarTitle = findViewById<TextView>(R.id.toolbar_title)

        if (tag == "Menu") {
            toolbarTitle.text = ""
            supportActionBar?.title = tag
            showMenu()
        } else {
            toolbarTitle.text = tag
            supportActionBar?.title = ""
            hideMenu()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val view = supportFragmentManager.findFragmentById(R.id.container)

        changeToolbar(view?.tag.toString())
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_option_bar, menu)
        this.menu = menu
        hideMenu()
        return true
    }

    fun hideMenu() {
        menu?.let {
            it.findItem(R.id.menu_main_setting)?.isVisible = false
            it.findItem(R.id.menu_main_setting2)?.isVisible = false
        }
    }

    fun showMenu() {
        menu?.let {
            it.findItem(R.id.menu_main_setting)?.isVisible = true
            it.findItem(R.id.menu_main_setting2)?.isVisible = true
        }
    }

}