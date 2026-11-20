# Peta Kode KASTEP – Kelompok 1

Panduan mencari bagian penting kode KASTEP dengan cepat. Daftar di bawah dibuat otomatis dari komentar `CARI-…` di kode.

## Cara mencari

1. Buka folder proyek di VS Code / Android Studio.
2. Tekan **Ctrl+Shift+F** (cari di semua file).
3. Ketik **`CARI-`** untuk melihat semua titik penting, atau langsung penanda tertentu, misalnya **`CARI-DENDA`**.
4. Setiap file utama juga punya kotak **`PETA FILE`** di baris paling atas yang menjelaskan isi file itu.

## Daftar penanda

| Penanda | Artinya | Jumlah titik |
|---|---|---|
| `CARI-MULAI` | Titik awal aplikasi (Activity, Application, splash) | 2 |
| `CARI-NAVIGASI` | Menu, tab, drawer, dan perpindahan halaman | 4 |
| `CARI-LOGIN` | Login, logout, akun admin, sesi | 6 |
| `CARI-REGISTER` | Pendaftaran akun baru | 2 |
| `CARI-PUSAT-DATA` | Pusat data aplikasi (repository / viewmodel) | 2 |
| `CARI-DATA-AWAL` | Data awal dari buku kas asli (siswa, pembayaran, pengeluaran) | 2 |
| `CARI-SIMPAN-DATA` | Penyimpanan permanen di HP (SharedPreferences) | 3 |
| `CARI-NOMINAL-KAS` | Nominal kas per minggu/bulan dan cara menghitung minggu | 2 |
| `CARI-SALDO` | Rumus dan tampilan saldo | 4 |
| `CARI-DENDA` | Rumus dan tampilan denda keterlambatan | 5 |
| `CARI-DISPENSASI` | Siswa yang dibebaskan kas (BI/PKL) | 3 |
| `CARI-PEMBAYARAN` | Mencatat / menampilkan pembayaran kas | 9 |
| `CARI-PENGELUARAN` | Mencatat / menampilkan pengeluaran kas | 5 |
| `CARI-ANGGOTA` | Data siswa / anggota kelas | 5 |
| `CARI-LAPORAN` | Laporan dan statistik kas | 2 |
| `CARI-GRAFIK` | Grafik pemasukan & pengeluaran | 1 |
| `CARI-PROFIL` | Halaman dan edit profil | 3 |
| `CARI-QRIS` | Kode QRIS dan rekening tujuan transfer | 2 |
| `CARI-WHATSAPP` | Pesan / konfirmasi lewat WhatsApp | 2 |
| `CARI-VALIDASI` | Pengecekan isian form (tanggal, nominal, nama) | 7 |
| `CARI-JADWAL` | Jadwal mata pelajaran | 2 |
| `CARI-FORMAT-RUPIAH` | Format angka ke Rupiah | 1 |
| `CARI-TEMA` | Warna dan tema tampilan | 1 |

## Lokasi lengkap

### `CARI-MULAI`

- `app/src/main/java/com/kastep/app/MainActivity.kt:64` — Aplikasi dibuka dari sini: membuat KastepViewModel lalu menampilkan KastepApp.
- `app/src/main/java/com/kastep/app/ui/screens/SplashScreen.kt:49` — Tampil 2,5 detik lalu pindah ke Login atau Dashboard (diatur di NavGraph).

### `CARI-NAVIGASI`

- `app/src/main/java/com/kastep/app/MainActivity.kt:114` — Isi menu samping. Tambah menu = DrawerMenuItem(label, ikon, Screen.X.route) + daftarkan di NavGraph.
- `app/src/main/java/com/kastep/app/ui/screens/DataSiswaScreen.kt:290` — Bar atas yang dipakai banyak halaman: tombol menu, tanggal, dan tombol profil.
- `app/src/main/java/com/kastep/app/ui/navigation/NavGraph.kt:32` — Setiap composable(Screen.X.route) = satu halaman. Splash langsung ke Dashboard bila sesi login masih aktif.
- `app/src/main/java/com/kastep/app/ui/navigation/Screen.kt:10` — Tambah halaman baru: object NamaHalaman : Screen("nama_rute").

### `CARI-LOGIN`

- `app/src/main/java/com/kastep/app/ui/screens/ProfileScreen.kt:63` — Tombol Simpan di Edit Akun memanggil viewModel.updateProfile; tombol Keluar memanggil logout.
- `app/src/main/java/com/kastep/app/ui/screens/LoginScreen.kt:70` — Form email & password; pesan error dari KastepRepository.login.
- `app/src/main/java/com/kastep/app/ui/navigation/NavGraph.kt:32` — Setiap composable(Screen.X.route) = satu halaman. Splash langsung ke Dashboard bila sesi login masih aktif.
- `app/src/main/java/com/kastep/app/data/KastepModels.kt:14` — Data akun yang sedang login (role ADMIN atau USER).
- `app/src/main/java/com/kastep/app/data/KastepRepository.kt:65` — Akun admin bawaan: admin@gmail.com / admin123.
- `app/src/main/java/com/kastep/app/data/KastepRepository.kt:126` — Cek format email, panjang password (min. 5), lalu cocokkan dengan akun terdaftar.

### `CARI-REGISTER`

- `app/src/main/java/com/kastep/app/ui/screens/RegisterScreen.kt:60` — Form nama, email, NIS, kelas, No. HP, password; link kembali ke Login di bawah.
- `app/src/main/java/com/kastep/app/data/KastepRepository.kt:146` — Akun baru selalu ber-role USER; email tidak boleh dobel.

### `CARI-PUSAT-DATA`

- `app/src/main/java/com/kastep/app/data/KastepViewModel.kt:16` — Layar memanggil fungsi di sini; isi logikanya ada di KastepRepository.
- `app/src/main/java/com/kastep/app/data/KastepRepository.kt:22` — Semua perubahan data lewat sini; layar membaca lewat KastepViewModel.

### `CARI-DATA-AWAL`

- `app/src/main/java/com/kastep/app/data/KastepRepository.kt:335` — Mengembalikan semua data ke buku kas awal (sesi admin tetap).
- `app/src/main/java/com/kastep/app/data/KastepRepository.kt:381` — Data awal dari buku kas asli XII PPLG per 20 November 2026: siswa, minggu terbayar, riwayat, pengeluaran.

### `CARI-SIMPAN-DATA`

- `app/src/main/java/com/kastep/app/data/KastepRepository.kt:58` — Kunci penyimpanan. Naikkan versinya bila data awal diganti supaya HP memuat ulang.
- `app/src/main/java/com/kastep/app/data/KastepRepository.kt:718` — Menyimpan semua data ke HP (JSON di SharedPreferences).
- `app/src/main/java/com/kastep/app/data/KastepRepository.kt:757` — Memuat data tersimpan; bila rusak kembali ke data awal.

### `CARI-NOMINAL-KAS`

- `app/src/main/java/com/kastep/app/data/KastepRepository.kt:54` — Iuran per minggu (setiap Senin). Tagihan satu bulan = jumlah Senin x nilai ini.
- `app/src/main/java/com/kastep/app/data/KastepRepository.kt:80` — Menghitung jumlah hari Senin dalam sebulan (4 atau 5) = jumlah minggu kas.

### `CARI-SALDO`

- `app/src/main/java/com/kastep/app/ui/screens/LaporanKasScreen.kt:42` — Pemasukan & pengeluaran mengikuti periode yang dipilih; Semua Periode menampilkan saldo akhir.
- `app/src/main/java/com/kastep/app/ui/screens/DashboardScreen.kt:34` — Tiga kartu ringkasan + 10 transaksi terbaru (diurutkan menurut tanggal).
- `app/src/main/java/com/kastep/app/data/KastepViewModel.kt:34` — Saldo = total pemasukan - total pengeluaran (dipakai Dashboard).
- `app/src/main/java/com/kastep/app/data/KastepRepository.kt:354` — Teks laporan ringkas untuk tombol Bagikan Laporan (saldo = pemasukan - pengeluaran).

### `CARI-DENDA`

- `app/src/main/java/com/kastep/app/ui/screens/DataSiswaScreen.kt:41` — Kolom bulan: ✓ lunas, angka = minggu terbayar, D = dispensasi, ✗ belum. Kolom terakhir = total denda.
- `app/src/main/java/com/kastep/app/ui/screens/PembayaranScreen.kt:195` — Total bayar = sisa minggu x Rp5.000 + denda. Bulan yang sudah lunas tidak bisa dipilih.
- `app/src/main/java/com/kastep/app/data/KastepRepository.kt:56` — Besar denda untuk setiap bulan keterlambatan. Ubah angka ini untuk mengganti tarif.
- `app/src/main/java/com/kastep/app/data/KastepRepository.kt:260` — Rumus: Rp5.000 x jumlah bulan terlambat, hanya untuk bulan yang belum lunas.
- `app/src/main/java/com/kastep/app/data/KastepRepository.kt:274` — Mencatat pembayaran: jumlah = sisa kas + denda, lalu bulan itu menjadi LUNAS.

### `CARI-DISPENSASI`

- `app/src/main/java/com/kastep/app/data/KastepModels.kt:29` — bayar = minggu lunas per bulan; dispensasi = minggu yang dibebaskan (BI/PKL).
- `app/src/main/java/com/kastep/app/data/KastepRepository.kt:244` — LUNAS bila minggu dibayar + minggu dispensasi >= jumlah Senin bulan itu.
- `app/src/main/java/com/kastep/app/data/KastepRepository.kt:419` — Minggu yang dibebaskan per siswa per bulan. Tambah: "NAMA" to mapOf("Bulan Tahun" to jumlahMinggu).

### `CARI-PEMBAYARAN`

- `app/src/main/java/com/kastep/app/ui/screens/PembayaranBerhasilScreen.kt:112` — Mengambil pembayaran terbaru lalu menampilkan jumlah & nama; tombol WA memakai sendWhatsAppConfirmation.
- `app/src/main/java/com/kastep/app/ui/screens/RiwayatPembayaranScreen.kt:56` — 8 data per halaman; cari berdasarkan nama atau bulan; tombol Bayar Kas ke halaman Pembayaran.
- `app/src/main/java/com/kastep/app/ui/screens/PembayaranScreen.kt:41` — Pilihan metode: QRIS, DANA, GoPay, OVO, BCA, Mandiri, BNI, Cash.
- `app/src/main/java/com/kastep/app/ui/screens/PembayaranScreen.kt:195` — Total bayar = sisa minggu x Rp5.000 + denda. Bulan yang sudah lunas tidak bisa dipilih.
- `app/src/main/java/com/kastep/app/data/KastepModels.kt:45` — Satu baris riwayat pembayaran; kolom denda menyimpan denda yang ikut dibayar.
- `app/src/main/java/com/kastep/app/data/KastepRepository.kt:227` — Daftar bulan yang ditagih: Juli 2026 sampai bulan berjalan (otomatis bertambah).
- `app/src/main/java/com/kastep/app/data/KastepRepository.kt:244` — LUNAS bila minggu dibayar + minggu dispensasi >= jumlah Senin bulan itu.
- `app/src/main/java/com/kastep/app/data/KastepRepository.kt:248` — Kekurangan kas satu bulan = minggu belum lunas x Rp5.000.
- `app/src/main/java/com/kastep/app/data/KastepRepository.kt:274` — Mencatat pembayaran: jumlah = sisa kas + denda, lalu bulan itu menjadi LUNAS.

### `CARI-PENGELUARAN`

- `app/src/main/java/com/kastep/app/ui/screens/PengeluaranKasScreen.kt:36` — Tanggal otomatis hari ini, divalidasi kalender (31/02 ditolak), disimpan sebagai "05 Oktober 2026".
- `app/src/main/java/com/kastep/app/ui/screens/PengeluaranKasScreen.kt:308` — Dialog edit; pesan error tampil bila isian salah.
- `app/src/main/java/com/kastep/app/data/KastepModels.kt:56` — Satu pengeluaran; tanggal berformat "15 Juli 2026".
- `app/src/main/java/com/kastep/app/data/KastepRepository.kt:303` — Tambah pengeluaran; nominal > 0 dan keterangan wajib.
- `app/src/main/java/com/kastep/app/data/KastepRepository.kt:316` — Edit pengeluaran; tanggal harus terbaca (contoh 15 Juli 2026).

### `CARI-ANGGOTA`

- `app/src/main/java/com/kastep/app/ui/screens/DataSiswaScreen.kt:41` — Kolom bulan: ✓ lunas, angka = minggu terbayar, D = dispensasi, ✗ belum. Kolom terakhir = total denda.
- `app/src/main/java/com/kastep/app/ui/screens/DataSiswaScreen.kt:243` — Dialog tambah/edit siswa; menampilkan pesan bila nama sudah dipakai.
- `app/src/main/java/com/kastep/app/data/KastepModels.kt:29` — bayar = minggu lunas per bulan; dispensasi = minggu yang dibebaskan (BI/PKL).
- `app/src/main/java/com/kastep/app/data/KastepRepository.kt:191` — Tambah siswa; nama disimpan huruf besar dan tidak boleh sama dengan siswa lain.
- `app/src/main/java/com/kastep/app/data/KastepRepository.kt:202` — Ganti nama/peran; riwayat pembayaran ikut memakai nama baru.

### `CARI-LAPORAN`

- `app/src/main/java/com/kastep/app/ui/screens/LaporanKasScreen.kt:42` — Pemasukan & pengeluaran mengikuti periode yang dipilih; Semua Periode menampilkan saldo akhir.
- `app/src/main/java/com/kastep/app/data/KastepRepository.kt:354` — Teks laporan ringkas untuk tombol Bagikan Laporan (saldo = pemasukan - pengeluaran).

### `CARI-GRAFIK`

- `app/src/main/java/com/kastep/app/ui/screens/LaporanKasScreen.kt:307` — Grafik batang 6 bulan terakhir: biru = pemasukan, abu = pengeluaran.

### `CARI-PROFIL`

- `app/src/main/java/com/kastep/app/ui/screens/ProfileScreen.kt:63` — Tombol Simpan di Edit Akun memanggil viewModel.updateProfile; tombol Keluar memanggil logout.
- `app/src/main/java/com/kastep/app/data/KastepModels.kt:14` — Data akun yang sedang login (role ADMIN atau USER).
- `app/src/main/java/com/kastep/app/data/KastepRepository.kt:172` — Simpan perubahan nama & No. HP dari dialog Edit Akun.

### `CARI-QRIS`

- `app/src/main/java/com/kastep/app/ui/screens/PembayaranScreen.kt:126` — Nomor rekening / e-wallet tujuan transfer; ubah di sini bila rekening kelas berganti.
- `app/src/main/java/com/kastep/app/ui/screens/PembayaranScreen.kt:326` — Gambar QRIS dari res/drawable/ic_qris.

### `CARI-WHATSAPP`

- `app/src/main/java/com/kastep/app/ui/screens/PembayaranBerhasilScreen.kt:112` — Mengambil pembayaran terbaru lalu menampilkan jumlah & nama; tombol WA memakai sendWhatsAppConfirmation.
- `app/src/main/java/com/kastep/app/ui/screens/PembayaranScreen.kt:362` — Membuka WhatsApp dengan pesan konfirmasi. Nomor tujuan ada di variabel phone.

### `CARI-VALIDASI`

- `app/src/main/java/com/kastep/app/ui/screens/DataSiswaScreen.kt:243` — Dialog tambah/edit siswa; menampilkan pesan bila nama sudah dipakai.
- `app/src/main/java/com/kastep/app/ui/screens/PengeluaranKasScreen.kt:36` — Tanggal otomatis hari ini, divalidasi kalender (31/02 ditolak), disimpan sebagai "05 Oktober 2026".
- `app/src/main/java/com/kastep/app/data/KastepRepository.kt:126` — Cek format email, panjang password (min. 5), lalu cocokkan dengan akun terdaftar.
- `app/src/main/java/com/kastep/app/data/KastepRepository.kt:146` — Akun baru selalu ber-role USER; email tidak boleh dobel.
- `app/src/main/java/com/kastep/app/data/KastepRepository.kt:191` — Tambah siswa; nama disimpan huruf besar dan tidak boleh sama dengan siswa lain.
- `app/src/main/java/com/kastep/app/data/KastepRepository.kt:303` — Tambah pengeluaran; nominal > 0 dan keterangan wajib.
- `app/src/main/java/com/kastep/app/data/KastepRepository.kt:316` — Edit pengeluaran; tanggal harus terbaca (contoh 15 Juli 2026).

### `CARI-JADWAL`

- `app/src/main/java/com/kastep/app/ui/screens/MataPelajaranScreen.kt:84` — Isi jadwal per hari: ScheduleEntry(mapel, guru, warna, jam mulai, lama jam).
- `app/src/main/java/com/kastep/app/ui/screens/MataPelajaranScreen.kt:131` — Menampilkan tabel jadwal dan tabel alokasi waktu.

### `CARI-FORMAT-RUPIAH`

- `app/src/main/java/com/kastep/app/data/KastepViewModel.kt:75` — Mengubah 1389000 menjadi "1.389.000".

### `CARI-TEMA`

- `app/src/main/java/com/kastep/app/ui/theme/Color.kt:13` — Ubah nilai warna di file ini untuk mengganti tema.
