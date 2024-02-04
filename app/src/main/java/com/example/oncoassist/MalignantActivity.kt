package com.example.oncoassist

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.oncoassist.databinding.ActivityBenignBinding
import com.example.oncoassist.databinding.ActivityMalignantBinding

class MalignantActivity:AppCompatActivity() {
    private lateinit var binding: ActivityMalignantBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMalignantBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }
}