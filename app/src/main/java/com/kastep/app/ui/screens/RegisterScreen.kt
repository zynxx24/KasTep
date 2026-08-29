package com.kastep.app.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kastep.app.data.KastepViewModel
import com.kastep.app.ui.theme.CircleBlue
import com.kastep.app.ui.theme.CirclePurple
import com.kastep.app.ui.theme.GradientBlueEnd
import com.kastep.app.ui.theme.GradientBlueStart
import com.kastep.app.ui.theme.KastepBlack
import com.kastep.app.ui.theme.KastepCyan
import com.kastep.app.ui.theme.KastepGray
import com.kastep.app.ui.theme.KastepRed
import com.kastep.app.ui.theme.KastepWhite

@Composable
fun RegisterScreen(
    viewModel: KastepViewModel,
    onRegisterSuccess: () -> Unit
) {
    var nama by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var nis by remember { mutableStateOf("") }
    var kelas by remember { mutableStateOf("XII PPLG") }
    var noHp by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Box(
        modifier = Modifier.fillMaxSize().background(KastepBlack)
    ) {
        Canvas(
            modifier = Modifier.size(350.dp).align(Alignment.BottomEnd).offset(x = 100.dp, y = 120.dp)
        ) {
            drawCircle(
                brush = Brush.linearGradient(listOf(CircleBlue, CirclePurple), Offset(0f, 0f), Offset(size.width, size.height)),
                radius = size.minDimension / 2
            )
        }

        Column(
            modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(horizontal = 24.dp, vertical = 40.dp)
        ) {
            KastepLogoHeader()
            Spacer(modifier = Modifier.height(24.dp))

            Text("Register", color = KastepWhite, fontSize = 28.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Text("Buat akun baru untuk mengelola kas kelas", color = KastepGray, fontSize = 13.sp)

            Spacer(modifier = Modifier.height(16.dp))

            if (errorMessage != null) {
                Text(
                    text = errorMessage!!,
                    color = KastepRed,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.fillMaxWidth()
                        .background(KastepRed.copy(alpha = 0.1f), RoundedCornerShape(8.dp))
                        .padding(12.dp)
                )
                Spacer(modifier = Modifier.height(12.dp))
            }

            FormField(label = "Nama *", value = nama, onValueChange = { nama = it; errorMessage = null }, placeholder = "Nama lengkap (min. 3 karakter)")
            Spacer(modifier = Modifier.height(12.dp))
            FormField(label = "Email *", value = email, onValueChange = { email = it; errorMessage = null }, placeholder = "contoh@email.com")
            Spacer(modifier = Modifier.height(12.dp))
            FormField(label = "NIS", value = nis, onValueChange = { nis = it }, placeholder = "Nomor Induk Siswa")
            Spacer(modifier = Modifier.height(12.dp))
            FormField(label = "Kelas", value = kelas, onValueChange = { kelas = it }, placeholder = "XII PPLG")
            Spacer(modifier = Modifier.height(12.dp))
            FormField(label = "No HP", value = noHp, onValueChange = { noHp = it }, placeholder = "+62...")
            Spacer(modifier = Modifier.height(12.dp))
            FormField(label = "Password *", value = password, onValueChange = { password = it; errorMessage = null }, isPassword = true, placeholder = "Minimal 5 karakter")

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    val result = viewModel.register(nama, email, nis, kelas, noHp, password)
                    if (result == null) {
                        onRegisterSuccess()
                    } else {
                        errorMessage = result
                    }
                },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                contentPadding = ButtonDefaults.TextButtonContentPadding
            ) {
                Box(
                    modifier = Modifier.fillMaxSize()
                        .background(Brush.horizontalGradient(listOf(GradientBlueStart, GradientBlueEnd)), RoundedCornerShape(16.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Register", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun FormField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    isPassword: Boolean = false,
    placeholder: String = ""
) {
    Text(text = label, color = KastepWhite, fontSize = 14.sp, fontWeight = FontWeight.Medium)
    Spacer(modifier = Modifier.height(6.dp))
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        placeholder = { Text(placeholder, color = KastepGray.copy(alpha = 0.5f)) },
        visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = KastepCyan,
            unfocusedBorderColor = KastepGray.copy(alpha = 0.5f),
            focusedTextColor = KastepWhite,
            unfocusedTextColor = KastepWhite,
            cursorColor = KastepCyan
        ),
        singleLine = true
    )
}
