package com.example.oncoassist



import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.oncoassist.databinding.ActivityHomepageBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import com.google.firebase.firestore.auth.User
import com.example.oncoassist.database
import com.google.firebase.database.DatabaseError


class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomepageBinding
    private lateinit var username:TextView
    private lateinit var auth : FirebaseAuth
    private lateinit var uid:String
    private lateinit var databaseReference:DatabaseReference
    private lateinit var user: User
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomepageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        uid = auth.currentUser?.uid.toString()
        databaseReference=FirebaseDatabase.getInstance().getReference("user")
        if (uid.isNotEmpty()){
            getUserData()
        }

        val addBtn = findViewById<ImageButton>(R.id.addbtn)
        addBtn.setOnClickListener {
            Toast.makeText(this, "Add button clicked", Toast.LENGTH_SHORT).show()

            val intent = Intent(this, AdImageActivity::class.java)
            startActivity(intent)
        }
    }
    private fun getUserData(){
        databaseReference.child(uid).addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val signInData = snapshot.getValue(database.SignIn::class.java)
                if (signInData != null) {
                    Log.d(TAG, "Username retrieved: ${signInData.name}")
                    binding.username.text = signInData.name
                } else {
                    // Handle the case where signInData is null
                }
            }
            override fun onCancelled(error: DatabaseError) {
                // Handle onCancelled event
                // For example:
                Log.e(TAG, "Database error occurred: ${error.message}")
            }
        })
    }
}
