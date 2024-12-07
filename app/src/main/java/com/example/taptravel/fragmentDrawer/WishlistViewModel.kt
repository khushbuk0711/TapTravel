package com.example.taptravel.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.taptravel.SearchApi.PlaceDetails
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration

class WishlistViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val _wishlist = MutableLiveData<List<PlaceDetails>?>()
    val wishlist: LiveData<List<PlaceDetails>?> get() = _wishlist
    private var wishlistListener: ListenerRegistration? = null

    init {
        _wishlist.value = emptyList()
        loadWishlist()
    }

    private fun loadWishlist() {
        val userId = auth.currentUser?.uid
        if (userId == null) {
            Log.e("WishlistViewModel", "User not logged in")
            _wishlist.value = emptyList()
            return
        }

        wishlistListener = db.collection("Wishlist")
            .document("Favourites")
            .collection(userId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e("WishlistViewModel", "Failed to load wishlist: ${error.message}")
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    val wishlistItems = snapshot.documents.mapNotNull { document ->
                        document.toObject(PlaceDetails::class.java)?.apply { id = document.id }
                    }
                    _wishlist.value = wishlistItems
                } else {
                    _wishlist.value = emptyList()
                    Log.d("WishlistViewModel", "No wishlist items found")
                }
            }
    }

    fun isPlaceInWishlist(placeId: String): Boolean {
        return _wishlist.value?.any { it.id == placeId } == true
    }

    fun addToWishlist(place: PlaceDetails) {
        val randomId = place.id ?: return
        val userId = auth.currentUser?.uid ?: return

        db.collection("Wishlist")
            .document("Favourites")
            .collection(userId)
            .document(randomId)
            .set(place)
            .addOnSuccessListener {
                Log.d("WishlistViewModel", "Successfully added to wishlist")
            }
            .addOnFailureListener { exception ->
                Log.e("WishlistViewModel", "Failed to add to wishlist: ${exception.message}")
            }
    }

    fun removeFromWishlist(place: PlaceDetails) {
        val randomId = place.id ?: return
        val userId = auth.currentUser?.uid ?: return

        db.collection("Wishlist")
            .document("Favourites")
            .collection(userId)
            .document(randomId)
            .delete()
            .addOnSuccessListener {
                Log.d("WishlistViewModel", "Successfully removed from wishlist")
            }
            .addOnFailureListener { exception ->
                Log.e("WishlistViewModel", "Failed to remove from wishlist: ${exception.message}")
            }
    }

    override fun onCleared() {
        super.onCleared()
        wishlistListener?.remove()
    }
}
