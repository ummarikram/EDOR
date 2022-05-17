package com.example.edor

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView

class Resources : AppCompatActivity() {

    var courses: Array<String> = arrayOf("SMD", "AI", "DLD")

    override fun onResume() {
        super.onResume()

        val arrayAdapter = ArrayAdapter(this, R.layout.dropdown_item, courses)

        val autoCompleteTextViewResources = findViewById<AutoCompleteTextView>(R.id.autoCompleteTextViewResources)

        autoCompleteTextViewResources.setAdapter(arrayAdapter)

        autoCompleteTextViewResources.setOnItemClickListener {parent, view, position, id ->

            Toast.makeText(this, "Clicked item : ${courses[position]}",Toast.LENGTH_SHORT).show()

        }

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_resources)

        val bottom_navigation = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        bottom_navigation.setSelectedItemId(R.id.resources)

        val arrayAdapter = ArrayAdapter(this, R.layout.dropdown_item, courses)

        val autoCompleteTextViewResources = findViewById<AutoCompleteTextView>(R.id.autoCompleteTextViewResources)

        autoCompleteTextViewResources.setAdapter(arrayAdapter)

        autoCompleteTextViewResources.setOnItemClickListener {parent, view, position, id ->

            Toast.makeText(this, "Clicked item : ${courses[position]}", Toast.LENGTH_SHORT).show()

        }

        bottom_navigation.setOnItemSelectedListener {
            when (it.itemId){
                R.id.profile -> {
                    val i = Intent(this,Profile::class.java)
                    startActivity(i)
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                }
                R.id.cgpa_cal -> {
                    val i = Intent(this,Calculator::class.java)
                    startActivity(i)
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                }
                R.id.timetable -> {
                    val i = Intent(this,Timetable::class.java)
                    startActivity(i)
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                }
                R.id.resources -> {
                    true
                }
            }
            true
        }


    }
}