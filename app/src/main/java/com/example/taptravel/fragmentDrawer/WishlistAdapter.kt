package com.example.taptravel.fragmentDrawer

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.taptravel.R
import com.example.taptravel.SearchApi.PlaceDetails
import com.example.taptravel.viewmodel.WishlistViewModel

class WishlistAdapter(
    private val context: Context,
    private var wishlistViewModel: WishlistViewModel,
    private var placeList: MutableList<PlaceDetails>,
    private val listener: WishlistItemClickListener
) : RecyclerView.Adapter<WishlistAdapter.WishlistViewHolder>() {

    interface WishlistItemClickListener {
        fun onItemClick(place: PlaceDetails)
    }

    inner class WishlistViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val locationImage: ImageView = itemView.findViewById(R.id.location_image)
        private val locationName: TextView = itemView.findViewById(R.id.location_name)
        private val locationCategory: TextView = itemView.findViewById(R.id.location_category)
        private val locationDescription: TextView = itemView.findViewById(R.id.location_description)

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(placeList[position])
                }
            }
        }

        fun bind(place: PlaceDetails) {
            locationName.text = place.name
            locationCategory.text = place.category
            locationDescription.text = place.description
            Glide.with(itemView).load(place.img).into(locationImage)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WishlistViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.wishlist_item, parent, false)
        return WishlistViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: WishlistViewHolder, position: Int) {
        holder.bind(placeList[position])
    }

    override fun getItemCount(): Int {
        return placeList.size
    }

    fun updateList(newList: List<PlaceDetails>) {
        placeList.clear()
        placeList.addAll(newList)
        notifyDataSetChanged()
    }

    fun deleteItem(position: Int) {
        val deletedItem = placeList[position]
        wishlistViewModel.removeFromWishlist(deletedItem)
        placeList.removeAt(position)
        notifyItemRemoved(position)
    }

    fun getItem(position: Int): PlaceDetails {
        return placeList[position]
    }
}
