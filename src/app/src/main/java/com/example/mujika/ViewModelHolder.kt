package com.example.mujika

import androidx.lifecycle.ViewModel
import java.util.*

class ViewModelHolder: ViewModel() {
    val fragmentStack = Stack<String>()
    var currentFragmentTag: String? = null
}