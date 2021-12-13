package com.vsahin.praytimes.ui.locationSelector

import android.content.Context
import android.content.Intent
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.wear.compose.material.ScalingLazyColumn
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.items
import androidx.wear.tiles.TileService
import com.vsahin.praytimes.ui.HOME
import com.vsahin.praytimes.ui.components.LoadingIndicator
import com.vsahin.praytimes.ui.tile.PrayTileService

@Composable
fun LocationSelectorScreen(
    navController: NavController,
    viewModel: LocationSelectorViewModel
) {
    val context = LocalContext.current
    val locationSelectorState = viewModel.state.observeAsState()
    val state = locationSelectorState.value!!
    val locationType = state.locationType

    BackHandler(
        enabled = locationType != LocationType.COUNTRY
    ) {
        when (locationType) {
            LocationType.CITY -> {
                viewModel.setLocationType(LocationType.COUNTRY)
            }
            LocationType.DISTRICT -> {
                viewModel.setLocationType(LocationType.CITY)
            }
            else -> {}
        }
    }

    if (state.isLoading) {
        LoadingIndicator()
    } else {
        ScalingLazyColumn(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = PaddingValues(vertical = 50.dp)
        ) {
            item {
                Text(
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    text = locationType.toString(),
                    textAlign = TextAlign.Center
                )
            }

            items(state.locations) {
                Text(
                    modifier = Modifier
                        .padding(vertical = 8.dp)
                        .clickable {
                            viewModel.setLocationId(it.id)
                            when (locationType) {
                                LocationType.COUNTRY -> viewModel.setLocationType(LocationType.CITY)
                                LocationType.CITY -> viewModel.setLocationType(LocationType.DISTRICT)
                                LocationType.DISTRICT -> {
                                    viewModel.setLocationName(it.name)
                                    viewModel.saveLocationName()
                                    viewModel.saveLocationIds()
                                    refreshTile(context)
                                    navController.navigate(HOME)
                                }
                            }
                        },
                    text = it.name
                )
            }
        }
    }
}

private fun refreshTile(context: Context) {
    TileService.getUpdater(context)
        .requestUpdate(PrayTileService::class.java)
}