package ru.auskov.gpstracker.main.settings.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import ru.auskov.gpstracker.components.CustomDropDownMenu
import ru.auskov.gpstracker.main.settings.data.ColorPickerData

@Composable
fun SettingsScreen(
    viewModel: SettingsScreenViewModel = hiltViewModel()
) {
    val optionsUpdateTime = listOf("3000", "5000", "10000")
    val optionsPriority = listOf(
        "PRIORITY_HIGH_ACCURACY",
        "PRIORITY_PASSIVE",
        "PRIORITY_LOW_POWER",
        "PRIORITY_BALANCED_POWER_ACCURACY"
    )
    val optionsTrackLineWidth = listOf("5", "10", "15")
    val optionsTrackLineColor = listOf(
        ColorPickerData(color = Color.Cyan),
        ColorPickerData(color = Color.Green),
        ColorPickerData(color = Color.Magenta),
        ColorPickerData(color = Color.Red),
        ColorPickerData(color = Color.Yellow)
    )

    val selectedUpdateTime = remember {
        mutableStateOf(viewModel.getLocationUpdateInterval())
    }

    val selectedPriority = remember {
        mutableStateOf(viewModel.getPriority())
    }

    val selectedTrackLineWidth = remember {
        mutableStateOf(viewModel.getTrackLineWidth())
    }

    val colorListState = remember {
        mutableStateOf(optionsTrackLineColor.map { colorPickerData ->
            colorPickerData.copy(isChecked = colorPickerData.color.value.toString() == viewModel.getColor())
        })
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        CustomDropDownMenu(
            "Location update time",
            options = optionsUpdateTime,
            selectedOption = selectedUpdateTime.value
        ) { selectedOption ->
            selectedUpdateTime.value = selectedOption
            viewModel.saveLocationUpdateInterval(selectedOption)
        }
        CustomDropDownMenu(
            "Priority",
            options = optionsPriority,
            selectedOption = selectedPriority.value
        ) { selectedOption ->
            selectedPriority.value = selectedOption
            viewModel.savePriority(selectedOption)
        }
        CustomDropDownMenu(
            "Track line width",
            options = optionsTrackLineWidth,
            selectedOption = selectedTrackLineWidth.value
        ) { selectedOption ->
            selectedTrackLineWidth.value = selectedOption
            viewModel.saveTrackLineWidth(selectedOption)
        }

        Text(
            text = "Track line color",
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            color = Color.Cyan,
            modifier = Modifier.padding(15.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp)
        ) {
            items(colorListState.value) { colorPickerData ->
                ColorPickerItem(colorPickerData) {
                    colorListState.value = colorListState.value.map {
                        it.copy(
                            isChecked = it.color == colorPickerData.color
                        )
                    }
                    viewModel.saveColor(colorPickerData.color)
                }

                Spacer(modifier = Modifier.width(10.dp))
            }
        }
    }
}