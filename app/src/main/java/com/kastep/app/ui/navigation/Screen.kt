// ==========================================================================
// PETA FILE · KASTEP (Kelompok 1) · Screen.kt
// Isi file : Nama rute setiap halaman.
// Penanda  : CARI-NAVIGASI
// Cara cari: VS Code Ctrl+Shift+F (Android Studio Ctrl+Shift+F) lalu ketik
//            "CARI-" untuk semua penanda, atau salah satu nama di baris Penanda.
// ==========================================================================
// » package com.kastep.app.ui.navigation = alamat folder file ini (navigasi = perpindahan antar halaman).
package com.kastep.app.ui.navigation

// » Komentar: daftar semua halaman KASTEP (ada 12).
// Daftar semua halaman (rute) aplikasi. Setiap object = satu halaman: Splash, Login, Register, Dashboard, Profile, DataSiswa,
// RiwayatPembayaran, Pembayaran, MataPelajaran, PengeluaranKas, LaporanKas, PembayaranBerhasil. 12 halaman.
// CARI-NAVIGASI | Tambah halaman baru: object NamaHalaman : Screen("nama_rute").
// » sealed class Screen = daftar halaman yang sudah tetap. route = alamat teks unik tiap halaman.
sealed class Screen(val route: String) {
    // » object Splash = halaman pembuka. Rutenya "splash".
    object Splash : Screen("splash")
    // » object Login = halaman masuk. Rutenya "login".
    object Login : Screen("login")
    // » object Register = halaman daftar akun baru. Rutenya "register".
    object Register : Screen("register")
    // » object Dashboard = halaman utama setelah login. Rutenya "dashboard".
    object Dashboard : Screen("dashboard")
    // » object Profile = halaman profil akun. Rutenya "profile".
    object Profile : Screen("profile")
    // » object DataSiswa = daftar data siswa. Rutenya "data_siswa".
    object DataSiswa : Screen("data_siswa")
    // » object RiwayatPembayaran = riwayat semua pembayaran. Rutenya "riwayat_pembayaran".
    object RiwayatPembayaran : Screen("riwayat_pembayaran")
    // » object Pembayaran = halaman input pembayaran kas. Rutenya "pembayaran".
    object Pembayaran : Screen("pembayaran")
    // » object MataPelajaran = jadwal/mata pelajaran. Rutenya "mata_pelajaran".
    object MataPelajaran : Screen("mata_pelajaran")
    // » object PengeluaranKas = daftar pengeluaran kas. Rutenya "pengeluaran_kas".
    object PengeluaranKas : Screen("pengeluaran_kas")
    // » object LaporanKas = laporan kas (grafik dan rekap). Rutenya "laporan_kas".
    object LaporanKas : Screen("laporan_kas")
    // » object PembayaranBerhasil = halaman konfirmasi setelah pembayaran sukses. Rutenya "pembayaran_berhasil".
    object PembayaranBerhasil : Screen("pembayaran_berhasil")
}
