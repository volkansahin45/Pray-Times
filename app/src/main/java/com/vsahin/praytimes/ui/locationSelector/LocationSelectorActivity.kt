package com.vsahin.praytimes.ui.locationSelector

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.wear.compose.material.ExperimentalWearMaterialApi
import com.vsahin.praytimes.ui.theme.PrayTimesTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LocationSelectorActivity : AppCompatActivity() {
    private val viewModel: LocationSelectorViewModel by viewModels()

    @ExperimentalWearMaterialApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            PrayTimesTheme {
                LocationSelectorScreen(
                    viewModel = viewModel,
                    onFinish = { finish() }
                )
            }
        }
    }
}