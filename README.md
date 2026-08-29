# 📱 KASTEP - Class Cash Management Application for XII PPLG

![Android](https://img.shields.io/badge/Platform-Android-green.svg)
![Kotlin](https://img.shields.io/badge/Language-Kotlin_2.0-blue.svg)
![UI Framework](https://img.shields.io/badge/UI-Jetpack_Compose_Material3-purple.svg)
![Storage](https://img.shields.io/badge/Storage-Offline--First_(SharedPreferences)-orange.svg)
![Build Tool](https://img.shields.io/badge/Build-Gradle_(JDK_21)-darkgreen.svg)
![Release](https://img.shields.io/badge/Release-v2.0.0--pre-brightgreen.svg)

**KASTEP** adalah aplikasi manajemen kas dan keuangan kelas modern yang dirancang khusus untuk memenuhi kebutuhan pengelolaan keuangan kelas **XII PPLG**. Aplikasi ini mengkombinasikan antarmuka bertema **Pure Black** dengan aksen Cyan/Blue yang elegan, responsif, berkinerja tinggi berbasis **Jetpack Compose (Material3)** dan menerapkan pola arsitektur **Offline-First**.

---

## 📋 Daftar Isi

- [🎯 Latar Belakang & Tujuan Project](#-latar-belakang--tujuan-project)
- [💾 Mekanisme Penyimpanan Data Lokal HP](#-mekanisme-penyimpanan-data-lokal-hp)
- [❓ Pertanyaan Umum & FAQ (QnA dengan Kode Langsung)](#-pertanyaan-umum--faq-qna-dengan-kode-langsung)
- [✨ Fitur-Fitur Utama](#-fitur-fitur-utama)
- [🔐 Hak Akses & Multi-Role (Admin & User)](#-hak-akses--multi-role-admin--user)
- [🏛️ Arsitektur Aplikasi & Design System](#️-arsitektur-aplikasi--design-system)
- [📂 Struktur Direktori Proyek](#-struktur-direktori-proyek)
- [💻 Panduan Kompilasi & Build APK](#-panduan-kompilasi--build-apk)
- [📊 Data Initial 33 Siswa XII PPLG](#-data-initial-33-siswa-xii-pplg)

---

## 🎯 Latar Belakang & Tujuan Project

Dalam pengelolaan kas kelas skala 33 siswa, pencatatan manual sering memicu ketidakcocokan data, hilangnya riwayat pembayaran, serta kesulitan dalam memberikan pelaporan transparan. **KASTEP** hadir sebagai solusi komprehensif dengan 4 pilar utama:

1. **Kejujuran & Transparansi**: Seluruh anggota kelas dapat melihat status pembayaran kas bulanan, daftar siswa yang sudah/belum bayar, serta laporan pengeluaran kas secara interaktif.
2. **Kemudahan Pembayaran**: Pilihan metode pembayaran lengkap (QRIS, E-wallets: DANA, GoPay, OVO, Bank: BCA, Mandiri, BNI, serta Cash) dengan **Auto-Confirmation WhatsApp**.
3. **Ketahanan Data (Offline-First)**: Seluruh data tersimpan secara lokal pada penyimpanan internal HP menggunakan `SharedPreferences` sehingga aplikasi tetap responsif tanpa ketergantungan internet.
4. **Desain Modern Premium**: Desain bertema serba hitam (Pure Black Theme `#000000`) dengan aksen cyan & biru, dialog interaktif, serta visualisasi grafik trend kas.

---

## 💾 Mekanisme Penyimpanan Data Lokal HP

Seluruh data pengguna, registrasi akun, riwayat transaksi pembayaran, pengeluaran kas, dan status iuran 33 siswa disimpan secara **LOKAL di penyimpanan internal perangkat (Offline-First)**.

### 📍 Lokasi File Penyimpanan di Android OS
- **Nama File SharedPreference**: `kastep_prefs.xml`
- **Path Asli di Perangkat**:  
  `/data/data/com.kastep.app/shared_prefs/kastep_prefs.xml`
- **Mode Akses**: `Context.MODE_PRIVATE` (Akses eksklusif hanya oleh paket aplikasi KASTEP).

---

## ❓ Pertanyaan Umum & FAQ (QnA dengan Kode Langsung)

Berikut adalah pertanyaan umum seputar teknis dan implementasi beserta **potongan kode Kotlin asli** yang mengatur logika masing-masing fitur:

---

### Q1: Di mana dan bagaimana data aplikasi ini disimpan di lokal HP?
> **Penjelasan**: Data disimpan menggunakan Android `SharedPreferences` yang di-serialize ke format JSON menggunakan library `Gson`.
>
> 💻 **Kode Pembuat Storage Engine (`KastepRepository.kt`)**:
```kotlin
private val prefs: SharedPreferences = 
    context.getSharedPreferences("kastep_prefs", Context.MODE_PRIVATE)

private fun saveSiswaList(list: List<Siswa>) {
    val json = gson.toJson(list)
    prefs.edit().putString(KEY_SISWA, json).apply()
}

private fun loadSiswaList(): List<Siswa> {
    val json = prefs.getString(KEY_SISWA, null) ?: return getInitialSiswaList()
    val type = object : TypeToken<List<Siswa>>() {}.type
    return gson.fromJson(json, type) ?: getInitialSiswaList()
}
```

---

### Q2: Apa yang terjadi jika "Clear Data / Storage" dilakukan di Pengaturan Android?
> **Penjelasan**: Jika data dihapus dari OS Android, file `kastep_prefs.xml` hilang. Saat aplikasi dibuka kembali, fungsi `loadSiswaList()` mendeteksi `json == null` dan otomatis me-load data initial standar 33 siswa (reset dari 0).
>
> 💻 **Kode Fallback Data Initial (`KastepRepository.kt`)**:
```kotlin
private fun loadSiswaList(): List<Siswa> {
    val json = prefs.getString(KEY_SISWA, null) ?: return getInitialSiswaList()
    // Jika file terhapus, fungsi di atas mengembalikan getInitialSiswaList()
}
```

---

### Q3: Bagaimana logika autentikasi dan akun Admin default ditentukan?
> **Penjelasan**: Akun administrator default hardcoded untuk email `admin@gmail.com` dan password `admin123` dengan role `UserRole.ADMIN`. Pengguna lain diperiksa dari daftar akun registered.
>
> 💻 **Kode Autentikasi Login (`KastepRepository.kt`)**:
```kotlin
fun login(email: String, pass: String): Pair<Boolean, String> {
    val cleanEmail = email.trim()
    val cleanPass = pass.trim()

    // Built-in Admin Account
    if (cleanEmail.equals("admin@gmail.com", ignoreCase = true) && cleanPass == "admin123") {
        val adminProfile = UserProfile(
            nama = "Administrator",
            email = "admin@gmail.com",
            role = UserRole.ADMIN
        )
        saveUserProfile(adminProfile)
        _userProfile.value = adminProfile
        return Pair(true, "Login Admin Berhasil")
    }
    
    // Regular Registered Users Check...
}
```

---

### Q4: Bagaimana cara pendaftaran (Register) akun pengguna baru?
> **Penjelasan**: Validasi memeriksa format email, kelengkapan nama, dan panjang password minimal 5 karakter. Jika valid, profil disimpan di `SharedPreferences`.
>
> 💻 **Kode Pendaftaran User Baru (`KastepRepository.kt`)**:
```kotlin
fun registerUser(nama: String, email: String, nis: String, kelas: String, noHp: String, pass: String): Pair<Boolean, String> {
    if (nama.isBlank() || email.isBlank() || pass.isBlank()) {
        return Pair(false, "Semua field bertanda * wajib diisi!")
    }
    if (!email.contains("@") || !email.contains(".")) {
        return Pair(false, "Format email tidak valid!")
    }
    if (pass.length < 5) {
        return Pair(false, "Password minimal 5 karakter!")
    }
    val newUser = UserProfile(nama = nama, email = email, nis = nis, kelas = kelas, noHp = noHp, password = pass, role = UserRole.USER)
    // Simpan ke daftar registered users
    saveRegisteredUser(newUser)
    return Pair(true, "Registrasi berhasil! Silakan login.")
}
```

---

### Q5: Berapa nominal kas bulanan dan bagaimana status pembayaran siswa diupdate?
> **Penjelasan**: Nominal kas ditetapkan **Rp 20.000 / bulan**. Saat pembayaran sukses diproses, fungsi `processPayment` memperbarui status iuran siswa (Juli / Agustus) dari `BELUM` menjadi `LUNAS` dan menambah catatan riwayat transaksi.
>
> 💻 **Kode Pemrosesan Pembayaran (`KastepRepository.kt`)**:
```kotlin
fun processPayment(namaSiswa: String, bulan: String, metode: String, jumlah: Long = 20000L): Boolean {
    val currentSiswa = _siswaList.value.toMutableList()
    val index = currentSiswa.indexOfFirst { it.nama.equals(namaSiswa, ignoreCase = true) }
    if (index != -1) {
        val s = currentSiswa[index]
        val updated = if (bulan.contains("Juli", ignoreCase = true)) {
            s.copy(statusJuli = StatusBayar.LUNAS)
        } else {
            s.copy(statusAgustus = StatusBayar.LUNAS)
        }
        currentSiswa[index] = updated
        saveSiswaList(currentSiswa)
        _siswaList.value = currentSiswa

        // Tambah ke riwayat transaksi
        val record = PaymentRecord(
            no = _paymentRecords.value.size + 1,
            tanggal = getCurrentDate(),
            namaSiswa = namaSiswa,
            jumlah = jumlah,
            bulan = bulan,
            metode = metode
        )
        addPaymentRecord(record)
        return true
    }
    return false
}
```

---

### Q6: Bagaimana mekanisme Auto-Confirmation ke WhatsApp?
> **Penjelasan**: Menggunakan `Intent.ACTION_VIEW` dengan URL schema `https://api.whatsapp.com/send` menyasar nomor WhatsApp **`+62 895-2037-1942`**.
>
> 💻 **Kode Konfirmasi WhatsApp (`PembayaranScreen.kt`)**:
```kotlin
val targetWa = "+6289520371942"
val message = "Halo Admin Kas KASTEP,\nSaya telah melakukan pembayaran kas kelas XII PPLG:\n\n" +
              "👤 Nama: $selectedStudent\n" +
              "📅 Bulan: $selectedMonth\n" +
              "💰 Nominal: Rp ${KastepViewModel.formatRupiah(20000)}\n" +
              "💳 Metode: $selectedMethod\n\nMohon konfirmasinya. Terima kasih!"

val encodedMsg = URLEncoder.encode(message, "UTF-8")
val waUrl = "https://api.whatsapp.com/send?phone=$targetWa&text=$encodedMsg"
val intent = Intent(Intent.ACTION_VIEW, Uri.parse(waUrl))
context.startActivity(intent)
```

---

### Q7: Bagaimana fungsi CRUD (Tambah, Edit, Hapus) Siswa & Pengeluaran?
> **Penjelasan**: Semua operasi CRUD langsung meng-update `StateFlow` dan melakukan persistence otomatis ke `SharedPreferences`.
>
> 💻 **Kode CRUD Siswa & Pengeluaran (`KastepRepository.kt`)**:
```kotlin
// Tambah Siswa
fun addSiswa(nama: String, peran: String, noHp: String) {
    val current = _siswaList.value.toMutableList()
    val newSiswa = Siswa(no = current.size + 1, nama = nama, peran = peran, noHp = noHp, statusJuli = StatusBayar.BELUM, statusAgustus = StatusBayar.BELUM)
    current.add(newSiswa)
    saveSiswaList(current)
    _siswaList.value = current
}

// Hapus Siswa (dengan Auto Re-numbering)
fun deleteSiswa(siswa: Siswa) {
    val current = _siswaList.value.filter { it.no != siswa.no }.mapIndexed { idx, s -> s.copy(no = idx + 1) }
    saveSiswaList(current)
    _siswaList.value = current
}

// Edit Pengeluaran Kas
fun updatePengeluaran(pengeluaran: Pengeluaran) {
    val current = _pengeluaranList.value.toMutableList()
    val idx = current.indexOfFirst { it.id == pengeluaran.id }
    if (idx != -1) {
        current[idx] = pengeluaran
        savePengeluaranList(current)
        _pengeluaranList.value = current
    }
}
```

---

### Q8: Bagaimana kartu Laporan Kas dapat diklik untuk menampilkan detail breakdown?
> **Penjelasan**: Kartu Total Pemasukan dan Pengeluaran dipasang Modifier `clickable` untuk membuka state dialog detail (`showPemasukanDetailDialog` & `showPengeluaranDetailDialog`).
>
> 💻 **Kode Dialog Clickable Laporan (`LaporanKasScreen.kt`)**:
```kotlin
// Card Total Pemasukan Clickable
Card(
    modifier = Modifier
        .weight(1f)
        .clickable { showPemasukanDetailDialog = true },
    colors = CardDefaults.cardColors(containerColor = KastepCardDark)
) {
    // Menampilkan total Pemasukan & label "(Klik detail)"
}

// Dialog Detail Pemasukan (Status Siswa Lunas vs Belum)
if (showPemasukanDetailDialog) {
    AlertDialog(
        onDismissRequest = { showPemasukanDetailDialog = false },
        title = { Text("Detail Pemasukan Kas ($selectedPeriod)") },
        text = {
            LazyColumn {
                items(siswaList) { s ->
                    Row(horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(s.nama)
                        Text(if (isLunas) "LUNAS (✓)" else "BELUM (✗)")
                    }
                }
            }
        },
        confirmButton = { TextButton(onClick = { showPemasukanDetailDialog = false }) { Text("Tutup") } }
    )
}
```

---

## ✨ Fitur-Fitur Utama

### 1. 🔑 Validasi Login & Register Interaktif
- Validasi format email (`@` dan `.`), batas panjang password (minimal 5 karakter), serta pengecekan nama kosong.
- Akun Administrator default (`admin@gmail.com` / `admin123`) dan sistem pendaftaran akun pengguna baru.

### 2. 👥 Manajemen Data Siswa (CRUD Full)
- Tabel 33 siswa kelas XII PPLG dengan indikator status pembayaran bulanan (`✓` Lunas / `✗` Belum).
- **Tambah Siswa**: Form modal dialog penambahan siswa baru.
- **Edit & Hapus Siswa**: Tap baris siswa untuk memperbarui nama/peran atau menghapus siswa dari daftar.

### 3. 💳 Pembayaran Multi-Channel & Auto-WA Confirmation
- Pilihan pembayaran: QRIS, DANA, GoPay, OVO, BCA, Mandiri, BNI, dan CASH.
- Simulasi nomor rekening & QRIS interaktif.
- Auto-Confirmation via WhatsApp ke nomor `+62 895-2037-1942`.
- Dialog perayaan sukses pembayaran.

### 4. 💸 Catatan Pengeluaran Kas (CRUD Full)
- Form pencatatan tanggal, nominal, dan keterangan pengeluaran.
- Riwayat pengeluaran kas yang dapat di-edit dan dihapus kapan saja.

### 5. 📈 Laporan Kas Interaktif
- Dropdown pemfilteran periode laporan (*Juli 2026*, *Agustus 2026*, *Semua Periode*).
- Kartu interaktif Total Pemasukan (detail Lunas/Belum) & Total Pengeluaran (detail log transaksi).
- Chart trend Pemasukan vs Pengeluaran berbasis Jetpack Compose Canvas.

---

## 🔐 Hak Akses & Multi-Role (Admin & User)

| Fitur | Administrator (`admin@gmail.com`) | User Terdaftar / Anggota |
|---|:---:|:---:|
| Login & Register | ✅ | ✅ |
| Dashboard Keuangan & Trend | ✅ | ✅ |
| Pembayaran Kas & Simulasi WA | ✅ | ✅ |
| Tambah / Edit / Hapus Data Siswa | ✅ | ✅ |
| Tambah / Edit / Hapus Pengeluaran | ✅ | ✅ |
| Filter & Detail Laporan Kas | ✅ | ✅ |
| Badge Indikator Role | `Admin` | `User` |

---

## 🏛️ Arsitektur Aplikasi & Design System

Aplikasi dibangun menggunakan arsitektur **MVVM (Model-View-ViewModel)** dengan **Unidirectional Data Flow (UDF)**:

```
 ┌─────────────────────────────────────────────────────────────┐
 │                       UI Layer (Compose)                    │
 │ DashboardScreen, DataSiswaScreen, PembayaranScreen, dst.    │
 └──────────────────────────────▲──────────────────────────────┘
                                │ StateFlow Updates
                                │ User Actions
 ┌──────────────────────────────┴──────────────────────────────┐
 │                     KastepViewModel                         │
 │        Exposes StateFlows & Delegates to Repository         │
 └──────────────────────────────▲──────────────────────────────┘
                                │ Flow Data
 ┌──────────────────────────────┴──────────────────────────────┐
 │                     KastepRepository                        │
 │     SharedPreferences Persistence Engine & Local Storage     │
 └─────────────────────────────────────────────────────────────┘
```

---

## 📂 Struktur Direktori Proyek

```
kastep/
├── app/
│   ├── build.gradle.kts                # Config Gradle & Dependensi Compose
│   └── src/
│       └── main/
│           ├── AndroidManifest.xml     # Manifest Aplikasi
│           ├── res/
│           │   └── drawable-xxhdpi/    # Asset Icon PNG (bca, bni, dana, gopay, mandiri, ovo, qris, spongebob)
│           └── java/com/kastep/app/
│               ├── MainActivity.kt     # Main Entry Point & Drawer Navigation
│               ├── data/
│               │   ├── KastepModels.kt     # Data Models (Siswa, UserProfile, Pengeluaran, UserRole, StatusBayar)
│               │   ├── KastepRepository.kt # Engine SharedPreferences Local Persistence
│               │   └── KastepViewModel.kt  # State management ViewModel
│               └── ui/
│                   ├── navigation/     # NavGraph & Screen Routes
│                   ├── screens/        # Login, Register, Dashboard, DataSiswa, Pembayaran, Pengeluaran, Laporan, Profile
│                   └── theme/          # Color, Theme, Type
├── KASTEP.apk                          # Output APK Compiled
├── README.md                           # Dokumentasi Resmi Aplikasi
└── build.gradle.kts                    # Root build configuration
```

---

## 💻 Panduan Kompilasi & Build APK

### Command Satu Baris (Build Debug APK):
```bash
cd /home/wira/Documents/kastep && JAVA_HOME=/usr/lib/jvm/java-21-openjdk-amd64 ./gradlew clean assembleDebug && cp app/build/outputs/apk/debug/app-debug.apk KASTEP.apk && echo "✅ Build selesai! APK: KASTEP.apk"
```

Output file `KASTEP.apk` akan otomatis disalin ke direktori utama proyek.

---

## 👨‍💻 Developer & Kontributor

Dikembangkan dengan 💙 oleh Tim Pengembang **KASTEP App** untuk kelas **XII PPLG**.  
Bantuan & Dukungan Teknis Direct WhatsApp: **[+62 895-2037-1942](https://wa.me/6289520371942)**.
