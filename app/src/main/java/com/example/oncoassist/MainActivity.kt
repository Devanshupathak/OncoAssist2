package com.example.oncoassist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.oncoassist.databinding.ActivityMainBinding
import com.example.oncoassist.databinding.HomepageBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class MainActivity : AppCompatActivity() {
    private var binding: ActivityMainBinding? = null
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        val homepageBinding = HomepageBinding.inflate(layoutInflater)
        setContentView(homepageBinding.root)

        val signOutButton = homepageBinding.btnSignOut

        auth = Firebase.auth
        signOutButton.setOnClickListener{
            if (auth.currentUser != null) {
                auth.signOut()
                startActivity(Intent(this, GetStartedActivity::class.java))
                finish()
            }
        }
    }
}
