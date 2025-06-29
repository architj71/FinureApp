package com.finure.app.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.finure.R
import com.finure.app.ui.components.LineChartComponent
import com.finure.app.viewmodel.ProductViewModel

@Composable
fun ProductScreen(
    navController: NavHostController,
    symbol: String,
    viewModel: ProductViewModel = hiltViewModel()
) {
    val overview by viewModel.stockOverview.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    val watchlistNames by viewModel.watchlistNames.collectAsState()
    val selectedWatchlists by viewModel.selectedWatchlists.collectAsState()
    var newWatchlistName by remember { mutableStateOf("") }
    var showWatchlistDialog by remember { mutableStateOf(false) }

    LaunchedEffect(symbol) {
        viewModel.loadOverview(symbol)
    }

    if (isLoading) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    error?.let {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Error: $it", color = MaterialTheme.colorScheme.error)
        }
        return
    }

    overview?.let { data ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("${data.Symbol} Details", style = MaterialTheme.typography.headlineSmall)
                IconButton(onClick = { showWatchlistDialog = true }) {
                    Icon(
                        imageVector = Icons.Default.BookmarkBorder,
                        contentDescription = "Add to Watchlist"
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = R.drawable.ic_launcher_foreground),
                    contentDescription = "Logo",
                    modifier = Modifier
                        .size(56.dp)
                        .clip(RoundedCornerShape(12.dp))
                )
                Spacer(Modifier.width(12.dp))
                Column {
                    Text(data.Name ?: "--", fontWeight = FontWeight.SemiBold)
                    Text("${data.Symbol} | ${data.Exchange}", style = MaterialTheme.typography.bodySmall)
                }
                Spacer(Modifier.weight(1f))
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        "\$${data.MarketCapitalization ?: "--"}",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        "+0.41%", // Placeholder
                        color = Color(0xFF4CAF50),
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            Spacer(Modifier.height(24.dp))

            LineChartComponent()
            Spacer(Modifier.height(8.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf("1D", "1W", "1M", "3M", "6M", "1Y").forEach {
                    AssistChip(
                        onClick = { /* TODO */ },
                        label = { Text(it) },
                        shape = RoundedCornerShape(50)
                    )
                }
            }

            Spacer(Modifier.height(24.dp))
            Text("About ${data.Name}", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(4.dp))
            Text(
                data.Description ?: "No description available.",
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(Modifier.height(16.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                data.Industry?.let {
                    AssistChip(onClick = {}, label = { Text(it) })
                }
                data.Sector?.let {
                    AssistChip(onClick = {}, label = { Text(it) })
                }
            }

            Spacer(Modifier.height(24.dp))

            Divider()

            Spacer(Modifier.height(12.dp))
            Text("Key Stats", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(8.dp))

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    MetricItem("52W Low", data.FiftyTwoWeekLow)
                    MetricItem("Current", data.MarketCapitalization)
                    MetricItem("52W High", data.FiftyTwoWeekHigh)
                }
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    MetricItem("Market Cap", data.MarketCapitalization)
                    MetricItem("P/E", data.PERatio)
                    MetricItem("EPS", data.EPS)
                }
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    MetricItem("Div Yield", data.DividendYield)
                    MetricItem("Margin", data.ProfitMargin)
                    MetricItem("ROE", data.ReturnOnEquityTTM)
                }
            }

            Spacer(Modifier.height(20.dp))
        }
    } ?: Text("No stock data found.")

    // ✅ Watchlist Dialog
    if (showWatchlistDialog) {
        AlertDialog(
            onDismissRequest = { showWatchlistDialog = false },
            title = { Text("Manage Watchlists") },
            text = {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        OutlinedTextField(
                            value = newWatchlistName,
                            onValueChange = { newWatchlistName = it },
                            placeholder = { Text("New Watchlist") },
                            modifier = Modifier.weight(1f)
                        )
                        Spacer(Modifier.width(8.dp))
                        Button(onClick = {
                            if (newWatchlistName.isNotBlank()) {
                                viewModel.createWatchlist(newWatchlistName.trim())
                                newWatchlistName = ""
                            }
                        }) {
                            Text("Add")
                        }
                    }

                    Spacer(Modifier.height(12.dp))
                    watchlistNames.forEach { name ->
                        val isChecked = name in selectedWatchlists
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                        ) {
                            Checkbox(
                                checked = isChecked,
                                onCheckedChange = { viewModel.toggleWatchlist(name, it) }
                            )
                            Text(name)
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showWatchlistDialog = false }) {
                    Text("Done")
                }
            }
        )
    }
}


@Composable
fun MetricItem(label: String, value: String?) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(label, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(value ?: "--", fontWeight = FontWeight.SemiBold)
    }
}

