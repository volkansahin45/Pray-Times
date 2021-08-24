package com.vsahin.praytimes.data.entity

data class PrayTimeResponse(
    val prayerTimes: HashMap<String, PrayTime>? = null
)