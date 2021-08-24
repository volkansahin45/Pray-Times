package com.vsahin.praytimes.data

import android.content.Context
import com.vsahin.praytimes.R
import com.vsahin.praytimes.data.entity.PrayTime
import com.vsahin.praytimes.data.entity.PrayTimeResponse
import com.vsahin.praytimes.data.uiModel.LocationUIModel
import com.vsahin.praytimes.di.IoDispatcher
import com.vsahin.praytimes.common.getCurrentDate
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PrayTimesRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    @IoDispatcher private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default,
    private val preferences: Preferences,
    private val service: PrayTimeService
) {
    private suspend fun getPrayTimes(): Result<PrayTimeResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val countryId = getCountryId()
                val cityId = getCityId()
                val districtId = getDistrictId()

                if (countryId.isNullOrEmpty()
                    || cityId.isNullOrEmpty()
                    || districtId.isNullOrEmpty()
                ) {
                    throw java.lang.Exception(context.getString(R.string.unknown_error))
                }

                val prayTimes = service.getPrayTimes(
                    countryId,
                    cityId,
                    districtId
                )
                Result.Success(prayTimes)
            } catch (e: Exception) {
                Result.Error(e)
            }
        }
    }

    suspend fun getPrayTimesToday(): Result<PrayTime> {
        return withContext(Dispatchers.IO) {
            try {
                val currentDateApi = getCurrentDate(context)
                when (val result = getPrayTimes()) {
                    is Result.Success -> {
                        val prayTimeToday = result.data.prayerTimes?.get(currentDateApi)!!
                        Result.Success(prayTimeToday)
                    }
                    is Result.Error -> {
                        Result.Error(result.exception)
                    }
                }
            } catch (e: Exception) {
                Result.Error(e)
            }
        }
    }

    suspend fun getCountries(): Result<List<LocationUIModel>> {
        return withContext(defaultDispatcher) {
            try {
                val countries = service.getCountries().countries
                val list = mutableListOf<LocationUIModel>()

                countries?.forEach {
                    if (!it.value.nameNative.isNullOrEmpty()) {
                        val name = it.value.nameNative ?: ""
                        list.add(LocationUIModel(it.key, name))
                    }
                }

                list.sortBy { it.name }
                Result.Success(list)
            } catch (e: Exception) {
                Result.Error(e)
            }
        }
    }

    suspend fun getCities(countryId: String?): Result<List<LocationUIModel>> {
        return withContext(defaultDispatcher) {
            try {
                if (countryId.isNullOrEmpty()) {
                    throw java.lang.Exception(context.getString(R.string.unknown_error))
                }

                val cities = service.getCities(countryId).cities
                val list = mutableListOf<LocationUIModel>()

                cities?.forEach {
                    if (it.value.name.isNotEmpty()) {
                        list.add(LocationUIModel(it.key, it.value.name))
                    }
                }

                list.sortBy { it.name }
                Result.Success(list)
            } catch (e: Exception) {
                Result.Error(e)
            }
        }
    }

    suspend fun getDistricts(countryId: String?, cityId: String?): Result<List<LocationUIModel>> {
        return withContext(defaultDispatcher) {
            try {
                if (countryId.isNullOrEmpty() || cityId.isNullOrEmpty()) {
                    throw java.lang.Exception(context.getString(R.string.unknown_error))
                }

                val districts = service.getDistricts(countryId, cityId).districts
                val list = mutableListOf<LocationUIModel>()

                districts?.forEach {
                    if (it.value.isNotEmpty()) {
                        list.add(LocationUIModel(it.key, it.value))
                    }
                }

                list.sortBy { it.name }
                Result.Success(list)
            } catch (e: Exception) {
                Result.Error(e)
            }
        }
    }

    fun isLocationSelected(): Boolean {
        return !preferences.getDistrictId().isNullOrEmpty()
    }

    fun getLocationName(): String? = preferences.getLocationName()
    fun setLocationName(locationName: String?) = preferences.setLocationName(locationName)

    private fun getCountryId() = preferences.getCountryId()
    private fun getCityId() = preferences.getCityId()
    private fun getDistrictId() = preferences.getDistrictId()

    fun setLocationIds(countryId: String?, cityId: String?, districtId: String?) =
        preferences.setLocationIds(countryId, cityId, districtId)
}