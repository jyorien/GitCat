package com.example.gitcat

import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.gitcat.databinding.ActivityGameBinding
import com.google.android.material.snackbar.Snackbar

class GameActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGameBinding
    private lateinit var viewModel: FirebaseViewModel
    var internalPatCount = 0
    var internalFoodCount = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        viewModel = ViewModelProvider(this).get(FirebaseViewModel::class.java)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_game)
        binding.pat.setOnClickListener {
            if (internalPatCount == 0) {
                Snackbar.make(binding.root, "You have no more pats! Make more commits to buy some.", Snackbar.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            decrementCount(PATS, 1)
            playHappyPatCat() }
        binding.catFood.setOnClickListener {
            if (internalFoodCount == 0) {
                Snackbar.make(binding.root, "You have no more food! Make more commits to buy some.", Snackbar.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            decrementCount(FOOD, 1)
            playHappyFedCat() }
        binding.btnRepo.setOnClickListener { goToRepoScreen() }
        binding.btnShop.setOnClickListener { goToShopScreen() }

        subscribeToValues()
        Glide.with(this)
            .load(R.drawable.heart_anim)
            .into(binding.heartPlaceholder)
        startSittingAnimation()

    }
    private fun decrementCount(field: String, quantity: Int) {

        viewModel.decreaseField(field,quantity)
    }
    private fun subscribeToValues() {
        viewModel.credits.observe(this) {
            Log.d("hello","credits $it")
        }

        viewModel.food.observe(this) {
            internalFoodCount = it
            binding.foodCount.text = it.toString()
        }

        viewModel.pats.observe(this) {
            internalPatCount = it
            binding.patCounts.text = it.toString()
        }


    }

    private fun startSittingAnimation() {
        binding.catSprite.setImageResource(R.drawable.cat_sit)
        val catAnimation = binding.catSprite.drawable as AnimationDrawable
        catAnimation.start()
    }

    private fun startHappyMoveAnimation() {
        binding.catSprite.setImageResource(R.drawable.happy_move)
        val catAnimation = binding.catSprite.drawable as AnimationDrawable
        catAnimation.start()
    }

    private fun playHappyFedCat() {
        startHappyMoveAnimation()
        binding.heartPlaceholder.visibility = View.VISIBLE
        val mediaPlayer = MediaPlayer.create(this, R.raw.happy_cat)
        mediaPlayer.start()
        Handler(Looper.getMainLooper()).postDelayed({
            startSittingAnimation()
            binding.heartPlaceholder.visibility = View.GONE
        },1000)
    }

    private fun playHappyPatCat() {
        val mediaPlayer = MediaPlayer.create(this, R.raw.happy_cat)
        mediaPlayer.start()
        binding.heartPlaceholder.visibility = View.VISIBLE
        Handler(Looper.getMainLooper()).postDelayed({
            binding.heartPlaceholder.visibility = View.GONE
        },1000)
    }

    private fun goToRepoScreen() {
        Intent(this, RepositoryActivity::class.java).also {
            startActivity(it)
        }
    }

    private fun goToShopScreen() {
        Intent(this, ShopActivity::class.java).also {
            startActivity(it)
        }
    }


}