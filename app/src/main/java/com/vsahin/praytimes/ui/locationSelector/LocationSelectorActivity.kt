package com.vsahin.praytimes.ui.locationSelector

import android.content.Intent
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.wear.widget.WearableLinearLayoutManager
import com.vsahin.praytimes.databinding.ActivityLocationSelectorBinding
import com.vsahin.praytimes.ui.home.HomeActivity
import dagger.hilt.android.AndroidEntryPoint
import com.vsahin.praytimes.ui.tile.PrayTileService
import androidx.wear.tiles.TileService

@AndroidEntryPoint
class LocationSelectorActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLocationSelectorBinding
    private val viewModel: LocationSelectorViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLocationSelectorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.locationRecyclerView.apply {
            isEdgeItemsCenteringEnabled = true
            layoutManager =
                WearableLinearLayoutManager(
                    this@LocationSelectorActivity,
                    CustomScrollingLayoutCallback()
                )
        }

        viewModel.state.observe(this, { state ->
            val locationType = state.locationType
            val locations = state.locations
            binding.loadingProgressBar.visibility = if (state.isLoading) VISIBLE else GONE
            binding.locationRecyclerView.adapter =
                LocationSelectorAdapter(locationType.toString(), locations, object :
                    LocationSelectorAdapter.LocationClickListener {
                    override fun onLocationClick(index: Int) {
                        viewModel.setLocationId(locations[index].id)
                        when (locationType) {
                            LocationType.COUNTRY -> viewModel.setLocationType(LocationType.CITY)
                            LocationType.CITY -> viewModel.setLocationType(LocationType.DISTRICT)
                            LocationType.DISTRICT -> {
                                viewModel.setLocationName(locations[index].name)
                                viewModel.saveLocationName()
                                viewModel.saveLocationIds()
                                refreshTile()
                                val intent =
                                    Intent(this@LocationSelectorActivity, HomeActivity::class.java)
                                startActivity(intent)
                                finish()
                            }
                        }
                    }
                })
        })
    }

    private fun refreshTile() {
        TileService.getUpdater(this@LocationSelectorActivity)
            .requestUpdate(PrayTileService::class.java)
    }

    override fun onBackPressed() {
        when (viewModel.state.value?.locationType) {
            LocationType.CITY -> {
                viewModel.setLocationType(LocationType.COUNTRY)
            }
            LocationType.DISTRICT -> {
                viewModel.setLocationType(LocationType.CITY)
            }
            else -> super.onBackPressed()
        }
    }
}