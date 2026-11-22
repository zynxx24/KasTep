# Peta Kode KASTEP – Kelompok 1

Panduan mencari bagian penting kode KASTEP dengan cepat. Daftar di bawah dibuat otomatis dari komentar `CARI-…` dan `TANYA-GURU` di kode.

## Cara mencari

1. Buka folder proyek di VS Code / Android Studio.
2. Tekan **Ctrl+Shift+F** (cari di semua file).
3. Ketik **`CARI-`** untuk semua titik penting, atau penanda tertentu, misalnya **`CARI-DENDA`**.
4. Ketik **`TANYA-GURU`** untuk melihat jawaban singkat pertanyaan yang paling mungkin ditanyakan.
5. Setiap fungsi sudah diberi komentar satu baris di atasnya, dan setiap file utama punya kotak **`PETA FILE`** di baris paling atas.
6. Daftar pertanyaan lengkap beserta jawabannya ada di **`TANYA-JAWAB.md`**.

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
| `CARI-PEMBAYARAN` | Mencatat / menampilkan pembayaran kas | 10 |
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

## Jawaban singkat di kode (`TANYA-GURU`)

- `app/src/main/java/com/kastep/app/data/KastepRepository.kt:163` — Validasi login? Email harus ada @ dan titik, password minimal 5 karakter, lalu dicocokkan dengan akun terdaftar.
- `app/src/main/java/com/kastep/app/data/KastepRepository.kt:320` — Rumus denda? Rp5.000 x n, n = bulan sekarang - bulan tagihan, hanya bulan yang belum LUNAS.
- `app/src/main/java/com/kastep/app/data/KastepRepository.kt:341` — Siapa yang membuat status jadi LUNAS? Hanya bendahara (Admin). Siswa transfer lalu kirim konfirmasi WA,
- `app/src/main/java/com/kastep/app/data/KastepRepository.kt:825` — Data disimpan di mana? File kastep_prefs di HP (SharedPreferences, JSON). Belum tersinkron antar HP.
- `app/src/main/java/com/kastep/app/data/KastepViewModel.kt:50` — Saldo dihitung dari mana? Total semua pembayaran (kas + donasi) - total pengeluaran = Rp1.389.000.
- `app/src/main/java/com/kastep/app/ui/screens/DataSiswaScreen.kt:134` — Kenapa siswa lain tampil titik? Akun USER hanya melihat status dan dendanya sendiri supaya tidak dipermalukan.

## Lokasi lengkap

### `CARI-MULAI`

- `app/src/main/java/com/kastep/app/MainActivity.kt:72` — Aplikasi dibuka dari sini: membuat KastepViewModel lalu menampilkan KastepApp.
- `app/src/main/java/com/kastep/app/ui/screens/SplashScreen.kt:52` — Tampil 2,5 detik lalu pindah ke Login atau Dashboard (diatur di NavGraph).

### `CARI-NAVIGASI`

- `app/src/main/java/com/kastep/app/MainActivity.kt:127` — Isi menu samping. Tambah menu = DrawerMenuItem(label, ikon, Screen.X.route) + daftarkan di NavGraph.
- `app/src/main/java/com/kastep/app/ui/navigation/NavGraph.kt:38` — Setiap composable(Screen.X.route) = satu halaman. Splash langsung ke Dashboard bila sesi login masih aktif.
- `app/src/main/java/com/kastep/app/ui/navigation/Screen.kt:12` — Tambah halaman baru: object NamaHalaman : Screen("nama_rute").
- `app/src/main/java/com/kastep/app/ui/screens/DataSiswaScreen.kt:305` — Bar atas yang dipakai banyak halaman: tombol menu, tanggal, dan tombol profil.

### `CARI-LOGIN`

- `app/src/main/java/com/kastep/app/data/KastepModels.kt:22` — Data akun yang sedang login (role ADMIN atau USER).
- `app/src/main/java/com/kastep/app/data/KastepRepository.kt:90` — Akun admin bawaan: admin@gmail.com / admin123.
- `app/src/main/java/com/kastep/app/data/KastepRepository.kt:162` — Cek format email, panjang password (min. 5), lalu cocokkan dengan akun terdaftar.
- `app/src/main/java/com/kastep/app/ui/navigation/NavGraph.kt:38` — Setiap composable(Screen.X.route) = satu halaman. Splash langsung ke Dashboard bila sesi login masih aktif.
- `app/src/main/java/com/kastep/app/ui/screens/LoginScreen.kt:74` — Form email & password; pesan error dari KastepRepository.login.
- `app/src/main/java/com/kastep/app/ui/screens/ProfileScreen.kt:66` — Tombol Simpan di Edit Akun memanggil viewModel.updateProfile; tombol Keluar memanggil logout.

### `CARI-REGISTER`

- `app/src/main/java/com/kastep/app/data/KastepRepository.kt:184` — Akun baru selalu ber-role USER; email tidak boleh dobel.
- `app/src/main/java/com/kastep/app/ui/screens/RegisterScreen.kt:63` — Form nama, email, NIS, kelas, No. HP, password; link kembali ke Login di bawah.

### `CARI-PUSAT-DATA`

- `app/src/main/java/com/kastep/app/data/KastepRepository.kt:43` — Semua perubahan data lewat sini; layar membaca lewat KastepViewModel.
- `app/src/main/java/com/kastep/app/data/KastepViewModel.kt:26` — Layar memanggil fungsi di sini; isi logikanya ada di KastepRepository.

### `CARI-DATA-AWAL`

- `app/src/main/java/com/kastep/app/data/KastepRepository.kt:428` — Mengembalikan semua data ke buku kas awal (sesi admin tetap).
- `app/src/main/java/com/kastep/app/data/KastepRepository.kt:481` — Data awal dari buku kas asli XII PPLG per 20 November 2026: siswa, minggu terbayar, riwayat, pengeluaran.

### `CARI-SIMPAN-DATA`

- `app/src/main/java/com/kastep/app/data/KastepRepository.kt:83` — Kunci penyimpanan. Naikkan versinya bila data awal diganti supaya HP memuat ulang.
- `app/src/main/java/com/kastep/app/data/KastepRepository.kt:824` — Menyimpan semua data ke HP (JSON di SharedPreferences).
- `app/src/main/java/com/kastep/app/data/KastepRepository.kt:865` — Memuat data tersimpan; bila rusak kembali ke data awal.

### `CARI-NOMINAL-KAS`

- `app/src/main/java/com/kastep/app/data/KastepRepository.kt:79` — Iuran per minggu (setiap Senin). Tagihan satu bulan = jumlah Senin x nilai ini.
- `app/src/main/java/com/kastep/app/data/KastepRepository.kt:108` — Menghitung jumlah hari Senin dalam sebulan (4 atau 5) = jumlah minggu kas.

### `CARI-SALDO`

- `app/src/main/java/com/kastep/app/data/KastepRepository.kt:450` — Teks laporan ringkas untuk tombol Bagikan Laporan (saldo = pemasukan - pengeluaran).
- `app/src/main/java/com/kastep/app/data/KastepViewModel.kt:49` — Saldo = total pemasukan - total pengeluaran (dipakai Dashboard).
- `app/src/main/java/com/kastep/app/ui/screens/DashboardScreen.kt:42` — Tiga kartu ringkasan + 10 transaksi terbaru (diurutkan menurut tanggal).
- `app/src/main/java/com/kastep/app/ui/screens/LaporanKasScreen.kt:59` — Pemasukan & pengeluaran mengikuti periode yang dipilih; Semua Periode menampilkan saldo akhir.

### `CARI-DENDA`

- `app/src/main/java/com/kastep/app/data/KastepRepository.kt:81` — Besar denda untuk setiap bulan keterlambatan. Ubah angka ini untuk mengganti tarif.
- `app/src/main/java/com/kastep/app/data/KastepRepository.kt:314` — Rumus: Rp5.000 x jumlah bulan terlambat, hanya untuk bulan yang belum lunas.
- `app/src/main/java/com/kastep/app/data/KastepRepository.kt:340` — Mencatat pembayaran (khusus Admin): jumlah = sisa kas + denda, lalu bulan itu menjadi LUNAS.
- `app/src/main/java/com/kastep/app/ui/screens/DataSiswaScreen.kt:48` — Kolom bulan: ✓ lunas, angka = minggu terbayar, D = dispensasi, ✗ belum. Kolom terakhir = total denda.
- `app/src/main/java/com/kastep/app/ui/screens/PembayaranScreen.kt:239` — Total bayar = jumlah (sisa minggu x Rp5.000 + denda) bulan yang belum lunas di rentang.

### `CARI-DISPENSASI`

- `app/src/main/java/com/kastep/app/data/KastepModels.kt:41` — bayar = minggu lunas per bulan; dispensasi = minggu yang dibebaskan (BI/PKL).
- `app/src/main/java/com/kastep/app/data/KastepRepository.kt:292` — LUNAS bila minggu dibayar + minggu dispensasi >= jumlah Senin bulan itu.
- `app/src/main/java/com/kastep/app/data/KastepRepository.kt:519` — Minggu yang dibebaskan per siswa per bulan. Tambah: "NAMA" to mapOf("Bulan Tahun" to jumlahMinggu).

### `CARI-PEMBAYARAN`

- `app/src/main/java/com/kastep/app/data/KastepModels.kt:58` — Satu baris riwayat pembayaran; kolom denda menyimpan denda yang ikut dibayar.
- `app/src/main/java/com/kastep/app/data/KastepRepository.kt:272` — Daftar bulan yang ditagih: Juli 2026 sampai bulan berjalan (otomatis bertambah).
- `app/src/main/java/com/kastep/app/data/KastepRepository.kt:292` — LUNAS bila minggu dibayar + minggu dispensasi >= jumlah Senin bulan itu.
- `app/src/main/java/com/kastep/app/data/KastepRepository.kt:297` — Kekurangan kas satu bulan = minggu belum lunas x Rp5.000.
- `app/src/main/java/com/kastep/app/data/KastepRepository.kt:340` — Mencatat pembayaran (khusus Admin): jumlah = sisa kas + denda, lalu bulan itu menjadi LUNAS.
- `app/src/main/java/com/kastep/app/data/KastepRepository.kt:373` — Catat rentang bulan (dari bulan ... sampai bulan ...), hanya Admin.
- `app/src/main/java/com/kastep/app/ui/screens/PembayaranBerhasilScreen.kt:121` — Menjumlahkan baris yang baru dicatat bendahara lalu menampilkan jumlah & nama; tombol WA memakai sendWhatsAppConfirmation.
- `app/src/main/java/com/kastep/app/ui/screens/PembayaranScreen.kt:54` — Pilihan metode: QRIS, DANA, GoPay, OVO, BCA, Mandiri, BNI, Cash.
- `app/src/main/java/com/kastep/app/ui/screens/PembayaranScreen.kt:239` — Total bayar = jumlah (sisa minggu x Rp5.000 + denda) bulan yang belum lunas di rentang.
- `app/src/main/java/com/kastep/app/ui/screens/RiwayatPembayaranScreen.kt:59` — 8 data per halaman; cari berdasarkan nama atau bulan; tombol Bayar Kas ke halaman Pembayaran.

### `CARI-PENGELUARAN`

- `app/src/main/java/com/kastep/app/data/KastepModels.kt:69` — Satu pengeluaran; tanggal berformat "15 Juli 2026".
- `app/src/main/java/com/kastep/app/data/KastepRepository.kt:392` — Tambah pengeluaran; nominal > 0 dan keterangan wajib.
- `app/src/main/java/com/kastep/app/data/KastepRepository.kt:406` — Edit pengeluaran; tanggal harus terbaca (contoh 15 Juli 2026).
- `app/src/main/java/com/kastep/app/ui/screens/PengeluaranKasScreen.kt:41` — Tanggal otomatis hari ini, divalidasi kalender (31/02 ditolak), disimpan sebagai "05 Oktober 2026".
- `app/src/main/java/com/kastep/app/ui/screens/PengeluaranKasScreen.kt:313` — Dialog edit; pesan error tampil bila isian salah.

### `CARI-ANGGOTA`

- `app/src/main/java/com/kastep/app/data/KastepModels.kt:41` — bayar = minggu lunas per bulan; dispensasi = minggu yang dibebaskan (BI/PKL).
- `app/src/main/java/com/kastep/app/data/KastepRepository.kt:232` — Tambah siswa; nama disimpan huruf besar dan tidak boleh sama dengan siswa lain.
- `app/src/main/java/com/kastep/app/data/KastepRepository.kt:244` — Ganti nama/peran; riwayat pembayaran ikut memakai nama baru.
- `app/src/main/java/com/kastep/app/ui/screens/DataSiswaScreen.kt:48` — Kolom bulan: ✓ lunas, angka = minggu terbayar, D = dispensasi, ✗ belum. Kolom terakhir = total denda.
- `app/src/main/java/com/kastep/app/ui/screens/DataSiswaScreen.kt:257` — Dialog tambah/edit siswa; menampilkan pesan bila nama sudah dipakai.

### `CARI-LAPORAN`

- `app/src/main/java/com/kastep/app/data/KastepRepository.kt:450` — Teks laporan ringkas untuk tombol Bagikan Laporan (saldo = pemasukan - pengeluaran).
- `app/src/main/java/com/kastep/app/ui/screens/LaporanKasScreen.kt:59` — Pemasukan & pengeluaran mengikuti periode yang dipilih; Semua Periode menampilkan saldo akhir.

### `CARI-GRAFIK`

- `app/src/main/java/com/kastep/app/ui/screens/LaporanKasScreen.kt:351` — Grafik batang 6 bulan terakhir: biru = pemasukan, abu = pengeluaran.

### `CARI-PROFIL`

- `app/src/main/java/com/kastep/app/data/KastepModels.kt:22` — Data akun yang sedang login (role ADMIN atau USER).
- `app/src/main/java/com/kastep/app/data/KastepRepository.kt:211` — Simpan perubahan nama & No. HP dari dialog Edit Akun.
- `app/src/main/java/com/kastep/app/ui/screens/ProfileScreen.kt:66` — Tombol Simpan di Edit Akun memanggil viewModel.updateProfile; tombol Keluar memanggil logout.

### `CARI-QRIS`

- `app/src/main/java/com/kastep/app/ui/screens/PembayaranScreen.kt:149` — Nomor rekening / e-wallet tujuan transfer; ubah di sini bila rekening kelas berganti.
- `app/src/main/java/com/kastep/app/ui/screens/PembayaranScreen.kt:419` — Gambar QRIS dari res/drawable/ic_qris.

### `CARI-WHATSAPP`

- `app/src/main/java/com/kastep/app/ui/screens/PembayaranBerhasilScreen.kt:121` — Menjumlahkan baris yang baru dicatat bendahara lalu menampilkan jumlah & nama; tombol WA memakai sendWhatsAppConfirmation.
- `app/src/main/java/com/kastep/app/ui/screens/PembayaranScreen.kt:459` — Membuka WhatsApp dengan pesan konfirmasi. Nomor bendahara ada di variabel phone.

### `CARI-VALIDASI`

- `app/src/main/java/com/kastep/app/data/KastepRepository.kt:162` — Cek format email, panjang password (min. 5), lalu cocokkan dengan akun terdaftar.
- `app/src/main/java/com/kastep/app/data/KastepRepository.kt:184` — Akun baru selalu ber-role USER; email tidak boleh dobel.
- `app/src/main/java/com/kastep/app/data/KastepRepository.kt:232` — Tambah siswa; nama disimpan huruf besar dan tidak boleh sama dengan siswa lain.
- `app/src/main/java/com/kastep/app/data/KastepRepository.kt:392` — Tambah pengeluaran; nominal > 0 dan keterangan wajib.
- `app/src/main/java/com/kastep/app/data/KastepRepository.kt:406` — Edit pengeluaran; tanggal harus terbaca (contoh 15 Juli 2026).
- `app/src/main/java/com/kastep/app/ui/screens/DataSiswaScreen.kt:257` — Dialog tambah/edit siswa; menampilkan pesan bila nama sudah dipakai.
- `app/src/main/java/com/kastep/app/ui/screens/PengeluaranKasScreen.kt:41` — Tanggal otomatis hari ini, divalidasi kalender (31/02 ditolak), disimpan sebagai "05 Oktober 2026".

### `CARI-JADWAL`

- `app/src/main/java/com/kastep/app/ui/screens/MataPelajaranScreen.kt:90` — Isi jadwal per hari: ScheduleEntry(mapel, guru, warna, jam mulai, lama jam).
- `app/src/main/java/com/kastep/app/ui/screens/MataPelajaranScreen.kt:137` — Menampilkan tabel jadwal dan tabel alokasi waktu.

### `CARI-FORMAT-RUPIAH`

- `app/src/main/java/com/kastep/app/data/KastepViewModel.kt:125` — Mengubah 1389000 menjadi "1.389.000".

### `CARI-TEMA`

- `app/src/main/java/com/kastep/app/ui/theme/Color.kt:15` — Ubah nilai warna di file ini untuk mengganti tema.
