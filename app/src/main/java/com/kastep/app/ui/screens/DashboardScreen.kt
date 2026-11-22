// ==========================================================================
// PETA FILE · KASTEP (Kelompok 1) · DashboardScreen.kt
// Isi file : Dashboard: saldo, pemasukan, pengeluaran, transaksi terbaru.
// Penanda  : CARI-SALDO
// Cara cari: VS Code Ctrl+Shift+F (Android Studio Ctrl+Shift+F) lalu ketik
//            "CARI-" untuk semua penanda, atau salah satu nama di baris Penanda.
// ==========================================================================
//  BACA INI DULU : ISI DASHBOARD DARI ATAS KE BAWAH
//    1. TopBar              tombol menu samping, tanggal hari ini, nama akun (menuju Profil)
//    2. Judul Dashboard     dengan label peran Admin (biru) atau User (abu)
//    3. Tiga kartu          Saldo saat ini, Pemasukan, Pengeluaran
//    4. Garis progres       panjang = pengeluaran dibagi pemasukan (seberapa banyak uang masuk yang sudah terpakai)
//    5. Transaksi Terbaru   10 transaksi terbaru, gabungan pembayaran (hijau +) dan pengeluaran (merah -)
//  Dashboard TIDAK punya grafik. Grafik KASTEP ada di layar Laporan Kas (fungsi IncomeExpenseChart di LaporanKasScreen.kt).
//  Semua angka berasal dari KastepViewModel (saldo, totalIncome, totalExpense).
// » package com.kastep.app.ui.screens = alamat folder file ini (kumpulan layar).
package com.kastep.app.ui.screens

// » background = mengisi warna/gradien latar sebuah tampilan.
import androidx.compose.foundation.background
// » clickable = membuat tampilan bisa diketuk.
import androidx.compose.foundation.clickable
// » Layout dasar: Column (susun atas-bawah), Row (susun kiri-kanan), Box (tumpuk), Spacer (jarak kosong),
// » padding, size, fillMaxWidth, dll.
import androidx.compose.foundation.layout.*
// » LazyColumn = daftar vertikal yang hanya menggambar baris yang terlihat (hemat memori).
import androidx.compose.foundation.lazy.LazyColumn
// » items = mengulang satu tampilan untuk setiap elemen daftar.
import androidx.compose.foundation.lazy.items
// » CircleShape = bentuk lingkaran.
import androidx.compose.foundation.shape.CircleShape
// » RoundedCornerShape = bentuk persegi dengan sudut membulat.
import androidx.compose.foundation.shape.RoundedCornerShape
// » Icons = daftar ikon bawaan Material.
import androidx.compose.material.icons.Icons
// » Ikon bawaan Material: * (versi terisi).
import androidx.compose.material.icons.filled.*
// » Komponen Material 3 (gaya resmi Android): Text, Button, Card, TextField, Scaffold, dll.
import androidx.compose.material3.*
// » @Composable = penanda bahwa sebuah fungsi menggambar tampilan.
import androidx.compose.runtime.Composable
// » Memanggil collectAsState (dari androidx.compose.runtime) supaya bisa dipakai di file ini.
import androidx.compose.runtime.collectAsState
// » Memanggil getValue (dari androidx.compose.runtime) supaya bisa dipakai di file ini.
import androidx.compose.runtime.getValue
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
// » Memanggil UserRole (dari com.kastep.app.data) supaya bisa dipakai di file ini.
import com.kastep.app.data.UserRole
// » Memanggil * (dari com.kastep.app.ui.theme) supaya bisa dipakai di file ini.
import com.kastep.app.ui.theme.*

// » Komentar: judul penanda pencarian (CARI-SALDO).
// CARI-SALDO | Tiga kartu ringkasan + 10 transaksi terbaru (diurutkan menurut tanggal).
// » @Composable = fungsi penggambar tampilan. Layar Dashboard.
@Composable
// » fun DashboardScreen = fungsi layar utama setelah login.
fun DashboardScreen(
    // » viewModel = sumber data (saldo, transaksi).
    viewModel: KastepViewModel,
    // » onNavigateToProfile = fungsi dipanggil saat nama akun ditekan.
    onNavigateToProfile: () -> Unit,
    // » onOpenDrawer = fungsi membuka menu samping. Default {} = tidak melakukan apa-apa.
    onOpenDrawer: () -> Unit = {}
) {
    // » paymentRecords = daftar pembayaran, dibaca lewat collectAsState (layar ikut berubah saat data berubah).
    val paymentRecords by viewModel.paymentRecords.collectAsState()
    // » pengeluaranList = daftar pengeluaran.
    val pengeluaranList by viewModel.pengeluaranList.collectAsState()
    // » userProfile = data akun yang sedang login.
    val userProfile by viewModel.userProfile.collectAsState()
    // » currentDate = tanggal hari ini sebagai teks (mis. "23 November 2026").
    val currentDate = viewModel.getCurrentDateString()

    // Bentuk satu baris transaksi gabungan untuk daftar "Transaksi Terbaru".
    // Merge payments and expenses into a recent transaction list
    // » data class RecentTx = cetakan satu baris transaksi gabungan. Hanya dipakai di fungsi ini.
    data class RecentTx(val title: String, val date: String, val amount: Long, val isIncome: Boolean)
    // Gabungkan pembayaran dan pengeluaran jadi satu daftar, urutkan dari tanggal terbaru (kunciTanggal), ambil 10.
    // » recentTxList = daftar transaksi gabungan: pembayaran + pengeluaran.
    val recentTxList = (
        // » Setiap pembayaran jadi baris: judul "Pembayaran - nama", tanggal, jumlah, bertanda pemasukan (true).
        paymentRecords.map { RecentTx("Pembayaran - ${it.namaSiswa}", it.tanggal, it.jumlah, true) } +
        // » Setiap pengeluaran jadi baris: judul = keterangan, bertanda pengeluaran (false).
        pengeluaranList.map { RecentTx(it.keterangan, it.tanggal, it.jumlah, false) }
    // » sortedByDescending = urutkan dari tanggal paling baru. take(10) = ambil 10 teratas.
    ).sortedByDescending { com.kastep.app.data.KastepRepository.kunciTanggal(it.date) }.take(10)

    // » Column = susunan atas-bawah untuk seluruh layar. fillMaxSize = penuh. background = latar hitam.
    Column(modifier = Modifier.fillMaxSize().background(KastepBlack)) {
        // Top bar
        // » TopBar = bar atas: tombol menu, tanggal, nama akun. Diberi data dari sini.
        TopBar(currentDate = currentDate, userName = userProfile.nama, onMenuClick = onOpenDrawer, onNavigateToProfile = onNavigateToProfile)

        // Dashboard title + role badge
        // » Column dengan jarak kiri-kanan 20dp untuk judul.
        Column(modifier = Modifier.padding(horizontal = 20.dp)) {
            // » Row = susunan mendatar untuk judul dan label peran.
            Row(verticalAlignment = Alignment.CenterVertically) {
                // » Box = kotak pembungkus teks "Dashboard" dengan latar.
                Box(
                    // » Latar hijau transparan 20%, sudut membulat 4dp.
                    modifier = Modifier.background(KastepGreen.copy(alpha = 0.2f), RoundedCornerShape(4.dp))
                        // » Jarak dalam kotak: 8dp kiri-kanan, 2dp atas-bawah.
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    // » Teks "Dashboard": hijau, 20sp, tebal.
                    Text("Dashboard", color = KastepGreen, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                }
                // » Jarak 8dp antara judul dan label peran.
                Spacer(modifier = Modifier.width(8.dp))
                // » Box = kotak label peran (Admin/User).
                Box(
                    // » background = latar label. Warna tergantung peran.
                    modifier = Modifier.background(
                        // » Kalau ADMIN: latar cyan transparan. Kalau bukan: latar abu transparan.
                        if (userProfile.role == UserRole.ADMIN) KastepCyan.copy(alpha = 0.2f) else KastepGray.copy(alpha = 0.2f),
                        RoundedCornerShape(4.dp)
                    // » Jarak dalam label 6dp kiri-kanan, 2dp atas-bawah.
                    ).padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    // » Text = tulisan peran.
                    Text(
                        // » Teks "Admin" kalau ADMIN, selain itu "User".
                        if (userProfile.role == UserRole.ADMIN) "Admin" else "User",
                        // » Warna teks: cyan untuk Admin, abu untuk User.
                        color = if (userProfile.role == UserRole.ADMIN) KastepCyan else KastepGray,
                        // » Ukuran 12sp, tebal.
                        fontSize = 12.sp, fontWeight = FontWeight.Bold
                    )
                }
            }
            // » Jarak 4dp.
            Spacer(modifier = Modifier.height(4.dp))
            // » Teks keterangan kecil di bawah judul: "Ringkasan informasi kas kelas".
            Text("Ringkasan informasi kas kelas", color = KastepGray, fontSize = 13.sp)
        }

        // » Jarak 16dp sebelum kartu ringkasan.
        Spacer(modifier = Modifier.height(16.dp))

        // Tiga kartu ringkasan sama lebar (weight 1f). Angka dari viewModel.saldo, totalIncome, totalExpense.
        // Summary cards
        // » Row = tiga kartu ringkasan berdampingan.
        Row(
            // » Penuh lebar dengan jarak kiri-kanan 16dp.
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            // » Jarak antar kartu 8dp.
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // » Kartu Saldo saat ini: angka dari viewModel.saldo, diformat rupiah, ikon panah tukar, warna cyan.
            SummaryCard(Modifier.weight(1f), "Saldo saat ini", "RP ${KastepViewModel.formatRupiah(viewModel.saldo)}", Icons.Default.SwapVert, KastepCyan)
            // » Kartu Pemasukan: angka totalIncome, ikon panah ke bawah, warna hijau.
            SummaryCard(Modifier.weight(1f), "Pemasukan", "RP ${KastepViewModel.formatRupiah(viewModel.totalIncome)}", Icons.Default.ArrowDownward, KastepGreen)
            // » Kartu Pengeluaran: angka totalExpense, ikon panah ke atas, warna merah.
            SummaryCard(Modifier.weight(1f), "Pengeluaran", "RP ${KastepViewModel.formatRupiah(viewModel.totalExpense)}", Icons.Default.ArrowUpward, KastepRed)
        // » Akhir Row kartu.
        }

        // » Jarak 16dp.
        Spacer(modifier = Modifier.height(16.dp))

        // Garis progres: progress = pengeluaran / pemasukan (dibatasi 0 sampai 1). Makin panjang makin besar bagian uang masuk yang sudah terpakai.
        // » LinearProgressIndicator = garis progres horizontal.
        LinearProgressIndicator(
            // » progress = lambda: jika pemasukan > 0, hitung pengeluaran / pemasukan lalu batasi 0 sampai 1. Kalau
            // » pemasukan 0, nilainya 0.
            progress = { if (viewModel.totalIncome > 0) (viewModel.totalExpense.toFloat() / viewModel.totalIncome.toFloat()).coerceIn(0f, 1f) else 0f },
            // » modifier: penuh lebar, jarak 16dp kiri-kanan, tinggi 8dp, sudut membulat.
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).height(8.dp).clip(RoundedCornerShape(4.dp)),
            // » Warna isi cyan; warna latar abu transparan 30%.
            color = KastepCyan, trackColor = KastepGray.copy(alpha = 0.3f)
        )

        // » Jarak 20dp.
        Spacer(modifier = Modifier.height(20.dp))

        // Kartu "Transaksi Terbaru": judul, jumlah transaksi, lalu daftar yang bisa digulir (LazyColumn). Pembayaran tanda +, pengeluaran tanda -.
        // Recent Transactions
        // » Card = kartu "Transaksi Terbaru". weight(1f) = mengisi sisa tinggi layar.
        Card(
            modifier = Modifier.fillMaxWidth().weight(1f).padding(horizontal = 16.dp),
            // » Sudut kartu membulat 20dp.
            shape = RoundedCornerShape(20.dp),
            // » Warna latar kartu biru tua.
            colors = CardDefaults.cardColors(containerColor = KastepCardDark)
        ) {
            // » Column dengan jarak dalam 20dp.
            Column(modifier = Modifier.padding(20.dp)) {
                // » Row = judul di kiri, jumlah di kanan.
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    // » Teks "Transaksi Terbaru": putih, 16sp, tebal.
                    Text("Transaksi Terbaru", color = KastepWhite, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    // » Teks jumlah transaksi, misalnya "10 transaksi", warna cyan.
                    Text("${recentTxList.size} transaksi", color = KastepCyan, fontSize = 13.sp, fontWeight = FontWeight.Medium)
                }
                // » Jarak 16dp.
                Spacer(modifier = Modifier.height(16.dp))

                // » Kalau tidak ada transaksi...
                if (recentTxList.isEmpty()) {
                    // » ...tampilkan teks "Belum ada transaksi".
                    Text("Belum ada transaksi", color = KastepGray, fontSize = 14.sp)
                } else {
                    // » LazyColumn = daftar yang bisa digulir; hanya baris yang terlihat yang digambar.
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        // » items = untuk setiap transaksi di recentTxList.
                        items(recentTxList) { tx ->
                            // » Row = satu baris transaksi: judul/tanggal di kiri, jumlah di kanan.
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Top) {
                                // » Column (weight 1f) = judul dan tanggal, mengisi ruang yang tersisa.
                                Column(modifier = Modifier.weight(1f)) {
                                    // » Judul transaksi, putih, 14sp, semi tebal.
                                    Text(tx.title, color = KastepWhite, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                                    Spacer(modifier = Modifier.height(2.dp))
                                    // » Tanggal transaksi, abu, 11sp.
                                    Text(tx.date, color = KastepGray, fontSize = 11.sp)
                                }
                                // » Jumlah transaksi di kanan.
                                Text(
                                    // » Tanda "+" untuk pemasukan, "-" untuk pengeluaran, lalu jumlah rupiah.
                                    "${if (tx.isIncome) "+" else "-"} ${KastepViewModel.formatRupiah(tx.amount)}",
                                    // » Warna hijau untuk pemasukan, merah untuk pengeluaran.
                                    color = if (tx.isIncome) KastepGreen else KastepRed,
                                    fontSize = 14.sp, fontWeight = FontWeight.Bold
                                )
                            }
                            // » Garis pemisah tipis antar transaksi.
                            HorizontalDivider(color = KastepGray.copy(alpha = 0.15f), thickness = 0.5.dp, modifier = Modifier.padding(vertical = 8.dp))
                        }
                    }
                }
            }
        }

        // » Jarak 16dp di bawah daftar.
        Spacer(modifier = Modifier.height(16.dp))
    }
}

// Kartu ringkasan Dashboard: Saldo saat ini, Pemasukan, Pengeluaran.
// » @Composable = fungsi penggambar tampilan. private = hanya dipakai di file ini.
@Composable
// » SummaryCard = kartu ringkasan kecil (judul, ikon, angka).
private fun SummaryCard(modifier: Modifier = Modifier, title: String, amount: String, icon: androidx.compose.ui.graphics.vector.ImageVector, iconTint: Color) {
    // » Card = kartu dengan sudut membulat 12dp dan warna biru tua.
    Card(modifier = modifier, shape = RoundedCornerShape(12.dp), colors = CardDefaults.cardColors(containerColor = KastepCardDark)) {
        // » Column dengan jarak dalam 12dp.
        Column(modifier = Modifier.padding(12.dp)) {
            // » Judul kartu (mis. "Saldo saat ini"), abu, 10sp.
            Text(title, color = KastepGray, fontSize = 10.sp, fontWeight = FontWeight.Medium)
            Spacer(modifier = Modifier.height(8.dp))
            // » Row = ikon dan angka berdampingan.
            Row(verticalAlignment = Alignment.CenterVertically) {
                // » Ikon kartu, warnanya dari parameter iconTint, ukuran 16dp.
                Icon(icon, contentDescription = title, tint = iconTint, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                // » Angka rupiah, putih, 11sp, tebal.
                Text(amount, color = KastepWhite, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}
