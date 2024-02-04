package com.example.oncoassist

import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.oncoassist.databinding.ActivityHistoryBinding
import com.example.oncoassist.databinding.ActivityReportBinding
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import org.tensorflow.lite.DataType
import org.tensorflow.lite.schema.Model
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.io.File

class ReportActivity :AppCompatActivity() {
    lateinit var predBtn: Button
    private lateinit var binding: ActivityReportBinding
    lateinit var reView: TextView
    lateinit var imageView: ImageView
    lateinit var bitmap: Bitmap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReportBinding.inflate(layoutInflater)
        setContentView(binding.root)
        reView = findViewById(R.id.review)
        predBtn = findViewById(R.id.predictBtn)
        var labels = application.assets.open("labels.txt").bufferedReader().readLines()

        // image processor
        var imageProcessor = ImageProcessor.Builder()
            .add(ResizeOp(224, 224, ResizeOp.ResizeMethod.BILINEAR))
            .build()
        val imageUrl = intent.getStringExtra("imageUrl")

        Toast.makeText(this@ReportActivity, "Received image URL: $imageUrl", Toast.LENGTH_SHORT)
            .show()

        val imageView = findViewById<ImageView>(R.id.imageView10)
        if (imageUrl != null) {
            try {
                // Load the image using Picasso or any other method
                val storageRef = FirebaseStorage.getInstance().getReferenceFromUrl(imageUrl)

                // Download the image into a local file
                val localFile = File.createTempFile("images", "jpg")
                storageRef.getFile(localFile)
                    .addOnSuccessListener {
                        // Image downloaded successfully, load it into ImageView with Picasso
                        Picasso.get()
                            .load(localFile)
                            .into(imageView, object : com.squareup.picasso.Callback {
                                override fun onSuccess() {
                                    // Image loaded successfully
                                    // Continue with image processing and model inference
                                    predBtn.setOnClickListener {
                                        try {
                                            // Ensure that bitmap is initialized
                                            if (!::bitmap.isInitialized) {
                                                Toast.makeText(
                                                    this@ReportActivity,
                                                    "Please select an image first",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                                return@setOnClickListener
                                            }

                                            // Simulate loading an image (replace this with actual image loading logic)
                                            var tensorImage = TensorImage(DataType.UINT8)
                                            tensorImage.load(bitmap)

                                            // Simulate image processing (replace this with actual image processing logic)
                                            tensorImage = imageProcessor.process(tensorImage)

                                            // Simulate model inference
                                            val model = com.example.oncoassist.ml.Model.newInstance(this@ReportActivity)

                                            // Create a dummy output array for testing
                                            val outputFeature0 =
                                                FloatArray(2) { Math.random().toFloat() } // 7 is the number of classes

                                            // Find the index with the maximum value in the dummy output array
                                            var maxIdx = outputFeature0.indices.maxByOrNull { outputFeature0[it] } ?: 0

                                            // Simulate displaying the result
                                            val predictedLabel = labels[maxIdx] // Get the predicted label
                                            reView.text = "Predicted Label: $predictedLabel"

                                            // Close the model (replace this with actual model closing logic)
                                            model.close()
                                        } catch (e: Exception) {
                                            Toast.makeText(
                                                this@ReportActivity,
                                                "Error: ${e.message}",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    }
                                }

                                override fun onError(e: Exception?) {
                                    // Handle error loading image
                                    Toast.makeText(
                                        this@ReportActivity,
                                        "Failed to load image: ${e?.message}",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            })
                    }
                    .addOnFailureListener { e ->
                        // Handle any errors
                        Log.e(TAG, "Error downloading image: ${e.message}")
                    }

                // Rest of your code for image processing and model inference
                // ...
            } catch (e: Exception) {
                Toast.makeText(this@ReportActivity, "Error: ${e.message}", Toast.LENGTH_SHORT)
                    .show()
                Log.e(TAG, "Error: ${e.message}", e)
            }
        }
    }
    private fun setBitmapFromUri(uri: Uri?) {
        uri?.let {
            bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, uri)
            imageView.setImageBitmap(bitmap)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == 100 && resultCode == RESULT_OK){
            val uri= data?.data
            uri?.let {
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, uri)
                    imageView.setImageBitmap(bitmap)
                } catch (e: Exception) {
                    Toast.makeText(
                        this@ReportActivity,
                        "Error loading image: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.e(TAG, "Error loading image: ${e.message}", e)
                }
            }
        } else {
            Log.d(TAG, "onActivityResult: Unexpected requestCode or resultCode")
        }
    }

}
