/*
SQLyog Ultimate v11.27 (32 bit)
MySQL - 5.6.21-log : Database - nyulink
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`nyulink` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `nyulink`;

/*Table structure for table `kactivityusers` */

DROP TABLE IF EXISTS `kactivityusers`;

CREATE TABLE `kactivityusers` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `AID` int(11) NOT NULL,
  `UID` int(11) NOT NULL,
  `CREATETIME` varchar(50) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

/*Data for the table `kactivityusers` */

/*Table structure for table `kchild` */

DROP TABLE IF EXISTS `kchild`;

CREATE TABLE `kchild` (
  `CHILDID` int(11) NOT NULL AUTO_INCREMENT,
  `USERID` int(11) NOT NULL,
  `CNAME` varchar(50) NOT NULL,
  `CHEAD` varchar(50) DEFAULT NULL,
  `CAGE` varchar(50) DEFAULT NULL,
  `CHOBBIES` varchar(50) DEFAULT NULL,
  `CDETAIL` varchar(4000) DEFAULT NULL,
  `CSEX` varchar(255) DEFAULT '1',
  PRIMARY KEY (`CHILDID`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

/*Data for the table `kchild` */

/*Table structure for table `kcomments` */

DROP TABLE IF EXISTS `kcomments`;

CREATE TABLE `kcomments` (
  `CID` int(11) NOT NULL AUTO_INCREMENT,
  `CVALUE` varchar(5000) NOT NULL,
  `QID` int(11) NOT NULL,
  `UID` int(11) NOT NULL,
  `CTIME` varchar(50) NOT NULL,
  PRIMARY KEY (`CID`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

/*Data for the table `kcomments` */

insert  into `kcomments`(`CID`,`CVALUE`,`QID`,`UID`,`CTIME`) values (1,'djdjnfcn',8,13,'2014-12-23 18:40'),(2,'ghhhh',12,15,'2014-12-23 20:09'),(3,'Yes',14,18,'2014-12-23 21:33');

/*Table structure for table `kfriend` */

DROP TABLE IF EXISTS `kfriend`;

CREATE TABLE `kfriend` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `UID` int(11) NOT NULL,
  `FID` int(11) NOT NULL,
  `CREATETIME` varchar(50) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8;

/*Data for the table `kfriend` */

insert  into `kfriend`(`ID`,`UID`,`FID`,`CREATETIME`) values (11,13,15,'2014-12-23 20:30'),(12,15,13,'2014-12-23 20:30');

/*Table structure for table `kinvitation` */

DROP TABLE IF EXISTS `kinvitation`;

CREATE TABLE `kinvitation` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `SENDUID` int(11) NOT NULL,
  `RECEIVEUID` int(11) NOT NULL,
  `INVITETIME` varchar(50) NOT NULL,
  `RESPONSETIME` varchar(50) NOT NULL,
  `STATUS` varchar(1) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8;

/*Data for the table `kinvitation` */

insert  into `kinvitation`(`ID`,`SENDUID`,`RECEIVEUID`,`INVITETIME`,`RESPONSETIME`,`STATUS`) values (7,14,13,'2014-12-23 17:27','2014-12-23 17:30','1'),(8,15,13,'2014-12-23 20:10','2014-12-23 20:11','2'),(9,15,16,'2014-12-23 20:28','','0'),(10,15,13,'2014-12-23 20:29','2014-12-23 20:30','1'),(11,18,14,'2014-12-23 21:34','2014-12-23 21:34','1'),(12,18,15,'2014-12-23 21:35','','0');

/*Table structure for table `knotes` */

DROP TABLE IF EXISTS `knotes`;

CREATE TABLE `knotes` (
  `NID` int(11) NOT NULL AUTO_INCREMENT,
  `SUID` int(11) NOT NULL DEFAULT '0',
  `RUID` int(11) NOT NULL DEFAULT '0',
  `NVALUE` varchar(5000) DEFAULT NULL,
  PRIMARY KEY (`NID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `knotes` */

/*Table structure for table `ktype` */

DROP TABLE IF EXISTS `ktype`;

CREATE TABLE `ktype` (
  `TID` int(11) NOT NULL AUTO_INCREMENT,
  `TNAME` varchar(50) NOT NULL,
  PRIMARY KEY (`TID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `ktype` */

/*Table structure for table `kuser` */

DROP TABLE IF EXISTS `kuser`;

CREATE TABLE `kuser` (
  `USERID` int(11) NOT NULL AUTO_INCREMENT,
  `UNAME` varchar(50) NOT NULL,
  `UHEAD` varchar(50) DEFAULT NULL,
  `UAGE` varchar(50) DEFAULT NULL,
  `UHOBBIES` varchar(50) DEFAULT NULL,
  `UPLACE` varchar(50) DEFAULT NULL,
  `UEXPLAIN` varchar(50) DEFAULT NULL,
  `UTIME` varchar(50) DEFAULT NULL,
  `UBRAND` varchar(50) DEFAULT NULL,
  `UPASS` varchar(112) NOT NULL DEFAULT '',
  `USEX` varchar(255) DEFAULT '1',
  `UEMAIL` varchar(500) DEFAULT NULL,
  `UFACEBOOK` varchar(4000) DEFAULT NULL,
  `ULAT` double(13,8) NOT NULL DEFAULT '0.00000000',
  `ULNG` double(13,8) NOT NULL DEFAULT '0.00000000',
  `UTOKEN` varchar(1000) DEFAULT NULL,
  `UTOKEN_EXPTIME` varchar(100) DEFAULT NULL,
  `USTATUS` varchar(1) DEFAULT '0',
  PRIMARY KEY (`USERID`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8;

/*Data for the table `kuser` */

insert  into `kuser`(`USERID`,`UNAME`,`UHEAD`,`UAGE`,`UHOBBIES`,`UPLACE`,`UEXPLAIN`,`UTIME`,`UBRAND`,`UPASS`,`USEX`,`UEMAIL`,`UFACEBOOK`,`ULAT`,`ULNG`,`UTOKEN`,`UTOKEN_EXPTIME`,`USTATUS`) values (13,'linxuanjiang','','30',NULL,'18th ave, brooklyn','','2014-12-23 17:02',NULL,'e10adc3949ba59abbe56e057f20f883e','0','asdf11gfffh@nyu.edu','',40.61913100,-73.99018200,'559cd3f7af0730afe0db90535066b292','1419472437','1'),(14,'hanjie',NULL,NULL,NULL,NULL,NULL,'2014-12-23 17:23',NULL,'e10adc3949ba59abbe56e057f20f883e','1','hs1942@nyu.edu',NULL,0.00000000,0.00000000,'7e0a8037cf65f0d25491aca97707cf89','1419459917','1'),(15,'lihong','','25',NULL,'8 avenue brooklyn','readdinggoof','2014-12-23 18:30',NULL,'e10adc3949ba59abbe56e057f20f883e','0','lihong@nyu.edu','',40.65960580,-73.98478340,'ed51f043c91be1b5bd70c83bc735ee1a','1419463803','1'),(16,'layla111',NULL,NULL,NULL,NULL,NULL,'2014-12-23 20:27',NULL,'e10adc3949ba59abbe56e057f20f883e','1','rw1333@nyu.edu',NULL,0.00000000,0.00000000,'8c91145c4819655f734f22d56d408700','1419472262','1'),(17,'linxuan',NULL,NULL,NULL,NULL,NULL,'2014-12-23 21:17',NULL,'e10adc3949ba59abbe56e057f20f883e','1','asdf11gh@nyu.edu',NULL,0.00000000,0.00000000,'4044ed63995f0263295dfa4fdca7a943','1419473853','0'),(18,'ljj123','','25',NULL,'11th ave, brooklyn','','2014-12-23 21:32',NULL,'e10adc3949ba59abbe56e057f20f883e','0','asdf12221gfffh@nyu.edu','',40.62804000,-74.00655400,'6abba5a8a41d73aec4ebc626df898188','1419474735','1');

/*Table structure for table `kvalue` */

DROP TABLE IF EXISTS `kvalue`;

CREATE TABLE `kvalue` (
  `QID` int(11) NOT NULL AUTO_INCREMENT,
  `UID` int(11) DEFAULT NULL,
  `TID` int(11) NOT NULL,
  `QIMG` varchar(50) CHARACTER SET gbk NOT NULL,
  `QVALUE` varchar(500) CHARACTER SET gbk NOT NULL,
  `QLIKE` varchar(50) CHARACTER SET gbk NOT NULL DEFAULT '0',
  `QUNLIKE` varchar(50) CHARACTER SET gbk NOT NULL DEFAULT '0',
  `QSHARE` int(11) DEFAULT '0',
  `ISCHECKDE` int(11) NOT NULL DEFAULT '0',
  `TITLE` varchar(500) NOT NULL,
  `STARTDATE` varchar(100) NOT NULL,
  `STARTTIME` varchar(100) NOT NULL,
  `ENDDATE` varchar(100) NOT NULL,
  `ENDTIME` varchar(100) NOT NULL,
  `SPOT` varchar(500) NOT NULL,
  `MAXNUM` int(11) NOT NULL,
  `DETAIL` varchar(4000) NOT NULL,
  PRIMARY KEY (`QID`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8;

/*Data for the table `kvalue` */

insert  into `kvalue`(`QID`,`UID`,`TID`,`QIMG`,`QVALUE`,`QLIKE`,`QUNLIKE`,`QSHARE`,`ISCHECKDE`,`TITLE`,`STARTDATE`,`STARTTIME`,`ENDDATE`,`ENDTIME`,`SPOT`,`MAXNUM`,`DETAIL`) values (8,13,1,'','','0','0',0,1,'','','','','','',0,'tyy'),(9,13,1,'','','0','0',0,1,'','','','','','',0,'Wahztv'),(10,15,1,'','','0','0',0,1,'','','','','','',0,'high. '),(11,13,1,'','','0','0',0,1,'','','','','','',0,'hdjklf'),(12,13,1,'','','0','0',0,1,'','','','','','',0,'demo time'),(13,15,1,'','','0','0',0,1,'','','','','','',0,'basketball'),(14,18,1,'','','0','0',0,1,'','','','','','',0,'What a day!');

/* Function  structure for function  `GETDISTANCE` */

/*!50003 DROP FUNCTION IF EXISTS `GETDISTANCE` */;
DELIMITER $$

/*!50003 CREATE DEFINER=`root`@`%` FUNCTION `GETDISTANCE`(deg_lat1 FLOAT, deg_lng1 FLOAT, deg_lat2 FLOAT, deg_lng2 FLOAT) RETURNS float
    DETERMINISTIC
BEGIN 
  DECLARE distance FLOAT;
  DECLARE delta_lat FLOAT; 
  DECLARE delta_lng FLOAT; 
  DECLARE lat1 FLOAT; 
  DECLARE lat2 FLOAT;
  DECLARE a FLOAT;
  SET distance = 0;
  /*Convert degrees to radians and get the variables I need.*/
  SET delta_lat = radians(deg_lat2 - deg_lat1); 
  SET delta_lng = radians(deg_lng2 - deg_lng1); 
  SET lat1 = radians(deg_lat1); 
  SET lat2 = radians(deg_lat2); 
  /*Formula found here: http://www.movable-type.co.uk/scripts/latlong.html*/
  SET a = sin(delta_lat/2.0) * sin(delta_lat/2.0) + sin(delta_lng/2.0) * sin(delta_lng/2.0) * cos(lat1) * cos(lat2); 
  SET distance = 3956.6 * 2 * atan2(sqrt(a),  sqrt(1-a)); 
  RETURN distance;
END */$$
DELIMITER ;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
