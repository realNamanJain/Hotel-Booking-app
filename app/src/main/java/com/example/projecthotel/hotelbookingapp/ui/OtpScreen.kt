package com.vishnurajan.hotelbookingapp.ui

import android.app.Activity
import android.content.Context
import android.text.format.DateUtils
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.PhoneAuthProvider.OnVerificationStateChangedCallbacks
import java.util.concurrent.TimeUnit

@Composable
fun OtpScreen(
    otp: String,
    hotelViewModel: HotelViewModel,
    verificationId: String,
    callbacks: OnVerificationStateChangedCallbacks
) {
    val context = LocalContext.current
    val ticks by hotelViewModel.ticks.collectAsState()
    val phoneNumber by hotelViewModel.phoneNumber.collectAsState()
    val email by hotelViewModel.email.collectAsState()
    val serverUser by hotelViewModel.serverUser.collectAsState()

    Spacer(modifier = Modifier.padding(10.dp))

    Text(
        text = "Verify Mobile Number",
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp,
        modifier = Modifier.fillMaxWidth()
    )
    
    Text(
        text = "OTP has been sent to you on your\n" +
                "mobile number, please enter it below",
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold,
        color = Color.Gray,
        modifier = Modifier.fillMaxWidth()
    )

    OtpTextBox(
        otp,
        hotelViewModel
    )

    FilledTonalButton(
        onClick = {
        if (otp.isEmpty()) {
            Toast.makeText(context, "Please enter otp", Toast.LENGTH_SHORT).show()
        } else {
            val credential = PhoneAuthProvider.getCredential(verificationId, otp)
            signInWithPhoneAuthCredential(
                credential,
                context,
                hotelViewModel,
                email, serverUser.email
            )
        }
    },
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        shape = RoundedCornerShape(6.dp)) {
        Text(text = "Verify OTP")
    }

    FilledTonalButton(
        onClick = {
            if (ticks == 0L) {
                val options = PhoneAuthOptions.newBuilder(auth)
                    .setPhoneNumber("+91$phoneNumber") // Phone number to verify
                    .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                    .setActivity(context as Activity) // Activity (for callback binding)
                    .setCallbacks(callbacks) // OnVerificationStateChangedCallbacks
                    .build()
                PhoneAuthProvider.verifyPhoneNumber(options)
            } else Toast.makeText(context, "Please wait for the timer to finish", Toast.LENGTH_SHORT).show()
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        shape = RoundedCornerShape(6.dp)) {
        Text(text = if (ticks == 0L) "Resend OTP" else "Resend OTP (${DateUtils.formatElapsedTime(ticks)})")
    }

        Button(
            onClick = {
                hotelViewModel.setVerificationId("")
                hotelViewModel.setOtp("")
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(6.dp)
        ) {
            Text(text = "Edit Phone Number")
        }

}


@Composable
fun OtpTextBox(
    otp: String,
    hotelViewModel: HotelViewModel
) {

    BasicTextField(
        value = otp,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        onValueChange = {
            hotelViewModel.setOtp(it)
                        },
        modifier = Modifier
            .fillMaxWidth(),
        singleLine = true
    ) {
        Row(
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(6) {index ->
                val number = when {
                    index >= otp.length -> ""
                    else -> otp[index].toString()
                }
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(4.dp)) {

                    Box(
                        modifier = Modifier
                            .width(42.dp)
                            .height(50.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .border(
                                    width = 2.dp, // Border width
                                    color = Color.Gray, // Border color
                                    shape = RoundedCornerShape(8.dp) // Adjust corner radius
                                )
                        ) {
                            Text(
                                text = number,
                                fontSize = 32.sp,
                                modifier = Modifier
                                    .fillMaxSize(),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }
    }

}

private fun signInWithPhoneAuthCredential(
    credential: PhoneAuthCredential,
    context: Context,
    hotelViewModel: HotelViewModel,
    email: String,
    serverEmail:String
) {
    auth.signInWithCredential(credential)
        .addOnCompleteListener() { task ->
            if (task.isSuccessful) {
                // Sign in success, update UI with the signed-in user's information
                Toast.makeText(context, "Verification Successful", Toast.LENGTH_SHORT).show()
                val user = task.result?.user
                hotelViewModel.setUser(user)
                hotelViewModel.saveUserData()

            } else {
                // Sign in failed, display a message and update the UI
                if (task.exception is FirebaseAuthInvalidCredentialsException) {
                    // The verification code entered was invalid
                    Toast.makeText(context, "The OTP you have entered is invalid. Please try again", Toast.LENGTH_SHORT).show()

                }
                // Update UI
            }
        }
}