package com.vsahin.praytimes.ui.home

import android.widget.Toast
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.wear.compose.material.*
import com.vsahin.praytimes.R
import com.vsahin.praytimes.common.getCurrentDateReadable
import com.vsahin.praytimes.ui.ABOUT
import com.vsahin.praytimes.ui.LOCATION_SELECTOR
import com.vsahin.praytimes.ui.common.exception.LocationIsNotSelectedException
import com.vsahin.praytimes.ui.components.LoadingIndicator

@ExperimentalWearMaterialApi
@Composable
fun HomeScreen(
    navController: NavHostController,
    viewModel: HomeViewModel
) {
    val context = LocalContext.current
    val homeState = viewModel.state.observeAsState()
    val state = homeState.value!!

    val prayTimesToday = state.prayTimesToday
    val prayTimes: Array<String> = if (prayTimesToday == null) {
        emptyArray()
    } else {
        arrayOf(
            "${stringResource(id = R.string.fajr)}: ${prayTimesToday.fajr}",
            "${stringResource(id = R.string.shuruq)}: ${prayTimesToday.shuruq}",
            "${stringResource(id = R.string.dhuhr)}: ${prayTimesToday.dhuhr}",
            "${stringResource(id = R.string.asr)}: ${prayTimesToday.asr}",
            "${stringResource(id = R.string.maghrib)}: ${prayTimesToday.maghrib}",
            "${stringResource(id = R.string.isha)}: ${prayTimesToday.isha}"
        )
    }

    state.error?.let {
        when (state.error) {
            is LocationIsNotSelectedException -> {
                navController.navigate(LOCATION_SELECTOR)
                //onFinish?.invoke()
                return
            }
            else -> {
                Toast.makeText(
                    context,
                    stringResource(R.string.unknown_error),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    if (state.isLoading) {
        LoadingIndicator()
    } else {
        HomeContent(
            navController = navController,
            state = state,
            prayTimes = prayTimes,
            onRefresh = { viewModel.init() }
        )
    }
}

@Composable
fun HomeContent(
    state: HomeState,
    prayTimes: Array<String>,
    onRefresh: () -> Unit,
    navController: NavHostController
) {
    val scalingLazyListState = ScalingLazyListState()

    ScalingLazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            top = 10.dp,
            start = 10.dp,
            end = 10.dp,
            bottom = 40.dp
        ),
        horizontalAlignment = Alignment.CenterHorizontally,
        state = scalingLazyListState
    ) {
        item {
            Text(text = getCurrentDateReadable())
        }

        item {
            Text(fontSize = 16.sp, text = state.locationName.toString())
        }

        items(prayTimes) {
            Text(text = it)
        }

        item {
            if (state.error != null) {
                Button(onClick = { onRefresh() }) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = stringResource(R.string.refresh),
                    )
                }
            }
        }

        item {
            Chip(
                icon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_baseline_edit_location_24),
                        contentDescription = stringResource(id = R.string.edit_location)
                    )
                },
                label = {
                    Text(stringResource(id = R.string.edit_location))
                },
                onClick = {
                    navController.navigate(LOCATION_SELECTOR)
                }
            )
        }

        item {
            Chip(
                icon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_baseline_about_24),
                        contentDescription = stringResource(id = R.string.about)
                    )
                },
                label = {
                    Text(stringResource(id = R.string.about))
                },
                onClick = {
                    navController.navigate(ABOUT)
                }
            )
        }
    }
}
