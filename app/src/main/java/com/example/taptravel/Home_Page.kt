package com.example.taptravel

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.MenuItem
import androidx.activity.addCallback
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.navigation.NavController
import com.example.taptravel.databinding.ActivityHomePageBinding
import com.google.firebase.auth.FirebaseAuth

class Home_Page : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityHomePageBinding
    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomePageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarHomePage.toolbar)

        binding.appBarHomePage.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null)
                .setAnchorView(R.id.fab).show()
        }
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_home_page)
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        if (savedInstanceState == null) {
            navController.navigate(R.id.nav_home)
            navView.setCheckedItem(R.id.nav_home)
        }
        navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_home -> {
                    navController.popBackStack()
                    navController.navigate(R.id.nav_home)
                    drawerLayout.closeDrawer(GravityCompat.START)
                    true

                }
                R.id.nav_myaccount -> {
                    navController.popBackStack()
                    navController.navigate(R.id.nav_myaccount)
                    drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }
                R.id.nav_bucketlist -> {
                    navController.popBackStack()
                    navController.navigate(R.id.nav_bucketlist)
                    drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }
                R.id.nav_about -> {
                    navController.popBackStack()
                    navController.navigate(R.id.nav_about)
                    drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }
                R.id.nav_logout -> {
                    handleLogout()
                    drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }else -> false
            }
        }
    }

//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//            // Inflate the menu; this adds items to the action bar if it is present.
//        menuInflater.inflate(R.menu.home__page, menu)
//        return true
//    }


        override fun onSupportNavigateUp(): Boolean {
            val navController = findNavController(R.id.nav_host_fragment_content_home_page)
            return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
        }
    private fun handleLogout() {
        FirebaseAuth.getInstance().signOut()
        startActivity(Intent(this, MainActivity::class.java))
        this.finish()
    }

    override fun onResume() {
        super.onResume()

        drawerLayout = binding.drawerLayout
        val navController: NavController = findNavController(R.id.nav_host_fragment_content_home_page)

        onBackPressedDispatcher.addCallback(this) {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START)
            } else {
                if (navController.currentDestination?.id == R.id.nav_home) {
                    finish()
                } else {
                    navController.navigate(R.id.nav_home)
                }
            }
        }
    }

}


