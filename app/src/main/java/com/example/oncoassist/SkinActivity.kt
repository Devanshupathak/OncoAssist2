package com.example.oncoassist

import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.oncoassist.ml.Model
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp


class SkinActivity : AppCompatActivity() {

    lateinit var selectBtn: Button
    lateinit var predBtn: Button
    lateinit var resView: TextView
    lateinit var imageView: ImageView
    lateinit var bitmap: Bitmap
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_skin)

        selectBtn = findViewById(R.id.selectBtn)
        predBtn = findViewById(R.id.predictBtn)
        resView = findViewById(R.id.resView)
        imageView = findViewById(R.id.imageView10)

        var labels = application.assets.open("labels.txt").bufferedReader().readLines()

        // image processor
        var imageProcessor = ImageProcessor.Builder()
            .add(ResizeOp(224, 224, ResizeOp.ResizeMethod.BILINEAR))
            .build()

        selectBtn.setOnClickListener {
            var intent = Intent()
            intent.setAction(Intent.ACTION_GET_CONTENT)
            intent.setType("image/*")
            startActivityForResult(intent, 100)
        }
        val model: Model = try {
            Model.newInstance(this)
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Failed to load model", Toast.LENGTH_SHORT).show()
            return
        }
        predBtn.setOnClickListener {
            // Simulate loading an image (replace this with actual image loading logic)
            var tensorImage = TensorImage(DataType.UINT8)
            tensorImage.load(bitmap)

            // Simulate image processing (replace this with actual image processing logic)
            tensorImage = imageProcessor.process(tensorImage)

            // Simulate model inference
            val model = Model.newInstance(this)

            // Create a dummy output array for testing
            val outputFeature0 =
                FloatArray(2) { Math.random().toFloat() } // 7 is the number of classes

            // Find the index with the maximum value in the dummy output array
            var maxIdx = outputFeature0.indices.maxByOrNull { outputFeature0[it] } ?: 0

            // Simulate displaying the result
            resView.setText("Test Label: ${labels[maxIdx]}")

            // Close the model (replace this with actual model closing logic)
            model.close()
        }
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == 100){
            var uri= data?.data
            bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, uri)
            imageView.setImageBitmap(bitmap)
        }
    }

}