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
    val transactions: StateFlow<List<Transaction>> = repository.transactions
    val students: StateFlow<List<Siswa>> = repository.students
    val paymentRecords: StateFlow<List<PaymentRecord>> = repository.paymentRecords
    val mataPelajaran: List<MataPelajaran> = repository.mataPelajaran

    val totalIncome: Long
        get() = transactions.value.filter { it.type == TransactionType.INCOME }.sumOf { it.amount }

    val totalExpense: Long
        get() = transactions.value.filter { it.type == TransactionType.EXPENSE }.sumOf { it.amount }

    val saldo: Long
        get() = totalIncome - totalExpense

    fun login(nameOrEmail: String, password: String) {
        repository.login(nameOrEmail, password)
    }

    fun register(nama: String, nis: String, kelas: String, noHp: String, password: String) {
        repository.register(nama, nis, kelas, noHp, password)
    }

    fun logout() {
        repository.logout()
    }

    fun addTransaction(title: String, amount: Long, type: TransactionType) {
        repository.addTransaction(title, amount, type)
    }

    fun deleteTransaction(id: String) {
        repository.deleteTransaction(id)
    }

    fun resetData() {
        repository.resetData()
    }

    fun generateReportText(): String {
        return repository.generateReportText()
    }

    fun getCurrentDateString(): String {
        return repository.getCurrentDateString()
    }

    companion object {
        fun formatRupiah(amount: Long): String {
            val formatter = NumberFormat.getInstance(Locale("id", "ID"))
            return formatter.format(amount)
        }
    }
}
