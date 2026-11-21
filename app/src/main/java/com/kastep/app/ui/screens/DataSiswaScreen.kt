// ==========================================================================
// PETA FILE · KASTEP (Kelompok 1) · DataSiswaScreen.kt
// Isi file : Data Siswa: tabel status kas per bulan, denda, tambah/edit/hapus siswa; juga TopBar bersama.
// Penanda  : CARI-ANGGOTA, CARI-DENDA, CARI-VALIDASI, CARI-NAVIGASI
// Cara cari: VS Code Ctrl+Shift+F (Android Studio Ctrl+Shift+F) lalu ketik
//            "CARI-" untuk semua penanda, atau salah satu nama di baris Penanda.
// ==========================================================================
//  BACA INI DULU : LAYAR "DATA SISWA"
//  Tabel 33 siswa: No, Nama, Peran, satu kolom per bulan (Juli sampai bulan sekarang), dan kolom Denda. Bisa digeser mendatar dan dicari.
//  Simbol di kolom bulan:  ✓ lunas  |  angka "2/5" = baru 2 dari 5 Senin  |  D = dispensasi (BI/PKL)  |  ✗ belum  |  · = disembunyikan.
//  ADMIN: melihat semua baris, menekan baris untuk edit atau hapus, ada tombol tambah siswa.
//  USER : mode lihat saja. Status dan denda HANYA terlihat di baris namanya sendiri, baris teman tampil "·" (privasi, supaya tunggakan tidak mempermalukan).
//  Aturan privasi ada di variabel bolehLihat di dalam daftar.
package com.kastep.app.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kastep.app.data.KastepViewModel
import com.kastep.app.data.Siswa
import com.kastep.app.data.StatusBayar
import com.kastep.app.data.UserRole
import com.kastep.app.ui.theme.*

// State: students (daftar), isAdmin, query (kata cari), showAddDialog / showEditDialog / showDeleteConfirm, selectedStudent (baris yang sedang diedit), dialogError (pesan bila nama kembar).
// CARI-ANGGOTA CARI-DENDA | Kolom bulan: ✓ lunas, angka = minggu terbayar, D = dispensasi, ✗ belum. Kolom terakhir = total denda.
@Composable
fun DataSiswaScreen(viewModel: KastepViewModel, onOpenDrawer: () -> Unit, onNavigateToProfile: () -> Unit = {}) {
    val students by viewModel.students.collectAsState()
    val userProfile by viewModel.userProfile.collectAsState()
    val currentDate = viewModel.getCurrentDateString()
    val isAdmin = userProfile.role == UserRole.ADMIN
    val bulanKas = viewModel.getBulanKas()

    var showAddDialog by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf(false) }
    var showDeleteConfirm by remember { mutableStateOf(false) }
    var selectedStudent by remember { mutableStateOf<Siswa?>(null) }
    var dialogError by remember { mutableStateOf<String?>(null) }
    var query by remember { mutableStateOf("") }

    val shown = students.filter { query.isBlank() || it.nama.contains(query.trim(), ignoreCase = true) || it.peran.contains(query.trim(), ignoreCase = true) }

    Column(modifier = Modifier.fillMaxSize().background(KastepBlack)) {
        TopBar(currentDate = currentDate, userName = userProfile.nama, onMenuClick = onOpenDrawer, onNavigateToProfile = onNavigateToProfile)

        Column(modifier = Modifier.padding(horizontal = 20.dp)) {
            Box(
                modifier = Modifier
                    .background(KastepBlue.copy(alpha = 0.25f), RoundedCornerShape(4.dp))
                    .padding(horizontal = 8.dp, vertical = 2.dp)
            ) {
                Text("Data Siswa", color = KastepWhite, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text("Status kas per bulan (Rp 5.000 / minggu)", color = KastepGray, fontSize = 13.sp)
        }

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = query,
            onValueChange = { query = it },
            modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
            placeholder = { Text("Cari nama atau peran", color = KastepGray) },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = KastepGray) },
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = KastepWhite, unfocusedTextColor = KastepWhite,
                focusedBorderColor = KastepCyan, unfocusedBorderColor = KastepGray.copy(alpha = 0.5f), cursorColor = KastepCyan
            )
        )

        Spacer(modifier = Modifier.height(8.dp))
        Text(
            "Jumlah Siswa : ${students.size}" + if (shown.size != students.size) "  ·  ditampilkan ${shown.size}" else "",
            color = KastepWhite, fontSize = 15.sp, fontWeight = FontWeight.Medium, modifier = Modifier.padding(horizontal = 20.dp)
        )
        Text(
            (if (isAdmin) "Tap baris siswa untuk edit/hapus. " else "Mode lihat saja. Status dan denda hanya tampil di baris kamu sendiri. ") + "✓ lunas · angka = minggu terbayar · D = dispensasi",
            color = KastepGray, fontSize = 11.sp, modifier = Modifier.padding(horizontal = 20.dp)
        )

        Spacer(modifier = Modifier.height(10.dp))

        Column(modifier = Modifier.weight(1f).padding(horizontal = 4.dp)) {
            val hScroll = rememberScrollState()

            Row(
                modifier = Modifier.fillMaxWidth().horizontalScroll(hScroll)
                    .background(Color(0xFF444444)).padding(vertical = 10.dp, horizontal = 12.dp)
            ) {
                Text("No", color = KastepWhite, fontWeight = FontWeight.Bold, fontSize = 13.sp, modifier = Modifier.width(35.dp))
                Text("Nama Siswa", color = KastepWhite, fontWeight = FontWeight.Bold, fontSize = 13.sp, modifier = Modifier.width(220.dp))
                Text("Peran", color = KastepWhite, fontWeight = FontWeight.Bold, fontSize = 13.sp, modifier = Modifier.width(100.dp))
                bulanKas.forEach { b ->
                    Text(b.take(3), color = KastepWhite, fontWeight = FontWeight.Bold, fontSize = 13.sp, modifier = Modifier.width(48.dp), textAlign = TextAlign.Center)
                }
                Text("Denda", color = KastepWhite, fontWeight = FontWeight.Bold, fontSize = 13.sp, modifier = Modifier.width(80.dp), textAlign = TextAlign.End)
            }

            Column(modifier = Modifier.weight(1f).verticalScroll(rememberScrollState())) {
                if (shown.isEmpty()) {
                    Text("Tidak ada siswa yang cocok dengan \"$query\"", color = KastepGray, fontSize = 13.sp, modifier = Modifier.padding(16.dp))
                }
                shown.forEach { siswa ->
                    val denda = viewModel.totalDenda(siswa)
                    // bolehLihat = true bila akun ADMIN, atau bila baris ini milik akun yang login (nama siswa sama dengan nama akun, keduanya huruf besar).
                    // Bila false, kolom bulan diganti "·" dan kolom denda dikosongkan.
                    // Privasi: status dan denda siswa lain hanya terlihat oleh ADMIN (sasaran "tunggakan tidak mempermalukan")
                    // TANYA-GURU: Kenapa siswa lain tampil titik? Akun USER hanya melihat status dan dendanya sendiri supaya tidak dipermalukan.
                    val bolehLihat = isAdmin || siswa.nama == userProfile.nama.trim().uppercase()
                    Row(
                        modifier = Modifier.fillMaxWidth().horizontalScroll(hScroll)
                            .then(if (isAdmin) Modifier.clickable {
                                selectedStudent = siswa
                                dialogError = null
                                showEditDialog = true
                            } else Modifier)
                            .padding(vertical = 10.dp, horizontal = 12.dp)
                    ) {
                        Text("${siswa.no}", color = KastepWhite, fontSize = 13.sp, modifier = Modifier.width(35.dp))
                        Text(siswa.nama, color = KastepWhite, fontSize = 13.sp, modifier = Modifier.width(220.dp))
                        Text(siswa.peran, color = KastepCyan, fontSize = 13.sp, modifier = Modifier.width(100.dp))
                        bulanKas.forEach { b ->
                            val lunas = viewModel.statusBulan(siswa, b) == StatusBayar.LUNAS
                            val dispensasi = (siswa.dispensasi[b] ?: 0) > 0
                            val minggu = viewModel.mingguLunas(siswa, b)
                            Text(
                                text = when {
                                    !bolehLihat -> "·"
                                    lunas && dispensasi -> "D"
                                    lunas -> "✓"
                                    minggu > 0 -> "$minggu/${viewModel.jumlahMinggu(b)}"
                                    else -> "✗"
                                },
                                color = when {
                                    !bolehLihat -> KastepGray
                                    lunas && dispensasi -> KastepCyan
                                    lunas -> KastepGreen
                                    minggu > 0 -> Color(0xFFFFC107)
                                    else -> KastepRed
                                },
                                fontSize = if (minggu > 0 && !lunas) 12.sp else 16.sp,
                                fontWeight = FontWeight.Bold, modifier = Modifier.width(48.dp), textAlign = TextAlign.Center
                            )
                        }
                        Text(
                            if (!bolehLihat) "" else if (denda > 0) "Rp ${KastepViewModel.formatRupiah(denda)}" else "-",
                            color = if (denda > 0) KastepRed else KastepGray, fontSize = 12.sp,
                            modifier = Modifier.width(80.dp), textAlign = TextAlign.End
                        )
                    }
                    HorizontalDivider(color = KastepGray.copy(alpha = 0.15f), thickness = 0.5.dp)
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        if (isAdmin) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = { dialogError = null; showAddDialog = true },
                    colors = ButtonDefaults.buttonColors(containerColor = KastepBlue),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Tambah", tint = KastepWhite, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Tambah Siswa", color = KastepWhite, fontSize = 13.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))
    }

    if (showAddDialog) {
        StudentDialog(
            title = "Tambah Siswa Baru",
            initialNama = "",
            initialPeran = "Anggota",
            error = dialogError,
            onDismiss = { showAddDialog = false },
            onConfirm = { nama, peran ->
                val err = viewModel.addStudent(nama, peran)
                if (err == null) showAddDialog = false else dialogError = err
            }
        )
    }

    if (showEditDialog && selectedStudent != null) {
        StudentDialog(
            title = "Edit Siswa",
            initialNama = selectedStudent!!.nama,
            initialPeran = selectedStudent!!.peran,
            showDelete = true,
            error = dialogError,
            onDismiss = { showEditDialog = false; selectedStudent = null },
            onConfirm = { nama, peran ->
                val err = viewModel.updateStudent(selectedStudent!!.no, nama, peran)
                if (err == null) { showEditDialog = false; selectedStudent = null } else dialogError = err
            },
            onDelete = {
                // Tanya dulu sebelum menghapus
                showEditDialog = false
                showDeleteConfirm = true
            }
        )
    }

    if (showDeleteConfirm && selectedStudent != null) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirm = false; selectedStudent = null },
            title = { Text("Hapus Siswa", fontWeight = FontWeight.Bold) },
            text = { Text("Yakin ingin menghapus ${selectedStudent!!.nama}? Riwayat pembayarannya tetap tersimpan.") },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.deleteStudent(selectedStudent!!.no)
                    showDeleteConfirm = false; selectedStudent = null
                }) { Text("Hapus", color = KastepRed) }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirm = false; selectedStudent = null }) { Text("Batal") }
            }
        )
    }
}

// CARI-ANGGOTA CARI-VALIDASI | Dialog tambah/edit siswa; menampilkan pesan bila nama sudah dipakai.
@Composable
private fun StudentDialog(
    title: String,
    initialNama: String,
    initialPeran: String,
    showDelete: Boolean = false,
    error: String? = null,
    onDismiss: () -> Unit,
    onConfirm: (String, String) -> Unit,
    onDelete: (() -> Unit)? = null
) {
    var nama by remember { mutableStateOf(initialNama) }
    var peran by remember { mutableStateOf(initialPeran) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title, fontWeight = FontWeight.Bold) },
        text = {
            Column {
                OutlinedTextField(
                    value = nama, onValueChange = { nama = it }, label = { Text("Nama Siswa") },
                    modifier = Modifier.fillMaxWidth(), singleLine = true,
                    isError = error != null, supportingText = { if (error != null) Text(error, color = KastepRed) }
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(value = peran, onValueChange = { peran = it }, label = { Text("Peran") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
                if (showDelete && onDelete != null) {
                    Spacer(modifier = Modifier.height(12.dp))
                    TextButton(onClick = onDelete, modifier = Modifier.fillMaxWidth()) {
                        Icon(Icons.Default.Delete, contentDescription = null, tint = KastepRed, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Hapus Siswa Ini", color = KastepRed)
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = { onConfirm(nama, peran) }, enabled = nama.isNotBlank()) { Text("Simpan") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Batal") }
        }
    )
}

// Bar atas yang dipakai hampir semua layar: tombol menu samping (kiri), tanggal hari ini (tengah), nama akun dan ikon profil (kanan, menuju Profil).
// Shared top bar
// CARI-NAVIGASI | Bar atas yang dipakai banyak halaman: tombol menu, tanggal, dan tombol profil.
@Composable
fun TopBar(currentDate: String, userName: String, onMenuClick: () -> Unit, onNavigateToProfile: () -> Unit = {}) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onMenuClick) {
            Icon(Icons.Default.Menu, contentDescription = "Menu", tint = KastepWhite, modifier = Modifier.size(28.dp))
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.CalendarMonth, contentDescription = "Calendar", tint = KastepWhite, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text(text = currentDate, color = KastepWhite, fontSize = 14.sp)
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .clip(RoundedCornerShape(20.dp))
                .clickable { onNavigateToProfile() }
                .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Box(modifier = Modifier.size(32.dp).clip(CircleShape).background(KastepGray), contentAlignment = Alignment.Center) {
                Icon(Icons.Default.Person, contentDescription = "Profile", tint = KastepWhite, modifier = Modifier.size(20.dp))
            }
            Spacer(modifier = Modifier.width(6.dp))
            Text(text = userName, color = KastepWhite, fontSize = 14.sp, fontWeight = FontWeight.Medium)
        }
    }
}
