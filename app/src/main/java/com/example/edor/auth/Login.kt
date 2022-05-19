package com.example.edor.auth

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.edor.R
import com.example.edor.activities.Profile
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser


class Login : AppCompatActivity(), View.OnClickListener {

    private lateinit var mAuth: FirebaseAuth
    private lateinit var progressBar: RelativeLayout
    private lateinit var email: EditText
    private lateinit var password: EditText
    private lateinit var register: TextView
    private lateinit var reset: TextView
    private lateinit var signin: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        mAuth = FirebaseAuth.getInstance()

        progressBar = findViewById(R.id.LoginSpinner)
        email = findViewById(R.id.LoginEmailText)
        password = findViewById(R.id.LoginPasswordText)
        signin = findViewById(R.id.LoginButton)
        register = findViewById(R.id.registerLabel)
        reset = findViewById(R.id.resetLabel)

        reset.setOnClickListener(this)
        register.setOnClickListener(this)
        signin.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        if (view != null) {
            when (view.id) {
                R.id.registerLabel -> {
                    val i = Intent(this, Register::class.java)
                    startActivity(i)
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                }

                R.id.LoginButton -> {
                        userLogin()
                }

                R.id.resetLabel -> {
                    val i = Intent(this, Reset::class.java)
                    startActivity(i)
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                }
            }
            true
        }
    }

    private fun userLogin() {
        val userEmail = email.text.toString().trim()
        val userPassword = password.text.toString().trim()

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

        if (userPassword.isEmpty())
        {
            password.setError("Password is Required!")
            password.requestFocus()
            return
        }

        if (userPassword.length < 6){
            password.setError("Password should be more than 6 characters!")
            password.requestFocus()
            return
        }

        progressBar.visibility = View.VISIBLE

        mAuth.signInWithEmailAndPassword(userEmail, userPassword).addOnCompleteListener { Task ->
                if (Task.isSuccessful)
                {

                    val user : FirebaseUser = mAuth.currentUser!!

                    if (user.isEmailVerified)
                    {
                        val i = Intent(this, Profile::class.java)

                        i.putExtra("Email", userEmail)

                        progressBar.visibility = View.GONE

                        startActivity(i)

                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)

                        finish()
                    }
                    else
                    {
                        user.sendEmailVerification()
                        Toast.makeText(this, "Check your email to verify your account!", Toast.LENGTH_LONG).show()
                    }

                }
                else
                {
                    Toast.makeText(this, "Failed to login, Please check your credentials!", Toast.LENGTH_LONG).show()
                }

            progressBar.visibility = View.GONE
        }
    }
}