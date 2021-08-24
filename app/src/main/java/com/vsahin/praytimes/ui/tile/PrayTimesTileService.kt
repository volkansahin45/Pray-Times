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
class PrayTileService : TileProviderService() {
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
            ResourceBuilders.Resources.builder()
                .setVersion(RESOURCES_VERSION)
                .build()
        )
    }

    private fun successTile(
        currentDateReadable: String,
        todayPrayTime: PrayTime?
    ): TileBuilders.Tile {
        return TileBuilders.Tile.builder()
            .setResourcesVersion(RESOURCES_VERSION)
            .setTimeline(
                TimelineBuilders.Timeline.builder().addTimelineEntry(
                    TimelineBuilders.TimelineEntry.builder()
                        .setValidity(
                            TimelineBuilders.TimeInterval.builder()
                                .setEndMillis( // refresh the tile at the end of the day
                                    getEndOfDayInMillis(this, getCurrentDate(this))
                                )
                        )
                        .setLayout(
                            LayoutElementBuilders.Layout.builder().setRoot(
                                successLayout(currentDateReadable, todayPrayTime)
                            )
                        )
                )
            ).build()
    }

    private fun successLayout(
        currentDateReadable: String,
        prayTime: PrayTime?
    ) =
        LayoutElementBuilders.Column.builder()
            .setModifiers(
                ModifiersBuilders.Modifiers.builder()
                    .setClickable(
                        ModifiersBuilders.Clickable.builder()
                            .setId(SUCCESS_MODIFIER_ID)
                            .setOnClick(
                                ActionBuilders.LaunchAction.builder()
                                    .setAndroidActivity(
                                        ActionBuilders.AndroidActivity.builder()
                                            .setClassName(HomeActivity::class.qualifiedName ?: "")
                                            .setPackageName(this.packageName)
                                    )
                            )
                    )
            )
            .addContent(
                LayoutElementBuilders.Text.builder()
                    .setText(currentDateReadable)
            )
            .addContent(
                LayoutElementBuilders.Text.builder()
                    .setText(repository.getLocationName() ?: "")
            )
            .addContent(
                LayoutElementBuilders.Text.builder()
                    .setText("${getString(R.string.fajr)}: ${prayTime?.fajr}")
            )
            .addContent(
                LayoutElementBuilders.Text.builder()
                    .setText("${getString(R.string.shuruq)}: ${prayTime?.shuruq}")
            )
            .addContent(
                LayoutElementBuilders.Text.builder()
                    .setText("${getString(R.string.dhuhr)}: ${prayTime?.dhuhr}")
            )
            .addContent(
                LayoutElementBuilders.Text.builder()
                    .setText("${getString(R.string.asr)}: ${prayTime?.asr}")
            )
            .addContent(
                LayoutElementBuilders.Text.builder()
                    .setText("${getString(R.string.maghrib)}: ${prayTime?.maghrib}")
            )
            .addContent(
                LayoutElementBuilders.Text.builder()
                    .setText("${getString(R.string.isha)}: ${prayTime?.isha}")
            )
            .build()

    private fun centerTextTile(
        modifier: String = "",
        text: String
    ): TileBuilders.Tile {
        return TileBuilders.Tile.builder()
            .setResourcesVersion(RESOURCES_VERSION)
            .setTimeline(
                TimelineBuilders.Timeline.builder().addTimelineEntry(
                    TimelineBuilders.TimelineEntry.builder().setLayout(
                        LayoutElementBuilders.Layout.builder().setRoot(
                            centerTextLayout(modifier, text)
                        )
                    )
                )
            ).build()
    }

    private fun centerTextLayout(
        modifier: String = "",
        text: String
    ) =
        LayoutElementBuilders.Column.builder()
            .setModifiers(
                ModifiersBuilders.Modifiers.builder()
                    .setClickable(
                        ModifiersBuilders.Clickable.builder()
                            .setId(modifier)
                            .setOnClick(
                                ActionBuilders.LaunchAction.builder()
                                    .setAndroidActivity(
                                        ActionBuilders.AndroidActivity.builder()
                                            .setClassName(HomeActivity::class.qualifiedName ?: "")
                                            .setPackageName(this.packageName)
                                    )
                            )
                    )
            )
            .addContent(
                LayoutElementBuilders.Text.builder()
                    .setMaxLines(5)
                    .setText(text)
            )
            .build()
}