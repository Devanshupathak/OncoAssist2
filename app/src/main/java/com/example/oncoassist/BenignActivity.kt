package com.example.oncoassist

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.example.oncoassist.databinding.ActivityBenignBinding
import com.example.oncoassist.databinding.ActivityTypesTumorBinding

class BenignActivity:AppCompatActivity() {

    private lateinit var binding: ActivityBenignBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBenignBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }
}