package com.kastep.app.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.graphics.drawscope.rotate
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

/**
 * "Si Kas" — KASTEP's own mascot: a gold coin in a green fedora and pinstripe suit,
 * giving a thumbs-up and holding a fan of banknotes. Drawn entirely in code (no
 * third-party character art), on a 100 x 110 unit grid scaled to the canvas.
 */
@Composable
internal fun KasMascot(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) { drawKasMascot() }
}

private val Outline = Color(0xFF2B1D14)
private val SuitDark = Color(0xFF1B5E20)
private val SuitMid = Color(0xFF2E7D32)
private val SuitLight = Color(0xFF66BB6A)
private val Glove = Color(0xFFFAFAFA)

private fun DrawScope.drawKasMascot() {
    val s = min(size.width / 100f, size.height / 110f)
    val ox = (size.width - 100f * s) / 2f
    val oy = (size.height - 110f * s) / 2f
    fun p(x: Float, y: Float) = Offset(ox + x * s, oy + y * s)
    fun u(v: Float) = v * s
    fun sz(w: Float, h: Float) = Size(w * s, h * s)
    val line = u(0.9f)

    // ---------- Torso: pinstripe suit, white shirt, green tie ----------
    val torso = Path().apply {
        moveTo(p(20f, 112f).x, p(20f, 112f).y)
        lineTo(p(27f, 92f).x, p(27f, 92f).y)
        quadraticBezierTo(p(50f, 84f).x, p(50f, 84f).y, p(73f, 92f).x, p(73f, 92f).y)
        lineTo(p(80f, 112f).x, p(80f, 112f).y)
        close()
    }
    drawPath(torso, Brush.verticalGradient(listOf(SuitMid, SuitDark), p(0f, 86f).y, p(0f, 112f).y))
    clipPath(torso) {
        var x = 18f
        while (x < 84f) {
            drawLine(SuitLight.copy(alpha = 0.55f), p(x, 84f), p(x + 3f, 112f), strokeWidth = u(0.5f))
            x += 3.5f
        }
    }
    val shirt = Path().apply {
        moveTo(p(42f, 87f).x, p(42f, 87f).y)
        lineTo(p(58f, 87f).x, p(58f, 87f).y)
        lineTo(p(50f, 106f).x, p(50f, 106f).y)
        close()
    }
    drawPath(shirt, Color.White)
    val tie = Path().apply {
        moveTo(p(48.2f, 91f).x, p(48.2f, 91f).y)
        lineTo(p(51.8f, 91f).x, p(51.8f, 91f).y)
        lineTo(p(53.4f, 103f).x, p(53.4f, 103f).y)
        lineTo(p(50f, 106.5f).x, p(50f, 106.5f).y)
        lineTo(p(46.6f, 103f).x, p(46.6f, 103f).y)
        close()
    }
    drawPath(tie, Color(0xFF43A047))
    drawRoundRect(Color(0xFF2E7D32), p(47.4f, 87.6f), sz(5.2f, 3.8f), CornerRadius(u(1.2f)))
    // lapels
    drawLine(SuitDark, p(42f, 87f), p(46f, 99f), strokeWidth = u(1.6f), cap = StrokeCap.Round)
    drawLine(SuitDark, p(58f, 87f), p(54f, 99f), strokeWidth = u(1.6f), cap = StrokeCap.Round)

    // ---------- Coin head ----------
    val c = p(50f, 60f)
    drawCircle(Color(0xFFD08A00), radius = u(30f), center = c)
    // reeded edge
    for (i in 0 until 72) {
        val a = Math.toRadians(i * 5.0)
        val d = Offset(cos(a).toFloat(), sin(a).toFloat())
        drawLine(Color(0xFFB57200), c + d * u(27.6f), c + d * u(29.6f), strokeWidth = u(0.55f))
    }
    drawCircle(
        Brush.radialGradient(
            listOf(Color(0xFFFFF4B8), Color(0xFFFFD54F), Color(0xFFFFB300)),
            center = p(42f, 49f), radius = u(36f)
        ),
        radius = u(27f), center = c
    )
    drawCircle(Color(0xFFFFE9A0), radius = u(24.6f), center = c, style = Stroke(u(0.7f)))
    // shine
    drawArc(
        Color.White.copy(alpha = 0.55f), startAngle = 200f, sweepAngle = 55f, useCenter = false,
        topLeft = p(29f, 39f), size = sz(42f, 42f), style = Stroke(u(2.2f), cap = StrokeCap.Round)
    )

    // cheeks
    drawCircle(Color(0xFFFF7043).copy(alpha = 0.35f), radius = u(3.2f), center = p(33.5f, 67f))
    drawCircle(Color(0xFFFF7043).copy(alpha = 0.35f), radius = u(3.2f), center = p(66.5f, 65.5f))

    // eyebrows (one raised: confident smirk)
    drawLine(Outline, p(35.5f, 48.6f), p(46.5f, 50.6f), strokeWidth = u(2.1f), cap = StrokeCap.Round)
    drawLine(Outline, p(53.5f, 49.4f), p(64.5f, 45.4f), strokeWidth = u(2.1f), cap = StrokeCap.Round)

    // sunglasses
    drawLine(Outline, p(34f, 54.5f), p(24.5f, 52.5f), strokeWidth = u(1.3f), cap = StrokeCap.Round)
    drawLine(Outline, p(66f, 54.5f), p(75.5f, 52.5f), strokeWidth = u(1.3f), cap = StrokeCap.Round)
    drawLine(Outline, p(47.5f, 55f), p(52.5f, 55f), strokeWidth = u(1.6f), cap = StrokeCap.Round)
    for (x0 in listOf(34f, 52f)) {
        drawRoundRect(
            Brush.verticalGradient(listOf(Color(0xFF263238), Color(0xFF05080A)), p(0f, 52f).y, p(0f, 61.5f).y),
            topLeft = p(x0, 52f), size = sz(14f, 9.5f), cornerRadius = CornerRadius(u(3.2f))
        )
        drawLine(
            Color.White.copy(alpha = 0.75f), p(x0 + 2.6f, 55.6f), p(x0 + 5.6f, 53.4f),
            strokeWidth = u(1.1f), cap = StrokeCap.Round
        )
        drawLine(
            Color.White.copy(alpha = 0.4f), p(x0 + 4.6f, 57.2f), p(x0 + 7f, 55.4f),
            strokeWidth = u(0.8f), cap = StrokeCap.Round
        )
    }

    // grin with a gold tooth
    val mouth = Path().apply {
        moveTo(p(39.5f, 67.5f).x, p(39.5f, 67.5f).y)
        cubicTo(p(45f, 79f).x, p(45f, 79f).y, p(57f, 78f).x, p(57f, 78f).y, p(62.5f, 64.5f).x, p(62.5f, 64.5f).y)
        quadraticBezierTo(p(51f, 70.5f).x, p(51f, 70.5f).y, p(39.5f, 67.5f).x, p(39.5f, 67.5f).y)
        close()
    }
    drawPath(mouth, Color(0xFF4E2A1E))
    clipPath(mouth) {
        drawRect(Color.White, p(36f, 62f), sz(30f, 9.6f))
        drawRect(Color(0xFFFFC107), p(52.6f, 62f), sz(3.6f, 9.6f))
        for (x in listOf(44f, 48.4f, 52.6f, 56.2f, 59.6f)) {
            drawLine(Color(0xFFBDBDBD), p(x, 66f), p(x, 71.4f), strokeWidth = u(0.45f))
        }
        drawCircle(Color(0xFFE57373), radius = u(4.4f), center = p(51f, 78f))
    }
    drawPath(mouth, Outline, style = Stroke(u(1.1f)))
    // smirk dimple
    drawLine(Outline, p(62.5f, 64.5f), p(64.6f, 63.2f), strokeWidth = u(1.1f), cap = StrokeCap.Round)

    // ---------- Fedora (tilted) ----------
    rotate(-7f, pivot = p(50f, 36f)) {
        val crown = Path().apply {
            moveTo(p(31f, 35f).x, p(31f, 35f).y)
            cubicTo(p(29.5f, 24f).x, p(29.5f, 24f).y, p(33f, 12.5f).x, p(33f, 12.5f).y, p(41f, 10.5f).x, p(41f, 10.5f).y)
            quadraticBezierTo(p(50f, 15.5f).x, p(50f, 15.5f).y, p(59f, 10.5f).x, p(59f, 10.5f).y)
            cubicTo(p(67f, 12.5f).x, p(67f, 12.5f).y, p(70.5f, 24f).x, p(70.5f, 24f).y, p(69f, 35f).x, p(69f, 35f).y)
            close()
        }
        drawPath(crown, Brush.verticalGradient(listOf(Color(0xFF43A047), SuitDark), p(0f, 10f).y, p(0f, 35f).y))
        // pinch crease
        drawLine(Color(0xFF14461A), p(50f, 15.5f), p(50f, 23.5f), strokeWidth = u(1f), cap = StrokeCap.Round)
        drawLine(Color.White.copy(alpha = 0.25f), p(36f, 16f), p(34.5f, 28f), strokeWidth = u(1.6f), cap = StrokeCap.Round)
        // band: gold with darker flecks
        drawRect(Color(0xFFB8962E), p(30.6f, 27f), sz(38.8f, 5.4f))
        var x = 32f
        while (x < 69f) {
            drawLine(Color(0xFF7A5F14), p(x, 27.4f), p(x + 1.6f, 32f), strokeWidth = u(0.6f))
            x += 2.6f
        }
        // brim
        drawOval(SuitDark, topLeft = p(11f, 31f), size = sz(78f, 12.5f))
        drawArc(
            Color(0xFF4CAF50).copy(alpha = 0.8f), startAngle = 190f, sweepAngle = 160f, useCenter = false,
            topLeft = p(12.5f, 31.8f), size = sz(75f, 10.5f), style = Stroke(u(0.9f))
        )
        drawOval(Color(0xFF0E3A12), topLeft = p(11f, 31f), size = sz(78f, 12.5f), style = Stroke(u(0.7f)))
    }

    // ---------- Right side: arm + fan of banknotes ----------
    drawLine(SuitDark, p(70f, 95f), p(80.5f, 83f), strokeWidth = u(8.5f), cap = StrokeCap.Round)
    val pivot = p(80.5f, 80.5f)
    for (angle in listOf(-104f, -84f, -64f, -44f, -24f)) {
        rotate(angle, pivot = pivot) {
            val tl = Offset(pivot.x + u(2.5f), pivot.y - u(5.5f))
            drawRoundRect(Color(0xFFC8E6C9), tl, sz(25f, 11f), CornerRadius(u(1.2f)))
            drawRoundRect(
                Color(0xFF66BB6A), tl + Offset(u(1.6f), u(1.6f)), sz(21.8f, 7.8f),
                CornerRadius(u(0.8f)), style = Stroke(u(0.6f))
            )
            drawCircle(Color(0xFF81C784), radius = u(2.6f), center = tl + Offset(u(14f), u(5.5f)))
            drawCircle(Color(0xFF388E3C), radius = u(2.6f), center = tl + Offset(u(14f), u(5.5f)), style = Stroke(u(0.5f)))
            drawRoundRect(Color(0xFF2E7D32), tl, sz(25f, 11f), CornerRadius(u(1.2f)), style = Stroke(u(0.7f)))
        }
    }
    drawCircle(Color.White, radius = u(4.4f), center = p(79.5f, 85f)) // cuff
    drawRoundRect(Glove, p(74.5f, 74.5f), sz(13f, 12.5f), CornerRadius(u(4f)))
    drawRoundRect(Outline, p(74.5f, 74.5f), sz(13f, 12.5f), CornerRadius(u(4f)), style = Stroke(line))
    for (y in listOf(77.6f, 80.6f, 83.6f)) {
        drawLine(Outline, p(74.6f, y), p(79f, y), strokeWidth = u(0.7f), cap = StrokeCap.Round)
    }

    // ---------- Left side: thumbs-up with a diamond ring ----------
    drawLine(SuitDark, p(30f, 95f), p(19f, 81f), strokeWidth = u(8.5f), cap = StrokeCap.Round)
    drawCircle(Color.White, radius = u(4.4f), center = p(19.5f, 79.5f)) // cuff
    // thumb
    drawRoundRect(Glove, p(13.2f, 52.5f), sz(5.8f, 14.5f), CornerRadius(u(2.9f)))
    drawRoundRect(Outline, p(13.2f, 52.5f), sz(5.8f, 14.5f), CornerRadius(u(2.9f)), style = Stroke(line))
    // fist
    drawRoundRect(Glove, p(11f, 64f), sz(15f, 14f), CornerRadius(u(4.2f)))
    drawRoundRect(Outline, p(11f, 64f), sz(15f, 14f), CornerRadius(u(4.2f)), style = Stroke(line))
    for (y in listOf(67.6f, 71f, 74.4f)) {
        drawLine(Outline, p(19.5f, y), p(26f, y), strokeWidth = u(0.7f), cap = StrokeCap.Round)
    }
    // ring: gold band + diamond
    drawLine(Color(0xFFFFC107), p(22.6f, 69.3f), p(22.6f, 72.7f), strokeWidth = u(1.4f))
    val gem = Path().apply {
        moveTo(p(25.6f, 68.2f).x, p(25.6f, 68.2f).y)
        lineTo(p(28.6f, 71f).x, p(28.6f, 71f).y)
        lineTo(p(25.6f, 73.8f).x, p(25.6f, 73.8f).y)
        lineTo(p(22.6f, 71f).x, p(22.6f, 71f).y)
        close()
    }
    drawPath(gem, Brush.linearGradient(listOf(Color.White, Color(0xFF80DEEA)), p(23f, 68f), p(28f, 74f)))
    drawPath(gem, Color(0xFF26C6DA), style = Stroke(u(0.5f)))
    drawLine(Color.White, p(27.6f, 66.4f), p(29f, 65f), strokeWidth = u(0.6f), cap = StrokeCap.Round)
    drawLine(Color.White, p(29.2f, 68f), p(30.8f, 67.6f), strokeWidth = u(0.6f), cap = StrokeCap.Round)
}

