package com.kastep.app.ui.navigation

import androidx.compose.runtime.Composable
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

@Composable
fun NavGraph(
    navController: NavHostController,
    viewModel: KastepViewModel,
    onOpenDrawer: () -> Unit,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route,
        modifier = modifier
    ) {
        composable(Screen.Splash.route) {
            SplashScreen(
                onNavigateToLogin = {
                    navController.navigate(Screen.Login.route) {
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
                }
            )
        }

        composable(Screen.Dashboard.route) {
            DashboardScreen(
                viewModel = viewModel,
                onNavigateToProfile = {
                    navController.navigate(Screen.Profile.route)
                },
                onOpenDrawer = onOpenDrawer
            )
        }

        composable(Screen.Profile.route) {
            ProfileScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }

        composable(Screen.DataSiswa.route) {
            DataSiswaScreen(
                viewModel = viewModel,
                onOpenDrawer = onOpenDrawer
            )
        }

        composable(Screen.RiwayatPembayaran.route) {
            RiwayatPembayaranScreen(
                viewModel = viewModel,
                onOpenDrawer = onOpenDrawer
            )
        }

        composable(Screen.Pembayaran.route) {
            PembayaranScreen(
                viewModel = viewModel,
                onOpenDrawer = onOpenDrawer
            )
        }

        composable(Screen.MataPelajaran.route) {
            MataPelajaranScreen(
                viewModel = viewModel,
                onOpenDrawer = onOpenDrawer
            )
        }

        composable(Screen.PengeluaranKas.route) {
            PengeluaranKasScreen(
                viewModel = viewModel,
                onOpenDrawer = onOpenDrawer
            )
        }

        composable(Screen.LaporanKas.route) {
            LaporanKasScreen(
                viewModel = viewModel,
                onOpenDrawer = onOpenDrawer
            )
        }

        composable(Screen.PembayaranBerhasil.route) {
            PembayaranBerhasilScreen(
                viewModel = viewModel,
                onOpenDrawer = onOpenDrawer,
                onKembali = { navController.popBackStack() }
            )
        }
    }
}
