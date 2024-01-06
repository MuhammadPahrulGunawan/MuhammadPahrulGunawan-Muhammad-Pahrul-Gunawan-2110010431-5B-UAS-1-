-- phpMyAdmin SQL Dump
-- version 5.0.2
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Waktu pembuatan: 06 Jan 2024 pada 14.36
-- Versi server: 10.4.11-MariaDB
-- Versi PHP: 7.4.4

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `toko_buku`
--

-- --------------------------------------------------------

--
-- Struktur dari tabel `buku`
--

CREATE TABLE `buku` (
  `id` int(11) NOT NULL,
  `gambar` varchar(25) NOT NULL,
  `judul` varchar(30) NOT NULL,
  `noisbn` varchar(20) NOT NULL,
  `penulis` varchar(25) NOT NULL,
  `penerbit` varchar(30) NOT NULL,
  `tahun` year(4) NOT NULL,
  `stok` int(4) NOT NULL,
  `harga_pokok` int(7) NOT NULL,
  `harga_jual` int(7) NOT NULL,
  `ppn` int(2) NOT NULL,
  `diskon` int(4) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data untuk tabel `buku`
--

INSERT INTO `buku` (`id`, `gambar`, `judul`, `noisbn`, `penulis`, `penerbit`, `tahun`, `stok`, `harga_pokok`, `harga_jual`, `ppn`, `diskon`) VALUES
(12, 'BUKU BUMI MANUSIA.jpg', 'Bumi Manusia', '890988', 'Auliya', 'PT. Kencana', 2005, -31, 100000, 98880, 3, 4),
(13, 'BUKU LASKAR PELANGI.jpg', 'Laskar Pelangi', '088998', 'Gondel', 'CV. Merah', 2001, 395, 200000, 189000, 5, 10),
(15, 'BUKU PEMROGRAMAN JAVA.jpg', 'Pemrograman Java', '120989012', 'Al Fatih', 'PT. Abraham', 2023, 722, 150000, 135000, 0, 10);

-- --------------------------------------------------------

--
-- Struktur dari tabel `distributor`
--

CREATE TABLE `distributor` (
  `id` int(11) NOT NULL,
  `nama_distributor` varchar(30) NOT NULL,
  `alamat` text NOT NULL,
  `telepon` varchar(13) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data untuk tabel `distributor`
--

INSERT INTO `distributor` (`id`, `nama_distributor`, `alamat`, `telepon`) VALUES
(11, 'PT. Murah Abadi', 'Jl. Nusa Indah II', '08219088761'),
(12, 'CV. Kecana Yo', 'Jl. Pangeran Antasari', '08578909812'),
(14, 'FTI', 'Jl. Aburawa', '0988099009121');

-- --------------------------------------------------------

--
-- Struktur dari tabel `kasir`
--

CREATE TABLE `kasir` (
  `id` int(11) NOT NULL,
  `username` varchar(15) NOT NULL,
  `password` varchar(20) NOT NULL,
  `nama` varchar(30) NOT NULL,
  `alamat` text NOT NULL,
  `hakakses` enum('Owner','Kasir') NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data untuk tabel `kasir`
--

INSERT INTO `kasir` (`id`, `username`, `password`, `nama`, `alamat`, `hakakses`) VALUES
(1, 'Pahrul25', 'Gondel123', 'Pahrul', 'Abuya Jalur III', 'Kasir'),
(2, 'Pahrul', 'Gondel123', 'Muhammad Pahrul Gunawan', 'Jl.Abuya Jalur III', 'Owner');

-- --------------------------------------------------------

--
-- Struktur dari tabel `pasok`
--

CREATE TABLE `pasok` (
  `id` int(11) NOT NULL,
  `id_distributor` int(11) NOT NULL,
  `id_buku` int(11) NOT NULL,
  `jumlah` int(4) NOT NULL,
  `tanggal` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data untuk tabel `pasok`
--

INSERT INTO `pasok` (`id`, `id_distributor`, `id_buku`, `jumlah`, `tanggal`) VALUES
(16, 11, 12, 100, '2024-01-01'),
(17, 12, 13, 50, '2024-01-23');

-- --------------------------------------------------------

--
-- Struktur dari tabel `penjualan`
--

CREATE TABLE `penjualan` (
  `id` int(11) NOT NULL,
  `nota` varchar(11) NOT NULL,
  `id_kasir` int(11) NOT NULL,
  `tanggal` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data untuk tabel `penjualan`
--

INSERT INTO `penjualan` (`id`, `nota`, `id_kasir`, `tanggal`) VALUES
(16, 'TB0001', 1, '2024-01-05'),
(17, 'TB0002', 1, '2024-01-05'),
(18, 'TB0003', 1, '2024-01-05'),
(19, 'TB0004', 1, '2024-01-06');

-- --------------------------------------------------------

--
-- Struktur dari tabel `penjualan_detail`
--

CREATE TABLE `penjualan_detail` (
  `id` int(11) NOT NULL,
  `id_penjualan` int(11) NOT NULL,
  `id_buku` int(11) NOT NULL,
  `qty` int(4) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data untuk tabel `penjualan_detail`
--

INSERT INTO `penjualan_detail` (`id`, `id_penjualan`, `id_buku`, `qty`) VALUES
(17, 16, 12, 1),
(18, 17, 13, 1),
(19, 18, 12, 1),
(20, 19, 15, 1);

--
-- Indexes for dumped tables
--

--
-- Indeks untuk tabel `buku`
--
ALTER TABLE `buku`
  ADD PRIMARY KEY (`id`);

--
-- Indeks untuk tabel `distributor`
--
ALTER TABLE `distributor`
  ADD PRIMARY KEY (`id`);

--
-- Indeks untuk tabel `kasir`
--
ALTER TABLE `kasir`
  ADD PRIMARY KEY (`id`);

--
-- Indeks untuk tabel `pasok`
--
ALTER TABLE `pasok`
  ADD PRIMARY KEY (`id`),
  ADD KEY `relasi_pasok` (`id_buku`),
  ADD KEY `relasi_pasok1` (`id_distributor`);

--
-- Indeks untuk tabel `penjualan`
--
ALTER TABLE `penjualan`
  ADD PRIMARY KEY (`id`),
  ADD KEY `relasi_penjualan` (`id_kasir`);

--
-- Indeks untuk tabel `penjualan_detail`
--
ALTER TABLE `penjualan_detail`
  ADD PRIMARY KEY (`id`),
  ADD KEY `relasi_penjualandetail` (`id_buku`),
  ADD KEY `relasi_penjualandetail1` (`id_penjualan`);

--
-- AUTO_INCREMENT untuk tabel yang dibuang
--

--
-- AUTO_INCREMENT untuk tabel `buku`
--
ALTER TABLE `buku`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=16;

--
-- AUTO_INCREMENT untuk tabel `distributor`
--
ALTER TABLE `distributor`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=15;

--
-- AUTO_INCREMENT untuk tabel `kasir`
--
ALTER TABLE `kasir`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- AUTO_INCREMENT untuk tabel `pasok`
--
ALTER TABLE `pasok`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=19;

--
-- AUTO_INCREMENT untuk tabel `penjualan`
--
ALTER TABLE `penjualan`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=20;

--
-- AUTO_INCREMENT untuk tabel `penjualan_detail`
--
ALTER TABLE `penjualan_detail`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=21;

--
-- Ketidakleluasaan untuk tabel pelimpahan (Dumped Tables)
--

--
-- Ketidakleluasaan untuk tabel `pasok`
--
ALTER TABLE `pasok`
  ADD CONSTRAINT `relasi_pasok` FOREIGN KEY (`id_buku`) REFERENCES `buku` (`id`),
  ADD CONSTRAINT `relasi_pasok1` FOREIGN KEY (`id_distributor`) REFERENCES `distributor` (`id`);

--
-- Ketidakleluasaan untuk tabel `penjualan`
--
ALTER TABLE `penjualan`
  ADD CONSTRAINT `relasi_penjualan` FOREIGN KEY (`id_kasir`) REFERENCES `kasir` (`id`);

--
-- Ketidakleluasaan untuk tabel `penjualan_detail`
--
ALTER TABLE `penjualan_detail`
  ADD CONSTRAINT `relasi_penjualandetail` FOREIGN KEY (`id_buku`) REFERENCES `buku` (`id`),
  ADD CONSTRAINT `relasi_penjualandetail1` FOREIGN KEY (`id_penjualan`) REFERENCES `penjualan` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
