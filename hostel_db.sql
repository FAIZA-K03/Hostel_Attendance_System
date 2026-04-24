-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Apr 24, 2026 at 11:14 AM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `hostel_db`
--

-- --------------------------------------------------------

--
-- Table structure for table `attendance`
--

CREATE TABLE `attendance` (
  `attendance_id` int(11) NOT NULL,
  `student_id` int(11) DEFAULT NULL,
  `date` date DEFAULT NULL,
  `status` varchar(20) DEFAULT NULL,
  `marked_by` int(11) DEFAULT NULL,
  `marked_at` timestamp NOT NULL DEFAULT current_timestamp(),
  `is_locked` tinyint(1) DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `attendance`
--

INSERT INTO `attendance` (`attendance_id`, `student_id`, `date`, `status`, `marked_by`, `marked_at`, `is_locked`) VALUES
(1, 1, '2026-04-21', 'Present', 2, '2026-04-21 14:28:19', 0),
(5, 2, '2026-04-21', 'Present', 2, '2026-04-21 14:28:19', 0),
(6, 3, '2026-04-21', 'Present', 2, '2026-04-21 14:28:19', 0),
(10, 6, '2026-04-21', 'Present', 2, '2026-04-21 14:28:19', 0),
(11, 8, '2026-04-21', 'Present', 1, '2026-04-21 17:43:02', 0),
(12, 4, '2026-04-21', 'Present', 1, '2026-04-21 17:43:02', 0),
(14, 5, '2026-04-21', 'Present', 3, '2026-04-21 17:01:19', 0),
(39, 10, '2026-04-21', 'Present', 1, '2026-04-21 17:43:02', 0),
(42, 5, '2026-04-22', 'Present', 3, '2026-04-21 20:41:34', 0),
(43, 13, '2026-04-22', 'Present', 3, '2026-04-21 20:41:34', 0),
(44, 14, '2026-04-22', 'Present', 3, '2026-04-21 20:41:34', 0),
(50, 10, '2026-04-22', 'Present', 1, '2026-04-21 20:41:01', 0),
(51, 8, '2026-04-22', 'Present', 1, '2026-04-21 20:41:01', 0),
(52, 4, '2026-04-22', 'Present', 1, '2026-04-21 20:41:01', 0),
(53, 16, '2026-04-22', 'Present', 1, '2026-04-21 20:41:01', 0),
(54, 20, '2026-04-22', 'Present', 3, '2026-04-21 20:41:34', 0),
(59, 23, '2026-04-22', 'Present', 1, '2026-04-21 20:41:01', 0),
(63, 24, '2026-04-22', 'Present', 1, '2026-04-21 20:41:01', 0),
(64, 25, '2026-04-22', 'Present', 1, '2026-04-21 20:41:01', 0),
(65, 26, '2026-04-22', 'Present', 1, '2026-04-21 20:41:01', 0),
(70, 27, '2026-04-22', 'Present', 3, '2026-04-21 20:41:34', 0),
(71, 28, '2026-04-22', 'Present', 3, '2026-04-21 20:41:34', 0),
(72, 29, '2026-04-22', 'Present', 3, '2026-04-21 20:41:34', 0),
(157, 10, '2026-04-24', 'Present', 1, '2026-04-24 08:28:54', 0),
(158, 23, '2026-04-24', 'Present', 1, '2026-04-24 08:28:54', 0),
(159, 8, '2026-04-24', 'Present', 1, '2026-04-24 08:28:54', 0),
(160, 4, '2026-04-24', 'Present', 1, '2026-04-24 08:28:54', 0),
(161, 16, '2026-04-24', 'Present', 1, '2026-04-24 08:28:54', 0),
(162, 24, '2026-04-24', 'Present', 1, '2026-04-24 08:28:54', 0),
(163, 25, '2026-04-24', 'Present', 1, '2026-04-24 08:28:54', 0),
(164, 26, '2026-04-24', 'Present', 1, '2026-04-24 08:28:54', 0),
(165, 20, '2026-04-24', 'Present', 3, '2026-04-24 08:17:30', 0),
(166, 5, '2026-04-24', 'Present', 3, '2026-04-24 08:17:30', 0),
(167, 13, '2026-04-24', 'Present', 3, '2026-04-24 08:17:30', 0),
(168, 14, '2026-04-24', 'Present', 3, '2026-04-24 08:17:30', 0),
(169, 27, '2026-04-24', 'Present', 3, '2026-04-24 08:17:30', 0),
(170, 28, '2026-04-24', 'Present', 3, '2026-04-24 08:17:30', 0),
(171, 29, '2026-04-24', 'Present', 3, '2026-04-24 08:17:30', 0);

-- --------------------------------------------------------

--
-- Table structure for table `block`
--

CREATE TABLE `block` (
  `block_id` int(11) NOT NULL,
  `block_no` int(11) NOT NULL,
  `block_name` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `block`
--

INSERT INTO `block` (`block_id`, `block_no`, `block_name`) VALUES
(1, 1, 'Block I'),
(2, 2, 'Block II');

-- --------------------------------------------------------

--
-- Table structure for table `category`
--

CREATE TABLE `category` (
  `category_id` int(11) NOT NULL,
  `category_name` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `category`
--

INSERT INTO `category` (`category_id`, `category_name`) VALUES
(1, 'PhD'),
(2, 'MBBS'),
(3, 'DBS'),
(4, 'Masters'),
(5, 'UG');

-- --------------------------------------------------------

--
-- Table structure for table `floor`
--

CREATE TABLE `floor` (
  `floor_id` int(11) NOT NULL,
  `floor_name` varchar(50) NOT NULL,
  `hostel_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `floor`
--

INSERT INTO `floor` (`floor_id`, `floor_name`, `hostel_id`) VALUES
(1, 'Ground Floor', 1),
(2, 'First Floor', 2),
(3, 'Second Floor', 2),
(6, 'First Floor', 1),
(7, 'Floor 1', 1),
(8, 'Floor 2', 2),
(9, 'Floor 1', 3);

-- --------------------------------------------------------

--
-- Table structure for table `headgirl`
--

CREATE TABLE `headgirl` (
  `headgirl_id` int(11) NOT NULL,
  `name` varchar(100) NOT NULL,
  `username` varchar(50) DEFAULT NULL,
  `password` varchar(50) DEFAULT NULL,
  `floor_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `headgirl`
--

INSERT INTO `headgirl` (`headgirl_id`, `name`, `username`, `password`, `floor_id`) VALUES
(1, 'Sumaniya Sheikh', 'Sumaniya', 'sumu123', 2),
(2, 'Rashika', 'Rashika', 'rashika123', 1),
(3, 'Faiza', 'Faiza', 'faiza123', 3),
(5, 'Hadiya', 'Hadiya', 'hadiya123', 2),
(7, 'Sumaniya', 'Sumu', 'sumu123', 1);

-- --------------------------------------------------------

--
-- Table structure for table `hostel`
--

CREATE TABLE `hostel` (
  `hostel_id` int(11) NOT NULL,
  `hostel_name` varchar(150) NOT NULL,
  `block_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `hostel`
--

INSERT INTO `hostel` (`hostel_id`, `hostel_name`, `block_id`) VALUES
(1, 'Sikandar Jahan Begum Hostel', 1),
(2, 'Hamida Habibullah Hostel', 1),
(3, 'Kadambini Ganguly Hostel', 1),
(4, 'Begum Hazrat Mahal Hostel', 2),
(5, 'Annie Besant Hostel', 2),
(6, 'Madam Cama Hostel', 2);

-- --------------------------------------------------------

--
-- Table structure for table `room`
--

CREATE TABLE `room` (
  `room_id` int(11) NOT NULL,
  `room_number` varchar(10) NOT NULL,
  `floor_id` int(11) DEFAULT NULL,
  `capacity` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `room`
--

INSERT INTO `room` (`room_id`, `room_number`, `floor_id`, `capacity`) VALUES
(1, '01', 1, 3),
(2, '02', 1, 3),
(3, '103', 2, 3),
(4, '102', 2, 3),
(5, '283', 3, 4),
(6, '284', 3, 4),
(7, '03', 1, 3),
(8, '04', 1, NULL),
(9, '113', 2, NULL),
(10, '05', 1, NULL),
(11, '06', 1, NULL),
(12, '112', 2, NULL),
(13, '290', 3, NULL),
(14, '290', 3, NULL),
(15, '203', 3, NULL),
(20, '101', 1, 2),
(21, '102', 1, 2),
(23, '103', 1, 2),
(24, '104', 1, 2),
(25, '105', 1, 2),
(26, '106', 1, 2),
(27, '107', 1, 2),
(28, '108', 1, 2),
(29, '109', 1, 2),
(30, '101', 1, 2),
(33, '201', 2, 2),
(34, '202', 2, 2),
(36, '201', 2, 2),
(37, '202', 2, 2),
(38, '203', 2, 2),
(39, '204', 2, 2),
(40, '205', 2, 2),
(41, '201', 2, 2),
(46, '301', 3, 2),
(47, '302', 3, 2),
(48, '303', 3, 2),
(49, '304', 3, 2),
(50, '305', 3, 2),
(51, '306', 3, 2),
(52, '307', 3, 2),
(53, '308', 3, 2),
(54, '309', 3, 2),
(55, '310', 3, 2),
(56, '201', 2, 2),
(57, '202', 2, 2),
(58, '203', 2, 2),
(59, '204', 2, 2),
(60, '205', 2, 2),
(61, '206', 2, 2),
(62, '207', 2, 2),
(63, '208', 2, 2),
(64, '209', 2, 2),
(65, '210', 2, 2),
(66, '301', 3, 3),
(67, '302', 3, 3),
(68, '303', 3, 3),
(69, '304', 3, 3),
(70, '305', 3, 4),
(71, '306', 3, 4),
(72, '307', 3, 4),
(73, '308', 3, 4),
(74, '309', 3, 4),
(75, '310', 3, 4);

-- --------------------------------------------------------

--
-- Table structure for table `student`
--

CREATE TABLE `student` (
  `student_id` int(11) NOT NULL,
  `name` varchar(100) NOT NULL,
  `enrollment_no` varchar(50) NOT NULL,
  `roll_no` varchar(50) DEFAULT NULL,
  `category_id` int(11) DEFAULT NULL,
  `room_id` int(11) DEFAULT NULL,
  `contact` varchar(15) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `student`
--

INSERT INTO `student` (`student_id`, `name`, `enrollment_no`, `roll_no`, `category_id`, `room_id`, `contact`) VALUES
(1, 'Ifa Fatima', 'EN001', '25CAMSA223', 5, 1, '9000000001'),
(2, 'Sumbul Javed', 'EN002', '25CAMSA221', 5, 1, '9000000002'),
(3, 'Neha Singh', 'EN003', '25CAMSA453', 4, 2, '9000000003'),
(4, 'Sneha Reddy', 'EN004', '25CAMSA123', 5, 3, '9000000004'),
(5, 'Meera Iyer', 'EN005', '25CAMSA344', 1, 5, '9000000005'),
(6, 'sumaniya', 'GR0960', '25CAMSA144', 1, 1, '8707256827'),
(8, 'Rashika Zehra', 'GR0393', '25CAMSA133', 4, 3, '852t234873'),
(10, 'iqra', 'GR0944', NULL, 4, 3, NULL),
(13, 'Ghazala', 'GR0332', NULL, 4, 14, NULL),
(14, 'Hadiya', 'GL0332', NULL, 4, 14, NULL),
(16, 'Zoya', 'GL4321', NULL, 1, 9, NULL),
(20, 'zoya', 'GR0876', NULL, 2, 15, NULL),
(21, 'Aisha Khan', 'GR1001', '01', 1, 1, '9876543210'),
(22, 'Sara Ali', 'GR1002', '02', 1, 2, '9876543211'),
(23, 'Neha Sharma', 'GR1003', '03', 1, 3, '9876543212'),
(24, 'Fatima Noor', 'GR2001', '101', 1, 36, '9876543213'),
(25, 'Hina Malik', 'GR2002', '102', 1, 37, '9876543214'),
(26, 'Zoya Khan', 'GR2003', '103', 1, 38, '9876543215'),
(27, 'Ayesha Siddiqui', 'GR3001', '201', 1, 46, '9876543216'),
(28, 'Mehak Ali', 'GR3002', '202', 1, 47, '9876543217'),
(29, 'Sana Sheikh', 'GR3003', '203', 1, 48, '9876543218');

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `user_id` int(11) NOT NULL,
  `username` varchar(50) DEFAULT NULL,
  `password` varchar(50) DEFAULT NULL,
  `role` varchar(20) DEFAULT NULL,
  `floor_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`user_id`, `username`, `password`, `role`, `floor_id`) VALUES
(1, 'admin1', 'admin1', 'warden', 1),
(2, 'hg1', 'hg1', 'headgirl', 1),
(3, 'admin2', 'admin2', 'warden', 2),
(4, 'hg2', 'hg2', 'headgirl', 2),
(5, 'admin3', 'admin3', 'warden', 3),
(6, 'hg3', 'hg3', 'headgirl', 3);

-- --------------------------------------------------------

--
-- Table structure for table `warden`
--

CREATE TABLE `warden` (
  `warden_id` int(11) NOT NULL,
  `username` varchar(50) DEFAULT NULL,
  `password` varchar(50) DEFAULT NULL,
  `name` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `warden`
--

INSERT INTO `warden` (`warden_id`, `username`, `password`, `name`) VALUES
(1, 'admin', 'admin', ''),
(2, 'warden', 'warden123', '');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `attendance`
--
ALTER TABLE `attendance`
  ADD PRIMARY KEY (`attendance_id`),
  ADD UNIQUE KEY `uniq_student_date` (`student_id`,`date`),
  ADD KEY `marked_by` (`marked_by`);

--
-- Indexes for table `block`
--
ALTER TABLE `block`
  ADD PRIMARY KEY (`block_id`);

--
-- Indexes for table `category`
--
ALTER TABLE `category`
  ADD PRIMARY KEY (`category_id`);

--
-- Indexes for table `floor`
--
ALTER TABLE `floor`
  ADD PRIMARY KEY (`floor_id`),
  ADD KEY `hostel_id` (`hostel_id`);

--
-- Indexes for table `headgirl`
--
ALTER TABLE `headgirl`
  ADD PRIMARY KEY (`headgirl_id`),
  ADD UNIQUE KEY `username` (`username`),
  ADD KEY `floor_id` (`floor_id`);

--
-- Indexes for table `hostel`
--
ALTER TABLE `hostel`
  ADD PRIMARY KEY (`hostel_id`),
  ADD KEY `block_id` (`block_id`);

--
-- Indexes for table `room`
--
ALTER TABLE `room`
  ADD PRIMARY KEY (`room_id`),
  ADD KEY `floor_id` (`floor_id`);

--
-- Indexes for table `student`
--
ALTER TABLE `student`
  ADD PRIMARY KEY (`student_id`),
  ADD UNIQUE KEY `enrollment_no` (`enrollment_no`),
  ADD KEY `category_id` (`category_id`),
  ADD KEY `room_id` (`room_id`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`user_id`);

--
-- Indexes for table `warden`
--
ALTER TABLE `warden`
  ADD PRIMARY KEY (`warden_id`),
  ADD UNIQUE KEY `username` (`username`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `attendance`
--
ALTER TABLE `attendance`
  MODIFY `attendance_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=222;

--
-- AUTO_INCREMENT for table `block`
--
ALTER TABLE `block`
  MODIFY `block_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `category`
--
ALTER TABLE `category`
  MODIFY `category_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=12;

--
-- AUTO_INCREMENT for table `floor`
--
ALTER TABLE `floor`
  MODIFY `floor_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- AUTO_INCREMENT for table `headgirl`
--
ALTER TABLE `headgirl`
  MODIFY `headgirl_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT for table `hostel`
--
ALTER TABLE `hostel`
  MODIFY `hostel_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT for table `room`
--
ALTER TABLE `room`
  MODIFY `room_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=76;

--
-- AUTO_INCREMENT for table `student`
--
ALTER TABLE `student`
  MODIFY `student_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=30;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `user_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT for table `warden`
--
ALTER TABLE `warden`
  MODIFY `warden_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `attendance`
--
ALTER TABLE `attendance`
  ADD CONSTRAINT `attendance_ibfk_1` FOREIGN KEY (`student_id`) REFERENCES `student` (`student_id`),
  ADD CONSTRAINT `attendance_ibfk_2` FOREIGN KEY (`marked_by`) REFERENCES `headgirl` (`headgirl_id`);

--
-- Constraints for table `floor`
--
ALTER TABLE `floor`
  ADD CONSTRAINT `floor_ibfk_1` FOREIGN KEY (`hostel_id`) REFERENCES `hostel` (`hostel_id`);

--
-- Constraints for table `headgirl`
--
ALTER TABLE `headgirl`
  ADD CONSTRAINT `headgirl_ibfk_1` FOREIGN KEY (`floor_id`) REFERENCES `floor` (`floor_id`);

--
-- Constraints for table `hostel`
--
ALTER TABLE `hostel`
  ADD CONSTRAINT `hostel_ibfk_1` FOREIGN KEY (`block_id`) REFERENCES `block` (`block_id`);

--
-- Constraints for table `room`
--
ALTER TABLE `room`
  ADD CONSTRAINT `room_ibfk_1` FOREIGN KEY (`floor_id`) REFERENCES `floor` (`floor_id`);

--
-- Constraints for table `student`
--
ALTER TABLE `student`
  ADD CONSTRAINT `student_ibfk_1` FOREIGN KEY (`category_id`) REFERENCES `category` (`category_id`),
  ADD CONSTRAINT `student_ibfk_2` FOREIGN KEY (`room_id`) REFERENCES `room` (`room_id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
