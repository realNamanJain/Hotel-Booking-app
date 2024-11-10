package com.vishnurajan.hotelbookingapp.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.projecthotel.R

@Composable
fun FAQ() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Image(painter = painterResource(id = R.drawable.fairfield), contentDescription = "FAQ", modifier = Modifier.width(300.dp).height(130.dp))
        Text(
            text = "What happens if I enter the wrong OTP during verification?",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color(66, 165, 245, 255),
            modifier = Modifier.fillMaxWidth()
                .padding(horizontal = 20.dp)
        )
        Text(
            text = "If you enter the wrong OTP, you'll be prompted to re-enter the correct code. Ensure to carefully input the code you received via SMS to proceed with the verification process successfully.",
            modifier = Modifier.fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 10.dp),
            textAlign = TextAlign.Justify
        )
        Text(
            text = "What happens if I encounter issues while booking a room?",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color(66, 165, 245, 255),
            modifier = Modifier.fillMaxWidth()
                .padding(horizontal = 20.dp)
        )
        Text(
            text = "If you encounter any issues during your booking process, please reach out to our customer support team by calling 9999955555.",
            modifier = Modifier.fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 10.dp),
            textAlign = TextAlign.Justify
        )
        Text(
            text = "How do I log out of the app?",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color(66, 165, 245, 255),
            modifier = Modifier.fillMaxWidth()
                .padding(horizontal = 20.dp)
        )
        Text(
            text = "To log out, navigate to the menu or profile section of the app and select the 'Logout' option. Upon logging out, you'll be redirected to the Send OTP screen.",
            modifier = Modifier.fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 10.dp),
            textAlign = TextAlign.Justify
        )
    }
}