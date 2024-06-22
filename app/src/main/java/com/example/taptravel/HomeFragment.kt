package com.example.taptravel

import android.app.ProgressDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.taptravel.databinding.FragmentHomeBinding
import com.example.taptravel.search.Destination
import java.util.Timer
import java.util.TimerTask

@Suppress("DEPRECATION")
class HomeFragment : Fragment(), SearchView.OnQueryTextListener {

    private var binding: FragmentHomeBinding? = null
    private lateinit var searchPlace: androidx.appcompat.widget.SearchView
    private lateinit var recyclerView: RecyclerView
    private lateinit var manager: RequestManager
    private lateinit var progress: ProgressDialog
    private lateinit var SHARED_PREFS: String

    // This property is only valid between onCreateView and
    // onDestroyView.

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding!!.root


        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        searchPlace = binding!!.searchPlace
        recyclerView = binding!!.RV
        manager = RequestManager(requireContext())
        progress = ProgressDialog(requireContext())

        SHARED_PREFS = "sharedPrefs"
    }





        override fun onQueryTextSubmit(query: String?): Boolean {
            progress.setTitle("Searching...")
            progress.show()
            if (query != null) {
                manager.searchPlaces(query, listener)
            }
            return true
        }

        override fun onQueryTextChange(newText: String?): Boolean {
            Timer().schedule(object : TimerTask() {
                override fun run() {
                    if (newText != null) {
                        manager.searchPlaces(newText, listener)
                    }
                }
            }, 2000)
            return true

        }


    private var listener = object : SearchListener {
        override fun onResponse(response: Destination?) {
            progress.dismiss()
            showResult(response!!)
        }

        override fun onError(msg: String) {
            progress.dismiss()
            Toast.makeText(context, "An Error Occurred", Toast.LENGTH_SHORT).show()
        }


    }

    private fun showResult(response: Destination) {


        val adapter = Place_Adapter(requireContext(), response.data, arrayListOf())
        recyclerView.adapter = adapter
        recyclerView.layoutManager = GridLayoutManager(context, 1)
        recyclerView.requestLayout()
        adapter.placesList = response.data // Update the adapter's data
        adapter.notifyDataSetChanged()
        adapter.setOnPlaceClickListener(object: PlaceClickListener {
            override fun onPlaceClicked(position: Int) {
                val place = PlaceFragment.newInstance()
                val bundle = Bundle()
                bundle.putString("id", response.data[position].result_object.location_id)
                bundle.putString("Name", response.data[position].result_object.name)
                bundle.putString("Description", response.data[position].result_object.geo_description)
                bundle.putString("image", response.data[position].result_object.photo.images.original.url)

                place.arguments = bundle
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.nav_host_fragment_content_home_page, place)
                    .addToBackStack(null).commit()

                }
            })

    }
    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

}




