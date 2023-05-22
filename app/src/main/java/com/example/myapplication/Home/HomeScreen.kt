package com.example.myapplication.Home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.example.myapplication.R
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_screen)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        val container:FrameLayout=findViewById(R.id.fragment_container)


        supportFragmentManager.beginTransaction().replace(R.id.fragment_container,homeFragment()).commit()


        val bottomNavigationView:BottomNavigationView=findViewById(R.id.bottom_navigation)

        bottomNavigationView.setOnNavigationItemSelectedListener {item ->
            when(item.itemId) {
                R.id.navigation_home -> {
                   setFragment(homeFragment())
                }
                R.id.navigation_Profile -> {
                    setFragment(profile())
                }
                R.id.navigation_dashboard->{
                    setFragment(DashBoard())
                }

            }
            true
        }
    }

    private fun setFragment(fragment: Fragment){
            supportFragmentManager.beginTransaction().replace(R.id.fragment_container,fragment)
                .commit()
    }

}