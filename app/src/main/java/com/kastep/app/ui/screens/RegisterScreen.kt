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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
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
import androidx.compose.ui.geometry.Offset

@Composable
fun RegisterScreen(
    viewModel: KastepViewModel,
    onRegisterSuccess: () -> Unit
) {
    var nama by remember { mutableStateOf("") }
    var nis by remember { mutableStateOf("") }
    var kelas by remember { mutableStateOf("") }
    var noHp by remember { mutableStateOf("") }
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
            // KASTEP Logo header (reused from LoginScreen)
            KastepLogoHeader()

            Spacer(modifier = Modifier.height(24.dp))

            // Register title
            Text(
                text = "Register",
                color = KastepWhite,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Nama field
            FormField(label = "Nama", value = nama, onValueChange = { nama = it })
            Spacer(modifier = Modifier.height(12.dp))

            // Nis field
            FormField(label = "Nis", value = nis, onValueChange = { nis = it })
            Spacer(modifier = Modifier.height(12.dp))

            // Kelas field
            FormField(label = "Kelas", value = kelas, onValueChange = { kelas = it })
            Spacer(modifier = Modifier.height(12.dp))

            // No hp field
            FormField(label = "No hp", value = noHp, onValueChange = { noHp = it })
            Spacer(modifier = Modifier.height(12.dp))

            // Password field
            FormField(
                label = "Password",
                value = password,
                onValueChange = { password = it },
                isPassword = true
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Login button with gradient
            Button(
                onClick = {
                    viewModel.register(nama, nis, kelas, noHp, password)
                    onRegisterSuccess()
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
        }
    }
}

@Composable
private fun FormField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    isPassword: Boolean = false
) {
    Text(
        text = label,
        color = KastepWhite,
        fontSize = 14.sp,
        fontWeight = FontWeight.Medium
    )
    Spacer(modifier = Modifier.height(6.dp))
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = KastepGray,
            unfocusedBorderColor = KastepGray.copy(alpha = 0.5f),
            focusedTextColor = KastepWhite,
            unfocusedTextColor = KastepWhite,
            cursorColor = KastepCyan
        ),
        singleLine = true
    )
}
