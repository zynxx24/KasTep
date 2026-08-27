package com.kastep.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
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
import com.kastep.app.data.Transaction
import com.kastep.app.data.TransactionType
import com.kastep.app.ui.theme.KastepBlack
import com.kastep.app.ui.theme.KastepCardDark
import com.kastep.app.ui.theme.KastepCyan
import com.kastep.app.ui.theme.KastepGray
import com.kastep.app.ui.theme.KastepGreen
import com.kastep.app.ui.theme.KastepRed
import com.kastep.app.ui.theme.KastepWhite

@Composable
fun DashboardScreen(
    viewModel: KastepViewModel,
    onNavigateToProfile: () -> Unit,
    onOpenDrawer: () -> Unit = {}
) {
    val transactions by viewModel.transactions.collectAsState()
    val userProfile by viewModel.userProfile.collectAsState()
    val currentDate = viewModel.getCurrentDateString()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(KastepBlack)
    ) {
        // Top bar
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

            // Date with calendar icon
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Default.CalendarMonth,
                    contentDescription = "Calendar",
                    tint = KastepWhite,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = currentDate,
                    color = KastepWhite,
                    fontSize = 14.sp
                )
            }

            // Admin avatar
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable { onNavigateToProfile() }
            ) {
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

        // Dashboard title
        Column(modifier = Modifier.padding(horizontal = 20.dp)) {
            Box(
                modifier = Modifier
                    .background(
                        color = KastepGreen.copy(alpha = 0.2f),
                        shape = RoundedCornerShape(4.dp)
                    )
                    .padding(horizontal = 8.dp, vertical = 2.dp)
            ) {
                Text(
                    text = "Dashboard",
                    color = KastepGreen,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Ringkasan informasi kas kelas",
                color = KastepGray,
                fontSize = 13.sp
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Summary cards row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Saldo card
            SummaryCard(
                modifier = Modifier.weight(1f),
                title = "Saldo saat ini",
                amount = "RP ${KastepViewModel.formatRupiah(viewModel.saldo)}",
                icon = Icons.Default.SwapVert,
                iconTint = KastepCyan
            )
            // Pemasukan card
            SummaryCard(
                modifier = Modifier.weight(1f),
                title = "Pemasukan",
                amount = "RP ${KastepViewModel.formatRupiah(viewModel.totalIncome)}",
                icon = Icons.Default.ArrowDownward,
                iconTint = KastepGreen
            )
            // Pengeluaran card
            SummaryCard(
                modifier = Modifier.weight(1f),
                title = "Pengeluaran",
                amount = "RP ${KastepViewModel.formatRupiah(viewModel.totalExpense)}",
                icon = Icons.Default.ArrowUpward,
                iconTint = KastepRed
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Progress bar (decorative)
        LinearProgressIndicator(
            progress = { if (viewModel.totalIncome > 0) (viewModel.totalExpense.toFloat() / viewModel.totalIncome.toFloat()).coerceIn(0f, 1f) else 0f },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp)),
            color = KastepCyan,
            trackColor = KastepGray.copy(alpha = 0.3f)
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Transaksi Terbaru card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = 16.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = KastepCardDark)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                // Header row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Transaksi Terbaru",
                        color = KastepWhite,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Lihat Semua",
                        color = KastepCyan,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Transaction list
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    items(transactions) { tx ->
                        TransactionItem(transaction = tx)
                        HorizontalDivider(
                            color = KastepGray.copy(alpha = 0.15f),
                            thickness = 0.5.dp,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun SummaryCard(
    modifier: Modifier = Modifier,
    title: String,
    amount: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconTint: Color
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = KastepCardDark)
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Text(
                text = title,
                color = KastepGray,
                fontSize = 10.sp,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    icon,
                    contentDescription = title,
                    tint = iconTint,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = amount,
                    color = KastepWhite,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun TransactionItem(transaction: Transaction) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
    ) {
        Column {
            Text(
                text = transaction.title,
                color = KastepWhite,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = transaction.date,
                color = KastepGray,
                fontSize = 11.sp
            )
        }

        Column(horizontalAlignment = Alignment.End) {
            val isIncome = transaction.type == TransactionType.INCOME
            Text(
                text = "${if (isIncome) "+" else "-"} ${KastepViewModel.formatRupiah(transaction.amount)}",
                color = if (isIncome) KastepGreen else KastepRed,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = transaction.date,
                color = KastepGray,
                fontSize = 11.sp
            )
        }
    }
}
