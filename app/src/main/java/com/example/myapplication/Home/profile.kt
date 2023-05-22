package com.example.myapplication.Home

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import com.example.myapplication.MainActivity
import com.example.myapplication.R
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class profile : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val auth= FirebaseAuth.getInstance().currentUser

        val id= auth?.uid

        val db=Firebase.firestore

        val database1=db.collection("Users")

        val database2=FirebaseDatabase.getInstance().getReference("Medicine")

        val tf1:TextView=view.findViewById(R.id.ProfileName)
        val tf2:TextView=view.findViewById(R.id.profilenumber)
        val tf3:TextView=view.findViewById(R.id.profileEmail)
        val tf4:TextView=view.findViewById(R.id.profiledays)
        val tf5:TextView=view.findViewById(R.id.profileage)


        database1.document(id.toString()).get().addOnSuccessListener {
            val data=it.data
            val name= data?.get("name")
            val age=data?.get("age")
            val phoneno=data?.get("phoneNo")
            val days=data?.get("course")


            tf1.text=name.toString()
            tf2.text=phoneno.toString()
            tf3.text=auth?.email.toString()
            tf4.text=days.toString()
            tf5.text=age.toString()

        }



        val signout:Button=view.findViewById(R.id.signout)

        signout.setOnClickListener {

            FirebaseAuth.getInstance().signOut()
            val i=Intent(context,MainActivity::class.java)
            startActivity(i)

        }

        // Call the function to get the document count in the "users" collection
        getDocumentCount(auth!!.uid) { documentCount ->
            // Use the document count
            val tf:TextView=view.findViewById(R.id.numerOfMedicines)
            tf.text=documentCount.toString()
        }


    }


    fun getDocumentCount(collection: String, onComplete: (Int) -> Unit) {
        val db = FirebaseFirestore.getInstance()
        val collectionRef: CollectionReference = db.collection(collection)

        collectionRef.get()
            .addOnCompleteListener(OnCompleteListener { task: Task<QuerySnapshot> ->
                if (task.isSuccessful) {
                    val querySnapshot = task.result
                    val documentCount = querySnapshot?.size() ?: 0
                    onComplete(documentCount)
                } else {
                    // Handle the error scenario
                    onComplete(0)
                }
            })
    }




}