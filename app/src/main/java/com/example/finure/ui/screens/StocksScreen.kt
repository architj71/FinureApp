package com.finure.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.finure.app.viewmodel.StockViewModel
import com.finure.app.data.model.StockInfo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StocksScreen(navController: NavHostController) {
    val viewModel: StockViewModel = hiltViewModel()
    val topMovers by viewModel.gainerData.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    var searchQuery by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        viewModel.loadTopMovers()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {

        // 🧭 Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Finure",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary
            )

            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Search stocks...") },
                leadingIcon = {
                    Icon(Icons.Default.Search, contentDescription = "Search Icon")
                },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    unfocusedBorderColor = Color.Transparent,
                    focusedBorderColor = MaterialTheme.colorScheme.primary
                ),
                shape = RoundedCornerShape(25.dp),
                singleLine = true,
                modifier = Modifier
                    .height(48.dp)
                    .width(220.dp)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // 🔼 Top Gainers Section
        SectionHeader("📈 Top Gainers") {
            navController.navigate("view_all/gainers")
        }

        StockGridSection(
            stocks = topMovers?.top_gainers,
            isLoading = isLoading,
            onStockClick = { symbol -> navController.navigate("product/$symbol") },
            isGainer = true
        )

        Spacer(modifier = Modifier.height(20.dp))

        // 🔽 Top Losers Section
        SectionHeader("📉 Top Losers") {
            navController.navigate("view_all/losers")
        }

        StockGridSection(
            stocks = topMovers?.top_losers,
            isLoading = isLoading,
            onStockClick = { symbol -> navController.navigate("product/$symbol") },
            isGainer = false
        )

        error?.let {
            Spacer(modifier = Modifier.height(16.dp))
            Text("Error: $it", color = MaterialTheme.colorScheme.error)
        }
    }
}

@Composable
fun SectionHeader(title: String, onViewAllClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(title, style = MaterialTheme.typography.titleLarge)
        TextButton(onClick = onViewAllClick) {
            Text("View All")
        }
    }
}

@Composable
fun StockGridSection(
    stocks: List<StockInfo>?,
    isLoading: Boolean,
    onStockClick: (String) -> Unit,
    isGainer: Boolean
) {
    if (isLoading) {
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else if (stocks.isNullOrEmpty()) {
        Text("No data available.", color = MaterialTheme.colorScheme.onBackground)
    } else {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .fillMaxHeight(0.45f)
                .padding(vertical = 8.dp)
        ) {
            items(stocks) { stock ->
                StockCard(
                    stock = stock,
                    onClick = { onStockClick(stock.ticker) },
                    isGainer = isGainer
                )
            }
        }
    }
}

@Composable
fun StockCard(stock: StockInfo, onClick: () -> Unit, isGainer: Boolean) {
    val color = if (isGainer) Color(0xFF2ECC71) else Color(0xFFE74C3C)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
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
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stock.ticker,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "Price: \$${stock.price}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Change: ${stock.change_percentage}",
                style = MaterialTheme.typography.bodyMedium,
                color = color
            )
        }
    }
}