package com.example.myapplication.signup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.myapplication.R
import com.example.myapplication.login.LoginScreen
import com.google.firebase.auth.FirebaseAuth

class Signup : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        val b3:Button=findViewById(R.id.button3)

        val tf1: TextView =findViewById(R.id.link_login)

        val e1:EditText=findViewById(R.id.email)
        val e2:EditText=findViewById(R.id.password)
        val e3:EditText=findViewById(R.id.confirm)

        val auth=FirebaseAuth.getInstance()


        b3.setOnClickListener {

            val name=e1.text.toString()
            val pass=e2.text.toString()
            val confirm=e3.text.toString()

            if(TextUtils.isEmpty(name) || TextUtils.isEmpty(pass) || TextUtils.isEmpty(confirm)){
                Toast.makeText(this,"credentials cannot be empty", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            if(pass.length<6){
                Toast.makeText(this,"Password should have at least 6 character", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            if(pass!=confirm)
            {
                Toast.makeText(this,"Password do not match", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            auth.createUserWithEmailAndPassword(name,pass)
                .addOnCompleteListener {
                    if (it.isSuccessful) {

                        Toast.makeText(
                            this,
                            "Registered Please Login to fill the other details",
                            Toast.LENGTH_LONG
                        ).show()
                        val i = Intent(this, LoginScreen::class.java)
                        startActivity(i)
                    } else {
                        Toast.makeText(
                            this,
                            "Something went wrong ${it.exception?.message}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
        }




        tf1.setOnClickListener {
            val i=Intent(this, LoginScreen::class.java)
            startActivity(i)
        }

    }
}