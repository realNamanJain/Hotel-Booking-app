package com.vishnurajan.hotelbookingapp.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.vishnurajan.hotelbookingapp.data.Places

@Composable
fun ChosenPlace(
    places: Places
) {

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        item {
            Column() {
                Card(
                    shape = RoundedCornerShape(bottomEnd = 50.dp)
                ) {
                    AsyncImage(
                        model = places.Image,
                        contentDescription = places.Name,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        contentScale = ContentScale.Crop

                    )
                }
                Text(
                    text = places.Name,
                    modifier = Modifier.padding(10.dp),
                    fontSize = 32.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(66, 165, 245, 255)
                )
                Text(
                    text = places.Desc,
                    modifier = Modifier.padding(10.dp),
                    textAlign = TextAlign.Justify,
                    color = Color.DarkGray
                )
            }
        }
    }

}