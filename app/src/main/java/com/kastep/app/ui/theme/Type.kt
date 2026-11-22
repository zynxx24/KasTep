// » package com.kastep.app.ui.theme = alamat folder file ini.
package com.kastep.app.ui.theme

// » import Typography = daftar gaya huruf Material 3.
import androidx.compose.material3.Typography
// » import TextStyle = satu gaya huruf (tebal, ukuran, jarak huruf).
import androidx.compose.ui.text.TextStyle
// » import FontWeight = ketebalan huruf.
import androidx.compose.ui.text.font.FontWeight
// » import sp = satuan ukuran huruf Android.
import androidx.compose.ui.unit.sp

// » val Typography = daftar gaya huruf KASTEP. Layar memakai nama seperti titleLarge, bodySmall.
val Typography = Typography(
    // » displayLarge = gaya huruf paling besar (judul sangat besar).
    displayLarge = TextStyle(
        // » Tebal (Bold).
        fontWeight = FontWeight.Bold,
        // » Ukuran 32sp.
        fontSize = 32.sp,
        // » Jarak antar huruf 1.5sp (agak renggang).
        letterSpacing = 1.5.sp
    ),
    // » headlineLarge = judul besar.
    headlineLarge = TextStyle(
        // » Tebal.
        fontWeight = FontWeight.Bold,
        // » Ukuran 28sp.
        fontSize = 28.sp,
        // » Jarak antar huruf normal (0).
        letterSpacing = 0.sp
    ),
    // » headlineMedium = judul sedang.
    headlineMedium = TextStyle(
        // » Tebal.
        fontWeight = FontWeight.Bold,
        // » Ukuran 24sp.
        fontSize = 24.sp,
        // » Jarak normal.
        letterSpacing = 0.sp
    ),
    // » titleLarge = judul layar/kartu.
    titleLarge = TextStyle(
        // » Semi tebal.
        fontWeight = FontWeight.SemiBold,
        // » Ukuran 20sp.
        fontSize = 20.sp,
        // » Jarak normal.
        letterSpacing = 0.sp
    ),
    // » titleMedium = sub-judul.
    titleMedium = TextStyle(
        // » Semi tebal.
        fontWeight = FontWeight.SemiBold,
        // » Ukuran 16sp.
        fontSize = 16.sp,
        // » Jarak antar huruf 0.15sp.
        letterSpacing = 0.15.sp
    ),
    // » bodyLarge = teks isi besar.
    bodyLarge = TextStyle(
        // » Normal.
        fontWeight = FontWeight.Normal,
        // » Ukuran 16sp.
        fontSize = 16.sp,
        // » Jarak 0.5sp.
        letterSpacing = 0.5.sp
    ),
    // » bodyMedium = teks isi standar.
    bodyMedium = TextStyle(
        // » Normal.
        fontWeight = FontWeight.Normal,
        // » Ukuran 14sp.
        fontSize = 14.sp,
        // » Jarak 0.25sp.
        letterSpacing = 0.25.sp
    ),
    // » bodySmall = teks keterangan kecil.
    bodySmall = TextStyle(
        // » Normal.
        fontWeight = FontWeight.Normal,
        // » Ukuran 12sp.
        fontSize = 12.sp,
        // » Jarak 0.4sp.
        letterSpacing = 0.4.sp
    ),
    // » labelLarge = teks tombol dan label.
    labelLarge = TextStyle(
        // » Tebal.
        fontWeight = FontWeight.Bold,
        // » Ukuran 18sp.
        fontSize = 18.sp,
        // » Jarak normal.
        letterSpacing = 0.sp
    )
)
