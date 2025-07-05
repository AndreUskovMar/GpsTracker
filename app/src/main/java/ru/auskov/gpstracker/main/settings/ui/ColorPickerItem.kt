package ru.auskov.gpstracker.main.settings.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import ru.auskov.gpstracker.main.settings.data.ColorPickerData

@Composable
fun ColorPickerItem(
    colorPickerData: ColorPickerData,
    onColorSelect: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(50.dp)
            .clip(CircleShape)
            .clickable {
                onColorSelect()
            }
            .background(colorPickerData.color)
            .padding(10.dp),
        contentAlignment = Alignment.Center
    ) {
        if (colorPickerData.isChecked)
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = "check",
                tint = Color.White
            )
    }
}