package com.finure.app.ui.components

import android.graphics.Color
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.data.*

/**
 * Displays a simple line chart using MPAndroidChart inside Jetpack Compose.
 * Integrates with Compose via AndroidView.
 */
@Composable
fun LineChartComponent() {
    // Extract Compose color and convert to ARGB format for MPAndroidChart
    val axisColor = MaterialTheme.colorScheme.onBackground.toArgb()

    AndroidView(
        factory = { context ->
            LineChart(context).apply {
                // ✏️ Dummy data points for demonstration
                val entries = listOf(
                    Entry(0f, 100f),
                    Entry(1f, 102f),
                    Entry(2f, 105f),
                    Entry(3f, 98f),
                    Entry(4f, 110f)
                )

                // Configure dataset appearance
                val dataSet = LineDataSet(entries, "Stock Price").apply {
                    color = Color.BLUE
                    valueTextColor = axisColor
                    lineWidth = 2f
                    setDrawCircles(false) // Hide data point circles
                }

                // Apply data and style to chart
                data = LineData(dataSet)
                description = Description().apply { text = "" } // Remove default description

                // Customize chart axis/legend appearance
                xAxis.textColor = axisColor
                axisLeft.textColor = axisColor
                axisRight.textColor = axisColor
                legend.textColor = axisColor

                setNoDataTextColor(axisColor)
                setGridBackgroundColor(Color.TRANSPARENT)

                invalidate() // Force redraw
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
    )
}
