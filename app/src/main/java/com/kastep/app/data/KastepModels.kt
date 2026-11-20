// ==========================================================================
// PETA FILE · KASTEP (Kelompok 1) · KastepModels.kt
// Isi file : Bentuk data: akun, siswa, pembayaran, pengeluaran.
// Penanda  : CARI-LOGIN, CARI-PROFIL, CARI-ANGGOTA, CARI-DISPENSASI, CARI-PEMBAYARAN, CARI-PENGELUARAN
// Cara cari: VS Code Ctrl+Shift+F (Android Studio Ctrl+Shift+F) lalu ketik
//            "CARI-" untuk semua penanda, atau salah satu nama di baris Penanda.
// ==========================================================================
package com.kastep.app.data

enum class UserRole {
    ADMIN, USER
}

// CARI-LOGIN CARI-PROFIL | Data akun yang sedang login (role ADMIN atau USER).
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

// CARI-ANGGOTA CARI-DISPENSASI | bayar = minggu lunas per bulan; dispensasi = minggu yang dibebaskan (BI/PKL).
/**
 * Kas dibayar Rp5.000 per minggu (setiap Senin). [bayar] menyimpan jumlah minggu yang sudah
 * lunas per bulan, misalnya "Agustus 2026" -> 3. [dispensasi] = minggu yang dibebaskan
 * (mis. sedang BI/PKL); minggu dispensasi ikut dihitung lunas tetapi tidak menambah pemasukan.
 */
data class Siswa(
    val no: Int,
    val nama: String,
    val peran: String,
    val bayar: Map<String, Int> = emptyMap(),
    val dispensasi: Map<String, Int> = emptyMap(),
    val kelas: String = "XII PPLG",
    val noHp: String = ""
)

// CARI-PEMBAYARAN | Satu baris riwayat pembayaran; kolom denda menyimpan denda yang ikut dibayar.
data class PaymentRecord(
    val no: Int,
    val tanggal: String,
    val namaSiswa: String,
    val jumlah: Long,
    val bulan: String = "Juli 2026",
    val metode: String = "Cash",
    val denda: Long = 0
)

// CARI-PENGELUARAN | Satu pengeluaran; tanggal berformat "15 Juli 2026".
data class Pengeluaran(
    val id: String = java.util.UUID.randomUUID().toString(),
    val tanggal: String,
    val jumlah: Long,
    val keterangan: String
)

data class MataPelajaran(
    val nama: String
)
