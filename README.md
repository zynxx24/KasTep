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
- [❓ Pertanyaan Umum & FAQ (QnA)](#-pertanyaan-umum--faq-qna)
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

### 🔒 Keamanan & Perizinan (Permissions)
- **Zero Runtime Permissions**: Menggunakan internal sandboxed storage Android, sehingga aplikasi **tidak memerlukan konfirmasi izin file/storage** dari pengguna.

---

## ❓ Pertanyaan Umum & FAQ (QnA)

### Q1: Di mana data aplikasi ini disimpan?
> **Jawaban**: Data disimpan secara internal di memori lokal perangkat (HP) melalui Android `SharedPreferences` dalam format serialized JSON di file `/data/data/com.kastep.app/shared_prefs/kastep_prefs.xml`.

### Q2: Apa yang terjadi jika data aplikasi atau cache dihapus di Pengaturan HP?
> **Jawaban**: Jika pengguna melakukan **"Hapus Data / Clear Storage"** pada aplikasi di menu Pengaturan Android, maka seluruh data yang tersimpan akan terhapus dan aplikasi akan kembali ke **kondisi awal (reset dari 0 / data initial 33 siswa standar)**.

### Q3: Berapa akun Administrator bawaan dan bagaimana cara masuk sebagai Admin?
> **Jawaban**: Terdapat akun Administrator default yang dapat digunakan untuk login:
> - **Email Admin**: `admin@gmail.com`
> - **Password Admin**: `admin123`

### Q4: Bagaimana cara pendaftaran akun baru (User)?
> **Jawaban**: Pengguna baru dapat menekan tombol **Register** pada halaman Login, lalu mengisi Nama, Email, NIS, Kelas, No HP, dan Password. Akun baru yang terdaftar akan otomatis memiliki role `User`.

### Q5: Berapa nominal iuran kas per bulan dan apa saja metode pembayarannya?
> **Jawaban**: Nominal iuran kas ditetapkan **Rp 20.000 / bulan**. Pilihan metode pembayaran mencakup:
> - **QRIS**: Merchant resmi KAS XII PPLG.
> - **E-Wallet**: DANA, GoPay, OVO (`0895-2037-1942`).
> - **Bank Transfer**: BCA (`7340-5812-9076`), Mandiri (`1280-0045-6789-012`), BNI (`0912-3456-7890`).
> - **CASH (Tunai)**: Pembayaran tunai langsung ke Bendahara.

### Q6: Bagaimana sistem konfirmasi otomatis ke WhatsApp bekerja?
> **Jawaban**: Setelah proses simulasi pembayaran selesai (baik CASH, Transfer, maupun QRIS), aplikasi secara otomatis membuka WhatsApp dan menyusun pesan konfirmasi ke nomor **`+62 895-2037-1942`** berisi rincian: Nama Siswa, Bulan Pembayaran, Nominal (Rp 20.000), dan Metode Pembayaran.

### Q7: Apakah data siswa dan pengeluaran kas dapat ditambah, di-edit, atau dihapus?
> **Jawaban**: **Ya**. 
> - **Data Siswa**: Pada menu *Data Siswa*, Anda dapat menambah siswa baru, menekan baris siswa untuk mengedit nama/peran, serta menghapus siswa dengan re-numbering otomatis.
> - **Pengeluaran Kas**: Pada menu *Pengeluaran Kas*, Anda dapat menambah pengeluaran baru, melihat riwayat pengeluaran, serta mengedit atau menghapus entri pengeluaran yang pernah dicatat.

### Q8: Mengapa kartu pada Laporan Kas bisa diklik?
> **Jawaban**: Kartu laporan kas dirancang interaktif:
> - **Periode Dropdown**: Bisa diklik untuk memfilter laporan berdasarkan *Juli 2026*, *Agustus 2026*, atau *Semua Periode*.
> - **Total Pemasukan**: Jika diklik, akan memunculkan dialog rincian **daftar siswa yang Lunas (`✓`) dan Belum Lunas (`✗`)**.
> - **Total Pengeluaran**: Jika diklik, akan memunculkan dialog rincian **seluruh item pengeluaran kas beserta nominalnya**.

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

### Palette Color Tokens:
- **Background**: Pure Black (`#000000`)
- **Accent Primary**: Cyan (`#00D4FF`) & Blue (`#4A90D9`)
- **Status Success**: Green (`#4CAF50`)
- **Status Warning/Expense**: Red (`#EF5350`)
- **Surface Cards**: Dark Navy (`#1A1A2E`, `#16213E`)

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

## 📊 Data Initial 33 Siswa XII PPLG

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
Bantuan & Dukungan Teknis Direct WhatsApp: **[+62 895-2037-1942](https://wa.me/6289520371942)**.
