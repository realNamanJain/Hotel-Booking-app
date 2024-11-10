package com.vishnurajan.hotelbookingapp.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.vishnurajan.hotelbookingapp.data.Checkout
import com.vishnurajan.hotelbookingapp.data.Hotels
import com.vishnurajan.hotelbookingapp.data.Places
import com.vishnurajan.hotelbookingapp.data.Rooms
import com.vishnurajan.hotelbookingapp.data.User
import com.vishnurajan.hotelbookingapp.data.UserDetails
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HotelViewModel: ViewModel() {

    private lateinit var timerJob: Job
    private val _user = MutableStateFlow<FirebaseUser?>(null)
    val user: MutableStateFlow<FirebaseUser?> get() = _user
    val database = Firebase.database

    private val _placesDownloaded = MutableStateFlow<List<Places>>(mutableListOf())
    val placesDownloaded: MutableStateFlow<List<Places>> get() = _placesDownloaded

    private val _roomsDownloaded = MutableStateFlow<List<Rooms>>(mutableListOf())
    val roomsDownload: MutableStateFlow<List<Rooms>> get() = _roomsDownloaded

    fun addRooms(rooms: Rooms) {
        val currentRooms = _roomsDownloaded.value.toMutableList()
        currentRooms.add(rooms)
        _roomsDownloaded.value = currentRooms.toList()
    }

    private val _checkoutDownloaded = MutableStateFlow<List<Checkout>>(mutableListOf())
    val checkoutDownloaded: MutableStateFlow<List<Checkout>> get() = _checkoutDownloaded

    fun addCheckout(checkout: Checkout) {
        val currentCheckout = _checkoutDownloaded.value.toMutableList()
        currentCheckout.add(checkout)
        _checkoutDownloaded.value = currentCheckout.toList()
    }

    private val _chosenHotel = MutableStateFlow(Hotels())
    val chosenHotel: MutableStateFlow<Hotels> get() = _chosenHotel

    fun setHotel(hotel: Hotels) {
        _chosenHotel.value = hotel
    }

    private val _hotelsDownloaded = MutableStateFlow<List<Hotels>>(mutableListOf())
    val hotelsDownloaded: MutableStateFlow<List<Hotels>> get() = _hotelsDownloaded

    fun addHotel(hotels: Hotels, numberOfRooms: Int) {
        val newHotels = hotels.copy(numberofrooms = numberOfRooms)
        val currentHotels = _hotelsDownloaded.value.toMutableList()
        currentHotels.add(newHotels)
        _hotelsDownloaded.value = currentHotels.toList()
    }

    fun addPlace(place: Places) {
        val currentPlaces = _placesDownloaded.value.toMutableList()
        currentPlaces.add(place)
        _placesDownloaded.value = currentPlaces.toList()
    }

    private val _selectedCity = MutableStateFlow("")
    val selectedCity: MutableStateFlow<String> get() = _selectedCity

    fun setCity(city: String) {
        _selectedCity.value = city
    }

    private val _chosenPlace = MutableStateFlow(Places())
    val chosenPlace: MutableStateFlow<Places> get() = _chosenPlace

    fun setChosenPlace(place: Places) {
        _chosenPlace.value = place
    }

    private val _userDetails = MutableStateFlow(UserDetails())
    val userDetails: MutableStateFlow<UserDetails> get() = _userDetails

    fun setUserDetails() {
        val data = UserDetails(
            checkin = checkIn.value,
            checkout = checkOut.value,
            hotelname = chosenHotel.value.Name,
            numberofrooms = checkoutDownloaded.value.size,
            hotelPhoneNumber = chosenHotel.value.Contact,
        )
        val userdetails = database.getReference("Users/${user.value!!.uid}/userdetails/")
        Log.d("rufus", userdetails.toString())
        userdetails.setValue(data)
    }

    private val _options = MutableStateFlow<List<String>>(mutableListOf())
    val options: MutableStateFlow<List<String>> get() = _options


    private val _phoneNumber = MutableStateFlow("")
    val phoneNumber: MutableStateFlow<String> get() = _phoneNumber

    private val _fullName = MutableStateFlow("")
    val fullName: MutableStateFlow<String> get() = _fullName

    fun saveUserData() {
        val users = database.getReference("Users/${user.value!!.uid}/userdata/")
        Log.d("rufus", users.toString())
        val userData = User(
            fullName = fullName.value,
            phoneNumber = phoneNumber.value,
            email = email.value
        )
        users.setValue(userData)
    }

    private val _email = MutableStateFlow("")
    val email: MutableStateFlow<String> get() = _email

    private val _serverUser = MutableStateFlow(User())
    val serverUser: MutableStateFlow<User> get() = _serverUser

    private val _ticks = MutableStateFlow(60L)
    val ticks: MutableStateFlow<Long> get() = _ticks

    private val _loading = MutableStateFlow(false)
    val loading: MutableStateFlow<Boolean> get() = _loading

    private val _otp = MutableStateFlow("")
    val otp: MutableStateFlow<String> get() = _otp

    private val _logoutClicked = MutableStateFlow(false)
    val logoutClicked: MutableStateFlow<Boolean> get() = _logoutClicked

    private val _verificationId = MutableStateFlow("")
    val verificationId: MutableStateFlow<String> get() = _verificationId

    private val _isSplashScreenVisible = MutableStateFlow(true)
    val isSplashScreenVisible: StateFlow<Boolean> get() = _isSplashScreenVisible

    private val _checkIn = MutableStateFlow("")
    val checkIn: MutableStateFlow<String> get() = _checkIn

    private val _checkOut = MutableStateFlow("")
    val checkOut: MutableStateFlow<String> get() = _checkOut

    private val _bedFlag = MutableStateFlow(false)
    val bedFlag: MutableStateFlow<Boolean> get() = _bedFlag

    private val _count = MutableStateFlow(0)
    val count: MutableStateFlow<Int> get() = _count


    fun setCount(count: Int) {
        _count.value = count
    }

    fun setBedFlag(bedFlag: Boolean) {
        _bedFlag.value = bedFlag
    }

    fun setCheckIn(checkIn: String) {
        _checkIn.value = checkIn
    }

    fun setCheckOut(checkOut:String) {
        _checkOut.value = checkOut
    }


    fun setLogoutStatus(logoutStatus: Boolean) {
        _logoutClicked.value = logoutStatus
    }

    fun setPhoneNumber(phoneNumber: String) {
        _phoneNumber.value = phoneNumber
    }

    fun setFullName(fullName: String) {
        _fullName.value = fullName
    }

    fun setEmail(email: String) {
        _email.value = email
    }

    fun setLoading(isLoading: Boolean) {
        _loading.value = isLoading
    }
    fun setOtp(otp: String) {
        _otp.value = otp
    }


    fun setVerificationId(verificationId: String) {
        _verificationId.value = verificationId
    }

    fun setUser(user:FirebaseUser?) {
        _user.value = user
    }


    fun clearData() {
        _user.value = null
        _phoneNumber.value = ""
        _otp.value = ""
        verificationId.value = ""
        resetTimer()
    }

    fun runTimer() {
        timerJob = viewModelScope.launch {
            while (_ticks.value > 0) {
                delay(1000)
                _ticks.value -= 1
            }
        }
    }

    fun resetTimer() {
        try {
            timerJob.cancel()
        } catch (_:Exception) {}
        finally {
            _ticks.value = 60
        }
    }

    fun downloadRooms() {
        val TAG = "Vishnu_Firebase"
        val hotels = database.getReference("Places/${selectedCity.value}/Hotels/${chosenHotel.value.Name.substringBefore(" ")}/Rooms")
        hotels.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                _roomsDownloaded.value = emptyList()
                for (childSnapshot in dataSnapshot.children) {
                    val itemFromFirebase = childSnapshot.getValue(Rooms::class.java)
                    itemFromFirebase?.let {
                        addRooms(it)
                    }

                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException())
            }
        })
    }

    fun downloadHotels() {
        val TAG = "Vishnu_Firebase"

        val hotels = database.getReference("Places/${selectedCity.value}/Hotels")
        hotels.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                _hotelsDownloaded.value = emptyList()
                for (childSnapshot in dataSnapshot.children) {
                    val roomsRef = childSnapshot.child("Rooms")
                    val numberOfRooms = roomsRef.childrenCount.toInt()
                    Log.d(TAG, "Number of Rooms are: $numberOfRooms")
                    val itemFromFirebase = childSnapshot.getValue(Hotels::class.java)
                    itemFromFirebase?.let {
                        /*if (numberOfRooms >= count.value) {*/
                            addHotel(it, numberOfRooms)
                        /*}*/
                    }

                }
                options.value = placesDownloaded.value.map { it.Name }
                Log.d(TAG, "Value is: $placesDownloaded")
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException())
            }
        })
    }

    fun readDatabase(
    ) {
        val myRef = database.getReference("Users/${user.value!!.uid}/cart")
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                _checkoutDownloaded.value = emptyList()
                for (childSnapshot in dataSnapshot.children) {
                    val item = childSnapshot.getValue(Checkout::class.java)
                    if (item != null) {
                        addCheckout(item)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
            }
        })
    }

    fun downloadFromFirebase() {

        val TAG = "Vishnu_Firebase"
        val places = database.getReference("Places")

        places.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                _placesDownloaded.value = emptyList()
                for (childSnapshot in dataSnapshot.children) {
                    val itemFromFirebase = childSnapshot.getValue(Places::class.java)
                    itemFromFirebase?.let {
                        addPlace(it)
                    }

                }
                options.value = placesDownloaded.value.map { it.Name }
                Log.d(TAG, "Value is: $placesDownloaded")
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException())
            }
        })
    }

    fun addToDatabase(room: Rooms) {
        val myRef = database.getReference("Users/${user.value!!.uid}/cart")
        myRef.push().setValue(room)

    }

    fun removeFromDatabase(
        room: Rooms
    ) {
        val myRef = database.getReference("Users/${user.value!!.uid}/cart")
        myRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                for (childSnapshot in dataSnapshot.children) {

                    var itemRemoved = false
                    val item = childSnapshot.getValue(Checkout::class.java)
                    Log.d("vishnufirebase", item.toString())

                    item?.let {
                        if (room.Name == it.name && room.Price == it.price) {
                            childSnapshot.ref.removeValue()
                            itemRemoved = true
                        }
                    }
                    if (itemRemoved) break // Exit the loop if item has been removed
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
            }
        })
    }

    fun readUser() {
        val userRef = database.getReference("Users/${user.value!!.uid}/")

        val userDataRef = userRef.child("userdata")
        val userDetailsRef = userRef.child("userdetails")

        userDataRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val name = dataSnapshot.child("fullName").getValue(String::class.java) ?: ""
                val email = dataSnapshot.child("email").getValue(String::class.java) ?: ""
                val phone = dataSnapshot.child("phoneNumber").getValue(String::class.java) ?: ""

                _serverUser.value = User(
                    fullName = name,
                    email = email,
                    phoneNumber = phone
                )
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
            }
        })

        userDetailsRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
               val userDetailsData = dataSnapshot.getValue(UserDetails::class.java)
                if (userDetailsData != null) {
                    _userDetails.value = userDetailsData
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
            }
        })
    }




    init {
        downloadFromFirebase()
    }


}