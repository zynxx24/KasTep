// ==========================================================================
// PETA FILE · KASTEP (Kelompok 1) · MataPelajaranScreen.kt
// Isi file : Jadwal pelajaran dan alokasi waktu XII PPLG.
// Penanda  : CARI-JADWAL
// Cara cari: VS Code Ctrl+Shift+F (Android Studio Ctrl+Shift+F) lalu ketik
//            "CARI-" untuk semua penanda, atau salah satu nama di baris Penanda.
// ==========================================================================
//  BACA INI DULU : LAYAR "MATA PELAJARAN" (jadwal pelajaran)
//  Dua tabel tetap (data ditulis langsung di file, tidak ada hubungannya dengan kas):
//    1. Tabel Jadwal Pelajaran Senin sampai Jumat, kolom JP 1 sampai 10, blok berwarna per mapel
//    2. Tabel Alokasi Waktu (jam mulai dan selesai tiap JP)
//  Mengubah jadwal: edit daftar ScheduleEntry(mapel, guru, warna, jam mulai, lama jam).
//  Senin punya baris khusus (SeninRow) karena dua praktik (PDL RPL dan PDL KL) berjalan bersamaan.
// » Alamat folder (package) file ini di dalam proyek.
package com.kastep.app.ui.screens

// » background = mengisi warna/gradien latar sebuah tampilan.
import androidx.compose.foundation.background
// » border = garis tepi sebuah tampilan.
import androidx.compose.foundation.border
// » Memanggil horizontalScroll (dari androidx.compose.foundation) supaya bisa dipakai di file ini.
import androidx.compose.foundation.horizontalScroll
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
// » Memanggil fillMaxHeight (dari androidx.compose.foundation.layout) supaya bisa dipakai di file ini.
import androidx.compose.foundation.layout.fillMaxHeight
// » Memanggil fillMaxSize (dari androidx.compose.foundation.layout) supaya bisa dipakai di file ini.
import androidx.compose.foundation.layout.fillMaxSize
// » Memanggil fillMaxWidth (dari androidx.compose.foundation.layout) supaya bisa dipakai di file ini.
import androidx.compose.foundation.layout.fillMaxWidth
// » Memanggil height (dari androidx.compose.foundation.layout) supaya bisa dipakai di file ini.
import androidx.compose.foundation.layout.height
// » Memanggil padding (dari androidx.compose.foundation.layout) supaya bisa dipakai di file ini.
import androidx.compose.foundation.layout.padding
// » Memanggil width (dari androidx.compose.foundation.layout) supaya bisa dipakai di file ini.
import androidx.compose.foundation.layout.width
// » rememberScrollState = menyimpan posisi gulir.
import androidx.compose.foundation.rememberScrollState
// » RoundedCornerShape = bentuk persegi dengan sudut membulat.
import androidx.compose.foundation.shape.RoundedCornerShape
// » verticalScroll = membuat isi bisa digulir naik-turun.
import androidx.compose.foundation.verticalScroll
// » Memanggil Card (dari androidx.compose.material3) supaya bisa dipakai di file ini.
import androidx.compose.material3.Card
// » Memanggil CardDefaults (dari androidx.compose.material3) supaya bisa dipakai di file ini.
import androidx.compose.material3.CardDefaults
// » Memanggil HorizontalDivider (dari androidx.compose.material3) supaya bisa dipakai di file ini.
import androidx.compose.material3.HorizontalDivider
// » Memanggil Text (dari androidx.compose.material3) supaya bisa dipakai di file ini.
import androidx.compose.material3.Text
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
// » Color = tipe warna Compose; dipakai untuk menulis warna seperti Color.White atau Color(0xFF...).
import androidx.compose.ui.graphics.Color
// » FontWeight = ketebalan huruf (Normal, Medium, Bold, ExtraBold).
import androidx.compose.ui.text.font.FontWeight
// » TextAlign = perataan teks (kiri, tengah, kanan).
import androidx.compose.ui.text.style.TextAlign
// » TextOverflow = cara menangani teks yang terlalu panjang (Ellipsis = dipotong dengan '...').
import androidx.compose.ui.text.style.TextOverflow
// » Dp = tipe ukuran dp, dipakai sebagai tipe parameter.
import androidx.compose.ui.unit.Dp
// » dp = satuan ukuran Android yang sama di semua layar (mis. 16.dp).
import androidx.compose.ui.unit.dp
// » sp = satuan ukuran huruf Android (mengikuti pengaturan ukuran font di HP).
import androidx.compose.ui.unit.sp
// » Memanggil KastepViewModel (dari com.kastep.app.data) supaya bisa dipakai di file ini.
import com.kastep.app.data.KastepViewModel
// » Memanggil KastepBlack (dari com.kastep.app.ui.theme) supaya bisa dipakai di file ini.
import com.kastep.app.ui.theme.KastepBlack
// » Memanggil KastepCyan (dari com.kastep.app.ui.theme) supaya bisa dipakai di file ini.
import com.kastep.app.ui.theme.KastepCyan
// » Memanggil KastepWhite (dari com.kastep.app.ui.theme) supaya bisa dipakai di file ini.
import com.kastep.app.ui.theme.KastepWhite

// » Komentar: warna-warna tabel jadwal (mengikuti gambar jadwal aslinya).
// ─── Color palette matching the schedule image ───
// » ScheduleBg = latar tabel jadwal (biru tua gelap). private val = hanya dipakai di file ini.
private val ScheduleBg = Color(0xFF1A1A2E)
// » HeaderBg = warna baris judul tabel.
private val HeaderBg = Color(0xFF2D2D44)
// » BorderColor = warna garis tepi tabel.
private val BorderColor = Color(0xFF3A3A55)
// » DayLabelBg = warna latar kolom nama hari.
private val DayLabelBg = Color(0xFF252540)

// Subject colors (matching the timetable image colors)
// » ColorPDLRPL = biru muda, untuk mapel PDL RPL.
private val ColorPDLRPL = Color(0xFFB3E5FC)    // Light blue
// » ColorPDLKL = magenta, untuk mapel PDL KL.
private val ColorPDLKL = Color(0xFFFF00FF)     // Magenta
// » ColorRPL = merah tua (maroon), untuk mapel RPL.
private val ColorRPL = Color(0xFF8B0000)       // Dark red / maroon
// » ColorBING = hijau, untuk mapel Bahasa Inggris (B.ING).
private val ColorBING = Color(0xFF8BC34A)      // Green
// » ColorMAT = biru keunguan, untuk Matematika.
private val ColorMAT = Color(0xFF7986CB)       // Blue-purple
// » ColorKIK = kuning, untuk KIK.
private val ColorKIK = Color(0xFFFFF176)       // Yellow
// » ColorBB = toska, untuk Bahasa Bali (BB).
private val ColorBB = Color(0xFF00BCD4)        // Cyan
// » ColorPP = biru, untuk PP.
private val ColorPP = Color(0xFF1565C0)        // Blue
// » ColorBINDO = merah muda, untuk Bahasa Indonesia (B.INDO).
private val ColorBINDO = Color(0xFFEF9A9A)     // Pink/salmon
// » ColorPABP = hijau terang, untuk PABP (pendidikan agama).
private val ColorPABP = Color(0xFF00C853)      // Bright green

// ─── Data classes for schedule ───
// » private data class ScheduleEntry = satu blok di tabel jadwal (mapel, guru, warna, jam mulai, lama).
private data class ScheduleEntry(
    // » subject = nama mata pelajaran.
    val subject: String,
    // » teacher = nama guru.
    val teacher: String,
    // » color = warna blok jadwal.
    val color: Color,
    // » startPeriod = jam pelajaran (JP) pertama blok ini (mulai dari 1).
    val startPeriod: Int,
    // » spanPeriods = berapa JP lamanya.
    val spanPeriods: Int
)

// » private data class TimeSlot = satu baris tabel alokasi waktu.
private data class TimeSlot(
    // » period = nomor JP, atau "BREAK" untuk istirahat.
    val period: String,
    // » senin = jam mulai-selesai untuk hari Senin.
    val senin: String,
    // » selasaKamis = jam mulai-selesai untuk Selasa dan Kamis.
    val selasaKamis: String,
    // » periodJumat = nomor JP untuk Jumat.
    val periodJumat: String,
    // » jumat = jam mulai-selesai untuk Jumat.
    val jumat: String
)

// ─── Schedule data per day (matching the image exactly) ───
// » Komentar: isi jadwal per hari. Ubah isi di sini untuk mengganti jadwal.
// CARI-JADWAL | Isi jadwal per hari: ScheduleEntry(mapel, guru, warna, jam mulai, lama jam).
// » scheduleData = peta: kode hari -> daftar blok pelajaran. mapOf = daftar tetap.
private val scheduleData = mapOf(
    // » "Sen" = hari Senin.
    "Sen" to listOf(
        // » PDL RPL: guru Pak Surya, mulai JP 1, lama 4 JP.
        ScheduleEntry("PDL RPL", "Pak Surya", ColorPDLRPL, 1, 4),
        // » PDL KL: guru Ms. Dayu N, mulai JP 1, lama 4 JP. Berjalan bersamaan dengan PDL RPL.
        ScheduleEntry("PDL KL", "Ms. Dayu N", ColorPDLKL, 1, 4),
        // » RPL: Pak Rizky, mulai JP 5, lama 2 JP.
        ScheduleEntry("RPL", "Pak Rizky", ColorRPL, 5, 2),
        // » B.ING: Ms. Diah, mulai JP 7, lama 4 JP.
        ScheduleEntry("B.ING", "Ms. Diah", ColorBING, 7, 4)
    ),
    // » "Sel" = hari Selasa.
    "Sel" to listOf(
        // » RPL: Pak Rizky, mulai JP 1, lama 4 JP.
        ScheduleEntry("RPL", "Pak Rizky", ColorRPL, 1, 4),
        // » RPL: Pak Rizky, mulai JP 7, lama 4 JP.
        ScheduleEntry("RPL", "Pak Rizky", ColorRPL, 7, 4)
    ),
    // » "Rab" = hari Rabu.
    "Rab" to listOf(
        // » RPL: Pak Rizky, mulai JP 1, lama 4 JP.
        ScheduleEntry("RPL", "Pak Rizky", ColorRPL, 1, 4),
        // » RPL: Pak Rizky, mulai JP 5, lama 4 JP.
        ScheduleEntry("RPL", "Pak Rizky", ColorRPL, 5, 4),
        // » PABP: Bu Happy / Bu Sari, mulai JP 9, lama 2 JP.
        ScheduleEntry("PABP", "Bu Happy / Bu Sari", ColorPABP, 9, 2)
    ),
    // » "Kam" = hari Kamis.
    "Kam" to listOf(
        // » MAT: Pak Restu, mulai JP 1, lama 4 JP.
        ScheduleEntry("MAT", "Pak Restu", ColorMAT, 1, 4),
        // » RPL: Pak Rizky, mulai JP 5, lama 2 JP.
        ScheduleEntry("RPL", "Pak Rizky", ColorRPL, 5, 2),
        // » KIK: Bu Lulu, mulai JP 7, lama 4 JP.
        ScheduleEntry("KIK", "Bu Lulu", ColorKIK, 7, 4)
    ),
    // » "Jum" = hari Jumat.
    "Jum" to listOf(
        // » BB: Bu Sinta, mulai JP 1, lama 2 JP.
        ScheduleEntry("BB", "Bu Sinta", ColorBB, 1, 2),
        // » PP: Bu Happy, mulai JP 3, lama 2 JP.
        ScheduleEntry("PP", "Bu Happy", ColorPP, 3, 2),
        // » B.INDO: Mr. Esa, mulai JP 5, lama 3 JP.
        ScheduleEntry("B.INDO", "Mr. Esa", ColorBINDO, 5, 3),
        // » PABP: Bu Chika, mulai JP 8, lama 2 JP.
        ScheduleEntry("PABP", "Bu Chika", ColorPABP, 8, 2)
    )
)

// » Komentar: tabel alokasi waktu (jam mulai dan selesai tiap JP).
// ─── Time allocation data ───
// » timeSlots = daftar baris alokasi waktu.
private val timeSlots = listOf(
    // » JP 0: Senin 07.10-08.00, Selasa/Kamis 07.10-07.40, Jumat 07.30-08.00.
    TimeSlot("0", "07.10 - 08.00", "07.10 - 07.40", "0", "07.30 – 08.00"),
    // » JP 1: Senin 08.00-08.40, Selasa/Kamis 07.40-08.20, Jumat 08.00-08.30.
    TimeSlot("1", "08.00 - 08.40", "07.40 - 08.20", "1", "08.00 – 08.30"),
    // » JP 2: Senin 08.40-09.20, Selasa/Kamis 08.20-09.00, Jumat 08.30-09.00.
    TimeSlot("2", "08.40 - 09.20", "08.20 - 09.00", "2", "08.30 – 09.00"),
    // » JP 3: Senin 09.20-10.00, Selasa/Kamis 09.00-09.40, Jumat 09.00-09.30.
    TimeSlot("3", "09.20 - 10.00", "09.00 - 09.40", "3", "09.00 – 09.30"),
    // » JP 4: Senin 10.00-10.40, Selasa/Kamis 09.40-10.20, Jumat 09.30-10.00.
    TimeSlot("4", "10.00 - 10.40", "09.40 - 10.20", "4", "09.30 – 10.00"),
    // » Istirahat pertama: Senin 10.40-11.10, Selasa/Kamis 10.20-10.50, Jumat 10.00-10.30.
    TimeSlot("BREAK", "10.40 - 11.10", "10.20 - 10.50", "BREAK", "10.00 – 10.30"),
    // » JP 5: Senin 11.10-11.50, Selasa/Kamis 10.50-11.30, Jumat 10.30-11.00.
    TimeSlot("5", "11.10 - 11.50", "10.50 - 11.30", "5", "10.30 – 11.00"),
    // » JP 6: Senin 11.50-12.30, Selasa/Kamis 11.30-12.10, Jumat 11.00-11.30.
    TimeSlot("6", "11.50 - 12.30", "11.30 - 12.10", "6", "11.00 – 11.30"),
    // » JP 7: Senin 12.30-13.10, Selasa/Kamis 12.10-12.50, Jumat 11.30-12.00.
    TimeSlot("7", "12.30 - 13.10", "12.10 - 12.50", "7", "11.30 – 12.00"),
    // » JP 8: Senin 13.10-13.50, Selasa/Kamis 12.50-13.30, Jumat 12.00-12.30.
    TimeSlot("8", "13.10 - 13.50", "12.50 - 13.30", "8", "12.00 – 12.30"),
    // » Istirahat kedua (Senin dan Selasa/Kamis): Senin 13.50-14.20, Selasa/Kamis 13.30-14.00. Jumat: JP 9 pada
    // » 12.30-13.00.
    TimeSlot("BREAK", "13.50 - 14.20", "13.30 - 14.00", "9", "12.30 – 13.00"),
    // » JP 9: Senin 14.20-14.50, Selasa/Kamis 14.00-14.40. Jumat: JP 10 pada 13.00-13.30.
    TimeSlot("9", "14.20 - 14.50", "14.00 - 14.40", "10", "13.00 – 13.30"),
    // » JP 10: Senin 14.50-15.20, Selasa/Kamis 14.40-15.20.
    TimeSlot("10", "14.50 - 15.20", "14.40 - 15.20", "", "")
)

// » Komentar: layar menampilkan tabel jadwal dan tabel alokasi waktu.
// CARI-JADWAL | Menampilkan tabel jadwal dan tabel alokasi waktu.
// » @Composable = fungsi penggambar tampilan.
@Composable
// » fun MataPelajaranScreen = layar jadwal pelajaran.
fun MataPelajaranScreen(
    viewModel: KastepViewModel,
    onOpenDrawer: () -> Unit,
    onNavigateToProfile: () -> Unit = {}
) {
    // » userProfile = akun yang login (untuk bar atas).
    val userProfile by viewModel.userProfile.collectAsState()
    // » currentDate = tanggal hari ini.
    val currentDate = viewModel.getCurrentDateString()

    // » Column = susunan atas-bawah seluruh layar. Penuh, latar hitam.
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(KastepBlack)
    ) {
        // » TopBar = bar atas (menu, tanggal, profil).
        TopBar(currentDate = currentDate, userName = userProfile.nama, onMenuClick = onOpenDrawer, onNavigateToProfile = onNavigateToProfile)

        // » Column isi halaman: penuh, bisa digulir, jarak 12dp kiri-kanan dan 8dp atas-bawah.
        Column(
            modifier = Modifier
                .fillMaxSize()
                // » verticalScroll = halaman bisa digulir ke bawah.
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
            // ── Section 1: Class Schedule Table ──
            // » ScheduleTableSection() = tabel jadwal Senin sampai Jumat.
            ScheduleTableSection()

            // » Jarak 20dp.
            Spacer(modifier = Modifier.height(20.dp))

            // ── Section 2: Time Allocation Table ──
            // » TimeAllocationSection() = tabel alokasi waktu.
            TimeAllocationSection()

            // » Jarak 16dp.
            Spacer(modifier = Modifier.height(16.dp))

            // » Komentar: garis hiasan di bagian bawah.
            // Decorative bottom line
            // » Box = garis hiasan bawah.
            Box(
                modifier = Modifier
                    // » Penuh lebar.
                    .fillMaxWidth()
                    // » Tinggi garis 3dp.
                    .height(3.dp)
                    // » background = isi garis dengan gradasi.
                    .background(
                        // » Brush.horizontalGradient = gradasi horizontal.
                        brush = androidx.compose.ui.graphics.Brush.horizontalGradient(
                            // » colors = daftar warna gradasi.
                            colors = listOf(
                                // » Cyan transparan 60% di kiri.
                                KastepCyan.copy(alpha = 0.6f),
                                // » Cyan penuh di tengah.
                                KastepCyan,
                                // » Cyan transparan 60% di kanan.
                                KastepCyan.copy(alpha = 0.6f)
                            )
                        )
                    )
            )
            // » Jarak 16dp di bawah garis.
            Spacer(modifier = Modifier.height(16.dp))
        // » Tutup Column halaman.
        }
    }
}

// » Komentar: kartu tabel jadwal Senin sampai Jumat.
// Kartu tabel jadwal pelajaran Senin sampai Jumat.
// » @Composable = fungsi penggambar tampilan.
@Composable
// » private fun ScheduleTableSection = tabel jadwal (hanya dipakai di file ini).
private fun ScheduleTableSection() {
    // » Card = kartu pembungkus tabel.
    Card(
        // » Penuh lebar.
        modifier = Modifier.fillMaxWidth(),
        // » Sudut membulat 12dp.
        shape = RoundedCornerShape(12.dp),
        // » Warna kartu = ScheduleBg (biru tua gelap).
        colors = CardDefaults.cardColors(containerColor = ScheduleBg)
    ) {
        // » Column isi kartu dengan jarak dalam 12dp.
        Column(modifier = Modifier.padding(12.dp)) {
            // » Komentar: judul tabel.
            // Title
            Text(
                // » Judul sekolah dan semester (kuning, 12sp, tebal). \n = pindah baris.
                text = "JADWAL PELAJARAN SMK NEGERI 2 KUTA SELATAN\nSMT GANJIL TA 26/27",
                // » Warna kuning terang untuk judul sekolah.
                color = Color(0xFFFFD600),
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                // » Jarak antar baris judul 16sp.
                lineHeight = 16.sp,
                // » Penuh lebar, teks di tengah.
                modifier = Modifier.fillMaxWidth()
            )
            // » Jarak 4dp.
            Spacer(modifier = Modifier.height(4.dp))
            // » Teks kelas "XII PPLG" (putih, 20sp, sangat tebal) di tengah.
            Text(
                text = "XII PPLG",
                color = KastepWhite,
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            // » Jarak 12dp sebelum tabel.
            Spacer(modifier = Modifier.height(12.dp))

            // Scrollable table
            // » Box = wadah tabel jadwal.
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    // » horizontalScroll = tabel bisa digeser ke kanan (karena lebar tabel melebihi layar).
                    .horizontalScroll(rememberScrollState())
            ) {
                // » Column = header dan baris hari disusun ke bawah.
                Column {
                    // Header row: period numbers
                    // » ScheduleHeaderRow() = baris angka JP 1 sampai 10.
                    ScheduleHeaderRow()

                    // Day rows
                    // » days = daftar kode hari: Senin, Selasa, Rabu, Kamis, Jumat.
                    val days = listOf("Sen", "Sel", "Rab", "Kam", "Jum")
                    // » Untuk setiap hari...
                    days.forEach { day ->
                        // » ...buat satu baris tabel dengan blok mapel hari itu (diambil dari scheduleData; kalau kosong, daftar
                        // » kosong).
                        ScheduleDayRow(day = day, entries = scheduleData[day] ?: emptyList())
                    }
                }
            }

            // » Jarak 8dp.
            Spacer(modifier = Modifier.height(8.dp))
            // » Catatan kaki tabel (abu, 9sp).
            Text(
                // » Isi catatan: Rabu di dua JP terakhir adalah pembelajaran khusus agama Islam, Kristen, dan Katolik.
                text = "Jadwal hari rabu di dua JP terakhir adalah pembelajaran khusus Agama Islam, Kristen, dan Katolik",
                color = KastepWhite.copy(alpha = 0.6f),
                fontSize = 9.sp,
                lineHeight = 12.sp
            )
        }
    }
}

// » Komentar: baris judul tabel (nomor JP 1 sampai 10).
// Baris judul tabel jadwal: nomor jam 1 sampai 10.
// » @Composable = fungsi penggambar tampilan.
@Composable
// » private fun ScheduleHeaderRow = baris judul kolom nomor JP (hanya dipakai di file ini).
private fun ScheduleHeaderRow() {
    // » cellWidth = lebar satu kotak JP = 36dp.
    val cellWidth = 36.dp
    // » dayLabelWidth = lebar kolom nama hari = 48dp.
    val dayLabelWidth = 48.dp
    // » Row = satu baris judul.
    Row {
        // Empty corner cell for day label
        // » Kotak kosong di pojok kiri atas (tempat nama hari).
        Box(
            // » Ukuran kotak: lebar dayLabelWidth, tinggi 28dp.
            modifier = Modifier
                // » Latar warna judul dan garis tepi tipis.
                .width(dayLabelWidth)
                .height(28.dp)
                .background(HeaderBg)
                // » Garis tepi tipis 0.5dp.
                .border(0.5.dp, BorderColor),
            contentAlignment = Alignment.Center
        ) {
            // » Teks kosong (tidak ada tulisan di pojok).
            Text("", color = KastepWhite, fontSize = 10.sp)
        }
        // Period numbers 1-10
        // » for i in 1..10 = satu kotak untuk setiap JP.
        for (i in 1..10) {
            // » Kotak untuk nomor JP ke-i.
            Box(
                modifier = Modifier
                    // » Lebar kotak 36dp.
                    .width(cellWidth)
                    .height(28.dp)
                    // » Latar judul.
                    .background(HeaderBg)
                    .border(0.5.dp, BorderColor),
                contentAlignment = Alignment.Center
            ) {
                // » Teks nomor JP.
                Text(
                    // » text = nomor i diubah jadi teks.
                    text = i.toString(),
                    color = KastepWhite,
                    // » Ukuran 11sp, tebal.
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

// Satu baris hari di tabel jadwal.
// » @Composable = fungsi penggambar tampilan.
@Composable
// » private fun ScheduleDayRow(day, entries) = satu baris hari di tabel.
private fun ScheduleDayRow(day: String, entries: List<ScheduleEntry>) {
    // » cellWidth = lebar satu kotak JP 36dp.
    val cellWidth = 36.dp
    // » dayLabelWidth = lebar kolom nama hari 48dp.
    val dayLabelWidth = 48.dp
    // » rowHeight = tinggi baris biasa 60dp.
    val rowHeight = 60.dp

    // Determine if this day has two rows (Sen has PDL RPL and PDL KL in parallel)
    // » isSenin = true kalau hari Senin (karena Senin punya dua baris mapel).
    val isSenin = day == "Sen"
    // » actualHeight = tinggi Senin 80dp, hari lain 60dp.
    val actualHeight = if (isSenin) 80.dp else rowHeight

    // » Row = satu baris hari, tinggi sesuai actualHeight.
    Row(modifier = Modifier.height(actualHeight)) {
        // Day label
        // » Box = kotak nama hari di kiri.
        Box(
            modifier = Modifier
                // » Lebar kolom nama hari 48dp, tinggi penuh baris.
                .width(dayLabelWidth)
                .fillMaxHeight()
                // » Latar nama hari (biru tua gelap).
                .background(DayLabelBg)
                .border(0.5.dp, BorderColor),
            contentAlignment = Alignment.Center
        ) {
            // » Teks nama hari (Sen, Sel, dll).
            Text(
                text = day,
                color = KastepWhite,
                // » Ukuran 16sp, sangat tebal.
                fontSize = 16.sp,
                fontWeight = FontWeight.ExtraBold
            )
        }

        // » Kalau hari Senin: pakai tampilan khusus SeninRow.
        if (isSenin) {
            // Senin has two rows: PDL RPL (top) + PDL KL (bottom left), RPL (mid), B.ING (right)
            // » SeninRow = gambar baris Senin (dua mapel berjalan bersamaan).
            SeninRow(entries, cellWidth, actualHeight)
        // » Kalau bukan Senin...
        } else {
            // Normal single row
            // » NormalDayRow = gambar baris biasa, blok mapel diletakkan sesuai jam mulai.
            NormalDayRow(entries, cellWidth)
        }
    }
}

// Baris khusus Senin karena ada dua mapel dalam jam yang sama.
// » @Composable = fungsi penggambar tampilan.
@Composable
// » @Suppress = matikan peringatan parameter yang tidak dipakai (rowHeight).
@Suppress("UNUSED_PARAMETER")
// » private fun SeninRow = baris khusus Senin (hanya dipakai di file ini). Parameter entries tidak dipakai:
// » isi blok ditulis langsung di bawah.
private fun SeninRow(entries: List<ScheduleEntry>, cellWidth: Dp, @Suppress("unused") rowHeight: Dp) {
    // » Komentar: susunan Senin dari gambar jadwal aslinya.
    // Senin layout from the image:
    // » Baris atas: PDL RPL (JP 1-4), RPL (JP 5-6), B.ING (JP 7-10).
    // Top half: PDL RPL spans period 1-4, then RPL spans 5-6, then B.ING spans 7-10
    // » Baris bawah: PDL KL (JP 1-4).
    // Bottom half: PDL KL spans period 1-4

    // » Column = dua baris Senin, susun ke bawah. Tinggi penuh.
    Column(modifier = Modifier.fillMaxHeight()) {
        // Top row
        // » Row baris atas. weight(1f) = membagi tinggi sama dengan baris bawah.
        Row(modifier = Modifier.weight(1f)) {
            // PDL RPL: periods 1-4
            // » SubjectBlock = satu kotak mapel (dibuat di bawah). Di sini PDL RPL.
            SubjectBlock(
                // » subject = nama mapel PDL RPL.
                subject = "PDL RPL",
                // » teacher = nama guru Pak Surya.
                teacher = "Pak Surya",
                // » color = warna kotak (biru muda).
                color = ColorPDLRPL,
                // » textColor = warna tulisan hitam (karena latar terang).
                textColor = Color.Black,
                // » width = lebar 4 kotak JP (4 x 36dp).
                width = cellWidth * 4,
                // » modifier = tinggi penuh baris.
                modifier = Modifier.fillMaxHeight()
            )
            // RPL: periods 5-6
            // » SubjectBlock RPL di JP 5-6.
            SubjectBlock(
                // » subject = RPL.
                subject = "RPL",
                // » teacher = Pak Rizky.
                teacher = "Pak Rizky",
                // » color = merah tua (ColorRPL).
                color = ColorRPL,
                // » textColor = putih.
                textColor = KastepWhite,
                // » width = 2 kotak JP.
                width = cellWidth * 2,
                modifier = Modifier.fillMaxHeight()
            )
            // B.ING: periods 7-10
            // » SubjectBlock B.ING di JP 7-10.
            SubjectBlock(
                // » subject = B.ING.
                subject = "B.ING",
                // » teacher = Ms. Diah.
                teacher = "Ms. Diah",
                // » color = hijau.
                color = ColorBING,
                // » textColor = hitam.
                textColor = Color.Black,
                // » width = 4 kotak JP.
                width = cellWidth * 4,
                modifier = Modifier.fillMaxHeight()
            )
        }
        // Bottom row
        // » Row baris bawah Senin.
        Row(modifier = Modifier.weight(1f)) {
            // PDL KL: periods 1-4
            // » SubjectBlock PDL KL di JP 1-4.
            SubjectBlock(
                // » subject = PDL KL.
                subject = "PDL KL",
                // » teacher = Ms. Dayu N.
                teacher = "Ms. Dayu N",
                // » color = magenta.
                color = ColorPDLKL,
                // » textColor = putih.
                textColor = KastepWhite,
                // » width = 4 kotak JP.
                width = cellWidth * 4,
                modifier = Modifier.fillMaxHeight()
            )
            // Empty remaining cells
            // » Box kosong untuk sisa kotak di baris bawah (JP 5-10).
            Box(
                // » Lebar = 6 kotak JP, tinggi penuh (kosong).
                modifier = Modifier
                    .width(cellWidth * 6)
                    .fillMaxHeight()
            )
        }
    }
}

// Baris hari biasa: kotak mapel ditempatkan sesuai jam mulai.
@Composable
// » @Composable = fungsi penggambar tampilan.
private fun NormalDayRow(entries: List<ScheduleEntry>, cellWidth: Dp) {
    // » private fun NormalDayRow = baris hari biasa.
    // Fill periods 1-10, placing subject blocks where they belong
    // » Komentar: isi JP 1 sampai 10, blok mapel diletakkan di jam mulai masing-masing.
    val sortedEntries = entries.sortedBy { it.startPeriod }
// » sortedEntries = daftar blok diurutkan dari jam mulai paling awal.

    // » Row = satu baris, tinggi penuh.
    Row(modifier = Modifier.fillMaxHeight()) {
        // » currentPeriod = JP yang sudah terisi sampai sekarang (mulai dari 1).
        var currentPeriod = 1
        // » Untuk setiap blok mapel (entry) di urutan jam...
        for (entry in sortedEntries) {
            // Fill empty gaps before this entry
            // » Kalau ada jeda kosong sebelum blok ini (jam mulai lebih besar dari JP sekarang)...
            if (entry.startPeriod > currentPeriod) {
                // » ...hitung lebar jeda = jumlah JP kosong dikali lebar kotak.
                val gapWidth = cellWidth * (entry.startPeriod - currentPeriod)
                // » Box = kotak kosong untuk mengisi jeda.
                Box(
                    modifier = Modifier
                        // » Modifier kotak kosong: tinggi penuh baris.
                        .width(gapWidth)
                        .fillMaxHeight()
                        // » Garis tepi tipis (30% transparan) untuk jeda kosong.
                        .border(0.5.dp, BorderColor.copy(alpha = 0.3f))
                )
            }

            // » textColor = warna tulisan sesuai warna blok. when = pilih berdasarkan warna:
            val textColor = when (entry.color) {
                // » Biru tua RPL dan PP: tulisan putih.
                ColorRPL, ColorPP -> KastepWhite
                // » Magenta PDL KL: tulisan putih.
                ColorPDLKL -> KastepWhite
                // » Warna lain: tulisan hitam.
                else -> Color.Black
            }

            // » SubjectBlock = gambar kotak mapel dari data entry.
            SubjectBlock(
                // » subject = nama mapel.
                subject = entry.subject,
                // » teacher = nama guru.
                teacher = entry.teacher,
                // » color = warna blok.
                color = entry.color,
                // » textColor = warna tulisan (dari pilihan di atas).
                textColor = textColor,
                // » width = lebar kotak = satu kotak JP dikali jumlah JP (spanPeriods).
                width = cellWidth * entry.spanPeriods,
                // » Tinggi penuh baris.
                modifier = Modifier.fillMaxHeight()
            )
            // » currentPeriod = JP berikutnya setelah blok ini (JP mulai + jumlah JP).
            currentPeriod = entry.startPeriod + entry.spanPeriods
        }

        // Fill remaining empty cells at the end
        // » Kalau masih ada sisa JP sampai JP 10...
        if (currentPeriod <= 10) {
            // » remainingWidth = lebar sisa kotak kosong (11 - JP sekarang) dikali lebar satu kotak.
            val remainingWidth = cellWidth * (11 - currentPeriod)
            // » Box = kotak kosong untuk sisa jam di akhir baris.
            Box(
                // » Lebar sisa, tinggi penuh baris.
                modifier = Modifier
                    .width(remainingWidth)
                    .fillMaxHeight()
                    // » Garis tepi tipis untuk sisa kosong.
                    .border(0.5.dp, BorderColor.copy(alpha = 0.3f))
            )
        }
    }
}

// » Komentar: satu kotak mapel berwarna (nama mapel dan nama guru).
// Satu kotak mapel berwarna berisi nama mapel dan guru.
// » @Composable = fungsi penggambar tampilan.
@Composable
// » private fun SubjectBlock = kotak mapel (hanya dipakai di file ini). Parameter: nama mapel, guru, warna,
// » warna tulisan, lebar, dan modifier.
private fun SubjectBlock(
    subject: String,
    teacher: String,
    color: Color,
    textColor: Color,
    width: Dp,
    modifier: Modifier = Modifier
) {
    // » Box = kotak mapel.
    Box(
        // » modifier = pengaturan yang diberikan pemanggil (tinggi).
        modifier = modifier
            // » width = lebar kotak.
            .width(width)
            // » background = warna mapel.
            .background(color)
            // » Garis tepi tipis.
            .border(0.5.dp, BorderColor),
        // » Isi di tengah kotak.
        contentAlignment = Alignment.Center
    ) {
        // » Column = nama mapel di atas, nama guru di bawah.
        Column(
            // » Isi rata tengah secara horizontal.
            horizontalAlignment = Alignment.CenterHorizontally,
            // » Isi rata tengah secara vertikal.
            verticalArrangement = Arrangement.Center,
            // » Jarak dalam 2dp.
            modifier = Modifier.padding(2.dp)
        ) {
            // » Text = nama mapel.
            Text(
                // » text = subject.
                text = subject,
                // » color = warna tulisan dari parameter.
                color = textColor,
                // » Ukuran 12sp.
                fontSize = 12.sp,
                // » Sangat tebal (ExtraBold).
                fontWeight = FontWeight.ExtraBold,
                textAlign = TextAlign.Center,
                // » maxLines = 1: satu baris saja.
                maxLines = 1,
                // » Kalau terlalu panjang, dipotong dengan "...".
                overflow = TextOverflow.Ellipsis
            )
            // » Text = nama guru.
            Text(
                // » text = teacher.
                text = teacher,
                // » Warna tulisan dibuat sedikit transparan (75%).
                color = textColor.copy(alpha = 0.75f),
                // » Ukuran 8sp (kecil).
                fontSize = 8.sp,
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                // » lineHeight = jarak baris 10sp.
                lineHeight = 10.sp
            )
        }
    }
}

// » Komentar: bagian tabel alokasi waktu.
// ─── Time Allocation Section ───
// » Komentar: kartu tabel alokasi waktu, jam mulai dan selesai tiap JP.
// Kartu tabel alokasi waktu: jam mulai dan selesai setiap jam pelajaran.
// » @Composable = fungsi penggambar tampilan.
@Composable
// » private fun TimeAllocationSection = tabel alokasi waktu (hanya dipakai di file ini).
private fun TimeAllocationSection() {
    // » Card = kartu pembungkus tabel.
    Card(
        // » Penuh lebar.
        modifier = Modifier.fillMaxWidth(),
        // » Sudut membulat 12dp.
        shape = RoundedCornerShape(12.dp),
        // » Warna kartu = ScheduleBg.
        colors = CardDefaults.cardColors(containerColor = ScheduleBg)
    ) {
        // » Column isi kartu, jarak dalam 12dp.
        Column(modifier = Modifier.padding(12.dp)) {
            // » Text = judul alokasi waktu (dua baris).
            Text(
                // » Isi judul: alokasi jam pelajaran semester ganjil SMK Negeri 2 Kuta Selatan TA 2026/2027.
                text = "ALOKASI JAM PELAJARAN SEMESTER GANJIL\nSMK NEGERI 2 KUTA SELATAN TA 2026/2027",
                // » Warna putih.
                color = KastepWhite,
                // » Ukuran 11sp.
                fontSize = 11.sp,
                // » Tebal.
                fontWeight = FontWeight.Bold,
                // » Rata tengah.
                textAlign = TextAlign.Center,
                // » Jarak baris 15sp.
                lineHeight = 15.sp,
                // » Penuh lebar.
                modifier = Modifier.fillMaxWidth()
            )
            // » Jarak 12dp.
            Spacer(modifier = Modifier.height(12.dp))

            // Scrollable time table
            // » Box = wadah tabel alokasi waktu.
            Box(
                modifier = Modifier
                    // » Penuh lebar.
                    .fillMaxWidth()
                    // » horizontalScroll = tabel bisa digeser ke kanan.
                    .horizontalScroll(rememberScrollState())
            ) {
                // » Column = header lalu baris-baris jam.
                Column {
                    // Header
                    // » TimeTableHeader() = baris judul kolom (JAM KE-, SENIN, dst).
                    TimeTableHeader()
                    // Rows
                    // » Untuk setiap baris alokasi waktu (slot), dengan nomor urut index...
                    timeSlots.forEachIndexed { index, slot ->
                        // » isBreak = true kalau baris ini istirahat.
                        val isBreak = slot.period == "BREAK"
                        // » TimeTableRow = gambar baris ini. isEven = nomor urut genap (untuk warna selang-seling).
                        TimeTableRow(slot = slot, isBreak = isBreak, isEven = index % 2 == 0)
                    }
                }
            }
        }
    }
}

// » Komentar: judul tabel alokasi waktu.
// Judul tabel alokasi waktu (jam ke, Senin, Selasa sampai Jumat).
// » @Composable = fungsi penggambar tampilan.
@Composable
// » private fun TimeTableHeader = baris judul tabel alokasi waktu (hanya dipakai di file ini).
private fun TimeTableHeader() {
    // » colWidth1 = lebar kolom "JAM KE-" = 56dp.
    val colWidth1 = 56.dp
    // » colWidth2 = lebar kolom Senin = 96dp.
    val colWidth2 = 96.dp
    // » colWidth3 = lebar kolom Selasa-Kamis = 96dp.
    val colWidth3 = 96.dp
    // » colWidth4 = lebar kolom JAM KE- (untuk Jumat) = 56dp.
    val colWidth4 = 56.dp
    // » colWidth5 = lebar kolom Jumat = 96dp.
    val colWidth5 = 96.dp
    // » headerColor = biru untuk latar judul.
    val headerColor = Color(0xFF1565C0)

    // » Row = satu baris judul.
    Row {
        // » Sel judul "JAM KE-" (kolom 1).
        TimeHeaderCell("JAM KE-", colWidth1, headerColor)
        // » Sel judul "SENIN".
        TimeHeaderCell("SENIN", colWidth2, headerColor)
        // » Sel judul "SELASA - KAMIS".
        TimeHeaderCell("SELASA - KAMIS", colWidth3, headerColor)
        // » Sel judul "JAM KE-" (kolom 4, untuk Jumat).
        TimeHeaderCell("JAM KE-", colWidth4, headerColor)
        // » Sel judul "JUMAT".
        TimeHeaderCell("JUMAT", colWidth5, headerColor)
    }
}

// » Komentar: satu sel judul tabel alokasi waktu.
// Satu sel judul tabel alokasi waktu.
// » @Composable = fungsi penggambar tampilan.
@Composable
// » private fun TimeHeaderCell = satu sel judul (teks, lebar, warna latar).
private fun TimeHeaderCell(text: String, width: Dp, bgColor: Color) {
    // » Box = kotak sel judul.
    Box(
        modifier = Modifier
            // » width = lebar sel.
            .width(width)
            // » Tinggi sel 36dp.
            .height(36.dp)
            // » Latar warna judul.
            .background(bgColor)
            // » Garis tepi tipis.
            .border(0.5.dp, BorderColor),
        // » Isi di tengah.
        contentAlignment = Alignment.Center
    ) {
        // » Text = teks judul sel.
        Text(
            // » text = isi judul.
            text = text,
            color = KastepWhite,
            // » Ukuran 9sp, tebal.
            fontSize = 9.sp,
            fontWeight = FontWeight.Bold,
            // » Rata tengah.
            textAlign = TextAlign.Center,
            // » maksimal 2 baris.
            maxLines = 2,
            // » lineHeight = jarak baris 11sp.
            lineHeight = 11.sp
        )
    }
}

// » Komentar: satu baris jam pelajaran atau istirahat.
// Satu baris jam pelajaran atau istirahat.
// » @Composable = fungsi penggambar tampilan.
@Composable
// » private fun TimeTableRow = satu baris tabel alokasi waktu (hanya dipakai di file ini).
private fun TimeTableRow(slot: TimeSlot, isBreak: Boolean, isEven: Boolean) {
    // » colWidth1 = lebar kolom JP (56dp), sama dengan judul.
    val colWidth1 = 56.dp
    // » colWidth2 = lebar kolom Senin (96dp).
    val colWidth2 = 96.dp
    // » colWidth3 = lebar kolom Selasa-Kamis (96dp).
    val colWidth3 = 96.dp
    // » colWidth4 = lebar kolom JP untuk Jumat (56dp).
    val colWidth4 = 56.dp
    // » colWidth5 = lebar kolom Jumat (96dp).
    val colWidth5 = 96.dp
    // » rowBg: kalau istirahat, latar kuning transparan 15%.
    val rowBg = if (isBreak) Color(0xFFFFEB3B).copy(alpha = 0.15f)
    // » Kalau tidak: genap pakai ScheduleBg, ganjil pakai ScheduleBg transparan 80% (selang-seling).
    else if (isEven) ScheduleBg else ScheduleBg.copy(alpha = 0.8f)
    // » textColor = kuning kalau istirahat, putih kalau jam pelajaran.
    val textColor = if (isBreak) Color(0xFFFFEB3B) else KastepWhite

    // » Row = satu baris, berisi lima sel.
    Row {
        // » Sel nomor JP (atau tulisan BREAK).
        TimeDataCell(slot.period, colWidth1, rowBg, textColor, isBreak)
        // » Sel jam Senin.
        TimeDataCell(slot.senin, colWidth2, rowBg, textColor, isBreak)
        // » Sel jam Selasa-Kamis.
        TimeDataCell(slot.selasaKamis, colWidth3, rowBg, textColor, isBreak)
        // » Sel nomor JP untuk Jumat.
        TimeDataCell(slot.periodJumat, colWidth4, rowBg, textColor, isBreak)
        // » Sel jam Jumat.
        TimeDataCell(slot.jumat, colWidth5, rowBg, textColor, isBreak)
    }
}

// » Komentar: satu sel isi tabel alokasi waktu.
// Satu sel isi tabel alokasi waktu.
// » @Composable = fungsi penggambar tampilan.
@Composable
// » private fun TimeDataCell = satu sel isi (teks, lebar, warna latar, warna teks, dan tebal atau tidak).
private fun TimeDataCell(text: String, width: Dp, bgColor: Color, textColor: Color, isBold: Boolean) {
    // » Box = kotak sel.
    Box(
        modifier = Modifier
            // » width = lebar sel.
            .width(width)
            // » Tinggi sel 28dp.
            .height(28.dp)
            // » Latar warna sel.
            .background(bgColor)
            // » Garis tepi tipis.
            .border(0.5.dp, BorderColor),
        // » Isi di tengah.
        contentAlignment = Alignment.Center
    ) {
        // » Text = isi sel.
        Text(
            // » text = isi sel.
            text = text,
            // » color = warna tulisan.
            color = textColor,
            // » Ukuran 9sp.
            fontSize = 9.sp,
            // » Tebal kalau isBold (baris istirahat).
            fontWeight = if (isBold) FontWeight.Bold else FontWeight.Normal,
            // » Rata tengah.
            textAlign = TextAlign.Center,
            // » maksimal 1 baris.
            maxLines = 1
        )
    }
}
