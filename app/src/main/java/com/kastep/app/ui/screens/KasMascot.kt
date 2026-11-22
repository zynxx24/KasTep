//  BACA INI DULU
//  Maskot KASTEP (koin bertopi dan berkacamata hitam) digambar SELURUHNYA dengan kode Canvas (bukan file gambar).
//  Dipakai di layar Pembayaran Berhasil (bagian atas). Koordinat desain 100x110 diskalakan ke ukuran layar lewat fungsi p(), u(), sz().
// » Alamat folder (package) file ini di dalam proyek.
package com.kastep.app.ui.screens

// » Canvas = kanvas kosong untuk menggambar sendiri (garis, lingkaran, grafik).
import androidx.compose.foundation.Canvas
// » @Composable = penanda bahwa sebuah fungsi menggambar tampilan.
import androidx.compose.runtime.Composable
// » Modifier = daftar 'dekorasi' sebuah tampilan: ukuran, jarak, warna latar, klik, dll.
import androidx.compose.ui.Modifier
// » Memanggil CornerRadius (dari androidx.compose.ui.geometry) supaya bisa dipakai di file ini.
import androidx.compose.ui.geometry.CornerRadius
// » Offset = titik koordinat (x, y) di Canvas.
import androidx.compose.ui.geometry.Offset
// » Memanggil Size (dari androidx.compose.ui.geometry) supaya bisa dipakai di file ini.
import androidx.compose.ui.geometry.Size
// » Brush = 'cat' warna, termasuk gradien (warna berubah halus).
import androidx.compose.ui.graphics.Brush
// » Color = tipe warna Compose; dipakai untuk menulis warna seperti Color.White atau Color(0xFF...).
import androidx.compose.ui.graphics.Color
// » Path = jalur garis yang digambar di Canvas (dipakai garis grafik).
import androidx.compose.ui.graphics.Path
// » StrokeCap = bentuk ujung garis (bulat atau rata).
import androidx.compose.ui.graphics.StrokeCap
// » Memanggil DrawScope (dari androidx.compose.ui.graphics.drawscope) supaya bisa dipakai di file ini.
import androidx.compose.ui.graphics.drawscope.DrawScope
// » Stroke = gaya menggambar garis tepi (tebal garis, ujung).
import androidx.compose.ui.graphics.drawscope.Stroke
// » Memanggil clipPath (dari androidx.compose.ui.graphics.drawscope) supaya bisa dipakai di file ini.
import androidx.compose.ui.graphics.drawscope.clipPath
// » Memanggil rotate (dari androidx.compose.ui.graphics.drawscope) supaya bisa dipakai di file ini.
import androidx.compose.ui.graphics.drawscope.rotate
// » Memanggil cos (dari kotlin.math) supaya bisa dipakai di file ini.
import kotlin.math.cos
// » Memanggil min (dari kotlin.math) supaya bisa dipakai di file ini.
import kotlin.math.min
// » Memanggil sin (dari kotlin.math) supaya bisa dipakai di file ini.
import kotlin.math.sin

/**
 * "Si Kas" — KASTEP's own mascot: a gold coin in a green fedora and pinstripe suit,
 * giving a thumbs-up and holding a fan of banknotes. Drawn entirely in code (no
 * third-party character art), on a 100 x 110 unit grid scaled to the canvas.
 */
// » @Composable = fungsi penggambar tampilan.
@Composable
// » internal fun KasMascot = maskot KASTEP (koin bertopi dan berjas hijau). internal = bisa dipakai file
// » lain di aplikasi.
internal fun KasMascot(modifier: Modifier = Modifier) {
    // » Canvas = area gambar. Isinya digambar oleh drawKasMascot (dengan kode, bukan file gambar).
    Canvas(modifier = modifier) { drawKasMascot() }
}

// » Outline = warna garis tepi maskot (cokelat sangat gelap).
private val Outline = Color(0xFF2B1D14)
// » SuitDark = hijau tua untuk jas dan pinggiran topi.
private val SuitDark = Color(0xFF1B5E20)
// » SuitMid = hijau sedang untuk jas.
private val SuitMid = Color(0xFF2E7D32)
// » SuitLight = hijau terang untuk garis jas (pinstripe).
private val SuitLight = Color(0xFF66BB6A)
// » Glove = putih untuk sarung tangan.
private val Glove = Color(0xFFFAFAFA)

// Menggambar maskot KASTEP dengan Canvas (bukan file gambar).
// » private fun DrawScope.drawKasMascot = gambar seluruh maskot di dalam area gambar.
private fun DrawScope.drawKasMascot() {
    // » s = skala: ukuran terkecil dari (lebar/100) dan (tinggi/110). Supaya gambar desain 100x110 pas dengan
    // » layar.
    val s = min(size.width / 100f, size.height / 110f)
    // » ox = margin kiri supaya maskot di tengah secara horizontal.
    val ox = (size.width - 100f * s) / 2f
    // » oy = margin atas supaya maskot di tengah secara vertikal.
    val oy = (size.height - 110f * s) / 2f
    // Ubah koordinat desain 100x110 ke ukuran layar.
    // » fun p(x, y) = ubah koordinat desain (100x110) menjadi posisi pixel di layar.
    fun p(x: Float, y: Float) = Offset(ox + x * s, oy + y * s)
    // Skala satu ukuran panjang.
    // » fun u(v) = ubah ukuran desain menjadi pixel.
    fun u(v: Float) = v * s
    // Skala ukuran lebar dan tinggi.
    // » fun sz(w, h) = ubah lebar dan tinggi desain menjadi ukuran pixel.
    fun sz(w: Float, h: Float) = Size(w * s, h * s)
    // » line = tebal garis standar maskot (0.9 satuan desain).
    val line = u(0.9f)

    // ---------- Torso: pinstripe suit, white shirt, green tie ----------
    // » Torso = jalur bentuk badan jas.
    val torso = Path().apply {
        // » moveTo = mulai dari kiri bawah badan.
        moveTo(p(20f, 112f).x, p(20f, 112f).y)
        // » lineTo = ke bahu kiri.
        lineTo(p(27f, 92f).x, p(27f, 92f).y)
        // » quadraticBezierTo = lengkung leher dari bahu kiri ke bahu kanan.
        quadraticBezierTo(p(50f, 84f).x, p(50f, 84f).y, p(73f, 92f).x, p(73f, 92f).y)
        // » lineTo = ke kanan bawah badan.
        lineTo(p(80f, 112f).x, p(80f, 112f).y)
        // » close = tutup jalur ke titik awal.
        close()
    }
    // » drawPath = isi badan jas dengan gradasi hijau dari atas ke bawah.
    drawPath(torso, Brush.verticalGradient(listOf(SuitMid, SuitDark), p(0f, 86f).y, p(0f, 112f).y))
    // » clipPath(torso) = gambar di dalam blok ini hanya terlihat di dalam badan jas.
    clipPath(torso) {
        // » x = posisi awal garis pinstripe di jas.
        var x = 18f
        // » Selama x masih di bawah 84...
        while (x < 84f) {
            // » ...gambar satu garis pinstripe (hijau terang transparan 55%).
            drawLine(SuitLight.copy(alpha = 0.55f), p(x, 84f), p(x + 3f, 112f), strokeWidth = u(0.5f))
            // » x += 3.5 = jarak antar garis pinstripe berikutnya.
            x += 3.5f
        }
    }
    // » shirt = jalur kemeja berbentuk segitiga di dada.
    val shirt = Path().apply {
        moveTo(p(42f, 87f).x, p(42f, 87f).y)
        lineTo(p(58f, 87f).x, p(58f, 87f).y)
        lineTo(p(50f, 106f).x, p(50f, 106f).y)
        close()
    }
    // » drawPath = isi kemeja putih.
    drawPath(shirt, Color.White)
    // » tie = jalur dasi (bentuk belah ketupat dan ujung lancip).
    val tie = Path().apply {
        moveTo(p(48.2f, 91f).x, p(48.2f, 91f).y)
        lineTo(p(51.8f, 91f).x, p(51.8f, 91f).y)
        lineTo(p(53.4f, 103f).x, p(53.4f, 103f).y)
        lineTo(p(50f, 106.5f).x, p(50f, 106.5f).y)
        lineTo(p(46.6f, 103f).x, p(46.6f, 103f).y)
        close()
    }
    // » drawPath = isi dasi hijau.
    drawPath(tie, Color(0xFF43A047))
    // » drawRoundRect = simpul dasi (kotak kecil membulat).
    drawRoundRect(Color(0xFF2E7D32), p(47.4f, 87.6f), sz(5.2f, 3.8f), CornerRadius(u(1.2f)))
    // lapels
    // » Garis lapel kiri jas.
    drawLine(SuitDark, p(42f, 87f), p(46f, 99f), strokeWidth = u(1.6f), cap = StrokeCap.Round)
    // » Garis lapel kanan jas.
    drawLine(SuitDark, p(58f, 87f), p(54f, 99f), strokeWidth = u(1.6f), cap = StrokeCap.Round)

    // ---------- Coin head ----------
    // » c = pusat kepala koin (titik 50,60 desain).
    val c = p(50f, 60f)
    // » drawCircle = kepala koin emas, jari-jari 30.
    drawCircle(Color(0xFFD08A00), radius = u(30f), center = c)
    // reeded edge
    // » Ulangi 72 kali: buat tepi bergerigi koin (setiap 5 derajat).
    for (i in 0 until 72) {
        // » a = sudut dalam radian.
        val a = Math.toRadians(i * 5.0)
        // » d = vektor arah untuk sudut itu.
        val d = Offset(cos(a).toFloat(), sin(a).toFloat())
        // » Garis tepi bergerigi (cokelat emas) dari jari-jari 27.6 sampai 29.6.
        drawLine(Color(0xFFB57200), c + d * u(27.6f), c + d * u(29.6f), strokeWidth = u(0.55f))
    }
    // » drawCircle = isi kepala koin dengan gradasi radial (kuning muda ke emas).
    drawCircle(
        // » Brush.radialGradient = gradasi melingkar.
        Brush.radialGradient(
            // » Warna gradasi: kuning muda, kuning, lalu oranye emas.
            listOf(Color(0xFFFFF4B8), Color(0xFFFFD54F), Color(0xFFFFB300)),
            // » center = pusat gradasi (sedikit kiri atas). radius = jari-jari gradasi.
            center = p(42f, 49f), radius = u(36f)
        ),
        // » radius = jari-jari kepala koin 27. center = c.
        radius = u(27f), center = c
    )
    // » Garis cincin tipis di dalam kepala (kuning muda).
    drawCircle(Color(0xFFFFE9A0), radius = u(24.6f), center = c, style = Stroke(u(0.7f)))
    // shine
    // » drawArc = kilau melengkung di kiri atas koin.
    drawArc(
        // » Warna kilau putih transparan 55%. Sudut mulai 200 derajat, sapuan 55 derajat.
        Color.White.copy(alpha = 0.55f), startAngle = 200f, sweepAngle = 55f, useCenter = false,
        // » topLeft dan size = posisi dan ukuran busur kilau. Stroke dengan ujung membulat.
        topLeft = p(29f, 39f), size = sz(42f, 42f), style = Stroke(u(2.2f), cap = StrokeCap.Round)
    )

    // cheeks
    // » Pipi kiri (oranye transparan 35%).
    drawCircle(Color(0xFFFF7043).copy(alpha = 0.35f), radius = u(3.2f), center = p(33.5f, 67f))
    // » Pipi kanan.
    drawCircle(Color(0xFFFF7043).copy(alpha = 0.35f), radius = u(3.2f), center = p(66.5f, 65.5f))

    // eyebrows (one raised: confident smirk)
    // » Alis kiri (garis tebal).
    drawLine(Outline, p(35.5f, 48.6f), p(46.5f, 50.6f), strokeWidth = u(2.1f), cap = StrokeCap.Round)
    // » Alis kanan, dibuat naik sedikit (ekspresi percaya diri).
    drawLine(Outline, p(53.5f, 49.4f), p(64.5f, 45.4f), strokeWidth = u(2.1f), cap = StrokeCap.Round)

    // sunglasses
    // » Gagang kacamata kiri.
    drawLine(Outline, p(34f, 54.5f), p(24.5f, 52.5f), strokeWidth = u(1.3f), cap = StrokeCap.Round)
    // » Gagang kacamata kanan.
    drawLine(Outline, p(66f, 54.5f), p(75.5f, 52.5f), strokeWidth = u(1.3f), cap = StrokeCap.Round)
    // » Jembatan kacamata di tengah.
    drawLine(Outline, p(47.5f, 55f), p(52.5f, 55f), strokeWidth = u(1.6f), cap = StrokeCap.Round)
    // » Ulangi untuk dua lensa: x0 = 34 (kiri) dan 52 (kanan).
    for (x0 in listOf(34f, 52f)) {
        // » drawRoundRect = lensa kacamata hitam.
        drawRoundRect(
            // » Gradasi lensa: dari abu gelap ke hitam.
            Brush.verticalGradient(listOf(Color(0xFF263238), Color(0xFF05080A)), p(0f, 52f).y, p(0f, 61.5f).y),
            // » topLeft dan ukuran lensa 14 x 9.5, sudut membulat 3.2.
            topLeft = p(x0, 52f), size = sz(14f, 9.5f), cornerRadius = CornerRadius(u(3.2f))
        )
        // » Kilau pertama di lensa (putih transparan 75%).
        drawLine(
            Color.White.copy(alpha = 0.75f), p(x0 + 2.6f, 55.6f), p(x0 + 5.6f, 53.4f),
            strokeWidth = u(1.1f), cap = StrokeCap.Round
        )
        // » Kilau kedua di lensa (lebih tipis).
        drawLine(
            Color.White.copy(alpha = 0.4f), p(x0 + 4.6f, 57.2f), p(x0 + 7f, 55.4f),
            strokeWidth = u(0.8f), cap = StrokeCap.Round
        )
    }

    // grin with a gold tooth
    // » mouth = jalur mulut berbentuk senyum.
    val mouth = Path().apply {
        // » moveTo = titik mulai di kiri.
        moveTo(p(39.5f, 67.5f).x, p(39.5f, 67.5f).y)
        // » cubicTo = lengkung bawah mulut (senyum lebar).
        cubicTo(p(45f, 79f).x, p(45f, 79f).y, p(57f, 78f).x, p(57f, 78f).y, p(62.5f, 64.5f).x, p(62.5f, 64.5f).y)
        // » quadraticBezierTo = lengkung kembali ke kiri.
        quadraticBezierTo(p(51f, 70.5f).x, p(51f, 70.5f).y, p(39.5f, 67.5f).x, p(39.5f, 67.5f).y)
        close()
    // » close = tutup jalur mulut.
    }
    // » drawPath = isi mulut cokelat tua.
    drawPath(mouth, Color(0xFF4E2A1E))
    // » clipPath(mouth) = gambar di dalam mulut saja.
    clipPath(mouth) {
        // » drawRect = gigi putih.
        drawRect(Color.White, p(36f, 62f), sz(30f, 9.6f))
        // » drawRect = satu gigi emas.
        drawRect(Color(0xFFFFC107), p(52.6f, 62f), sz(3.6f, 9.6f))
        // » Ulangi untuk beberapa posisi garis pemisah gigi.
        for (x in listOf(44f, 48.4f, 52.6f, 56.2f, 59.6f)) {
            // » drawLine = garis pemisah gigi (abu-abu).
            drawLine(Color(0xFFBDBDBD), p(x, 66f), p(x, 71.4f), strokeWidth = u(0.45f))
        }
        // » drawCircle = lidah merah muda di dalam mulut.
        drawCircle(Color(0xFFE57373), radius = u(4.4f), center = p(51f, 78f))
    }
    // » drawPath garis tepi mulut.
    drawPath(mouth, Outline, style = Stroke(u(1.1f)))
    // smirk dimple
    // » Garis kecil di sudut mulut (lesung pipi senyum miring).
    drawLine(Outline, p(62.5f, 64.5f), p(64.6f, 63.2f), strokeWidth = u(1.1f), cap = StrokeCap.Round)

    // ---------- Fedora (tilted) ----------
    // » rotate(-7) = topi diputar 7 derajat berlawanan jarum jam, pusat putar (50,36).
    rotate(-7f, pivot = p(50f, 36f)) {
        // » crown = jalur mahkota topi.
        val crown = Path().apply {
            // » moveTo = mulai dari kiri bawah mahkota.
            moveTo(p(31f, 35f).x, p(31f, 35f).y)
            // » cubicTo = sisi kiri mahkota naik ke puncak.
            cubicTo(p(29.5f, 24f).x, p(29.5f, 24f).y, p(33f, 12.5f).x, p(33f, 12.5f).y, p(41f, 10.5f).x, p(41f, 10.5f).y)
            // » quadraticBezierTo = lekuk di puncak topi.
            quadraticBezierTo(p(50f, 15.5f).x, p(50f, 15.5f).y, p(59f, 10.5f).x, p(59f, 10.5f).y)
            // » cubicTo = sisi kanan mahkota turun.
            cubicTo(p(67f, 12.5f).x, p(67f, 12.5f).y, p(70.5f, 24f).x, p(70.5f, 24f).y, p(69f, 35f).x, p(69f, 35f).y)
            close()
        // » close = tutup jalur mahkota.
        }
        // » drawPath = isi topi dengan gradasi hijau.
        drawPath(crown, Brush.verticalGradient(listOf(Color(0xFF43A047), SuitDark), p(0f, 10f).y, p(0f, 35f).y))
        // pinch crease
        // » Garis lekuk tengah (cubitan topi) berwarna hijau gelap.
        drawLine(Color(0xFF14461A), p(50f, 15.5f), p(50f, 23.5f), strokeWidth = u(1f), cap = StrokeCap.Round)
        // » Kilau putih transparan di sisi kiri topi.
        drawLine(Color.White.copy(alpha = 0.25f), p(36f, 16f), p(34.5f, 28f), strokeWidth = u(1.6f), cap = StrokeCap.Round)
        // band: gold with darker flecks
        // » drawRect = pita topi berwarna emas.
        drawRect(Color(0xFFB8962E), p(30.6f, 27f), sz(38.8f, 5.4f))
        // » x = posisi awal serat pita.
        var x = 32f
        // » Selama x masih di dalam lebar pita...
        while (x < 69f) {
            // » ...gambar garis serat tipis berwarna cokelat emas.
            drawLine(Color(0xFF7A5F14), p(x, 27.4f), p(x + 1.6f, 32f), strokeWidth = u(0.6f))
            // » x += 2.6 = jarak serat berikutnya.
            x += 2.6f
        }
        // brim
        // » drawOval = pinggiran topi (brim) hijau tua.
        drawOval(SuitDark, topLeft = p(11f, 31f), size = sz(78f, 12.5f))
        // » drawArc = garis kilau hijau terang di pinggiran topi.
        drawArc(
            Color(0xFF4CAF50).copy(alpha = 0.8f), startAngle = 190f, sweepAngle = 160f, useCenter = false,
            topLeft = p(12.5f, 31.8f), size = sz(75f, 10.5f), style = Stroke(u(0.9f))
        )
        // » drawOval = garis tepi pinggiran topi.
        drawOval(Color(0xFF0E3A12), topLeft = p(11f, 31f), size = sz(78f, 12.5f), style = Stroke(u(0.7f)))
    }

    // ---------- Right side: arm + fan of banknotes ----------
    // » drawLine = lengan kanan jas, dari badan ke tangan (tebal 8.5).
    drawLine(SuitDark, p(70f, 95f), p(80.5f, 83f), strokeWidth = u(8.5f), cap = StrokeCap.Round)
    // » pivot = pusat kipas uang (80.5, 80.5).
    val pivot = p(80.5f, 80.5f)
    // » Ulangi untuk lima sudut: -104, -84, -64, -44, -24 derajat (kipas lima lembar uang).
    for (angle in listOf(-104f, -84f, -64f, -44f, -24f)) {
        // » rotate(angle, pivot) = putar gambar lembar di sekitar pusat kipas.
        rotate(angle, pivot = pivot) {
            // » tl = pojok kiri atas lembar uang.
            val tl = Offset(pivot.x + u(2.5f), pivot.y - u(5.5f))
            // » drawRoundRect = isi lembar uang hijau muda (25 x 11).
            drawRoundRect(Color(0xFFC8E6C9), tl, sz(25f, 11f), CornerRadius(u(1.2f)))
            // » drawRoundRect = bingkai dalam lembar uang.
            drawRoundRect(
                Color(0xFF66BB6A), tl + Offset(u(1.6f), u(1.6f)), sz(21.8f, 7.8f),
                CornerRadius(u(0.8f)), style = Stroke(u(0.6f))
            )
            // » drawCircle = lingkaran di tengah lembar (gambar muka uang).
            drawCircle(Color(0xFF81C784), radius = u(2.6f), center = tl + Offset(u(14f), u(5.5f)))
            // » Garis tepi lingkaran di lembar.
            drawCircle(Color(0xFF388E3C), radius = u(2.6f), center = tl + Offset(u(14f), u(5.5f)), style = Stroke(u(0.5f)))
            // » Garis tepi lembar uang.
            drawRoundRect(Color(0xFF2E7D32), tl, sz(25f, 11f), CornerRadius(u(1.2f)), style = Stroke(u(0.7f)))
        }
    }
    // » drawCircle putih = manset lengan kanan.
    drawCircle(Color.White, radius = u(4.4f), center = p(79.5f, 85f)) // cuff
    // » drawRoundRect = sarung tangan kanan (putih, membulat).
    drawRoundRect(Glove, p(74.5f, 74.5f), sz(13f, 12.5f), CornerRadius(u(4f)))
    // » Garis tepi sarung tangan.
    drawRoundRect(Outline, p(74.5f, 74.5f), sz(13f, 12.5f), CornerRadius(u(4f)), style = Stroke(line))
    // » Ulangi untuk tiga garis jari.
    for (y in listOf(77.6f, 80.6f, 83.6f)) {
        // » Garis jari di sarung tangan.
        drawLine(Outline, p(74.6f, y), p(79f, y), strokeWidth = u(0.7f), cap = StrokeCap.Round)
    }

    // ---------- Left side: thumbs-up with a diamond ring ----------
    // » drawLine = lengan kiri jas, ke arah jempol.
    drawLine(SuitDark, p(30f, 95f), p(19f, 81f), strokeWidth = u(8.5f), cap = StrokeCap.Round)
    // » drawCircle putih = manset lengan kiri.
    drawCircle(Color.White, radius = u(4.4f), center = p(19.5f, 79.5f)) // cuff
    // thumb
    // » drawRoundRect = jempol (sarung putih).
    drawRoundRect(Glove, p(13.2f, 52.5f), sz(5.8f, 14.5f), CornerRadius(u(2.9f)))
    // » Garis tepi jempol.
    drawRoundRect(Outline, p(13.2f, 52.5f), sz(5.8f, 14.5f), CornerRadius(u(2.9f)), style = Stroke(line))
    // fist
    // » drawRoundRect = kepalan tangan kiri (putih).
    drawRoundRect(Glove, p(11f, 64f), sz(15f, 14f), CornerRadius(u(4.2f)))
    // » Garis tepi kepalan.
    drawRoundRect(Outline, p(11f, 64f), sz(15f, 14f), CornerRadius(u(4.2f)), style = Stroke(line))
    // » Ulangi untuk tiga garis jari.
    for (y in listOf(67.6f, 71f, 74.4f)) {
        // » Garis jari di kepalan.
        drawLine(Outline, p(19.5f, y), p(26f, y), strokeWidth = u(0.7f), cap = StrokeCap.Round)
    }
    // ring: gold band + diamond
    // » drawLine = cincin emas di jari.
    drawLine(Color(0xFFFFC107), p(22.6f, 69.3f), p(22.6f, 72.7f), strokeWidth = u(1.4f))
    // » gem = jalur bentuk berlian.
    val gem = Path().apply {
        // » moveTo = puncak berlian.
        moveTo(p(25.6f, 68.2f).x, p(25.6f, 68.2f).y)
        // » lineTo = sisi kanan berlian.
        lineTo(p(28.6f, 71f).x, p(28.6f, 71f).y)
        // » lineTo = bawah berlian.
        lineTo(p(25.6f, 73.8f).x, p(25.6f, 73.8f).y)
        // » lineTo = sisi kiri berlian.
        lineTo(p(22.6f, 71f).x, p(22.6f, 71f).y)
        // » close = tutup jalur berlian.
        close()
    }
    // » drawPath = isi berlian dengan gradasi putih ke toska.
    drawPath(gem, Brush.linearGradient(listOf(Color.White, Color(0xFF80DEEA)), p(23f, 68f), p(28f, 74f)))
    // » Garis tepi berlian toska.
    drawPath(gem, Color(0xFF26C6DA), style = Stroke(u(0.5f)))
    // » Kilau pertama pada berlian.
    drawLine(Color.White, p(27.6f, 66.4f), p(29f, 65f), strokeWidth = u(0.6f), cap = StrokeCap.Round)
    // » Kilau kedua pada berlian.
    drawLine(Color.White, p(29.2f, 68f), p(30.8f, 67.6f), strokeWidth = u(0.6f), cap = StrokeCap.Round)
}

