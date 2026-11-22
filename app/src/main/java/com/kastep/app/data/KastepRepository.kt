// ==========================================================================
// PETA FILE · KASTEP (Kelompok 1) · KastepRepository.kt
// Isi file : Pusat data offline: akun, siswa, status kas bulanan, denda, pembayaran, pengeluaran, laporan, penyimpanan.
// Penanda  : CARI-PUSAT-DATA, CARI-NOMINAL-KAS, CARI-DENDA, CARI-SIMPAN-DATA, CARI-LOGIN, CARI-VALIDASI, CARI-REGISTER, CARI-PROFIL, CARI-ANGGOTA, CARI-PEMBAYARAN, CARI-DISPENSASI, CARI-PENGELUARAN, CARI-DATA-AWAL, CARI-LAPORAN, CARI-SALDO
// Cara cari: VS Code Ctrl+Shift+F (Android Studio Ctrl+Shift+F) lalu ketik
//            "CARI-" untuk semua penanda, atau salah satu nama di baris Penanda.
// ==========================================================================
//  BACA INI DULU : INI "OTAK" KASTEP
//  Semua data dan rumus ada di file ini. Layar hanya menampilkan hasilnya.
//  Urutan isi file:
//    1. Daftar data hidup : _students, _paymentRecords, _pengeluaranList, _userProfile, _isLoggedIn
//    2. Konstanta         : KAS_MINGGUAN 5.000, DENDA_PER_BULAN 5.000, DATA_KEY, akun admin bawaan
//    3. Fungsi bantu      : parseBulan, jumlahMinggu (hitung Senin sebulan), bulanDariTanggal, kunciTanggal
//    4. AUTH              : login, register, updateProfile, logout
//    5. Kelola siswa      : addStudent, updateStudent, deleteStudent
//    6. STATUS KAS        : getBulanKas, mingguLunas, statusBulan, sisaKas
//    7. DENDA             : bulanTerlambat, calculateDenda, totalDenda
//    8. processPayment    : mencatat pembayaran (KHUSUS ADMIN), satu bulan atau rentang bulan (processPaymentRange)
//    9. Pengeluaran       : addPengeluaran, updatePengeluaran, deletePengeluaran
//   10. Reset dan laporan : resetData, generateReportText
//   11. Data awal         : loadInitialData (33 siswa, riwayat, pengeluaran asli buku kas)
//   12. Penyimpanan HP    : saveToPrefs dan loadFromPrefs (JSON di SharedPreferences "kastep_prefs")
//  JAWABAN CEPAT:
//   - Tagihan satu bulan = jumlah Senin di bulan itu x Rp5.000 (4 atau 5 minggu).
//   - Denda = Rp5.000 x jumlah bulan terlambat, hanya untuk bulan yang belum LUNAS. Bulan berjalan belum didenda.
//   - Saldo = total pembayaran - total pengeluaran = Rp1.389.000.
//   - Data disimpan di HP (JSON), belum ada server atau Firebase. Password disimpan apa adanya (belum di-hash).
// » package com.kastep.app.data = alamat folder file ini (data dan logika).
package com.kastep.app.data

// » Context = informasi aplikasi di HP. Dipakai untuk membuka penyimpanan lokal.
import android.content.Context
// » SharedPreferences = penyimpanan kunci-nilai kecil di HP (mirip buku catatan kecil).
import android.content.SharedPreferences
// » MutableStateFlow = wadah nilai yang bisa diubah.
import kotlinx.coroutines.flow.MutableStateFlow
// » StateFlow = versi baca-saja dari wadah tadi.
import kotlinx.coroutines.flow.StateFlow
// » asStateFlow = ubah jadi baca-saja.
import kotlinx.coroutines.flow.asStateFlow
// » JSONArray = daftar data dalam format JSON (teks terstruktur).
import org.json.JSONArray
// » JSONObject = satu objek data dalam format JSON.
import org.json.JSONObject
// » SimpleDateFormat = format tanggal (dipakai untuk membaca/menulis tanggal).
import java.text.SimpleDateFormat
// » Calendar = alat hitung kalender (bulan, hari).
import java.util.Calendar
// » Date = tanggal dalam bentuk waktu.
import java.util.Date
// » Locale = pengaturan bahasa untuk format tanggal.
import java.util.Locale

// Satu-satunya tempat yang boleh mengubah data. context dipakai untuk SharedPreferences (penyimpanan lokal HP).
// » Komentar: satu-satunya tempat yang boleh mengubah data.
// CARI-PUSAT-DATA | Semua perubahan data lewat sini; layar membaca lewat KastepViewModel.
// » class KastepRepository = kelas penyimpan data. context boleh kosong (null) untuk tes.
class KastepRepository(context: Context? = null) {

    // » prefs = penyimpanan lokal HP dengan nama "kastep_prefs". ?. = kalau context null, prefs ikut null.
    private val prefs: SharedPreferences? = context?.getSharedPreferences("kastep_prefs", Context.MODE_PRIVATE)

    // Daftar data "hidup" (MutableStateFlow). Awalan _ = versi yang bisa diubah (private), tanpa _ = versi baca-saja untuk ViewModel.
    // Begitu isinya berubah, semua layar yang membacanya digambar ulang otomatis.
    // » _isLoggedIn = status login yang bisa diubah. Awalnya false (belum login). private = hanya di kelas ini.
    private val _isLoggedIn = MutableStateFlow(false)
    // » isLoggedIn = versi baca-saja, dipakai ViewModel.
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    // » _userProfile = data akun yang login. Awalnya UserProfile() (kosong).
    private val _userProfile = MutableStateFlow(UserProfile())
    // » userProfile = versi baca-saja.
    val userProfile: StateFlow<UserProfile> = _userProfile.asStateFlow()

    // » _students = daftar siswa yang bisa diubah. emptyList() = kosong di awal.
    private val _students = MutableStateFlow<List<Siswa>>(emptyList())
    // » students = versi baca-saja.
    val students: StateFlow<List<Siswa>> = _students.asStateFlow()

    // » _paymentRecords = riwayat pembayaran. Awalnya kosong.
    private val _paymentRecords = MutableStateFlow<List<PaymentRecord>>(emptyList())
    // » paymentRecords = versi baca-saja.
    val paymentRecords: StateFlow<List<PaymentRecord>> = _paymentRecords.asStateFlow()

    // » _pengeluaranList = daftar pengeluaran. Awalnya kosong.
    private val _pengeluaranList = MutableStateFlow<List<Pengeluaran>>(emptyList())
    // » pengeluaranList = versi baca-saja.
    val pengeluaranList: StateFlow<List<Pengeluaran>> = _pengeluaranList.asStateFlow()

    // Daftar nama mapel untuk layar Mata Pelajaran. Jadwal lengkapnya ditulis tetap di MataPelajaranScreen.kt.
    // » mataPelajaran = daftar nama mapel. listOf(...) = daftar tetap.
    val mataPelajaran = listOf(
        // » MataPelajaran("BAHASA INGGRIS") = satu mapel. Baris berikutnya berisi mapel lain.
        MataPelajaran("BAHASA INGGRIS"), MataPelajaran("PDL RPL"),
        // » Mapel PABP Hindu dan Bahasa Indonesia.
        MataPelajaran("BAHASA INDONESIA"), MataPelajaran("PABP HINDU"),
        // » Mapel Bahasa Bali dan PABP Kristen.
        MataPelajaran("BAHASA BALI"), MataPelajaran("PABP KRISTEN"),
        // » Mapel RPL dan PP.
        MataPelajaran("RPL"), MataPelajaran("PP"),
        // » Mapel KIK dan MAT.
        MataPelajaran("KIK"), MataPelajaran("MAT")
    )

    // Akun terdaftar: email -> profil lengkap (termasuk password). Akun bendahara bawaan: admin@gmail.com / admin123.
    // Akun terdaftar: email -> profil lengkap (termasuk password)
    // » _registeredUsers = daftar semua akun: email -> profil. mutableMapOf = peta yang bisa diubah.
    private val _registeredUsers = mutableMapOf<String, UserProfile>()

    companion object {
        // » companion object = bagian milik kelas (bisa dipakai tanpa membuat objek).
        // CARI-NOMINAL-KAS | Iuran per minggu (setiap Senin). Tagihan satu bulan = jumlah Senin x nilai ini.
        // » KAS_MINGGUAN = iuran per minggu Rp5.000. const = nilai tetap saat kompilasi. L = tipe Long.
        const val KAS_MINGGUAN = 5000L
        // CARI-DENDA | Besar denda untuk setiap bulan keterlambatan. Ubah angka ini untuk mengganti tarif.
        // » DENDA_PER_BULAN = denda Rp5.000 per bulan keterlambatan.
        const val DENDA_PER_BULAN = 5000L
        // CARI-SIMPAN-DATA | Kunci penyimpanan. Naikkan versinya bila data awal diganti supaya HP memuat ulang.
        // » DATA_KEY = nama kunci penyimpanan data. Ganti angka versinya bila data awal diubah.
        private const val DATA_KEY = "saved_data_v3_pengurus"
        // » NAMA_BULAN = daftar nama bulan Indonesia, urutannya Januari (indeks 0) sampai Desember.
        private val NAMA_BULAN = listOf(
            "Januari", "Februari", "Maret", "April", "Mei", "Juni",
            "Juli", "Agustus", "September", "Oktober", "November", "Desember"
        )
        // » BULAN_KAS_AWAL = bulan-bulan awal tagihan kas.
        private val BULAN_KAS_AWAL = listOf("Juli 2026", "Agustus 2026", "September 2026", "Oktober 2026")
        // CARI-LOGIN | Akun admin bawaan: admin@gmail.com / admin123.
        // » ADMIN = akun bendahara bawaan (admin@gmail.com, password admin123).
        private val ADMIN = UserProfile(
            nama = "Administrator", email = "admin@gmail.com", password = "admin123",
            role = UserRole.ADMIN, kelas = "XII PPLG"
        )

        // Mengubah "Agustus 2026" jadi pasangan (2026, 8) supaya bulan bisa dibandingkan dan dihitung selisihnya.
        /** "Agustus 2026" -> (2026, 8) */
        // » parseBulan = mengubah "Agustus 2026" jadi pasangan (tahun, bulan). Hasil null kalau format salah.
        fun parseBulan(label: String): Pair<Int, Int>? {
            // » split(" ") = pisahkan teks per spasi: ["Agustus", "2026"].
            val parts = label.trim().split(" ")
            // » Kalau bagiannya bukan dua, berarti format salah: kembalikan null.
            if (parts.size != 2) return null
            // » Cari nomor bulan dari nama (Agustus = 8). indexOfFirst = posisi pertama yang cocok (tanpa huruf
            // » besar/kecil), +1 karena posisi mulai dari 0.
            val m = NAMA_BULAN.indexOfFirst { it.equals(parts[0], ignoreCase = true) } + 1
            // » Ubah bagian tahun jadi angka. Kalau gagal, kembalikan null.
            val y = parts[1].toIntOrNull() ?: return null
            // » Kalau bulan ditemukan (m > 0), kembalikan pasangan tahun to bulan. Kalau tidak, null.
            return if (m > 0) y to m else null
        }

        // Menghitung jumlah hari SENIN dalam satu bulan (4 atau 5). Contoh "Agustus 2026" = 5.
        // Penting: tagihan bulan itu = jumlah Senin x Rp5.000. Loop semua tanggal bulan itu dan hitung yang harinya Senin.
        // CARI-NOMINAL-KAS | Menghitung jumlah hari Senin dalam sebulan (4 atau 5) = jumlah minggu kas.
        /** Jumlah hari Senin (hari kas) dalam satu bulan. */
        // » jumlahMinggu = hitung jumlah Senin dalam bulan. Kalau format bulan salah, anggap 4.
        fun jumlahMinggu(bulan: String): Int {
            // » Ambil tahun dan bulan. "?: return 4" = kalau null, kembalikan 4.
            val (y, m) = parseBulan(bulan) ?: return 4
            // » Kalender diatur ke tanggal 1 bulan itu.
            val cal = Calendar.getInstance().apply { clear(); set(y, m - 1, 1) }
            // » getActualMaximum = jumlah hari dalam bulan (28 sampai 31).
            val days = cal.getActualMaximum(Calendar.DAY_OF_MONTH)
            // » Hitung tanggal 1 sampai akhir bulan yang harinya MONDAY (Senin). count = jumlahkan yang cocok.
            return (1..days).count { d -> cal.set(Calendar.DAY_OF_MONTH, d); cal.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY }
        }

        // Mengambil bulan dari tanggal teks ("15 Juli 2026" atau "15/07/2026"). Dipakai grafik dan filter periode untuk mengelompokkan pengeluaran.
        /** Tanggal "15 Juli 2026" atau "15/07/2026" -> (2026, 7) */
        // » bulanDariTanggal = ambil tahun dan bulan dari teks tanggal.
        fun bulanDariTanggal(tanggal: String): Pair<Int, Int>? {
            // » t = tanggal tanpa spasi di awal dan akhir.
            val t = tanggal.trim()
            // » Coba format angka "15/07/2026": grup ke-2 = tahun, grup ke-1 = bulan. Kalau cocok, langsung kembalikan.
            Regex("""^\d{1,2}/(\d{1,2})/(\d{4})$""").find(t)?.let { return it.groupValues[2].toInt() to it.groupValues[1].toInt() }
            // » Pisahkan teks per spasi, mis. ["15", "Juli", "2026"].
            val parts = t.split(" ")
            // » Kalau ada tiga bagian, cari bulan dari bagian ke-2 dan ke-3. Kalau tidak, null.
            return if (parts.size == 3) parseBulan("${parts[1]} ${parts[2]}") else null
        }

        // Mengubah tanggal teks jadi angka yyyyMMdd supaya transaksi bisa diurutkan dari yang terbaru (tanggal disimpan sebagai teks).
        /** Kunci urut tanggal (yyyyMMdd) untuk mengurutkan transaksi terbaru. */
        // » kunciTanggal = ubah tanggal jadi angka urut yyyyMMdd (contoh 20260815), supaya bisa diurutkan.
        fun kunciTanggal(tanggal: String): Int {
            // » t = tanggal tanpa spasi.
            val t = tanggal.trim()
            // » Coba format "15/07/2026": ubah jadi tahun*10000 + bulan*100 + tanggal.
            Regex("""^(\d{1,2})/(\d{1,2})/(\d{4})$""").find(t)?.let { val g = it.groupValues; return g[3].toInt() * 10000 + g[2].toInt() * 100 + g[1].toInt() }
            // » Pisahkan teks per spasi.
            val parts = t.split(" ")
            // » Kalau ada tiga bagian (format "15 Juli 2026")...
            if (parts.size == 3) {
                // » ...ambil pasangan tahun-bulan. Kalau gagal, kembalikan 0.
                val ym = parseBulan("${parts[1]} ${parts[2]}") ?: return 0
                // » Hitung kunci: tahun*10000 + bulan*100 + tanggal. Tanggal diambil dari bagian pertama.
                return ym.first * 10000 + ym.second * 100 + (parts[0].toIntOrNull() ?: 0)
            }
            // » Format tidak dikenal = 0 (diletakkan paling bawah saat diurutkan).
            return 0
        }
    }

    init {
        // » init = blok yang dijalankan saat objek repository dibuat.
        _registeredUsers[ADMIN.email] = ADMIN
        // » Daftarkan akun admin bawaan ke daftar akun.
        loadData()
    // » Muat data (dari HP atau data awal).
    }

    // Urutan saat aplikasi dibuka: bila di HP sudah ada data tersimpan (DATA_KEY) -> loadFromPrefs. Bila belum (install pertama) ->
    // loadInitialData (buku kas asli) lalu simpan.
    // Saat aplikasi dibuka: muat data tersimpan, atau pakai data awal buku kas bila belum ada.
    private fun loadData() {
        // » Kalau ada prefs dan kunci DATA_KEY sudah tersimpan...
        if (prefs != null && prefs.contains(DATA_KEY)) {
            // » ...muat data dari HP.
            loadFromPrefs()
        // » Kalau belum ada (install pertama)...
        } else {
            // » ...isi data awal buku kas...
            loadInitialData()
            // » ...lalu simpan ke HP.
            saveToPrefs()
        }
    }

    // » Komentar: bagian login, daftar, profil, dan logout.
    // ==================== AUTH ====================

    // LOGIN. Hasil: null = berhasil, teks = pesan error yang ditampilkan di layar Login. Pemeriksaan berurutan:
    //   email kosong -> password kosong -> format email (harus ada @ dan titik) -> password minimal 5 karakter ->
    //   akun harus terdaftar -> password harus cocok. Bila lolos semua, profil diisi dan status login true.
    // CARI-LOGIN CARI-VALIDASI | Cek format email, panjang password (min. 5), lalu cocokkan dengan akun terdaftar.
    // TANYA-GURU: Validasi login? Email harus ada @ dan titik, password minimal 5 karakter, lalu dicocokkan dengan akun terdaftar.
    // » login = cek email dan password. Hasil null = berhasil; teks = pesan error.
    fun login(email: String, password: String): String? {
        // » trimEmail = email tanpa spasi, huruf kecil.
        val trimEmail = email.trim().lowercase()
        // » trimPass = password tanpa spasi.
        val trimPass = password.trim()

        // » Email kosong: kembalikan pesan error.
        if (trimEmail.isBlank()) return "Email tidak boleh kosong"
        // » Password kosong: kembalikan pesan error.
        if (trimPass.isBlank()) return "Password tidak boleh kosong"
        // » Format email harus punya @ dan titik.
        if (!trimEmail.contains("@") || !trimEmail.contains(".")) return "Format email tidak valid"
        // » Password minimal 5 karakter.
        if (trimPass.length < 5) return "Password minimal 5 karakter"

        // » Cari akun dengan email itu. "?: return" = kalau tidak ada, kembalikan pesan.
        val registered = _registeredUsers[trimEmail]
            ?: return "Akun tidak ditemukan. Silakan register terlebih dahulu."
        // » Password tidak cocok: kembalikan pesan.
        if (registered.password != trimPass) return "Password salah"

        // » Simpan profil akun ini sebagai yang sedang login.
        _userProfile.value = registered
        // » Tandai sudah login.
        _isLoggedIn.value = true
        // » Simpan ke HP.
        saveToPrefs()
        // » null = tidak ada error (berhasil).
        return null
    }

    // DAFTAR. Validasi: nama minimal 3 huruf, email valid, password minimal 5, email belum terdaftar. Akun baru selalu USER (bukan ADMIN).
    // CARI-REGISTER CARI-VALIDASI | Akun baru selalu ber-role USER; email tidak boleh dobel.
    // » register = daftarkan akun baru. Selalu berperan USER.
    fun register(nama: String, email: String, nis: String, kelas: String, noHp: String, password: String): String? {
        // » trimNama = nama tanpa spasi.
        val trimNama = nama.trim()
        // » trimEmail = email huruf kecil tanpa spasi.
        val trimEmail = email.trim().lowercase()
        // » trimPass = password tanpa spasi.
        val trimPass = password.trim()

        // » Nama kosong: pesan error.
        if (trimNama.isBlank()) return "Nama tidak boleh kosong"
        // » Nama minimal 3 karakter.
        if (trimNama.length < 3) return "Nama minimal 3 karakter"
        // » Email kosong: pesan error.
        if (trimEmail.isBlank()) return "Email tidak boleh kosong"
        // » Format email tidak valid.
        if (!trimEmail.contains("@") || !trimEmail.contains(".")) return "Format email tidak valid"
        // » Password kosong: pesan error.
        if (trimPass.isBlank()) return "Password tidak boleh kosong"
        // » Password minimal 5 karakter.
        if (trimPass.length < 5) return "Password minimal 5 karakter"
        // » Email sudah dipakai: pesan error.
        if (_registeredUsers.containsKey(trimEmail)) return "Email sudah terdaftar"

        // » Buat profil baru.
        val profile = UserProfile(
            // » nama, email, dan nis (dibuang spasi di ujung).
            nama = trimNama, email = trimEmail, nis = nis.trim(),
            // » kelas: kalau kosong pakai "XII PPLG". noHp: tanpa spasi.
            kelas = kelas.ifBlank { "XII PPLG" }, noHp = noHp.trim(),
            // » password dan role = USER.
            password = trimPass, role = UserRole.USER
        )
        // » Simpan akun ke daftar akun.
        _registeredUsers[trimEmail] = profile
        // » Akun ini langsung jadi akun yang login.
        _userProfile.value = profile
        // » Tandai sudah login.
        _isLoggedIn.value = true
        // » Simpan ke HP.
        saveToPrefs()
        // » null = berhasil.
        return null
    }

    // Ubah nama dan No. HP akun yang login (nama minimal 3 huruf). Perubahan juga ditulis ke daftar akun terdaftar.
    // CARI-PROFIL | Simpan perubahan nama & No. HP dari dialog Edit Akun.
    // » updateProfile = ubah nama dan nomor HP akun yang login.
    fun updateProfile(nama: String, noHp: String): String? {
        // » trimNama = nama tanpa spasi.
        val trimNama = nama.trim()
        // » Nama minimal 3 karakter. Kalau kurang, pesan error.
        if (trimNama.length < 3) return "Nama minimal 3 karakter"
        // » copy = salin profil dengan nama dan HP baru (field lain tetap).
        val updated = _userProfile.value.copy(nama = trimNama, noHp = noHp.trim())
        // » Simpan ke profil yang sedang login.
        _userProfile.value = updated
        // » Kalau punya email, perbarui juga di daftar akun.
        if (updated.email.isNotBlank()) _registeredUsers[updated.email] = updated
        // » Simpan ke HP.
        saveToPrefs()
        // » null = berhasil.
        return null
    }

    // Keluar akun: profil dikosongkan dan status login false, lalu disimpan.
    // » logout = keluar dari akun.
    fun logout() {
        // » Kosongkan profil (kembali ke UserProfile() awal).
        _userProfile.value = UserProfile()
        // » Tandai belum login.
        _isLoggedIn.value = false
        // » Simpan ke HP.
        saveToPrefs()
    }

    // ==================== STUDENT CRUD ====================

    // Tambah siswa (hanya lewat layar Data Siswa akun ADMIN). Nama dijadikan HURUF BESAR dan tidak boleh kembar. Nomor urut = nomor terbesar + 1.
    // CARI-ANGGOTA CARI-VALIDASI | Tambah siswa; nama disimpan huruf besar dan tidak boleh sama dengan siswa lain.
    // » addStudent = tambah siswa baru (khusus admin lewat layar Data Siswa).
    fun addStudent(nama: String, peran: String): String? {
        // » clean = nama tanpa spasi di ujung, diubah jadi HURUF BESAR.
        val clean = nama.trim().uppercase()
        // » Nama kosong: kembalikan pesan error.
        if (clean.isBlank()) return "Nama siswa tidak boleh kosong"
        // » any = cek apakah ada siswa lain dengan nama sama. Kalau ada, error.
        if (_students.value.any { it.nama == clean }) return "Siswa dengan nama ini sudah ada"
        // » nextNo = nomor urut baru = nomor terbesar + 1. Kalau daftar kosong, mulai dari 1.
        val nextNo = (_students.value.maxOfOrNull { it.no } ?: 0) + 1
        // » Tambah siswa baru ke daftar. Kalau peran kosong, pakai "Anggota".
        _students.value = _students.value + Siswa(no = nextNo, nama = clean, peran = peran.ifBlank { "Anggota" })
        // » Simpan ke HP.
        saveToPrefs()
        // » null = berhasil.
        return null
    }

    // Ubah nama atau peran siswa. Riwayat pembayaran ikut memakai nama baru supaya tetap terhubung ke siswa itu.
    // CARI-ANGGOTA | Ganti nama/peran; riwayat pembayaran ikut memakai nama baru.
    // » updateStudent = ubah nama dan peran siswa dengan nomor urut no.
    fun updateStudent(no: Int, nama: String, peran: String): String? {
        // » clean = nama baru tanpa spasi, huruf besar.
        val clean = nama.trim().uppercase()
        // » Nama kosong: error.
        if (clean.isBlank()) return "Nama siswa tidak boleh kosong"
        // » Nama dipakai siswa lain (nomor berbeda): error.
        if (_students.value.any { it.nama == clean && it.no != no }) return "Siswa dengan nama ini sudah ada"
        // » oldName = nama lama siswa itu (untuk mengubah riwayat).
        val oldName = _students.value.find { it.no == no }?.nama
        // » map = buat daftar baru: baris yang nomornya cocok diganti, sisanya tetap.
        _students.value = _students.value.map {
            // » copy = salin siswa dengan nama dan peran baru.
            if (it.no == no) it.copy(nama = clean, peran = peran.ifBlank { "Anggota" }) else it
        }
        // Riwayat pembayaran ikut memakai nama baru agar tetap terhubung ke siswa
        // » Kalau nama benar-benar berubah...
        if (oldName != null && oldName != clean) {
            // » ...ganti juga nama di semua riwayat pembayaran yang memakai nama lama.
            _paymentRecords.value = _paymentRecords.value.map { if (it.namaSiswa == oldName) it.copy(namaSiswa = clean) else it }
        }
        // » Simpan ke HP.
        saveToPrefs()
        // » null = berhasil.
        return null
    }

    // Hapus siswa lalu nomor urut dibuat ulang 1,2,3. Riwayat pembayaran lama TIDAK dihapus (uangnya tetap tercatat di saldo).
    // Menghapus siswa lalu menomori ulang daftar. Riwayat pembayaran lamanya tetap ada.
    // » deleteStudent = hapus siswa dengan nomor urut no.
    fun deleteStudent(no: Int) {
        // » filter = ambil semua siswa KECUALI yang nomornya no.
        _students.value = _students.value.filter { it.no != no }
            // » mapIndexed = beri nomor ulang 1, 2, 3, dst.
            .mapIndexed { index, siswa -> siswa.copy(no = index + 1) }
        // » Simpan ke HP. Riwayat pembayaran lama tidak dihapus.
        saveToPrefs()
    }

    // ==================== BULAN & STATUS KAS ====================

    // Daftar bulan tagihan: Juli 2026 sampai bulan sekarang (bertambah otomatis tiap bulan). Dipakai kolom bulan di Data Siswa, filter periode, dan dialog bayar.
    // CARI-PEMBAYARAN | Daftar bulan yang ditagih: Juli 2026 sampai bulan berjalan (otomatis bertambah).
    /** Bulan kas semester ganjil: Juli 2026 sampai bulan berjalan. */
    // » getBulanKas = daftar bulan tagihan dari Juli 2026 sampai bulan sekarang.
    fun getBulanKas(): List<String> {
        // » cal = kalender hari ini.
        val cal = Calendar.getInstance()
        // » nowKey = nomor bulan sekarang = tahun x 12 + bulan (0 = Januari). Cara menghitung bulan jadi satu angka.
        val nowKey = cal.get(Calendar.YEAR) * 12 + cal.get(Calendar.MONTH)
        // » result = daftar hasil, mula-mula kosong.
        val result = mutableListOf<String>()
        // » key = mulai dari Juli 2026 (2026 x 12 + 6).
        var key = 2026 * 12 + 6 // Juli 2026
        // » Ulangi selama key belum melewati bulan sekarang, atau jumlah bulan belum mencapai 4 (bulan awal).
        while (key <= nowKey || result.size < BULAN_KAS_AWAL.size) {
            // » Tambah teks bulan: nama bulan (key mod 12) + tahun (key dibagi 12).
            result.add("${NAMA_BULAN[key % 12]} ${key / 12}")
            // » Lanjut ke bulan berikutnya.
            key++
        }
        // » Kembalikan daftar bulan.
        return result
    }

    // Jumlah minggu yang dianggap lunas pada satu bulan = minggu dibayar + minggu dispensasi, dibatasi maksimal jumlah Senin bulan itu.
    // Jumlah minggu lunas satu bulan = minggu dibayar + minggu dispensasi (maksimal jumlah Senin).
    // » mingguLunas = jumlah minggu lunas satu bulan.
    fun mingguLunas(siswa: Siswa, bulan: String): Int =
        // » bayar + dispensasi (kalau bulan belum ada, 0). coerceAtMost = jangan lebih dari jumlah Senin.
        ((siswa.bayar[bulan] ?: 0) + (siswa.dispensasi[bulan] ?: 0)).coerceAtMost(jumlahMinggu(bulan))

    // LUNAS atau BELUM untuk satu siswa di satu bulan. LUNAS bila mingguLunas >= jumlah Senin bulan itu.
    // CARI-PEMBAYARAN CARI-DISPENSASI | LUNAS bila minggu dibayar + minggu dispensasi >= jumlah Senin bulan itu.
    // » statusBulan = status satu bulan untuk siswa.
    fun statusBulan(siswa: Siswa, bulan: String): StatusBayar =
        // » Kalau minggu lunas >= jumlah Senin: LUNAS. Kalau kurang: BELUM.
        if (mingguLunas(siswa, bulan) >= jumlahMinggu(bulan)) StatusBayar.LUNAS else StatusBayar.BELUM

    // Kekurangan kas satu bulan dalam rupiah = (jumlah Senin - minggu lunas) x Rp5.000. Contoh Agustus (5 Senin), baru 2 minggu: 3 x 5.000 = Rp15.000.
    // CARI-PEMBAYARAN | Kekurangan kas satu bulan = minggu belum lunas x Rp5.000.
    /** Sisa kas yang belum dibayar untuk satu bulan. */
    // » sisaKas = uang yang belum dibayar satu bulan dalam rupiah.
    fun sisaKas(siswa: Siswa, bulan: String): Long =
        // » (jumlah Senin - minggu lunas), tidak boleh minus, dikali Rp5.000.
        (jumlahMinggu(bulan) - mingguLunas(siswa, bulan)).coerceAtLeast(0) * KAS_MINGGUAN

    // n di rumus denda = berapa bulan tagihan itu sudah lewat dari bulan sekarang. Bulan berjalan n = 0 (belum didenda).
    // Contoh bulan ini November: tagihan Oktober n=1, Juli n=4.
    // Berapa bulan sebuah tagihan sudah lewat dari bulan sekarang (n di rumus denda).
    // » bulanTerlambat = berapa bulan tagihan sudah lewat dari bulan sekarang.
    private fun bulanTerlambat(bulan: String): Int {
        // » Ambil tahun dan bulan tagihan. Kalau format salah, anggap 0.
        val (y, m) = parseBulan(bulan) ?: return 0
        // » Kalender hari ini.
        val cal = Calendar.getInstance()
        // » now = bulan sekarang dalam bentuk nomor (tahun x 12 + bulan + 1).
        val now = cal.get(Calendar.YEAR) * 12 + cal.get(Calendar.MONTH) + 1
        // » Selisih bulan, tidak boleh minus (coerceAtLeast 0). Bulan berjalan = 0.
        return (now - (y * 12 + m)).coerceAtLeast(0)
    }

    // RUMUS DENDA satu siswa untuk satu bulan: bila bulan itu sudah LUNAS -> 0. Bila belum -> bulan terlambat x Rp5.000 (flat).
    // Contoh November: Juli belum lunas = 4 x 5.000 = Rp20.000. Tidak berlipat dan tidak naik per hari.
    // CARI-DENDA | Rumus: Rp5.000 x jumlah bulan terlambat, hanya untuk bulan yang belum lunas.
    //   Contoh: kas Juli belum lunas, sekarang November -> 4 x 5.000 = Rp20.000.
    /**
     * Denda keterlambatan: Rp5.000 x jumlah bulan terlambat, untuk bulan yang belum lunas.
     * Contoh: kas Juli belum lunas dan sekarang November -> 4 bulan x Rp5.000 = Rp20.000.
     */
    // TANYA-GURU: Rumus denda? Rp5.000 x n, n = bulan sekarang - bulan tagihan, hanya bulan yang belum LUNAS.
    // Contoh: kas Juli belum lunas, sekarang November: 4 x 5.000 = Rp20.000. Bulan berjalan belum didenda (n = 0).
    // » calculateDenda = denda satu siswa untuk satu bulan.
    fun calculateDenda(studentName: String, bulan: String): Long {
        // » Cari siswa dengan nama itu. Kalau tidak ada, denda 0.
        val siswa = _students.value.find { it.nama == studentName } ?: return 0L
        // » Kalau bulan sudah LUNAS, denda 0.
        if (statusBulan(siswa, bulan) == StatusBayar.LUNAS) return 0L
        // » Denda = bulan terlambat x Rp5.000 (DENDA_PER_BULAN).
        return bulanTerlambat(bulan) * DENDA_PER_BULAN
    }

    // Total denda satu siswa = jumlahkan denda semua bulan tagihan (Juli sampai sekarang) yang belum lunas.
    // Total denda satu siswa = jumlah denda semua bulan yang belum lunas.
    // » totalDenda = jumlah denda semua bulan kas untuk satu siswa.
    fun totalDenda(siswa: Siswa): Long = getBulanKas().sumOf { calculateDenda(siswa.nama, it) }

    // MENCATAT PEMBAYARAN satu bulan untuk satu siswa. HANYA ADMIN (bendahara). Langkahnya:
    //   0. tolak bila yang login bukan ADMIN. Siswa (USER) hanya bisa mengirim konfirmasi WA, tidak bisa mengubah status.
    //   1. validasi: siswa dipilih, bulan dipilih, siswa ada, bulan itu belum LUNAS (tidak bisa bayar dua kali)
    //   2. hitung denda dan sisa kas bulan itu
    //   3. buat PaymentRecord dengan jumlah = sisa kas + denda, taruh paling atas riwayat
    //   4. tandai bulan itu LUNAS: minggu dibayar diisi penuh (jumlah Senin dikurangi minggu dispensasi)
    //   5. simpan ke HP. Saldo otomatis naik karena dihitung dari semua riwayat.
    // Hasil null = berhasil, teks = pesan error.
    // CARI-PEMBAYARAN CARI-DENDA | Mencatat pembayaran (khusus Admin): jumlah = sisa kas + denda, lalu bulan itu menjadi LUNAS.
    // TANYA-GURU: Siapa yang membuat status jadi LUNAS? Hanya bendahara (Admin). Siswa transfer lalu kirim konfirmasi WA,
    // bendahara mengecek uangnya, lalu mencatat bulan dari sampai bulan berapa yang dibayar. Baru setelah itu status LUNAS.
    // » processPayment = catat pembayaran satu bulan (khusus admin).
    fun processPayment(studentName: String, bulan: String, metode: String): String? {
        // » Kalau yang login bukan ADMIN, tolak dengan pesan error.
        if (_userProfile.value.role != UserRole.ADMIN) return "Hanya bendahara (Admin) yang bisa mencatat pembayaran"
        // » Nama siswa kosong: error.
        if (studentName.isBlank()) return "Pilih siswa terlebih dahulu"
        // » Bulan kosong: error.
        if (bulan.isBlank()) return "Pilih bulan pembayaran"

        // » Cari siswa dengan nama itu.
        val siswa = _students.value.find { it.nama == studentName }
            // » Siswa tidak ada: error.
            ?: return "Siswa tidak ditemukan"
        // » Bulan itu sudah LUNAS: tolak (tidak boleh bayar dua kali).
        if (statusBulan(siswa, bulan) == StatusBayar.LUNAS) return "$studentName sudah lunas untuk $bulan"

        // » denda = denda bulan itu.
        val denda = calculateDenda(studentName, bulan)
        // » sisa = kekurangan kas bulan itu.
        val sisa = sisaKas(siswa, bulan)
        // » nextNo = nomor riwayat baru.
        val nextNo = (_paymentRecords.value.maxOfOrNull { it.no } ?: 0) + 1
        // » Buat satu baris riwayat pembayaran baru.
        val record = PaymentRecord(
            // » tanggal = hari ini.
            no = nextNo, tanggal = getCurrentDateString(),
            // » jumlah yang dibayar = sisa kas + denda.
            namaSiswa = studentName, jumlah = sisa + denda,
            // » bulan, metode bayar, dan denda ikut dicatat.
            bulan = bulan, metode = metode, denda = denda
        )
        // » Taruh riwayat baru di paling atas (terbaru di depan).
        _paymentRecords.value = listOf(record) + _paymentRecords.value

        // » wajib = jumlah Senin - minggu dispensasi: minggu yang harus dibayar bulan itu.
        val wajib = jumlahMinggu(bulan) - (siswa.dispensasi[bulan] ?: 0)
        // » Ubah daftar siswa: hanya siswa ini yang diubah.
        _students.value = _students.value.map {
            // » Isi kolom bayar untuk bulan itu dengan nilai wajib. Dengan begitu bulan jadi LUNAS.
            if (it.nama == studentName) it.copy(bayar = it.bayar + (bulan to wajib)) else it
        }
        // » Simpan ke HP. Saldo naik otomatis karena dihitung dari riwayat.
        saveToPrefs()
        // » null = berhasil.
        return null
    }

    // Mencatat beberapa bulan sekaligus untuk satu siswa, misalnya Juli sampai September (khusus Admin).
    // bulanList = bulan dari sampai bulan yang dipilih bendahara. Bulan yang sudah LUNAS di dalamnya dilewati.
    // Tiap bulan dicatat sebagai satu baris riwayat lewat processPayment, urut dari bulan paling awal.
    // CARI-PEMBAYARAN | Catat rentang bulan (dari bulan ... sampai bulan ...), hanya Admin.
    // » processPaymentRange = catat beberapa bulan sekaligus untuk satu siswa.
    fun processPaymentRange(studentName: String, bulanList: List<String>, metode: String): String? {
        // » Hanya ADMIN yang boleh.
        if (_userProfile.value.role != UserRole.ADMIN) return "Hanya bendahara (Admin) yang bisa mencatat pembayaran"
        // » Nama siswa kosong: error.
        if (studentName.isBlank()) return "Pilih siswa terlebih dahulu"
        // » Daftar bulan kosong: error.
        if (bulanList.isEmpty()) return "Pilih bulan pembayaran"
        // » Cari siswa.
        val siswa = _students.value.find { it.nama == studentName }
            // » Siswa tidak ada: error.
            ?: return "Siswa tidak ditemukan"
        // » belum = bulan dalam daftar yang belum LUNAS (yang sudah lunas dilewati).
        val belum = bulanList.filter { statusBulan(siswa, it) != StatusBayar.LUNAS }
        // » Kalau semua sudah lunas: error.
        if (belum.isEmpty()) return "$studentName sudah lunas untuk bulan yang dipilih"
        // » Untuk setiap bulan yang belum lunas...
        for (b in belum) {
            // » ...catat pembayarannya dengan processPayment.
            val err = processPayment(studentName, b, metode)
            // » Kalau ada error, hentikan dan kembalikan error itu.
            if (err != null) return err
        }
        // » null = semua berhasil.
        return null
    }

    // ==================== PENGELUARAN ====================

    // Tambah pengeluaran. Validasi: jumlah > 0 dan keterangan tidak kosong. Tanggal kosong otomatis diisi hari ini. Saldo turun otomatis.
    // CARI-PENGELUARAN CARI-VALIDASI | Tambah pengeluaran; nominal > 0 dan keterangan wajib.
    // » addPengeluaran = tambah pengeluaran baru (khusus admin).
    fun addPengeluaran(tanggal: String, jumlah: Long, keterangan: String): String? {
        // » Jumlah harus lebih dari 0. Kalau tidak, error.
        if (jumlah <= 0) return "Jumlah harus lebih dari 0"
        // » Keterangan wajib diisi. Kalau kosong, error.
        if (keterangan.isBlank()) return "Keterangan tidak boleh kosong"
        // » Buat data Pengeluaran baru.
        val p = Pengeluaran(
            // » Kalau tanggal kosong, pakai hari ini.
            tanggal = tanggal.ifBlank { getCurrentDateString() },
            // » jumlah dan keterangan (tanpa spasi di ujung).
            jumlah = jumlah, keterangan = keterangan.trim()
        )
        // » Taruh pengeluaran baru di paling atas.
        _pengeluaranList.value = listOf(p) + _pengeluaranList.value
        // » Simpan ke HP.
        saveToPrefs()
        // » null = berhasil.
        return null
    }

    // Ubah pengeluaran. Selain jumlah dan keterangan, tanggal harus terbaca (contoh 15 Juli 2026).
    // CARI-PENGELUARAN CARI-VALIDASI | Edit pengeluaran; tanggal harus terbaca (contoh 15 Juli 2026).
    // » updatePengeluaran = ubah pengeluaran dengan id tertentu.
    fun updatePengeluaran(id: String, tanggal: String, jumlah: Long, keterangan: String): String? {
        // » Jumlah harus lebih dari 0.
        if (jumlah <= 0) return "Jumlah harus lebih dari 0"
        // » Keterangan wajib.
        if (keterangan.isBlank()) return "Keterangan tidak boleh kosong"
        // » Tanggal harus bisa dibaca (contoh 15 Juli 2026).
        if (bulanDariTanggal(tanggal) == null) return "Format tanggal tidak dikenali (contoh: 15 Juli 2026)"
        // » map = ganti baris yang id-nya cocok.
        _pengeluaranList.value = _pengeluaranList.value.map {
            // » copy = salin dengan tanggal, jumlah, dan keterangan baru.
            if (it.id == id) it.copy(tanggal = tanggal.trim(), jumlah = jumlah, keterangan = keterangan.trim()) else it
        }
        // » Simpan ke HP.
        saveToPrefs()
        // » null = berhasil.
        return null
    }

    // Hapus pengeluaran. Saldo otomatis naik lagi.
    // Menghapus pengeluaran (khusus Admin), saldo otomatis bertambah lagi.
    // » deletePengeluaran = hapus pengeluaran dengan id tertentu.
    fun deletePengeluaran(id: String) {
        // » filter = ambil semua KECUALI id itu.
        _pengeluaranList.value = _pengeluaranList.value.filter { it.id != id }
        // » Simpan ke HP. Saldo naik lagi otomatis.
        saveToPrefs()
    }

    // ==================== RESET / REPORT ====================

    // Mengembalikan semua data ke buku kas awal, sesi login tetap dipertahankan. (Belum ada tombolnya di layar manapun.)
    // CARI-DATA-AWAL | Mengembalikan semua data ke buku kas awal (sesi admin tetap).
    // » resetData = kembalikan semua data ke buku kas awal (belum ada tombolnya di layar).
    fun resetData() {
        // Sesi admin tetap, data lain kembali ke buku kas awal
        // » Simpan profil akun yang login sekarang.
        val currentProfile = _userProfile.value
        // » Simpan status login sekarang.
        val loggedIn = _isLoggedIn.value
        // » Hapus SEMUA isi penyimpanan HP.
        prefs?.edit()?.clear()?.apply()
        // » Kosongkan daftar akun terdaftar.
        _registeredUsers.clear()
        // » Masukkan lagi akun admin bawaan.
        _registeredUsers[ADMIN.email] = ADMIN
        // » Muat ulang data buku kas awal.
        loadInitialData()
        // » Kembalikan profil akun yang tadi login (sesi tetap).
        _userProfile.value = currentProfile
        // » Kembalikan status login.
        _isLoggedIn.value = loggedIn
        // » Simpan data baru ke HP.
        saveToPrefs()
    }

    // Tanggal hari ini dengan format Indonesia, contoh "21 November 2026".
    // » getCurrentDateString = tanggal hari ini dalam format Indonesia.
    fun getCurrentDateString(): String {
        // » SimpleDateFormat("dd MMMM yyyy", id ID) = format "21 November 2026" dengan bahasa Indonesia.
        val sdf = SimpleDateFormat("dd MMMM yyyy", Locale("id", "ID"))
        // » Format waktu sekarang (Date()) jadi teks.
        return sdf.format(Date())
    }

    // Teks laporan untuk tombol "Bagikan Laporan": pemasukan, pengeluaran, saldo, total denda, jumlah siswa, dan rekap lunas/belum tiap bulan.
    // Tidak memuat nama siswa, jadi aman dibagikan ke grup.
    // CARI-LAPORAN CARI-SALDO | Teks laporan ringkas untuk tombol Bagikan Laporan (saldo = pemasukan - pengeluaran).
    fun generateReportText(): String {
        // » totalIncome = jumlah semua pembayaran di riwayat.
        val totalIncome = _paymentRecords.value.sumOf { it.jumlah }
        // » totalExpense = jumlah semua pengeluaran.
        val totalExpense = _pengeluaranList.value.sumOf { it.jumlah }
        // » saldo = pemasukan dikurangi pengeluaran.
        val saldo = totalIncome - totalExpense
        // » bulan = hanya bulan yang sudah terlambat (bulanTerlambat lebih dari 0).
        val bulan = getBulanKas().filter { bulanTerlambat(it) > 0 }
        // » rekapBulan = daftar teks rekap per bulan, dipisah baris baru (\n).
        val rekapBulan = bulan.joinToString("\n") { b ->
            // » Per bulan: hitung berapa siswa LUNAS (count = hitung yang memenuhi syarat).
            "• $b: ${_students.value.count { statusBulan(it, b) == StatusBayar.LUNAS }} lunas, " +
                // » Hitung berapa siswa yang BELUM lunas.
                "${_students.value.count { statusBulan(it, b) == StatusBayar.BELUM }} belum"
        }
        // » totalDenda = jumlah denda semua siswa.
        val totalDenda = _students.value.sumOf { totalDenda(it) }
        // » Teks laporan (raw string """ """) = teks multi-baris apa adanya.
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
    // » Akhir fungsi generateReportText.
    }

    // DATA AWAL = buku kas asli XII PPLG per 20 November 2026.
    //   bayarAwal      : 33 siswa dengan 4 angka = minggu dibayar bulan Juli, Agustus, September, Oktober
    //   dispensasiAwal : siswa yang BI/PKL (satu siswa), minggunya dihitung lunas
    //   lalu riwayat pembayaran dan pengeluaran asli diisi. Hasil: pemasukan, pengeluaran, dan saldo Rp1.389.000.
    // Data awal sesuai buku kas XII PPLG per 20 November 2026 (Rp5.000/minggu, Juni libur)
    // CARI-DATA-AWAL | Data awal dari buku kas asli XII PPLG per 20 November 2026: siswa, minggu terbayar, riwayat, pengeluaran.
    // » private fun loadInitialData = isi data awal dari buku kas asli. private = hanya dipakai di dalam kelas
    // » ini.
    private fun loadInitialData() {
        // minggu terbayar per bulan (Juli, Agustus, September, Oktober); minggu dispensasi BI/PKL dicatat terpisah
        // » bayarAwal = daftar awal siswa: (nama, peran, minggu dibayar Juli-Oktober). listOf = daftar tetap.
        val bayarAwal = listOf(
            // » Siswa no 1: BOYKE VILANO HAMONANGAN SIHITE (Anggota). Minggu dibayar Juli=4, Agustus=5, September=4,
            // » Oktober=0.
            Triple("BOYKE VILANO HAMONANGAN SIHITE", "Anggota", listOf(4, 5, 4, 0)),
            // » Siswa no 2: BINTANG LEONITA CHRISTYA RENATA (Anggota). Minggu dibayar Juli=0, Agustus=0, September=0,
            // » Oktober=0.
            Triple("BINTANG LEONITA CHRISTYA RENATA", "Anggota", listOf(0, 0, 0, 0)),
            // » Siswa no 3: CAROLINA TIMUTHY JANGGUR (Anggota). Minggu dibayar Juli=4, Agustus=5, September=0,
            // » Oktober=0.
            Triple("CAROLINA TIMUTHY JANGGUR", "Anggota", listOf(4, 5, 0, 0)),
            // » Siswa no 4: DEWA GEDE DALEM OKA ADNYANA SANDI (Anggota). Minggu dibayar Juli=4, Agustus=5, September=0,
            // » Oktober=0.
            Triple("DEWA GEDE DALEM OKA ADNYANA SANDI", "Anggota", listOf(4, 5, 0, 0)),
            // » Siswa no 5: GALISTAN RAMADHAN KURNIA TAUNAES (Ketua Kelas). Minggu dibayar Juli=4, Agustus=5,
            // » September=4, Oktober=4.
            Triple("GALISTAN RAMADHAN KURNIA TAUNAES", "Ketua Kelas", listOf(4, 5, 4, 4)),
            // » Siswa no 6: GEDE AGUS WIRA DARMA PUTRA (Bendahara 1). Minggu dibayar Juli=4, Agustus=5, September=0,
            // » Oktober=0.
            Triple("GEDE AGUS WIRA DARMA PUTRA", "Bendahara 1", listOf(4, 5, 0, 0)),
            // » Siswa no 7: I GEDE ABI WIRYA DINATA (Anggota). Minggu dibayar Juli=4, Agustus=0, September=0, Oktober=0.
            Triple("I GEDE ABI WIRYA DINATA", "Anggota", listOf(4, 0, 0, 0)),
            // » Siswa no 8: I GEDE DARMA SUPTIAWAN (Anggota). Minggu dibayar Juli=4, Agustus=1, September=0, Oktober=0.
            Triple("I GEDE DARMA SUPTIAWAN", "Anggota", listOf(4, 1, 0, 0)),
            // » Siswa no 9: I KOMANG RADITYA PUTRA (Anggota). Minggu dibayar Juli=4, Agustus=1, September=0, Oktober=0.
            Triple("I KOMANG RADITYA PUTRA", "Anggota", listOf(4, 1, 0, 0)),
            // » Siswa no 10: I KOMANG RISKI SETIAWAN (Anggota). Minggu dibayar Juli=2, Agustus=0, September=0,
            // » Oktober=0.
            Triple("I KOMANG RISKI SETIAWAN", "Anggota", listOf(2, 0, 0, 0)),
            // » Siswa no 11: I NYOMAN GEDE ARTA WIGUNA (Anggota). Minggu dibayar Juli=4, Agustus=1, September=0,
            // » Oktober=0.
            Triple("I NYOMAN GEDE ARTA WIGUNA", "Anggota", listOf(4, 1, 0, 0)),
            // » Siswa no 12: I PUTU DIKA LAKSMANA PUTRA (Anggota). Minggu dibayar Juli=4, Agustus=4, September=0,
            // » Oktober=0.
            Triple("I PUTU DIKA LAKSMANA PUTRA", "Anggota", listOf(4, 4, 0, 0)),
            // » Siswa no 13: I PUTU DITYA ARTHA WIJAYA (Anggota). Minggu dibayar Juli=2, Agustus=0, September=0,
            // » Oktober=0.
            Triple("I PUTU DITYA ARTHA WIJAYA", "Anggota", listOf(2, 0, 0, 0)),
            // » Siswa no 14: I PUTU PANDE ANDIKA (Anggota). Minggu dibayar Juli=4, Agustus=0, September=0, Oktober=0.
            Triple("I PUTU PANDE ANDIKA", "Anggota", listOf(4, 0, 0, 0)),
            // » Siswa no 15: I PUTU SUYOGA MAHENDRA (Anggota). Minggu dibayar Juli=4, Agustus=2, September=0, Oktober=0.
            Triple("I PUTU SUYOGA MAHENDRA", "Anggota", listOf(4, 2, 0, 0)),
            // » Siswa no 16: I WAYAN BAGUS PUTRAWAN (Anggota). Minggu dibayar Juli=4, Agustus=5, September=4, Oktober=4.
            Triple("I WAYAN BAGUS PUTRAWAN", "Anggota", listOf(4, 5, 4, 4)),
            // » Siswa no 17: I WAYAN PASEK KEVIN ARIADI (Anggota). Minggu dibayar Juli=4, Agustus=5, September=4,
            // » Oktober=4.
            Triple("I WAYAN PASEK KEVIN ARIADI", "Anggota", listOf(4, 5, 4, 4)),
            // » Siswa no 18: KADEK YUDA PRASETYA (Anggota). Minggu dibayar Juli=3, Agustus=0, September=0, Oktober=0.
            Triple("KADEK YUDA PRASETYA", "Anggota", listOf(3, 0, 0, 0)),
            // » Siswa no 19: KADEK YUNI CALLISTA PUTRI DEWI (Anggota). Minggu dibayar Juli=4, Agustus=4, September=0,
            // » Oktober=0.
            Triple("KADEK YUNI CALLISTA PUTRI DEWI", "Anggota", listOf(4, 4, 0, 0)),
            // » Siswa no 20: KOMANG DIAH PUTRI PRATIWI (Anggota). Minggu dibayar Juli=4, Agustus=2, September=0,
            // » Oktober=0.
            Triple("KOMANG DIAH PUTRI PRATIWI", "Anggota", listOf(4, 2, 0, 0)),
            // » Siswa no 21: LUH RIA MIRASIH (Bendahara 2). Minggu dibayar Juli=4, Agustus=5, September=4, Oktober=4.
            Triple("LUH RIA MIRASIH", "Bendahara 2", listOf(4, 5, 4, 4)),
            // » Siswa no 22: NI KADEK ADELIA CAHYA KENCANA PUTRI (Anggota). Minggu dibayar Juli=4, Agustus=0,
            // » September=0, Oktober=0.
            Triple("NI KADEK ADELIA CAHYA KENCANA PUTRI", "Anggota", listOf(4, 0, 0, 0)),
            // » Siswa no 23: NI KADEK LINA ANTIKA DEWI (Anggota). Minggu dibayar Juli=4, Agustus=5, September=0,
            // » Oktober=0.
            Triple("NI KADEK LINA ANTIKA DEWI", "Anggota", listOf(4, 5, 0, 0)),
            // » Siswa no 24: NI KOMANG KIRANA PARAMITA ARDANARI (Anggota). Minggu dibayar Juli=4, Agustus=5,
            // » September=4, Oktober=4.
            Triple("NI KOMANG KIRANA PARAMITA ARDANARI", "Anggota", listOf(4, 5, 4, 4)),
            // » Siswa no 25: NI KOMANG SEPTIARINI (Anggota). Minggu dibayar Juli=4, Agustus=5, September=1, Oktober=0.
            Triple("NI KOMANG SEPTIARINI", "Anggota", listOf(4, 5, 1, 0)),
            // » Siswa no 26: NI LUH PUTU KESYA ASTRI MELANI (Anggota). Minggu dibayar Juli=4, Agustus=5, September=4,
            // » Oktober=0.
            Triple("NI LUH PUTU KESYA ASTRI MELANI", "Anggota", listOf(4, 5, 4, 0)),
            // » Siswa no 27: NI PUTU CAHAYA LESTARI DEWI (Sekretaris 1). Minggu dibayar Juli=4, Agustus=5, September=0,
            // » Oktober=0.
            Triple("NI PUTU CAHAYA LESTARI DEWI", "Sekretaris 1", listOf(4, 5, 0, 0)),
            // » Siswa no 28: NI PUTU INTAN LESTARI DARMAYANTI (Sekretaris 2). Minggu dibayar Juli=4, Agustus=4,
            // » September=0, Oktober=0.
            Triple("NI PUTU INTAN LESTARI DARMAYANTI", "Sekretaris 2", listOf(4, 4, 0, 0)),
            // » Siswa no 29: OKTA PRADIPTA ATTALA DZAKI (Wakil Ketua). Minggu dibayar Juli=4, Agustus=5, September=4,
            // » Oktober=4.
            Triple("OKTA PRADIPTA ATTALA DZAKI", "Wakil Ketua", listOf(4, 5, 4, 4)),
            // » Siswa no 30: PUTU BAYU SATRIA WANGSA BUKIAN (Anggota). Minggu dibayar Juli=2, Agustus=0, September=0,
            // » Oktober=0.
            Triple("PUTU BAYU SATRIA WANGSA BUKIAN", "Anggota", listOf(2, 0, 0, 0)),
            // » Siswa no 31: PUTU NANDA LINDIA MAHARANI (Anggota). Minggu dibayar Juli=4, Agustus=4, September=0,
            // » Oktober=0.
            Triple("PUTU NANDA LINDIA MAHARANI", "Anggota", listOf(4, 4, 0, 0)),
            // » Siswa no 32: PUTU PUTRI CAHYANI (Anggota). Minggu dibayar Juli=4, Agustus=2, September=0, Oktober=0.
            Triple("PUTU PUTRI CAHYANI", "Anggota", listOf(4, 2, 0, 0)),
            // » Siswa no 33: RADITYA RONDI (Anggota). Minggu dibayar Juli=4, Agustus=0, September=0, Oktober=0.
            Triple("RADITYA RONDI", "Anggota", listOf(4, 0, 0, 0))
        )
        // CARI-DISPENSASI | Minggu yang dibebaskan per siswa per bulan. Tambah: "NAMA" to mapOf("Bulan Tahun" to jumlahMinggu).
        // » dispensasiAwal = peta: nama siswa -> bulan dan minggu dispensasi (dibebaskan).
        val dispensasiAwal = mapOf(
            // » BINTANG LEONITA punya dispensasi: Juli 4 minggu, Agustus 5 minggu, September 2 minggu (sedang BI/PKL).
            "BINTANG LEONITA CHRISTYA RENATA" to mapOf("Juli 2026" to 4, "Agustus 2026" to 5, "September 2026" to 2)
        )
        // » _students.value = ubah daftar bayarAwal jadi daftar Siswa. mapIndexed = sambil memberi nomor urut i
        // » (mulai 0).
        _students.value = bayarAwal.mapIndexed { i, (nama, peran, minggu) ->
            // » Membuat satu objek Siswa.
            Siswa(
                // » no = i + 1 (nomor urut mulai 1). nama dan peran dari daftar awal.
                no = i + 1, nama = nama, peran = peran,
                // » bayar = gabung nama bulan (BULAN_KAS_AWAL) dengan minggu, lalu jadi peta. zip = pasangkan per urutan.
                bayar = BULAN_KAS_AWAL.zip(minggu).toMap(),
                // » dispensasi = ambil dari dispensasiAwal berdasarkan nama. orEmpty() = kosong kalau siswa tidak punya
                // » dispensasi.
                dispensasi = dispensasiAwal[nama].orEmpty()
            // » Tutup pembuatan Siswa.
            )
        // » Tutup mapIndexed.
        }

        // » _paymentRecords.value = riwayat pembayaran awal. Urutan di daftar = dari yang terbaru ke yang terlama.
        _paymentRecords.value = listOf(
            // » Riwayat no 238: OKTA PRADIPTA ATTALA DZAKI membayar Rp20.000 untuk Oktober 2026, dicatat 26 Oktober
            // » 2026, metode Cash. Data awal buku kas.
            PaymentRecord(238, "26 Oktober 2026", "OKTA PRADIPTA ATTALA DZAKI", 20000, "Oktober 2026", "Cash"),
            // » Riwayat no 237: NI KOMANG KIRANA PARAMITA ARDANARI membayar Rp20.000 untuk Oktober 2026, dicatat 26
            // » Oktober 2026, metode Cash. Data awal buku kas.
            PaymentRecord(237, "26 Oktober 2026", "NI KOMANG KIRANA PARAMITA ARDANARI", 20000, "Oktober 2026", "Cash"),
            // » Riwayat no 236: LUH RIA MIRASIH membayar Rp20.000 untuk Oktober 2026, dicatat 26 Oktober 2026, metode
            // » Cash. Data awal buku kas.
            PaymentRecord(236, "26 Oktober 2026", "LUH RIA MIRASIH", 20000, "Oktober 2026", "Cash"),
            // » Riwayat no 235: I WAYAN PASEK KEVIN ARIADI membayar Rp20.000 untuk Oktober 2026, dicatat 26 Oktober
            // » 2026, metode Cash. Data awal buku kas.
            PaymentRecord(235, "26 Oktober 2026", "I WAYAN PASEK KEVIN ARIADI", 20000, "Oktober 2026", "Cash"),
            // » Riwayat no 234: I WAYAN BAGUS PUTRAWAN membayar Rp20.000 untuk Oktober 2026, dicatat 26 Oktober 2026,
            // » metode Cash. Data awal buku kas.
            PaymentRecord(234, "26 Oktober 2026", "I WAYAN BAGUS PUTRAWAN", 20000, "Oktober 2026", "Cash"),
            // » Riwayat no 233: GALISTAN RAMADHAN KURNIA TAUNAES membayar Rp20.000 untuk Oktober 2026, dicatat 26
            // » Oktober 2026, metode Cash. Data awal buku kas.
            PaymentRecord(233, "26 Oktober 2026", "GALISTAN RAMADHAN KURNIA TAUNAES", 20000, "Oktober 2026", "Cash"),
            // » Riwayat no 232: OKTA PRADIPTA ATTALA DZAKI membayar Rp20.000 untuk September 2026, dicatat 28 September
            // » 2026, metode Cash. Data awal buku kas.
            PaymentRecord(232, "28 September 2026", "OKTA PRADIPTA ATTALA DZAKI", 20000, "September 2026", "Cash"),
            // » Riwayat no 231: NI LUH PUTU KESYA ASTRI MELANI membayar Rp20.000 untuk September 2026, dicatat 28
            // » September 2026, metode Cash. Data awal buku kas.
            PaymentRecord(231, "28 September 2026", "NI LUH PUTU KESYA ASTRI MELANI", 20000, "September 2026", "Cash"),
            // » Riwayat no 230: NI KOMANG KIRANA PARAMITA ARDANARI membayar Rp20.000 untuk September 2026, dicatat 28
            // » September 2026, metode Cash. Data awal buku kas.
            PaymentRecord(230, "28 September 2026", "NI KOMANG KIRANA PARAMITA ARDANARI", 20000, "September 2026", "Cash"),
            // » Riwayat no 229: LUH RIA MIRASIH membayar Rp20.000 untuk September 2026, dicatat 28 September 2026,
            // » metode Cash. Data awal buku kas.
            PaymentRecord(229, "28 September 2026", "LUH RIA MIRASIH", 20000, "September 2026", "Cash"),
            // » Riwayat no 228: I WAYAN PASEK KEVIN ARIADI membayar Rp20.000 untuk September 2026, dicatat 28 September
            // » 2026, metode Cash. Data awal buku kas.
            PaymentRecord(228, "28 September 2026", "I WAYAN PASEK KEVIN ARIADI", 20000, "September 2026", "Cash"),
            // » Riwayat no 227: I WAYAN BAGUS PUTRAWAN membayar Rp20.000 untuk September 2026, dicatat 28 September
            // » 2026, metode Cash. Data awal buku kas.
            PaymentRecord(227, "28 September 2026", "I WAYAN BAGUS PUTRAWAN", 20000, "September 2026", "Cash"),
            // » Riwayat no 226: GALISTAN RAMADHAN KURNIA TAUNAES membayar Rp20.000 untuk September 2026, dicatat 28
            // » September 2026, metode Cash. Data awal buku kas.
            PaymentRecord(226, "28 September 2026", "GALISTAN RAMADHAN KURNIA TAUNAES", 20000, "September 2026", "Cash"),
            // » Riwayat no 225: BOYKE VILANO HAMONANGAN SIHITE membayar Rp20.000 untuk September 2026, dicatat 28
            // » September 2026, metode Cash. Data awal buku kas.
            PaymentRecord(225, "28 September 2026", "BOYKE VILANO HAMONANGAN SIHITE", 20000, "September 2026", "Cash"),
            // » Riwayat no 224: NI KOMANG SEPTIARINI membayar Rp5.000 untuk September 2026, dicatat 07 September 2026,
            // » metode Cash. Data awal buku kas.
            PaymentRecord(224, "07 September 2026", "NI KOMANG SEPTIARINI", 5000, "September 2026", "Cash"),
            // » Riwayat no 223: OKTA PRADIPTA ATTALA DZAKI membayar Rp25.000 untuk Agustus 2026, dicatat 31 Agustus
            // » 2026, metode Cash. Data awal buku kas.
            PaymentRecord(223, "31 Agustus 2026", "OKTA PRADIPTA ATTALA DZAKI", 25000, "Agustus 2026", "Cash"),
            // » Riwayat no 222: NI PUTU CAHAYA LESTARI DEWI membayar Rp25.000 untuk Agustus 2026, dicatat 31 Agustus
            // » 2026, metode Cash. Data awal buku kas.
            PaymentRecord(222, "31 Agustus 2026", "NI PUTU CAHAYA LESTARI DEWI", 25000, "Agustus 2026", "Cash"),
            // » Riwayat no 221: NI LUH PUTU KESYA ASTRI MELANI membayar Rp25.000 untuk Agustus 2026, dicatat 31 Agustus
            // » 2026, metode Cash. Data awal buku kas.
            PaymentRecord(221, "31 Agustus 2026", "NI LUH PUTU KESYA ASTRI MELANI", 25000, "Agustus 2026", "Cash"),
            // » Riwayat no 220: NI KOMANG SEPTIARINI membayar Rp25.000 untuk Agustus 2026, dicatat 31 Agustus 2026,
            // » metode Cash. Data awal buku kas.
            PaymentRecord(220, "31 Agustus 2026", "NI KOMANG SEPTIARINI", 25000, "Agustus 2026", "Cash"),
            // » Riwayat no 219: NI KOMANG KIRANA PARAMITA ARDANARI membayar Rp25.000 untuk Agustus 2026, dicatat 31
            // » Agustus 2026, metode Cash. Data awal buku kas.
            PaymentRecord(219, "31 Agustus 2026", "NI KOMANG KIRANA PARAMITA ARDANARI", 25000, "Agustus 2026", "Cash"),
            // » Riwayat no 218: NI KADEK LINA ANTIKA DEWI membayar Rp25.000 untuk Agustus 2026, dicatat 31 Agustus 2026,
            // » metode Cash. Data awal buku kas.
            PaymentRecord(218, "31 Agustus 2026", "NI KADEK LINA ANTIKA DEWI", 25000, "Agustus 2026", "Cash"),
            // » Riwayat no 217: LUH RIA MIRASIH membayar Rp25.000 untuk Agustus 2026, dicatat 31 Agustus 2026, metode
            // » Cash. Data awal buku kas.
            PaymentRecord(217, "31 Agustus 2026", "LUH RIA MIRASIH", 25000, "Agustus 2026", "Cash"),
            // » Riwayat no 216: I WAYAN PASEK KEVIN ARIADI membayar Rp25.000 untuk Agustus 2026, dicatat 31 Agustus
            // » 2026, metode Cash. Data awal buku kas.
            PaymentRecord(216, "31 Agustus 2026", "I WAYAN PASEK KEVIN ARIADI", 25000, "Agustus 2026", "Cash"),
            // » Riwayat no 215: I WAYAN BAGUS PUTRAWAN membayar Rp25.000 untuk Agustus 2026, dicatat 31 Agustus 2026,
            // » metode Cash. Data awal buku kas.
            PaymentRecord(215, "31 Agustus 2026", "I WAYAN BAGUS PUTRAWAN", 25000, "Agustus 2026", "Cash"),
            // » Riwayat no 214: GEDE AGUS WIRA DARMA PUTRA membayar Rp25.000 untuk Agustus 2026, dicatat 31 Agustus
            // » 2026, metode Cash. Data awal buku kas.
            PaymentRecord(214, "31 Agustus 2026", "GEDE AGUS WIRA DARMA PUTRA", 25000, "Agustus 2026", "Cash"),
            // » Riwayat no 213: GALISTAN RAMADHAN KURNIA TAUNAES membayar Rp25.000 untuk Agustus 2026, dicatat 31
            // » Agustus 2026, metode Cash. Data awal buku kas.
            PaymentRecord(213, "31 Agustus 2026", "GALISTAN RAMADHAN KURNIA TAUNAES", 25000, "Agustus 2026", "Cash"),
            // » Riwayat no 212: DEWA GEDE DALEM OKA ADNYANA SANDI membayar Rp25.000 untuk Agustus 2026, dicatat 31
            // » Agustus 2026, metode Cash. Data awal buku kas.
            PaymentRecord(212, "31 Agustus 2026", "DEWA GEDE DALEM OKA ADNYANA SANDI", 25000, "Agustus 2026", "Cash"),
            // » Riwayat no 211: CAROLINA TIMUTHY JANGGUR membayar Rp25.000 untuk Agustus 2026, dicatat 31 Agustus 2026,
            // » metode Cash. Data awal buku kas.
            PaymentRecord(211, "31 Agustus 2026", "CAROLINA TIMUTHY JANGGUR", 25000, "Agustus 2026", "Cash"),
            // » Riwayat no 210: BOYKE VILANO HAMONANGAN SIHITE membayar Rp25.000 untuk Agustus 2026, dicatat 31 Agustus
            // » 2026, metode Cash. Data awal buku kas.
            PaymentRecord(210, "31 Agustus 2026", "BOYKE VILANO HAMONANGAN SIHITE", 25000, "Agustus 2026", "Cash"),
            // » Riwayat no 209: PUTU NANDA LINDIA MAHARANI membayar Rp20.000 untuk Agustus 2026, dicatat 24 Agustus
            // » 2026, metode Cash. Data awal buku kas.
            PaymentRecord(209, "24 Agustus 2026", "PUTU NANDA LINDIA MAHARANI", 20000, "Agustus 2026", "Cash"),
            // » Riwayat no 208: NI PUTU INTAN LESTARI DARMAYANTI membayar Rp20.000 untuk Agustus 2026, dicatat 24
            // » Agustus 2026, metode Cash. Data awal buku kas.
            PaymentRecord(208, "24 Agustus 2026", "NI PUTU INTAN LESTARI DARMAYANTI", 20000, "Agustus 2026", "Cash"),
            // » Riwayat no 207: KADEK YUNI CALLISTA PUTRI DEWI membayar Rp20.000 untuk Agustus 2026, dicatat 24 Agustus
            // » 2026, metode Cash. Data awal buku kas.
            PaymentRecord(207, "24 Agustus 2026", "KADEK YUNI CALLISTA PUTRI DEWI", 20000, "Agustus 2026", "Cash"),
            // » Riwayat no 206: I PUTU DIKA LAKSMANA PUTRA membayar Rp20.000 untuk Agustus 2026, dicatat 24 Agustus
            // » 2026, metode Cash. Data awal buku kas.
            PaymentRecord(206, "24 Agustus 2026", "I PUTU DIKA LAKSMANA PUTRA", 20000, "Agustus 2026", "Cash"),
            // » Riwayat no 205: PUTU PUTRI CAHYANI membayar Rp10.000 untuk Agustus 2026, dicatat 10 Agustus 2026, metode
            // » Cash. Data awal buku kas.
            PaymentRecord(205, "10 Agustus 2026", "PUTU PUTRI CAHYANI", 10000, "Agustus 2026", "Cash"),
            // » Riwayat no 204: KOMANG DIAH PUTRI PRATIWI membayar Rp10.000 untuk Agustus 2026, dicatat 10 Agustus 2026,
            // » metode Cash. Data awal buku kas.
            PaymentRecord(204, "10 Agustus 2026", "KOMANG DIAH PUTRI PRATIWI", 10000, "Agustus 2026", "Cash"),
            // » Riwayat no 203: I PUTU SUYOGA MAHENDRA membayar Rp10.000 untuk Agustus 2026, dicatat 10 Agustus 2026,
            // » metode Cash. Data awal buku kas.
            PaymentRecord(203, "10 Agustus 2026", "I PUTU SUYOGA MAHENDRA", 10000, "Agustus 2026", "Cash"),
            // » Riwayat no 202: I NYOMAN GEDE ARTA WIGUNA membayar Rp5.000 untuk Agustus 2026, dicatat 03 Agustus 2026,
            // » metode Cash. Data awal buku kas.
            PaymentRecord(202, "03 Agustus 2026", "I NYOMAN GEDE ARTA WIGUNA", 5000, "Agustus 2026", "Cash"),
            // » Riwayat no 201: I KOMANG RADITYA PUTRA membayar Rp5.000 untuk Agustus 2026, dicatat 03 Agustus 2026,
            // » metode Cash. Data awal buku kas.
            PaymentRecord(201, "03 Agustus 2026", "I KOMANG RADITYA PUTRA", 5000, "Agustus 2026", "Cash"),
            // » Riwayat no 200: I GEDE DARMA SUPTIAWAN membayar Rp5.000 untuk Agustus 2026, dicatat 03 Agustus 2026,
            // » metode Cash. Data awal buku kas.
            PaymentRecord(200, "03 Agustus 2026", "I GEDE DARMA SUPTIAWAN", 5000, "Agustus 2026", "Cash"),
            // » Riwayat no 199: RADITYA RONDI membayar Rp20.000 untuk Juli 2026, dicatat 27 Juli 2026, metode Cash. Data
            // » awal buku kas.
            PaymentRecord(199, "27 Juli 2026", "RADITYA RONDI", 20000, "Juli 2026", "Cash"),
            // » Riwayat no 198: PUTU PUTRI CAHYANI membayar Rp20.000 untuk Juli 2026, dicatat 27 Juli 2026, metode Cash.
            // » Data awal buku kas.
            PaymentRecord(198, "27 Juli 2026", "PUTU PUTRI CAHYANI", 20000, "Juli 2026", "Cash"),
            // » Riwayat no 197: PUTU NANDA LINDIA MAHARANI membayar Rp20.000 untuk Juli 2026, dicatat 27 Juli 2026,
            // » metode Cash. Data awal buku kas.
            PaymentRecord(197, "27 Juli 2026", "PUTU NANDA LINDIA MAHARANI", 20000, "Juli 2026", "Cash"),
            // » Riwayat no 196: OKTA PRADIPTA ATTALA DZAKI membayar Rp20.000 untuk Juli 2026, dicatat 27 Juli 2026,
            // » metode Cash. Data awal buku kas.
            PaymentRecord(196, "27 Juli 2026", "OKTA PRADIPTA ATTALA DZAKI", 20000, "Juli 2026", "Cash"),
            // » Riwayat no 195: NI PUTU INTAN LESTARI DARMAYANTI membayar Rp20.000 untuk Juli 2026, dicatat 27 Juli
            // » 2026, metode Cash. Data awal buku kas.
            PaymentRecord(195, "27 Juli 2026", "NI PUTU INTAN LESTARI DARMAYANTI", 20000, "Juli 2026", "Cash"),
            // » Riwayat no 194: NI PUTU CAHAYA LESTARI DEWI membayar Rp20.000 untuk Juli 2026, dicatat 27 Juli 2026,
            // » metode Cash. Data awal buku kas.
            PaymentRecord(194, "27 Juli 2026", "NI PUTU CAHAYA LESTARI DEWI", 20000, "Juli 2026", "Cash"),
            // » Riwayat no 193: NI LUH PUTU KESYA ASTRI MELANI membayar Rp20.000 untuk Juli 2026, dicatat 27 Juli 2026,
            // » metode Cash. Data awal buku kas.
            PaymentRecord(193, "27 Juli 2026", "NI LUH PUTU KESYA ASTRI MELANI", 20000, "Juli 2026", "Cash"),
            // » Riwayat no 192: NI KOMANG SEPTIARINI membayar Rp20.000 untuk Juli 2026, dicatat 27 Juli 2026, metode
            // » Cash. Data awal buku kas.
            PaymentRecord(192, "27 Juli 2026", "NI KOMANG SEPTIARINI", 20000, "Juli 2026", "Cash"),
            // » Riwayat no 191: NI KOMANG KIRANA PARAMITA ARDANARI membayar Rp20.000 untuk Juli 2026, dicatat 27 Juli
            // » 2026, metode Cash. Data awal buku kas.
            PaymentRecord(191, "27 Juli 2026", "NI KOMANG KIRANA PARAMITA ARDANARI", 20000, "Juli 2026", "Cash"),
            // » Riwayat no 190: NI KADEK LINA ANTIKA DEWI membayar Rp20.000 untuk Juli 2026, dicatat 27 Juli 2026,
            // » metode Cash. Data awal buku kas.
            PaymentRecord(190, "27 Juli 2026", "NI KADEK LINA ANTIKA DEWI", 20000, "Juli 2026", "Cash"),
            // » Riwayat no 189: NI KADEK ADELIA CAHYA KENCANA PUTRI membayar Rp20.000 untuk Juli 2026, dicatat 27 Juli
            // » 2026, metode Cash. Data awal buku kas.
            PaymentRecord(189, "27 Juli 2026", "NI KADEK ADELIA CAHYA KENCANA PUTRI", 20000, "Juli 2026", "Cash"),
            // » Riwayat no 188: LUH RIA MIRASIH membayar Rp20.000 untuk Juli 2026, dicatat 27 Juli 2026, metode Cash.
            // » Data awal buku kas.
            PaymentRecord(188, "27 Juli 2026", "LUH RIA MIRASIH", 20000, "Juli 2026", "Cash"),
            // » Riwayat no 187: KOMANG DIAH PUTRI PRATIWI membayar Rp20.000 untuk Juli 2026, dicatat 27 Juli 2026,
            // » metode Cash. Data awal buku kas.
            PaymentRecord(187, "27 Juli 2026", "KOMANG DIAH PUTRI PRATIWI", 20000, "Juli 2026", "Cash"),
            // » Riwayat no 186: KADEK YUNI CALLISTA PUTRI DEWI membayar Rp20.000 untuk Juli 2026, dicatat 27 Juli 2026,
            // » metode Cash. Data awal buku kas.
            PaymentRecord(186, "27 Juli 2026", "KADEK YUNI CALLISTA PUTRI DEWI", 20000, "Juli 2026", "Cash"),
            // » Riwayat no 185: I WAYAN PASEK KEVIN ARIADI membayar Rp20.000 untuk Juli 2026, dicatat 27 Juli 2026,
            // » metode Cash. Data awal buku kas.
            PaymentRecord(185, "27 Juli 2026", "I WAYAN PASEK KEVIN ARIADI", 20000, "Juli 2026", "Cash"),
            // » Riwayat no 184: I WAYAN BAGUS PUTRAWAN membayar Rp20.000 untuk Juli 2026, dicatat 27 Juli 2026, metode
            // » Cash. Data awal buku kas.
            PaymentRecord(184, "27 Juli 2026", "I WAYAN BAGUS PUTRAWAN", 20000, "Juli 2026", "Cash"),
            // » Riwayat no 183: I PUTU SUYOGA MAHENDRA membayar Rp20.000 untuk Juli 2026, dicatat 27 Juli 2026, metode
            // » Cash. Data awal buku kas.
            PaymentRecord(183, "27 Juli 2026", "I PUTU SUYOGA MAHENDRA", 20000, "Juli 2026", "Cash"),
            // » Riwayat no 182: I PUTU PANDE ANDIKA membayar Rp20.000 untuk Juli 2026, dicatat 27 Juli 2026, metode
            // » Cash. Data awal buku kas.
            PaymentRecord(182, "27 Juli 2026", "I PUTU PANDE ANDIKA", 20000, "Juli 2026", "Cash"),
            // » Riwayat no 181: I PUTU DIKA LAKSMANA PUTRA membayar Rp20.000 untuk Juli 2026, dicatat 27 Juli 2026,
            // » metode Cash. Data awal buku kas.
            PaymentRecord(181, "27 Juli 2026", "I PUTU DIKA LAKSMANA PUTRA", 20000, "Juli 2026", "Cash"),
            // » Riwayat no 180: I NYOMAN GEDE ARTA WIGUNA membayar Rp20.000 untuk Juli 2026, dicatat 27 Juli 2026,
            // » metode Cash. Data awal buku kas.
            PaymentRecord(180, "27 Juli 2026", "I NYOMAN GEDE ARTA WIGUNA", 20000, "Juli 2026", "Cash"),
            // » Riwayat no 179: I KOMANG RADITYA PUTRA membayar Rp20.000 untuk Juli 2026, dicatat 27 Juli 2026, metode
            // » Cash. Data awal buku kas.
            PaymentRecord(179, "27 Juli 2026", "I KOMANG RADITYA PUTRA", 20000, "Juli 2026", "Cash"),
            // » Riwayat no 178: I GEDE DARMA SUPTIAWAN membayar Rp20.000 untuk Juli 2026, dicatat 27 Juli 2026, metode
            // » Cash. Data awal buku kas.
            PaymentRecord(178, "27 Juli 2026", "I GEDE DARMA SUPTIAWAN", 20000, "Juli 2026", "Cash"),
            // » Riwayat no 177: I GEDE ABI WIRYA DINATA membayar Rp20.000 untuk Juli 2026, dicatat 27 Juli 2026, metode
            // » Cash. Data awal buku kas.
            PaymentRecord(177, "27 Juli 2026", "I GEDE ABI WIRYA DINATA", 20000, "Juli 2026", "Cash"),
            // » Riwayat no 176: GEDE AGUS WIRA DARMA PUTRA membayar Rp20.000 untuk Juli 2026, dicatat 27 Juli 2026,
            // » metode Cash. Data awal buku kas.
            PaymentRecord(176, "27 Juli 2026", "GEDE AGUS WIRA DARMA PUTRA", 20000, "Juli 2026", "Cash"),
            // » Riwayat no 175: GALISTAN RAMADHAN KURNIA TAUNAES membayar Rp20.000 untuk Juli 2026, dicatat 27 Juli
            // » 2026, metode Cash. Data awal buku kas.
            PaymentRecord(175, "27 Juli 2026", "GALISTAN RAMADHAN KURNIA TAUNAES", 20000, "Juli 2026", "Cash"),
            // » Riwayat no 174: DEWA GEDE DALEM OKA ADNYANA SANDI membayar Rp20.000 untuk Juli 2026, dicatat 27 Juli
            // » 2026, metode Cash. Data awal buku kas.
            PaymentRecord(174, "27 Juli 2026", "DEWA GEDE DALEM OKA ADNYANA SANDI", 20000, "Juli 2026", "Cash"),
            // » Riwayat no 173: CAROLINA TIMUTHY JANGGUR membayar Rp20.000 untuk Juli 2026, dicatat 27 Juli 2026, metode
            // » Cash. Data awal buku kas.
            PaymentRecord(173, "27 Juli 2026", "CAROLINA TIMUTHY JANGGUR", 20000, "Juli 2026", "Cash"),
            // » Riwayat no 172: BOYKE VILANO HAMONANGAN SIHITE membayar Rp20.000 untuk Juli 2026, dicatat 27 Juli 2026,
            // » metode Cash. Data awal buku kas.
            PaymentRecord(172, "27 Juli 2026", "BOYKE VILANO HAMONANGAN SIHITE", 20000, "Juli 2026", "Cash"),
            // » Riwayat no 171 (donasi, bukan kas): DONASI - NI KOMANG SEPTIARINI, Rp20.000, tanggal 21 Juli 2026, untuk
            // » Juli 2026. Data awal buku kas.
            PaymentRecord(171, "21 Juli 2026", "DONASI - NI KOMANG SEPTIARINI", 20000, "Juli 2026", "Donasi"),
            // » Riwayat no 170: KADEK YUDA PRASETYA membayar Rp15.000 untuk Juli 2026, dicatat 20 Juli 2026, metode
            // » Cash. Data awal buku kas.
            PaymentRecord(170, "20 Juli 2026", "KADEK YUDA PRASETYA", 15000, "Juli 2026", "Cash"),
            // » Riwayat no 169: PUTU BAYU SATRIA WANGSA BUKIAN membayar Rp10.000 untuk Juli 2026, dicatat 13 Juli 2026,
            // » metode Cash. Data awal buku kas.
            PaymentRecord(169, "13 Juli 2026", "PUTU BAYU SATRIA WANGSA BUKIAN", 10000, "Juli 2026", "Cash"),
            // » Riwayat no 168: I PUTU DITYA ARTHA WIJAYA membayar Rp10.000 untuk Juli 2026, dicatat 13 Juli 2026,
            // » metode Cash. Data awal buku kas.
            PaymentRecord(168, "13 Juli 2026", "I PUTU DITYA ARTHA WIJAYA", 10000, "Juli 2026", "Cash"),
            // » Riwayat no 167: I KOMANG RISKI SETIAWAN membayar Rp10.000 untuk Juli 2026, dicatat 13 Juli 2026, metode
            // » Cash. Data awal buku kas.
            PaymentRecord(167, "13 Juli 2026", "I KOMANG RISKI SETIAWAN", 10000, "Juli 2026", "Cash"),
            // » Riwayat no 166: RADITYA RONDI membayar Rp20.000 untuk Mei 2026, dicatat 25 Mei 2026, metode Cash. Data
            // » awal buku kas.
            PaymentRecord(166, "25 Mei 2026", "RADITYA RONDI", 20000, "Mei 2026", "Cash"),
            // » Riwayat no 165: PUTU PUTRI CAHYANI membayar Rp20.000 untuk Mei 2026, dicatat 25 Mei 2026, metode Cash.
            // » Data awal buku kas.
            PaymentRecord(165, "25 Mei 2026", "PUTU PUTRI CAHYANI", 20000, "Mei 2026", "Cash"),
            // » Riwayat no 164: PUTU NANDA LINDIA MAHARANI membayar Rp20.000 untuk Mei 2026, dicatat 25 Mei 2026, metode
            // » Cash. Data awal buku kas.
            PaymentRecord(164, "25 Mei 2026", "PUTU NANDA LINDIA MAHARANI", 20000, "Mei 2026", "Cash"),
            // » Riwayat no 163: PUTU BAYU SATRIA WANGSA BUKIAN membayar Rp20.000 untuk Mei 2026, dicatat 25 Mei 2026,
            // » metode Cash. Data awal buku kas.
            PaymentRecord(163, "25 Mei 2026", "PUTU BAYU SATRIA WANGSA BUKIAN", 20000, "Mei 2026", "Cash"),
            // » Riwayat no 162: OKTA PRADIPTA ATTALA DZAKI membayar Rp20.000 untuk Mei 2026, dicatat 25 Mei 2026, metode
            // » Cash. Data awal buku kas.
            PaymentRecord(162, "25 Mei 2026", "OKTA PRADIPTA ATTALA DZAKI", 20000, "Mei 2026", "Cash"),
            // » Riwayat no 161: NI PUTU INTAN LESTARI DARMAYANTI membayar Rp20.000 untuk Mei 2026, dicatat 25 Mei 2026,
            // » metode Cash. Data awal buku kas.
            PaymentRecord(161, "25 Mei 2026", "NI PUTU INTAN LESTARI DARMAYANTI", 20000, "Mei 2026", "Cash"),
            // » Riwayat no 160: NI PUTU CAHAYA LESTARI DEWI membayar Rp20.000 untuk Mei 2026, dicatat 25 Mei 2026,
            // » metode Cash. Data awal buku kas.
            PaymentRecord(160, "25 Mei 2026", "NI PUTU CAHAYA LESTARI DEWI", 20000, "Mei 2026", "Cash"),
            // » Riwayat no 159: NI LUH PUTU KESYA ASTRI MELANI membayar Rp20.000 untuk Mei 2026, dicatat 25 Mei 2026,
            // » metode Cash. Data awal buku kas.
            PaymentRecord(159, "25 Mei 2026", "NI LUH PUTU KESYA ASTRI MELANI", 20000, "Mei 2026", "Cash"),
            // » Riwayat no 158: NI KOMANG SEPTIARINI membayar Rp20.000 untuk Mei 2026, dicatat 25 Mei 2026, metode Cash.
            // » Data awal buku kas.
            PaymentRecord(158, "25 Mei 2026", "NI KOMANG SEPTIARINI", 20000, "Mei 2026", "Cash"),
            // » Riwayat no 157: NI KOMANG KIRANA PARAMITA ARDANARI membayar Rp20.000 untuk Mei 2026, dicatat 25 Mei
            // » 2026, metode Cash. Data awal buku kas.
            PaymentRecord(157, "25 Mei 2026", "NI KOMANG KIRANA PARAMITA ARDANARI", 20000, "Mei 2026", "Cash"),
            // » Riwayat no 156: NI KADEK LINA ANTIKA DEWI membayar Rp20.000 untuk Mei 2026, dicatat 25 Mei 2026, metode
            // » Cash. Data awal buku kas.
            PaymentRecord(156, "25 Mei 2026", "NI KADEK LINA ANTIKA DEWI", 20000, "Mei 2026", "Cash"),
            // » Riwayat no 155: NI KADEK ADELIA CAHYA KENCANA PUTRI membayar Rp20.000 untuk Mei 2026, dicatat 25 Mei
            // » 2026, metode Cash. Data awal buku kas.
            PaymentRecord(155, "25 Mei 2026", "NI KADEK ADELIA CAHYA KENCANA PUTRI", 20000, "Mei 2026", "Cash"),
            // » Riwayat no 154: LUH RIA MIRASIH membayar Rp20.000 untuk Mei 2026, dicatat 25 Mei 2026, metode Cash. Data
            // » awal buku kas.
            PaymentRecord(154, "25 Mei 2026", "LUH RIA MIRASIH", 20000, "Mei 2026", "Cash"),
            // » Riwayat no 153: KOMANG DIAH PUTRI PRATIWI membayar Rp20.000 untuk Mei 2026, dicatat 25 Mei 2026, metode
            // » Cash. Data awal buku kas.
            PaymentRecord(153, "25 Mei 2026", "KOMANG DIAH PUTRI PRATIWI", 20000, "Mei 2026", "Cash"),
            // » Riwayat no 152: KADEK YUNI CALLISTA PUTRI DEWI membayar Rp20.000 untuk Mei 2026, dicatat 25 Mei 2026,
            // » metode Cash. Data awal buku kas.
            PaymentRecord(152, "25 Mei 2026", "KADEK YUNI CALLISTA PUTRI DEWI", 20000, "Mei 2026", "Cash"),
            // » Riwayat no 151: KADEK YUDA PRASETYA membayar Rp20.000 untuk Mei 2026, dicatat 25 Mei 2026, metode Cash.
            // » Data awal buku kas.
            PaymentRecord(151, "25 Mei 2026", "KADEK YUDA PRASETYA", 20000, "Mei 2026", "Cash"),
            // » Riwayat no 150: I WAYAN PASEK KEVIN ARIADI membayar Rp20.000 untuk Mei 2026, dicatat 25 Mei 2026, metode
            // » Cash. Data awal buku kas.
            PaymentRecord(150, "25 Mei 2026", "I WAYAN PASEK KEVIN ARIADI", 20000, "Mei 2026", "Cash"),
            // » Riwayat no 149: I WAYAN BAGUS PUTRAWAN membayar Rp20.000 untuk Mei 2026, dicatat 25 Mei 2026, metode
            // » Cash. Data awal buku kas.
            PaymentRecord(149, "25 Mei 2026", "I WAYAN BAGUS PUTRAWAN", 20000, "Mei 2026", "Cash"),
            // » Riwayat no 148: I PUTU SUYOGA MAHENDRA membayar Rp20.000 untuk Mei 2026, dicatat 25 Mei 2026, metode
            // » Cash. Data awal buku kas.
            PaymentRecord(148, "25 Mei 2026", "I PUTU SUYOGA MAHENDRA", 20000, "Mei 2026", "Cash"),
            // » Riwayat no 147: I PUTU PANDE ANDIKA membayar Rp20.000 untuk Mei 2026, dicatat 25 Mei 2026, metode Cash.
            // » Data awal buku kas.
            PaymentRecord(147, "25 Mei 2026", "I PUTU PANDE ANDIKA", 20000, "Mei 2026", "Cash"),
            // » Riwayat no 146: I PUTU DITYA ARTHA WIJAYA membayar Rp20.000 untuk Mei 2026, dicatat 25 Mei 2026, metode
            // » Cash. Data awal buku kas.
            PaymentRecord(146, "25 Mei 2026", "I PUTU DITYA ARTHA WIJAYA", 20000, "Mei 2026", "Cash"),
            // » Riwayat no 145: I PUTU DIKA LAKSMANA PUTRA membayar Rp20.000 untuk Mei 2026, dicatat 25 Mei 2026, metode
            // » Cash. Data awal buku kas.
            PaymentRecord(145, "25 Mei 2026", "I PUTU DIKA LAKSMANA PUTRA", 20000, "Mei 2026", "Cash"),
            // » Riwayat no 144: I NYOMAN GEDE ARTA WIGUNA membayar Rp20.000 untuk Mei 2026, dicatat 25 Mei 2026, metode
            // » Cash. Data awal buku kas.
            PaymentRecord(144, "25 Mei 2026", "I NYOMAN GEDE ARTA WIGUNA", 20000, "Mei 2026", "Cash"),
            // » Riwayat no 143: I KOMANG RISKI SETIAWAN membayar Rp20.000 untuk Mei 2026, dicatat 25 Mei 2026, metode
            // » Cash. Data awal buku kas.
            PaymentRecord(143, "25 Mei 2026", "I KOMANG RISKI SETIAWAN", 20000, "Mei 2026", "Cash"),
            // » Riwayat no 142: I KOMANG RADITYA PUTRA membayar Rp20.000 untuk Mei 2026, dicatat 25 Mei 2026, metode
            // » Cash. Data awal buku kas.
            PaymentRecord(142, "25 Mei 2026", "I KOMANG RADITYA PUTRA", 20000, "Mei 2026", "Cash"),
            // » Riwayat no 141: I GEDE DARMA SUPTIAWAN membayar Rp20.000 untuk Mei 2026, dicatat 25 Mei 2026, metode
            // » Cash. Data awal buku kas.
            PaymentRecord(141, "25 Mei 2026", "I GEDE DARMA SUPTIAWAN", 20000, "Mei 2026", "Cash"),
            // » Riwayat no 140: I GEDE ABI WIRYA DINATA membayar Rp20.000 untuk Mei 2026, dicatat 25 Mei 2026, metode
            // » Cash. Data awal buku kas.
            PaymentRecord(140, "25 Mei 2026", "I GEDE ABI WIRYA DINATA", 20000, "Mei 2026", "Cash"),
            // » Riwayat no 139: GEDE AGUS WIRA DARMA PUTRA membayar Rp20.000 untuk Mei 2026, dicatat 25 Mei 2026, metode
            // » Cash. Data awal buku kas.
            PaymentRecord(139, "25 Mei 2026", "GEDE AGUS WIRA DARMA PUTRA", 20000, "Mei 2026", "Cash"),
            // » Riwayat no 138: GALISTAN RAMADHAN KURNIA TAUNAES membayar Rp20.000 untuk Mei 2026, dicatat 25 Mei 2026,
            // » metode Cash. Data awal buku kas.
            PaymentRecord(138, "25 Mei 2026", "GALISTAN RAMADHAN KURNIA TAUNAES", 20000, "Mei 2026", "Cash"),
            // » Riwayat no 137: DEWA GEDE DALEM OKA ADNYANA SANDI membayar Rp20.000 untuk Mei 2026, dicatat 25 Mei 2026,
            // » metode Cash. Data awal buku kas.
            PaymentRecord(137, "25 Mei 2026", "DEWA GEDE DALEM OKA ADNYANA SANDI", 20000, "Mei 2026", "Cash"),
            // » Riwayat no 136: CAROLINA TIMUTHY JANGGUR membayar Rp20.000 untuk Mei 2026, dicatat 25 Mei 2026, metode
            // » Cash. Data awal buku kas.
            PaymentRecord(136, "25 Mei 2026", "CAROLINA TIMUTHY JANGGUR", 20000, "Mei 2026", "Cash"),
            // » Riwayat no 135: BOYKE VILANO HAMONANGAN SIHITE membayar Rp20.000 untuk Mei 2026, dicatat 25 Mei 2026,
            // » metode Cash. Data awal buku kas.
            PaymentRecord(135, "25 Mei 2026", "BOYKE VILANO HAMONANGAN SIHITE", 20000, "Mei 2026", "Cash"),
            // » Riwayat no 134: BINTANG LEONITA CHRISTYA RENATA membayar Rp20.000 untuk Mei 2026, dicatat 25 Mei 2026,
            // » metode Cash. Data awal buku kas.
            PaymentRecord(134, "25 Mei 2026", "BINTANG LEONITA CHRISTYA RENATA", 20000, "Mei 2026", "Cash"),
            // » Riwayat no 133: RADITYA RONDI membayar Rp20.000 untuk April 2026, dicatat 27 April 2026, metode Cash.
            // » Data awal buku kas.
            PaymentRecord(133, "27 April 2026", "RADITYA RONDI", 20000, "April 2026", "Cash"),
            // » Riwayat no 132: PUTU PUTRI CAHYANI membayar Rp20.000 untuk April 2026, dicatat 27 April 2026, metode
            // » Cash. Data awal buku kas.
            PaymentRecord(132, "27 April 2026", "PUTU PUTRI CAHYANI", 20000, "April 2026", "Cash"),
            // » Riwayat no 131: PUTU NANDA LINDIA MAHARANI membayar Rp20.000 untuk April 2026, dicatat 27 April 2026,
            // » metode Cash. Data awal buku kas.
            PaymentRecord(131, "27 April 2026", "PUTU NANDA LINDIA MAHARANI", 20000, "April 2026", "Cash"),
            // » Riwayat no 130: PUTU BAYU SATRIA WANGSA BUKIAN membayar Rp20.000 untuk April 2026, dicatat 27 April
            // » 2026, metode Cash. Data awal buku kas.
            PaymentRecord(130, "27 April 2026", "PUTU BAYU SATRIA WANGSA BUKIAN", 20000, "April 2026", "Cash"),
            // » Riwayat no 129: OKTA PRADIPTA ATTALA DZAKI membayar Rp20.000 untuk April 2026, dicatat 27 April 2026,
            // » metode Cash. Data awal buku kas.
            PaymentRecord(129, "27 April 2026", "OKTA PRADIPTA ATTALA DZAKI", 20000, "April 2026", "Cash"),
            // » Riwayat no 128: NI PUTU INTAN LESTARI DARMAYANTI membayar Rp20.000 untuk April 2026, dicatat 27 April
            // » 2026, metode Cash. Data awal buku kas.
            PaymentRecord(128, "27 April 2026", "NI PUTU INTAN LESTARI DARMAYANTI", 20000, "April 2026", "Cash"),
            // » Riwayat no 127: NI PUTU CAHAYA LESTARI DEWI membayar Rp20.000 untuk April 2026, dicatat 27 April 2026,
            // » metode Cash. Data awal buku kas.
            PaymentRecord(127, "27 April 2026", "NI PUTU CAHAYA LESTARI DEWI", 20000, "April 2026", "Cash"),
            // » Riwayat no 126: NI LUH PUTU KESYA ASTRI MELANI membayar Rp20.000 untuk April 2026, dicatat 27 April
            // » 2026, metode Cash. Data awal buku kas.
            PaymentRecord(126, "27 April 2026", "NI LUH PUTU KESYA ASTRI MELANI", 20000, "April 2026", "Cash"),
            // » Riwayat no 125: NI KOMANG SEPTIARINI membayar Rp20.000 untuk April 2026, dicatat 27 April 2026, metode
            // » Cash. Data awal buku kas.
            PaymentRecord(125, "27 April 2026", "NI KOMANG SEPTIARINI", 20000, "April 2026", "Cash"),
            // » Riwayat no 124: NI KOMANG KIRANA PARAMITA ARDANARI membayar Rp20.000 untuk April 2026, dicatat 27 April
            // » 2026, metode Cash. Data awal buku kas.
            PaymentRecord(124, "27 April 2026", "NI KOMANG KIRANA PARAMITA ARDANARI", 20000, "April 2026", "Cash"),
            // » Riwayat no 123: NI KADEK LINA ANTIKA DEWI membayar Rp20.000 untuk April 2026, dicatat 27 April 2026,
            // » metode Cash. Data awal buku kas.
            PaymentRecord(123, "27 April 2026", "NI KADEK LINA ANTIKA DEWI", 20000, "April 2026", "Cash"),
            // » Riwayat no 122: NI KADEK ADELIA CAHYA KENCANA PUTRI membayar Rp20.000 untuk April 2026, dicatat 27 April
            // » 2026, metode Cash. Data awal buku kas.
            PaymentRecord(122, "27 April 2026", "NI KADEK ADELIA CAHYA KENCANA PUTRI", 20000, "April 2026", "Cash"),
            // » Riwayat no 121: LUH RIA MIRASIH membayar Rp20.000 untuk April 2026, dicatat 27 April 2026, metode Cash.
            // » Data awal buku kas.
            PaymentRecord(121, "27 April 2026", "LUH RIA MIRASIH", 20000, "April 2026", "Cash"),
            // » Riwayat no 120: KOMANG DIAH PUTRI PRATIWI membayar Rp20.000 untuk April 2026, dicatat 27 April 2026,
            // » metode Cash. Data awal buku kas.
            PaymentRecord(120, "27 April 2026", "KOMANG DIAH PUTRI PRATIWI", 20000, "April 2026", "Cash"),
            // » Riwayat no 119: KADEK YUNI CALLISTA PUTRI DEWI membayar Rp20.000 untuk April 2026, dicatat 27 April
            // » 2026, metode Cash. Data awal buku kas.
            PaymentRecord(119, "27 April 2026", "KADEK YUNI CALLISTA PUTRI DEWI", 20000, "April 2026", "Cash"),
            // » Riwayat no 118: KADEK YUDA PRASETYA membayar Rp20.000 untuk April 2026, dicatat 27 April 2026, metode
            // » Cash. Data awal buku kas.
            PaymentRecord(118, "27 April 2026", "KADEK YUDA PRASETYA", 20000, "April 2026", "Cash"),
            // » Riwayat no 117: I WAYAN PASEK KEVIN ARIADI membayar Rp20.000 untuk April 2026, dicatat 27 April 2026,
            // » metode Cash. Data awal buku kas.
            PaymentRecord(117, "27 April 2026", "I WAYAN PASEK KEVIN ARIADI", 20000, "April 2026", "Cash"),
            // » Riwayat no 116: I WAYAN BAGUS PUTRAWAN membayar Rp20.000 untuk April 2026, dicatat 27 April 2026, metode
            // » Cash. Data awal buku kas.
            PaymentRecord(116, "27 April 2026", "I WAYAN BAGUS PUTRAWAN", 20000, "April 2026", "Cash"),
            // » Riwayat no 115: I PUTU SUYOGA MAHENDRA membayar Rp20.000 untuk April 2026, dicatat 27 April 2026, metode
            // » Cash. Data awal buku kas.
            PaymentRecord(115, "27 April 2026", "I PUTU SUYOGA MAHENDRA", 20000, "April 2026", "Cash"),
            // » Riwayat no 114: I PUTU PANDE ANDIKA membayar Rp20.000 untuk April 2026, dicatat 27 April 2026, metode
            // » Cash. Data awal buku kas.
            PaymentRecord(114, "27 April 2026", "I PUTU PANDE ANDIKA", 20000, "April 2026", "Cash"),
            // » Riwayat no 113: I PUTU DITYA ARTHA WIJAYA membayar Rp20.000 untuk April 2026, dicatat 27 April 2026,
            // » metode Cash. Data awal buku kas.
            PaymentRecord(113, "27 April 2026", "I PUTU DITYA ARTHA WIJAYA", 20000, "April 2026", "Cash"),
            // » Riwayat no 112: I PUTU DIKA LAKSMANA PUTRA membayar Rp20.000 untuk April 2026, dicatat 27 April 2026,
            // » metode Cash. Data awal buku kas.
            PaymentRecord(112, "27 April 2026", "I PUTU DIKA LAKSMANA PUTRA", 20000, "April 2026", "Cash"),
            // » Riwayat no 111: I NYOMAN GEDE ARTA WIGUNA membayar Rp20.000 untuk April 2026, dicatat 27 April 2026,
            // » metode Cash. Data awal buku kas.
            PaymentRecord(111, "27 April 2026", "I NYOMAN GEDE ARTA WIGUNA", 20000, "April 2026", "Cash"),
            // » Riwayat no 110: I KOMANG RISKI SETIAWAN membayar Rp20.000 untuk April 2026, dicatat 27 April 2026,
            // » metode Cash. Data awal buku kas.
            PaymentRecord(110, "27 April 2026", "I KOMANG RISKI SETIAWAN", 20000, "April 2026", "Cash"),
            // » Riwayat no 109: I KOMANG RADITYA PUTRA membayar Rp20.000 untuk April 2026, dicatat 27 April 2026, metode
            // » Cash. Data awal buku kas.
            PaymentRecord(109, "27 April 2026", "I KOMANG RADITYA PUTRA", 20000, "April 2026", "Cash"),
            // » Riwayat no 108: I GEDE DARMA SUPTIAWAN membayar Rp20.000 untuk April 2026, dicatat 27 April 2026, metode
            // » Cash. Data awal buku kas.
            PaymentRecord(108, "27 April 2026", "I GEDE DARMA SUPTIAWAN", 20000, "April 2026", "Cash"),
            // » Riwayat no 107: I GEDE ABI WIRYA DINATA membayar Rp20.000 untuk April 2026, dicatat 27 April 2026,
            // » metode Cash. Data awal buku kas.
            PaymentRecord(107, "27 April 2026", "I GEDE ABI WIRYA DINATA", 20000, "April 2026", "Cash"),
            // » Riwayat no 106: GEDE AGUS WIRA DARMA PUTRA membayar Rp20.000 untuk April 2026, dicatat 27 April 2026,
            // » metode Cash. Data awal buku kas.
            PaymentRecord(106, "27 April 2026", "GEDE AGUS WIRA DARMA PUTRA", 20000, "April 2026", "Cash"),
            // » Riwayat no 105: GALISTAN RAMADHAN KURNIA TAUNAES membayar Rp20.000 untuk April 2026, dicatat 27 April
            // » 2026, metode Cash. Data awal buku kas.
            PaymentRecord(105, "27 April 2026", "GALISTAN RAMADHAN KURNIA TAUNAES", 20000, "April 2026", "Cash"),
            // » Riwayat no 104: DEWA GEDE DALEM OKA ADNYANA SANDI membayar Rp20.000 untuk April 2026, dicatat 27 April
            // » 2026, metode Cash. Data awal buku kas.
            PaymentRecord(104, "27 April 2026", "DEWA GEDE DALEM OKA ADNYANA SANDI", 20000, "April 2026", "Cash"),
            // » Riwayat no 103: CAROLINA TIMUTHY JANGGUR membayar Rp20.000 untuk April 2026, dicatat 27 April 2026,
            // » metode Cash. Data awal buku kas.
            PaymentRecord(103, "27 April 2026", "CAROLINA TIMUTHY JANGGUR", 20000, "April 2026", "Cash"),
            // » Riwayat no 102: BOYKE VILANO HAMONANGAN SIHITE membayar Rp20.000 untuk April 2026, dicatat 27 April
            // » 2026, metode Cash. Data awal buku kas.
            PaymentRecord(102, "27 April 2026", "BOYKE VILANO HAMONANGAN SIHITE", 20000, "April 2026", "Cash"),
            // » Riwayat no 101: BINTANG LEONITA CHRISTYA RENATA membayar Rp20.000 untuk April 2026, dicatat 27 April
            // » 2026, metode Cash. Data awal buku kas.
            PaymentRecord(101, "27 April 2026", "BINTANG LEONITA CHRISTYA RENATA", 20000, "April 2026", "Cash"),
            // » Riwayat no 100: RADITYA RONDI membayar Rp20.000 untuk Maret 2026, dicatat 23 Maret 2026, metode Cash.
            // » Data awal buku kas.
            PaymentRecord(100, "23 Maret 2026", "RADITYA RONDI", 20000, "Maret 2026", "Cash"),
            // » Riwayat no 99: PUTU PUTRI CAHYANI membayar Rp20.000 untuk Maret 2026, dicatat 23 Maret 2026, metode
            // » Cash. Data awal buku kas.
            PaymentRecord(99, "23 Maret 2026", "PUTU PUTRI CAHYANI", 20000, "Maret 2026", "Cash"),
            // » Riwayat no 98: PUTU NANDA LINDIA MAHARANI membayar Rp20.000 untuk Maret 2026, dicatat 23 Maret 2026,
            // » metode Cash. Data awal buku kas.
            PaymentRecord(98, "23 Maret 2026", "PUTU NANDA LINDIA MAHARANI", 20000, "Maret 2026", "Cash"),
            // » Riwayat no 97: PUTU BAYU SATRIA WANGSA BUKIAN membayar Rp20.000 untuk Maret 2026, dicatat 23 Maret 2026,
            // » metode Cash. Data awal buku kas.
            PaymentRecord(97, "23 Maret 2026", "PUTU BAYU SATRIA WANGSA BUKIAN", 20000, "Maret 2026", "Cash"),
            // » Riwayat no 96: OKTA PRADIPTA ATTALA DZAKI membayar Rp20.000 untuk Maret 2026, dicatat 23 Maret 2026,
            // » metode Cash. Data awal buku kas.
            PaymentRecord(96, "23 Maret 2026", "OKTA PRADIPTA ATTALA DZAKI", 20000, "Maret 2026", "Cash"),
            // » Riwayat no 95: NI PUTU INTAN LESTARI DARMAYANTI membayar Rp20.000 untuk Maret 2026, dicatat 23 Maret
            // » 2026, metode Cash. Data awal buku kas.
            PaymentRecord(95, "23 Maret 2026", "NI PUTU INTAN LESTARI DARMAYANTI", 20000, "Maret 2026", "Cash"),
            // » Riwayat no 94: NI PUTU CAHAYA LESTARI DEWI membayar Rp20.000 untuk Maret 2026, dicatat 23 Maret 2026,
            // » metode Cash. Data awal buku kas.
            PaymentRecord(94, "23 Maret 2026", "NI PUTU CAHAYA LESTARI DEWI", 20000, "Maret 2026", "Cash"),
            // » Riwayat no 93: NI LUH PUTU KESYA ASTRI MELANI membayar Rp20.000 untuk Maret 2026, dicatat 23 Maret 2026,
            // » metode Cash. Data awal buku kas.
            PaymentRecord(93, "23 Maret 2026", "NI LUH PUTU KESYA ASTRI MELANI", 20000, "Maret 2026", "Cash"),
            // » Riwayat no 92: NI KOMANG SEPTIARINI membayar Rp20.000 untuk Maret 2026, dicatat 23 Maret 2026, metode
            // » Cash. Data awal buku kas.
            PaymentRecord(92, "23 Maret 2026", "NI KOMANG SEPTIARINI", 20000, "Maret 2026", "Cash"),
            // » Riwayat no 91: NI KOMANG KIRANA PARAMITA ARDANARI membayar Rp20.000 untuk Maret 2026, dicatat 23 Maret
            // » 2026, metode Cash. Data awal buku kas.
            PaymentRecord(91, "23 Maret 2026", "NI KOMANG KIRANA PARAMITA ARDANARI", 20000, "Maret 2026", "Cash"),
            // » Riwayat no 90: NI KADEK LINA ANTIKA DEWI membayar Rp20.000 untuk Maret 2026, dicatat 23 Maret 2026,
            // » metode Cash. Data awal buku kas.
            PaymentRecord(90, "23 Maret 2026", "NI KADEK LINA ANTIKA DEWI", 20000, "Maret 2026", "Cash"),
            // » Riwayat no 89: NI KADEK ADELIA CAHYA KENCANA PUTRI membayar Rp20.000 untuk Maret 2026, dicatat 23 Maret
            // » 2026, metode Cash. Data awal buku kas.
            PaymentRecord(89, "23 Maret 2026", "NI KADEK ADELIA CAHYA KENCANA PUTRI", 20000, "Maret 2026", "Cash"),
            // » Riwayat no 88: LUH RIA MIRASIH membayar Rp20.000 untuk Maret 2026, dicatat 23 Maret 2026, metode Cash.
            // » Data awal buku kas.
            PaymentRecord(88, "23 Maret 2026", "LUH RIA MIRASIH", 20000, "Maret 2026", "Cash"),
            // » Riwayat no 87: KOMANG DIAH PUTRI PRATIWI membayar Rp20.000 untuk Maret 2026, dicatat 23 Maret 2026,
            // » metode Cash. Data awal buku kas.
            PaymentRecord(87, "23 Maret 2026", "KOMANG DIAH PUTRI PRATIWI", 20000, "Maret 2026", "Cash"),
            // » Riwayat no 86: KADEK YUNI CALLISTA PUTRI DEWI membayar Rp20.000 untuk Maret 2026, dicatat 23 Maret 2026,
            // » metode Cash. Data awal buku kas.
            PaymentRecord(86, "23 Maret 2026", "KADEK YUNI CALLISTA PUTRI DEWI", 20000, "Maret 2026", "Cash"),
            // » Riwayat no 85: KADEK YUDA PRASETYA membayar Rp20.000 untuk Maret 2026, dicatat 23 Maret 2026, metode
            // » Cash. Data awal buku kas.
            PaymentRecord(85, "23 Maret 2026", "KADEK YUDA PRASETYA", 20000, "Maret 2026", "Cash"),
            // » Riwayat no 84: I WAYAN PASEK KEVIN ARIADI membayar Rp20.000 untuk Maret 2026, dicatat 23 Maret 2026,
            // » metode Cash. Data awal buku kas.
            PaymentRecord(84, "23 Maret 2026", "I WAYAN PASEK KEVIN ARIADI", 20000, "Maret 2026", "Cash"),
            // » Riwayat no 83: I WAYAN BAGUS PUTRAWAN membayar Rp20.000 untuk Maret 2026, dicatat 23 Maret 2026, metode
            // » Cash. Data awal buku kas.
            PaymentRecord(83, "23 Maret 2026", "I WAYAN BAGUS PUTRAWAN", 20000, "Maret 2026", "Cash"),
            // » Riwayat no 82: I PUTU SUYOGA MAHENDRA membayar Rp20.000 untuk Maret 2026, dicatat 23 Maret 2026, metode
            // » Cash. Data awal buku kas.
            PaymentRecord(82, "23 Maret 2026", "I PUTU SUYOGA MAHENDRA", 20000, "Maret 2026", "Cash"),
            // » Riwayat no 81: I PUTU PANDE ANDIKA membayar Rp20.000 untuk Maret 2026, dicatat 23 Maret 2026, metode
            // » Cash. Data awal buku kas.
            PaymentRecord(81, "23 Maret 2026", "I PUTU PANDE ANDIKA", 20000, "Maret 2026", "Cash"),
            // » Riwayat no 80: I PUTU DITYA ARTHA WIJAYA membayar Rp20.000 untuk Maret 2026, dicatat 23 Maret 2026,
            // » metode Cash. Data awal buku kas.
            PaymentRecord(80, "23 Maret 2026", "I PUTU DITYA ARTHA WIJAYA", 20000, "Maret 2026", "Cash"),
            // » Riwayat no 79: I PUTU DIKA LAKSMANA PUTRA membayar Rp20.000 untuk Maret 2026, dicatat 23 Maret 2026,
            // » metode Cash. Data awal buku kas.
            PaymentRecord(79, "23 Maret 2026", "I PUTU DIKA LAKSMANA PUTRA", 20000, "Maret 2026", "Cash"),
            // » Riwayat no 78: I NYOMAN GEDE ARTA WIGUNA membayar Rp20.000 untuk Maret 2026, dicatat 23 Maret 2026,
            // » metode Cash. Data awal buku kas.
            PaymentRecord(78, "23 Maret 2026", "I NYOMAN GEDE ARTA WIGUNA", 20000, "Maret 2026", "Cash"),
            // » Riwayat no 77: I KOMANG RISKI SETIAWAN membayar Rp20.000 untuk Maret 2026, dicatat 23 Maret 2026, metode
            // » Cash. Data awal buku kas.
            PaymentRecord(77, "23 Maret 2026", "I KOMANG RISKI SETIAWAN", 20000, "Maret 2026", "Cash"),
            // » Riwayat no 76: I KOMANG RADITYA PUTRA membayar Rp20.000 untuk Maret 2026, dicatat 23 Maret 2026, metode
            // » Cash. Data awal buku kas.
            PaymentRecord(76, "23 Maret 2026", "I KOMANG RADITYA PUTRA", 20000, "Maret 2026", "Cash"),
            // » Riwayat no 75: I GEDE DARMA SUPTIAWAN membayar Rp20.000 untuk Maret 2026, dicatat 23 Maret 2026, metode
            // » Cash. Data awal buku kas.
            PaymentRecord(75, "23 Maret 2026", "I GEDE DARMA SUPTIAWAN", 20000, "Maret 2026", "Cash"),
            // » Riwayat no 74: I GEDE ABI WIRYA DINATA membayar Rp20.000 untuk Maret 2026, dicatat 23 Maret 2026, metode
            // » Cash. Data awal buku kas.
            PaymentRecord(74, "23 Maret 2026", "I GEDE ABI WIRYA DINATA", 20000, "Maret 2026", "Cash"),
            // » Riwayat no 73: GEDE AGUS WIRA DARMA PUTRA membayar Rp20.000 untuk Maret 2026, dicatat 23 Maret 2026,
            // » metode Cash. Data awal buku kas.
            PaymentRecord(73, "23 Maret 2026", "GEDE AGUS WIRA DARMA PUTRA", 20000, "Maret 2026", "Cash"),
            // » Riwayat no 72: GALISTAN RAMADHAN KURNIA TAUNAES membayar Rp20.000 untuk Maret 2026, dicatat 23 Maret
            // » 2026, metode Cash. Data awal buku kas.
            PaymentRecord(72, "23 Maret 2026", "GALISTAN RAMADHAN KURNIA TAUNAES", 20000, "Maret 2026", "Cash"),
            // » Riwayat no 71: DEWA GEDE DALEM OKA ADNYANA SANDI membayar Rp20.000 untuk Maret 2026, dicatat 23 Maret
            // » 2026, metode Cash. Data awal buku kas.
            PaymentRecord(71, "23 Maret 2026", "DEWA GEDE DALEM OKA ADNYANA SANDI", 20000, "Maret 2026", "Cash"),
            // » Riwayat no 70: CAROLINA TIMUTHY JANGGUR membayar Rp20.000 untuk Maret 2026, dicatat 23 Maret 2026,
            // » metode Cash. Data awal buku kas.
            PaymentRecord(70, "23 Maret 2026", "CAROLINA TIMUTHY JANGGUR", 20000, "Maret 2026", "Cash"),
            // » Riwayat no 69: BOYKE VILANO HAMONANGAN SIHITE membayar Rp20.000 untuk Maret 2026, dicatat 23 Maret 2026,
            // » metode Cash. Data awal buku kas.
            PaymentRecord(69, "23 Maret 2026", "BOYKE VILANO HAMONANGAN SIHITE", 20000, "Maret 2026", "Cash"),
            // » Riwayat no 68: BINTANG LEONITA CHRISTYA RENATA membayar Rp20.000 untuk Maret 2026, dicatat 23 Maret
            // » 2026, metode Cash. Data awal buku kas.
            PaymentRecord(68, "23 Maret 2026", "BINTANG LEONITA CHRISTYA RENATA", 20000, "Maret 2026", "Cash"),
            // » Riwayat no 67: RADITYA RONDI membayar Rp20.000 untuk Februari 2026, dicatat 23 Februari 2026, metode
            // » Cash. Data awal buku kas.
            PaymentRecord(67, "23 Februari 2026", "RADITYA RONDI", 20000, "Februari 2026", "Cash"),
            // » Riwayat no 66: PUTU PUTRI CAHYANI membayar Rp20.000 untuk Februari 2026, dicatat 23 Februari 2026,
            // » metode Cash. Data awal buku kas.
            PaymentRecord(66, "23 Februari 2026", "PUTU PUTRI CAHYANI", 20000, "Februari 2026", "Cash"),
            // » Riwayat no 65: PUTU NANDA LINDIA MAHARANI membayar Rp20.000 untuk Februari 2026, dicatat 23 Februari
            // » 2026, metode Cash. Data awal buku kas.
            PaymentRecord(65, "23 Februari 2026", "PUTU NANDA LINDIA MAHARANI", 20000, "Februari 2026", "Cash"),
            // » Riwayat no 64: PUTU BAYU SATRIA WANGSA BUKIAN membayar Rp20.000 untuk Februari 2026, dicatat 23 Februari
            // » 2026, metode Cash. Data awal buku kas.
            PaymentRecord(64, "23 Februari 2026", "PUTU BAYU SATRIA WANGSA BUKIAN", 20000, "Februari 2026", "Cash"),
            // » Riwayat no 63: OKTA PRADIPTA ATTALA DZAKI membayar Rp20.000 untuk Februari 2026, dicatat 23 Februari
            // » 2026, metode Cash. Data awal buku kas.
            PaymentRecord(63, "23 Februari 2026", "OKTA PRADIPTA ATTALA DZAKI", 20000, "Februari 2026", "Cash"),
            // » Riwayat no 62: NI PUTU INTAN LESTARI DARMAYANTI membayar Rp20.000 untuk Februari 2026, dicatat 23
            // » Februari 2026, metode Cash. Data awal buku kas.
            PaymentRecord(62, "23 Februari 2026", "NI PUTU INTAN LESTARI DARMAYANTI", 20000, "Februari 2026", "Cash"),
            // » Riwayat no 61: NI PUTU CAHAYA LESTARI DEWI membayar Rp20.000 untuk Februari 2026, dicatat 23 Februari
            // » 2026, metode Cash. Data awal buku kas.
            PaymentRecord(61, "23 Februari 2026", "NI PUTU CAHAYA LESTARI DEWI", 20000, "Februari 2026", "Cash"),
            // » Riwayat no 60: NI LUH PUTU KESYA ASTRI MELANI membayar Rp20.000 untuk Februari 2026, dicatat 23 Februari
            // » 2026, metode Cash. Data awal buku kas.
            PaymentRecord(60, "23 Februari 2026", "NI LUH PUTU KESYA ASTRI MELANI", 20000, "Februari 2026", "Cash"),
            // » Riwayat no 59: NI KOMANG SEPTIARINI membayar Rp20.000 untuk Februari 2026, dicatat 23 Februari 2026,
            // » metode Cash. Data awal buku kas.
            PaymentRecord(59, "23 Februari 2026", "NI KOMANG SEPTIARINI", 20000, "Februari 2026", "Cash"),
            // » Riwayat no 58: NI KOMANG KIRANA PARAMITA ARDANARI membayar Rp20.000 untuk Februari 2026, dicatat 23
            // » Februari 2026, metode Cash. Data awal buku kas.
            PaymentRecord(58, "23 Februari 2026", "NI KOMANG KIRANA PARAMITA ARDANARI", 20000, "Februari 2026", "Cash"),
            // » Riwayat no 57: NI KADEK LINA ANTIKA DEWI membayar Rp20.000 untuk Februari 2026, dicatat 23 Februari
            // » 2026, metode Cash. Data awal buku kas.
            PaymentRecord(57, "23 Februari 2026", "NI KADEK LINA ANTIKA DEWI", 20000, "Februari 2026", "Cash"),
            // » Riwayat no 56: NI KADEK ADELIA CAHYA KENCANA PUTRI membayar Rp20.000 untuk Februari 2026, dicatat 23
            // » Februari 2026, metode Cash. Data awal buku kas.
            PaymentRecord(56, "23 Februari 2026", "NI KADEK ADELIA CAHYA KENCANA PUTRI", 20000, "Februari 2026", "Cash"),
            // » Riwayat no 55: LUH RIA MIRASIH membayar Rp20.000 untuk Februari 2026, dicatat 23 Februari 2026, metode
            // » Cash. Data awal buku kas.
            PaymentRecord(55, "23 Februari 2026", "LUH RIA MIRASIH", 20000, "Februari 2026", "Cash"),
            // » Riwayat no 54: KOMANG DIAH PUTRI PRATIWI membayar Rp20.000 untuk Februari 2026, dicatat 23 Februari
            // » 2026, metode Cash. Data awal buku kas.
            PaymentRecord(54, "23 Februari 2026", "KOMANG DIAH PUTRI PRATIWI", 20000, "Februari 2026", "Cash"),
            // » Riwayat no 53: KADEK YUNI CALLISTA PUTRI DEWI membayar Rp20.000 untuk Februari 2026, dicatat 23 Februari
            // » 2026, metode Cash. Data awal buku kas.
            PaymentRecord(53, "23 Februari 2026", "KADEK YUNI CALLISTA PUTRI DEWI", 20000, "Februari 2026", "Cash"),
            // » Riwayat no 52: KADEK YUDA PRASETYA membayar Rp20.000 untuk Februari 2026, dicatat 23 Februari 2026,
            // » metode Cash. Data awal buku kas.
            PaymentRecord(52, "23 Februari 2026", "KADEK YUDA PRASETYA", 20000, "Februari 2026", "Cash"),
            // » Riwayat no 51: I WAYAN PASEK KEVIN ARIADI membayar Rp20.000 untuk Februari 2026, dicatat 23 Februari
            // » 2026, metode Cash. Data awal buku kas.
            PaymentRecord(51, "23 Februari 2026", "I WAYAN PASEK KEVIN ARIADI", 20000, "Februari 2026", "Cash"),
            // » Riwayat no 50: I WAYAN BAGUS PUTRAWAN membayar Rp20.000 untuk Februari 2026, dicatat 23 Februari 2026,
            // » metode Cash. Data awal buku kas.
            PaymentRecord(50, "23 Februari 2026", "I WAYAN BAGUS PUTRAWAN", 20000, "Februari 2026", "Cash"),
            // » Riwayat no 49: I PUTU SUYOGA MAHENDRA membayar Rp20.000 untuk Februari 2026, dicatat 23 Februari 2026,
            // » metode Cash. Data awal buku kas.
            PaymentRecord(49, "23 Februari 2026", "I PUTU SUYOGA MAHENDRA", 20000, "Februari 2026", "Cash"),
            // » Riwayat no 48: I PUTU PANDE ANDIKA membayar Rp20.000 untuk Februari 2026, dicatat 23 Februari 2026,
            // » metode Cash. Data awal buku kas.
            PaymentRecord(48, "23 Februari 2026", "I PUTU PANDE ANDIKA", 20000, "Februari 2026", "Cash"),
            // » Riwayat no 47: I PUTU DITYA ARTHA WIJAYA membayar Rp20.000 untuk Februari 2026, dicatat 23 Februari
            // » 2026, metode Cash. Data awal buku kas.
            PaymentRecord(47, "23 Februari 2026", "I PUTU DITYA ARTHA WIJAYA", 20000, "Februari 2026", "Cash"),
            // » Riwayat no 46: I PUTU DIKA LAKSMANA PUTRA membayar Rp20.000 untuk Februari 2026, dicatat 23 Februari
            // » 2026, metode Cash. Data awal buku kas.
            PaymentRecord(46, "23 Februari 2026", "I PUTU DIKA LAKSMANA PUTRA", 20000, "Februari 2026", "Cash"),
            // » Riwayat no 45: I NYOMAN GEDE ARTA WIGUNA membayar Rp20.000 untuk Februari 2026, dicatat 23 Februari
            // » 2026, metode Cash. Data awal buku kas.
            PaymentRecord(45, "23 Februari 2026", "I NYOMAN GEDE ARTA WIGUNA", 20000, "Februari 2026", "Cash"),
            // » Riwayat no 44: I KOMANG RISKI SETIAWAN membayar Rp20.000 untuk Februari 2026, dicatat 23 Februari 2026,
            // » metode Cash. Data awal buku kas.
            PaymentRecord(44, "23 Februari 2026", "I KOMANG RISKI SETIAWAN", 20000, "Februari 2026", "Cash"),
            // » Riwayat no 43: I KOMANG RADITYA PUTRA membayar Rp20.000 untuk Februari 2026, dicatat 23 Februari 2026,
            // » metode Cash. Data awal buku kas.
            PaymentRecord(43, "23 Februari 2026", "I KOMANG RADITYA PUTRA", 20000, "Februari 2026", "Cash"),
            // » Riwayat no 42: I GEDE DARMA SUPTIAWAN membayar Rp20.000 untuk Februari 2026, dicatat 23 Februari 2026,
            // » metode Cash. Data awal buku kas.
            PaymentRecord(42, "23 Februari 2026", "I GEDE DARMA SUPTIAWAN", 20000, "Februari 2026", "Cash"),
            // » Riwayat no 41: I GEDE ABI WIRYA DINATA membayar Rp20.000 untuk Februari 2026, dicatat 23 Februari 2026,
            // » metode Cash. Data awal buku kas.
            PaymentRecord(41, "23 Februari 2026", "I GEDE ABI WIRYA DINATA", 20000, "Februari 2026", "Cash"),
            // » Riwayat no 40: GEDE AGUS WIRA DARMA PUTRA membayar Rp20.000 untuk Februari 2026, dicatat 23 Februari
            // » 2026, metode Cash. Data awal buku kas.
            PaymentRecord(40, "23 Februari 2026", "GEDE AGUS WIRA DARMA PUTRA", 20000, "Februari 2026", "Cash"),
            // » Riwayat no 39: GALISTAN RAMADHAN KURNIA TAUNAES membayar Rp20.000 untuk Februari 2026, dicatat 23
            // » Februari 2026, metode Cash. Data awal buku kas.
            PaymentRecord(39, "23 Februari 2026", "GALISTAN RAMADHAN KURNIA TAUNAES", 20000, "Februari 2026", "Cash"),
            // » Riwayat no 38: DEWA GEDE DALEM OKA ADNYANA SANDI membayar Rp20.000 untuk Februari 2026, dicatat 23
            // » Februari 2026, metode Cash. Data awal buku kas.
            PaymentRecord(38, "23 Februari 2026", "DEWA GEDE DALEM OKA ADNYANA SANDI", 20000, "Februari 2026", "Cash"),
            // » Riwayat no 37: CAROLINA TIMUTHY JANGGUR membayar Rp20.000 untuk Februari 2026, dicatat 23 Februari 2026,
            // » metode Cash. Data awal buku kas.
            PaymentRecord(37, "23 Februari 2026", "CAROLINA TIMUTHY JANGGUR", 20000, "Februari 2026", "Cash"),
            // » Riwayat no 36: BOYKE VILANO HAMONANGAN SIHITE membayar Rp20.000 untuk Februari 2026, dicatat 23 Februari
            // » 2026, metode Cash. Data awal buku kas.
            PaymentRecord(36, "23 Februari 2026", "BOYKE VILANO HAMONANGAN SIHITE", 20000, "Februari 2026", "Cash"),
            // » Riwayat no 35: BINTANG LEONITA CHRISTYA RENATA membayar Rp20.000 untuk Februari 2026, dicatat 23
            // » Februari 2026, metode Cash. Data awal buku kas.
            PaymentRecord(35, "23 Februari 2026", "BINTANG LEONITA CHRISTYA RENATA", 20000, "Februari 2026", "Cash"),
            // » Riwayat no 34 (donasi, bukan kas): DONASI - BU JALLU, Rp200.000, tanggal 01 Februari 2026, untuk
            // » Februari 2026. Data awal buku kas.
            PaymentRecord(34, "01 Februari 2026", "DONASI - BU JALLU", 200000, "Februari 2026", "Donasi"),
            // » Riwayat no 33: RADITYA RONDI membayar Rp20.000 untuk Januari 2026, dicatat 26 Januari 2026, metode Cash.
            // » Data awal buku kas.
            PaymentRecord(33, "26 Januari 2026", "RADITYA RONDI", 20000, "Januari 2026", "Cash"),
            // » Riwayat no 32: PUTU PUTRI CAHYANI membayar Rp20.000 untuk Januari 2026, dicatat 26 Januari 2026, metode
            // » Cash. Data awal buku kas.
            PaymentRecord(32, "26 Januari 2026", "PUTU PUTRI CAHYANI", 20000, "Januari 2026", "Cash"),
            // » Riwayat no 31: PUTU NANDA LINDIA MAHARANI membayar Rp20.000 untuk Januari 2026, dicatat 26 Januari 2026,
            // » metode Cash. Data awal buku kas.
            PaymentRecord(31, "26 Januari 2026", "PUTU NANDA LINDIA MAHARANI", 20000, "Januari 2026", "Cash"),
            // » Riwayat no 30: PUTU BAYU SATRIA WANGSA BUKIAN membayar Rp20.000 untuk Januari 2026, dicatat 26 Januari
            // » 2026, metode Cash. Data awal buku kas.
            PaymentRecord(30, "26 Januari 2026", "PUTU BAYU SATRIA WANGSA BUKIAN", 20000, "Januari 2026", "Cash"),
            // » Riwayat no 29: OKTA PRADIPTA ATTALA DZAKI membayar Rp20.000 untuk Januari 2026, dicatat 26 Januari 2026,
            // » metode Cash. Data awal buku kas.
            PaymentRecord(29, "26 Januari 2026", "OKTA PRADIPTA ATTALA DZAKI", 20000, "Januari 2026", "Cash"),
            // » Riwayat no 28: NI PUTU INTAN LESTARI DARMAYANTI membayar Rp20.000 untuk Januari 2026, dicatat 26 Januari
            // » 2026, metode Cash. Data awal buku kas.
            PaymentRecord(28, "26 Januari 2026", "NI PUTU INTAN LESTARI DARMAYANTI", 20000, "Januari 2026", "Cash"),
            // » Riwayat no 27: NI PUTU CAHAYA LESTARI DEWI membayar Rp20.000 untuk Januari 2026, dicatat 26 Januari
            // » 2026, metode Cash. Data awal buku kas.
            PaymentRecord(27, "26 Januari 2026", "NI PUTU CAHAYA LESTARI DEWI", 20000, "Januari 2026", "Cash"),
            // » Riwayat no 26: NI LUH PUTU KESYA ASTRI MELANI membayar Rp20.000 untuk Januari 2026, dicatat 26 Januari
            // » 2026, metode Cash. Data awal buku kas.
            PaymentRecord(26, "26 Januari 2026", "NI LUH PUTU KESYA ASTRI MELANI", 20000, "Januari 2026", "Cash"),
            // » Riwayat no 25: NI KOMANG SEPTIARINI membayar Rp20.000 untuk Januari 2026, dicatat 26 Januari 2026,
            // » metode Cash. Data awal buku kas.
            PaymentRecord(25, "26 Januari 2026", "NI KOMANG SEPTIARINI", 20000, "Januari 2026", "Cash"),
            // » Riwayat no 24: NI KOMANG KIRANA PARAMITA ARDANARI membayar Rp20.000 untuk Januari 2026, dicatat 26
            // » Januari 2026, metode Cash. Data awal buku kas.
            PaymentRecord(24, "26 Januari 2026", "NI KOMANG KIRANA PARAMITA ARDANARI", 20000, "Januari 2026", "Cash"),
            // » Riwayat no 23: NI KADEK LINA ANTIKA DEWI membayar Rp20.000 untuk Januari 2026, dicatat 26 Januari 2026,
            // » metode Cash. Data awal buku kas.
            PaymentRecord(23, "26 Januari 2026", "NI KADEK LINA ANTIKA DEWI", 20000, "Januari 2026", "Cash"),
            // » Riwayat no 22: NI KADEK ADELIA CAHYA KENCANA PUTRI membayar Rp20.000 untuk Januari 2026, dicatat 26
            // » Januari 2026, metode Cash. Data awal buku kas.
            PaymentRecord(22, "26 Januari 2026", "NI KADEK ADELIA CAHYA KENCANA PUTRI", 20000, "Januari 2026", "Cash"),
            // » Riwayat no 21: LUH RIA MIRASIH membayar Rp20.000 untuk Januari 2026, dicatat 26 Januari 2026, metode
            // » Cash. Data awal buku kas.
            PaymentRecord(21, "26 Januari 2026", "LUH RIA MIRASIH", 20000, "Januari 2026", "Cash"),
            // » Riwayat no 20: KOMANG DIAH PUTRI PRATIWI membayar Rp20.000 untuk Januari 2026, dicatat 26 Januari 2026,
            // » metode Cash. Data awal buku kas.
            PaymentRecord(20, "26 Januari 2026", "KOMANG DIAH PUTRI PRATIWI", 20000, "Januari 2026", "Cash"),
            // » Riwayat no 19: KADEK YUNI CALLISTA PUTRI DEWI membayar Rp20.000 untuk Januari 2026, dicatat 26 Januari
            // » 2026, metode Cash. Data awal buku kas.
            PaymentRecord(19, "26 Januari 2026", "KADEK YUNI CALLISTA PUTRI DEWI", 20000, "Januari 2026", "Cash"),
            // » Riwayat no 18: KADEK YUDA PRASETYA membayar Rp20.000 untuk Januari 2026, dicatat 26 Januari 2026, metode
            // » Cash. Data awal buku kas.
            PaymentRecord(18, "26 Januari 2026", "KADEK YUDA PRASETYA", 20000, "Januari 2026", "Cash"),
            // » Riwayat no 17: I WAYAN PASEK KEVIN ARIADI membayar Rp20.000 untuk Januari 2026, dicatat 26 Januari 2026,
            // » metode Cash. Data awal buku kas.
            PaymentRecord(17, "26 Januari 2026", "I WAYAN PASEK KEVIN ARIADI", 20000, "Januari 2026", "Cash"),
            // » Riwayat no 16: I WAYAN BAGUS PUTRAWAN membayar Rp20.000 untuk Januari 2026, dicatat 26 Januari 2026,
            // » metode Cash. Data awal buku kas.
            PaymentRecord(16, "26 Januari 2026", "I WAYAN BAGUS PUTRAWAN", 20000, "Januari 2026", "Cash"),
            // » Riwayat no 15: I PUTU SUYOGA MAHENDRA membayar Rp20.000 untuk Januari 2026, dicatat 26 Januari 2026,
            // » metode Cash. Data awal buku kas.
            PaymentRecord(15, "26 Januari 2026", "I PUTU SUYOGA MAHENDRA", 20000, "Januari 2026", "Cash"),
            // » Riwayat no 14: I PUTU PANDE ANDIKA membayar Rp20.000 untuk Januari 2026, dicatat 26 Januari 2026, metode
            // » Cash. Data awal buku kas.
            PaymentRecord(14, "26 Januari 2026", "I PUTU PANDE ANDIKA", 20000, "Januari 2026", "Cash"),
            // » Riwayat no 13: I PUTU DITYA ARTHA WIJAYA membayar Rp20.000 untuk Januari 2026, dicatat 26 Januari 2026,
            // » metode Cash. Data awal buku kas.
            PaymentRecord(13, "26 Januari 2026", "I PUTU DITYA ARTHA WIJAYA", 20000, "Januari 2026", "Cash"),
            // » Riwayat no 12: I PUTU DIKA LAKSMANA PUTRA membayar Rp20.000 untuk Januari 2026, dicatat 26 Januari 2026,
            // » metode Cash. Data awal buku kas.
            PaymentRecord(12, "26 Januari 2026", "I PUTU DIKA LAKSMANA PUTRA", 20000, "Januari 2026", "Cash"),
            // » Riwayat no 11: I NYOMAN GEDE ARTA WIGUNA membayar Rp20.000 untuk Januari 2026, dicatat 26 Januari 2026,
            // » metode Cash. Data awal buku kas.
            PaymentRecord(11, "26 Januari 2026", "I NYOMAN GEDE ARTA WIGUNA", 20000, "Januari 2026", "Cash"),
            // » Riwayat no 10: I KOMANG RISKI SETIAWAN membayar Rp20.000 untuk Januari 2026, dicatat 26 Januari 2026,
            // » metode Cash. Data awal buku kas.
            PaymentRecord(10, "26 Januari 2026", "I KOMANG RISKI SETIAWAN", 20000, "Januari 2026", "Cash"),
            // » Riwayat no 9: I KOMANG RADITYA PUTRA membayar Rp20.000 untuk Januari 2026, dicatat 26 Januari 2026,
            // » metode Cash. Data awal buku kas.
            PaymentRecord(9, "26 Januari 2026", "I KOMANG RADITYA PUTRA", 20000, "Januari 2026", "Cash"),
            // » Riwayat no 8: I GEDE DARMA SUPTIAWAN membayar Rp20.000 untuk Januari 2026, dicatat 26 Januari 2026,
            // » metode Cash. Data awal buku kas.
            PaymentRecord(8, "26 Januari 2026", "I GEDE DARMA SUPTIAWAN", 20000, "Januari 2026", "Cash"),
            // » Riwayat no 7: I GEDE ABI WIRYA DINATA membayar Rp20.000 untuk Januari 2026, dicatat 26 Januari 2026,
            // » metode Cash. Data awal buku kas.
            PaymentRecord(7, "26 Januari 2026", "I GEDE ABI WIRYA DINATA", 20000, "Januari 2026", "Cash"),
            // » Riwayat no 6: GEDE AGUS WIRA DARMA PUTRA membayar Rp20.000 untuk Januari 2026, dicatat 26 Januari 2026,
            // » metode Cash. Data awal buku kas.
            PaymentRecord(6, "26 Januari 2026", "GEDE AGUS WIRA DARMA PUTRA", 20000, "Januari 2026", "Cash"),
            // » Riwayat no 5: GALISTAN RAMADHAN KURNIA TAUNAES membayar Rp20.000 untuk Januari 2026, dicatat 26 Januari
            // » 2026, metode Cash. Data awal buku kas.
            PaymentRecord(5, "26 Januari 2026", "GALISTAN RAMADHAN KURNIA TAUNAES", 20000, "Januari 2026", "Cash"),
            // » Riwayat no 4: DEWA GEDE DALEM OKA ADNYANA SANDI membayar Rp20.000 untuk Januari 2026, dicatat 26 Januari
            // » 2026, metode Cash. Data awal buku kas.
            PaymentRecord(4, "26 Januari 2026", "DEWA GEDE DALEM OKA ADNYANA SANDI", 20000, "Januari 2026", "Cash"),
            // » Riwayat no 3: CAROLINA TIMUTHY JANGGUR membayar Rp20.000 untuk Januari 2026, dicatat 26 Januari 2026,
            // » metode Cash. Data awal buku kas.
            PaymentRecord(3, "26 Januari 2026", "CAROLINA TIMUTHY JANGGUR", 20000, "Januari 2026", "Cash"),
            // » Riwayat no 2: BOYKE VILANO HAMONANGAN SIHITE membayar Rp20.000 untuk Januari 2026, dicatat 26 Januari
            // » 2026, metode Cash. Data awal buku kas.
            PaymentRecord(2, "26 Januari 2026", "BOYKE VILANO HAMONANGAN SIHITE", 20000, "Januari 2026", "Cash"),
            // » Riwayat no 1: BINTANG LEONITA CHRISTYA RENATA membayar Rp20.000 untuk Januari 2026, dicatat 26 Januari
            // » 2026, metode Cash. Data awal buku kas.
            PaymentRecord(1, "26 Januari 2026", "BINTANG LEONITA CHRISTYA RENATA", 20000, "Januari 2026", "Cash")
        // » Tutup daftar riwayat pembayaran awal.
        )
        // » _pengeluaranList.value = daftar pengeluaran awal dari buku kas (terbaru di atas).
        _pengeluaranList.value = listOf(
            // » Pengeluaran exp-30: "Selasa Literasi" sebesar Rp59.000, tanggal 10 November 2026.
            Pengeluaran(id = "exp-30", tanggal = "10 November 2026", jumlah = 59000, keterangan = "Selasa Literasi"),
            // » Pengeluaran exp-29: "Saraswati" sebesar Rp995.000, tanggal 30 Oktober 2026.
            Pengeluaran(id = "exp-29", tanggal = "30 Oktober 2026", jumlah = 995000, keterangan = "Saraswati"),
            // » Pengeluaran exp-28: "Literasi Bahasa Inggris" sebesar Rp47.000, tanggal 30 September 2026.
            Pengeluaran(id = "exp-28", tanggal = "30 September 2026", jumlah = 47000, keterangan = "Literasi Bahasa Inggris"),
            // » Pengeluaran exp-23: "Kamis Literasi" sebesar Rp36.000, tanggal 10 September 2026.
            Pengeluaran(id = "exp-23", tanggal = "10 September 2026", jumlah = 36000, keterangan = "Kamis Literasi"),
            // » Pengeluaran exp-22: "MAP PLASTIK BIG" sebesar Rp17.000, tanggal 26 Juli 2026.
            Pengeluaran(id = "exp-22", tanggal = "26 Juli 2026", jumlah = 17000, keterangan = "MAP PLASTIK BIG"),
            // » Pengeluaran exp-21: "SNOWMAN BOARD MARKER BLACK" sebesar Rp9.000, tanggal 26 Juli 2026.
            Pengeluaran(id = "exp-21", tanggal = "26 Juli 2026", jumlah = 9000, keterangan = "SNOWMAN BOARD MARKER BLACK"),
            // » Pengeluaran exp-20: "Jagung, Sayur, Kacang" sebesar Rp40.000, tanggal 01 Mei 2026.
            Pengeluaran(id = "exp-20", tanggal = "01 Mei 2026", jumlah = 40000, keterangan = "Jagung, Sayur, Kacang"),
            // » Pengeluaran exp-16: "Jahit Bendera" sebesar Rp35.000, tanggal 01 Mei 2026.
            Pengeluaran(id = "exp-16", tanggal = "01 Mei 2026", jumlah = 35000, keterangan = "Jahit Bendera"),
            // » Pengeluaran exp-15: "Property Drama" sebesar Rp200.000, tanggal 01 Mei 2026.
            Pengeluaran(id = "exp-15", tanggal = "01 Mei 2026", jumlah = 200000, keterangan = "Property Drama"),
            // » Pengeluaran exp-14: "Tirai & Jahit" sebesar Rp255.000, tanggal 01 April 2026.
            Pengeluaran(id = "exp-14", tanggal = "01 April 2026", jumlah = 255000, keterangan = "Tirai & Jahit"),
            // » Pengeluaran exp-13: "Latihan Masak" sebesar Rp93.000, tanggal 01 April 2026.
            Pengeluaran(id = "exp-13", tanggal = "01 April 2026", jumlah = 93000, keterangan = "Latihan Masak"),
            // » Pengeluaran exp-12: "Saraswati" sebesar Rp440.000, tanggal 01 April 2026.
            Pengeluaran(id = "exp-12", tanggal = "01 April 2026", jumlah = 440000, keterangan = "Saraswati"),
            // » Pengeluaran exp-11: "Property Drama" sebesar Rp300.000, tanggal 01 Maret 2026.
            Pengeluaran(id = "exp-11", tanggal = "01 Maret 2026", jumlah = 300000, keterangan = "Property Drama"),
            // » Pengeluaran exp-10: "Ogoh - Ogoh" sebesar Rp140.000, tanggal 01 Maret 2026.
            Pengeluaran(id = "exp-10", tanggal = "01 Maret 2026", jumlah = 140000, keterangan = "Ogoh - Ogoh"),
            // » Pengeluaran exp-9: "Literasi Pagi" sebesar Rp30.000, tanggal 01 Maret 2026.
            Pengeluaran(id = "exp-9", tanggal = "01 Maret 2026", jumlah = 30000, keterangan = "Literasi Pagi"),
            // » Pengeluaran exp-19: "Dompet kas" sebesar Rp6.500, tanggal 01 Februari 2026.
            Pengeluaran(id = "exp-19", tanggal = "01 Februari 2026", jumlah = 6500, keterangan = "Dompet kas"),
            // » Pengeluaran exp-18: "Pulpen kas" sebesar Rp4.000, tanggal 01 Februari 2026.
            Pengeluaran(id = "exp-18", tanggal = "01 Februari 2026", jumlah = 4000, keterangan = "Pulpen kas"),
            // » Pengeluaran exp-17: "Tugas Agama" sebesar Rp62.000, tanggal 01 Februari 2026.
            Pengeluaran(id = "exp-17", tanggal = "01 Februari 2026", jumlah = 62000, keterangan = "Tugas Agama"),
            // » Pengeluaran exp-8: "Kanvas" sebesar Rp67.000, tanggal 01 Februari 2026.
            Pengeluaran(id = "exp-8", tanggal = "01 Februari 2026", jumlah = 67000, keterangan = "Kanvas"),
            // » Pengeluaran exp-7: "Ogoh - Ogoh" sebesar Rp100.000, tanggal 01 Februari 2026.
            Pengeluaran(id = "exp-7", tanggal = "01 Februari 2026", jumlah = 100000, keterangan = "Ogoh - Ogoh"),
            // » Pengeluaran exp-6: "Ogoh - Ogoh" sebesar Rp200.000, tanggal 01 Februari 2026.
            Pengeluaran(id = "exp-6", tanggal = "01 Februari 2026", jumlah = 200000, keterangan = "Ogoh - Ogoh"),
            // » Pengeluaran exp-5: "Uang Keluar" sebesar Rp50.000, tanggal 01 Februari 2026.
            Pengeluaran(id = "exp-5", tanggal = "01 Februari 2026", jumlah = 50000, keterangan = "Uang Keluar"),
            // » Pengeluaran exp-2: "Buku Kas" sebesar Rp10.500, tanggal 01 Februari 2026.
            Pengeluaran(id = "exp-2", tanggal = "01 Februari 2026", jumlah = 10500, keterangan = "Buku Kas"),
            // » Pengeluaran exp-1: "Ogoh - Ogoh" sebesar Rp300.000, tanggal 01 Februari 2026.
            Pengeluaran(id = "exp-1", tanggal = "01 Februari 2026", jumlah = 300000, keterangan = "Ogoh - Ogoh")
        // » Tutup daftar pengeluaran awal.
        )
        // » _userProfile.value = UserProfile(). Profil dikosongkan (belum ada yang login).
        _userProfile.value = UserProfile()
    // » Tutup fungsi loadInitialData.
    }

    // » Bagian ini mengubah data dari dan ke JSON (format teks) untuk disimpan di HP.
    // ==================== PERSISTENCE ====================

    // Mengubah profil akun menjadi JSON untuk disimpan di HP.
    // » profileToJson = ubah profil jadi objek JSON. private = hanya dipakai di kelas ini.
    private fun profileToJson(p: UserProfile) = JSONObject().apply {
        // » put("nama", ...) = isi kunci "nama" dengan nilai nama (begitu juga email, nis, kelas).
        put("nama", p.nama); put("email", p.email); put("nis", p.nis); put("kelas", p.kelas)
        // » put noHp, password, dan role (role disimpan sebagai teks "ADMIN" atau "USER").
        put("noHp", p.noHp); put("password", p.password); put("role", p.role.name)
    // » Tutup pembuatan JSON profil.
    }

    // Membaca profil akun dari JSON yang tersimpan.
    // » profileFromJson = kebalikan: ubah JSON jadi UserProfile.
    private fun profileFromJson(o: JSONObject) = UserProfile(
        // » optString("nama", "Admin") = ambil teks "nama". Kalau tidak ada, pakai "Admin".
        nama = o.optString("nama", "Admin"), email = o.optString("email", ""),
        // » nis dan kelas. Kalau kelas tidak ada, pakai "XII PPLG".
        nis = o.optString("nis", ""), kelas = o.optString("kelas", "XII PPLG"),
        // » noHp dan password. Kalau tidak ada, teks kosong.
        noHp = o.optString("noHp", ""), password = o.optString("password", ""),
        // » role = ubah teks jadi enum UserRole. runCatching = kalau gagal (teks salah), pakai USER.
        role = runCatching { UserRole.valueOf(o.optString("role", "USER")) }.getOrDefault(UserRole.USER)
    // » Tutup pembuatan UserProfile.
    )

    // Mengubah peta bulan ke jumlah minggu menjadi JSON.
    // » mapToJson = ubah peta bulan -> minggu jadi objek JSON. forEach = untuk setiap pasangan (k, v) lakukan
    // » put.
    private fun mapToJson(m: Map<String, Int>) = JSONObject().apply { m.forEach { (k, v) -> put(k, v) } }
    // Membaca peta bulan ke jumlah minggu dari JSON.
    // » mapFromJson = kebalikan: ubah JSON jadi peta bulan -> minggu.
    private fun mapFromJson(o: JSONObject?): Map<String, Int> =
        // » Kalau objek JSON kosong (null), hasilnya peta kosong. Kalau ada, setiap kunci diubah jadi pasangan
        // » (kunci, optInt nilainya).
        o?.keys()?.asSequence()?.associateWith { o.optInt(it) } ?: emptyMap()

    // MENYIMPAN KE HP: siswa, riwayat pembayaran, pengeluaran, akun terdaftar, dan profil dibuat jadi JSON lalu ditulis ke SharedPreferences "kastep_prefs".
    // Dipanggil setiap ada perubahan data sehingga tidak hilang saat aplikasi ditutup.
    // CARI-SIMPAN-DATA | Menyimpan semua data ke HP (JSON di SharedPreferences).
    // TANYA-GURU: Data disimpan di mana? File kastep_prefs di HP (SharedPreferences, JSON). Belum tersinkron antar HP.
    // » saveToPrefs = simpan semua data ke penyimpanan HP. Dipanggil setiap ada perubahan.
    private fun saveToPrefs() {
        // » Kalau penyimpanan tidak ada (prefs null), berhenti.
        if (prefs == null) return
        // » stArray = wadah JSON untuk daftar siswa.
        val stArray = JSONArray()
        // » Untuk setiap siswa...
        _students.value.forEach { st ->
            // » ...buat objek JSON dan masukkan ke stArray.
            stArray.put(JSONObject().apply {
                // » Simpan no, nama, dan peran siswa.
                put("no", st.no); put("nama", st.nama); put("peran", st.peran)
                // » Simpan bayar dan dispensasi (peta bulan -> minggu) dalam bentuk JSON.
                put("bayar", mapToJson(st.bayar)); put("dispensasi", mapToJson(st.dispensasi))
                // » Simpan kelas dan noHp.
                put("kelas", st.kelas); put("noHp", st.noHp)
            })
        }
        // » prArray = wadah JSON untuk riwayat pembayaran.
        val prArray = JSONArray()
        // » Untuk setiap riwayat...
        _paymentRecords.value.forEach { pr ->
            // » ...buat objek JSON dan masukkan ke prArray.
            prArray.put(JSONObject().apply {
                // » Simpan no, tanggal, dan namaSiswa.
                put("no", pr.no); put("tanggal", pr.tanggal); put("namaSiswa", pr.namaSiswa)
                // » Simpan jumlah, bulan, metode, dan denda.
                put("jumlah", pr.jumlah); put("bulan", pr.bulan); put("metode", pr.metode); put("denda", pr.denda)
            })
        }
        // » penArray = wadah JSON untuk pengeluaran.
        val penArray = JSONArray()
        // » Untuk setiap pengeluaran...
        _pengeluaranList.value.forEach { p ->
            // » ...buat objek JSON.
            penArray.put(JSONObject().apply {
                // » Simpan id, tanggal, jumlah, dan keterangan.
                put("id", p.id); put("tanggal", p.tanggal); put("jumlah", p.jumlah); put("keterangan", p.keterangan)
            })
        }
        // » usersArray = wadah JSON untuk semua akun terdaftar.
        val usersArray = JSONArray()
        // » Setiap akun diubah jadi JSON profil lalu dimasukkan ke usersArray.
        _registeredUsers.values.forEach { usersArray.put(profileToJson(it)) }

        // » prefs.edit().apply { ... } = mulai menulis beberapa data sekaligus ke HP.
        prefs.edit().apply {
            // » putBoolean(DATA_KEY, true) = tanda bahwa data sudah pernah disimpan (dipakai saat aplikasi dibuka).
            putBoolean(DATA_KEY, true)
            // » Simpan status login.
            putBoolean("is_logged_in", _isLoggedIn.value)
            // » Simpan profil akun yang login (sebagai teks JSON).
            putString("user_profile", profileToJson(_userProfile.value).toString())
            // » Simpan daftar siswa (teks JSON).
            putString("students", stArray.toString())
            // » Simpan riwayat pembayaran (teks JSON).
            putString("payment_records", prArray.toString())
            // » Simpan daftar pengeluaran (teks JSON).
            putString("pengeluaran_list", penArray.toString())
            // » Simpan daftar semua akun terdaftar (teks JSON).
            putString("registered_users_v2", usersArray.toString())
            // » apply() = tulis ke HP di belakang layar (tanpa menunggu).
            apply()
        // » Tutup penulisan ke penyimpanan.
        }
    }

    // MEMBACA KEMBALI dari HP saat aplikasi dibuka (JSON diubah jadi objek). Bila data rusak, dipakai data awal agar tidak crash.
    // CARI-SIMPAN-DATA | Memuat data tersimpan; bila rusak kembali ke data awal.
    // » loadFromPrefs = baca data yang sudah disimpan di HP.
    private fun loadFromPrefs() {
        if (prefs == null) return
        // » try = coba baca. Kalau ada yang gagal, lompat ke catch di bawah.
        try {
            // » Baca status login (kalau tidak ada, false).
            _isLoggedIn.value = prefs.getBoolean("is_logged_in", false)
            // » Kalau sedang login, baca profil yang tersimpan. Kalau tidak, profil kosong.
            _userProfile.value = if (_isLoggedIn.value) {
                // » getString("user_profile") = ambil teks profil. let = kalau ada, ubah jadi UserProfile. ?: = kalau tidak
                // » ada, pakai UserProfile().
                prefs.getString("user_profile", null)?.let { profileFromJson(JSONObject(it)) } ?: UserProfile()
            // » Cabang lain: profil kosong.
            } else UserProfile()

            // » Kalau ada daftar siswa tersimpan (let = hanya jika tidak kosong)...
            prefs.getString("students", null)?.let { str ->
                // » arr = ubah teks jadi JSONArray (daftar).
                val arr = JSONArray(str)
                // » Buat daftar Siswa dari setiap item JSON (0 sampai panjang - 1).
                _students.value = (0 until arr.length()).map { i ->
                    // » o = objek JSON siswa ke-i.
                    val o = arr.getJSONObject(i)
                    // » Buat objek Siswa dari JSON.
                    Siswa(
                        // » no, nama, dan peran wajib ada (getInt dan getString).
                        no = o.getInt("no"), nama = o.getString("nama"), peran = o.getString("peran"),
                        // » bayar dibaca dari JSON. optJSONObject = null kalau tidak ada.
                        bayar = mapFromJson(o.optJSONObject("bayar")),
                        // » dispensasi dibaca dari JSON.
                        dispensasi = mapFromJson(o.optJSONObject("dispensasi")),
                        // » kelas dan noHp (boleh tidak ada, pakai default).
                        kelas = o.optString("kelas", "XII PPLG"), noHp = o.optString("noHp", "")
                    )
                // » Tutup pembuatan daftar siswa.
                }
            }
            // » Kalau ada riwayat pembayaran tersimpan...
            prefs.getString("payment_records", null)?.let { str ->
                // » arr = daftar JSON riwayat.
                val arr = JSONArray(str)
                // » Buat daftar PaymentRecord dari setiap item JSON.
                _paymentRecords.value = (0 until arr.length()).map { i ->
                    // » o = objek JSON riwayat ke-i.
                    val o = arr.getJSONObject(i)
                    // » Buat satu PaymentRecord dari JSON.
                    // » Membuat satu PaymentRecord dari JSON (dipakai saat data dimuat dari HP).
                    PaymentRecord(
                        // » no dan tanggal wajib ada.
                        no = o.getInt("no"), tanggal = o.getString("tanggal"),
                        // » namaSiswa dan jumlah (getLong = angka besar) wajib ada.
                        namaSiswa = o.getString("namaSiswa"), jumlah = o.getLong("jumlah"),
                        // » bulan dan metode: kalau tidak ada, pakai default.
                        bulan = o.optString("bulan", "Juli 2026"), metode = o.optString("metode", "Cash"),
                        // » denda: kalau tidak ada, 0.
                        denda = o.optLong("denda", 0L)
                    )
                }
            }
            // » Kalau ada pengeluaran tersimpan...
            prefs.getString("pengeluaran_list", null)?.let { str ->
                // » arr = daftar JSON pengeluaran.
                val arr = JSONArray(str)
                // » Buat daftar Pengeluaran dari setiap item JSON.
                _pengeluaranList.value = (0 until arr.length()).map { i ->
                    // » o = objek JSON pengeluaran ke-i.
                    val o = arr.getJSONObject(i)
                    // » Buat satu Pengeluaran.
                    Pengeluaran(
                        // » id: kalau tidak ada, buat kode acak baru.
                        id = o.optString("id", java.util.UUID.randomUUID().toString()),
                        // » tanggal, jumlah, dan keterangan (wajib ada).
                        tanggal = o.getString("tanggal"), jumlah = o.getLong("jumlah"),
                        keterangan = o.getString("keterangan")
                    )
                }
            }
            // » Kalau ada daftar akun tersimpan...
            prefs.getString("registered_users_v2", null)?.let { str ->
                // » arr = daftar JSON akun.
                val arr = JSONArray(str)
                // » Untuk setiap akun di daftar...
                for (i in 0 until arr.length()) {
                    // » p = ubah JSON jadi UserProfile.
                    val p = profileFromJson(arr.getJSONObject(i))
                    // » Kalau email tidak kosong, masukkan ke daftar akun (email jadi kuncinya).
                    if (p.email.isNotBlank()) _registeredUsers[p.email] = p
                }
            }
        // » catch (e: Exception) = kalau terjadi error saat membaca data (mis. data rusak)...
        } catch (e: Exception) {
            // » Cetak detail error ke Logcat (bahan untuk mencari penyebab).
            e.printStackTrace()
            // » ...pakai data awal supaya aplikasi tidak crash.
            loadInitialData()
        // » Tutup blok catch.
        }
    // » Tutup fungsi loadFromPrefs.
    }
// » Tutup kelas KastepRepository.
}
