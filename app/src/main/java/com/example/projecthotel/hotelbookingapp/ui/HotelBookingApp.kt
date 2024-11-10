package com.vishnurajan.hotelbookingapp.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.projecthotel.R
import com.google.firebase.auth.FirebaseAuth


enum class HotelAppScreen(val title: String) {
    Start(title = "Find Room"),
    Hotel(title = "Select Hotel"),
    Room(title = "Select Room"),
    Checkout(title = "Checkout"),
    Where2Go(title = "Where2Go"),
    ChosenPlace(title = "Places"),
    FAQ(title = "BookNest FAQs")
}
var canNavigateBack = false
val auth = FirebaseAuth.getInstance()

@Composable
fun HotelBookingApp(
    navController: NavHostController = rememberNavController(),
    hotelViewModel: HotelViewModel = viewModel()
    ) {

    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = HotelAppScreen.valueOf(
        backStackEntry?.destination?.route ?: HotelAppScreen.Start.name
    )
    canNavigateBack = navController.previousBackStackEntry != null
    hotelViewModel.setUser(auth.currentUser)
    val user by hotelViewModel.user.collectAsState()

    if (user == null) {
        LoginUi(hotelViewModel = hotelViewModel)
    } else {
        hotelViewModel.readUser()
        FullApp(currentScreen = currentScreen, navController = navController, hotelViewModel = hotelViewModel)
    }
}

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun FullApp(
        currentScreen: HotelAppScreen,
        navController: NavHostController,
        hotelViewModel: HotelViewModel,
    ) {
        val chosenPlace by hotelViewModel.chosenPlace.collectAsState()
        val chosenHotel by hotelViewModel.chosenHotel.collectAsState()
        hotelViewModel.readDatabase()
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = currentScreen.title,
                                    fontSize = 26.sp,
                                    fontFamily = FontFamily.SansSerif,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                            Row(
                                modifier = Modifier.clickable {
                                    hotelViewModel.setLogoutStatus(true)
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                                    contentDescription = "Logout",
                                    modifier = Modifier.size(24.dp)
                                )
                                Text(
                                    text = "Logout",
                                    fontSize = 18.sp,
                                    modifier = Modifier.padding(end = 14.dp, start = 4.dp)
                                )
                            }

                        }
                    },
                    navigationIcon = {
                        if (canNavigateBack) {
                            IconButton(onClick = {
                                navController.navigateUp()
                            }) {
                                Icon(
                                    imageVector = Icons.Filled.ArrowBack,
                                    contentDescription = "Back Button"
                                )
                            }
                        }
                    }
                )
            },
            bottomBar = {
                HotelAppBar(
                    navController = navController
                )
            }

        ) {

            NavHost(
                navController = navController,
                startDestination = HotelAppScreen.Start.name,
                modifier = Modifier.padding(it)
            ) {

                composable(route = HotelAppScreen.Start.name) {
                    StartScreen(
                        hotelViewModel,
                        viewAll = {
                            navController.navigate(HotelAppScreen.Where2Go.name)
                        },
                        search = { city ->
                            hotelViewModel.setCity(city)
                            hotelViewModel.downloadHotels()
                            navController.navigate(HotelAppScreen.Hotel.name)
                        },
                        chosenPlace = {
                            hotelViewModel.setChosenPlace(it)
                            navController.navigate(HotelAppScreen.ChosenPlace.name)
                        }
                    )
                }

                composable(route = HotelAppScreen.Hotel.name) {
                    SelectHotel(
                        hotelViewModel
                    ) { hotel ->
                        hotelViewModel.setHotel(hotel)
                        navController.navigate(HotelAppScreen.Room.name)
                    }
                }

                composable(route = HotelAppScreen.Where2Go.name) {
                    BestPlaces(
                        hotelViewModel
                    ) {
                        hotelViewModel.setChosenPlace(it)
                        navController.navigate(HotelAppScreen.ChosenPlace.name)
                    }
                }

                composable(route = HotelAppScreen.ChosenPlace.name) {
                    ChosenPlace(
                        chosenPlace
                    )
                }

                composable(route = HotelAppScreen.Room.name) {
                    SelectRoom(
                        hotels = chosenHotel,
                        hotelViewModel = hotelViewModel,
                        finishCheckout = {
                            navController.navigate(HotelAppScreen.Checkout.name)
                        }
                        )
                }

                composable(route = HotelAppScreen.Checkout.name) {
                    Checkout(
                        hotelViewModel = hotelViewModel
                    )
                }

                composable(route = HotelAppScreen.FAQ.name) {
                    FAQ(
                    )
                }
            }


        }
        val logoutClicked by hotelViewModel.logoutClicked.collectAsState()
        if (logoutClicked) {
            AlertCheck(
                onYesButtonPressed = {
                    hotelViewModel.setLogoutStatus(false)
                    auth.signOut()
                    hotelViewModel.clearData()

                },
                onNoButtonPressed = {
                    hotelViewModel.setLogoutStatus(false)
                }
            )
        }
    }


    @Composable
    fun HotelAppBar(
        navController: NavHostController
    ) {

        val gradient = Brush.horizontalGradient(
            colors = listOf(
                Color(66, 165, 245, 255),
                Color(38, 198, 218, 255)
            ),
            startX = 0f,
            endX = 1000f
        )

        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier
                .fillMaxWidth()
                .background(gradient)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier
                    .clickable {
                        navController.navigate(HotelAppScreen.Start.name) {
                            popUpTo(0)
                        }
                    }
                    .padding(horizontal = 40.dp, vertical = 10.dp)
            ) {
                Icon(imageVector = Icons.Outlined.Home, contentDescription = "Home", tint = Color.White)
                Text(text = "Home", fontSize = 10.sp, color = Color.White)
            }


                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier
                        .clickable {
                            navController.navigate(HotelAppScreen.Where2Go.name) {
                                // popUpTo(0)
                            }
                        }
                        .padding(horizontal = 40.dp, vertical = 10.dp)
                ) {
                    Icon(imageVector = Icons.Outlined.LocationOn, contentDescription = "Checkout", tint = Color.White)
                    Text(text = "Where2Go", fontSize = 10.sp, color = Color.White)
                }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier
                    .clickable {
                        navController.navigate(HotelAppScreen.FAQ.name) {
                            // popUpTo(0)
                        }
                    }
                    .padding(horizontal = 40.dp, vertical = 10.dp)
            ) {
                Icon(imageVector = Icons.Outlined.Info, contentDescription = "FAQ", tint = Color.White)
                Text(text = "FAQs", fontSize = 10.sp, color = Color.White)
            }
        }
    }

    @Composable
    fun AlertCheck(
        onYesButtonPressed: () -> Unit,
        onNoButtonPressed: () -> Unit

    ) {
        AlertDialog(
            onDismissRequest = { onNoButtonPressed() },
            confirmButton = {
                TextButton(
                    onClick = {
                        onYesButtonPressed()
                    }
                ) {
                    Text(text = "Yes")
                }
            },
            title = {
                Text(text = "Logout?", fontWeight = FontWeight.Bold)
            },
            text = {
                Text(text = "Are you sure you want to logout?")
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        onNoButtonPressed()
                    }
                ) {
                    Text("No")
                }
            },
            containerColor = Color(225, 245, 254, 255)
        )
    }