package com.kastep.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kastep.app.data.KastepViewModel
import com.kastep.app.data.UserRole
import com.kastep.app.ui.theme.*

@Composable
fun DashboardScreen(
    viewModel: KastepViewModel,
    onNavigateToProfile: () -> Unit,
    onOpenDrawer: () -> Unit = {}
) {
    val paymentRecords by viewModel.paymentRecords.collectAsState()
    val pengeluaranList by viewModel.pengeluaranList.collectAsState()
    val userProfile by viewModel.userProfile.collectAsState()
    val currentDate = viewModel.getCurrentDateString()

    // Merge payments and expenses into a recent transaction list
    data class RecentTx(val title: String, val date: String, val amount: Long, val isIncome: Boolean)
    val recentTxList = (
        paymentRecords.map { RecentTx("Pembayaran - ${it.namaSiswa}", it.tanggal, it.jumlah, true) } +
        pengeluaranList.map { RecentTx(it.keterangan, it.tanggal, it.jumlah, false) }
    ).take(10)

    Column(modifier = Modifier.fillMaxSize().background(KastepBlack)) {
        // Top bar
        TopBar(currentDate = currentDate, userName = userProfile.nama, onMenuClick = onOpenDrawer, onNavigateToProfile = onNavigateToProfile)

        // Dashboard title + role badge
        Column(modifier = Modifier.padding(horizontal = 20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier.background(KastepGreen.copy(alpha = 0.2f), RoundedCornerShape(4.dp))
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    Text("Dashboard", color = KastepGreen, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.width(8.dp))
                Box(
                    modifier = Modifier.background(
                        if (userProfile.role == UserRole.ADMIN) KastepCyan.copy(alpha = 0.2f) else KastepGray.copy(alpha = 0.2f),
                        RoundedCornerShape(4.dp)
                    ).padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        if (userProfile.role == UserRole.ADMIN) "Admin" else "User",
                        color = if (userProfile.role == UserRole.ADMIN) KastepCyan else KastepGray,
                        fontSize = 12.sp, fontWeight = FontWeight.Bold
                    )
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text("Ringkasan informasi kas kelas", color = KastepGray, fontSize = 13.sp)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Summary cards
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            SummaryCard(Modifier.weight(1f), "Saldo saat ini", "RP ${KastepViewModel.formatRupiah(viewModel.saldo)}", Icons.Default.SwapVert, KastepCyan)
            SummaryCard(Modifier.weight(1f), "Pemasukan", "RP ${KastepViewModel.formatRupiah(viewModel.totalIncome)}", Icons.Default.ArrowDownward, KastepGreen)
            SummaryCard(Modifier.weight(1f), "Pengeluaran", "RP ${KastepViewModel.formatRupiah(viewModel.totalExpense)}", Icons.Default.ArrowUpward, KastepRed)
        }

        Spacer(modifier = Modifier.height(16.dp))

        LinearProgressIndicator(
            progress = { if (viewModel.totalIncome > 0) (viewModel.totalExpense.toFloat() / viewModel.totalIncome.toFloat()).coerceIn(0f, 1f) else 0f },
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).height(8.dp).clip(RoundedCornerShape(4.dp)),
            color = KastepCyan, trackColor = KastepGray.copy(alpha = 0.3f)
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Recent Transactions
        Card(
            modifier = Modifier.fillMaxWidth().weight(1f).padding(horizontal = 16.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = KastepCardDark)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text("Transaksi Terbaru", color = KastepWhite, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    Text("${recentTxList.size} transaksi", color = KastepCyan, fontSize = 13.sp, fontWeight = FontWeight.Medium)
                }
                Spacer(modifier = Modifier.height(16.dp))

                if (recentTxList.isEmpty()) {
                    Text("Belum ada transaksi", color = KastepGray, fontSize = 14.sp)
                } else {
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        items(recentTxList) { tx ->
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Top) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(tx.title, color = KastepWhite, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(tx.date, color = KastepGray, fontSize = 11.sp)
                                }
                                Text(
                                    "${if (tx.isIncome) "+" else "-"} ${KastepViewModel.formatRupiah(tx.amount)}",
                                    color = if (tx.isIncome) KastepGreen else KastepRed,
                                    fontSize = 14.sp, fontWeight = FontWeight.Bold
                                )
                            }
                            HorizontalDivider(color = KastepGray.copy(alpha = 0.15f), thickness = 0.5.dp, modifier = Modifier.padding(vertical = 8.dp))
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun SummaryCard(modifier: Modifier = Modifier, title: String, amount: String, icon: androidx.compose.ui.graphics.vector.ImageVector, iconTint: Color) {
    Card(modifier = modifier, shape = RoundedCornerShape(12.dp), colors = CardDefaults.cardColors(containerColor = KastepCardDark)) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(title, color = KastepGray, fontSize = 10.sp, fontWeight = FontWeight.Medium)
            Spacer(modifier = Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(icon, contentDescription = title, tint = iconTint, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(amount, color = KastepWhite, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}
