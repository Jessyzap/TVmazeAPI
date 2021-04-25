package com.api.tvmaze

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.api.tvmaze.fragments.HomeFragment
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setCurrentFragment(HomeFragment())


        bottom_navigation.setOnNavigationItemSelectedListener {
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
        transaction.replace(R.id.fragment_container_view, HomeFragment()).commit()
        return transaction
    }
}


