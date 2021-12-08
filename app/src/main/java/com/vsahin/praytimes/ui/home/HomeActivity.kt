package com.vsahin.praytimes.ui.home

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.wear.compose.material.ExperimentalWearMaterialApi
import com.vsahin.praytimes.ui.theme.PrayTimesTheme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {
    private val viewModel: HomeViewModel by viewModels()

    @ExperimentalWearMaterialApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            PrayTimesTheme {
                HomeScreen(
                    viewModel = viewModel,
                    onFinish = { finish() },
                    onRefresh = { viewModel.init() }
                )
            }
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        viewModel.init()
    }
}