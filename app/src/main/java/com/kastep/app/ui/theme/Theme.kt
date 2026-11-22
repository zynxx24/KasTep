// » package com.kastep.app.ui.theme = alamat folder file ini (tema warna dan huruf).
package com.kastep.app.ui.theme

// » import Activity = layar Android; dipakai untuk mengubah warna status bar.
import android.app.Activity
// » import MaterialTheme = mesin tema Material 3 (gaya tampilan Android modern).
import androidx.compose.material3.MaterialTheme
// » import darkColorScheme = pembuat daftar warna tema GELAP.
import androidx.compose.material3.darkColorScheme
// » import Composable = penanda fungsi penggambar tampilan.
import androidx.compose.runtime.Composable
// » import SideEffect = menjalankan kode di luar tampilan tiap kali layar digambar ulang.
import androidx.compose.runtime.SideEffect
// » import Color = tipe warna.
import androidx.compose.ui.graphics.Color
// » import toArgb = mengubah warna Compose jadi angka warna Android.
import androidx.compose.ui.graphics.toArgb
// » import LocalView = mengambil view tempat tampilan digambar.
import androidx.compose.ui.platform.LocalView
// » import WindowCompat = alat mengatur ikon status bar di berbagai versi Android.
import androidx.core.view.WindowCompat

// » private val KastepDarkScheme = daftar warna tema gelap KASTEP. private = hanya dipakai di file ini.
private val KastepDarkScheme = darkColorScheme(
    // » primary = warna utama (tombol, aksen): cyan.
    primary = KastepCyan,
    // » secondary = warna kedua: biru.
    secondary = KastepBlue,
    // » tertiary = warna ketiga: ungu.
    tertiary = KastepPurple,
    // » background = warna latar layar: hitam.
    background = KastepBlack,
    // » surface = warna permukaan (lembar, menu): biru tua gelap.
    surface = KastepDarkSurface,
    // » surfaceVariant = warna kartu: biru tua.
    surfaceVariant = KastepCardDark,
    // » onPrimary = warna teks di atas warna utama: putih.
    onPrimary = Color.White,
    // » onSecondary = warna teks di atas warna kedua: putih.
    onSecondary = Color.White,
    // » onBackground = warna teks di atas latar: putih.
    onBackground = KastepWhite,
    // » onSurface = warna teks di atas permukaan: putih.
    onSurface = KastepWhite,
    // » onSurfaceVariant = warna teks keterangan: abu.
    onSurfaceVariant = KastepGray,
    // » outline = warna garis tepi: abu gelap.
    outline = KastepCardBorder
)

// » Komentar: fungsi tema yang membungkus semua layar.
// Tema hitam dengan aksen cyan dan biru yang membungkus seluruh layar.
// » @Composable = fungsi penggambar tampilan.
@Composable
// » KastepTheme = tema KASTEP. content = isi layar yang dibungkus.
fun KastepTheme(content: @Composable () -> Unit) {
    // » Ambil view tempat layar digambar.
    val view = LocalView.current
    // » Kalau bukan mode pratinjau di Android Studio...
    if (!view.isInEditMode) {
        // » ...jalankan pengaturan status bar setiap kali layar digambar.
        SideEffect {
            // » Ambil jendela (window) dari Activity (dipaksa jadi Activity dengan "as").
            val window = (view.context as Activity).window
            // » Warna status bar (bar atas, tempat jam dan baterai) = hitam.
            window.statusBarColor = KastepBlack.toArgb()
            // » Warna bar navigasi (bar bawah, tombol Home) = hitam.
            window.navigationBarColor = KastepBlack.toArgb()
            // » isAppearanceLightStatusBars = false: ikon status bar dibuat PUTIH (karena latarnya gelap).
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
        }
    }

    // » MaterialTheme = pasang tema Material 3 ke seluruh isi.
    MaterialTheme(
        // » colorScheme = pakai daftar warna KastepDarkScheme di atas.
        colorScheme = KastepDarkScheme,
        // » typography = pakai gaya huruf dari Type.kt.
        typography = Typography,
        // » content = isi layar yang dibungkus tema.
        content = content
    )
}
