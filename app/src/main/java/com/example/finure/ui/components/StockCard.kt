package com.finure.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.finure.app.data.model.StockItem

@Composable
fun StockCard(stock: StockItem, onClick: () -> Unit = {}) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation()
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(stock.symbol, style = MaterialTheme.typography.titleMedium)
            Text(stock.name, style = MaterialTheme.typography.bodySmall)
            Spacer(modifier = Modifier.height(4.dp))
            Text("Price: \$${stock.price}", style = MaterialTheme.typography.bodyMedium)
            Text(
                "Change: ${stock.changePercent}",
                color = if (stock.changePercent.startsWith("-")) Color.Red else Color.Green
            )
        }
    }
}
