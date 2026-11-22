// ==========================================================================
// PETA FILE · KASTEP (Kelompok 1) · ProfileScreen.kt
// Isi file : Profil akun: edit akun dan keluar.
// Penanda  : CARI-PROFIL, CARI-LOGIN
// Cara cari: VS Code Ctrl+Shift+F (Android Studio Ctrl+Shift+F) lalu ketik
//            "CARI-" untuk semua penanda, atau salah satu nama di baris Penanda.
// ==========================================================================
//  BACA INI DULU
//  Profil akun: avatar, nama, email, No. HP, lalu dua menu: Edit Akun (ubah nama dan No. HP lewat viewModel.updateProfile)
//  dan Keluar (dialog konfirmasi lalu viewModel.logout, kembali ke Login).
// » Alamat folder (package) file ini di dalam proyek.
package com.kastep.app.ui.screens

// » background = mengisi warna/gradien latar sebuah tampilan.
import androidx.compose.foundation.background
// » border = garis tepi sebuah tampilan.
import androidx.compose.foundation.border
// » clickable = membuat tampilan bisa diketuk.
import androidx.compose.foundation.clickable
// » Memanggil Arrangement (dari androidx.compose.foundation.layout) supaya bisa dipakai di file ini.
import androidx.compose.foundation.layout.Arrangement
// » Memanggil Box (dari androidx.compose.foundation.layout) supaya bisa dipakai di file ini.
import androidx.compose.foundation.layout.Box
// » Memanggil Column (dari androidx.compose.foundation.layout) supaya bisa dipakai di file ini.
import androidx.compose.foundation.layout.Column
// » Memanggil Row (dari androidx.compose.foundation.layout) supaya bisa dipakai di file ini.
import androidx.compose.foundation.layout.Row
// » Memanggil Spacer (dari androidx.compose.foundation.layout) supaya bisa dipakai di file ini.
import androidx.compose.foundation.layout.Spacer
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
// » CircleShape = bentuk lingkaran.
import androidx.compose.foundation.shape.CircleShape
// » RoundedCornerShape = bentuk persegi dengan sudut membulat.
import androidx.compose.foundation.shape.RoundedCornerShape
// » Icons = daftar ikon bawaan Material.
import androidx.compose.material.icons.Icons
// » Ikon bawaan Material: Edit (versi terisi).
import androidx.compose.material.icons.filled.Edit
// » Ikon bawaan Material: ExitToApp (versi terisi).
import androidx.compose.material.icons.filled.ExitToApp
// » Ikon bawaan Material: Menu (versi terisi).
import androidx.compose.material.icons.filled.Menu
// » Ikon bawaan Material: Person (versi terisi).
import androidx.compose.material.icons.filled.Person
// » Memanggil AlertDialog (dari androidx.compose.material3) supaya bisa dipakai di file ini.
import androidx.compose.material3.AlertDialog
// » Memanggil Card (dari androidx.compose.material3) supaya bisa dipakai di file ini.
import androidx.compose.material3.Card
// » Memanggil CardDefaults (dari androidx.compose.material3) supaya bisa dipakai di file ini.
import androidx.compose.material3.CardDefaults
// » Memanggil HorizontalDivider (dari androidx.compose.material3) supaya bisa dipakai di file ini.
import androidx.compose.material3.HorizontalDivider
// » Memanggil Icon (dari androidx.compose.material3) supaya bisa dipakai di file ini.
import androidx.compose.material3.Icon
// » Memanggil IconButton (dari androidx.compose.material3) supaya bisa dipakai di file ini.
import androidx.compose.material3.IconButton
// » Memanggil OutlinedTextField (dari androidx.compose.material3) supaya bisa dipakai di file ini.
import androidx.compose.material3.OutlinedTextField
// » Memanggil Text (dari androidx.compose.material3) supaya bisa dipakai di file ini.
import androidx.compose.material3.Text
// » Memanggil TextButton (dari androidx.compose.material3) supaya bisa dipakai di file ini.
import androidx.compose.material3.TextButton
// » @Composable = penanda bahwa sebuah fungsi menggambar tampilan.
import androidx.compose.runtime.Composable
// » Memanggil collectAsState (dari androidx.compose.runtime) supaya bisa dipakai di file ini.
import androidx.compose.runtime.collectAsState
// » Memanggil getValue (dari androidx.compose.runtime) supaya bisa dipakai di file ini.
import androidx.compose.runtime.getValue
// » Memanggil mutableStateOf (dari androidx.compose.runtime) supaya bisa dipakai di file ini.
import androidx.compose.runtime.mutableStateOf
// » Memanggil remember (dari androidx.compose.runtime) supaya bisa dipakai di file ini.
import androidx.compose.runtime.remember
// » Memanggil setValue (dari androidx.compose.runtime) supaya bisa dipakai di file ini.
import androidx.compose.runtime.setValue
// » Alignment = pengatur posisi isi (tengah, atas, bawah, kiri, kanan).
import androidx.compose.ui.Alignment
// » Modifier = daftar 'dekorasi' sebuah tampilan: ukuran, jarak, warna latar, klik, dll.
import androidx.compose.ui.Modifier
// » clip = memotong tampilan mengikuti sebuah bentuk (mis. sudut bulat atau lingkaran).
import androidx.compose.ui.draw.clip
// » Color = tipe warna Compose; dipakai untuk menulis warna seperti Color.White atau Color(0xFF...).
import androidx.compose.ui.graphics.Color
// » ImageVector = tipe gambar ikon vektor.
import androidx.compose.ui.graphics.vector.ImageVector
// » FontWeight = ketebalan huruf (Normal, Medium, Bold, ExtraBold).
import androidx.compose.ui.text.font.FontWeight
// » dp = satuan ukuran Android yang sama di semua layar (mis. 16.dp).
import androidx.compose.ui.unit.dp
// » sp = satuan ukuran huruf Android (mengikuti pengaturan ukuran font di HP).
import androidx.compose.ui.unit.sp
// » Memanggil KastepViewModel (dari com.kastep.app.data) supaya bisa dipakai di file ini.
import com.kastep.app.data.KastepViewModel
// » Memanggil KastepBlack (dari com.kastep.app.ui.theme) supaya bisa dipakai di file ini.
import com.kastep.app.ui.theme.KastepBlack
// » Memanggil KastepBlue (dari com.kastep.app.ui.theme) supaya bisa dipakai di file ini.
import com.kastep.app.ui.theme.KastepBlue
// » Memanggil KastepCardDark (dari com.kastep.app.ui.theme) supaya bisa dipakai di file ini.
import com.kastep.app.ui.theme.KastepCardDark
// » Memanggil KastepGray (dari com.kastep.app.ui.theme) supaya bisa dipakai di file ini.
import com.kastep.app.ui.theme.KastepGray
// » Memanggil KastepRed (dari com.kastep.app.ui.theme) supaya bisa dipakai di file ini.
import com.kastep.app.ui.theme.KastepRed
// » Memanggil KastepWhite (dari com.kastep.app.ui.theme) supaya bisa dipakai di file ini.
import com.kastep.app.ui.theme.KastepWhite
// » Ikon bawaan Material: ArrowBack (versi terisi).
import androidx.compose.material.icons.automirrored.filled.ArrowBack

// » Komentar: penanda pencarian. Simpan di Edit Akun memanggil updateProfile; Keluar memanggil logout.
// CARI-PROFIL CARI-LOGIN | Tombol Simpan di Edit Akun memanggil viewModel.updateProfile; tombol Keluar memanggil logout.
// » @Composable = fungsi penggambar tampilan.
@Composable
// » fun ProfileScreen = layar Profil akun.
fun ProfileScreen(
    // » viewModel = sumber data akun.
    viewModel: KastepViewModel,
    // » onBack = fungsi kembali ke halaman sebelumnya.
    onBack: () -> Unit,
    // » onLogout = fungsi setelah keluar akun (pindah ke Login). Default {} = tidak melakukan apa-apa.
    onLogout: () -> Unit = {}
) {
    // » userProfile = data akun yang login (ikut berubah otomatis).
    val userProfile by viewModel.userProfile.collectAsState()
    // » showEditDialog = apakah dialog Edit Akun terbuka.
    var showEditDialog by remember { mutableStateOf(false) }
    // » showLogoutConfirm = apakah konfirmasi keluar terbuka.
    var showLogoutConfirm by remember { mutableStateOf(false) }

    // » Column = susunan atas-bawah seluruh layar.
    Column(
        // » Penuh sebesar layar.
        modifier = Modifier
            // » Penuh.
            .fillMaxSize()
            // » Latar hitam.
            .background(KastepBlack)
    ) {
        // Top bar
        // » Row = bar atas (tombol kembali di kiri, nama di kanan).
        Row(
            modifier = Modifier
                .fillMaxWidth()
                // » Jarak dalam 16dp kiri-kanan dan 12dp atas-bawah.
                .padding(horizontal = 16.dp, vertical = 12.dp),
            // » SpaceBetween = bagian ke kiri dan kanan.
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // » IconButton kembali: onClick panggil onBack.
            IconButton(onClick = onBack) {
                // » Icon panah kembali (ArrowBack) di dalam tombol.
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    // » contentDescription = "Kembali" untuk pembaca layar.
                    contentDescription = "Kembali",
                    tint = KastepWhite,
                    // » Ukuran ikon 28dp.
                    modifier = Modifier.size(28.dp)
                )
            }
            // » Teks nama akun di bar atas (14sp).
            Text(
                text = userProfile.nama,
                color = KastepWhite,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
        }

        // » Jarak 24dp.
        Spacer(modifier = Modifier.height(24.dp))

        // Profile avatar centered
        // » Column bagian foto profil, rata tengah secara horizontal.
        Column(
            // » Penuh lebar.
            modifier = Modifier.fillMaxWidth(),
            // » Isi di tengah.
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // » Box = lingkaran foto profil.
            Box(
                // » modifier foto.
                modifier = Modifier
                    // » Ukuran 140dp.
                    .size(140.dp)
                    // » clip(CircleShape) = potong jadi lingkaran.
                    .clip(CircleShape)
                    // » border = garis tepi biru 4dp berbentuk lingkaran.
                    .border(4.dp, KastepBlue, CircleShape)
                    // » Latar abu transparan 40%.
                    .background(KastepGray.copy(alpha = 0.4f)),
                // » Isi di tengah.
                contentAlignment = Alignment.Center
            ) {
                // » Icon = ikon orang sebagai foto profil.
                Icon(
                    // » Ikon Person.
                    Icons.Default.Person,
                    // » contentDescription "Avatar".
                    contentDescription = "Avatar",
                    // » Warna putih transparan 70%.
                    tint = KastepWhite.copy(alpha = 0.7f),
                    // » Ukuran 80dp.
                    modifier = Modifier.size(80.dp)
                )
            }

            // » Jarak 16dp.
            Spacer(modifier = Modifier.height(16.dp))

            // » Teks nama akun besar di bawah foto.
            Text(
                // » text = nama akun.
                text = userProfile.nama,
                color = KastepWhite,
                // » Ukuran 24sp.
                fontSize = 24.sp,
                // » Tebal.
                fontWeight = FontWeight.Bold
            )

            // » Jarak 4dp.
            Spacer(modifier = Modifier.height(4.dp))

            Text(
                // » Teks email akun (abu, 14sp).
                text = userProfile.email,
                color = KastepGray,
                fontSize = 14.sp
            )

            // » Kalau nomor HP tidak kosong, tampilkan...
            if (userProfile.noHp.isNotBlank()) {
                // » ...jarak 2dp.
                Spacer(modifier = Modifier.height(2.dp))
                // » Teks nomor HP (abu, 13sp).
                Text(
                    text = userProfile.noHp,
                    color = KastepGray,
                    fontSize = 13.sp
                )
            }
        }

        // » Jarak 32dp sebelum kartu menu.
        Spacer(modifier = Modifier.height(32.dp))

        // Menu items card — only Edit Akun and Logout
        // » Card = kartu berisi menu Edit Akun dan Keluar.
        Card(
            modifier = Modifier
                .fillMaxWidth()
                // » Jarak 16dp kiri-kanan.
                .padding(horizontal = 16.dp),
            // » Sudut membulat 16dp.
            shape = RoundedCornerShape(16.dp),
            // » Warna kartu biru tua.
            colors = CardDefaults.cardColors(containerColor = KastepCardDark)
        ) {
            // » Column isi kartu, susun ke bawah.
            Column {
                // » Baris menu "Edit Akun" (ProfileMenuItem dibuat di bawah).
                ProfileMenuItem(
                    // » Ikon pensil (Edit).
                    icon = Icons.Default.Edit,
                    // » Label "Edit Akun".
                    label = "Edit Akun",
                    // » onClick: buka dialog Edit Akun.
                    onClick = { showEditDialog = true }
                )
                // » Garis pemisah tipis antar menu.
                HorizontalDivider(color = KastepGray.copy(alpha = 0.15f), thickness = 0.5.dp)
                // » Baris menu "Keluar".
                ProfileMenuItem(
                    // » Ikon keluar (ExitToApp).
                    icon = Icons.Default.ExitToApp,
                    // » Label "Keluar".
                    label = "Keluar",
                    // » Warna merah.
                    tint = KastepRed,
                    // » onClick: buka konfirmasi keluar.
                    onClick = { showLogoutConfirm = true }
                )
            }
        }
    }

    // Edit Akun Dialog
    // » Kalau dialog Edit Akun terbuka...
    if (showEditDialog) {
        // » editNama = salinan nama untuk diedit (awal = nama akun). Hanya dipakai di dialog ini.
        var editNama by remember { mutableStateOf(userProfile.nama) }
        // » editNoHp = salinan nomor HP untuk diedit.
        var editNoHp by remember { mutableStateOf(userProfile.noHp) }
        // » editError = pesan error di dialog (null = tidak ada).
        var editError by remember { mutableStateOf<String?>(null) }

        // » AlertDialog = kotak dialog Edit Akun.
        AlertDialog(
            // » Tekan di luar kotak = tutup dialog.
            onDismissRequest = { showEditDialog = false },
            // » Judul "Edit Akun", tebal.
            title = { Text("Edit Akun", fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    // » Kotak isian Nama.
                    OutlinedTextField(
                        // » value = isi kotak = editNama.
                        value = editNama,
                        // » Setiap diketik: simpan nama baru dan hapus pesan error.
                        onValueChange = { editNama = it; editError = null },
                        // » Label "Nama".
                        label = { Text("Nama") },
                        // » Penuh lebar.
                        modifier = Modifier.fillMaxWidth(),
                        // » singleLine = satu baris.
                        singleLine = true,
                        // » isError = kalau ada error, kotak jadi merah.
                        isError = editError != null,
                        // » supportingText = tampilkan pesan error kecil di bawah kotak.
                        supportingText = { editError?.let { Text(it, color = KastepRed) } }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    // » Kotak isian No. HP.
                    OutlinedTextField(
                        // » value = isi kotak = editNoHp.
                        value = editNoHp,
                        // » Setiap diketik: hanya simpan angka dan tanda + (filter).
                        onValueChange = { v -> editNoHp = v.filter { it.isDigit() || it == '+' } },
                        // » Label "No. HP".
                        label = { Text("No. HP") },
                        // » Penuh lebar.
                        modifier = Modifier.fillMaxWidth(),
                        // » Satu baris.
                        singleLine = true
                    )
                }
            },
            // » confirmButton = tombol Simpan.
            confirmButton = {
                // Sebelumnya tombol ini hanya menutup dialog tanpa menyimpan
                // » Saat ditekan: panggil updateProfile dengan nama dan no HP baru. Hasilnya null (berhasil) atau pesan
                // » error.
                TextButton(onClick = {
                    // » Simpan hasilnya ke editError: null berarti tidak ada error; teks berarti error ditampilkan.
                    editError = viewModel.updateProfile(editNama, editNoHp)
                    // » Kalau berhasil (editError null), tutup dialog.
                    if (editError == null) showEditDialog = false
                }) {
                    // » Teks tombol "Simpan".
                    Text("Simpan")
                }
            },
            // » dismissButton = tombol Batal.
            dismissButton = {
                // » Batal: tutup dialog tanpa menyimpan.
                TextButton(onClick = { showEditDialog = false }) {
                    Text("Batal")
                }
            }
        )
    }

    // Logout Confirmation Dialog
    // » Kalau konfirmasi keluar terbuka...
    if (showLogoutConfirm) {
        // » AlertDialog konfirmasi keluar.
        AlertDialog(
            // » Tekan di luar kotak = batal.
            onDismissRequest = { showLogoutConfirm = false },
            // » Judul "Keluar", tebal.
            title = { Text("Keluar", fontWeight = FontWeight.Bold) },
            // » Pertanyaan: "Yakin ingin keluar dari akun?".
            text = { Text("Yakin ingin keluar dari akun?") },
            confirmButton = {
                // » Tombol konfirmasi (Keluar) ditekan:
                TextButton(onClick = {
                    // » logout() = hapus sesi login (kembali ke profil kosong).
                    viewModel.logout()
                    // » Tutup konfirmasi.
                    showLogoutConfirm = false
                    // » onLogout() = pindah ke halaman Login (diatur di NavGraph).
                    onLogout()
                // » Teks tombol "Keluar", merah.
                }) { Text("Keluar", color = KastepRed) }
            },
            dismissButton = {
                // » Tombol Batal: tutup tanpa keluar.
                TextButton(onClick = { showLogoutConfirm = false }) { Text("Batal") }
            }
        )
    }
}

// » Komentar: satu baris menu di Profil.
// Satu baris menu di Profil (Edit Akun, Keluar).
// » @Composable private fun ProfileMenuItem = baris menu (ikon + teks) yang hanya dipakai di file ini.
@Composable
// » Parameter-parameter baris menu dijelaskan di bawah.
private fun ProfileMenuItem(
    // » icon = gambar ikon di sebelah kiri.
    icon: ImageVector,
    // » label = tulisan baris menu.
    label: String,
    // » tint = warna ikon dan teks (default putih).
    tint: Color = KastepWhite,
    // » onClick = aksi saat baris ditekan.
    onClick: () -> Unit
) {
    // » Row = ikon dan tulisan berdampingan.
    Row(
        // » modifier baris.
        modifier = Modifier
            // » Penuh lebar.
            .fillMaxWidth()
            // » clickable: saat ditekan, panggil onClick().
            .clickable { onClick() }
            // » Jarak dalam 20dp kiri-kanan dan 18dp atas-bawah.
            .padding(horizontal = 20.dp, vertical = 18.dp),
        // » Ikon dan tulisan rata tengah secara vertikal.
        verticalAlignment = Alignment.CenterVertically
    ) {
        // » Icon = gambar ikon baris.
        Icon(
            // » icon = ikon yang diberikan.
            icon,
            // » contentDescription = label untuk pembaca layar.
            contentDescription = label,
            // » tint = warna ikon.
            tint = tint,
            // » Ukuran ikon 24dp.
            modifier = Modifier.size(24.dp)
        )
        // » Jarak 16dp antara ikon dan tulisan.
        Spacer(modifier = Modifier.width(16.dp))
        // » Teks label baris menu.
        Text(
            // » text = label.
            text = label,
            // » color = tint (warna sama dengan ikon).
            color = tint,
            // » Ukuran 16sp.
            fontSize = 16.sp,
            // » Semi tebal.
            fontWeight = FontWeight.Medium
        )
    }
}
