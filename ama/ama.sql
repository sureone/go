-- MySQL dump 10.13  Distrib 5.5.54, for debian-linux-gnu (x86_64)
--
-- Host: localhost    Database: ama
-- ------------------------------------------------------
-- Server version	5.5.54-0ubuntu0.14.04.1

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `datas`
--


CREATE DATABASE IF NOT EXISTS ama default charset utf8 COLLATE utf8_general_ci;

use ama;

CREATE USER IF NOT EXISTS 'ama'@'localhost' IDENTIFIED BY 'amagood123';

GRANT ALL ON ama.* TO 'ama'@'localhost'  

DROP TABLE IF EXISTS `datas`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `datas` (
  `thing_id` int(11) NOT NULL,
  `key` varchar(20) NOT NULL,
  `value` varchar(4096) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `thing_attach_map`
--

DROP TABLE IF EXISTS `thing_attach_map`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `thing_attach_map` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `thingid` int(11) DEFAULT NULL,
  `file_no` int(11) NOT NULL,
  `file_type` varchar(50) NOT NULL,
  `file_name` varchar(100) NOT NULL,
  `file_comment` varchar(200) NOT NULL,
  `file_path` varchar(200) NOT NULL,
  `file_size` int(11) NOT NULL,
  `image_width` int(11) DEFAULT NULL,
  `image_height` int(11) DEFAULT NULL,
  `userid` varchar(100) DEFAULT NULL,
  `cdate` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=149 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `things`
--

DROP TABLE IF EXISTS `things`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `things` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
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
  `readed` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=314 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user_thing_map`
--

DROP TABLE IF EXISTS `user_thing_map`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_thing_map` (
  `id` varchar(200) NOT NULL,
  `userid` varchar(100) NOT NULL,
  `thingid` bigint(20) NOT NULL,
  `maptype` varchar(100) NOT NULL,
  `idata` int(11) DEFAULT NULL,
  `sdata` varchar(200) DEFAULT NULL,
  `cdate` int(11) NOT NULL,
  `udate` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `users` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `userid` varchar(64) DEFAULT NULL,
  `name` varchar(64) NOT NULL,
  `passwd` varchar(128) NOT NULL,
  `email` varchar(128) DEFAULT NULL,
  `mobile` varchar(18) DEFAULT NULL,
  `status` varchar(24) NOT NULL DEFAULT 'normal',
  `cdate` int(11) DEFAULT NULL,
  `lastdate` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`),
  UNIQUE KEY `userid` (`userid`),
  KEY `name_2` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8 COMMENT='...';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `wx_users`
--

DROP TABLE IF EXISTS `wx_users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `wx_users` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `openId` varchar(128) NOT NULL,
  `nickName` varchar(128) NOT NULL,
  `gender` int(11) DEFAULT NULL,
  `language` varchar(128) DEFAULT NULL,
  `city` varchar(64) DEFAULT NULL,
  `province` varchar(64) DEFAULT NULL,
  `country` varchar(64) DEFAULT NULL,
  `avatarUrl` varchar(256) DEFAULT NULL,
  `cdate` int(11) DEFAULT NULL,
  `lastdate` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8 COMMENT='微信用户表';
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2017-05-09  9:07:33
