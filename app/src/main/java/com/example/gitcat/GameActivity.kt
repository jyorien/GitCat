package com.example.gitcat

import android.graphics.drawable.AnimationDrawable
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.example.gitcat.databinding.ActivityGameBinding

class GameActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGameBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_game)
        binding.pat.setOnClickListener { playHappyPatCat() }
        binding.catFood.setOnClickListener {  playHappyFedCat() }
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



}