package ru.auskov.gpstracker.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import ru.auskov.gpstracker.utils.TimeUtils


@Composable
fun TrackDialog(
    title: String = "",
    isVisible: Boolean = true,
    dialogType: DialogType = DialogType.SAVE,
    onDismiss: () -> Unit = {},
    onSubmit: (trackName: String) -> Unit = {}
) {
    val trackName = remember {
        mutableStateOf("")
    }

    if (isVisible) {
        AlertDialog(
            title = {
                Text(text = title, fontWeight = FontWeight.Bold)
            },
            text = {
                if (dialogType == DialogType.SAVE) {
                    TextField(
                        value = trackName.value,
                        label = {
                            Text(text = "Enter track name:")
                        },
                        onValueChange = {
                            trackName.value = it
                        },
                        colors = TextFieldDefaults.colors(
                            disabledContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            focusedContainerColor = Color.Transparent
                        )
                    )
                } else {
                    Text(text = "Are you sure you want to delete track?")
                }
            },
            onDismissRequest = {
                onDismiss()
                trackName.value = ""
            },
            confirmButton = {
                Button(
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                    onClick = {
                        onDismiss()
                        trackName.value = ""
                    }
                ) {
                    Text(text = "Cancel")
                }
                Button(
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Blue),
                    onClick = {
                        onSubmit(trackName.value.ifEmpty {
                            "track_${TimeUtils.getTrackTime()}"
                        })
                        trackName.value = ""
                    }
                ) {
                    Text(text = "Ok")
                }
            }
        )
    }
}

enum class DialogType {
    DELETE, SAVE
}