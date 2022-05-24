package com.ummarikram.edor.activities

import android.annotation.SuppressLint
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ummarikram.edor.singletons.DatabaseFactory
import com.ummarikram.edor.R
import com.ummarikram.edor.dataClasses.Schedule
import com.ummarikram.edor.recyclerAdaptors.TimetableRecycleAdaptor
import com.ummarikram.edor.sqlDatabases.TimetableDatabase
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class Timetable : AppCompatActivity(), TimePickerDialog.OnTimeSetListener {

    private var days: Array<String> = arrayOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday")

    private var day: String? = null
    private var startHour = -1
    private var startMinute = -1
    private var endHour = -1
    private var endMinute = -1
    private var setStartTime = false
    private lateinit var start: Button
    private lateinit var end: Button
    private var recyclerLayoutManager: RecyclerView.LayoutManager?=null
    private var recyclerAdapter: RecyclerView.Adapter<TimetableRecycleAdaptor.ViewHolder>?=null
    lateinit var scheduleArrayList : ArrayList<Schedule>
    private lateinit var timetableDatabase: TimetableDatabase

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timetable)

        val bottom_navigation = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        val arrayAdapter = ArrayAdapter(this, R.layout.dropdown_item, days)

        val autoCompleteTextViewTimetable = findViewById<AutoCompleteTextView>(R.id.autoCompleteTextViewTimetable)

        val addTime = findViewById<FloatingActionButton>(R.id.add_time)

        val courseName = findViewById<EditText>(R.id.course_name)

        recyclerLayoutManager = LinearLayoutManager(this)

        val recyclerView = findViewById<RecyclerView>(R.id.scheduleRecyclerView)

        recyclerView.layoutManager = recyclerLayoutManager

        timetableDatabase = DatabaseFactory.getTimetableDatabaseInstance(this)!!

        scheduleArrayList = arrayListOf<Schedule>()

        recyclerAdapter= TimetableRecycleAdaptor(scheduleArrayList)

        recyclerView.adapter=recyclerAdapter

        start = findViewById<Button>(R.id.start_time)

        end = findViewById<Button>(R.id.end_time)

        bottom_navigation.setSelectedItemId(R.id.timetable)

        autoCompleteTextViewTimetable.setAdapter(arrayAdapter)

        autoCompleteTextViewTimetable.setOnItemClickListener {parent, view, position, id ->

            day = days[position]

            try {

                scheduleArrayList.clear()

                scheduleArrayList.addAll(timetableDatabase.getScheduleByDay(day!!, this))

                recyclerView.adapter?.notifyDataSetChanged()
            }
            catch (e: NullPointerException) {
                Toast.makeText(this, "Error : ${e.toString()}",Toast.LENGTH_SHORT).show()
            }

        }

        start.setOnClickListener{

            setStartTime = true
           TimePickerDialog(this, this, startHour, startMinute, true).show()

        }

        end.setOnClickListener{

            if (startHour != -1 && startMinute != -1) {
                setStartTime = false
                TimePickerDialog(this, this, endHour, endMinute, true).show()
            }
            else
            {
                Toast.makeText(this, "Please choose start time first!",Toast.LENGTH_SHORT).show()
            }
        }

        addTime.setOnClickListener{

                if (courseName.text.toString().trim().isEmpty())
                {
                    courseName.setError("Course Name is Required!")
                    courseName.requestFocus()
                    return@setOnClickListener
                }

                if (startHour != -1 && endHour!=-1 && day != null)
                {
                      var schedule = Schedule(day!!, courseName.text.toString().trim(),start.text.toString(), end.text.toString())

                      var status = timetableDatabase.insertSchedule(schedule, this)

                        if(status>-1)
                        {
                            Toast.makeText(this, "Schedule Added",Toast.LENGTH_SHORT).show()

                            scheduleArrayList.clear()

                            scheduleArrayList.addAll(timetableDatabase.getScheduleByDay(day!!, this))

                            recyclerView.adapter?.notifyDataSetChanged()
                        }
                }
                else{
                    Toast.makeText(this, "Please choose a day and time slot!",Toast.LENGTH_SHORT).show()
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
                    val i = Intent(this, Calculator::class.java)
                    startActivity(i)
                    overridePendingTransition(0,0)
                    finish()
                }
                R.id.timetable -> {
                    true
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

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {

        var hours = "$hourOfDay:"
        var minutes = "$minute"

        if (hourOfDay < 10)
        {
            hours = "0$hourOfDay:"
        }

        if (minute < 10)
        {
            minutes = "0$minute"
        }

        if (setStartTime)
        {
            startHour = hourOfDay
            startMinute = minute


            start.text = hours + minutes

            endHour = -1
            endMinute = -1

            end.text = "END TIME"
        }
        else
        {
                if (hourOfDay > startHour || (hourOfDay == startHour && minute > startMinute)) {
                    endHour = hourOfDay
                    endMinute = minute

                    end.text = hours + minutes
                }
                else{
                    Toast.makeText(this, "End time must be after start time!",Toast.LENGTH_SHORT).show()
                }

        }
    }


}