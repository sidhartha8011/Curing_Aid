package com.example.myapplication.signup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.myapplication.Home.HomeScreen
import com.example.myapplication.R
import com.example.myapplication.data.RegistrationData
import com.example.myapplication.login.LoginScreen
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class SignupContinue : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup_continue)

        val tf1: TextView =findViewById(R.id.link_login1)

        val e1:EditText=findViewById(R.id.mediName)
        val e2:EditText=findViewById(R.id.MedDays)
        val e3:EditText=findViewById(R.id.MedDoses)
        val e4:EditText=findViewById(R.id.signup_days)

        val b1:Button=findViewById(R.id.button4)

        val auth=FirebaseAuth.getInstance().currentUser

        val database=FirebaseDatabase.getInstance().getReference("Users")

        b1.setOnClickListener{

            val name=e1.text.toString()
            val age=e2.text.toString()
            val phnumber=e3.text.toString()
            val days=e4.text.toString()

            if(TextUtils.isEmpty(name) || TextUtils.isEmpty(age) || TextUtils.isEmpty(phnumber)||TextUtils.isEmpty(days)){
                Toast.makeText(this,"credentials cannot be empty", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            val data=RegistrationData(name,age.toInt(),phnumber.toLong(),days.toInt())
            if (auth != null) {
                database.child(auth.uid).setValue(data).addOnCompleteListener {
                    Toast.makeText(this,"Registraion Complete",Toast.LENGTH_LONG).show()
                    val i=Intent(this, HomeScreen::class.java)
                    startActivity(i)
                }
                    .addOnFailureListener{
                        Toast.makeText(
                            this,
                            "Something went wrong ${it.message}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
            }
        }


        tf1.setOnClickListener {
            val i= Intent(this, LoginScreen::class.java)
            startActivity(i)
        }



    }
}