package com.ummarikram.edor.recyclerAdaptors

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.ummarikram.edor.dataClasses.Grade
import com.ummarikram.edor.singletons.GradePoints
import com.ummarikram.edor.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

class GradeRecyclerAdaptor(private val gradeList:ArrayList<Grade>): RecyclerView.Adapter<GradeRecyclerAdaptor.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v= LayoutInflater.from(parent.context).inflate(R.layout.grade_layout,parent,false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var curr = gradeList[position]
        holder.course.text = curr.course
        holder.credit.text = "CREDIT HOURS : " + curr.creditHour.toString()
        holder.grade.text = "GRADE : " + curr.grade
        holder.score.text = "SCORE : " + GradePoints.getGradePoint(curr.grade)!!.toString()

        holder.removeCourse.setOnClickListener(View.OnClickListener {
            val removedItem: Grade = gradeList.get(position)

            Toast.makeText(holder.itemView.context, "Removed Course : ${removedItem.course}", Toast.LENGTH_SHORT).show()

            gradeList.removeAt(position) // remove the item from list

            notifyDataSetChanged()
        })

    }

    override fun getItemCount(): Int {
        return gradeList.size
    }
    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var course: TextView
        var credit: TextView
        var grade: TextView
        var score: TextView
        var removeCourse: FloatingActionButton

        init {
            course=itemView.findViewById(R.id.grade_course_title)
            credit=itemView.findViewById(R.id.course_hours)
            grade=itemView.findViewById(R.id.course_grade)
            score=itemView.findViewById(R.id.course_score)
            removeCourse=itemView.findViewById(R.id.grade_remove_course)
        }

    }

}