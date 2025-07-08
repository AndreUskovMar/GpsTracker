package ru.auskov.gpstracker.main.track.ui

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun TrackScreen() {
    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(horizontal = 10.dp)
    ) {
        items(10) {
            TrackListItem()
            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}