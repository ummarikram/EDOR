package com.example.edor.activities

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.TextView
import com.example.edor.auth.Login
import com.example.edor.R
import com.example.edor.UserInfo
import com.example.edor.dataClasses.User
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*

class Profile : AppCompatActivity() {

    private lateinit var logout : Button
    private lateinit var user : FirebaseUser
    private lateinit var userID : String
    private lateinit var databaseReference : DatabaseReference

    private lateinit var fullName: TextView
    private lateinit var university: TextView
    private lateinit var program: TextView
    private lateinit var email: TextView
    private lateinit var progressBar: RelativeLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_profile)

        fullName = findViewById(R.id.editName)
        university = findViewById(R.id.editUniversity)
        program = findViewById(R.id.editProgram)
        email = findViewById(R.id.editEmail)
        progressBar = findViewById(R.id.ProfileSpinner)

        progressBar.visibility = View.VISIBLE

        val bottom_navigation = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        logout = findViewById(R.id.logout)

        logout.setOnClickListener{
            FirebaseAuth.getInstance().signOut()
            UserInfo.setName(null)
            UserInfo.setUniversity(null)
            UserInfo.setEmail(null)
            UserInfo.setProgram(null)

            progressBar.visibility = View.GONE

            val i = Intent(this, Login::class.java)
            startActivity(i)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            finish()
        }

        user = FirebaseAuth.getInstance().currentUser!!
        databaseReference = FirebaseDatabase.getInstance().getReference("Users")
        userID = user.uid

        val profileListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                val userProfile = dataSnapshot.getValue(User::class.java)

                if (userProfile != null){
                    fullName.text = userProfile.name
                    university.text = userProfile.university
                    email.text = userProfile.email
                    program.text = userProfile.program

                    UserInfo.setName(userProfile.name)
                    UserInfo.setUniversity(userProfile.university)
                    UserInfo.setEmail(userProfile.email)
                    UserInfo.setProgram(userProfile.program)
                }

                progressBar.visibility = View.GONE
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w(TAG, "Profile Info :: Cancelled", databaseError.toException())

                progressBar.visibility = View.GONE
            }
        }

        if (UserInfo.getName() == null) {
            databaseReference.child(userID).addListenerForSingleValueEvent(profileListener)
        }
        else
        {
            fullName.text = UserInfo.getName()
            university.text = UserInfo.getUniversity()
            email.text = UserInfo.getEmail()
            program.text = UserInfo.getProgram()

            progressBar.visibility = View.GONE
        }

        bottom_navigation.setOnItemSelectedListener {
            when (it.itemId){
                R.id.profile -> {
                    true
                }
                R.id.cgpa_cal -> {
                    val i = Intent(this, Calculator::class.java)
                    startActivity(i)
                    overridePendingTransition(0,0)
                    finish()
                    true
                }
                R.id.timetable -> {
                    val i = Intent(this, Timetable::class.java)
                    startActivity(i)
                    overridePendingTransition(0,0)
                    finish()
                    true
                }
                R.id.resources -> {
                    val i = Intent(this, Resources::class.java)
                    startActivity(i)
                    overridePendingTransition(0,0)
                    finish()
                    true
                }
            }
            false
        }
    }
}