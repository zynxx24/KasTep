// ==========================================================================
// PETA FILE · KASTEP (Kelompok 1) · DataSiswaScreen.kt
// Isi file : Data Siswa: tabel status kas per bulan, denda, tambah/edit/hapus siswa; juga TopBar bersama.
// Penanda  : CARI-ANGGOTA, CARI-DENDA, CARI-VALIDASI, CARI-NAVIGASI
// Cara cari: VS Code Ctrl+Shift+F (Android Studio Ctrl+Shift+F) lalu ketik
//            "CARI-" untuk semua penanda, atau salah satu nama di baris Penanda.
// ==========================================================================
//  BACA INI DULU : LAYAR "DATA SISWA"
//  Tabel 33 siswa: No, Nama, Peran, satu kolom per bulan (Juli sampai bulan sekarang), dan kolom Denda. Bisa digeser mendatar dan dicari.
//  Simbol di kolom bulan:  ✓ lunas  |  angka "2/5" = baru 2 dari 5 Senin  |  D = dispensasi (BI/PKL)  |  ✗ belum  |  · = disembunyikan.
//  ADMIN: melihat semua baris, menekan baris untuk edit atau hapus, ada tombol tambah siswa.
//  USER : mode lihat saja. Status dan denda HANYA terlihat di baris namanya sendiri, baris teman tampil "·" (privasi, supaya tunggakan tidak mempermalukan).
//  Aturan privasi ada di variabel bolehLihat di dalam daftar.
// » Alamat folder (package) file ini di dalam proyek.
package com.kastep.app.ui.screens

// » Canvas = kanvas kosong untuk menggambar sendiri (garis, lingkaran, grafik).
import androidx.compose.foundation.Canvas
// » background = mengisi warna/gradien latar sebuah tampilan.
import androidx.compose.foundation.background
// » clickable = membuat tampilan bisa diketuk.
import androidx.compose.foundation.clickable
// » Memanggil horizontalScroll (dari androidx.compose.foundation) supaya bisa dipakai di file ini.
import androidx.compose.foundation.horizontalScroll
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
// » Ikon bawaan Material: * (versi terisi).
import androidx.compose.material.icons.filled.*
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
// » FontWeight = ketebalan huruf (Normal, Medium, Bold, ExtraBold).
import androidx.compose.ui.text.font.FontWeight
// » TextAlign = perataan teks (kiri, tengah, kanan).
import androidx.compose.ui.text.style.TextAlign
// » dp = satuan ukuran Android yang sama di semua layar (mis. 16.dp).
import androidx.compose.ui.unit.dp
// » sp = satuan ukuran huruf Android (mengikuti pengaturan ukuran font di HP).
import androidx.compose.ui.unit.sp
// » Memanggil KastepViewModel (dari com.kastep.app.data) supaya bisa dipakai di file ini.
import com.kastep.app.data.KastepViewModel
// » Memanggil Siswa (dari com.kastep.app.data) supaya bisa dipakai di file ini.
import com.kastep.app.data.Siswa
// » Memanggil StatusBayar (dari com.kastep.app.data) supaya bisa dipakai di file ini.
import com.kastep.app.data.StatusBayar
// » Memanggil UserRole (dari com.kastep.app.data) supaya bisa dipakai di file ini.
import com.kastep.app.data.UserRole
// » Memanggil * (dari com.kastep.app.ui.theme) supaya bisa dipakai di file ini.
import com.kastep.app.ui.theme.*

// » Komentar: daftar state (variabel yang disimpan) di layar ini dijelaskan di bawah.
// State: students (daftar), isAdmin, query (kata cari), showAddDialog / showEditDialog / showDeleteConfirm, selectedStudent (baris yang sedang diedit), dialogError (pesan bila nama kembar).
// CARI-ANGGOTA CARI-DENDA | Kolom bulan: ✓ lunas, angka = minggu terbayar, D = dispensasi, ✗ belum. Kolom terakhir = total denda.
// » @Composable = fungsi penggambar tampilan.
@Composable
// » fun DataSiswaScreen = layar Data Siswa (tabel status kas per bulan).
fun DataSiswaScreen(viewModel: KastepViewModel, onOpenDrawer: () -> Unit, onNavigateToProfile: () -> Unit = {}) {
    // » students = daftar siswa, dibaca dengan collectAsState (ikut berubah otomatis).
    val students by viewModel.students.collectAsState()
    // » userProfile = data akun yang sedang login.
    val userProfile by viewModel.userProfile.collectAsState()
    // » currentDate = tanggal hari ini untuk bar atas.
    val currentDate = viewModel.getCurrentDateString()
    // » isAdmin = true kalau akun yang login berperan ADMIN.
    val isAdmin = userProfile.role == UserRole.ADMIN
    // » bulanKas = daftar bulan kas (Juli 2026 sampai bulan sekarang) sebagai kolom tabel.
    val bulanKas = viewModel.getBulanKas()

    // » showAddDialog = apakah dialog Tambah Siswa terbuka.
    var showAddDialog by remember { mutableStateOf(false) }
    // » showEditDialog = apakah dialog Edit Siswa terbuka.
    var showEditDialog by remember { mutableStateOf(false) }
    // » showDeleteConfirm = apakah konfirmasi hapus terbuka.
    var showDeleteConfirm by remember { mutableStateOf(false) }
    // » selectedStudent = siswa yang baris-nya sedang dipilih (null = tidak ada).
    var selectedStudent by remember { mutableStateOf<Siswa?>(null) }
    // » dialogError = pesan error di dalam dialog (null = tidak ada).
    var dialogError by remember { mutableStateOf<String?>(null) }
    // » query = teks kotak pencarian.
    var query by remember { mutableStateOf("") }

    // » shown = daftar siswa yang tampil. Kalau kata cari kosong, semua siswa. Kalau tidak, siswa yang namanya
    // » atau perannya mengandung kata cari (tanpa huruf besar/kecil).
    val shown = students.filter { query.isBlank() || it.nama.contains(query.trim(), ignoreCase = true) || it.peran.contains(query.trim(), ignoreCase = true) }

    // » Column = susunan atas-bawah seluruh layar. Penuh dan latar hitam.
    Column(modifier = Modifier.fillMaxSize().background(KastepBlack)) {
        // » TopBar = bar atas (menu, tanggal, nama akun).
        TopBar(currentDate = currentDate, userName = userProfile.nama, onMenuClick = onOpenDrawer, onNavigateToProfile = onNavigateToProfile)

        // » Kolom judul dengan jarak 20dp kiri-kanan.
        Column(modifier = Modifier.padding(horizontal = 20.dp)) {
            // » Box = kotak judul dengan latar biru transparan.
            Box(
                modifier = Modifier
                    .background(KastepBlue.copy(alpha = 0.25f), RoundedCornerShape(4.dp))
                    .padding(horizontal = 8.dp, vertical = 2.dp)
            ) {
                // » Teks judul "Data Siswa", putih, 20sp, tebal.
                Text("Data Siswa", color = KastepWhite, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(4.dp))
            // » Keterangan: status kas per bulan, Rp 5.000 per minggu.
            Text("Status kas per bulan (Rp 5.000 / minggu)", color = KastepGray, fontSize = 13.sp)
        }

        // » Jarak 10dp.
        Spacer(modifier = Modifier.height(10.dp))

        // » OutlinedTextField = kotak pencarian.
        OutlinedTextField(
            value = query,
            // » onValueChange = setiap diketik, simpan ke query (langsung memfilter daftar).
            onValueChange = { query = it },
            // » Penuh lebar, jarak 20dp kiri-kanan.
            modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
            // » Petunjuk "Cari nama atau peran".
            placeholder = { Text("Cari nama atau peran", color = KastepGray) },
            // » leadingIcon = ikon kaca pembesar di kiri kotak.
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = KastepGray) },
            // » singleLine = satu baris.
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            // » colors = warna kotak pencarian (cyan saat dipilih).
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = KastepWhite, unfocusedTextColor = KastepWhite,
                focusedBorderColor = KastepCyan, unfocusedBorderColor = KastepGray.copy(alpha = 0.5f), cursorColor = KastepCyan
            )
        )

        // » Jarak 8dp.
        Spacer(modifier = Modifier.height(8.dp))
        // » Teks jumlah siswa.
        Text(
            // » Teks "Jumlah Siswa : N". Kalau daftar sedang difilter, ditambah "· ditampilkan X".
            "Jumlah Siswa : ${students.size}" + if (shown.size != students.size) "  ·  ditampilkan ${shown.size}" else "",
            color = KastepWhite, fontSize = 15.sp, fontWeight = FontWeight.Medium, modifier = Modifier.padding(horizontal = 20.dp)
        )
        // » Teks keterangan cara pakai (beda untuk ADMIN dan USER).
        Text(
            // » Kalau ADMIN: tekan baris untuk edit/hapus. Kalau bukan: mode lihat saja.
            (if (isAdmin) "Tap baris siswa untuk edit/hapus. " else "Mode lihat saja. Status dan denda hanya tampil di baris kamu sendiri. ") + "✓ lunas · angka = minggu terbayar · D = dispensasi",
            color = KastepGray, fontSize = 11.sp, modifier = Modifier.padding(horizontal = 20.dp)
        )

        Spacer(modifier = Modifier.height(10.dp))

        // » Column bagian tabel. weight(1f) = mengisi sisa tinggi layar.
        Column(modifier = Modifier.weight(1f).padding(horizontal = 4.dp)) {
            // » hScroll = status geser mendatar. Dipakai bersama oleh header dan baris agar kolom tetap sejajar.
            val hScroll = rememberScrollState()

            // » Row = baris judul kolom (header tabel).
            Row(
                // » horizontalScroll = header bisa digeser ke kanan.
                modifier = Modifier.fillMaxWidth().horizontalScroll(hScroll)
                    // » Latar abu gelap (#444444) dengan jarak dalam.
                    .background(Color(0xFF444444)).padding(vertical = 10.dp, horizontal = 12.dp)
            ) {
                // » Header "No", lebar 35dp.
                Text("No", color = KastepWhite, fontWeight = FontWeight.Bold, fontSize = 13.sp, modifier = Modifier.width(35.dp))
                // » Header "Nama Siswa", lebar 220dp.
                Text("Nama Siswa", color = KastepWhite, fontWeight = FontWeight.Bold, fontSize = 13.sp, modifier = Modifier.width(220.dp))
                // » Header "Peran", lebar 100dp.
                Text("Peran", color = KastepWhite, fontWeight = FontWeight.Bold, fontSize = 13.sp, modifier = Modifier.width(100.dp))
                // » Untuk setiap bulan kas...
                bulanKas.forEach { b ->
                    // » ...buat header dengan 3 huruf pertama (contoh "Jul", "Agu").
                    Text(b.take(3), color = KastepWhite, fontWeight = FontWeight.Bold, fontSize = 13.sp, modifier = Modifier.width(48.dp), textAlign = TextAlign.Center)
                }
                // » Header "Denda", lebar 80dp, rata tengah.
                Text("Denda", color = KastepWhite, fontWeight = FontWeight.Bold, fontSize = 13.sp, modifier = Modifier.width(80.dp), textAlign = TextAlign.End)
            }

            // » Column isi tabel, bisa digulir ke bawah.
            Column(modifier = Modifier.weight(1f).verticalScroll(rememberScrollState())) {
                // » Kalau tidak ada siswa yang cocok...
                if (shown.isEmpty()) {
                    // » ...tampilkan pesan dengan kata cari.
                    Text("Tidak ada siswa yang cocok dengan \"$query\"", color = KastepGray, fontSize = 13.sp, modifier = Modifier.padding(16.dp))
                }
                // » Untuk setiap siswa yang tampil...
                shown.forEach { siswa ->
                    // » denda = total denda siswa (dihitung dari repository).
                    val denda = viewModel.totalDenda(siswa)
                    // bolehLihat = true bila akun ADMIN, atau bila baris ini milik akun yang login (nama siswa sama dengan nama akun, keduanya huruf besar).
                    // Bila false, kolom bulan diganti "·" dan kolom denda dikosongkan.
                    // Privasi: status dan denda siswa lain hanya terlihat oleh ADMIN (sasaran "tunggakan tidak mempermalukan")
                    // TANYA-GURU: Kenapa siswa lain tampil titik? Akun USER hanya melihat status dan dendanya sendiri supaya tidak dipermalukan.
                    // » bolehLihat = true kalau ADMIN, atau kalau nama siswa ini sama dengan nama akun yang login (huruf besar).
                    val bolehLihat = isAdmin || siswa.nama == userProfile.nama.trim().uppercase()
                    // » Row = satu baris siswa.
                    Row(
                        // » Baris ikut digeser mendatar sesuai header.
                        modifier = Modifier.fillMaxWidth().horizontalScroll(hScroll)
                            // » Kalau ADMIN, baris bisa ditekan. Kalau USER, tidak (Modifier kosong).
                            .then(if (isAdmin) Modifier.clickable {
                                // » Simpan siswa ini sebagai yang dipilih.
                                selectedStudent = siswa
                                // » Hapus pesan error dialog lama.
                                dialogError = null
                                // » Buka dialog Edit.
                                showEditDialog = true
                            } else Modifier)
                            .padding(vertical = 10.dp, horizontal = 12.dp)
                    ) {
                        // » Nomor urut siswa (lebar 35dp).
                        Text("${siswa.no}", color = KastepWhite, fontSize = 13.sp, modifier = Modifier.width(35.dp))
                        // » Nama siswa (lebar 220dp).
                        Text(siswa.nama, color = KastepWhite, fontSize = 13.sp, modifier = Modifier.width(220.dp))
                        // » Peran siswa, warna cyan (lebar 100dp).
                        Text(siswa.peran, color = KastepCyan, fontSize = 13.sp, modifier = Modifier.width(100.dp))
                        bulanKas.forEach { b ->
                            // » lunas = true kalau bulan itu LUNAS.
                            val lunas = viewModel.statusBulan(siswa, b) == StatusBayar.LUNAS
                            // » dispensasi = true kalau ada minggu dispensasi di bulan itu.
                            val dispensasi = (siswa.dispensasi[b] ?: 0) > 0
                            // » minggu = jumlah minggu lunas (dibayar + dispensasi) di bulan itu.
                            val minggu = viewModel.mingguLunas(siswa, b)
                            // » Teks isi sel bulan. when = pilih tampilan pertama yang cocok:
                            Text(
                                // » Mulai daftar pilihan when.
                                text = when {
                                    // » Kalau USER dan bukan baris miliknya: tampilkan titik "·" (disembunyikan).
                                    !bolehLihat -> "·"
                                    // » Lunas dan ada dispensasi: tampilkan "D" (dibebaskan).
                                    lunas && dispensasi -> "D"
                                    // » Lunas: tampilkan centang "✓".
                                    lunas -> "✓"
                                    // » Sebagian dibayar: tampilkan "minggu/jumlah Senin", misalnya "2/5".
                                    minggu > 0 -> "$minggu/${viewModel.jumlahMinggu(b)}"
                                    // » Lainnya (belum bayar sama sekali): tampilkan "✗".
                                    else -> "✗"
                                },
                                // » Warna isi sel, sesuai pilihan yang sama.
                                color = when {
                                    // » Baris disembunyikan: warna abu.
                                    !bolehLihat -> KastepGray
                                    // » Dispensasi: warna cyan.
                                    lunas && dispensasi -> KastepCyan
                                    // » Lunas: warna hijau.
                                    lunas -> KastepGreen
                                    // » Sebagian dibayar: warna kuning (#FFC107).
                                    minggu > 0 -> Color(0xFFFFC107)
                                    // » Belum bayar: warna merah.
                                    else -> KastepRed
                                },
                                // » Ukuran huruf: kecil (12sp) untuk "x/y" sebagian bayar; besar (16sp) untuk simbol.
                                fontSize = if (minggu > 0 && !lunas) 12.sp else 16.sp,
                                // » Lebar sel 48dp, teks rata tengah.
                                fontWeight = FontWeight.Bold, modifier = Modifier.width(48.dp), textAlign = TextAlign.Center
                            )
                        }
                        // » Teks kolom Denda: kosong kalau tidak boleh dilihat; "Rp ..." kalau ada denda; "-" kalau tidak ada denda.
                        Text(
                            // » Pilihan isi: if (!bolehLihat) kosong, else if denda > 0 tampilkan rupiah, else "-".
                            if (!bolehLihat) "" else if (denda > 0) "Rp ${KastepViewModel.formatRupiah(denda)}" else "-",
                            // » Warna: merah kalau ada denda, abu kalau tidak.
                            color = if (denda > 0) KastepRed else KastepGray, fontSize = 12.sp,
                            // » Lebar kolom 80dp, rata kanan.
                            modifier = Modifier.width(80.dp), textAlign = TextAlign.End
                        )
                    }
                    // » Garis tipis pemisah antar baris siswa.
                    HorizontalDivider(color = KastepGray.copy(alpha = 0.15f), thickness = 0.5.dp)
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // » Bagian tombol Tambah hanya tampil untuk ADMIN.
        if (isAdmin) {
            // » Row = tombol di pojok kanan.
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    // » onClick: hapus pesan error lama dan buka dialog Tambah Siswa.
                    onClick = { dialogError = null; showAddDialog = true },
                    // » Warna tombol biru.
                    colors = ButtonDefaults.buttonColors(containerColor = KastepBlue),
                    // » Sudut tombol membulat 20dp.
                    shape = RoundedCornerShape(20.dp)
                ) {
                    // » Ikon tambah (+) ukuran 18dp.
                    Icon(Icons.Default.Add, contentDescription = "Tambah", tint = KastepWhite, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    // » Teks tombol "Tambah Siswa".
                    Text("Tambah Siswa", color = KastepWhite, fontSize = 13.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))
    }

    // » Kalau dialog Tambah sedang terbuka...
    if (showAddDialog) {
        // » StudentDialog = dialog bersama untuk tambah dan edit (dibuat di bawah).
        StudentDialog(
            // » Judul dialog: "Tambah Siswa Baru".
            title = "Tambah Siswa Baru",
            // » Isian nama awalnya kosong.
            initialNama = "",
            // » Isian peran awalnya "Anggota".
            initialPeran = "Anggota",
            // » Pesan error yang ditampilkan di dialog.
            error = dialogError,
            // » onDismiss: tutup dialog.
            onDismiss = { showAddDialog = false },
            // » onConfirm: dipanggil saat tombol Simpan ditekan; nama dan peran dari isian dialog.
            onConfirm = { nama, peran ->
                // » Panggil addStudent. Hasilnya null = berhasil; teks = pesan error.
                val err = viewModel.addStudent(nama, peran)
                // » Kalau berhasil, tutup dialog. Kalau tidak, tampilkan pesan error.
                if (err == null) showAddDialog = false else dialogError = err
            }
        )
    }

    // » Kalau dialog Edit terbuka dan ada siswa yang dipilih...
    if (showEditDialog && selectedStudent != null) {
        // » StudentDialog untuk mengedit siswa.
        StudentDialog(
            // » Judul dialog: "Edit Siswa".
            title = "Edit Siswa",
            // » Isian nama awal = nama siswa yang dipilih.
            initialNama = selectedStudent!!.nama,
            // » Isian peran awal = peran siswa yang dipilih.
            initialPeran = selectedStudent!!.peran,
            // » showDelete = true: tombol "Hapus Siswa Ini" muncul di dialog.
            showDelete = true,
            error = dialogError,
            // » onDismiss: tutup dialog dan lepas pilihan siswa.
            onDismiss = { showEditDialog = false; selectedStudent = null },
            // » onConfirm: dipanggil saat Simpan ditekan.
            onConfirm = { nama, peran ->
                // » Ubah data siswa dengan nomor yang dipilih.
                val err = viewModel.updateStudent(selectedStudent!!.no, nama, peran)
                // » Kalau berhasil, tutup dialog dan lepas pilihan. Kalau gagal, tampilkan error.
                if (err == null) { showEditDialog = false; selectedStudent = null } else dialogError = err
            },
            // » onDelete: dipanggil saat tombol "Hapus Siswa Ini" ditekan.
            onDelete = {
                // Tanya dulu sebelum menghapus
                // » Tutup dialog edit dulu.
                showEditDialog = false
                // » Buka konfirmasi hapus (supaya pengguna ditanya dulu).
                showDeleteConfirm = true
            }
        )
    }

    // » Kalau konfirmasi hapus terbuka dan ada siswa yang dipilih...
    if (showDeleteConfirm && selectedStudent != null) {
        // » AlertDialog = kotak konfirmasi standar.
        AlertDialog(
            // » onDismissRequest = kalau kotak ditekan di luar, anggap Batal: tutup dan lepas pilihan.
            onDismissRequest = { showDeleteConfirm = false; selectedStudent = null },
            // » Judul "Hapus Siswa", tebal.
            title = { Text("Hapus Siswa", fontWeight = FontWeight.Bold) },
            // » Isi pertanyaan: yakin hapus nama siswa ini? Riwayat pembayarannya tetap tersimpan.
            text = { Text("Yakin ingin menghapus ${selectedStudent!!.nama}? Riwayat pembayarannya tetap tersimpan.") },
            confirmButton = {
                // » Tombol konfirmasi (Hapus): onClick menjalankan penghapusan.
                TextButton(onClick = {
                    // » deleteStudent memakai nomor siswa yang dipilih.
                    viewModel.deleteStudent(selectedStudent!!.no)
                    // » Tutup konfirmasi dan lepas pilihan.
                    showDeleteConfirm = false; selectedStudent = null
                // » Teks tombol "Hapus", merah.
                }) { Text("Hapus", color = KastepRed) }
            },
            dismissButton = {
                // » Tombol Batal: tutup tanpa menghapus.
                TextButton(onClick = { showDeleteConfirm = false; selectedStudent = null }) { Text("Batal") }
            }
        )
    }
}

// CARI-ANGGOTA CARI-VALIDASI | Dialog tambah/edit siswa; menampilkan pesan bila nama sudah dipakai.
// » @Composable private fun StudentDialog = dialog tambah/edit yang hanya dipakai di file ini.
@Composable
// » title = judul dialog.
private fun StudentDialog(
    // » initialNama = nama awal di isian.
    title: String,
    // » initialPeran = peran awal di isian.
    initialNama: String,
    initialPeran: String,
    // » showDelete = apakah tombol hapus ditampilkan (default tidak).
    showDelete: Boolean = false,
    // » error = pesan error (null = tidak ada).
    error: String? = null,
    // » onDismiss = fungsi saat dialog ditutup.
    onDismiss: () -> Unit,
    // » onConfirm = fungsi saat Simpan ditekan, membawa nama dan peran.
    onConfirm: (String, String) -> Unit,
    // » onDelete = fungsi saat tombol hapus ditekan (null = tidak ada tombol hapus).
    onDelete: (() -> Unit)? = null
) {
    // » nama = isian nama, diambil dari nilai awal.
    var nama by remember { mutableStateOf(initialNama) }
    // » peran = isian peran, diambil dari nilai awal.
    var peran by remember { mutableStateOf(initialPeran) }

    // » AlertDialog = kotak dialog berisi form.
    AlertDialog(
        // » Tekan di luar kotak = tutup dialog (onDismiss).
        onDismissRequest = onDismiss,
        // » Judul dialog.
        title = { Text(title, fontWeight = FontWeight.Bold) },
        text = {
            // » Column = isian disusun ke bawah.
            Column {
                // » OutlinedTextField untuk nama siswa.
                OutlinedTextField(
                    // » Label kecil di dalam kotak: "Nama Siswa".
                    value = nama, onValueChange = { nama = it }, label = { Text("Nama Siswa") },
                    // » singleLine = satu baris.
                    modifier = Modifier.fillMaxWidth(), singleLine = true,
                    // » isError = kalau ada error, garis kotak jadi merah.
                    isError = error != null, supportingText = { if (error != null) Text(error, color = KastepRed) }
                // » supportingText = tampilkan pesan error kecil di bawah kotak.
                )
                Spacer(modifier = Modifier.height(8.dp))
                // » Kotak isian peran dengan label "Peran". Lebar penuh.
                OutlinedTextField(value = peran, onValueChange = { peran = it }, label = { Text("Peran") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
                // » Kalau tombol hapus diminta dan ada fungsi hapus...
                if (showDelete && onDelete != null) {
                    // » ...beri jarak 12dp di atasnya.
                    Spacer(modifier = Modifier.height(12.dp))
                    // » TextButton hapus, lebar penuh: saat ditekan jalankan onDelete.
                    TextButton(onClick = onDelete, modifier = Modifier.fillMaxWidth()) {
                        // » Ikon tempat sampah merah, 18dp.
                        Icon(Icons.Default.Delete, contentDescription = null, tint = KastepRed, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        // » Teks "Hapus Siswa Ini", merah.
                        Text("Hapus Siswa Ini", color = KastepRed)
                    }
                }
            }
        },
        confirmButton = {
            // » Tombol Simpan: panggil onConfirm dengan nama dan peran. enabled = mati kalau nama kosong.
            TextButton(onClick = { onConfirm(nama, peran) }, enabled = nama.isNotBlank()) { Text("Simpan") }
        },
        dismissButton = {
            // » Tombol Batal: panggil onDismiss.
            TextButton(onClick = onDismiss) { Text("Batal") }
        }
    )
}

// Bar atas yang dipakai hampir semua layar: tombol menu samping (kiri), tanggal hari ini (tengah), nama akun dan ikon profil (kanan, menuju Profil).
// Shared top bar
// CARI-NAVIGASI | Bar atas yang dipakai banyak halaman: tombol menu, tanggal, dan tombol profil.
@Composable
// » fun TopBar = bar atas yang dipakai banyak layar (public, bisa dipanggil dari file lain).
fun TopBar(currentDate: String, userName: String, onMenuClick: () -> Unit, onNavigateToProfile: () -> Unit = {}) {
    // » Row = tiga bagian dalam satu baris: menu, tanggal, profil.
    Row(
        // » Penuh lebar, jarak dalam 16dp kiri-kanan dan 12dp atas-bawah.
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp),
        // » SpaceBetween = bagian diberi jarak merata sampai ke tepi.
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // » IconButton menu: saat ditekan, panggil onMenuClick (buka menu samping).
        IconButton(onClick = onMenuClick) {
            // » Ikon garis tiga (menu), putih, 28dp.
            Icon(Icons.Default.Menu, contentDescription = "Menu", tint = KastepWhite, modifier = Modifier.size(28.dp))
        }
        // » Row untuk tanggal.
        Row(verticalAlignment = Alignment.CenterVertically) {
            // » Ikon kalender, 18dp.
            Icon(Icons.Default.CalendarMonth, contentDescription = "Calendar", tint = KastepWhite, modifier = Modifier.size(18.dp))
            // » Jarak 6dp.
            Spacer(modifier = Modifier.width(6.dp))
            // » Teks tanggal hari ini, 14sp.
            Text(text = currentDate, color = KastepWhite, fontSize = 14.sp)
        }
        // » Row bagian profil.
        Row(
            verticalAlignment = Alignment.CenterVertically,
            // » modifier bagian profil.
            modifier = Modifier
                // » clip = potong area menjadi sudut membulat 20dp (supaya area tekan rapi).
                .clip(RoundedCornerShape(20.dp))
                // » clickable: saat ditekan, pindah ke Profil.
                .clickable { onNavigateToProfile() }
                // » Jarak dalam 8dp kiri-kanan dan 4dp atas-bawah.
                .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            // » Box lingkaran abu 32dp sebagai foto profil.
            Box(modifier = Modifier.size(32.dp).clip(CircleShape).background(KastepGray), contentAlignment = Alignment.Center) {
                // » Ikon orang (Person), 20dp.
                Icon(Icons.Default.Person, contentDescription = "Profile", tint = KastepWhite, modifier = Modifier.size(20.dp))
            }
            // » Jarak 6dp.
            Spacer(modifier = Modifier.width(6.dp))
            // » Nama akun (14sp, semi tebal).
            Text(text = userName, color = KastepWhite, fontSize = 14.sp, fontWeight = FontWeight.Medium)
        }
    }
}
