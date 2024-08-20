package com.example.taptravel

import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import com.google.firebase.auth.FirebaseUser
import com.bumptech.glide.Glide
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import com.example.taptravel.fragmentDrawer.HomeFragment

class Home_Page : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityHomePageBinding
    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomePageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarHomePage.toolbar)

//        binding.appBarHomePage.fab.setOnClickListener { view ->
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                .setAction("Action", null)
//                .setAnchorView(R.id.fab).show()
//        }
        drawerLayout = binding.drawerLayout
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
                }

                else -> false
            }
        }

        loadUserProfile()
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                handleBackNavigation()
            }
        })

    }

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
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                handleBackNavigation()
            }
        })



    }
    private fun handleBackNavigation() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            val navController = findNavController(R.id.nav_host_fragment_content_home_page)
            val currentFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_content_home_page)

            Log.d("Home_Page", "Current Fragment: ${currentFragment?.javaClass?.simpleName}")

            if (currentFragment is HomeFragment) {
                if (currentFragment.hardcodedPlacesVisible) {
                    Log.d("Home_Page", "Hardcoded places visible, finishing activity")
                    finish()
                } else {
                    Log.d("Home_Page", "Loading hardcoded places")
                    currentFragment.hardcodedPlacesVisible = true
                    currentFragment.searchPlace.setQuery("", false)
                    currentFragment.searchPlace.clearFocus()
                    currentFragment.loadHardcodedPlaces()
                }
            } else {
                // Use popBackStack() to handle back navigation properly
                if (!navController.popBackStack(R.id.nav_home, false)) {
                    Log.d("Home_Page", "Navigating to HomeFragment")
                    navController.navigate(R.id.nav_home)
                }
            }
        }
    }


    fun loadUserProfile() {
        val navView: NavigationView = binding.navView
        val headerView = navView.getHeaderView(0)
        val profileImageView: ImageView = headerView.findViewById(R.id.imageView)
        val usernameTextView: TextView = headerView.findViewById(R.id.user_name)
        val emailTextView: TextView = headerView.findViewById(R.id.user_email)

        val currentUser: FirebaseUser? = FirebaseAuth.getInstance().currentUser

        currentUser?.let { user ->
            usernameTextView.text = user.displayName ?: ""
            emailTextView.text = user.email ?: "user@example.com"
            user.photoUrl?.let { photoUrl ->
                Glide.with(this)
                    .load(photoUrl)
                    .placeholder(R.mipmap.ic_launcher)// Placeholder image if none is available
                    .into(profileImageView)
            }
        }
    }
}

