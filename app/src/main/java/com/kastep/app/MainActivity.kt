// ==========================================================================
// PETA FILE · KASTEP (Kelompok 1) · MainActivity.kt
// Isi file : Activity utama dan menu samping (drawer).
// Penanda  : CARI-MULAI, CARI-NAVIGASI
// Cara cari: VS Code Ctrl+Shift+F (Android Studio Ctrl+Shift+F) lalu ketik
//            "CARI-" untuk semua penanda, atau salah satu nama di baris Penanda.
// ==========================================================================
//  BACA INI DULU
//  File pertama yang jalan saat aplikasi dibuka. Alur:
//    1. MainActivity.onCreate memasang tema gelap KASTEP dan memanggil KastepApp
//    2. KastepApp membuat navController, menu samping (drawer), dan NavGraph
//    3. NavGraph mulai dari SplashScreen, lalu ke Login atau Dashboard (bila sesi masih aktif)
//  Navigasi KASTEP memakai MENU SAMPING (geser dari kiri atau tekan ikon menu), bukan tab bawah.
//  Isi menu: Dashboard, Data Siswa, Pembayaran, Riwayat Bayar, Pengeluaran, Mata Pelajaran, Laporan Kas.
//  KastepViewModel dibuat sekali di sini (by viewModels()) dan dipakai bersama semua layar.
// » Alamat folder (package) file ini di dalam proyek.
package com.kastep.app

// » Bundle = paket data kecil yang diberikan Android saat layar dibuat.
import android.os.Bundle
// » ComponentActivity = jenis layar dasar untuk aplikasi Compose.
import androidx.activity.ComponentActivity
// » setContent = memasang tampilan Compose ke layar.
import androidx.activity.compose.setContent
// » Memanggil viewModels (dari androidx.activity) supaya bisa dipakai di file ini.
import androidx.activity.viewModels
// » background = mengisi warna/gradien latar sebuah tampilan.
import androidx.compose.foundation.background
// » clickable = membuat tampilan bisa diketuk.
import androidx.compose.foundation.clickable
// » Memanggil Column (dari androidx.compose.foundation.layout) supaya bisa dipakai di file ini.
import androidx.compose.foundation.layout.Column
// » Memanggil Row (dari androidx.compose.foundation.layout) supaya bisa dipakai di file ini.
import androidx.compose.foundation.layout.Row
// » Memanggil Spacer (dari androidx.compose.foundation.layout) supaya bisa dipakai di file ini.
import androidx.compose.foundation.layout.Spacer
// » Memanggil fillMaxHeight (dari androidx.compose.foundation.layout) supaya bisa dipakai di file ini.
import androidx.compose.foundation.layout.fillMaxHeight
// » Memanggil fillMaxSize (dari androidx.compose.foundation.layout) supaya bisa dipakai di file ini.
import androidx.compose.foundation.layout.fillMaxSize
// » Memanggil fillMaxWidth (dari androidx.compose.foundation.layout) supaya bisa dipakai di file ini.
import androidx.compose.foundation.layout.fillMaxWidth
// » Memanggil height (dari androidx.compose.foundation.layout) supaya bisa dipakai di file ini.
import androidx.compose.foundation.layout.height
// » Memanggil padding (dari androidx.compose.foundation.layout) supaya bisa dipakai di file ini.
import androidx.compose.foundation.layout.padding
// » Memanggil size (dari androidx.compose.foundation.layout) supaya bisa dipakai di file ini.
import androidx.compose.foundation.layout.size
// » Memanggil width (dari androidx.compose.foundation.layout) supaya bisa dipakai di file ini.
import androidx.compose.foundation.layout.width
// » Icons = daftar ikon bawaan Material.
import androidx.compose.material.icons.Icons
// » Ikon bawaan Material: AssignmentReturn (versi terisi).
import androidx.compose.material.icons.filled.AssignmentReturn
// » Ikon bawaan Material: Book (versi terisi).
import androidx.compose.material.icons.filled.Book
// » Ikon bawaan Material: Dashboard (versi terisi).
import androidx.compose.material.icons.filled.Dashboard
// » Ikon bawaan Material: Description (versi terisi).
import androidx.compose.material.icons.filled.Description
// » Ikon bawaan Material: History (versi terisi).
import androidx.compose.material.icons.filled.History
// » Ikon bawaan Material: Payment (versi terisi).
import androidx.compose.material.icons.filled.Payment
// » Ikon bawaan Material: People (versi terisi).
import androidx.compose.material.icons.filled.People
// » Memanggil DrawerValue (dari androidx.compose.material3) supaya bisa dipakai di file ini.
import androidx.compose.material3.DrawerValue
// » Memanggil HorizontalDivider (dari androidx.compose.material3) supaya bisa dipakai di file ini.
import androidx.compose.material3.HorizontalDivider
// » Memanggil Icon (dari androidx.compose.material3) supaya bisa dipakai di file ini.
import androidx.compose.material3.Icon
// » Memanggil ModalDrawerSheet (dari androidx.compose.material3) supaya bisa dipakai di file ini.
import androidx.compose.material3.ModalDrawerSheet
// » Memanggil ModalNavigationDrawer (dari androidx.compose.material3) supaya bisa dipakai di file ini.
import androidx.compose.material3.ModalNavigationDrawer
// » Memanggil Surface (dari androidx.compose.material3) supaya bisa dipakai di file ini.
import androidx.compose.material3.Surface
// » Memanggil Text (dari androidx.compose.material3) supaya bisa dipakai di file ini.
import androidx.compose.material3.Text
// » Memanggil rememberDrawerState (dari androidx.compose.material3) supaya bisa dipakai di file ini.
import androidx.compose.material3.rememberDrawerState
// » @Composable = penanda bahwa sebuah fungsi menggambar tampilan.
import androidx.compose.runtime.Composable
// » Memanggil getValue (dari androidx.compose.runtime) supaya bisa dipakai di file ini.
import androidx.compose.runtime.getValue
// » Memanggil rememberCoroutineScope (dari androidx.compose.runtime) supaya bisa dipakai di file ini.
import androidx.compose.runtime.rememberCoroutineScope
// » Alignment = pengatur posisi isi (tengah, atas, bawah, kiri, kanan).
import androidx.compose.ui.Alignment
// » Modifier = daftar 'dekorasi' sebuah tampilan: ukuran, jarak, warna latar, klik, dll.
import androidx.compose.ui.Modifier
// » ImageVector = tipe gambar ikon vektor.
import androidx.compose.ui.graphics.vector.ImageVector
// » FontWeight = ketebalan huruf (Normal, Medium, Bold, ExtraBold).
import androidx.compose.ui.text.font.FontWeight
// » dp = satuan ukuran Android yang sama di semua layar (mis. 16.dp).
import androidx.compose.ui.unit.dp
// » sp = satuan ukuran huruf Android (mengikuti pengaturan ukuran font di HP).
import androidx.compose.ui.unit.sp
// » Memanggil currentBackStackEntryAsState (dari androidx.navigation.compose) supaya bisa dipakai di file
// » ini.
import androidx.navigation.compose.currentBackStackEntryAsState
// » Memanggil rememberNavController (dari androidx.navigation.compose) supaya bisa dipakai di file ini.
import androidx.navigation.compose.rememberNavController
// » Memanggil KastepViewModel (dari com.kastep.app.data) supaya bisa dipakai di file ini.
import com.kastep.app.data.KastepViewModel
// » Memanggil NavGraph (dari com.kastep.app.ui.navigation) supaya bisa dipakai di file ini.
import com.kastep.app.ui.navigation.NavGraph
// » Memanggil Screen (dari com.kastep.app.ui.navigation) supaya bisa dipakai di file ini.
import com.kastep.app.ui.navigation.Screen
// » Memanggil KastepBlack (dari com.kastep.app.ui.theme) supaya bisa dipakai di file ini.
import com.kastep.app.ui.theme.KastepBlack
// » Memanggil KastepBlue (dari com.kastep.app.ui.theme) supaya bisa dipakai di file ini.
import com.kastep.app.ui.theme.KastepBlue
// » Memanggil KastepCyan (dari com.kastep.app.ui.theme) supaya bisa dipakai di file ini.
import com.kastep.app.ui.theme.KastepCyan
// » Memanggil KastepGray (dari com.kastep.app.ui.theme) supaya bisa dipakai di file ini.
import com.kastep.app.ui.theme.KastepGray
// » Memanggil KastepTheme (dari com.kastep.app.ui.theme) supaya bisa dipakai di file ini.
import com.kastep.app.ui.theme.KastepTheme
// » Memanggil KastepWhite (dari com.kastep.app.ui.theme) supaya bisa dipakai di file ini.
import com.kastep.app.ui.theme.KastepWhite
// » launch = menjalankan tugas di jalur belakang (coroutine) supaya layar tidak membeku.
import kotlinx.coroutines.launch

// » CARA BACA: "//" di awal baris = komentar, tidak dijalankan. Catatan ini menjelaskan baris kode di
// » bawahnya.
// CARI-MULAI | Aplikasi dibuka dari sini: membuat KastepViewModel lalu menampilkan KastepApp.
// » class MainActivity = layar utama yang dibuka Android saat ikon KASTEP ditekan (didaftarkan di manifest).
// » ": ComponentActivity()" = mewarisi sifat layar dasar Android.
class MainActivity : ComponentActivity() {

    // » viewModel = tempat data dan logika KASTEP disimpan. by viewModels() = dibuat sekali, tidak hilang saat
    // » layar diputar.
    private val viewModel: KastepViewModel by viewModels()

    // Titik awal aplikasi: memasang tema KASTEP lalu memanggil KastepApp.
    // » onCreate = fungsi yang dijalankan Android pertama kali saat layar dibuat. savedInstanceState = kondisi
    // » tersimpan sebelumnya (bisa kosong).
    override fun onCreate(savedInstanceState: Bundle?) {
        // » super.onCreate = menjalankan bagian standar Android (wajib dipanggil dulu).
        super.onCreate(savedInstanceState)
        // » setContent = memasang tampilan Compose ke layar.
        setContent {
            // » KastepTheme = membungkus semua tampilan dengan tema warna & huruf KASTEP.
            KastepTheme {
                // » Surface = lapisan dasar layar yang memberi warna latar.
                Surface(
                    // » fillMaxSize = lapisan memenuhi seluruh layar.
                    modifier = Modifier.fillMaxSize(),
                    // » color KastepBlack = warna latar hitam KASTEP.
                    color = KastepBlack
                ) {
                    // » KastepApp(viewModel) = memanggil kerangka aplikasi (menu samping + halaman), dengan viewModel yang sama.
                    KastepApp(viewModel = viewModel)
                }
            }
        }
    }
}

// » DrawerMenuItem = cetakan data satu baris menu samping. private = hanya dipakai di file ini.
private data class DrawerMenuItem(
    // » label = tulisan menu (mis. "Dashboard").
    val label: String,
    // » icon = ikon di sebelah kiri tulisan.
    val icon: ImageVector,
    // » route = alamat halaman yang dituju saat menu ditekan.
    val route: String
)

// » Catatan bawaan: drawer (menu samping) hanya boleh dibuka setelah login.
// Kerangka aplikasi. drawerEnabled = true hanya di halaman setelah login (menu samping tidak boleh dibuka di Login/Register/Splash).
// Kerangka aplikasi: menu samping (drawer) + NavGraph. Drawer hanya aktif setelah login.
// » @Composable = penanda fungsi penggambar tampilan.
@Composable
// » KastepApp = kerangka aplikasi. Menyusun menu samping dan halaman (NavGraph).
fun KastepApp(viewModel: KastepViewModel) {
    // » navController = pengatur perpindahan halaman. rememberNavController() = dibuat dan diingat selama layar
    // » hidup.
    val navController = rememberNavController()
    // » drawerState = status menu samping. initialValue Closed = awalnya tertutup.
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    // » scope = pengelola tugas belakang untuk membuka/menutup menu (animasi).
    val scope = rememberCoroutineScope()

    // » currentBackStackEntry = halaman yang sedang tampil (dipantau supaya tampilan ikut berubah).
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    // » currentRoute = alamat (rute) halaman yang sedang tampil, mis. "dashboard".
    val currentRoute = currentBackStackEntry?.destination?.route

    // Only enable drawer for post-login screens
    // » drawerEnabled = true kalau halaman sekarang termasuk daftar halaman setelah login.
    val drawerEnabled = currentRoute in listOf(
        // » Dashboard termasuk halaman setelah login.
        Screen.Dashboard.route,
        // » Data Siswa termasuk.
        Screen.DataSiswa.route,
        // » Riwayat Pembayaran termasuk.
        Screen.RiwayatPembayaran.route,
        // » Pembayaran termasuk.
        Screen.Pembayaran.route,
        // » Mata Pelajaran termasuk.
        Screen.MataPelajaran.route,
        // » Profil termasuk.
        Screen.Profile.route,
        // » Pengeluaran Kas termasuk.
        Screen.PengeluaranKas.route,
        // » Laporan Kas termasuk.
        Screen.LaporanKas.route,
        // » Pembayaran Berhasil termasuk.
        Screen.PembayaranBerhasil.route
    )

    // » Penjelasan daftar menu samping: tambah menu = tambah satu baris DrawerMenuItem, lalu daftarkan halaman
    // » di NavGraph.
    // DAFTAR MENU SAMPING. Satu DrawerMenuItem = tulisan, ikon, dan rute (nama halaman di Screen.kt). Menekan menu -> navController.navigate(rute).
    // Menu yang sedang dibuka diberi latar biru. Menambah menu: tambah satu baris di sini dan satu composable di NavGraph.
    // Drawer items matching the design exactly:
    // Dashboard, Data Siswa, Pembayaran, Pengeluaran, Mata Pelajaran, Laporan Kas
    // CARI-NAVIGASI | Isi menu samping. Tambah menu = DrawerMenuItem(label, ikon, Screen.X.route) + daftarkan di NavGraph.
    // » drawerItems = daftar semua menu samping yang tampil, urut dari atas ke bawah.
    val drawerItems = listOf(
        // » Menu Dashboard: ikon dashboard, menuju rute Dashboard.
        DrawerMenuItem("Dashboard", Icons.Default.Dashboard, Screen.Dashboard.route),
        // » Menu Data Siswa: ikon orang, menuju rute DataSiswa.
        DrawerMenuItem("Data Siswa", Icons.Default.People, Screen.DataSiswa.route),
        // » Menu Pembayaran: ikon pembayaran, menuju rute Pembayaran.
        DrawerMenuItem("Pembayaran", Icons.Default.Payment, Screen.Pembayaran.route),
        // » Menu Riwayat Bayar: ikon sejarah, menuju rute RiwayatPembayaran.
        DrawerMenuItem("Riwayat Bayar", Icons.Default.History, Screen.RiwayatPembayaran.route),
        // » Menu Pengeluaran: ikon pengeluaran, menuju rute PengeluaranKas.
        DrawerMenuItem("Pengeluaran", Icons.Default.AssignmentReturn, Screen.PengeluaranKas.route),
        // » Menu Mata Pelajaran: ikon buku, menuju rute MataPelajaran.
        DrawerMenuItem("Mata Pelajaran", Icons.Default.Book, Screen.MataPelajaran.route),
        // » Menu Laporan Kas: ikon dokumen, menuju rute LaporanKas.
        DrawerMenuItem("Laporan Kas", Icons.Default.Description, Screen.LaporanKas.route)
    )

    // ModalNavigationDrawer = menu samping yang menutupi layar. Isi layarnya (di dalam kurung terakhir) adalah NavGraph, yaitu halaman-halaman aplikasi.
    // » ModalNavigationDrawer = menu samping yang menutupi layar saat dibuka.
    ModalNavigationDrawer(
        // » drawerState = status buka/tutup menu.
        drawerState = drawerState,
        // » gesturesEnabled = menu boleh digeser dengan jari hanya kalau drawerEnabled true (halaman setelah login).
        gesturesEnabled = drawerEnabled,
        // » drawerContent = isi menu samping.
        drawerContent = {
            // » ModalDrawerSheet = lembar menu samping, lebarnya 220dp.
            ModalDrawerSheet(
                // » modifier = lebar 220dp, tinggi penuh.
                modifier = Modifier
                    .width(220.dp)
                    .fillMaxHeight()
            ) {
                // » Column = susunan atas-bawah isi menu.
                Column(
                    // » modifier = isi memenuhi lembar menu.
                    modifier = Modifier
                        .fillMaxSize()
                        // » Warna latar hitam hampir pekat (97%).
                        .background(KastepBlack.copy(alpha = 0.97f))
                        // » Jarak atas-bawah 16dp.
                        .padding(vertical = 16.dp)
                ) {
                    // » Spacer = jarak kosong 24dp di atas daftar menu.
                    Spacer(modifier = Modifier.height(24.dp))

                    // Menu items
                    // » Untuk setiap menu di daftar (item)...
                    drawerItems.forEach { item ->
                        // » isSelected = true kalau rute menu ini sama dengan halaman yang sedang tampil.
                        val isSelected = currentRoute == item.route
                        // » Row = satu baris menu (ikon + tulisan) dalam susunan mendatar.
                        Row(
                            // » modifier = penuh lebar.
                            modifier = Modifier
                                .fillMaxWidth()
                                // » clickable = baris bisa diketuk.
                                .clickable {
                                    // » Tutup menu samping dulu (animasi).
                                    scope.launch { drawerState.close() }
                                    // » Kalau rute menu ini belum sedang dibuka...
                                    if (currentRoute != item.route) {
                                        // » ...pindah ke halaman itu (navigate).
                                        navController.navigate(item.route) {
                                            // » popUpTo = buang halaman-halaman di atasnya sampai Dashboard, supaya tumpukan halaman tidak menumpuk.
                                            popUpTo(Screen.Dashboard.route) {
                                                // » saveState = simpan kondisi halaman yang ditinggalkan.
                                                saveState = true
                                            }
                                            // » launchSingleTop = jangan buka halaman yang sama dua kali.
                                            launchSingleTop = true
                                            // » restoreState = kalau kembali, pulihkan kondisi halaman itu.
                                            restoreState = true
                                        }
                                    }
                                }
                                // » background = warna latar baris.
                                .background(
                                    // » Kalau dipilih: latar biru transparan 60%.
                                    if (isSelected) KastepBlue.copy(alpha = 0.6f)
                                    // » Kalau tidak dipilih: latar transparan (tidak terlihat).
                                    else KastepBlack.copy(alpha = 0f)
                                )
                                // » padding = jarak dalam baris: 16dp kiri-kanan, 12dp atas-bawah.
                                .padding(horizontal = 16.dp, vertical = 12.dp),
                            // » Ikon dan tulisan rata tengah secara vertikal.
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // » Icon = gambar ikon menu.
                            Icon(
                                // » item.icon = ikon sesuai menu.
                                item.icon,
                                // » contentDescription = teks untuk pembaca layar (aksesibilitas) = nama menu.
                                contentDescription = item.label,
                                // » Warna ikon: putih kalau dipilih, abu kalau tidak.
                                tint = if (isSelected) KastepWhite else KastepGray,
                                // » size 20dp = ukuran ikon.
                                modifier = Modifier.size(20.dp)
                            )
                            // » Spacer 12dp = jarak antara ikon dan tulisan.
                            Spacer(modifier = Modifier.width(12.dp))
                            // » Text = tulisan nama menu.
                            Text(
                                // » text = nama menu dari item.label.
                                text = item.label,
                                // » Warna tulisan: putih kalau dipilih, abu kalau tidak.
                                color = if (isSelected) KastepWhite else KastepGray,
                                // » fontSize 14sp = ukuran huruf.
                                fontSize = 14.sp,
                                // » Tebal (SemiBold) kalau dipilih, normal kalau tidak.
                                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
                            )
                        }
                    }
                }
            }
        }
    // » Isi utama layar (di luar menu): kumpulan halaman KASTEP.
    ) {
        // » NavGraph = daftar semua halaman dan perpindahannya.
        NavGraph(
            // » navController = pengatur perpindahan halaman yang dibuat di atas.
            navController = navController,
            // » viewModel = sumber data yang dipakai semua halaman.
            viewModel = viewModel,
            // » onOpenDrawer = fungsi yang dipanggil halaman saat ikon menu (≡) ditekan.
            onOpenDrawer = {
                // » Membuka menu samping (animasi).
                scope.launch { drawerState.open() }
            }
        )
    }
}
