package com.example.finure.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.finure.app.data.model.StockInfo
import com.finure.app.ui.components.StockCard
import com.finure.app.viewmodel.WatchlistViewModel

@Composable
fun WatchlistDetailScreen(
    navController: NavHostController,
    name: String,
    viewModel: WatchlistViewModel = hiltViewModel()
) {
    val watchlist by viewModel.watchlist.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    LaunchedEffect(name) {
        viewModel.selectWatchlist(name)
    }

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text(name, style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(16.dp))

        if (isLoading) {
            CircularProgressIndicator()
        } else if (watchlist.isEmpty()) {
            Text("No stocks in this watchlist.")
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(watchlist) { stock ->
                    WatchlistCard(stock = stock) {
                        navController.navigate("product/${stock.ticker}")
                    }
                }


            }
        }
    }
}

@Composable
fun WatchlistCard(stock: StockInfo, onClick: () -> Unit) {
    val isGainer = !stock.change_percentage.startsWith("-")
    val changeColor = if (isGainer) Color(0xFF2ECC71) else Color(0xFFE74C3C)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.SpaceAround
        ) {
            Text(
                text = stock.ticker,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "\$${stock.price}",
                style = MaterialTheme.typography.bodyMedium,
                fontSize = 20.sp,
                fontWeight = FontWeight.W500
            )
            Text(
                text = stock.change_percentage,
                style = MaterialTheme.typography.bodyMedium,
                color = changeColor,
                fontSize = 16.sp
            )
        }
    }
}
