package com.example.practiceoffirebase

import android.app.Dialog
import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.example.practiceoffirebase.databinding.ActivityMainBinding
import com.google.firebase.Firebase
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.firestore

class MainActivity : AppCompatActivity(), RecInterface {

    //--------Declaration---------------
    private var binding: ActivityMainBinding? = null
    var itemData = arrayListOf<ItemData>()
    var myAdapter = MyAdapter(itemData, this)
    var db = Firebase.firestore
    var collectName = "Student data"
    lateinit var linearLayoutManager: LayoutManager

    //--------Declaration---------------


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //--------------Recycler Functionality & layout setup---------

        binding?.recycler?.adapter = myAdapter
        linearLayoutManager =
            LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false)
        binding?.recycler?.layoutManager = linearLayoutManager

        //--------------Recycler Functionality & layout setup---------

        //--------------FireBase Function------------

        db.collection(collectName).addSnapshotListener { snapshots, e ->

            if (e != null) {
                return@addSnapshotListener
            }

            for (snapshot in snapshots!!.documentChanges) {

                val itemModel = convertObject(snapshot.document)
                when (snapshot.type) {
                    DocumentChange.Type.ADDED -> {
                        itemModel.let {
                            itemData.add(it)
                        }
                        Log.e(TAG, "onCreate: ${itemData.size}")
                        Log.e(TAG, "onCreate: $itemData")
                    }

                    DocumentChange.Type.REMOVED -> {
                        val index = getIndex(itemModel)
                        if (index > -1) {
                            itemData.removeAt(index)

                        }


                    }

                    DocumentChange.Type.MODIFIED -> {
                        itemModel.let {
                            val index = getIndex(itemModel)
                            if (index > -1) {
                                itemData.set(index, it)
                            }
                        }
                    }
                }


            }


        }
        myAdapter.notifyDataSetChanged()

        //--------------FireBase Function------------


        //--------------ADD FUNCTION WITH FAB AND ADD BTN-----

        binding?.btnFab?.setOnClickListener {
            Dialog(this).apply {
                setContentView(R.layout.custom_fab_layout)
                window?.setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                var etNAme = findViewById<EditText>(R.id.etName)
                var etClass = findViewById<EditText>(R.id.etClass)
                var etRollNumber = findViewById<EditText>(R.id.etRollNo)
                val btnAdd = findViewById<Button>(R.id.btnAdd)


                btnAdd.setOnClickListener {
                    if (etNAme.text.isNullOrEmpty()) {
                        etNAme.error = "Enter Name"
                    } else if (etClass.text.isNullOrEmpty()) {
                        etClass.error = "Enter Class"
                    } else if (etRollNumber.text.isNullOrEmpty()) {
                        etRollNumber.error = "Enter  Roll Number"
                    } else {
                        val model = ItemData(
                            stuName = etNAme.text.toString(),
                            stuRoll = etRollNumber.text.toString().toInt(),
                            stuClass = etClass.text.toString()


                        )

                        db.collection(collectName).add(model).addOnCompleteListener {
                            if (it.isSuccessful){
                                println("Data Saved, ${it.result}")
                        }else{
                            println("Error in Adding")
                            }
                        }
                        myAdapter.notifyDataSetChanged()
                        dismiss()

                    }
                }
            }.show()


        }

        //--------------ADD FUNCTION WITH FAB AND ADD BTN-----





    }

    //-------------Indexing for Model & itemData--------
    private fun getIndex(itemModel: ItemData): Int {
        var index = -1
        index = itemData.indexOfFirst { element ->
            element.id?.equals(itemModel.id) == true
        }
        return index


    }

    //-------------Indexing for Model & itemData--------

    //-------------firebase convert object-------

    private fun convertObject(document: QueryDocumentSnapshot): ItemData {

        val itemModel: ItemData = document.toObject(ItemData::class.java)
        if (itemModel != null) {
            itemModel.id = document.id ?: ""
        }
        return itemModel

    }
    //-------------firebase convert object-------

    //--------------Update data functionality------

    override fun updateBtn(position: Int) {
        Toast.makeText(this, "$position", Toast.LENGTH_SHORT).show()
        Dialog(this).apply {
            setContentView(R.layout.custom_fab_layout)
            window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

            val nameEt = findViewById<EditText>(R.id.etName)
            val classEt = findViewById<EditText>(R.id.etClass)
            val rollNoEt = findViewById<EditText>(R.id.etRollNo)
            val updateBtn = findViewById<Button>(R.id.btnAdd)

            val className = itemData[position].stuName
            val classData = itemData[position].stuClass
            val rollNoData = itemData[position].stuRoll

            nameEt.setText(className)
            classEt.setText(classData)
            rollNoEt.setText(rollNoData.toString())

            updateBtn.setOnClickListener {
                if (nameEt.text.isNullOrEmpty()){
                    nameEt.error = "Enter Your Name"
                }else if (classEt.text.isNullOrEmpty()){
                    classEt.error = "Enter Your Class"
                }else if (rollNoEt.text.isNullOrEmpty()){
                    rollNoEt.error = "Enter Your Roll Number"
                }else{

                }

            }








        }
    }

    override fun deleteBtn(position: Int) {
        TODO("Not yet implemented")
    }











}