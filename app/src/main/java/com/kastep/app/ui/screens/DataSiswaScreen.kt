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

@Composable
fun DataSiswaScreen(viewModel: KastepViewModel, onOpenDrawer: () -> Unit) {
    val students by viewModel.students.collectAsState()
    val userProfile by viewModel.userProfile.collectAsState()
    val currentDate = viewModel.getCurrentDateString()
    val isAdmin = userProfile.role == UserRole.ADMIN

    var showAddDialog by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf(false) }
    var showDeleteConfirm by remember { mutableStateOf(false) }
    var selectedStudent by remember { mutableStateOf<Siswa?>(null) }

    Column(modifier = Modifier.fillMaxSize().background(KastepBlack)) {
        TopBar(currentDate = currentDate, userName = userProfile.nama, onMenuClick = onOpenDrawer)

        Column(modifier = Modifier.padding(horizontal = 20.dp)) {
            Box(
                modifier = Modifier
                    .background(KastepBlue.copy(alpha = 0.25f), RoundedCornerShape(4.dp))
                    .padding(horizontal = 8.dp, vertical = 2.dp)
            ) {
                Text("Data Siswa", color = KastepWhite, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text("Ringkasan informasi kas kelas", color = KastepGray, fontSize = 13.sp)
        }

        Spacer(modifier = Modifier.height(8.dp))

        Box(
            modifier = Modifier.fillMaxWidth().height(20.dp).padding(horizontal = 20.dp)
                .background(
                    brush = androidx.compose.ui.graphics.Brush.horizontalGradient(listOf(GradientBlueStart, GradientBlueEnd)),
                    shape = RoundedCornerShape(4.dp)
                )
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text("Jumlah Siswa : ${students.size}", color = KastepWhite, fontSize = 16.sp, fontWeight = FontWeight.Medium, modifier = Modifier.padding(horizontal = 20.dp))
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            if (isAdmin) "Tap baris siswa untuk edit/hapus" else "Mode lihat saja (hanya Admin yang dapat mengelola)",
            color = KastepGray, fontSize = 12.sp, modifier = Modifier.padding(horizontal = 20.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        Column(modifier = Modifier.weight(1f).padding(horizontal = 4.dp)) {
            val hScroll = rememberScrollState()

            Row(
                modifier = Modifier.fillMaxWidth().horizontalScroll(hScroll)
                    .background(Color(0xFF444444)).padding(vertical = 10.dp, horizontal = 12.dp)
            ) {
                Text("No", color = KastepWhite, fontWeight = FontWeight.Bold, fontSize = 13.sp, modifier = Modifier.width(35.dp))
                Text("Nama Siswa", color = KastepWhite, fontWeight = FontWeight.Bold, fontSize = 13.sp, modifier = Modifier.width(220.dp))
                Text("Peran", color = KastepWhite, fontWeight = FontWeight.Bold, fontSize = 13.sp, modifier = Modifier.width(100.dp))
                Text("Juli", color = KastepWhite, fontWeight = FontWeight.Bold, fontSize = 13.sp, modifier = Modifier.width(60.dp), textAlign = TextAlign.Center)
                Text("Agustus", color = KastepWhite, fontWeight = FontWeight.Bold, fontSize = 13.sp, modifier = Modifier.width(70.dp), textAlign = TextAlign.Center)
            }

            Column(modifier = Modifier.weight(1f).verticalScroll(rememberScrollState())) {
                students.forEach { siswa ->
                    Row(
                        modifier = Modifier.fillMaxWidth().horizontalScroll(hScroll)
                            .then(if (isAdmin) Modifier.clickable {
                                selectedStudent = siswa
                                showEditDialog = true
                            } else Modifier)
                            .padding(vertical = 10.dp, horizontal = 12.dp)
                    ) {
                        Text("${siswa.no}", color = KastepWhite, fontSize = 13.sp, modifier = Modifier.width(35.dp))
                        Text(siswa.nama, color = KastepWhite, fontSize = 13.sp, modifier = Modifier.width(220.dp))
                        Text(siswa.peran, color = KastepCyan, fontSize = 13.sp, modifier = Modifier.width(100.dp))
                        Text(
                            text = if (siswa.statusJuli == StatusBayar.LUNAS) "✓" else "✗",
                            color = if (siswa.statusJuli == StatusBayar.LUNAS) KastepGreen else KastepRed,
                            fontSize = 16.sp, fontWeight = FontWeight.Bold, modifier = Modifier.width(60.dp), textAlign = TextAlign.Center
                        )
                        Text(
                            text = if (siswa.statusAgustus == StatusBayar.LUNAS) "✓" else "✗",
                            color = if (siswa.statusAgustus == StatusBayar.LUNAS) KastepGreen else KastepRed,
                            fontSize = 16.sp, fontWeight = FontWeight.Bold, modifier = Modifier.width(70.dp), textAlign = TextAlign.Center
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
                    onClick = { showAddDialog = true },
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

    // Add Dialog
    if (showAddDialog) {
        StudentDialog(
            title = "Tambah Siswa Baru",
            initialNama = "",
            initialPeran = "Anggota",
            onDismiss = { showAddDialog = false },
            onConfirm = { nama, peran ->
                viewModel.addStudent(nama, peran)
                showAddDialog = false
            }
        )
    }

    // Edit Dialog
    if (showEditDialog && selectedStudent != null) {
        StudentDialog(
            title = "Edit Siswa",
            initialNama = selectedStudent!!.nama,
            initialPeran = selectedStudent!!.peran,
            showDelete = true,
            onDismiss = { showEditDialog = false; selectedStudent = null },
            onConfirm = { nama, peran ->
                viewModel.updateStudent(selectedStudent!!.no, nama, peran)
                showEditDialog = false; selectedStudent = null
            },
            onDelete = {
                viewModel.deleteStudent(selectedStudent!!.no)
                showEditDialog = false; selectedStudent = null
            }
        )
    }

    // Delete Confirm
    if (showDeleteConfirm && selectedStudent != null) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirm = false },
            title = { Text("Hapus Siswa", fontWeight = FontWeight.Bold) },
            text = { Text("Yakin ingin menghapus ${selectedStudent!!.nama}?") },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.deleteStudent(selectedStudent!!.no)
                    showDeleteConfirm = false; selectedStudent = null
                }) { Text("Hapus", color = KastepRed) }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirm = false }) { Text("Batal") }
            }
        )
    }
}

@Composable
private fun StudentDialog(
    title: String,
    initialNama: String,
    initialPeran: String,
    showDelete: Boolean = false,
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
                OutlinedTextField(value = nama, onValueChange = { nama = it }, label = { Text("Nama Siswa") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
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
            TextButton(onClick = { if (nama.isNotBlank()) onConfirm(nama, peran) }) { Text("Simpan") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Batal") }
        }
    )
}

// Shared top bar
@Composable
fun TopBar(currentDate: String, userName: String, onMenuClick: () -> Unit) {
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
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(32.dp).clip(CircleShape).background(KastepGray), contentAlignment = Alignment.Center) {
                Icon(Icons.Default.Person, contentDescription = "Profile", tint = KastepWhite, modifier = Modifier.size(20.dp))
            }
            Spacer(modifier = Modifier.width(6.dp))
            Text(text = userName, color = KastepWhite, fontSize = 14.sp, fontWeight = FontWeight.Medium)
        }
    }
}
