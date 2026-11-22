// ==========================================================================
// PETA FILE · KASTEP (Kelompok 1) · Color.kt
// Isi file : Warna tema hitam-cyan KASTEP.
// Penanda  : CARI-TEMA
// Cara cari: VS Code Ctrl+Shift+F (Android Studio Ctrl+Shift+F) lalu ketik
//            "CARI-" untuk semua penanda, atau salah satu nama di baris Penanda.
// ==========================================================================
// » package com.kastep.app.ui.theme = alamat folder file ini. Folder "theme" = warna dan huruf tampilan.
package com.kastep.app.ui.theme

// » import Color = alat untuk membuat warna Compose.
import androidx.compose.ui.graphics.Color

// » Komentar: daftar warna tema gelap KASTEP.
// Daftar warna KASTEP (tema gelap): KastepBlack (latar), KastepCardDark (kartu), KastepBlue / KastepCyan (aksen), KastepGreen (lunas, pemasukan),
// KastepRed (denda, pengeluaran), KastepWhite dan KastepGray (teks). Ganti nilai hex di sini untuk mengubah tema.
// KASTEP Design Colors — Pure dark theme with blue/cyan accents
// CARI-TEMA | Ubah nilai warna di file ini untuk mengganti tema.
// » KastepBlack = warna hitam untuk latar layar. 0xFF = tidak transparan, lalu 000000 = hitam.
val KastepBlack = Color(0xFF000000)
// » KastepDarkSurface = warna permukaan gelap biru tua.
val KastepDarkSurface = Color(0xFF1A1A2E)
// » KastepCardDark = warna kartu gelap (biru tua).
val KastepCardDark = Color(0xFF16213E)
// » KastepCardBorder = warna garis tepi kartu (abu gelap).
val KastepCardBorder = Color(0xFF2A2A40)

// » KastepBlue = biru utama (#4A90D9), untuk aksen dan menu terpilih.
val KastepBlue = Color(0xFF4A90D9)
// » KastepCyan = cyan terang (#00D4FF), warna aksen utama tombol.
val KastepCyan = Color(0xFF00D4FF)
// » KastepBlueDark = biru tua sekali (#1A237E).
val KastepBlueDark = Color(0xFF1A237E)
// » KastepPurple = ungu (#7B1FA2).
val KastepPurple = Color(0xFF7B1FA2)

// » KastepGreen = hijau (#4CAF50), dipakai untuk status LUNAS dan pemasukan.
val KastepGreen = Color(0xFF4CAF50)
// » KastepRed = merah (#EF5350), dipakai untuk denda dan pengeluaran.
val KastepRed = Color(0xFFEF5350)

// » KastepWhite = putih, untuk teks utama.
val KastepWhite = Color(0xFFFFFFFF)
// » KastepGray = abu sedang (#9E9E9E), untuk teks keterangan.
val KastepGray = Color(0xFF9E9E9E)
// » KastepLightGray = abu muda (#BDBDBD).
val KastepLightGray = Color(0xFFBDBDBD)

// Gradient colors for button
// » GradientBlueStart = warna awal gradasi tombol (biru).
val GradientBlueStart = Color(0xFF5B86E5)
// » GradientBlueEnd = warna akhir gradasi tombol (toska).
val GradientBlueEnd = Color(0xFF36D1DC)

// Bottom decorative circle
// » CircleBlue = biru untuk lingkaran dekorasi di bagian bawah.
val CircleBlue = Color(0xFF1565C0)
// » CirclePurple = ungu untuk lingkaran dekorasi.
val CirclePurple = Color(0xFF7B1FA2)
