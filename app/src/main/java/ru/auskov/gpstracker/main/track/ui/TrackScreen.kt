package ru.auskov.gpstracker.main.track.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import ru.auskov.gpstracker.R
import ru.auskov.gpstracker.components.DialogType
import ru.auskov.gpstracker.components.TrackDialog
import ru.auskov.gpstracker.main.track.data.TrackData

@Composable
fun TrackScreen(
    viewModel: TrackViewModel = hiltViewModel(),
    onTrackClick: (TrackData) -> Unit
) {
    val trackList = viewModel.trackList.collectAsState(initial = emptyList())
    var isTrackDialogVisible by remember {
        mutableStateOf(false)
    }

    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 10.dp)
        ) {
            items(trackList.value) { trackData ->
                TrackListItem(
                    trackData,
                    onDelete = {
                        viewModel.trackToDelete = trackData
                        isTrackDialogVisible = true
                    },
                    onClick = {
                        onTrackClick(trackData)
                    }
                )
                Spacer(modifier = Modifier.height(10.dp))
            }
        }

        if (trackList.value.isEmpty()) {
            Text(
                text = stringResource(R.string.empty_tracks),
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }

    TrackDialog(
        title = stringResource(R.string.delete_track),
        isVisible = isTrackDialogVisible,
        dialogType = DialogType.DELETE,
        onDismiss = {
            isTrackDialogVisible = false
        },
        onSubmit = {
            isTrackDialogVisible = false
            viewModel.deleteTrack()
        }
    )
}