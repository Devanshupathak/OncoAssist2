package com.example.oncoassist

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.oncoassist.ForgetPasswordActivity
import com.example.oncoassist.MainActivity
import com.example.oncoassist.SignUpActivity
import com.example.oncoassist.databinding.ActivitySignInBinding
import com.google.firebase.auth.FirebaseAuth

class SignInActivity: AppCompatActivity() {
    private var binding: ActivitySignInBinding? = null
    private val auth = FirebaseAuth.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        binding?.tvRegister?.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
            finish()
        }

        binding?.tvForgotPassword?.setOnClickListener {
            startActivity(Intent(this, ForgetPasswordActivity::class.java))
        }

        binding?.btnSignIn?.setOnClickListener {
            userLogin()
        }
    }

    private fun userLogin() {
        val email = binding?.etSinInEmail?.text?.toString()?.trim() ?: ""
        val password = binding?.etSinInPassword?.text?.toString()?.trim() ?: ""

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show()
            return
        }



        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->   // error line while login
                if (task.isSuccessful) {
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(this, "Oops! Something went wrong", Toast.LENGTH_SHORT).show()
                }


            }
    }


}
