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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


       val db=Firebase.firestore

//        val database= FirebaseDatabase.getInstance().getReference("Medicine")

        val ref=db.collection("Medicine")



        val tf1: EditText =view.findViewById(R.id.medicineName)
        val tf2: EditText =view.findViewById(R.id.MedicineDays)
        val tf3: EditText =view.findViewById(R.id.MedicineTimes)

        val auth=FirebaseAuth.getInstance().currentUser


//        database.child(auth?.uid.toString()).child(counter.toString()).get().addOnSuccessListener {
        ref.document(auth!!.uid).get().addOnSuccessListener{
            if(it.exists()) {
                val data=
                val name = it.child("medName").value
                val Days = it.child("days").value
                val Time = it.child("times").value

                tf1.isEnabled = true
                tf2.isEnabled = true
                tf3.isEnabled = true


                tf1.setText(name.toString())
                tf2.setText(Days.toString())
                tf3.setText(Time.toString())

                tf1.isEnabled = false
                tf2.isEnabled = false
                tf3.isEnabled = false

                counter += 1
            }
        }

        val b2:Button=view.findViewById(R.id.nextHome)

        b2.setOnClickListener {
            database.child(auth?.uid.toString()).child(counter.toString()).get()
                .addOnSuccessListener {
                    if (it.exists()) {

                        val name = it.child("medName").value
                        val Days = it.child("days").value
                        val Time = it.child("times").value

                        tf1.isEnabled = true
                        tf2.isEnabled = true
                        tf3.isEnabled = true


                        tf1.setText(name.toString())
                        tf2.setText(Days.toString())
                        tf3.setText(Time.toString())

                        tf1.isEnabled = false
                        tf2.isEnabled = false
                        tf3.isEnabled = false

                        counter += 1
                    } else {
                        Toast.makeText(context, "No More Medicines", Toast.LENGTH_LONG).show()
                    }
                }
        }


        val b1:FloatingActionButton = view.findViewById(R.id.add)

        b1.setOnClickListener{
            fragmentManager?.beginTransaction()
                ?.replace(R.id.fragment_container,NewDoses())
                ?.addToBackStack(null)
                ?.commit()
        }

    }


}

