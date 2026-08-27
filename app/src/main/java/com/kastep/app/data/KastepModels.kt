package com.kastep.app.data

enum class TransactionType {
    INCOME, EXPENSE
}

data class Transaction(
    val id: String = java.util.UUID.randomUUID().toString(),
    val title: String,
    val amount: Long,
    val type: TransactionType,
    val date: String
)

data class UserProfile(
    val nama: String = "Admin",
    val nis: String = "",
    val kelas: String = "",
    val noHp: String = "+62 811 2222 4444",
    val password: String = ""
)

enum class StatusBayar {
    LUNAS, BELUM
}

data class Siswa(
    val no: Int,
    val nama: String,
    val peran: String,
    val statusJuli: StatusBayar = StatusBayar.LUNAS,
    val statusAgustus: StatusBayar = StatusBayar.BELUM,
    val kelas: String = "XII PPLG",
    val noHp: String = ""
)

data class PaymentRecord(
    val no: Int,
    val tanggal: String,
    val namaSiswa: String,
    val jumlah: Long
)

data class MataPelajaran(
    val nama: String
)
