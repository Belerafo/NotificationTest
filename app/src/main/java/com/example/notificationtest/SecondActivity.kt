package com.example.notificationtest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast

class SecondActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)
        val numberOfCookies = intent.getStringExtra("weird_pineapple")
        val cookies = numberOfCookies?.toInt()
        if (cookies!! < 50){
            Toast.makeText(this, "You get small bonus", Toast.LENGTH_SHORT).show()
        }else{
            Toast.makeText(this, "You get HUGE bonus", Toast.LENGTH_SHORT).show()
        }
    }
}