// ==========================================================================
// PETA FILE · KASTEP (Kelompok 1) · KastepModels.kt
// Isi file : Bentuk data: akun, siswa, pembayaran, pengeluaran.
// Penanda  : CARI-LOGIN, CARI-PROFIL, CARI-ANGGOTA, CARI-DISPENSASI, CARI-PEMBAYARAN, CARI-PENGELUARAN
// Cara cari: VS Code Ctrl+Shift+F (Android Studio Ctrl+Shift+F) lalu ketik
//            "CARI-" untuk semua penanda, atau salah satu nama di baris Penanda.
// ==========================================================================
//  BACA INI DULU
//  File ini hanya "cetakan data" (tanpa tampilan, tanpa hitungan). Seperti kolom di buku kas:
//    UserProfile   = akun yang login (nama, email, password, peran ADMIN atau USER)
//    Siswa         = 1 siswa dengan minggu yang sudah dibayar dan minggu dispensasi per bulan
//    PaymentRecord = 1 baris riwayat pembayaran (siapa, berapa, bulan apa, metode, denda)
//    Pengeluaran   = 1 pengeluaran kas
//    MataPelajaran = nama mapel untuk layar jadwal
// » package com.kastep.app.data = alamat folder file ini. Folder "data" berisi bentuk data dan logika
// » penyimpanan.
package com.kastep.app.data

// » Komentar: enum UserRole berisi dua peran.
// Dua peran. ADMIN = bendahara (bisa kelola siswa dan pengeluaran, melihat semua status). USER = siswa biasa (hanya status miliknya).
// » enum class UserRole = daftar pilihan tetap (tidak bisa diubah saat jalan).
enum class UserRole {
    // » ADMIN = bendahara: kelola siswa dan pengeluaran, melihat semua status. USER = siswa: hanya melihat
    // » status miliknya.
    ADMIN, USER
}

// » Komentar: data akun yang sedang login.
// CARI-LOGIN CARI-PROFIL | Data akun yang sedang login (role ADMIN atau USER).
// » data class UserProfile = cetakan data akun. "data class" = kelas khusus untuk menyimpan data.
data class UserProfile(
    // » nama = nama akun, awalnya "Admin".
    val nama: String = "Admin",
    // » email = email akun (kosong bila belum ada).
    val email: String = "",
    // » nis = nomor induk siswa.
    val nis: String = "",
    // » kelas = nama kelas, awalnya "XII PPLG".
    val kelas: String = "XII PPLG",
    // » noHp = nomor WhatsApp akun.
    val noHp: String = "",
    // » password = kata sandi akun (disimpan di data model ini).
    val password: String = "",
    // » role = peran akun; awalnya USER (siswa).
    val role: UserRole = UserRole.USER
)

// » enum class StatusBayar = dua pilihan status: LUNAS atau BELUM.
enum class StatusBayar {
    LUNAS, BELUM
}

// » Penjelasan: status bayar disimpan per BULAN.
// Satu siswa. Status bayar disimpan PER BULAN, bukan per transaksi:
// » Contoh: bayar["Agustus 2026"] = 5 berarti 5 Senin di Agustus sudah dibayar.
//   bayar["Agustus 2026"] = 5   artinya 5 Senin di bulan Agustus sudah dibayar
// » Contoh: dispensasi["Juli 2026"] = 4 berarti 4 Senin di Juli dibebaskan (BI/PKL).
//   dispensasi["Juli 2026"] = 4 artinya 4 Senin di Juli dibebaskan (BI/PKL), dihitung sudah lunas tetapi tidak menambah pemasukan
// » Bulan dianggap LUNAS bila minggu dibayar + minggu dispensasi >= jumlah Senin di bulan itu.
// Bulan LUNAS bila minggu dibayar + minggu dispensasi >= jumlah Senin bulan itu (lihat statusBulan di repository).
// » Penanda untuk mencari: anggota dan dispensasi.
// CARI-ANGGOTA CARI-DISPENSASI | bayar = minggu lunas per bulan; dispensasi = minggu yang dibebaskan (BI/PKL).
// » KDoc (komentar dokumentasi) kelas Siswa di bawah.
/**
 // » Kas Rp5.000 per minggu (setiap Senin).
 * Kas dibayar Rp5.000 per minggu (setiap Senin). [bayar] menyimpan jumlah minggu yang sudah
 // » bayar = jumlah minggu lunas per bulan.
 * lunas per bulan, misalnya "Agustus 2026" -> 3. [dispensasi] = minggu yang dibebaskan
 // » dispensasi = minggu yang dibebaskan; dihitung lunas tapi tidak menambah pemasukan.
 * (mis. sedang BI/PKL); minggu dispensasi ikut dihitung lunas tetapi tidak menambah pemasukan.
 */
// » data class Siswa = cetakan data satu siswa.
data class Siswa(
    // » no = nomor urut siswa.
    val no: Int,
    // » nama = nama siswa.
    val nama: String,
    // » peran = jabatan siswa (mis. Ketua Kelas, Anggota).
    val peran: String,
    // » bayar = peta: nama bulan -> jumlah minggu yang sudah dibayar. emptyMap() = kosong di awal.
    val bayar: Map<String, Int> = emptyMap(),
    // » dispensasi = peta: nama bulan -> jumlah minggu dispensasi.
    val dispensasi: Map<String, Int> = emptyMap(),
    // » kelas = nama kelas siswa.
    val kelas: String = "XII PPLG",
    // » noHp = nomor WhatsApp siswa.
    val noHp: String = ""
)

// » Komentar: satu baris riwayat pembayaran. jumlah = kas + denda yang dibayar; denda = bagian denda di
// » dalam jumlah.
// Satu baris Riwayat Pembayaran. jumlah = kas + denda yang dibayar saat itu, denda = bagian denda di dalam jumlah (untuk ditampilkan terpisah).
// CARI-PEMBAYARAN | Satu baris riwayat pembayaran; kolom denda menyimpan denda yang ikut dibayar.
// » data class PaymentRecord = cetakan data satu riwayat pembayaran.
data class PaymentRecord(
    // » no = nomor urut riwayat.
    val no: Int,
    // » tanggal = tanggal pembayaran (teks).
    val tanggal: String,
    // » namaSiswa = nama siswa yang membayar.
    val namaSiswa: String,
    // » jumlah = total rupiah yang dibayar (Long = bilangan bulat besar).
    val jumlah: Long,
    // » bulan = bulan yang dibayar, awalnya "Juli 2026".
    val bulan: String = "Juli 2026",
    // » metode = cara bayar, awalnya "Cash".
    val metode: String = "Cash",
    // » denda = bagian denda di dalam jumlah, awalnya 0.
    val denda: Long = 0
)

// » Komentar: satu pengeluaran kas. Tanggal ditulis seperti "15 Juli 2026".
// CARI-PENGELUARAN | Satu pengeluaran; tanggal berformat "15 Juli 2026".
// » data class Pengeluaran = cetakan data satu pengeluaran.
data class Pengeluaran(
    // » id = kode unik. java.util.UUID.randomUUID() = membuat kode acak yang hampir pasti tidak sama dengan kode
    // » lain.
    val id: String = java.util.UUID.randomUUID().toString(),
    // » tanggal = tanggal pengeluaran (teks).
    val tanggal: String,
    // » jumlah = rupiah yang dikeluarkan.
    val jumlah: Long,
    // » keterangan = untuk apa uang dikeluarkan.
    val keterangan: String
)

// » data class MataPelajaran = cetakan data satu mata pelajaran (dipakai layar jadwal).
data class MataPelajaran(
    // » nama = nama mata pelajaran.
    val nama: String
)
