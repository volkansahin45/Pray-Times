package com.vsahin.praytimes.ui.about

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.vsahin.praytimes.ui.theme.PrayTimesTheme

class AboutActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            PrayTimesTheme {
                AboutScreen()
            }
        }
    }
}