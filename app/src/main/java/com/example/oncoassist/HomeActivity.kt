package com.example.oncoassist

import android.content.ContentValues.TAG
import android.content.Intent
import android.media.Image
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
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
    private lateinit var btnedit:Button
    private lateinit var hisbtn:ImageButton
    private lateinit var docbtn:ImageButton
    private lateinit var typebtn:ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomepageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize username TextView
        username = findViewById(R.id.username) // Replace R.id.username with the actual ID of your TextView
        btnedit=findViewById(R.id.btnedit)
        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        val displayName = currentUser?.displayName
        val name = intent.getStringExtra("name")
        val uid = intent.getStringExtra("uid")
        Toast.makeText(this, "${uid}", Toast.LENGTH_SHORT).show()
        if (name != null && name.isNotEmpty()) {
            username.text = "$name!"
        } else {
            username.text = "User"
        }

        hisbtn= findViewById(R.id.square1)
        hisbtn.setOnClickListener{
            val intent= Intent(this,historyActivity::class.java)
            startActivity(intent)
        }
        docbtn=findViewById(R.id.square2)
        docbtn.setOnClickListener{
                val intent= Intent(this,Doctor_activity::class.java)
                startActivity(intent)
            }
        typebtn=findViewById(R.id.square4)
        typebtn.setOnClickListener{
            val intent= Intent(this,TypeActivity::class.java)
            startActivity(intent)
        }
        val addBtn = findViewById<ImageButton>(R.id.addbtn)
        addBtn.setOnClickListener {
            Toast.makeText(this, "Add button clicked", Toast.LENGTH_SHORT).show()

            val intent = Intent(this, AdImageActivity::class.java)
            startActivity(intent)
        }
        btnedit.setOnClickListener { view -> onEditProfileButtonClick(view) }
    }
    private fun onEditProfileButtonClick(view: View) {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val intent = Intent(this, editprofile::class.java)
            startActivity(intent)
        } else {
            Toast.makeText(this, "User is not found", Toast.LENGTH_SHORT).show()
        }

    }

    }

