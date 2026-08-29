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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PembayaranScreen(viewModel: KastepViewModel, onOpenDrawer: () -> Unit) {
    val userProfile by viewModel.userProfile.collectAsState()
    val students by viewModel.students.collectAsState()
    val currentDate = viewModel.getCurrentDateString()
    val context = LocalContext.current

    var selectedMethod by remember { mutableStateOf<String?>(null) }
    var showPaymentDialog by remember { mutableStateOf(false) }
    var showQrisDialog by remember { mutableStateOf(false) }
    var showSuccessDialog by remember { mutableStateOf(false) }
    var lastPaidStudent by remember { mutableStateOf("") }
    var lastPaidMonth by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize().background(KastepBlack).verticalScroll(rememberScrollState())
    ) {
        TopBar(currentDate = currentDate, userName = userProfile.nama, onMenuClick = onOpenDrawer)
        Spacer(modifier = Modifier.height(12.dp))

        Text("Pembayaran", color = KastepWhite, fontSize = 22.sp, fontWeight = FontWeight.Medium, modifier = Modifier.padding(horizontal = 20.dp))
        Spacer(modifier = Modifier.height(4.dp))
        Text("Iuran Kas: Rp 20.000 / bulan", color = KastepCyan, fontSize = 14.sp, modifier = Modifier.padding(horizontal = 20.dp))

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
                    Text("Nominal: Rp 20.000", color = KastepGreen, fontSize = 14.sp, fontWeight = FontWeight.Medium)
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
            students = students,
            method = selectedMethod ?: "Cash",
            onDismiss = { showPaymentDialog = false },
            onConfirm = { studentName, bulan ->
                val result = viewModel.processPayment(studentName, bulan, selectedMethod ?: "Cash", 20000)
                if (result == null) {
                    lastPaidStudent = studentName
                    lastPaidMonth = bulan
                    showPaymentDialog = false
                    showSuccessDialog = true
                    // WhatsApp auto confirmation
                    sendWhatsAppConfirmation(context, studentName, bulan, selectedMethod ?: "Cash")
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

    // Success Dialog
    if (showSuccessDialog) {
        AlertDialog(
            onDismissRequest = { showSuccessDialog = false },
            icon = { Icon(Icons.Default.CheckCircle, contentDescription = null, tint = KastepGreen, modifier = Modifier.size(48.dp)) },
            title = { Text("Pembayaran Berhasil!", fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    Text("Siswa: $lastPaidStudent")
                    Text("Bulan: $lastPaidMonth")
                    Text("Metode: ${selectedMethod ?: "Cash"}")
                    Text("Nominal: Rp 20.000")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Konfirmasi telah dikirim ke WhatsApp.", color = KastepGreen, fontSize = 12.sp)
                }
            },
            confirmButton = {
                TextButton(onClick = { showSuccessDialog = false }) { Text("OK") }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PaymentConfirmDialog(
    students: List<com.kastep.app.data.Siswa>,
    method: String,
    onDismiss: () -> Unit,
    onConfirm: (String, String) -> Unit
) {
    var selectedStudent by remember { mutableStateOf("") }
    var selectedBulan by remember { mutableStateOf("") }
    var expandedStudent by remember { mutableStateOf(false) }
    var expandedBulan by remember { mutableStateOf(false) }
    val bulanOptions = listOf("Juli 2026", "Agustus 2026")

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
                            DropdownMenuItem(text = { Text(s.nama, fontSize = 13.sp) }, onClick = {
                                selectedStudent = s.nama; expandedStudent = false
                            })
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
                            DropdownMenuItem(text = { Text(b) }, onClick = {
                                selectedBulan = b; expandedBulan = false
                            })
                        }
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
                Text("Nominal: Rp 20.000", fontWeight = FontWeight.Bold, color = KastepGreen)
            }
        },
        confirmButton = {
            TextButton(
                onClick = { if (selectedStudent.isNotBlank() && selectedBulan.isNotBlank()) onConfirm(selectedStudent, selectedBulan) },
                enabled = selectedStudent.isNotBlank() && selectedBulan.isNotBlank()
            ) { Text("Bayar Sekarang") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Batal") } }
    )
}

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
                Text("Nominal: Rp 20.000", color = KastepGreen, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                Text("Scan kode di atas, lalu klik Lanjutkan", fontSize = 12.sp, color = Color.Gray, textAlign = TextAlign.Center)
            }
        },
        confirmButton = { TextButton(onClick = onProceed) { Text("Lanjutkan") } },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Batal") } }
    )
}

private fun sendWhatsAppConfirmation(context: Context, studentName: String, bulan: String, method: String) {
    val phone = "6289520371942"
    val message = "✅ *KONFIRMASI PEMBAYARAN KAS*\n\n" +
            "Nama: $studentName\n" +
            "Bulan: $bulan\n" +
            "Metode: $method\n" +
            "Nominal: Rp 20.000\n\n" +
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
