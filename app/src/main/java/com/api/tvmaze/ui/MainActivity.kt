package com.api.tvmaze.ui

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.api.tvmaze.R
import com.api.tvmaze.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val bottomNavigation = binding.bottomNavigation

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragment_container_view) as NavHostFragment
        navController = navHostFragment.findNavController()

        setupActionBarWithNavController(navController)
        bottomNavigation.setupWithNavController(navController)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return NavigationUI.
        onNavDestinationSelected(item, navController)
                || super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp()
    }

    override fun onBackPressed() {
        super.onBackPressed()

//        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
//        transaction.replace(R.id.fragment_container_view, EpisodeFragment()).commit()

//        val view: View? = null
//
//        val currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_container_view)
//
//        when (currentFragment) {
//            is ShowDetailFragment -> {
//                //view?.findNavController()?.navigate(R.id.action_showDetailFragment_to_homeFragment)
//
//                val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
//                transaction.replace(R.id.fragment_container_view, HomeFragment()).commit()
//            }
//            is EpisodeFragment -> {
//                //view?.findNavController()?.navigate(R.id.action_episodeFragment_to_showDetailFragment)
//            }
//            is EpisodeDetailFragment -> {
//                //view?.findNavController()?.navigate(R.id.action_episodeDetailFragment_to_episodeFragment)
//            }
//        }
    }
}


