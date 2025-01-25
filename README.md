
# README - Aplikasi Penyewaan PC

## Deskripsi
Aplikasi ini adalah sistem desktop berbasis Java yang dirancang untuk membantu dalam pengelolaan penyewaan PC. Dengan aplikasi ini, pengguna dapat:

1. Mengelola data PC yang tersedia untuk disewakan.
2. Mengelola data penyewa.
3. Mengelola transaksi penyewaan.
4. Melakukan pencetakan data (PC, penyewa, transaksi).

Aplikasi ini dibangun menggunakan **Java Swing** sebagai antarmuka pengguna dan **MySQL** sebagai database.

---

## Fitur Utama
1. **Pengelolaan Data PC**
   - Menambah, mengedit, menghapus data PC.
   - Menampilkan tabel daftar PC lengkap dengan spesifikasi, harga sewa, dan status (tersedia/disewa).
   - Pencetakan data PC.

2. **Pengelolaan Data Penyewa**
   - Menambah, mengedit, menghapus data penyewa.
   - Menampilkan tabel daftar penyewa lengkap dengan nama, alamat, dan kontak.
   - Pencetakan data penyewa.

3. **Pengelolaan Data Transaksi**
   - Menambah, mengedit, menghapus data transaksi penyewaan.
   - Menampilkan tabel daftar transaksi lengkap dengan informasi penyewa, PC, tanggal sewa, tanggal kembali, dan total harga.
   - Pencetakan data transaksi.

4. **Fungsi Cetak**
   - Semua data pada tabel (PC, penyewa, transaksi) dapat dicetak langsung dari aplikasi.

---

## Persyaratan Sistem
- **Java Development Kit (JDK)**: Minimum versi 8.
- **MySQL Server**: Versi terbaru.
- **IDE**: Disarankan menggunakan NetBeans untuk mempermudah integrasi GUI.

---

## Instalasi
1. **Persiapkan Database**:
   - Buat database baru di MySQL dengan nama `penyewaan_pc`.
   - Import file SQL yang disediakan untuk membuat tabel-tabel yang diperlukan.

2. **Konfigurasi Aplikasi**:
   - Pastikan koneksi database diatur pada file `menuUtama.java`:
     ```java
     String url = "jdbc:mysql://localhost:3306/penyewaan_pc";
     String user = "root";
     String password = "";
     ```
   - Sesuaikan `user` dan `password` sesuai dengan konfigurasi MySQL Anda.

3. **Compile dan Jalankan**:
   - Buka proyek di NetBeans atau IDE lain.
   - Jalankan aplikasi dengan mengeksekusi kelas utama `menuUtama`.

---

## Penggunaan
1. **Menambah Data**:
   - Isi formulir yang disediakan pada tab masing-masing (PC, Penyewa, atau Transaksi).
   - Klik tombol **Tambah** untuk menyimpan data ke database.

2. **Mengedit Data**:
   - Pilih baris data yang ingin diubah pada tabel.
   - Edit data pada formulir, lalu klik tombol **Edit**.

3. **Menghapus Data**:
   - Pilih baris data yang ingin dihapus pada tabel.
   - Klik tombol **Hapus** untuk menghapus data dari database.

4. **Mencetak Data**:
   - Klik tombol **Cetak** pada bagian bawah tabel untuk mencetak data.

---

## Struktur Proyek
- **menuUtama.java**: File utama yang mengatur seluruh logika aplikasi, termasuk antarmuka pengguna dan koneksi database.
- **menuUtama.form**: File desain GUI.

---

## Catatan Tambahan
- Pastikan MySQL Server berjalan saat menggunakan aplikasi.
- Pastikan koneksi jaringan stabil jika menggunakan database pada server jarak jauh.

---

## Lisensi
Aplikasi ini dibuat untuk keperluan pembelajaran dan bersifat open-source. Anda bebas untuk memodifikasi dan menggunakan kode ini sesuai kebutuhan Anda.
