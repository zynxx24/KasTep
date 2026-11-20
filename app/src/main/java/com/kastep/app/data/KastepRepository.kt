// ==========================================================================
// PETA FILE · KASTEP (Kelompok 1) · KastepRepository.kt
// Isi file : Pusat data offline: akun, siswa, status kas bulanan, denda, pembayaran, pengeluaran, laporan, penyimpanan.
// Penanda  : CARI-PUSAT-DATA, CARI-NOMINAL-KAS, CARI-DENDA, CARI-SIMPAN-DATA, CARI-LOGIN, CARI-VALIDASI, CARI-REGISTER, CARI-PROFIL, CARI-ANGGOTA, CARI-PEMBAYARAN, CARI-DISPENSASI, CARI-PENGELUARAN, CARI-DATA-AWAL, CARI-LAPORAN, CARI-SALDO
// Cara cari: VS Code Ctrl+Shift+F (Android Studio Ctrl+Shift+F) lalu ketik
//            "CARI-" untuk semua penanda, atau salah satu nama di baris Penanda.
// ==========================================================================
package com.kastep.app.data

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

// CARI-PUSAT-DATA | Semua perubahan data lewat sini; layar membaca lewat KastepViewModel.
class KastepRepository(context: Context? = null) {

    private val prefs: SharedPreferences? = context?.getSharedPreferences("kastep_prefs", Context.MODE_PRIVATE)

    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    private val _userProfile = MutableStateFlow(UserProfile())
    val userProfile: StateFlow<UserProfile> = _userProfile.asStateFlow()

    private val _students = MutableStateFlow<List<Siswa>>(emptyList())
    val students: StateFlow<List<Siswa>> = _students.asStateFlow()

    private val _paymentRecords = MutableStateFlow<List<PaymentRecord>>(emptyList())
    val paymentRecords: StateFlow<List<PaymentRecord>> = _paymentRecords.asStateFlow()

    private val _pengeluaranList = MutableStateFlow<List<Pengeluaran>>(emptyList())
    val pengeluaranList: StateFlow<List<Pengeluaran>> = _pengeluaranList.asStateFlow()

    val mataPelajaran = listOf(
        MataPelajaran("BAHASA INGGRIS"), MataPelajaran("PDL RPL"),
        MataPelajaran("BAHASA INDONESIA"), MataPelajaran("PABP HINDU"),
        MataPelajaran("BAHASA BALI"), MataPelajaran("PABP KRISTEN"),
        MataPelajaran("RPL"), MataPelajaran("PP"),
        MataPelajaran("KIK"), MataPelajaran("MAT")
    )

    // Akun terdaftar: email -> profil lengkap (termasuk password)
    private val _registeredUsers = mutableMapOf<String, UserProfile>()

    companion object {
        // CARI-NOMINAL-KAS | Iuran per minggu (setiap Senin). Tagihan satu bulan = jumlah Senin x nilai ini.
        const val KAS_MINGGUAN = 5000L
        // CARI-DENDA | Besar denda untuk setiap bulan keterlambatan. Ubah angka ini untuk mengganti tarif.
        const val DENDA_PER_BULAN = 5000L
        // CARI-SIMPAN-DATA | Kunci penyimpanan. Naikkan versinya bila data awal diganti supaya HP memuat ulang.
        private const val DATA_KEY = "saved_data_v3_pengurus"
        private val NAMA_BULAN = listOf(
            "Januari", "Februari", "Maret", "April", "Mei", "Juni",
            "Juli", "Agustus", "September", "Oktober", "November", "Desember"
        )
        private val BULAN_KAS_AWAL = listOf("Juli 2026", "Agustus 2026", "September 2026", "Oktober 2026")
        // CARI-LOGIN | Akun admin bawaan: admin@gmail.com / admin123.
        private val ADMIN = UserProfile(
            nama = "Administrator", email = "admin@gmail.com", password = "admin123",
            role = UserRole.ADMIN, kelas = "XII PPLG"
        )

        /** "Agustus 2026" -> (2026, 8) */
        fun parseBulan(label: String): Pair<Int, Int>? {
            val parts = label.trim().split(" ")
            if (parts.size != 2) return null
            val m = NAMA_BULAN.indexOfFirst { it.equals(parts[0], ignoreCase = true) } + 1
            val y = parts[1].toIntOrNull() ?: return null
            return if (m > 0) y to m else null
        }

        // CARI-NOMINAL-KAS | Menghitung jumlah hari Senin dalam sebulan (4 atau 5) = jumlah minggu kas.
        /** Jumlah hari Senin (hari kas) dalam satu bulan. */
        fun jumlahMinggu(bulan: String): Int {
            val (y, m) = parseBulan(bulan) ?: return 4
            val cal = Calendar.getInstance().apply { clear(); set(y, m - 1, 1) }
            val days = cal.getActualMaximum(Calendar.DAY_OF_MONTH)
            return (1..days).count { d -> cal.set(Calendar.DAY_OF_MONTH, d); cal.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY }
        }

        /** Tanggal "15 Juli 2026" atau "15/07/2026" -> (2026, 7) */
        fun bulanDariTanggal(tanggal: String): Pair<Int, Int>? {
            val t = tanggal.trim()
            Regex("""^\d{1,2}/(\d{1,2})/(\d{4})$""").find(t)?.let { return it.groupValues[2].toInt() to it.groupValues[1].toInt() }
            val parts = t.split(" ")
            return if (parts.size == 3) parseBulan("${parts[1]} ${parts[2]}") else null
        }

        /** Kunci urut tanggal (yyyyMMdd) untuk mengurutkan transaksi terbaru. */
        fun kunciTanggal(tanggal: String): Int {
            val t = tanggal.trim()
            Regex("""^(\d{1,2})/(\d{1,2})/(\d{4})$""").find(t)?.let { val g = it.groupValues; return g[3].toInt() * 10000 + g[2].toInt() * 100 + g[1].toInt() }
            val parts = t.split(" ")
            if (parts.size == 3) {
                val ym = parseBulan("${parts[1]} ${parts[2]}") ?: return 0
                return ym.first * 10000 + ym.second * 100 + (parts[0].toIntOrNull() ?: 0)
            }
            return 0
        }
    }

    init {
        _registeredUsers[ADMIN.email] = ADMIN
        loadData()
    }

    private fun loadData() {
        if (prefs != null && prefs.contains(DATA_KEY)) {
            loadFromPrefs()
        } else {
            loadInitialData()
            saveToPrefs()
        }
    }

    // ==================== AUTH ====================

    // CARI-LOGIN CARI-VALIDASI | Cek format email, panjang password (min. 5), lalu cocokkan dengan akun terdaftar.
    fun login(email: String, password: String): String? {
        val trimEmail = email.trim().lowercase()
        val trimPass = password.trim()

        if (trimEmail.isBlank()) return "Email tidak boleh kosong"
        if (trimPass.isBlank()) return "Password tidak boleh kosong"
        if (!trimEmail.contains("@") || !trimEmail.contains(".")) return "Format email tidak valid"
        if (trimPass.length < 5) return "Password minimal 5 karakter"

        val registered = _registeredUsers[trimEmail]
            ?: return "Akun tidak ditemukan. Silakan register terlebih dahulu."
        if (registered.password != trimPass) return "Password salah"

        _userProfile.value = registered
        _isLoggedIn.value = true
        saveToPrefs()
        return null
    }

    // CARI-REGISTER CARI-VALIDASI | Akun baru selalu ber-role USER; email tidak boleh dobel.
    fun register(nama: String, email: String, nis: String, kelas: String, noHp: String, password: String): String? {
        val trimNama = nama.trim()
        val trimEmail = email.trim().lowercase()
        val trimPass = password.trim()

        if (trimNama.isBlank()) return "Nama tidak boleh kosong"
        if (trimNama.length < 3) return "Nama minimal 3 karakter"
        if (trimEmail.isBlank()) return "Email tidak boleh kosong"
        if (!trimEmail.contains("@") || !trimEmail.contains(".")) return "Format email tidak valid"
        if (trimPass.isBlank()) return "Password tidak boleh kosong"
        if (trimPass.length < 5) return "Password minimal 5 karakter"
        if (_registeredUsers.containsKey(trimEmail)) return "Email sudah terdaftar"

        val profile = UserProfile(
            nama = trimNama, email = trimEmail, nis = nis.trim(),
            kelas = kelas.ifBlank { "XII PPLG" }, noHp = noHp.trim(),
            password = trimPass, role = UserRole.USER
        )
        _registeredUsers[trimEmail] = profile
        _userProfile.value = profile
        _isLoggedIn.value = true
        saveToPrefs()
        return null
    }

    // CARI-PROFIL | Simpan perubahan nama & No. HP dari dialog Edit Akun.
    fun updateProfile(nama: String, noHp: String): String? {
        val trimNama = nama.trim()
        if (trimNama.length < 3) return "Nama minimal 3 karakter"
        val updated = _userProfile.value.copy(nama = trimNama, noHp = noHp.trim())
        _userProfile.value = updated
        if (updated.email.isNotBlank()) _registeredUsers[updated.email] = updated
        saveToPrefs()
        return null
    }

    fun logout() {
        _userProfile.value = UserProfile()
        _isLoggedIn.value = false
        saveToPrefs()
    }

    // ==================== STUDENT CRUD ====================

    // CARI-ANGGOTA CARI-VALIDASI | Tambah siswa; nama disimpan huruf besar dan tidak boleh sama dengan siswa lain.
    fun addStudent(nama: String, peran: String): String? {
        val clean = nama.trim().uppercase()
        if (clean.isBlank()) return "Nama siswa tidak boleh kosong"
        if (_students.value.any { it.nama == clean }) return "Siswa dengan nama ini sudah ada"
        val nextNo = (_students.value.maxOfOrNull { it.no } ?: 0) + 1
        _students.value = _students.value + Siswa(no = nextNo, nama = clean, peran = peran.ifBlank { "Anggota" })
        saveToPrefs()
        return null
    }

    // CARI-ANGGOTA | Ganti nama/peran; riwayat pembayaran ikut memakai nama baru.
    fun updateStudent(no: Int, nama: String, peran: String): String? {
        val clean = nama.trim().uppercase()
        if (clean.isBlank()) return "Nama siswa tidak boleh kosong"
        if (_students.value.any { it.nama == clean && it.no != no }) return "Siswa dengan nama ini sudah ada"
        val oldName = _students.value.find { it.no == no }?.nama
        _students.value = _students.value.map {
            if (it.no == no) it.copy(nama = clean, peran = peran.ifBlank { "Anggota" }) else it
        }
        // Riwayat pembayaran ikut memakai nama baru agar tetap terhubung ke siswa
        if (oldName != null && oldName != clean) {
            _paymentRecords.value = _paymentRecords.value.map { if (it.namaSiswa == oldName) it.copy(namaSiswa = clean) else it }
        }
        saveToPrefs()
        return null
    }

    fun deleteStudent(no: Int) {
        _students.value = _students.value.filter { it.no != no }
            .mapIndexed { index, siswa -> siswa.copy(no = index + 1) }
        saveToPrefs()
    }

    // ==================== BULAN & STATUS KAS ====================

    // CARI-PEMBAYARAN | Daftar bulan yang ditagih: Juli 2026 sampai bulan berjalan (otomatis bertambah).
    /** Bulan kas semester ganjil: Juli 2026 sampai bulan berjalan. */
    fun getBulanKas(): List<String> {
        val cal = Calendar.getInstance()
        val nowKey = cal.get(Calendar.YEAR) * 12 + cal.get(Calendar.MONTH)
        val result = mutableListOf<String>()
        var key = 2026 * 12 + 6 // Juli 2026
        while (key <= nowKey || result.size < BULAN_KAS_AWAL.size) {
            result.add("${NAMA_BULAN[key % 12]} ${key / 12}")
            key++
        }
        return result
    }

    fun mingguLunas(siswa: Siswa, bulan: String): Int =
        ((siswa.bayar[bulan] ?: 0) + (siswa.dispensasi[bulan] ?: 0)).coerceAtMost(jumlahMinggu(bulan))

    // CARI-PEMBAYARAN CARI-DISPENSASI | LUNAS bila minggu dibayar + minggu dispensasi >= jumlah Senin bulan itu.
    fun statusBulan(siswa: Siswa, bulan: String): StatusBayar =
        if (mingguLunas(siswa, bulan) >= jumlahMinggu(bulan)) StatusBayar.LUNAS else StatusBayar.BELUM

    // CARI-PEMBAYARAN | Kekurangan kas satu bulan = minggu belum lunas x Rp5.000.
    /** Sisa kas yang belum dibayar untuk satu bulan. */
    fun sisaKas(siswa: Siswa, bulan: String): Long =
        (jumlahMinggu(bulan) - mingguLunas(siswa, bulan)).coerceAtLeast(0) * KAS_MINGGUAN

    private fun bulanTerlambat(bulan: String): Int {
        val (y, m) = parseBulan(bulan) ?: return 0
        val cal = Calendar.getInstance()
        val now = cal.get(Calendar.YEAR) * 12 + cal.get(Calendar.MONTH) + 1
        return (now - (y * 12 + m)).coerceAtLeast(0)
    }

    // CARI-DENDA | Rumus: Rp5.000 x jumlah bulan terlambat, hanya untuk bulan yang belum lunas.
    //   Contoh: kas Juli belum lunas, sekarang November -> 4 x 5.000 = Rp20.000.
    /**
     * Denda keterlambatan: Rp5.000 x jumlah bulan terlambat, untuk bulan yang belum lunas.
     * Contoh: kas Juli belum lunas dan sekarang November -> 4 bulan x Rp5.000 = Rp20.000.
     */
    fun calculateDenda(studentName: String, bulan: String): Long {
        val siswa = _students.value.find { it.nama == studentName } ?: return 0L
        if (statusBulan(siswa, bulan) == StatusBayar.LUNAS) return 0L
        return bulanTerlambat(bulan) * DENDA_PER_BULAN
    }

    fun totalDenda(siswa: Siswa): Long = getBulanKas().sumOf { calculateDenda(siswa.nama, it) }

    // CARI-PEMBAYARAN CARI-DENDA | Mencatat pembayaran: jumlah = sisa kas + denda, lalu bulan itu menjadi LUNAS.
    fun processPayment(studentName: String, bulan: String, metode: String): String? {
        if (studentName.isBlank()) return "Pilih siswa terlebih dahulu"
        if (bulan.isBlank()) return "Pilih bulan pembayaran"

        val siswa = _students.value.find { it.nama == studentName }
            ?: return "Siswa tidak ditemukan"
        if (statusBulan(siswa, bulan) == StatusBayar.LUNAS) return "$studentName sudah lunas untuk $bulan"

        val denda = calculateDenda(studentName, bulan)
        val sisa = sisaKas(siswa, bulan)
        val nextNo = (_paymentRecords.value.maxOfOrNull { it.no } ?: 0) + 1
        val record = PaymentRecord(
            no = nextNo, tanggal = getCurrentDateString(),
            namaSiswa = studentName, jumlah = sisa + denda,
            bulan = bulan, metode = metode, denda = denda
        )
        _paymentRecords.value = listOf(record) + _paymentRecords.value

        val wajib = jumlahMinggu(bulan) - (siswa.dispensasi[bulan] ?: 0)
        _students.value = _students.value.map {
            if (it.nama == studentName) it.copy(bayar = it.bayar + (bulan to wajib)) else it
        }
        saveToPrefs()
        return null
    }

    // ==================== PENGELUARAN ====================

    // CARI-PENGELUARAN CARI-VALIDASI | Tambah pengeluaran; nominal > 0 dan keterangan wajib.
    fun addPengeluaran(tanggal: String, jumlah: Long, keterangan: String): String? {
        if (jumlah <= 0) return "Jumlah harus lebih dari 0"
        if (keterangan.isBlank()) return "Keterangan tidak boleh kosong"
        val p = Pengeluaran(
            tanggal = tanggal.ifBlank { getCurrentDateString() },
            jumlah = jumlah, keterangan = keterangan.trim()
        )
        _pengeluaranList.value = listOf(p) + _pengeluaranList.value
        saveToPrefs()
        return null
    }

    // CARI-PENGELUARAN CARI-VALIDASI | Edit pengeluaran; tanggal harus terbaca (contoh 15 Juli 2026).
    fun updatePengeluaran(id: String, tanggal: String, jumlah: Long, keterangan: String): String? {
        if (jumlah <= 0) return "Jumlah harus lebih dari 0"
        if (keterangan.isBlank()) return "Keterangan tidak boleh kosong"
        if (bulanDariTanggal(tanggal) == null) return "Format tanggal tidak dikenali (contoh: 15 Juli 2026)"
        _pengeluaranList.value = _pengeluaranList.value.map {
            if (it.id == id) it.copy(tanggal = tanggal.trim(), jumlah = jumlah, keterangan = keterangan.trim()) else it
        }
        saveToPrefs()
        return null
    }

    fun deletePengeluaran(id: String) {
        _pengeluaranList.value = _pengeluaranList.value.filter { it.id != id }
        saveToPrefs()
    }

    // ==================== RESET / REPORT ====================

    // CARI-DATA-AWAL | Mengembalikan semua data ke buku kas awal (sesi admin tetap).
    fun resetData() {
        // Sesi admin tetap, data lain kembali ke buku kas awal
        val currentProfile = _userProfile.value
        val loggedIn = _isLoggedIn.value
        prefs?.edit()?.clear()?.apply()
        _registeredUsers.clear()
        _registeredUsers[ADMIN.email] = ADMIN
        loadInitialData()
        _userProfile.value = currentProfile
        _isLoggedIn.value = loggedIn
        saveToPrefs()
    }

    fun getCurrentDateString(): String {
        val sdf = SimpleDateFormat("dd MMMM yyyy", Locale("id", "ID"))
        return sdf.format(Date())
    }

    // CARI-LAPORAN CARI-SALDO | Teks laporan ringkas untuk tombol Bagikan Laporan (saldo = pemasukan - pengeluaran).
    fun generateReportText(): String {
        val totalIncome = _paymentRecords.value.sumOf { it.jumlah }
        val totalExpense = _pengeluaranList.value.sumOf { it.jumlah }
        val saldo = totalIncome - totalExpense
        val bulan = getBulanKas().filter { bulanTerlambat(it) > 0 }
        val rekapBulan = bulan.joinToString("\n") { b ->
            "• $b: ${_students.value.count { statusBulan(it, b) == StatusBayar.LUNAS }} lunas, " +
                "${_students.value.count { statusBulan(it, b) == StatusBayar.BELUM }} belum"
        }
        val totalDenda = _students.value.sumOf { totalDenda(it) }
        return """
            📊 LAPORAN KAS KELAS - KASTEP 📊
            Tanggal: ${getCurrentDateString()}
            Kelas: XII PPLG

            ------------------------------------
            💰 Total Pemasukan : Rp ${KastepViewModel.formatRupiah(totalIncome)}
            💸 Total Pengeluaran: Rp ${KastepViewModel.formatRupiah(totalExpense)}
            💵 Saldo Akhir      : Rp ${KastepViewModel.formatRupiah(saldo)}
            ⚠️ Total Denda Berjalan: Rp ${KastepViewModel.formatRupiah(totalDenda)}
            ------------------------------------
            👥 Total Siswa: ${_students.value.size} Siswa
        """.trimIndent() + "\n" + rekapBulan + "\n------------------------------------\nDibuat otomatis oleh KASTEP."
    }

    // Data awal sesuai buku kas XII PPLG per 20 November 2026 (Rp5.000/minggu, Juni libur)
    // CARI-DATA-AWAL | Data awal dari buku kas asli XII PPLG per 20 November 2026: siswa, minggu terbayar, riwayat, pengeluaran.
    private fun loadInitialData() {
        // minggu terbayar per bulan (Juli, Agustus, September, Oktober); minggu dispensasi BI/PKL dicatat terpisah
        val bayarAwal = listOf(
            Triple("BOYKE VILANO HAMONANGAN SIHITE", "Anggota", listOf(4, 5, 4, 0)),
            Triple("BINTANG LEONITA CHRISTYA RENATA", "Anggota", listOf(0, 0, 0, 0)),
            Triple("CAROLINA TIMUTHY JANGGUR", "Anggota", listOf(4, 5, 0, 0)),
            Triple("DEWA GEDE DALEM OKA ADNYANA SANDI", "Anggota", listOf(4, 5, 0, 0)),
            Triple("GALISTAN RAMADHAN KURNIA TAUNAES", "Ketua Kelas", listOf(4, 5, 4, 4)),
            Triple("GEDE AGUS WIRA DARMA PUTRA", "Bendahara 1", listOf(4, 5, 0, 0)),
            Triple("I GEDE ABI WIRYA DINATA", "Anggota", listOf(4, 0, 0, 0)),
            Triple("I GEDE DARMA SUPTIAWAN", "Anggota", listOf(4, 1, 0, 0)),
            Triple("I KOMANG RADITYA PUTRA", "Anggota", listOf(4, 1, 0, 0)),
            Triple("I KOMANG RISKI SETIAWAN", "Anggota", listOf(2, 0, 0, 0)),
            Triple("I NYOMAN GEDE ARTA WIGUNA", "Anggota", listOf(4, 1, 0, 0)),
            Triple("I PUTU DIKA LAKSMANA PUTRA", "Anggota", listOf(4, 4, 0, 0)),
            Triple("I PUTU DITYA ARTHA WIJAYA", "Anggota", listOf(2, 0, 0, 0)),
            Triple("I PUTU PANDE ANDIKA", "Anggota", listOf(4, 0, 0, 0)),
            Triple("I PUTU SUYOGA MAHENDRA", "Anggota", listOf(4, 2, 0, 0)),
            Triple("I WAYAN BAGUS PUTRAWAN", "Anggota", listOf(4, 5, 4, 4)),
            Triple("I WAYAN PASEK KEVIN ARIADI", "Anggota", listOf(4, 5, 4, 4)),
            Triple("KADEK YUDA PRASETYA", "Anggota", listOf(3, 0, 0, 0)),
            Triple("KADEK YUNI CALLISTA PUTRI DEWI", "Anggota", listOf(4, 4, 0, 0)),
            Triple("KOMANG DIAH PUTRI PRATIWI", "Anggota", listOf(4, 2, 0, 0)),
            Triple("LUH RIA MIRASIH", "Bendahara 2", listOf(4, 5, 4, 4)),
            Triple("NI KADEK ADELIA CAHYA KENCANA PUTRI", "Anggota", listOf(4, 0, 0, 0)),
            Triple("NI KADEK LINA ANTIKA DEWI", "Anggota", listOf(4, 5, 0, 0)),
            Triple("NI KOMANG KIRANA PARAMITA ARDANARI", "Anggota", listOf(4, 5, 4, 4)),
            Triple("NI KOMANG SEPTIARINI", "Anggota", listOf(4, 5, 1, 0)),
            Triple("NI LUH PUTU KESYA ASTRI MELANI", "Anggota", listOf(4, 5, 4, 0)),
            Triple("NI PUTU CAHAYA LESTARI DEWI", "Sekretaris 1", listOf(4, 5, 0, 0)),
            Triple("NI PUTU INTAN LESTARI DARMAYANTI", "Sekretaris 2", listOf(4, 4, 0, 0)),
            Triple("OKTA PRADIPTA ATTALA DZAKI", "Wakil Ketua", listOf(4, 5, 4, 4)),
            Triple("PUTU BAYU SATRIA WANGSA BUKIAN", "Anggota", listOf(2, 0, 0, 0)),
            Triple("PUTU NANDA LINDIA MAHARANI", "Anggota", listOf(4, 4, 0, 0)),
            Triple("PUTU PUTRI CAHYANI", "Anggota", listOf(4, 2, 0, 0)),
            Triple("RADITYA RONDI", "Anggota", listOf(4, 0, 0, 0))
        )
        // CARI-DISPENSASI | Minggu yang dibebaskan per siswa per bulan. Tambah: "NAMA" to mapOf("Bulan Tahun" to jumlahMinggu).
        val dispensasiAwal = mapOf(
            "BINTANG LEONITA CHRISTYA RENATA" to mapOf("Juli 2026" to 4, "Agustus 2026" to 5, "September 2026" to 2)
        )
        _students.value = bayarAwal.mapIndexed { i, (nama, peran, minggu) ->
            Siswa(
                no = i + 1, nama = nama, peran = peran,
                bayar = BULAN_KAS_AWAL.zip(minggu).toMap(),
                dispensasi = dispensasiAwal[nama].orEmpty()
            )
        }

        _paymentRecords.value = listOf(
            PaymentRecord(238, "26 Oktober 2026", "OKTA PRADIPTA ATTALA DZAKI", 20000, "Oktober 2026", "Cash"),
            PaymentRecord(237, "26 Oktober 2026", "NI KOMANG KIRANA PARAMITA ARDANARI", 20000, "Oktober 2026", "Cash"),
            PaymentRecord(236, "26 Oktober 2026", "LUH RIA MIRASIH", 20000, "Oktober 2026", "Cash"),
            PaymentRecord(235, "26 Oktober 2026", "I WAYAN PASEK KEVIN ARIADI", 20000, "Oktober 2026", "Cash"),
            PaymentRecord(234, "26 Oktober 2026", "I WAYAN BAGUS PUTRAWAN", 20000, "Oktober 2026", "Cash"),
            PaymentRecord(233, "26 Oktober 2026", "GALISTAN RAMADHAN KURNIA TAUNAES", 20000, "Oktober 2026", "Cash"),
            PaymentRecord(232, "28 September 2026", "OKTA PRADIPTA ATTALA DZAKI", 20000, "September 2026", "Cash"),
            PaymentRecord(231, "28 September 2026", "NI LUH PUTU KESYA ASTRI MELANI", 20000, "September 2026", "Cash"),
            PaymentRecord(230, "28 September 2026", "NI KOMANG KIRANA PARAMITA ARDANARI", 20000, "September 2026", "Cash"),
            PaymentRecord(229, "28 September 2026", "LUH RIA MIRASIH", 20000, "September 2026", "Cash"),
            PaymentRecord(228, "28 September 2026", "I WAYAN PASEK KEVIN ARIADI", 20000, "September 2026", "Cash"),
            PaymentRecord(227, "28 September 2026", "I WAYAN BAGUS PUTRAWAN", 20000, "September 2026", "Cash"),
            PaymentRecord(226, "28 September 2026", "GALISTAN RAMADHAN KURNIA TAUNAES", 20000, "September 2026", "Cash"),
            PaymentRecord(225, "28 September 2026", "BOYKE VILANO HAMONANGAN SIHITE", 20000, "September 2026", "Cash"),
            PaymentRecord(224, "07 September 2026", "NI KOMANG SEPTIARINI", 5000, "September 2026", "Cash"),
            PaymentRecord(223, "31 Agustus 2026", "OKTA PRADIPTA ATTALA DZAKI", 25000, "Agustus 2026", "Cash"),
            PaymentRecord(222, "31 Agustus 2026", "NI PUTU CAHAYA LESTARI DEWI", 25000, "Agustus 2026", "Cash"),
            PaymentRecord(221, "31 Agustus 2026", "NI LUH PUTU KESYA ASTRI MELANI", 25000, "Agustus 2026", "Cash"),
            PaymentRecord(220, "31 Agustus 2026", "NI KOMANG SEPTIARINI", 25000, "Agustus 2026", "Cash"),
            PaymentRecord(219, "31 Agustus 2026", "NI KOMANG KIRANA PARAMITA ARDANARI", 25000, "Agustus 2026", "Cash"),
            PaymentRecord(218, "31 Agustus 2026", "NI KADEK LINA ANTIKA DEWI", 25000, "Agustus 2026", "Cash"),
            PaymentRecord(217, "31 Agustus 2026", "LUH RIA MIRASIH", 25000, "Agustus 2026", "Cash"),
            PaymentRecord(216, "31 Agustus 2026", "I WAYAN PASEK KEVIN ARIADI", 25000, "Agustus 2026", "Cash"),
            PaymentRecord(215, "31 Agustus 2026", "I WAYAN BAGUS PUTRAWAN", 25000, "Agustus 2026", "Cash"),
            PaymentRecord(214, "31 Agustus 2026", "GEDE AGUS WIRA DARMA PUTRA", 25000, "Agustus 2026", "Cash"),
            PaymentRecord(213, "31 Agustus 2026", "GALISTAN RAMADHAN KURNIA TAUNAES", 25000, "Agustus 2026", "Cash"),
            PaymentRecord(212, "31 Agustus 2026", "DEWA GEDE DALEM OKA ADNYANA SANDI", 25000, "Agustus 2026", "Cash"),
            PaymentRecord(211, "31 Agustus 2026", "CAROLINA TIMUTHY JANGGUR", 25000, "Agustus 2026", "Cash"),
            PaymentRecord(210, "31 Agustus 2026", "BOYKE VILANO HAMONANGAN SIHITE", 25000, "Agustus 2026", "Cash"),
            PaymentRecord(209, "24 Agustus 2026", "PUTU NANDA LINDIA MAHARANI", 20000, "Agustus 2026", "Cash"),
            PaymentRecord(208, "24 Agustus 2026", "NI PUTU INTAN LESTARI DARMAYANTI", 20000, "Agustus 2026", "Cash"),
            PaymentRecord(207, "24 Agustus 2026", "KADEK YUNI CALLISTA PUTRI DEWI", 20000, "Agustus 2026", "Cash"),
            PaymentRecord(206, "24 Agustus 2026", "I PUTU DIKA LAKSMANA PUTRA", 20000, "Agustus 2026", "Cash"),
            PaymentRecord(205, "10 Agustus 2026", "PUTU PUTRI CAHYANI", 10000, "Agustus 2026", "Cash"),
            PaymentRecord(204, "10 Agustus 2026", "KOMANG DIAH PUTRI PRATIWI", 10000, "Agustus 2026", "Cash"),
            PaymentRecord(203, "10 Agustus 2026", "I PUTU SUYOGA MAHENDRA", 10000, "Agustus 2026", "Cash"),
            PaymentRecord(202, "03 Agustus 2026", "I NYOMAN GEDE ARTA WIGUNA", 5000, "Agustus 2026", "Cash"),
            PaymentRecord(201, "03 Agustus 2026", "I KOMANG RADITYA PUTRA", 5000, "Agustus 2026", "Cash"),
            PaymentRecord(200, "03 Agustus 2026", "I GEDE DARMA SUPTIAWAN", 5000, "Agustus 2026", "Cash"),
            PaymentRecord(199, "27 Juli 2026", "RADITYA RONDI", 20000, "Juli 2026", "Cash"),
            PaymentRecord(198, "27 Juli 2026", "PUTU PUTRI CAHYANI", 20000, "Juli 2026", "Cash"),
            PaymentRecord(197, "27 Juli 2026", "PUTU NANDA LINDIA MAHARANI", 20000, "Juli 2026", "Cash"),
            PaymentRecord(196, "27 Juli 2026", "OKTA PRADIPTA ATTALA DZAKI", 20000, "Juli 2026", "Cash"),
            PaymentRecord(195, "27 Juli 2026", "NI PUTU INTAN LESTARI DARMAYANTI", 20000, "Juli 2026", "Cash"),
            PaymentRecord(194, "27 Juli 2026", "NI PUTU CAHAYA LESTARI DEWI", 20000, "Juli 2026", "Cash"),
            PaymentRecord(193, "27 Juli 2026", "NI LUH PUTU KESYA ASTRI MELANI", 20000, "Juli 2026", "Cash"),
            PaymentRecord(192, "27 Juli 2026", "NI KOMANG SEPTIARINI", 20000, "Juli 2026", "Cash"),
            PaymentRecord(191, "27 Juli 2026", "NI KOMANG KIRANA PARAMITA ARDANARI", 20000, "Juli 2026", "Cash"),
            PaymentRecord(190, "27 Juli 2026", "NI KADEK LINA ANTIKA DEWI", 20000, "Juli 2026", "Cash"),
            PaymentRecord(189, "27 Juli 2026", "NI KADEK ADELIA CAHYA KENCANA PUTRI", 20000, "Juli 2026", "Cash"),
            PaymentRecord(188, "27 Juli 2026", "LUH RIA MIRASIH", 20000, "Juli 2026", "Cash"),
            PaymentRecord(187, "27 Juli 2026", "KOMANG DIAH PUTRI PRATIWI", 20000, "Juli 2026", "Cash"),
            PaymentRecord(186, "27 Juli 2026", "KADEK YUNI CALLISTA PUTRI DEWI", 20000, "Juli 2026", "Cash"),
            PaymentRecord(185, "27 Juli 2026", "I WAYAN PASEK KEVIN ARIADI", 20000, "Juli 2026", "Cash"),
            PaymentRecord(184, "27 Juli 2026", "I WAYAN BAGUS PUTRAWAN", 20000, "Juli 2026", "Cash"),
            PaymentRecord(183, "27 Juli 2026", "I PUTU SUYOGA MAHENDRA", 20000, "Juli 2026", "Cash"),
            PaymentRecord(182, "27 Juli 2026", "I PUTU PANDE ANDIKA", 20000, "Juli 2026", "Cash"),
            PaymentRecord(181, "27 Juli 2026", "I PUTU DIKA LAKSMANA PUTRA", 20000, "Juli 2026", "Cash"),
            PaymentRecord(180, "27 Juli 2026", "I NYOMAN GEDE ARTA WIGUNA", 20000, "Juli 2026", "Cash"),
            PaymentRecord(179, "27 Juli 2026", "I KOMANG RADITYA PUTRA", 20000, "Juli 2026", "Cash"),
            PaymentRecord(178, "27 Juli 2026", "I GEDE DARMA SUPTIAWAN", 20000, "Juli 2026", "Cash"),
            PaymentRecord(177, "27 Juli 2026", "I GEDE ABI WIRYA DINATA", 20000, "Juli 2026", "Cash"),
            PaymentRecord(176, "27 Juli 2026", "GEDE AGUS WIRA DARMA PUTRA", 20000, "Juli 2026", "Cash"),
            PaymentRecord(175, "27 Juli 2026", "GALISTAN RAMADHAN KURNIA TAUNAES", 20000, "Juli 2026", "Cash"),
            PaymentRecord(174, "27 Juli 2026", "DEWA GEDE DALEM OKA ADNYANA SANDI", 20000, "Juli 2026", "Cash"),
            PaymentRecord(173, "27 Juli 2026", "CAROLINA TIMUTHY JANGGUR", 20000, "Juli 2026", "Cash"),
            PaymentRecord(172, "27 Juli 2026", "BOYKE VILANO HAMONANGAN SIHITE", 20000, "Juli 2026", "Cash"),
            PaymentRecord(171, "21 Juli 2026", "DONASI - NI KOMANG SEPTIARINI", 20000, "Juli 2026", "Donasi"),
            PaymentRecord(170, "20 Juli 2026", "KADEK YUDA PRASETYA", 15000, "Juli 2026", "Cash"),
            PaymentRecord(169, "13 Juli 2026", "PUTU BAYU SATRIA WANGSA BUKIAN", 10000, "Juli 2026", "Cash"),
            PaymentRecord(168, "13 Juli 2026", "I PUTU DITYA ARTHA WIJAYA", 10000, "Juli 2026", "Cash"),
            PaymentRecord(167, "13 Juli 2026", "I KOMANG RISKI SETIAWAN", 10000, "Juli 2026", "Cash"),
            PaymentRecord(166, "25 Mei 2026", "RADITYA RONDI", 20000, "Mei 2026", "Cash"),
            PaymentRecord(165, "25 Mei 2026", "PUTU PUTRI CAHYANI", 20000, "Mei 2026", "Cash"),
            PaymentRecord(164, "25 Mei 2026", "PUTU NANDA LINDIA MAHARANI", 20000, "Mei 2026", "Cash"),
            PaymentRecord(163, "25 Mei 2026", "PUTU BAYU SATRIA WANGSA BUKIAN", 20000, "Mei 2026", "Cash"),
            PaymentRecord(162, "25 Mei 2026", "OKTA PRADIPTA ATTALA DZAKI", 20000, "Mei 2026", "Cash"),
            PaymentRecord(161, "25 Mei 2026", "NI PUTU INTAN LESTARI DARMAYANTI", 20000, "Mei 2026", "Cash"),
            PaymentRecord(160, "25 Mei 2026", "NI PUTU CAHAYA LESTARI DEWI", 20000, "Mei 2026", "Cash"),
            PaymentRecord(159, "25 Mei 2026", "NI LUH PUTU KESYA ASTRI MELANI", 20000, "Mei 2026", "Cash"),
            PaymentRecord(158, "25 Mei 2026", "NI KOMANG SEPTIARINI", 20000, "Mei 2026", "Cash"),
            PaymentRecord(157, "25 Mei 2026", "NI KOMANG KIRANA PARAMITA ARDANARI", 20000, "Mei 2026", "Cash"),
            PaymentRecord(156, "25 Mei 2026", "NI KADEK LINA ANTIKA DEWI", 20000, "Mei 2026", "Cash"),
            PaymentRecord(155, "25 Mei 2026", "NI KADEK ADELIA CAHYA KENCANA PUTRI", 20000, "Mei 2026", "Cash"),
            PaymentRecord(154, "25 Mei 2026", "LUH RIA MIRASIH", 20000, "Mei 2026", "Cash"),
            PaymentRecord(153, "25 Mei 2026", "KOMANG DIAH PUTRI PRATIWI", 20000, "Mei 2026", "Cash"),
            PaymentRecord(152, "25 Mei 2026", "KADEK YUNI CALLISTA PUTRI DEWI", 20000, "Mei 2026", "Cash"),
            PaymentRecord(151, "25 Mei 2026", "KADEK YUDA PRASETYA", 20000, "Mei 2026", "Cash"),
            PaymentRecord(150, "25 Mei 2026", "I WAYAN PASEK KEVIN ARIADI", 20000, "Mei 2026", "Cash"),
            PaymentRecord(149, "25 Mei 2026", "I WAYAN BAGUS PUTRAWAN", 20000, "Mei 2026", "Cash"),
            PaymentRecord(148, "25 Mei 2026", "I PUTU SUYOGA MAHENDRA", 20000, "Mei 2026", "Cash"),
            PaymentRecord(147, "25 Mei 2026", "I PUTU PANDE ANDIKA", 20000, "Mei 2026", "Cash"),
            PaymentRecord(146, "25 Mei 2026", "I PUTU DITYA ARTHA WIJAYA", 20000, "Mei 2026", "Cash"),
            PaymentRecord(145, "25 Mei 2026", "I PUTU DIKA LAKSMANA PUTRA", 20000, "Mei 2026", "Cash"),
            PaymentRecord(144, "25 Mei 2026", "I NYOMAN GEDE ARTA WIGUNA", 20000, "Mei 2026", "Cash"),
            PaymentRecord(143, "25 Mei 2026", "I KOMANG RISKI SETIAWAN", 20000, "Mei 2026", "Cash"),
            PaymentRecord(142, "25 Mei 2026", "I KOMANG RADITYA PUTRA", 20000, "Mei 2026", "Cash"),
            PaymentRecord(141, "25 Mei 2026", "I GEDE DARMA SUPTIAWAN", 20000, "Mei 2026", "Cash"),
            PaymentRecord(140, "25 Mei 2026", "I GEDE ABI WIRYA DINATA", 20000, "Mei 2026", "Cash"),
            PaymentRecord(139, "25 Mei 2026", "GEDE AGUS WIRA DARMA PUTRA", 20000, "Mei 2026", "Cash"),
            PaymentRecord(138, "25 Mei 2026", "GALISTAN RAMADHAN KURNIA TAUNAES", 20000, "Mei 2026", "Cash"),
            PaymentRecord(137, "25 Mei 2026", "DEWA GEDE DALEM OKA ADNYANA SANDI", 20000, "Mei 2026", "Cash"),
            PaymentRecord(136, "25 Mei 2026", "CAROLINA TIMUTHY JANGGUR", 20000, "Mei 2026", "Cash"),
            PaymentRecord(135, "25 Mei 2026", "BOYKE VILANO HAMONANGAN SIHITE", 20000, "Mei 2026", "Cash"),
            PaymentRecord(134, "25 Mei 2026", "BINTANG LEONITA CHRISTYA RENATA", 20000, "Mei 2026", "Cash"),
            PaymentRecord(133, "27 April 2026", "RADITYA RONDI", 20000, "April 2026", "Cash"),
            PaymentRecord(132, "27 April 2026", "PUTU PUTRI CAHYANI", 20000, "April 2026", "Cash"),
            PaymentRecord(131, "27 April 2026", "PUTU NANDA LINDIA MAHARANI", 20000, "April 2026", "Cash"),
            PaymentRecord(130, "27 April 2026", "PUTU BAYU SATRIA WANGSA BUKIAN", 20000, "April 2026", "Cash"),
            PaymentRecord(129, "27 April 2026", "OKTA PRADIPTA ATTALA DZAKI", 20000, "April 2026", "Cash"),
            PaymentRecord(128, "27 April 2026", "NI PUTU INTAN LESTARI DARMAYANTI", 20000, "April 2026", "Cash"),
            PaymentRecord(127, "27 April 2026", "NI PUTU CAHAYA LESTARI DEWI", 20000, "April 2026", "Cash"),
            PaymentRecord(126, "27 April 2026", "NI LUH PUTU KESYA ASTRI MELANI", 20000, "April 2026", "Cash"),
            PaymentRecord(125, "27 April 2026", "NI KOMANG SEPTIARINI", 20000, "April 2026", "Cash"),
            PaymentRecord(124, "27 April 2026", "NI KOMANG KIRANA PARAMITA ARDANARI", 20000, "April 2026", "Cash"),
            PaymentRecord(123, "27 April 2026", "NI KADEK LINA ANTIKA DEWI", 20000, "April 2026", "Cash"),
            PaymentRecord(122, "27 April 2026", "NI KADEK ADELIA CAHYA KENCANA PUTRI", 20000, "April 2026", "Cash"),
            PaymentRecord(121, "27 April 2026", "LUH RIA MIRASIH", 20000, "April 2026", "Cash"),
            PaymentRecord(120, "27 April 2026", "KOMANG DIAH PUTRI PRATIWI", 20000, "April 2026", "Cash"),
            PaymentRecord(119, "27 April 2026", "KADEK YUNI CALLISTA PUTRI DEWI", 20000, "April 2026", "Cash"),
            PaymentRecord(118, "27 April 2026", "KADEK YUDA PRASETYA", 20000, "April 2026", "Cash"),
            PaymentRecord(117, "27 April 2026", "I WAYAN PASEK KEVIN ARIADI", 20000, "April 2026", "Cash"),
            PaymentRecord(116, "27 April 2026", "I WAYAN BAGUS PUTRAWAN", 20000, "April 2026", "Cash"),
            PaymentRecord(115, "27 April 2026", "I PUTU SUYOGA MAHENDRA", 20000, "April 2026", "Cash"),
            PaymentRecord(114, "27 April 2026", "I PUTU PANDE ANDIKA", 20000, "April 2026", "Cash"),
            PaymentRecord(113, "27 April 2026", "I PUTU DITYA ARTHA WIJAYA", 20000, "April 2026", "Cash"),
            PaymentRecord(112, "27 April 2026", "I PUTU DIKA LAKSMANA PUTRA", 20000, "April 2026", "Cash"),
            PaymentRecord(111, "27 April 2026", "I NYOMAN GEDE ARTA WIGUNA", 20000, "April 2026", "Cash"),
            PaymentRecord(110, "27 April 2026", "I KOMANG RISKI SETIAWAN", 20000, "April 2026", "Cash"),
            PaymentRecord(109, "27 April 2026", "I KOMANG RADITYA PUTRA", 20000, "April 2026", "Cash"),
            PaymentRecord(108, "27 April 2026", "I GEDE DARMA SUPTIAWAN", 20000, "April 2026", "Cash"),
            PaymentRecord(107, "27 April 2026", "I GEDE ABI WIRYA DINATA", 20000, "April 2026", "Cash"),
            PaymentRecord(106, "27 April 2026", "GEDE AGUS WIRA DARMA PUTRA", 20000, "April 2026", "Cash"),
            PaymentRecord(105, "27 April 2026", "GALISTAN RAMADHAN KURNIA TAUNAES", 20000, "April 2026", "Cash"),
            PaymentRecord(104, "27 April 2026", "DEWA GEDE DALEM OKA ADNYANA SANDI", 20000, "April 2026", "Cash"),
            PaymentRecord(103, "27 April 2026", "CAROLINA TIMUTHY JANGGUR", 20000, "April 2026", "Cash"),
            PaymentRecord(102, "27 April 2026", "BOYKE VILANO HAMONANGAN SIHITE", 20000, "April 2026", "Cash"),
            PaymentRecord(101, "27 April 2026", "BINTANG LEONITA CHRISTYA RENATA", 20000, "April 2026", "Cash"),
            PaymentRecord(100, "23 Maret 2026", "RADITYA RONDI", 20000, "Maret 2026", "Cash"),
            PaymentRecord(99, "23 Maret 2026", "PUTU PUTRI CAHYANI", 20000, "Maret 2026", "Cash"),
            PaymentRecord(98, "23 Maret 2026", "PUTU NANDA LINDIA MAHARANI", 20000, "Maret 2026", "Cash"),
            PaymentRecord(97, "23 Maret 2026", "PUTU BAYU SATRIA WANGSA BUKIAN", 20000, "Maret 2026", "Cash"),
            PaymentRecord(96, "23 Maret 2026", "OKTA PRADIPTA ATTALA DZAKI", 20000, "Maret 2026", "Cash"),
            PaymentRecord(95, "23 Maret 2026", "NI PUTU INTAN LESTARI DARMAYANTI", 20000, "Maret 2026", "Cash"),
            PaymentRecord(94, "23 Maret 2026", "NI PUTU CAHAYA LESTARI DEWI", 20000, "Maret 2026", "Cash"),
            PaymentRecord(93, "23 Maret 2026", "NI LUH PUTU KESYA ASTRI MELANI", 20000, "Maret 2026", "Cash"),
            PaymentRecord(92, "23 Maret 2026", "NI KOMANG SEPTIARINI", 20000, "Maret 2026", "Cash"),
            PaymentRecord(91, "23 Maret 2026", "NI KOMANG KIRANA PARAMITA ARDANARI", 20000, "Maret 2026", "Cash"),
            PaymentRecord(90, "23 Maret 2026", "NI KADEK LINA ANTIKA DEWI", 20000, "Maret 2026", "Cash"),
            PaymentRecord(89, "23 Maret 2026", "NI KADEK ADELIA CAHYA KENCANA PUTRI", 20000, "Maret 2026", "Cash"),
            PaymentRecord(88, "23 Maret 2026", "LUH RIA MIRASIH", 20000, "Maret 2026", "Cash"),
            PaymentRecord(87, "23 Maret 2026", "KOMANG DIAH PUTRI PRATIWI", 20000, "Maret 2026", "Cash"),
            PaymentRecord(86, "23 Maret 2026", "KADEK YUNI CALLISTA PUTRI DEWI", 20000, "Maret 2026", "Cash"),
            PaymentRecord(85, "23 Maret 2026", "KADEK YUDA PRASETYA", 20000, "Maret 2026", "Cash"),
            PaymentRecord(84, "23 Maret 2026", "I WAYAN PASEK KEVIN ARIADI", 20000, "Maret 2026", "Cash"),
            PaymentRecord(83, "23 Maret 2026", "I WAYAN BAGUS PUTRAWAN", 20000, "Maret 2026", "Cash"),
            PaymentRecord(82, "23 Maret 2026", "I PUTU SUYOGA MAHENDRA", 20000, "Maret 2026", "Cash"),
            PaymentRecord(81, "23 Maret 2026", "I PUTU PANDE ANDIKA", 20000, "Maret 2026", "Cash"),
            PaymentRecord(80, "23 Maret 2026", "I PUTU DITYA ARTHA WIJAYA", 20000, "Maret 2026", "Cash"),
            PaymentRecord(79, "23 Maret 2026", "I PUTU DIKA LAKSMANA PUTRA", 20000, "Maret 2026", "Cash"),
            PaymentRecord(78, "23 Maret 2026", "I NYOMAN GEDE ARTA WIGUNA", 20000, "Maret 2026", "Cash"),
            PaymentRecord(77, "23 Maret 2026", "I KOMANG RISKI SETIAWAN", 20000, "Maret 2026", "Cash"),
            PaymentRecord(76, "23 Maret 2026", "I KOMANG RADITYA PUTRA", 20000, "Maret 2026", "Cash"),
            PaymentRecord(75, "23 Maret 2026", "I GEDE DARMA SUPTIAWAN", 20000, "Maret 2026", "Cash"),
            PaymentRecord(74, "23 Maret 2026", "I GEDE ABI WIRYA DINATA", 20000, "Maret 2026", "Cash"),
            PaymentRecord(73, "23 Maret 2026", "GEDE AGUS WIRA DARMA PUTRA", 20000, "Maret 2026", "Cash"),
            PaymentRecord(72, "23 Maret 2026", "GALISTAN RAMADHAN KURNIA TAUNAES", 20000, "Maret 2026", "Cash"),
            PaymentRecord(71, "23 Maret 2026", "DEWA GEDE DALEM OKA ADNYANA SANDI", 20000, "Maret 2026", "Cash"),
            PaymentRecord(70, "23 Maret 2026", "CAROLINA TIMUTHY JANGGUR", 20000, "Maret 2026", "Cash"),
            PaymentRecord(69, "23 Maret 2026", "BOYKE VILANO HAMONANGAN SIHITE", 20000, "Maret 2026", "Cash"),
            PaymentRecord(68, "23 Maret 2026", "BINTANG LEONITA CHRISTYA RENATA", 20000, "Maret 2026", "Cash"),
            PaymentRecord(67, "23 Februari 2026", "RADITYA RONDI", 20000, "Februari 2026", "Cash"),
            PaymentRecord(66, "23 Februari 2026", "PUTU PUTRI CAHYANI", 20000, "Februari 2026", "Cash"),
            PaymentRecord(65, "23 Februari 2026", "PUTU NANDA LINDIA MAHARANI", 20000, "Februari 2026", "Cash"),
            PaymentRecord(64, "23 Februari 2026", "PUTU BAYU SATRIA WANGSA BUKIAN", 20000, "Februari 2026", "Cash"),
            PaymentRecord(63, "23 Februari 2026", "OKTA PRADIPTA ATTALA DZAKI", 20000, "Februari 2026", "Cash"),
            PaymentRecord(62, "23 Februari 2026", "NI PUTU INTAN LESTARI DARMAYANTI", 20000, "Februari 2026", "Cash"),
            PaymentRecord(61, "23 Februari 2026", "NI PUTU CAHAYA LESTARI DEWI", 20000, "Februari 2026", "Cash"),
            PaymentRecord(60, "23 Februari 2026", "NI LUH PUTU KESYA ASTRI MELANI", 20000, "Februari 2026", "Cash"),
            PaymentRecord(59, "23 Februari 2026", "NI KOMANG SEPTIARINI", 20000, "Februari 2026", "Cash"),
            PaymentRecord(58, "23 Februari 2026", "NI KOMANG KIRANA PARAMITA ARDANARI", 20000, "Februari 2026", "Cash"),
            PaymentRecord(57, "23 Februari 2026", "NI KADEK LINA ANTIKA DEWI", 20000, "Februari 2026", "Cash"),
            PaymentRecord(56, "23 Februari 2026", "NI KADEK ADELIA CAHYA KENCANA PUTRI", 20000, "Februari 2026", "Cash"),
            PaymentRecord(55, "23 Februari 2026", "LUH RIA MIRASIH", 20000, "Februari 2026", "Cash"),
            PaymentRecord(54, "23 Februari 2026", "KOMANG DIAH PUTRI PRATIWI", 20000, "Februari 2026", "Cash"),
            PaymentRecord(53, "23 Februari 2026", "KADEK YUNI CALLISTA PUTRI DEWI", 20000, "Februari 2026", "Cash"),
            PaymentRecord(52, "23 Februari 2026", "KADEK YUDA PRASETYA", 20000, "Februari 2026", "Cash"),
            PaymentRecord(51, "23 Februari 2026", "I WAYAN PASEK KEVIN ARIADI", 20000, "Februari 2026", "Cash"),
            PaymentRecord(50, "23 Februari 2026", "I WAYAN BAGUS PUTRAWAN", 20000, "Februari 2026", "Cash"),
            PaymentRecord(49, "23 Februari 2026", "I PUTU SUYOGA MAHENDRA", 20000, "Februari 2026", "Cash"),
            PaymentRecord(48, "23 Februari 2026", "I PUTU PANDE ANDIKA", 20000, "Februari 2026", "Cash"),
            PaymentRecord(47, "23 Februari 2026", "I PUTU DITYA ARTHA WIJAYA", 20000, "Februari 2026", "Cash"),
            PaymentRecord(46, "23 Februari 2026", "I PUTU DIKA LAKSMANA PUTRA", 20000, "Februari 2026", "Cash"),
            PaymentRecord(45, "23 Februari 2026", "I NYOMAN GEDE ARTA WIGUNA", 20000, "Februari 2026", "Cash"),
            PaymentRecord(44, "23 Februari 2026", "I KOMANG RISKI SETIAWAN", 20000, "Februari 2026", "Cash"),
            PaymentRecord(43, "23 Februari 2026", "I KOMANG RADITYA PUTRA", 20000, "Februari 2026", "Cash"),
            PaymentRecord(42, "23 Februari 2026", "I GEDE DARMA SUPTIAWAN", 20000, "Februari 2026", "Cash"),
            PaymentRecord(41, "23 Februari 2026", "I GEDE ABI WIRYA DINATA", 20000, "Februari 2026", "Cash"),
            PaymentRecord(40, "23 Februari 2026", "GEDE AGUS WIRA DARMA PUTRA", 20000, "Februari 2026", "Cash"),
            PaymentRecord(39, "23 Februari 2026", "GALISTAN RAMADHAN KURNIA TAUNAES", 20000, "Februari 2026", "Cash"),
            PaymentRecord(38, "23 Februari 2026", "DEWA GEDE DALEM OKA ADNYANA SANDI", 20000, "Februari 2026", "Cash"),
            PaymentRecord(37, "23 Februari 2026", "CAROLINA TIMUTHY JANGGUR", 20000, "Februari 2026", "Cash"),
            PaymentRecord(36, "23 Februari 2026", "BOYKE VILANO HAMONANGAN SIHITE", 20000, "Februari 2026", "Cash"),
            PaymentRecord(35, "23 Februari 2026", "BINTANG LEONITA CHRISTYA RENATA", 20000, "Februari 2026", "Cash"),
            PaymentRecord(34, "01 Februari 2026", "DONASI - BU JALLU", 200000, "Februari 2026", "Donasi"),
            PaymentRecord(33, "26 Januari 2026", "RADITYA RONDI", 20000, "Januari 2026", "Cash"),
            PaymentRecord(32, "26 Januari 2026", "PUTU PUTRI CAHYANI", 20000, "Januari 2026", "Cash"),
            PaymentRecord(31, "26 Januari 2026", "PUTU NANDA LINDIA MAHARANI", 20000, "Januari 2026", "Cash"),
            PaymentRecord(30, "26 Januari 2026", "PUTU BAYU SATRIA WANGSA BUKIAN", 20000, "Januari 2026", "Cash"),
            PaymentRecord(29, "26 Januari 2026", "OKTA PRADIPTA ATTALA DZAKI", 20000, "Januari 2026", "Cash"),
            PaymentRecord(28, "26 Januari 2026", "NI PUTU INTAN LESTARI DARMAYANTI", 20000, "Januari 2026", "Cash"),
            PaymentRecord(27, "26 Januari 2026", "NI PUTU CAHAYA LESTARI DEWI", 20000, "Januari 2026", "Cash"),
            PaymentRecord(26, "26 Januari 2026", "NI LUH PUTU KESYA ASTRI MELANI", 20000, "Januari 2026", "Cash"),
            PaymentRecord(25, "26 Januari 2026", "NI KOMANG SEPTIARINI", 20000, "Januari 2026", "Cash"),
            PaymentRecord(24, "26 Januari 2026", "NI KOMANG KIRANA PARAMITA ARDANARI", 20000, "Januari 2026", "Cash"),
            PaymentRecord(23, "26 Januari 2026", "NI KADEK LINA ANTIKA DEWI", 20000, "Januari 2026", "Cash"),
            PaymentRecord(22, "26 Januari 2026", "NI KADEK ADELIA CAHYA KENCANA PUTRI", 20000, "Januari 2026", "Cash"),
            PaymentRecord(21, "26 Januari 2026", "LUH RIA MIRASIH", 20000, "Januari 2026", "Cash"),
            PaymentRecord(20, "26 Januari 2026", "KOMANG DIAH PUTRI PRATIWI", 20000, "Januari 2026", "Cash"),
            PaymentRecord(19, "26 Januari 2026", "KADEK YUNI CALLISTA PUTRI DEWI", 20000, "Januari 2026", "Cash"),
            PaymentRecord(18, "26 Januari 2026", "KADEK YUDA PRASETYA", 20000, "Januari 2026", "Cash"),
            PaymentRecord(17, "26 Januari 2026", "I WAYAN PASEK KEVIN ARIADI", 20000, "Januari 2026", "Cash"),
            PaymentRecord(16, "26 Januari 2026", "I WAYAN BAGUS PUTRAWAN", 20000, "Januari 2026", "Cash"),
            PaymentRecord(15, "26 Januari 2026", "I PUTU SUYOGA MAHENDRA", 20000, "Januari 2026", "Cash"),
            PaymentRecord(14, "26 Januari 2026", "I PUTU PANDE ANDIKA", 20000, "Januari 2026", "Cash"),
            PaymentRecord(13, "26 Januari 2026", "I PUTU DITYA ARTHA WIJAYA", 20000, "Januari 2026", "Cash"),
            PaymentRecord(12, "26 Januari 2026", "I PUTU DIKA LAKSMANA PUTRA", 20000, "Januari 2026", "Cash"),
            PaymentRecord(11, "26 Januari 2026", "I NYOMAN GEDE ARTA WIGUNA", 20000, "Januari 2026", "Cash"),
            PaymentRecord(10, "26 Januari 2026", "I KOMANG RISKI SETIAWAN", 20000, "Januari 2026", "Cash"),
            PaymentRecord(9, "26 Januari 2026", "I KOMANG RADITYA PUTRA", 20000, "Januari 2026", "Cash"),
            PaymentRecord(8, "26 Januari 2026", "I GEDE DARMA SUPTIAWAN", 20000, "Januari 2026", "Cash"),
            PaymentRecord(7, "26 Januari 2026", "I GEDE ABI WIRYA DINATA", 20000, "Januari 2026", "Cash"),
            PaymentRecord(6, "26 Januari 2026", "GEDE AGUS WIRA DARMA PUTRA", 20000, "Januari 2026", "Cash"),
            PaymentRecord(5, "26 Januari 2026", "GALISTAN RAMADHAN KURNIA TAUNAES", 20000, "Januari 2026", "Cash"),
            PaymentRecord(4, "26 Januari 2026", "DEWA GEDE DALEM OKA ADNYANA SANDI", 20000, "Januari 2026", "Cash"),
            PaymentRecord(3, "26 Januari 2026", "CAROLINA TIMUTHY JANGGUR", 20000, "Januari 2026", "Cash"),
            PaymentRecord(2, "26 Januari 2026", "BOYKE VILANO HAMONANGAN SIHITE", 20000, "Januari 2026", "Cash"),
            PaymentRecord(1, "26 Januari 2026", "BINTANG LEONITA CHRISTYA RENATA", 20000, "Januari 2026", "Cash")
        )
        _pengeluaranList.value = listOf(
            Pengeluaran(id = "exp-30", tanggal = "10 November 2026", jumlah = 59000, keterangan = "Selasa Literasi"),
            Pengeluaran(id = "exp-29", tanggal = "30 Oktober 2026", jumlah = 995000, keterangan = "Saraswati"),
            Pengeluaran(id = "exp-28", tanggal = "30 September 2026", jumlah = 47000, keterangan = "Literasi Bahasa Inggris"),
            Pengeluaran(id = "exp-23", tanggal = "10 September 2026", jumlah = 36000, keterangan = "Kamis Literasi"),
            Pengeluaran(id = "exp-22", tanggal = "26 Juli 2026", jumlah = 17000, keterangan = "MAP PLASTIK BIG"),
            Pengeluaran(id = "exp-21", tanggal = "26 Juli 2026", jumlah = 9000, keterangan = "SNOWMAN BOARD MARKER BLACK"),
            Pengeluaran(id = "exp-20", tanggal = "01 Mei 2026", jumlah = 40000, keterangan = "Jagung, Sayur, Kacang"),
            Pengeluaran(id = "exp-16", tanggal = "01 Mei 2026", jumlah = 35000, keterangan = "Jahit Bendera"),
            Pengeluaran(id = "exp-15", tanggal = "01 Mei 2026", jumlah = 200000, keterangan = "Property Drama"),
            Pengeluaran(id = "exp-14", tanggal = "01 April 2026", jumlah = 255000, keterangan = "Tirai & Jahit"),
            Pengeluaran(id = "exp-13", tanggal = "01 April 2026", jumlah = 93000, keterangan = "Latihan Masak"),
            Pengeluaran(id = "exp-12", tanggal = "01 April 2026", jumlah = 440000, keterangan = "Saraswati"),
            Pengeluaran(id = "exp-11", tanggal = "01 Maret 2026", jumlah = 300000, keterangan = "Property Drama"),
            Pengeluaran(id = "exp-10", tanggal = "01 Maret 2026", jumlah = 140000, keterangan = "Ogoh - Ogoh"),
            Pengeluaran(id = "exp-9", tanggal = "01 Maret 2026", jumlah = 30000, keterangan = "Literasi Pagi"),
            Pengeluaran(id = "exp-19", tanggal = "01 Februari 2026", jumlah = 6500, keterangan = "Dompet kas"),
            Pengeluaran(id = "exp-18", tanggal = "01 Februari 2026", jumlah = 4000, keterangan = "Pulpen kas"),
            Pengeluaran(id = "exp-17", tanggal = "01 Februari 2026", jumlah = 62000, keterangan = "Tugas Agama"),
            Pengeluaran(id = "exp-8", tanggal = "01 Februari 2026", jumlah = 67000, keterangan = "Kanvas"),
            Pengeluaran(id = "exp-7", tanggal = "01 Februari 2026", jumlah = 100000, keterangan = "Ogoh - Ogoh"),
            Pengeluaran(id = "exp-6", tanggal = "01 Februari 2026", jumlah = 200000, keterangan = "Ogoh - Ogoh"),
            Pengeluaran(id = "exp-5", tanggal = "01 Februari 2026", jumlah = 50000, keterangan = "Uang Keluar"),
            Pengeluaran(id = "exp-2", tanggal = "01 Februari 2026", jumlah = 10500, keterangan = "Buku Kas"),
            Pengeluaran(id = "exp-1", tanggal = "01 Februari 2026", jumlah = 300000, keterangan = "Ogoh - Ogoh")
        )
        _userProfile.value = UserProfile()
    }

    // ==================== PERSISTENCE ====================

    private fun profileToJson(p: UserProfile) = JSONObject().apply {
        put("nama", p.nama); put("email", p.email); put("nis", p.nis); put("kelas", p.kelas)
        put("noHp", p.noHp); put("password", p.password); put("role", p.role.name)
    }

    private fun profileFromJson(o: JSONObject) = UserProfile(
        nama = o.optString("nama", "Admin"), email = o.optString("email", ""),
        nis = o.optString("nis", ""), kelas = o.optString("kelas", "XII PPLG"),
        noHp = o.optString("noHp", ""), password = o.optString("password", ""),
        role = runCatching { UserRole.valueOf(o.optString("role", "USER")) }.getOrDefault(UserRole.USER)
    )

    private fun mapToJson(m: Map<String, Int>) = JSONObject().apply { m.forEach { (k, v) -> put(k, v) } }
    private fun mapFromJson(o: JSONObject?): Map<String, Int> =
        o?.keys()?.asSequence()?.associateWith { o.optInt(it) } ?: emptyMap()

    // CARI-SIMPAN-DATA | Menyimpan semua data ke HP (JSON di SharedPreferences).
    private fun saveToPrefs() {
        if (prefs == null) return
        val stArray = JSONArray()
        _students.value.forEach { st ->
            stArray.put(JSONObject().apply {
                put("no", st.no); put("nama", st.nama); put("peran", st.peran)
                put("bayar", mapToJson(st.bayar)); put("dispensasi", mapToJson(st.dispensasi))
                put("kelas", st.kelas); put("noHp", st.noHp)
            })
        }
        val prArray = JSONArray()
        _paymentRecords.value.forEach { pr ->
            prArray.put(JSONObject().apply {
                put("no", pr.no); put("tanggal", pr.tanggal); put("namaSiswa", pr.namaSiswa)
                put("jumlah", pr.jumlah); put("bulan", pr.bulan); put("metode", pr.metode); put("denda", pr.denda)
            })
        }
        val penArray = JSONArray()
        _pengeluaranList.value.forEach { p ->
            penArray.put(JSONObject().apply {
                put("id", p.id); put("tanggal", p.tanggal); put("jumlah", p.jumlah); put("keterangan", p.keterangan)
            })
        }
        val usersArray = JSONArray()
        _registeredUsers.values.forEach { usersArray.put(profileToJson(it)) }

        prefs.edit().apply {
            putBoolean(DATA_KEY, true)
            putBoolean("is_logged_in", _isLoggedIn.value)
            putString("user_profile", profileToJson(_userProfile.value).toString())
            putString("students", stArray.toString())
            putString("payment_records", prArray.toString())
            putString("pengeluaran_list", penArray.toString())
            putString("registered_users_v2", usersArray.toString())
            apply()
        }
    }

    // CARI-SIMPAN-DATA | Memuat data tersimpan; bila rusak kembali ke data awal.
    private fun loadFromPrefs() {
        if (prefs == null) return
        try {
            _isLoggedIn.value = prefs.getBoolean("is_logged_in", false)
            _userProfile.value = if (_isLoggedIn.value) {
                prefs.getString("user_profile", null)?.let { profileFromJson(JSONObject(it)) } ?: UserProfile()
            } else UserProfile()

            prefs.getString("students", null)?.let { str ->
                val arr = JSONArray(str)
                _students.value = (0 until arr.length()).map { i ->
                    val o = arr.getJSONObject(i)
                    Siswa(
                        no = o.getInt("no"), nama = o.getString("nama"), peran = o.getString("peran"),
                        bayar = mapFromJson(o.optJSONObject("bayar")),
                        dispensasi = mapFromJson(o.optJSONObject("dispensasi")),
                        kelas = o.optString("kelas", "XII PPLG"), noHp = o.optString("noHp", "")
                    )
                }
            }
            prefs.getString("payment_records", null)?.let { str ->
                val arr = JSONArray(str)
                _paymentRecords.value = (0 until arr.length()).map { i ->
                    val o = arr.getJSONObject(i)
                    PaymentRecord(
                        no = o.getInt("no"), tanggal = o.getString("tanggal"),
                        namaSiswa = o.getString("namaSiswa"), jumlah = o.getLong("jumlah"),
                        bulan = o.optString("bulan", "Juli 2026"), metode = o.optString("metode", "Cash"),
                        denda = o.optLong("denda", 0L)
                    )
                }
            }
            prefs.getString("pengeluaran_list", null)?.let { str ->
                val arr = JSONArray(str)
                _pengeluaranList.value = (0 until arr.length()).map { i ->
                    val o = arr.getJSONObject(i)
                    Pengeluaran(
                        id = o.optString("id", java.util.UUID.randomUUID().toString()),
                        tanggal = o.getString("tanggal"), jumlah = o.getLong("jumlah"),
                        keterangan = o.getString("keterangan")
                    )
                }
            }
            prefs.getString("registered_users_v2", null)?.let { str ->
                val arr = JSONArray(str)
                for (i in 0 until arr.length()) {
                    val p = profileFromJson(arr.getJSONObject(i))
                    if (p.email.isNotBlank()) _registeredUsers[p.email] = p
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            loadInitialData()
        }
    }
}
