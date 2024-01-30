package com.example.oncoassist

import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.example.oncoassist.databinding.UploadimgBinding
import java.io.File

class AdImageActivity : AppCompatActivity() {
    private lateinit var binding: UploadimgBinding
    private lateinit var imageView8: ImageButton
    private lateinit var pic: ImageView
    private var imageUri: Uri? = null

    private val contract = registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success) {
            // If the picture was taken successfully, update the ImageView with the new image
            pic.setImageURI(null)
            pic.setImageURI(imageUri)
        } else {
            // Handle failure to take picture
            // You may want to show a toast or handle this case in some other way
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = UploadimgBinding.inflate(layoutInflater)
        setContentView(binding.root)

        imageView8 = binding.imageView8
        pic = binding.pic

        imageUri = createImageUri()!!

        imageView8.setOnClickListener {
            if (imageUri != null) {
                try {
                    contract.launch(imageUri)
                } catch (e: Exception) {
                    e.printStackTrace()
                    // Handle any exceptions that occur when launching the camera
                }
            } else {
                // Handle case where imageUri is null
            }
        }
    }

    private fun createImageUri(): Uri? {

        val image = File(applicationContext.filesDir, "camera_photo.png")
        return FileProvider.getUriForFile(applicationContext, "com.example.oncoassist.fileProvider", image)
    }
}
