package com.finure.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.finure.app.ui.components.StockCard

@Composable
fun ExploreScreen(viewModel: NavHostController = hiltViewModel()) {
    val gainers by viewModel.topGainers.collectAsState()

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {

        Text("Top Gainers", style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(8.dp))

        if (gainers.isEmpty()) {
            Text("No data available", color = MaterialTheme.colorScheme.error)
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.fillMaxHeight()
            ) {
                items(gainers) { stock ->
                    StockCard(stock)
                }
            }
        }
    }
}
