package com.example.myapplication.Home

import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.myapplication.R
import com.example.myapplication.data.MedData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class NewDoses : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_new_doses, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val button: Button = view.findViewById(R.id.Add)

        val e1: EditText = view.findViewById(R.id.mediName)
        val e2: EditText = view.findViewById(R.id.MedDays)
        val e3: EditText = view.findViewById(R.id.MedDoses)
      //  val e4: EditText= view.findViewById(R.id.medNumber)

        val auth = FirebaseAuth.getInstance().currentUser

        val db=Firebase.firestore

        button.setOnClickListener {

            val name = e1.text.toString()
            val days = e2.text.toString()
            val times = e3.text.toString()

            if (TextUtils.isEmpty(name) || TextUtils.isEmpty(days) || TextUtils.isEmpty(times)) {
                Toast.makeText(context, "credentials cannot be empty", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

          //  val database = FirebaseDatabase.getInstance().getReference("Medicine")
           val ref=db.collection(auth!!.uid)
            val data = MedData(name, days.toInt(), times.toInt())
            if (auth != null) {

//                database.child(auth.uid).child(e4.text.toString()).setValue(data).addOnCompleteListener {
//                    Toast.makeText(context, "Successfully Added", Toast.LENGTH_LONG).show()
//                    fragmentManager?.beginTransaction()
//                        ?.replace(R.id.fragment_container, homeFragment())
//                        ?.addToBackStack(null)
//                        ?.commit()
//                }
//                    .addOnFailureListener {
//                        Toast.makeText(
//                            context,
//                            "Something went wrong ${it.message}",
//                            Toast.LENGTH_LONG
//                        ).show()
//                    }

                ref.document().set(data).addOnCompleteListener{
                    Toast.makeText(context, "Successfully Added", Toast.LENGTH_LONG).show()
                    fragmentManager?.beginTransaction()
                        ?.replace(R.id.fragment_container, homeFragment())
                        ?.addToBackStack(null)
                        ?.commit()
                }
                    .addOnFailureListener {
                        Toast.makeText(
                            context,
                            "Something went wrong ${it.message}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        }
    }


