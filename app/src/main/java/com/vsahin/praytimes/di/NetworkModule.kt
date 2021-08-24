package com.vsahin.praytimes.di

import com.vsahin.praytimes.data.PrayTimeService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {
    @Singleton
    @Provides
    fun provideEntryService(): PrayTimeService {
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(PrayTimeService.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(PrayTimeService::class.java)
    }
}