package com.example.oncoassist

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.example.oncoassist.databinding.ActivityDoctorBinding
import com.example.oncoassist.databinding.ActivityReportBinding

class Doctor_activity:AppCompatActivity(){
    private lateinit var binding:ActivityDoctorBinding
    private lateinit var backbtn:ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDoctorBinding.inflate(layoutInflater)
        setContentView(binding.root)
        backbtn=findViewById(R.id.back)
        backbtn.setOnClickListener{
            val intent= Intent(this,HomeActivity::class.java)
            startActivity(intent)
        }

    }
}