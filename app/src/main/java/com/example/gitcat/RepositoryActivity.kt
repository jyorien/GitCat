package com.example.gitcat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.databinding.DataBindingUtil
import com.example.gitcat.databinding.ActivityRepositoryBinding
import com.google.android.material.snackbar.Snackbar
import okhttp3.MediaType
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RepositoryActivity : AppCompatActivity() {
    private val retrofitClient = AuthService.getInstance()
    private lateinit var binding: ActivityRepositoryBinding
    private lateinit var adapter: RepositoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_repository)
        supportActionBar?.title = "Choose repositories"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_repository)
        adapter = RepositoryAdapter(this::onAdd)

        binding.recyclerView.adapter = adapter

        val json = JSONObject()
        json.put("username", "zS1L3NT")
        val body = RequestBody.create(MediaType.parse("application/json"), json.toString())
        retrofitClient.getRepos(body).enqueue(object : Callback<List<Repository>> {
            override fun onResponse(
                call: Call<List<Repository>>,
                response: Response<List<Repository>>
            ) {
                adapter.submitList(response.body())
            }

            override fun onFailure(call: Call<List<Repository>>, t: Throwable) {
                throw t
            }

        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        finish()
        return true;
    }

    private fun onAdd(repository: Repository) {
        val json = JSONObject()
        json.put("username", "zS1L3NT")
        json.put("repository", repository.name)
        json.put("access_token", "")
        val body = RequestBody.create(MediaType.parse("application/json"), json.toString())

        retrofitClient.createWebhook(body).enqueue(object : Callback<Any> {
            override fun onResponse(
                call: Call<Any>,
                response: Response<Any>
            ) {
                if (response.code() == 201) {
                    Snackbar.make(
                        binding.root,
                        "Repository added: ${repository.name}",
                        Snackbar.LENGTH_SHORT
                    ).show()
                } else {
                    Snackbar.make(
                        binding.root,
                        "Failed to add repository: ${repository.name}",
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<Any>, t: Throwable) {
                throw t
            }
        })
    }
}