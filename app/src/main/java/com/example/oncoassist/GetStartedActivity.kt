package com.example.oncoassist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.oncoassist.databinding.ActivityGetStartedBinding

class GetStartedActivity : AppCompatActivity() {
    private var binding:ActivityGetStartedBinding?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityGetStartedBinding.inflate (layoutInflater)
        setContentView(binding?.root)
        binding?.cvGetStarted?.setOnClickListener {
            startActivity(Intent(this, SignInActivity::class.java))
            finish()
        }
    }
}