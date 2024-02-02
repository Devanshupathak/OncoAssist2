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
            saveUserdata()
            val name = binding.suser.text.toString()
            val email = binding.semail.text.toString()
            val pass = binding.spassword.text.toString()
            val cpass = binding.cpassword.text.toString()

            if (email.isNotEmpty() && pass.isNotEmpty() && cpass.isNotEmpty()) {
                if (pass == cpass) {
                    firebaseAuth.createUserWithEmailAndPassword(email, pass)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                binding.semail.text.clear()
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
                            if(!pass.matches(".*[A-Z].*".toRegex()))
                            {
                                Toast.makeText(this, "Must contain 1 uppercase", Toast.LENGTH_SHORT).show()
                            }
                            if(!pass.matches(".*[a-z].*".toRegex()))
                            {
                                Toast.makeText(this, "Must contain 1 Lowercase", Toast.LENGTH_SHORT).show()
                            }
                            if(!pass.matches(".*[@#/$%^+=&].*".toRegex()))
                            {
                                Toast.makeText(this, "Must contain 1 [@#/$%^+=&]", Toast.LENGTH_SHORT).show()
                            }
                        }
                } else {
                    Toast.makeText(this, "Password is not matching", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Empty fields are not allowed!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun saveUserdata() {
        val name = binding.suser.text.toString()
        val email = binding.semail.text.toString()
        val pass = binding.spassword.text.toString()

        if (email.isNotEmpty() && pass.isNotEmpty()) {
            if (pass == binding.cpassword.text.toString()) {
                firebaseAuth.createUserWithEmailAndPassword(email, pass)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val currentUser = firebaseAuth.currentUser
                            val uid = currentUser?.uid
                            if (uid != null) {
                                val user = database.SignIn(uid, name, email, pass)
                                dbref.child(uid).setValue(user)
                            }
                            binding.semail.text.clear()
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
                Toast.makeText(this, "Password is not matching", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "Empty fields are not allowed!", Toast.LENGTH_SHORT).show()
        }
    }
}
