package com.example.oncoassist



import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.oncoassist.databinding.ActivityHomepageBinding

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomepageBinding
    private lateinit var username:TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomepageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        username=findViewById<TextView>(R.id.username)
        val addBtn = findViewById<ImageButton>(R.id.addbtn)
        addBtn.setOnClickListener {
            Toast.makeText(this, "Add button clicked", Toast.LENGTH_SHORT).show()

            val intent = Intent(this, AdImageActivity::class.java)
            startActivity(intent)
        }
    }
}
