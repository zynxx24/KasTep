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
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
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
import com.kastep.app.ui.theme.KastepWhite

@Composable
fun LoginScreen(
    viewModel: KastepViewModel,
    onLoginSuccess: () -> Unit,
    onNavigateToRegister: () -> Unit
) {
    var nameOrEmail by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(KastepBlack)
    ) {
        // Decorative circle at bottom-right
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
            // KASTEP Logo header
            KastepLogoHeader()

            Spacer(modifier = Modifier.height(32.dp))

            // Login Page title
            Text(
                text = "Login Page",
                color = KastepWhite,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Nama atau Email label and field
            Text(
                text = "Nama atau Email",
                color = KastepWhite,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = nameOrEmail,
                onValueChange = { nameOrEmail = it },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = KastepGray,
                    unfocusedBorderColor = KastepGray.copy(alpha = 0.5f),
                    focusedTextColor = KastepWhite,
                    unfocusedTextColor = KastepWhite,
                    cursorColor = KastepCyan
                ),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Password label and field
            Text(
                text = "Password",
                color = KastepWhite,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                visualTransformation = PasswordVisualTransformation(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = KastepGray,
                    unfocusedBorderColor = KastepGray.copy(alpha = 0.5f),
                    focusedTextColor = KastepWhite,
                    unfocusedTextColor = KastepWhite,
                    cursorColor = KastepCyan
                ),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Login Button with gradient
            Button(
                onClick = {
                    viewModel.login(nameOrEmail, password)
                    onLoginSuccess()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                contentPadding = ButtonDefaults.TextButtonContentPadding
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = listOf(GradientBlueStart, GradientBlueEnd)
                            ),
                            shape = RoundedCornerShape(16.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Login",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Register link
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Belum memiliki akun? ",
                    color = KastepWhite,
                    fontSize = 14.sp
                )
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
        // Logo - wallet icon + KASTEP text
        Row(verticalAlignment = Alignment.CenterVertically) {
            // Small wallet icon
            Text(
                text = "KASTEP",
                color = KastepWhite,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
            // Wallet icon drawn with canvas
            Canvas(modifier = Modifier.size(28.dp)) {
                val c = KastepCyan
                val sw = 1.5.dp.toPx()
                drawRoundRect(
                    color = c,
                    topLeft = Offset(size.width * 0.05f, size.height * 0.3f),
                    size = Size(size.width * 0.7f, size.height * 0.55f),
                    cornerRadius = CornerRadius(4.dp.toPx()),
                    style = Stroke(width = sw)
                )
                drawRoundRect(
                    color = c,
                    topLeft = Offset(size.width * 0.15f, size.height * 0.12f),
                    size = Size(size.width * 0.5f, size.height * 0.35f),
                    cornerRadius = CornerRadius(3.dp.toPx()),
                    style = Stroke(width = sw * 0.8f)
                )
                drawCircle(
                    color = c,
                    radius = 3.dp.toPx(),
                    center = Offset(size.width * 0.68f, size.height * 0.57f),
                    style = Stroke(width = sw)
                )
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        // Separator line
        Box(
            modifier = Modifier
                .width(1.dp)
                .height(36.dp)
                .background(KastepGray.copy(alpha = 0.5f))
        )

        Spacer(modifier = Modifier.width(12.dp))

        // KAS ANAK SEKOLAH TEEP text
        Column {
            Text(
                text = "KAS ANAK",
                color = KastepWhite,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
            Text(
                text = "SEKOLAH",
                color = KastepWhite,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
            Text(
                text = "TEEP",
                color = KastepWhite,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
        }
    }
}
