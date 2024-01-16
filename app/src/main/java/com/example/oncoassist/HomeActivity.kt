package com.example.oncoassist

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.oncoassist.databinding.HomepageBinding


class HomeActivity: AppCompatActivity() {
    private lateinit var binding: HomepageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = HomepageBinding().inflate(layoutInflater)
        setContentView(binding.root)

        binding.imageView2.setOnClickListener{
            val intent = Intent(this,AddImageActivity::class.java)
            startActivity(intent)
        }
    }


}




