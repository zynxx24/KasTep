// ==========================================================================
// PETA FILE · KASTEP (Kelompok 1) · PembayaranScreen.kt
// Isi file : Halaman Pembayaran: pilih metode, pilih siswa & bulan, hitung total dengan denda.
// Penanda  : CARI-PEMBAYARAN, CARI-QRIS, CARI-DENDA, CARI-WHATSAPP
// Cara cari: VS Code Ctrl+Shift+F (Android Studio Ctrl+Shift+F) lalu ketik
//            "CARI-" untuk semua penanda, atau salah satu nama di baris Penanda.
// ==========================================================================
package com.kastep.app.ui.screens

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kastep.app.R
import com.kastep.app.data.KastepViewModel
import com.kastep.app.data.StatusBayar
import com.kastep.app.ui.theme.*
import java.net.URLEncoder

// CARI-PEMBAYARAN | Pilihan metode: QRIS, DANA, GoPay, OVO, BCA, Mandiri, BNI, Cash.
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PembayaranScreen(viewModel: KastepViewModel, onOpenDrawer: () -> Unit, onNavigateToProfile: () -> Unit = {}, onPaymentSuccess: () -> Unit = {}) {
    val userProfile by viewModel.userProfile.collectAsState()
    val students by viewModel.students.collectAsState()
    val currentDate = viewModel.getCurrentDateString()
    val context = LocalContext.current

    var selectedMethod by remember { mutableStateOf<String?>(null) }
    var showPaymentDialog by remember { mutableStateOf(false) }
    var showQrisDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize().background(KastepBlack).verticalScroll(rememberScrollState())
    ) {
        TopBar(currentDate = currentDate, userName = userProfile.nama, onMenuClick = onOpenDrawer, onNavigateToProfile = onNavigateToProfile)
        Spacer(modifier = Modifier.height(12.dp))

        Text("Pembayaran", color = KastepWhite, fontSize = 22.sp, fontWeight = FontWeight.Medium, modifier = Modifier.padding(horizontal = 20.dp))
        Spacer(modifier = Modifier.height(4.dp))
        Text("Iuran Kas: Rp 5.000 / minggu, dibayar per bulan", color = KastepCyan, fontSize = 14.sp, modifier = Modifier.padding(horizontal = 20.dp))

        Spacer(modifier = Modifier.height(16.dp))
        HorizontalDivider(color = KastepGray.copy(alpha = 0.3f), thickness = 0.5.dp)

        // QRIS
        PaymentMethodSection {
            Card(
                modifier = Modifier.size(width = 100.dp, height = 60.dp)
                    .clickable { selectedMethod = "QRIS"; showQrisDialog = true },
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = if (selectedMethod == "QRIS") KastepCyan.copy(alpha = 0.3f) else Color.White)
            ) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Image(painter = painterResource(R.drawable.ic_qris), contentDescription = "QRIS", modifier = Modifier.size(80.dp).padding(8.dp), contentScale = ContentScale.Fit)
                }
            }
        }
        HorizontalDivider(color = KastepGray.copy(alpha = 0.3f), thickness = 0.5.dp)

        // E-Wallets
        PaymentMethodSection {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                PaymentIconCardSelectable(R.drawable.ic_dana, "DANA", selectedMethod == "DANA") { selectedMethod = "DANA" }
                PaymentIconCardSelectable(R.drawable.ic_gopay, "GoPay", selectedMethod == "GoPay") { selectedMethod = "GoPay" }
                PaymentIconCardSelectable(R.drawable.ic_ovo, "OVO", selectedMethod == "OVO") { selectedMethod = "OVO" }
            }
        }
        HorizontalDivider(color = KastepGray.copy(alpha = 0.3f), thickness = 0.5.dp)

        // Banks
        PaymentMethodSection {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                PaymentIconCardSelectable(R.drawable.ic_bca, "BCA", selectedMethod == "BCA") { selectedMethod = "BCA" }
                PaymentIconCardSelectable(R.drawable.ic_mandiri, "Mandiri", selectedMethod == "Mandiri") { selectedMethod = "Mandiri" }
                PaymentIconCardSelectable(R.drawable.ic_bni, "BNI", selectedMethod == "BNI") { selectedMethod = "BNI" }
            }
        }
        HorizontalDivider(color = KastepGray.copy(alpha = 0.3f), thickness = 0.5.dp)

        // CASH
        PaymentMethodSection {
            Card(
                modifier = Modifier.fillMaxWidth().height(60.dp)
                    .clickable { selectedMethod = "Cash" },
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = if (selectedMethod == "Cash") KastepCyan.copy(alpha = 0.15f) else Color.Transparent)
            ) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("CASH", color = KastepWhite, fontSize = 32.sp, fontWeight = FontWeight.ExtraBold)
                }
            }
        }

        // Simulated account numbers
        if (selectedMethod != null && selectedMethod != "Cash" && selectedMethod != "QRIS") {
            Card(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A2E))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Transfer via $selectedMethod", color = KastepCyan, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    // CARI-QRIS | Nomor rekening / e-wallet tujuan transfer; ubah di sini bila rekening kelas berganti.
                    val accNo = when (selectedMethod) {
                        "DANA" -> "0895-2037-1942"
                        "GoPay" -> "0895-2037-1942"
                        "OVO" -> "0895-2037-1942"
                        "BCA" -> "7340-5812-9076"
                        "Mandiri" -> "1280-0045-6789-012"
                        "BNI" -> "0912-3456-7890"
                        else -> "-"
                    }
                    Text("No. Rekening: $accNo", color = KastepWhite, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    Text("a.n. KAS XII PPLG", color = KastepGray, fontSize = 13.sp)
                    Text("Nominal: sesuai tagihan di langkah berikutnya", color = KastepGreen, fontSize = 14.sp, fontWeight = FontWeight.Medium)
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Big green PEMBAYARAN button
        Button(
            onClick = {
                if (selectedMethod != null) showPaymentDialog = true
            },
            modifier = Modifier.fillMaxWidth().height(70.dp).padding(horizontal = 40.dp),
            colors = ButtonDefaults.buttonColors(containerColor = if (selectedMethod != null) Color(0xFF4CAF50) else KastepGray),
            shape = RoundedCornerShape(16.dp),
            enabled = selectedMethod != null
        ) {
            Text("PEMBAYARAN", color = KastepWhite, fontSize = 24.sp, fontWeight = FontWeight.ExtraBold)
        }

        Spacer(modifier = Modifier.height(32.dp))
    }

    // Payment Dialog: select student + month
    if (showPaymentDialog) {
        PaymentConfirmDialog(
            viewModel = viewModel,
            students = students,
            method = selectedMethod ?: "Cash",
            onDismiss = { showPaymentDialog = false },
            onConfirm = { studentName, bulan, _, _ ->
                val result = viewModel.processPayment(studentName, bulan, selectedMethod ?: "Cash")
                if (result == null) {
                    showPaymentDialog = false
                    // WhatsApp confirmation is sent from the success screen, so it is always seen first.
                    onPaymentSuccess()
                } else {
                    android.widget.Toast.makeText(context, result, android.widget.Toast.LENGTH_SHORT).show()
                }
            }
        )
    }

    // QRIS Dialog
    if (showQrisDialog) {
        QrisSimulationDialog(
            onDismiss = { showQrisDialog = false },
            onProceed = {
                showQrisDialog = false
                showPaymentDialog = true
            }
        )
    }

}

// CARI-PEMBAYARAN CARI-DENDA | Total bayar = sisa minggu x Rp5.000 + denda. Bulan yang sudah lunas tidak bisa dipilih.
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PaymentConfirmDialog(
    viewModel: KastepViewModel,
    students: List<com.kastep.app.data.Siswa>,
    method: String,
    onDismiss: () -> Unit,
    onConfirm: (String, String, Long, Long) -> Unit
) {
    var selectedStudent by remember { mutableStateOf("") }
    var selectedBulan by remember { mutableStateOf("") }
    var expandedStudent by remember { mutableStateOf(false) }
    var expandedBulan by remember { mutableStateOf(false) }
    val bulanOptions = viewModel.getBulanKas()

    fun isLunas(siswa: com.kastep.app.data.Siswa?, bulan: String): Boolean =
        siswa != null && viewModel.statusBulan(siswa, bulan) == com.kastep.app.data.StatusBayar.LUNAS
    val selectedSiswa = students.find { it.nama == selectedStudent }
    val sudahLunas = selectedBulan.isNotBlank() && isLunas(selectedSiswa, selectedBulan)
    val canPay = selectedStudent.isNotBlank() && selectedBulan.isNotBlank() && !sudahLunas

    val denda = if (selectedStudent.isNotBlank() && selectedBulan.isNotBlank()) {
        viewModel.calculateDenda(selectedStudent, selectedBulan)
    } else 0L
    // Tagihan = sisa minggu yang belum dibayar pada bulan itu x Rp5.000
    val kasBulanan = if (selectedSiswa != null && selectedBulan.isNotBlank()) viewModel.sisaKas(selectedSiswa, selectedBulan) else 0L
    val mingguBelum = if (kasBulanan > 0) (kasBulanan / 5000).toInt() else 0
    val totalBayar = kasBulanan + denda

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Konfirmasi Pembayaran ($method)", fontWeight = FontWeight.Bold, fontSize = 16.sp) },
        text = {
            Column {
                Text("Pilih Siswa:", fontWeight = FontWeight.Medium)
                Spacer(modifier = Modifier.height(4.dp))
                ExposedDropdownMenuBox(expanded = expandedStudent, onExpandedChange = { expandedStudent = it }) {
                    OutlinedTextField(
                        value = selectedStudent, onValueChange = {}, readOnly = true,
                        label = { Text("Nama Siswa") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expandedStudent) },
                        modifier = Modifier.menuAnchor().fillMaxWidth()
                    )
                    ExposedDropdownMenu(expanded = expandedStudent, onDismissRequest = { expandedStudent = false }) {
                        students.forEach { s ->
                            val belum = bulanOptions.filterNot { isLunas(s, it) }
                            val lunasSemua = belum.isEmpty()
                            DropdownMenuItem(
                                text = {
                                    Column {
                                        Text(s.nama, fontSize = 13.sp, color = if (lunasSemua) Color.Gray else Color.Unspecified)
                                        Text(
                                            if (lunasSemua) "Lunas semua"
                                            else "Belum: " + belum.joinToString { it.substringBefore(" ") },
                                            fontSize = 11.sp,
                                            color = if (lunasSemua) KastepGreen else Color(0xFFFF6B6B)
                                        )
                                    }
                                },
                                enabled = !lunasSemua,
                                onClick = {
                                    selectedStudent = s.nama; expandedStudent = false
                                    // Pre-select the first unpaid month so the user can pay right away.
                                    if (selectedBulan.isBlank() || isLunas(s, selectedBulan)) {
                                        selectedBulan = belum.firstOrNull() ?: ""
                                    }
                                }
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
                Text("Pilih Bulan:", fontWeight = FontWeight.Medium)
                Spacer(modifier = Modifier.height(4.dp))
                ExposedDropdownMenuBox(expanded = expandedBulan, onExpandedChange = { expandedBulan = it }) {
                    OutlinedTextField(
                        value = selectedBulan, onValueChange = {}, readOnly = true,
                        label = { Text("Bulan") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expandedBulan) },
                        modifier = Modifier.menuAnchor().fillMaxWidth()
                    )
                    ExposedDropdownMenu(expanded = expandedBulan, onDismissRequest = { expandedBulan = false }) {
                        bulanOptions.forEach { b ->
                            val lunas = isLunas(selectedSiswa, b)
                            DropdownMenuItem(
                                text = { Text(if (lunas) "$b (Lunas)" else b) },
                                enabled = !lunas,
                                onClick = { selectedBulan = b; expandedBulan = false }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                if (sudahLunas) {
                    Text(
                        "$selectedStudent sudah lunas untuk $selectedBulan.",
                        color = Color(0xFFFF6B6B), fontSize = 12.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }

                // Payment breakdown
                Text(
                    if (mingguBelum > 0) "Kas ($mingguBelum minggu x Rp 5.000): Rp ${KastepViewModel.formatRupiah(kasBulanan)}"
                    else "Kas: Rp ${KastepViewModel.formatRupiah(kasBulanan)}",
                    fontWeight = FontWeight.Medium
                )
                if (denda > 0) {
                    val monthsLate = denda / 5000
                    Text(
                        "Denda ($monthsLate bulan telat): Rp ${KastepViewModel.formatRupiah(denda)}",
                        color = Color(0xFFFF6B6B), fontWeight = FontWeight.Medium
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text("Total Bayar: Rp ${KastepViewModel.formatRupiah(totalBayar)}", fontWeight = FontWeight.Bold, color = KastepGreen, fontSize = 16.sp)
            }
        },
        confirmButton = {
            TextButton(
                onClick = { if (canPay) onConfirm(selectedStudent, selectedBulan, totalBayar, denda) },
                enabled = canPay
            ) { Text("Bayar Sekarang") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Batal") } }
    )
}

// CARI-QRIS | Gambar QRIS dari res/drawable/ic_qris.
@Composable
private fun QrisSimulationDialog(onDismiss: () -> Unit, onProceed: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("QRIS Pembayaran", fontWeight = FontWeight.Bold) },
        text = {
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                // Simulated QRIS code
                Card(
                    modifier = Modifier.size(200.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Box(modifier = Modifier.fillMaxSize().padding(16.dp), contentAlignment = Alignment.Center) {
                        Image(
                            painter = painterResource(R.drawable.ic_qris),
                            contentDescription = "QRIS",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Fit
                        )
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
                Text("NMID: ID1026552494969", fontSize = 12.sp, color = Color.Gray)
                Text("Merchant: KAS XII PPLG", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Text("Nominal: sesuai tagihan siswa", color = KastepGreen, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                Text("Scan kode di atas, lalu klik Lanjutkan", fontSize = 12.sp, color = Color.Gray, textAlign = TextAlign.Center)
            }
        },
        confirmButton = { TextButton(onClick = onProceed) { Text("Lanjutkan") } },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Batal") } }
    )
}

// CARI-WHATSAPP | Membuka WhatsApp dengan pesan konfirmasi. Nomor tujuan ada di variabel phone.
internal fun sendWhatsAppConfirmation(context: Context, studentName: String, bulan: String, method: String, totalBayar: Long, denda: Long) {
    val phone = "6289520371942"
    val dendaLine = if (denda > 0) "Denda Telat: Rp ${KastepViewModel.formatRupiah(denda)}\n" else ""
    val message = "✅ *KONFIRMASI PEMBAYARAN KAS*\n\n" +
            "Nama: $studentName\n" +
            "Bulan: $bulan\n" +
            "Metode: $method\n" +
            "Kas: Rp ${KastepViewModel.formatRupiah(totalBayar - denda)}\n" +
            dendaLine +
            "Total Bayar: Rp ${KastepViewModel.formatRupiah(totalBayar)}\n\n" +
            "Pembayaran telah berhasil dicatat di aplikasi KASTEP."
    val encodedMsg = URLEncoder.encode(message, "UTF-8")
    try {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://wa.me/$phone?text=$encodedMsg"))
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    } catch (_: Exception) { }
}

@Composable
private fun PaymentMethodSection(content: @Composable () -> Unit) {
    Box(modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 16.dp)) { content() }
}

@Composable
private fun PaymentIconCardSelectable(iconRes: Int, label: String, selected: Boolean, onClick: () -> Unit) {
    Card(
        modifier = Modifier.size(width = 100.dp, height = 70.dp).clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = if (selected) KastepCyan.copy(alpha = 0.3f) else Color.White)
    ) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Image(painter = painterResource(iconRes), contentDescription = label, modifier = Modifier.size(60.dp).padding(4.dp), contentScale = ContentScale.Fit)
        }
    }
}
