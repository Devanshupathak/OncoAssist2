package com.example.oncoassist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.oncoassist.databinding.ActivityGetStartedBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.auth.ktx.auth

class GetStartedActivity : AppCompatActivity() {
    private var binding: ActivityGetStartedBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGetStartedBinding.inflate (layoutInflater)
        setContentView(binding?.root)
        binding?.cvGetStarted?.setOnClickListener {
            startActivity (Intent(  this, SignInActivity::class.java))
            finish()
        }
        val auth = Firebase.auth
        if (auth.currentUser!= null) {
            startActivity (Intent( this, MainActivity::class.java))
            finish()
        }
    }


}