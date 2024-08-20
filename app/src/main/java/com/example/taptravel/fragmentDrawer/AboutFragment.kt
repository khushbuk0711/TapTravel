package com.example.taptravel.fragmentDrawer

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.taptravel.databinding.FragmentAboutBinding

class AboutFragment : Fragment() {

    private var _binding: FragmentAboutBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentAboutBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root

    }
//    override fun onViewCreated{
//
//        val imageView: ImageView = binding.profile_image
//            val imageUrl = "https://lh3.googleusercontent.com/a/ACg8ocKjSjEtm-OiXwN_9XjuFE09mLc8LDsI-X5bOsXTGQwWxck55SdC=s360-c-no"
//
//        Glide.with(this)
//            .load(imageUrl)
//            .apply(RequestOptions.bitmapTransform(CircleCrop()))
//            .into(imageView)
//
//    }
      override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}