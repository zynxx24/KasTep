package com.kastep.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kastep.app.data.KastepViewModel
import com.kastep.app.data.PaymentRecord
import com.kastep.app.ui.theme.KastepBlack
import com.kastep.app.ui.theme.KastepBlue
import com.kastep.app.ui.theme.KastepCyan
import com.kastep.app.ui.theme.KastepGray
import com.kastep.app.ui.theme.KastepWhite

@Composable
fun RiwayatPembayaranScreen(
    viewModel: KastepViewModel,
    onOpenDrawer: () -> Unit
) {
    val paymentRecords by viewModel.paymentRecords.collectAsState()
    val userProfile by viewModel.userProfile.collectAsState()
    val currentDate = viewModel.getCurrentDateString()

    val itemsPerPage = 5
    val totalPages = (paymentRecords.size + itemsPerPage - 1) / itemsPerPage
    var currentPage by remember { mutableIntStateOf(1) }

    val startIndex = (currentPage - 1) * itemsPerPage
    val endIndex = minOf(startIndex + itemsPerPage, paymentRecords.size)
    val currentPageItems = paymentRecords.subList(startIndex, endIndex)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(KastepBlack)
    ) {
        // Top bar
        TopBar(currentDate = currentDate, userName = userProfile.nama, onMenuClick = onOpenDrawer)

        // Title section
        Column(modifier = Modifier.padding(horizontal = 20.dp)) {
            Box(
                modifier = Modifier
                    .background(
                        color = KastepBlue.copy(alpha = 0.25f),
                        shape = RoundedCornerShape(4.dp)
                    )
                    .padding(horizontal = 8.dp, vertical = 2.dp)
            ) {
                Text(
                    text = "Riwayat Pembayaran",
                    color = KastepWhite,
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

        Spacer(modifier = Modifier.height(12.dp))

        // Bayar Kas button (top right)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.End
        ) {
            Button(
                onClick = { },
                colors = ButtonDefaults.buttonColors(containerColor = KastepBlue),
                shape = RoundedCornerShape(20.dp)
            ) {
                Text("Bayar Kas", color = KastepWhite, fontSize = 13.sp, fontWeight = FontWeight.Medium)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Payment table in a rounded card
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .border(
                    width = 1.dp,
                    color = KastepGray.copy(alpha = 0.3f),
                    shape = RoundedCornerShape(16.dp)
                )
                .clip(RoundedCornerShape(16.dp))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                // Table header
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                ) {
                    Text("NO", color = KastepGray, fontSize = 12.sp, fontWeight = FontWeight.Medium, modifier = Modifier.width(30.dp))
                    Text("Tanggal", color = KastepGray, fontSize = 12.sp, fontWeight = FontWeight.Medium, modifier = Modifier.weight(1.2f))
                    Text("Nama Siswa", color = KastepGray, fontSize = 12.sp, fontWeight = FontWeight.Medium, modifier = Modifier.weight(1f))
                    Text("Jumlah", color = KastepGray, fontSize = 12.sp, fontWeight = FontWeight.Medium, modifier = Modifier.weight(1f))
                }

                HorizontalDivider(color = KastepGray.copy(alpha = 0.2f), thickness = 0.5.dp)

                // Table rows
                currentPageItems.forEach { record ->
                    Spacer(modifier = Modifier.height(20.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("${record.no}", color = KastepWhite, fontSize = 14.sp, modifier = Modifier.width(30.dp))
                        Text(record.tanggal, color = KastepWhite, fontSize = 14.sp, modifier = Modifier.weight(1.2f))
                        Text(record.namaSiswa, color = KastepWhite, fontSize = 14.sp, modifier = Modifier.weight(1f))
                        Text("Rp${KastepViewModel.formatRupiah(record.jumlah)}", color = KastepWhite, fontSize = 14.sp, modifier = Modifier.weight(1f))
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    HorizontalDivider(color = KastepGray.copy(alpha = 0.1f), thickness = 0.5.dp)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Pagination
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Previous
            Text(
                text = "<",
                color = if (currentPage > 1) KastepWhite else KastepGray.copy(alpha = 0.3f),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .clickable(enabled = currentPage > 1) { currentPage-- }
                    .padding(horizontal = 8.dp)
            )

            Spacer(modifier = Modifier.width(8.dp))

            // Page numbers
            for (page in 1..minOf(totalPages, 3)) {
                val isSelected = page == currentPage
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(if (isSelected) KastepBlue else Color.Transparent)
                        .clickable { currentPage = page },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "$page",
                        color = KastepWhite,
                        fontSize = 14.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                    )
                }
                Spacer(modifier = Modifier.width(4.dp))
            }

            if (totalPages > 3) {
                Text(". . .", color = KastepGray, fontSize = 14.sp)
                Spacer(modifier = Modifier.width(4.dp))
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(if (currentPage == totalPages) KastepBlue else Color.Transparent)
                        .clickable { currentPage = totalPages },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "$totalPages",
                        color = KastepWhite,
                        fontSize = 14.sp,
                        fontWeight = if (currentPage == totalPages) FontWeight.Bold else FontWeight.Normal
                    )
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Next
            Text(
                text = ">",
                color = if (currentPage < totalPages) KastepWhite else KastepGray.copy(alpha = 0.3f),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .clickable(enabled = currentPage < totalPages) { currentPage++ }
                    .padding(horizontal = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))
    }
}
