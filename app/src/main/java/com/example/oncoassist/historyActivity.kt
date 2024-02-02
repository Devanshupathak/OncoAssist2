package com.example.oncoassist

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.oncoassist.databinding.ActivityHistoryBinding
import com.example.oncoassist.databinding.ActivityHomepageBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class historyActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHistoryBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        if (currentUser != null) {
            databaseReference = FirebaseDatabase.getInstance().reference
                .child("user")
                .child(currentUser.uid) // Assuming your database node is called "user"

            // Retrieve image URLs from the database
            databaseReference.child("images").addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val imageContainer = findViewById<LinearLayout>(R.id.imageContainer)
                    for (imageSnapshot in dataSnapshot.children) {
                        val imageUrl = imageSnapshot.getValue(String::class.java)

                        if (imageUrl != null) {
                            // Load the image using Glide
                            val imageView = ImageView(this@historyActivity)
                            Glide.with(this@historyActivity)
                                .load(imageUrl)
                                .into(imageView)
                            // Add the ImageView to your layout
                            imageContainer.addView(imageView)
                        }
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Handle database error
                    Log.e(TAG, "Database error: ${databaseError.message}")
                    Toast.makeText(this@historyActivity, "Failed to fetch images", Toast.LENGTH_SHORT).show()
                }
            })
        } else {
            // User is not authenticated
            Log.e(TAG, "User is not authenticated")
            Toast.makeText(this, "User is not authenticated", Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        private const val TAG = "historyActivity"
    }
}