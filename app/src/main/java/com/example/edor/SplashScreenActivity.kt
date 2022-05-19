package com.example.edor

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.example.edor.activities.Profile
import com.example.edor.auth.Login
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class SplashScreenActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        var splashLogo = findViewById<ImageView>(R.id.iv_tie)

        mAuth = FirebaseAuth.getInstance()

        splashLogo.alpha = 0f
        splashLogo.animate().setDuration(1500).alpha(1f).withEndAction {

            val user : FirebaseUser? = mAuth.currentUser

            // redirect to profile
            if (user != null)
            {
                val i = Intent(this, Profile::class.java)
                startActivity(i)
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                finish()
            }
            else
            {
                val i = Intent(this, Login::class.java)
                startActivity(i)
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                finish()

            }
        }

    }
}