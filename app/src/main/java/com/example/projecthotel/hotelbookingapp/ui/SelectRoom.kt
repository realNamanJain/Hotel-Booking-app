package com.vishnurajan.hotelbookingapp.ui

import android.content.Intent
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.vishnurajan.hotelbookingapp.data.Amenities
import com.vishnurajan.hotelbookingapp.data.Checkout
import com.vishnurajan.hotelbookingapp.data.DataSource
import com.vishnurajan.hotelbookingapp.data.Hotels
import com.vishnurajan.hotelbookingapp.data.Rooms
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectRoom(
    hotels: Hotels,
    hotelViewModel: HotelViewModel,
    finishCheckout: (Hotels) -> Unit
) {
    hotelViewModel.downloadRooms()
    val dialerLauncher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { }
    val rooms by hotelViewModel.roomsDownload.collectAsState()
    val checkout by hotelViewModel.checkoutDownloaded.collectAsState()
    val userDetails by hotelViewModel.userDetails.collectAsState()
    val days = calculateDaysBetweenDates(userDetails.checkin, userDetails.checkout)
    val numberOfDays = if (days.toInt() != 0) days else 1
    val amenities: List<String> = hotels.Amenities.split(",")
    val database = DataSource.loadAmenities(amenities)


    LazyColumn(modifier = Modifier.fillMaxSize()) {
        item {
            Column {
                Card(
                    shape = RoundedCornerShape(bottomEnd = 50.dp)
                ) {
                    AsyncImage(
                        model = hotels.Photo,
                        contentDescription = hotels.Name,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        contentScale = ContentScale.Crop

                    )
                }

                Text(
                    text = hotels.Name,
                    modifier = Modifier.padding(10.dp),
                    fontSize = 32.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(66, 165, 245, 255)
                )

                Box(
                    modifier = Modifier.padding(10.dp)
                ) {
                    Row {
                        (1..5).forEach {
                            Icon(imageVector = Icons.Default.Star, contentDescription = "Star", tint = Color.LightGray)
                        }
                        Text(text = "${hotels.Rating}", fontSize = 18.sp, modifier = Modifier.padding(horizontal = 10.dp), fontWeight = FontWeight.Bold)
                    }
                    Row {
                        (1..hotels.Rating.toInt()).forEach {
                            Icon(imageVector = Icons.Default.Star, contentDescription = "Star")
                        }
                    }
                }



                Text(
                    text = "About the hotel",
                    modifier = Modifier.padding(10.dp),
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(66, 165, 245, 255)
                )

                Text(
                    text = hotels.Desc,
                    modifier = Modifier.padding(10.dp),
                    color = Color.DarkGray,
                    textAlign = TextAlign.Justify
                )

                Divider(thickness = 1.dp, color = Color.LightGray)

                Text(
                    text = "Amenities available",
                    modifier = Modifier.padding(10.dp),
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(66, 165, 245, 255)
                )
            }
        }

        item {
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                items(database) {
                    AmenitiesGrid(amenitiy = it)
                }
            }
        }

        item {
            Column(modifier = Modifier.fillMaxWidth()) {
                Divider(thickness = 1.dp, color = Color.LightGray)
                Text(
                    text = "Property Rules & Information",
                    modifier = Modifier.padding(10.dp),
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(66, 165, 245, 255)
                )

                Text(text = "○ Check-in: 12.00 Pm, Check-out: 11.00 Am \n"+
                        "○ Pets are not allowed.\n" +
                        "○ Outside food is not allowed\n" +
                        "○ Passport, Aadhar, and Govt. ID are accepted as ID proofs.\n",
                    modifier = Modifier.padding(10.dp),
                    )

                Text(
                    text = "Select Room(s)",
                    modifier = Modifier.padding(10.dp),
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(66, 165, 245, 255)
                )
                for (room in rooms) {
                    roomCard(
                        room = room,
                        hotelViewModel = hotelViewModel,
                        checkout = checkout,
                    )
                }
                Spacer(modifier = Modifier.size(68.dp))
            }
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp),
        verticalArrangement = Arrangement.Bottom
    ){

        if(checkout.isNotEmpty())
            Card (
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                          finishCheckout(hotels)
                          },
                colors = CardDefaults.cardColors(
                    Color(66, 165, 245, 255)
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    val day = if (days.toInt() == 1) "Day" else "Days"
                    Text(text = "${checkout.size} Rooms | $numberOfDays $day | Rs. ${NumberFormat.getNumberInstance(Locale.US).format(checkout.sumOf { it.price } * numberOfDays)}", color = Color.White, fontSize = 16.sp)
                    Card(
                    ) {
                        Text(text = "Checkout", fontSize = 16.sp, modifier = Modifier.padding(6.dp))
                    }
                }
            }
    }
}

@Composable
fun AmenitiesGrid(
    amenitiy: Amenities
) {
    Column(
        modifier = Modifier.size(100.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(painter = painterResource(id = amenitiy.img), contentDescription = amenitiy.name, modifier = Modifier
            .size(60.dp)
            .padding(10.dp))
        Text(text = amenitiy.name)
    }
}

@Composable
fun roomCard(
    room: Rooms,
    hotelViewModel: HotelViewModel,
    checkout : List<Checkout>
) {
    var selected = false
    for (item in checkout) {
        if (item.name == room.Name) {
            selected = true
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
    ) {
        AsyncImage(model = room.Photo, contentDescription = room.Name, modifier = Modifier
            .height(120.dp),
            contentScale = ContentScale.Crop
        )
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(10.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column (
            verticalArrangement = Arrangement.SpaceBetween
        ){
            Text(text = room.Name, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Text(text = "Rs. ${NumberFormat.getNumberInstance(Locale.US).format(room.Price)}")
        }

        Card(
            modifier = Modifier
                .fillMaxHeight()
                .align(Alignment.CenterVertically)
                .width(140.dp),
            border = BorderStroke(2.dp, Color(66, 165, 245, 255)),
            colors = CardDefaults.cardColors(
                containerColor = if (!selected) Color.White else Color(66, 165, 245, 50)
            )

        ) {
            Row(
                modifier = Modifier
                .fillMaxWidth()
                .padding(11.dp)
                .clickable {
                    if (!selected) {
                        hotelViewModel.addToDatabase(room)
                    } else {
                        hotelViewModel.removeFromDatabase(room)
                    }
                },
                horizontalArrangement = Arrangement.Center
            ) {
                if (selected)
                Icon(Icons.Default.Check, contentDescription = "selected", tint = Color(66, 165, 245, 255))
                Text(
                    text = if (selected)"SELECTED" else "SELECT",
                    fontSize = 18.sp,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    color = Color(66, 165, 245, 255)
                )
            }
        }
    }
}

fun launchDialer( launcher: androidx.activity.result.ActivityResultLauncher<android.content.Intent>, phoneNumber: String) {
    val intent = Intent(Intent.ACTION_DIAL).apply {
        data = android.net.Uri.parse("tel:$phoneNumber")
    }
    launcher.launch(intent)
}