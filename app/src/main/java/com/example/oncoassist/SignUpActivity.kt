package com.example.oncoassist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.oncoassist.databinding.ActivitySignUpBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import android.util.Log

data class User(
    val username: String,
    val useremail: String,
    val userpass: String
)

class SignUpActivity : AppCompatActivity() {
    private lateinit var suser: EditText
    private lateinit var semail: EditText
    private lateinit var spassword: EditText
    private lateinit var sbutton: Button
    private lateinit var firebase: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        suser = binding.suser
        semail = binding.semail
        spassword = binding.spassword
        sbutton = binding.sbutton

        firebase = FirebaseDatabase.getInstance().getReference("user")

        sbutton.setOnClickListener {
            saveUserData()
            Toast.makeText(this, "Sign-up button clicked", Toast.LENGTH_SHORT).show()
        }
    }

    private fun saveUserData() {
        Log.d("SignUpActivity", "saveUserData started")
        val username = suser.text.toString().trim()
        val useremail = semail.text.toString().trim()
        val userpass = spassword.text.toString().trim()

        if (username.isEmpty()) {
            suser.error = "Please enter name"
            return
        }
        if (useremail.isEmpty()) {
            semail.error = "Please enter email"
            return
        }
        if (userpass.isEmpty()) {
            spassword.error = "Please enter password"
            return
        }

        val userId = firebase.push().key ?: ""
        val user = User(username, useremail, userpass)

        firebase.child(userId).setValue(user)
            .addOnSuccessListener {
                Toast.makeText(this, "Data inserted successfully", Toast.LENGTH_LONG).show()
                suser.text.clear()
                semail.text.clear()
                spassword.text.clear()

                // Start the SignInActivity after successful signup
                startActivity(Intent(this, SignInActivity::class.java))
                finish()
            }
            .addOnFailureListener { err ->
                Toast.makeText(this, "Error ${err.message}", Toast.LENGTH_LONG).show()
            }

    }
}