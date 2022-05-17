package com.example.edor

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ResourceRecyclerAdaptor(private val resourceList:ArrayList< Resource >): RecyclerView.Adapter<ResourceRecyclerAdaptor.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResourceRecyclerAdaptor.ViewHolder {
        val v= LayoutInflater.from(parent.context).inflate(R.layout.resource_layout,parent,false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ResourceRecyclerAdaptor.ViewHolder, position: Int) {
        var curr = resourceList[position]
        holder.resource.text = curr.name

    }

    override fun getItemCount(): Int {
        return resourceList.size
    }
    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var resource: TextView

        init {
            resource=itemView.findViewById(R.id.resource_title)
        }

    }

}