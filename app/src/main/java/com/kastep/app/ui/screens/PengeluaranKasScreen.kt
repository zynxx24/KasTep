package com.kastep.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kastep.app.data.KastepViewModel
import com.kastep.app.data.Pengeluaran
import com.kastep.app.data.UserRole
import com.kastep.app.ui.theme.*

@Composable
fun PengeluaranKasScreen(viewModel: KastepViewModel, onOpenDrawer: () -> Unit) {
    val userProfile by viewModel.userProfile.collectAsState()
    val pengeluaranList by viewModel.pengeluaranList.collectAsState()
    val currentDate = viewModel.getCurrentDateString()
    val isAdmin = userProfile.role == UserRole.ADMIN

    var tanggalHari by remember { mutableStateOf("") }
    var tanggalBulan by remember { mutableStateOf("") }
    var tanggalTahun by remember { mutableStateOf("") }
    var jumlah by remember { mutableStateOf("") }
    var keterangan by remember { mutableStateOf("") }
    var errorMsg by remember { mutableStateOf<String?>(null) }
    var successMsg by remember { mutableStateOf<String?>(null) }

    var showEditDialog by remember { mutableStateOf(false) }
    var selectedPengeluaran by remember { mutableStateOf<Pengeluaran?>(null) }

    val textFieldColors = OutlinedTextFieldDefaults.colors(
        focusedTextColor = KastepWhite, unfocusedTextColor = KastepWhite,
        cursorColor = KastepWhite, focusedBorderColor = KastepCyan,
        unfocusedBorderColor = KastepGray.copy(alpha = 0.5f),
        focusedPlaceholderColor = KastepGray, unfocusedPlaceholderColor = KastepGray
    )

    Column(
        modifier = Modifier.fillMaxSize().background(KastepBlack).verticalScroll(rememberScrollState())
    ) {
        TopBar(currentDate = currentDate, userName = userProfile.nama, onMenuClick = onOpenDrawer)

        Column(modifier = Modifier.padding(horizontal = 20.dp)) {
            Box(
                modifier = Modifier.background(KastepBlue.copy(alpha = 0.25f), RoundedCornerShape(4.dp)).padding(horizontal = 8.dp, vertical = 2.dp)
            ) {
                Text("Pengeluaran Kas", color = KastepWhite, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text("Catat dan kelola pengeluaran kas kelas", color = KastepGray, fontSize = 13.sp)
        }

        Spacer(modifier = Modifier.height(24.dp))

        if (isAdmin) {
            // Error/Success messages
            if (errorMsg != null) {
                Text(errorMsg!!, color = KastepRed, fontSize = 13.sp, modifier = Modifier.padding(horizontal = 20.dp)
                    .fillMaxWidth().background(KastepRed.copy(alpha = 0.1f), RoundedCornerShape(8.dp)).padding(12.dp))
                Spacer(modifier = Modifier.height(8.dp))
            }
            if (successMsg != null) {
                Text(successMsg!!, color = KastepGreen, fontSize = 13.sp, modifier = Modifier.padding(horizontal = 20.dp)
                    .fillMaxWidth().background(KastepGreen.copy(alpha = 0.1f), RoundedCornerShape(8.dp)).padding(12.dp))
                Spacer(modifier = Modifier.height(8.dp))
            }

            // Tanggal
            Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                Text("Tanggal", color = KastepWhite, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(12.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedTextField(
                        value = tanggalHari, onValueChange = { if (it.length <= 2) tanggalHari = it },
                        modifier = Modifier.weight(0.8f).height(56.dp), placeholder = { Text("DD") },
                        colors = textFieldColors, shape = RoundedCornerShape(12.dp), singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                    OutlinedTextField(
                        value = tanggalBulan, onValueChange = { if (it.length <= 2) tanggalBulan = it },
                        modifier = Modifier.weight(1.2f).height(56.dp), placeholder = { Text("MM") },
                        colors = textFieldColors, shape = RoundedCornerShape(12.dp), singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                    OutlinedTextField(
                        value = tanggalTahun, onValueChange = { if (it.length <= 4) tanggalTahun = it },
                        modifier = Modifier.weight(1.2f).height(56.dp), placeholder = { Text("YYYY") },
                        colors = textFieldColors, shape = RoundedCornerShape(12.dp), singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Jumlah
            Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                Text("Jumlah", color = KastepWhite, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(
                    value = jumlah, onValueChange = { jumlah = it; errorMsg = null; successMsg = null },
                    modifier = Modifier.fillMaxWidth().height(56.dp), placeholder = { Text("Rp 0") },
                    colors = textFieldColors, shape = RoundedCornerShape(28.dp), singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Keterangan
            Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                Text("Keterangan", color = KastepWhite, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(
                    value = keterangan, onValueChange = { keterangan = it; errorMsg = null; successMsg = null },
                    modifier = Modifier.fillMaxWidth().height(120.dp), placeholder = { Text("Keterangan pengeluaran") },
                    colors = textFieldColors, shape = RoundedCornerShape(20.dp), maxLines = 5
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Submit button
            Button(
                onClick = {
                    val amount = jumlah.toLongOrNull() ?: 0L
                    val tanggal = if (tanggalHari.isNotBlank() && tanggalBulan.isNotBlank() && tanggalTahun.isNotBlank())
                        "$tanggalHari/$tanggalBulan/$tanggalTahun" else currentDate
                    val result = viewModel.addPengeluaran(tanggal, amount, keterangan)
                    if (result != null) {
                        errorMsg = result; successMsg = null
                    } else {
                        successMsg = "Pengeluaran berhasil ditambahkan!"
                        errorMsg = null
                        tanggalHari = ""; tanggalBulan = ""; tanggalTahun = ""
                        jumlah = ""; keterangan = ""
                    }
                },
                modifier = Modifier.fillMaxWidth().height(56.dp).padding(horizontal = 32.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                shape = RoundedCornerShape(16.dp)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize()
                        .background(Brush.horizontalGradient(listOf(GradientBlueStart, GradientBlueEnd)), RoundedCornerShape(16.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Add, contentDescription = null, tint = KastepWhite, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Tambah Pengeluaran", color = KastepWhite, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }

        // Riwayat Pengeluaran List
        Text("Riwayat Pengeluaran", color = KastepWhite, fontSize = 18.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 20.dp))
        Spacer(modifier = Modifier.height(12.dp))

        if (pengeluaranList.isEmpty()) {
            Text("Belum ada pengeluaran tercatat.", color = KastepGray, fontSize = 14.sp, modifier = Modifier.padding(horizontal = 20.dp))
        } else {
            pengeluaranList.forEach { p ->
                Card(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 4.dp)
                        .then(if (isAdmin) Modifier.clickable { selectedPengeluaran = p; showEditDialog = true } else Modifier),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A2E))
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(p.keterangan, color = KastepWhite, fontSize = 14.sp, fontWeight = FontWeight.Medium)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(p.tanggal, color = KastepGray, fontSize = 12.sp)
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text("Rp ${KastepViewModel.formatRupiah(p.jumlah)}", color = KastepRed, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                            if (isAdmin) {
                                Spacer(modifier = Modifier.height(4.dp))
                                Row {
                                    Icon(Icons.Default.Edit, contentDescription = "Edit", tint = KastepCyan, modifier = Modifier.size(18.dp).clickable { selectedPengeluaran = p; showEditDialog = true })
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Icon(Icons.Default.Delete, contentDescription = "Hapus", tint = KastepRed, modifier = Modifier.size(18.dp).clickable { viewModel.deletePengeluaran(p.id) })
                                }
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))
    }

    // Edit Pengeluaran Dialog
    if (showEditDialog && selectedPengeluaran != null) {
        EditPengeluaranDialog(
            pengeluaran = selectedPengeluaran!!,
            onDismiss = { showEditDialog = false; selectedPengeluaran = null },
            onSave = { tanggal, amt, ket ->
                viewModel.updatePengeluaran(selectedPengeluaran!!.id, tanggal, amt, ket)
                showEditDialog = false; selectedPengeluaran = null
            },
            onDelete = {
                viewModel.deletePengeluaran(selectedPengeluaran!!.id)
                showEditDialog = false; selectedPengeluaran = null
            }
        )
    }
}

@Composable
private fun EditPengeluaranDialog(
    pengeluaran: Pengeluaran,
    onDismiss: () -> Unit,
    onSave: (String, Long, String) -> Unit,
    onDelete: () -> Unit
) {
    var tanggal by remember { mutableStateOf(pengeluaran.tanggal) }
    var jumlah by remember { mutableStateOf(pengeluaran.jumlah.toString()) }
    var keterangan by remember { mutableStateOf(pengeluaran.keterangan) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Edit Pengeluaran", fontWeight = FontWeight.Bold) },
        text = {
            Column {
                OutlinedTextField(value = tanggal, onValueChange = { tanggal = it }, label = { Text("Tanggal") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(value = jumlah, onValueChange = { jumlah = it }, label = { Text("Jumlah (Rp)") }, modifier = Modifier.fillMaxWidth(), singleLine = true, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(value = keterangan, onValueChange = { keterangan = it }, label = { Text("Keterangan") }, modifier = Modifier.fillMaxWidth(), maxLines = 3)
                Spacer(modifier = Modifier.height(12.dp))
                TextButton(onClick = onDelete, modifier = Modifier.fillMaxWidth()) {
                    Icon(Icons.Default.Delete, contentDescription = null, tint = KastepRed, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Hapus Pengeluaran", color = KastepRed)
                }
            }
        },
        confirmButton = {
            TextButton(onClick = {
                val amt = jumlah.toLongOrNull() ?: 0L
                if (amt > 0 && keterangan.isNotBlank()) onSave(tanggal, amt, keterangan)
            }) { Text("Simpan") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Batal") } }
    )
}
