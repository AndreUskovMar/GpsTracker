package ru.auskov.gpstracker.main.track.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.auskov.gpstracker.R
import ru.auskov.gpstracker.main.track.data.TrackData

@Composable
fun TrackListItem(trackData: TrackData, onDelete: () -> Unit = {}, onClick: () -> Unit) {
    Card(modifier = Modifier
        .fillMaxWidth()
        .clickable {
            onClick()
        }) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp)
        ) {
            Text(text = "${stringResource(R.string.date)}: ${trackData.date}", color = Color.Blue)
            Text(
                text = trackData.name, fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
            Text(
                text = "${stringResource(R.string.time)}: ${trackData.time}",
                color = Color.Magenta
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${stringResource(R.string.distance)}: ${trackData.distance}km",
                    modifier = Modifier.weight(1f),
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                IconButton(onClick = {
                    onDelete()
                }) {
                    Icon(Icons.Default.Delete, contentDescription = "trash")
                }
            }

        }
    }
}