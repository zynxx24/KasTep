// ==========================================================================
// PETA FILE · KASTEP (Kelompok 1) · LoginScreen.kt
// Isi file : Halaman Login dan logo header.
// Penanda  : CARI-LOGIN
// Cara cari: VS Code Ctrl+Shift+F (Android Studio Ctrl+Shift+F) lalu ketik
//            "CARI-" untuk semua penanda, atau salah satu nama di baris Penanda.
// ==========================================================================
//  BACA INI DULU
//  Layar login: logo KASTEP, kolom Email, kolom Password (ikon mata), pesan error merah, tombol Masuk, tautan Daftar.
//  Tombol Masuk memanggil viewModel.login(email, password). Hasil null = berhasil (onLoginSuccess). Teks = pesan error dari KastepRepository.login.
//  Akun bendahara bawaan: admin@gmail.com / admin123.
// » Alamat folder (package) file ini di dalam proyek.
package com.kastep.app.ui.screens

// » Canvas = kanvas kosong untuk menggambar sendiri (garis, lingkaran, grafik).
import androidx.compose.foundation.Canvas
// » background = mengisi warna/gradien latar sebuah tampilan.
import androidx.compose.foundation.background
// » clickable = membuat tampilan bisa diketuk.
import androidx.compose.foundation.clickable
// » Memanggil Arrangement (dari androidx.compose.foundation.layout) supaya bisa dipakai di file ini.
import androidx.compose.foundation.layout.Arrangement
// » Memanggil Box (dari androidx.compose.foundation.layout) supaya bisa dipakai di file ini.
import androidx.compose.foundation.layout.Box
// » Memanggil Column (dari androidx.compose.foundation.layout) supaya bisa dipakai di file ini.
import androidx.compose.foundation.layout.Column
// » Memanggil Row (dari androidx.compose.foundation.layout) supaya bisa dipakai di file ini.
import androidx.compose.foundation.layout.Row
// » Memanggil Spacer (dari androidx.compose.foundation.layout) supaya bisa dipakai di file ini.
import androidx.compose.foundation.layout.Spacer
// » Memanggil fillMaxSize (dari androidx.compose.foundation.layout) supaya bisa dipakai di file ini.
import androidx.compose.foundation.layout.fillMaxSize
// » Memanggil fillMaxWidth (dari androidx.compose.foundation.layout) supaya bisa dipakai di file ini.
import androidx.compose.foundation.layout.fillMaxWidth
// » Memanggil height (dari androidx.compose.foundation.layout) supaya bisa dipakai di file ini.
import androidx.compose.foundation.layout.height
// » Memanggil offset (dari androidx.compose.foundation.layout) supaya bisa dipakai di file ini.
import androidx.compose.foundation.layout.offset
// » Memanggil padding (dari androidx.compose.foundation.layout) supaya bisa dipakai di file ini.
import androidx.compose.foundation.layout.padding
// » Memanggil size (dari androidx.compose.foundation.layout) supaya bisa dipakai di file ini.
import androidx.compose.foundation.layout.size
// » Memanggil width (dari androidx.compose.foundation.layout) supaya bisa dipakai di file ini.
import androidx.compose.foundation.layout.width
// » rememberScrollState = menyimpan posisi gulir.
import androidx.compose.foundation.rememberScrollState
// » RoundedCornerShape = bentuk persegi dengan sudut membulat.
import androidx.compose.foundation.shape.RoundedCornerShape
// » verticalScroll = membuat isi bisa digulir naik-turun.
import androidx.compose.foundation.verticalScroll
// » Memanggil Button (dari androidx.compose.material3) supaya bisa dipakai di file ini.
import androidx.compose.material3.Button
// » Memanggil ButtonDefaults (dari androidx.compose.material3) supaya bisa dipakai di file ini.
import androidx.compose.material3.ButtonDefaults
// » Memanggil OutlinedTextField (dari androidx.compose.material3) supaya bisa dipakai di file ini.
import androidx.compose.material3.OutlinedTextField
// » Memanggil OutlinedTextFieldDefaults (dari androidx.compose.material3) supaya bisa dipakai di file ini.
import androidx.compose.material3.OutlinedTextFieldDefaults
// » Memanggil Text (dari androidx.compose.material3) supaya bisa dipakai di file ini.
import androidx.compose.material3.Text
// » @Composable = penanda bahwa sebuah fungsi menggambar tampilan.
import androidx.compose.runtime.Composable
// » Memanggil getValue (dari androidx.compose.runtime) supaya bisa dipakai di file ini.
import androidx.compose.runtime.getValue
// » Memanggil mutableStateOf (dari androidx.compose.runtime) supaya bisa dipakai di file ini.
import androidx.compose.runtime.mutableStateOf
// » Memanggil remember (dari androidx.compose.runtime) supaya bisa dipakai di file ini.
import androidx.compose.runtime.remember
// » Memanggil setValue (dari androidx.compose.runtime) supaya bisa dipakai di file ini.
import androidx.compose.runtime.setValue
// » Alignment = pengatur posisi isi (tengah, atas, bawah, kiri, kanan).
import androidx.compose.ui.Alignment
// » Modifier = daftar 'dekorasi' sebuah tampilan: ukuran, jarak, warna latar, klik, dll.
import androidx.compose.ui.Modifier
// » Memanggil CornerRadius (dari androidx.compose.ui.geometry) supaya bisa dipakai di file ini.
import androidx.compose.ui.geometry.CornerRadius
// » Offset = titik koordinat (x, y) di Canvas.
import androidx.compose.ui.geometry.Offset
// » Memanggil Size (dari androidx.compose.ui.geometry) supaya bisa dipakai di file ini.
import androidx.compose.ui.geometry.Size
// » Brush = 'cat' warna, termasuk gradien (warna berubah halus).
import androidx.compose.ui.graphics.Brush
// » Color = tipe warna Compose; dipakai untuk menulis warna seperti Color.White atau Color(0xFF...).
import androidx.compose.ui.graphics.Color
// » Stroke = gaya menggambar garis tepi (tebal garis, ujung).
import androidx.compose.ui.graphics.drawscope.Stroke
// » FontWeight = ketebalan huruf (Normal, Medium, Bold, ExtraBold).
import androidx.compose.ui.text.font.FontWeight
// » PasswordVisualTransformation = menyamarkan teks menjadi titik-titik.
import androidx.compose.ui.text.input.PasswordVisualTransformation
// » dp = satuan ukuran Android yang sama di semua layar (mis. 16.dp).
import androidx.compose.ui.unit.dp
// » sp = satuan ukuran huruf Android (mengikuti pengaturan ukuran font di HP).
import androidx.compose.ui.unit.sp
// » Memanggil KastepViewModel (dari com.kastep.app.data) supaya bisa dipakai di file ini.
import com.kastep.app.data.KastepViewModel
// » Memanggil CircleBlue (dari com.kastep.app.ui.theme) supaya bisa dipakai di file ini.
import com.kastep.app.ui.theme.CircleBlue
// » Memanggil CirclePurple (dari com.kastep.app.ui.theme) supaya bisa dipakai di file ini.
import com.kastep.app.ui.theme.CirclePurple
// » Memanggil GradientBlueEnd (dari com.kastep.app.ui.theme) supaya bisa dipakai di file ini.
import com.kastep.app.ui.theme.GradientBlueEnd
// » Memanggil GradientBlueStart (dari com.kastep.app.ui.theme) supaya bisa dipakai di file ini.
import com.kastep.app.ui.theme.GradientBlueStart
// » Memanggil KastepBlack (dari com.kastep.app.ui.theme) supaya bisa dipakai di file ini.
import com.kastep.app.ui.theme.KastepBlack
// » Memanggil KastepCyan (dari com.kastep.app.ui.theme) supaya bisa dipakai di file ini.
import com.kastep.app.ui.theme.KastepCyan
// » Memanggil KastepGray (dari com.kastep.app.ui.theme) supaya bisa dipakai di file ini.
import com.kastep.app.ui.theme.KastepGray
// » Memanggil KastepRed (dari com.kastep.app.ui.theme) supaya bisa dipakai di file ini.
import com.kastep.app.ui.theme.KastepRed
// » Memanggil KastepWhite (dari com.kastep.app.ui.theme) supaya bisa dipakai di file ini.
import com.kastep.app.ui.theme.KastepWhite
// » KeyboardOptions = pengaturan jenis keyboard (angka, email, dll).
import androidx.compose.foundation.text.KeyboardOptions
// » KeyboardType = jenis keyboard (Number, Email, Password, Phone).
import androidx.compose.ui.text.input.KeyboardType
// » Memanggil ImeAction (dari androidx.compose.ui.text.input) supaya bisa dipakai di file ini.
import androidx.compose.ui.text.input.ImeAction
// » VisualTransformation = cara menampilkan teks isian (mis. diberi titik ribuan).
import androidx.compose.ui.text.input.VisualTransformation
// » Memanggil IconButton (dari androidx.compose.material3) supaya bisa dipakai di file ini.
import androidx.compose.material3.IconButton
// » Memanggil Icon (dari androidx.compose.material3) supaya bisa dipakai di file ini.
import androidx.compose.material3.Icon
// » Icons = daftar ikon bawaan Material.
import androidx.compose.material.icons.Icons
// » Ikon bawaan Material: Visibility (versi terisi).
import androidx.compose.material.icons.filled.Visibility
// » Ikon bawaan Material: VisibilityOff (versi terisi).
import androidx.compose.material.icons.filled.VisibilityOff

// » Komentar: judul penanda pencarian (CARI-LOGIN).
// CARI-LOGIN | Form email & password; pesan error dari KastepRepository.login.
// » @Composable = fungsi penggambar tampilan.
@Composable
// » fun LoginScreen = layar Login (form email dan password).
fun LoginScreen(
    // » viewModel = sumber data untuk mengecek login.
    viewModel: KastepViewModel,
    // » onLoginSuccess = fungsi dipanggil kalau login berhasil.
    onLoginSuccess: () -> Unit,
    // » onNavigateToRegister = fungsi dipanggil saat tautan Daftar ditekan.
    onNavigateToRegister: () -> Unit
) {
    // » email = teks email yang diketik. by remember { mutableStateOf } = kalau nilainya berubah, tampilan ikut
    // » digambar ulang.
    var email by remember { mutableStateOf("") }
    // » password = teks password yang diketik.
    var password by remember { mutableStateOf("") }
    // » showPassword = apakah password terlihat (true) atau tersamar (false), diatur lewat ikon mata.
    var showPassword by remember { mutableStateOf(false) }
    // » errorMessage = pesan error yang ditampilkan. null = tidak ada error.
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // » Box = wadah bertumpuk: lingkaran dekorasi di belakang, form di depan.
    Box(
        // » modifier = pengaturan kotak.
        modifier = Modifier
            // » Penuh sebesar layar.
            .fillMaxSize()
            // » Latar hitam KASTEP.
            .background(KastepBlack)
    ) {
        // » Canvas = area gambar bebas (dipakai untuk lingkaran dekorasi).
        Canvas(
            // » modifier kanvas.
            modifier = Modifier
                // » Ukuran kanvas 350dp.
                .size(350.dp)
                // » align(BottomEnd) = posisi di pojok kanan bawah.
                .align(Alignment.BottomEnd)
                // » offset = geser kanvas 100dp ke kanan dan 120dp ke bawah (sebagian keluar dari layar).
                .offset(x = 100.dp, y = 120.dp)
        ) {
            // » drawCircle = gambar lingkaran dekorasi.
            drawCircle(
                // » brush = isi berupa gradasi linier.
                brush = Brush.linearGradient(
                    // » colors = dari biru (CircleBlue) ke ungu (CirclePurple).
                    colors = listOf(CircleBlue, CirclePurple),
                    // » start = titik awal gradasi di pojok kiri atas.
                    start = Offset(0f, 0f),
                    // » end = titik akhir gradasi di pojok kanan bawah (ukuran kanvas).
                    end = Offset(size.width, size.height)
                ),
                // » radius = setengah dari sisi terkecil kanvas (lingkaran penuh).
                radius = size.minDimension / 2
            )
        }

        // » Column = susunan atas-bawah untuk form login.
        Column(
            // » modifier kolom.
            modifier = Modifier
                // » Penuh sebesar layar.
                .fillMaxSize()
                // » verticalScroll = bisa digulir kalau layar pendek (misalnya saat keyboard muncul).
                .verticalScroll(rememberScrollState())
                // » Jarak dalam: 24dp kiri-kanan, 40dp atas-bawah.
                .padding(horizontal = 24.dp, vertical = 40.dp)
        ) {
            // » KastepLogoHeader() = panggil fungsi logo di bagian atas (dibuat di bawah).
            KastepLogoHeader()

            // » Jarak kosong 32dp.
            Spacer(modifier = Modifier.height(32.dp))

            // » Text = judul layar.
            Text(
                // » Isi judul: "Login Page".
                text = "Login Page",
                // » Warna putih.
                color = KastepWhite,
                // » Ukuran 28sp.
                fontSize = 28.sp,
                // » Huruf tebal.
                fontWeight = FontWeight.Bold
            )

            // » Jarak kosong 20dp.
            Spacer(modifier = Modifier.height(20.dp))

            // Error message
            // » Kalau ada pesan error (tidak null)...
            if (errorMessage != null) {
                // » Text = kotak pesan error.
                Text(
                    // » text = isi pesan. !! = pasti tidak null di sini.
                    text = errorMessage!!,
                    // » Warna teks merah.
                    color = KastepRed,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    // » modifier kotak pesan.
                    modifier = Modifier
                        .fillMaxWidth()
                        // » Latar merah transparan 10% dengan sudut membulat 8dp.
                        .background(KastepRed.copy(alpha = 0.1f), RoundedCornerShape(8.dp))
                        // » Jarak dalam 12dp.
                        .padding(12.dp)
                )
                // » Jarak 12dp di bawah pesan error.
                Spacer(modifier = Modifier.height(12.dp))
            }

            // » Teks label "Email" (putih, 14sp).
            Text("Email", color = KastepWhite, fontSize = 14.sp, fontWeight = FontWeight.Medium)
            // » Jarak 8dp.
            Spacer(modifier = Modifier.height(8.dp))
            // » OutlinedTextField = kotak isian dengan garis tepi.
            OutlinedTextField(
                // » value = isi kotak = variabel email.
                value = email,
                // » onValueChange = dipanggil saat diketik: simpan teks baru ke email, dan hapus pesan error lama.
                onValueChange = { email = it; errorMessage = null },
                // » modifier = penuh lebar.
                modifier = Modifier.fillMaxWidth(),
                // » Sudut kotak membulat 12dp.
                shape = RoundedCornerShape(12.dp),
                // » placeholder = teks petunjuk abu-abu transparan saat kotak kosong.
                placeholder = { Text("contoh@email.com", color = KastepGray.copy(alpha = 0.5f)) },
                // » KeyboardType.Email = keyboard untuk email (ada tanda @). ImeAction.Next = tombol keyboard berikutnya
                // » pindah ke kolom lain.
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next),
                // » colors = warna kotak saat dipilih dan tidak dipilih.
                colors = OutlinedTextFieldDefaults.colors(
                    // » Garis tepi cyan saat kotak sedang dipilih (fokus).
                    focusedBorderColor = KastepCyan,
                    // » Garis tepi abu-abu transparan saat tidak dipilih.
                    unfocusedBorderColor = KastepGray.copy(alpha = 0.5f),
                    // » Warna teks saat dipilih: putih.
                    focusedTextColor = KastepWhite,
                    // » Warna teks saat tidak dipilih: putih.
                    unfocusedTextColor = KastepWhite,
                    // » Warna kursor (garis berkedip): cyan.
                    cursorColor = KastepCyan
                ),
                // » singleLine = true: isian hanya satu baris.
                singleLine = true
            )

            // » Jarak 16dp.
            Spacer(modifier = Modifier.height(16.dp))

            // » Teks label "Password".
            Text("Password", color = KastepWhite, fontSize = 14.sp, fontWeight = FontWeight.Medium)
            Spacer(modifier = Modifier.height(8.dp))
            // » OutlinedTextField untuk password.
            OutlinedTextField(
                // » value = isi kotak = variabel password.
                value = password,
                // » onValueChange = simpan password baru dan hapus pesan error.
                onValueChange = { password = it; errorMessage = null },
                modifier = Modifier.fillMaxWidth(),
                // » Sudut membulat 12dp.
                shape = RoundedCornerShape(12.dp),
                // » Petunjuk "Minimal 5 karakter".
                placeholder = { Text("Minimal 5 karakter", color = KastepGray.copy(alpha = 0.5f)) },
                // » visualTransformation = kalau showPassword true: tampilkan teks asli (None). Kalau false: tampilkan
                // » titik-titik (PasswordVisualTransformation).
                visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                // » KeyboardType.Password = keyboard untuk password.
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                // » trailingIcon = ikon di ujung kanan kotak (ikon mata).
                trailingIcon = {
                    // » IconButton = tombol ikon. onClick: balik nilai showPassword (true jadi false, false jadi true).
                    IconButton(onClick = { showPassword = !showPassword }) {
                        // » Icon = gambar ikon di dalam tombol.
                        Icon(
                            // » Kalau password terlihat, pakai ikon mata dicoret; kalau tidak, ikon mata biasa.
                            if (showPassword) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                            // » contentDescription = teks untuk pembaca layar (aksesibilitas) sesuai kondisi.
                            contentDescription = if (showPassword) "Sembunyikan password" else "Lihat password",
                            // » tint = warna ikon abu-abu.
                            tint = KastepGray
                        )
                    }
                },
                // » colors kotak password (sama dengan kotak email).
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = KastepCyan,
                    unfocusedBorderColor = KastepGray.copy(alpha = 0.5f),
                    focusedTextColor = KastepWhite,
                    unfocusedTextColor = KastepWhite,
                    cursorColor = KastepCyan
                ),
                singleLine = true
            )

            // » Jarak 32dp sebelum tombol Login.
            Spacer(modifier = Modifier.height(32.dp))

            // » Button = tombol utama Login.
            Button(
                // » onClick = yang terjadi saat tombol ditekan.
                onClick = {
                    // » Panggil viewModel.login(email, password). Hasilnya null (berhasil) atau teks error.
                    val result = viewModel.login(email, password)
                    // » Kalau hasilnya null (tidak ada error)...
                    if (result == null) {
                        // » ...panggil onLoginSuccess (pindah ke Dashboard).
                        onLoginSuccess()
                    } else {
                        // » Kalau ada error, simpan teks error supaya tampil di layar.
                        errorMessage = result
                    }
                },
                // » modifier: penuh lebar, tinggi 56dp.
                modifier = Modifier.fillMaxWidth().height(56.dp),
                // » Sudut tombol membulat 16dp.
                shape = RoundedCornerShape(16.dp),
                // » Warna tombol transparan (warna sebenarnya dari gradasi di dalam).
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                // » contentPadding = jarak isi tombol (standar teks).
                contentPadding = ButtonDefaults.TextButtonContentPadding
            ) {
                // » Box = kotak isi di dalam tombol.
                Box(
                    modifier = Modifier
                        // » Penuh sebesar tombol.
                        .fillMaxSize()
                        // » background = gambar latar gradasi.
                        .background(
                            // » brush horizontalGradient = gradasi kiri ke kanan dari biru awal ke biru akhir.
                            brush = Brush.horizontalGradient(listOf(GradientBlueStart, GradientBlueEnd)),
                            // » Sudut gradasi membulat 16dp.
                            shape = RoundedCornerShape(16.dp)
                        ),
                    // » contentAlignment = Center: teks di tengah.
                    contentAlignment = Alignment.Center
                ) {
                    // » Teks "Login": putih, 20sp, tebal.
                    Text("Login", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                }
            }

            // » Jarak 20dp.
            Spacer(modifier = Modifier.height(20.dp))

            // » Row = susunan mendatar untuk tautan Daftar.
            Row(
                // » Penuh lebar.
                modifier = Modifier.fillMaxWidth(),
                // » Isi di tengah.
                horizontalArrangement = Arrangement.Center
            ) {
                // » Teks "Belum memiliki akun? " (putih, 14sp).
                Text("Belum memiliki akun? ", color = KastepWhite, fontSize = 14.sp)
                // » Teks tautan "Register".
                Text(
                    // » text = "Register".
                    text = "Register",
                    // » Warna cyan.
                    color = KastepCyan,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    // » clickable = bisa ditekan; saat ditekan panggil onNavigateToRegister.
                    modifier = Modifier.clickable { onNavigateToRegister() }
                )
            }
        }
    }
}

// Logo dan tulisan KASTEP (Kas Anak Sekolah Teep) di layar login.
// » @Composable = fungsi penggambar tampilan.
@Composable
// » KastepLogoHeader = logo dan tulisan KASTEP di atas.
fun KastepLogoHeader() {
    // » Row = logo dan tulisan berdampingan, ditengah secara vertikal.
    Row(verticalAlignment = Alignment.CenterVertically) {
        // » Row kecil untuk nama KASTEP dan ikon.
        Row(verticalAlignment = Alignment.CenterVertically) {
            // » Teks "KASTEP": putih, 18sp, tebal, jarak huruf 1sp.
            Text("KASTEP", color = KastepWhite, fontSize = 18.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
            // » Canvas = gambar ikon kecil (dompet/kartu) ukuran 28dp.
            Canvas(modifier = Modifier.size(28.dp)) {
                // » c = warna cyan untuk garis.
                val c = KastepCyan
                // » sw = tebal garis 1.5dp dalam pixel (toPx = ubah dp ke pixel).
                val sw = 1.5.dp.toPx()
                // » drawRoundRect = gambar persegi membulat (badan dompet): posisi dan ukuran dalam persentase kanvas.
                drawRoundRect(c, Offset(size.width * 0.05f, size.height * 0.3f), Size(size.width * 0.7f, size.height * 0.55f), CornerRadius(4.dp.toPx()), style = Stroke(sw))
                // » Persegi membulat kedua (kartu di atas dompet), lebih kecil.
                drawRoundRect(c, Offset(size.width * 0.15f, size.height * 0.12f), Size(size.width * 0.5f, size.height * 0.35f), CornerRadius(3.dp.toPx()), style = Stroke(sw * 0.8f))
                // » drawCircle = lingkaran kecil (gesper) berwarna cyan.
                drawCircle(c, 3.dp.toPx(), Offset(size.width * 0.68f, size.height * 0.57f), style = Stroke(sw))
            }
        }
        // » Jarak 12dp.
        Spacer(modifier = Modifier.width(12.dp))
        // » Box = garis tegak tipis pemisah (lebar 1dp, tinggi 36dp, abu-abu transparan).
        Box(modifier = Modifier.width(1.dp).height(36.dp).background(KastepGray.copy(alpha = 0.5f)))
        // » Jarak 12dp.
        Spacer(modifier = Modifier.width(12.dp))
        // » Column = tiga baris teks susun ke bawah.
        Column {
            // » Teks baris 1: "KAS ANAK" (kepanjangan KASTEP).
            Text("KAS ANAK", color = KastepWhite, fontSize = 11.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
            // » Teks baris 2: "SEKOLAH".
            Text("SEKOLAH", color = KastepWhite, fontSize = 11.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
            // » Teks baris 3: "TEEP".
            Text("TEEP", color = KastepWhite, fontSize = 11.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
        }
    }
}
