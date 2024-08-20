package com.example.taptravel.fragmentPlaceDetails

import MapFragment
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.example.taptravel.R
import com.example.taptravel.databinding.FragmentPlaceBinding
import com.example.taptravel.fragmentDrawer.WeatherFragment


class PlaceFragment : Fragment() {
    private var _binding: FragmentPlaceBinding? = null
    private val binding get() = _binding!!
    private val placeViewModel: PlaceViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPlaceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        placeViewModel.name.observe(viewLifecycleOwner) { binding.locationName.text = it }
        placeViewModel.category.observe(viewLifecycleOwner) { binding.locationCategory.text = it }
        placeViewModel.description.observe(viewLifecycleOwner) { binding.locationDescription.text = it }
        placeViewModel.image.observe(viewLifecycleOwner) { link ->
            val imageUrl =
                if (link.isNotEmpty()) link else "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQlZSOLoJ-j_JMIlVZZAlQg3R6_y8Jvq4QNumJl1fbowA&s"
            Glide.with(this).load(imageUrl).into(binding.locationImage)
        }
        placeViewModel.rating.observe(viewLifecycleOwner) { binding.locationRating.rating = it }
        placeViewModel.rating.observe(viewLifecycleOwner) { binding.ratingText.text = it.toString() }

        val latitude = arguments?.getString("latitude", "0.0")?.toDoubleOrNull() ?:0.0
        val longitude = arguments?.getString("longitude", "0.0")?.toDoubleOrNull() ?:0.0

        arguments?.let { args ->
            val name = args.getString("name", "")
            val category = args.getString("category", "")
            val description = args.getString("description", "")
            val image = args.getString("image", "")
            val rating = args.getString("rating", "0.0")?.toFloatOrNull() ?: 0.0f

            // Set data in ViewModel
            placeViewModel.setData(name, category, description, image, rating)
        }
        binding.weatherBtn.setOnClickListener {
            // Navigate to WeatherFragment
            navigateToWeatherFragment(latitude, longitude)
        }

        binding.mapBtn.setOnClickListener {
            // Navigate to MapFragment
            navigateToMapFragment(latitude, longitude)
        }
    }
    private fun navigateToWeatherFragment(latitude: Double?, longitude: Double?) {
        // Example: Replace current fragment with WeatherFragment
        if (latitude != null && longitude != null) {
            val weatherFragment = WeatherFragment().apply {
                arguments = Bundle().apply {
                    putDouble("latitude", latitude)
                    putDouble("longitude", longitude)
                }
            }

        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, weatherFragment)
            .addToBackStack(null)
            .commit()
        }
    }

    private fun navigateToMapFragment(latitude: Double?, longitude: Double?) {
        // Example: Replace current fragment with MapFragment
        if (latitude != null && longitude != null) {
            val mapFragment = MapFragment().apply {
                arguments = Bundle().apply {
                    putDouble("latitude", latitude)
                    putDouble("longitude", longitude)
                }
            }
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, mapFragment)
                .addToBackStack(null)
                .commit()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

//        parentFragmentManager.setFragmentResultListener("dataFromAdapter", this) { requestKey, bundle ->
//            val name = bundle.getString("name")
//            val category = bundle.getString("category")
//            val description = bundle.getString("description")
//            val image = bundle.getString("image")
//            val ratingString = bundle.getString("rating", "0.0")
//            var link = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQlZSOLoJ-j_JMIlVZZAlQg3R6_y8Jvq4QNumJl1fbowA&s"
//            if (image != null) {
//                link = image
//                Log.i("runCheck", link)
//            }
//            val rating = ratingString.toFloatOrNull() ?: 0.0f
//            binding.locationName.text = name
//            binding.locationDescription.text = description
//            binding.locationCategory.text = category
//            binding.locationRating.rating = rating
//            Glide.with(this)
//                .load(link)
//                .into(binding.locationImage)
//
//        }
