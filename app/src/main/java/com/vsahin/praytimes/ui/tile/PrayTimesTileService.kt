package com.vsahin.praytimes.ui.tile

import androidx.wear.tiles.*
import com.google.common.util.concurrent.Futures
import com.google.common.util.concurrent.ListenableFuture
import com.vsahin.praytimes.R
import com.vsahin.praytimes.common.getCurrentDate
import com.vsahin.praytimes.common.getCurrentDateReadable
import com.vsahin.praytimes.common.getEndOfDayInMillis
import com.vsahin.praytimes.data.PrayTimesRepository
import com.vsahin.praytimes.data.Result
import com.vsahin.praytimes.data.entity.PrayTime
import com.vsahin.praytimes.ui.home.HomeActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.guava.future
import javax.inject.Inject

private const val SUCCESS_MODIFIER_ID = "successModifierId"
private const val ERROR_MODIFIER_ID = "errorModifierId"
private const val LOCATION_MODIFIER_ID = "locationModifierId"

private const val RESOURCES_VERSION = "1"

@AndroidEntryPoint
class PrayTileService : TileService() {
    @Inject
    lateinit var repository: PrayTimesRepository
    private val serviceScope = CoroutineScope(Dispatchers.IO)

    override fun onTileRequest(requestParams: RequestBuilders.TileRequest) = serviceScope.future {
        val currentDateReadable = getCurrentDateReadable()
        val todayPrayTime: PrayTime?

        if (repository.isLocationSelected()) {
            when (val prayTimesResponse = repository.getPrayTimesToday()) {
                is Result.Success -> {
                    todayPrayTime = prayTimesResponse.data

                    successTile(currentDateReadable, todayPrayTime)
                }
                is Result.Error -> {
                    centerTextTile(
                        ERROR_MODIFIER_ID,
                        applicationContext.getString(R.string.unknown_error)
                    )
                }
            }
        } else {
            centerTextTile(
                LOCATION_MODIFIER_ID,
                applicationContext.getString(R.string.location_should_be_selected_first)
            )
        }
    }

    override fun onResourcesRequest(requestParams: RequestBuilders.ResourcesRequest): ListenableFuture<ResourceBuilders.Resources> {
        return Futures.immediateFuture(
            ResourceBuilders.Resources.Builder()
                .setVersion(RESOURCES_VERSION)
                .build()
        )
    }

    private fun successTile(
        currentDateReadable: String,
        todayPrayTime: PrayTime?
    ): TileBuilders.Tile {
        return TileBuilders.Tile.Builder()
            .setResourcesVersion(RESOURCES_VERSION)
            .setTimeline(
                TimelineBuilders.Timeline.Builder().addTimelineEntry(
                    TimelineBuilders.TimelineEntry.Builder()
                        .setValidity(
                            TimelineBuilders.TimeInterval.Builder()
                                .setEndMillis( // refresh the tile at the end of the day
                                    getEndOfDayInMillis(this, getCurrentDate(this))
                                ).build()
                        )
                        .setLayout(
                            LayoutElementBuilders.Layout.Builder().setRoot(
                                successLayout(currentDateReadable, todayPrayTime)
                            ).build()
                        ).build()
                ).build()
            ).build()
    }

    private fun successLayout(
        currentDateReadable: String,
        prayTime: PrayTime?
    ) =
        LayoutElementBuilders.Column.Builder()
            .setModifiers(
                ModifiersBuilders.Modifiers.Builder()
                    .setClickable(
                        ModifiersBuilders.Clickable.Builder()
                            .setId(SUCCESS_MODIFIER_ID)
                            .setOnClick(
                                ActionBuilders.LaunchAction.Builder()
                                    .setAndroidActivity(
                                        ActionBuilders.AndroidActivity.Builder()
                                            .setClassName(HomeActivity::class.qualifiedName ?: "")
                                            .setPackageName(this.packageName)
                                            .build()
                                    ).build()
                            ).build()
                    ).build()
            )
            .addContent(
                LayoutElementBuilders.Text.Builder()
                    .setText(currentDateReadable)
                    .build()
            )
            .addContent(
                LayoutElementBuilders.Text.Builder()
                    .setText(repository.getLocationName() ?: "")
                    .build()
            )
            .addContent(
                LayoutElementBuilders.Text.Builder()
                    .setText("${getString(R.string.fajr)}: ${prayTime?.fajr}")
                    .build()
            )
            .addContent(
                LayoutElementBuilders.Text.Builder()
                    .setText("${getString(R.string.shuruq)}: ${prayTime?.shuruq}")
                    .build()
            )
            .addContent(
                LayoutElementBuilders.Text.Builder()
                    .setText("${getString(R.string.dhuhr)}: ${prayTime?.dhuhr}")
                    .build()
            )
            .addContent(
                LayoutElementBuilders.Text.Builder()
                    .setText("${getString(R.string.asr)}: ${prayTime?.asr}")
                    .build()
            )
            .addContent(
                LayoutElementBuilders.Text.Builder()
                    .setText("${getString(R.string.maghrib)}: ${prayTime?.maghrib}")
                    .build()
            )
            .addContent(
                LayoutElementBuilders.Text.Builder()
                    .setText("${getString(R.string.isha)}: ${prayTime?.isha}")
                    .build()
            )
            .build()

    private fun centerTextTile(
        modifier: String = "",
        text: String
    ): TileBuilders.Tile {
        return TileBuilders.Tile.Builder()
            .setResourcesVersion(RESOURCES_VERSION)
            .setTimeline(
                TimelineBuilders.Timeline.Builder().addTimelineEntry(
                    TimelineBuilders.TimelineEntry.Builder().setLayout(
                        LayoutElementBuilders.Layout.Builder().setRoot(
                            centerTextLayout(modifier, text)
                        ).build()
                    ).build()
                ).build()
            ).build()
    }

    private fun centerTextLayout(
        modifier: String = "",
        text: String
    ) =
        LayoutElementBuilders.Column.Builder()
            .setModifiers(
                ModifiersBuilders.Modifiers.Builder()
                    .setClickable(
                        ModifiersBuilders.Clickable.Builder()
                            .setId(modifier)
                            .setOnClick(
                                ActionBuilders.LaunchAction.Builder()
                                    .setAndroidActivity(
                                        ActionBuilders.AndroidActivity.Builder()
                                            .setClassName(HomeActivity::class.qualifiedName ?: "")
                                            .setPackageName(this.packageName)
                                            .build()
                                    ).build()
                            ).build()
                    ).build()
            )
            .addContent(
                LayoutElementBuilders.Text.Builder()
                    .setMaxLines(5)
                    .setText(text)
                    .build()
            )
            .build()
}