// ==========================================================================
// PETA FILE · KASTEP (Kelompok 1) · NavGraph.kt
// Isi file : Daftar semua halaman dan perpindahan antarhalaman.
// Penanda  : CARI-NAVIGASI, CARI-LOGIN
// Cara cari: VS Code Ctrl+Shift+F (Android Studio Ctrl+Shift+F) lalu ketik
//            "CARI-" untuk semua penanda, atau salah satu nama di baris Penanda.
// ==========================================================================
package com.kastep.app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.kastep.app.data.KastepViewModel
import com.kastep.app.ui.screens.DashboardScreen
import com.kastep.app.ui.screens.DataSiswaScreen
import com.kastep.app.ui.screens.LaporanKasScreen
import com.kastep.app.ui.screens.LoginScreen
import com.kastep.app.ui.screens.MataPelajaranScreen
import com.kastep.app.ui.screens.PembayaranBerhasilScreen
import com.kastep.app.ui.screens.PembayaranScreen
import com.kastep.app.ui.screens.PengeluaranKasScreen
import com.kastep.app.ui.screens.ProfileScreen
import com.kastep.app.ui.screens.RegisterScreen
import com.kastep.app.ui.screens.RiwayatPembayaranScreen
import com.kastep.app.ui.screens.SplashScreen

// PETA HALAMAN. NavHost memilih halaman sesuai rute; tiap composable(Screen.X.route) memanggil satu layar.
// Alur penting:
//   Splash -> Login (belum login) atau Dashboard (sesi aktif)
//   Login berhasil -> Dashboard (Login dihapus dari tumpukan supaya tombol Kembali tidak balik ke Login)
//   Pembayaran berhasil -> PembayaranBerhasil (layar animasi sukses + tombol WhatsApp)
// LaunchedEffect(isLoggedIn) di atas: bila di halaman mana pun pengguna keluar (isLoggedIn false), otomatis kembali ke Login.
// CARI-NAVIGASI CARI-LOGIN | Setiap composable(Screen.X.route) = satu halaman. Splash langsung ke Dashboard bila sesi login masih aktif.
@Composable
fun NavGraph(
    navController: NavHostController,
    viewModel: KastepViewModel,
    onOpenDrawer: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isLoggedIn by viewModel.isLoggedIn.collectAsState()

    // Observe login state — if user logs out from anywhere, navigate to Login
    LaunchedEffect(isLoggedIn) {
        if (!isLoggedIn) {
            val currentRoute = navController.currentBackStackEntry?.destination?.route
            if (currentRoute != null && currentRoute != Screen.Login.route && currentRoute != Screen.Splash.route && currentRoute != Screen.Register.route) {
                navController.navigate(Screen.Login.route) {
                    popUpTo(0) { inclusive = true }
                }
            }
        }
    }

    val navigateToProfile: () -> Unit = {
        if (navController.currentDestination?.route != Screen.Profile.route) {
            navController.navigate(Screen.Profile.route) {
                launchSingleTop = true
            }
        }
    }

    val navigateToLogin: () -> Unit = {
        navController.navigate(Screen.Login.route) {
            popUpTo(0) { inclusive = true }
        }
    }

    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route,
        modifier = modifier
    ) {
        composable(Screen.Splash.route) {
            SplashScreen(
                onNavigateToLogin = {
                    // Sesi yang masih aktif langsung masuk ke Dashboard, tidak perlu login ulang
                    val tujuan = if (viewModel.isLoggedIn.value) Screen.Dashboard.route else Screen.Login.route
                    navController.navigate(tujuan) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Login.route) {
            LoginScreen(
                viewModel = viewModel,
                onLoginSuccess = {
                    navController.navigate(Screen.Dashboard.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onNavigateToRegister = {
                    navController.navigate(Screen.Register.route)
                }
            )
        }

        composable(Screen.Register.route) {
            RegisterScreen(
                viewModel = viewModel,
                onRegisterSuccess = {
                    navController.navigate(Screen.Dashboard.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onBackToLogin = { navController.popBackStack() }
            )
        }

        composable(Screen.Dashboard.route) {
            DashboardScreen(
                viewModel = viewModel,
                onNavigateToProfile = navigateToProfile,
                onOpenDrawer = onOpenDrawer
            )
        }

        composable(Screen.Profile.route) {
            ProfileScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
                onLogout = navigateToLogin
            )
        }

        composable(Screen.DataSiswa.route) {
            DataSiswaScreen(
                viewModel = viewModel,
                onOpenDrawer = onOpenDrawer,
                onNavigateToProfile = navigateToProfile
            )
        }

        composable(Screen.RiwayatPembayaran.route) {
            RiwayatPembayaranScreen(
                viewModel = viewModel,
                onOpenDrawer = onOpenDrawer,
                onNavigateToProfile = navigateToProfile,
                onBayarKas = { navController.navigate(Screen.Pembayaran.route) { launchSingleTop = true } }
            )
        }

        composable(Screen.Pembayaran.route) {
            PembayaranScreen(
                viewModel = viewModel,
                onOpenDrawer = onOpenDrawer,
                onNavigateToProfile = navigateToProfile,
                onPaymentSuccess = { navController.navigate(Screen.PembayaranBerhasil.route) }
            )
        }

        composable(Screen.MataPelajaran.route) {
            MataPelajaranScreen(
                viewModel = viewModel,
                onOpenDrawer = onOpenDrawer,
                onNavigateToProfile = navigateToProfile
            )
        }

        composable(Screen.PengeluaranKas.route) {
            PengeluaranKasScreen(
                viewModel = viewModel,
                onOpenDrawer = onOpenDrawer,
                onNavigateToProfile = navigateToProfile
            )
        }

        composable(Screen.LaporanKas.route) {
            LaporanKasScreen(
                viewModel = viewModel,
                onOpenDrawer = onOpenDrawer,
                onNavigateToProfile = navigateToProfile
            )
        }

        composable(Screen.PembayaranBerhasil.route) {
            PembayaranBerhasilScreen(
                viewModel = viewModel,
                onOpenDrawer = onOpenDrawer,
                onNavigateToProfile = navigateToProfile,
                onKembali = { navController.popBackStack() }
            )
        }
    }
}
