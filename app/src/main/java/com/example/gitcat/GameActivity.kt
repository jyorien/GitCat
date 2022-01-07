package com.example.gitcat

import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.gitcat.databinding.ActivityGameBinding

class GameActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGameBinding
    private lateinit var viewModel: FirebaseViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        viewModel = ViewModelProvider(this).get(FirebaseViewModel::class.java)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_game)
        binding.pat.setOnClickListener { playHappyPatCat() }
        binding.catFood.setOnClickListener {  playHappyFedCat() }
        binding.btnRepo.setOnClickListener { goToRepoScreen() }
        binding.btnShop.setOnClickListener { goToShopScreen() }
        Glide.with(this)
            .load(R.drawable.heart_anim)
            .into(binding.heartPlaceholder)
        startSittingAnimation()

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