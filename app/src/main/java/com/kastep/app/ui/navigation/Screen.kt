package com.kastep.app.ui.navigation

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
