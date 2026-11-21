# Tanya Jawab Kode KASTEP – Kelompok 1

Persiapan menjawab pertanyaan guru. Setiap jawaban diberi letak kodenya (file:baris). Di Android Studio tekan **Ctrl+Shift+N** lalu ketik nama file, atau **Ctrl+Shift+F** lalu ketik `TANYA-GURU`.

### 1. Aplikasi ini dibuat dengan apa?

Kotlin 2.0 dan Jetpack Compose Material 3 di Android Studio, Gradle JDK 21. Android 7.0 sampai 14.

Letak: `app/build.gradle.kts:14`

### 2. Kode pertama yang jalan saat aplikasi dibuka?

`MainActivity.onCreate` memasang tema lalu `KastepApp` (drawer + NavGraph). Splash 2,5 detik lalu ke Login atau Dashboard.

Letak: `app/src/main/java/com/kastep/app/MainActivity.kt:70`

### 3. Bagaimana validasi login?

Email harus ada @ dan titik, password minimal 5 karakter, lalu dicocokkan dengan akun terdaftar.

Letak: `app/src/main/java/com/kastep/app/data/KastepRepository.kt:129`

### 4. Akun admin dan user bedanya?

Register selalu membuat USER. Admin bawaan untuk bendahara. Tombol tambah, ubah, hapus hanya muncul untuk ADMIN.

Letak: `app/src/main/java/com/kastep/app/data/KastepRepository.kt:149`

### 5. Data disimpan di mana?

File kastep_prefs di HP (SharedPreferences, JSON). Kalau rusak, data awal buku kas dimuat lagi.

Letak: `app/src/main/java/com/kastep/app/data/KastepRepository.kt:737`

### 6. Rumus saldo?

Total semua pembayaran (kas + donasi) - total pengeluaran = Rp1.389.000.

Letak: `app/src/main/java/com/kastep/app/data/KastepViewModel.kt:36`

### 7. Rumus denda?

Rp5.000 x n, n = bulan sekarang - bulan tagihan, hanya bulan yang belum LUNAS. Bulan berjalan belum didenda.

Letak: `app/src/main/java/com/kastep/app/data/KastepRepository.kt:274`

### 8. Tagihan satu bulan dihitung bagaimana?

Jumlah Senin bulan itu x Rp5.000. Kekurangan = (Senin - minggu lunas - minggu dispensasi) x Rp5.000.

Letak: `app/src/main/java/com/kastep/app/data/KastepRepository.kt:255`

### 9. Jumlah Senin dihitung dari mana?

Fungsi `jumlahMinggu` menghitung hari Senin di kalender bulan itu (4 atau 5).

Letak: `app/src/main/java/com/kastep/app/data/KastepRepository.kt:82`

### 10. Apa yang terjadi saat Bayar Sekarang ditekan?

Total = sisa + denda disimpan ke riwayat, bulan itu jadi LUNAS, lalu muncul halaman Pembayaran Berhasil.

Letak: `app/src/main/java/com/kastep/app/data/KastepRepository.kt:286`

### 11. Kenapa bulan yang lunas tidak bisa dipilih?

Dicek `isLunas` di dialog pembayaran supaya tidak bayar dua kali.

Letak: `app/src/main/java/com/kastep/app/ui/screens/PembayaranScreen.kt:212`

### 12. Dispensasi BI/PKL di mana?

Peta minggu dispensasi per siswa per bulan di data awal. Minggu itu dihitung lunas tanpa menambah pemasukan.

Letak: `app/src/main/java/com/kastep/app/data/KastepRepository.kt:432`

### 13. Kalau nominal kas atau denda mau diubah?

Konstanta KAS_MINGGUAN dan DENDA_PER_BULAN di companion object.

Letak: `app/src/main/java/com/kastep/app/data/KastepRepository.kt:55`

### 14. Bagaimana konfirmasi WhatsApp bekerja?

Membuka wa.me dengan pesan berisi nama, bulan, metode, kas, denda, total.

Letak: `app/src/main/java/com/kastep/app/ui/screens/PembayaranScreen.kt:364`

### 15. Nomor rekening dan QRIS di mana?

Daftar nomor per metode dan gambar QRIS di PembayaranScreen.

Letak: `app/src/main/java/com/kastep/app/ui/screens/PembayaranScreen.kt:126`

### 16. Validasi pengeluaran?

Nominal harus lebih dari 0, keterangan wajib, tanggal harus ada di kalender (31/02 ditolak).

Letak: `app/src/main/java/com/kastep/app/data/KastepRepository.kt:315`

### 17. Laporan dan grafik dari mana?

Laporan per periode dan grafik batang 6 bulan, tombol Bagikan memakai `generateReportText` (tanpa nama).

Letak: `app/src/main/java/com/kastep/app/data/KastepRepository.kt:368`

### 18. Kenapa user tidak melihat status teman?

Di Data Siswa, USER hanya melihat baris namanya sendiri. Nama yang belum lunas di Laporan hanya untuk Admin.

Letak: `app/src/main/java/com/kastep/app/ui/screens/DataSiswaScreen.kt:126`

### 19. Apa itu ViewModel di sini?

`KastepViewModel` jembatan layar dan repository, menyimpan StateFlow supaya layar ikut berubah otomatis (pola MVVM).

Letak: `app/src/main/java/com/kastep/app/data/KastepViewModel.kt:17`

### 20. Menu dan perpindahan halaman diatur di mana?

Isi menu samping di MainActivity, rute halaman di NavGraph.

Letak: `app/src/main/java/com/kastep/app/ui/navigation/NavGraph.kt:32`

### 21. Tabel ERD ada di kode mana?

Data class di KastepModels.kt: UserProfile, Siswa, PaymentRecord, Pengeluaran, MataPelajaran.

Letak: `app/src/main/java/com/kastep/app/data/KastepModels.kt:35`

### 22. Apa kekurangan aplikasi?

Status langsung LUNAS tanpa verifikasi admin, belum ada koreksi pembayaran, belum ada batas denda, data belum online. Lihat SRS 5.2.

Letak: `app/src/main/java/com/kastep/app/data/KastepRepository.kt:286`
