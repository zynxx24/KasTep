package com.kastep.app.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import com.kastep.app.ui.theme.GradientBlueEnd
import com.kastep.app.ui.theme.GradientBlueStart
import com.kastep.app.ui.theme.KastepBlack
import com.kastep.app.ui.theme.KastepBlue
import com.kastep.app.ui.theme.KastepCyan
import com.kastep.app.ui.theme.KastepGray
import com.kastep.app.ui.theme.KastepGreen
import com.kastep.app.ui.theme.KastepRed
import com.kastep.app.ui.theme.KastepWhite

@Composable
fun DataSiswaScreen(
    viewModel: KastepViewModel,
    onOpenDrawer: () -> Unit
) {
    val students by viewModel.students.collectAsState()
    val userProfile by viewModel.userProfile.collectAsState()
    val currentDate = viewModel.getCurrentDateString()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(KastepBlack)
    ) {
        // Top bar
        TopBar(currentDate = currentDate, userName = userProfile.nama, onMenuClick = onOpenDrawer)

        // Title section
        Column(modifier = Modifier.padding(horizontal = 20.dp)) {
            Box(
                modifier = Modifier
                    .background(
                        color = KastepBlue.copy(alpha = 0.25f),
                        shape = RoundedCornerShape(4.dp)
                    )
                    .padding(horizontal = 8.dp, vertical = 2.dp)
            ) {
                Text(
                    text = "Data Siswa",
                    color = KastepWhite,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Ringkasan informasi kas kelas",
                color = KastepGray,
                fontSize = 13.sp
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Blue gradient decorative bar
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(20.dp)
                .padding(horizontal = 20.dp)
                .background(
                    brush = androidx.compose.ui.graphics.Brush.horizontalGradient(
                        colors = listOf(GradientBlueStart, GradientBlueEnd)
                    ),
                    shape = RoundedCornerShape(4.dp)
                )
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Jumlah Siswa counter
        Text(
            text = "Jumlah Siswa : ${students.size}",
            color = KastepWhite,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(horizontal = 20.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Table with horizontal scroll
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 4.dp)
        ) {
            val horizontalScrollState = rememberScrollState()

            // Table header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(horizontalScrollState)
                    .background(Color(0xFF444444))
                    .padding(vertical = 10.dp, horizontal = 12.dp)
            ) {
                Text("No", color = KastepWhite, fontWeight = FontWeight.Bold, fontSize = 13.sp, modifier = Modifier.width(35.dp))
                Text("Nama Siswa", color = KastepWhite, fontWeight = FontWeight.Bold, fontSize = 13.sp, modifier = Modifier.width(220.dp))
                Text("Peran", color = KastepWhite, fontWeight = FontWeight.Bold, fontSize = 13.sp, modifier = Modifier.width(100.dp))
                Text("Juli", color = KastepWhite, fontWeight = FontWeight.Bold, fontSize = 13.sp, modifier = Modifier.width(60.dp), textAlign = TextAlign.Center)
                Text("Agustus", color = KastepWhite, fontWeight = FontWeight.Bold, fontSize = 13.sp, modifier = Modifier.width(70.dp), textAlign = TextAlign.Center)
            }

            // Table rows
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
            ) {
                students.forEach { siswa ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(horizontalScrollState)
                            .padding(vertical = 10.dp, horizontal = 12.dp)
                    ) {
                        Text("${siswa.no}", color = KastepWhite, fontSize = 13.sp, modifier = Modifier.width(35.dp))
                        Text(siswa.nama, color = KastepWhite, fontSize = 13.sp, modifier = Modifier.width(220.dp))
                        Text(siswa.peran, color = KastepCyan, fontSize = 13.sp, modifier = Modifier.width(100.dp))
                        // Juli status
                        Text(
                            text = if (siswa.statusJuli == StatusBayar.LUNAS) "✓" else "✗",
                            color = if (siswa.statusJuli == StatusBayar.LUNAS) KastepGreen else KastepRed,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.width(60.dp),
                            textAlign = TextAlign.Center
                        )
                        // Agustus status
                        Text(
                            text = if (siswa.statusAgustus == StatusBayar.LUNAS) "✓" else "✗",
                            color = if (siswa.statusAgustus == StatusBayar.LUNAS) KastepGreen else KastepRed,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.width(70.dp),
                            textAlign = TextAlign.Center
                        )
                    }
                    HorizontalDivider(color = KastepGray.copy(alpha = 0.15f), thickness = 0.5.dp)
                }
            }

            // Scrollbar indicator
            Spacer(modifier = Modifier.height(8.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp))
                    .background(KastepGray.copy(alpha = 0.3f))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.4f)
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp))
                        .background(KastepGray.copy(alpha = 0.7f))
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Bottom actions: Edit, Hapus, Tambah Siswa
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row {
                TextButton(onClick = { }) {
                    Icon(Icons.Default.Edit, contentDescription = "Edit", tint = KastepWhite, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Edit", color = KastepWhite, fontSize = 14.sp)
                }
                Spacer(modifier = Modifier.width(4.dp))
                TextButton(onClick = { }) {
                    Icon(Icons.Default.Delete, contentDescription = "Hapus", tint = KastepWhite, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Hapus", color = KastepWhite, fontSize = 14.sp)
                }
            }

            Button(
                onClick = { },
                colors = ButtonDefaults.buttonColors(containerColor = KastepBlue),
                shape = RoundedCornerShape(20.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Tambah", tint = KastepWhite, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Tambah Siswa", color = KastepWhite, fontSize = 13.sp)
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Book illustration
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp),
            contentAlignment = Alignment.Center
        ) {
            BookIllustration()
        }

        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
private fun BookIllustration() {
    Canvas(modifier = Modifier.size(180.dp, 120.dp)) {
        val cyanColor = Color(0xFF00D4FF)
        val strokeWidth = 2.5f

        val leftPage = Path().apply {
            moveTo(size.width * 0.5f, size.height * 0.3f)
            cubicTo(size.width * 0.35f, size.height * 0.2f, size.width * 0.15f, size.height * 0.25f, size.width * 0.05f, size.height * 0.4f)
            lineTo(size.width * 0.05f, size.height * 0.9f)
            cubicTo(size.width * 0.15f, size.height * 0.75f, size.width * 0.35f, size.height * 0.7f, size.width * 0.5f, size.height * 0.8f)
            close()
        }
        val rightPage = Path().apply {
            moveTo(size.width * 0.5f, size.height * 0.3f)
            cubicTo(size.width * 0.65f, size.height * 0.2f, size.width * 0.85f, size.height * 0.25f, size.width * 0.95f, size.height * 0.4f)
            lineTo(size.width * 0.95f, size.height * 0.9f)
            cubicTo(size.width * 0.85f, size.height * 0.75f, size.width * 0.65f, size.height * 0.7f, size.width * 0.5f, size.height * 0.8f)
            close()
        }
        drawPath(leftPage, cyanColor, style = Stroke(width = strokeWidth, cap = StrokeCap.Round))
        drawPath(rightPage, cyanColor, style = Stroke(width = strokeWidth, cap = StrokeCap.Round))
        drawLine(cyanColor, Offset(size.width * 0.5f, size.height * 0.3f), Offset(size.width * 0.5f, size.height * 0.8f), strokeWidth = strokeWidth)
        for (i in 1..4) {
            val y = size.height * (0.45f + i * 0.07f)
            drawLine(cyanColor.copy(alpha = 0.4f), Offset(size.width * 0.15f, y), Offset(size.width * 0.45f, y), strokeWidth = 1f)
            drawLine(cyanColor.copy(alpha = 0.4f), Offset(size.width * 0.55f, y), Offset(size.width * 0.85f, y), strokeWidth = 1f)
        }
    }
}

// Shared top bar composable used across screens
@Composable
fun TopBar(
    currentDate: String,
    userName: String,
    onMenuClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
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
            Box(
                modifier = Modifier.size(32.dp).clip(CircleShape).background(KastepGray),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Person, contentDescription = "Profile", tint = KastepWhite, modifier = Modifier.size(20.dp))
            }
            Spacer(modifier = Modifier.width(6.dp))
            Text(text = userName, color = KastepWhite, fontSize = 14.sp, fontWeight = FontWeight.Medium)
        }
    }
}
