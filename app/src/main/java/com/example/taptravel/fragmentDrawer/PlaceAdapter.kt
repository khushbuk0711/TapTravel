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
        private val locationImage: ImageView = itemView.findViewById(R.id.place_image)
        private val locationName: TextView = itemView.findViewById(R.id.place_name)
        private val locationCategory: TextView = itemView.findViewById(R.id.place_category)
        private val locationDescription: TextView = itemView.findViewById(R.id.place_description)
        private val wishlistBtn: CheckBox = itemView.findViewById(R.id.wishlist_btn)

        fun bindHardcodedPlace(place: Place) {
            locationName.text = place.name
            locationCategory.text = place.category
            locationDescription.text = place.location
            Glide.with(itemView).load(place.imageUrl).placeholder(R.drawable.placeholder).into(locationImage)
            wishlistBtn.isChecked = wishlistViewModel.isPlaceInWishlist(place.id.toString())
        }

        fun bindApiPlace(place: Data) {
            locationName.text = place.result_object.name
            locationCategory.text = place.result_object.category.name
            locationDescription.text = place.result_object.location_string
            place.result_object.photo.images.original.url.let { imageUrl ->
                Glide.with(itemView)
                    .load(imageUrl)
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.placeholder)
                    .into(locationImage)
            }
            wishlistBtn.isChecked = wishlistViewModel.isPlaceInWishlist(place.result_object.location_id)
        }
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
    result_object.photo.images.original.url,
    result_object.category.name,
    result_object.geo_description,
    isInWishlist
)

