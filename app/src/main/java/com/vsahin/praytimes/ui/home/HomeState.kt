package com.vsahin.praytimes.ui.home

import com.vsahin.praytimes.data.entity.PrayTime

data class HomeState(
    val prayTimesToday: PrayTime? = null,
    val locationName: String? = null,
    val isLoading: Boolean = false,
    val error: Exception? = null
)