package ru.auskov.gpstracker.main.bottom_nav_bar.data

import ru.auskov.gpstracker.R

sealed class BottomMenuItem(val title: String, val iconId: Int) {
    data object Home: BottomMenuItem(title = "Home", iconId = R.drawable.ic_home)
    data object Track: BottomMenuItem(title = "Track", iconId = R.drawable.ic_track)
    data object Settings: BottomMenuItem(title = "Settings", iconId = R.drawable.ic_settings)
}