package com.example.gitcat

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

const val TOKEN = "token"
class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        val data = intent.data
        val key = data?.getQueryParameter("access_token")
        getSharedPreferences(TOKEN, Context.MODE_PRIVATE).edit().putString(TOKEN,key).apply()
        val token = getSharedPreferences(TOKEN, Context.MODE_PRIVATE).getString(TOKEN, "")
        Log.d("hello","access token $token")
        Intent(this, GameActivity::class.java).also {
            startActivity(it)
        }
    }
}