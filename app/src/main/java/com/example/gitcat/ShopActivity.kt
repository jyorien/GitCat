package com.example.gitcat

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.gitcat.databinding.ActivityShopBinding
import com.google.android.material.snackbar.Snackbar

class ShopActivity : AppCompatActivity() {
    private lateinit var viewModel: FirebaseViewModel
    private lateinit var binding: ActivityShopBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_shop)
        viewModel = ViewModelProvider(this).get(FirebaseViewModel::class.java)
        supportActionBar?.title = "Shop"
        supportActionBar?.setBackgroundDrawable(ColorDrawable(Color.parseColor("#D1A226")))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.itemFish.setOnClickListener { buyFish() }
        binding.itemPat.setOnClickListener { buyPat() }
        viewModel.credits.observe(this) {
            val txt = "Credits: $it"
            binding.credits.text = txt
        }
    }

    private fun buyFish() {
        val fishPrice = 2
        if (viewModel.credits.value!! < fishPrice) {
            Snackbar.make(binding.root, "Oops, Not enough credits! Make more commits to get more!", Snackbar.LENGTH_SHORT).show()
            return
        }
        viewModel.decreaseField(quantity = fishPrice, field = CREDITS)
        viewModel.increaseField(quantity = 1, field = FOOD)
        Snackbar.make(binding.root, "Successfully purchased fish!", Snackbar.LENGTH_SHORT).show()
    }

    private fun buyPat() {
        val patPrice = 1
        if (viewModel.credits.value!! < patPrice) {
            Snackbar.make(binding.root, "Oops, Not enough credits! Make more commits to get more!", Snackbar.LENGTH_SHORT).show()
            return
        }
        viewModel.decreaseField(quantity = patPrice, field = CREDITS)
        viewModel.increaseField(quantity = 1, field = PATS)
        Snackbar.make(binding.root, "Successfully purchased pat!", Snackbar.LENGTH_SHORT).show()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        finish()
        return true
    }
}