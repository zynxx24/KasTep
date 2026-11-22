// » pluginManagement = dari mana Gradle mengambil plugin (alat pembangun).
pluginManagement {
    // » repositories = daftar gudang tempat plugin diunduh.
    repositories {
        // » google { ... } = gudang Google, hanya untuk grup paket tertentu.
        google {
            // » content = batasi: hanya ambil paket com.android.*, ...
            content {
                // » includeGroupByRegex("com\\.android.*") = paket yang namanya diawali com.android.
                includeGroupByRegex("com\\.android.*")
                // » includeGroupByRegex("com\\.google.*") = paket yang namanya diawali com.google.
                includeGroupByRegex("com\\.google.*")
                // » includeGroupByRegex("androidx.*") = paket yang namanya diawali androidx (pustaka resmi Android).
                includeGroupByRegex("androidx.*")
            }
        // » Tutup blok google.
        }
        // » mavenCentral() = gudang umum Java/Kotlin.
        mavenCentral()
        // » gradlePluginPortal() = gudang plugin Gradle.
        gradlePluginPortal()
    }
}

// » dependencyResolutionManagement = aturan unduhan pustaka (library) yang dipakai aplikasi.
dependencyResolutionManagement {
    // » FAIL_ON_PROJECT_REPOS = modul tidak boleh menambah gudang sendiri; semua harus lewat sini.
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    // » repositories = gudang pustaka: Google dan Maven Central.
    repositories {
        google()
        mavenCentral()
    }
}

// » Nama proyek: KASTEP.
rootProject.name = "KASTEP"
// » Memasukkan modul "app" (isi aplikasi sebenarnya) ke dalam proyek.
include(":app")
