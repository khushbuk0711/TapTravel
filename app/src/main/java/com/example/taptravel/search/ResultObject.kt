package com.example.taptravel.search

data class ResultObject(
    val address: String,
    val address_obj: AddressObj,
    val amenities: List<Any>,
    val ancestors: List<Ancestor>,
    val awards: List<Any>,
    val bearing: Any,
    val category: Category,
    val category_counts: CategoryCounts,
    val cuisine: List<Cuisine>,
    val description: String,
    val distance: Any,
    val distance_string: Any,
    val doubleclick_zone: String,
    val establishment_types: List<EstablishmentType>,
    val geo_description: String,
    val geo_type: String,
    val has_attraction_coverpage: Boolean,
    val has_curated_shopping_list: Boolean,
    val has_restaurant_coverpage: Boolean,
    val is_candidate_for_contact_info_suppression: Boolean,
    val is_closed: Boolean,
    val is_jfy_enabled: Boolean,
    val is_localized_description: Boolean,
    val is_long_closed: Boolean,
    val is_reviewable: Boolean,
    val latitude: String,
    val location_id: String,
    val location_string: String,
    val location_subtype: String,
    val longitude: String,
    val map_image_url: String,
    val name: String,
    val nearby_attractions: List<Any>,
    val nearest_metro_station: List<Any>,
    val num_reviews: String,
    val open_now_text: String,
    val photo: Photo,
    val preferred_map_engine: String,
    val rating: String,
    val ride_providers: List<String>,
    val special_offers: SpecialOffers,
    val subcategory: List<SubcategoryX>,
    val supplier_location_id: String,
    val supplier_location_name: String,
    val supplier_location_subtype: String,
    val timezone: String,
    val web_url: String
)