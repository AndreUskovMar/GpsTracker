package ru.auskov.gpstracker.main.settings.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ru.auskov.gpstracker.components.CustomDropDownMenu

@Composable
fun SettingsScreen() {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        CustomDropDownMenu("Location update time")
    }
}