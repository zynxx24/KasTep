package com.kastep.app.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import com.kastep.app.data.StatusBayar
import com.kastep.app.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LaporanKasScreen(viewModel: KastepViewModel, onOpenDrawer: () -> Unit, onNavigateToProfile: () -> Unit = {}) {
    val userProfile by viewModel.userProfile.collectAsState()
    val students by viewModel.students.collectAsState()
    val paymentRecords by viewModel.paymentRecords.collectAsState()
    val pengeluaranList by viewModel.pengeluaranList.collectAsState()
    val totalIncome = paymentRecords.sumOf { it.jumlah }
    val totalExpense = pengeluaranList.sumOf { it.jumlah }
    val saldo = totalIncome - totalExpense

    var selectedPeriode by remember { mutableStateOf("Juli 2026") }
    var showPeriodeMenu by remember { mutableStateOf(false) }
    val periodeOptions = listOf("Juli 2026", "Agustus 2026", "Semua Periode")

    var showPemasukanSheet by remember { mutableStateOf(false) }
    var showPengeluaranSheet by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize().background(KastepBlack).verticalScroll(rememberScrollState())
    ) {
        // Top bar
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onOpenDrawer) {
                Icon(Icons.Default.Menu, contentDescription = "Menu", tint = KastepWhite, modifier = Modifier.size(28.dp))
            }
            Text("Laporan kas", color = KastepWhite, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .clickable { onNavigateToProfile() }
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Box(modifier = Modifier.size(32.dp).clip(CircleShape).background(KastepGray), contentAlignment = Alignment.Center) {
                    Icon(Icons.Default.Person, contentDescription = "Profile", tint = KastepWhite, modifier = Modifier.size(20.dp))
                }
                Spacer(modifier = Modifier.width(6.dp))
                Text(userProfile.nama, color = KastepWhite, fontSize = 14.sp, fontWeight = FontWeight.Medium)
            }
        }

        // Periode dropdown - CLICKABLE
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Periode", color = KastepWhite, fontSize = 14.sp)
            Spacer(modifier = Modifier.width(12.dp))
            ExposedDropdownMenuBox(expanded = showPeriodeMenu, onExpandedChange = { showPeriodeMenu = it }) {
                Box(
                    modifier = Modifier.weight(1f).menuAnchor()
                        .border(1.dp, KastepGray.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                        .padding(horizontal = 12.dp, vertical = 10.dp)
                ) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Text(selectedPeriode, color = KastepWhite, fontSize = 14.sp)
                        Icon(Icons.Default.ArrowDropDown, contentDescription = "Dropdown", tint = KastepWhite)
                    }
                }
                ExposedDropdownMenu(expanded = showPeriodeMenu, onDismissRequest = { showPeriodeMenu = false }) {
                    periodeOptions.forEach { option ->
                        DropdownMenuItem(text = { Text(option) }, onClick = {
                            selectedPeriode = option; showPeriodeMenu = false
                        })
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Filtered data based on period
        val filteredPayments = when {
            selectedPeriode.contains("Juli") -> paymentRecords.filter { it.bulan.contains("Juli") }
            selectedPeriode.contains("Agustus") -> paymentRecords.filter { it.bulan.contains("Agustus") }
            else -> paymentRecords
        }
        val filteredIncome = filteredPayments.sumOf { it.jumlah }

        // Summary cards — CLICKABLE
        Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            ReportSummaryCard(
                modifier = Modifier.weight(1f).clickable { showPemasukanSheet = true },
                label = "Total Pemasukan", amount = "Rp ${KastepViewModel.formatRupiah(filteredIncome)}",
                borderColor = KastepBlue, labelColor = KastepBlue, coinColor = KastepBlue
            )
            ReportSummaryCard(
                modifier = Modifier.weight(1f).clickable { showPengeluaranSheet = true },
                label = "Total Pengeluaran", amount = "Rp ${KastepViewModel.formatRupiah(totalExpense)}",
                borderColor = KastepRed, labelColor = KastepRed, coinColor = KastepRed
            )
        }

        Spacer(modifier = Modifier.height(8.dp))
        Text("Tap kartu untuk melihat detail", color = KastepGray, fontSize = 11.sp, modifier = Modifier.padding(horizontal = 20.dp))

        Spacer(modifier = Modifier.height(12.dp))

        ReportSummaryCard(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            label = "Total Akhir", amount = "Rp ${KastepViewModel.formatRupiah(saldo)}",
            borderColor = KastepGreen, labelColor = KastepGreen, coinColor = KastepGreen
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Chart card
        Card(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A2E))
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text("Grafis Pemasukan & Pengeluaran", color = KastepWhite, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(12.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(modifier = Modifier.size(14.dp).background(KastepBlue))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Pemasukan", color = KastepWhite, fontSize = 12.sp)
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(modifier = Modifier.size(14.dp).background(KastepGray))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Pengeluaran", color = KastepWhite, fontSize = 12.sp)
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                IncomeExpenseChart(modifier = Modifier.fillMaxWidth().height(220.dp))
            }
        }

        Spacer(modifier = Modifier.height(32.dp))
    }

    // Pemasukan Bottom Sheet - shows paid/unpaid students
    if (showPemasukanSheet) {
        AlertDialog(
            onDismissRequest = { showPemasukanSheet = false },
            title = { Text("Detail Pemasukan - $selectedPeriode", fontWeight = FontWeight.Bold, fontSize = 16.sp) },
            text = {
                Column(modifier = Modifier.verticalScroll(rememberScrollState()).heightIn(max = 400.dp)) {
                    val isJuli = selectedPeriode.contains("Juli") || selectedPeriode.contains("Semua")
                    val isAgustus = selectedPeriode.contains("Agustus") || selectedPeriode.contains("Semua")

                    if (isJuli) {
                        Text("📅 Juli 2026", fontWeight = FontWeight.Bold, color = KastepBlue, fontSize = 14.sp)
                        Spacer(modifier = Modifier.height(4.dp))
                        val lunasJuli = students.filter { it.statusJuli == StatusBayar.LUNAS }
                        val belumJuli = students.filter { it.statusJuli == StatusBayar.BELUM }
                        Text("✅ Lunas (${lunasJuli.size}):", fontWeight = FontWeight.Medium, color = KastepGreen, fontSize = 13.sp)
                        lunasJuli.forEach { Text("  • ${it.nama}", fontSize = 12.sp) }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("❌ Belum (${belumJuli.size}):", fontWeight = FontWeight.Medium, color = KastepRed, fontSize = 13.sp)
                        belumJuli.forEach { Text("  • ${it.nama}", fontSize = 12.sp) }
                        Spacer(modifier = Modifier.height(12.dp))
                    }

                    if (isAgustus) {
                        Text("📅 Agustus 2026", fontWeight = FontWeight.Bold, color = KastepBlue, fontSize = 14.sp)
                        Spacer(modifier = Modifier.height(4.dp))
                        val lunasAgs = students.filter { it.statusAgustus == StatusBayar.LUNAS }
                        val belumAgs = students.filter { it.statusAgustus == StatusBayar.BELUM }
                        Text("✅ Lunas (${lunasAgs.size}):", fontWeight = FontWeight.Medium, color = KastepGreen, fontSize = 13.sp)
                        lunasAgs.forEach { Text("  • ${it.nama}", fontSize = 12.sp) }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("❌ Belum (${belumAgs.size}):", fontWeight = FontWeight.Medium, color = KastepRed, fontSize = 13.sp)
                        belumAgs.forEach { Text("  • ${it.nama}", fontSize = 12.sp) }
                    }
                }
            },
            confirmButton = { TextButton(onClick = { showPemasukanSheet = false }) { Text("Tutup") } }
        )
    }

    // Pengeluaran Bottom Sheet - shows expense list
    if (showPengeluaranSheet) {
        AlertDialog(
            onDismissRequest = { showPengeluaranSheet = false },
            title = { Text("Detail Pengeluaran", fontWeight = FontWeight.Bold, fontSize = 16.sp) },
            text = {
                Column(modifier = Modifier.verticalScroll(rememberScrollState()).heightIn(max = 400.dp)) {
                    if (pengeluaranList.isEmpty()) {
                        Text("Belum ada pengeluaran.", color = Color.Gray)
                    } else {
                        pengeluaranList.forEachIndexed { idx, p ->
                            Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text("${idx + 1}. ${p.keterangan}", fontSize = 13.sp, fontWeight = FontWeight.Medium)
                                    Text(p.tanggal, fontSize = 11.sp, color = Color.Gray)
                                }
                                Text("Rp ${KastepViewModel.formatRupiah(p.jumlah)}", color = KastepRed, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                            }
                            if (idx < pengeluaranList.size - 1) HorizontalDivider(modifier = Modifier.padding(vertical = 2.dp))
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Total: Rp ${KastepViewModel.formatRupiah(totalExpense)}", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    }
                }
            },
            confirmButton = { TextButton(onClick = { showPengeluaranSheet = false }) { Text("Tutup") } }
        )
    }
}

@Composable
private fun ReportSummaryCard(modifier: Modifier = Modifier, label: String, amount: String, borderColor: Color, labelColor: Color, coinColor: Color) {
    Box(modifier = modifier.border(1.5.dp, borderColor, RoundedCornerShape(12.dp)).padding(16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            CoinStackIcon(color = coinColor)
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(label, color = labelColor, fontSize = 11.sp, fontWeight = FontWeight.Medium)
                Spacer(modifier = Modifier.height(4.dp))
                Text(amount, color = KastepWhite, fontSize = 16.sp, fontWeight = FontWeight.Bold)
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
        for (i in 0..2) {
            val y = size.height * 0.3f + i * (coinHeight + 4.dp.toPx())
            drawRoundRect(color, Offset(startX, y), androidx.compose.ui.geometry.Size(coinWidth, coinHeight), androidx.compose.ui.geometry.CornerRadius(4.dp.toPx()))
            drawLine(color.copy(alpha = 0.5f), Offset(startX + coinWidth * 0.2f, y + coinHeight * 0.5f), Offset(startX + coinWidth * 0.8f, y + coinHeight * 0.5f), 1.dp.toPx())
        }
    }
}

@Composable
private fun IncomeExpenseChart(modifier: Modifier = Modifier) {
    val incomeData = listOf(5f, 15f, 10f, 20f, 30f, 100f)
    val expenseData = listOf(2f, 8f, 5f, 25f, 15f, 40f)
    val labels = listOf("1 Jul", "2 Jul", "3 Jul", "4 Jul", "5 Jul", "6 Jul")
    val yLabels = listOf("0", "10", "20", "50", "80", "100")

    Canvas(modifier = modifier) {
        val chartLeft = 50.dp.toPx(); val chartBottom = size.height - 30.dp.toPx()
        val chartTop = 10.dp.toPx(); val chartRight = size.width - 10.dp.toPx()
        val chartWidth = chartRight - chartLeft; val chartHeight = chartBottom - chartTop
        val maxValue = 100f

        val paint = android.graphics.Paint().apply {
            color = android.graphics.Color.parseColor("#9E9E9E"); textSize = 10.sp.toPx(); isAntiAlias = true
        }

        yLabels.forEachIndexed { _, label ->
            val y = chartBottom - (chartHeight * (label.toFloat() / maxValue))
            drawContext.canvas.nativeCanvas.drawText(label, 5.dp.toPx(), y + 4.dp.toPx(), paint)
            drawLine(Color(0xFF333333), Offset(chartLeft, y), Offset(chartRight, y), 0.5.dp.toPx())
        }

        labels.forEachIndexed { index, label ->
            val x = chartLeft + (chartWidth * index / (labels.size - 1).coerceAtLeast(1))
            drawContext.canvas.nativeCanvas.drawText(label, x - 15.dp.toPx(), size.height - 5.dp.toPx(), paint)
        }

        val incomePath = Path()
        incomeData.forEachIndexed { index, value ->
            val x = chartLeft + (chartWidth * index / (incomeData.size - 1).coerceAtLeast(1))
            val y = chartBottom - (chartHeight * (value / maxValue))
            if (index == 0) incomePath.moveTo(x, y) else incomePath.lineTo(x, y)
        }
        drawPath(incomePath, Color(0xFF4A90D9), style = Stroke(2.5.dp.toPx(), cap = StrokeCap.Round))

        val expensePath = Path()
        expenseData.forEachIndexed { index, value ->
            val x = chartLeft + (chartWidth * index / (expenseData.size - 1).coerceAtLeast(1))
            val y = chartBottom - (chartHeight * (value / maxValue))
            if (index == 0) expensePath.moveTo(x, y) else expensePath.lineTo(x, y)
        }
        drawPath(expensePath, Color(0xFF9E9E9E), style = Stroke(2.dp.toPx(), cap = StrokeCap.Round))

        incomeData.forEachIndexed { index, value ->
            val x = chartLeft + (chartWidth * index / (incomeData.size - 1).coerceAtLeast(1))
            val y = chartBottom - (chartHeight * (value / maxValue))
            drawCircle(Color(0xFF4A90D9), 4.dp.toPx(), Offset(x, y))
        }
        expenseData.forEachIndexed { index, value ->
            val x = chartLeft + (chartWidth * index / (expenseData.size - 1).coerceAtLeast(1))
            val y = chartBottom - (chartHeight * (value / maxValue))
            drawCircle(Color(0xFF9E9E9E), 4.dp.toPx(), Offset(x, y))
        }

        drawLine(Color(0xFFFF0000), Offset(chartLeft, chartBottom), Offset(chartRight, chartBottom), 1.dp.toPx())
    }
}
