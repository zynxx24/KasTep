# Peta Layar KASTEP (Kelompok 1)

Dipakai kalau guru bertanya **"ini yang di layar kodenya di mana?"**. Cari baris di tabel, lalu buka file dan nomor barisnya.
Di Android Studio: **Ctrl+Shift+N** (ketik nama file) lalu **Ctrl+G** (ketik nomor baris).

Alur singkat aplikasi: `MainActivity` memanggil `KastepApp` (menu samping + `NavGraph`), mulai dari Splash lalu Login atau Dashboard. Data lewat `KastepViewModel` dan `KastepRepository`.
Grafik: **hanya satu**, grafik batang berpasangan di layar Laporan Kas (`IncomeExpenseChart`). Dashboard tidak punya grafik.

## DASHBOARD

| Yang terlihat atau ditanyakan | Penjelasan | Letak kode |
|---|---|---|
| Tombol menu, tanggal, nama akun | TopBar | `app/src/main/java/com/kastep/app/ui/screens/DataSiswaScreen.kt:307` |
| Tiga kartu Saldo, Pemasukan, Pengeluaran | SummaryCard | `app/src/main/java/com/kastep/app/ui/screens/DashboardScreen.kt:97` |
| Garis progres | LinearProgressIndicator | `app/src/main/java/com/kastep/app/ui/screens/DashboardScreen.kt:110` |
| 10 Transaksi Terbaru | recentTxList | `app/src/main/java/com/kastep/app/ui/screens/DashboardScreen.kt:58` |

## LAPORAN KAS (tempat GRAFIK)

| Yang terlihat atau ditanyakan | Penjelasan | Letak kode |
|---|---|---|
| Dropdown Periode | selectedPeriode | `app/src/main/java/com/kastep/app/ui/screens/LaporanKasScreen.kt:120` |
| Kartu Total Pemasukan dan Pengeluaran | ReportSummaryCard | `app/src/main/java/com/kastep/app/ui/screens/LaporanKasScreen.kt:153` |
| Kartu Saldo Akhir atau Selisih | label Saldo Akhir | `app/src/main/java/com/kastep/app/ui/screens/LaporanKasScreen.kt:174` |
| GRAFIK batang (kartu) | Chart card | `app/src/main/java/com/kastep/app/ui/screens/LaporanKasScreen.kt:182` |
| GRAFIK batang (gambarnya) | IncomeExpenseChart, Canvas manual | `app/src/main/java/com/kastep/app/ui/screens/LaporanKasScreen.kt:353` |
| Pilihan 6 bulan di grafik | bulanGrafik | `app/src/main/java/com/kastep/app/ui/screens/LaporanKasScreen.kt:207` |
| Tombol Bagikan Laporan | ACTION_SEND | `app/src/main/java/com/kastep/app/ui/screens/LaporanKasScreen.kt:222` |
| Dialog Detail Pemasukan (nama belum bayar untuk admin) | showPemasukanSheet | `app/src/main/java/com/kastep/app/ui/screens/LaporanKasScreen.kt:245` |

## PEMBAYARAN

| Yang terlihat atau ditanyakan | Penjelasan | Letak kode |
|---|---|---|
| Pilihan metode QRIS, e-wallet, bank, Cash | PembayaranScreen | `app/src/main/java/com/kastep/app/ui/screens/PembayaranScreen.kt:57` |
| Nomor rekening tujuan (contoh) | accNo | `app/src/main/java/com/kastep/app/ui/screens/PembayaranScreen.kt:150` |
| Tombol hijau PEMBAYARAN | Big green button | `app/src/main/java/com/kastep/app/ui/screens/PembayaranScreen.kt:170` |
| Dialog pilih siswa (Admin), dari bulan sampai bulan, total bayar | PaymentConfirmDialog | `app/src/main/java/com/kastep/app/ui/screens/PembayaranScreen.kt:242` |
| Kotak pilihan bulan (Dari dan Sampai) | BulanPicker | `app/src/main/java/com/kastep/app/ui/screens/PembayaranScreen.kt:397` |
| Dialog gambar QRIS | QrisSimulationDialog | `app/src/main/java/com/kastep/app/ui/screens/PembayaranScreen.kt:421` |
| Pesan WhatsApp konfirmasi | sendWhatsAppConfirmation | `app/src/main/java/com/kastep/app/ui/screens/PembayaranScreen.kt:460` |
| Layar animasi Pembayaran Berhasil | PembayaranBerhasilScreen | `app/src/main/java/com/kastep/app/ui/screens/PembayaranBerhasilScreen.kt:123` |
| Tabel Riwayat Bayar (8 per halaman) | RiwayatPembayaranScreen | `app/src/main/java/com/kastep/app/ui/screens/RiwayatPembayaranScreen.kt:61` |

## DATA SISWA, PENGELUARAN, LAIN-LAIN

| Yang terlihat atau ditanyakan | Penjelasan | Letak kode |
|---|---|---|
| Tabel siswa dan simbol status bulan | DataSiswaScreen | `app/src/main/java/com/kastep/app/ui/screens/DataSiswaScreen.kt:50` |
| Aturan privasi (siswa lain tampil titik) | bolehLihat | `app/src/main/java/com/kastep/app/ui/screens/DataSiswaScreen.kt:135` |
| Form tambah atau edit siswa | StudentDialog | `app/src/main/java/com/kastep/app/ui/screens/DataSiswaScreen.kt:259` |
| Form dan daftar pengeluaran | PengeluaranKasScreen | `app/src/main/java/com/kastep/app/ui/screens/PengeluaranKasScreen.kt:43` |
| Tabel jadwal dan alokasi waktu | MataPelajaranScreen | `app/src/main/java/com/kastep/app/ui/screens/MataPelajaranScreen.kt:139` |
| Menu samping (7 menu) | drawerItems | `app/src/main/java/com/kastep/app/MainActivity.kt:128` |
| Peta halaman | NavGraph | `app/src/main/java/com/kastep/app/ui/navigation/NavGraph.kt:40` |
| Login | LoginScreen | `app/src/main/java/com/kastep/app/ui/screens/LoginScreen.kt:76` |
| Profil dan Edit Akun | ProfileScreen | `app/src/main/java/com/kastep/app/ui/screens/ProfileScreen.kt:68` |

## OTAK DATA (tidak terlihat di layar)

| Yang terlihat atau ditanyakan | Penjelasan | Letak kode |
|---|---|---|
| Rumus SALDO | KastepViewModel.saldo | `app/src/main/java/com/kastep/app/data/KastepViewModel.kt:51` |
| Rumus DENDA | calculateDenda | `app/src/main/java/com/kastep/app/data/KastepRepository.kt:322` |
| Status LUNAS atau BELUM satu bulan | statusBulan | `app/src/main/java/com/kastep/app/data/KastepRepository.kt:293` |
| Kekurangan kas satu bulan | sisaKas | `app/src/main/java/com/kastep/app/data/KastepRepository.kt:299` |
| Hitung hari Senin sebulan | jumlahMinggu | `app/src/main/java/com/kastep/app/data/KastepRepository.kt:110` |
| Mencatat pembayaran (khusus Admin) | processPayment | `app/src/main/java/com/kastep/app/data/KastepRepository.kt:343` |
| Mencatat rentang bulan (khusus Admin) | processPaymentRange | `app/src/main/java/com/kastep/app/data/KastepRepository.kt:374` |
| Data awal 33 siswa dan buku kas | loadInitialData | `app/src/main/java/com/kastep/app/data/KastepRepository.kt:482` |
| Simpan ke HP | saveToPrefs | `app/src/main/java/com/kastep/app/data/KastepRepository.kt:826` |
| Bentuk data | KastepModels.kt | `app/src/main/java/com/kastep/app/data/KastepModels.kt:47` |
