package com.example.oncoassist

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.example.oncoassist.databinding.ActivityHomepageBinding



class HomeActivity: AppCompatActivity() {
    private lateinit var binding: ActivityHomepageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomepageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val addBtn = findViewById<ImageButton>(R.id.addbtn)
        addBtn.setOnClickListener {
            try {
                Log.d("HomeActivity", "Add button clicked")
                val intent = Intent(this, AddImageActivity::class.java)
                startActivity(intent)
            } catch (e: Exception) {
                Log.e("HomeActivity", "Error starting AddImageActivity", e)
            }
        }


    }


}




