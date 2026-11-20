// ==========================================================================
// PETA FILE · KASTEP (Kelompok 1) · KastepViewModel.kt
// Isi file : Penghubung layar dengan repository.
// Penanda  : CARI-PUSAT-DATA, CARI-SALDO, CARI-FORMAT-RUPIAH
// Cara cari: VS Code Ctrl+Shift+F (Android Studio Ctrl+Shift+F) lalu ketik
//            "CARI-" untuk semua penanda, atau salah satu nama di baris Penanda.
// ==========================================================================
package com.kastep.app.data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.flow.StateFlow
import java.text.NumberFormat
import java.util.Locale

// CARI-PUSAT-DATA | Layar memanggil fungsi di sini; isi logikanya ada di KastepRepository.
class KastepViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = KastepRepository(application.applicationContext)

    val isLoggedIn: StateFlow<Boolean> = repository.isLoggedIn
    val userProfile: StateFlow<UserProfile> = repository.userProfile
    val students: StateFlow<List<Siswa>> = repository.students
    val paymentRecords: StateFlow<List<PaymentRecord>> = repository.paymentRecords
    val pengeluaranList: StateFlow<List<Pengeluaran>> = repository.pengeluaranList
    val mataPelajaran: List<MataPelajaran> = repository.mataPelajaran

    val totalIncome: Long
        get() = paymentRecords.value.sumOf { it.jumlah }

    val totalExpense: Long
        get() = pengeluaranList.value.sumOf { it.jumlah }

    // CARI-SALDO | Saldo = total pemasukan - total pengeluaran (dipakai Dashboard).
    val saldo: Long
        get() = totalIncome - totalExpense

    // Auth
    fun login(email: String, password: String): String? = repository.login(email, password)
    fun register(nama: String, email: String, nis: String, kelas: String, noHp: String, password: String): String? =
        repository.register(nama, email, nis, kelas, noHp, password)
    fun logout() = repository.logout()
    fun updateProfile(nama: String, noHp: String): String? = repository.updateProfile(nama, noHp)

    // Student CRUD
    fun addStudent(nama: String, peran: String): String? = repository.addStudent(nama, peran)
    fun updateStudent(no: Int, nama: String, peran: String): String? = repository.updateStudent(no, nama, peran)
    fun deleteStudent(no: Int) = repository.deleteStudent(no)

    // Payments
    fun processPayment(studentName: String, bulan: String, metode: String): String? =
        repository.processPayment(studentName, bulan, metode)
    fun calculateDenda(studentName: String, bulan: String): Long =
        repository.calculateDenda(studentName, bulan)
    fun getBulanKas(): List<String> = repository.getBulanKas()
    fun statusBulan(siswa: Siswa, bulan: String): StatusBayar = repository.statusBulan(siswa, bulan)
    fun mingguLunas(siswa: Siswa, bulan: String): Int = repository.mingguLunas(siswa, bulan)
    fun sisaKas(siswa: Siswa, bulan: String): Long = repository.sisaKas(siswa, bulan)
    fun totalDenda(siswa: Siswa): Long = repository.totalDenda(siswa)
    fun jumlahMinggu(bulan: String): Int = KastepRepository.jumlahMinggu(bulan)

    // Pengeluaran
    fun addPengeluaran(tanggal: String, jumlah: Long, keterangan: String): String? =
        repository.addPengeluaran(tanggal, jumlah, keterangan)
    fun updatePengeluaran(id: String, tanggal: String, jumlah: Long, keterangan: String): String? =
        repository.updatePengeluaran(id, tanggal, jumlah, keterangan)
    fun deletePengeluaran(id: String) = repository.deletePengeluaran(id)

    // Misc
    fun resetData() = repository.resetData()
    fun generateReportText(): String = repository.generateReportText()
    fun getCurrentDateString(): String = repository.getCurrentDateString()

    companion object {
        // CARI-FORMAT-RUPIAH | Mengubah 1389000 menjadi "1.389.000".
        fun formatRupiah(amount: Long): String {
            val formatter = NumberFormat.getInstance(Locale("id", "ID"))
            return formatter.format(amount)
        }
    }
}
