package com.example.oncoassist

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import com.example.oncoassist.databinding.ActivityForgetPasswordBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import android.view.View
import android.widget.Toast


class ForgetPasswordActivity: AppCompatActivity() {
    private var binding: ActivityForgetPasswordBinding? = null
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityForgetPasswordBinding.inflate (layoutInflater)
        setContentView(binding?.root)
        auth= Firebase.auth

        binding?.btnForgotPasswordSubmit?.setOnClickListener { resetPassword() }

    }

    private fun validateForm (email: String): Boolean {
        return when {
            TextUtils.isEmpty(email) && !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                binding?.tilEmailForgetPassword?.error = "Enter valid email address"
                false
            }

            else -> true
        }
    }
    private fun showToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
    private fun resetPassword()
    {
        val email = binding?.etForgotPasswordEmail?.text.toString()
        if (validateForm (email))
        {
            auth.sendPasswordResetEmail (email).addOnCompleteListener { task->
                if (task.isSuccessful)
                {
                    binding?.tilEmailForgetPassword?.visibility = View.GONE
                    binding?.tvSubmitMsg?.visibility = View.VISIBLE
                    binding?.btnForgotPasswordSubmit?.visibility = View. GONE
                }
                else
                {
                    showToast(this,"Can not reset your password.try after sometimes")
                }
            }
        }
    }
    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}