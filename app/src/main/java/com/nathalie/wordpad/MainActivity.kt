package com.nathalie.wordpad

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.nathalie.wordpad.repository.WordRepository

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        pick()
    }

    val names = listOf("cap1", "cap2", "justin", "nathalie", "joel", "yen han", "xiang ze")

    fun pick() {
        Log.d("picked", "Winner\n1. ${names[(0..6).random()]} \n2. ${names[(0..6).random()]} \n3. ${names[(0..6).random()]}")
    }
}