package com.vsahin.praytimes.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.vsahin.praytimes.R
import com.vsahin.praytimes.databinding.ActivityHomeBinding
import com.vsahin.praytimes.common.getCurrentDateReadable
import com.vsahin.praytimes.ui.about.AboutActivity
import com.vsahin.praytimes.ui.common.exception.LocationDidNotSelectedException
import com.vsahin.praytimes.ui.locationSelector.LocationSelectorActivity
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private val viewModel: HomeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val menuItems = getMenuItems()
        binding.topNavigationDrawer.setAdapter(HomeMenuAdapter(menuItems))
        binding.topNavigationDrawer.controller.peekDrawer()
        binding.topNavigationDrawer.addOnItemSelectedListener { pos ->
            if (pos == 0) {
                return@addOnItemSelectedListener
            }

            val intent = when (pos) {
                1 -> Intent(this, LocationSelectorActivity::class.java)
                2 -> Intent(this, AboutActivity::class.java)
                else -> Intent(this, HomeActivity::class.java)
            }

            startActivity(intent)
        }

        viewModel.state.observe(this, { state ->
            state.error?.let {
                when (state.error) {
                    is LocationDidNotSelectedException -> {
                        startActivity(Intent(this, LocationSelectorActivity::class.java))
                        finish()
                        return@observe
                    }
                    else -> {
                        Toast.makeText(this, getString(R.string.unknown_error), Toast.LENGTH_SHORT).show()
                    }
                }
            }

            binding.loadingProgressBar.visibility = if (state.isLoading) View.VISIBLE else View.GONE

            val prayTimesToday = state.prayTimesToday
            binding.dateText.text = getCurrentDateReadable()
            binding.locationText.text = state.locationName
            binding.fajrText.text = prayTimesToday?.fajr
            binding.shuruqText.text = prayTimesToday?.shuruq
            binding.dhuhrText.text = prayTimesToday?.dhuhr
            binding.asrText.text = prayTimesToday?.asr
            binding.maghribText.text = prayTimesToday?.maghrib
            binding.ishaText.text = prayTimesToday?.isha
        })
    }

    override fun onResume() {
        super.onResume()

        binding.topNavigationDrawer.setCurrentItem(0, false)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        viewModel.init()
        binding.topNavigationDrawer.setCurrentItem(0, false)
    }

    private fun getMenuItems(): List<MenuItem> {
        return listOf(
            MenuItem(getDrawable(R.drawable.ic_baseline_home_24), getString(R.string.home)),
            MenuItem(
                getDrawable(R.drawable.ic_baseline_edit_location_24),
                getString(R.string.edit_location)
            ),
            MenuItem(getDrawable(R.drawable.ic_baseline_about_24), getString(R.string.about))
        )
    }
}