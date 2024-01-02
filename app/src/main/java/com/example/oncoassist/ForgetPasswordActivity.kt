package com.example.oncoassist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class ForgetPasswordActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forget_password)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}