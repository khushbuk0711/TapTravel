package com.example.taptravel.fragmentDrawer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.taptravel.SearchApi.PlaceDetails
import com.example.taptravel.databinding.FragmentBucketlistBinding
import com.example.taptravel.viewmodel.WishlistViewModel

class BucketListFragment : Fragment(), WishlistAdapter.WishlistItemClickListener {

    private lateinit var binding: FragmentBucketlistBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: WishlistAdapter
    private lateinit var wishlistViewModel: WishlistViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBucketlistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        wishlistViewModel = ViewModelProvider(this)[WishlistViewModel::class.java]
        recyclerView = binding.wishlistRV
        adapter = WishlistAdapter(requireContext(), wishlistViewModel, mutableListOf(), this)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())



        wishlistViewModel.wishlist.observe(viewLifecycleOwner) { wishlist ->
            adapter.updateList(wishlist)
        }

        val swipeGesture = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val place = adapter.getItem(position)
                adapter.deleteItem(position)
                wishlistViewModel.removeFromWishlist(place)
                Toast.makeText(context, "Removed from Wishlist: ${place.name}", Toast.LENGTH_SHORT).show()
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeGesture)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    override fun onItemClick(place: PlaceDetails) {
        // Handle item click
    }
}
