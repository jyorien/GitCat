package com.example.gitcat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.gitcat.databinding.ActivityMainBinding
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.OAuthProvider
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri


class MainActivity : AppCompatActivity() {
    private val mAuth = FirebaseAuth.getInstance()
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.btnLogin.setOnClickListener { auth() }
    }

    private fun auth(){
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://gitcat.zectan.com/start-oauth"))
        startActivity(browserIntent)
    }

    private fun loginWithGitHub() {
        val provider = OAuthProvider.newBuilder("github.com")

        val pendingResultTask = mAuth.pendingAuthResult
        if (pendingResultTask != null) {
            // There's something already here! Finish the sign-in for your user.
            pendingResultTask
                .addOnSuccessListener(
                    OnSuccessListener {
                        // User is signed in.
                        // IdP data available in
                        // authResult.getAdditionalUserInfo().getProfile().
                        // The OAuth access token can also be retrieved:
                        // authResult.getCredential().getAccessToken().

                    })
                .addOnFailureListener {
                    // Handle failure.
                }
        } else {
            // There's no pending result so you need to start the sign-in flow.
            // See below.

            mAuth
                .startActivityForSignInWithProvider( /* activity= */this, provider.build())
                .addOnSuccessListener { res ->
                    // User is signed in.
                    // IdP data available in
                    // authResult.getAdditionalUserInfo().getProfile().
                    // The OAuth access token can also be retrieved:
                    // authResult.getCredential().getAccessToken().
                    Log.d("hello","extra info: ${res.additionalUserInfo?.profile}")
                    Log.d("hello","extra info: ${res.additionalUserInfo?.username}")
                    Log.d("hello","extra info: ${res.credential?.provider}")
                    Toast.makeText(this, "hello", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener(
                    OnFailureListener {
                        // Handle failure.
                    })
        }

    }

    override fun onResume() {
        super.onResume()
    }
}