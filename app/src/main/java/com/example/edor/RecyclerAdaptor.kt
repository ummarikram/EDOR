package com.example.edor

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class RecycleAdaptor(private val scheduleList:ArrayList< Schedule >): RecyclerView.Adapter<RecycleAdaptor.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecycleAdaptor.ViewHolder {
        val v= LayoutInflater.from(parent.context).inflate(R.layout.resource_layout,parent,false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: RecycleAdaptor.ViewHolder, position: Int) {
        var curr=scheduleList[position]
        holder.course.text = curr.course
        holder.startTime.text= curr.startTime
        holder.EndTime.text = curr.endTime

        holder.removeCourse.setOnClickListener(View.OnClickListener {
            val removedItem: Schedule = scheduleList.get(position)

            Toast.makeText(holder.itemView.context, "Removed Course : ${removedItem.course}", Toast.LENGTH_SHORT).show()
            // remove your item from data base
            scheduleList.removeAt(position) // remove the item from list

            var database = TimetableDatabase(holder.itemView.context)

            database.deleteSchedule(removedItem, holder.itemView.context)

            notifyItemRemoved(position) // notify the adapter about the removed item
        })
    }

    override fun getItemCount(): Int {
        return scheduleList.size
    }
    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var course: TextView
        var startTime: TextView
        var EndTime: TextView
        var removeCourse: FloatingActionButton

        init {
            course=itemView.findViewById(R.id.course_title)
            startTime =itemView.findViewById(R.id.startTime)
            EndTime=itemView.findViewById(R.id.endTime)
            removeCourse=itemView.findViewById(R.id.remove_course)
        }

    }

}