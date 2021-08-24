package com.vsahin.praytimes.data

import com.vsahin.praytimes.data.entity.CitiesResponse
import com.vsahin.praytimes.data.entity.CountriesResponse
import com.vsahin.praytimes.data.entity.DistrictResponse
import com.vsahin.praytimes.data.entity.PrayTimeResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface PrayTimeService {
    companion object {
        const val BASE_URL = "https://muezzin.herokuapp.com/"
    }

    @GET("/countries")
    suspend fun getCountries(): CountriesResponse

    @GET("/countries/{countryId}/cities")
    suspend fun getCities(@Path("countryId") countryId: String): CitiesResponse

    @GET("/countries/{countryId}/cities/{cityId}/districts")
    suspend fun getDistricts(
        @Path("countryId") countryId: String,
        @Path("cityId") cityId: String,
    ): DistrictResponse

    @GET("/prayerTimes/country/{countryId}/city/{cityId}/district/{districtId}")
    suspend fun getPrayTimes(
        @Path("countryId") countryId: String,
        @Path("cityId") cityId: String,
        @Path("districtId") districtId: String,
    ): PrayTimeResponse
}