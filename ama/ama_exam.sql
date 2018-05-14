CREATE TABLE `exam_users` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(40) NOT NULL,
  `danwei` varchar(60) NOT NULL,
  `bumen` varchar(60) NOT NULL,
  `cdate` int(11) NOT NULL, 
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
CREATE TABLE `exam_answers` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `userid` int(20) NOT NULL,
  `answers` varchar(2048) NOT NULL,
  `score` int(20) NOT NULL,
  `cdate` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
