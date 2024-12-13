package com.example.taptravel.fragmentDrawer

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.taptravel.PlaceActivity
import com.example.taptravel.R
import com.example.taptravel.SearchApi.PlaceClickListener
import com.example.taptravel.SearchApi.RequestManager
import com.example.taptravel.SearchApi.SearchListener
import com.example.taptravel.databinding.FragmentHomeBinding
import com.example.taptravel.search_data.Data
import com.example.taptravel.search_data.Destination
import com.example.taptravel.viewmodel.WishlistViewModel

class HomeFragment : Fragment() {
    var hardcodedPlacesVisible = true
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    lateinit var searchPlace: SearchView
    private lateinit var recyclerView: RecyclerView
    private lateinit var manager: RequestManager
    private lateinit var progressBar: ProgressBar
    private lateinit var adapter: PlaceAdapter

    private lateinit var wishlistViewModel: WishlistViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        searchPlace = binding.searchPlace
        recyclerView = binding.RV
        manager = RequestManager(requireContext())
        progressBar = binding.progressBar

        wishlistViewModel = ViewModelProvider(this)[WishlistViewModel::class.java]

        adapter = PlaceAdapter(requireContext(), wishlistViewModel, emptyList(), emptyList(), object : PlaceClickListener {
            override fun onPlaceClicked(position: Int, isApi: Boolean) {
                if (isApi) {
                    val apiPlace = adapter.apiPlaces[position]
                    navigateToApiPlaceActivity(apiPlace)
                } else {
                    val hardcodedPlace = adapter.hardcodedPlaces[position]
                    navigateToHardcodedPlaceActivity(hardcodedPlace)
                }
            }
        })
        recyclerView.adapter = adapter
        recyclerView.layoutManager = GridLayoutManager(context, 1)
        searchPlace.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                progressBar.visibility = View.VISIBLE
                val inputMethodManager = requireContext().getSystemService(InputMethodManager::class.java)
                inputMethodManager?.hideSoftInputFromWindow(view.windowToken, 0)

                query?.let {
                    manager.searchPlaces(it, listener)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.isNullOrEmpty()) {
                    if (!hardcodedPlacesVisible) {
                        hardcodedPlacesVisible = true
                        loadHardcodedPlaces()
                    }
                }
                return false
            }
        })


        loadHardcodedPlaces()

    }

    fun loadHardcodedPlaces() {
        val hardcodedPlaces = Hardcode_Data.places

        adapter.hardcodedPlaces = hardcodedPlaces
        adapter.apiPlaces = emptyList()
        adapter.notifyDataSetChanged()
    }

    private val listener = object : SearchListener {
        override fun onResponse(response: Destination?) {
            Handler(Looper.getMainLooper()).post {
                progressBar.visibility = View.GONE
                if (response != null && response.data.isNotEmpty()) {
                    showResult(response)
                    hardcodedPlacesVisible = false
                } else {
                    Toast.makeText(requireContext(), "No results found", Toast.LENGTH_SHORT).show()
                }
            }
        }

        override fun onError(msg: String) {
            Handler(Looper.getMainLooper()).post {
                progressBar.visibility = View.GONE
                Toast.makeText(requireContext(), "An Error Occurred: $msg", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showResult(response: Destination) {
        adapter.apiPlaces = response.data
        adapter.hardcodedPlaces = emptyList()
        adapter.notifyDataSetChanged()
    }

    private fun navigateToHardcodedPlaceActivity(place: Place) {
        val placeIntent = Intent(requireContext(), PlaceActivity::class.java).apply {
            putExtra("id", place.id)
            putExtra("name", place.name)
            putExtra("description", place.description)
            putExtra("image", place.imageUrl)
            putExtra("category", place.category)
            putExtra("rating", place.rating.toString())
            putExtra("latitude", place.latitude.toString())
            putExtra("longitude", place.longitude.toString() )
        }
        startActivity(placeIntent)
    }

    private fun navigateToApiPlaceActivity(place: Data) {
        val placeIntent = Intent(requireContext(), PlaceActivity::class.java).apply {
            putExtra("id", place.result_object.location_id)
            putExtra("name", place.result_object.name)
            putExtra("description", place.result_object.geo_description)
            putExtra("image", place.result_object.photo.images.original.url)
            putExtra("category", place.result_object.category.name)
            putExtra("rating", place.result_object.rating)
            putExtra("latitude", place.result_object.latitude)
            putExtra("longitude", place.result_object.longitude)
        }
        startActivity(placeIntent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
