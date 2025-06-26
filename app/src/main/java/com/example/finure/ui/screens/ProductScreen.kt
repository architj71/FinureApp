package com.finure.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.finure.app.ui.components.LineChartComponent

@Composable
fun ProductScreen(navController: NavController, symbol: String) {
    var isInWatchlist by remember { mutableStateOf(false) }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(symbol, style = MaterialTheme.typography.headlineSmall)
            IconButton(onClick = { isInWatchlist = !isInWatchlist }) {
                Icon(
                    imageVector = if (isInWatchlist) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                    contentDescription = "Watchlist Toggle"
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text("Price Chart", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))

        LineChartComponent() // Replace with real data when integrated

        Spacer(modifier = Modifier.height(32.dp))

        Text("Company details and stats will appear here.")
    }
}
