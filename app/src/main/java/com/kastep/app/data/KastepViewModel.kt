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
package com.kastep.app.data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.NumberFormat
import java.util.Locale

// AndroidViewModel membawa Application (konteks aplikasi) supaya repository bisa menyimpan data ke HP.
// Repository dibuat di sini sekali, lalu semua layar memakai ViewModel yang sama (dibagikan lewat MainActivity dan NavGraph).
// CARI-PUSAT-DATA | Layar memanggil fungsi di sini; isi logikanya ada di KastepRepository.
class KastepViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = KastepRepository(application.applicationContext)

    val isLoggedIn: StateFlow<Boolean> = repository.isLoggedIn
    val userProfile: StateFlow<UserProfile> = repository.userProfile
    val students: StateFlow<List<Siswa>> = repository.students
    val paymentRecords: StateFlow<List<PaymentRecord>> = repository.paymentRecords
    val pengeluaranList: StateFlow<List<Pengeluaran>> = repository.pengeluaranList
    val mataPelajaran: List<MataPelajaran> = repository.mataPelajaran

    // Tiga angka utama Dashboard dan Laporan:
    //   totalIncome  = jumlahkan semua pembayaran di riwayat (kas + denda yang pernah dibayar)
    //   totalExpense = jumlahkan semua pengeluaran
    //   saldo        = totalIncome - totalExpense   (data awal Rp1.389.000)
    // Memakai get() sehingga selalu dihitung dari data terbaru.
    val totalIncome: Long
        get() = paymentRecords.value.sumOf { it.jumlah }

    val totalExpense: Long
        get() = pengeluaranList.value.sumOf { it.jumlah }

    // CARI-SALDO | Saldo = total pemasukan - total pengeluaran (dipakai Dashboard).
    // TANYA-GURU: Saldo dihitung dari mana? Total semua pembayaran (kas + donasi) - total pengeluaran = Rp1.389.000.
    val saldo: Long
        get() = totalIncome - totalExpense

    // Auth
    // Login: diteruskan ke repository, hasil null = berhasil, teks = pesan kesalahan.
    fun login(email: String, password: String): String? = repository.login(email, password)
    // Daftar akun baru (selalu berperan USER).
    fun register(nama: String, email: String, nis: String, kelas: String, noHp: String, password: String): String? =
        repository.register(nama, email, nis, kelas, noHp, password)
    // Keluar dari akun.
    fun logout() = repository.logout()
    // Simpan perubahan nama dan No. HP.
    fun updateProfile(nama: String, noHp: String): String? = repository.updateProfile(nama, noHp)

    // Student CRUD
    // Tambah siswa (Admin).
    fun addStudent(nama: String, peran: String): String? = repository.addStudent(nama, peran)
    // Ubah nama atau peran siswa (Admin).
    fun updateStudent(no: Int, nama: String, peran: String): String? = repository.updateStudent(no, nama, peran)
    // Hapus siswa (Admin).
    fun deleteStudent(no: Int) = repository.deleteStudent(no)

    // Payments
    // Catat pembayaran satu bulan (khusus Admin): sisa kas + denda, bulan itu jadi LUNAS.
    fun processPayment(studentName: String, bulan: String, metode: String): String? =
        repository.processPayment(studentName, bulan, metode)

    // Jumlah baris riwayat yang dibuat oleh pencatatan terakhir. Dipakai layar Pembayaran Berhasil untuk menjumlahkan rentang bulan.
    private val _lastPaymentCount = MutableStateFlow(1)
    val lastPaymentCount: StateFlow<Int> = _lastPaymentCount.asStateFlow()

    // Catat rentang bulan sekaligus (khusus Admin), misalnya Juli sampai September. Hasil null = berhasil.
    fun processPaymentRange(studentName: String, bulanList: List<String>, metode: String): String? {
        val before = repository.paymentRecords.value.size
        val result = repository.processPaymentRange(studentName, bulanList, metode)
        if (result == null) _lastPaymentCount.value = (repository.paymentRecords.value.size - before).coerceAtLeast(1)
        return result
    }
    // Denda satu bulan tagihan untuk satu siswa.
    fun calculateDenda(studentName: String, bulan: String): Long =
        repository.calculateDenda(studentName, bulan)
    // Daftar bulan tagihan: Juli 2026 sampai bulan sekarang.
    fun getBulanKas(): List<String> = repository.getBulanKas()
    // LUNAS atau BELUM untuk satu siswa di satu bulan.
    fun statusBulan(siswa: Siswa, bulan: String): StatusBayar = repository.statusBulan(siswa, bulan)
    // Jumlah minggu lunas (dibayar + dispensasi) satu bulan.
    fun mingguLunas(siswa: Siswa, bulan: String): Int = repository.mingguLunas(siswa, bulan)
    // Kekurangan kas satu bulan dalam rupiah.
    fun sisaKas(siswa: Siswa, bulan: String): Long = repository.sisaKas(siswa, bulan)
    // Total denda semua bulan untuk satu siswa.
    fun totalDenda(siswa: Siswa): Long = repository.totalDenda(siswa)
    // Jumlah hari Senin (minggu kas) dalam satu bulan.
    fun jumlahMinggu(bulan: String): Int = KastepRepository.jumlahMinggu(bulan)

    // Pengeluaran
    // Tambah pengeluaran (Admin).
    fun addPengeluaran(tanggal: String, jumlah: Long, keterangan: String): String? =
        repository.addPengeluaran(tanggal, jumlah, keterangan)
    // Ubah pengeluaran (Admin).
    fun updatePengeluaran(id: String, tanggal: String, jumlah: Long, keterangan: String): String? =
        repository.updatePengeluaran(id, tanggal, jumlah, keterangan)
    // Hapus pengeluaran (Admin).
    fun deletePengeluaran(id: String) = repository.deletePengeluaran(id)

    // Misc
    // Kembalikan data ke buku kas awal (belum ada tombolnya di aplikasi).
    fun resetData() = repository.resetData()
    // Teks laporan untuk tombol Bagikan Laporan (tanpa nama siswa).
    fun generateReportText(): String = repository.generateReportText()
    // Tanggal hari ini untuk bar atas dan transaksi.
    fun getCurrentDateString(): String = repository.getCurrentDateString()

    companion object {
        // Mengubah 1389000 jadi "1.389.000" (pemisah ribuan titik gaya Indonesia). Layar menambahkan "Rp" sendiri.
        // CARI-FORMAT-RUPIAH | Mengubah 1389000 menjadi "1.389.000".
        fun formatRupiah(amount: Long): String {
            val formatter = NumberFormat.getInstance(Locale("id", "ID"))
            return formatter.format(amount)
        }
    }
}
