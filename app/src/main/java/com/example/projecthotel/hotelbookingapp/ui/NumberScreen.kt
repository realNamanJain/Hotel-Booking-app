package com.vishnurajan.hotelbookingapp.ui

import android.app.Activity
import android.text.format.DateUtils
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.PhoneAuthProvider.OnVerificationStateChangedCallbacks
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NumberScreen(
    hotelViewModel: HotelViewModel,
    callbacks: OnVerificationStateChangedCallbacks
) {
    val phoneNumber by hotelViewModel.phoneNumber.collectAsState()
    val fullName by hotelViewModel.fullName.collectAsState()
    val email by hotelViewModel.email.collectAsState()

    val ticks by hotelViewModel.ticks.collectAsState()
    val context = LocalContext.current

    OutlinedTextField(
        value = fullName,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
        onValueChange = { hotelViewModel.setFullName(it) },
        label = { Text(text = "Full Name") },
        modifier = Modifier
            .fillMaxWidth(),
        singleLine = true,
        leadingIcon = { Icon(Icons.Outlined.AccountCircle, "") }
    )

    OutlinedTextField(
        value = email,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
        onValueChange = { hotelViewModel.setEmail(it) },
        label = { Text(text = "Email") },
        modifier = Modifier
            .fillMaxWidth(),
        singleLine = true,
        leadingIcon = { Icon(Icons.Outlined.Email, "") }
    )


    OutlinedTextField(
        value = phoneNumber,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        onValueChange = { hotelViewModel.setPhoneNumber(it) },
        label = { Text(text = "Mobile Number") },
        modifier = Modifier
            .fillMaxWidth(),
        singleLine = true,
        leadingIcon = { Icon(Icons.Outlined.Phone, "") }
    )

    Spacer(modifier = Modifier.padding(top = 0.4.dp))

    Button(
        enabled = (ticks == 0L || ticks == 60L),
        onClick = {
            val options = PhoneAuthOptions.newBuilder(auth)
                .setPhoneNumber("+91$phoneNumber") // Phone number to verify
                .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                .setActivity(context as Activity) // Activity (for callback binding)
                .setCallbacks(callbacks) // OnVerificationStateChangedCallbacks
                .build()
            PhoneAuthProvider.verifyPhoneNumber(options)
            hotelViewModel.setLoading(true)
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        shape = RoundedCornerShape(6.dp)
    ) {
        Text(text = if (ticks == 0L || ticks == 60L) "Send OTP" else "Send OTP ${DateUtils.formatElapsedTime(ticks)}")
    }
}