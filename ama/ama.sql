-- phpMyAdmin SQL Dump
-- version 4.1.12
-- http://www.phpmyadmin.net
--
-- Host: 127.0.0.1
-- Generation Time: 2017-03-17 08:33:24
-- 服务器版本： 5.6.16
-- PHP Version: 5.5.11

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `ama`
--

-- --------------------------------------------------------

--
-- 表的结构 `datas`
--

CREATE TABLE IF NOT EXISTS `datas` (
  `thing_id` int(11) NOT NULL,
  `key` varchar(20) NOT NULL,
  `value` varchar(4096) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- 表的结构 `things`
--

CREATE TABLE IF NOT EXISTS `things` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `ups` int(11) NOT NULL DEFAULT '0',
  `downs` int(11) NOT NULL DEFAULT '0',
  `type` varchar(12) NOT NULL,
  `cdate` int(11) NOT NULL,
  `udate` int(11) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- 表的结构 `users`
--

CREATE TABLE IF NOT EXISTS `users` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `userid` varchar(64) NOT NULL,
  `name` varchar(64) NOT NULL,
  `passwd` varchar(128) NOT NULL,
  `email` varchar(128) DEFAULT NULL,
  `mobile` varchar(18) DEFAULT NULL,
  `status` varchar(24) NOT NULL DEFAULT 'normal',
  PRIMARY KEY (`id`),
  UNIQUE KEY `userid` (`userid`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COMMENT='用户表' AUTO_INCREMENT=2 ;

--
-- 转存表中的数据 `users`
--

INSERT INTO `users` (`id`, `userid`, `name`, `passwd`, `email`, `mobile`, `status`) VALUES
(1, 'sureone', '小网', '81dc9bdb52d04dc20036dbd8313ed055', 'yang.xiaowang@wcare.cn', NULL, 'normal');

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
