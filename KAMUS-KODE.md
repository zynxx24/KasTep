# Kamus Istilah Kode KASTEP

Untuk yang belum terbiasa membaca Kotlin dan Jetpack Compose. Semua istilah di bawah muncul di kode KASTEP.

## Cara berpikir singkat
Layar di Compose adalah **fungsi** yang menggambar tampilan. Layar tidak menyimpan data sendiri.
Data hidup di repository, lewat `KastepViewModel`, lalu layar menampilkannya. Bila data berubah, layar digambar ulang otomatis.

## Istilah tata letak (apa yang tampil)
| Istilah | Artinya |
|---|---|
| `@Composable` | Penanda bahwa fungsi ini menggambar sebagian layar |
| `Column` | Susun isi dari atas ke bawah |
| `Row` | Susun isi dari kiri ke kanan |
| `Box` | Tumpuk isi satu di atas yang lain, atau satu kotak |
| `Card` | Kartu berlatar dengan sudut membulat |
| `Text` | Tulisan |
| `Icon` | Gambar ikon kecil |
| `Spacer` | Ruang kosong pemisah |
| `LazyColumn` | Daftar vertikal yang bisa digulir, hanya menggambar yang terlihat |
| `Modifier` | Daftar "dandanan" untuk elemen: ukuran, jarak (`padding`), latar (`background`), bisa ditekan (`clickable`), sudut (`clip`) |
| `weight(1f)` | Bagi lebar sama rata dengan elemen lain di baris yang sama |
| `dp` dan `sp` | Satuan ukuran. `dp` untuk jarak dan bentuk, `sp` untuk huruf |
| `ModalBottomSheet` | Lembar yang muncul dari bawah layar |
| `AlertDialog` | Kotak pesan di tengah layar |
| `Canvas` | Kanvas kosong untuk menggambar manual (garis, lingkaran). Dipakai grafik |
| `Scaffold` | Kerangka halaman (isi dan bar bawah) |
| `NavigationBar` | Bar 5 tab di bawah layar |

## Istilah data dan logika (apa yang terjadi)
| Istilah | Artinya |
|---|---|
| `remember { mutableStateOf(...) }` | Variabel yang diingat layar. Bila nilainya berubah, layar digambar ulang |
| `StateFlow` | Nilai yang bisa "dipantau". Perubahannya otomatis sampai ke layar |
| `ViewModel` | Perantara antara layar dan data (`KastepViewModel`) |
| `Repository` | Tempat data dan rumus (`KastepRepository`) |
| `data class` | Cetakan data, contoh `Siswa`, `PaymentRecord` |
| `enum class` | Daftar pilihan tetap, contoh `UserRole` (ADMIN atau USER) |
| `SharedPreferences` | Laci penyimpanan kecil di HP. KASTEP menyimpan data di sini |
| `JSON` | Format teks untuk menyimpan data terstruktur |
| `Intent` | Perintah ke Android untuk membuka aplikasi lain (WhatsApp, menu Bagikan) |
| `combine` | Gabungkan dua sumber data lalu hitung ulang tiap kali salah satu berubah |
| `map`, `filter`, `sumOf` | Mengubah, menyaring, dan menjumlahkan isi daftar |
| `?.` dan `?:` | Aman bila nilainya kosong (null), dan nilai cadangan bila kosong |
| `remember(x) { ... }` | Hitung sekali, hitung ulang hanya bila `x` berubah |

## Istilah KASTEP
| Istilah | Artinya |
|---|---|
| ADMIN | Akun bendahara. Kelola siswa dan pengeluaran, melihat semua status |
| USER | Akun siswa. Hanya status miliknya yang terlihat |
| Drawer | Menu samping yang digeser dari kiri |
| Rute (`Screen.X.route`) | Nama sebuah halaman, dipakai `NavGraph` untuk berpindah |
| `NavController` | Pengatur perpindahan halaman |
| Bulan tagihan | Juli 2026 sampai bulan sekarang. Satu bulan = jumlah Senin x Rp5.000 |
| LUNAS atau BELUM | Hasil `statusBulan`: minggu dibayar + dispensasi dibandingkan jumlah Senin bulan itu |
| Dispensasi | Minggu bebas kas karena BI atau PKL, dihitung lunas |
| Denda | Rp5.000 x bulan terlambat, hanya untuk bulan yang belum lunas |
| `processPayment` | Mencatat pembayaran satu bulan (sisa kas + denda) lalu menandainya LUNAS. Hanya akun ADMIN |
| `processPaymentRange` | Mencatat beberapa bulan sekaligus (dari bulan sampai bulan). Hanya akun ADMIN |
