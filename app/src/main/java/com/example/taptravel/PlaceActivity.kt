package com.example.taptravel

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.taptravel.fragmentPlaceDetails.PlaceFragment

class PlaceActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_place)

        if (savedInstanceState == null) {
            val placeFragment = PlaceFragment().apply {
                arguments = intent.extras
            }
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, placeFragment)
                .commit()
        }
    }
}
