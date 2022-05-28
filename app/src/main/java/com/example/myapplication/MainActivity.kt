package com.example.myapplication
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.myapplication.Home.HomeScreen
import com.example.myapplication.login.LoginScreen
import com.example.myapplication.signup.Signup
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {


        val auth=FirebaseAuth.getInstance()

        if(auth.currentUser!=null){
            val intent=Intent(this,HomeScreen::class.java)
            startActivity(intent)
        }

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val patient:Button=findViewById(R.id.button)
        val pharmacy:Button=findViewById(R.id.button1)

        patient.setOnClickListener {
            val i=Intent(this,LoginScreen::class.java)
            startActivity(i)
        }

        pharmacy.setOnClickListener {
            val i=Intent(this, Signup::class.java)
            startActivity(i)
        }


    }
}
