package com.example.taptravel

import android.content.Context
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.taptravel.search.Data
import com.google.android.material.imageview.ShapeableImageView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.ArrayList

class Place_Adapter(val context: Context, var placesList: List<Data>, var wishlist: ArrayList<PlaceDetails>) : RecyclerView.Adapter<Place_Adapter.PlaceViewHolder>() {

    @RequiresApi(Build.VERSION_CODES.P)
    class PlaceViewHolder(val itemView: View, listener: PlaceClickListener) : RecyclerView.ViewHolder(itemView){
        var placeName :TextView
        var placeShapeableImageView: ShapeableImageView
        var placeDescription: TextView
        var wishlist: CheckBox
        init {
            placeName=itemView.requireViewById(R.id.location_name)
            placeDescription=itemView.requireViewById(R.id.location_description)
            placeShapeableImageView=itemView.requireViewById(R.id.location_image)
            wishlist=itemView.requireViewById(R.id.wishlist_btn)
            itemView.setOnClickListener {
                listener.onPlaceClicked(adapterPosition)
            }
        }

    }
    private lateinit var placeId:String
    private lateinit var placeListener : PlaceClickListener
    private var db = FirebaseFirestore.getInstance()
    private var auth = FirebaseAuth.getInstance()
    private var uid =auth.currentUser?.uid.toString()

    fun setOnPlaceClickListener(placeListener: PlaceClickListener){
        this.placeListener=placeListener
    }


    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.place_item, parent, false)
        return PlaceViewHolder(view, placeListener)
    }

    override fun getItemCount(): Int {
        return placesList.size

    }

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onBindViewHolder(holder: PlaceViewHolder, position: Int) {
        try {
            val currentItem = placesList[position]
            Log.i("delCheck", wishlist.size.toString())
            var isInWishlist = false

            for (item in wishlist) {
                if (item.id == currentItem.result_object.location_id) {
                    Log.i("checker", item.id.toString())
                    isInWishlist = true
                    break
                }
            }
            holder.wishlist.isChecked = isInWishlist
            holder.placeName.text = currentItem.result_object.name
            holder.placeDescription.text=currentItem.result_object.location_string
            placeId = currentItem.result_object.location_id
            val placeName = currentItem.result_object.name
            val placeImage = currentItem.result_object.photo.images.original.url
            val placeDescription = currentItem.result_object.location_string


            holder.wishlist.setOnClickListener {
                val randomId = placesList[position].result_object.location_id
                if (holder.wishlist.isChecked) {

                    val place = PlaceDetails(randomId,placeName, placeImage,placeDescription)
                    wishlist.add(place)
                    db.collection(uid).document(randomId).set(place).addOnSuccessListener {
                        Toast.makeText(context, "Added to Wishlist", Toast.LENGTH_SHORT).show()
                        holder.wishlist.isChecked = true
                    }
                } else {
                    for ((index, item) in wishlist.withIndex()) {
                        if (item.id == randomId) {
                            wishlist.removeAt(index)
                            break
                        }
                    }
                    db.collection(uid).document(randomId).delete().addOnSuccessListener {
                        Toast.makeText(context, "Removed from Wishlist", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            val url = currentItem.result_object.photo.images.original.url
            if(url != null){
                Glide.with(context)
                    .load(url as String)
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.placeholder)
                    .into(holder.placeShapeableImageView)
            }

        } catch (e: Exception) {
            Log.e("Place_Adapter", "Error in onBindViewHolder", e)
        }
    }
}