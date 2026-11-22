// ==========================================================================
// PETA FILE · KASTEP (Kelompok 1) · PembayaranBerhasilScreen.kt
// Isi file : Layar sukses setelah membayar (animasi maskot, tombol Konfirmasi WA).
// Penanda  : CARI-PEMBAYARAN, CARI-WHATSAPP
// Cara cari: VS Code Ctrl+Shift+F (Android Studio Ctrl+Shift+F) lalu ketik
//            "CARI-" untuk semua penanda, atau salah satu nama di baris Penanda.
// ==========================================================================
//  BACA INI DULU : LAYAR "PEMBAYARAN BERHASIL"
//  Tampil setelah BENDAHARA (Admin) mencatat pembayaran (processPaymentRange sukses). Siswa tidak sampai ke layar ini,
//  karena siswa hanya mengirim konfirmasi WA. Layar mengambil baris riwayat yang baru dicatat (sebanyak lastPaymentCount,
//  yang terbaru ada di indeks 0), menjumlahkannya, lalu menampilkan jumlah, nama siswa, rentang bulan, dan metode.
//  Ada animasi: uang melayang, kembang api, maskot (KasMascot), dan kubah hijau.
//  Tombol putih "Konfirmasi WA" memanggil sendWhatsAppConfirmation (dari PembayaranScreen.kt) untuk meneruskan bukti ke siswa.
//  Tombol biru tua "Kembali" menutup layar.
//  Semua gambar di sini digambar dengan kode (Canvas), bukan file gambar.
// » Alamat folder (package) file ini di dalam proyek.
package com.kastep.app.ui.screens

// » Memanggil Animatable (dari androidx.compose.animation.core) supaya bisa dipakai di file ini.
import androidx.compose.animation.core.Animatable
// » Memanggil FastOutSlowInEasing (dari androidx.compose.animation.core) supaya bisa dipakai di file ini.
import androidx.compose.animation.core.FastOutSlowInEasing
// » Memanggil LinearOutSlowInEasing (dari androidx.compose.animation.core) supaya bisa dipakai di file ini.
import androidx.compose.animation.core.LinearOutSlowInEasing
// » Memanggil RepeatMode (dari androidx.compose.animation.core) supaya bisa dipakai di file ini.
import androidx.compose.animation.core.RepeatMode
// » Memanggil Spring (dari androidx.compose.animation.core) supaya bisa dipakai di file ini.
import androidx.compose.animation.core.Spring
// » Memanggil animateFloat (dari androidx.compose.animation.core) supaya bisa dipakai di file ini.
import androidx.compose.animation.core.animateFloat
// » Memanggil infiniteRepeatable (dari androidx.compose.animation.core) supaya bisa dipakai di file ini.
import androidx.compose.animation.core.infiniteRepeatable
// » Memanggil rememberInfiniteTransition (dari androidx.compose.animation.core) supaya bisa dipakai di file
// » ini.
import androidx.compose.animation.core.rememberInfiniteTransition
// » Memanggil spring (dari androidx.compose.animation.core) supaya bisa dipakai di file ini.
import androidx.compose.animation.core.spring
// » Memanggil tween (dari androidx.compose.animation.core) supaya bisa dipakai di file ini.
import androidx.compose.animation.core.tween
// » Canvas = kanvas kosong untuk menggambar sendiri (garis, lingkaran, grafik).
import androidx.compose.foundation.Canvas
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
// » RoundedCornerShape = bentuk persegi dengan sudut membulat.
import androidx.compose.foundation.shape.RoundedCornerShape
// » verticalScroll = membuat isi bisa digulir naik-turun.
import androidx.compose.foundation.verticalScroll
// » Icons = daftar ikon bawaan Material.
import androidx.compose.material.icons.Icons
// » Ikon bawaan Material: Send (versi terisi).
import androidx.compose.material.icons.automirrored.filled.Send
// » Memanggil HorizontalDivider (dari androidx.compose.material3) supaya bisa dipakai di file ini.
import androidx.compose.material3.HorizontalDivider
// » Memanggil Icon (dari androidx.compose.material3) supaya bisa dipakai di file ini.
import androidx.compose.material3.Icon
// » Memanggil Text (dari androidx.compose.material3) supaya bisa dipakai di file ini.
import androidx.compose.material3.Text
// » @Composable = penanda bahwa sebuah fungsi menggambar tampilan.
import androidx.compose.runtime.Composable
// » Memanggil LaunchedEffect (dari androidx.compose.runtime) supaya bisa dipakai di file ini.
import androidx.compose.runtime.LaunchedEffect
// » Memanggil collectAsState (dari androidx.compose.runtime) supaya bisa dipakai di file ini.
import androidx.compose.runtime.collectAsState
// » Memanggil getValue (dari androidx.compose.runtime) supaya bisa dipakai di file ini.
import androidx.compose.runtime.getValue
// » Memanggil remember (dari androidx.compose.runtime) supaya bisa dipakai di file ini.
import androidx.compose.runtime.remember
// » Alignment = pengatur posisi isi (tengah, atas, bawah, kiri, kanan).
import androidx.compose.ui.Alignment
// » Modifier = daftar 'dekorasi' sebuah tampilan: ukuran, jarak, warna latar, klik, dll.
import androidx.compose.ui.Modifier
// » clip = memotong tampilan mengikuti sebuah bentuk (mis. sudut bulat atau lingkaran).
import androidx.compose.ui.draw.clip
// » Memanggil CornerRadius (dari androidx.compose.ui.geometry) supaya bisa dipakai di file ini.
import androidx.compose.ui.geometry.CornerRadius
// » Offset = titik koordinat (x, y) di Canvas.
import androidx.compose.ui.geometry.Offset
// » Memanggil Rect (dari androidx.compose.ui.geometry) supaya bisa dipakai di file ini.
import androidx.compose.ui.geometry.Rect
// » Memanggil Size (dari androidx.compose.ui.geometry) supaya bisa dipakai di file ini.
import androidx.compose.ui.geometry.Size
// » Brush = 'cat' warna, termasuk gradien (warna berubah halus).
import androidx.compose.ui.graphics.Brush
// » Color = tipe warna Compose; dipakai untuk menulis warna seperti Color.White atau Color(0xFF...).
import androidx.compose.ui.graphics.Color
// » Memanggil Outline (dari androidx.compose.ui.graphics) supaya bisa dipakai di file ini.
import androidx.compose.ui.graphics.Outline
// » Path = jalur garis yang digambar di Canvas (dipakai garis grafik).
import androidx.compose.ui.graphics.Path
// » Memanggil Shape (dari androidx.compose.ui.graphics) supaya bisa dipakai di file ini.
import androidx.compose.ui.graphics.Shape
// » Memanggil TransformOrigin (dari androidx.compose.ui.graphics) supaya bisa dipakai di file ini.
import androidx.compose.ui.graphics.TransformOrigin
// » Memanggil DrawScope (dari androidx.compose.ui.graphics.drawscope) supaya bisa dipakai di file ini.
import androidx.compose.ui.graphics.drawscope.DrawScope
// » Stroke = gaya menggambar garis tepi (tebal garis, ujung).
import androidx.compose.ui.graphics.drawscope.Stroke
// » Memanggil rotate (dari androidx.compose.ui.graphics.drawscope) supaya bisa dipakai di file ini.
import androidx.compose.ui.graphics.drawscope.rotate
// » Memanggil graphicsLayer (dari androidx.compose.ui.graphics) supaya bisa dipakai di file ini.
import androidx.compose.ui.graphics.graphicsLayer
// » LocalContext = mengambil Context aplikasi di dalam tampilan (untuk membuka WhatsApp, Toast, dll).
import androidx.compose.ui.platform.LocalContext
// » FontWeight = ketebalan huruf (Normal, Medium, Bold, ExtraBold).
import androidx.compose.ui.text.font.FontWeight
// » TextAlign = perataan teks (kiri, tengah, kanan).
import androidx.compose.ui.text.style.TextAlign
// » TextOverflow = cara menangani teks yang terlalu panjang (Ellipsis = dipotong dengan '...').
import androidx.compose.ui.text.style.TextOverflow
// » Memanggil Density (dari androidx.compose.ui.unit) supaya bisa dipakai di file ini.
import androidx.compose.ui.unit.Density
// » Memanggil LayoutDirection (dari androidx.compose.ui.unit) supaya bisa dipakai di file ini.
import androidx.compose.ui.unit.LayoutDirection
// » dp = satuan ukuran Android yang sama di semua layar (mis. 16.dp).
import androidx.compose.ui.unit.dp
// » sp = satuan ukuran huruf Android (mengikuti pengaturan ukuran font di HP).
import androidx.compose.ui.unit.sp
// » Memanggil KastepViewModel (dari com.kastep.app.data) supaya bisa dipakai di file ini.
import com.kastep.app.data.KastepViewModel
// » Memanggil KastepBlack (dari com.kastep.app.ui.theme) supaya bisa dipakai di file ini.
import com.kastep.app.ui.theme.KastepBlack
// » Memanggil KastepWhite (dari com.kastep.app.ui.theme) supaya bisa dipakai di file ini.
import com.kastep.app.ui.theme.KastepWhite
// » Memanggil cos (dari kotlin.math) supaya bisa dipakai di file ini.
import kotlin.math.cos
// » Memanggil roundToLong (dari kotlin.math) supaya bisa dipakai di file ini.
import kotlin.math.roundToLong
// » Memanggil sin (dari kotlin.math) supaya bisa dipakai di file ini.
import kotlin.math.sin

// » SuccessGreen = warna hijau utama layar sukses (#12BE64). private val = hanya dipakai di file ini.
private val SuccessGreen = Color(0xFF12BE64)
// » SuccessGreenDark = hijau tua (#0A8A47), untuk bagian bawah.
private val SuccessGreenDark = Color(0xFF0A8A47)
// » DotGreen = hijau terang (#1ED775) untuk titik-titik dekorasi.
private val DotGreen = Color(0xFF1ED775)
// » DividerDark = hijau sangat gelap (#0A2A17) untuk garis pemisah.
private val DividerDark = Color(0xFF0A2A17)

// » FwRed = merah (#E53935) untuk kembang api.
private val FwRed = Color(0xFFE53935)
// » FwYellow = kuning (#FFC107) untuk kembang api.
private val FwYellow = Color(0xFFFFC107)
// » FwGreen = hijau (#43A047) untuk kembang api.
private val FwGreen = Color(0xFF43A047)
// » FwBlue = biru (#1E40AF) untuk kembang api.
private val FwBlue = Color(0xFF1E40AF)

/** Height of the black hero area (dots, fireworks, mascot). */
// » HeroHeight = tinggi area hitam atas (titik, kembang api, maskot) = 320dp.
private val HeroHeight = 320.dp
/** Where the green dome starts; the dome overlaps the bottom of the mascot. */
// » DomeTop = posisi awal kubah hijau = 292dp dari atas (menimpa bagian bawah maskot).
private val DomeTop = 292.dp
/** How much the dome edges sit below its peak. */
// » DomeRise = seberapa tinggi puncak kubah dari tepinya = 45dp.
private val DomeRise = 45.dp

/** Green section with a convex dome on top, like the design. */
// » private object DomeShape : Shape = bentuk kubah kustom, dibuat sekali (object).
private object DomeShape : Shape {
    // Bentuk kubah melengkung di bagian atas kartu Pembayaran Berhasil.
    // » createOutline = hitung bentuk kubah untuk ukuran tertentu.
    override fun createOutline(size: Size, layoutDirection: LayoutDirection, density: Density): Outline {
        // » rise = nilai DomeRise dalam pixel (toPx = ubah dp ke pixel).
        val rise = with(density) { DomeRise.toPx() }
        // » path = jalur gambar kubah.
        val path = Path().apply {
            // » moveTo(0, rise) = mulai di tepi kiri, sedikit ke bawah.
            moveTo(0f, rise)
            // » quadraticBezierTo = garis lengkung ke tepi kanan dengan puncak di tengah atas (bentuk kubah).
            quadraticBezierTo(size.width / 2f, -rise, size.width, rise)
            // » lineTo kanan bawah = turun ke pojok kanan bawah.
            lineTo(size.width, size.height)
            // » lineTo kiri bawah = ke pojok kiri bawah.
            lineTo(0f, size.height)
            // » close = tutup jalur ke titik awal.
            close()
        }
        // » Outline.Generic(path) = pakai jalur itu sebagai bentuk kartu.
        return Outline.Generic(path)
    }
}

// CARI-PEMBAYARAN CARI-WHATSAPP | Menjumlahkan baris yang baru dicatat bendahara lalu menampilkan jumlah & nama; tombol WA memakai sendWhatsAppConfirmation.
// » @Composable = fungsi penggambar tampilan.
@Composable
// » fun PembayaranBerhasilScreen = layar yang tampil setelah bendahara mencatat pembayaran.
fun PembayaranBerhasilScreen(
    viewModel: KastepViewModel,
    onOpenDrawer: () -> Unit,
    onNavigateToProfile: () -> Unit = {},
    onKembali: () -> Unit
) {
    // » context = konteks Android (dipakai untuk membuka WhatsApp).
    val context = LocalContext.current
    // » userProfile = akun yang login.
    val userProfile by viewModel.userProfile.collectAsState()
    // » records = daftar riwayat pembayaran (ikut berubah otomatis).
    val records by viewModel.paymentRecords.collectAsState()
    // » jumlahBaris = berapa baris riwayat yang baru dicatat (diatur ViewModel).
    val jumlahBaris by viewModel.lastPaymentCount.collectAsState()
    // » currentDate = tanggal hari ini.
    val currentDate = viewModel.getCurrentDateString()

    // Baris yang baru dicatat ada di depan (indeks 0 = bulan terakhir). Dibalik supaya urut dari bulan paling awal.
    // » batch = ambil jumlahBaris riwayat terbaru (take), lalu balik urutannya (reversed) supaya dari bulan
    // » paling awal.
    val batch = records.take(jumlahBaris).reversed()
    // » last = riwayat paling baru (null kalau tidak ada).
    val last = records.firstOrNull()
    // » totalJumlah = jumlah uang semua baris di batch.
    val totalJumlah = batch.sumOf { it.jumlah }
    // » totalDenda = total denda semua baris di batch.
    val totalDenda = batch.sumOf { it.denda }
    // » labelBulan = teks judul bulan: diatur oleh when di bawah.
    val labelBulan = when {
        // » Kalau cuma satu bulan (atau kurang)...
        batch.size <= 1 -> last?.bulan.orEmpty()
        // » ...pakai bulan dari riwayat terakhir (orEmpty = kosong kalau null).
        // » Kalau lebih dari satu bulan: "bulan pertama s/d bulan terakhir".
        else -> "${batch.first().bulan} s/d ${batch.last().bulan}"
    }

    // » PembayaranBerhasilContent = tampilan layar (dibuat di bawah). Data di atas dikirim lewat parameter.
    PembayaranBerhasilContent(
        currentDate = currentDate,
        userName = userProfile.nama,
        // » jumlah = total uang yang dicatat.
        jumlah = totalJumlah,
        // » atasNama = nama siswa dari riwayat terbaru ("-" kalau kosong).
        atasNama = last?.namaSiswa ?: "-",
        // » metode = metode bayar dari riwayat terbaru (orEmpty = kosong kalau null).
        metode = last?.metode.orEmpty(),
        // » bulan = teks label bulan.
        bulan = labelBulan,
        onOpenDrawer = onOpenDrawer,
        onNavigateToProfile = onNavigateToProfile,
        // » onKonfirmasiWa = tombol WhatsApp. null kalau tidak ada riwayat (tombol tidak aktif).
        onKonfirmasiWa = last?.let { r ->
            // » Kalau ada riwayat, fungsi ini yang dijalankan saat tombol WA ditekan.
            {
                // » sendWhatsAppConfirmation = buka WhatsApp dengan rincian: nama siswa, daftar bulan, metode, total, denda.
                sendWhatsAppConfirmation(context, r.namaSiswa, batch.joinToString(", ") { it.bulan }, r.metode, totalJumlah, totalDenda, sudahDicatat = true)
            }
        },
        // » onKembali = fungsi saat tombol Kembali ditekan.
        onKembali = onKembali
    )
}

// » Komentar: tampilan dipisahkan dari ViewModel supaya bisa dipratinjau.
/**
 * Stateless UI so the layout can be previewed / screenshot-tested without a ViewModel.
 * [animate] = false renders the final frame (used for screenshots).
 */
// » @Composable = fungsi penggambar tampilan.
@Composable
// » internal fun PembayaranBerhasilContent = tampilan murni (tanpa ViewModel). internal = bisa dipakai file
// » lain di aplikasi.
internal fun PembayaranBerhasilContent(
    // » currentDate = tanggal yang ditampilkan di bar atas.
    currentDate: String,
    // » userName = nama akun untuk bar atas.
    userName: String,
    // » jumlah = total uang yang ditampilkan dan dihitung animasi.
    jumlah: Long,
    // » atasNama = nama siswa yang ditampilkan.
    atasNama: String,
    // » metode = metode bayar yang ditampilkan.
    metode: String,
    // » bulan = teks rentang bulan.
    bulan: String,
    // » onOpenDrawer = fungsi membuka menu samping.
    onOpenDrawer: () -> Unit = {},
    // » onNavigateToProfile = fungsi ke halaman Profil.
    onNavigateToProfile: () -> Unit = {},
    // » onKonfirmasiWa = fungsi tombol WhatsApp (boleh null).
    onKonfirmasiWa: (() -> Unit)? = null,
    // » onKembali = fungsi tombol Kembali.
    onKembali: () -> Unit = {},
    // » animate = true: tampilkan animasi. false: langsung tampil hasil akhir (dipakai untuk tangkapan layar).
    animate: Boolean = true
) {
    // » Komentar: animasi saat layar baru dibuka.
    // ---- one-shot entry animations ----
    // » enter = nilai animasi masuk. Mulai dari 0 (kalau animasi) atau 1 (langsung jadi).
    val enter = remember { Animatable(if (animate) 0f else 1f) }
    // » counter = nilai animasi penghitung uang. Sama seperti enter.
    val counter = remember { Animatable(if (animate) 0f else 1f) }
    // » LaunchedEffect(animate) = jalankan blok ini saat layar pertama tampil.
    LaunchedEffect(animate) {
        // » Kalau animasi aktif...
        if (animate) {
            // » ...animasi enter bergerak ke 1 dengan efek pegas (spring) supaya memantul sedikit.
            enter.animateTo(1f, spring(dampingRatio = 0.45f, stiffness = Spring.StiffnessLow))
        }
    }
    // » LaunchedEffect(animate, jumlah) = jalankan lagi setiap kali jumlah uang berubah.
    LaunchedEffect(animate, jumlah) {
        if (animate) {
            // » Kembalikan penghitung ke 0 dulu (tanpa animasi).
            counter.snapTo(0f)
            // » Hitung naik ke 1 selama 1100ms, mulai setelah jeda 250ms, dengan gerakan melambat di akhir.
            counter.animateTo(1f, tween(durationMillis = 1100, delayMillis = 250, easing = LinearOutSlowInEasing))
        }
    }

    // » Komentar: animasi berulang terus-menerus saat layar terbuka.
    // ---- looping idle animations ----
    // » rememberInfiniteTransition = pengatur animasi berulang.
    val loop = rememberInfiniteTransition(label = "berhasil")
    // » bobAnim = nilai naik-turun (dari -1 ke 1) untuk gerakan naik-turun maskot.
    val bobAnim by loop.animateFloat(
        // » tween(1600) = durasi satu arah 1,6 detik. RepeatMode.Reverse = bolak-balik (naik lalu turun).
        -1f, 1f, infiniteRepeatable(tween(1600, easing = FastOutSlowInEasing), RepeatMode.Reverse), label = "bob"
    )
    // » burstA = nilai animasi kembang api kiri-kanan A (0 sampai 1, bolak-balik 900ms).
    val burstA by loop.animateFloat(
        0f, 1f, infiniteRepeatable(tween(900, easing = FastOutSlowInEasing), RepeatMode.Reverse), label = "burstA"
    )
    // » burstB = nilai animasi kembang api B (bolak-balik 1150ms).
    val burstB by loop.animateFloat(
        0f, 1f, infiniteRepeatable(tween(1150, easing = FastOutSlowInEasing), RepeatMode.Reverse), label = "burstB"
    )
    // » bob = gerakan naik-turun maskot. Kalau animate mati, nilainya 0 (diam).
    val bob = if (animate) bobAnim else 0f
    // » pulseA = besar-kecil kembang api A (dari 0.88 sampai 1.06). Kalau animate mati, 1 (ukuran biasa).
    val pulseA = if (animate) 0.88f + 0.18f * burstA else 1f
    // » pulseB = besar-kecil kembang api B.
    val pulseB = if (animate) 0.88f + 0.18f * burstB else 1f

    // » shownAmount = angka uang yang sedang naik (jumlah x nilai penghitung), dibulatkan, diformat rupiah.
    val shownAmount = "Rp " + KastepViewModel.formatRupiah((jumlah * counter.value).roundToLong())
    // » finalAmount = angka uang akhir, diformat rupiah.
    val finalAmount = "Rp " + KastepViewModel.formatRupiah(jumlah)
    // » namaRapi = nama siswa dengan huruf awal besar (toTitleCase).
    val namaRapi = atasNama.toTitleCase()

    // » Column = susunan atas-bawah seluruh layar. Penuh dan latar hitam.
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(KastepBlack)
    ) {
        // » TopBar = bar atas (menu, tanggal, profil).
        TopBar(currentDate = currentDate, userName = userName, onMenuClick = onOpenDrawer, onNavigateToProfile = onNavigateToProfile)

        // » Box bagian isi di bawah bar atas. weight(1f) = mengisi sisa tinggi layar.
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            // Hero: dot grid + fireworks + mascot
            // » Box area atas (hero): titik, kembang api, dan maskot.
            Box(
                modifier = Modifier
                    // » Penuh lebar, tinggi HeroHeight (320dp).
                    .fillMaxWidth()
                    .height(HeroHeight),
                // » BottomCenter = isi rata bawah tengah.
                contentAlignment = Alignment.BottomCenter
            ) {
                // » DotGrid() = pola titik-titik dekorasi (digambar dengan kode).
                DotGrid()
                // » Fireworks = kembang api kiri dan kanan. Besarnya mengikuti animasi pulse dan enter.
                Fireworks(scaleRight = pulseA * enter.value, scaleLeft = pulseB * enter.value)
                // » KasMascot = maskot KASTEP (digambar di KasMascot.kt).
                KasMascot(
                    // » Ukuran maskot 272dp lebar, tinggi HeroHeight - 10dp.
                    modifier = Modifier
                        .size(width = 272.dp, height = HeroHeight - 10.dp)
                        // » graphicsLayer = atur transformasi visual (skala, transparansi, geser, putar) di dalam lambda.
                        .graphicsLayer {
                            // » e = nilai animasi masuk (0 sampai 1).
                            val e = enter.value
                            // » Komentar: skala akhir 1.12 supaya maskot memenuhi area hero seperti desain.
                            // 1.12 = final size; slightly larger than the canvas so the
                            // mascot fills the hero like the design.
                            // » scaleX = dari 55% sampai 112% saat masuk.
                            scaleX = 1.12f * (0.55f + 0.45f * e)
                            // » scaleY = sama seperti scaleX.
                            scaleY = 1.12f * (0.55f + 0.45f * e)
                            // » alpha = transparansi: 0 (tidak terlihat) sampai 1 (jelas).
                            alpha = e.coerceIn(0f, 1f)
                            // » transformOrigin = titik skala di tengah bawah maskot.
                            transformOrigin = TransformOrigin(0.5f, 1f)
                            // » translationY = geser naik-turun maskot (bob x 4dp).
                            translationY = bob * 4.dp.toPx()
                            // » rotationZ = sedikit berputar ikut gerakan (bob x 1.5 derajat).
                            rotationZ = bob * 1.5f
                        }
                )
            }

            // Green dome section, overlapping the bottom of the mascot like the design
            // » Column bagian hijau: kubah sukses yang menimpa bagian bawah maskot.
            Column(
                modifier = Modifier
                    // » Penuh sebesar bagian bawah.
                    .fillMaxSize()
                    // » padding top = DomeTop (292dp) supaya kubah mulai dari posisi itu.
                    .padding(top = DomeTop)
                    // » clip(DomeShape) = potong area menjadi bentuk kubah.
                    .clip(DomeShape)
                    // » background = warna gradasi.
                    .background(
                        // » verticalGradient = dari hijau terang (#18C96C) lalu ke hijau sukses.
                        Brush.verticalGradient(listOf(Color(0xFF18C96C), SuccessGreen, SuccessGreen))
                    )
                    // » verticalScroll = bagian bisa digulir kalau layar pendek.
                    .verticalScroll(rememberScrollState()),
                // » Isi rata tengah secara horizontal.
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // » Jarak 24dp di atas.
                Spacer(Modifier.height(24.dp))
                // » FlyingMoney = ilustrasi uang yang melayang.
                FlyingMoney(
                    // » Ukuran uang 110 x 100dp.
                    modifier = Modifier
                        // » graphicsLayer: geser dan putar ikut gerakan naik-turun.
                        .size(width = 110.dp, height = 100.dp)
                        .graphicsLayer {
                            // » translationY = geser naik-turun uang.
                            translationY = -bob * 3.dp.toPx()
                            // » rotationZ = putar uang ikut gerakan.
                            rotationZ = -bob * 2f
                            // » e = nilai masuk, dibatasi 0 sampai 1.
                            val e = enter.value.coerceIn(0f, 1f)
                            // » scaleX = dari 70% sampai 100% saat masuk.
                            scaleX = 0.7f + 0.3f * e
                            // » scaleY = sama.
                            scaleY = 0.7f + 0.3f * e
                        }
                )
                // » Jarak 10dp.
                Spacer(Modifier.height(10.dp))
                // » Box rata tengah untuk tulisan jumlah.
                Box(contentAlignment = Alignment.Center) {
                    // » Komentar: lebar angka akhir disiapkan (teks transparan) supaya saat angka naik, tulisan tidak bergeser.
                    // Reserve the final width so the count-up doesn't make the line jump.
                    // » Teks jumlah akhir dengan warna TRANSPARAN (hanya untuk menyediakan lebar).
                    Text(finalAmount, color = Color.Transparent, fontSize = 26.sp, fontWeight = FontWeight.Bold)
                    // » Teks jumlah yang sedang naik, putih, 26sp, tebal.
                    Text(shownAmount, color = KastepWhite, fontSize = 26.sp, fontWeight = FontWeight.Bold)
                }
                // » Jarak 2dp.
                Spacer(Modifier.height(2.dp))
                // » Teks judul "PEMBAYARAN BERHASIL".
                Text(
                    // » text = isi judul.
                    text = "PEMBAYARAN BERHASIL",
                    color = KastepWhite,
                    // » Ukuran 24sp.
                    fontSize = 24.sp,
                    // » Sangat tebal (ExtraBold).
                    fontWeight = FontWeight.ExtraBold,
                    // » Jarak huruf 0.5sp.
                    letterSpacing = 0.5.sp,
                    // » Rata tengah.
                    textAlign = TextAlign.Center
                )
                // » Kalau metode atau bulan tidak kosong...
                if (metode.isNotBlank() || bulan.isNotBlank()) {
                    // » Jarak 10dp.
                    Spacer(Modifier.height(10.dp))
                    // » Row untuk dua label kecil (chip), jarak 8dp.
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        // » Label metode (contoh: Cash).
                        if (metode.isNotBlank()) InfoChip(metode)
                        // » Label bulan (contoh: Juli 2026).
                        if (bulan.isNotBlank()) InfoChip(bulan)
                    }
                }
                // » Jarak 18dp.
                Spacer(Modifier.height(18.dp))

                // » Garis pemisah tebal (1.5dp) hijau gelap.
                HorizontalDivider(color = DividerDark, thickness = 1.5.dp)
                // » Baris detail "Jumlah Pembayaran" dengan nilai jumlah akhir. \n = label dua baris.
                DetailRow(label = "Jumlah\nPembayaran", value = finalAmount)
                // » Garis pemisah lagi.
                HorizontalDivider(color = DividerDark, thickness = 1.5.dp)
                // » Baris detail "Atas Nama" dengan nama siswa yang sudah rapi.
                DetailRow(label = "Atas\nNama", value = namaRapi)
                // » Garis pemisah.
                HorizontalDivider(color = DividerDark, thickness = 1.5.dp)

                // » Jarak 18dp.
                Spacer(Modifier.height(18.dp))

                // » Row = dua tombol di bawah.
                Row(
                    // » Jarak kiri-kanan 18dp.
                    modifier = Modifier.padding(horizontal = 18.dp),
                    // » spacedBy(12dp) = jarak antar tombol 12dp, rata tengah.
                    horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterHorizontally)
                ) {
                    // » Kalau ada fungsi WhatsApp (tidak null), tampilkan tombol Konfirmasi WA.
                    if (onKonfirmasiWa != null) {
                        // » Komentar: tombol putih, kirim konfirmasi WA dari layar ini supaya layar sukses dilihat dulu.
                        // White pill: send the WhatsApp confirmation from here so the success
                        // screen is always seen first.
                        // » Row = tombol putih berbentuk pil.
                        Row(
                            // » modifier tombol.
                            modifier = Modifier
                                // » Lebar 166dp.
                                .width(166.dp)
                                // » Tinggi 42dp.
                                .height(42.dp)
                                // » Sudut membulat 21dp (bentuk pil).
                                .clip(RoundedCornerShape(21.dp))
                                // » Latar putih.
                                .background(KastepWhite)
                                // » clickable: saat ditekan, jalankan onKonfirmasiWa.
                                .clickable(onClick = onKonfirmasiWa),
                            // » Isi tombol di tengah secara horizontal.
                            horizontalArrangement = Arrangement.Center,
                            // » Isi di tengah secara vertikal.
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // » Icon = ikon kirim (Send).
                            Icon(
                                Icons.AutoMirrored.Filled.Send, contentDescription = null,
                                // » Warna ikon hijau tua, ukuran 18dp.
                                tint = SuccessGreenDark, modifier = Modifier.size(18.dp)
                            )
                            // » Jarak 8dp.
                            Spacer(Modifier.width(8.dp))
                            // » Teks "Konfirmasi WA", hijau tua, 16sp, semi tebal.
                            Text("Konfirmasi WA", color = SuccessGreenDark, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                        }
                    }
                    // » Komentar: tombol Kembali berwarna gradasi biru tua.
                    // Navy gradient "Kembali" button (as in the design)
                    // » Box = tombol Kembali.
                    Box(
                        // » modifier tombol.
                        modifier = Modifier
                            // » Lebar 166dp, tinggi 42dp.
                            .width(166.dp)
                            // » Sudut bulat 21dp.
                            .height(42.dp)
                            // » (catatan) clip membentuk pil.
                            .clip(RoundedCornerShape(21.dp))
                            // » Gradasi vertikal dari biru tua (#1A237E) ke biru terang (#5B7CFA).
                            .background(Brush.verticalGradient(listOf(Color(0xFF1A237E), Color(0xFF5B7CFA))))
                            // » Garis tepi putih tipis (25% transparan) 1dp.
                            .border(1.dp, Color.White.copy(alpha = 0.25f), RoundedCornerShape(21.dp))
                            // » clickable: saat ditekan, jalankan onKembali.
                            .clickable(onClick = onKembali),
                        // » Isi di tengah.
                        contentAlignment = Alignment.Center
                    ) {
                        // » Teks "Kembali", putih, 18sp.
                        Text("Kembali", color = KastepWhite, fontSize = 18.sp, fontWeight = FontWeight.Medium)
                    }
                }
                // » Jarak 24dp di bawah.
                Spacer(Modifier.height(24.dp))
            }
        }
    }
}

// » Komentar: label kecil untuk info bulan dan metode.
// Label kecil berisi info (bulan dan metode).
// » @Composable = fungsi penggambar tampilan.
@Composable
// » private fun InfoChip = label kecil (hanya dipakai di file ini).
private fun InfoChip(text: String) {
    // » Text = isi label.
    Text(
        text = text,
        // » Warna putih.
        color = KastepWhite,
        // » Ukuran 13sp.
        fontSize = 13.sp,
        // » Semi tebal.
        fontWeight = FontWeight.Medium,
        // » modifier label.
        modifier = Modifier
            // » Sudut dibuat bulat penuh (50dp) = bentuk pil.
            .clip(RoundedCornerShape(50))
            // » Latar hitam transparan 18%.
            .background(Color.Black.copy(alpha = 0.18f))
            // » Jarak dalam 12dp kiri-kanan dan 4dp atas-bawah.
            .padding(horizontal = 12.dp, vertical = 4.dp)
    )
}

// Baris Jumlah Pembayaran dan Atas Nama.
@Composable
// » private fun DetailRow = satu baris detail: label di kiri, nilai di kanan (hanya dipakai di file ini).
private fun DetailRow(label: String, value: String) {
    // » Row = label dan nilai berdampingan.
    Row(
        // » Penuh lebar.
        modifier = Modifier
            .fillMaxWidth()
            // » Jarak dalam 18dp kiri-kanan dan 8dp atas-bawah.
            .padding(horizontal = 18.dp, vertical = 8.dp),
        // » SpaceBetween = label ke kiri, nilai ke kanan.
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // » Teks label, putih, 18sp. lineHeight = jarak antar baris 26sp.
        Text(text = label, color = KastepWhite, fontSize = 18.sp, lineHeight = 26.sp)
        // » Jarak 16dp antara label dan nilai.
        Spacer(Modifier.width(16.dp))
        // » Teks nilai.
        Text(
            text = value,
            color = KastepWhite,
            // » Komentar: nama siswa bisa panjang, jadi ukurannya diperkecil.
            // Real student names are long; shrink them instead of breaking the layout.
            // » Kalau nilai lebih dari 14 karakter: 19sp. Kalau tidak: 24sp.
            fontSize = if (value.length > 14) 19.sp else 24.sp,
            // » lineHeight = jarak antar baris 24sp.
            lineHeight = 24.sp,
            // » maxLines = maksimal 2 baris.
            maxLines = 2,
            // » Kalau tetap kepanjangan, dipotong dengan "..." di akhir.
            overflow = TextOverflow.Ellipsis,
            // » Rata kanan.
            textAlign = TextAlign.End,
            // » weight(1f, fill = false) = nilai mengambil ruang sesuai isi, tidak harus penuh.
            modifier = Modifier.weight(1f, fill = false)
        )
    }
}

// » Komentar: contoh hasil toTitleCase.
/** "NI KADEK LINA" -> "Ni Kadek Lina" */
// » fun String.toTitleCase() = fungsi tambahan untuk teks: tiap kata diberi huruf awal kapital.
private fun String.toTitleCase(): String =
    // » lowercase() = semua huruf kecil dulu. split(" ") = pisah per spasi. filter = buang bagian kosong.
    lowercase().split(" ").filter { it.isNotBlank() }
        // » joinToString(" ") = gabung kembali dengan spasi; replaceFirstChar = huruf pertama tiap kata dibuat
        // » kapital.
        .joinToString(" ") { w -> w.replaceFirstChar { it.uppercase() } }
        // » ifBlank = kalau hasilnya kosong, kembalikan teks asli.
        .ifBlank { this }

// » Komentar: uang kertas melayang dengan kilau bintang.
/** Falling banknotes with sparkles (matches the design illustration). */
// » @Composable = fungsi penggambar tampilan.
@Composable
// » private fun FlyingMoney = ilustrasi uang melayang (hanya dipakai di file ini).
private fun FlyingMoney(modifier: Modifier = Modifier) {
    // » billFill = warna isi lembar uang (hijau muda).
    val billFill = Color(0xFFC8E6C0)
    // » billInner = warna bagian dalam lembar (hijau).
    val billInner = Color(0xFFA5D6A7)
    // » outline = warna garis tepi (hijau sangat gelap).
    val outline = Color(0xFF1B2A1B)
    // » coin = warna koin (kuning).
    val coin = Color(0xFFFFC107)
    // » Canvas = area gambar bebas.
    Canvas(modifier = modifier) {
        // » w = lebar area gambar.
        val w = size.width
        // » h = tinggi area gambar.
        val h = size.height
        // » bw = lebar satu lembar uang = 40% lebar area.
        val bw = w * 0.40f
        // » bh = tinggi satu lembar = 17% tinggi area.
        val bh = h * 0.17f
        // » stroke = tebal garis tepi 1.6dp (dalam pixel).
        val stroke = 1.6.dp.toPx()

        // » Komentar: fungsi gambar satu lembar uang.
        // Menggambar satu lembar uang kertas untuk animasi berhasil.
        // » fun bill(cx, cy, deg) = gambar satu lembar di pusat (cx, cy) dengan sudut putar deg.
        fun bill(cx: Float, cy: Float, deg: Float) {
            // » rotate(deg) = putar semua gambar di dalam blok ini di sekitar pusat lembar.
            rotate(deg, pivot = Offset(cx, cy)) {
                // » tl = pojok kiri atas lembar.
                val tl = Offset(cx - bw / 2, cy - bh / 2)
                // » sz = ukuran lembar (lebar bw, tinggi bh).
                val sz = Size(bw, bh)
                // » r = sudut membulat 3dp.
                val r = CornerRadius(3.dp.toPx())
                // » drawRoundRect = gambar lembar luar (warna isi).
                drawRoundRect(billFill, tl, sz, r)
                // » drawRoundRect kedua = gambar bagian dalam lembar (lebih gelap), di tengah lembar.
                drawRoundRect(
                    billInner, tl + Offset(bw * 0.1f, bh * 0.18f),
                    Size(bw * 0.8f, bh * 0.64f), r
                )
                // » Garis tepi lembar (outline, tebal stroke).
                drawRoundRect(outline, tl, sz, r, style = Stroke(stroke))
                // » drawOval = koin kuning oval di tengah lembar.
                drawOval(coin, Offset(cx - bw * 0.12f, cy - bh * 0.22f), Size(bw * 0.24f, bh * 0.44f))
                // » Garis tepi koin, lebih tipis.
                drawOval(
                    outline, Offset(cx - bw * 0.12f, cy - bh * 0.22f),
                    Size(bw * 0.24f, bh * 0.44f), style = Stroke(stroke * 0.7f)
                )
            }
        }

        // » Komentar: fungsi kilau bintang.
        // Menggambar kilau bintang kecil untuk animasi.
        // » fun sparkle(cx, cy, s) = gambar kilau berbentuk belah ketupat dengan jari-jari s.
        fun sparkle(cx: Float, cy: Float, s: Float) {
            // » Path() = jalur baru. apply = isi jalurnya.
            val path = Path().apply {
                // » moveTo atas, lineTo kanan, bawah, kiri, lalu close = jalur belah ketupat.
                moveTo(cx, cy - s); lineTo(cx + s, cy); lineTo(cx, cy + s); lineTo(cx - s, cy); close()
            }
            // » drawPath(isi) = isi kilau dengan warna koin.
            drawPath(path, coin)
            // » drawPath(garis) = garis tepi kilau.
            drawPath(path, outline, style = Stroke(stroke * 0.6f))
        }

        // » Komentar: dua lembar di atas, dua di tengah, satu jatuh di bawah.
        // Two notes on top, two in the middle, one falling at the bottom.
        // » Lembar uang 1 di kanan atas, miring -12 derajat (posisi dalam persen kanvas).
        bill(w * 0.44f, h * 0.10f, -12f)
        // » Lembar uang 2 di bawahnya, miring -14 derajat.
        bill(w * 0.56f, h * 0.30f, -14f)
        // » Lembar uang 3 di kiri tengah, miring -24 derajat.
        bill(w * 0.28f, h * 0.58f, -24f)
        // » Lembar uang 4 di kanan tengah, miring 22 derajat.
        bill(w * 0.72f, h * 0.55f, 22f)
        // » Lembar uang 5 di bawah, miring 68 derajat (paling bawah).
        bill(w * 0.52f, h * 0.84f, 68f)

        // » Kilau 1 di kanan atas.
        sparkle(w * 0.82f, h * 0.02f, 3.dp.toPx())
        // » Kilau 2 di kiri tengah.
        sparkle(w * 0.18f, h * 0.24f, 2.6.dp.toPx())
        // » Kilau 3 di kanan.
        sparkle(w * 0.92f, h * 0.36f, 3.dp.toPx())
        // » Kilau 4 di kiri.
        sparkle(w * 0.08f, h * 0.46f, 2.6.dp.toPx())
        // » Kilau 5 di bawah.
        sparkle(w * 0.40f, h * 0.96f, 2.4.dp.toPx())
    }
}

// » Komentar: pola titik hijau di belakang maskot.
/** Green halftone dot pattern behind the mascot (round cluster, uniform dots). */
// » @Composable = fungsi penggambar tampilan.
@Composable
// » private fun DotGrid = pola titik-titik dekorasi.
private fun DotGrid() {
    // » Canvas penuh layar hero.
    Canvas(modifier = Modifier.fillMaxSize()) {
        // » step = jarak antar titik 26dp.
        val step = 26.dp.toPx()
        // » radius = jari-jari satu titik 2.4dp.
        val radius = 2.4.dp.toPx()
        // » cx = pusat horizontal area.
        val cx = size.width / 2f
        // » cy = pusat vertikal (47% tinggi).
        val cy = size.height * 0.47f
        // » maxDist = radius pola (52% lebar). Titik di luar jarak ini tidak digambar, jadi pola berbentuk bulat.
        val maxDist = size.width * 0.52f
        // » Komentar: pola dipusatkan supaya simetris.
        // Center the grid horizontally so the pattern is symmetric.
        // » startX = kolom awal supaya pola simetris di tengah.
        val startX = cx - (cx / step).toInt() * step
        // » y = baris pertama titik (60% dari jarak).
        var y = step * 0.6f
        // » Ulangi selama y masih di dalam tinggi area: gambar satu baris.
        while (y < size.height) {
            // » x = kolom pertama pada baris ini.
            var x = startX
            // » Ulangi selama x masih di dalam lebar area.
            while (x < size.width) {
                // » Hitung jarak titik dari pusat. Hanya gambar kalau jaraknya kurang dari maxDist.
                if (Offset(x - cx, y - cy).getDistance() < maxDist) {
                    // » drawCircle = gambar satu titik hijau di posisi (x, y).
                    drawCircle(DotGreen, radius = radius, center = Offset(x, y))
                }
                // » x += step = pindah ke kolom berikutnya.
                x += step
            }
            // » y += step = pindah ke baris berikutnya.
            y += step
        }
    }
}

// » Komentar: ledakan kelopak warna-warni di kiri dan kanan.
/** Colourful teardrop "petal" firework bursts on both sides, like the design. */
// » @Composable = fungsi penggambar tampilan.
@Composable
// » private fun Fireworks(scaleRight, scaleLeft) = dua ledakan kembang api.
private fun Fireworks(scaleRight: Float, scaleLeft: Float) {
    // » Canvas penuh.
    Canvas(modifier = Modifier.fillMaxSize()) {
        // » Komentar: ledakan kanan atas, kelopak menyebar dari atas ke kanan bawah.
        // top-right burst: petals fan from straight up round to lower-right
        // » burst = gambar satu ledakan.
        burst(
            // » center = pusat ledakan: 75% lebar, 92dp dari atas.
            center = Offset(size.width * 0.75f, 92.dp.toPx()),
            // » startDeg = sudut awal -107 derajat. sweepDeg = sebaran 138 derajat. scale = besar dari animasi kanan.
            startDeg = -107f, sweepDeg = 138f, scale = scaleRight,
            // » colors = warna tiap kelopak secara berurutan.
            colors = listOf(FwRed, FwGreen, FwYellow, FwGreen, FwYellow, FwBlue, FwRed)
        )
        // » Komentar: ledakan kiri, kelopak menyebar ke kiri.
        // left burst: petals fan out to the left
        // » burst = ledakan kedua.
        burst(
            // » center = pusat: 21% lebar, 232dp dari atas.
            center = Offset(size.width * 0.21f, 232.dp.toPx()),
            // » startDeg 107 derajat, sweep 124 derajat, scale dari animasi kiri.
            startDeg = 107f, sweepDeg = 124f, scale = scaleLeft,
            // » colors = warna kelopak ledakan kiri.
            colors = listOf(FwRed, FwBlue, FwYellow, FwGreen, FwYellow, FwRed)
        )
    }
}

// » Komentar: fungsi ledakan satu set kelopak.
// Efek ledakan garis warna saat pembayaran berhasil.
// » private fun DrawScope.burst = gambar satu ledakan (extension dari DrawScope).
private fun DrawScope.burst(center: Offset, startDeg: Float, sweepDeg: Float, scale: Float, colors: List<Color>) {
    // » Kalau skala terlalu kecil (hampir 0), tidak perlu digambar.
    if (scale <= 0.01f) return
    // » inner = jarak awal kelopak dari pusat = 30dp x skala.
    val inner = 30.dp.toPx() * scale
    // » outer = panjang kelopak terjauh = 78dp x skala.
    val outer = 78.dp.toPx() * scale
    // » tipRadius = jari-jari ujung kelopak = 6dp x skala (maksimal 1 kali).
    val tipRadius = 6.dp.toPx() * scale.coerceAtMost(1f)
    // » forEachIndexed = untuk setiap warna (dengan nomor i)...
    colors.forEachIndexed { i, color ->
        // » deg = sudut kelopak ke-i: menyebar rata dari startDeg sampai startDeg + sweepDeg.
        val deg = startDeg + sweepDeg * i / (colors.size - 1)
        // » Komentar: panjang kelopak diselang-seling supaya tidak terlihat kaku.
        // Alternate petal length slightly so the burst doesn't look mechanical.
        // » Kelopak genap panjang penuh, kelopak ganjil 86% panjang.
        val len = if (i % 2 == 0) outer else outer * 0.86f
        // » drawPetal = gambar satu kelopak.
        drawPetal(center, deg, inner, len, tipRadius, color)
    }
}

// » Komentar: bentuk kelopak (runcing di dalam, membulat di luar).
/** A teardrop: pointed at the inner end, rounded at the outer end. */
// » private fun DrawScope.drawPetal = gambar satu kelopak (air mata).
private fun DrawScope.drawPetal(
    center: Offset, deg: Float, inner: Float, outer: Float, r: Float, color: Color
) {
    // » Komentar: fungsi arah dari sudut.
    // Arah garis dari sudut derajat.
    // » fun dir(d) = ubah sudut derajat menjadi vektor arah (x, y).
    fun dir(d: Float): Offset {
        // » toRadians = ubah derajat ke radian (satuan untuk cos dan sin).
        val a = Math.toRadians(d.toDouble())
        // » Offset(cos, sin) = vektor arah satuan.
        return Offset(cos(a).toFloat(), sin(a).toFloat())
    }
    // » tip = titik ujung runcing kelopak (dari pusat sejauh inner).
    val tip = center + dir(deg) * inner
    // » roundCenter = pusat lingkaran ujung membulat (sejauh outer - r).
    val roundCenter = center + dir(deg) * (outer - r)
    // » path = jalur bentuk kelopak.
    val path = Path().apply {
        // » moveTo tip = mulai dari ujung runcing.
        moveTo(tip.x, tip.y)
        // » side = titik di tepi kiri lingkaran ujung membulat.
        val side = roundCenter + dir(deg - 90f) * r
        // » lineTo side = garis dari ujung runcing ke tepi lingkaran.
        lineTo(side.x, side.y)
        // » arcTo = lengkung setengah lingkaran untuk ujung membulat.
        arcTo(
            // » rect = kotak pembatas lingkaran ujung (pusat roundCenter, jari-jari r).
            rect = Rect(roundCenter, r),
            // » startAngleDegrees = sudut mulai lengkung (arah kelopak dikurangi 90 derajat).
            startAngleDegrees = deg - 90f,
            // » sweepAngleDegrees = 180 derajat = setengah lingkaran.
            sweepAngleDegrees = 180f,
            // » forceMoveTo = false: lanjut dari titik sebelumnya, tidak loncat.
            forceMoveTo = false
        )
        // » close = tutup jalur kembali ke ujung runcing.
        close()
    }
    // » drawPath = isi kelopak dengan warna.
    drawPath(path, color)
}
