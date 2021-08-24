package com.vsahin.praytimes.ui.home

import androidx.wear.widget.drawer.WearableNavigationDrawerView
import com.vsahin.praytimes.R

class HomeMenuAdapter(private val menuItems: List<MenuItem>) :
    WearableNavigationDrawerView.WearableNavigationDrawerAdapter() {
    override fun getItemText(pos: Int): CharSequence = menuItems[pos].text

    override fun getItemDrawable(pos: Int) = menuItems[pos].icon

    override fun getCount() = menuItems.size
}