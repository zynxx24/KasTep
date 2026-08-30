package com.kastep.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kastep.app.data.KastepViewModel
import com.kastep.app.ui.theme.KastepBlack
import com.kastep.app.ui.theme.KastepCyan
import com.kastep.app.ui.theme.KastepWhite

// ─── Color palette matching the schedule image ───
private val ScheduleBg = Color(0xFF1A1A2E)
private val HeaderBg = Color(0xFF2D2D44)
private val BorderColor = Color(0xFF3A3A55)
private val DayLabelBg = Color(0xFF252540)

// Subject colors (matching the timetable image colors)
private val ColorPDLRPL = Color(0xFFB3E5FC)    // Light blue
private val ColorPDLKL = Color(0xFFFF00FF)     // Magenta
private val ColorRPL = Color(0xFF8B0000)       // Dark red / maroon
private val ColorBING = Color(0xFF8BC34A)      // Green
private val ColorMAT = Color(0xFF7986CB)       // Blue-purple
private val ColorKIK = Color(0xFFFFF176)       // Yellow
private val ColorBB = Color(0xFF00BCD4)        // Cyan
private val ColorPP = Color(0xFF1565C0)        // Blue
private val ColorBINDO = Color(0xFFEF9A9A)     // Pink/salmon
private val ColorPABP = Color(0xFF00C853)      // Bright green

// ─── Data classes for schedule ───
private data class ScheduleEntry(
    val subject: String,
    val teacher: String,
    val color: Color,
    val startPeriod: Int,
    val spanPeriods: Int
)

private data class TimeSlot(
    val period: String,
    val senin: String,
    val selasaKamis: String,
    val periodJumat: String,
    val jumat: String
)

// ─── Schedule data per day (matching the image exactly) ───
private val scheduleData = mapOf(
    "Sen" to listOf(
        ScheduleEntry("PDL RPL", "Pak Surya", ColorPDLRPL, 1, 4),
        ScheduleEntry("PDL KL", "Ms. Dayu N", ColorPDLKL, 1, 4),
        ScheduleEntry("RPL", "Pak Rizky", ColorRPL, 5, 2),
        ScheduleEntry("B.ING", "Ms. Diah", ColorBING, 7, 4)
    ),
    "Sel" to listOf(
        ScheduleEntry("RPL", "Pak Rizky", ColorRPL, 1, 4),
        ScheduleEntry("RPL", "Pak Rizky", ColorRPL, 7, 4)
    ),
    "Rab" to listOf(
        ScheduleEntry("RPL", "Pak Rizky", ColorRPL, 1, 4),
        ScheduleEntry("RPL", "Pak Rizky", ColorRPL, 5, 4),
        ScheduleEntry("PABP", "Bu Happy / Bu Sari", ColorPABP, 9, 2)
    ),
    "Kam" to listOf(
        ScheduleEntry("MAT", "Pak Restu", ColorMAT, 1, 4),
        ScheduleEntry("RPL", "Pak Rizky", ColorRPL, 5, 2),
        ScheduleEntry("KIK", "Bu Lulu", ColorKIK, 7, 4)
    ),
    "Jum" to listOf(
        ScheduleEntry("BB", "Bu Sinta", ColorBB, 1, 2),
        ScheduleEntry("PP", "Bu Happy", ColorPP, 3, 2),
        ScheduleEntry("B.INDO", "Mr. Esa", ColorBINDO, 5, 3),
        ScheduleEntry("PABP", "Bu Chika", ColorPABP, 8, 2)
    )
)

// ─── Time allocation data ───
private val timeSlots = listOf(
    TimeSlot("0", "07.10 - 08.00", "07.10 - 07.40", "0", "07.30 – 08.00"),
    TimeSlot("1", "08.00 - 08.40", "07.40 - 08.20", "1", "08.00 – 08.30"),
    TimeSlot("2", "08.40 - 09.20", "08.20 - 09.00", "2", "08.30 – 09.00"),
    TimeSlot("3", "09.20 - 10.00", "09.00 - 09.40", "3", "09.00 – 09.30"),
    TimeSlot("4", "10.00 - 10.40", "09.40 - 10.20", "4", "09.30 – 10.00"),
    TimeSlot("BREAK", "10.40 - 11.10", "10.20 - 10.50", "BREAK", "10.00 – 10.30"),
    TimeSlot("5", "11.10 - 11.50", "10.50 - 11.30", "5", "10.30 – 11.00"),
    TimeSlot("6", "11.50 - 12.30", "11.30 - 12.10", "6", "11.00 – 11.30"),
    TimeSlot("7", "12.30 - 13.10", "12.10 - 12.50", "7", "11.30 – 12.00"),
    TimeSlot("8", "13.10 - 13.50", "12.50 - 13.30", "8", "12.00 – 12.30"),
    TimeSlot("BREAK", "13.50 - 14.20", "13.30 - 14.00", "9", "12.30 – 13.00"),
    TimeSlot("9", "14.20 - 14.50", "14.00 - 14.40", "10", "13.00 – 13.30"),
    TimeSlot("10", "14.50 - 15.20", "14.40 - 15.20", "", "")
)

@Composable
fun MataPelajaranScreen(
    viewModel: KastepViewModel,
    onOpenDrawer: () -> Unit,
    onNavigateToProfile: () -> Unit = {}
) {
    val userProfile by viewModel.userProfile.collectAsState()
    val currentDate = viewModel.getCurrentDateString()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(KastepBlack)
    ) {
        TopBar(currentDate = currentDate, userName = userProfile.nama, onMenuClick = onOpenDrawer, onNavigateToProfile = onNavigateToProfile)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
            // ── Section 1: Class Schedule Table ──
            ScheduleTableSection()

            Spacer(modifier = Modifier.height(20.dp))

            // ── Section 2: Time Allocation Table ──
            TimeAllocationSection()

            Spacer(modifier = Modifier.height(16.dp))

            // Decorative bottom line
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(3.dp)
                    .background(
                        brush = androidx.compose.ui.graphics.Brush.horizontalGradient(
                            colors = listOf(
                                KastepCyan.copy(alpha = 0.6f),
                                KastepCyan,
                                KastepCyan.copy(alpha = 0.6f)
                            )
                        )
                    )
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun ScheduleTableSection() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = ScheduleBg)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            // Title
            Text(
                text = "JADWAL PELAJARAN SMK NEGERI 2 KUTA SELATAN\nSMT GANJIL TA 26/27",
                color = Color(0xFFFFD600),
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                lineHeight = 16.sp,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "XII PPLG",
                color = KastepWhite,
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(12.dp))

            // Scrollable table
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
            ) {
                Column {
                    // Header row: period numbers
                    ScheduleHeaderRow()

                    // Day rows
                    val days = listOf("Sen", "Sel", "Rab", "Kam", "Jum")
                    days.forEach { day ->
                        ScheduleDayRow(day = day, entries = scheduleData[day] ?: emptyList())
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Jadwal hari rabu di dua JP terakhir adalah pembelajaran khusus Agama Islam, Kristen, dan Katolik",
                color = KastepWhite.copy(alpha = 0.6f),
                fontSize = 9.sp,
                lineHeight = 12.sp
            )
        }
    }
}

@Composable
private fun ScheduleHeaderRow() {
    val cellWidth = 36.dp
    val dayLabelWidth = 48.dp
    Row {
        // Empty corner cell for day label
        Box(
            modifier = Modifier
                .width(dayLabelWidth)
                .height(28.dp)
                .background(HeaderBg)
                .border(0.5.dp, BorderColor),
            contentAlignment = Alignment.Center
        ) {
            Text("", color = KastepWhite, fontSize = 10.sp)
        }
        // Period numbers 1-10
        for (i in 1..10) {
            Box(
                modifier = Modifier
                    .width(cellWidth)
                    .height(28.dp)
                    .background(HeaderBg)
                    .border(0.5.dp, BorderColor),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = i.toString(),
                    color = KastepWhite,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun ScheduleDayRow(day: String, entries: List<ScheduleEntry>) {
    val cellWidth = 36.dp
    val dayLabelWidth = 48.dp
    val rowHeight = 60.dp

    // Determine if this day has two rows (Sen has PDL RPL and PDL KL in parallel)
    val isSenin = day == "Sen"
    val actualHeight = if (isSenin) 80.dp else rowHeight

    Row(modifier = Modifier.height(actualHeight)) {
        // Day label
        Box(
            modifier = Modifier
                .width(dayLabelWidth)
                .fillMaxHeight()
                .background(DayLabelBg)
                .border(0.5.dp, BorderColor),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = day,
                color = KastepWhite,
                fontSize = 16.sp,
                fontWeight = FontWeight.ExtraBold
            )
        }

        if (isSenin) {
            // Senin has two rows: PDL RPL (top) + PDL KL (bottom left), RPL (mid), B.ING (right)
            SeninRow(entries, cellWidth, actualHeight)
        } else {
            // Normal single row
            NormalDayRow(entries, cellWidth)
        }
    }
}

@Composable
@Suppress("UNUSED_PARAMETER")
private fun SeninRow(entries: List<ScheduleEntry>, cellWidth: Dp, @Suppress("unused") rowHeight: Dp) {
    // Senin layout from the image:
    // Top half: PDL RPL spans period 1-4, then RPL spans 5-6, then B.ING spans 7-10
    // Bottom half: PDL KL spans period 1-4

    Column(modifier = Modifier.fillMaxHeight()) {
        // Top row
        Row(modifier = Modifier.weight(1f)) {
            // PDL RPL: periods 1-4
            SubjectBlock(
                subject = "PDL RPL",
                teacher = "Pak Surya",
                color = ColorPDLRPL,
                textColor = Color.Black,
                width = cellWidth * 4,
                modifier = Modifier.fillMaxHeight()
            )
            // RPL: periods 5-6
            SubjectBlock(
                subject = "RPL",
                teacher = "Pak Rizky",
                color = ColorRPL,
                textColor = KastepWhite,
                width = cellWidth * 2,
                modifier = Modifier.fillMaxHeight()
            )
            // B.ING: periods 7-10
            SubjectBlock(
                subject = "B.ING",
                teacher = "Ms. Diah",
                color = ColorBING,
                textColor = Color.Black,
                width = cellWidth * 4,
                modifier = Modifier.fillMaxHeight()
            )
        }
        // Bottom row
        Row(modifier = Modifier.weight(1f)) {
            // PDL KL: periods 1-4
            SubjectBlock(
                subject = "PDL KL",
                teacher = "Ms. Dayu N",
                color = ColorPDLKL,
                textColor = KastepWhite,
                width = cellWidth * 4,
                modifier = Modifier.fillMaxHeight()
            )
            // Empty remaining cells
            Box(
                modifier = Modifier
                    .width(cellWidth * 6)
                    .fillMaxHeight()
            )
        }
    }
}

@Composable
private fun NormalDayRow(entries: List<ScheduleEntry>, cellWidth: Dp) {
    // Fill periods 1-10, placing subject blocks where they belong
    val sortedEntries = entries.sortedBy { it.startPeriod }

    Row(modifier = Modifier.fillMaxHeight()) {
        var currentPeriod = 1
        for (entry in sortedEntries) {
            // Fill empty gaps before this entry
            if (entry.startPeriod > currentPeriod) {
                val gapWidth = cellWidth * (entry.startPeriod - currentPeriod)
                Box(
                    modifier = Modifier
                        .width(gapWidth)
                        .fillMaxHeight()
                        .border(0.5.dp, BorderColor.copy(alpha = 0.3f))
                )
            }

            val textColor = when (entry.color) {
                ColorRPL, ColorPP -> KastepWhite
                ColorPDLKL -> KastepWhite
                else -> Color.Black
            }

            SubjectBlock(
                subject = entry.subject,
                teacher = entry.teacher,
                color = entry.color,
                textColor = textColor,
                width = cellWidth * entry.spanPeriods,
                modifier = Modifier.fillMaxHeight()
            )
            currentPeriod = entry.startPeriod + entry.spanPeriods
        }

        // Fill remaining empty cells at the end
        if (currentPeriod <= 10) {
            val remainingWidth = cellWidth * (11 - currentPeriod)
            Box(
                modifier = Modifier
                    .width(remainingWidth)
                    .fillMaxHeight()
                    .border(0.5.dp, BorderColor.copy(alpha = 0.3f))
            )
        }
    }
}

@Composable
private fun SubjectBlock(
    subject: String,
    teacher: String,
    color: Color,
    textColor: Color,
    width: Dp,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .width(width)
            .background(color)
            .border(0.5.dp, BorderColor),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(2.dp)
        ) {
            Text(
                text = subject,
                color = textColor,
                fontSize = 12.sp,
                fontWeight = FontWeight.ExtraBold,
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = teacher,
                color = textColor.copy(alpha = 0.75f),
                fontSize = 8.sp,
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                lineHeight = 10.sp
            )
        }
    }
}

// ─── Time Allocation Section ───
@Composable
private fun TimeAllocationSection() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = ScheduleBg)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = "ALOKASI JAM PELAJARAN SEMESTER GANJIL\nSMK NEGERI 2 KUTA SELATAN TA 2026/2027",
                color = KastepWhite,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                lineHeight = 15.sp,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(12.dp))

            // Scrollable time table
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
            ) {
                Column {
                    // Header
                    TimeTableHeader()
                    // Rows
                    timeSlots.forEachIndexed { index, slot ->
                        val isBreak = slot.period == "BREAK"
                        TimeTableRow(slot = slot, isBreak = isBreak, isEven = index % 2 == 0)
                    }
                }
            }
        }
    }
}

@Composable
private fun TimeTableHeader() {
    val colWidth1 = 56.dp
    val colWidth2 = 96.dp
    val colWidth3 = 96.dp
    val colWidth4 = 56.dp
    val colWidth5 = 96.dp
    val headerColor = Color(0xFF1565C0)

    Row {
        TimeHeaderCell("JAM KE-", colWidth1, headerColor)
        TimeHeaderCell("SENIN", colWidth2, headerColor)
        TimeHeaderCell("SELASA - KAMIS", colWidth3, headerColor)
        TimeHeaderCell("JAM KE-", colWidth4, headerColor)
        TimeHeaderCell("JUMAT", colWidth5, headerColor)
    }
}

@Composable
private fun TimeHeaderCell(text: String, width: Dp, bgColor: Color) {
    Box(
        modifier = Modifier
            .width(width)
            .height(36.dp)
            .background(bgColor)
            .border(0.5.dp, BorderColor),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = KastepWhite,
            fontSize = 9.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            maxLines = 2,
            lineHeight = 11.sp
        )
    }
}

@Composable
private fun TimeTableRow(slot: TimeSlot, isBreak: Boolean, isEven: Boolean) {
    val colWidth1 = 56.dp
    val colWidth2 = 96.dp
    val colWidth3 = 96.dp
    val colWidth4 = 56.dp
    val colWidth5 = 96.dp
    val rowBg = if (isBreak) Color(0xFFFFEB3B).copy(alpha = 0.15f)
    else if (isEven) ScheduleBg else ScheduleBg.copy(alpha = 0.8f)
    val textColor = if (isBreak) Color(0xFFFFEB3B) else KastepWhite

    Row {
        TimeDataCell(slot.period, colWidth1, rowBg, textColor, isBreak)
        TimeDataCell(slot.senin, colWidth2, rowBg, textColor, isBreak)
        TimeDataCell(slot.selasaKamis, colWidth3, rowBg, textColor, isBreak)
        TimeDataCell(slot.periodJumat, colWidth4, rowBg, textColor, isBreak)
        TimeDataCell(slot.jumat, colWidth5, rowBg, textColor, isBreak)
    }
}

@Composable
private fun TimeDataCell(text: String, width: Dp, bgColor: Color, textColor: Color, isBold: Boolean) {
    Box(
        modifier = Modifier
            .width(width)
            .height(28.dp)
            .background(bgColor)
            .border(0.5.dp, BorderColor),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = textColor,
            fontSize = 9.sp,
            fontWeight = if (isBold) FontWeight.Bold else FontWeight.Normal,
            textAlign = TextAlign.Center,
            maxLines = 1
        )
    }
}
