# Peta Layar KASTEP (Kelompok 1)

Dipakai kalau guru bertanya **"ini yang di layar kodenya di mana?"**. Cari baris di tabel, lalu buka file dan nomor barisnya.
Di Android Studio: **Ctrl+Shift+N** (ketik nama file) lalu **Ctrl+G** (ketik nomor baris).

Alur singkat aplikasi: `MainActivity` memanggil `KastepApp` (menu samping + `NavGraph`), mulai dari Splash lalu Login atau Dashboard. Data lewat `KastepViewModel` dan `KastepRepository`.
Grafik: **hanya satu**, grafik batang berpasangan di layar Laporan Kas (`IncomeExpenseChart`). Dashboard tidak punya grafik.

## DASHBOARD

| Yang terlihat atau ditanyakan | Penjelasan | Letak kode |
|---|---|---|
| Tombol menu, tanggal, nama akun | TopBar | `app/src/main/java/com/kastep/app/ui/screens/DataSiswaScreen.kt:493` |
| Tiga kartu Saldo, Pemasukan, Pengeluaran | SummaryCard | `app/src/main/java/com/kastep/app/ui/screens/DashboardScreen.kt:157` |
| Garis progres | LinearProgressIndicator | `app/src/main/java/com/kastep/app/ui/screens/DashboardScreen.kt:179` |
| 10 Transaksi Terbaru | recentTxList | `app/src/main/java/com/kastep/app/ui/screens/DashboardScreen.kt:95` |

## LAPORAN KAS (tempat GRAFIK)

| Yang terlihat atau ditanyakan | Penjelasan | Letak kode |
|---|---|---|
| Dropdown Periode | selectedPeriode | `app/src/main/java/com/kastep/app/ui/screens/LaporanKasScreen.kt:198` |
| Kartu Total Pemasukan dan Pengeluaran | ReportSummaryCard | `app/src/main/java/com/kastep/app/ui/screens/LaporanKasScreen.kt:247` |
| Kartu Saldo Akhir atau Selisih | label Saldo Akhir | `app/src/main/java/com/kastep/app/ui/screens/LaporanKasScreen.kt:283` |
| GRAFIK batang (kartu) | Chart card | `app/src/main/java/com/kastep/app/ui/screens/LaporanKasScreen.kt:296` |
| GRAFIK batang (gambarnya) | IncomeExpenseChart, Canvas manual | `app/src/main/java/com/kastep/app/ui/screens/LaporanKasScreen.kt:567` |
| Pilihan 6 bulan di grafik | bulanGrafik | `app/src/main/java/com/kastep/app/ui/screens/LaporanKasScreen.kt:338` |
| Tombol Bagikan Laporan | ACTION_SEND | `app/src/main/java/com/kastep/app/ui/screens/LaporanKasScreen.kt:364` |
| Dialog Detail Pemasukan (nama belum bayar untuk admin) | showPemasukanSheet | `app/src/main/java/com/kastep/app/ui/screens/LaporanKasScreen.kt:401` |

## PEMBAYARAN

| Yang terlihat atau ditanyakan | Penjelasan | Letak kode |
|---|---|---|
| Pilihan metode QRIS, e-wallet, bank, Cash | PembayaranScreen | `app/src/main/java/com/kastep/app/ui/screens/PembayaranScreen.kt:95` |
| Nomor rekening tujuan (contoh) | accNo | `app/src/main/java/com/kastep/app/ui/screens/PembayaranScreen.kt:245` |
| Tombol hijau PEMBAYARAN | Big green button | `app/src/main/java/com/kastep/app/ui/screens/PembayaranScreen.kt:278` |
| Dialog pilih siswa (Admin), dari bulan sampai bulan, total bayar | PaymentConfirmDialog | `app/src/main/java/com/kastep/app/ui/screens/PembayaranScreen.kt:394` |
| Kotak pilihan bulan (Dari dan Sampai) | BulanPicker | `app/src/main/java/com/kastep/app/ui/screens/PembayaranScreen.kt:651` |
| Dialog gambar QRIS | QrisSimulationDialog | `app/src/main/java/com/kastep/app/ui/screens/PembayaranScreen.kt:692` |
| Pesan WhatsApp konfirmasi | sendWhatsAppConfirmation | `app/src/main/java/com/kastep/app/ui/screens/PembayaranScreen.kt:755` |
| Layar animasi Pembayaran Berhasil | PembayaranBerhasilScreen | `app/src/main/java/com/kastep/app/ui/screens/PembayaranBerhasilScreen.kt:217` |
| Tabel Riwayat Bayar (8 per halaman) | RiwayatPembayaranScreen | `app/src/main/java/com/kastep/app/ui/screens/RiwayatPembayaranScreen.kt:110` |

## DATA SISWA, PENGELUARAN, LAIN-LAIN

| Yang terlihat atau ditanyakan | Penjelasan | Letak kode |
|---|---|---|
| Tabel siswa dan simbol status bulan | DataSiswaScreen | `app/src/main/java/com/kastep/app/ui/screens/DataSiswaScreen.kt:86` |
| Aturan privasi (siswa lain tampil titik) | bolehLihat | `app/src/main/java/com/kastep/app/ui/screens/DataSiswaScreen.kt:220` |
| Form tambah atau edit siswa | StudentDialog | `app/src/main/java/com/kastep/app/ui/screens/DataSiswaScreen.kt:418` |
| Form dan daftar pengeluaran | PengeluaranKasScreen | `app/src/main/java/com/kastep/app/ui/screens/PengeluaranKasScreen.kt:75` |
| Tabel jadwal dan alokasi waktu | MataPelajaranScreen | `app/src/main/java/com/kastep/app/ui/screens/MataPelajaranScreen.kt:246` |
| Menu samping (7 menu) | drawerItems | `app/src/main/java/com/kastep/app/MainActivity.kt:224` |
| Peta halaman | NavGraph | `app/src/main/java/com/kastep/app/ui/navigation/NavGraph.kt:65` |
| Login | LoginScreen | `app/src/main/java/com/kastep/app/ui/screens/LoginScreen.kt:139` |
| Profil dan Edit Akun | ProfileScreen | `app/src/main/java/com/kastep/app/ui/screens/ProfileScreen.kt:124` |

## OTAK DATA (tidak terlihat di layar)

| Yang terlihat atau ditanyakan | Penjelasan | Letak kode |
|---|---|---|
| Rumus SALDO | KastepViewModel.saldo | `app/src/main/java/com/kastep/app/data/KastepViewModel.kt:73` |
| Rumus DENDA | calculateDenda | `app/src/main/java/com/kastep/app/data/KastepRepository.kt:484` |
| Status LUNAS atau BELUM satu bulan | statusBulan | `app/src/main/java/com/kastep/app/data/KastepRepository.kt:446` |
| Kekurangan kas satu bulan | sisaKas | `app/src/main/java/com/kastep/app/data/KastepRepository.kt:454` |
| Hitung hari Senin sebulan | jumlahMinggu | `app/src/main/java/com/kastep/app/data/KastepRepository.kt:157` |
| Mencatat pembayaran (khusus Admin) | processPayment | `app/src/main/java/com/kastep/app/data/KastepRepository.kt:510` |
| Mencatat rentang bulan (khusus Admin) | processPaymentRange | `app/src/main/java/com/kastep/app/data/KastepRepository.kt:561` |
| Data awal 33 siswa dan buku kas | loadInitialData | `app/src/main/java/com/kastep/app/data/KastepRepository.kt:725` |
| Simpan ke HP | saveToPrefs | `app/src/main/java/com/kastep/app/data/KastepRepository.kt:1659` |
| Bentuk data | KastepModels.kt | `app/src/main/java/com/kastep/app/data/KastepModels.kt:73` |
