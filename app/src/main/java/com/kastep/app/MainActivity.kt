package com.kastep.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AssignmentReturn
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.People
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.kastep.app.data.KastepViewModel
import com.kastep.app.ui.navigation.NavGraph
import com.kastep.app.ui.navigation.Screen
import com.kastep.app.ui.theme.KastepBlack
import com.kastep.app.ui.theme.KastepBlue
import com.kastep.app.ui.theme.KastepCyan
import com.kastep.app.ui.theme.KastepGray
import com.kastep.app.ui.theme.KastepTheme
import com.kastep.app.ui.theme.KastepWhite
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private val viewModel: KastepViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KastepTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = KastepBlack
                ) {
                    KastepApp(viewModel = viewModel)
                }
            }
        }
    }
}

private data class DrawerMenuItem(
    val label: String,
    val icon: ImageVector,
    val route: String
)

@Composable
fun KastepApp(viewModel: KastepViewModel) {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route

    // Only enable drawer for post-login screens
    val drawerEnabled = currentRoute in listOf(
        Screen.Dashboard.route,
        Screen.DataSiswa.route,
        Screen.RiwayatPembayaran.route,
        Screen.Pembayaran.route,
        Screen.MataPelajaran.route,
        Screen.Profile.route,
        Screen.PengeluaranKas.route,
        Screen.LaporanKas.route,
        Screen.PembayaranBerhasil.route
    )

    // Drawer items matching the design exactly:
    // Dashboard, Data Siswa, Pembayaran, Pengeluaran, Mata Pelajaran, Laporan Kas
    val drawerItems = listOf(
        DrawerMenuItem("Dashboard", Icons.Default.Dashboard, Screen.Dashboard.route),
        DrawerMenuItem("Data Siswa", Icons.Default.People, Screen.DataSiswa.route),
        DrawerMenuItem("Pembayaran", Icons.Default.Payment, Screen.Pembayaran.route),
        DrawerMenuItem("Pengeluaran", Icons.Default.AssignmentReturn, Screen.PengeluaranKas.route),
        DrawerMenuItem("Mata Pelajaran", Icons.Default.Book, Screen.MataPelajaran.route),
        DrawerMenuItem("Laporan Kas", Icons.Default.Description, Screen.LaporanKas.route)
    )

    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = drawerEnabled,
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier
                    .width(220.dp)
                    .fillMaxHeight()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(KastepBlack.copy(alpha = 0.97f))
                        .padding(vertical = 16.dp)
                ) {
                    Spacer(modifier = Modifier.height(24.dp))

                    // Menu items
                    drawerItems.forEach { item ->
                        val isSelected = currentRoute == item.route
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    scope.launch { drawerState.close() }
                                    if (currentRoute != item.route) {
                                        navController.navigate(item.route) {
                                            popUpTo(Screen.Dashboard.route) {
                                                saveState = true
                                            }
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                    }
                                }
                                .background(
                                    if (isSelected) KastepBlue.copy(alpha = 0.6f)
                                    else KastepBlack.copy(alpha = 0f)
                                )
                                .padding(horizontal = 16.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                item.icon,
                                contentDescription = item.label,
                                tint = if (isSelected) KastepWhite else KastepGray,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = item.label,
                                color = if (isSelected) KastepWhite else KastepGray,
                                fontSize = 14.sp,
                                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
                            )
                        }
                    }
                }
            }
        }
    ) {
        NavGraph(
            navController = navController,
            viewModel = viewModel,
            onOpenDrawer = {
                scope.launch { drawerState.open() }
            }
        )
    }
}
