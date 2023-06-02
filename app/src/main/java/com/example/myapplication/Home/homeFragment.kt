package com.example.myapplication.Home

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
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
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
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
    private var documentID=""
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



//        val database= FirebaseDatabase.getInstance().getReference("Medicine")

        val ref=db.collection("Medicine")



        val tf1: EditText =view.findViewById(R.id.medicineName)
        val tf2: EditText =view.findViewById(R.id.MedicineDays)
        val tf3: EditText =view.findViewById(R.id.MedicineTimes)
        val tf4:EditText=view.findViewById(R.id.remainingDosage)



        getNextDocument { documentSnapshot ->
            if (documentSnapshot != null) {
                // Update the current document snapshot
                currentDocument = documentSnapshot

                documentID= currentDocument!!.id

                // Update the UI with the document data
                val data = documentSnapshot.data

                val name = data?.get("medName")
                val Days = data?.get("days")
                val Time = data?.get("times")
                val today=data?.get("today")


                if(Days==0){
                   val ref=db.collection(auth!!.uid).document(documentID)

                    ref.delete().addOnSuccessListener {
                        Toast.makeText(context, "Medicine course Done it will deleted", Toast.LENGTH_LONG).show()

                    }
                }


                if (name != null && Days != null && Time != null) {
                    tf1.isEnabled = true
                    tf2.isEnabled = true
                    tf3.isEnabled = true
                    tf4.isEnabled=true

                    tf1.setText(name.toString())
                    tf2.setText(Days.toString())
                    tf3.setText(Time.toString())
                    tf4.setText(today.toString())

                    tf1.isEnabled = false
                    tf2.isEnabled = false
                    tf3.isEnabled = false
                    tf4.isEnabled=false
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

        val b3:Button=view.findViewById(R.id.takeDose)

        b3.setOnClickListener {
            val ref=db.collection(auth!!.uid).document(documentID)


            ref.get().addOnSuccessListener {
                val data=it.data

                if(data!!.get("today").toString().toInt()<data!!.get("times").toString().toInt()){
                    ref.update("today", FieldValue.increment(1))
                        .addOnSuccessListener{
                            val tf4:EditText=view.findViewById(R.id.remainingDosage)
                            tf4.isEnabled=true
                            tf4.setText((tf4.text.toString().toInt()+1).toString())
                            tf4.isEnabled=false

                        }
                    ref.update("Total", FieldValue.increment(1))

                    if((data!!.get("today").toString().toInt()+1)==data!!.get("times").toString().toInt()){
                        Toast.makeText(context, "Done this Meds for today stats will reset 8hr", Toast.LENGTH_LONG).show()


                        updateDocumentAfterDelay(auth.uid,documentID,"days",data!!.get("days").toString().toInt()-1)
                        updateDocumentAfterDelay(auth.uid,documentID,"today",0)
                    }

                }
                else{
                    Toast.makeText(context, "Don't take more Dosage for today", Toast.LENGTH_LONG).show()

                }
            }




        }






        val b2:Button=view.findViewById(R.id.nextHome)

        b2.setOnClickListener {
            getNextDocument { documentSnapshot ->
                if (documentSnapshot != null) {
                    // Update the current document snapshot
                    currentDocument = documentSnapshot
                    documentID= currentDocument!!.id

                    // Update the UI with the document data
                    val data = documentSnapshot.data
                    val name = data?.get("medName")
                    val Days = data?.get("days")
                    val Time = data?.get("times")
                    val today=data?.get("today")

                    if(Days.toString().toInt()==0){
                        val ref=db.collection(auth!!.uid).document(documentID)

                        ref.delete().addOnSuccessListener {
                            Toast.makeText(context, "Medicine course Done it will deleted", Toast.LENGTH_LONG).show()

                        }
                    }

                    if (name != null && Days != null && Time != null) {
                        tf1.isEnabled = true
                        tf2.isEnabled = true
                        tf3.isEnabled = true
                        tf4.isEnabled=true

                        tf1.setText(name.toString())
                        tf2.setText(Days.toString())
                        tf3.setText(Time.toString())
                        tf4.setText(today.toString())

                        tf1.isEnabled = false
                        tf2.isEnabled = false
                        tf3.isEnabled = false
                        tf4.isEnabled=false
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

    private val mHandler = Handler()

    fun updateDocumentAfterDelay(collection: String, document: String, fieldToUpdate: String, newValue: Int) {
        val mRunnable = Runnable {

            val db = FirebaseFirestore.getInstance()
            val documentRef = db.collection(collection).document(document)

            documentRef.update(fieldToUpdate, newValue)
                .addOnFailureListener { e ->

                    Toast.makeText(
                        context,
                        e.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }
        val lockTimeMillis: Long = 8 * 60 * 60 * 1000
        // Schedule the document update after the specified time delay
        mHandler.postDelayed(mRunnable, lockTimeMillis)
    }





}

