package ru.auskov.gpstracker.main.track.ui

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel

@Composable
fun TrackScreen(
    viewModel: TrackViewModel = hiltViewModel()
) {
    val trackList = viewModel.trackList.collectAsState(initial = emptyList())

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(horizontal = 10.dp)
    ) {
        items(trackList.value) { trackData ->
            TrackListItem(trackData)
            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}