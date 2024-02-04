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
import com.google.android.gms.common.util.WorkSourceUtil.add
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import android.util.Log


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
        imageView = findViewById(R.id.imageView)

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
            if (::bitmap.isInitialized) {
                try {
                    var tensorImage = TensorImage(DataType.UINT8)
                    tensorImage.load(bitmap)

                    tensorImage = imageProcessor.process(tensorImage)
                    Toast.makeText(this, "Image processed successfully", Toast.LENGTH_SHORT).show()

                    val inputFeature0 =
                        TensorBuffer.createFixedSize(intArrayOf(1, 224, 224, 3), DataType.FLOAT32)
                    inputFeature0.loadBuffer(tensorImage.buffer)
                    Toast.makeText(this, "inputfeature run successfully", Toast.LENGTH_SHORT).show()

                    // Perform inference
                    Toast.makeText(this, "Performing inference...", Toast.LENGTH_SHORT).show()
                    val outputs = model.process(inputFeature0)
                    Toast.makeText(this, "Inference completed", Toast.LENGTH_SHORT).show()

                    // Process the output
                    val outputFeature0 = outputs.outputFeature0AsTensorBuffer.floatArray
                    var maxIdx = outputFeature0.indices.maxByOrNull { outputFeature0[it] } ?: 0
                    // ... (prediction logic)

                    val predictionResult = labels[maxIdx]
                    resView.text = predictionResult
                    Toast.makeText(this, "Prediction: $predictionResult", Toast.LENGTH_SHORT).show()
                } catch (e: Exception) {
                    e.printStackTrace()
                    Toast.makeText(this, "Prediction failed: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
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