package com.finure.app.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
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
import com.example.finure.viewmodel.ProductViewModel
import kotlinx.coroutines.launch

/**
 * Screen to display detailed stock information for a given symbol.
 * Includes chart, metadata, and watchlist controls.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductScreen(
    navController: NavHostController,
    symbol: String,
    viewModel: ProductViewModel = hiltViewModel()
) {
    // State variables collected from ViewModel
    val overview by viewModel.stockOverview.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    val watchlistNames by viewModel.watchlistNames.collectAsState()
    val selectedWatchlists by viewModel.selectedWatchlists.collectAsState()
    val isInWatchlist by viewModel.isInWatchlist.collectAsState()

    // Dialog + Bottom Sheet related UI state
    var newWatchlistName by remember { mutableStateOf("") }
    var showWatchlistDialog by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    // Load stock details when screen opens or symbol changes
    LaunchedEffect(symbol) {
        viewModel.loadOverview(symbol)
    }

    // Show loading indicator while fetching data
    if (isLoading) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    // Handle error state
    error?.let {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Error: $it", color = MaterialTheme.colorScheme.error)
        }
        return
    }

    // Main content if data available
    overview?.let { data ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // Top bar with symbol title and bookmark toggle
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("${data.Symbol} Details", style = MaterialTheme.typography.headlineSmall)
                IconButton(onClick = {
                    showWatchlistDialog = true
                    scope.launch { sheetState.show() }
                }) {
                    Icon(
                        imageVector = if (isInWatchlist) Icons.Filled.Bookmark else Icons.Filled.BookmarkBorder,
                        contentDescription = if (isInWatchlist) "In Watchlist" else "Add to Watchlist",
                        tint = if (isInWatchlist) MaterialTheme.colorScheme.primary else LocalContentColor.current
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            // Stock summary section with logo and basic info
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
                        "+0.41%", // Placeholder for real-time price change
                        color = Color(0xFF4CAF50),
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            Spacer(Modifier.height(24.dp))

            // Chart for historical stock price
            LineChartComponent()
            Spacer(Modifier.height(8.dp))

            // Time range selection (1D, 1W, etc.)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf("1D", "1W", "1M", "3M", "6M", "1Y").forEach {
                    AssistChip(
                        onClick = { /* TODO: Implement range filter */ },
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

            // Industry and Sector as colored chips
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                data.Industry?.let {
                    AssistChip(
                        onClick = { },
                        label = { Text(it, color = MaterialTheme.colorScheme.onPrimary) },
                        colors = AssistChipDefaults.assistChipColors(
                            containerColor = Color(0xFF2196F3),
                            labelColor = Color.White
                        )
                    )
                }
                data.Sector?.let {
                    AssistChip(
                        onClick = { },
                        label = { Text(it, color = MaterialTheme.colorScheme.onPrimary) },
                        colors = AssistChipDefaults.assistChipColors(
                            containerColor = Color(0xFF4CAF50),
                            labelColor = Color.White
                        )
                    )
                }
            }

            Spacer(Modifier.height(24.dp))
            Divider()

            // Key Stats Section
            Spacer(Modifier.height(12.dp))
            Text("Key Stats", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(8.dp))

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    MetricItem("52W Low", data.fiftyTwoWeekLow)
                    MetricItem("Current", data.MarketCapitalization)
                    MetricItem("52W High", data.fiftyTwoWeekHigh)
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
    } ?: Text("No stock data found.") // Fallback if overview is null

    // Watchlist bottom sheet dialog
    if (showWatchlistDialog) {
        ModalBottomSheet(
            onDismissRequest = {
                showWatchlistDialog = false
                scope.launch { sheetState.hide() }
            },
            sheetState = sheetState,
            shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
            containerColor = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 10.dp)
            ) {
                Text(
                    "Add to Watchlist",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                // Input for creating a new watchlist
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = newWatchlistName,
                        onValueChange = { newWatchlistName = it },
                        placeholder = { Text("New Watchlist Name") },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            if (newWatchlistName.isNotBlank()) {
                                viewModel.createWatchlist(newWatchlistName.trim())
                                newWatchlistName = ""
                            }
                        },
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Add")
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // List of watchlists with checkboxes
                watchlistNames.forEach { name ->
                    val isChecked = name in selectedWatchlists
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = isChecked,
                            onCheckedChange = { viewModel.toggleWatchlist(name, it) },
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(name, style = MaterialTheme.typography.bodyLarge)
                    }
                }

                Spacer(modifier = Modifier.height(30.dp))
            }
        }
    }
}

/**
 * A reusable UI component to display a stock metric (label + value).
 */
@Composable
fun MetricItem(label: String, value: String?) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(label, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(value ?: "--", fontWeight = FontWeight.SemiBold)
    }
}
