package com.vishnurajan.hotelbookingapp.data

import com.example.projecthotel.R


object DataSource {
    fun loadCategories(): List<RowItem> {
        return listOf(
            RowItem("Taj Mahal", "", R.drawable.taj),
            RowItem("Taj Mahal", "", R.drawable.taj),
            RowItem("Taj Mahal", "", R.drawable.taj),
            RowItem("Taj Mahal", "", R.drawable.taj),
            RowItem("Taj Mahal", "", R.drawable.taj),
            RowItem("Taj Mahal", "", R.drawable.taj),
        )
    }

    fun loadAmenities(amenities: List<String>): List<Amenities> {
        val fullAmenities =  listOf(
            Amenities(R.drawable.gym, "Gym", "1"),
            Amenities(R.drawable.parking, "Free Parking", "2"),
            Amenities(R.drawable.restaurant, "Restaurant", "3"),
            Amenities(R.drawable.roomservice, "Room Service", "4"),
            Amenities(R.drawable.swimmingpool, "Swimming Pool", "5"),
            Amenities(R.drawable.wifi, "Wifi", "6"),
            )
        return fullAmenities.filter { it.id in amenities }
    }
}