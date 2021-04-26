package com.api.tvmaze.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.api.tvmaze.R
import com.api.tvmaze.ui.fragments.HomeFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        setCurrentFragment(HomeFragment())


        bottomNavigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.item_home -> {
                    setCurrentFragment(HomeFragment())
                }
            }
            true
        }
    }

    private fun setCurrentFragment(fragment: Fragment): FragmentTransaction {

        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container_view,
            HomeFragment()
        ).commit()
        transaction.addToBackStack(null)
        return transaction
    }
}


