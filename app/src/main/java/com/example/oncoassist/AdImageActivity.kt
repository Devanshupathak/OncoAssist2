package com.example.oncoassist

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.oncoassist.databinding.UploadimgBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.ByteArrayOutputStream

class AdImageActivity : AppCompatActivity() {
    private lateinit var binding: UploadimgBinding
    private lateinit var pic: ImageView
    private lateinit var btn: Button
    private lateinit var upload: Button
    private lateinit var storageReference: StorageReference
    private var imageUri: Uri? = null
    private lateinit var auth: FirebaseAuth

    private val takePictureLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val imageBitmap = result.data?.extras?.get("data") as? Bitmap
                pic.setImageBitmap(imageBitmap) // Display the captured image in the ImageView
                imageUri = getImageUriFromBitmap(imageBitmap)
            }
        }

    private val cameraPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                // Permission is granted, launch the camera intent
                takePictureLauncher.launch(Intent(MediaStore.ACTION_IMAGE_CAPTURE))
            } else {
                // Permission is not granted, handle the scenario where the user denied the permission
                // You can show a message or request the permission again
                Toast.makeText(this, "Camera permission denied", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = UploadimgBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        storageReference = FirebaseStorage.getInstance().reference

        pic = findViewById(R.id.pic)
        btn = findViewById(R.id.btnpic)
        upload = findViewById(R.id.btnupload)

        btn.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.CAMERA
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // Permission is not granted, request the permission
                cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
            } else {
                // Permission is already granted, launch the camera intent
                takePictureLauncher.launch(Intent(MediaStore.ACTION_IMAGE_CAPTURE))
            }
        }

        upload.setOnClickListener {
            if (imageUri != null) {
                uploadImageToFirebaseStorage()
            } else {
                Toast.makeText(this, "No image selected", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getImageUriFromBitmap(bitmap: Bitmap?): Uri? {
        bitmap ?: return null
        val tempUri =
            Uri.parse(MediaStore.Images.Media.insertImage(contentResolver, bitmap, "Title", null))
        return tempUri
    }

    private fun uploadImageToFirebaseStorage() {
        val imagesRef =
            storageReference.child("images/${auth.currentUser?.uid}/${System.currentTimeMillis()}.jpg")

        val bitmap = (pic.drawable as BitmapDrawable).bitmap
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()

        val uploadTask = imagesRef.putBytes(data)
        uploadTask.addOnSuccessListener { taskSnapshot ->
            // Image uploaded successfully
            Toast.makeText(this, "Image uploaded successfully", Toast.LENGTH_SHORT).show()
            val imageUrl = imagesRef.toString()
            // Update the image URL in the database
            updateImageUrlInDatabase(imageUrl)
        }.addOnFailureListener { exception ->
            // Handle unsuccessful uploads
            Toast.makeText(this, "Failed to upload image: ${exception.message}", Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun updateImageUrlInDatabase(imageUrl: String) {
        // Get a reference to the database location where you want to store the image URL
        val currentUser = auth.currentUser
        Toast.makeText(this, "${currentUser?.uid}", Toast.LENGTH_SHORT).show()
        val databaseReference = FirebaseDatabase.getInstance().reference
            .child("user")
            .child(auth.currentUser?.uid ?: "")
            .child("images")
            .push() // Create a new child node under "images" with a unique key
        // Set the image URL as the value for the new child node
        databaseReference.setValue(imageUrl)
    }
}