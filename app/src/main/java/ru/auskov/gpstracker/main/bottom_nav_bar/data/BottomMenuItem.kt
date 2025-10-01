package ru.auskov.gpstracker.main.bottom_nav_bar.data

import ru.auskov.gpstracker.R

sealed class BottomMenuItem(val titleId: Int, val iconId: Int) {
    data object Home: BottomMenuItem(titleId = R.string.home, iconId = R.drawable.ic_home)
    data object Track: BottomMenuItem(titleId = R.string.track, iconId = R.drawable.ic_track)
    data object Settings: BottomMenuItem(titleId = R.string.settings, iconId = R.drawable.ic_settings)
}