package com.vsahin.praytimes

import android.content.ComponentName
import android.os.Bundle
import android.widget.FrameLayout
import androidx.activity.ComponentActivity
import androidx.wear.tiles.manager.TileUiClient
import com.vsahin.praytimes.ui.tile.PrayTileService

class TilePreviewActivity : ComponentActivity() {
    private lateinit var tileUiClient: TileUiClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tile_preview)
        val rootLayout = findViewById<FrameLayout>(R.id.tile_container)
        tileUiClient = TileUiClient(
            context = this,
            component = ComponentName(this, PrayTileService::class.java),
            parentView = rootLayout
        )
        tileUiClient.connect()
    }

    override fun onDestroy() {
        super.onDestroy()
        tileUiClient.close()
    }
}