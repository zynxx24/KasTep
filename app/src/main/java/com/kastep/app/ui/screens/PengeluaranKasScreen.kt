// ==========================================================================
// PETA FILE · KASTEP (Kelompok 1) · PengeluaranKasScreen.kt
// Isi file : Pengeluaran Kas: form tambah, daftar, edit, hapus dengan konfirmasi.
// Penanda  : CARI-PENGELUARAN, CARI-VALIDASI
// Cara cari: VS Code Ctrl+Shift+F (Android Studio Ctrl+Shift+F) lalu ketik
//            "CARI-" untuk semua penanda, atau salah satu nama di baris Penanda.
// ==========================================================================
//  BACA INI DULU : LAYAR "PENGELUARAN KAS"
//  ADMIN : form catat pengeluaran (Tanggal dd/mm/yyyy otomatis hari ini, Jumlah, Keterangan) + daftar riwayat (tekan baris untuk edit atau hapus).
//  USER  : hanya melihat daftar riwayat pengeluaran (form tidak tampil).
//  Validasi tanggal: harus ada di kalender (31/02 ditolak). Tanggal disimpan sebagai teks "05 Oktober 2026".
//  Simpan -> viewModel.addPengeluaran -> saldo turun otomatis.
// » Alamat folder (package) file ini di dalam proyek.
package com.kastep.app.ui.screens

// » background = mengisi warna/gradien latar sebuah tampilan.
import androidx.compose.foundation.background
// » clickable = membuat tampilan bisa diketuk.
import androidx.compose.foundation.clickable
// » Layout dasar: Column (susun atas-bawah), Row (susun kiri-kanan), Box (tumpuk), Spacer (jarak kosong),
// » padding, size, fillMaxWidth, dll.
import androidx.compose.foundation.layout.*
// » rememberScrollState = menyimpan posisi gulir.
import androidx.compose.foundation.rememberScrollState
// » RoundedCornerShape = bentuk persegi dengan sudut membulat.
import androidx.compose.foundation.shape.RoundedCornerShape
// » KeyboardOptions = pengaturan jenis keyboard (angka, email, dll).
import androidx.compose.foundation.text.KeyboardOptions
// » verticalScroll = membuat isi bisa digulir naik-turun.
import androidx.compose.foundation.verticalScroll
// » Icons = daftar ikon bawaan Material.
import androidx.compose.material.icons.Icons
// » Ikon bawaan Material: Add (versi terisi).
import androidx.compose.material.icons.filled.Add
// » Ikon bawaan Material: Delete (versi terisi).
import androidx.compose.material.icons.filled.Delete
// » Ikon bawaan Material: Edit (versi terisi).
import androidx.compose.material.icons.filled.Edit
// » Komponen Material 3 (gaya resmi Android): Text, Button, Card, TextField, Scaffold, dll.
import androidx.compose.material3.*
// » Alat state Compose: remember (mengingat nilai), mutableStateOf (nilai yang bila berubah menggambar ulang
// » layar), LaunchedEffect, dll.
import androidx.compose.runtime.*
// » Alignment = pengatur posisi isi (tengah, atas, bawah, kiri, kanan).
import androidx.compose.ui.Alignment
// » Modifier = daftar 'dekorasi' sebuah tampilan: ukuran, jarak, warna latar, klik, dll.
import androidx.compose.ui.Modifier
// » Brush = 'cat' warna, termasuk gradien (warna berubah halus).
import androidx.compose.ui.graphics.Brush
// » Color = tipe warna Compose; dipakai untuk menulis warna seperti Color.White atau Color(0xFF...).
import androidx.compose.ui.graphics.Color
// » FontWeight = ketebalan huruf (Normal, Medium, Bold, ExtraBold).
import androidx.compose.ui.text.font.FontWeight
// » KeyboardType = jenis keyboard (Number, Email, Password, Phone).
import androidx.compose.ui.text.input.KeyboardType
// » dp = satuan ukuran Android yang sama di semua layar (mis. 16.dp).
import androidx.compose.ui.unit.dp
// » sp = satuan ukuran huruf Android (mengikuti pengaturan ukuran font di HP).
import androidx.compose.ui.unit.sp
// » Memanggil KastepViewModel (dari com.kastep.app.data) supaya bisa dipakai di file ini.
import com.kastep.app.data.KastepViewModel
// » Memanggil Pengeluaran (dari com.kastep.app.data) supaya bisa dipakai di file ini.
import com.kastep.app.data.Pengeluaran
// » Memanggil UserRole (dari com.kastep.app.data) supaya bisa dipakai di file ini.
import com.kastep.app.data.UserRole
// » Memanggil * (dari com.kastep.app.ui.theme) supaya bisa dipakai di file ini.
import com.kastep.app.ui.theme.*

// » Komentar: penanda pencarian. Tanggal otomatis hari ini, dicek kalender, disimpan seperti "05 Oktober
// » 2026".
// CARI-PENGELUARAN CARI-VALIDASI | Tanggal otomatis hari ini, divalidasi kalender (31/02 ditolak), disimpan sebagai "05 Oktober 2026".
// » @Composable = fungsi penggambar tampilan.
@Composable
// » fun PengeluaranKasScreen = layar daftar dan form pengeluaran kas.
fun PengeluaranKasScreen(viewModel: KastepViewModel, onOpenDrawer: () -> Unit, onNavigateToProfile: () -> Unit = {}) {
    // » userProfile = akun yang login.
    val userProfile by viewModel.userProfile.collectAsState()
    // » pengeluaranList = daftar pengeluaran (ikut berubah otomatis).
    val pengeluaranList by viewModel.pengeluaranList.collectAsState()
    // » currentDate = tanggal hari ini untuk bar atas.
    val currentDate = viewModel.getCurrentDateString()
    // » isAdmin = true kalau bendahara. Hanya bendahara yang bisa mencatat.
    val isAdmin = userProfile.role == UserRole.ADMIN

    // Tanggal otomatis diisi hari ini
    // » today = objek kalender hari ini. remember = dibuat sekali selama layar hidup.
    val today = remember { java.util.Calendar.getInstance() }
    // » tanggalHari = dua digit hari sekarang (format "%02d" = selalu 2 digit, contoh "05").
    var tanggalHari by remember { mutableStateOf("%02d".format(today.get(java.util.Calendar.DAY_OF_MONTH))) }
    // » tanggalBulan = dua digit bulan sekarang. +1 karena kalender mulai dari 0.
    var tanggalBulan by remember { mutableStateOf("%02d".format(today.get(java.util.Calendar.MONTH) + 1)) }
    // » tanggalTahun = tahun sekarang sebagai teks.
    var tanggalTahun by remember { mutableStateOf(today.get(java.util.Calendar.YEAR).toString()) }
    // » hapusTarget = pengeluaran yang akan dihapus (null = tidak ada).
    var hapusTarget by remember { mutableStateOf<Pengeluaran?>(null) }
    // » jumlah = isi kotak jumlah (teks, awalnya kosong).
    var jumlah by remember { mutableStateOf("") }
    // » keterangan = isi kotak keterangan.
    var keterangan by remember { mutableStateOf("") }
    // » errorMsg = pesan error (merah). null = tidak ada.
    var errorMsg by remember { mutableStateOf<String?>(null) }
    // » successMsg = pesan sukses (hijau). null = tidak ada.
    var successMsg by remember { mutableStateOf<String?>(null) }

    // » showEditDialog = apakah dialog edit terbuka.
    var showEditDialog by remember { mutableStateOf(false) }
    // » selectedPengeluaran = pengeluaran yang sedang diedit.
    var selectedPengeluaran by remember { mutableStateOf<Pengeluaran?>(null) }

    // » textFieldColors = warna kotak isian yang dipakai bersama semua kotak di layar ini.
    val textFieldColors = OutlinedTextFieldDefaults.colors(
        // » Teks di dalam kotak: putih.
        focusedTextColor = KastepWhite, unfocusedTextColor = KastepWhite,
        // » Warna kursor putih, garis tepi cyan saat dipilih.
        cursorColor = KastepWhite, focusedBorderColor = KastepCyan,
        // » Garis tepi abu transparan 50% saat tidak dipilih.
        unfocusedBorderColor = KastepGray.copy(alpha = 0.5f),
        // » Warna petunjuk (placeholder) abu.
        focusedPlaceholderColor = KastepGray, unfocusedPlaceholderColor = KastepGray
    )

    // » Column = susunan atas-bawah seluruh layar. Penuh, latar hitam, bisa digulir.
    Column(
        modifier = Modifier.fillMaxSize().background(KastepBlack).verticalScroll(rememberScrollState())
    ) {
        // » TopBar = bar atas.
        TopBar(currentDate = currentDate, userName = userProfile.nama, onMenuClick = onOpenDrawer, onNavigateToProfile = onNavigateToProfile)

        // » Kolom judul dengan jarak 20dp kiri-kanan.
        Column(modifier = Modifier.padding(horizontal = 20.dp)) {
            // » Box judul berlatar biru transparan.
            Box(
                modifier = Modifier.background(KastepBlue.copy(alpha = 0.25f), RoundedCornerShape(4.dp)).padding(horizontal = 8.dp, vertical = 2.dp)
            ) {
                // » Teks judul "Pengeluaran Kas" (20sp, tebal).
                Text("Pengeluaran Kas", color = KastepWhite, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(4.dp))
            // » Keterangan kecil di bawah judul.
            Text("Catat dan kelola pengeluaran kas kelas", color = KastepGray, fontSize = 13.sp)
        }

        // » Jarak 24dp.
        Spacer(modifier = Modifier.height(24.dp))

        // » Form input hanya tampil untuk bendahara (ADMIN).
        if (isAdmin) {
            // Error/Success messages
            // » Kalau ada pesan error...
            if (errorMsg != null) {
                // » Teks error merah dengan latar merah transparan 10%.
                Text(errorMsg!!, color = KastepRed, fontSize = 13.sp, modifier = Modifier.padding(horizontal = 20.dp)
                    .fillMaxWidth().background(KastepRed.copy(alpha = 0.1f), RoundedCornerShape(8.dp)).padding(12.dp))
                Spacer(modifier = Modifier.height(8.dp))
            }
            // » Kalau ada pesan sukses...
            if (successMsg != null) {
                // » Teks sukses hijau dengan latar hijau transparan 10%.
                Text(successMsg!!, color = KastepGreen, fontSize = 13.sp, modifier = Modifier.padding(horizontal = 20.dp)
                    .fillMaxWidth().background(KastepGreen.copy(alpha = 0.1f), RoundedCornerShape(8.dp)).padding(12.dp))
                Spacer(modifier = Modifier.height(8.dp))
            }

            // Tanggal
            // » Kolom judul "Tanggal" dan tiga kotak isian.
            Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                // » Label "Tanggal" (18sp, tebal).
                Text("Tanggal", color = KastepWhite, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(12.dp))
                // » Row = tiga kotak (hari, bulan, tahun) berdampingan, jarak 12dp.
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedTextField(
                        // » Kotak hari: setiap diketik, ambil angka saja dan maksimal 2 digit, lalu simpan.
                        value = tanggalHari, onValueChange = { v -> v.filter { it.isDigit() }.take(2).let { tanggalHari = it } },
                        // » Bobot 0.8 (lebih kecil), tinggi 56dp, petunjuk "DD".
                        modifier = Modifier.weight(0.8f).height(56.dp), placeholder = { Text("DD") },
                        colors = textFieldColors, shape = RoundedCornerShape(12.dp), singleLine = true,
                        // » Keyboard khusus angka.
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                    OutlinedTextField(
                        // » Kotak bulan: angka saja, maksimal 2 digit.
                        value = tanggalBulan, onValueChange = { v -> v.filter { it.isDigit() }.take(2).let { tanggalBulan = it } },
                        // » Bobot 1.2, petunjuk "MM".
                        modifier = Modifier.weight(1.2f).height(56.dp), placeholder = { Text("MM") },
                        colors = textFieldColors, shape = RoundedCornerShape(12.dp), singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                    OutlinedTextField(
                        // » Kotak tahun: angka saja, maksimal 4 digit.
                        value = tanggalTahun, onValueChange = { v -> v.filter { it.isDigit() }.take(4).let { tanggalTahun = it } },
                        // » Bobot 1.2, petunjuk "YYYY".
                        modifier = Modifier.weight(1.2f).height(56.dp), placeholder = { Text("YYYY") },
                        colors = textFieldColors, shape = RoundedCornerShape(12.dp), singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                }
            }

            // » Jarak 24dp.
            Spacer(modifier = Modifier.height(24.dp))

            // Jumlah
            // » Kolom "Jumlah".
            Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                // » Label "Jumlah".
                Text("Jumlah", color = KastepWhite, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(
                    // » Kotak jumlah: angka saja, maksimal 9 digit. Setiap diketik, pesan error dan sukses dihapus.
                    value = jumlah, onValueChange = { v -> jumlah = v.filter { it.isDigit() }.take(9); errorMsg = null; successMsg = null },
                    // » Petunjuk "Rp 0".
                    modifier = Modifier.fillMaxWidth().height(56.dp), placeholder = { Text("Rp 0") },
                    // » Sudut membulat 28dp (bentuk pil).
                    colors = textFieldColors, shape = RoundedCornerShape(28.dp), singleLine = true,
                    // » Keyboard angka.
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }

            // » Jarak 24dp.
            Spacer(modifier = Modifier.height(24.dp))

            // Keterangan
            // » Kolom "Keterangan".
            Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                // » Label "Keterangan".
                Text("Keterangan", color = KastepWhite, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(12.dp))
                // » Kotak keterangan.
                OutlinedTextField(
                    // » Setiap diketik: simpan teks dan hapus pesan error/sukses.
                    value = keterangan, onValueChange = { keterangan = it; errorMsg = null; successMsg = null },
                    // » Tinggi 120dp. Petunjuk "Keterangan pengeluaran".
                    modifier = Modifier.fillMaxWidth().height(120.dp), placeholder = { Text("Keterangan pengeluaran") },
                    // » maxLines = 5: maksimal 5 baris, lalu bisa digulir.
                    colors = textFieldColors, shape = RoundedCornerShape(20.dp), maxLines = 5
                )
            }

            // » Jarak 24dp.
            Spacer(modifier = Modifier.height(24.dp))

            // Submit button
            // » Button = tombol simpan pengeluaran.
            Button(
                // » onClick: cek semua isian satu per satu sebelum menyimpan.
                onClick = {
                    // » day = hari sebagai angka (null kalau bukan angka).
                    val day = tanggalHari.toIntOrNull()
                    // » month = bulan sebagai angka.
                    val month = tanggalBulan.toIntOrNull()
                    // » year = tahun sebagai angka.
                    val year = tanggalTahun.toIntOrNull()
                    // » amount = jumlah sebagai angka besar (Long). Kalau kosong atau salah, 0.
                    val amount = jumlah.toLongOrNull() ?: 0L

                    // » Kalau ada kotak tanggal yang kosong...
                    if (tanggalHari.isBlank() || tanggalBulan.isBlank() || tanggalTahun.isBlank()) {
                        // » ...pesan error: tanggal harus diisi lengkap.
                        errorMsg = "Tanggal (DD/MM/YYYY) harus diisi lengkap"
                        // » Hapus pesan sukses lama.
                        successMsg = null
                        // » return@Button = berhenti di sini (tidak lanjut menyimpan).
                        return@Button
                    }
                    // » Kalau hari kosong atau di luar 1 sampai 31...
                    if (day == null || day !in 1..31) {
                        // » ...pesan error: hari harus 01-31.
                        errorMsg = "Hari (DD) harus berupa angka 01 - 31"
                        successMsg = null
                        return@Button
                    }
                    // » Kalau bulan kosong atau di luar 1 sampai 12...
                    if (month == null || month !in 1..12) {
                        // » ...pesan error: bulan harus 01-12.
                        errorMsg = "Bulan (MM) harus berupa angka 01 - 12"
                        successMsg = null
                        return@Button
                    }
                    // » Kalau tahun kosong atau di luar 2020 sampai 2100...
                    if (year == null || year < 2020 || year > 2100) {
                        // » ...pesan error: tahun tidak valid (contoh 2026).
                        errorMsg = "Tahun (YYYY) tidak valid (contoh: 2026)"
                        successMsg = null
                        // » Berhenti di sini: tanggal salah atau kosong.
                        return@Button
                    }
                    // » Komentar: tolak tanggal yang tidak ada di kalender, misalnya 31/02.
                    // Tolak tanggal yang tidak ada di kalender, misalnya 31/02
                    // » cal = kalender baru. isLenient = false = tanggal harus benar-benar ada (kalau tidak, error). Bulan
                    // » dikurangi 1 karena kalender mulai dari 0.
                    val cal = java.util.Calendar.getInstance().apply { isLenient = false; clear(); set(year, month - 1, day) }
                    // » runCatching { cal.time } = coba ambil tanggalnya. Kalau gagal, berarti tanggal tidak valid.
                    if (runCatching { cal.time }.isFailure) {
                        // » Pesan error: tanggal tidak ada di kalender.
                        errorMsg = "Tanggal $day/$month/$year tidak ada di kalender"
                        successMsg = null
                        return@Button
                    }
                    // » Kalau jumlah 0 atau kurang: error.
                    if (amount <= 0) {
                        errorMsg = "Jumlah pengeluaran harus lebih dari Rp 0"
                        successMsg = null
                        return@Button
                    }
                    // » Kalau keterangan kosong: error.
                    if (keterangan.isBlank()) {
                        errorMsg = "Keterangan pengeluaran tidak boleh kosong"
                        successMsg = null
                        return@Button
                    }

                    // » Komentar: simpan dengan format yang sama dengan data lain, misalnya "05 Oktober 2026".
                    // Simpan dengan format yang sama seperti data lain: "05 Oktober 2026"
                    // » tanggal = ubah tanggal dari kalender jadi teks "dd MMMM yyyy" dalam bahasa Indonesia.
                    val tanggal = java.text.SimpleDateFormat("dd MMMM yyyy", java.util.Locale("id", "ID")).format(cal.time)

                    // » addPengeluaran = simpan pengeluaran baru lewat ViewModel. Hasil null = berhasil, teks = error.
                    val result = viewModel.addPengeluaran(tanggal, amount, keterangan)
                    // » Kalau ada error...
                    if (result != null) {
                        // » ...tampilkan error, sembunyikan pesan sukses.
                        errorMsg = result; successMsg = null
                    // » Kalau tidak ada error (berhasil)...
                    } else {
                        // » ...tampilkan pesan sukses.
                        successMsg = "Pengeluaran berhasil ditambahkan!"
                        // » Hapus pesan error.
                        errorMsg = null
                        // » Kosongkan jumlah dan keterangan (tanggal tetap, supaya mudah isi lagi).
                        jumlah = ""; keterangan = ""
                    }
                },
                // » modifier tombol: penuh lebar, tinggi 56dp, jarak kiri-kanan 32dp.
                modifier = Modifier.fillMaxWidth().height(56.dp).padding(horizontal = 32.dp),
                // » Warna tombol transparan (warna sebenarnya dari gradasi di dalam).
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                // » Sudut membulat 16dp.
                shape = RoundedCornerShape(16.dp)
            ) {
                // » Box isi tombol, penuh sebesar tombol.
                Box(
                    modifier = Modifier.fillMaxSize()
                        // » Latar gradasi horizontal biru (GradientBlueStart ke GradientBlueEnd), sudut 16dp.
                        .background(Brush.horizontalGradient(listOf(GradientBlueStart, GradientBlueEnd)), RoundedCornerShape(16.dp)),
                    // » Isi di tengah.
                    contentAlignment = Alignment.Center
                ) {
                    // » Row = ikon dan tulisan berdampingan.
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        // » Ikon tambah (+), putih, 20dp.
                        Icon(Icons.Default.Add, contentDescription = null, tint = KastepWhite, modifier = Modifier.size(20.dp))
                        // » Jarak 8dp.
                        Spacer(modifier = Modifier.width(8.dp))
                        // » Teks "Tambah Pengeluaran" (18sp, tebal).
                        Text("Tambah Pengeluaran", color = KastepWhite, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            // » Jarak 32dp di bawah form.
            Spacer(modifier = Modifier.height(32.dp))
        }

        // » Komentar: bagian daftar riwayat pengeluaran.
        // Riwayat Pengeluaran List
        // » Row = judul daftar dan total di kanan.
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // » Teks "Riwayat Pengeluaran".
            Text("Riwayat Pengeluaran", color = KastepWhite, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            // » Teks total semua pengeluaran (merah). sumOf = jumlahkan jumlah semua pengeluaran.
            Text("Total Rp ${KastepViewModel.formatRupiah(pengeluaranList.sumOf { it.jumlah })}", color = KastepRed, fontSize = 13.sp, fontWeight = FontWeight.Bold)
        }
        // » Jarak 12dp.
        Spacer(modifier = Modifier.height(12.dp))

        // » Kalau daftar kosong...
        if (pengeluaranList.isEmpty()) {
            // » ...tampilkan "Belum ada pengeluaran tercatat."
            Text("Belum ada pengeluaran tercatat.", color = KastepGray, fontSize = 14.sp, modifier = Modifier.padding(horizontal = 20.dp))
        } else {
            // » Urutkan dari tanggal terbaru (kunciTanggal), lalu tampilkan setiap pengeluaran sebagai p.
            pengeluaranList.sortedByDescending { com.kastep.app.data.KastepRepository.kunciTanggal(it.tanggal) }.forEach { p ->
                // » Card = kartu satu pengeluaran.
                Card(
                    // » Penuh lebar, jarak kiri-kanan 20dp dan atas-bawah 4dp.
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 4.dp)
                        // » Kalau ADMIN: kartu bisa ditekan untuk edit. Kalau USER: tidak.
                        .then(if (isAdmin) Modifier.clickable { selectedPengeluaran = p; showEditDialog = true } else Modifier),
                    // » Sudut kartu membulat 12dp.
                    shape = RoundedCornerShape(12.dp),
                    // » Warna biru tua (#1A1A2E).
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A2E))
                ) {
                    // » Row isi kartu.
                    Row(
                        // » Penuh lebar, jarak dalam 16dp.
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                        // » SpaceBetween: kiri dan kanan.
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // » Column kiri (keterangan dan tanggal), mengisi sisa lebar.
                        Column(modifier = Modifier.weight(1f)) {
                            // » Keterangan pengeluaran (14sp, semi tebal).
                            Text(p.keterangan, color = KastepWhite, fontSize = 14.sp, fontWeight = FontWeight.Medium)
                            Spacer(modifier = Modifier.height(4.dp))
                            // » Tanggal pengeluaran (abu, 12sp).
                            Text(p.tanggal, color = KastepGray, fontSize = 12.sp)
                        }
                        // » Column kanan: jumlah dan ikon, rata kanan.
                        Column(horizontalAlignment = Alignment.End) {
                            // » Jumlah rupiah berwarna merah, tebal.
                            Text("Rp ${KastepViewModel.formatRupiah(p.jumlah)}", color = KastepRed, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                            // » Kalau ADMIN: tampilkan ikon edit dan hapus.
                            if (isAdmin) {
                                Spacer(modifier = Modifier.height(4.dp))
                                // » Row ikon edit dan hapus.
                                Row {
                                    // » Ikon pensil (cyan), 18dp. Saat ditekan: pilih pengeluaran dan buka dialog edit.
                                    Icon(Icons.Default.Edit, contentDescription = "Edit", tint = KastepCyan, modifier = Modifier.size(18.dp).clickable { selectedPengeluaran = p; showEditDialog = true })
                                    // » Jarak 8dp.
                                    Spacer(modifier = Modifier.width(8.dp))
                                    // » Ikon tempat sampah (merah), 18dp. Saat ditekan: jadikan pengeluaran ini target hapus (muncul
                                    // » konfirmasi).
                                    Icon(Icons.Default.Delete, contentDescription = "Hapus", tint = KastepRed, modifier = Modifier.size(18.dp).clickable { hapusTarget = p })
                                }
                            }
                        }
                    }
                }
            }
        }

        // » Jarak 32dp di bawah daftar.
        Spacer(modifier = Modifier.height(32.dp))
    }

    // Edit Pengeluaran Dialog
    // » Kalau dialog edit harus tampil dan ada pengeluaran yang dipilih...
    if (showEditDialog && selectedPengeluaran != null) {
        // » EditPengeluaranDialog = dialog edit (dibuat di bawah).
        EditPengeluaranDialog(
            // » pengeluaran = data yang diedit (!! = pasti tidak null di sini).
            pengeluaran = selectedPengeluaran!!,
            // » onDismiss: tutup dialog dan lepas pilihan.
            onDismiss = { showEditDialog = false; selectedPengeluaran = null },
            // » onSave: dipanggil saat Simpan ditekan dengan (tanggal, jumlah, keterangan).
            onSave = { tanggal, amt, ket ->
                // » updatePengeluaran dengan id pengeluaran yang dipilih. Hasilnya error atau null.
                val err = viewModel.updatePengeluaran(selectedPengeluaran!!.id, tanggal, amt, ket)
                // » Kalau berhasil (err == null), tutup dialog dan lepas pilihan.
                if (err == null) { showEditDialog = false; selectedPengeluaran = null }
                // » Kembalikan hasil (null atau teks error) ke dialog.
                err
            },
            // » onDelete: dipanggil saat "Hapus Pengeluaran" di dalam dialog.
            onDelete = {
                // » Simpan pengeluaran ini sebagai target hapus.
                hapusTarget = selectedPengeluaran
                // » Tutup dialog edit dan lepas pilihan.
                showEditDialog = false; selectedPengeluaran = null
            }
        )
    }

    // » Kalau ada target hapus (tidak null), tampilkan konfirmasi hapus.
    hapusTarget?.let { target ->
        // » AlertDialog = kotak konfirmasi hapus.
        AlertDialog(
            // » Tekan di luar kotak = batal (target hapus dikosongkan).
            onDismissRequest = { hapusTarget = null },
            // » Judul "Hapus Pengeluaran?".
            title = { Text("Hapus Pengeluaran?", fontWeight = FontWeight.Bold) },
            // » Isi: nama pengeluaran dan jumlahnya akan dihapus, dan saldo kas akan bertambah.
            text = { Text("\"${target.keterangan}\" sebesar Rp ${KastepViewModel.formatRupiah(target.jumlah)} akan dihapus dan saldo kas bertambah kembali.") },
            confirmButton = {
                // » Tombol Hapus: panggil deletePengeluaran(id), lalu kosongkan target. Teks merah.
                TextButton(onClick = { viewModel.deletePengeluaran(target.id); hapusTarget = null }) { Text("Hapus", color = KastepRed) }
            },
            // » Tombol Batal: kosongkan target tanpa menghapus.
            dismissButton = { TextButton(onClick = { hapusTarget = null }) { Text("Batal") } }
        )
    }
}

// » Komentar: dialog edit. Pesan error tampil kalau isian salah.
// CARI-PENGELUARAN | Dialog edit; pesan error tampil bila isian salah.
@Composable
// » private fun EditPengeluaranDialog = dialog edit pengeluaran (hanya dipakai di file ini).
private fun EditPengeluaranDialog(
    // » pengeluaran = data yang sedang diedit.
    pengeluaran: Pengeluaran,
    // » onDismiss = fungsi tutup dialog.
    onDismiss: () -> Unit,
    // » onSave = fungsi simpan. Mengembalikan null (berhasil) atau teks error.
    onSave: (String, Long, String) -> String?,
    // » onDelete = fungsi hapus.
    onDelete: () -> Unit
) {
    // » tanggal = salinan tanggal untuk diedit.
    var tanggal by remember { mutableStateOf(pengeluaran.tanggal) }
    // » jumlah = salinan jumlah sebagai teks.
    var jumlah by remember { mutableStateOf(pengeluaran.jumlah.toString()) }
    // » keterangan = salinan keterangan.
    var keterangan by remember { mutableStateOf(pengeluaran.keterangan) }
    // » error = pesan error di dalam dialog.
    var error by remember { mutableStateOf<String?>(null) }

    // » AlertDialog = kotak dialog edit.
    AlertDialog(
        // » Tekan di luar kotak = tutup.
        onDismissRequest = onDismiss,
        // » Judul "Edit Pengeluaran".
        title = { Text("Edit Pengeluaran", fontWeight = FontWeight.Bold) },
        text = {
            Column {
                // » Kotak tanggal dengan label contoh "15 Juli 2026". Setiap diketik, error dihapus.
                OutlinedTextField(value = tanggal, onValueChange = { tanggal = it; error = null }, label = { Text("Tanggal (contoh: 15 Juli 2026)") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
                Spacer(modifier = Modifier.height(8.dp))
                // » Kotak jumlah: angka saja, maksimal 9 digit. Setiap diketik, error dihapus.
                OutlinedTextField(value = jumlah, onValueChange = { v -> jumlah = v.filter { it.isDigit() }.take(9); error = null }, label = { Text("Jumlah (Rp)") }, modifier = Modifier.fillMaxWidth(), singleLine = true, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
                Spacer(modifier = Modifier.height(8.dp))
                // » Kotak keterangan. Setiap diketik, isinya disimpan.
                OutlinedTextField(value = keterangan, onValueChange = { keterangan = it }, label = { Text("Keterangan") }, modifier = Modifier.fillMaxWidth(), maxLines = 3)
                // » Kalau ada error...
                if (error != null) {
                    Spacer(modifier = Modifier.height(6.dp))
                    // » Tampilkan pesan error kecil merah.
                    Text(error!!, color = KastepRed, fontSize = 12.sp)
                }
                Spacer(modifier = Modifier.height(12.dp))
                // » Tombol hapus di dalam dialog, lebar penuh.
                TextButton(onClick = onDelete, modifier = Modifier.fillMaxWidth()) {
                    // » Ikon tempat sampah merah.
                    Icon(Icons.Default.Delete, contentDescription = null, tint = KastepRed, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    // » Teks "Hapus Pengeluaran", merah.
                    Text("Hapus Pengeluaran", color = KastepRed)
                }
            }
        },
        confirmButton = {
            // » Tombol Simpan: ambil jumlah, lalu simpan.
            TextButton(onClick = {
                // » amt = jumlah sebagai angka (0 kalau kosong atau salah).
                val amt = jumlah.toLongOrNull() ?: 0L
                // » error = onSave(tanggal, amt, keterangan). Kalau null berarti berhasil. Kalau teks, tampil di dialog.
                error = onSave(tanggal, amt, keterangan)
            }) { Text("Simpan") }
        },
        // » Tombol Batal: tutup dialog.
        dismissButton = { TextButton(onClick = onDismiss) { Text("Batal") } }
    )
}
