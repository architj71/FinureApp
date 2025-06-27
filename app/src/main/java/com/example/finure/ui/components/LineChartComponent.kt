package com.finure.app.ui.components

import android.graphics.Color
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.data.*

@Composable
fun LineChartComponent() {
    AndroidView(factory = { context ->
        LineChart(context).apply {
            val entries = listOf(
                Entry(0f, 100f),
                Entry(1f, 102f),
                Entry(2f, 105f),
                Entry(3f, 98f),
                Entry(4f, 110f)
            )
            val dataSet = LineDataSet(entries, "Stock Price").apply {
                color = Color.BLUE
                valueTextColor = Color.BLACK
                lineWidth = 2f
                setDrawCircles(false)
            }
            data = LineData(dataSet)
            description = Description().apply { text = "" }
            invalidate()
        }
    }, modifier = androidx.compose.ui.Modifier
        .fillMaxWidth()
        .height(200.dp))
}
