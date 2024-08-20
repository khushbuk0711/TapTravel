package com.example.taptravel.fragmentPlaceDetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PlaceViewModel : ViewModel() {
    private val _name = MutableLiveData<String>()
    val name: LiveData<String> get() = _name

    private val _category = MutableLiveData<String>()
    val category: LiveData<String> get() = _category

    private val _description = MutableLiveData<String>()
    val description: LiveData<String> get() = _description

    private val _image = MutableLiveData<String>()
    val image: LiveData<String> get() = _image

    private val _rating = MutableLiveData<Float>()
    val rating: LiveData<Float> get() = _rating

    fun setData(name: String, category: String, description: String, image: String, rating: Float) {
        _name.value = name
        _category.value = category
        _description.value = description
        _image.value = image
        _rating.value = rating
    }
}
