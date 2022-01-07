package com.example.gitcat

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider

class ShopActivity : AppCompatActivity() {
    private lateinit var viewModel: FirebaseViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(FirebaseViewModel::class.java)
        supportActionBar?.title = "Shop"
        supportActionBar?.setBackgroundDrawable(ColorDrawable(Color.parseColor("#D1A226")))
        setContentView(R.layout.activity_shop)
    }
}