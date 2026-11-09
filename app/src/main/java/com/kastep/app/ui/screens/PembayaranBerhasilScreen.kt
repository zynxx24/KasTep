package com.kastep.app.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kastep.app.R
import com.kastep.app.data.KastepViewModel
import com.kastep.app.ui.theme.KastepBlack
import com.kastep.app.ui.theme.KastepWhite
import kotlin.math.cos
import kotlin.math.sin

private val SuccessGreen = Color(0xFF12BE64)
private val DotGreen = Color(0xFF1ED775)
private val DividerDark = Color(0xFF0A2A17)

/** Green section with a convex dome on top, like the design. */
private object DomeShape : Shape {
    override fun createOutline(size: Size, layoutDirection: LayoutDirection, density: Density): Outline {
        val dome = with(density) { 70.dp.toPx() }
        val path = Path().apply {
            moveTo(0f, dome)
            quadraticBezierTo(size.width / 2f, -dome, size.width, dome)
            lineTo(size.width, size.height)
            lineTo(0f, size.height)
            close()
        }
        return Outline.Generic(path)
    }
}

@Composable
fun PembayaranBerhasilScreen(
    viewModel: KastepViewModel,
    onOpenDrawer: () -> Unit,
    onNavigateToProfile: () -> Unit = {},
    onKembali: () -> Unit
) {
    val userProfile by viewModel.userProfile.collectAsState()
    val records by viewModel.paymentRecords.collectAsState()
    val currentDate = viewModel.getCurrentDateString()

    // Newest record is always inserted at index 0 by the repository.
    val last = records.firstOrNull()
    val jumlah = "Rp " + KastepViewModel.formatRupiah(last?.jumlah ?: 0L)
    val atasNama = last?.namaSiswa ?: "-"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(KastepBlack)
    ) {
        TopBar(currentDate = currentDate, userName = userProfile.nama, onMenuClick = onOpenDrawer, onNavigateToProfile = onNavigateToProfile)

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            // Hero: dot grid + fireworks + mascot
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
                contentAlignment = Alignment.BottomCenter
            ) {
                DotGrid()
                Fireworks()
                Image(
                    painter = painterResource(id = R.drawable.ic_spongebob),
                    contentDescription = "Maskot pembayaran berhasil",
                    modifier = Modifier.size(300.dp),
                    contentScale = ContentScale.Fit
                )
            }

            // Green dome section, overlapping the bottom of the mascot like the design
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 230.dp)
                    .clip(DomeShape)
                    .background(SuccessGreen)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(Modifier.height(36.dp))
                FlyingMoney(modifier = Modifier.size(width = 150.dp, height = 140.dp))
                Spacer(Modifier.height(12.dp))
                Text(
                    text = jumlah,
                    color = KastepWhite,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = "PEMBAYARAN BERHASIL",
                    color = KastepWhite,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.ExtraBold,
                    textAlign = TextAlign.Center
                )
                Spacer(Modifier.height(40.dp))

                HorizontalDivider(color = DividerDark, thickness = 1.5.dp)
                DetailRow(label = "Jumlah\nPembayaran", value = jumlah)
                HorizontalDivider(color = DividerDark, thickness = 1.5.dp)
                DetailRow(label = "Atas\nNama", value = atasNama)

                Spacer(Modifier.height(20.dp))

                // Navy gradient "Kembali" button
                Box(
                    modifier = Modifier
                        .width(200.dp)
                        .height(46.dp)
                        .clip(RoundedCornerShape(23.dp))
                        .background(Brush.verticalGradient(listOf(Color(0xFF1A237E), Color(0xFF5B7CFA))))
                        .clickable(onClick = onKembali),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Kembali", color = KastepWhite, fontSize = 20.sp, fontWeight = FontWeight.Medium)
                }
                Spacer(Modifier.height(28.dp))
            }
        }
    }
}

/** Falling banknotes with sparkles (replaces the emoji, matches the design illustration). */
@Composable
private fun FlyingMoney(modifier: Modifier = Modifier) {
    val billFill = Color(0xFFC8E6C0)
    val billInner = Color(0xFFA5D6A7)
    val outline = Color(0xFF1B2A1B)
    val coin = Color(0xFFFFC107)
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        val bw = w * 0.42f
        val bh = h * 0.2f
        val stroke = 2.dp.toPx()

        fun bill(cx: Float, cy: Float, deg: Float) {
            rotate(deg, pivot = Offset(cx, cy)) {
                val tl = Offset(cx - bw / 2, cy - bh / 2)
                val sz = Size(bw, bh)
                val r = CornerRadius(3.dp.toPx())
                drawRoundRect(billFill, tl, sz, r)
                drawRoundRect(
                    billInner, tl + Offset(bw * 0.1f, bh * 0.18f),
                    Size(bw * 0.8f, bh * 0.64f), r
                )
                drawRoundRect(outline, tl, sz, r, style = Stroke(stroke))
                drawOval(coin, Offset(cx - bw * 0.12f, cy - bh * 0.22f), Size(bw * 0.24f, bh * 0.44f))
                drawOval(
                    outline, Offset(cx - bw * 0.12f, cy - bh * 0.22f),
                    Size(bw * 0.24f, bh * 0.44f), style = Stroke(stroke * 0.7f)
                )
            }
        }

        fun sparkle(cx: Float, cy: Float, s: Float) {
            val path = Path().apply {
                moveTo(cx, cy - s); lineTo(cx + s, cy); lineTo(cx, cy + s); lineTo(cx - s, cy); close()
            }
            drawPath(path, coin)
            drawPath(path, outline, style = Stroke(stroke * 0.6f))
        }

        bill(w * 0.38f, h * 0.18f, -20f)
        bill(w * 0.52f, h * 0.30f, -8f)
        bill(w * 0.28f, h * 0.62f, -28f)
        bill(w * 0.72f, h * 0.58f, 22f)
        bill(w * 0.50f, h * 0.84f, 70f)

        sparkle(w * 0.78f, h * 0.05f, 4.dp.toPx())
        sparkle(w * 0.12f, h * 0.32f, 3.5.dp.toPx())
        sparkle(w * 0.90f, h * 0.38f, 4.dp.toPx())
        sparkle(w * 0.22f, h * 0.45f, 3.5.dp.toPx())
        sparkle(w * 0.30f, h * 0.92f, 3.dp.toPx())
    }
}

@Composable
private fun DetailRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 14.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = label, color = KastepWhite, fontSize = 18.sp, lineHeight = 24.sp)
        Spacer(Modifier.width(16.dp))
        Text(
            text = value,
            color = KastepWhite,
            fontSize = 24.sp,
            textAlign = TextAlign.End,
            modifier = Modifier.weight(1f, fill = false)
        )
    }
}

/** Green halftone dot pattern behind the mascot. */
@Composable
private fun DotGrid() {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val step = 22.dp.toPx()
        val cx = size.width / 2f
        val cy = size.height * 0.55f
        val maxDist = size.width * 0.55f
        var y = step / 2
        while (y < size.height) {
            var x = step / 2
            while (x < size.width) {
                val d = Offset(x - cx, y - cy).getDistance()
                if (d < maxDist) {
                    val r = 2.6.dp.toPx() * (1f - d / maxDist * 0.6f)
                    drawCircle(DotGreen, radius = r, center = Offset(x, y))
                }
                x += step
            }
            y += step
        }
    }
}

/** Colourful firework bursts on both sides, like the design. */
@Composable
private fun Fireworks() {
    val palette = listOf(Color(0xFFE53935), Color(0xFFFFC107), Color(0xFF43A047), Color(0xFF1E40AF))
    Canvas(modifier = Modifier.fillMaxSize()) {
        fun burst(center: Offset, startDeg: Float, sweepDeg: Float, count: Int) {
            val inner = 22.dp.toPx()
            val outer = 62.dp.toPx()
            for (i in 0 until count) {
                val a = Math.toRadians((startDeg + sweepDeg * i / (count - 1)).toDouble())
                val dir = Offset(cos(a).toFloat(), sin(a).toFloat())
                drawLine(
                    color = palette[i % palette.size],
                    start = center + dir * inner,
                    end = center + dir * outer,
                    strokeWidth = 7.dp.toPx(),
                    cap = StrokeCap.Round
                )
            }
        }
        // top-right: rays fan out to the right
        burst(Offset(size.width * 0.80f, size.height * 0.30f), -100f, 140f, 6)
        // bottom-left: rays fan out to the left
        burst(Offset(size.width * 0.18f, size.height * 0.62f), 100f, 140f, 6)
    }
}
