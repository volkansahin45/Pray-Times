package com.vsahin.praytimes.common

import android.content.Context
import com.vsahin.praytimes.R
import java.time.LocalDateTime
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

fun getEndOfDayInMillis(context: Context, todayDate: String): Long {
    val dateTime = "$todayDate 23:59"
    val zonedDateTime = ZonedDateTime.now()
    val localDateTime: LocalDateTime =
        LocalDateTime.parse(dateTime, DateTimeFormatter.ofPattern(context.getString(R.string.date_time_format)))

    return localDateTime
        .plusMinutes(1)
        .toEpochSecond(zonedDateTime.offset)
}