package com.kastep.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kastep.app.data.KastepViewModel
import com.kastep.app.data.MataPelajaran
import com.kastep.app.ui.theme.KastepBlack
import com.kastep.app.ui.theme.KastepGray
import com.kastep.app.ui.theme.KastepPurple
import com.kastep.app.ui.theme.KastepWhite

@Composable
fun MataPelajaranScreen(
    viewModel: KastepViewModel,
    onOpenDrawer: () -> Unit
) {
    val userProfile by viewModel.userProfile.collectAsState()
    val currentDate = viewModel.getCurrentDateString()
    val subjects = viewModel.mataPelajaran

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(KastepBlack)
    ) {
        // Top bar
        TopBar(currentDate = currentDate, userName = userProfile.nama, onMenuClick = onOpenDrawer)

        Spacer(modifier = Modifier.height(12.dp))

        // Title
        Text(
            text = "MATA\nPELAJARAN",
            color = KastepWhite,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            lineHeight = 30.sp,
            modifier = Modifier.padding(horizontal = 20.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Subjects grid — purple cards
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(0.dp),
            horizontalArrangement = Arrangement.spacedBy(0.dp)
        ) {
            items(subjects) { subject ->
                SubjectCard(subject = subject)
            }
        }

        // Decorative cyan line at the bottom
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(3.dp)
                .background(
                    brush = androidx.compose.ui.graphics.Brush.horizontalGradient(
                        colors = listOf(
                            com.kastep.app.ui.theme.KastepCyan.copy(alpha = 0.6f),
                            com.kastep.app.ui.theme.KastepCyan,
                            com.kastep.app.ui.theme.KastepCyan.copy(alpha = 0.6f)
                        )
                    )
                )
        )

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun SubjectCard(subject: MataPelajaran) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .padding(2.dp),
        shape = RoundedCornerShape(0.dp),
        colors = CardDefaults.cardColors(containerColor = KastepPurple)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = subject.nama,
                color = KastepWhite,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}
