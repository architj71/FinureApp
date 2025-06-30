package com.finure.app.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.finure.R
import com.example.finure.viewmodel.StockViewModel
import com.example.finure.data.model.StockInfo

// Temporary placeholder data for preview/demo purposes
val dummyStocks = listOf(
    StockInfo("AAPL", "182.91", "+4.20", "+2.35%", "10.5M"),
    StockInfo("GOOGL", "2743.21", "+30.47", "+1.12%", "8.1M"),
    StockInfo("TSLA", "691.76", "-6.12", "-0.87%", "14.2M"),
    StockInfo("MSFT", "299.79", "+1.58", "+0.53%", "12.9M")
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StocksScreen(navController: NavHostController) {
    val viewModel: StockViewModel = hiltViewModel()
    val topMovers by viewModel.gainerData.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    var searchQuery by remember { mutableStateOf("") }

    // Trigger API call once when screen is composed
    LaunchedEffect(Unit) {
        viewModel.loadTopMovers()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()) // Enables screen scrolling
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {

        // 🔷 App Header with Logo + Search Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = R.drawable.finure_logo),
                    contentDescription = "Finure Logo",
                    modifier = Modifier
                        .size(32.dp)
                        .padding(end = 6.dp)
                )
                Text(
                    text = "Finure",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = {
                    Text(
                        text = "Search stocks...",
                        fontSize = 12.sp,
                        modifier = Modifier.offset(y = (-2).dp) // Slight visual tweak
                    )
                },
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
                    .padding(end = 50.dp)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Top Gainers Section
        SectionHeader("🔼 Top Gainers") {
            navController.navigate("view_all/gainers")
        }

        StockGridSection(
            // Uncomment below to use dummy API data instead of dummy
            // stocks = dummyStocks,
            stocks = topMovers?.top_gainers?.take(4),
            isLoading = isLoading,
            onStockClick = { symbol -> navController.navigate("product/$symbol") },
            isGainer = true
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Top Losers Section
        SectionHeader("🔽 Top Losers") {
            navController.navigate("view_all/losers")
        }

        StockGridSection(
            // Uncomment below to use dummy API data instead of dummy
            // stocks = dummyStocks,
            stocks = topMovers?.top_losers?.take(4),
            isLoading = isLoading,
            onStockClick = { symbol -> navController.navigate("product/$symbol") },
            isGainer = false
        )

        Spacer(modifier = Modifier.height(80.dp))

        // Display error message if data fetch fails
        error?.let {
            Spacer(modifier = Modifier.height(16.dp))
            Text("Error: $it", color = MaterialTheme.colorScheme.error)
        }
    }
}

/**
 * Section header with a title and "View All" button
 */
@Composable
fun SectionHeader(title: String, onViewAllClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            fontSize = 20.sp,
            color = MaterialTheme.colorScheme.onBackground
        )
        TextButton(onClick = onViewAllClick) {
            Text("View All")
        }
    }
}

/**
 * Displays a manual 2-column stock card grid for a section (gainers/losers).
 */
@Composable
fun StockGridSection(
    stocks: List<StockInfo>?,
    isLoading: Boolean,
    onStockClick: (String) -> Unit,
    isGainer: Boolean
) {
    when {
        isLoading -> {
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        stocks.isNullOrEmpty() -> {
            Text("No data available.", color = MaterialTheme.colorScheme.onBackground)
        }

        else -> {
            // Render grid using nested Rows (manual grid for layout control)
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                stocks.chunked(2).forEach { rowStocks ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        rowStocks.forEach { stock ->
                            Box(modifier = Modifier.weight(1f)) {
                                StockCard(
                                    stock = stock,
                                    onClick = { onStockClick(stock.ticker) },
                                    isGainer = isGainer
                                )
                            }
                        }

                        // Fill blank space if row has only 1 item
                        if (rowStocks.size == 1) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }
        }
    }
}

/**
 * Individual stock card with ticker, price and change percent.
 * Changes text color based on gainer/loser.
 */
@Composable
fun StockCard(stock: StockInfo, onClick: () -> Unit, isGainer: Boolean) {
    val color = if (isGainer) Color(0xFF2ECC71) else Color(0xFFE74C3C) // Green vs Red

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
                fontWeight = FontWeight.W500
            )
            Text(
                text = stock.change_percentage,
                style = MaterialTheme.typography.bodyMedium,
                color = color
            )
        }
    }
}
