package com.vsahin.praytimes.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vsahin.praytimes.data.PrayTimesRepository
import com.vsahin.praytimes.data.Result
import com.vsahin.praytimes.ui.common.exception.LocationDidNotSelectedException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: PrayTimesRepository
) : ViewModel() {
    private var _state = MutableLiveData(HomeState())
    val state: LiveData<HomeState> = _state

    init {
       init()
    }

    fun init(){
        _state.value = _state.value?.copy(isLoading = true, locationName = repository.getLocationName())

        if(!repository.isLocationSelected()) {
            _state.value = _state.value?.copy(isLoading = false, error = LocationDidNotSelectedException())
            return
        }

        viewModelScope.launch {
            _state.value = when (val result = repository.getPrayTimesToday()) {
                is Result.Success -> {
                    _state.value?.copy(isLoading = false, prayTimesToday = result.data)
                }
                is Result.Error -> {
                    _state.value?.copy(isLoading = false, error = result.exception)
                }
            }
        }
    }
}