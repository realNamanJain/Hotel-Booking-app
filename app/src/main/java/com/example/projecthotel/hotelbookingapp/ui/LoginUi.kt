package com.vishnurajan.hotelbookingapp.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider

@Composable
fun LoginUi(
    hotelViewModel: HotelViewModel
) {
    val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
        }

        override fun onVerificationFailed(e: FirebaseException) {
        }

        override fun onCodeSent(
            verificationId: String,
            token: PhoneAuthProvider.ForceResendingToken,
        ) {

            hotelViewModel.setVerificationId(verificationId)
            hotelViewModel.resetTimer()
            hotelViewModel.runTimer()
            hotelViewModel.setLoading(false)
        }
    }

    val otp by hotelViewModel.otp.collectAsState()
    val verificationId by hotelViewModel.verificationId.collectAsState()
    val loading by hotelViewModel.loading.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(66, 165, 245, 255),
                        Color(38, 198, 218, 255)
                    ),
                    startY = 0f,
                    endY = 2000f // Adjust the end position based on your requirement
                )
            )
    ) {
        Column {
            Text(
                text = "Sign Up",
                color = Color.White,
                modifier = Modifier.padding(start = 20.dp, top = 60.dp),
                fontSize = 26.sp
            )
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = Color.White, //Card background color
                    contentColor = Color.DarkGray  //Card content color,e.g.text
                ),
                modifier = Modifier.padding(start = 20.dp, end = 20.dp, bottom = 20.dp, top = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {


                    if (verificationId.isEmpty())
                        NumberScreen(
                            hotelViewModel = hotelViewModel,
                            callbacks = callbacks,
                        )
                    else
                        OtpScreen(
                            otp = otp,
                            hotelViewModel = hotelViewModel,
                            verificationId = verificationId,
                            callbacks = callbacks
                        )

                }
            }
        }
    }


    if (loading)
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color(255, 255, 255, 190))
        ) {

            CircularProgressIndicator()
            Text(text = "Loading")
        }



}