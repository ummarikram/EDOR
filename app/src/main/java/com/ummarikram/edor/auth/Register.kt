package com.ummarikram.edor.auth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.*
import com.ummarikram.edor.R
import com.ummarikram.edor.dataClasses.User
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase

class Register : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth
    private lateinit var register: Button
    private lateinit var fullName: EditText
    private lateinit var university: EditText
    private lateinit var program: EditText
    private lateinit var email: EditText
    private lateinit var password: EditText
    private lateinit var progressBar: RelativeLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        mAuth = FirebaseAuth.getInstance()

        register = findViewById(R.id.registerButton)
        fullName = findViewById(R.id.RegisterNameText)
        university = findViewById(R.id.RegisterUniversityText)
        program = findViewById(R.id.RegisterProgramText)
        email = findViewById(R.id.RegisterEmailText)
        password = findViewById(R.id.RegisterPasswordText)
        progressBar = findViewById(R.id.RegisterSpinner)
        val backBtn = findViewById<FloatingActionButton>(R.id.backButton)

        backBtn.setOnClickListener{
            onBackPressed();
        }

        register.setOnClickListener{
                registerUser()
        }

    }

    private fun registerUser() {
        val userEmail: String = email.text.toString().trim()
        val userUniversity: String = university.text.toString().trim()
        val userProgram: String = program.text.toString().trim()
        val userName: String = fullName.text.toString().trim()
        val userPassword: String = password.text.toString().trim()

        if (userName.isEmpty())
        {
            fullName.setError("Full Name is Required!")
            fullName.requestFocus()
            return
        }

        if (userProgram.isEmpty())
        {
            program.setError("Program is Required!")
            program.requestFocus()
            return
        }

        if (userUniversity.isEmpty())
        {
            university.setError("University is Required!")
            university.requestFocus()
            return
        }

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

        mAuth.createUserWithEmailAndPassword(userEmail,userPassword).addOnCompleteListener { task ->
            if(task.isSuccessful){
                val user = User(userName, userUniversity, userProgram, userEmail)

                FirebaseAuth.getInstance().currentUser?.let { it1 ->
                    FirebaseDatabase.getInstance().getReference("Users")
                        .child(it1.uid).setValue(user).addOnCompleteListener { response ->
                            if (response.isSuccessful){

                                val user : FirebaseUser = mAuth.currentUser!!
                                user.sendEmailVerification()
                                Toast.makeText(this, "Please check your email for verification!", Toast.LENGTH_LONG).show()

                            }
                            else{
                                Toast.makeText(this, "Failed to register!", Toast.LENGTH_LONG).show()
                            }

                        }
                }
            }
            else{
                Toast.makeText(this, "Failed to connect to Firebase!", Toast.LENGTH_LONG).show()
            }

            progressBar.visibility = View.GONE
        }
    }
}
