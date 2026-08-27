# 📱 KASTEP - Cash & Financial Management Application for XII PPLG

![Android](https://img.shields.io/badge/Platform-Android-green.svg)
![Kotlin](https://img.shields.io/badge/Language-Kotlin_2.0-blue.svg)
![UI Framework](https://img.shields.io/badge/UI-Jetpack_Compose_Material3-purple.svg)
![Storage](https://img.shields.io/badge/Storage-Offline--First_(SharedPreferences)-orange.svg)
![Build Tool](https://img.shields.io/badge/Build-Gradle_(JDK_21)-darkgreen.svg)

**KASTEP** adalah aplikasi manajemen kas dan keuangan kelas modern yang dirancang khusus untuk memenuhi kebutuhan pengelolaan kas kelas **XII PPLG**. Aplikasi ini menghadirkan antarmuka bertema **Pure Black** dengan aksen cyan/biru yang elegan, responsif, berkinerja tinggi berbasis **Jetpack Compose (Material3)** dan menerapkan arsitektur **Offline-First**.

---

## 📋 Daftar Isi

- [🎯 Latar Belakang & Tujuan Project](#-latar-belakang--tujuan-project)
- [💾 Mekanisme Penyimpanan Data Lokal HP](#-mekanisme-penyimpanan-data-lokal-hp)
- [✨ Fitur-Fitur Utama](#-fitur-fitur-utama)
- [🏛️ Arsitektur Aplikasi & Design System](#️-arsitektur-aplikasi--design-system)
- [📂 Struktur Direktori Proyek](#-struktur-direktori-proyek)
- [🔍 Deep-Dive Kode & Komponen Utama](#-deep-dive-kode--komponen-utama)
- [💻 Panduan Instalasi Lokal & Build APK](#-panduan-instalasi-lokal--build-apk)
- [📊 Ringkasan Data Initial 33 Anggota](#-ringkasan-data-initial-33-anggota)

---

## 🎯 Latar Belakang & Tujuan Project

Dalam pengelolaan kas kelas skala 33 siswa, pencatatan manual sering memicu ketidakcocokan data, hilangnya riwayat pembayaran, serta kesulitan dalam memberikan pelaporan transparan. **KASTEP** hadir sebagai solusi komprehensif dengan 4 pilar utama:

1. **Kejujuran & Transparansi**: Seluruh anggota kelas dapat melihat status pembayaran kas bulanan dan grafik pemasukan vs pengeluaran secara langsung.
2. **Kemudahan Pembayaran**: Pilihan metode pembayaran lengkap (QRIS, E-wallets: DANA, GoPay, OVO, Bank: BCA, Mandiri, BNI, serta Cash).
3. **Ketahanan Data (Offline-First)**: Data tersimpan secara lokal pada penyimpanan internal HP menggunakan `SharedPreferences` dan `Gson` sehingga aplikasi tetap responsif tanpa koneksi internet.
4. **Desain Modern Premium**: Desain bertema serba hitam (Pure Black Theme `#000000`) dengan aksen cyan & biru, animasi perayaan pembayaran, serta ilustrasi custom Canvas.

---

## 💾 Mekanisme Penyimpanan Data Lokal HP

Seluruh data pengguna, riwayat transaksi, dan status iuran 33 siswa disimpan secara **LOKAL di penyimpanan internal handphone**.

### 📍 Lokasi File Penyimpanan di Android OS
- **Nama File Preference**: `kastep_prefs.xml`
- **Path Asli di Perangkat**:  
  `/data/data/com.kastep.app/shared_prefs/kastep_prefs.xml`
- **Mode Akses**: `Context.MODE_PRIVATE` (Akses eksklusif oleh aplikasi KASTEP).

### 🔒 Perizinan Aplikasi (Permissions)
- **Zero Runtime Permissions**: Menggunakan internal sandboxed storage Android, sehingga aplikasi **tidak memerlukan konfirmasi izin file/storage** dari pengguna.

---

## ✨ Fitur-Fitur Utama

### 📊 1. Dashboard Keuangan Class Cash
- **Kartu Total Kas**: Menampilkan saldo kas terkini (`Rp 1.000.000`), Pemasukan, dan Pengeluaran.
- **Progress Bar Iuran**: Indikator persentase siswa yang sudah lunas bayar kas.
- **Navigasi Cepat (Quick Actions)**: Pilihan mudah menuju Data Siswa, Pembayaran, Pengeluaran, Mata Pelajaran, dan Laporan Kas.

### 👥 2. Data Siswa 33 Anggota Real
- Tabel scrollable interaktif menampilkan 33 siswa kelas XII PPLG lengkap dengan peran (Ketua Kelas, Bendahara, Sekretaris, Wakil Ketua, Anggota).
- Status iuran bulanan (**Juli 2026** & **Agustus 2026**) dengan indikator visual `✓` (Lunas / Hijau) dan `✗` (Belum / Merah).
- Ilustrasi buku custom menggunakan Compose `Canvas`.

### 📜 3. Riwayat Pembayaran Kas
- Tabel riwayat transaksi dengan sistem **paginasi** (halaman 1 dari 10).
- Pencatatan nama siswa, tanggal transaksi, dan nominal secara terperinci.

### 💳 4. Pembayaran Multi-Channel & QRIS
- Pilihan metode pembayaran modern menggunakan gambar icon PNG asli:
  - **QRIS**: Merchant resmi.
  - **E-Wallets**: DANA, GoPay, OVO.
  - **Bank Transfer**: BCA, Mandiri, BNI.
  - **Tunai**: Cash.
- Tombol hijau menonjol **PEMBAYARAN**.

### 💰 5. Pengeluaran Kas Kelas
- Form pencatatan pengeluaran kas dengan field Tanggal (DD/MM/YYYY), Nominal Jumlah (Rp), dan Keterangan.
- Tombol kirim dengan gradien biru elegan.

### 📈 6. Laporan Kas & Grafik Trend
- Ringkasan **Total Pemasukan**, **Total Pengeluaran**, dan **Total Akhir**.
- **Grafik Garis (Line Chart)** interaktif berbasis `Canvas` yang membandingkan tren Pemasukan (garis biru) vs Pengeluaran (garis abu-abu).

### 🎉 7. Perayaan Pembayaran Berhasil
- Halaman konfirmasi pembayaran sukses lengkap dengan ilustrasi **SpongeBob**, animasi confetti melayang, rincian nominal, dan atas nama pembayaran.

---

## 🏛️ Arsitektur Aplikasi & Design System

Aplikasi ini menggunakan arsitektur **MVVM (Model-View-ViewModel)** berbasis **Unidirectional Data Flow (UDF)**:

```
 ┌─────────────────────────────────────────────────────────────┐
 │                       UI Layer (Compose)                    │
 │ DashboardScreen, DataSiswaScreen, PembayaranScreen, dst.    │
 └──────────────────────────────▲──────────────────────────────┘
                                │ StateFlow<List<...>>
                                │ User Actions (login, addTx)
 ┌──────────────────────────────┴──────────────────────────────┐
 │                     KastepViewModel                         │
 │        Exposes StateFlows & Delegates to Repository         │
 └──────────────────────────────▲──────────────────────────────┘
                                │ Flow Data
 ┌──────────────────────────────┴──────────────────────────────┐
 │                     KastepRepository                        │
 │     SharedPreferences Persistence Engine & Initial Data      │
 └─────────────────────────────────────────────────────────────┘
```

### Design System Tokens
- **Background**: Pure Black (`#000000`)
- **Primary Accent**: Cyan (`#00D4FF`) & Blue (`#1565C0`)
- **Gradient**: `GradientBlueStart` (`#0052D4`) → `GradientBlueEnd` (`#4364F7`)
- **Status Colors**: Success Green (`#4CAF50`) & Expense Red (`#F44336`)
- **Card Backgrounds**: Dark Surface (`#1A1A2E`, `#1E1E1E`, `#2B2B2B`)

---

## 📂 Struktur Direktori Proyek

```
kastep/
├── app/
│   ├── build.gradle.kts                # Gradle script dependensi Compose & Material3
│   └── src/
│       └── main/
│           ├── AndroidManifest.xml     # Manifest aplikasi
│           ├── res/
│           │   ├── drawable-xxhdpi/    # Icon PNG (bca, bni, dana, gopay, mandiri, ovo, qris, spongebob)
│           │   └── values/
│           └── java/com/kastep/app/
│               ├── MainActivity.kt     # Entry Point & ModalNavigationDrawer
│               ├── data/
│               │   ├── KastepModels.kt     # Siswa, Transaction, PaymentRecord, MataPelajaran, StatusBayar
│               │   ├── KastepRepository.kt # SharedPreferences storage engine
│               │   └── KastepViewModel.kt  # State management & business logic
│               ├── ui/
│               │   ├── navigation/
│               │   │   ├── Screen.kt       # Sealed class routes
│               │   │   └── NavGraph.kt     # Setup Jetpack Navigation
│               │   ├── screens/
│               │   │   ├── SplashScreen.kt
│               │   │   ├── LoginScreen.kt
│               │   │   ├── RegisterScreen.kt
│               │   │   ├── DashboardScreen.kt
│               │   │   ├── DataSiswaScreen.kt
│               │   │   ├── RiwayatPembayaranScreen.kt
│               │   │   ├── PembayaranScreen.kt
│               │   │   ├── MataPelajaranScreen.kt
│               │   │   ├── PengeluaranKasScreen.kt
│               │   │   ├── LaporanKasScreen.kt
│               │   │   ├── PembayaranBerhasilScreen.kt
│               │   │   └── ProfileScreen.kt
│               │   └── theme/
│               │       ├── Color.kt
│               │       ├── Theme.kt
│               │       └── Type.kt
├── KASTEP.apk                          # Output APK Debug Siap Install
└── build.gradle.kts                    # Root build configuration
```

---

## 💻 Panduan Instalasi Lokal & Build APK

### Prasyarat Sistem
- **Java Development Kit (JDK)**: OpenJDK 21.
- **Android SDK**: API Level 34.
- **Gradle**: Gradle Wrapper (`./gradlew`).

### 1. Navigasi ke Folder Project
```bash
cd /home/wira/Documents/kastep
```

### 2. Atur Environment JDK 21
```bash
export JAVA_HOME=/usr/lib/jvm/java-21-openjdk-amd64
```

### 3. Kompilasi APK Debug
```bash
JAVA_HOME=/usr/lib/jvm/java-21-openjdk-amd64 ./gradlew assembleDebug
```

Output APK berlokasi di:
`app/build/outputs/apk/debug/app-debug.apk`

---

## 📊 Ringkasan Data Initial 33 Anggota

| No | Nama Siswa | Peran | Status Juli 2026 | Status Agustus 2026 |
|---|---|---|:---:|:---:|
| 1 | BOYKE VILANO HAMONANGAN SIHITE | Ketua Kelas | Lunas (V) | Lunas (V) |
| 2 | BINTANG LEONITA CHRISTYA RENATA | Bendahara | Lunas (V) | Lunas (V) |
| 3 | CAROLINA TIMUTHY JANGGUR | Sekretaris | Lunas (V) | Lunas (V) |
| 4 | DEWA GEDE DALEM OKA ADNYANA SANDI | Wakil Ketua | Lunas (V) | Lunas (V) |
| 5 | GALISTAN RAMADHAN KURNIA TAUNAES | Anggota | Lunas (V) | Lunas (V) |
| 6 | GEDE AGUS WIRA DARMA PUTRA | Anggota | Lunas (V) | Lunas (V) |
| 7 | I GEDE ABI WIRYA DINATA | Anggota | Lunas (V) | Lunas (V) |
| 8 | I GEDE DARMA SUPTIAWAN | Anggota | Lunas (V) | Lunas (V) |
| 9 | I KOMANG RADITYA PUTRA | Anggota | Lunas (V) | Lunas (V) |
| 10 | I KOMANG RISKI SETIAWAN | Anggota | Lunas (V) | Lunas (V) |
| 11 | I NYOMAN GEDE ARTA WIGUNA | Anggota | Lunas (V) | Lunas (V) |
| 12 | I PUTU DIKA LAKSMANA PUTRA | Anggota | Lunas (V) | Lunas (V) |
| 13 | I PUTU DITYA ARTHA WIJAYA | Anggota | Lunas (V) | Lunas (V) |
| 14 | I PUTU PANDE ANDIKA | Anggota | Lunas (V) | Lunas (V) |
| 15 | I PUTU SUYOGA MAHENDRA | Anggota | Lunas (V) | Lunas (V) |
| 16 | I WAYAN BAGUS PUTRAWAN | Anggota | Lunas (V) | Lunas (V) |
| 17 | I WAYAN PASEK KEVIN ARIADI | Anggota | Lunas (V) | Lunas (V) |
| 18 | KADEK YUDA PRASETYA | Anggota | Lunas (V) | Lunas (V) |
| 19 | KADEK YUNI CALLISTA PUTRI DEWI | Anggota | Lunas (V) | Lunas (V) |
| 20 | KOMANG DIAH PUTRI PRATIWI | Anggota | Lunas (V) | Lunas (V) |
| 21 | LUH RIA MIRASIH | Anggota | Lunas (V) | Lunas (V) |
| 22 | NI KADEK ADELIA CAHYA KENCANA PUTRI | Anggota | Lunas (V) | Lunas (V) |
| 23 | NI KADEK LINA ANTIKA DEWI | Anggota | Lunas (V) | Belum (X) |
| 24 | NI KOMANG KIRANA PARAMITA ARDANARI | Anggota | Lunas (V) | Belum (X) |
| 25 | NI KOMANG SEPTIARINI | Anggota | Lunas (V) | Belum (X) |
| 26 | NI LUH PUTU KESYA ASTRI MELANI | Anggota | Lunas (V) | Belum (X) |
| 27 | NI PUTU CAHAYA LESTARI DEWI | Anggota | Lunas (V) | Belum (X) |
| 28 | NI PUTU INTAN LESTARI DARMAYANTI | Anggota | Lunas (V) | Belum (X) |
| 29 | OKTA PRADIPTA ATTALA DZAKI | Anggota | Belum (X) | Belum (X) |
| 30 | PUTU BAYU SATRIA WANGSA BUKIAN | Anggota | Belum (X) | Belum (X) |
| 31 | PUTU NANDA LINDIA MAHARANI | Anggota | Belum (X) | Belum (X) |
| 32 | PUTU PUTRI CAHYANI | Anggota | Belum (X) | Belum (X) |
| 33 | RADITYA RONDI | Anggota | Belum (X) | Belum (X) |

---

## 👨‍💻 Developer & Kontributor

Dikembangkan dengan 💙 oleh Tim Pengembang **KASTEP App** untuk kelas **XII PPLG**.  
Bantuan & Dukungan Teknis Direct WhatsApp: **[+62 812-3720-1227](https://wa.me/6281237201227)**.
