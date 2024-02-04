package com.example.oncoassist

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.example.oncoassist.databinding.ActivityTypesTumorBinding

class TypeActivity:AppCompatActivity(){
    private lateinit var binding:ActivityTypesTumorBinding
    private lateinit var benign: ImageButton
    private lateinit var melignant:ImageButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTypesTumorBinding.inflate(layoutInflater)
        setContentView(binding.root)
        benign=findViewById(R.id.imageView31)
        melignant=findViewById(R.id.imageView6)
        benign.setOnClickListener{
            val intent= Intent(this,BenignActivity::class.java)
            startActivity(intent)
        }
        melignant.setOnClickListener{
            val intent= Intent(this,MalignantActivity::class.java)
            startActivity(intent)
        }

    }

}