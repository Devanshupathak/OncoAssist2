package com.example.oncoassist

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.oncoassist.ForgetPasswordActivity
import com.example.oncoassist.MainActivity
import com.example.oncoassist.SignUpActivity
import com.example.oncoassist.databinding.ActivitySignInBinding
import com.google.firebase.auth.FirebaseAuth

class SignInActivity: AppCompatActivity() {
    private lateinit var binding: ActivitySignInBinding
    private val firebaseAuth = FirebaseAuth.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvForgotPassword.setOnClickListener{
            val intent = Intent(this,ForgetPasswordActivity::class.java)
            startActivity(intent)
        }

        binding.tvRegister.setOnClickListener{
            val intent = Intent(this,SignUpActivity::class.java)
            startActivity(intent)
        }

        binding.btnSignIn.setOnClickListener {
            val email = binding.etSinInEmail.text.toString()
            val pass = binding.etSinInPassword.text.toString()
            if (email.isNotEmpty() && pass.isNotEmpty()) {
                firebaseAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener { signInTask ->
                    if (signInTask.isSuccessful) {
                        // Navigate to MainActivity after successful sign-in
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                    } else {
                        Toast.makeText(this, "Sign-in failed: ${signInTask.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(this, "Empty fields are not allowed!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        if (firebaseAuth.currentUser != null) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}