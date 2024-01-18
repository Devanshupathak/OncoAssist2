package com.example.oncoassist

import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.example.oncoassist.databinding.ActivityForgetPasswordBinding
import com.example.oncoassist.databinding.AddimageBinding
import java.io.File

    class AddImageActivity: AppCompatActivity() {
        private var binding: AddimageBinding? = null
        lateinit var imageView8: Button
        lateinit var pic:ImageView
        lateinit var imageUri : Uri
        private val  contract = registerForActivityResult(ActivityResultContracts.TakePicture()){
            pic.setImageURI(null)
            pic.setImageURI(imageUri)

        }
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            binding= AddimageBinding.inflate (layoutInflater)
            setContentView(binding?.root)
            imageView8=findViewById(R.id.imageView8)
            pic=findViewById(R.id.pic)
            imageUri=createImageUri()!!
            imageView8.setOnClickListener{
                contract.launch(imageUri)
            }

        }
        private fun createImageUri(): Uri? {
            val image = File(applicationContext.filesDir,"camera_photo.png")
            return FileProvider.getUriForFile(applicationContext,"com.example.oncoassist.fileProvider",image)
        }
    }


