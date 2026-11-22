// ==========================================================================
// PETA FILE · KASTEP (Kelompok 1) · RegisterScreen.kt
// Isi file : Halaman Register akun baru.
// Penanda  : CARI-REGISTER
// Cara cari: VS Code Ctrl+Shift+F (Android Studio Ctrl+Shift+F) lalu ketik
//            "CARI-" untuk semua penanda, atau salah satu nama di baris Penanda.
// ==========================================================================
//  BACA INI DULU
//  Form daftar: Nama, Email, NIS, Kelas, No. HP, Password. Memanggil viewModel.register. Akun baru berperan USER.
//  Validasi ada di KastepRepository.register (nama min 3 huruf, email valid, password min 5, email belum terdaftar).
// » Alamat folder (package) file ini di dalam proyek.
package com.kastep.app.ui.screens

// » Canvas = kanvas kosong untuk menggambar sendiri (garis, lingkaran, grafik).
import androidx.compose.foundation.Canvas
// » background = mengisi warna/gradien latar sebuah tampilan.
import androidx.compose.foundation.background
// » Memanggil Box (dari androidx.compose.foundation.layout) supaya bisa dipakai di file ini.
import androidx.compose.foundation.layout.Box
// » Memanggil Column (dari androidx.compose.foundation.layout) supaya bisa dipakai di file ini.
import androidx.compose.foundation.layout.Column
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
// » Offset = titik koordinat (x, y) di Canvas.
import androidx.compose.ui.geometry.Offset
// » Brush = 'cat' warna, termasuk gradien (warna berubah halus).
import androidx.compose.ui.graphics.Brush
// » Color = tipe warna Compose; dipakai untuk menulis warna seperti Color.White atau Color(0xFF...).
import androidx.compose.ui.graphics.Color
// » FontWeight = ketebalan huruf (Normal, Medium, Bold, ExtraBold).
import androidx.compose.ui.text.font.FontWeight
// » PasswordVisualTransformation = menyamarkan teks menjadi titik-titik.
import androidx.compose.ui.text.input.PasswordVisualTransformation
// » VisualTransformation = cara menampilkan teks isian (mis. diberi titik ribuan).
import androidx.compose.ui.text.input.VisualTransformation
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
// » Memanggil Row (dari androidx.compose.foundation.layout) supaya bisa dipakai di file ini.
import androidx.compose.foundation.layout.Row
// » Memanggil Arrangement (dari androidx.compose.foundation.layout) supaya bisa dipakai di file ini.
import androidx.compose.foundation.layout.Arrangement
// » clickable = membuat tampilan bisa diketuk.
import androidx.compose.foundation.clickable

// » Komentar: penanda pencarian (CARI-REGISTER). Isi form dibahas di bawah.
// CARI-REGISTER | Form nama, email, NIS, kelas, No. HP, password; link kembali ke Login di bawah.
// » @Composable = fungsi penggambar tampilan.
@Composable
// » fun RegisterScreen = layar daftar akun baru.
fun RegisterScreen(
    // » viewModel = sumber data untuk menyimpan akun baru.
    viewModel: KastepViewModel,
    // » onRegisterSuccess = dipanggil kalau pendaftaran berhasil.
    onRegisterSuccess: () -> Unit,
    // » onBackToLogin = fungsi kembali ke Login. Default {} = tidak melakukan apa-apa.
    onBackToLogin: () -> Unit = {}
) {
    // » nama = isi kolom Nama (kosong di awal).
    var nama by remember { mutableStateOf("") }
    // » email = isi kolom Email.
    var email by remember { mutableStateOf("") }
    // » nis = isi kolom NIS (Nomor Induk Siswa).
    var nis by remember { mutableStateOf("") }
    // » kelas = isi kolom Kelas, awalnya "XII PPLG".
    var kelas by remember { mutableStateOf("XII PPLG") }
    // » noHp = isi kolom No HP.
    var noHp by remember { mutableStateOf("") }
    // » password = isi kolom Password.
    var password by remember { mutableStateOf("") }
    // » errorMessage = pesan error (null = tidak ada).
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // » Box = wadah bertumpuk: dekorasi lingkaran di belakang, form di depan.
    Box(
        // » Penuh sebesar layar, latar hitam.
        modifier = Modifier.fillMaxSize().background(KastepBlack)
    ) {
        // » Canvas = area gambar dekorasi.
        Canvas(
            // » Ukuran 350dp, posisi pojok kanan bawah, digeser 100dp kanan dan 120dp bawah.
            modifier = Modifier.size(350.dp).align(Alignment.BottomEnd).offset(x = 100.dp, y = 120.dp)
        ) {
            // » drawCircle = gambar lingkaran dekorasi.
            drawCircle(
                // » Gradasi linier dari CircleBlue ke CirclePurple, dari pojok kiri atas ke kanan bawah.
                brush = Brush.linearGradient(listOf(CircleBlue, CirclePurple), Offset(0f, 0f), Offset(size.width, size.height)),
                // » Jari-jari = setengah sisi terkecil.
                radius = size.minDimension / 2
            )
        }

        // » Column = susunan atas-bawah form.
        Column(
            // » Penuh, bisa digulir (verticalScroll), jarak dalam 24dp kiri-kanan dan 40dp atas-bawah.
            modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(horizontal = 24.dp, vertical = 40.dp)
        ) {
            // » KastepLogoHeader() = logo KASTEP di atas.
            KastepLogoHeader()
            // » Jarak 24dp.
            Spacer(modifier = Modifier.height(24.dp))

            // » Judul "Register" (putih, 28sp, tebal).
            Text("Register", color = KastepWhite, fontSize = 28.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            // » Keterangan kecil di bawah judul (abu-abu, 13sp).
            Text("Buat akun baru untuk mengelola kas kelas", color = KastepGray, fontSize = 13.sp)

            Spacer(modifier = Modifier.height(16.dp))

            // » Kalau ada pesan error...
            if (errorMessage != null) {
                // » Teks pesan error (merah).
                Text(
                    // » text = isi pesan error (!! = pasti tidak null).
                    text = errorMessage!!,
                    color = KastepRed,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    // » Penuh lebar, latar merah transparan dan sudut membulat.
                    modifier = Modifier.fillMaxWidth()
                        .background(KastepRed.copy(alpha = 0.1f), RoundedCornerShape(8.dp))
                        .padding(12.dp)
                )
                Spacer(modifier = Modifier.height(12.dp))
            }

            // » FormField = kolom isian berlabel. Nama wajib diisi (tanda *). Setiap diketik, pesan error dihapus.
            FormField(label = "Nama *", value = nama, onValueChange = { nama = it; errorMessage = null }, placeholder = "Nama lengkap (min. 3 karakter)")
            Spacer(modifier = Modifier.height(12.dp))
            // » Kolom Email (wajib). keyboardType untuk email (lihat parameter di akhir baris).
            FormField(label = "Email *", value = email, onValueChange = { email = it; errorMessage = null }, placeholder = "contoh@email.com", keyboardType = KeyboardType.Email)
            Spacer(modifier = Modifier.height(12.dp))
            // » Kolom NIS (tidak wajib). Hanya menyimpan teks yang diketik.
            FormField(label = "NIS", value = nis, onValueChange = { nis = it }, placeholder = "Nomor Induk Siswa")
            Spacer(modifier = Modifier.height(12.dp))
            // » Kolom Kelas, default "XII PPLG".
            FormField(label = "Kelas", value = kelas, onValueChange = { kelas = it }, placeholder = "XII PPLG")
            Spacer(modifier = Modifier.height(12.dp))
            // » Kolom No HP. Hanya menyimpan angka dan tanda + (filter: isDigit atau '+').
            FormField(label = "No HP", value = noHp, onValueChange = { v -> noHp = v.filter { it.isDigit() || it == '+' } }, placeholder = "+62...", keyboardType = KeyboardType.Phone)
            Spacer(modifier = Modifier.height(12.dp))
            // » Kolom Password (wajib). isPassword = true: teks tersamar dengan titik.
            FormField(label = "Password *", value = password, onValueChange = { password = it; errorMessage = null }, isPassword = true, placeholder = "Minimal 5 karakter")

            // » Jarak 32dp sebelum tombol.
            Spacer(modifier = Modifier.height(32.dp))

            // » Button = tombol Register.
            Button(
                onClick = {
                    // » Panggil viewModel.register(...) dengan semua isian. Hasilnya null (berhasil) atau teks error.
                    val result = viewModel.register(nama, email, nis, kelas, noHp, password)
                    // » Kalau null (berhasil)...
                    if (result == null) {
                        // » ...panggil onRegisterSuccess (pindah ke Dashboard).
                        onRegisterSuccess()
                    } else {
                        // » Kalau ada error, tampilkan pesan error di layar.
                        errorMessage = result
                    }
                },
                // » Tombol penuh lebar, tinggi 56dp.
                modifier = Modifier.fillMaxWidth().height(56.dp),
                // » Sudut membulat 16dp.
                shape = RoundedCornerShape(16.dp),
                // » Warna tombol transparan (gradasi dibuat di dalam).
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                // » Jarak isi tombol standar.
                contentPadding = ButtonDefaults.TextButtonContentPadding
            ) {
                // » Box isi tombol.
                Box(
                    // » Penuh sebesar tombol.
                    modifier = Modifier.fillMaxSize()
                        // » Latar gradasi horizontal dari GradientBlueStart ke GradientBlueEnd, sudut membulat 16dp.
                        .background(Brush.horizontalGradient(listOf(GradientBlueStart, GradientBlueEnd)), RoundedCornerShape(16.dp)),
                    // » Teks di tengah.
                    contentAlignment = Alignment.Center
                ) {
                    // » Teks "Register": putih, 20sp, tebal.
                    Text("Register", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                }
            }

            // » Jarak 20dp.
            Spacer(modifier = Modifier.height(20.dp))
            // » Row = tautan kembali ke Login di tengah.
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                // » Teks "Sudah punya akun? ".
                Text("Sudah punya akun? ", color = KastepWhite, fontSize = 14.sp)
                // » Teks tautan "Login" (cyan, tebal).
                Text(
                    "Login", color = KastepCyan, fontSize = 14.sp, fontWeight = FontWeight.Bold,
                    // » clickable = saat ditekan, panggil onBackToLogin.
                    modifier = Modifier.clickable { onBackToLogin() }
                )
            }
        }
    }
}

// Kolom isian berlabel di form Register.
// » @Composable private fun FormField = fungsi bantu untuk satu kolom isian berlabel. Hanya dipakai di file
// » ini.
@Composable
private fun FormField(
    // » label = teks label di atas kotak.
    label: String,
    // » value = isi kotak.
    value: String,
    // » onValueChange = dipanggil saat isi berubah.
    onValueChange: (String) -> Unit,
    // » isPassword = apakah kolom ini password (default false).
    isPassword: Boolean = false,
    // » placeholder = teks petunjuk.
    placeholder: String = "",
    // » keyboardType = jenis keyboard. Default: keyboard password kalau isPassword, selain itu keyboard teks
    // » biasa.
    keyboardType: KeyboardType = if (isPassword) KeyboardType.Password else KeyboardType.Text
) {
    // » Teks label kolom (putih, 14sp, semi tebal).
    Text(text = label, color = KastepWhite, fontSize = 14.sp, fontWeight = FontWeight.Medium)
    // » Jarak 6dp.
    Spacer(modifier = Modifier.height(6.dp))
    // » OutlinedTextField = kotak isian.
    OutlinedTextField(
        // » value = isi kotak.
        value = value,
        // » onValueChange = fungsi yang dipanggil saat diketik (diteruskan dari pemanggil).
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        // » Sudut membulat 12dp.
        shape = RoundedCornerShape(12.dp),
        // » placeholder = teks petunjuk abu-abu transparan.
        placeholder = { Text(placeholder, color = KastepGray.copy(alpha = 0.5f)) },
        // » visualTransformation: kalau password, tampil titik-titik. Kalau tidak, teks biasa.
        visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
        // » keyboardOptions = jenis keyboard sesuai keyboardType.
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        // » colors = warna kotak (cyan saat dipilih, abu-abu saat tidak).
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = KastepCyan,
            unfocusedBorderColor = KastepGray.copy(alpha = 0.5f),
            focusedTextColor = KastepWhite,
            unfocusedTextColor = KastepWhite,
            cursorColor = KastepCyan
        ),
        // » singleLine = true: satu baris saja.
        singleLine = true
    )
}
