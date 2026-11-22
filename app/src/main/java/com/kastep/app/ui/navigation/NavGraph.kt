// ==========================================================================
// PETA FILE · KASTEP (Kelompok 1) · NavGraph.kt
// Isi file : Daftar semua halaman dan perpindahan antarhalaman.
// Penanda  : CARI-NAVIGASI, CARI-LOGIN
// Cara cari: VS Code Ctrl+Shift+F (Android Studio Ctrl+Shift+F) lalu ketik
//            "CARI-" untuk semua penanda, atau salah satu nama di baris Penanda.
// ==========================================================================
// » package com.kastep.app.ui.navigation = alamat folder file ini (navigasi antar halaman).
package com.kastep.app.ui.navigation

// » @Composable = penanda bahwa sebuah fungsi menggambar tampilan.
import androidx.compose.runtime.Composable
// » Memanggil LaunchedEffect (dari androidx.compose.runtime) supaya bisa dipakai di file ini.
import androidx.compose.runtime.LaunchedEffect
// » Memanggil collectAsState (dari androidx.compose.runtime) supaya bisa dipakai di file ini.
import androidx.compose.runtime.collectAsState
// » Memanggil getValue (dari androidx.compose.runtime) supaya bisa dipakai di file ini.
import androidx.compose.runtime.getValue
// » Modifier = daftar 'dekorasi' sebuah tampilan: ukuran, jarak, warna latar, klik, dll.
import androidx.compose.ui.Modifier
// » Memanggil NavHostController (dari androidx.navigation) supaya bisa dipakai di file ini.
import androidx.navigation.NavHostController
// » Memanggil NavHost (dari androidx.navigation.compose) supaya bisa dipakai di file ini.
import androidx.navigation.compose.NavHost
// » Memanggil composable (dari androidx.navigation.compose) supaya bisa dipakai di file ini.
import androidx.navigation.compose.composable
// » Memanggil KastepViewModel (dari com.kastep.app.data) supaya bisa dipakai di file ini.
import com.kastep.app.data.KastepViewModel
// » Memanggil DashboardScreen (dari com.kastep.app.ui.screens) supaya bisa dipakai di file ini.
import com.kastep.app.ui.screens.DashboardScreen
// » Memanggil DataSiswaScreen (dari com.kastep.app.ui.screens) supaya bisa dipakai di file ini.
import com.kastep.app.ui.screens.DataSiswaScreen
// » Memanggil LaporanKasScreen (dari com.kastep.app.ui.screens) supaya bisa dipakai di file ini.
import com.kastep.app.ui.screens.LaporanKasScreen
// » Memanggil LoginScreen (dari com.kastep.app.ui.screens) supaya bisa dipakai di file ini.
import com.kastep.app.ui.screens.LoginScreen
// » Memanggil MataPelajaranScreen (dari com.kastep.app.ui.screens) supaya bisa dipakai di file ini.
import com.kastep.app.ui.screens.MataPelajaranScreen
// » Memanggil PembayaranBerhasilScreen (dari com.kastep.app.ui.screens) supaya bisa dipakai di file ini.
import com.kastep.app.ui.screens.PembayaranBerhasilScreen
// » Memanggil PembayaranScreen (dari com.kastep.app.ui.screens) supaya bisa dipakai di file ini.
import com.kastep.app.ui.screens.PembayaranScreen
// » Memanggil PengeluaranKasScreen (dari com.kastep.app.ui.screens) supaya bisa dipakai di file ini.
import com.kastep.app.ui.screens.PengeluaranKasScreen
// » Memanggil ProfileScreen (dari com.kastep.app.ui.screens) supaya bisa dipakai di file ini.
import com.kastep.app.ui.screens.ProfileScreen
// » Memanggil RegisterScreen (dari com.kastep.app.ui.screens) supaya bisa dipakai di file ini.
import com.kastep.app.ui.screens.RegisterScreen
// » Memanggil RiwayatPembayaranScreen (dari com.kastep.app.ui.screens) supaya bisa dipakai di file ini.
import com.kastep.app.ui.screens.RiwayatPembayaranScreen
// » Memanggil SplashScreen (dari com.kastep.app.ui.screens) supaya bisa dipakai di file ini.
import com.kastep.app.ui.screens.SplashScreen

// » Komentar: NavHost = wadah semua halaman. Tiap composable(Screen.X.route) berisi satu halaman.
// PETA HALAMAN. NavHost memilih halaman sesuai rute; tiap composable(Screen.X.route) memanggil satu layar.
// Alur penting:
//   Splash -> Login (belum login) atau Dashboard (sesi aktif)
//   Login berhasil -> Dashboard (Login dihapus dari tumpukan supaya tombol Kembali tidak balik ke Login)
//   Pembayaran berhasil -> PembayaranBerhasil (layar animasi sukses + tombol WhatsApp)
// LaunchedEffect(isLoggedIn) di atas: bila di halaman mana pun pengguna keluar (isLoggedIn false), otomatis kembali ke Login.
// CARI-NAVIGASI CARI-LOGIN | Setiap composable(Screen.X.route) = satu halaman. Splash langsung ke Dashboard bila sesi login masih aktif.
// » @Composable = fungsi penggambar tampilan. NavGraph = peta semua halaman KASTEP.
@Composable
// » fun NavGraph = fungsi yang menyusun daftar halaman. Dipanggil dari MainActivity.
fun NavGraph(
    // » navController = pengatur perpindahan halaman (dibuat di KastepApp).
    navController: NavHostController,
    // » viewModel = sumber data bersama.
    viewModel: KastepViewModel,
    // » onOpenDrawer = fungsi untuk membuka menu samping (dikirim ke tiap halaman).
    onOpenDrawer: () -> Unit,
    // » modifier = pengatur tambahan tampilan (default kosong).
    modifier: Modifier = Modifier
) {
    // » isLoggedIn = status login, dibaca dari viewModel. "by" = langsung nilainya, bukan kotaknya.
    // » collectAsState = ikut berubah saat data berubah.
    val isLoggedIn by viewModel.isLoggedIn.collectAsState()

    // Observe login state — if user logs out from anywhere, navigate to Login
    // » LaunchedEffect(isLoggedIn) = jalankan blok di bawah setiap kali isLoggedIn berubah.
    LaunchedEffect(isLoggedIn) {
        // » Kalau sudah tidak login (keluar dari akun)...
        if (!isLoggedIn) {
            // » ...ambil halaman yang sedang tampil.
            val currentRoute = navController.currentBackStackEntry?.destination?.route
            // » ...dan kalau bukan Login, Splash, atau Register...
            if (currentRoute != null && currentRoute != Screen.Login.route && currentRoute != Screen.Splash.route && currentRoute != Screen.Register.route) {
                // » ...pindah paksa ke halaman Login.
                navController.navigate(Screen.Login.route) {
                    // » popUpTo(0) inclusive = hapus SEMUA halaman dari tumpukan, supaya tombol Kembali tidak kembali ke halaman
                    // » sebelumnya.
                    popUpTo(0) { inclusive = true }
                }
            }
        }
    }

    // » navigateToProfile = fungsi pindah ke halaman Profil.
    val navigateToProfile: () -> Unit = {
        // » Hanya pindah kalau belum berada di Profil.
        if (navController.currentDestination?.route != Screen.Profile.route) {
            // » navigate ke rute Profile.
            navController.navigate(Screen.Profile.route) {
                // » launchSingleTop = jangan membuka dua halaman Profil bersamaan.
                launchSingleTop = true
            }
        }
    }

    // » navigateToLogin = fungsi pindah ke Login dan membersihkan tumpukan halaman.
    val navigateToLogin: () -> Unit = {
        // » navigate ke rute Login.
        navController.navigate(Screen.Login.route) {
            // » popUpTo(0) inclusive = hapus semua halaman sebelumnya.
            popUpTo(0) { inclusive = true }
        }
    }

    // » NavHost = wadah halaman. Menampilkan halaman sesuai rute yang aktif.
    NavHost(
        // » navController = pengatur perpindahan.
        navController = navController,
        // » startDestination = halaman pertama yang dibuka: Splash.
        startDestination = Screen.Splash.route,
        // » modifier = pengatur tampilan tambahan.
        modifier = modifier
    ) {
        // » composable(Splash) = aturan untuk halaman Splash.
        composable(Screen.Splash.route) {
            // » SplashScreen = tampilan pembuka.
            SplashScreen(
                // » onNavigateToLogin = dipanggil setelah splash selesai.
                onNavigateToLogin = {
                    // Sesi yang masih aktif langsung masuk ke Dashboard, tidak perlu login ulang
                    // » tujuan = Dashboard kalau sudah login, kalau belum Login.
                    val tujuan = if (viewModel.isLoggedIn.value) Screen.Dashboard.route else Screen.Login.route
                    // » Pindah ke tujuan itu.
                    navController.navigate(tujuan) {
                        // » popUpTo(Splash) inclusive = buang Splash dari tumpukan supaya tidak bisa kembali ke sana.
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                }
            )
        }

        // » composable(Login) = aturan untuk halaman Login.
        composable(Screen.Login.route) {
            // » LoginScreen = tampilan form masuk.
            LoginScreen(
                // » viewModel = sumber data untuk cek login.
                viewModel = viewModel,
                // » onLoginSuccess = dipanggil kalau login berhasil.
                onLoginSuccess = {
                    // » Pindah ke Dashboard.
                    navController.navigate(Screen.Dashboard.route) {
                        // » popUpTo(Login) inclusive = buang Login dari tumpukan.
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                // » onNavigateToRegister = dipanggil saat tombol daftar ditekan.
                onNavigateToRegister = {
                    // » Pindah ke halaman Register (tanpa menghapus Login dari tumpukan).
                    navController.navigate(Screen.Register.route)
                }
            )
        }

        // » composable(Register) = aturan untuk halaman Daftar.
        composable(Screen.Register.route) {
            // » RegisterScreen = tampilan form daftar akun baru.
            RegisterScreen(
                viewModel = viewModel,
                // » onRegisterSuccess = dipanggil kalau pendaftaran berhasil.
                onRegisterSuccess = {
                    // » Pindah ke Dashboard.
                    navController.navigate(Screen.Dashboard.route) {
                        // » popUpTo(Login) inclusive = buang Login dan Register dari tumpukan.
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                // » onBackToLogin = kembali ke halaman sebelumnya (Login) dengan popBackStack.
                onBackToLogin = { navController.popBackStack() }
            )
        }

        // » composable(Dashboard) = aturan untuk halaman utama.
        composable(Screen.Dashboard.route) {
            // » DashboardScreen = tampilan ringkasan kas (saldo, grafik).
            DashboardScreen(
                viewModel = viewModel,
                // » onNavigateToProfile = tombol profil di bar atas.
                onNavigateToProfile = navigateToProfile,
                // » onOpenDrawer = tombol menu (≡) di bar atas.
                onOpenDrawer = onOpenDrawer
            )
        }

        // » composable(Profile) = aturan untuk halaman Profil.
        composable(Screen.Profile.route) {
            // » ProfileScreen = tampilan profil akun.
            ProfileScreen(
                viewModel = viewModel,
                // » onBack = tombol kembali: buang halaman ini dari tumpukan.
                onBack = { navController.popBackStack() },
                // » onLogout = tombol keluar: pindah ke Login.
                onLogout = navigateToLogin
            )
        }

        // » composable(DataSiswa) = aturan untuk halaman Data Siswa.
        composable(Screen.DataSiswa.route) {
            // » DataSiswaScreen = tampilan daftar siswa.
            DataSiswaScreen(
                viewModel = viewModel,
                // » onOpenDrawer = tombol menu.
                onOpenDrawer = onOpenDrawer,
                // » onNavigateToProfile = tombol profil.
                onNavigateToProfile = navigateToProfile
            )
        }

        // » composable(RiwayatPembayaran) = aturan untuk halaman Riwayat Bayar.
        composable(Screen.RiwayatPembayaran.route) {
            // » RiwayatPembayaranScreen = tampilan daftar riwayat pembayaran.
            RiwayatPembayaranScreen(
                viewModel = viewModel,
                onOpenDrawer = onOpenDrawer,
                onNavigateToProfile = navigateToProfile,
                // » onBayarKas = tombol Bayar: pindah ke halaman Pembayaran. launchSingleTop = jangan buka dua kali.
                onBayarKas = { navController.navigate(Screen.Pembayaran.route) { launchSingleTop = true } }
            )
        }

        // » composable(Pembayaran) = aturan untuk halaman Pembayaran.
        composable(Screen.Pembayaran.route) {
            // » PembayaranScreen = tampilan input pembayaran kas.
            PembayaranScreen(
                viewModel = viewModel,
                onOpenDrawer = onOpenDrawer,
                onNavigateToProfile = navigateToProfile,
                // » onPaymentSuccess = dipanggil setelah pembayaran berhasil. Pindah ke halaman Pembayaran Berhasil.
                onPaymentSuccess = { navController.navigate(Screen.PembayaranBerhasil.route) }
            )
        }

        // » composable(MataPelajaran) = aturan untuk halaman Mata Pelajaran.
        composable(Screen.MataPelajaran.route) {
            // » MataPelajaranScreen = tampilan daftar mata pelajaran (jadwal).
            MataPelajaranScreen(
                viewModel = viewModel,
                onOpenDrawer = onOpenDrawer,
                onNavigateToProfile = navigateToProfile
            )
        }

        // » composable(PengeluaranKas) = aturan untuk halaman Pengeluaran.
        composable(Screen.PengeluaranKas.route) {
            // » PengeluaranKasScreen = tampilan daftar dan input pengeluaran.
            PengeluaranKasScreen(
                viewModel = viewModel,
                onOpenDrawer = onOpenDrawer,
                onNavigateToProfile = navigateToProfile
            )
        }

        // » composable(LaporanKas) = aturan untuk halaman Laporan Kas.
        composable(Screen.LaporanKas.route) {
            // » LaporanKasScreen = tampilan laporan kas (grafik dan rekap).
            LaporanKasScreen(
                viewModel = viewModel,
                onOpenDrawer = onOpenDrawer,
                onNavigateToProfile = navigateToProfile
            )
        }

        // » composable(PembayaranBerhasil) = aturan untuk halaman sukses.
        composable(Screen.PembayaranBerhasil.route) {
            // » PembayaranBerhasilScreen = tampilan animasi sukses dan tombol WhatsApp.
            PembayaranBerhasilScreen(
                viewModel = viewModel,
                onOpenDrawer = onOpenDrawer,
                onNavigateToProfile = navigateToProfile,
                // » onKembali = tombol kembali: popBackStack (keluar dari halaman ini).
                onKembali = { navController.popBackStack() }
            )
        }
    // » Akhir dari NavHost (wadah halaman).
    }
// » Akhir dari fungsi NavGraph.
}
