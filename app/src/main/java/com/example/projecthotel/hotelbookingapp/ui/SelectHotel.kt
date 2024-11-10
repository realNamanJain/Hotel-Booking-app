package com.vishnurajan.hotelbookingapp.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.vishnurajan.hotelbookingapp.data.Hotels
import com.vishnurajan.hotelbookingapp.data.Rooms
import java.text.NumberFormat
import java.util.Locale

@Composable
fun SelectHotel(
    hotelViewModel: HotelViewModel,
    chosenHotel: (Hotels) -> Unit
) {
    val database = hotelViewModel.hotelsDownloaded.collectAsState()
    val count = hotelViewModel.count.collectAsState()
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        items(database.value) {
            HotelCard(it, chosenHotel, hotelViewModel, count.value)
        }
    }
}

@Composable
fun HotelCard(
    hotels: Hotels,
    chosenHotel: (Hotels) -> Unit,
    hotelViewModel: HotelViewModel,
    count: Int
) {
    val roomsHashMap: HashMap<String, Rooms>? = hotels.Rooms
    val roomsList: List<Rooms> = roomsHashMap?.values?.toList() ?: emptyList()
    val maxPriceRoom: Rooms? = roomsList.maxByOrNull { it.Price }
    val minPriceRoom: Rooms? = roomsList.minByOrNull { it.Price }
    val maxPrice: Int = maxPriceRoom?.Price ?: 0
    val minPrice: Int = minPriceRoom?.Price ?: 0
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                chosenHotel(hotels)
                hotelViewModel.setUserDetails()
            }
    ) {
        AsyncImage(model = hotels.Photo,
            contentDescription = hotels.Name,
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp),
            contentScale = ContentScale.Crop
        )
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = hotels.Name,
                modifier = Modifier.padding(10.dp),
                fontWeight = FontWeight.Bold,
                color = Color.Gray
            )

                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = if (hotels.numberofrooms >= count) Color(66, 165, 245, 255) else Color(
                            218,
                            96,
                            137,
                            255
                        ),
                    ),
                    shape = RoundedCornerShape(topStart = 0.dp, topEnd = 0.dp, bottomStart = 8.dp)
                ) {
                    Text(text = if (hotels.numberofrooms >= count) "Rs.${NumberFormat.getNumberInstance(Locale.US).format(minPrice)} to Rs.${NumberFormat.getNumberInstance(Locale.US).format(maxPrice)}" else "Sold Out",
                        modifier = Modifier.padding(10.dp),
                        color = Color.White
                    )
                }
        }

    }
}