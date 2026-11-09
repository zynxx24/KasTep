package com.kastep.app.ui.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
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
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kastep.app.data.KastepRepository
import com.kastep.app.data.KastepViewModel
import com.kastep.app.ui.theme.KastepBlack
import com.kastep.app.ui.theme.KastepWhite
import kotlin.math.cos
import kotlin.math.roundToLong
import kotlin.math.sin

private val SuccessGreen = Color(0xFF12BE64)
private val SuccessGreenDark = Color(0xFF0A8A47)
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
    val context = LocalContext.current
    val userProfile by viewModel.userProfile.collectAsState()
    val records by viewModel.paymentRecords.collectAsState()
    val currentDate = viewModel.getCurrentDateString()

    // Newest record is always inserted at index 0 by the repository.
    val last = records.firstOrNull()

    PembayaranBerhasilContent(
        currentDate = currentDate,
        userName = userProfile.nama,
        jumlah = last?.jumlah ?: 0L,
        atasNama = last?.namaSiswa ?: "-",
        metode = last?.metode.orEmpty(),
        bulan = last?.bulan.orEmpty(),
        onOpenDrawer = onOpenDrawer,
        onNavigateToProfile = onNavigateToProfile,
        onKonfirmasiWa = last?.let { r ->
            {
                val denda = (r.jumlah - KastepRepository.KAS_BULANAN).coerceAtLeast(0L)
                sendWhatsAppConfirmation(context, r.namaSiswa, r.bulan, r.metode, r.jumlah, denda)
            }
        },
        onKembali = onKembali
    )
}

/**
 * Stateless UI so the layout can be previewed / screenshot-tested without a ViewModel.
 * [animate] = false renders the final frame (used for screenshots).
 */
@Composable
internal fun PembayaranBerhasilContent(
    currentDate: String,
    userName: String,
    jumlah: Long,
    atasNama: String,
    metode: String,
    bulan: String,
    onOpenDrawer: () -> Unit = {},
    onNavigateToProfile: () -> Unit = {},
    onKonfirmasiWa: (() -> Unit)? = null,
    onKembali: () -> Unit = {},
    animate: Boolean = true
) {
    // ---- one-shot entry animations ----
    val enter = remember { Animatable(if (animate) 0f else 1f) }
    val counter = remember { Animatable(if (animate) 0f else 1f) }
    LaunchedEffect(animate) {
        if (animate) {
            enter.animateTo(1f, spring(dampingRatio = 0.45f, stiffness = Spring.StiffnessLow))
        }
    }
    LaunchedEffect(animate, jumlah) {
        if (animate) {
            counter.snapTo(0f)
            counter.animateTo(1f, tween(durationMillis = 1100, delayMillis = 250, easing = LinearOutSlowInEasing))
        }
    }

    // ---- looping idle animations ----
    val loop = rememberInfiniteTransition(label = "berhasil")
    val bobAnim by loop.animateFloat(
        -1f, 1f, infiniteRepeatable(tween(1600, easing = FastOutSlowInEasing), RepeatMode.Reverse), label = "bob"
    )
    val burstA by loop.animateFloat(
        0f, 1f, infiniteRepeatable(tween(900, easing = FastOutSlowInEasing), RepeatMode.Reverse), label = "burstA"
    )
    val burstB by loop.animateFloat(
        0f, 1f, infiniteRepeatable(tween(1150, easing = FastOutSlowInEasing), RepeatMode.Reverse), label = "burstB"
    )
    val bob = if (animate) bobAnim else 0f
    val pulseA = if (animate) 0.88f + 0.18f * burstA else 1f
    val pulseB = if (animate) 0.88f + 0.18f * burstB else 1f

    val shownAmount = "Rp " + KastepViewModel.formatRupiah((jumlah * counter.value).roundToLong())
    val finalAmount = "Rp " + KastepViewModel.formatRupiah(jumlah)
    val namaRapi = atasNama.toTitleCase()

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
                Fireworks(scaleRight = pulseA * enter.value, scaleLeft = pulseB * enter.value)
                KasMascot(
                    modifier = Modifier
                        .size(width = 272.dp, height = HeroHeight - 10.dp)
                        .graphicsLayer {
                            val e = enter.value
                            // 1.12 = final size; slightly larger than the canvas so the
                            // mascot fills the hero like the design.
                            scaleX = 1.12f * (0.55f + 0.45f * e)
                            scaleY = 1.12f * (0.55f + 0.45f * e)
                            alpha = e.coerceIn(0f, 1f)
                            transformOrigin = TransformOrigin(0.5f, 1f)
                            translationY = bob * 4.dp.toPx()
                            rotationZ = bob * 1.5f
                        }
                )
            }

            // Green dome section, overlapping the bottom of the mascot like the design
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = DomeTop)
                    .clip(DomeShape)
                    .background(
                        Brush.verticalGradient(listOf(Color(0xFF18C96C), SuccessGreen, SuccessGreen))
                    )
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(Modifier.height(24.dp))
                FlyingMoney(
                    modifier = Modifier
                        .size(width = 110.dp, height = 100.dp)
                        .graphicsLayer {
                            translationY = -bob * 3.dp.toPx()
                            rotationZ = -bob * 2f
                            val e = enter.value.coerceIn(0f, 1f)
                            scaleX = 0.7f + 0.3f * e
                            scaleY = 0.7f + 0.3f * e
                        }
                )
                Spacer(Modifier.height(10.dp))
                Box(contentAlignment = Alignment.Center) {
                    // Reserve the final width so the count-up doesn't make the line jump.
                    Text(finalAmount, color = Color.Transparent, fontSize = 26.sp, fontWeight = FontWeight.Bold)
                    Text(shownAmount, color = KastepWhite, fontSize = 26.sp, fontWeight = FontWeight.Bold)
                }
                Spacer(Modifier.height(2.dp))
                Text(
                    text = "PEMBAYARAN BERHASIL",
                    color = KastepWhite,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 0.5.sp,
                    textAlign = TextAlign.Center
                )
                if (metode.isNotBlank() || bulan.isNotBlank()) {
                    Spacer(Modifier.height(10.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        if (metode.isNotBlank()) InfoChip(metode)
                        if (bulan.isNotBlank()) InfoChip(bulan)
                    }
                }
                Spacer(Modifier.height(18.dp))

                HorizontalDivider(color = DividerDark, thickness = 1.5.dp)
                DetailRow(label = "Jumlah\nPembayaran", value = finalAmount)
                HorizontalDivider(color = DividerDark, thickness = 1.5.dp)
                DetailRow(label = "Atas\nNama", value = namaRapi)
                HorizontalDivider(color = DividerDark, thickness = 1.5.dp)

                Spacer(Modifier.height(18.dp))

                Row(
                    modifier = Modifier.padding(horizontal = 18.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterHorizontally)
                ) {
                    if (onKonfirmasiWa != null) {
                        // White pill: send the WhatsApp confirmation from here so the success
                        // screen is always seen first.
                        Row(
                            modifier = Modifier
                                .width(166.dp)
                                .height(42.dp)
                                .clip(RoundedCornerShape(21.dp))
                                .background(KastepWhite)
                                .clickable(onClick = onKonfirmasiWa),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.AutoMirrored.Filled.Send, contentDescription = null,
                                tint = SuccessGreenDark, modifier = Modifier.size(18.dp)
                            )
                            Spacer(Modifier.width(8.dp))
                            Text("Konfirmasi WA", color = SuccessGreenDark, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                        }
                    }
                    // Navy gradient "Kembali" button (as in the design)
                    Box(
                        modifier = Modifier
                            .width(166.dp)
                            .height(42.dp)
                            .clip(RoundedCornerShape(21.dp))
                            .background(Brush.verticalGradient(listOf(Color(0xFF1A237E), Color(0xFF5B7CFA))))
                            .border(1.dp, Color.White.copy(alpha = 0.25f), RoundedCornerShape(21.dp))
                            .clickable(onClick = onKembali),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Kembali", color = KastepWhite, fontSize = 18.sp, fontWeight = FontWeight.Medium)
                    }
                }
                Spacer(Modifier.height(24.dp))
            }
        }
    }
}

@Composable
private fun InfoChip(text: String) {
    Text(
        text = text,
        color = KastepWhite,
        fontSize = 13.sp,
        fontWeight = FontWeight.Medium,
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(Color.Black.copy(alpha = 0.18f))
            .padding(horizontal = 12.dp, vertical = 4.dp)
    )
}

@Composable
private fun DetailRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 18.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = label, color = KastepWhite, fontSize = 18.sp, lineHeight = 26.sp)
        Spacer(Modifier.width(16.dp))
        Text(
            text = value,
            color = KastepWhite,
            // Real student names are long; shrink them instead of breaking the layout.
            fontSize = if (value.length > 14) 19.sp else 24.sp,
            lineHeight = 24.sp,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.End,
            modifier = Modifier.weight(1f, fill = false)
        )
    }
}

/** "NI KADEK LINA" -> "Ni Kadek Lina" */
private fun String.toTitleCase(): String =
    lowercase().split(" ").filter { it.isNotBlank() }
        .joinToString(" ") { w -> w.replaceFirstChar { it.uppercase() } }
        .ifBlank { this }

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

        // Two notes on top, two in the middle, one falling at the bottom.
        bill(w * 0.44f, h * 0.10f, -12f)
        bill(w * 0.56f, h * 0.30f, -14f)
        bill(w * 0.28f, h * 0.58f, -24f)
        bill(w * 0.72f, h * 0.55f, 22f)
        bill(w * 0.52f, h * 0.84f, 68f)

        sparkle(w * 0.82f, h * 0.02f, 3.dp.toPx())
        sparkle(w * 0.18f, h * 0.24f, 2.6.dp.toPx())
        sparkle(w * 0.92f, h * 0.36f, 3.dp.toPx())
        sparkle(w * 0.08f, h * 0.46f, 2.6.dp.toPx())
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
private fun Fireworks(scaleRight: Float, scaleLeft: Float) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        // top-right burst: petals fan from straight up round to lower-right
        burst(
            center = Offset(size.width * 0.75f, 92.dp.toPx()),
            startDeg = -107f, sweepDeg = 138f, scale = scaleRight,
            colors = listOf(FwRed, FwGreen, FwYellow, FwGreen, FwYellow, FwBlue, FwRed)
        )
        // left burst: petals fan out to the left
        burst(
            center = Offset(size.width * 0.21f, 232.dp.toPx()),
            startDeg = 107f, sweepDeg = 124f, scale = scaleLeft,
            colors = listOf(FwRed, FwBlue, FwYellow, FwGreen, FwYellow, FwRed)
        )
    }
}

private fun DrawScope.burst(center: Offset, startDeg: Float, sweepDeg: Float, scale: Float, colors: List<Color>) {
    if (scale <= 0.01f) return
    val inner = 30.dp.toPx() * scale
    val outer = 78.dp.toPx() * scale
    val tipRadius = 6.dp.toPx() * scale.coerceAtMost(1f)
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
