package com.example.myapplication.Home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.example.myapplication.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class homeFragment : Fragment() {

    override fun onCreateView(

        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        return inflater.inflate(R.layout.fragment_home, container, false)
    }
    private var currentDocument: DocumentSnapshot? = null
    private val db=Firebase.firestore
    private val auth=FirebaseAuth.getInstance().currentUser
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)




//        val database= FirebaseDatabase.getInstance().getReference("Medicine")

        val ref=db.collection("Medicine")



        val tf1: EditText =view.findViewById(R.id.medicineName)
        val tf2: EditText =view.findViewById(R.id.MedicineDays)
        val tf3: EditText =view.findViewById(R.id.MedicineTimes)



        getNextDocument { documentSnapshot ->
            if (documentSnapshot != null) {
                // Update the current document snapshot
                currentDocument = documentSnapshot

                // Update the UI with the document data
                val data = documentSnapshot.data

                val name = data?.get("medName")
                val Days = data?.get("days")
                val Time = data?.get("times")

                if (name != null && Days != null && Time != null) {
                    tf1.isEnabled = true
                    tf2.isEnabled = true
                    tf3.isEnabled = true

                    tf1.setText(name.toString())
                    tf2.setText(Days.toString())
                    tf3.setText(Time.toString())

                    tf1.isEnabled = false
                    tf2.isEnabled = false
                    tf3.isEnabled = false
                } else {
                    // Handle missing or null values in document data
                    // Display an error or perform appropriate action
                    Toast.makeText(context, "No More Medicines", Toast.LENGTH_LONG).show()
                }
            } else {
                // Handle case when documentSnapshot is null
                Toast.makeText(context, "No Medicines", Toast.LENGTH_LONG).show()
                // Display an error or perform appropriate action
            }
        }


//            if(it.exists()) {
//                val data= it.data
//                val name = data!!.get("medName")
//                val Days = data!!.get("days")
//                val Time = data!!.get("times")
//
//                tf1.isEnabled = true
//                tf2.isEnabled = true
//                tf3.isEnabled = true
//
//
//                tf1.setText(name.toString())
//                tf2.setText(Days.toString())
//                tf3.setText(Time.toString())
//
//                tf1.isEnabled = false
//                tf2.isEnabled = false
//                tf3.isEnabled = false
//
//                counter += 1
//            }
        //}

        val b2:Button=view.findViewById(R.id.nextHome)

        b2.setOnClickListener {
            getNextDocument { documentSnapshot ->
                if (documentSnapshot != null) {
                    // Update the current document snapshot
                    currentDocument = documentSnapshot

                    // Update the UI with the document data
                    val data = documentSnapshot.data

                    val name = data?.get("medName")
                    val Days = data?.get("days")
                    val Time = data?.get("times")

                    if (name != null && Days != null && Time != null) {
                        tf1.isEnabled = true
                        tf2.isEnabled = true
                        tf3.isEnabled = true

                        tf1.setText(name.toString())
                        tf2.setText(Days.toString())
                        tf3.setText(Time.toString())

                        tf1.isEnabled = false
                        tf2.isEnabled = false
                        tf3.isEnabled = false
                    } else {
                        // Handle missing or null values in document data
                        // Display an error or perform appropriate action
                        Toast.makeText(context, "No More Medicines", Toast.LENGTH_LONG).show()
                    }
                } else {
                    // Handle case when documentSnapshot is null
                    Toast.makeText(context, "No Medicines", Toast.LENGTH_LONG).show()
                    // Display an error or perform appropriate action
                }
            }
        }

//            database.child(auth?.uid.toString()).child(counter.toString()).get()
//                .addOnSuccessListener {
//                    if (it.exists()) {
//
//                        val name = it.child("medName").value
//                        val Days = it.child("days").value
//                        val Time = it.child("times").value
//
//                        tf1.isEnabled = true
//                        tf2.isEnabled = true
//                        tf3.isEnabled = true
//
//
//                        tf1.setText(name.toString())
//                        tf2.setText(Days.toString())
//                        tf3.setText(Time.toString())
//
//                        tf1.isEnabled = false
//                        tf2.isEnabled = false
//                        tf3.isEnabled = false
//
//                        counter += 1
//                    } else {
//                        Toast.makeText(context, "No More Medicines", Toast.LENGTH_LONG).show()
//                    }
             //   }
      //  }


        val b1:FloatingActionButton = view.findViewById(R.id.add)

        b1.setOnClickListener{
            fragmentManager?.beginTransaction()
                ?.replace(R.id.fragment_container,NewDoses())
                ?.addToBackStack(null)
                ?.commit()
        }

    }

    private fun getNextDocument(callback: (DocumentSnapshot?) -> Unit) {
        val collectionRef = db.collection(auth!!.uid.toString()) // Replace with your collection name

        if (currentDocument == null) {
            collectionRef.limit(1).get().addOnCompleteListener { task ->
                if (task.isSuccessful && task.result != null) {
                    val documents = task.result!!.documents
                    if (documents.isNotEmpty()) {
                        callback(documents[0])
                    } else {
                        callback(null)
                    }
                } else {
                    callback(null)
                }
            }
        } else {
            val currentDocumentId = currentDocument!!.id

            collectionRef.orderBy(FieldPath.documentId())
                .startAfter(currentDocumentId)
                .limit(1)
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful && task.result != null) {
                        val documents = task.result!!.documents
                        if (documents.isNotEmpty()) {
                            callback(documents[0])
                        } else {
                            // Reached the end of documents, start from the beginning
                            collectionRef.orderBy(FieldPath.documentId())
                                .limit(1)
                                .get()
                                .addOnCompleteListener { newTask ->
                                    if (newTask.isSuccessful && newTask.result != null) {
                                        val newDocuments = newTask.result!!.documents
                                        if (newDocuments.isNotEmpty()) {
                                            callback(newDocuments[0])
                                            Toast.makeText(
                                                context,
                                                "Displaying from the beginning again",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        } else {
                                            callback(null)
                                        }
                                    } else {
                                        callback(null)
                                    }
                                }
                        }
                    } else {
                        callback(null)
                    }
                }
        }
    }




}

