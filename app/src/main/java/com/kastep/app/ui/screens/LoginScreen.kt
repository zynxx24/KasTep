package com.kastep.app.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
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
fun LoginScreen(
    viewModel: KastepViewModel,
    onLoginSuccess: () -> Unit,
    onNavigateToRegister: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(KastepBlack)
    ) {
        Canvas(
            modifier = Modifier
                .size(350.dp)
                .align(Alignment.BottomEnd)
                .offset(x = 100.dp, y = 120.dp)
        ) {
            drawCircle(
                brush = Brush.linearGradient(
                    colors = listOf(CircleBlue, CirclePurple),
                    start = Offset(0f, 0f),
                    end = Offset(size.width, size.height)
                ),
                radius = size.minDimension / 2
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 40.dp)
        ) {
            KastepLogoHeader()

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Login Page",
                color = KastepWhite,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Admin: admin@gmail.com / admin123",
                color = KastepGray,
                fontSize = 12.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Error message
            if (errorMessage != null) {
                Text(
                    text = errorMessage!!,
                    color = KastepRed,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(KastepRed.copy(alpha = 0.1f), RoundedCornerShape(8.dp))
                        .padding(12.dp)
                )
                Spacer(modifier = Modifier.height(12.dp))
            }

            Text("Email", color = KastepWhite, fontSize = 14.sp, fontWeight = FontWeight.Medium)
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = email,
                onValueChange = { email = it; errorMessage = null },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                placeholder = { Text("contoh@email.com", color = KastepGray.copy(alpha = 0.5f)) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = KastepCyan,
                    unfocusedBorderColor = KastepGray.copy(alpha = 0.5f),
                    focusedTextColor = KastepWhite,
                    unfocusedTextColor = KastepWhite,
                    cursorColor = KastepCyan
                ),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text("Password", color = KastepWhite, fontSize = 14.sp, fontWeight = FontWeight.Medium)
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = password,
                onValueChange = { password = it; errorMessage = null },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                placeholder = { Text("Minimal 5 karakter", color = KastepGray.copy(alpha = 0.5f)) },
                visualTransformation = PasswordVisualTransformation(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = KastepCyan,
                    unfocusedBorderColor = KastepGray.copy(alpha = 0.5f),
                    focusedTextColor = KastepWhite,
                    unfocusedTextColor = KastepWhite,
                    cursorColor = KastepCyan
                ),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    val result = viewModel.login(email, password)
                    if (result == null) {
                        onLoginSuccess()
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
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            brush = Brush.horizontalGradient(listOf(GradientBlueStart, GradientBlueEnd)),
                            shape = RoundedCornerShape(16.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Login", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text("Belum memiliki akun? ", color = KastepWhite, fontSize = 14.sp)
                Text(
                    text = "Register",
                    color = KastepCyan,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { onNavigateToRegister() }
                )
            }
        }
    }
}

@Composable
fun KastepLogoHeader() {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("KASTEP", color = KastepWhite, fontSize = 18.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
            Canvas(modifier = Modifier.size(28.dp)) {
                val c = KastepCyan
                val sw = 1.5.dp.toPx()
                drawRoundRect(c, Offset(size.width * 0.05f, size.height * 0.3f), Size(size.width * 0.7f, size.height * 0.55f), CornerRadius(4.dp.toPx()), style = Stroke(sw))
                drawRoundRect(c, Offset(size.width * 0.15f, size.height * 0.12f), Size(size.width * 0.5f, size.height * 0.35f), CornerRadius(3.dp.toPx()), style = Stroke(sw * 0.8f))
                drawCircle(c, 3.dp.toPx(), Offset(size.width * 0.68f, size.height * 0.57f), style = Stroke(sw))
            }
        }
        Spacer(modifier = Modifier.width(12.dp))
        Box(modifier = Modifier.width(1.dp).height(36.dp).background(KastepGray.copy(alpha = 0.5f)))
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text("KAS ANAK", color = KastepWhite, fontSize = 11.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
            Text("SEKOLAH", color = KastepWhite, fontSize = 11.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
            Text("TEEP", color = KastepWhite, fontSize = 11.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
        }
    }
}
