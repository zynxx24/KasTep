// ==========================================================================
// PETA FILE · KASTEP (Kelompok 1) · LaporanKasScreen.kt
// Isi file : Laporan Kas: filter periode, total, detail lunas/belum, grafik, bagikan laporan.
// Penanda  : CARI-LAPORAN, CARI-SALDO, CARI-GRAFIK
// Cara cari: VS Code Ctrl+Shift+F (Android Studio Ctrl+Shift+F) lalu ketik
//            "CARI-" untuk semua penanda, atau salah satu nama di baris Penanda.
// ==========================================================================
//  BACA INI DULU : LAYAR "LAPORAN KAS" (tempat GRAFIK KASTEP)
//  Susunan dari atas ke bawah:
//    1. Top bar
//    2. Dropdown "Periode"      Semua Periode atau satu bulan (Juli 2026 dst). Mengubah angka dua kartu di bawahnya.
//    3. Dua kartu               Total Pemasukan dan Total Pengeluaran (bisa DITEKAN untuk melihat rincian)
//    4. Kartu besar             Saldo Akhir (Semua Periode) atau Selisih bulan terpilih
//    5. GRAFIK                  "Grafis Pemasukan & Pengeluaran" = IncomeExpenseChart
//    6. Tombol Bagikan Laporan  kirim ringkasan teks ke WhatsApp atau aplikasi lain
//  Kartu Pemasukan dibuka -> dialog Detail Pemasukan: per bulan, jumlah siswa Lunas dan Belum (NAMA yang belum hanya terlihat ADMIN).
//  Kartu Pengeluaran dibuka -> dialog daftar pengeluaran dan totalnya.
//  JAWABAN CEPAT:
//   - Grafik di mana?   -> IncomeExpenseChart di bawah file ini. Jenis: GRAFIK BATANG BERPASANGAN (biru = pemasukan, abu = pengeluaran) per bulan.
//   - Isi grafik?       -> 6 bulan terakhir yang punya data. Sumbu kiri = rupiah dalam ribuan ("20rb"), sumbu bawah = nama bulan.
//   - Digambar pakai?   -> Canvas manual, tanpa library grafik.
//   - Periode memengaruhi grafik? -> TIDAK. Grafik selalu 6 bulan terakhir, periode hanya mengubah kartu angka.
// » Alamat folder (package) file ini di dalam proyek.
package com.kastep.app.ui.screens

// » Canvas = kanvas kosong untuk menggambar sendiri (garis, lingkaran, grafik).
import androidx.compose.foundation.Canvas
// » background = mengisi warna/gradien latar sebuah tampilan.
import androidx.compose.foundation.background
// » border = garis tepi sebuah tampilan.
import androidx.compose.foundation.border
// » clickable = membuat tampilan bisa diketuk.
import androidx.compose.foundation.clickable
// » Layout dasar: Column (susun atas-bawah), Row (susun kiri-kanan), Box (tumpuk), Spacer (jarak kosong),
// » padding, size, fillMaxWidth, dll.
import androidx.compose.foundation.layout.*
// » rememberScrollState = menyimpan posisi gulir.
import androidx.compose.foundation.rememberScrollState
// » CircleShape = bentuk lingkaran.
import androidx.compose.foundation.shape.CircleShape
// » RoundedCornerShape = bentuk persegi dengan sudut membulat.
import androidx.compose.foundation.shape.RoundedCornerShape
// » verticalScroll = membuat isi bisa digulir naik-turun.
import androidx.compose.foundation.verticalScroll
// » Icons = daftar ikon bawaan Material.
import androidx.compose.material.icons.Icons
// » Ikon bawaan Material: ArrowDropDown (versi terisi).
import androidx.compose.material.icons.filled.ArrowDropDown
// » Ikon bawaan Material: Menu (versi terisi).
import androidx.compose.material.icons.filled.Menu
// » Ikon bawaan Material: Person (versi terisi).
import androidx.compose.material.icons.filled.Person
// » Ikon bawaan Material: Share (versi terisi).
import androidx.compose.material.icons.filled.Share
// » Komponen Material 3 (gaya resmi Android): Text, Button, Card, TextField, Scaffold, dll.
import androidx.compose.material3.*
// » Alat state Compose: remember (mengingat nilai), mutableStateOf (nilai yang bila berubah menggambar ulang
// » layar), LaunchedEffect, dll.
import androidx.compose.runtime.*
// » Alignment = pengatur posisi isi (tengah, atas, bawah, kiri, kanan).
import androidx.compose.ui.Alignment
// » Modifier = daftar 'dekorasi' sebuah tampilan: ukuran, jarak, warna latar, klik, dll.
import androidx.compose.ui.Modifier
// » clip = memotong tampilan mengikuti sebuah bentuk (mis. sudut bulat atau lingkaran).
import androidx.compose.ui.draw.clip
// » Offset = titik koordinat (x, y) di Canvas.
import androidx.compose.ui.geometry.Offset
// » Color = tipe warna Compose; dipakai untuk menulis warna seperti Color.White atau Color(0xFF...).
import androidx.compose.ui.graphics.Color
// » Path = jalur garis yang digambar di Canvas (dipakai garis grafik).
import androidx.compose.ui.graphics.Path
// » StrokeCap = bentuk ujung garis (bulat atau rata).
import androidx.compose.ui.graphics.StrokeCap
// » Stroke = gaya menggambar garis tepi (tebal garis, ujung).
import androidx.compose.ui.graphics.drawscope.Stroke
// » nativeCanvas = kanvas Android asli, dipakai untuk menggambar teks pada grafik.
import androidx.compose.ui.graphics.nativeCanvas
// » FontWeight = ketebalan huruf (Normal, Medium, Bold, ExtraBold).
import androidx.compose.ui.text.font.FontWeight
// » dp = satuan ukuran Android yang sama di semua layar (mis. 16.dp).
import androidx.compose.ui.unit.dp
// » sp = satuan ukuran huruf Android (mengikuti pengaturan ukuran font di HP).
import androidx.compose.ui.unit.sp
// » Memanggil KastepViewModel (dari com.kastep.app.data) supaya bisa dipakai di file ini.
import com.kastep.app.data.KastepViewModel
// » Memanggil StatusBayar (dari com.kastep.app.data) supaya bisa dipakai di file ini.
import com.kastep.app.data.StatusBayar
// » Memanggil UserRole (dari com.kastep.app.data) supaya bisa dipakai di file ini.
import com.kastep.app.data.UserRole
// » Memanggil * (dari com.kastep.app.ui.theme) supaya bisa dipakai di file ini.
import com.kastep.app.ui.theme.*

// » Komentar: variabel penting di layar ini (periode, dialog rincian, dan isAdmin).
// State penting: selectedPeriode (periode terpilih), showPemasukanSheet / showPengeluaranSheet (dialog rincian), isAdmin (menentukan nama yang belum bayar terlihat atau tidak).
// » Komentar: pemasukan dan pengeluaran mengikuti periode yang dipilih. "Semua Periode" menampilkan saldo
// » akhir.
// CARI-LAPORAN CARI-SALDO | Pemasukan & pengeluaran mengikuti periode yang dipilih; Semua Periode menampilkan saldo akhir.
// » @OptIn(ExperimentalMaterial3Api) = izinkan komponen Material 3 yang masih eksperimental.
@OptIn(ExperimentalMaterial3Api::class)
// » @Composable = fungsi penggambar tampilan.
@Composable
// » fun LaporanKasScreen = layar laporan kas (ringkasan, grafik, dan tombol bagikan).
fun LaporanKasScreen(viewModel: KastepViewModel, onOpenDrawer: () -> Unit, onNavigateToProfile: () -> Unit = {}) {
    // » userProfile = akun yang login.
    val userProfile by viewModel.userProfile.collectAsState()
    // » isAdmin = true kalau bendahara.
    val isAdmin = userProfile.role == UserRole.ADMIN
    // » students = daftar siswa.
    val students by viewModel.students.collectAsState()
    // » paymentRecords = semua riwayat pembayaran.
    val paymentRecords by viewModel.paymentRecords.collectAsState()
    // » pengeluaranList = semua pengeluaran.
    val pengeluaranList by viewModel.pengeluaranList.collectAsState()
    // » totalIncome = jumlah semua pembayaran (dari riwayat).
    val totalIncome = paymentRecords.sumOf { it.jumlah }
    // » totalExpense = jumlah semua pengeluaran.
    val totalExpense = pengeluaranList.sumOf { it.jumlah }
    // » saldo = total pemasukan dikurangi total pengeluaran.
    val saldo = totalIncome - totalExpense

    // » selectedPeriode = periode yang dipilih. Awalnya "Semua Periode".
    var selectedPeriode by remember { mutableStateOf("Semua Periode") }
    // » showPeriodeMenu = apakah daftar periode sedang terbuka.
    var showPeriodeMenu by remember { mutableStateOf(false) }
    // » bulanKas = daftar bulan kas (Juli 2026 sampai bulan sekarang).
    val bulanKas = viewModel.getBulanKas()
    // » periodeOptions = pilihan periode: "Semua Periode" lalu semua bulan. + menggabungkan dua daftar.
    val periodeOptions = listOf("Semua Periode") + bulanKas
    // » context = konteks Android.
    val context = androidx.compose.ui.platform.LocalContext.current

    // Menyaring data sesuai periode: "Semua Periode" = semua data. Pilihan bulan = pembayaran bulan itu dan pengeluaran yang tanggalnya di bulan itu.
    // filteredIncome dan filteredExpense = jumlah hasil saringan, ditampilkan di dua kartu.
    // » semua = true kalau periode yang dipilih "Semua Periode".
    val semua = selectedPeriode == "Semua Periode"
    // » periodeYm = pasangan (tahun, bulan) dari teks periode. Dipakai untuk menyaring pengeluaran.
    val periodeYm = com.kastep.app.data.KastepRepository.parseBulan(selectedPeriode)
    // » filteredPayments = kalau semua: semua riwayat. Kalau tidak: riwayat yang kolom bulannya sama dengan
    // » periode.
    val filteredPayments = if (semua) paymentRecords else paymentRecords.filter { it.bulan == selectedPeriode }
    // » filteredExpenses = kalau semua: semua pengeluaran...
    val filteredExpenses = if (semua) pengeluaranList
        // » ...kalau tidak: pengeluaran yang tanggalnya ada di bulan dan tahun periode (bulanDariTanggal).
        else pengeluaranList.filter { com.kastep.app.data.KastepRepository.bulanDariTanggal(it.tanggal) == periodeYm }
    // » filteredIncome = total pemasukan sesuai periode.
    val filteredIncome = filteredPayments.sumOf { it.jumlah }
    // » filteredExpense = total pengeluaran sesuai periode.
    val filteredExpense = filteredExpenses.sumOf { it.jumlah }

    // » showPemasukanSheet = apakah dialog rincian pemasukan terbuka.
    var showPemasukanSheet by remember { mutableStateOf(false) }
    // » showPengeluaranSheet = apakah dialog rincian pengeluaran terbuka.
    var showPengeluaranSheet by remember { mutableStateOf(false) }

    // » Column = susunan atas-bawah seluruh layar. Penuh, latar hitam, bisa digulir.
    Column(
        modifier = Modifier.fillMaxSize().background(KastepBlack).verticalScroll(rememberScrollState())
    ) {
        // Top bar
        // » Row = bar atas (menu, judul, profil).
        Row(
            // » Jarak 16dp kiri-kanan dan 12dp atas-bawah.
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // » IconButton menu: buka menu samping.
            IconButton(onClick = onOpenDrawer) {
                // » Ikon menu (garis tiga), 28dp.
                Icon(Icons.Default.Menu, contentDescription = "Menu", tint = KastepWhite, modifier = Modifier.size(28.dp))
            }
            // » Teks judul "Laporan kas" (18sp, tebal).
            Text("Laporan kas", color = KastepWhite, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            // » Row profil di kanan atas (bisa ditekan ke Profil).
            Row(
                verticalAlignment = Alignment.CenterVertically,
                // » modifier profil.
                modifier = Modifier
                    // » Sudut membulat 20dp (area tekan).
                    .clip(RoundedCornerShape(20.dp))
                    // » clickable: pindah ke Profil.
                    .clickable { onNavigateToProfile() }
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                // » Lingkaran abu 32dp sebagai foto profil.
                Box(modifier = Modifier.size(32.dp).clip(CircleShape).background(KastepGray), contentAlignment = Alignment.Center) {
                    // » Ikon orang di dalam lingkaran.
                    Icon(Icons.Default.Person, contentDescription = "Profile", tint = KastepWhite, modifier = Modifier.size(20.dp))
                }
                Spacer(modifier = Modifier.width(6.dp))
                // » Nama akun.
                Text(userProfile.nama, color = KastepWhite, fontSize = 14.sp, fontWeight = FontWeight.Medium)
            }
        }

        // » Komentar: dropdown pilihan periode.
        // Dropdown pilihan periode. Isinya "Semua Periode" ditambah semua bulan tagihan (Juli 2026 sampai sekarang).
        // Periode dropdown - CLICKABLE
        // » Row = label "Periode" dan kotak pilihan.
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // » Label "Periode".
            Text("Periode", color = KastepWhite, fontSize = 14.sp)
            Spacer(modifier = Modifier.width(12.dp))
            // » ExposedDropdownMenuBox = kotak pilihan periode. onExpandedChange = buka/tutup daftar.
            ExposedDropdownMenuBox(expanded = showPeriodeMenu, onExpandedChange = { showPeriodeMenu = it }) {
                // » Box = kotak tampilan periode terpilih.
                Box(
                    // » weight(1f) = lebar penuh sisa. menuAnchor = tempat daftar menempel.
                    modifier = Modifier.weight(1f).menuAnchor()
                        // » Garis tepi tipis abu, sudut 8dp.
                        .border(1.dp, KastepGray.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                        // » Jarak dalam 12dp kiri-kanan, 10dp atas-bawah.
                        .padding(horizontal = 12.dp, vertical = 10.dp)
                ) {
                    // » Row isi kotak: teks periode di kiri, panah di kanan.
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        // » Teks periode yang dipilih.
                        Text(selectedPeriode, color = KastepWhite, fontSize = 14.sp)
                        // » Ikon panah turun (ArrowDropDown).
                        Icon(Icons.Default.ArrowDropDown, contentDescription = "Dropdown", tint = KastepWhite)
                    }
                }
                // » ExposedDropdownMenu = daftar pilihan periode.
                ExposedDropdownMenu(expanded = showPeriodeMenu, onDismissRequest = { showPeriodeMenu = false }) {
                    // » Untuk setiap pilihan periode...
                    periodeOptions.forEach { option ->
                        // » DropdownMenuItem = satu baris pilihan.
                        DropdownMenuItem(text = { Text(option) }, onClick = {
                            // » Saat dipilih: simpan periode dan tutup daftar.
                            selectedPeriode = option; showPeriodeMenu = false
                        })
                    }
                }
            }
        }

        // » Jarak 16dp.
        Spacer(modifier = Modifier.height(16.dp))

        // Filtered data based on period

        // » Komentar: dua kartu angka (bisa ditekan) dan kartu saldo besar di bawahnya.
        // Dua kartu angka (ditekan = buka dialog rincian). Kartu besar di bawahnya: Saldo Akhir bila Semua Periode, bila satu bulan jadi "Selisih <bulan>" (pemasukan dikurangi pengeluaran, bisa minus).
        // Summary cards — CLICKABLE
        // » Row = dua kartu berdampingan, jarak 8dp.
        Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            // » ReportSummaryCard = kartu ringkasan (dibuat di bawah). Ini kartu pemasukan.
            ReportSummaryCard(
                // » weight(1f): lebar sama. clickable: tekan = buka rincian pemasukan.
                modifier = Modifier.weight(1f).clickable { showPemasukanSheet = true },
                // » label "Total Pemasukan". amount = pemasukan periode yang diformat rupiah.
                label = "Total Pemasukan", amount = "Rp ${KastepViewModel.formatRupiah(filteredIncome)}",
                // » Warna garis, label, dan ikon koin: biru.
                borderColor = KastepBlue, labelColor = KastepBlue, coinColor = KastepBlue
            )
            // » Kartu pengeluaran.
            ReportSummaryCard(
                // » clickable: tekan = buka rincian pengeluaran.
                modifier = Modifier.weight(1f).clickable { showPengeluaranSheet = true },
                // » label "Total Pengeluaran", amount = pengeluaran periode yang diformat rupiah.
                label = "Total Pengeluaran", amount = "Rp ${KastepViewModel.formatRupiah(filteredExpense)}",
                // » Warna merah.
                borderColor = KastepRed, labelColor = KastepRed, coinColor = KastepRed
            )
        }

        // » Jarak 8dp.
        Spacer(modifier = Modifier.height(8.dp))
        // » Petunjuk kecil: "Tap kartu untuk melihat detail".
        Text("Tap kartu untuk melihat detail", color = KastepGray, fontSize = 11.sp, modifier = Modifier.padding(horizontal = 20.dp))

        // » Jarak 12dp.
        Spacer(modifier = Modifier.height(12.dp))

        // » Kartu besar: saldo akhir atau selisih periode.
        ReportSummaryCard(
            // » Lebar penuh dengan jarak kiri-kanan 16dp.
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            // » label: "Saldo Akhir" kalau semua periode. Kalau tidak: "Selisih <periode>".
            label = if (semua) "Saldo Akhir" else "Selisih $selectedPeriode",
            // » amount: kalau periode bulanan dan selisih negatif, pakai tanda "-Rp ". Lalu nilai selisihnya tanpa tanda
            // » minus (abs), diformat rupiah.
            amount = (if (!semua && filteredIncome - filteredExpense < 0) "-Rp " else "Rp ") + KastepViewModel.formatRupiah(kotlin.math.abs(if (semua) saldo else filteredIncome - filteredExpense)),
            // » Warna hijau.
            borderColor = KastepGreen, labelColor = KastepGreen, coinColor = KastepGreen
        )

        // » Jarak 24dp.
        Spacer(modifier = Modifier.height(24.dp))

        // » Komentar: kartu grafik berisi judul, legenda, dan grafik setinggi 220dp.
        // GRAFIK: kartu gelap berisi judul, legenda (kotak biru = Pemasukan, kotak abu = Pengeluaran), lalu IncomeExpenseChart setinggi 220.dp.
        // Chart card
        // » Card = kartu grafik.
        Card(
            // » Penuh lebar, jarak kiri-kanan 16dp.
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            // » Sudut membulat 20dp.
            shape = RoundedCornerShape(20.dp),
            // » Warna biru tua (#1A1A2E).
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A2E))
        ) {
            // » Column isi kartu, jarak dalam 20dp.
            Column(modifier = Modifier.padding(20.dp)) {
                // » Judul "Grafis Pemasukan & Pengeluaran" (16sp, tebal).
                Text("Grafis Pemasukan & Pengeluaran", color = KastepWhite, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                // » Jarak 12dp di bawah judul.
                Spacer(modifier = Modifier.height(12.dp))
                // » Row = legenda grafik, jaraknya 24dp.
                Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                    // » Satu item legenda: pemasukan.
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        // » Kotak biru 14dp = warna batang pemasukan.
                        Box(modifier = Modifier.size(14.dp).background(KastepBlue))
                        Spacer(modifier = Modifier.width(6.dp))
                        // » Teks "Pemasukan".
                        Text("Pemasukan", color = KastepWhite, fontSize = 12.sp)
                    }
                    // » Satu item legenda: pengeluaran.
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        // » Kotak abu 14dp = warna batang pengeluaran.
                        Box(modifier = Modifier.size(14.dp).background(KastepGray))
                        Spacer(modifier = Modifier.width(6.dp))
                        // » Teks "Pengeluaran".
                        Text("Pengeluaran", color = KastepWhite, fontSize = 12.sp)
                    }
                }
                // » Jarak 16dp di bawah legenda.
                Spacer(modifier = Modifier.height(16.dp))
                // Menentukan bulan yang digambar: kumpulkan semua bulan yang punya pembayaran atau pengeluaran, buang yang kembar (distinct),
                // urutkan dari yang paling lama, ambil 6 terakhir (takeLast(6)). Lalu per bulan dijumlahkan pemasukan dan pengeluarannya.
                // Data grafik: pemasukan & pengeluaran nyata per bulan
                // » bulanGrafik = kumpulkan bulan dari riwayat pembayaran (parseBulan) dan dari pengeluaran
                // » (bulanDariTanggal). mapNotNull = buang yang gagal dibaca.
                val bulanGrafik = (paymentRecords.mapNotNull { com.kastep.app.data.KastepRepository.parseBulan(it.bulan) } +
                    // » Gabungkan kedua daftar bulan jadi satu.
                    pengeluaranList.mapNotNull { com.kastep.app.data.KastepRepository.bulanDariTanggal(it.tanggal) })
                    // » distinct = hapus bulan yang kembar. sortedBy = urutkan dari bulan paling lama. takeLast(6) = ambil 6
                    // » bulan terakhir saja.
                    .distinct().sortedBy { it.first * 12 + it.second }.takeLast(6)
                // » IncomeExpenseChart = grafik batang (digambar di bagian bawah file ini).
                IncomeExpenseChart(
                    // » labels = nama singkat bulan (Jan, Feb, ..., Des) sesuai bulan di bulanGrafik. second - 1 karena indeks
                    // » mulai 0.
                    labels = bulanGrafik.map { listOf("Jan", "Feb", "Mar", "Apr", "Mei", "Jun", "Jul", "Agu", "Sep", "Okt", "Nov", "Des")[it.second - 1] },
                    // » income = total pemasukan tiap bulan di bulanGrafik (filter riwayat yang bulannya sama).
                    income = bulanGrafik.map { ym -> paymentRecords.filter { com.kastep.app.data.KastepRepository.parseBulan(it.bulan) == ym }.sumOf { it.jumlah } },
                    // » expense = total pengeluaran tiap bulan di bulanGrafik (filter pengeluaran yang bulannya sama).
                    expense = bulanGrafik.map { ym -> pengeluaranList.filter { com.kastep.app.data.KastepRepository.bulanDariTanggal(it.tanggal) == ym }.sumOf { it.jumlah } },
                    // » Ukuran grafik: penuh lebar, tinggi 220dp.
                    modifier = Modifier.fillMaxWidth().height(220.dp)
                )
            }
        }

        // » Jarak 16dp.
        Spacer(modifier = Modifier.height(16.dp))

        // » Komentar: tombol Bagikan membuat teks laporan lalu menampilkan pilihan aplikasi.
        // Tombol Bagikan Laporan: membuat Intent ACTION_SEND berisi teks dari viewModel.generateReportText(), lalu menampilkan pilihan aplikasi (WhatsApp, dll).
        // Bagikan laporan ringkas ke WhatsApp / aplikasi lain
        // » Button = tombol Bagikan Laporan.
        Button(
            // » onClick: buat dan kirim laporan.
            onClick = {
                // » Intent ACTION_SEND = niat kirim data. apply = isi pengaturannya di blok ini.
                val intent = android.content.Intent(android.content.Intent.ACTION_SEND).apply {
                    // » type = "text/plain": isi yang dikirim berupa teks biasa.
                    type = "text/plain"
                    // » EXTRA_TEXT = isi teks laporan (dari generateReportText).
                    putExtra(android.content.Intent.EXTRA_TEXT, viewModel.generateReportText())
                }
                // » createChooser = tampilkan daftar aplikasi untuk memilih (WhatsApp, dll). startActivity = buka daftar
                // » itu.
                context.startActivity(android.content.Intent.createChooser(intent, "Bagikan laporan kas"))
            },
            // » modifier: penuh lebar, jarak 16dp kiri-kanan, tinggi 48dp.
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).height(48.dp),
            // » Warna tombol biru.
            colors = ButtonDefaults.buttonColors(containerColor = KastepBlue),
            // » Sudut membulat 14dp.
            shape = RoundedCornerShape(14.dp)
        ) {
            // » Ikon Share (bagikan).
            Icon(Icons.Default.Share, contentDescription = null, tint = KastepWhite, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(8.dp))
            // » Teks "Bagikan Laporan" (tebal).
            Text("Bagikan Laporan", color = KastepWhite, fontWeight = FontWeight.Bold)
        }

        // » Jarak 32dp di bawah.
        Spacer(modifier = Modifier.height(32.dp))
    }

    // » Komentar: dialog rincian pemasukan. Nama siswa yang belum bayar hanya tampil untuk bendahara.
    // Dialog Detail Pemasukan. Per bulan: jumlah siswa LUNAS dan BELUM. Daftar nama yang belum (dengan kekurangan dan denda) hanya tampil bila isAdmin.
    // Akun USER hanya melihat jumlahnya supaya yang menunggak tidak dipermalukan.
    // Pemasukan Bottom Sheet - shows paid/unpaid students
    // » Kalau showPemasukanSheet true, tampilkan dialog rincian pemasukan.
    if (showPemasukanSheet) {
        // » AlertDialog = kotak dialog rincian.
        AlertDialog(
            // » Tekan di luar kotak = tutup dialog.
            onDismissRequest = { showPemasukanSheet = false },
            // » Judul "Detail Pemasukan - <periode>".
            title = { Text("Detail Pemasukan - $selectedPeriode", fontWeight = FontWeight.Bold, fontSize = 16.sp) },
            text = {
                // » Column isi dialog, bisa digulir, tinggi maksimal 400dp.
                Column(modifier = Modifier.verticalScroll(rememberScrollState()).heightIn(max = 400.dp)) {
                    // » daftarBulan = kalau Semua Periode, semua bulan kas. Kalau tidak, hanya periode yang dipilih.
                    val daftarBulan = if (semua) bulanKas else listOf(selectedPeriode)
                    // » Untuk setiap bulan (b) dalam daftar...
                    daftarBulan.forEach { b ->
                        // » lunas = siswa yang statusnya LUNAS di bulan b.
                        val lunas = students.filter { viewModel.statusBulan(it, b) == StatusBayar.LUNAS }
                        // » belum = siswa yang statusnya BELUM di bulan b.
                        val belum = students.filter { viewModel.statusBulan(it, b) == StatusBayar.BELUM }
                        // » Judul bulan dengan ikon kalender (biru, tebal).
                        Text("📅 $b", fontWeight = FontWeight.Bold, color = KastepBlue, fontSize = 14.sp)
                        Spacer(modifier = Modifier.height(4.dp))
                        // » Jumlah siswa yang lunas (hijau).
                        Text("✅ Lunas (${lunas.size})", fontWeight = FontWeight.Medium, color = KastepGreen, fontSize = 13.sp)
                        // » Jumlah siswa yang belum (merah). Kalau bendahara, ditambah ":" tanda daftar namanya menyusul.
                        Text("❌ Belum (${belum.size})" + if (isAdmin) ":" else "", fontWeight = FontWeight.Medium, color = KastepRed, fontSize = 13.sp)
                        // Privasi: nama yang belum bayar hanya untuk ADMIN, USER cukup melihat jumlahnya
                        // » Kalau bendahara: tampilkan nama setiap siswa yang belum bayar. Siswa biasa tidak melihat nama.
                        if (isAdmin) belum.forEach {
                            // » denda = denda siswa itu di bulan b.
                            val denda = viewModel.calculateDenda(it.nama, b)
                            Text(
                                // » Baris: nama siswa, lalu kekurangan kas (sisaKas) bulan itu.
                                "  • ${it.nama} — kurang Rp ${KastepViewModel.formatRupiah(viewModel.sisaKas(it, b))}" +
                                    // » Kalau ada denda, tambahkan "+ denda Rp ..." di belakang.
                                    if (denda > 0) " + denda Rp ${KastepViewModel.formatRupiah(denda)}" else "",
                                fontSize = 12.sp
                            )
                        }
                        // » Jarak 12dp antar bulan.
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }
            },
            // » Tombol Tutup: sembunyikan dialog.
            confirmButton = { TextButton(onClick = { showPemasukanSheet = false }) { Text("Tutup") } }
        )
    }

    // Dialog Detail Pengeluaran: daftar keterangan, tanggal, nominal, dan total untuk periode terpilih.
    // Pengeluaran Bottom Sheet - shows expense list
    // » Kalau showPengeluaranSheet true, tampilkan dialog rincian pengeluaran.
    if (showPengeluaranSheet) {
        // » AlertDialog = kotak dialog rincian pengeluaran.
        AlertDialog(
            // » Tekan di luar kotak = tutup.
            onDismissRequest = { showPengeluaranSheet = false },
            // » Judul "Detail Pengeluaran - <periode>".
            title = { Text("Detail Pengeluaran - $selectedPeriode", fontWeight = FontWeight.Bold, fontSize = 16.sp) },
            text = {
                // » Column isi dialog, bisa digulir, tinggi maksimal 400dp.
                Column(modifier = Modifier.verticalScroll(rememberScrollState()).heightIn(max = 400.dp)) {
                    // » Kalau tidak ada pengeluaran pada periode ini...
                    if (filteredExpenses.isEmpty()) {
                        // » ...tampilkan "Belum ada pengeluaran." abu.
                        Text("Belum ada pengeluaran.", color = Color.Gray)
                    } else {
                        // » Untuk setiap pengeluaran: idx = nomor urut (mulai 0), p = datanya.
                        filteredExpenses.forEachIndexed { idx, p ->
                            // » Row satu baris pengeluaran: penuh lebar, jarak 4dp, label kiri dan jumlah kanan.
                            Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                                // » Column kiri (keterangan dan tanggal), mengisi sisa lebar.
                                Column(modifier = Modifier.weight(1f)) {
                                    // » Teks nomor dan keterangan. idx + 1 = nomor mulai dari 1.
                                    Text("${idx + 1}. ${p.keterangan}", fontSize = 13.sp, fontWeight = FontWeight.Medium)
                                    // » Tanggal pengeluaran (abu, kecil).
                                    Text(p.tanggal, fontSize = 11.sp, color = Color.Gray)
                                }
                                // » Jumlah pengeluaran (merah, tebal).
                                Text("Rp ${KastepViewModel.formatRupiah(p.jumlah)}", color = KastepRed, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                            }
                            // » Kalau bukan baris terakhir, beri garis pemisah.
                            if (idx < filteredExpenses.size - 1) HorizontalDivider(modifier = Modifier.padding(vertical = 2.dp))
                        }
                        // » Jarak 8dp.
                        Spacer(modifier = Modifier.height(8.dp))
                        // » Total pengeluaran periode ini (tebal).
                        Text("Total: Rp ${KastepViewModel.formatRupiah(filteredExpense)}", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    }
                }
            },
            // » Tombol Tutup: sembunyikan dialog.
            confirmButton = { TextButton(onClick = { showPengeluaranSheet = false }) { Text("Tutup") } }
        )
    }
}

// Kartu bergaris warna berisi ikon koin, label, dan angka (dipakai Pemasukan, Pengeluaran, dan Saldo).
// Kartu Total Pemasukan / Total Pengeluaran, bisa ditekan untuk melihat rincian.
@Composable
// » private fun ReportSummaryCard = kartu ringkasan (label, angka, ikon koin). Hanya dipakai di file ini.
private fun ReportSummaryCard(modifier: Modifier = Modifier, label: String, amount: String, borderColor: Color, labelColor: Color, coinColor: Color) {
    // » Box dengan garis tepi berwarna (1.5dp), sudut 12dp, dan jarak dalam 16dp.
    Box(modifier = modifier.border(1.5.dp, borderColor, RoundedCornerShape(12.dp)).padding(16.dp)) {
        // » Row: ikon koin di kiri, teks di kanan.
        Row(verticalAlignment = Alignment.CenterVertically) {
            // » CoinStackIcon = ikon tumpukan koin dengan warna yang diberikan.
            CoinStackIcon(color = coinColor)
            // » Jarak 12dp.
            Spacer(modifier = Modifier.width(12.dp))
            // » Column = label di atas, angka di bawah.
            Column {
                // » Label (11sp) dengan warna label.
                Text(label, color = labelColor, fontSize = 11.sp, fontWeight = FontWeight.Medium)
                // » Jarak 4dp.
                Spacer(modifier = Modifier.height(4.dp))
                // » Angka (16sp, tebal, putih).
                Text(amount, color = KastepWhite, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

// » Komentar: ikon tumpukan koin dibuat dengan Canvas.
// Ikon tumpukan koin digambar dengan Canvas.
// » @Composable = fungsi penggambar tampilan.
@Composable
// » private fun CoinStackIcon = ikon tumpukan koin (hanya dipakai di file ini).
private fun CoinStackIcon(color: Color) {
    // » Canvas 36dp = area gambar ikon.
    Canvas(modifier = Modifier.size(36.dp)) {
        // » coinWidth = lebar koin 70% area.
        val coinWidth = size.width * 0.7f
        // » coinHeight = tinggi satu koin 15% area.
        val coinHeight = size.height * 0.15f
        // » startX = posisi kiri koin 15% area.
        val startX = size.width * 0.15f
        // » for i in 0..2 = gambar tiga koin.
        for (i in 0..2) {
            // » y = posisi vertikal koin ke-i (ditumpuk dengan jarak 4dp).
            val y = size.height * 0.3f + i * (coinHeight + 4.dp.toPx())
            // » drawRoundRect = gambar satu koin persegi membulat.
            drawRoundRect(color, Offset(startX, y), androidx.compose.ui.geometry.Size(coinWidth, coinHeight), androidx.compose.ui.geometry.CornerRadius(4.dp.toPx()))
            // » drawLine = garis tipis di tengah koin (kilau), transparansi 50%.
            drawLine(color.copy(alpha = 0.5f), Offset(startX + coinWidth * 0.2f, y + coinHeight * 0.5f), Offset(startX + coinWidth * 0.8f, y + coinHeight * 0.5f), 1.dp.toPx())
        }
    }
}

// =====================  GRAFIK KAS KASTEP  =====================
// INI KODE YANG MENGGAMBAR GRAFIK. Jenis: GRAFIK BATANG BERPASANGAN. Digambar manual di Canvas, tanpa library.
// Input: labels (nama bulan), income (pemasukan per bulan), expense (pengeluaran per bulan), maksimal 6 bulan.
// LANGKAH GAMBAR:
//   1. maxValue = nilai terbesar dari semua pemasukan dan pengeluaran (minimal 1). Semua tinggi batang dibandingkan dengan angka ini.
//   2. Tentukan area gambar: chartLeft 44.dp disisakan untuk tulisan sumbu Y, chartBottom disisakan untuk nama bulan.
//   3. 5 garis bantu mendatar (i dari 0 sampai 4) dengan label ribuan di kiri, contoh "0rb", "20rb", "40rb".
//   4. slot = lebar satu bulan = lebar area dibagi jumlah bulan. barW = lebar batang (maksimal 18.dp).
//   5. Tiap bulan: batang BIRU (pemasukan, tinggi = lebar area x pemasukan/maxValue) di kiri, batang ABU (pengeluaran) di kanan, nama bulan di bawahnya.
//   6. Garis dasar di paling bawah.
// Batang paling tinggi = bulan dengan nilai terbesar. Bulan tanpa data tidak digambar.
// CARI-GRAFIK | Grafik batang 6 bulan terakhir: biru = pemasukan, abu = pengeluaran.
// » @Composable = fungsi penggambar tampilan.
@Composable
// » private fun IncomeExpenseChart = grafik batang pemasukan dan pengeluaran. labels = nama bulan, income =
// » pemasukan, expense = pengeluaran.
private fun IncomeExpenseChart(labels: List<String>, income: List<Long>, expense: List<Long>, modifier: Modifier = Modifier) {
    // » maxValue = nilai terbesar dari semua pemasukan dan pengeluaran (minimal 1). Dipakai sebagai patokan
    // » tinggi batang. ?: 1f kalau tidak ada data.
    val maxValue = (income + expense).maxOrNull()?.coerceAtLeast(1L)?.toFloat() ?: 1f
    // » Canvas = area gambar grafik.
    Canvas(modifier = modifier) {
        // » Kalau tidak ada bulan, berhenti (return@Canvas).
        if (labels.isEmpty()) return@Canvas
        // » chartLeft = margin kiri 44dp (ruang angka sumbu). chartBottom = tinggi dikurangi 26dp (ruang nama
        // » bulan).
        val chartLeft = 44.dp.toPx(); val chartBottom = size.height - 26.dp.toPx()
        // » chartTop = margin atas 10dp. chartRight = lebar dikurangi 10dp.
        val chartTop = 10.dp.toPx(); val chartRight = size.width - 10.dp.toPx()
        // » chartWidth = lebar area batang. chartHeight = tinggi area batang.
        val chartWidth = chartRight - chartLeft; val chartHeight = chartBottom - chartTop

        // » paint = alat tulis teks di canvas Android.
        val paint = android.graphics.Paint().apply {
            // » Warna teks abu (#9E9E9E), ukuran 10sp, dengan anti-alias (tepi halus).
            color = android.graphics.Color.parseColor("#9E9E9E"); textSize = 10.sp.toPx(); isAntiAlias = true
        }
        // Garis bantu sumbu Y (dalam ribuan rupiah)
        // » Untuk i 0 sampai 4 = lima garis bantu mendatar.
        for (i in 0..4) {
            // » v = nilai sumbu di garis ke-i (maxValue dibagi 4 dikali i).
            val v = maxValue * i / 4
            // » y = posisi vertikal garis ke-i.
            val y = chartBottom - chartHeight * i / 4
            // » Tulis label sumbu: nilai dibagi 1000 lalu "rb" (contoh "20rb").
            drawContext.canvas.nativeCanvas.drawText("${(v / 1000).toInt()}rb", 2.dp.toPx(), y + 4.dp.toPx(), paint)
            // » Gambar garis bantu abu gelap tipis.
            drawLine(Color(0xFF333333), Offset(chartLeft, y), Offset(chartRight, y), 0.5.dp.toPx())
        }
        // » slot = lebar satu bulan = lebar area dibagi jumlah bulan.
        val slot = chartWidth / labels.size
        // » barW = lebar satu batang = 32% slot, maksimal 18dp.
        val barW = (slot * 0.32f).coerceAtMost(18.dp.toPx())
        // » Untuk setiap bulan (label, indeks i)...
        labels.forEachIndexed { i, label ->
            // » cx = posisi tengah bulan ke-i.
            val cx = chartLeft + slot * i + slot / 2
            // » hIn = tinggi batang pemasukan: proporsional terhadap maxValue.
            val hIn = chartHeight * (income[i] / maxValue)
            // » hOut = tinggi batang pengeluaran.
            val hOut = chartHeight * (expense[i] / maxValue)
            // » Gambar batang biru (pemasukan) di sisi kiri tengah.
            drawRect(Color(0xFF4A90D9), Offset(cx - barW - 1.dp.toPx(), chartBottom - hIn), androidx.compose.ui.geometry.Size(barW, hIn))
            // » Gambar batang abu (pengeluaran) di sisi kanan tengah.
            drawRect(Color(0xFF9E9E9E), Offset(cx + 1.dp.toPx(), chartBottom - hOut), androidx.compose.ui.geometry.Size(barW, hOut))
            // » Tulis nama bulan di bawah grafik.
            drawContext.canvas.nativeCanvas.drawText(label, cx - 9.dp.toPx(), size.height - 6.dp.toPx(), paint)
        }
        // » Gambar garis dasar grafik (abu).
        drawLine(Color(0xFF555555), Offset(chartLeft, chartBottom), Offset(chartRight, chartBottom), 1.dp.toPx())
    }
}
