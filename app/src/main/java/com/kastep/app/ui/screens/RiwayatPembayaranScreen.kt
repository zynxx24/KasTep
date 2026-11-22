// ==========================================================================
// PETA FILE · KASTEP (Kelompok 1) · RiwayatPembayaranScreen.kt
// Isi file : Riwayat semua pembayaran dengan pencarian dan halaman.
// Penanda  : CARI-PEMBAYARAN
// Cara cari: VS Code Ctrl+Shift+F (Android Studio Ctrl+Shift+F) lalu ketik
//            "CARI-" untuk semua penanda, atau salah satu nama di baris Penanda.
// ==========================================================================
//  BACA INI DULU : LAYAR "RIWAYAT BAYAR"
//  Tabel semua pembayaran (kolom NO, Tanggal, Nama Siswa, Jumlah). Ada kolom cari (nama atau bulan), tombol Bayar Kas (menuju layar Pembayaran),
//  dan nomor halaman: 8 baris per halaman (itemsPerPage = 8). Data dari viewModel.paymentRecords.
// » Alamat folder (package) file ini di dalam proyek.
package com.kastep.app.ui.screens

// » background = mengisi warna/gradien latar sebuah tampilan.
import androidx.compose.foundation.background
// » border = garis tepi sebuah tampilan.
import androidx.compose.foundation.border
// » clickable = membuat tampilan bisa diketuk.
import androidx.compose.foundation.clickable
// » Memanggil Arrangement (dari androidx.compose.foundation.layout) supaya bisa dipakai di file ini.
import androidx.compose.foundation.layout.Arrangement
// » Memanggil Box (dari androidx.compose.foundation.layout) supaya bisa dipakai di file ini.
import androidx.compose.foundation.layout.Box
// » Memanggil Column (dari androidx.compose.foundation.layout) supaya bisa dipakai di file ini.
import androidx.compose.foundation.layout.Column
// » Memanggil Row (dari androidx.compose.foundation.layout) supaya bisa dipakai di file ini.
import androidx.compose.foundation.layout.Row
// » Memanggil Spacer (dari androidx.compose.foundation.layout) supaya bisa dipakai di file ini.
import androidx.compose.foundation.layout.Spacer
// » Memanggil fillMaxSize (dari androidx.compose.foundation.layout) supaya bisa dipakai di file ini.
import androidx.compose.foundation.layout.fillMaxSize
// » Memanggil fillMaxWidth (dari androidx.compose.foundation.layout) supaya bisa dipakai di file ini.
import androidx.compose.foundation.layout.fillMaxWidth
// » Memanggil height (dari androidx.compose.foundation.layout) supaya bisa dipakai di file ini.
import androidx.compose.foundation.layout.height
// » Memanggil padding (dari androidx.compose.foundation.layout) supaya bisa dipakai di file ini.
import androidx.compose.foundation.layout.padding
// » Memanggil size (dari androidx.compose.foundation.layout) supaya bisa dipakai di file ini.
import androidx.compose.foundation.layout.size
// » Memanggil width (dari androidx.compose.foundation.layout) supaya bisa dipakai di file ini.
import androidx.compose.foundation.layout.width
// » rememberScrollState = menyimpan posisi gulir.
import androidx.compose.foundation.rememberScrollState
// » CircleShape = bentuk lingkaran.
import androidx.compose.foundation.shape.CircleShape
// » RoundedCornerShape = bentuk persegi dengan sudut membulat.
import androidx.compose.foundation.shape.RoundedCornerShape
// » verticalScroll = membuat isi bisa digulir naik-turun.
import androidx.compose.foundation.verticalScroll
// » Memanggil Button (dari androidx.compose.material3) supaya bisa dipakai di file ini.
import androidx.compose.material3.Button
// » Memanggil ButtonDefaults (dari androidx.compose.material3) supaya bisa dipakai di file ini.
import androidx.compose.material3.ButtonDefaults
// » Memanggil HorizontalDivider (dari androidx.compose.material3) supaya bisa dipakai di file ini.
import androidx.compose.material3.HorizontalDivider
// » Memanggil OutlinedTextField (dari androidx.compose.material3) supaya bisa dipakai di file ini.
import androidx.compose.material3.OutlinedTextField
// » Memanggil OutlinedTextFieldDefaults (dari androidx.compose.material3) supaya bisa dipakai di file ini.
import androidx.compose.material3.OutlinedTextFieldDefaults
// » Memanggil Text (dari androidx.compose.material3) supaya bisa dipakai di file ini.
import androidx.compose.material3.Text
// » @Composable = penanda bahwa sebuah fungsi menggambar tampilan.
import androidx.compose.runtime.Composable
// » Memanggil collectAsState (dari androidx.compose.runtime) supaya bisa dipakai di file ini.
import androidx.compose.runtime.collectAsState
// » Memanggil getValue (dari androidx.compose.runtime) supaya bisa dipakai di file ini.
import androidx.compose.runtime.getValue
// » Memanggil mutableIntStateOf (dari androidx.compose.runtime) supaya bisa dipakai di file ini.
import androidx.compose.runtime.mutableIntStateOf
// » Memanggil mutableStateOf (dari androidx.compose.runtime) supaya bisa dipakai di file ini.
import androidx.compose.runtime.mutableStateOf
// » Memanggil remember (dari androidx.compose.runtime) supaya bisa dipakai di file ini.
import androidx.compose.runtime.remember
// » Memanggil setValue (dari androidx.compose.runtime) supaya bisa dipakai di file ini.
import androidx.compose.runtime.setValue
// » Alignment = pengatur posisi isi (tengah, atas, bawah, kiri, kanan).
import androidx.compose.ui.Alignment
// » Modifier = daftar 'dekorasi' sebuah tampilan: ukuran, jarak, warna latar, klik, dll.
import androidx.compose.ui.Modifier
// » clip = memotong tampilan mengikuti sebuah bentuk (mis. sudut bulat atau lingkaran).
import androidx.compose.ui.draw.clip
// » Color = tipe warna Compose; dipakai untuk menulis warna seperti Color.White atau Color(0xFF...).
import androidx.compose.ui.graphics.Color
// » FontWeight = ketebalan huruf (Normal, Medium, Bold, ExtraBold).
import androidx.compose.ui.text.font.FontWeight
// » dp = satuan ukuran Android yang sama di semua layar (mis. 16.dp).
import androidx.compose.ui.unit.dp
// » sp = satuan ukuran huruf Android (mengikuti pengaturan ukuran font di HP).
import androidx.compose.ui.unit.sp
// » Memanggil KastepViewModel (dari com.kastep.app.data) supaya bisa dipakai di file ini.
import com.kastep.app.data.KastepViewModel
// » Memanggil PaymentRecord (dari com.kastep.app.data) supaya bisa dipakai di file ini.
import com.kastep.app.data.PaymentRecord
// » Memanggil KastepBlack (dari com.kastep.app.ui.theme) supaya bisa dipakai di file ini.
import com.kastep.app.ui.theme.KastepBlack
// » Memanggil KastepBlue (dari com.kastep.app.ui.theme) supaya bisa dipakai di file ini.
import com.kastep.app.ui.theme.KastepBlue
// » Memanggil KastepCyan (dari com.kastep.app.ui.theme) supaya bisa dipakai di file ini.
import com.kastep.app.ui.theme.KastepCyan
// » Memanggil KastepGray (dari com.kastep.app.ui.theme) supaya bisa dipakai di file ini.
import com.kastep.app.ui.theme.KastepGray
// » Memanggil KastepWhite (dari com.kastep.app.ui.theme) supaya bisa dipakai di file ini.
import com.kastep.app.ui.theme.KastepWhite

// » Komentar: fitur layar riwayat: 8 data per halaman, cari nama atau bulan, tombol Bayar Kas.
// CARI-PEMBAYARAN | 8 data per halaman; cari berdasarkan nama atau bulan; tombol Bayar Kas ke halaman Pembayaran.
// » @Composable = fungsi penggambar tampilan.
@Composable
// » fun RiwayatPembayaranScreen = layar daftar semua riwayat pembayaran.
fun RiwayatPembayaranScreen(
    // » viewModel = sumber data riwayat.
    viewModel: KastepViewModel,
    // » onOpenDrawer = fungsi membuka menu samping.
    onOpenDrawer: () -> Unit,
    // » onNavigateToProfile = fungsi ke halaman Profil.
    onNavigateToProfile: () -> Unit = {},
    // » onBayarKas = fungsi pindah ke layar Pembayaran (default kosong).
    onBayarKas: () -> Unit = {}
) {
    // » allRecords = semua riwayat pembayaran. collectAsState = ikut berubah otomatis.
    val allRecords by viewModel.paymentRecords.collectAsState()
    // » query = teks kata cari (kosong di awal).
    var query by remember { mutableStateOf("") }
    // » paymentRecords = riwayat yang nama siswanya atau bulannya mengandung kata cari (tanpa huruf
    // » besar/kecil). Kalau kata cari kosong, semua riwayat.
    val paymentRecords = allRecords.filter { query.isBlank() || it.namaSiswa.contains(query.trim(), ignoreCase = true) || it.bulan.contains(query.trim(), ignoreCase = true) }
    // » userProfile = akun yang login (untuk bar atas).
    val userProfile by viewModel.userProfile.collectAsState()
    // » currentDate = tanggal hari ini untuk bar atas.
    val currentDate = viewModel.getCurrentDateString()

    // » itemsPerPage = jumlah baris per halaman = 8.
    val itemsPerPage = 8
    // » totalPages = jumlah halaman. Pembulatan ke atas. coerceAtLeast(1) = minimal 1 halaman walau kosong.
    val totalPages = ((paymentRecords.size + itemsPerPage - 1) / itemsPerPage).coerceAtLeast(1)
    // » currentPage = nomor halaman yang sedang dilihat (mulai 1). mutableIntStateOf = angka yang kalau berubah,
    // » tampilan ikut digambar ulang.
    var currentPage by remember { mutableIntStateOf(1) }
    // Halaman tidak boleh melebihi jumlah data (mis. setelah pencarian / reset data)
    // » Kalau halaman yang dilihat lebih besar dari jumlah halaman (misalnya setelah dicari), pindah ke halaman
    // » terakhir.
    if (currentPage > totalPages) currentPage = totalPages

    // » startIndex = posisi data pertama di halaman ini (dihitung dari 0).
    val startIndex = (currentPage - 1) * itemsPerPage
    // » endIndex = posisi data terakhir + 1, tidak boleh melewati jumlah data.
    val endIndex = minOf(startIndex + itemsPerPage, paymentRecords.size)
    // » currentPageItems = data yang tampil di halaman ini.
    val currentPageItems = paymentRecords.subList(startIndex, endIndex)

    // » Column = susunan atas-bawah layar. Penuh, latar hitam.
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(KastepBlack)
    ) {
        // Top bar
        // » TopBar = bar atas (menu, tanggal, profil).
        TopBar(currentDate = currentDate, userName = userProfile.nama, onMenuClick = onOpenDrawer, onNavigateToProfile = onNavigateToProfile)

        // Title section
        // » Kolom judul dengan jarak kiri-kanan 20dp.
        Column(modifier = Modifier.padding(horizontal = 20.dp)) {
            // » Box judul berlatar biru transparan.
            Box(
                modifier = Modifier
                    .background(
                        color = KastepBlue.copy(alpha = 0.25f),
                        shape = RoundedCornerShape(4.dp)
                    )
                    .padding(horizontal = 8.dp, vertical = 2.dp)
            ) {
                Text(
                    // » Teks judul "Riwayat Pembayaran" (putih, 20sp, tebal).
                    text = "Riwayat Pembayaran",
                    color = KastepWhite,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            // » Keterangan kecil: "Ringkasan informasi kas kelas".
            Text(
                text = "Ringkasan informasi kas kelas",
                color = KastepGray,
                fontSize = 13.sp
            )
        }

        // » Jarak 12dp.
        Spacer(modifier = Modifier.height(12.dp))

        // Pencarian + tombol Bayar Kas
        // » Row = kotak cari dan tombol Bayar Kas dalam satu baris.
        Row(
            modifier = Modifier
                .fillMaxWidth()
                // » Jarak kiri-kanan 20dp.
                .padding(horizontal = 20.dp),
            // » Jarak antar elemen 8dp.
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // » OutlinedTextField = kotak cari.
            OutlinedTextField(
                // » value = isi kotak cari = query.
                value = query,
                // » Setiap diketik: simpan kata cari dan kembali ke halaman 1 (supaya hasil dimulai dari awal).
                onValueChange = { query = it; currentPage = 1 },
                // » weight(1f) = kotak cari mengisi sisa lebar baris.
                modifier = Modifier.weight(1f),
                // » Petunjuk "Cari nama / bulan".
                placeholder = { Text("Cari nama / bulan", color = KastepGray, fontSize = 13.sp) },
                // » singleLine = satu baris.
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = KastepWhite, unfocusedTextColor = KastepWhite,
                    focusedBorderColor = KastepCyan, unfocusedBorderColor = KastepGray.copy(alpha = 0.5f), cursorColor = KastepCyan
                )
            )
            // » Button = tombol Bayar Kas.
            Button(
                // » onClick: panggil onBayarKas (pindah ke layar Pembayaran).
                onClick = onBayarKas,
                // » Warna biru KASTEP.
                colors = ButtonDefaults.buttonColors(containerColor = KastepBlue),
                // » Sudut membulat 20dp.
                shape = RoundedCornerShape(20.dp)
            ) {
                // » Teks "Bayar Kas" (13sp, semi tebal).
                Text("Bayar Kas", color = KastepWhite, fontSize = 13.sp, fontWeight = FontWeight.Medium)
            }
        }

        // » Jarak 16dp.
        Spacer(modifier = Modifier.height(16.dp))

        // » Komentar: tabel pembayaran dalam kartu bersudut membulat.
        // Payment table in a rounded card
        // » Box = wadah tabel.
        Box(
            // » Modifier kartu tabel: weight(1f) = mengisi sisa tinggi layar.
            modifier = Modifier
                .weight(1f)
                // » Penuh lebar.
                .fillMaxWidth()
                // » Jarak kiri-kanan 20dp.
                .padding(horizontal = 20.dp)
                // » border = garis tepi tipis (1dp) abu transparan 30%.
                .border(
                    width = 1.dp,
                    color = KastepGray.copy(alpha = 0.3f),
                    // » Sudut garis tepi membulat 16dp.
                    shape = RoundedCornerShape(16.dp)
                )
                // » clip = potong isi mengikuti sudut kartu 16dp.
                .clip(RoundedCornerShape(16.dp))
        ) {
            // » Column = isi tabel, susun ke bawah.
            Column(
                modifier = Modifier
                    // » Penuh lebar dan tinggi kartu.
                    .fillMaxSize()
                    // » Jarak dalam 16dp.
                    .padding(16.dp)
                    // » verticalScroll = tabel bisa digulir kalau barisnya banyak.
                    .verticalScroll(rememberScrollState())
            ) {
                // » Komentar: header tabel.
                // Table header
                // » Row = baris judul kolom.
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        // » Jarak 12dp di bawah header.
                        .padding(bottom = 12.dp)
                ) {
                    // » Header kolom "NO", lebar 30dp.
                    Text("NO", color = KastepGray, fontSize = 12.sp, fontWeight = FontWeight.Medium, modifier = Modifier.width(30.dp))
                    // » Header kolom "Tanggal", bobot 1.2 (lebar relatif).
                    Text("Tanggal", color = KastepGray, fontSize = 12.sp, fontWeight = FontWeight.Medium, modifier = Modifier.weight(1.2f))
                    // » Header kolom "Nama Siswa", bobot 1.
                    Text("Nama Siswa", color = KastepGray, fontSize = 12.sp, fontWeight = FontWeight.Medium, modifier = Modifier.weight(1f))
                    // » Header kolom "Jumlah", bobot 1.
                    Text("Jumlah", color = KastepGray, fontSize = 12.sp, fontWeight = FontWeight.Medium, modifier = Modifier.weight(1f))
                }

                // » Garis tipis di bawah header.
                HorizontalDivider(color = KastepGray.copy(alpha = 0.2f), thickness = 0.5.dp)

                // Table rows
                // » Kalau tidak ada baris di halaman ini...
                if (currentPageItems.isEmpty()) {
                    // » Jarak 20dp.
                    Spacer(modifier = Modifier.height(20.dp))
                    // » Tampilkan "Belum ada pembayaran yang cocok".
                    Text("Belum ada pembayaran yang cocok", color = KastepGray, fontSize = 14.sp)
                }
                // » Untuk setiap riwayat di halaman ini...
                currentPageItems.forEach { record ->
                    // » Jarak 20dp di atas baris.
                    Spacer(modifier = Modifier.height(20.dp))

                    // » Row = satu baris data tabel.
                    Row(
                        // » Penuh lebar.
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        // » Nomor riwayat, lebar 30dp.
                        Text("${record.no}", color = KastepWhite, fontSize = 14.sp, modifier = Modifier.width(30.dp))
                        // » Tanggal pembayaran, bobot 1.2.
                        Text(record.tanggal, color = KastepWhite, fontSize = 14.sp, modifier = Modifier.weight(1.2f))
                        // » Nama siswa, bobot 1.
                        Text(record.namaSiswa, color = KastepWhite, fontSize = 14.sp, modifier = Modifier.weight(1f))
                        // » Jumlah uang: "Rp" dan angka dengan titik ribuan, bobot 1.
                        Text("Rp${KastepViewModel.formatRupiah(record.jumlah)}", color = KastepWhite, fontSize = 14.sp, modifier = Modifier.weight(1f))
                    }

                    // » Jarak 20dp di bawah baris.
                    Spacer(modifier = Modifier.height(20.dp))

                    // » Garis pemisah tipis antar baris.
                    HorizontalDivider(color = KastepGray.copy(alpha = 0.1f), thickness = 0.5.dp)
                }
            }
        // » Tutup kartu tabel.
        }

        // » Jarak 16dp.
        Spacer(modifier = Modifier.height(16.dp))

        // » Komentar: navigasi halaman (bawah tabel).
        // Pagination
        // » Row = baris navigasi.
        Row(
            modifier = Modifier
                .fillMaxWidth()
                // » Jarak kiri-kanan 20dp, atas-bawah 12dp.
                .padding(horizontal = 20.dp, vertical = 12.dp),
            // » Isi rata tengah secara horizontal.
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Previous
            // » Tombol "<" = halaman sebelumnya.
            Text(
                text = "<",
                // » Warna: putih kalau bisa mundur, abu transparan kalau tidak.
                color = if (currentPage > 1) KastepWhite else KastepGray.copy(alpha = 0.3f),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    // » clickable(enabled = currentPage > 1): kurangi halaman satu. Mati di halaman 1.
                    .clickable(enabled = currentPage > 1) { currentPage-- }
                    // » Jarak dalam 8dp kiri-kanan (memperbesar area sentuh).
                    .padding(horizontal = 8.dp)
            )

            // » Jarak 8dp.
            Spacer(modifier = Modifier.width(8.dp))

            // Page numbers
            // » for page in 1..min(totalPages, 3) = tampilkan nomor halaman 1 sampai 3 (paling banyak 3).
            for (page in 1..minOf(totalPages, 3)) {
                // » isSelected = true kalau halaman ini sedang dilihat.
                val isSelected = page == currentPage
                // » Box = kotak nomor halaman.
                Box(
                    modifier = Modifier
                        // » Ukuran kotak 32dp.
                        .size(32.dp)
                        // » Sudut membulat 4dp.
                        .clip(RoundedCornerShape(4.dp))
                        // » Latar biru kalau dipilih, transparan kalau tidak.
                        .background(if (isSelected) KastepBlue else Color.Transparent)
                        // » clickable: pindah ke halaman ini.
                        .clickable { currentPage = page },
                    contentAlignment = Alignment.Center
                ) {
                    // » Teks nomor halaman.
                    Text(
                        text = "$page",
                        color = KastepWhite,
                        fontSize = 14.sp,
                        // » Tebal kalau dipilih.
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                    )
                }
                // » Jarak 4dp antar nomor.
                Spacer(modifier = Modifier.width(4.dp))
            }

            // » Kalau halaman lebih dari 3...
            if (totalPages > 3) {
                // » Tampilkan ". . ." sebagai tanda ada halaman lain.
                Text(". . .", color = KastepGray, fontSize = 14.sp)
                Spacer(modifier = Modifier.width(4.dp))
                // » Box = tombol nomor halaman terakhir.
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(RoundedCornerShape(4.dp))
                        // » Biru kalau sedang di halaman terakhir.
                        .background(if (currentPage == totalPages) KastepBlue else Color.Transparent)
                        // » clickable: pindah ke halaman terakhir.
                        .clickable { currentPage = totalPages },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        // » Teks nomor halaman terakhir.
                        text = "$totalPages",
                        color = KastepWhite,
                        fontSize = 14.sp,
                        fontWeight = if (currentPage == totalPages) FontWeight.Bold else FontWeight.Normal
                    )
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Next
            // » Tombol ">" = halaman berikutnya.
            Text(
                text = ">",
                // » Putih kalau masih bisa maju, abu transparan kalau tidak.
                color = if (currentPage < totalPages) KastepWhite else KastepGray.copy(alpha = 0.3f),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    // » clickable(enabled = currentPage < totalPages): tambah halaman satu. Mati di halaman terakhir.
                    .clickable(enabled = currentPage < totalPages) { currentPage++ }
                    .padding(horizontal = 8.dp)
            )
        }

        // » Jarak 8dp di bawah navigasi.
        Spacer(modifier = Modifier.height(8.dp))
    }
}
