package com.example.oncoassist

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.oncoassist.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var dbref:DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        dbref = FirebaseDatabase.getInstance().getReference("user")
        binding.sbutton.setOnClickListener {
            val name = binding.suser.text.toString()
            val email = binding.semail.text.toString()
            val pass = binding.spassword.text.toString()
            val cpass = binding.cpassword.text.toString()
            val age = binding.sage.text.toString()

            if (email.isNotEmpty() && pass.isNotEmpty() && cpass.isNotEmpty() && age.isNotEmpty()) {
                if (pass == cpass) {
                    saveUserdata(name, email, pass, age)
                } else {
                    Toast.makeText(this, "Password is not matching", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Empty fields are not allowed!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun saveUserdata(name: String, email: String, pass: String, age: String) {
        val number = binding.number.text.toString()

        if (pass.isNotEmpty()) {
            // Validate password strength here
            if (pass.matches(".*[A-Z].*".toRegex()) &&
                pass.matches(".*[a-z].*".toRegex()) &&
                pass.matches(".*[@#/$%^+=&].*".toRegex())) {
                firebaseAuth.createUserWithEmailAndPassword(email, pass)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val currentUser = firebaseAuth.currentUser
                            val uid = currentUser?.uid
                            if (uid != null) {
                                val user = database.SignIn(uid, name, age, number, email, pass)
                                dbref.child(uid).setValue(user)
                            }
                            binding.semail.text.clear()
                            binding.sage.text.clear()
                            binding.number.text.clear()
                            binding.spassword.text.clear()
                            binding.cpassword.text.clear()
                            startActivity(Intent(this, SignInActivity::class.java))
                            finish()
                        } else {
                            Toast.makeText(
                                this,
                                task.exception?.message ?: "Authentication failed",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            } else {
                Toast.makeText(this, "Password must contain at least 1 uppercase, 1 lowercase, and 1 special character", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "Empty fields are not allowed!", Toast.LENGTH_SHORT).show()
        }
    }
}