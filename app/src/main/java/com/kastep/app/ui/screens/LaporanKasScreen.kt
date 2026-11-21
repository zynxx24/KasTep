// ==========================================================================
// PETA FILE · KASTEP (Kelompok 1) · LaporanKasScreen.kt
// Isi file : Laporan Kas: filter periode, total, detail lunas/belum, grafik, bagikan laporan.
// Penanda  : CARI-LAPORAN, CARI-SALDO, CARI-GRAFIK
// Cara cari: VS Code Ctrl+Shift+F (Android Studio Ctrl+Shift+F) lalu ketik
//            "CARI-" untuk semua penanda, atau salah satu nama di baris Penanda.
// ==========================================================================
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
import androidx.compose.material.icons.filled.Share
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
import com.kastep.app.data.UserRole
import com.kastep.app.ui.theme.*

// CARI-LAPORAN CARI-SALDO | Pemasukan & pengeluaran mengikuti periode yang dipilih; Semua Periode menampilkan saldo akhir.
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LaporanKasScreen(viewModel: KastepViewModel, onOpenDrawer: () -> Unit, onNavigateToProfile: () -> Unit = {}) {
    val userProfile by viewModel.userProfile.collectAsState()
    val isAdmin = userProfile.role == UserRole.ADMIN
    val students by viewModel.students.collectAsState()
    val paymentRecords by viewModel.paymentRecords.collectAsState()
    val pengeluaranList by viewModel.pengeluaranList.collectAsState()
    val totalIncome = paymentRecords.sumOf { it.jumlah }
    val totalExpense = pengeluaranList.sumOf { it.jumlah }
    val saldo = totalIncome - totalExpense

    var selectedPeriode by remember { mutableStateOf("Semua Periode") }
    var showPeriodeMenu by remember { mutableStateOf(false) }
    val bulanKas = viewModel.getBulanKas()
    val periodeOptions = listOf("Semua Periode") + bulanKas
    val context = androidx.compose.ui.platform.LocalContext.current

    val semua = selectedPeriode == "Semua Periode"
    val periodeYm = com.kastep.app.data.KastepRepository.parseBulan(selectedPeriode)
    val filteredPayments = if (semua) paymentRecords else paymentRecords.filter { it.bulan == selectedPeriode }
    val filteredExpenses = if (semua) pengeluaranList
        else pengeluaranList.filter { com.kastep.app.data.KastepRepository.bulanDariTanggal(it.tanggal) == periodeYm }
    val filteredIncome = filteredPayments.sumOf { it.jumlah }
    val filteredExpense = filteredExpenses.sumOf { it.jumlah }

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

        // Summary cards — CLICKABLE
        Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            ReportSummaryCard(
                modifier = Modifier.weight(1f).clickable { showPemasukanSheet = true },
                label = "Total Pemasukan", amount = "Rp ${KastepViewModel.formatRupiah(filteredIncome)}",
                borderColor = KastepBlue, labelColor = KastepBlue, coinColor = KastepBlue
            )
            ReportSummaryCard(
                modifier = Modifier.weight(1f).clickable { showPengeluaranSheet = true },
                label = "Total Pengeluaran", amount = "Rp ${KastepViewModel.formatRupiah(filteredExpense)}",
                borderColor = KastepRed, labelColor = KastepRed, coinColor = KastepRed
            )
        }

        Spacer(modifier = Modifier.height(8.dp))
        Text("Tap kartu untuk melihat detail", color = KastepGray, fontSize = 11.sp, modifier = Modifier.padding(horizontal = 20.dp))

        Spacer(modifier = Modifier.height(12.dp))

        ReportSummaryCard(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            label = if (semua) "Saldo Akhir" else "Selisih $selectedPeriode",
            amount = (if (!semua && filteredIncome - filteredExpense < 0) "-Rp " else "Rp ") + KastepViewModel.formatRupiah(kotlin.math.abs(if (semua) saldo else filteredIncome - filteredExpense)),
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
                // Data grafik: pemasukan & pengeluaran nyata per bulan
                val bulanGrafik = (paymentRecords.mapNotNull { com.kastep.app.data.KastepRepository.parseBulan(it.bulan) } +
                    pengeluaranList.mapNotNull { com.kastep.app.data.KastepRepository.bulanDariTanggal(it.tanggal) })
                    .distinct().sortedBy { it.first * 12 + it.second }.takeLast(6)
                IncomeExpenseChart(
                    labels = bulanGrafik.map { listOf("Jan", "Feb", "Mar", "Apr", "Mei", "Jun", "Jul", "Agu", "Sep", "Okt", "Nov", "Des")[it.second - 1] },
                    income = bulanGrafik.map { ym -> paymentRecords.filter { com.kastep.app.data.KastepRepository.parseBulan(it.bulan) == ym }.sumOf { it.jumlah } },
                    expense = bulanGrafik.map { ym -> pengeluaranList.filter { com.kastep.app.data.KastepRepository.bulanDariTanggal(it.tanggal) == ym }.sumOf { it.jumlah } },
                    modifier = Modifier.fillMaxWidth().height(220.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Bagikan laporan ringkas ke WhatsApp / aplikasi lain
        Button(
            onClick = {
                val intent = android.content.Intent(android.content.Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(android.content.Intent.EXTRA_TEXT, viewModel.generateReportText())
                }
                context.startActivity(android.content.Intent.createChooser(intent, "Bagikan laporan kas"))
            },
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).height(48.dp),
            colors = ButtonDefaults.buttonColors(containerColor = KastepBlue),
            shape = RoundedCornerShape(14.dp)
        ) {
            Icon(Icons.Default.Share, contentDescription = null, tint = KastepWhite, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text("Bagikan Laporan", color = KastepWhite, fontWeight = FontWeight.Bold)
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
                    val daftarBulan = if (semua) bulanKas else listOf(selectedPeriode)
                    daftarBulan.forEach { b ->
                        val lunas = students.filter { viewModel.statusBulan(it, b) == StatusBayar.LUNAS }
                        val belum = students.filter { viewModel.statusBulan(it, b) == StatusBayar.BELUM }
                        Text("📅 $b", fontWeight = FontWeight.Bold, color = KastepBlue, fontSize = 14.sp)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("✅ Lunas (${lunas.size})", fontWeight = FontWeight.Medium, color = KastepGreen, fontSize = 13.sp)
                        Text("❌ Belum (${belum.size})" + if (isAdmin) ":" else "", fontWeight = FontWeight.Medium, color = KastepRed, fontSize = 13.sp)
                        // Privasi: nama yang belum bayar hanya untuk ADMIN, USER cukup melihat jumlahnya
                        if (isAdmin) belum.forEach {
                            val denda = viewModel.calculateDenda(it.nama, b)
                            Text(
                                "  • ${it.nama} — kurang Rp ${KastepViewModel.formatRupiah(viewModel.sisaKas(it, b))}" +
                                    if (denda > 0) " + denda Rp ${KastepViewModel.formatRupiah(denda)}" else "",
                                fontSize = 12.sp
                            )
                        }
                        Spacer(modifier = Modifier.height(12.dp))
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
            title = { Text("Detail Pengeluaran - $selectedPeriode", fontWeight = FontWeight.Bold, fontSize = 16.sp) },
            text = {
                Column(modifier = Modifier.verticalScroll(rememberScrollState()).heightIn(max = 400.dp)) {
                    if (filteredExpenses.isEmpty()) {
                        Text("Belum ada pengeluaran.", color = Color.Gray)
                    } else {
                        filteredExpenses.forEachIndexed { idx, p ->
                            Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text("${idx + 1}. ${p.keterangan}", fontSize = 13.sp, fontWeight = FontWeight.Medium)
                                    Text(p.tanggal, fontSize = 11.sp, color = Color.Gray)
                                }
                                Text("Rp ${KastepViewModel.formatRupiah(p.jumlah)}", color = KastepRed, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                            }
                            if (idx < filteredExpenses.size - 1) HorizontalDivider(modifier = Modifier.padding(vertical = 2.dp))
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Total: Rp ${KastepViewModel.formatRupiah(filteredExpense)}", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    }
                }
            },
            confirmButton = { TextButton(onClick = { showPengeluaranSheet = false }) { Text("Tutup") } }
        )
    }
}

// Kartu Total Pemasukan / Total Pengeluaran, bisa ditekan untuk melihat rincian.
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

// Ikon tumpukan koin digambar dengan Canvas.
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

// CARI-GRAFIK | Grafik batang 6 bulan terakhir: biru = pemasukan, abu = pengeluaran.
@Composable
private fun IncomeExpenseChart(labels: List<String>, income: List<Long>, expense: List<Long>, modifier: Modifier = Modifier) {
    val maxValue = (income + expense).maxOrNull()?.coerceAtLeast(1L)?.toFloat() ?: 1f
    Canvas(modifier = modifier) {
        if (labels.isEmpty()) return@Canvas
        val chartLeft = 44.dp.toPx(); val chartBottom = size.height - 26.dp.toPx()
        val chartTop = 10.dp.toPx(); val chartRight = size.width - 10.dp.toPx()
        val chartWidth = chartRight - chartLeft; val chartHeight = chartBottom - chartTop

        val paint = android.graphics.Paint().apply {
            color = android.graphics.Color.parseColor("#9E9E9E"); textSize = 10.sp.toPx(); isAntiAlias = true
        }
        // Garis bantu sumbu Y (dalam ribuan rupiah)
        for (i in 0..4) {
            val v = maxValue * i / 4
            val y = chartBottom - chartHeight * i / 4
            drawContext.canvas.nativeCanvas.drawText("${(v / 1000).toInt()}rb", 2.dp.toPx(), y + 4.dp.toPx(), paint)
            drawLine(Color(0xFF333333), Offset(chartLeft, y), Offset(chartRight, y), 0.5.dp.toPx())
        }
        val slot = chartWidth / labels.size
        val barW = (slot * 0.32f).coerceAtMost(18.dp.toPx())
        labels.forEachIndexed { i, label ->
            val cx = chartLeft + slot * i + slot / 2
            val hIn = chartHeight * (income[i] / maxValue)
            val hOut = chartHeight * (expense[i] / maxValue)
            drawRect(Color(0xFF4A90D9), Offset(cx - barW - 1.dp.toPx(), chartBottom - hIn), androidx.compose.ui.geometry.Size(barW, hIn))
            drawRect(Color(0xFF9E9E9E), Offset(cx + 1.dp.toPx(), chartBottom - hOut), androidx.compose.ui.geometry.Size(barW, hOut))
            drawContext.canvas.nativeCanvas.drawText(label, cx - 9.dp.toPx(), size.height - 6.dp.toPx(), paint)
        }
        drawLine(Color(0xFF555555), Offset(chartLeft, chartBottom), Offset(chartRight, chartBottom), 1.dp.toPx())
    }
}
