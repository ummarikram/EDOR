package com.ummarikram.edor.auth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.Toast
import com.ummarikram.edor.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth

class Reset : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth
    private lateinit var progressBar: RelativeLayout
    private lateinit var email: EditText
    private lateinit var reset: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset)

        mAuth = FirebaseAuth.getInstance()
        reset = findViewById(R.id.ResetButton)
        progressBar = findViewById(R.id.ResetSpinner)
        email = findViewById(R.id.ResetEmailText)

        val backBtn = findViewById<FloatingActionButton>(R.id.backButtonReset)

        backBtn.setOnClickListener{
            onBackPressed();
        }

        reset.setOnClickListener{view ->
            resetPassword()
        }
    }

    private fun resetPassword() {

        val userEmail = email.text.toString().trim()

        if (userEmail.isEmpty())
        {
            email.setError("Email is Required!")
            email.requestFocus()
            return
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(userEmail).matches())
        {
            email.setError("Please provide a valid email!")
            email.requestFocus()
            return
        }

        progressBar.visibility = View.VISIBLE

        mAuth.sendPasswordResetEmail(userEmail).addOnCompleteListener { Task ->

            if (Task.isSuccessful)
            {
                Toast.makeText(this, "Check your email for instructions!", Toast.LENGTH_LONG).show()
            }
            else
            {
                Toast.makeText(this, "Failed to verify!", Toast.LENGTH_LONG).show()
            }

            progressBar.visibility = View.GONE
        }
    }
}