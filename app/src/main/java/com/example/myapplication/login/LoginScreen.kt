package com.example.myapplication.login

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
import com.example.myapplication.signup.Signup
import com.example.myapplication.signup.SignupContinue
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class LoginScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_screen)
        val tf1: TextView =findViewById(R.id.link_signup)

        val e1: EditText =findViewById(R.id.loginid)
        val e2: EditText =findViewById(R.id.loginPass)

        val auth= FirebaseAuth.getInstance()

        val b1: Button =findViewById(R.id.button4)

        b1.setOnClickListener {
            val name = e1.text.toString()
            val pass = e2.text.toString()

            if(TextUtils.isEmpty(name) || TextUtils.isEmpty(pass)){
                Toast.makeText(this,"credentials cannot be empty", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            auth.signInWithEmailAndPassword(name,pass)
                .addOnCompleteListener {
                    if(it.isSuccessful)
                    {
                        val database= FirebaseDatabase.getInstance().getReference("Users")
                        database.child(auth.currentUser!!.uid).get().addOnSuccessListener { dataSnapshot ->
                            if (dataSnapshot.exists()){
                                Toast.makeText(this,"login success", Toast.LENGTH_SHORT).show()
                                val i=Intent(this,HomeScreen::class.java)
                                startActivity(i)
                            }
                            else{
                                val intent= Intent(this, SignupContinue::class.java)
                                startActivity(intent)
                                finish()
                            }
                        }

                    }
                    else{
                        Toast.makeText(this,it.exception?.message, Toast.LENGTH_LONG).show()
                    }
                }
        }


        val textView:TextView=findViewById(R.id.forgot)

        textView.setOnClickListener {
            auth.sendPasswordResetEmail(e1.text.toString())
        }


        tf1.setOnClickListener {
            val i= Intent(this, Signup::class.java)
            startActivity(i)
        }
    }
}