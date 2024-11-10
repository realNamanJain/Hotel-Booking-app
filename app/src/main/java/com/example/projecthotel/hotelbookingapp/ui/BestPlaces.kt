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
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.vishnurajan.hotelbookingapp.data.Hotels
import com.vishnurajan.hotelbookingapp.data.Places
import com.vishnurajan.hotelbookingapp.data.Rooms

@Composable
fun BestPlaces(
    hotelViewModel: HotelViewModel,
    chosenPlace : (Places) -> Unit
) {
    val database = hotelViewModel.placesDownloaded.collectAsState()
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp,),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        items(database.value) {
            PlacesCard(it, chosenPlace)
        }
    }

}

@Composable
fun PlacesCard(
    places: Places,
    chosenPlace: (Places) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { chosenPlace(places) }
    ) {
        AsyncImage(model = places.Image,
            contentDescription = places.Name,
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp),
            contentScale = ContentScale.Crop
        )
        Text(
            text = "${places.Name}",
            modifier = Modifier.padding(10.dp),
            fontWeight = FontWeight.Bold,
            color = Color.Gray
        )
    }
}