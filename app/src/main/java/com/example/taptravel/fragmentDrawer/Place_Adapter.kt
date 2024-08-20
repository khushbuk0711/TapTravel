package com.example.taptravel.fragmentDrawer

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.taptravel.R
import com.example.taptravel.SearchApi.PlaceClickListener
import com.example.taptravel.search_data.Data
import com.example.taptravel.viewmodel.WishlistViewModel
import com.example.taptravel.SearchApi.PlaceDetails

class PlaceAdapter(
    private val context: Context,
    private val wishlistViewModel: WishlistViewModel,
    var hardcodedPlaces: List<Place>,
    var apiPlaces: List<Data>,
    private val listener: PlaceClickListener
) : RecyclerView.Adapter<PlaceAdapter.PlaceViewHolder>() {

    inner class PlaceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val locationImage: ImageView = itemView.findViewById(R.id.location_image)
        private val locationName: TextView = itemView.findViewById(R.id.location_name)
        private val locationCategory: TextView = itemView.findViewById(R.id.location_category)
        private val locationDescription: TextView = itemView.findViewById(R.id.location_description)
        private val wishlistBtn: CheckBox = itemView.findViewById(R.id.wishlist_btn)

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    if (hardcodedPlaces.isNotEmpty()) {
                        listener.onPlaceClicked(position, false)
                    } else if (apiPlaces.isNotEmpty()) {
                        listener.onPlaceClicked(position, true)
                    }
                }
            }

            wishlistBtn.setOnCheckedChangeListener { _, isChecked ->
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    if (hardcodedPlaces.isNotEmpty()) {
                        val place = hardcodedPlaces[position]
                        place.isInWishlist = isChecked
                        if (isChecked) {
                            wishlistViewModel.addToWishlist(place.toPlaceDetails())
                        } else {
                            wishlistViewModel.removeFromWishlist(place.toPlaceDetails())
                        }
                    } else if (apiPlaces.isNotEmpty()) {
                        val place = apiPlaces[position]
                        place.isInWishlist = isChecked
                        if (isChecked) {
                            wishlistViewModel.addToWishlist(place.toPlaceDetails())
                        } else {
                            wishlistViewModel.removeFromWishlist(place.toPlaceDetails())
                        }
                    }
                }
            }
        }

        fun bindHardcodedPlace(place: Place) {
            locationName.text = place.name
            locationCategory.text = place.category
            locationDescription.text = place.location
            Glide.with(itemView).load(place.imageUrl).placeholder(R.drawable.placeholder).into(locationImage)
            wishlistBtn.isChecked = place.isInWishlist
        }

        fun bindApiPlace(place: Data) {
            locationName.text = place.result_object.name
            locationCategory.text = place.result_object.category.name
            locationDescription.text = place.result_object.location_string
            place.result_object?.photo?.images?.original?.url?.let { imageUrl ->
                Glide.with(itemView)
                    .load(imageUrl)
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.placeholder)
                    .into(locationImage)
            }
            wishlistBtn.isChecked = place.isInWishlist
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.place_item, parent, false)
        return PlaceViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: PlaceViewHolder, position: Int) {
        if (hardcodedPlaces.isNotEmpty()) {
            holder.bindHardcodedPlace(hardcodedPlaces[position])
        } else if (apiPlaces.isNotEmpty()) {
            holder.bindApiPlace(apiPlaces[position])
        }
    }

    override fun getItemCount(): Int {
        return if (hardcodedPlaces.isNotEmpty()) {
            hardcodedPlaces.size
        } else {
            apiPlaces.size
        }
    }
}

fun Place.toPlaceDetails() = PlaceDetails(
    id.toString(), name, imageUrl, category,description, isInWishlist
)

fun Data.toPlaceDetails() = PlaceDetails(
    result_object.location_id,
    result_object.name,
    result_object?.photo?.images?.original?.url,
    result_object.category?.name,
    result_object?.geo_description,
    isInWishlist
)

//package com.example.taptravel.fragmentDrawer
//
//import android.content.Context
//import android.os.Build
//import android.util.Log
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.CheckBox
//import android.widget.TextView
//import android.widget.Toast
//import androidx.annotation.RequiresApi
//import androidx.recyclerview.widget.RecyclerView
//import com.bumptech.glide.Glide
//import com.example.taptravel.SearchApi.PlaceClickListener
//import com.example.taptravel.SearchApi.PlaceDetails
//import com.example.taptravel.R
//import com.example.taptravel.search_data.Data
//import com.google.android.material.imageview.ShapeableImageView
//import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.firestore.FirebaseFirestore
//
//class Place_Adapter(
//    val context: Context,
//    var placesList: List<Data>,
//    var Wishlist: ArrayList<PlaceDetails>
//) : RecyclerView.Adapter<Place_Adapter.PlaceViewHolder>() {
//
//    @RequiresApi(Build.VERSION_CODES.P)
//    class PlaceViewHolder(val itemView: View, private val listener: PlaceClickListener?) : RecyclerView.ViewHolder(itemView) {
//        var placeName: TextView = itemView.findViewById(R.id.location_name)
//        var placeImage: ShapeableImageView = itemView.findViewById(R.id.location_image)
//        var placeDescription: TextView = itemView.findViewById(R.id.location_description)
//        var Wishlist: CheckBox = itemView.findViewById(R.id.wishlist_btn)
//        var placeCategory: TextView = itemView.findViewById(R.id.location_category)
//
//        init {
//            itemView.setOnClickListener {
//                listener?.onPlaceClicked(adapterPosition)
//            }
//        }
//    }
//
//    private var placeListener: PlaceClickListener? = null
//    private val db = FirebaseFirestore.getInstance()
//    private val auth = FirebaseAuth.getInstance()
//    private val uid = auth.currentUser?.uid.toString()
//
//    fun setOnPlaceClickListener(placeListener: PlaceClickListener) {
//        this.placeListener = placeListener
//    }
//
//    @RequiresApi(Build.VERSION_CODES.P)
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceViewHolder {
//        val view = LayoutInflater.from(parent.context).inflate(R.layout.place_item, parent, false)
//        return PlaceViewHolder(view, placeListener)
//    }
//
//    override fun getItemCount(): Int {
//        return placesList.size
//    }
//
//    @RequiresApi(Build.VERSION_CODES.P)
//    override fun onBindViewHolder(holder: PlaceViewHolder, position: Int) {
//        try {
//            val currentItem = placesList[position]
//            Log.i("delCheck", Wishlist.size.toString())
//            var isInWishlist = false
//
//            for (item in Wishlist) {
//                if (item.id == currentItem.result_object.location_id) {
//                    Log.i("checker", item.id.toString())
//                    isInWishlist = true
//                    break
//                }
//            }
//
//            holder.Wishlist.isChecked = isInWishlist
//            holder.placeName.text = currentItem.result_object.name
//            holder.placeDescription.text = currentItem.result_object.location_string
//            holder.placeCategory.text = currentItem.result_object.category.name
//            val placeName = currentItem.result_object.name
//            val placeImage = currentItem.result_object.photo.images.original.url
//            val placeCategory = currentItem.result_object.category.name
//            val placeDescription = currentItem.result_object.geo_description
//
//
//            holder.Wishlist.setOnClickListener {
//                val randomId = placesList[position].result_object.location_id
//                if (holder.Wishlist.isChecked) {
//
//                    val place = PlaceDetails(randomId,placeName, placeImage,placeCategory,placeDescription)
//                    Wishlist.add(place)
//                    db.collection("Wishlist").document("Favourites").collection(uid).document(randomId).set(place)
//                        .addOnSuccessListener {
//                            Toast.makeText(context, "Added to Wishlist", Toast.LENGTH_SHORT).show()
//                            holder.Wishlist.isChecked = true
//                        }
//                } else {
//                    Wishlist.removeIf { it.id == randomId }
//                    db.collection("Wishlist").document("Favourites").collection(uid).document(randomId).delete()
//                        .addOnSuccessListener {
//                            Toast.makeText(context, "Removed from Wishlist", Toast.LENGTH_SHORT).show()
//                        }
//                }
//            }
//
//            if (placeImage.isNotEmpty()) {
//                Glide.with(context)
//                    .load(placeImage)
//                    .placeholder(R.drawable.placeholder)
//                    .error(R.drawable.placeholder)
//                    .into(holder.placeImage)
//            } else {
//                holder.placeImage.setImageResource(R.drawable.placeholder)
//            }
//
//        } catch (e: Exception) {
//            Log.e("Place_Adapter", "Error in onBindViewHolder", e)
//        }
//    }
//}
