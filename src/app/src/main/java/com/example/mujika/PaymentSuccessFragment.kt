package com.example.mujika

import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.*

class PaymentSuccessFragment : Fragment() {

    private val time = 5000L

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
                val transaction = requireActivity().supportFragmentManager.beginTransaction()
                transaction.replace(R.id.container, MenuFragment())
                transaction.remove(this@PaymentSuccessFragment)
                transaction.commit()
            }
        }
        timer.start()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }

}