# Tanya Jawab Kode KASTEP – Kelompok 1

Persiapan menjawab pertanyaan guru. Setiap jawaban diberi letak kodenya (file:baris). Di Android Studio tekan **Ctrl+Shift+N** lalu ketik nama file, atau **Ctrl+Shift+F** lalu ketik `TANYA-GURU`.

### 1. Aplikasi ini dibuat dengan apa?

Kotlin 2.0 dan Jetpack Compose Material 3 di Android Studio, Gradle JDK 21. Android 7.0 sampai 14.

Letak: `app/build.gradle.kts:14`

### 2. Kode pertama yang jalan saat aplikasi dibuka?

`MainActivity.onCreate` memasang tema lalu `KastepApp` (drawer + NavGraph). Splash 2,5 detik lalu ke Login atau Dashboard.

Letak: `app/src/main/java/com/kastep/app/MainActivity.kt:78`

### 3. Bagaimana validasi login?

Email harus ada @ dan titik, password minimal 5 karakter, lalu dicocokkan dengan akun terdaftar.

Letak: `app/src/main/java/com/kastep/app/data/KastepRepository.kt:164`

### 4. Akun admin dan user bedanya?

Register selalu membuat USER. Admin bawaan untuk bendahara. Tombol tambah, ubah, hapus hanya muncul untuk ADMIN.

Letak: `app/src/main/java/com/kastep/app/data/KastepRepository.kt:185`

### 5. Data disimpan di mana?

File kastep_prefs di HP (SharedPreferences, JSON). Kalau rusak, data awal buku kas dimuat lagi.

Letak: `app/src/main/java/com/kastep/app/data/KastepRepository.kt:826`

### 6. Rumus saldo?

Total semua pembayaran (kas + donasi) - total pengeluaran = Rp1.389.000.

Letak: `app/src/main/java/com/kastep/app/data/KastepViewModel.kt:51`

### 7. Rumus denda?

Rp5.000 x n, n = bulan sekarang - bulan tagihan, hanya bulan yang belum LUNAS. Bulan berjalan belum didenda.

Letak: `app/src/main/java/com/kastep/app/data/KastepRepository.kt:322`

### 8. Tagihan satu bulan dihitung bagaimana?

Jumlah Senin bulan itu x Rp5.000. Kekurangan = (Senin - minggu lunas - minggu dispensasi) x Rp5.000.

Letak: `app/src/main/java/com/kastep/app/data/KastepRepository.kt:299`

### 9. Jumlah Senin dihitung dari mana?

Fungsi `jumlahMinggu` menghitung hari Senin di kalender bulan itu (4 atau 5).

Letak: `app/src/main/java/com/kastep/app/data/KastepRepository.kt:110`

### 10. Siapa yang membuat status jadi LUNAS?

Hanya bendahara (Admin). Siswa transfer lalu menekan Kirim Konfirmasi ke Bendahara, WhatsApp bendahara terbuka, status belum berubah. Bendahara mengecek uangnya, lalu di layar Pembayaran memilih siswa, dari bulan dan sampai bulan, dan menekan Catat Pembayaran. `processPayment` menolak akun selain Admin.

Letak: `app/src/main/java/com/kastep/app/data/KastepRepository.kt:343`

### 11. Bagaimana bendahara mencatat beberapa bulan sekaligus?

Pilih Dari bulan dan Sampai bulan. `processPaymentRange` mencatat tiap bulan yang belum lunas di rentang itu sebagai satu baris riwayat. Bulan yang sudah lunas dilewati.

Letak: `app/src/main/java/com/kastep/app/data/KastepRepository.kt:374`

### 12. Kenapa bulan yang lunas tidak bisa dipilih?

Dicek `isLunas` di dialog pembayaran supaya tidak bayar dua kali.

Letak: `app/src/main/java/com/kastep/app/ui/screens/PembayaranScreen.kt:253`

### 13. Dispensasi BI/PKL di mana?

Peta minggu dispensasi per siswa per bulan di data awal. Minggu itu dihitung lunas tanpa menambah pemasukan.

Letak: `app/src/main/java/com/kastep/app/data/KastepRepository.kt:519`

### 14. Kalau nominal kas atau denda mau diubah?

Konstanta KAS_MINGGUAN dan DENDA_PER_BULAN di companion object.

Letak: `app/src/main/java/com/kastep/app/data/KastepRepository.kt:80`

### 15. Bagaimana konfirmasi WhatsApp bekerja?

Membuka wa.me dengan pesan berisi nama, bulan, metode, kas, denda, total.

Letak: `app/src/main/java/com/kastep/app/ui/screens/PembayaranScreen.kt:460`

### 16. Nomor rekening dan QRIS di mana?

Daftar nomor per metode dan gambar QRIS di PembayaranScreen.

Letak: `app/src/main/java/com/kastep/app/ui/screens/PembayaranScreen.kt:149`

### 17. Validasi pengeluaran?

Nominal harus lebih dari 0, keterangan wajib, tanggal harus ada di kalender (31/02 ditolak).

Letak: `app/src/main/java/com/kastep/app/data/KastepRepository.kt:393`

### 18. Laporan dan grafik dari mana?

Laporan per periode dan grafik batang 6 bulan, tombol Bagikan memakai `generateReportText` (tanpa nama).

Letak: `app/src/main/java/com/kastep/app/data/KastepRepository.kt:451`

### 19. Kenapa user tidak melihat status teman?

Di Data Siswa, USER hanya melihat baris namanya sendiri. Nama yang belum lunas di Laporan hanya untuk Admin.

Letak: `app/src/main/java/com/kastep/app/ui/screens/DataSiswaScreen.kt:135`

### 20. Apa itu ViewModel di sini?

`KastepViewModel` jembatan layar dan repository, menyimpan StateFlow supaya layar ikut berubah otomatis (pola MVVM).

Letak: `app/src/main/java/com/kastep/app/data/KastepViewModel.kt:27`

### 21. Menu dan perpindahan halaman diatur di mana?

Isi menu samping di MainActivity, rute halaman di NavGraph.

Letak: `app/src/main/java/com/kastep/app/ui/navigation/NavGraph.kt:38`

### 22. Tabel ERD ada di kode mana?

Data class di KastepModels.kt: UserProfile, Siswa, PaymentRecord, Pengeluaran, MataPelajaran.

Letak: `app/src/main/java/com/kastep/app/data/KastepModels.kt:47`

### 23. Apa kekurangan aplikasi?

Belum ada koreksi atau hapus pembayaran yang salah catat, belum ada batas denda, data belum online. Lihat SRS 5.2.

Letak: `app/src/main/java/com/kastep/app/data/KastepRepository.kt:343`

### 24. Grafiknya di mana dan jenis grafik apa?

Satu grafik di layar Laporan Kas: grafik batang berpasangan (biru pemasukan, abu pengeluaran) per bulan, fungsi `IncomeExpenseChart`. Digambar manual pakai Canvas, tanpa library. Dashboard tidak punya grafik.

Letak: `app/src/main/java/com/kastep/app/ui/screens/LaporanKasScreen.kt:353`

### 25. Grafik menampilkan bulan apa saja?

Enam bulan terakhir yang punya pembayaran atau pengeluaran (`bulanGrafik`: distinct, urut, takeLast 6).

Letak: `app/src/main/java/com/kastep/app/ui/screens/LaporanKasScreen.kt:207`

### 26. Apakah pilihan Periode mengubah grafik?

Tidak. Periode hanya mengubah dua kartu angka dan kartu saldo atau selisih. Grafik selalu 6 bulan terakhir.

Letak: `app/src/main/java/com/kastep/app/ui/screens/LaporanKasScreen.kt:80`

### 27. Bagaimana grafik batang dihitung?

Tinggi batang = tinggi area x nilai / nilai terbesar. Ada 5 garis bantu berlabel ribuan (mis. 20rb).

Letak: `app/src/main/java/com/kastep/app/ui/screens/LaporanKasScreen.kt:375`

### 28. Menu aplikasi pakai apa, tab bawah?

Menu samping (ModalNavigationDrawer) berisi 7 menu, bukan tab bawah. Isinya di `drawerItems`.

Letak: `app/src/main/java/com/kastep/app/MainActivity.kt:128`

### 29. Dashboard isinya apa?

Tiga kartu (saldo, pemasukan, pengeluaran), garis progres pengeluaran per pemasukan, dan 10 transaksi terbaru gabungan.

Letak: `app/src/main/java/com/kastep/app/ui/screens/DashboardScreen.kt:58`

### 30. Alur membayar kas bagaimana?

Siswa: pilih metode, tekan PEMBAYARAN, pilih dari bulan dan sampai bulan, total otomatis (sisa kas + denda), Kirim Konfirmasi ke Bendahara lewat WA. Bendahara: pilih metode, siswa, dari bulan dan sampai bulan, Catat Pembayaran, layar Berhasil, tombol Konfirmasi WA untuk meneruskan bukti.

Letak: `app/src/main/java/com/kastep/app/ui/screens/PembayaranScreen.kt:242`

### 31. Nomor rekening di layar Pembayaran asli?

Masih nomor contoh (variabel accNo). Harus diganti dengan rekening kelas yang sebenarnya sebelum dipakai.

Letak: `app/src/main/java/com/kastep/app/ui/screens/PembayaranScreen.kt:150`

### 32. Kenapa bisa bayar dua kali?

Tidak bisa. Bulan yang sudah LUNAS di dalam rentang dilewati dan processPayment menolaknya.

Letak: `app/src/main/java/com/kastep/app/data/KastepRepository.kt:350`

### 33. Bagaimana halaman berpindah?

NavGraph.kt: setiap composable(Screen.X.route) satu halaman. Daftar rute di Screen.kt.

Letak: `app/src/main/java/com/kastep/app/ui/navigation/NavGraph.kt:40`

### 34. Jadwal pelajaran dari mana?

Data tetap di MataPelajaranScreen.kt (ScheduleEntry), tidak terhubung dengan kas.

Letak: `app/src/main/java/com/kastep/app/ui/screens/MataPelajaranScreen.kt:90`
