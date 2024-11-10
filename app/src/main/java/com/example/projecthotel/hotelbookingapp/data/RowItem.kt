package com.vishnurajan.hotelbookingapp.data

import androidx.annotation.DrawableRes

data class RowItem(
    val title: String,
    val desc: String,
    @DrawableRes
    val image: Int
)