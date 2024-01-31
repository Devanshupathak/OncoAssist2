package com.example.oncoassist

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.oncoassist.databinding.ActivityHomepageBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.example.oncoassist.database
import com.google.firebase.database.DatabaseError
import com.google.firebase.firestore.auth.User

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomepageBinding
    private lateinit var username: TextView
    private lateinit var auth: FirebaseAuth
    private lateinit var uid: String
    private lateinit var databaseReference: DatabaseReference
    private lateinit var user: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomepageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize username TextView
        username = findViewById(R.id.username) // Replace R.id.username with the actual ID of your TextView

        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        val displayName = currentUser?.displayName
        if (displayName != null && displayName.isNotEmpty()) {
            username.text = "$displayName!"
        } else {
            username.text = "User"
        }

        val addBtn = findViewById<ImageButton>(R.id.addbtn)
        addBtn.setOnClickListener {
            Toast.makeText(this, "Add button clicked", Toast.LENGTH_SHORT).show()

            val intent = Intent(this, AdImageActivity::class.java)
            startActivity(intent)
        }
    }


    }

