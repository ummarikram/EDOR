package com.ummarikram.edor.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ummarikram.edor.singletons.GradePoints
import com.ummarikram.edor.recyclerAdaptors.GradeRecyclerAdaptor
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.ummarikram.edor.R
import com.ummarikram.edor.dataClasses.Grade

class Calculator : AppCompatActivity() {

    var grades: Array<String> = GradePoints.getGradeArray().toTypedArray()
    private var recyclerLayoutManager: RecyclerView.LayoutManager?=null
    private var recyclerAdapter: RecyclerView.Adapter<GradeRecyclerAdaptor.ViewHolder>?=null
    lateinit var gradeArrayList : ArrayList<Grade>
    var grade: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calculator)

        var creditHours = findViewById<NumberPicker>(R.id.course_credit_hours)
        creditHours.setMaxValue(9);
        creditHours.value = 3
        creditHours.setMinValue(1);
        creditHours.setWrapSelectorWheel(false);

        val arrayAdapter = ArrayAdapter(this, R.layout.dropdown_item, grades)

        val autoCompleteTextViewGrades = findViewById<AutoCompleteTextView>(R.id.autoCompleteTextViewGrades)

        val bottom_navigation = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        val addGrade = findViewById<FloatingActionButton>(R.id.add_grade)

        val checkCGPA= findViewById<FloatingActionButton>(R.id.check_cgpa)

        val course = findViewById<EditText>(R.id.grade_course_name)

        recyclerLayoutManager = LinearLayoutManager(this)

        val recyclerView = findViewById<RecyclerView>(R.id.gradeRecyclerView)

        recyclerView.layoutManager = recyclerLayoutManager

        gradeArrayList = arrayListOf<Grade>()

        recyclerAdapter= GradeRecyclerAdaptor(gradeArrayList)

        recyclerView.adapter = recyclerAdapter

        bottom_navigation.setSelectedItemId(R.id.cgpa_cal)

        autoCompleteTextViewGrades.setAdapter(arrayAdapter)

        autoCompleteTextViewGrades.setOnItemClickListener {parent, view, position, id ->

            grade = grades[position]
        }

        addGrade.setOnClickListener{
            if (course.text.toString() != "" && grade != null && creditHours.value > 0 && creditHours.value < 10)
            {
                gradeArrayList.add(Grade(course.text.toString(), creditHours.value, grade!!))

                recyclerView.adapter?.notifyDataSetChanged()

                Toast.makeText(this, "Added Course : ${course.text}",Toast.LENGTH_SHORT).show()
            }
            else{
                Toast.makeText(this, "Please fill all properties",Toast.LENGTH_SHORT).show()
            }
        }

        checkCGPA.setOnClickListener{
            if (gradeArrayList.size > 0)
            {
                var TotalCreditHours = 0
                var TotalPoints: Double = 0.0

                for (gradeValue in gradeArrayList){

                    TotalCreditHours += gradeValue.creditHour
                    var gradePoint = GradePoints.getGradePoint(gradeValue.grade)!!
                    TotalPoints = TotalPoints + (gradeValue.creditHour * gradePoint)
                }

                Toast.makeText(this, "GPA : ${TotalPoints/TotalCreditHours}",Toast.LENGTH_LONG).show()
            }
            else{
                Toast.makeText(this, "No Courses Added",Toast.LENGTH_SHORT).show()
            }
        }

        bottom_navigation.setOnItemSelectedListener {
            when (it.itemId){
                R.id.profile -> {
                    val i = Intent(this, Profile::class.java)
                    startActivity(i)
                    overridePendingTransition(0,0)
                    finish()
                }
                R.id.cgpa_cal -> {
                    true
                }
                R.id.timetable -> {
                    val i = Intent(this, Timetable::class.java)
                    startActivity(i)
                    overridePendingTransition(0,0)
                    finish()
                }
                R.id.resources -> {
                    val i = Intent(this, Resources::class.java)
                    startActivity(i)
                    overridePendingTransition(0,0)
                    finish()
                }
            }
            true
        }
    }
}