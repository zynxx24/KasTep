// ==========================================================================
// PETA FILE · KASTEP (Kelompok 1) · KastepViewModel.kt
// Isi file : Penghubung layar dengan repository.
// Penanda  : CARI-PUSAT-DATA, CARI-SALDO, CARI-FORMAT-RUPIAH
// Cara cari: VS Code Ctrl+Shift+F (Android Studio Ctrl+Shift+F) lalu ketik
//            "CARI-" untuk semua penanda, atau salah satu nama di baris Penanda.
// ==========================================================================
//  BACA INI DULU
//  ViewModel = perantara antara layar dan data. Layar tidak menyentuh data langsung:
//     Layar (Dashboard dll)  ->  KastepViewModel (file ini)  ->  KastepRepository (data + rumus)
//  Isi file: daftar data "hidup" (StateFlow, layar ikut berubah otomatis saat data berubah), tiga angka ringkas
//  (totalIncome, totalExpense, saldo), dan fungsi tipis yang hanya meneruskan ke repository.
//  Kalau ditanya "rumus saldo di mana?": val saldo di file ini = total pemasukan dikurangi total pengeluaran.
// » package com.kastep.app.data = alamat folder file ini (data dan logika).
package com.kastep.app.data

// » Application = pegangan ke seluruh aplikasi, dibutuhkan untuk menyimpan data ke HP.
import android.app.Application
// » AndroidViewModel = penghubung layar dengan data yang tetap hidup saat layar diputar.
import androidx.lifecycle.AndroidViewModel
// » MutableStateFlow = wadah nilai yang bisa berubah dan diamati layar.
import kotlinx.coroutines.flow.MutableStateFlow
// » StateFlow = versi hanya-baca dari wadah tadi (dipakai layar untuk membaca).
import kotlinx.coroutines.flow.StateFlow
// » asStateFlow = mengubah MutableStateFlow menjadi StateFlow hanya-baca.
import kotlinx.coroutines.flow.asStateFlow
// » NumberFormat = alat pemformat angka (mis. 1.389.000).
import java.text.NumberFormat
// » Locale = pengaturan bahasa/negara untuk format angka.
import java.util.Locale

// » Komentar: ViewModel membawa Application supaya repository bisa menyimpan data.
// AndroidViewModel membawa Application (konteks aplikasi) supaya repository bisa menyimpan data ke HP.
// Repository dibuat di sini sekali, lalu semua layar memakai ViewModel yang sama (dibagikan lewat MainActivity dan NavGraph).
// CARI-PUSAT-DATA | Layar memanggil fungsi di sini; isi logikanya ada di KastepRepository.
// » class KastepViewModel = perantara antara layar dan repository. Dibuat sekali dan dipakai semua layar.
class KastepViewModel(application: Application) : AndroidViewModel(application) {

    // » repository = objek yang benar-benar menyimpan dan menghitung data. Dibuat sekali di sini.
    private val repository = KastepRepository(application.applicationContext)

    // » isLoggedIn = status login (dibaca layar, berubah otomatis).
    val isLoggedIn: StateFlow<Boolean> = repository.isLoggedIn
    // » userProfile = data akun yang sedang login.
    val userProfile: StateFlow<UserProfile> = repository.userProfile
    // » students = daftar semua siswa.
    val students: StateFlow<List<Siswa>> = repository.students
    // » paymentRecords = riwayat pembayaran.
    val paymentRecords: StateFlow<List<PaymentRecord>> = repository.paymentRecords
    // » pengeluaranList = daftar pengeluaran kas.
    val pengeluaranList: StateFlow<List<Pengeluaran>> = repository.pengeluaranList
    // » mataPelajaran = daftar mata pelajaran (tidak berubah, jadi bukan StateFlow).
    val mataPelajaran: List<MataPelajaran> = repository.mataPelajaran

    // Tiga angka utama Dashboard dan Laporan:
    //   totalIncome  = jumlahkan semua pembayaran di riwayat (kas + denda yang pernah dibayar)
    //   totalExpense = jumlahkan semua pengeluaran
    //   saldo        = totalIncome - totalExpense   (data awal Rp1.389.000)
    // Memakai get() sehingga selalu dihitung dari data terbaru.
    // » totalIncome = total semua pembayaran di riwayat (dihitung ulang tiap kali dibaca).
    val totalIncome: Long
        // » sumOf { it.jumlah } = jumlahkan kolom jumlah dari setiap riwayat.
        get() = paymentRecords.value.sumOf { it.jumlah }

    // » totalExpense = total semua pengeluaran.
    val totalExpense: Long
        // » sumOf { it.jumlah } = jumlahkan semua pengeluaran.
        get() = pengeluaranList.value.sumOf { it.jumlah }

    // CARI-SALDO | Saldo = total pemasukan - total pengeluaran (dipakai Dashboard).
    // TANYA-GURU: Saldo dihitung dari mana? Total semua pembayaran (kas + donasi) - total pengeluaran = Rp1.389.000.
    // » saldo = saldo kas = total pemasukan dikurangi total pengeluaran.
    val saldo: Long
        // » Rumus: totalIncome - totalExpense.
        get() = totalIncome - totalExpense

    // Auth
    // Login: diteruskan ke repository, hasil null = berhasil, teks = pesan kesalahan.
    // » login = meneruskan email dan password ke repository. Hasil null = berhasil; teks = pesan error.
    fun login(email: String, password: String): String? = repository.login(email, password)
    // Daftar akun baru (selalu berperan USER).
    // » register = meneruskan data pendaftaran ke repository. Akun baru selalu berperan USER.
    fun register(nama: String, email: String, nis: String, kelas: String, noHp: String, password: String): String? =
        // » Teruskan semua isian ke repository.
        repository.register(nama, email, nis, kelas, noHp, password)
    // Keluar dari akun.
    // » logout = keluar dari akun (diteruskan ke repository).
    fun logout() = repository.logout()
    // Simpan perubahan nama dan No. HP.
    // » updateProfile = ubah nama dan nomor HP akun. Hasil null = berhasil.
    fun updateProfile(nama: String, noHp: String): String? = repository.updateProfile(nama, noHp)

    // Student CRUD
    // Tambah siswa (Admin).
    // » addStudent = tambah siswa baru (khusus admin). peran = jabatan siswa.
    fun addStudent(nama: String, peran: String): String? = repository.addStudent(nama, peran)
    // Ubah nama atau peran siswa (Admin).
    // » updateStudent = ubah nama atau jabatan siswa dengan nomor urut no.
    fun updateStudent(no: Int, nama: String, peran: String): String? = repository.updateStudent(no, nama, peran)
    // Hapus siswa (Admin).
    // » deleteStudent = hapus siswa dengan nomor urut no.
    fun deleteStudent(no: Int) = repository.deleteStudent(no)

    // Payments
    // Catat pembayaran satu bulan (khusus Admin): sisa kas + denda, bulan itu jadi LUNAS.
    // » processPayment = catat pembayaran satu bulan untuk satu siswa (khusus admin). metode = cara bayar.
    fun processPayment(studentName: String, bulan: String, metode: String): String? =
        // » Teruskan ke repository.
        repository.processPayment(studentName, bulan, metode)

    // Jumlah baris riwayat yang dibuat oleh pencatatan terakhir. Dipakai layar Pembayaran Berhasil untuk menjumlahkan rentang bulan.
    // » _lastPaymentCount = jumlah baris riwayat yang dibuat pencatatan terakhir. Awalnya 1. Garis bawah (_) =
    // » variabel internal.
    private val _lastPaymentCount = MutableStateFlow(1)
    // » lastPaymentCount = versi hanya-baca untuk layar Pembayaran Berhasil.
    val lastPaymentCount: StateFlow<Int> = _lastPaymentCount.asStateFlow()

    // Catat rentang bulan sekaligus (khusus Admin), misalnya Juli sampai September. Hasil null = berhasil.
    // » processPaymentRange = catat beberapa bulan sekaligus (khusus admin), misalnya Juli sampai September.
    fun processPaymentRange(studentName: String, bulanList: List<String>, metode: String): String? {
        // » before = jumlah riwayat sebelum pencatatan.
        val before = repository.paymentRecords.value.size
        // » Teruskan ke repository dan simpan hasilnya.
        val result = repository.processPaymentRange(studentName, bulanList, metode)
        // » Kalau berhasil (null): hitung berapa baris baru = selisih jumlah riwayat. coerceAtLeast(1) = minimal 1.
        if (result == null) _lastPaymentCount.value = (repository.paymentRecords.value.size - before).coerceAtLeast(1)
        // » Kembalikan hasil (null = berhasil).
        return result
    }
    // Denda satu bulan tagihan untuk satu siswa.
    // » calculateDenda = hitung denda satu bulan untuk satu siswa (diteruskan ke repository).
    fun calculateDenda(studentName: String, bulan: String): Long =
        repository.calculateDenda(studentName, bulan)
    // Daftar bulan tagihan: Juli 2026 sampai bulan sekarang.
    // » getBulanKas = daftar bulan tagihan dari Juli 2026 sampai bulan sekarang.
    fun getBulanKas(): List<String> = repository.getBulanKas()
    // LUNAS atau BELUM untuk satu siswa di satu bulan.
    // » statusBulan = status LUNAS atau BELUM satu siswa di satu bulan.
    fun statusBulan(siswa: Siswa, bulan: String): StatusBayar = repository.statusBulan(siswa, bulan)
    // Jumlah minggu lunas (dibayar + dispensasi) satu bulan.
    // » mingguLunas = berapa minggu sudah lunas (dibayar + dispensasi) di satu bulan.
    fun mingguLunas(siswa: Siswa, bulan: String): Int = repository.mingguLunas(siswa, bulan)
    // Kekurangan kas satu bulan dalam rupiah.
    // » sisaKas = sisa kas yang belum dibayar satu bulan dalam rupiah.
    fun sisaKas(siswa: Siswa, bulan: String): Long = repository.sisaKas(siswa, bulan)
    // Total denda semua bulan untuk satu siswa.
    // » totalDenda = total denda semua bulan untuk satu siswa.
    fun totalDenda(siswa: Siswa): Long = repository.totalDenda(siswa)
    // Jumlah hari Senin (minggu kas) dalam satu bulan.
    // » jumlahMinggu = jumlah Senin di bulan itu (dihitung oleh repository, fungsi milik kelas).
    fun jumlahMinggu(bulan: String): Int = KastepRepository.jumlahMinggu(bulan)

    // Pengeluaran
    // Tambah pengeluaran (Admin).
    // » addPengeluaran = tambah pengeluaran baru (khusus admin).
    fun addPengeluaran(tanggal: String, jumlah: Long, keterangan: String): String? =
        repository.addPengeluaran(tanggal, jumlah, keterangan)
    // Ubah pengeluaran (Admin).
    // » updatePengeluaran = ubah pengeluaran yang sudah ada, dicari dengan id.
    fun updatePengeluaran(id: String, tanggal: String, jumlah: Long, keterangan: String): String? =
        repository.updatePengeluaran(id, tanggal, jumlah, keterangan)
    // Hapus pengeluaran (Admin).
    // » deletePengeluaran = hapus pengeluaran dengan id.
    fun deletePengeluaran(id: String) = repository.deletePengeluaran(id)

    // Misc
    // Kembalikan data ke buku kas awal (belum ada tombolnya di aplikasi).
    // » resetData = kembalikan semua data ke keadaan buku kas awal (belum ada tombolnya).
    fun resetData() = repository.resetData()
    // Teks laporan untuk tombol Bagikan Laporan (tanpa nama siswa).
    // » generateReportText = buat teks laporan untuk dibagikan (tanpa nama siswa).
    fun generateReportText(): String = repository.generateReportText()
    // Tanggal hari ini untuk bar atas dan transaksi.
    // » getCurrentDateString = tanggal hari ini sebagai teks (untuk bar atas dan transaksi).
    fun getCurrentDateString(): String = repository.getCurrentDateString()

    // » companion object = bagian milik kelas, bukan milik tiap objek. Berisi fungsi bantu.
    companion object {
        // » Komentar fungsi formatRupiah.
        // Mengubah 1389000 jadi "1.389.000" (pemisah ribuan titik gaya Indonesia). Layar menambahkan "Rp" sendiri.
        // CARI-FORMAT-RUPIAH | Mengubah 1389000 menjadi "1.389.000".
        // » formatRupiah = mengubah angka jadi teks ber-titik ribuan (contoh 1389000 jadi 1.389.000).
        fun formatRupiah(amount: Long): String {
            // » NumberFormat.getInstance(Locale("id","ID")) = pemformat angka gaya Indonesia.
            val formatter = NumberFormat.getInstance(Locale("id", "ID"))
            // » format(amount) = ubah angka jadi teks sesuai format.
            return formatter.format(amount)
        }
    }
}
