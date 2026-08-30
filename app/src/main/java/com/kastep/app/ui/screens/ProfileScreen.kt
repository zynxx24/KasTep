package com.kastep.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kastep.app.data.KastepViewModel
import com.kastep.app.ui.theme.KastepBlack
import com.kastep.app.ui.theme.KastepBlue
import com.kastep.app.ui.theme.KastepCardDark
import com.kastep.app.ui.theme.KastepGray
import com.kastep.app.ui.theme.KastepRed
import com.kastep.app.ui.theme.KastepWhite

@Composable
fun ProfileScreen(
    viewModel: KastepViewModel,
    onBack: () -> Unit
) {
    val userProfile by viewModel.userProfile.collectAsState()
    var showEditDialog by remember { mutableStateOf(false) }
    var showLogoutConfirm by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(KastepBlack)
    ) {
        // Top bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(
                    Icons.Default.Menu,
                    contentDescription = "Menu",
                    tint = KastepWhite,
                    modifier = Modifier.size(28.dp)
                )
            }
            Text(
                text = userProfile.nama,
                color = KastepWhite,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Profile avatar centered
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(140.dp)
                    .clip(CircleShape)
                    .border(4.dp, KastepBlue, CircleShape)
                    .background(KastepGray.copy(alpha = 0.4f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.Person,
                    contentDescription = "Avatar",
                    tint = KastepWhite.copy(alpha = 0.7f),
                    modifier = Modifier.size(80.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = userProfile.nama,
                color = KastepWhite,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = userProfile.email,
                color = KastepGray,
                fontSize = 14.sp
            )

            if (userProfile.noHp.isNotBlank()) {
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = userProfile.noHp,
                    color = KastepGray,
                    fontSize = 13.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Menu items card — only Edit Akun and Logout
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = KastepCardDark)
        ) {
            Column {
                ProfileMenuItem(
                    icon = Icons.Default.Edit,
                    label = "Edit Akun",
                    onClick = { showEditDialog = true }
                )
                HorizontalDivider(color = KastepGray.copy(alpha = 0.15f), thickness = 0.5.dp)
                ProfileMenuItem(
                    icon = Icons.Default.ExitToApp,
                    label = "Keluar",
                    tint = KastepRed,
                    onClick = { showLogoutConfirm = true }
                )
            }
        }
    }

    // Edit Akun Dialog
    if (showEditDialog) {
        var editNama by remember { mutableStateOf(userProfile.nama) }
        var editNoHp by remember { mutableStateOf(userProfile.noHp) }

        AlertDialog(
            onDismissRequest = { showEditDialog = false },
            title = { Text("Edit Akun", fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    OutlinedTextField(
                        value = editNama,
                        onValueChange = { editNama = it },
                        label = { Text("Nama") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = editNoHp,
                        onValueChange = { editNoHp = it },
                        label = { Text("No. HP") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = { showEditDialog = false }) {
                    Text("Simpan")
                }
            },
            dismissButton = {
                TextButton(onClick = { showEditDialog = false }) {
                    Text("Batal")
                }
            }
        )
    }

    // Logout Confirmation Dialog
    if (showLogoutConfirm) {
        AlertDialog(
            onDismissRequest = { showLogoutConfirm = false },
            title = { Text("Keluar", fontWeight = FontWeight.Bold) },
            text = { Text("Yakin ingin keluar dari akun?") },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.logout()
                    showLogoutConfirm = false
                }) { Text("Keluar", color = KastepRed) }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutConfirm = false }) { Text("Batal") }
            }
        )
    }
}

@Composable
private fun ProfileMenuItem(
    icon: ImageVector,
    label: String,
    tint: Color = KastepWhite,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 20.dp, vertical = 18.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            icon,
            contentDescription = label,
            tint = tint,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = label,
            color = tint,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium
        )
    }
}
