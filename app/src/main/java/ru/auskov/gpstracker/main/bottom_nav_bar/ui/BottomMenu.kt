package ru.auskov.gpstracker.main.bottom_nav_bar.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.toLowerCase
import ru.auskov.gpstracker.main.bottom_nav_bar.data.BottomMenuItem

@Composable
fun BottomMenu(
    selectedItemTitle: String,
    onItemClick: (String) -> Unit
) {
    val items = listOf(
        BottomMenuItem.Home,
        BottomMenuItem.Track,
        BottomMenuItem.Settings,
    )

    NavigationBar(
        modifier = Modifier.fillMaxWidth(),
        containerColor = Color.Black,
    ) {
        items.forEach { item ->
            NavigationBarItem(
                selected = item.title == selectedItemTitle,
                onClick = {
                    onItemClick(item.title)
                },
                icon = {
                    Icon(
                        painter = painterResource(item.iconId),
                        contentDescription = item.title.toLowerCase(Locale.current)
                    )
                },
                label = {
                    Text(text = item.title)
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color.White,
                    unselectedIconColor = Color.Gray,
                    selectedTextColor = Color.White,
                    unselectedTextColor = Color.Gray,
                    indicatorColor = Color.Black
                )
            )
        }
    }
}