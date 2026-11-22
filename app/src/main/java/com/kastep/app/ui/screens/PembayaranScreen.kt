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
// » Alamat folder (package) file ini di dalam proyek.
package com.kastep.app.ui.screens

// » Context = pegangan ke lingkungan aplikasi (dibutuhkan untuk membuka penyimpanan, WhatsApp, dll).
import android.content.Context
// » Intent = 'niat' membuka aplikasi lain, mis. membuka WhatsApp atau membagikan teks.
import android.content.Intent
// » Uri = alamat/tautan, mis. tautan WhatsApp wa.me.
import android.net.Uri
// » Image = menampilkan gambar.
import androidx.compose.foundation.Image
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
// » verticalScroll = membuat isi bisa digulir naik-turun.
import androidx.compose.foundation.verticalScroll
// » Icons = daftar ikon bawaan Material.
import androidx.compose.material.icons.Icons
// » Ikon bawaan Material: CheckCircle (versi terisi).
import androidx.compose.material.icons.filled.CheckCircle
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
// » Color = tipe warna Compose; dipakai untuk menulis warna seperti Color.White atau Color(0xFF...).
import androidx.compose.ui.graphics.Color
// » ContentScale = cara gambar menyesuaikan kotak (Crop = penuhi kotak dan potong kelebihan).
import androidx.compose.ui.layout.ContentScale
// » LocalContext = mengambil Context aplikasi di dalam tampilan (untuk membuka WhatsApp, Toast, dll).
import androidx.compose.ui.platform.LocalContext
// » painterResource = mengambil gambar dari folder res/drawable.
import androidx.compose.ui.res.painterResource
// » FontWeight = ketebalan huruf (Normal, Medium, Bold, ExtraBold).
import androidx.compose.ui.text.font.FontWeight
// » TextAlign = perataan teks (kiri, tengah, kanan).
import androidx.compose.ui.text.style.TextAlign
// » dp = satuan ukuran Android yang sama di semua layar (mis. 16.dp).
import androidx.compose.ui.unit.dp
// » sp = satuan ukuran huruf Android (mengikuti pengaturan ukuran font di HP).
import androidx.compose.ui.unit.sp
// » Memanggil R (dari com.kastep.app) supaya bisa dipakai di file ini.
import com.kastep.app.R
// » Memanggil KastepViewModel (dari com.kastep.app.data) supaya bisa dipakai di file ini.
import com.kastep.app.data.KastepViewModel
// » Memanggil StatusBayar (dari com.kastep.app.data) supaya bisa dipakai di file ini.
import com.kastep.app.data.StatusBayar
// » Memanggil * (dari com.kastep.app.ui.theme) supaya bisa dipakai di file ini.
import com.kastep.app.ui.theme.*
// » Memanggil URLEncoder (dari java.net) supaya bisa dipakai di file ini.
import java.net.URLEncoder

// » Komentar: daftar metode pembayaran yang tersedia.
// CARI-PEMBAYARAN | Pilihan metode: QRIS, DANA, GoPay, OVO, BCA, Mandiri, BNI, Cash.
// » @OptIn(ExperimentalMaterial3Api) = izinkan komponen Material 3 yang masih bersifat eksperimental.
@OptIn(ExperimentalMaterial3Api::class)
// » @Composable = fungsi penggambar tampilan.
@Composable
// » fun PembayaranScreen = layar Pembayaran (bayar kas). onPaymentSuccess = dipanggil setelah pembayaran
// » dicatat.
fun PembayaranScreen(viewModel: KastepViewModel, onOpenDrawer: () -> Unit, onNavigateToProfile: () -> Unit = {}, onPaymentSuccess: () -> Unit = {}) {
    // » userProfile = data akun yang login (menentukan mode ADMIN atau USER).
    val userProfile by viewModel.userProfile.collectAsState()
    // » students = daftar siswa (untuk dipilih bendahara).
    val students by viewModel.students.collectAsState()
    // » currentDate = tanggal hari ini untuk bar atas.
    val currentDate = viewModel.getCurrentDateString()
    // » context = konteks Android. Dipakai untuk membuka WhatsApp dan aplikasi lain.
    val context = LocalContext.current
    // isAdmin = bendahara (bisa mencatat pembayaran). Selain itu USER (siswa) yang hanya bisa mengirim konfirmasi WA.
    // » isAdmin = true kalau akun ini bendahara (ADMIN).
    val isAdmin = userProfile.role == com.kastep.app.data.UserRole.ADMIN

    // selectedMethod = metode yang dipilih (null = belum ada). showPaymentDialog / showQrisDialog = dialog mana yang sedang tampil.
    // » selectedMethod = metode yang sedang dipilih (null = belum memilih).
    var selectedMethod by remember { mutableStateOf<String?>(null) }
    // » showPaymentDialog = apakah dialog pembayaran terbuka.
    var showPaymentDialog by remember { mutableStateOf(false) }
    // » showQrisDialog = apakah dialog gambar QRIS terbuka.
    var showQrisDialog by remember { mutableStateOf(false) }

    // » Column = susunan atas-bawah seluruh layar.
    Column(
        // » Penuh, latar hitam, bisa digulir ke bawah (verticalScroll).
        modifier = Modifier.fillMaxSize().background(KastepBlack).verticalScroll(rememberScrollState())
    ) {
        // » TopBar = bar atas (menu, tanggal, profil).
        TopBar(currentDate = currentDate, userName = userProfile.nama, onMenuClick = onOpenDrawer, onNavigateToProfile = onNavigateToProfile)
        // » Jarak 12dp.
        Spacer(modifier = Modifier.height(12.dp))

        // » Judul "Pembayaran", 22sp.
        Text("Pembayaran", color = KastepWhite, fontSize = 22.sp, fontWeight = FontWeight.Medium, modifier = Modifier.padding(horizontal = 20.dp))
        Spacer(modifier = Modifier.height(4.dp))
        // » Keterangan iuran: Rp 5.000 per minggu, dibayar per bulan (warna cyan).
        Text("Iuran Kas: Rp 5.000 / minggu, dibayar per bulan", color = KastepCyan, fontSize = 14.sp, modifier = Modifier.padding(horizontal = 20.dp))
        Spacer(modifier = Modifier.height(4.dp))
        // » Teks petunjuk. Kalau bendahara, pakai kalimat mode bendahara.
        Text(
            // » Kalimat untuk bendahara: catat pembayaran setelah uangnya dicek.
            if (isAdmin) "Mode bendahara: catat pembayaran siswa setelah uangnya dicek."
            // » Kalau bukan bendahara (siswa): kirim konfirmasi, status LUNAS setelah bendahara mencatat.
            else "Setelah membayar, kirim konfirmasi ke bendahara. Status menjadi LUNAS setelah bendahara mencatatnya.",
            color = KastepGray, fontSize = 12.sp, modifier = Modifier.padding(horizontal = 20.dp)
        )

        // » Jarak 16dp.
        Spacer(modifier = Modifier.height(16.dp))
        // » Garis pemisah tipis.
        HorizontalDivider(color = KastepGray.copy(alpha = 0.3f), thickness = 0.5.dp)

        // QRIS
        // » PaymentMethodSection = kotak pengelompokan satu jenis metode (dibuat di bawah). Bagian ini untuk QRIS.
        PaymentMethodSection {
            // » Card = kartu tombol QRIS.
            Card(
                // » Ukuran kartu 100dp x 60dp.
                modifier = Modifier.size(width = 100.dp, height = 60.dp)
                    // » Saat ditekan: pilih QRIS dan buka dialog gambar QRIS.
                    .clickable { selectedMethod = "QRIS"; showQrisDialog = true },
                // » Sudut kartu membulat 12dp.
                shape = RoundedCornerShape(12.dp),
                // » Warna kartu: cyan transparan kalau sedang dipilih, putih kalau tidak.
                colors = CardDefaults.cardColors(containerColor = if (selectedMethod == "QRIS") KastepCyan.copy(alpha = 0.3f) else Color.White)
            ) {
                // » Box isi kartu, rata tengah.
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    // » Image = gambar logo QRIS dari folder res/drawable (ic_qris), ukuran 80dp.
                    Image(painter = painterResource(R.drawable.ic_qris), contentDescription = "QRIS", modifier = Modifier.size(80.dp).padding(8.dp), contentScale = ContentScale.Fit)
                }
            }
        }
        // » Garis pemisah.
        HorizontalDivider(color = KastepGray.copy(alpha = 0.3f), thickness = 0.5.dp)

        // E-Wallets
        // » PaymentMethodSection untuk dompet digital (e-wallet).
        PaymentMethodSection {
            // » Row = tiga pilihan berdampingan, jaraknya merata.
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                // » PaymentIconCardSelectable = kartu pilihan berikon. Di sini DANA: gambar ic_dana, nama "DANA", terpilih
                // » kalau selectedMethod == "DANA".
                PaymentIconCardSelectable(R.drawable.ic_dana, "DANA", selectedMethod == "DANA") { selectedMethod = "DANA" }
                // » Pilihan GoPay (ikon ic_gopay).
                PaymentIconCardSelectable(R.drawable.ic_gopay, "GoPay", selectedMethod == "GoPay") { selectedMethod = "GoPay" }
                // » Pilihan OVO (ikon ic_ovo).
                PaymentIconCardSelectable(R.drawable.ic_ovo, "OVO", selectedMethod == "OVO") { selectedMethod = "OVO" }
            }
        }
        HorizontalDivider(color = KastepGray.copy(alpha = 0.3f), thickness = 0.5.dp)

        // Banks
        // » Bagian bank.
        PaymentMethodSection {
            // » Row tiga bank berdampingan.
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                // » Pilihan BCA (ikon ic_bca).
                PaymentIconCardSelectable(R.drawable.ic_bca, "BCA", selectedMethod == "BCA") { selectedMethod = "BCA" }
                // » Pilihan Mandiri.
                PaymentIconCardSelectable(R.drawable.ic_mandiri, "Mandiri", selectedMethod == "Mandiri") { selectedMethod = "Mandiri" }
                // » Pilihan BNI.
                PaymentIconCardSelectable(R.drawable.ic_bni, "BNI", selectedMethod == "BNI") { selectedMethod = "BNI" }
            }
        }
        HorizontalDivider(color = KastepGray.copy(alpha = 0.3f), thickness = 0.5.dp)

        // CASH
        // » Bagian CASH (tunai).
        PaymentMethodSection {
            // » Card tunai.
            Card(
                // » Penuh lebar, tinggi 60dp.
                modifier = Modifier.fillMaxWidth().height(60.dp)
                    // » Saat ditekan: pilih metode "Cash".
                    .clickable { selectedMethod = "Cash" },
                // » Sudut membulat 12dp.
                shape = RoundedCornerShape(12.dp),
                // » Warna: cyan transparan 15% kalau dipilih, transparan kalau tidak.
                colors = CardDefaults.cardColors(containerColor = if (selectedMethod == "Cash") KastepCyan.copy(alpha = 0.15f) else Color.Transparent)
            // » Tutup kartu CASH (blok lambda kartu).
            ) {
                // » Box isi kartu tunai, teks di tengah.
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    // » Teks besar "CASH" (32sp, sangat tebal).
                    Text("CASH", color = KastepWhite, fontSize = 32.sp, fontWeight = FontWeight.ExtraBold)
                }
            }
        }

        // » Komentar: kotak nomor tujuan transfer. Nomor di bawah masih CONTOH (bukan rekening asli kelas). Untuk
        // » QRIS dan Cash kotak ini tidak tampil.
        // Kotak nomor tujuan transfer. accNo di bawah berisi nomor CONTOH (belum rekening asli kelas). Untuk QRIS dan Cash kotak ini tidak tampil.
        // Simulated account numbers
        // » Kotak tujuan hanya tampil kalau metode dipilih, dan bukan Cash atau QRIS (berarti e-wallet atau bank).
        if (selectedMethod != null && selectedMethod != "Cash" && selectedMethod != "QRIS") {
            // » Card kotak tujuan transfer.
            Card(
                // » Penuh lebar dengan jarak kiri-kanan 20dp.
                modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
                shape = RoundedCornerShape(12.dp),
                // » Warna kotak biru tua gelap (#1A1A2E).
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A2E))
            ) {
                // » Column isi kotak, jarak dalam 16dp.
                Column(modifier = Modifier.padding(16.dp)) {
                    // » Judul "Transfer via <metode>", cyan, tebal.
                    Text("Transfer via $selectedMethod", color = KastepCyan, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    // CARI-QRIS | Nomor rekening / e-wallet tujuan transfer; ubah di sini bila rekening kelas berganti.
                    // » accNo = nomor tujuan sesuai metode yang dipilih. when = pilih nilai berdasarkan metode.
                    val accNo = when (selectedMethod) {
                        // » DANA = nomor 0895-2037-1942 (contoh).
                        "DANA" -> "0895-2037-1942"
                        // » GoPay = nomor yang sama dengan DANA (contoh).
                        "GoPay" -> "0895-2037-1942"
                        // » OVO = nomor yang sama (contoh).
                        "OVO" -> "0895-2037-1942"
                        // » BCA = nomor rekening 7340-5812-9076 (contoh).
                        "BCA" -> "7340-5812-9076"
                        // » Mandiri = nomor 1280-0045-6789-012 (contoh).
                        "Mandiri" -> "1280-0045-6789-012"
                        // » BNI = nomor 0912-3456-7890 (contoh).
                        "BNI" -> "0912-3456-7890"
                        // » else = tanda "-" kalau tidak cocok dengan semuanya.
                        else -> "-"
                    }
                    // » Teks nomor rekening/tujuan, tebal, 16sp.
                    Text("No. Rekening: $accNo", color = KastepWhite, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    // » Atas nama kas kelas (a.n. KAS XII PPLG).
                    Text("a.n. KAS XII PPLG", color = KastepGray, fontSize = 13.sp)
                    // » Keterangan: nominal menyesuaikan tagihan di langkah berikutnya (hijau).
                    Text("Nominal: sesuai tagihan di langkah berikutnya", color = KastepGreen, fontSize = 14.sp, fontWeight = FontWeight.Medium)
                }
            }
            // » Jarak 12dp setelah kotak.
            Spacer(modifier = Modifier.height(12.dp))
        }

        // » Jarak 16dp sebelum tombol besar.
        Spacer(modifier = Modifier.height(16.dp))

        // » Komentar: tombol PEMBAYARAN mati (abu) sampai metode dipilih.
        // Tombol besar PEMBAYARAN. Mati (abu-abu) sampai ada metode dipilih (enabled = selectedMethod != null).
        // Big green PEMBAYARAN button
        // » Button = tombol besar PEMBAYARAN.
        Button(
            // » onClick: kalau sudah ada metode, buka dialog pembayaran.
            onClick = {
                // » Cek metode dulu sebelum membuka dialog.
                if (selectedMethod != null) showPaymentDialog = true
            },
            // » Penuh lebar, tinggi 70dp, jarak kiri-kanan 40dp.
            modifier = Modifier.fillMaxWidth().height(70.dp).padding(horizontal = 40.dp),
            // » Warna hijau kalau ada metode terpilih, abu kalau belum.
            colors = ButtonDefaults.buttonColors(containerColor = if (selectedMethod != null) Color(0xFF4CAF50) else KastepGray),
            // » Sudut membulat 16dp.
            shape = RoundedCornerShape(16.dp),
            // » enabled = tombol hanya bisa ditekan kalau metode sudah dipilih.
            enabled = selectedMethod != null
        ) {
            // » Teks "PEMBAYARAN", putih, 24sp, sangat tebal.
            Text("PEMBAYARAN", color = KastepWhite, fontSize = 24.sp, fontWeight = FontWeight.ExtraBold)
        }

        // » Jarak 32dp di bagian bawah.
        Spacer(modifier = Modifier.height(32.dp))
    // » Tutup Column utama layar.
    }

    // » Komentar: dialog pembayaran. Untuk ADMIN, konfirmasi mencatat pembayaran. Untuk USER, konfirmasi hanya
    // » membuka WhatsApp bendahara.
    // Dialog pembayaran.
    //   ADMIN: onConfirm memanggil viewModel.processPaymentRange (mencatat bulan dari sampai bulan). null = sukses -> layar Berhasil.
    //   USER : onConfirm hanya membuka WhatsApp bendahara. Tidak ada data yang diubah, status tetap sampai bendahara mencatat.
    // Payment Dialog: select student + month range
    // » Kalau dialog pembayaran harus tampil...
    if (showPaymentDialog) {
        // » Panggil dialog PaymentConfirmDialog (dibuat di bawah).
        PaymentConfirmDialog(
            // » viewModel = sumber data.
            viewModel = viewModel,
            // » students = daftar siswa.
            students = students,
            // » method = metode terpilih (kalau null, pakai Cash).
            method = selectedMethod ?: "Cash",
            // » isAdmin = apakah bendahara.
            isAdmin = isAdmin,
            // » namaAkun = nama akun login (huruf besar, tanpa spasi).
            namaAkun = userProfile.nama.trim().uppercase(),
            // » onDismiss = tutup dialog.
            onDismiss = { showPaymentDialog = false },
            // » onConfirm = dipanggil saat tombol konfirmasi ditekan: nama siswa, daftar bulan, total bayar, denda.
            onConfirm = { studentName, bulanList, totalBayar, denda ->
                // » metode = metode yang dipilih (default Cash).
                val metode = selectedMethod ?: "Cash"
                // » Kalau yang login bendahara (ADMIN)...
                if (isAdmin) {
                    // » ...catat pembayaran untuk semua bulan yang dipilih.
                    val result = viewModel.processPaymentRange(studentName, bulanList, metode)
                    // » Kalau berhasil (null)...
                    if (result == null) {
                        // » ...tutup dialog...
                        showPaymentDialog = false
                        // » ...lalu pindah ke layar Pembayaran Berhasil.
                        onPaymentSuccess()
                    } else {
                        // » Kalau gagal, tampilkan pesan error singkat (Toast).
                        android.widget.Toast.makeText(context, result, android.widget.Toast.LENGTH_SHORT).show()
                    }
                // » Kalau bukan bendahara (siswa)...
                } else {
                    // » Tutup dialog.
                    showPaymentDialog = false
                    // » Buka WhatsApp berisi rincian konfirmasi (fungsi sendWhatsAppConfirmation). sudahDicatat = false (belum
                    // » dicatat bendahara).
                    sendWhatsAppConfirmation(context, studentName, bulanList.joinToString(", "), metode, totalBayar, denda, sudahDicatat = false)
                    // » Tampilkan pesan: konfirmasi terkirim, status LUNAS setelah bendahara mencatat.
                    android.widget.Toast.makeText(
                        // » Toast = pesan singkat di layar. LENGTH_LONG = tampil lebih lama.
                        context, "Konfirmasi dikirim. Status menjadi LUNAS setelah bendahara mencatat.", android.widget.Toast.LENGTH_LONG
                    ).show()
                }
            }
        )
    }

    // QRIS Dialog
    // » Kalau dialog QRIS harus tampil...
    if (showQrisDialog) {
        // » QrisSimulationDialog = dialog gambar QRIS (tampil dulu sebelum pembayaran).
        QrisSimulationDialog(
            // » onDismiss = tutup dialog QRIS.
            onDismiss = { showQrisDialog = false },
            // » onProceed = tombol Lanjut ditekan.
            onProceed = {
                // » Tutup dialog QRIS.
                showQrisDialog = false
                // » Lanjut ke dialog pembayaran.
                showPaymentDialog = true
            }
        )
    }

}

// » Komentar: penjelasan perhitungan total bayar di dialog pembayaran.
// DIALOG PEMBAYARAN (dipakai USER dan ADMIN). Hitungan di dalamnya, untuk tiap bulan yang BELUM LUNAS di rentang yang dipilih:
//   denda      = viewModel.calculateDenda(siswa, bulan)         (Rp5.000 x bulan terlambat bila belum lunas)
//   kasBulanan = viewModel.sisaKas(siswa, bulan)                (minggu belum bayar x Rp5.000)
//   totalBayar = jumlah (kasBulanan + denda) semua bulan itu
// ADMIN memilih siswa dari daftar (ada keterangan "Belum: Juli, Agustus"). USER tidak memilih siswa, nama terkunci ke akunnya sendiri.
// Rentang bulan: "Dari bulan" dan "Sampai bulan". Bulan yang sudah LUNAS di dalam rentang dilewati, tidak dibayar dua kali.
// canPay = siswa ada, rentang benar (sampai tidak sebelum dari), dan ada minimal satu bulan yang belum lunas.
// CARI-PEMBAYARAN CARI-DENDA | Total bayar = jumlah (sisa minggu x Rp5.000 + denda) bulan yang belum lunas di rentang.
// » @OptIn(ExperimentalMaterial3Api) = izinkan API Material 3 eksperimental.
@OptIn(ExperimentalMaterial3Api::class)
// » @Composable = fungsi penggambar tampilan.
@Composable
// » private fun PaymentConfirmDialog = dialog pembayaran (hanya dipakai di file ini).
private fun PaymentConfirmDialog(
    // » viewModel = sumber data.
    viewModel: KastepViewModel,
    // » students = daftar siswa.
    students: List<com.kastep.app.data.Siswa>,
    // » method = metode pembayaran.
    method: String,
    // » isAdmin = apakah bendahara.
    isAdmin: Boolean,
    // » namaAkun = nama akun login.
    namaAkun: String,
    // » onDismiss = fungsi tutup dialog.
    onDismiss: () -> Unit,
    // » onConfirm = fungsi saat konfirmasi: (nama siswa, daftar bulan, total bayar, denda).
    onConfirm: (String, List<String>, Long, Long) -> Unit
) {
    // » bulanOptions = daftar bulan kas yang bisa dipilih.
    val bulanOptions = viewModel.getBulanKas()
    // Cek apakah bulan itu sudah LUNAS supaya tidak bisa dibayar dua kali.
    // » isLunas = fungsi bantu: true kalau siswa ada dan bulan itu sudah LUNAS.
    fun isLunas(siswa: com.kastep.app.data.Siswa?, bulan: String): Boolean =
        // » Cek status LUNAS lewat viewModel.statusBulan.
        siswa != null && viewModel.statusBulan(siswa, bulan) == com.kastep.app.data.StatusBayar.LUNAS

    // USER: siswa = baris Data Siswa yang namanya sama dengan nama akun. ADMIN: siswa dipilih dari daftar.
    // » siswaAkun = baris siswa yang namanya sama dengan akun login (untuk USER).
    val siswaAkun = students.find { it.nama == namaAkun }
    // » selectedStudent = nama siswa yang dipilih. ADMIN mulai dari kosong; USER langsung nama akunnya sendiri.
    var selectedStudent by remember { mutableStateOf(if (isAdmin) "" else siswaAkun?.nama.orEmpty()) }
    // » selectedSiswa = data siswa yang dipilih (null kalau belum ada).
    val selectedSiswa = students.find { it.nama == selectedStudent }
    // » awalBelum = bulan pertama yang belum lunas untuk siswa ini (sebagai pilihan awal). orEmpty() = kosong
    // » kalau tidak ada.
    val awalBelum = selectedSiswa?.let { s -> bulanOptions.firstOrNull { !isLunas(s, it) } }.orEmpty()
    // » dariBulan = bulan awal rentang pembayaran. Awalnya bulan pertama yang belum lunas.
    var dariBulan by remember { mutableStateOf(awalBelum) }
    // » sampaiBulan = bulan akhir rentang. Awalnya sama dengan dariBulan.
    var sampaiBulan by remember { mutableStateOf(awalBelum) }
    // » expandedStudent = apakah daftar pilihan siswa sedang terbuka (ADMIN).
    var expandedStudent by remember { mutableStateOf(false) }
    // » expandedDari = apakah daftar "Dari bulan" sedang terbuka.
    var expandedDari by remember { mutableStateOf(false) }
    // » expandedSampai = apakah daftar "Sampai bulan" sedang terbuka.
    var expandedSampai by remember { mutableStateOf(false) }

    // Rentang bulan yang dipilih, lalu hanya bulan yang belum lunas yang dihitung.
    // » iDari = posisi bulan awal dalam daftar bulan (-1 kalau tidak ada).
    val iDari = bulanOptions.indexOf(dariBulan)
    // » iSampai = posisi bulan akhir dalam daftar bulan.
    val iSampai = bulanOptions.indexOf(sampaiBulan)
    // » rentangSalah = true kalau bulan akhir lebih awal dari bulan awal.
    val rentangSalah = iDari >= 0 && iSampai >= 0 && iSampai < iDari
    // » rentang = daftar bulan dari awal sampai akhir (kalau urutannya benar). Kalau tidak, kosong.
    val rentang = if (iDari >= 0 && iSampai >= iDari) bulanOptions.subList(iDari, iSampai + 1) else emptyList()
    // » bulanBelum = bulan di rentang yang BELUM lunas. Hanya ini yang akan dibayar.
    val bulanBelum = rentang.filterNot { isLunas(selectedSiswa, it) }
    // » rincian = daftar (bulan, sisa kas, denda) untuk setiap bulan yang belum lunas. Kosong kalau siswa belum
    // » dipilih.
    val rincian = if (selectedSiswa != null) bulanBelum.map { b ->
        // » Triple(bulan, sisa kas, denda) = satu baris rincian. sisaKas dan calculateDenda dihitung dari
        // » repository.
        Triple(b, viewModel.sisaKas(selectedSiswa, b), viewModel.calculateDenda(selectedSiswa.nama, b))
    } else emptyList()
    // » totalKas = jumlah sisa kas semua bulan di rincian.
    val totalKas = rincian.sumOf { it.second }
    // » totalDenda = jumlah denda semua bulan di rincian.
    val totalDenda = rincian.sumOf { it.third }
    // » totalBayar = total kas + total denda: jumlah yang harus dibayar.
    val totalBayar = totalKas + totalDenda
    // » canPay = true kalau siswa sudah dipilih, rentang benar, dan ada bulan yang belum lunas. Tombol
    // » konfirmasi hanya aktif kalau true.
    val canPay = selectedSiswa != null && !rentangSalah && bulanBelum.isNotEmpty()

    // » AlertDialog = kotak dialog pembayaran.
    AlertDialog(
        // » Tekan di luar kotak = tutup dialog.
        onDismissRequest = onDismiss,
        title = {
            // » Judul dialog: untuk bendahara "Catat Pembayaran (metode)"; untuk siswa "Konfirmasi Pembayaran (metode)".
            Text(
                if (isAdmin) "Catat Pembayaran ($method)" else "Konfirmasi Pembayaran ($method)",
                fontWeight = FontWeight.Bold, fontSize = 16.sp
            )
        },
        text = {
            // » Column isi dialog, bisa digulir.
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                // » Kalau bendahara (ADMIN): tampilkan pilihan siswa.
                if (isAdmin) {
                    // » Label "Pilih Siswa:".
                    Text("Pilih Siswa:", fontWeight = FontWeight.Medium)
                    Spacer(modifier = Modifier.height(4.dp))
                    // » ExposedDropdownMenuBox = kotak pilihan dengan panah turun. expanded = terbuka atau tidak.
                    ExposedDropdownMenuBox(expanded = expandedStudent, onExpandedChange = { expandedStudent = it }) {
                        // » OutlinedTextField = kotak yang menampilkan siswa terpilih.
                        OutlinedTextField(
                            // » value = nama siswa terpilih. readOnly = tidak bisa diketik, hanya dipilih dari daftar.
                            value = selectedStudent, onValueChange = {}, readOnly = true,
                            // » Label "Nama Siswa".
                            label = { Text("Nama Siswa") },
                            // » Ikon panah turun di ujung kanan.
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expandedStudent) },
                            // » menuAnchor = titik tempat daftar menempel. Penuh lebar.
                            modifier = Modifier.menuAnchor().fillMaxWidth()
                        )
                        // » ExposedDropdownMenu = daftar siswa yang muncul saat kotak ditekan.
                        ExposedDropdownMenu(expanded = expandedStudent, onDismissRequest = { expandedStudent = false }) {
                            // » Untuk setiap siswa di daftar...
                            students.forEach { s ->
                                // » belum = bulan yang belum lunas untuk siswa ini.
                                val belum = bulanOptions.filterNot { isLunas(s, it) }
                                // » lunasSemua = true kalau semua bulan sudah lunas.
                                val lunasSemua = belum.isEmpty()
                                // » DropdownMenuItem = satu baris pilihan siswa.
                                DropdownMenuItem(
                                    // » text = isi baris: nama siswa dan keterangan status.
                                    text = {
                                        // » Kolom nama dan keterangan.
                                        Column {
                                            // » Nama siswa. Abu kalau sudah lunas semua.
                                            Text(s.nama, fontSize = 13.sp, color = if (lunasSemua) Color.Gray else Color.Unspecified)
                                            // » Keterangan status siswa.
                                            Text(
                                                // » Kalau lunas semua: "Lunas semua". Kalau belum: daftar bulan yang belum dibayar.
                                                if (lunasSemua) "Lunas semua"
                                                // » Bulan yang belum: diambil nama bulannya saja (sebelum spasi), contoh "Juli".
                                                else "Belum: " + belum.joinToString { it.substringBefore(" ") },
                                                fontSize = 11.sp,
                                                // » Warna keterangan: hijau kalau lunas semua; merah muda kalau belum.
                                                color = if (lunasSemua) KastepGreen else Color(0xFFFF6B6B)
                                            )
                                        }
                                    },
                                    // » enabled = baris tidak bisa dipilih kalau siswa sudah lunas semua.
                                    enabled = !lunasSemua,
                                    // » onClick: pilih siswa ini dan tutup daftar.
                                    onClick = {
                                        // » Simpan nama siswa yang dipilih dan tutup menu.
                                        selectedStudent = s.nama; expandedStudent = false
                                        // Bulan awal dan akhir langsung diisi bulan pertama yang belum lunas.
                                        // » pertama = bulan pertama yang belum lunas (kosong kalau tidak ada).
                                        val pertama = belum.firstOrNull().orEmpty()
                                        // » Isi bulan awal dan akhir dengan bulan pertama itu.
                                        dariBulan = pertama; sampaiBulan = pertama
                                    }
                                )
                            }
                        }
                    }
                // » Kalau bukan bendahara (siswa): nama terkunci ke akun sendiri.
                } else {
                    // » Tampilkan nama siswa dari akun (atau nama akun kalau tidak ada di daftar).
                    Text("Nama: ${if (siswaAkun != null) siswaAkun.nama else namaAkun}", fontWeight = FontWeight.Medium)
                    // » Kalau akun belum ada di Data Siswa...
                    if (siswaAkun == null) {
                        Spacer(modifier = Modifier.height(4.dp))
                        // » ...tampilkan peringatan: samakan nama di Profil atau hubungi bendahara.
                        Text(
                            // » Isi peringatan.
                            "Nama akunmu belum ada di Data Siswa. Samakan nama di Profil atau hubungi bendahara.",
                            color = Color(0xFFFF6B6B), fontSize = 12.sp
                        )
                    }
                }
                // » Jarak 12dp.
                Spacer(modifier = Modifier.height(12.dp))

                // » Label "Bulan yang dibayar:".
                Text("Bulan yang dibayar:", fontWeight = FontWeight.Medium)
                Spacer(modifier = Modifier.height(4.dp))
                // » pilihan = daftar (bulan, sudah lunas?) untuk pilihan bulan.
                val pilihan = bulanOptions.map { it to isLunas(selectedSiswa, it) }
                // » BulanPicker = kotak pilih bulan untuk "Dari bulan".
                BulanPicker("Dari bulan", dariBulan, expandedDari, { expandedDari = it }, pilihan) { b ->
                    // » Saat bulan awal dipilih: simpan ke dariBulan.
                    dariBulan = b
                    // » Kalau bulan akhir jadi lebih awal dari bulan awal, samakan bulan akhir dengan bulan awal.
                    if (bulanOptions.indexOf(sampaiBulan) < bulanOptions.indexOf(b)) sampaiBulan = b
                }
                Spacer(modifier = Modifier.height(8.dp))
                // » BulanPicker untuk "Sampai bulan". Saat dipilih, simpan ke sampaiBulan.
                BulanPicker("Sampai bulan", sampaiBulan, expandedSampai, { expandedSampai = it }, pilihan) { b -> sampaiBulan = b }

                Spacer(modifier = Modifier.height(12.dp))
                // » when = pesan keterangan sesuai kondisi (tanpa else, jadi kalau tidak ada yang cocok tidak ada pesan).
                when {
                    // » Kalau semua bulan siswa sudah lunas: tampilkan pesan hijau.
                    selectedSiswa != null && bulanOptions.all { isLunas(selectedSiswa, it) } ->
                        Text("${selectedSiswa.nama} sudah lunas semua bulan.", color = KastepGreen, fontSize = 12.sp)
                    // » Kalau rentang salah: tampilkan pesan merah "Bulan akhir tidak boleh sebelum bulan awal".
                    rentangSalah -> Text("Bulan akhir tidak boleh sebelum bulan awal.", color = Color(0xFFFF6B6B), fontSize = 12.sp)
                    // » Kalau rentang yang dipilih semuanya sudah lunas: tampilkan pesan merah.
                    selectedSiswa != null && rentang.isNotEmpty() && bulanBelum.isEmpty() ->
                        Text("$selectedStudent sudah lunas untuk bulan yang dipilih.", color = Color(0xFFFF6B6B), fontSize = 12.sp)
                }

                // Rincian per bulan: kas + denda.
                // » Untuk setiap baris rincian (bulan, kas, denda)...
                rincian.forEach { (b, kas, dnd) ->
                    // » Teks satu baris rincian.
                    Text(
                        // » "bulan: Rp kas" dan kalau ada denda, tambahkan "+ denda Rp ...".
                        "$b: Rp ${KastepViewModel.formatRupiah(kas)}" + if (dnd > 0) " + denda Rp ${KastepViewModel.formatRupiah(dnd)}" else "",
                        fontSize = 13.sp,
                        // » Warna merah muda kalau ada denda, warna biasa kalau tidak.
                        color = if (dnd > 0) Color(0xFFFF6B6B) else Color.Unspecified
                    )
                }
                // » Jarak 4dp kalau ada rincian.
                if (rincian.isNotEmpty()) Spacer(modifier = Modifier.height(4.dp))
                // » Teks total kas.
                Text("Kas: Rp ${KastepViewModel.formatRupiah(totalKas)}", fontWeight = FontWeight.Medium)
                // » Kalau ada denda...
                if (totalDenda > 0) {
                    // » ...tampilkan total denda (merah muda).
                    Text("Denda: Rp ${KastepViewModel.formatRupiah(totalDenda)}", color = Color(0xFFFF6B6B), fontWeight = FontWeight.Medium)
                }
                // » Jarak 4dp.
                Spacer(modifier = Modifier.height(4.dp))
                // » Teks total yang harus dibayar, tebal, hijau.
                Text("Total Bayar: Rp ${KastepViewModel.formatRupiah(totalBayar)}", fontWeight = FontWeight.Bold, color = KastepGreen, fontSize = 16.sp)
                // » Kalau bukan bendahara...
                if (!isAdmin) {
                    // » ...beri jarak 8dp lalu...
                    Spacer(modifier = Modifier.height(8.dp))
                    // » ...tampilkan catatan: status belum berubah sebelum bendahara mencatat.
                    Text(
                        "Status belum berubah sebelum bendahara mengecek dan mencatat pembayaranmu.",
                        fontSize = 12.sp, color = Color.Gray
                    )
                }
            }
        },
        confirmButton = {
            TextButton(
                // » Tombol konfirmasi: kalau canPay, panggil onConfirm dengan (siswa, bulan belum lunas, total bayar, total
                // » denda).
                onClick = { if (canPay) onConfirm(selectedStudent, bulanBelum, totalBayar, totalDenda) },
                // » enabled = tombol aktif hanya kalau canPay.
                enabled = canPay
            // » Teks tombol: "Catat Pembayaran" untuk bendahara, "Kirim Konfirmasi ke Bendahara" untuk siswa.
            ) { Text(if (isAdmin) "Catat Pembayaran" else "Kirim Konfirmasi ke Bendahara") }
        },
        // » dismissButton = tombol Batal: tutup dialog.
        dismissButton = { TextButton(onClick = onDismiss) { Text("Batal") } }
    )
}

// » Komentar: kotak pilihan satu bulan (dipakai dua kali: dari dan sampai).
// Kotak pilihan satu bulan (dipakai untuk "Dari bulan" dan "Sampai bulan").
// » Komentar: bulan yang sudah lunas tetap tampil (berlabel Lunas) supaya rentang bisa melewatinya.
// pilihan = daftar (bulan, sudahLunas). Bulan yang lunas tetap tampil berlabel (Lunas) supaya rentang bisa melewatinya.
// » @OptIn(ExperimentalMaterial3Api) = izinkan API Material 3 eksperimental.
@OptIn(ExperimentalMaterial3Api::class)
// » @Composable = fungsi penggambar tampilan.
@Composable
// » private fun BulanPicker = kotak pilihan bulan (hanya dipakai di file ini).
private fun BulanPicker(
    // » label = judul kotak. value = bulan yang sedang dipilih. expanded = apakah daftar terbuka. onExpanded =
    // » fungsi ubah status buka/tutup.
    label: String, value: String, expanded: Boolean, onExpanded: (Boolean) -> Unit,
    // » pilihan = daftar (bulan, sudah lunas?).
    // » onPick = fungsi saat bulan dipilih.
    pilihan: List<Pair<String, Boolean>>, onPick: (String) -> Unit
// » Isi fungsi BulanPicker mulai di sini.
) {
    // » ExposedDropdownMenuBox = kotak pilihan dengan panah. Ditekan = buka atau tutup daftar bulan.
    ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = onExpanded) {
        // » OutlinedTextField = kotak yang menampilkan bulan terpilih.
        OutlinedTextField(
            // » value = bulan yang dipilih. readOnly = tidak bisa diketik.
            value = value, onValueChange = {}, readOnly = true,
            // » Label kotak (mis. "Dari bulan").
            label = { Text(label) },
            // » Ikon panah turun (berubah arah saat daftar terbuka).
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
            // » menuAnchor = titik tempat daftar menempel. Penuh lebar.
            modifier = Modifier.menuAnchor().fillMaxWidth()
        )
        // » ExposedDropdownMenu = daftar bulan. Kalau diklik di luar, tutup daftar.
        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { onExpanded(false) }) {
            // » Untuk setiap pasangan (bulan, sudah lunas)...
            pilihan.forEach { (b, lunas) ->
                // » DropdownMenuItem = satu baris bulan.
                DropdownMenuItem(
                    // » Teks baris: bulan, dan kalau sudah lunas ditambah "(Lunas)".
                    text = { Text(if (lunas) "$b (Lunas)" else b) },
                    // » Saat dipilih: panggil onPick dengan bulan itu, lalu tutup daftar.
                    onClick = { onPick(b); onExpanded(false) }
                )
            }
        }
    }
}

// CARI-QRIS | Gambar QRIS dari res/drawable/ic_qris.
@Composable
// » private fun QrisSimulationDialog = dialog gambar QRIS (hanya dipakai di file ini).
private fun QrisSimulationDialog(onDismiss: () -> Unit, onProceed: () -> Unit) {
    // » AlertDialog = kotak dialog QRIS.
    AlertDialog(
        // » Tekan di luar kotak = tutup.
        onDismissRequest = onDismiss,
        // » Judul "QRIS Pembayaran".
        title = { Text("QRIS Pembayaran", fontWeight = FontWeight.Bold) },
        text = {
            // » Column di tengah secara horizontal.
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                // Simulated QRIS code
                // » Card putih = kotak latar gambar QRIS.
                Card(
                    // » Ukuran kotak 200dp.
                    modifier = Modifier.size(200.dp),
                    shape = RoundedCornerShape(12.dp),
                    // » Warna putih supaya QRIS mudah dipindai.
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    // » Box isi kotak dengan padding 16dp, rata tengah.
                    Box(modifier = Modifier.fillMaxSize().padding(16.dp), contentAlignment = Alignment.Center) {
                        // » Image = gambar QRIS.
                        Image(
                            // » painterResource(ic_qris) = ambil gambar dari folder res/drawable.
                            painter = painterResource(R.drawable.ic_qris),
                            // » contentDescription = "QRIS" untuk pembaca layar.
                            contentDescription = "QRIS",
                            // » Gambar memenuhi kotak.
                            modifier = Modifier.fillMaxSize(),
                            // » ContentScale.Fit = gambar diperkecil supaya muat utuh (tidak terpotong).
                            contentScale = ContentScale.Fit
                        )
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
                // » Nomor identitas merchant QRIS (teks contoh).
                Text("NMID: ID1026552494969", fontSize = 12.sp, color = Color.Gray)
                // » Nama tujuan pembayaran: KAS XII PPLG.
                Text("Merchant: KAS XII PPLG", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                // » Nominal mengikuti tagihan siswa.
                Text("Nominal: sesuai tagihan siswa", color = KastepGreen, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                // » Petunjuk: scan kode, lalu klik Lanjutkan.
                Text("Scan kode di atas, lalu klik Lanjutkan", fontSize = 12.sp, color = Color.Gray, textAlign = TextAlign.Center)
            }
        },
        // » Tombol Lanjutkan: panggil onProceed.
        confirmButton = { TextButton(onClick = onProceed) { Text("Lanjutkan") } },
        // » Tombol Batal: tutup dialog.
        dismissButton = { TextButton(onClick = onDismiss) { Text("Batal") } }
    )
}

// » Komentar: fungsi membuka WhatsApp dengan pesan konfirmasi.
// Membuka WhatsApp dengan pesan berisi nama, bulan, metode, kas, denda, dan total.
// » Kalau sudahDicatat = false (dari siswa): pesan dikirim ke nomor WhatsApp bendahara.
//   sudahDicatat = false (dari siswa)      : dikirim ke WA bendahara (nomor 6289520371942), isinya minta dicek dan dicatat.
// » Kalau sudahDicatat = true: WhatsApp membuka pilihan kontak untuk meneruskan bukti.
//   sudahDicatat = true  (dari bendahara) : WA membuka pilihan kontak, bendahara meneruskan bukti ke siswa atau grup.
// Pesan disusun lalu di-encode untuk URL wa.me. Bila WhatsApp tidak ada, diam saja (try catch) agar tidak crash.
// CARI-WHATSAPP | Membuka WhatsApp dengan pesan konfirmasi. Nomor bendahara ada di variabel phone.
// » internal fun sendWhatsAppConfirmation = fungsi buka WhatsApp. internal = bisa dipakai dari file lain di
// » aplikasi ini.
internal fun sendWhatsAppConfirmation(
    // » Parameter: context (konteks Android), nama siswa, bulan, metode, total bayar, denda.
    context: Context, studentName: String, bulan: String, method: String, totalBayar: Long, denda: Long,
    // » sudahDicatat = default true.
    sudahDicatat: Boolean = true
) {
    // » phone = nomor bendahara (6289520371942) kalau belum dicatat; kosong kalau sudah dicatat (pilih kontak).
    val phone = if (sudahDicatat) "" else "6289520371942"
    // » dendaLine = baris denda. Hanya diisi kalau ada denda. \n = pindah baris.
    val dendaLine = if (denda > 0) "Denda Telat: Rp ${KastepViewModel.formatRupiah(denda)}\n" else ""
    // » judul pesan. Tanda * membuat teks tebal di WhatsApp. Versi sudah dicatat dan belum dicatat berbeda.
    val judul = if (sudahDicatat) "✅ *PEMBAYARAN KAS SUDAH DICATAT*" else "🧾 *KONFIRMASI PEMBAYARAN KAS*"
    // » penutup kalimat versi sudah dicatat.
    val penutup = if (sudahDicatat) "Pembayaran sudah dicatat bendahara di aplikasi KASTEP."
                  // » penutup kalimat versi belum dicatat (minta dicek).
                  else "Mohon dicek dan dicatat di aplikasi KASTEP. Terima kasih."
    // » message = gabungan semua baris pesan.
    val message = "$judul\n\n" +
            // » Baris Nama siswa.
            "Nama: $studentName\n" +
            // » Baris Bulan yang dibayar.
            "Bulan: $bulan\n" +
            // » Baris Metode pembayaran.
            "Metode: $method\n" +
            // » Baris Kas = total bayar dikurangi denda.
            "Kas: Rp ${KastepViewModel.formatRupiah(totalBayar - denda)}\n" +
            // » Baris denda (kosong kalau tidak ada denda).
            dendaLine +
            // » Baris Total Bayar.
            "Total Bayar: Rp ${KastepViewModel.formatRupiah(totalBayar)}\n\n" +
            // » Baris penutup.
            penutup
    // » URLEncoder.encode = ubah teks jadi format aman untuk alamat web (spasi dan simbol diubah kode).
    val encodedMsg = URLEncoder.encode(message, "UTF-8")
    // » try = coba buka WhatsApp. Kalau gagal, tidak crash.
    try {
        // » url = alamat wa.me. Kalau tanpa nomor, pesan dikirim lewat pilihan kontak.
        val url = if (phone.isEmpty()) "https://wa.me/?text=$encodedMsg" else "https://wa.me/$phone?text=$encodedMsg"
        // » Intent ACTION_VIEW = niat membuka alamat. Uri.parse = ubah teks alamat jadi Uri.
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        // » FLAG_ACTIVITY_NEW_TASK = buka aplikasi di task baru (dibutuhkan saat dipanggil dari luar layar).
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        // » startActivity = buka WhatsApp atau browser.
        context.startActivity(intent)
    // » catch (_) = kalau gagal, diam saja (tidak menampilkan error).
    } catch (_: Exception) { }
}

// Wadah bagian pilihan metode bayar.
// » @Composable private fun PaymentMethodSection = wadah bagian metode bayar (jarak 20dp kiri-kanan, 16dp
// » atas-bawah).
@Composable
// » content = isi bagian (kartu-kartu metode).
private fun PaymentMethodSection(content: @Composable () -> Unit) {
    // » Box penuh lebar dengan jarak, isinya content().
    Box(modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 16.dp)) { content() }
}

// » Komentar: kartu logo metode yang bisa dipilih. Terpilih = latar cyan transparan.
// Kartu logo metode bayar yang bisa dipilih. Terpilih: berlatar cyan transparan.
// Kartu logo metode bayar (DANA, GoPay, BCA, dll) yang bisa dipilih.
// » @Composable private fun PaymentIconCardSelectable = kartu logo metode yang bisa dipilih.
@Composable
// » iconRes = id gambar logo. label = nama metode. selected = apakah dipilih. onClick = aksi saat ditekan.
private fun PaymentIconCardSelectable(iconRes: Int, label: String, selected: Boolean, onClick: () -> Unit) {
    Card(
        // » Ukuran kartu 100 x 70dp. clickable: panggil onClick.
        modifier = Modifier.size(width = 100.dp, height = 70.dp).clickable { onClick() },
        // » Sudut membulat 12dp.
        shape = RoundedCornerShape(12.dp),
        // » Warna cyan transparan 30% kalau dipilih, putih kalau tidak.
        colors = CardDefaults.cardColors(containerColor = if (selected) KastepCyan.copy(alpha = 0.3f) else Color.White)
    ) {
        // » Box isi kartu, rata tengah.
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            // » Image = logo metode, ukuran 60dp, padding 4dp, contentDescription = label untuk pembaca layar.
            Image(painter = painterResource(iconRes), contentDescription = label, modifier = Modifier.size(60.dp).padding(4.dp), contentScale = ContentScale.Fit)
        }
    }
}
