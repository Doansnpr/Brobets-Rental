-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Waktu pembuatan: 14 Bulan Mei 2025 pada 02.54
-- Versi server: 10.4.32-MariaDB
-- Versi PHP: 8.0.30

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `brobets`
--

-- --------------------------------------------------------

--
-- Struktur dari tabel `barang`
--

CREATE TABLE `barang` (
  `id_barang` varchar(20) NOT NULL,
  `nama_barang` varchar(30) NOT NULL,
  `harga_sewa` int(11) NOT NULL,
  `kategori` varchar(20) NOT NULL,
  `stok` int(11) NOT NULL,
  `status` enum('Tersedia','Disewa','Rusak','Hilang','Maintenance') NOT NULL,
  `harga_beli` int(11) NOT NULL,
  `barcode` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `barang`
--

INSERT INTO `barang` (`id_barang`, `nama_barang`, `harga_sewa`, `kategori`, `stok`, `status`, `harga_beli`, `barcode`) VALUES
('BRG001', 'Tenda', 20000, 'Peralatan', 10, 'Tersedia', 80000, ''),
('BRG002', 'Ransel', 40000, 'Tas', 15, 'Tersedia', 30000, ''),
('BRG003', 'Kompor Kecil', 26000, 'Peralatan', 5, 'Tersedia', 50000, '');

-- --------------------------------------------------------

--
-- Struktur dari tabel `detail_pengembalian`
--

CREATE TABLE `detail_pengembalian` (
  `id_detail_kembali` varchar(20) NOT NULL,
  `id_pengembalian` varchar(20) NOT NULL,
  `id_barang` varchar(20) NOT NULL,
  `jumlah` int(10) NOT NULL,
  `kondisi` enum('Baik','Rusak','Hilang','') NOT NULL,
  `denda_barang` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `detail_pengembalian`
--

INSERT INTO `detail_pengembalian` (`id_detail_kembali`, `id_pengembalian`, `id_barang`, `jumlah`, `kondisi`, `denda_barang`) VALUES
('DTP001', 'PM001', 'BRG001', 1, 'Rusak', 80000),
('DTP002', 'PM004', 'BRG001', 2, 'Baik', 0),
('DTP003', 'PM005', 'BRG002', 1, 'Baik', 0),
('DTP004', 'PM006', 'BRG002', 1, 'Baik', 0),
('DTP005', 'PM007', 'BRG001', 1, 'Baik', 0);

-- --------------------------------------------------------

--
-- Struktur dari tabel `detail_sewa`
--

CREATE TABLE `detail_sewa` (
  `id_detail` varchar(20) NOT NULL,
  `id_sewa` varchar(20) NOT NULL,
  `id_barang` varchar(20) NOT NULL,
  `qty` int(11) NOT NULL,
  `sub_total` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `detail_sewa`
--

INSERT INTO `detail_sewa` (`id_detail`, `id_sewa`, `id_barang`, `qty`, `sub_total`) VALUES
('DTS001', 'PN001', 'BRG001', 1, 20000),
('DTS002', 'PN002', 'BRG001', 1, 20000),
('DTS003', 'PN002', 'BRG002', 6, 240000),
('DTS004', 'PN002', 'BRG003', 3, 150000),
('DTS005', 'PN004', 'BRG001', 2, 40000),
('DTS006', 'PN005', 'BRG002', 1, 40000),
('DTS007', 'PN006', 'BRG001', 1, 20000);

-- --------------------------------------------------------

--
-- Struktur dari tabel `pelanggan`
--

CREATE TABLE `pelanggan` (
  `id_pelanggan` varchar(20) NOT NULL,
  `nama_pelanggan` varchar(50) NOT NULL,
  `no_hp` varchar(13) NOT NULL,
  `poin` int(11) NOT NULL,
  `status` enum('aktif','nonaktif') DEFAULT 'aktif',
  `status_reward` varchar(20) DEFAULT 'tidak tersedia'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `pelanggan`
--

INSERT INTO `pelanggan` (`id_pelanggan`, `nama_pelanggan`, `no_hp`, `poin`, `status`, `status_reward`) VALUES
('PL001', 'Bintang', '9876543', 1, 'aktif', 'tidak tersedia'),
('PL008', 'Risky', '09876543', 1, 'aktif', 'tidak tersedia'),
('PL009', 'Risy', '09876543', 4, 'aktif', 'tidak tersedia');

-- --------------------------------------------------------

--
-- Struktur dari tabel `pemasok`
--

CREATE TABLE `pemasok` (
  `id_pemasok` varchar(25) NOT NULL,
  `nama_pemasok` varchar(25) NOT NULL,
  `no_telp` varchar(13) NOT NULL,
  `alamat` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Struktur dari tabel `pengembalian`
--

CREATE TABLE `pengembalian` (
  `id_kembali` varchar(20) NOT NULL,
  `id_sewa` varchar(20) NOT NULL,
  `tgl_kembali` date NOT NULL,
  `status` varchar(20) DEFAULT NULL,
  `denda_keterlambatan` int(11) DEFAULT NULL,
  `total_denda` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `pengembalian`
--

INSERT INTO `pengembalian` (`id_kembali`, `id_sewa`, `tgl_kembali`, `status`, `denda_keterlambatan`, `total_denda`) VALUES
('PM001', 'PN001', '2025-05-07', 'Terlambat', 260000, 340000),
('PM002', 'PN004', '2025-05-14', 'Terlambat', 30000, 30000),
('PM003', 'PN004', '2025-05-14', 'Terlambat', 30000, 30000),
('PM004', 'PN004', '2025-05-14', 'Terlambat', 30000, 30000),
('PM005', 'PN005', '2025-05-14', 'Terlambat', 30000, 30000),
('PM006', 'PN005', '2025-05-14', 'Terlambat', 30000, 30000),
('PM007', 'PN006', '2025-05-14', 'Tepat Waktu', 0, 0);

-- --------------------------------------------------------

--
-- Struktur dari tabel `pengguna`
--

CREATE TABLE `pengguna` (
  `id_pengguna` varchar(5) NOT NULL,
  `nama_lengkap` varchar(50) NOT NULL,
  `email` varchar(25) NOT NULL,
  `nama_pengguna` varchar(20) NOT NULL,
  `password` varchar(8) NOT NULL,
  `no_hp` varchar(13) NOT NULL,
  `alamat` text NOT NULL,
  `role` enum('Admin','Pegawai') NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `pengguna`
--

INSERT INTO `pengguna` (`id_pengguna`, `nama_lengkap`, `email`, `nama_pengguna`, `password`, `no_hp`, `alamat`, `role`) VALUES
('UR001', 'doan', 'doansri@gmail.com', 'doan', 'doan', '98765432', 'Bodo', 'Pegawai');

-- --------------------------------------------------------

--
-- Struktur dari tabel `penyewaan`
--

CREATE TABLE `penyewaan` (
  `id_sewa` varchar(20) NOT NULL,
  `id_pelanggan` varchar(20) NOT NULL,
  `id_pengguna` varchar(5) NOT NULL,
  `tgl_sewa` date NOT NULL,
  `tgl_rencana_kembali` date NOT NULL,
  `total_harga` int(11) NOT NULL,
  `bayar` int(11) NOT NULL,
  `kembalian` int(11) NOT NULL,
  `jaminan` enum('KTP','SIM','KTM','FC KK') NOT NULL,
  `Status` varchar(25) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `penyewaan`
--

INSERT INTO `penyewaan` (`id_sewa`, `id_pelanggan`, `id_pengguna`, `tgl_sewa`, `tgl_rencana_kembali`, `total_harga`, `bayar`, `kembalian`, `jaminan`, `Status`) VALUES
('PN001', 'PL001', 'UR001', '2025-04-10', '2025-04-11', 20000, 20000, 0, 'KTM', 'Sudah Kembali'),
('PN002', 'PL001', 'UR001', '2025-05-07', '2025-05-10', 410000, 420000, 10000, 'KTP', 'Sudah Kembali'),
('PN004', 'PL009', 'UR001', '2025-05-08', '2025-05-11', 40000, 40000, 0, 'KTM', 'Sudah Kembali'),
('PN005', 'PL009', 'UR001', '2025-05-09', '2025-05-11', 40000, 50000, 10000, 'FC KK', 'Sudah Kembali'),
('PN006', 'PL009', 'UR001', '2025-05-14', '2025-05-16', 20000, 20000, 0, 'SIM', 'Sudah Kembali');

-- --------------------------------------------------------

--
-- Struktur dari tabel `stok_masuk`
--

CREATE TABLE `stok_masuk` (
  `id_stok_masuk` varchar(20) NOT NULL,
  `id_barang` varchar(20) NOT NULL,
  `kategori` varchar(20) NOT NULL,
  `qty` int(11) NOT NULL,
  `harga` int(11) NOT NULL,
  `id_pemasok` varchar(25) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Indexes for dumped tables
--

--
-- Indeks untuk tabel `barang`
--
ALTER TABLE `barang`
  ADD PRIMARY KEY (`id_barang`);

--
-- Indeks untuk tabel `detail_pengembalian`
--
ALTER TABLE `detail_pengembalian`
  ADD PRIMARY KEY (`id_detail_kembali`),
  ADD KEY `id_pengembalian` (`id_pengembalian`),
  ADD KEY `id_barang` (`id_barang`);

--
-- Indeks untuk tabel `detail_sewa`
--
ALTER TABLE `detail_sewa`
  ADD PRIMARY KEY (`id_detail`),
  ADD KEY `item_id` (`id_barang`),
  ADD KEY `sewa_id` (`id_sewa`);

--
-- Indeks untuk tabel `pelanggan`
--
ALTER TABLE `pelanggan`
  ADD PRIMARY KEY (`id_pelanggan`);

--
-- Indeks untuk tabel `pemasok`
--
ALTER TABLE `pemasok`
  ADD PRIMARY KEY (`id_pemasok`);

--
-- Indeks untuk tabel `pengembalian`
--
ALTER TABLE `pengembalian`
  ADD PRIMARY KEY (`id_kembali`),
  ADD KEY `sewa_id` (`id_sewa`);

--
-- Indeks untuk tabel `pengguna`
--
ALTER TABLE `pengguna`
  ADD PRIMARY KEY (`id_pengguna`);

--
-- Indeks untuk tabel `penyewaan`
--
ALTER TABLE `penyewaan`
  ADD PRIMARY KEY (`id_sewa`),
  ADD KEY `customer_id` (`id_pelanggan`),
  ADD KEY `user_id` (`id_pengguna`);

--
-- Indeks untuk tabel `stok_masuk`
--
ALTER TABLE `stok_masuk`
  ADD PRIMARY KEY (`id_stok_masuk`),
  ADD KEY `id_barang` (`id_barang`),
  ADD KEY `id_pemasok` (`id_pemasok`);

--
-- Ketidakleluasaan untuk tabel pelimpahan (Dumped Tables)
--

--
-- Ketidakleluasaan untuk tabel `detail_pengembalian`
--
ALTER TABLE `detail_pengembalian`
  ADD CONSTRAINT `join_barang` FOREIGN KEY (`id_barang`) REFERENCES `barang` (`id_barang`),
  ADD CONSTRAINT `join_pengembalian` FOREIGN KEY (`id_pengembalian`) REFERENCES `pengembalian` (`id_kembali`);

--
-- Ketidakleluasaan untuk tabel `detail_sewa`
--
ALTER TABLE `detail_sewa`
  ADD CONSTRAINT `detail_sewa_ibfk_1` FOREIGN KEY (`id_barang`) REFERENCES `barang` (`id_barang`),
  ADD CONSTRAINT `detail_sewa_ibfk_2` FOREIGN KEY (`id_sewa`) REFERENCES `penyewaan` (`id_sewa`);

--
-- Ketidakleluasaan untuk tabel `pemasok`
--
ALTER TABLE `pemasok`
  ADD CONSTRAINT `join_stok_masuk` FOREIGN KEY (`id_pemasok`) REFERENCES `stok_masuk` (`id_pemasok`);

--
-- Ketidakleluasaan untuk tabel `pengembalian`
--
ALTER TABLE `pengembalian`
  ADD CONSTRAINT `pengembalian_ibfk_1` FOREIGN KEY (`id_sewa`) REFERENCES `penyewaan` (`id_sewa`);

--
-- Ketidakleluasaan untuk tabel `penyewaan`
--
ALTER TABLE `penyewaan`
  ADD CONSTRAINT `penyewaan_ibfk_1` FOREIGN KEY (`id_pelanggan`) REFERENCES `pelanggan` (`id_pelanggan`),
  ADD CONSTRAINT `penyewaan_ibfk_2` FOREIGN KEY (`id_pengguna`) REFERENCES `pengguna` (`id_pengguna`);

--
-- Ketidakleluasaan untuk tabel `stok_masuk`
--
ALTER TABLE `stok_masuk`
  ADD CONSTRAINT `stok_masuk_ibfk_1` FOREIGN KEY (`id_barang`) REFERENCES `barang` (`id_barang`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
