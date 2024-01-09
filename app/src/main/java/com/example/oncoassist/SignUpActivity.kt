package com.example.oncoassist

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.oncoassist.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()


        binding.sbutton.setOnClickListener {
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
}
