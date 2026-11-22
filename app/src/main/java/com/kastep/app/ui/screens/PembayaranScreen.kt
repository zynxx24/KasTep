// ==========================================================================
// PETA FILE · KASTEP (Kelompok 1) · PembayaranScreen.kt
// Isi file : Halaman Pembayaran: pilih metode, pilih siswa & bulan, hitung total dengan denda.
// Penanda  : CARI-PEMBAYARAN, CARI-QRIS, CARI-DENDA, CARI-WHATSAPP
// Cara cari: VS Code Ctrl+Shift+F (Android Studio Ctrl+Shift+F) lalu ketik
//            "CARI-" untuk semua penanda, atau salah satu nama di baris Penanda.
// ==========================================================================
//  BACA INI DULU : LAYAR "PEMBAYARAN" (bayar kas)
//  Layar ini punya DUA mode menurut peran akun yang login:
//  USER (siswa), alurnya:
//    1. Pilih METODE: QRIS, e-wallet (DANA, GoPay, OVO), bank (BCA, Mandiri, BNI), atau CASH. Metode dipilih = kartu berwarna.
//    2. Untuk e-wallet dan bank, muncul kotak "Transfer via ..." berisi nomor tujuan (accNo). Nomor ini masih NOMOR CONTOH, ganti dengan rekening kelas asli.
//    3. Tekan tombol hijau PEMBAYARAN -> dialog (PaymentConfirmDialog). Nama terkunci ke nama akun sendiri, pilih DARI BULAN dan SAMPAI BULAN.
//       Dialog menghitung otomatis: sisa kas + denda tiap bulan yang belum lunas = Total Bayar.
//    4. Tekan "Kirim Konfirmasi ke Bendahara" -> WhatsApp bendahara terbuka berisi rincian. STATUS TIDAK BERUBAH di aplikasi.
//  ADMIN (bendahara), alurnya:
//    1. Pilih metode yang dipakai siswa, tekan PEMBAYARAN, pilih SISWA, lalu DARI BULAN dan SAMPAI BULAN yang dibayar.
//    2. Tekan "Catat Pembayaran" -> viewModel.processPaymentRange mencatat tiap bulan -> bulan itu LUNAS -> layar Pembayaran Berhasil.
//  Jadi yang membuat status LUNAS hanya bendahara, setelah uangnya dicek. Siswa tidak bisa menandai dirinya sendiri lunas.
//  Untuk QRIS: dialog gambar QRIS (res/drawable/ic_qris) tampil dulu, "Lanjut" membuka dialog yang sama.
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
    // isAdmin = bendahara (bisa mencatat pembayaran). Selain itu USER (siswa) yang hanya bisa mengirim konfirmasi WA.
    val isAdmin = userProfile.role == com.kastep.app.data.UserRole.ADMIN

    // selectedMethod = metode yang dipilih (null = belum ada). showPaymentDialog / showQrisDialog = dialog mana yang sedang tampil.
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
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            if (isAdmin) "Mode bendahara: catat pembayaran siswa setelah uangnya dicek."
            else "Setelah membayar, kirim konfirmasi ke bendahara. Status menjadi LUNAS setelah bendahara mencatatnya.",
            color = KastepGray, fontSize = 12.sp, modifier = Modifier.padding(horizontal = 20.dp)
        )

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

        // Kotak nomor tujuan transfer. accNo di bawah berisi nomor CONTOH (belum rekening asli kelas). Untuk QRIS dan Cash kotak ini tidak tampil.
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

        // Tombol besar PEMBAYARAN. Mati (abu-abu) sampai ada metode dipilih (enabled = selectedMethod != null).
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

    // Dialog pembayaran.
    //   ADMIN: onConfirm memanggil viewModel.processPaymentRange (mencatat bulan dari sampai bulan). null = sukses -> layar Berhasil.
    //   USER : onConfirm hanya membuka WhatsApp bendahara. Tidak ada data yang diubah, status tetap sampai bendahara mencatat.
    // Payment Dialog: select student + month range
    if (showPaymentDialog) {
        PaymentConfirmDialog(
            viewModel = viewModel,
            students = students,
            method = selectedMethod ?: "Cash",
            isAdmin = isAdmin,
            namaAkun = userProfile.nama.trim().uppercase(),
            onDismiss = { showPaymentDialog = false },
            onConfirm = { studentName, bulanList, totalBayar, denda ->
                val metode = selectedMethod ?: "Cash"
                if (isAdmin) {
                    val result = viewModel.processPaymentRange(studentName, bulanList, metode)
                    if (result == null) {
                        showPaymentDialog = false
                        onPaymentSuccess()
                    } else {
                        android.widget.Toast.makeText(context, result, android.widget.Toast.LENGTH_SHORT).show()
                    }
                } else {
                    showPaymentDialog = false
                    sendWhatsAppConfirmation(context, studentName, bulanList.joinToString(", "), metode, totalBayar, denda, sudahDicatat = false)
                    android.widget.Toast.makeText(
                        context, "Konfirmasi dikirim. Status menjadi LUNAS setelah bendahara mencatat.", android.widget.Toast.LENGTH_LONG
                    ).show()
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

// DIALOG PEMBAYARAN (dipakai USER dan ADMIN). Hitungan di dalamnya, untuk tiap bulan yang BELUM LUNAS di rentang yang dipilih:
//   denda      = viewModel.calculateDenda(siswa, bulan)         (Rp5.000 x bulan terlambat bila belum lunas)
//   kasBulanan = viewModel.sisaKas(siswa, bulan)                (minggu belum bayar x Rp5.000)
//   totalBayar = jumlah (kasBulanan + denda) semua bulan itu
// ADMIN memilih siswa dari daftar (ada keterangan "Belum: Juli, Agustus"). USER tidak memilih siswa, nama terkunci ke akunnya sendiri.
// Rentang bulan: "Dari bulan" dan "Sampai bulan". Bulan yang sudah LUNAS di dalam rentang dilewati, tidak dibayar dua kali.
// canPay = siswa ada, rentang benar (sampai tidak sebelum dari), dan ada minimal satu bulan yang belum lunas.
// CARI-PEMBAYARAN CARI-DENDA | Total bayar = jumlah (sisa minggu x Rp5.000 + denda) bulan yang belum lunas di rentang.
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PaymentConfirmDialog(
    viewModel: KastepViewModel,
    students: List<com.kastep.app.data.Siswa>,
    method: String,
    isAdmin: Boolean,
    namaAkun: String,
    onDismiss: () -> Unit,
    onConfirm: (String, List<String>, Long, Long) -> Unit
) {
    val bulanOptions = viewModel.getBulanKas()
    // Cek apakah bulan itu sudah LUNAS supaya tidak bisa dibayar dua kali.
    fun isLunas(siswa: com.kastep.app.data.Siswa?, bulan: String): Boolean =
        siswa != null && viewModel.statusBulan(siswa, bulan) == com.kastep.app.data.StatusBayar.LUNAS

    // USER: siswa = baris Data Siswa yang namanya sama dengan nama akun. ADMIN: siswa dipilih dari daftar.
    val siswaAkun = students.find { it.nama == namaAkun }
    var selectedStudent by remember { mutableStateOf(if (isAdmin) "" else siswaAkun?.nama.orEmpty()) }
    val selectedSiswa = students.find { it.nama == selectedStudent }
    val awalBelum = selectedSiswa?.let { s -> bulanOptions.firstOrNull { !isLunas(s, it) } }.orEmpty()
    var dariBulan by remember { mutableStateOf(awalBelum) }
    var sampaiBulan by remember { mutableStateOf(awalBelum) }
    var expandedStudent by remember { mutableStateOf(false) }
    var expandedDari by remember { mutableStateOf(false) }
    var expandedSampai by remember { mutableStateOf(false) }

    // Rentang bulan yang dipilih, lalu hanya bulan yang belum lunas yang dihitung.
    val iDari = bulanOptions.indexOf(dariBulan)
    val iSampai = bulanOptions.indexOf(sampaiBulan)
    val rentangSalah = iDari >= 0 && iSampai >= 0 && iSampai < iDari
    val rentang = if (iDari >= 0 && iSampai >= iDari) bulanOptions.subList(iDari, iSampai + 1) else emptyList()
    val bulanBelum = rentang.filterNot { isLunas(selectedSiswa, it) }
    val rincian = if (selectedSiswa != null) bulanBelum.map { b ->
        Triple(b, viewModel.sisaKas(selectedSiswa, b), viewModel.calculateDenda(selectedSiswa.nama, b))
    } else emptyList()
    val totalKas = rincian.sumOf { it.second }
    val totalDenda = rincian.sumOf { it.third }
    val totalBayar = totalKas + totalDenda
    val canPay = selectedSiswa != null && !rentangSalah && bulanBelum.isNotEmpty()

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                if (isAdmin) "Catat Pembayaran ($method)" else "Konfirmasi Pembayaran ($method)",
                fontWeight = FontWeight.Bold, fontSize = 16.sp
            )
        },
        text = {
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                if (isAdmin) {
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
                                        // Bulan awal dan akhir langsung diisi bulan pertama yang belum lunas.
                                        val pertama = belum.firstOrNull().orEmpty()
                                        dariBulan = pertama; sampaiBulan = pertama
                                    }
                                )
                            }
                        }
                    }
                } else {
                    Text("Nama: ${if (siswaAkun != null) siswaAkun.nama else namaAkun}", fontWeight = FontWeight.Medium)
                    if (siswaAkun == null) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            "Nama akunmu belum ada di Data Siswa. Samakan nama di Profil atau hubungi bendahara.",
                            color = Color(0xFFFF6B6B), fontSize = 12.sp
                        )
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))

                Text("Bulan yang dibayar:", fontWeight = FontWeight.Medium)
                Spacer(modifier = Modifier.height(4.dp))
                val pilihan = bulanOptions.map { it to isLunas(selectedSiswa, it) }
                BulanPicker("Dari bulan", dariBulan, expandedDari, { expandedDari = it }, pilihan) { b ->
                    dariBulan = b
                    if (bulanOptions.indexOf(sampaiBulan) < bulanOptions.indexOf(b)) sampaiBulan = b
                }
                Spacer(modifier = Modifier.height(8.dp))
                BulanPicker("Sampai bulan", sampaiBulan, expandedSampai, { expandedSampai = it }, pilihan) { b -> sampaiBulan = b }

                Spacer(modifier = Modifier.height(12.dp))
                when {
                    selectedSiswa != null && bulanOptions.all { isLunas(selectedSiswa, it) } ->
                        Text("${selectedSiswa.nama} sudah lunas semua bulan.", color = KastepGreen, fontSize = 12.sp)
                    rentangSalah -> Text("Bulan akhir tidak boleh sebelum bulan awal.", color = Color(0xFFFF6B6B), fontSize = 12.sp)
                    selectedSiswa != null && rentang.isNotEmpty() && bulanBelum.isEmpty() ->
                        Text("$selectedStudent sudah lunas untuk bulan yang dipilih.", color = Color(0xFFFF6B6B), fontSize = 12.sp)
                }

                // Rincian per bulan: kas + denda.
                rincian.forEach { (b, kas, dnd) ->
                    Text(
                        "$b: Rp ${KastepViewModel.formatRupiah(kas)}" + if (dnd > 0) " + denda Rp ${KastepViewModel.formatRupiah(dnd)}" else "",
                        fontSize = 13.sp,
                        color = if (dnd > 0) Color(0xFFFF6B6B) else Color.Unspecified
                    )
                }
                if (rincian.isNotEmpty()) Spacer(modifier = Modifier.height(4.dp))
                Text("Kas: Rp ${KastepViewModel.formatRupiah(totalKas)}", fontWeight = FontWeight.Medium)
                if (totalDenda > 0) {
                    Text("Denda: Rp ${KastepViewModel.formatRupiah(totalDenda)}", color = Color(0xFFFF6B6B), fontWeight = FontWeight.Medium)
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text("Total Bayar: Rp ${KastepViewModel.formatRupiah(totalBayar)}", fontWeight = FontWeight.Bold, color = KastepGreen, fontSize = 16.sp)
                if (!isAdmin) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "Status belum berubah sebelum bendahara mengecek dan mencatat pembayaranmu.",
                        fontSize = 12.sp, color = Color.Gray
                    )
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = { if (canPay) onConfirm(selectedStudent, bulanBelum, totalBayar, totalDenda) },
                enabled = canPay
            ) { Text(if (isAdmin) "Catat Pembayaran" else "Kirim Konfirmasi ke Bendahara") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Batal") } }
    )
}

// Kotak pilihan satu bulan (dipakai untuk "Dari bulan" dan "Sampai bulan").
// pilihan = daftar (bulan, sudahLunas). Bulan yang lunas tetap tampil berlabel (Lunas) supaya rentang bisa melewatinya.
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BulanPicker(
    label: String, value: String, expanded: Boolean, onExpanded: (Boolean) -> Unit,
    pilihan: List<Pair<String, Boolean>>, onPick: (String) -> Unit
) {
    ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = onExpanded) {
        OutlinedTextField(
            value = value, onValueChange = {}, readOnly = true,
            label = { Text(label) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
            modifier = Modifier.menuAnchor().fillMaxWidth()
        )
        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { onExpanded(false) }) {
            pilihan.forEach { (b, lunas) ->
                DropdownMenuItem(
                    text = { Text(if (lunas) "$b (Lunas)" else b) },
                    onClick = { onPick(b); onExpanded(false) }
                )
            }
        }
    }
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

// Membuka WhatsApp dengan pesan berisi nama, bulan, metode, kas, denda, dan total.
//   sudahDicatat = false (dari siswa)      : dikirim ke WA bendahara (nomor 6289520371942), isinya minta dicek dan dicatat.
//   sudahDicatat = true  (dari bendahara) : WA membuka pilihan kontak, bendahara meneruskan bukti ke siswa atau grup.
// Pesan disusun lalu di-encode untuk URL wa.me. Bila WhatsApp tidak ada, diam saja (try catch) agar tidak crash.
// CARI-WHATSAPP | Membuka WhatsApp dengan pesan konfirmasi. Nomor bendahara ada di variabel phone.
internal fun sendWhatsAppConfirmation(
    context: Context, studentName: String, bulan: String, method: String, totalBayar: Long, denda: Long,
    sudahDicatat: Boolean = true
) {
    val phone = if (sudahDicatat) "" else "6289520371942"
    val dendaLine = if (denda > 0) "Denda Telat: Rp ${KastepViewModel.formatRupiah(denda)}\n" else ""
    val judul = if (sudahDicatat) "✅ *PEMBAYARAN KAS SUDAH DICATAT*" else "🧾 *KONFIRMASI PEMBAYARAN KAS*"
    val penutup = if (sudahDicatat) "Pembayaran sudah dicatat bendahara di aplikasi KASTEP."
                  else "Mohon dicek dan dicatat di aplikasi KASTEP. Terima kasih."
    val message = "$judul\n\n" +
            "Nama: $studentName\n" +
            "Bulan: $bulan\n" +
            "Metode: $method\n" +
            "Kas: Rp ${KastepViewModel.formatRupiah(totalBayar - denda)}\n" +
            dendaLine +
            "Total Bayar: Rp ${KastepViewModel.formatRupiah(totalBayar)}\n\n" +
            penutup
    val encodedMsg = URLEncoder.encode(message, "UTF-8")
    try {
        val url = if (phone.isEmpty()) "https://wa.me/?text=$encodedMsg" else "https://wa.me/$phone?text=$encodedMsg"
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    } catch (_: Exception) { }
}

// Wadah bagian pilihan metode bayar.
@Composable
private fun PaymentMethodSection(content: @Composable () -> Unit) {
    Box(modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 16.dp)) { content() }
}

// Kartu logo metode bayar yang bisa dipilih. Terpilih: berlatar cyan transparan.
// Kartu logo metode bayar (DANA, GoPay, BCA, dll) yang bisa dipilih.
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
