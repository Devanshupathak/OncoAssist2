package com.example.oncoassist

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.FirebaseDatabase

class editprofile : AppCompatActivity() {

    private lateinit var displayNameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var saveButton: Button
    private lateinit var auth: FirebaseAuth
    private lateinit var numedit:EditText
    private lateinit var editage:EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.editprofile)


        emailEditText = findViewById(R.id.editTextEmail)
        displayNameEditText = findViewById(R.id.ename)
        editage = findViewById(R.id.agedit)
        numedit = findViewById(R.id.num)
        saveButton = findViewById(R.id.buttonSave)
        auth = FirebaseAuth.getInstance()

        val currentUser = auth.currentUser
        if (currentUser != null) {
            displayNameEditText.setText(currentUser.displayName)

            emailEditText.setText(currentUser.email)
            numedit.setText(currentUser.phoneNumber)
        } else {
            // Handle the case where currentUser is null (e.g., user not signed in)
        }

        saveButton.setOnClickListener { saveProfile() }
    }

    private fun saveProfile() {
        val user = auth.currentUser
        val newDisplayName = displayNameEditText.text.toString().trim()
        val newEmail = emailEditText.text.toString().trim()
        val newAge = editage.text.toString().trim()
        val newContactNumber = numedit.text.toString().trim()

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
                        updateUserDetails(user, newAge, newContactNumber,newDisplayName)
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
    private fun updateUserDetails(user: FirebaseUser, newAge: String, newContactNumber: String,newDisplayName:String) {
        // Assuming you have a 'users' collection in your Firebase Realtime Database
        val database = FirebaseDatabase.getInstance()
        val usersRef = database.getReference("user")

        val userDetails = mapOf(
            "oage" to newAge,
            "number" to newContactNumber,
            "name" to newDisplayName
        )

        usersRef.child(user.uid).updateChildren(userDetails)
            .addOnSuccessListener {
                Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to update profile details in database", Toast.LENGTH_SHORT).show()
            }
    }
}
