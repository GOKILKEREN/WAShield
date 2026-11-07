# WaShield 0.1.0

Modul Xposed defensif untuk eksperimen deteksi payload pada proses WhatsApp yang diluncurkan melalui ShizukuPosed. UI konfigurasi berjalan sebagai aplikasi Android terpisah. Tidak ada koneksi internet atau telemetry.

## Build
- Android Studio dengan JDK 17.
- Gradle wrapper yang kompatibel dengan Gradle 8.7.
- Android Gradle Plugin 8.5.2, Kotlin 1.9.24, compileSdk 34.
- Buka folder proyek, tunggu Gradle Sync, lalu jalankan `./gradlew assembleDebug`.
- APK: `app/build/outputs/apk/debug/app-debug.apk`.
- Xposed API disediakan sebagai `compileOnly("de.robv.android.xposed:api:82")`; API tidak dibundel ke APK.

## Instalasi
1. Build dan install APK WaShield.
2. Aktifkan WaShield pada ShizukuPosed.
3. Pastikan ShizukuPosed mendukung modul Xposed API klasik yang dipakai.
4. Luncurkan WhatsApp resmi melalui ShizukuPosed, bukan ikon launcher biasa.
5. Jangan hapus data atau cache WhatsApp. Sesi login yang sudah ada seharusnya tetap tersimpan karena paket dan data aplikasi tidak diganti.

## Batasan ShizukuPosed
- Modul tidak melakukan hook `system_server` atau zygote.
- Modul tidak dapat meng-hook proses WhatsApp yang sudah berjalan sebelum diluncurkan melalui ShizukuPosed.
- Proses target harus diluncurkan melalui ShizukuPosed.
- API version 96 yang dilaporkan ShizukuPosed tidak otomatis berarti semua ekstensi API 96 tersedia pada compile-time API 82.
- Modul menggunakan entry point `IXposedHookLoadPackage` API klasik. Perilaku aktual wajib diuji pada build ShizukuPosed yang terpasang.
- Hook `ClassLoader.loadClass` bersifat heuristik. Nama kelas yang cocok bukan bukti bahwa kelas tersebut memproses pesan.
- SQLite Guard hanya melihat operasi SQLite yang cocok; tidak menjamin semua penyimpanan WhatsApp melewati hook tersebut.
- Deteksi generic tidak dapat menjamin pemblokiran semua payload. Salah deteksi dapat mengganggu pemrosesan pesan.
- Provider log IPC pada prototipe ini membatasi pemanggil berdasarkan package target, tetapi bukan batas keamanan kuat terhadap proses target yang telah dikompromikan.
- Pengaturan rule pada UI prototipe belum disinkronkan ke proses hook. Default hook saat ini memakai rule HIGH/CRITICAL.
- Whitelist, filter tanggal, notifikasi, dan ekspor dari UI belum seluruhnya diimplementasikan pada versi prototipe ini.

## Privasi dan legal
WaShield dirancang defensif: deteksi, pencatatan lokal, dan pemblokiran heuristik. Tidak ada telemetry atau koneksi internet. Gunakan hanya pada perangkat dan akun yang berhak kamu kelola. Penggunaan modul dapat melanggar ketentuan layanan WhatsApp atau menyebabkan pesan tidak diproses sebagaimana mestinya. Pengguna bertanggung jawab atas pengujian dan penggunaan.

## Status
Ini adalah prototipe fondasi untuk memvalidasi build dan pemuatan modul. Jangan menganggapnya sebagai perlindungan produksi sebelum adapter decoder WhatsApp spesifik versi diuji.
