package com.example.gitcat

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class ShopActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.title = "Shop"
        supportActionBar?.setBackgroundDrawable(ColorDrawable(Color.parseColor("#D1A226")))
        setContentView(R.layout.activity_shop)
    }
}