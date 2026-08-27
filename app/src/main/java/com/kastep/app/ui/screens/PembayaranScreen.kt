package com.kastep.app.ui.screens

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kastep.app.R
import com.kastep.app.data.KastepViewModel
import com.kastep.app.ui.theme.KastepBlack
import com.kastep.app.ui.theme.KastepGray
import com.kastep.app.ui.theme.KastepWhite

@Composable
fun PembayaranScreen(
    viewModel: KastepViewModel,
    onOpenDrawer: () -> Unit
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
        TopBar(currentDate = currentDate, userName = userProfile.nama, onMenuClick = onOpenDrawer)

        Spacer(modifier = Modifier.height(12.dp))

        // Title
        Text(
            text = "Pembayaran",
            color = KastepWhite,
            fontSize = 22.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(horizontal = 20.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        HorizontalDivider(color = KastepGray.copy(alpha = 0.3f), thickness = 0.5.dp)

        // QRIS Section
        PaymentMethodSection {
            Card(
                modifier = Modifier.size(width = 100.dp, height = 60.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_qris),
                        contentDescription = "QRIS",
                        modifier = Modifier
                            .size(80.dp)
                            .padding(8.dp),
                        contentScale = ContentScale.Fit
                    )
                }
            }
        }

        HorizontalDivider(color = KastepGray.copy(alpha = 0.3f), thickness = 0.5.dp)

        // E-Wallets Section (DANA, GoPay, OVO)
        PaymentMethodSection {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                PaymentIconCard(iconRes = R.drawable.ic_dana, label = "DANA")
                PaymentIconCard(iconRes = R.drawable.ic_gopay, label = "GoPay")
                PaymentIconCard(iconRes = R.drawable.ic_ovo, label = "OVO")
            }
        }

        HorizontalDivider(color = KastepGray.copy(alpha = 0.3f), thickness = 0.5.dp)

        // Banks Section (BCA, Mandiri, BNI)
        PaymentMethodSection {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                PaymentIconCard(iconRes = R.drawable.ic_bca, label = "BCA")
                PaymentIconCard(iconRes = R.drawable.ic_mandiri, label = "Mandiri")
                PaymentIconCard(iconRes = R.drawable.ic_bni, label = "BNI")
            }
        }

        HorizontalDivider(color = KastepGray.copy(alpha = 0.3f), thickness = 0.5.dp)

        // CASH Section
        PaymentMethodSection {
            Text(
                text = "CASH",
                color = KastepWhite,
                fontSize = 32.sp,
                fontWeight = FontWeight.ExtraBold
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Big green PEMBAYARAN button
        Button(
            onClick = { },
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp)
                .padding(horizontal = 40.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text(
                text = "PEMBAYARAN",
                color = KastepWhite,
                fontSize = 24.sp,
                fontWeight = FontWeight.ExtraBold
            )
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
private fun PaymentMethodSection(content: @Composable () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        content()
    }
}

@Composable
private fun PaymentIconCard(
    iconRes: Int,
    label: String
) {
    Card(
        modifier = Modifier.size(width = 100.dp, height = 70.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = iconRes),
                contentDescription = label,
                modifier = Modifier
                    .size(60.dp)
                    .padding(4.dp),
                contentScale = ContentScale.Fit
            )
        }
    }
}
