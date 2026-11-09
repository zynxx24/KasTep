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
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawscope.DrawScope
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

private val FwRed = Color(0xFFE53935)
private val FwYellow = Color(0xFFFFC107)
private val FwGreen = Color(0xFF43A047)
private val FwBlue = Color(0xFF1E40AF)

/** Height of the black hero area (dots, fireworks, mascot). */
private val HeroHeight = 320.dp
/** Where the green dome starts; the dome overlaps the bottom of the mascot. */
private val DomeTop = 292.dp
/** How much the dome edges sit below its peak. */
private val DomeRise = 45.dp

/** Green section with a convex dome on top, like the design. */
private object DomeShape : Shape {
    override fun createOutline(size: Size, layoutDirection: LayoutDirection, density: Density): Outline {
        val rise = with(density) { DomeRise.toPx() }
        val path = Path().apply {
            moveTo(0f, rise)
            quadraticBezierTo(size.width / 2f, -rise, size.width, rise)
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

    PembayaranBerhasilContent(
        currentDate = currentDate,
        userName = userProfile.nama,
        jumlah = jumlah,
        atasNama = atasNama,
        onOpenDrawer = onOpenDrawer,
        onNavigateToProfile = onNavigateToProfile,
        onKembali = onKembali
    )
}

/** Stateless UI so the layout can be previewed / screenshot-tested without a ViewModel. */
@Composable
internal fun PembayaranBerhasilContent(
    currentDate: String,
    userName: String,
    jumlah: String,
    atasNama: String,
    onOpenDrawer: () -> Unit = {},
    onNavigateToProfile: () -> Unit = {},
    onKembali: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(KastepBlack)
    ) {
        TopBar(currentDate = currentDate, userName = userName, onMenuClick = onOpenDrawer, onNavigateToProfile = onNavigateToProfile)

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            // Hero: dot grid + fireworks + mascot
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(HeroHeight),
                contentAlignment = Alignment.BottomCenter
            ) {
                DotGrid()
                Fireworks()
                Image(
                    painter = painterResource(id = R.drawable.ic_spongebob),
                    contentDescription = "Maskot pembayaran berhasil",
                    modifier = Modifier.height(HeroHeight - 10.dp),
                    contentScale = ContentScale.FillHeight
                )
            }

            // Green dome section, overlapping the bottom of the mascot like the design
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = DomeTop)
                    .clip(DomeShape)
                    .background(SuccessGreen)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(Modifier.height(34.dp))
                FlyingMoney(modifier = Modifier.size(width = 122.dp, height = 112.dp))
                Spacer(Modifier.height(10.dp))
                Text(
                    text = jumlah,
                    color = KastepWhite,
                    fontSize = 26.sp,
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
                Spacer(Modifier.height(36.dp))

                HorizontalDivider(color = DividerDark, thickness = 1.5.dp)
                DetailRow(label = "Jumlah\nPembayaran", value = jumlah)
                HorizontalDivider(color = DividerDark, thickness = 1.5.dp)
                DetailRow(label = "Atas\nNama", value = atasNama)

                Spacer(Modifier.height(18.dp))

                // Navy gradient "Kembali" button
                Box(
                    modifier = Modifier
                        .width(182.dp)
                        .height(40.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(Brush.verticalGradient(listOf(Color(0xFF1A237E), Color(0xFF5B7CFA))))
                        .clickable(onClick = onKembali),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Kembali", color = KastepWhite, fontSize = 19.sp, fontWeight = FontWeight.Normal)
                }
                Spacer(Modifier.height(24.dp))
            }
        }
    }
}

@Composable
private fun DetailRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 18.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = label, color = KastepWhite, fontSize = 18.sp, lineHeight = 26.sp)
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

/** Falling banknotes with sparkles (matches the design illustration). */
@Composable
private fun FlyingMoney(modifier: Modifier = Modifier) {
    val billFill = Color(0xFFC8E6C0)
    val billInner = Color(0xFFA5D6A7)
    val outline = Color(0xFF1B2A1B)
    val coin = Color(0xFFFFC107)
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        val bw = w * 0.40f
        val bh = h * 0.17f
        val stroke = 1.6.dp.toPx()

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

        // Layout follows the design: two notes on top, two in the middle, one falling at the bottom.
        bill(w * 0.44f, h * 0.10f, -12f)
        bill(w * 0.56f, h * 0.30f, -14f)
        bill(w * 0.30f, h * 0.58f, -24f)
        bill(w * 0.70f, h * 0.55f, 22f)
        bill(w * 0.52f, h * 0.84f, 68f)

        sparkle(w * 0.82f, h * 0.02f, 3.dp.toPx())
        sparkle(w * 0.18f, h * 0.24f, 2.6.dp.toPx())
        sparkle(w * 0.90f, h * 0.36f, 3.dp.toPx())
        sparkle(w * 0.12f, h * 0.46f, 2.6.dp.toPx())
        sparkle(w * 0.40f, h * 0.96f, 2.4.dp.toPx())
    }
}

/** Green halftone dot pattern behind the mascot (round cluster, uniform dots). */
@Composable
private fun DotGrid() {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val step = 26.dp.toPx()
        val radius = 2.4.dp.toPx()
        val cx = size.width / 2f
        val cy = size.height * 0.47f
        val maxDist = size.width * 0.52f
        // Center the grid horizontally so the pattern is symmetric.
        val startX = cx - (cx / step).toInt() * step
        var y = step * 0.6f
        while (y < size.height) {
            var x = startX
            while (x < size.width) {
                if (Offset(x - cx, y - cy).getDistance() < maxDist) {
                    drawCircle(DotGreen, radius = radius, center = Offset(x, y))
                }
                x += step
            }
            y += step
        }
    }
}

/** Colourful teardrop "petal" firework bursts on both sides, like the design. */
@Composable
private fun Fireworks() {
    Canvas(modifier = Modifier.fillMaxSize()) {
        // top-right burst: petals fan from straight up round to lower-right
        burst(
            center = Offset(size.width * 0.75f, 92.dp.toPx()),
            startDeg = -107f, sweepDeg = 138f,
            colors = listOf(FwRed, FwGreen, FwYellow, FwGreen, FwYellow, FwBlue, FwRed)
        )
        // left burst: petals fan out to the left
        burst(
            center = Offset(size.width * 0.21f, 232.dp.toPx()),
            startDeg = 107f, sweepDeg = 124f,
            colors = listOf(FwRed, FwBlue, FwYellow, FwGreen, FwYellow, FwRed)
        )
    }
}

private fun DrawScope.burst(center: Offset, startDeg: Float, sweepDeg: Float, colors: List<Color>) {
    val inner = 30.dp.toPx()
    val outer = 78.dp.toPx()
    val tipRadius = 6.dp.toPx()
    colors.forEachIndexed { i, color ->
        val deg = startDeg + sweepDeg * i / (colors.size - 1)
        // Alternate petal length slightly so the burst doesn't look mechanical.
        val len = if (i % 2 == 0) outer else outer * 0.86f
        drawPetal(center, deg, inner, len, tipRadius, color)
    }
}

/** A teardrop: pointed at the inner end, rounded at the outer end. */
private fun DrawScope.drawPetal(
    center: Offset, deg: Float, inner: Float, outer: Float, r: Float, color: Color
) {
    fun dir(d: Float): Offset {
        val a = Math.toRadians(d.toDouble())
        return Offset(cos(a).toFloat(), sin(a).toFloat())
    }
    val tip = center + dir(deg) * inner
    val roundCenter = center + dir(deg) * (outer - r)
    val path = Path().apply {
        moveTo(tip.x, tip.y)
        val side = roundCenter + dir(deg - 90f) * r
        lineTo(side.x, side.y)
        arcTo(
            rect = Rect(roundCenter, r),
            startAngleDegrees = deg - 90f,
            sweepAngleDegrees = 180f,
            forceMoveTo = false
        )
        close()
    }
    drawPath(path, color)
}
