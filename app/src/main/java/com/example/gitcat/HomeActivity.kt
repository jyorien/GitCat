package com.example.gitcat

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import okhttp3.MediaType
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

const val ACCESS_TOKEN = "token"
const val ID = "id"
const val USERNAME = "username"

class HomeActivity : AppCompatActivity() {
    private val retrofitClient = AuthService.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        supportActionBar?.setBackgroundDrawable(ColorDrawable(Color.parseColor("#D1A226")))

        val data = intent.data
        getSharedPreferences(ACCESS_TOKEN, Context.MODE_PRIVATE).edit().putString(ACCESS_TOKEN, data?.getQueryParameter("access_token")).apply()

        val json = JSONObject()
        json.put("access_token", data?.getQueryParameter("access_token"))
        val body = RequestBody.create(MediaType.parse("application/json"), json.toString())
        retrofitClient.getUser(body).enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.code() == 200) {
                    getSharedPreferences(ID, Context.MODE_PRIVATE).edit().putInt(ID, response.body()!!.id).apply()
                    getSharedPreferences(USERNAME, Context.MODE_PRIVATE).edit().putString(USERNAME, response.body()!!.username).apply()

                    Intent(this@HomeActivity, GameActivity::class.java).also {
                        startActivity(it)
                    }
                } else {
                    Log.w("hello", response.body().toString())
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                Log.e("hello", t.message ?: "")
            }

        })
    }
}