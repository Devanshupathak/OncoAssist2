package com.example.oncoassist

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.oncoassist.databinding.ActivityHistoryBinding
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
                            imageLayout.gravity = Gravity.CENTER
                            // Create a storage reference from the URL
                            val storageRef = storage.getReferenceFromUrl(imageUrl)

                            // Download the image into a local file
                            val localFile = File.createTempFile("images", "jpg")
                            storageRef.getFile(localFile)
                                .addOnSuccessListener {
                                    // Image downloaded successfully, load it into ImageView with Picasso
                                    val imageView = ImageView(this@historyActivity)
                                    val layoutParams = LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.MATCH_PARENT,
                                        LinearLayout.LayoutParams.WRAP_CONTENT
                                    )
                                    layoutParams.width = 400 // Set the desired width in pixels
                                    layoutParams.height = 400
                                    imageView.layoutParams = layoutParams
                                    imageView.adjustViewBounds = true
                                    imageView.scaleType = ImageView.ScaleType.CENTER_CROP
                                    Picasso.get()
                                        .load(localFile)
                                        .into(imageView)
                                    val reportButton = Button(this@historyActivity)
                                    reportButton.text = "Report"
                                    reportButton.backgroundTintList = ContextCompat.getColorStateList(this@historyActivity, R.color.purple_200)
                                    val imageLayout = LinearLayout(this@historyActivity)
                                    imageLayout.orientation = LinearLayout.VERTICAL
                                    imageLayout.addView(imageView)
                                    imageLayout.addView(reportButton)
                                    // Add the ImageView to your layout
                                    val imageContainer = findViewById<LinearLayout>(R.id.imageContainer)
                                    imageContainer.addView(imageLayout)
                                    reportButton.setOnClickListener {
                                        // Start the new activity here
                                        val intent = Intent(this@historyActivity, ReportActivity::class.java)
                                        intent.putExtra("imageUrl", imageUrl)
                                        startActivity(intent)
                                    }
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