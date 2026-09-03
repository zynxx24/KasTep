package com.kastep.app.data

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

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

    // Registered users stored in prefs
    private val _registeredUsers = mutableMapOf<String, Pair<String, String>>() // email -> (name, password)

    init {
        // Built-in admin account
        _registeredUsers["admin@gmail.com"] = Pair("Administrator", "admin123")
        loadData()
    }

    private fun loadData() {
        if (prefs != null && prefs.contains("saved_data")) {
            loadFromPrefs()
        } else {
            loadInitialData()
            saveToPrefs()
        }
    }

    // ==================== AUTH ====================

    fun login(email: String, password: String): String? {
        val trimEmail = email.trim().lowercase()
        val trimPass = password.trim()

        if (trimEmail.isBlank()) return "Email tidak boleh kosong"
        if (trimPass.isBlank()) return "Password tidak boleh kosong"
        if (!trimEmail.contains("@") || !trimEmail.contains(".")) return "Format email tidak valid"
        if (trimPass.length < 5) return "Password minimal 5 karakter"

        // Check admin
        if (trimEmail == "admin@gmail.com" && trimPass == "admin123") {
            _userProfile.value = UserProfile(
                nama = "Administrator", email = trimEmail, password = trimPass,
                role = UserRole.ADMIN, kelas = "XII PPLG"
            )
            _isLoggedIn.value = true
            saveToPrefs()
            return null
        }

        // Check registered users
        val registered = _registeredUsers[trimEmail]
        if (registered != null) {
            if (registered.second == trimPass) {
                _userProfile.value = UserProfile(
                    nama = registered.first, email = trimEmail, password = trimPass,
                    role = UserRole.USER, kelas = "XII PPLG"
                )
                _isLoggedIn.value = true
                saveToPrefs()
                return null
            }
            return "Password salah"
        }

        return "Akun tidak ditemukan. Silakan register terlebih dahulu."
    }

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

        _registeredUsers[trimEmail] = Pair(trimNama, trimPass)

        _userProfile.value = UserProfile(
            nama = trimNama, email = trimEmail, nis = nis.trim(),
            kelas = kelas.ifBlank { "XII PPLG" }, noHp = noHp.trim(),
            password = trimPass, role = UserRole.USER
        )
        _isLoggedIn.value = true
        saveToPrefs()
        return null
    }

    fun logout() {
        _userProfile.value = UserProfile()
        _isLoggedIn.value = false
        saveToPrefs()
    }

    // ==================== STUDENT CRUD ====================

    fun addStudent(nama: String, peran: String): String? {
        if (nama.isBlank()) return "Nama siswa tidak boleh kosong"
        val nextNo = (_students.value.maxOfOrNull { it.no } ?: 0) + 1
        val newStudent = Siswa(no = nextNo, nama = nama.trim().uppercase(), peran = peran.ifBlank { "Anggota" })
        _students.value = _students.value + newStudent
        saveToPrefs()
        return null
    }

    fun updateStudent(no: Int, nama: String, peran: String): String? {
        if (nama.isBlank()) return "Nama siswa tidak boleh kosong"
        _students.value = _students.value.map {
            if (it.no == no) it.copy(nama = nama.trim().uppercase(), peran = peran) else it
        }
        saveToPrefs()
        return null
    }

    fun deleteStudent(no: Int) {
        _students.value = _students.value.filter { it.no != no }
            .mapIndexed { index, siswa -> siswa.copy(no = index + 1) }
        saveToPrefs()
    }

    // ==================== PAYMENT ====================

    fun processPayment(studentName: String, bulan: String, metode: String, jumlah: Long): String? {
        if (studentName.isBlank()) return "Pilih siswa terlebih dahulu"
        if (bulan.isBlank()) return "Pilih bulan pembayaran"

        val nextNo = (_paymentRecords.value.maxOfOrNull { it.no } ?: 0) + 1
        val record = PaymentRecord(
            no = nextNo, tanggal = getCurrentDateString(),
            namaSiswa = studentName, jumlah = jumlah,
            bulan = bulan, metode = metode
        )
        _paymentRecords.value = listOf(record) + _paymentRecords.value

        // Update student payment status
        _students.value = _students.value.map { siswa ->
            if (siswa.nama == studentName) {
                when {
                    bulan.contains("Juli") -> siswa.copy(statusJuli = StatusBayar.LUNAS)
                    bulan.contains("Agustus") -> siswa.copy(statusAgustus = StatusBayar.LUNAS)
                    else -> siswa
                }
            } else siswa
        }
        saveToPrefs()
        return null
    }

    // ==================== PENGELUARAN ====================

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

    fun updatePengeluaran(id: String, tanggal: String, jumlah: Long, keterangan: String) {
        _pengeluaranList.value = _pengeluaranList.value.map {
            if (it.id == id) it.copy(tanggal = tanggal, jumlah = jumlah, keterangan = keterangan) else it
        }
        saveToPrefs()
    }

    fun deletePengeluaran(id: String) {
        _pengeluaranList.value = _pengeluaranList.value.filter { it.id != id }
        saveToPrefs()
    }

    // ==================== RESET / REPORT ====================

    fun resetData() {
        prefs?.edit()?.clear()?.apply()
        loadInitialData()
        saveToPrefs()
    }

    fun getCurrentDateString(): String {
        val sdf = SimpleDateFormat("dd MMMM yyyy", Locale("id", "ID"))
        return sdf.format(Date())
    }

    fun generateReportText(): String {
        val totalIncome = _paymentRecords.value.sumOf { it.jumlah }
        val totalExpense = _pengeluaranList.value.sumOf { it.jumlah }
        val saldo = totalIncome - totalExpense
        return """
            📊 LAPORAN KAS KELAS - KASTEP 📊
            Tanggal: ${getCurrentDateString()}
            Kelas: XII PPLG
            
            ------------------------------------
            💰 Total Pemasukan : Rp ${KastepViewModel.formatRupiah(totalIncome)}
            💸 Total Pengeluaran: Rp ${KastepViewModel.formatRupiah(totalExpense)}
            💵 Saldo Akhir      : Rp ${KastepViewModel.formatRupiah(saldo)}
            ------------------------------------
            👥 Total Siswa: ${_students.value.size} Siswa
            ✅ Lunas (Juli): ${_students.value.count { it.statusJuli == StatusBayar.LUNAS }}
            ❌ Belum (Juli): ${_students.value.count { it.statusJuli == StatusBayar.BELUM }}
            ✅ Lunas (Agustus): ${_students.value.count { it.statusAgustus == StatusBayar.LUNAS }}
            ❌ Belum (Agustus): ${_students.value.count { it.statusAgustus == StatusBayar.BELUM }}
            ------------------------------------
            Dibuat otomatis oleh KASTEP.
        """.trimIndent()
    }

    // ==================== INITIAL DATA ====================

    private fun loadInitialData() {
        _students.value = listOf(
            Siswa(1, "BOYKE VILANO HAMONANGAN SIHITE", "Ketua Kelas", StatusBayar.LUNAS, StatusBayar.LUNAS),
            Siswa(2, "BINTANG LEONITA CHRISTYA RENATA", "Bendahara", StatusBayar.LUNAS, StatusBayar.LUNAS),
            Siswa(3, "CAROLINA TIMUTHY JANGGUR", "Sekretaris", StatusBayar.LUNAS, StatusBayar.LUNAS),
            Siswa(4, "DEWA GEDE DALEM OKA ADNYANA SANDI", "Wakil Ketua", StatusBayar.LUNAS, StatusBayar.LUNAS),
            Siswa(5, "GALISTAN RAMADHAN KURNIA TAUNAES", "Anggota", StatusBayar.LUNAS, StatusBayar.LUNAS),
            Siswa(6, "GEDE AGUS WIRA DARMA PUTRA", "Anggota", StatusBayar.LUNAS, StatusBayar.LUNAS),
            Siswa(7, "I GEDE ABI WIRYA DINATA", "Anggota", StatusBayar.LUNAS, StatusBayar.LUNAS),
            Siswa(8, "I GEDE DARMA SUPTIAWAN", "Anggota", StatusBayar.LUNAS, StatusBayar.LUNAS),
            Siswa(9, "I KOMANG RADITYA PUTRA", "Anggota", StatusBayar.LUNAS, StatusBayar.LUNAS),
            Siswa(10, "I KOMANG RISKI SETIAWAN", "Anggota", StatusBayar.LUNAS, StatusBayar.LUNAS),
            Siswa(11, "I NYOMAN GEDE ARTA WIGUNA", "Anggota", StatusBayar.LUNAS, StatusBayar.LUNAS),
            Siswa(12, "I PUTU DIKA LAKSMANA PUTRA", "Anggota", StatusBayar.LUNAS, StatusBayar.LUNAS),
            Siswa(13, "I PUTU DITYA ARTHA WIJAYA", "Anggota", StatusBayar.LUNAS, StatusBayar.LUNAS),
            Siswa(14, "I PUTU PANDE ANDIKA", "Anggota", StatusBayar.LUNAS, StatusBayar.LUNAS),
            Siswa(15, "I PUTU SUYOGA MAHENDRA", "Anggota", StatusBayar.LUNAS, StatusBayar.LUNAS),
            Siswa(16, "I WAYAN BAGUS PUTRAWAN", "Anggota", StatusBayar.LUNAS, StatusBayar.LUNAS),
            Siswa(17, "I WAYAN PASEK KEVIN ARIADI", "Anggota", StatusBayar.LUNAS, StatusBayar.LUNAS),
            Siswa(18, "KADEK YUDA PRASETYA", "Anggota", StatusBayar.LUNAS, StatusBayar.LUNAS),
            Siswa(19, "KADEK YUNI CALLISTA PUTRI DEWI", "Anggota", StatusBayar.LUNAS, StatusBayar.LUNAS),
            Siswa(20, "KOMANG DIAH PUTRI PRATIWI", "Anggota", StatusBayar.LUNAS, StatusBayar.LUNAS),
            Siswa(21, "LUH RIA MIRASIH", "Anggota", StatusBayar.LUNAS, StatusBayar.LUNAS),
            Siswa(22, "NI KADEK ADELIA CAHYA KENCANA PUTRI", "Anggota", StatusBayar.LUNAS, StatusBayar.LUNAS),
            Siswa(23, "NI KADEK LINA ANTIKA DEWI", "Anggota", StatusBayar.LUNAS, StatusBayar.BELUM),
            Siswa(24, "NI KOMANG KIRANA PARAMITA ARDANARI", "Anggota", StatusBayar.LUNAS, StatusBayar.BELUM),
            Siswa(25, "NI KOMANG SEPTIARINI", "Anggota", StatusBayar.LUNAS, StatusBayar.BELUM),
            Siswa(26, "NI LUH PUTU KESYA ASTRI MELANI", "Anggota", StatusBayar.LUNAS, StatusBayar.BELUM),
            Siswa(27, "NI PUTU CAHAYA LESTARI DEWI", "Anggota", StatusBayar.LUNAS, StatusBayar.BELUM),
            Siswa(28, "NI PUTU INTAN LESTARI DARMAYANTI", "Anggota", StatusBayar.LUNAS, StatusBayar.BELUM),
            Siswa(29, "OKTA PRADIPTA ATTALA DZAKI", "Anggota", StatusBayar.BELUM, StatusBayar.BELUM),
            Siswa(30, "PUTU BAYU SATRIA WANGSA BUKIAN", "Anggota", StatusBayar.BELUM, StatusBayar.BELUM),
            Siswa(31, "PUTU NANDA LINDIA MAHARANI", "Anggota", StatusBayar.BELUM, StatusBayar.BELUM),
            Siswa(32, "PUTU PUTRI CAHYANI", "Anggota", StatusBayar.BELUM, StatusBayar.BELUM),
            Siswa(33, "RADITYA RONDI", "Anggota", StatusBayar.BELUM, StatusBayar.BELUM)
        )

        _paymentRecords.value = listOf(
            PaymentRecord(1, "01 Juli 2026", "Boyke Vilano", 20000, "Juli 2026", "Cash"),
            PaymentRecord(2, "01 Juli 2026", "Bintang Leonita", 20000, "Juli 2026", "DANA"),
            PaymentRecord(3, "02 Juli 2026", "Carolina Timuthy", 20000, "Juli 2026", "Cash"),
            PaymentRecord(4, "02 Juli 2026", "Dewa Gede Dalem", 20000, "Juli 2026", "BCA"),
            PaymentRecord(5, "03 Juli 2026", "Galistan Ramadhan", 20000, "Juli 2026", "Cash"),
            PaymentRecord(6, "03 Juli 2026", "Gede Agus Wira", 20000, "Juli 2026", "GoPay"),
            PaymentRecord(7, "04 Juli 2026", "I Gede Abi Wirya", 20000, "Juli 2026", "OVO"),
            PaymentRecord(8, "04 Juli 2026", "I Gede Darma", 20000, "Juli 2026", "Cash"),
            PaymentRecord(9, "05 Juli 2026", "I Komang Raditya", 20000, "Juli 2026", "Mandiri"),
            PaymentRecord(10, "05 Juli 2026", "I Komang Riski", 20000, "Juli 2026", "Cash")
        )

        _pengeluaranList.value = listOf(
            Pengeluaran(tanggal = "15 Juli 2026", jumlah = 50000, keterangan = "Beli spidol & penghapus"),
            Pengeluaran(tanggal = "20 Juli 2026", jumlah = 30000, keterangan = "Print materi kelas"),
            Pengeluaran(tanggal = "25 Juli 2026", jumlah = 25000, keterangan = "Beli tinta printer")
        )

        _userProfile.value = UserProfile()
    }

    // ==================== PERSISTENCE ====================

    private fun saveToPrefs() {
        if (prefs == null) return
        val profileObj = JSONObject().apply {
            put("nama", _userProfile.value.nama)
            put("email", _userProfile.value.email)
            put("nis", _userProfile.value.nis)
            put("kelas", _userProfile.value.kelas)
            put("noHp", _userProfile.value.noHp)
            put("password", _userProfile.value.password)
            put("role", _userProfile.value.role.name)
        }

        val stArray = JSONArray()
        _students.value.forEach { st ->
            stArray.put(JSONObject().apply {
                put("no", st.no); put("nama", st.nama); put("peran", st.peran)
                put("statusJuli", st.statusJuli.name); put("statusAgustus", st.statusAgustus.name)
                put("kelas", st.kelas); put("noHp", st.noHp)
            })
        }

        val prArray = JSONArray()
        _paymentRecords.value.forEach { pr ->
            prArray.put(JSONObject().apply {
                put("no", pr.no); put("tanggal", pr.tanggal); put("namaSiswa", pr.namaSiswa)
                put("jumlah", pr.jumlah); put("bulan", pr.bulan); put("metode", pr.metode)
            })
        }

        val penArray = JSONArray()
        _pengeluaranList.value.forEach { p ->
            penArray.put(JSONObject().apply {
                put("id", p.id); put("tanggal", p.tanggal)
                put("jumlah", p.jumlah); put("keterangan", p.keterangan)
            })
        }

        // Save registered users
        val usersArray = JSONArray()
        _registeredUsers.forEach { (email, pair) ->
            usersArray.put(JSONObject().apply {
                put("email", email); put("name", pair.first); put("password", pair.second)
            })
        }

        prefs.edit().apply {
            putBoolean("saved_data", true)
            putBoolean("is_logged_in", _isLoggedIn.value)
            putString("user_profile", profileObj.toString())
            putString("students", stArray.toString())
            putString("payment_records", prArray.toString())
            putString("pengeluaran_list", penArray.toString())
            putString("registered_users", usersArray.toString())
            apply()
        }
    }

    private fun loadFromPrefs() {
        if (prefs == null) return
        try {
            _isLoggedIn.value = prefs.getBoolean("is_logged_in", false)

            if (_isLoggedIn.value) {
                prefs.getString("user_profile", null)?.let { str ->
                    val p = JSONObject(str)
                    _userProfile.value = UserProfile(
                        nama = p.optString("nama", "Admin"),
                        email = p.optString("email", ""),
                        nis = p.optString("nis", ""),
                        kelas = p.optString("kelas", "XII PPLG"),
                        noHp = p.optString("noHp", ""),
                        password = p.optString("password", ""),
                        role = try { UserRole.valueOf(p.optString("role", "USER")) } catch (_: Exception) { UserRole.USER }
                    )
                }
            } else {
                _userProfile.value = UserProfile()
            }

            prefs.getString("students", null)?.let { str ->
                val arr = JSONArray(str)
                val list = mutableListOf<Siswa>()
                for (i in 0 until arr.length()) {
                    val o = arr.getJSONObject(i)
                    list.add(Siswa(
                        no = o.getInt("no"), nama = o.getString("nama"), peran = o.getString("peran"),
                        statusJuli = StatusBayar.valueOf(o.optString("statusJuli", "LUNAS")),
                        statusAgustus = StatusBayar.valueOf(o.optString("statusAgustus", "BELUM")),
                        kelas = o.optString("kelas", "XII PPLG"), noHp = o.optString("noHp", "")
                    ))
                }
                _students.value = list
            }

            prefs.getString("payment_records", null)?.let { str ->
                val arr = JSONArray(str)
                val list = mutableListOf<PaymentRecord>()
                for (i in 0 until arr.length()) {
                    val o = arr.getJSONObject(i)
                    list.add(PaymentRecord(
                        no = o.getInt("no"), tanggal = o.getString("tanggal"),
                        namaSiswa = o.getString("namaSiswa"), jumlah = o.getLong("jumlah"),
                        bulan = o.optString("bulan", "Juli 2026"), metode = o.optString("metode", "Cash")
                    ))
                }
                _paymentRecords.value = list
            }

            prefs.getString("pengeluaran_list", null)?.let { str ->
                val arr = JSONArray(str)
                val list = mutableListOf<Pengeluaran>()
                for (i in 0 until arr.length()) {
                    val o = arr.getJSONObject(i)
                    list.add(Pengeluaran(
                        id = o.optString("id", java.util.UUID.randomUUID().toString()),
                        tanggal = o.getString("tanggal"),
                        jumlah = o.getLong("jumlah"),
                        keterangan = o.getString("keterangan")
                    ))
                }
                _pengeluaranList.value = list
            }

            prefs.getString("registered_users", null)?.let { str ->
                val arr = JSONArray(str)
                for (i in 0 until arr.length()) {
                    val o = arr.getJSONObject(i)
                    _registeredUsers[o.getString("email")] = Pair(o.getString("name"), o.getString("password"))
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            loadInitialData()
        }
    }
}
