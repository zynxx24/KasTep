// » import java.util.Properties = memanggil alat untuk membaca file pengaturan (.properties), dipakai untuk
// » membaca data kunci tanda tangan rilis.
import java.util.Properties

// » plugins = plugin yang dipakai modul app.
plugins {
    // » Plugin Android (membuat .apk).
    alias(libs.plugins.android.application)
    // » Plugin Kotlin.
    alias(libs.plugins.kotlin.android)
}

// » android = pengaturan khusus aplikasi Android.
android {
    // » namespace = alamat paket kode (com.kastep.app), dipakai untuk mencari kelas R dan kode internal.
    namespace = "com.kastep.app"
    // » compileSdk = versi Android (34) yang dipakai saat menyusun kode.
    compileSdk = 34

    // » defaultConfig = pengaturan dasar yang sama untuk semua jenis build.
    defaultConfig {
        // » applicationId = ID unik aplikasi di HP (com.kastep.app).
        applicationId = "com.kastep.app"
        // » minSdk = versi Android paling rendah yang masih didukung (24 = Android 7.0).
        minSdk = 24
        // » targetSdk = versi Android yang menjadi sasaran (34).
        targetSdk = 34
        // » versionCode = nomor versi untuk sistem Android (angka naik setiap rilis).
        versionCode = 11
        // » versionName = nama versi yang dibaca manusia (1.3.1).
        versionName = "1.3.1"

        // » testInstrumentationRunner = penjalan tes di perangkat (AndroidJUnitRunner).
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        // » vectorDrawables = pengaturan gambar vektor.
        vectorDrawables {
            // » useSupportLibrary = dukung gambar vektor di versi Android lama.
            useSupportLibrary = true
        }
    }

    // » Komentar: tanda tangan rilis dibaca dari keystore.properties (file rahasia, tidak ikut di Git).
    // Release signing is read from keystore.properties (gitignored, never committed).
    // » keystoreFile = lokasi file keystore.properties di folder tingkat atas.
    val keystoreFile = rootProject.file("keystore.properties")
    // » keystoreProps = isi file itu dibaca. Kalau file tidak ada, isinya kosong.
    val keystoreProps = Properties().apply {
        // » Baca file kalau ada, lalu tutup filenya (use).
        if (keystoreFile.exists()) keystoreFile.inputStream().use { load(it) }
    }
    // » signingConfigs = daftar pengaturan tanda tangan.
    signingConfigs {
        // » Hanya buat konfigurasi "release" kalau file keystore ada.
        if (keystoreFile.exists()) {
            // » create("release") = buat konfigurasi tanda tangan bernama release.
            create("release") {
                // » storeFile = lokasi file penyimpanan kunci.
                storeFile = file(keystoreProps.getProperty("storeFile"))
                // » storePassword = kata sandi penyimpanan kunci (dari file rahasia).
                storePassword = keystoreProps.getProperty("storePassword")
                // » keyAlias = nama kunci di dalam penyimpanan.
                keyAlias = keystoreProps.getProperty("keyAlias")
                // » keyPassword = kata sandi kunci.
                keyPassword = keystoreProps.getProperty("keyPassword")
            }
        }
    }

    // » buildTypes = jenis build.
    buildTypes {
        // » release = build untuk rilis ke pengguna.
        release {
            // » Pakai tanda tangan release (kalau ada).
            signingConfig = signingConfigs.findByName("release")
            // » isMinifyEnabled = false: kode TIDAK dikecilkan/diacak di build rilis.
            isMinifyEnabled = false
            // » proguardFiles = aturan pengecil kode (ProGuard). Dipakai hanya bila minify aktif.
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    // » compileOptions = versi Java yang dipakai untuk kode sumber dan target.
    compileOptions {
        // » sourceCompatibility = kode ditulis seperti Java 17.
        sourceCompatibility = JavaVersion.VERSION_17
        // » targetCompatibility = hasil build juga Java 17.
        targetCompatibility = JavaVersion.VERSION_17
    }
    // » kotlinOptions = pengaturan khusus Kotlin.
    kotlinOptions {
        // » jvmTarget = 17, sesuai versi Java di atas.
        jvmTarget = "17"
    }
    // » buildFeatures = fitur yang dinyalakan.
    buildFeatures {
        // » compose = true: Jetpack Compose (cara membuat tampilan modern) aktif.
        compose = true
    }
    // » composeOptions = versi kompiler Compose.
    composeOptions {
        // » kotlinCompilerExtensionVersion = 1.5.11 (cocok dengan versi Kotlin proyek).
        kotlinCompilerExtensionVersion = "1.5.11"
    }
    // » packaging = aturan saat semua file digabung jadi .apk.
    packaging {
        // » resources = file yang dibuang saat digabung.
        resources {
            // » Buang file lisensi AL2.0 yang tidak diperlukan.
            excludes += "/META-INDEX/AL2.0"
            // » Buang file lisensi LGPL2.1 yang tidak diperlukan.
            excludes += "/META-INDEX/LGPL2.1"
        }
    }
}

// » dependencies = daftar pustaka (library) yang dipakai aplikasi.
dependencies {
    // » core-ktx = fungsi tambahan Android untuk Kotlin.
    implementation(libs.androidx.core.ktx)
    // » lifecycle-runtime-ktx = pustaka yang mengikuti siklus hidup layar.
    implementation(libs.androidx.lifecycle.runtime.ktx)
    // » viewmodel-compose = penghubung ViewModel dengan Compose (tempat menyimpan data layar).
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    // » activity-compose = penghubung Activity (layar utama) dengan Compose.
    implementation(libs.androidx.activity.compose)
    // » platform(compose-bom) = daftar versi Compose yang sudah cocok satu sama lain (BOM).
    implementation(platform(libs.androidx.compose.bom))
    // » ui = dasar Compose (tampilan).
    implementation(libs.androidx.ui)
    // » ui-graphics = gambar, warna, dan bentuk untuk Compose.
    implementation(libs.androidx.ui.graphics)
    // » ui-tooling-preview = pratinjau tampilan di Android Studio.
    implementation(libs.androidx.ui.tooling.preview)
    // » material3 = komponen tampilan gaya Material 3 (tombol, kartu, dst).
    implementation(libs.androidx.material3)
    // » material-icons-extended = kumpulan ikon tambahan.
    implementation(libs.androidx.material.icons.extended)
    // » navigation-compose = alat perpindahan antar layar.
    implementation(libs.androidx.navigation.compose)
    // » kotlinx-coroutines-android = alat pekerjaan di belakang layar (tidak membuat layar macet).
    implementation(libs.kotlinx.coroutines.android)

    // » ui-tooling = alat bantu tampilan untuk debug (hanya di build debug).
    debugImplementation(libs.androidx.ui.tooling)
}
