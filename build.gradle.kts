// » Komentar bawaan: file build TINGKAT ATAS. Pengaturan di sini dipakai bersama semua modul (di proyek ini
// » hanya modul "app").
// Top-level build file where you can add configuration options common to all sub-projects/modules.
// » plugins = daftar alat tambahan untuk Gradle (program pembangun aplikasi).
plugins {
    // » alias(libs.plugins.android.application) = plugin Android (pembuat .apk). Versinya diambil dari file
    // » gradle/libs.versions.toml. apply false = hanya didaftarkan, dipasang nanti di app/build.gradle.kts.
    alias(libs.plugins.android.application) apply false
    // » alias(libs.plugins.kotlin.android) = plugin untuk bahasa Kotlin. apply false = sama seperti baris di
    // » atas.
    alias(libs.plugins.kotlin.android) apply false
}
