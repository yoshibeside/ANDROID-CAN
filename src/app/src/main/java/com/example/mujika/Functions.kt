package com.example.mujika

import android.R
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction


public fun changeFragment(fManager: FragmentManager, tag: String, newFrag: Fragment?, containerId: Int) {
    val transaction = fManager.beginTransaction()
    val fragment = fManager.findFragmentByTag(tag)

    if (fragment == null) {
        if (newFrag != null) {
            transaction.replace(containerId, newFrag)
        }
    }
    else {
        transaction.replace(containerId, fragment)
    }
    transaction.commit()
}