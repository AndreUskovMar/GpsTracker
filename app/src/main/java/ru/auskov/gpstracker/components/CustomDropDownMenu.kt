package ru.auskov.gpstracker.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CustomDropDownMenu(
    label: String = "",
    options: List<String>,
    selectedOption: String,
    onOptionSelect: (String) -> Unit
) {
    val expanded = remember {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier.padding(15.dp)
    ) {
        if (label.isNotEmpty()) {
            Text(
                text = label,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = Color.Cyan
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Transparent)
                .clickable {
                    expanded.value = true
                }
                .padding(20.dp)
        ) {
            Text(text = selectedOption, color = Color.DarkGray)

            DropdownMenu(
                expanded = expanded.value,
                onDismissRequest = {
                    expanded.value = false
                }
            ) {
                options.forEach { option ->
                    DropdownMenuItem(
                        text = {
                            Text(text = option)
                        },
                        onClick = {
                            onOptionSelect(option)
                            expanded.value = false
                        }
                    )
                }
            }
        }
    }
}