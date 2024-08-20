package com.example.taptravel.viewmodel

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.taptravel.SearchApi.PlaceDetails
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class WishlistViewModel() : ViewModel() {

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val uid = auth.currentUser?.uid ?: ""
    private val _wishlist = MutableLiveData<List<PlaceDetails>>()
    val wishlist: LiveData<List<PlaceDetails>> get() = _wishlist

    init {
        // Initialize with empty wishlist
        _wishlist.value = emptyList()
        loadWishlist()
    }

    private fun loadWishlist() {
        db.collection("Wishlist").document("Favourites")
            .collection(uid).get()
            .addOnSuccessListener { snapshot ->
                val wishlistItems = mutableListOf<PlaceDetails>()
                for (document in snapshot.documents) {
                    val place = document.toObject(PlaceDetails::class.java)
                    place?.let {
                        wishlistItems.add(it)
                    }
                }
                _wishlist.value = wishlistItems
            }
            .addOnFailureListener {
                // Handle failure
            }
    }

    fun addToWishlist(place: PlaceDetails) {
        val randomId = place.id // Assuming place.id is the unique identifier
        if (randomId != null) {
            db.collection("Wishlist").document("Favourites")
                .collection(uid).document(randomId).set(place)
        }
    }

    fun removeFromWishlist(place: PlaceDetails) {
        val randomId = place.id // Assuming place.id is the unique identifier
        if (randomId != null) {
            db.collection("Wishlist")
                .document("Favourites")
                .collection(uid).document(randomId).delete()
        }
    }
}
