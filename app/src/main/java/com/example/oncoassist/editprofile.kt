package com.example.oncoassist

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest

class editprofile : AppCompatActivity() {

    private lateinit var displayNameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var saveButton: Button
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.editprofile)

        displayNameEditText = findViewById(R.id.editTextDisplayName)
        emailEditText = findViewById(R.id.editTextEmail)
        saveButton = findViewById(R.id.buttonSave)
        auth = FirebaseAuth.getInstance()

        val currentUser = auth.currentUser
        if (currentUser != null) {
            displayNameEditText.setText(currentUser.displayName)
            emailEditText.setText(currentUser.email)
        } else {
            // Handle the case where currentUser is null (e.g., user not signed in)
        }

        saveButton.setOnClickListener { saveProfile() }
    }

    private fun saveProfile() {
        val user = auth.currentUser
        val newDisplayName = displayNameEditText.text.toString().trim()
        val newEmail = emailEditText.text.toString().trim()

        if (newDisplayName.isEmpty() || newEmail.isEmpty()) {
            Toast.makeText(this, "Please enter display name and email", Toast.LENGTH_SHORT).show()
            return
        }

        val profileUpdates = UserProfileChangeRequest.Builder()
            .setDisplayName(newDisplayName)
            .build()

        user?.updateProfile(profileUpdates)?.addOnCompleteListener { profileTask ->
            if (profileTask.isSuccessful) {
                user.updateEmail(newEmail).addOnCompleteListener { emailTask ->
                    if (emailTask.isSuccessful) {
                        Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "Failed to update email", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(this, "Failed to update display name", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
