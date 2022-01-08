package com.example.gitcat

import android.animation.ValueAnimator
import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import android.media.MediaPlayer
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.annotation.RequiresApi
import androidx.core.animation.doOnCancel
import androidx.core.animation.doOnEnd
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.gitcat.databinding.ActivityGameBinding
import com.google.android.material.snackbar.Snackbar


class GameActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGameBinding
    private lateinit var viewModel: FirebaseViewModel
    var incrementValue = 0f
    var internalPatCount = 0
    var internalFoodCount = 0

    private var direction: Direction = Direction.RIGHT
    private var position: Int = 0
    private var cancel: (() -> Unit) = {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        viewModel = ViewModelProvider(this).get(FirebaseViewModel::class.java)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_game)
        incrementValue = binding.healthBarNoFill.layoutParams.width / 5f
        binding.pat.setOnClickListener {
            if (viewModel.pats.value == 0) {
                Snackbar.make(
                    binding.root,
                    "You have no more pats! Make more commits to buy some.",
                    Snackbar.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            cancel()
            decrementCount(PATS)
            startSatisfiedCatAnimation()
        }

        viewModel.health.observe(this) {
            val current = it * incrementValue
            val layoutParams = binding.healthBarFill.layoutParams
            layoutParams.width = current.toInt()
            binding.healthBarFill.layoutParams = layoutParams
        }

        binding.catFood.setOnClickListener {
            if (viewModel.food.value == 0) {
                Snackbar.make(
                    binding.root,
                    "You have no more food! Make more commits to buy some.",
                    Snackbar.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            if (binding.healthBarFill.width >= binding.healthBarNoFill.width) {
                Snackbar.make(
                    binding.root,
                    "GitCat's health is full. It fills you with a sense of accomplishment!",
                    Snackbar.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            decrementCount(FOOD)
            cancel()
            startSatisfiedCatAnimation()
            increaseHealth()
        }

        binding.btnRepo.setOnClickListener { goToRepoScreen() }
        binding.btnShop.setOnClickListener { goToShopScreen() }

        subscribeToValues()
        Glide.with(this)
            .load(R.drawable.heart_anim)
            .into(binding.heartPlaceholder)

        animateCatWalk()
    }

    private fun decrementCount(field: String) {
        viewModel.decreaseField(field, 1)
    }

    private fun subscribeToValues() {
        viewModel.credits.observe(this) {
            binding.totalCredits.text = "Credits: $it"
        }

        viewModel.food.observe(this) {
            binding.foodCount.text = it.toString()
        }

        viewModel.pats.observe(this) {
            binding.patCounts.text = it.toString()
        }
    }

    private fun animateCatWalk() {
        var cancelled = false

        binding.catSprite.setImageResource(R.drawable.cat_walk)
        binding.catSprite.rotationY = when (direction) {
            Direction.LEFT -> 0f
            Direction.RIGHT -> 180f
        }
        val catWalkAnimation = binding.catSprite.drawable as AnimationDrawable
        catWalkAnimation.start()

        val distance =
            windowManager.currentWindowMetrics.bounds.width() - binding.catSprite.layoutParams.width
        val animation = when (direction) {
            Direction.LEFT -> ValueAnimator.ofInt(position, 0)
            Direction.RIGHT -> ValueAnimator.ofInt(position, distance)
        }

        cancel = {
            animation.cancel()
            cancelled = true
        }

        animation.duration = ((when (direction) {
            Direction.LEFT -> position.toFloat() / distance
            Direction.RIGHT -> 1 - (position.toFloat() / distance)
        }) * 8000).toLong()
        animation.interpolator = LinearInterpolator()
        animation.addUpdateListener { v ->
            run {
                val value = v.animatedValue as Int
                position = value
                binding.catSprite.translationX = value.toFloat()
                binding.catSprite.requestLayout()
            }
        }
        animation.start()
        animation.doOnCancel {
            cancel = {}
            cancelled = true
        }
        animation.doOnEnd {
            if (cancelled) return@doOnEnd
            direction = when (direction) {
                Direction.LEFT -> Direction.RIGHT
                Direction.RIGHT -> Direction.LEFT
            }

            animateCatWalk()
        }
    }

    private fun increaseHealth() {
        if (viewModel.health.value == 5f) {
            Snackbar.make(
                binding.root,
                "GitCat's health is full. It fills you with a sense of accomplishment!",
                Snackbar.LENGTH_SHORT
            ).show()
        }

        viewModel.increaseField(HEALTH, 1)
    }

    private fun startSatisfiedCatAnimation() {
        // Cat Sit
        binding.catSprite.setImageResource(R.drawable.cat_sit)
        val catSitAnimation = binding.catSprite.drawable as AnimationDrawable
        catSitAnimation.start()

        // Cat Meow
        val mediaPlayer = MediaPlayer.create(this, R.raw.happy_cat)
        mediaPlayer.start()

        // Show Heart Icon
        binding.heartPlaceholder.visibility = View.VISIBLE

        Handler(Looper.getMainLooper()).postDelayed({

            // Cat Wag Tail
            binding.catSprite.setImageResource(R.drawable.cat_wagtail)
            val catWagTailAnimation = binding.catSprite.drawable as AnimationDrawable
            catWagTailAnimation.start()

            Handler(Looper.getMainLooper()).postDelayed({
                // Hide Heart Icon
                binding.heartPlaceholder.visibility = View.GONE
                // Cat Walk
                animateCatWalk()
            }, 1500)
        }, 1000)
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

enum class Direction {
    LEFT,
    RIGHT
}