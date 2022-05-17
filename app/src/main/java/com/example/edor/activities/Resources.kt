package com.example.edor.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.edor.R
import com.example.edor.Resource
import com.example.edor.recyclerAdaptors.ResourceRecyclerAdaptor
import com.google.android.material.bottomnavigation.BottomNavigationView

class Resources : AppCompatActivity() {

    var courses: Array<String> = arrayOf("SMD", "AI", "DLD")
    private var recyclerLayoutManager: RecyclerView.LayoutManager?=null
    private var recyclerAdapter: RecyclerView.Adapter<ResourceRecyclerAdaptor.ViewHolder>?=null
    lateinit var resourceArrayList : ArrayList<Resource>

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

        recyclerLayoutManager = LinearLayoutManager(this)

        val recyclerView = findViewById<RecyclerView>(R.id.resourceRecyclerView)

        recyclerView.layoutManager = recyclerLayoutManager

        resourceArrayList = arrayListOf<Resource>()

        resourceArrayList.add(Resource("SMD", "Final Term Spring 2020.pdf"))

        recyclerAdapter= ResourceRecyclerAdaptor(resourceArrayList)

        recyclerView.adapter = recyclerAdapter

        autoCompleteTextViewResources.setAdapter(arrayAdapter)

        autoCompleteTextViewResources.setOnItemClickListener {parent, view, position, id ->

            Toast.makeText(this, "Clicked item : ${courses[position]}", Toast.LENGTH_SHORT).show()

        }

        bottom_navigation.setOnItemSelectedListener {
            when (it.itemId){
                R.id.profile -> {
                    val i = Intent(this, Profile::class.java)
                    startActivity(i)
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                }
                R.id.cgpa_cal -> {
                    val i = Intent(this, Calculator::class.java)
                    startActivity(i)
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                }
                R.id.timetable -> {
                    val i = Intent(this, Timetable::class.java)
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