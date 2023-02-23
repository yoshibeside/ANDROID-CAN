package com.example.mujika

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.*

class PaymentSuccessFragment : Fragment() {

    private val time = 5000L
    private var appBar: AppBarLayout? = null
    private var bottomNav: BottomNavigationView? = null

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_payment_success, container, false)

        val countdownMsg = view.findViewById<TextView>(R.id.countdown)
        val timer = object: CountDownTimer(5000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                countdownMsg.text = "Redirecting to Menu in ${millisUntilFinished/1000 + 1}"
            }

            override fun onFinish() {
                val activity = activity as? MainActivity
                // Menu Fragment should have been created
                activity?.changeFragment("Menu")
            }
        }
        timer.start()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val parentView = (view.parent as? FrameLayout)?.parent as? RelativeLayout
        appBar = parentView?.findViewById<AppBarLayout>(R.id.app_bar)
        bottomNav = parentView?.findViewById<BottomNavigationView>(R.id.bottom_navigation)
    }

    override fun onResume() {
        super.onResume()
        appBar?.visibility = View.INVISIBLE
        bottomNav?.visibility = View.INVISIBLE
    }


    override fun onPause() {
        super.onPause()
        appBar?.visibility = View.VISIBLE
        bottomNav?.visibility = View.VISIBLE
    }

}