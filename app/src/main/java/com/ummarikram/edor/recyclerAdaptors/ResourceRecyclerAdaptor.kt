package com.ummarikram.edor.recyclerAdaptors

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.ummarikram.edor.R
import com.ummarikram.edor.dataClasses.Resource
import com.google.android.material.floatingactionbutton.FloatingActionButton


class ResourceRecyclerAdaptor(private val resourceList:ArrayList<Resource>): RecyclerView.Adapter<ResourceRecyclerAdaptor.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v= LayoutInflater.from(parent.context).inflate(R.layout.resource_layout,parent,false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var curr = resourceList[position]
        holder.resource.text = curr.name

        holder.download.setOnClickListener{
            val selectedItem: Resource = resourceList.get(position)

            val browserIntent = Intent(Intent.ACTION_VIEW)
            browserIntent.setDataAndType(Uri.parse(selectedItem.url!!), "application/pdf")

            val chooser = Intent.createChooser(browserIntent, "Choose app")
            chooser.flags = Intent.FLAG_ACTIVITY_NEW_TASK // optional

            startActivity(holder.itemView.context, chooser, null)
        }
    }

    override fun getItemCount(): Int {
        return resourceList.size
    }
    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var resource: TextView
        var download: FloatingActionButton

        init {
            resource=itemView.findViewById(R.id.resource_title)
            download=itemView.findViewById(R.id.download_course)
        }

    }

}