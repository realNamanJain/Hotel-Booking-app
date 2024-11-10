package com.vishnurajan.hotelbookingapp.ui

import android.app.DatePickerDialog
import android.widget.DatePicker
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import com.vishnurajan.hotelbookingapp.data.Places
import java.util.Calendar
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StartScreen(
    hotelViewModel: HotelViewModel,
    search: (String) -> Unit,
    viewAll: () -> Unit,
    chosenPlace: (Places) -> Unit
) {

    var expanded by remember { mutableStateOf(false) }
    var selectedOptionText by remember { mutableStateOf("") }
    val options by hotelViewModel.options.collectAsState()
    val database by hotelViewModel.placesDownloaded.collectAsState()
    val checkin by hotelViewModel.checkIn.collectAsState()
    val checkout by hotelViewModel.checkOut.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.Top
    ) {

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = {
                expanded = !expanded
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            TextField(
                value = selectedOptionText,
                onValueChange = { selectedOptionText = it },
                label = { Text("Where you want to go?") },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(
                        expanded = expanded
                    )
                },
                colors = TextFieldDefaults.textFieldColors(
                    unfocusedLabelColor = MaterialTheme.colorScheme.onBackground,
                    focusedLabelColor = MaterialTheme.colorScheme.primary,  // You can set this to a color of your choice
                    unfocusedIndicatorColor = MaterialTheme.colorScheme.secondaryContainer,
                    focusedIndicatorColor = MaterialTheme.colorScheme.secondaryContainer,
                    unfocusedTextColor = MaterialTheme.colorScheme.onBackground,  // For the text color when unfocused
                    focusedTextColor = MaterialTheme.colorScheme.onBackground,   // For the text color when focused
                    containerColor = Color.White
                ),
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth(),
                readOnly = true,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.LocationOn,
                        contentDescription = "Where?",
                        tint = Color(
                            66,
                            165,
                            245,
                            255
                        )
                    )
                }

            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = {
                    expanded = false
                }
            ) {
                options.forEach { selectionOption ->
                    DropdownMenuItem(
                        { Text(text = selectionOption) },
                        onClick = {
                            selectedOptionText = selectionOption
                            expanded = false
                        })
                }
            }
        }

        val checkIn by hotelViewModel.checkIn.collectAsState()
        val checkOut by hotelViewModel.checkOut.collectAsState()
        val count by hotelViewModel.count.collectAsState()

        MyDatePicker("Check-in Date", checkIn, { hotelViewModel.setCheckIn(it) })
        MyDatePicker("Check-out Date", checkOut, { hotelViewModel.setCheckOut(it) })

        TextField(
            value = if (count == 0) "" else count.toString(),
            onValueChange = { },
            label = { Text("Number of Rooms") },
            enabled = false,
            modifier = Modifier
                .clickable { hotelViewModel.setBedFlag(true) }
                .fillMaxWidth(),
            colors = TextFieldDefaults.textFieldColors(
                disabledLabelColor = MaterialTheme.colorScheme.onBackground,
                disabledTextColor = MaterialTheme.colorScheme.onBackground,
                containerColor = Color.White,
                disabledIndicatorColor = MaterialTheme.colorScheme.secondaryContainer

            ),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Outlined.Home,
                    contentDescription = "date",
                    tint = Color(
                        66,
                        165,
                        245,
                        255
                    )
                )
            }
        )

        val gradient = Brush.horizontalGradient(
            colors = listOf(
                Color(66, 165, 245, 255),
                Color(38, 198, 218, 255)
            ),
            startX = 0f,
            endX = 1000f
        )
        val context = LocalContext.current
        Button(
            onClick = {
                if (selectedOptionText.isEmpty()) Toast.makeText(
                    context,
                    "Please select the city",
                    Toast.LENGTH_SHORT
                ).show()
                else if (checkin.isEmpty() || checkOut.isEmpty()) {
                    Toast.makeText(context, "Please check dates", Toast.LENGTH_SHORT).show()
                } else if (count == 0) Toast.makeText(
                    context,
                    "Select the number of rooms",
                    Toast.LENGTH_SHORT
                ).show()
                else search(selectedOptionText)
            },
            modifier = Modifier
                .fillMaxWidth()
                .background(gradient),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent
            )
        ) {
            Text(text = "SEARCH")
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "BEST PLACES",
                color = Color.Gray,
                fontWeight = FontWeight.Bold
            )
            TextButton(onClick = { viewAll() }) {
                Text(
                    text = "VIEW ALL",
                    fontWeight = FontWeight.Bold,
                    color = Color(66, 165, 245, 255),
                    fontSize = 16.sp
                )
            }
        }



        LazyRow(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.padding(vertical = 5.dp)
        ) {
            items(database) {
                RowCard(
                    item = it,
                    chosenPlace = chosenPlace
                )
            }
        }

        val bedFlag by hotelViewModel.bedFlag.collectAsState()

        if (bedFlag) {
            AlertDialogWithCounter(
                onDismissRequest = { hotelViewModel.setBedFlag(false) },
                hotelViewModel = hotelViewModel
            )
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyDatePicker(
    label: String,
    check: String,
    dateValue: (String) -> Unit,
) {
    val mContext = LocalContext.current
    val mYear: Int
    val mMonth: Int
    val mDay: Int

    val mCalendar = Calendar.getInstance()

    mYear = mCalendar.get(Calendar.YEAR)
    mMonth = mCalendar.get(Calendar.MONTH)
    mDay = mCalendar.get(Calendar.DAY_OF_MONTH)

    mCalendar.time = Date()


    val mDatePickerDialog = DatePickerDialog(
        mContext,
        { _: DatePicker, mYear: Int, mMonth: Int, mDayOfMonth: Int ->
            dateValue("$mDayOfMonth/${mMonth + 1}/$mYear")
        }, mYear, mMonth, mDay
    )


    TextField(
        value = check,
        onValueChange = {
            dateValue(it)
        },
        label = { Text(label) },
        enabled = false,
        modifier = Modifier
            .clickable { mDatePickerDialog.show() }
            .fillMaxWidth(),
        colors = TextFieldDefaults.textFieldColors(
            disabledLabelColor = MaterialTheme.colorScheme.onBackground,
            disabledTextColor = MaterialTheme.colorScheme.onBackground,
            containerColor = Color.White,
            disabledIndicatorColor = MaterialTheme.colorScheme.secondaryContainer

        ),
        leadingIcon = {
            Icon(
                imageVector = Icons.Outlined.DateRange, contentDescription = "date", tint = Color(
                    66,
                    165,
                    245,
                    255
                )
            )
        }
    )
}

@Composable
fun AlertDialogWithCounter(
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    hotelViewModel: HotelViewModel
) {

    val nCount by hotelViewModel.count.collectAsState()


    Dialog(onDismissRequest = { onDismissRequest() }) {
        Card(
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(225, 245, 254, 255)
            )
        ) {
            Text(
                text = "Number of Rooms",
                modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                textAlign = TextAlign.Center
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = {
                        if (nCount > 0) {
                            hotelViewModel.setCount(nCount - 1)
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowLeft,
                        contentDescription = "Decrease"
                    )
                }

                Text(
                    text = nCount.toString(),
                    modifier = Modifier.padding(horizontal = 16.dp),
                    fontSize = 20.sp
                )

                IconButton(
                    onClick = {
                        hotelViewModel.setCount(nCount + 1)
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowRight,
                        contentDescription = "Increase"
                    )
                }
            }

        }
    }
}

@Composable
fun RowCard(
    item: Places,
    chosenPlace: (Places) -> Unit
) {
    Card(
        modifier = Modifier
            .size(150.dp)
            .clickable { chosenPlace(item) }
    ) {
        AsyncImage(
            model = item.Image,
            contentDescription = item.Name,
            modifier = Modifier
                .fillMaxWidth()
                .height(128.dp),
            contentScale = ContentScale.Crop
        )
        Text(text = item.Name, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
    }
}


