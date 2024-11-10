package com.vishnurajan.hotelbookingapp.data


data class Places(
    val Desc: String = "",
    val Image: String = "",
    val Name: String = ""
)

data class Hotels(
    val Name: String = "",
    val Photo: String = "",
    val Contact:Long = 0,
    val ContactName:String = "",
    val Desc: String = "",
    val Rooms: HashMap<String, Rooms>? = null,
    val Job: String = "",
    val Amenities: String = "",
    val Rating: Double = 0.0,
    val numberofrooms: Int = 0
)

data class Rooms(
    val Name: String = "",
    val Photo: String = "",
    val Price: Int = 0,
)

data class Checkout(
    val name: String = "",
    val photo: String = "",
    val price: Int = 0
)

data class User(
    val fullName: String = "",
    val phoneNumber: String = "",
    val email: String = "",
    val cart: Map<String, Rooms> = mapOf()
)

data class UserDetails(
    val checkin: String = "",
    val checkout: String = "",
    val hotelname: String = "",
    val numberofrooms: Int = 0,
    val hotelPhoneNumber: Long = 0
)

data class Amenities(
    val img: Int,
    val name: String,
    val id: String
)
