package com.vsahin.praytimes.ui.locationSelector

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vsahin.praytimes.data.PrayTimesRepository
import com.vsahin.praytimes.data.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LocationSelectorViewModel @Inject constructor(
    private val repository: PrayTimesRepository
) : ViewModel() {
    private var _state = MutableLiveData(LocationSelectorState())
    val state: LiveData<LocationSelectorState> = _state

    init {
        setLocationType(LocationType.COUNTRY)
    }

    fun setLocationType(locationType: LocationType) {
        _state.value =
            _state.value?.copy(isLoading = true, locations = listOf(), locationType = locationType)

        viewModelScope.launch {
            val result = when (locationType) {
                LocationType.COUNTRY -> repository.getCountries()
                LocationType.CITY -> repository.getCities(state.value?.countryId)
                LocationType.DISTRICT -> repository.getDistricts(
                    state.value?.countryId,
                    state.value?.cityId
                )
            }

            _state.value = when (result) {
                is Result.Success -> {
                    _state.value?.copy(isLoading = false, locations = result.data)
                }
                is Result.Error -> {
                    _state.value?.copy(isLoading = false, error = result.exception)
                }
            }
        }
    }

    fun setLocationId(locationId: String) {
        _state.value = when (state.value?.locationType) {
            LocationType.COUNTRY -> _state.value?.copy(countryId = locationId)
            LocationType.CITY -> _state.value?.copy(cityId = locationId)
            LocationType.DISTRICT -> _state.value?.copy(districtId = locationId)
            null -> _state.value
        }
    }

    fun saveLocationIds() {
        repository.setLocationIds(
            state.value?.countryId,
            state.value?.cityId,
            state.value?.districtId,
        )
    }

    fun setLocationName(locationName: String) {
        _state.value = _state.value?.copy(locationName = locationName)
    }

    fun saveLocationName() {
        repository.setLocationName(
            state.value?.locationName,
        )
    }
}