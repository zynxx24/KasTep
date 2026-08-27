package com.kastep.app.data

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.json.JSONArray
import org.json.JSONObject
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class KastepRepository(context: Context? = null) {

    private val prefs: SharedPreferences? = context?.getSharedPreferences("kastep_prefs", Context.MODE_PRIVATE)

    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    private val _userProfile = MutableStateFlow(UserProfile())
    val userProfile: StateFlow<UserProfile> = _userProfile.asStateFlow()

    private val _transactions = MutableStateFlow<List<Transaction>>(emptyList())
    val transactions: StateFlow<List<Transaction>> = _transactions.asStateFlow()

    private val _students = MutableStateFlow<List<Siswa>>(emptyList())
    val students: StateFlow<List<Siswa>> = _students.asStateFlow()

    private val _paymentRecords = MutableStateFlow<List<PaymentRecord>>(emptyList())
    val paymentRecords: StateFlow<List<PaymentRecord>> = _paymentRecords.asStateFlow()

    val mataPelajaran = listOf(
        MataPelajaran("BAHASA INGGRIS"),
        MataPelajaran("PDL RPL"),
        MataPelajaran("BAHASA INDONESIA"),
        MataPelajaran("PABP HINDU"),
        MataPelajaran("BAHASA BALI"),
        MataPelajaran("PABP KRISTEN"),
        MataPelajaran("RPL"),
        MataPelajaran("PP"),
        MataPelajaran("KIK"),
        MataPelajaran("MAT")
    )

    init {
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

    private fun loadInitialData() {
        val initialStudents = listOf(
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
        _students.value = initialStudents

        val initialTransactions = listOf(
            Transaction(title = "Pembayaran Kas", amount = 50000, type = TransactionType.INCOME, date = "28 Juli 2026"),
            Transaction(title = "Pengeluaran Kas", amount = 20000, type = TransactionType.EXPENSE, date = "28 Juli 2026"),
            Transaction(title = "Pengeluaran Kas", amount = 20000, type = TransactionType.EXPENSE, date = "28 Juli 2026"),
            Transaction(title = "Pembayaran Kas", amount = 50000, type = TransactionType.INCOME, date = "28 Juli 2026"),
            Transaction(title = "Pengeluaran Kas", amount = 20000, type = TransactionType.EXPENSE, date = "28 Juli 2026"),
            Transaction(title = "Pembayaran Kas", amount = 50000, type = TransactionType.INCOME, date = "28 Juli 2026")
        )
        _transactions.value = initialTransactions

        val initialPaymentRecords = listOf(
            PaymentRecord(1, "01 Juli 2026", "Boyke Vilano", 10000),
            PaymentRecord(2, "01 Juli 2026", "Bintang Leonita", 10000),
            PaymentRecord(3, "01 Juli 2026", "Carolina Timuthy", 10000),
            PaymentRecord(4, "02 Juli 2026", "Dewa Gede Dalem", 10000),
            PaymentRecord(5, "02 Juli 2026", "Galistan Ramadhan", 10000),
            PaymentRecord(6, "03 Juli 2026", "Gede Agus Wira", 10000),
            PaymentRecord(7, "03 Juli 2026", "I Gede Abi Wirya", 10000),
            PaymentRecord(8, "04 Juli 2026", "I Gede Darma", 10000),
            PaymentRecord(9, "04 Juli 2026", "I Komang Raditya", 10000),
            PaymentRecord(10, "05 Juli 2026", "I Komang Riski", 10000)
        )
        _paymentRecords.value = initialPaymentRecords
        _userProfile.value = UserProfile()
    }

    private fun saveToPrefs() {
        if (prefs == null) return

        // User profile
        val profileObj = JSONObject().apply {
            put("nama", _userProfile.value.nama)
            put("nis", _userProfile.value.nis)
            put("kelas", _userProfile.value.kelas)
            put("noHp", _userProfile.value.noHp)
            put("password", _userProfile.value.password)
        }

        // Transactions
        val txArray = JSONArray()
        _transactions.value.forEach { tx ->
            val obj = JSONObject().apply {
                put("id", tx.id)
                put("title", tx.title)
                put("amount", tx.amount)
                put("type", tx.type.name)
                put("date", tx.date)
            }
            txArray.put(obj)
        }

        // Students
        val stArray = JSONArray()
        _students.value.forEach { st ->
            val obj = JSONObject().apply {
                put("no", st.no)
                put("nama", st.nama)
                put("peran", st.peran)
                put("statusJuli", st.statusJuli.name)
                put("statusAgustus", st.statusAgustus.name)
                put("kelas", st.kelas)
                put("noHp", st.noHp)
            }
            stArray.put(obj)
        }

        // Payment records
        val prArray = JSONArray()
        _paymentRecords.value.forEach { pr ->
            val obj = JSONObject().apply {
                put("no", pr.no)
                put("tanggal", pr.tanggal)
                put("namaSiswa", pr.namaSiswa)
                put("jumlah", pr.jumlah)
            }
            prArray.put(obj)
        }

        prefs.edit().apply {
            putBoolean("saved_data", true)
            putBoolean("is_logged_in", _isLoggedIn.value)
            putString("user_profile", profileObj.toString())
            putString("transactions", txArray.toString())
            putString("students", stArray.toString())
            putString("payment_records", prArray.toString())
            apply()
        }
    }

    private fun loadFromPrefs() {
        if (prefs == null) return

        try {
            _isLoggedIn.value = prefs.getBoolean("is_logged_in", false)

            val profileStr = prefs.getString("user_profile", null)
            if (profileStr != null) {
                val p = JSONObject(profileStr)
                _userProfile.value = UserProfile(
                    nama = p.optString("nama", "Admin"),
                    nis = p.optString("nis", ""),
                    kelas = p.optString("kelas", ""),
                    noHp = p.optString("noHp", "+62 811 2222 4444"),
                    password = p.optString("password", "")
                )
            }

            val txStr = prefs.getString("transactions", null)
            if (txStr != null) {
                val arr = JSONArray(txStr)
                val list = mutableListOf<Transaction>()
                for (i in 0 until arr.length()) {
                    val obj = arr.getJSONObject(i)
                    list.add(
                        Transaction(
                            id = obj.optString("id", java.util.UUID.randomUUID().toString()),
                            title = obj.getString("title"),
                            amount = obj.getLong("amount"),
                            type = TransactionType.valueOf(obj.getString("type")),
                            date = obj.getString("date")
                        )
                    )
                }
                _transactions.value = list
            }

            val stStr = prefs.getString("students", null)
            if (stStr != null) {
                val arr = JSONArray(stStr)
                val list = mutableListOf<Siswa>()
                for (i in 0 until arr.length()) {
                    val obj = arr.getJSONObject(i)
                    list.add(
                        Siswa(
                            no = obj.getInt("no"),
                            nama = obj.getString("nama"),
                            peran = obj.getString("peran"),
                            statusJuli = StatusBayar.valueOf(obj.optString("statusJuli", "LUNAS")),
                            statusAgustus = StatusBayar.valueOf(obj.optString("statusAgustus", "BELUM")),
                            kelas = obj.optString("kelas", "XII PPLG"),
                            noHp = obj.optString("noHp", "")
                        )
                    )
                }
                _students.value = list
            }

            val prStr = prefs.getString("payment_records", null)
            if (prStr != null) {
                val arr = JSONArray(prStr)
                val list = mutableListOf<PaymentRecord>()
                for (i in 0 until arr.length()) {
                    val obj = arr.getJSONObject(i)
                    list.add(
                        PaymentRecord(
                            no = obj.getInt("no"),
                            tanggal = obj.getString("tanggal"),
                            namaSiswa = obj.getString("namaSiswa"),
                            jumlah = obj.getLong("jumlah")
                        )
                    )
                }
                _paymentRecords.value = list
            }
        } catch (e: Exception) {
            e.printStackTrace()
            loadInitialData()
        }
    }

    fun login(nameOrEmail: String, password: String) {
        _userProfile.value = _userProfile.value.copy(nama = nameOrEmail, password = password)
        _isLoggedIn.value = true
        saveToPrefs()
    }

    fun register(nama: String, nis: String, kelas: String, noHp: String, password: String) {
        _userProfile.value = UserProfile(nama = nama, nis = nis, kelas = kelas, noHp = noHp, password = password)
        _isLoggedIn.value = true
        saveToPrefs()
    }

    fun logout() {
        _isLoggedIn.value = false
        saveToPrefs()
    }

    fun addTransaction(title: String, amount: Long, type: TransactionType) {
        val newTx = Transaction(
            title = title,
            amount = amount,
            type = type,
            date = getCurrentDateString()
        )
        _transactions.value = listOf(newTx) + _transactions.value
        saveToPrefs()
    }

    fun deleteTransaction(id: String) {
        _transactions.value = _transactions.value.filterNot { it.id == id }
        saveToPrefs()
    }

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
        val totalIncome = _transactions.value.filter { it.type == TransactionType.INCOME }.sumOf { it.amount }
        val totalExpense = _transactions.value.filter { it.type == TransactionType.EXPENSE }.sumOf { it.amount }
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
            ✅ Siswa Lunas (Juli): ${_students.value.count { it.statusJuli == StatusBayar.LUNAS }} Siswa
            ❌ Siswa Belum (Juli): ${_students.value.count { it.statusJuli == StatusBayar.BELUM }} Siswa
            ------------------------------------
            Dibuat secara otomatis melalui aplikasi KASTEP.
        """.trimIndent()
    }
}
