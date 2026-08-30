package com.kastep.app.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kastep.app.R
import com.kastep.app.data.KastepViewModel
import com.kastep.app.ui.theme.KastepBlack
import com.kastep.app.ui.theme.KastepGray
import com.kastep.app.ui.theme.KastepGreen
import com.kastep.app.ui.theme.KastepWhite

@Composable
fun PembayaranBerhasilScreen(
    viewModel: KastepViewModel,
    onOpenDrawer: () -> Unit,
    onNavigateToProfile: () -> Unit = {},
    onKembali: () -> Unit
) {
    val userProfile by viewModel.userProfile.collectAsState()
    val currentDate = viewModel.getCurrentDateString()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(KastepBlack)
            .verticalScroll(rememberScrollState())
    ) {
        // Top bar
        TopBar(currentDate = currentDate, userName = userProfile.nama, onMenuClick = onOpenDrawer, onNavigateToProfile = onNavigateToProfile)

        // Confetti + SpongeBob area
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(260.dp),
            contentAlignment = Alignment.Center
        ) {
            // Confetti decoration
            ConfettiDecoration()

            // SpongeBob image from drawable
            Image(
                painter = painterResource(id = R.drawable.ic_spongebob),
                contentDescription = "SpongeBob",
                modifier = Modifier
                    .size(220.dp),
                contentScale = ContentScale.Fit
            )
        }

        // Green curved section
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = KastepGreen,
                    shape = RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp)
                )
                .padding(vertical = 32.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Money flying emoji
                Text(
                    text = "💸💵💸",
                    fontSize = 36.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Rp 10.000",
                    color = KastepWhite,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "PEMBAYARAN BERHASIL",
                    color = KastepWhite,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold,
                    textAlign = TextAlign.Center
                )
            }
        }

        // Details section on light green background
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFE8F5E9))
                .padding(horizontal = 24.dp, vertical = 20.dp)
        ) {
            // Jumlah Pembayaran
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Jumlah\nPembayaran",
                    color = KastepBlack,
                    fontSize = 14.sp,
                    lineHeight = 20.sp
                )
                Text(
                    text = "Rp 10.000",
                    color = KastepBlack,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            HorizontalDivider(
                color = KastepGray.copy(alpha = 0.4f),
                modifier = Modifier.padding(vertical = 12.dp)
            )

            // Atas Nama
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Atas\nNama",
                    color = KastepBlack,
                    fontSize = 14.sp,
                    lineHeight = 20.sp
                )
                Text(
                    text = "Ruli",
                    color = KastepBlack,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Kembali button
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Button(
                onClick = onKembali,
                modifier = Modifier
                    .width(200.dp)
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(containerColor = KastepGreen),
                shape = RoundedCornerShape(24.dp)
            ) {
                Text(
                    text = "Kembali",
                    color = KastepWhite,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
private fun ConfettiDecoration() {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val colors = listOf(
            Color(0xFFFF0000),
            Color(0xFF00FF00),
            Color(0xFFFFFF00),
            Color(0xFF0000FF),
            Color(0xFFFF00FF),
            Color(0xFFFF8800),
        )

        val confettiPositions = listOf(
            Offset(size.width * 0.1f, size.height * 0.15f),
            Offset(size.width * 0.2f, size.height * 0.05f),
            Offset(size.width * 0.3f, size.height * 0.2f),
            Offset(size.width * 0.15f, size.height * 0.35f),
            Offset(size.width * 0.8f, size.height * 0.1f),
            Offset(size.width * 0.85f, size.height * 0.25f),
            Offset(size.width * 0.7f, size.height * 0.05f),
            Offset(size.width * 0.9f, size.height * 0.35f),
            Offset(size.width * 0.5f, size.height * 0.05f),
            Offset(size.width * 0.6f, size.height * 0.15f),
            Offset(size.width * 0.4f, size.height * 0.08f),
            Offset(size.width * 0.75f, size.height * 0.3f),
        )

        confettiPositions.forEachIndexed { index, pos ->
            val color = colors[index % colors.size]
            val confettiSize = if (index % 3 == 0) 6.dp.toPx() else 4.dp.toPx()
            if (index % 2 == 0) {
                drawCircle(color = color, radius = confettiSize, center = pos)
            } else {
                drawLine(
                    color = color,
                    start = pos,
                    end = Offset(pos.x + confettiSize * 2, pos.y + confettiSize),
                    strokeWidth = 3.dp.toPx()
                )
            }
        }
    }
}
