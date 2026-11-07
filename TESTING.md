# WaShield — Manual Testing

Lakukan hanya pada akun uji dan perangkat yang datanya sudah dicadangkan.

1. **Teks panjang:** kirim teks lebih dari 5.000 karakter. Pastikan detector unit test mengidentifikasi `text_length`. Pemblokiran runtime bergantung pada method WhatsApp yang berhasil ditemukan.
2. **Zero-width:** kirim teks uji dengan lebih dari 50 karakter U+200B. Pastikan rule `zero_width` menghasilkan HIGH.
3. **Flood:** uji rangkaian karakter berulang lebih dari 500 kali dan teks emoji panjang. Pastikan rule terkait terdeteksi.
4. **Whitelist:** belum tersedia pada prototipe 0.1.0; tandai sebagai pengujian tertunda, jangan menganggap whitelist aktif.
5. **Toggle rule:** UI menampilkan toggle, tetapi sinkronisasi konfigurasi ke proses hook belum tersedia pada prototipe 0.1.0. Uji perubahan UI saja dan tandai integrasi sebagai tertunda.

## Validasi instalasi
- APK dapat dipasang pada Android 13.
- WaShield dapat dibuka tanpa meminta login WhatsApp.
- Aktifkan modul di ShizukuPosed dan luncurkan WhatsApp melalui ShizukuPosed.
- Periksa log modul ShizukuPosed untuk pesan `WaShield: target process loaded`.
- Jangan menguji pemblokiran pada percakapan atau data penting.
