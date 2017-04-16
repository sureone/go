-- phpMyAdmin SQL Dump
-- version 4.2.11
-- http://www.phpmyadmin.net
--
-- Host: 127.0.0.1
-- Generation Time: Apr 16, 2017 at 11:16 AM
-- Server version: 5.6.21
-- PHP Version: 5.6.3

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `ama`
--
CREATE DATABASE IF NOT EXISTS `ama` DEFAULT CHARACTER SET latin1 COLLATE latin1_swedish_ci;
USE `ama`;

-- --------------------------------------------------------

--
-- Table structure for table `datas`
--

DROP TABLE IF EXISTS `datas`;
CREATE TABLE IF NOT EXISTS `datas` (
  `thing_id` int(11) NOT NULL,
  `key` varchar(20) NOT NULL,
  `value` varchar(4096) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `things`
--

DROP TABLE IF EXISTS `things`;
CREATE TABLE IF NOT EXISTS `things` (
`ID` bigint(20) NOT NULL,
  `ups` int(11) NOT NULL DEFAULT '0',
  `downs` int(11) NOT NULL DEFAULT '0',
  `score` int(11) NOT NULL DEFAULT '0',
  `type` varchar(12) NOT NULL,
  `cdate` int(11) NOT NULL,
  `udate` int(11) NOT NULL,
  `title` varchar(1024) CHARACTER SET utf8 NOT NULL,
  `author` varchar(100) NOT NULL,
  `content` text CHARACTER SET utf8,
  `main` bigint(20) NOT NULL DEFAULT '0',
  `parent` bigint(20) NOT NULL DEFAULT '0',
  `replies` int(11) NOT NULL DEFAULT '0',
  `recipients` varchar(200) DEFAULT NULL,
  `stype` varchar(20) NOT NULL DEFAULT 'link',
  `readed` int(11) NOT NULL DEFAULT '0'
) ENGINE=InnoDB AUTO_INCREMENT=207 DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `user_thing_map`
--

DROP TABLE IF EXISTS `user_thing_map`;
CREATE TABLE IF NOT EXISTS `user_thing_map` (
  `id` varchar(200) NOT NULL,
  `userid` varchar(100) NOT NULL,
  `thingid` bigint(20) NOT NULL,
  `maptype` varchar(100) NOT NULL,
  `idata` int(11) DEFAULT NULL,
  `sdata` varchar(200) DEFAULT NULL,
  `cdate` int(11) NOT NULL,
  `udate` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
CREATE TABLE IF NOT EXISTS `users` (
`id` bigint(20) NOT NULL,
  `userid` varchar(64) NOT NULL,
  `name` varchar(64) NOT NULL,
  `passwd` varchar(128) NOT NULL,
  `email` varchar(128) DEFAULT NULL,
  `mobile` varchar(18) DEFAULT NULL,
  `status` varchar(24) NOT NULL DEFAULT 'normal',
  `cdate` int(11) DEFAULT NULL,
  `lastdate` int(11) DEFAULT NULL
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COMMENT='用户表';

--
-- Indexes for dumped tables
--

--
-- Indexes for table `things`
--
ALTER TABLE `things`
 ADD PRIMARY KEY (`ID`);

--
-- Indexes for table `user_thing_map`
--
ALTER TABLE `user_thing_map`
 ADD PRIMARY KEY (`id`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
 ADD PRIMARY KEY (`id`), ADD UNIQUE KEY `userid` (`userid`), ADD UNIQUE KEY `name` (`name`), ADD KEY `name_2` (`name`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `things`
--
ALTER TABLE `things`
MODIFY `ID` bigint(20) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=207;
--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=4;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
