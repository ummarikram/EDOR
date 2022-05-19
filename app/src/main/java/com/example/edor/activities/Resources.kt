package com.example.edor.activities

import android.app.ProgressDialog
import android.content.ContentValues.TAG
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.edor.R
import com.example.edor.Resource
import com.example.edor.UserInfo
import com.example.edor.dataClasses.User
import com.example.edor.recyclerAdaptors.ResourceRecyclerAdaptor
import com.google.android.gms.tasks.Task
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class Resources : AppCompatActivity() {

    var courses: Array<String> = arrayOf("SMD", "AI", "PDC")
    private var category : String? = null
    private var recyclerLayoutManager: RecyclerView.LayoutManager?=null
    private var recyclerAdapter: RecyclerView.Adapter<ResourceRecyclerAdaptor.ViewHolder>?=null
    private lateinit var recyclerView : RecyclerView
    private lateinit var resourceArrayList : ArrayList<Resource>
    private lateinit var uploadBtn : FloatingActionButton
    private lateinit var fileName : EditText
    private lateinit var progressBar : RelativeLayout
    private var PdfUri: Uri? = null
    private lateinit var databaseReference : DatabaseReference
    private lateinit var storageReference : StorageReference

    private fun selectPDFFile()
    {
        val intent=Intent()
        intent.type="application/pdf"
        intent.action=Intent.ACTION_GET_CONTENT
        pdfActivityResultLauncher.launch(intent)
    }

    private val pdfActivityResultLauncher=registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            PdfUri = result.data!!.data

            uploadPDF(PdfUri!!)
        } else {
            Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show()
        }
    }

    private fun uploadPDF(data: Uri) {

        val filePath = "${category!!}/" + System.currentTimeMillis()+".pdf"

        val reference : StorageReference = storageReference.child(filePath)

        reference.putFile(data).addOnSuccessListener { success ->
            val uri : Task<Uri>  = success.storage.downloadUrl
            while (!uri.isSuccessful);
            val url: Uri = uri.result

            val pdf : Resource = Resource(category,fileName.text.toString().trim(), url.toString())

            databaseReference.child(databaseReference.push().key!!).setValue(pdf)

            Toast.makeText(this, "File Uploaded!", Toast.LENGTH_SHORT).show()

            progressBar.visibility = View.GONE

        }.addOnProgressListener { progress->
               progressBar.visibility = View.VISIBLE
        }
    }

    fun fetchAllFilesByCategory(){

        progressBar.visibility = View.VISIBLE

        databaseReference = FirebaseDatabase.getInstance().getReference(category!!)

        val resourceListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                resourceArrayList.clear()

                for (resources in dataSnapshot.children){
                    val resource = resources.getValue(Resource::class.java)

                    if (resource != null){
                        resourceArrayList.add(resource)
                    }
                }

                recyclerView.adapter?.notifyDataSetChanged()

                progressBar.visibility = View.GONE
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w(TAG, "Resource Info :: Cancelled", databaseError.toException())

                progressBar.visibility = View.GONE
            }
        }

        databaseReference.addValueEventListener(resourceListener)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_resources)

        val bottom_navigation = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        uploadBtn = findViewById(R.id.addResourceBtn)

        fileName = findViewById(R.id.file_name)

        progressBar = findViewById(R.id.UploadSpinner)

        progressBar.visibility = View.GONE

        bottom_navigation.setSelectedItemId(R.id.resources)

        val arrayAdapter = ArrayAdapter(this, R.layout.dropdown_item, courses)

        val autoCompleteTextViewResources = findViewById<AutoCompleteTextView>(R.id.autoCompleteTextViewResources)

        recyclerLayoutManager = LinearLayoutManager(this)

        recyclerView = findViewById<RecyclerView>(R.id.resourceRecyclerView)

        recyclerView.layoutManager = recyclerLayoutManager

        resourceArrayList = arrayListOf<Resource>()

//        resourceArrayList.add(Resource("SMD", "Final Term Spring 2020.pdf"))

        recyclerAdapter= ResourceRecyclerAdaptor(resourceArrayList)

        recyclerView.adapter = recyclerAdapter

        autoCompleteTextViewResources.setAdapter(arrayAdapter)

        uploadBtn.setOnClickListener{

            if (fileName.text.toString().trim() == "")
            {
                fileName.setError("File Name is Required!")
                fileName.requestFocus()
                return@setOnClickListener
            }

            if (category != null) {

                databaseReference = FirebaseDatabase.getInstance().getReference(category!!)

                storageReference = FirebaseStorage.getInstance().getReference(category!!)

                selectPDFFile()
            }
            else{
                Toast.makeText(this, "Please choose a course!",Toast.LENGTH_SHORT).show()
            }
        }

        autoCompleteTextViewResources.setOnItemClickListener {parent, view, position, id ->

            if (category != courses[position]) {
                category = courses[position]

                fetchAllFilesByCategory()
            }
        }

        bottom_navigation.setOnItemSelectedListener {
            when (it.itemId){
                R.id.profile -> {
                    val i = Intent(this, Profile::class.java)
                    startActivity(i)
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                    finish()
                }
                R.id.cgpa_cal -> {
                    val i = Intent(this, Calculator::class.java)
                    startActivity(i)
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                    finish()
                }
                R.id.timetable -> {
                    val i = Intent(this, Timetable::class.java)
                    startActivity(i)
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                    finish()
                }
                R.id.resources -> {
                    true
                }
            }
            true
        }


    }
}