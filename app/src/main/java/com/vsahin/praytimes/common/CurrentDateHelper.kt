package com.vsahin.praytimes.common

import android.content.Context
import com.vsahin.praytimes.R
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

fun getCurrentDate(applicationContext: Context): String {
    val currentDate = LocalDate.now()

    val formatterForApi =
        DateTimeFormatter.ofPattern(applicationContext.getString(R.string.date_format_for_api))

    return currentDate.format(formatterForApi)
}

fun getCurrentDateReadable(): String {
    val currentDate = LocalDate.now()

    val formatterForReadable = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)
    return currentDate.format(formatterForReadable)
}