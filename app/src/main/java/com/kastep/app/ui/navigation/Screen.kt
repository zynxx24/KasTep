// ==========================================================================
// PETA FILE · KASTEP (Kelompok 1) · Screen.kt
// Isi file : Nama rute setiap halaman.
// Penanda  : CARI-NAVIGASI
// Cara cari: VS Code Ctrl+Shift+F (Android Studio Ctrl+Shift+F) lalu ketik
//            "CARI-" untuk semua penanda, atau salah satu nama di baris Penanda.
// ==========================================================================
package com.kastep.app.ui.navigation

// Daftar semua halaman (rute) aplikasi. Setiap object = satu halaman: Splash, Login, Register, Dashboard, Profile, DataSiswa,
// RiwayatPembayaran, Pembayaran, MataPelajaran, PengeluaranKas, LaporanKas, PembayaranBerhasil. 12 halaman.
// CARI-NAVIGASI | Tambah halaman baru: object NamaHalaman : Screen("nama_rute").
sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Login : Screen("login")
    object Register : Screen("register")
    object Dashboard : Screen("dashboard")
    object Profile : Screen("profile")
    object DataSiswa : Screen("data_siswa")
    object RiwayatPembayaran : Screen("riwayat_pembayaran")
    object Pembayaran : Screen("pembayaran")
    object MataPelajaran : Screen("mata_pelajaran")
    object PengeluaranKas : Screen("pengeluaran_kas")
    object LaporanKas : Screen("laporan_kas")
    object PembayaranBerhasil : Screen("pembayaran_berhasil")
}
