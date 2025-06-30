package com.finure.app.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.finure.data.model.StockItem

/**
 * StockCard is a reusable UI component that displays a brief summary of a stock.
 *
 * @param stock StockItem containing symbol, name, price, and change info.
 * @param onClick Optional click action when the card is tapped.
 */
@Composable
fun StockCard(stock: StockItem, onClick: () -> Unit = {}) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable { onClick() }, // Optional interaction
        elevation = CardDefaults.cardElevation()
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = stock.symbol,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = stock.name,
                style = MaterialTheme.typography.bodySmall
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Price: \$${stock.price}",
                style = MaterialTheme.typography.bodyMedium
            )

            Text(
                text = "Change: ${stock.changePercent}",
                color = if (stock.changePercent.startsWith("-")) Color.Red else Color.Green,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
