package com.kastep.app.data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.flow.StateFlow
import java.text.NumberFormat
import java.util.Locale

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

    val saldo: Long
        get() = totalIncome - totalExpense

    // Auth
    fun login(email: String, password: String): String? = repository.login(email, password)
    fun register(nama: String, email: String, nis: String, kelas: String, noHp: String, password: String): String? =
        repository.register(nama, email, nis, kelas, noHp, password)
    fun logout() = repository.logout()

    // Student CRUD
    fun addStudent(nama: String, peran: String): String? = repository.addStudent(nama, peran)
    fun updateStudent(no: Int, nama: String, peran: String): String? = repository.updateStudent(no, nama, peran)
    fun deleteStudent(no: Int) = repository.deleteStudent(no)

    // Payments
    fun processPayment(studentName: String, bulan: String, metode: String, jumlah: Long = 20000): String? =
        repository.processPayment(studentName, bulan, metode, jumlah)

    // Pengeluaran
    fun addPengeluaran(tanggal: String, jumlah: Long, keterangan: String): String? =
        repository.addPengeluaran(tanggal, jumlah, keterangan)
    fun updatePengeluaran(id: String, tanggal: String, jumlah: Long, keterangan: String) =
        repository.updatePengeluaran(id, tanggal, jumlah, keterangan)
    fun deletePengeluaran(id: String) = repository.deletePengeluaran(id)

    // Misc
    fun resetData() = repository.resetData()
    fun generateReportText(): String = repository.generateReportText()
    fun getCurrentDateString(): String = repository.getCurrentDateString()

    companion object {
        fun formatRupiah(amount: Long): String {
            val formatter = NumberFormat.getInstance(Locale("id", "ID"))
            return formatter.format(amount)
        }
    }
}
