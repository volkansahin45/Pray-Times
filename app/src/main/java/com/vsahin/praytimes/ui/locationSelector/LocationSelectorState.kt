package com.vsahin.praytimes.ui.locationSelector

import com.vsahin.praytimes.data.uiModel.LocationUIModel

data class LocationSelectorState(
    val locationType: LocationType = LocationType.COUNTRY,
    val locationName: String? = null,
    val countryId: String? = null,
    val cityId: String? = null,
    val districtId: String? = null,
    val locations: List<LocationUIModel> = listOf(),
    val isLoading: Boolean = false,
    val error: Exception? = null
)