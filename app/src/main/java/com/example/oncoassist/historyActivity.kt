package com.example.oncoassist

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.oncoassist.databinding.ActivityHistoryBinding
import com.example.oncoassist.databinding.ActivityHomepageBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class historyActivity: AppCompatActivity() {
    private lateinit var binding: ActivityHistoryBinding
    private lateinit var img:ImageView
    private lateinit var auth: FirebaseAuth
    private lateinit var storageRef: StorageReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        img = findViewById(R.id.his1)
        auth = Firebase.auth
        storageRef = FirebaseStorage.getInstance().reference
        val currentUser = auth.currentUser
        if (currentUser != null) {
            // Construct a reference to the image in storage using the user's ID
            val imageRef = storageRef.child("images/${currentUser.uid}/image.jpg")
            Glide.with(this /* context */)
                .load(imageRef)
                .into(img)
        }else{

        }

    }
}