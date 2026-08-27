package com.kastep.app.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kastep.app.data.KastepViewModel
import com.kastep.app.ui.theme.KastepBlack
import com.kastep.app.ui.theme.KastepBlue
import com.kastep.app.ui.theme.KastepCyan
import com.kastep.app.ui.theme.KastepGray
import com.kastep.app.ui.theme.KastepGreen
import com.kastep.app.ui.theme.KastepRed
import com.kastep.app.ui.theme.KastepWhite

@Composable
fun LaporanKasScreen(
    viewModel: KastepViewModel,
    onOpenDrawer: () -> Unit
) {
    val userProfile by viewModel.userProfile.collectAsState()
    val totalIncome = viewModel.totalIncome
    val totalExpense = viewModel.totalExpense
    val saldo = viewModel.saldo

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(KastepBlack)
            .verticalScroll(rememberScrollState())
    ) {
        // Custom top bar for Laporan Kas (matches design: hamburger, "Laporan kas", Admin)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onOpenDrawer) {
                Icon(
                    Icons.Default.Menu,
                    contentDescription = "Menu",
                    tint = KastepWhite,
                    modifier = Modifier.size(28.dp)
                )
            }

            Text(
                text = "Laporan kas",
                color = KastepWhite,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(KastepGray),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.Person,
                        contentDescription = "Profile",
                        tint = KastepWhite,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = userProfile.nama,
                    color = KastepWhite,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        // Periode dropdown
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Periode",
                color = KastepWhite,
                fontSize = 14.sp
            )
            Spacer(modifier = Modifier.width(12.dp))
            Box(
                modifier = Modifier
                    .weight(1f)
                    .border(1.dp, KastepGray.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                    .padding(horizontal = 12.dp, vertical = 10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Juli 2026",
                        color = KastepWhite,
                        fontSize = 14.sp
                    )
                    Icon(
                        Icons.Default.ArrowDropDown,
                        contentDescription = "Dropdown",
                        tint = KastepWhite
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Summary cards row — Total Pemasukan & Total Pengeluaran
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Total Pemasukan — blue border
            ReportSummaryCard(
                modifier = Modifier.weight(1f),
                label = "Total Pemasukan",
                amount = "Rp ${KastepViewModel.formatRupiah(totalIncome)}",
                borderColor = KastepBlue,
                labelColor = KastepBlue,
                coinColor = KastepBlue
            )
            // Total Pengeluaran — red border
            ReportSummaryCard(
                modifier = Modifier.weight(1f),
                label = "Total Pengeluaran",
                amount = "Rp ${KastepViewModel.formatRupiah(totalExpense)}",
                borderColor = KastepRed,
                labelColor = KastepRed,
                coinColor = KastepRed
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Total Akhir — green border, full width
        ReportSummaryCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            label = "Total Akhir",
            amount = "Rp ${KastepViewModel.formatRupiah(saldo)}",
            borderColor = KastepGreen,
            labelColor = KastepGreen,
            coinColor = KastepGreen
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Chart card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A2E))
        ) {
            Column(
                modifier = Modifier.padding(20.dp)
            ) {
                Text(
                    text = "Grafis Pemasukan & Pengeluaran",
                    color = KastepWhite,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Legend
                Row(
                    horizontalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(14.dp)
                                .background(KastepBlue)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Pemasukan", color = KastepWhite, fontSize = 12.sp)
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(14.dp)
                                .background(KastepGray)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Pengeluaram", color = KastepWhite, fontSize = 12.sp)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Line chart
                IncomeExpenseChart(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
private fun ReportSummaryCard(
    modifier: Modifier = Modifier,
    label: String,
    amount: String,
    borderColor: Color,
    labelColor: Color,
    coinColor: Color
) {
    Box(
        modifier = modifier
            .border(1.5.dp, borderColor, RoundedCornerShape(12.dp))
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            // Coin stack icon drawn with Canvas
            CoinStackIcon(color = coinColor)
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = label,
                    color = labelColor,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = amount,
                    color = KastepWhite,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun CoinStackIcon(color: Color) {
    Canvas(modifier = Modifier.size(36.dp)) {
        val coinWidth = size.width * 0.7f
        val coinHeight = size.height * 0.15f
        val startX = size.width * 0.15f

        // Draw 3 stacked coins
        for (i in 0..2) {
            val y = size.height * 0.3f + i * (coinHeight + 4.dp.toPx())
            drawRoundRect(
                color = color,
                topLeft = Offset(startX, y),
                size = androidx.compose.ui.geometry.Size(coinWidth, coinHeight),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(4.dp.toPx())
            )
            // Highlight line
            drawLine(
                color = color.copy(alpha = 0.5f),
                start = Offset(startX + coinWidth * 0.2f, y + coinHeight * 0.5f),
                end = Offset(startX + coinWidth * 0.8f, y + coinHeight * 0.5f),
                strokeWidth = 1.dp.toPx()
            )
        }
    }
}

@Composable
private fun IncomeExpenseChart(modifier: Modifier = Modifier) {
    // Dummy data points for the chart
    val incomeData = listOf(5f, 15f, 10f, 20f, 30f, 100f)
    val expenseData = listOf(2f, 8f, 5f, 25f, 15f, 40f)
    val labels = listOf("1 Juli", "2 Juli", "3 Juli", "4 Juli", "5 Juli", "2 Juli")
    val yLabels = listOf("0", "10", "20", "50", "80", "100")

    Canvas(modifier = modifier) {
        val chartLeft = 50.dp.toPx()
        val chartBottom = size.height - 30.dp.toPx()
        val chartTop = 10.dp.toPx()
        val chartRight = size.width - 10.dp.toPx()
        val chartWidth = chartRight - chartLeft
        val chartHeight = chartBottom - chartTop
        val maxValue = 100f

        // Draw Y-axis labels
        val paint = android.graphics.Paint().apply {
            color = android.graphics.Color.parseColor("#9E9E9E")
            textSize = 10.sp.toPx()
            isAntiAlias = true
        }

        yLabels.forEachIndexed { index, label ->
            val y = chartBottom - (chartHeight * (label.toFloat() / maxValue))
            drawContext.canvas.nativeCanvas.drawText(
                label,
                5.dp.toPx(),
                y + 4.dp.toPx(),
                paint
            )
            // Grid line
            drawLine(
                color = Color(0xFF333333),
                start = Offset(chartLeft, y),
                end = Offset(chartRight, y),
                strokeWidth = 0.5.dp.toPx()
            )
        }

        // Draw X-axis labels
        labels.forEachIndexed { index, label ->
            val x = chartLeft + (chartWidth * index / (labels.size - 1).coerceAtLeast(1))
            drawContext.canvas.nativeCanvas.drawText(
                label,
                x - 15.dp.toPx(),
                size.height - 5.dp.toPx(),
                paint
            )
        }

        // Draw income line (blue)
        val incomePath = Path()
        incomeData.forEachIndexed { index, value ->
            val x = chartLeft + (chartWidth * index / (incomeData.size - 1).coerceAtLeast(1))
            val y = chartBottom - (chartHeight * (value / maxValue))
            if (index == 0) incomePath.moveTo(x, y) else incomePath.lineTo(x, y)
        }
        drawPath(
            incomePath,
            color = Color(0xFF4A90D9),
            style = Stroke(width = 2.5.dp.toPx(), cap = StrokeCap.Round)
        )

        // Draw expense line (gray)
        val expensePath = Path()
        expenseData.forEachIndexed { index, value ->
            val x = chartLeft + (chartWidth * index / (expenseData.size - 1).coerceAtLeast(1))
            val y = chartBottom - (chartHeight * (value / maxValue))
            if (index == 0) expensePath.moveTo(x, y) else expensePath.lineTo(x, y)
        }
        drawPath(
            expensePath,
            color = Color(0xFF9E9E9E),
            style = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round)
        )

        // Draw data points for income
        incomeData.forEachIndexed { index, value ->
            val x = chartLeft + (chartWidth * index / (incomeData.size - 1).coerceAtLeast(1))
            val y = chartBottom - (chartHeight * (value / maxValue))
            drawCircle(color = Color(0xFF4A90D9), radius = 4.dp.toPx(), center = Offset(x, y))
        }

        // Draw data points for expense
        expenseData.forEachIndexed { index, value ->
            val x = chartLeft + (chartWidth * index / (expenseData.size - 1).coerceAtLeast(1))
            val y = chartBottom - (chartHeight * (value / maxValue))
            drawCircle(color = Color(0xFF9E9E9E), radius = 4.dp.toPx(), center = Offset(x, y))
        }

        // Draw axes
        drawLine(
            color = Color(0xFFFF0000),
            start = Offset(chartLeft, chartBottom),
            end = Offset(chartRight, chartBottom),
            strokeWidth = 1.dp.toPx()
        )
    }
}
