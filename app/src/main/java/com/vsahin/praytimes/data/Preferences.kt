package com.vsahin.praytimes.data

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

private const val LOCATION_NAME = "locationName"
private const val COUNTRY_ID = "countryId"
private const val CITY_ID = "cityId"
private const val DISTRICT_ID = "districtId"

class Preferences @Inject constructor(
    @ApplicationContext val context: Context
) {
    private val preferences: SharedPreferences
        get() = PreferenceManager.getDefaultSharedPreferences(context)

    fun getCountryId(): String? = preferences.getString(COUNTRY_ID, null)
    fun getCityId(): String? = preferences.getString(CITY_ID, null)
    fun getDistrictId(): String? = preferences.getString(DISTRICT_ID, null)

    fun setLocationIds(countryId: String?, cityId: String?, districtId: String?) {
        preferences
            .edit()
            .putString(COUNTRY_ID, countryId)
            .putString(CITY_ID, cityId)
            .putString(DISTRICT_ID, districtId)
            .apply()
    }

    fun getLocationName(): String? = preferences.getString(LOCATION_NAME, null)

    fun setLocationName(locationName: String?){
        preferences
            .edit()
            .putString(LOCATION_NAME, locationName)
            .apply()
    }
}