package com.example.oncoassist

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
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
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import java.io.File

class historyActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHistoryBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private lateinit var firebaseFirestore:FirebaseFirestore
    private lateinit var img:ImageView

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
            Toast.makeText(this, "${currentUser.uid}", Toast.LENGTH_SHORT).show()

            // Retrieve image URL from the database
            databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val imagesSnapshot = dataSnapshot.child("images") // Assuming the images are stored under a "images" node
                    for (imageSnapshot in imagesSnapshot.children) {
                        val imageUrl = imageSnapshot.getValue(String::class.java)

                        if (imageUrl != null) {
                            // Create a FirebaseStorage instance
                            val storage = FirebaseStorage.getInstance()
                            val imageLayout = LinearLayout(this@historyActivity)
                            imageLayout.orientation = LinearLayout.VERTICAL
                            // Create a storage reference from the URL
                            val storageRef = storage.getReferenceFromUrl(imageUrl)

                            // Download the image into a local file
                            val localFile = File.createTempFile("images", "jpg")
                            storageRef.getFile(localFile)
                                .addOnSuccessListener {
                                    // Image downloaded successfully, load it into ImageView with Picasso
                                    val imageView = ImageView(this@historyActivity)
                                    Picasso.get()
                                        .load(localFile)
                                        .into(imageView)
                                    val reportButton = Button(this@historyActivity)
                                    reportButton.text = "Report"
                                    imageLayout.addView(imageView)
                                    imageLayout.addView(reportButton)
                                    // Add the ImageView to your layout
                                    val imageContainer = findViewById<LinearLayout>(R.id.imageContainer)
                                    imageContainer.addView(imageLayout)
                                }
                                .addOnFailureListener { e ->
                                    // Handle any errors
                                    Log.e(TAG, "Error downloading image: ${e.message}")
                                }
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