// ==========================================================================
// PETA FILE · KASTEP (Kelompok 1) · SplashScreen.kt
// Isi file : Layar pembuka dengan logo dan animasi memuat.
// Penanda  : CARI-MULAI
// Cara cari: VS Code Ctrl+Shift+F (Android Studio Ctrl+Shift+F) lalu ketik
//            "CARI-" untuk semua penanda, atau salah satu nama di baris Penanda.
// ==========================================================================
//  BACA INI DULU
//  Layar pembuka berisi ikon dompet (digambar Canvas), tulisan KASTEP, dan garis loading animasi. Tampil sekitar 2,5 detik lalu
//  onNavigateToLogin dipanggil. NavGraph memutuskan tujuan: Dashboard bila sesi masih aktif, Login bila tidak.
// » Alamat folder (package) file ini di dalam proyek.
package com.kastep.app.ui.screens

// » Memanggil Animatable (dari androidx.compose.animation.core) supaya bisa dipakai di file ini.
import androidx.compose.animation.core.Animatable
// » Memanggil LinearEasing (dari androidx.compose.animation.core) supaya bisa dipakai di file ini.
import androidx.compose.animation.core.LinearEasing
// » Memanggil RepeatMode (dari androidx.compose.animation.core) supaya bisa dipakai di file ini.
import androidx.compose.animation.core.RepeatMode
// » Memanggil animateFloat (dari androidx.compose.animation.core) supaya bisa dipakai di file ini.
import androidx.compose.animation.core.animateFloat
// » Memanggil infiniteRepeatable (dari androidx.compose.animation.core) supaya bisa dipakai di file ini.
import androidx.compose.animation.core.infiniteRepeatable
// » Memanggil rememberInfiniteTransition (dari androidx.compose.animation.core) supaya bisa dipakai di file
// » ini.
import androidx.compose.animation.core.rememberInfiniteTransition
// » Memanggil tween (dari androidx.compose.animation.core) supaya bisa dipakai di file ini.
import androidx.compose.animation.core.tween
// » Canvas = kanvas kosong untuk menggambar sendiri (garis, lingkaran, grafik).
import androidx.compose.foundation.Canvas
// » background = mengisi warna/gradien latar sebuah tampilan.
import androidx.compose.foundation.background
// » Memanggil Arrangement (dari androidx.compose.foundation.layout) supaya bisa dipakai di file ini.
import androidx.compose.foundation.layout.Arrangement
// » Memanggil Box (dari androidx.compose.foundation.layout) supaya bisa dipakai di file ini.
import androidx.compose.foundation.layout.Box
// » Memanggil Column (dari androidx.compose.foundation.layout) supaya bisa dipakai di file ini.
import androidx.compose.foundation.layout.Column
// » Memanggil Spacer (dari androidx.compose.foundation.layout) supaya bisa dipakai di file ini.
import androidx.compose.foundation.layout.Spacer
// » Memanggil fillMaxSize (dari androidx.compose.foundation.layout) supaya bisa dipakai di file ini.
import androidx.compose.foundation.layout.fillMaxSize
// » Memanggil height (dari androidx.compose.foundation.layout) supaya bisa dipakai di file ini.
import androidx.compose.foundation.layout.height
// » Memanggil size (dari androidx.compose.foundation.layout) supaya bisa dipakai di file ini.
import androidx.compose.foundation.layout.size
// » Memanggil width (dari androidx.compose.foundation.layout) supaya bisa dipakai di file ini.
import androidx.compose.foundation.layout.width
// » Memanggil Text (dari androidx.compose.material3) supaya bisa dipakai di file ini.
import androidx.compose.material3.Text
// » @Composable = penanda bahwa sebuah fungsi menggambar tampilan.
import androidx.compose.runtime.Composable
// » Memanggil LaunchedEffect (dari androidx.compose.runtime) supaya bisa dipakai di file ini.
import androidx.compose.runtime.LaunchedEffect
// » Memanggil getValue (dari androidx.compose.runtime) supaya bisa dipakai di file ini.
import androidx.compose.runtime.getValue
// » Memanggil remember (dari androidx.compose.runtime) supaya bisa dipakai di file ini.
import androidx.compose.runtime.remember
// » Alignment = pengatur posisi isi (tengah, atas, bawah, kiri, kanan).
import androidx.compose.ui.Alignment
// » Modifier = daftar 'dekorasi' sebuah tampilan: ukuran, jarak, warna latar, klik, dll.
import androidx.compose.ui.Modifier
// » Memanggil CornerRadius (dari androidx.compose.ui.geometry) supaya bisa dipakai di file ini.
import androidx.compose.ui.geometry.CornerRadius
// » Offset = titik koordinat (x, y) di Canvas.
import androidx.compose.ui.geometry.Offset
// » Memanggil Size (dari androidx.compose.ui.geometry) supaya bisa dipakai di file ini.
import androidx.compose.ui.geometry.Size
// » Color = tipe warna Compose; dipakai untuk menulis warna seperti Color.White atau Color(0xFF...).
import androidx.compose.ui.graphics.Color
// » StrokeCap = bentuk ujung garis (bulat atau rata).
import androidx.compose.ui.graphics.StrokeCap
// » Stroke = gaya menggambar garis tepi (tebal garis, ujung).
import androidx.compose.ui.graphics.drawscope.Stroke
// » FontWeight = ketebalan huruf (Normal, Medium, Bold, ExtraBold).
import androidx.compose.ui.text.font.FontWeight
// » dp = satuan ukuran Android yang sama di semua layar (mis. 16.dp).
import androidx.compose.ui.unit.dp
// » sp = satuan ukuran huruf Android (mengikuti pengaturan ukuran font di HP).
import androidx.compose.ui.unit.sp
// » Memanggil KastepBlack (dari com.kastep.app.ui.theme) supaya bisa dipakai di file ini.
import com.kastep.app.ui.theme.KastepBlack
// » Memanggil KastepCyan (dari com.kastep.app.ui.theme) supaya bisa dipakai di file ini.
import com.kastep.app.ui.theme.KastepCyan
// » Memanggil KastepGray (dari com.kastep.app.ui.theme) supaya bisa dipakai di file ini.
import com.kastep.app.ui.theme.KastepGray
// » Memanggil delay (dari kotlinx.coroutines) supaya bisa dipakai di file ini.
import kotlinx.coroutines.delay
// » Memanggil padding (dari androidx.compose.foundation.layout) supaya bisa dipakai di file ini.
import androidx.compose.foundation.layout.padding

// » Komentar: penanda pencarian. Layar tampil 2,5 detik, lalu pindah (diatur di NavGraph).
// CARI-MULAI | Tampil 2,5 detik lalu pindah ke Login atau Dashboard (diatur di NavGraph).
// » @Composable = fungsi penggambar tampilan.
@Composable
// » fun SplashScreen = layar pembuka. onNavigateToLogin = fungsi untuk pindah ke layar berikutnya.
fun SplashScreen(onNavigateToLogin: () -> Unit) {
    // » LaunchedEffect(Unit) = jalankan blok di dalamnya sekali saat layar pertama tampil.
    LaunchedEffect(Unit) {
        // » delay(2500) = tunggu 2,5 detik.
        delay(2500L)
        // » Setelah menunggu, panggil onNavigateToLogin (pindah ke Login atau Dashboard).
        onNavigateToLogin()
    }

    // » rememberInfiniteTransition = pengatur animasi yang berulang terus.
    val infiniteTransition = rememberInfiniteTransition(label = "loading")
    // » progress = nilai animasi dari 0 sampai 1, dipakai untuk menggerakkan garis loading.
    val progress by infiniteTransition.animateFloat(
        // » initialValue = 0, awal animasi.
        initialValue = 0f,
        // » targetValue = 1, akhir animasi.
        targetValue = 1f,
        // » infiniteRepeatable = animasi diulang terus-menerus.
        animationSpec = infiniteRepeatable(
            // » tween(1500) = satu putaran 1,5 detik. LinearEasing = kecepatan tetap (tidak melambat).
            animation = tween(1500, easing = LinearEasing),
            // » RepeatMode.Restart = setelah sampai 1, mulai lagi dari 0 (tidak bolak-balik).
            repeatMode = RepeatMode.Restart
        ),
        label = "progress"
    )

    // » Box = wadah penuh layar.
    Box(
        // » modifier = penuh sebesar layar.
        modifier = Modifier
            // » Penuh.
            .fillMaxSize()
            // » Latar hitam KASTEP.
            .background(KastepBlack),
        // » Isi di tengah layar.
        contentAlignment = Alignment.Center
    ) {
        // » Column = logo, judul, dan garis loading disusun ke bawah.
        Column(
            // » Isi rata tengah secara horizontal.
            horizontalAlignment = Alignment.CenterHorizontally,
            // » Isi rata tengah secara vertikal.
            verticalArrangement = Arrangement.Center
        ) {
            // Wallet Icon drawn with Canvas
            // » Canvas 80dp = area gambar ikon dompet.
            Canvas(modifier = Modifier.size(80.dp)) {
                // » cyan = warna dompet (KastepCyan).
                val cyan = KastepCyan
                // » strokeWidth = tebal garis 3dp (dalam pixel).
                val strokeWidth = 3.dp.toPx()

                // Wallet body
                // » drawRoundRect = badan dompet berupa kotak sudut membulat (garis saja).
                drawRoundRect(
                    // » color = warna cyan.
                    color = cyan,
                    // » topLeft = posisi pojok kiri atas badan: 10% dari kiri, 30% dari atas.
                    topLeft = Offset(size.width * 0.1f, size.height * 0.3f),
                    // » size = ukuran badan: 70% lebar, 55% tinggi.
                    size = Size(size.width * 0.7f, size.height * 0.55f),
                    // » cornerRadius = sudut membulat 8dp.
                    cornerRadius = CornerRadius(8.dp.toPx()),
                    // » style = Stroke = digambar sebagai garis (bukan isi).
                    style = Stroke(width = strokeWidth)
                )

                // Card flap sticking out top
                // » drawRoundRect = kartu yang menyembul di atas dompet.
                drawRoundRect(
                    color = cyan,
                    // » topLeft = posisi kartu: 20% kiri, 12% atas.
                    topLeft = Offset(size.width * 0.2f, size.height * 0.12f),
                    // » size = ukuran kartu: 55% lebar, 35% tinggi.
                    size = Size(size.width * 0.55f, size.height * 0.35f),
                    // » cornerRadius = sudut membulat 6dp.
                    cornerRadius = CornerRadius(6.dp.toPx()),
                    // » Garis kartu lebih tipis (80% dari tebal utama).
                    style = Stroke(width = strokeWidth * 0.8f)
                )

                // Clasp circle on right side
                // » claspCenterX = posisi horizontal pusat gesper (72% lebar).
                val claspCenterX = size.width * 0.72f
                // » claspCenterY = posisi vertikal pusat gesper (57% tinggi).
                val claspCenterY = size.height * 0.57f
                // » drawCircle = lingkaran garis di gesper, jari-jari 8dp.
                drawCircle(
                    color = cyan,
                    radius = 8.dp.toPx(),
                    center = Offset(claspCenterX, claspCenterY),
                    style = Stroke(width = strokeWidth)
                )
                // » drawCircle kedua = titik isi di tengah gesper, jari-jari 3dp.
                drawCircle(
                    color = cyan,
                    radius = 3.dp.toPx(),
                    center = Offset(claspCenterX, claspCenterY)
                )
            }

            // » Jarak 16dp.
            Spacer(modifier = Modifier.height(16.dp))

            // KASTEP text
            // » Teks "KASTEP" (putih, 32sp, tebal).
            Text(
                text = "KASTEP",
                color = Color.White,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                // » letterSpacing 4sp = jarak antar huruf lebar.
                letterSpacing = 4.sp
            )

            // » Jarak 12dp.
            Spacer(modifier = Modifier.height(12.dp))

            // Animated progress line
            // » Canvas garis loading: lebar 160dp, tinggi 3dp.
            Canvas(
                modifier = Modifier
                    .width(160.dp)
                    .height(3.dp)
            ) {
                // Background line
                // » drawLine = garis latar biru tua yang selalu tampil.
                drawLine(
                    // » Warna biru tua (#1A237E).
                    color = Color(0xFF1A237E),
                    // » start = titik kiri di tengah tinggi.
                    start = Offset(0f, size.height / 2),
                    // » end = titik kanan di tengah tinggi.
                    end = Offset(size.width, size.height / 2),
                    // » strokeWidth = tinggi kanvas (garis setebal kanvas).
                    strokeWidth = size.height,
                    // » StrokeCap.Round = ujung garis membulat.
                    cap = StrokeCap.Round
                )
                // Animated progress
                // » lineWidth = panjang bagian yang bergerak = 40% lebar kanvas.
                val lineWidth = size.width * 0.4f
                // » startX = posisi awal bagian bergerak. Berubah sesuai progress, jadi bergerak dari kiri ke kanan.
                val startX = (size.width + lineWidth) * progress - lineWidth
                // » drawLine cyan = bagian garis yang bergerak.
                drawLine(
                    color = KastepCyan,
                    // » start = titik awal bagian bergerak, dibatasi tidak kurang dari 0.
                    start = Offset(startX.coerceAtLeast(0f), size.height / 2),
                    // » end = titik akhir, dibatasi tidak lebih dari lebar kanvas.
                    end = Offset((startX + lineWidth).coerceAtMost(size.width), size.height / 2),
                    strokeWidth = size.height,
                    cap = StrokeCap.Round
                )
                // Dots at ends
                // » Titik bulat cyan di ujung kiri (radius 4dp).
                drawCircle(
                    color = KastepCyan,
                    radius = 4.dp.toPx(),
                    center = Offset(0f, size.height / 2)
                )
                // » Titik bulat cyan di ujung kanan.
                drawCircle(
                    color = KastepCyan,
                    radius = 4.dp.toPx(),
                    center = Offset(size.width, size.height / 2)
                )
            }
        }

        // » Komentar: teks "Memuat..." di bagian bawah layar.
        // Teks memuat di bagian bawah (sebelumnya tergambar dua kali)
        // » Teks "Memuat..." (abu, 14sp).
        Text(
            text = "Memuat...",
            color = KastepGray,
            fontSize = 14.sp,
            // » align BottomCenter = di tengah bawah layar, dengan jarak bawah 48dp.
            modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 48.dp)
        )
    }
}
