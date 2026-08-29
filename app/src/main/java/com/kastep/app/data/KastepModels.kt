package com.kastep.app.data

enum class UserRole {
    ADMIN, USER
}

data class UserProfile(
    val nama: String = "Admin",
    val email: String = "",
    val nis: String = "",
    val kelas: String = "XII PPLG",
    val noHp: String = "",
    val password: String = "",
    val role: UserRole = UserRole.USER
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
    val jumlah: Long,
    val bulan: String = "Juli 2026",
    val metode: String = "Cash"
)

data class Pengeluaran(
    val id: String = java.util.UUID.randomUUID().toString(),
    val tanggal: String,
    val jumlah: Long,
    val keterangan: String
)

data class MataPelajaran(
    val nama: String
)
