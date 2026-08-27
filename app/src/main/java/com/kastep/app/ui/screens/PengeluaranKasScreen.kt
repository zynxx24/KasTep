package com.kastep.app.ui.screens

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kastep.app.data.KastepViewModel
import com.kastep.app.data.TransactionType
import com.kastep.app.ui.theme.GradientBlueEnd
import com.kastep.app.ui.theme.GradientBlueStart
import com.kastep.app.ui.theme.KastepBlack
import com.kastep.app.ui.theme.KastepBlue
import com.kastep.app.ui.theme.KastepGray
import com.kastep.app.ui.theme.KastepWhite

@Composable
fun PengeluaranKasScreen(
    viewModel: KastepViewModel,
    onOpenDrawer: () -> Unit
) {
    val userProfile by viewModel.userProfile.collectAsState()
    val currentDate = viewModel.getCurrentDateString()

    var tanggalHari by remember { mutableStateOf("") }
    var tanggalBulan by remember { mutableStateOf("") }
    var tanggalTahun by remember { mutableStateOf("") }
    var jumlah by remember { mutableStateOf("") }
    var keterangan by remember { mutableStateOf("") }

    val textFieldColors = OutlinedTextFieldDefaults.colors(
        focusedTextColor = KastepWhite,
        unfocusedTextColor = KastepWhite,
        cursorColor = KastepWhite,
        focusedBorderColor = KastepGray,
        unfocusedBorderColor = KastepGray.copy(alpha = 0.5f),
        focusedPlaceholderColor = KastepGray,
        unfocusedPlaceholderColor = KastepGray
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(KastepBlack)
            .verticalScroll(rememberScrollState())
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
                    text = "Pengeluaran Kas",
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

        Spacer(modifier = Modifier.height(32.dp))

        // Tanggal (Date) section
        Column(modifier = Modifier.padding(horizontal = 20.dp)) {
            Text(
                text = "Tanggal",
                color = KastepWhite,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Three date input boxes
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Day
                OutlinedTextField(
                    value = tanggalHari,
                    onValueChange = { if (it.length <= 2) tanggalHari = it },
                    modifier = Modifier
                        .weight(0.8f)
                        .height(56.dp),
                    placeholder = { Text("DD") },
                    colors = textFieldColors,
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                // Month
                OutlinedTextField(
                    value = tanggalBulan,
                    onValueChange = { if (it.length <= 2) tanggalBulan = it },
                    modifier = Modifier
                        .weight(1.2f)
                        .height(56.dp),
                    placeholder = { Text("MM") },
                    colors = textFieldColors,
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                // Year
                OutlinedTextField(
                    value = tanggalTahun,
                    onValueChange = { if (it.length <= 4) tanggalTahun = it },
                    modifier = Modifier
                        .weight(1.2f)
                        .height(56.dp),
                    placeholder = { Text("YYYY") },
                    colors = textFieldColors,
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Jumlah (Amount) section
        Column(modifier = Modifier.padding(horizontal = 20.dp)) {
            Text(
                text = "Jumlah",
                color = KastepWhite,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = jumlah,
                onValueChange = { jumlah = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                placeholder = { Text("Rp 0") },
                colors = textFieldColors,
                shape = RoundedCornerShape(28.dp),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Keterangan (Description) section
        Column(modifier = Modifier.padding(horizontal = 20.dp)) {
            Text(
                text = "Keterangan",
                color = KastepWhite,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = keterangan,
                onValueChange = { keterangan = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                placeholder = { Text("Keterangan") },
                colors = textFieldColors,
                shape = RoundedCornerShape(20.dp),
                maxLines = 5
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        // Submit button (blue gradient — "Login" as in design)
        Button(
            onClick = {
                val amount = jumlah.toLongOrNull() ?: 0L
                if (amount > 0) {
                    viewModel.addTransaction(
                        title = keterangan.ifBlank { "Pengeluaran Kas" },
                        amount = amount,
                        type = TransactionType.EXPENSE
                    )
                    // Reset fields
                    tanggalHari = ""
                    tanggalBulan = ""
                    tanggalTahun = ""
                    jumlah = ""
                    keterangan = ""
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .padding(horizontal = 32.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
            shape = RoundedCornerShape(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(GradientBlueStart, GradientBlueEnd)
                        ),
                        shape = RoundedCornerShape(16.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Login",
                    color = KastepWhite,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}
