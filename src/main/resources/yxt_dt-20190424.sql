/*
Navicat MySQL Data Transfer

Source Server         : localhost_3306
Source Server Version : 50717
Source Host           : localhost:3306
Source Database       : yxt_dt

Target Server Type    : MYSQL
Target Server Version : 50717
File Encoding         : 65001

Date: 2019-04-24 08:20:01
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `base_khsk`
-- ----------------------------
DROP TABLE IF EXISTS `base_khsk`;
CREATE TABLE `base_khsk` (
  `LCXH` int(11) NOT NULL,
  `XH` int(11) NOT NULL,
  `CZ_ID` int(11) DEFAULT NULL,
  `CZ_NAME` varchar(30) DEFAULT NULL,
  `ARR_TIME` varchar(20) DEFAULT NULL,
  `DEP_TIME` varchar(20) DEFAULT NULL,
  `DFX_NAME` varchar(20) DEFAULT NULL,
  `YYTS` int(11) DEFAULT NULL,
  `JSTS` int(11) DEFAULT NULL,
  `BK_TYPE` varchar(15) DEFAULT NULL,
  `JOB_TYPE` varchar(15) DEFAULT NULL,
  `JOB_TIME` int(11) DEFAULT NULL,
  `QJ_ID` int(11) DEFAULT NULL,
  `YXZLH` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=gbk;

-- ----------------------------
-- Records of base_khsk
-- ----------------------------

-- ----------------------------
-- Table structure for `base_lckxfa`
-- ----------------------------
DROP TABLE IF EXISTS `base_lckxfa`;
CREATE TABLE `base_lckxfa` (
  `LCKXFA_ID` int(11) NOT NULL,
  `QSZM` varchar(30) DEFAULT NULL,
  `ZZZM` varchar(30) DEFAULT NULL,
  `LJH` int(11) DEFAULT NULL,
  `QSQJH` int(11) DEFAULT NULL,
  `ZZQJH` int(11) DEFAULT NULL,
  `QSSJ` int(11) DEFAULT NULL,
  `ZZSJ` int(11) DEFAULT NULL,
  `XCL` int(11) DEFAULT NULL,
  `YXZL` varchar(30) DEFAULT NULL,
  `FCJG` int(11) DEFAULT NULL,
  `ZQ` int(11) DEFAULT NULL,
  `CS` int(11) DEFAULT NULL,
  `SJCS` int(11) DEFAULT NULL,
  `ZDFCJG` int(11) DEFAULT NULL,
  `ZXFCJG` int(11) DEFAULT NULL,
  `ZCDS` int(11) DEFAULT NULL,
  `ZSJCDS` int(11) DEFAULT NULL,
  `TZH` int(11) DEFAULT NULL,
  PRIMARY KEY (`LCKXFA_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=gbk;

-- ----------------------------
-- Records of base_lckxfa
-- ----------------------------

-- ----------------------------
-- Table structure for `base_lctz`
-- ----------------------------
DROP TABLE IF EXISTS `base_lctz`;
CREATE TABLE `base_lctz` (
  `TRAIN_XH` int(11) NOT NULL,
  `TRAIN_CC` varchar(100) DEFAULT NULL,
  `START_TIME` varchar(20) DEFAULT NULL,
  `END_TIME` varchar(20) DEFAULT NULL,
  `START_STA_ID` int(11) DEFAULT NULL,
  `START_STATION` varchar(30) DEFAULT NULL,
  `SEC_START_STA_ID` int(11) DEFAULT NULL,
  `SECTION_START_STA` varchar(30) DEFAULT NULL,
  `END_STA_ID` int(11) DEFAULT NULL,
  `END_STATION` varchar(30) DEFAULT NULL,
  `SEC_END_STA_ID` int(11) DEFAULT NULL,
  `SECTION_END_STA` varchar(30) DEFAULT NULL,
  `KXGL` varchar(40) DEFAULT NULL,
  PRIMARY KEY (`TRAIN_XH`)
) ENGINE=InnoDB DEFAULT CHARSET=gbk;

-- ----------------------------
-- Records of base_lctz
-- ----------------------------

-- ----------------------------
-- Table structure for `dic_hczjxsj`
-- ----------------------------
DROP TABLE IF EXISTS `dic_hczjxsj`;
CREATE TABLE `dic_hczjxsj` (
  `XH` int(11) NOT NULL,
  `OUT_CZM` varchar(30) DEFAULT NULL,
  `OUT_XLM` varchar(20) DEFAULT NULL,
  `OUT_FX` varchar(10) DEFAULT NULL,
  `IN_XLM` varchar(20) DEFAULT NULL,
  `IN_CZM` varchar(30) DEFAULT NULL,
  `IN_FX` varchar(10) DEFAULT NULL,
  `HCJXSJ` int(11) DEFAULT NULL,
  PRIMARY KEY (`XH`)
) ENGINE=InnoDB DEFAULT CHARSET=gbk;

-- ----------------------------
-- Records of dic_hczjxsj
-- ----------------------------

-- ----------------------------
-- Table structure for `dic_jlsm`
-- ----------------------------
DROP TABLE IF EXISTS `dic_jlsm`;
CREATE TABLE `dic_jlsm` (
  `JL_ID` int(11) NOT NULL,
  `JLSM` varchar(50) DEFAULT NULL,
  `JL_QZM` varchar(30) DEFAULT NULL,
  `JL_ZZM` varchar(30) DEFAULT NULL,
  `JL_DXID` int(11) DEFAULT NULL,
  PRIMARY KEY (`JL_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=gbk;

-- ----------------------------
-- Records of dic_jlsm
-- ----------------------------
INSERT INTO `dic_jlsm` VALUES ('1', '成都东到重庆北', '成都东', '重庆北', '2');
INSERT INTO `dic_jlsm` VALUES ('2', '重庆北', '重庆北', '成都东', '1');

-- ----------------------------
-- Table structure for `dic_lcjl`
-- ----------------------------
DROP TABLE IF EXISTS `dic_lcjl`;
CREATE TABLE `dic_lcjl` (
  `JL_ID` int(11) NOT NULL,
  `XH` int(11) NOT NULL,
  `QJ_ID` int(11) NOT NULL,
  `QJ_QZM` varchar(30) DEFAULT NULL,
  `QJ_ZZM` varchar(30) DEFAULT NULL,
  PRIMARY KEY (`JL_ID`,`XH`,`QJ_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=gbk;

-- ----------------------------
-- Records of dic_lcjl
-- ----------------------------
INSERT INTO `dic_lcjl` VALUES ('1', '1', '1', '成都东', '简阳南');
INSERT INTO `dic_lcjl` VALUES ('1', '2', '3', '简阳南', '资阳北');
INSERT INTO `dic_lcjl` VALUES ('1', '3', '5', '资阳北', '资中北');
INSERT INTO `dic_lcjl` VALUES ('1', '4', '7', '资中北', '内江北');
INSERT INTO `dic_lcjl` VALUES ('2', '1', '8', '内江北', '资中北');
INSERT INTO `dic_lcjl` VALUES ('2', '2', '6', '资中北', '资阳北');
INSERT INTO `dic_lcjl` VALUES ('2', '3', '4', '资阳北', '简阳南');
INSERT INTO `dic_lcjl` VALUES ('2', '4', '2', '简阳南', '成都东');

-- ----------------------------
-- Table structure for `dic_line`
-- ----------------------------
DROP TABLE IF EXISTS `dic_line`;
CREATE TABLE `dic_line` (
  `LINE_ID` int(11) NOT NULL,
  `LINE_NAME` varchar(20) DEFAULT NULL,
  `LINE_START_CZM` varchar(30) DEFAULT NULL,
  `LINE_END_CZM` varchar(30) DEFAULT NULL,
  `LINELENGTH` double(8,3) DEFAULT NULL,
  `LINE_TYPE` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`LINE_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=gbk;

-- ----------------------------
-- Records of dic_line
-- ----------------------------
INSERT INTO `dic_line` VALUES ('1', '成渝高铁', '成都东', '重庆北', '325.000', '一等');

-- ----------------------------
-- Table structure for `dic_linesection`
-- ----------------------------
DROP TABLE IF EXISTS `dic_linesection`;
CREATE TABLE `dic_linesection` (
  `LINE_ID` int(11) NOT NULL,
  `LINE_NAME` varchar(20) DEFAULT NULL,
  `QJ_SN` int(11) NOT NULL,
  `QJ_ID` int(11) NOT NULL,
  `CZ1_ID` int(11) NOT NULL,
  `CZ1_NAME` varchar(30) DEFAULT NULL,
  `CZ2_ID` int(11) NOT NULL,
  `CZ2_NAME` varchar(30) DEFAULT NULL,
  PRIMARY KEY (`LINE_ID`,`QJ_SN`,`QJ_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=gbk;

-- ----------------------------
-- Records of dic_linesection
-- ----------------------------
INSERT INTO `dic_linesection` VALUES ('1', '成渝高铁', '1', '1', '1', '成都东', '2', '简阳南');
INSERT INTO `dic_linesection` VALUES ('1', '成渝高铁', '2', '3', '2', '简阳南', '3', '资阳北');
INSERT INTO `dic_linesection` VALUES ('1', '成渝高铁', '3', '5', '3', '资阳北', '4', '资中北');
INSERT INTO `dic_linesection` VALUES ('1', '成渝高铁', '4', '7', '4', '资中北', '5', '内江北');

-- ----------------------------
-- Table structure for `dic_linestation`
-- ----------------------------
DROP TABLE IF EXISTS `dic_linestation`;
CREATE TABLE `dic_linestation` (
  `LINE_ID` int(11) NOT NULL,
  `LINE_NAME` varchar(20) DEFAULT NULL,
  `CZ_SN` int(11) NOT NULL,
  `CZ_ID` int(11) NOT NULL,
  `CZ_NAME` varchar(30) DEFAULT NULL,
  PRIMARY KEY (`LINE_ID`,`CZ_SN`,`CZ_ID`),
  CONSTRAINT `dic_linestation_ibfk_1` FOREIGN KEY (`LINE_ID`) REFERENCES `dic_line` (`LINE_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=gbk;

-- ----------------------------
-- Records of dic_linestation
-- ----------------------------
INSERT INTO `dic_linestation` VALUES ('1', '成渝高铁', '1', '1', '成都东');
INSERT INTO `dic_linestation` VALUES ('1', '成渝高铁', '2', '2', '简阳南');
INSERT INTO `dic_linestation` VALUES ('1', '成渝高铁', '3', '3', '资阳北');
INSERT INTO `dic_linestation` VALUES ('1', '成渝高铁', '4', '4', '资中北');
INSERT INTO `dic_linestation` VALUES ('1', '成渝高铁', '5', '5', '内江北');
INSERT INTO `dic_linestation` VALUES ('1', '成渝高铁', '6', '6', '隆昌北');
INSERT INTO `dic_linestation` VALUES ('1', '成渝高铁', '7', '7', '大足南');
INSERT INTO `dic_linestation` VALUES ('1', '成渝高铁', '8', '8', '永川东');
INSERT INTO `dic_linestation` VALUES ('1', '成渝高铁', '9', '9', '璧山');
INSERT INTO `dic_linestation` VALUES ('1', '成渝高铁', '10', '10', '沙坪坝');
INSERT INTO `dic_linestation` VALUES ('1', '成渝高铁', '11', '11', '歌乐山');
INSERT INTO `dic_linestation` VALUES ('1', '成渝高铁', '12', '12', '重庆北');

-- ----------------------------
-- Table structure for `dic_section`
-- ----------------------------
DROP TABLE IF EXISTS `dic_section`;
CREATE TABLE `dic_section` (
  `QJ_ID` int(11) NOT NULL,
  `CZ1_ID` int(11) NOT NULL,
  `CZ1_NAME` varchar(30) DEFAULT NULL,
  `CZ2_ID` int(11) NOT NULL,
  `CZ2_NAME` varchar(30) DEFAULT NULL,
  `QJ_SXX` varchar(5) DEFAULT NULL,
  `QJ_BSFS` varchar(20) DEFAULT NULL,
  `QJ_YXXZ` varchar(8) DEFAULT NULL,
  `QJ_LJM` varchar(20) DEFAULT NULL,
  `QJ_LENGTH` double(8,3) DEFAULT NULL,
  PRIMARY KEY (`QJ_ID`,`CZ1_ID`,`CZ2_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=gbk;

-- ----------------------------
-- Records of dic_section
-- ----------------------------
INSERT INTO `dic_section` VALUES ('1', '1', '成都东', '2', '简阳南', '下行', '自动闭塞', '双线', '成都', '20.500');
INSERT INTO `dic_section` VALUES ('2', '2', '简阳南', '1', '成都东', '上行', '自动闭塞', '双线', '成都', '20.500');
INSERT INTO `dic_section` VALUES ('3', '2', '简阳南', '3', '资阳北', '下行', '自动闭塞', '双线', '成都', '18.000');
INSERT INTO `dic_section` VALUES ('4', '3', '资阳北', '2', '简阳南', '上行', '自动闭塞', '双线', '成都', '18.000');
INSERT INTO `dic_section` VALUES ('5', '3', '资阳北', '4', '资中北', '下行', '自动闭塞', '双线', '成都', '15.000');
INSERT INTO `dic_section` VALUES ('6', '4', '资中北', '3', '资阳北', '上行', '自动闭塞', '双线', '成都', '15.000');
INSERT INTO `dic_section` VALUES ('7', '4', '资中北', '5', '内江北', '下行', '自动闭塞', '双线', '成都', '19.000');
INSERT INTO `dic_section` VALUES ('8', '5', '内江北', '4', '资中北', '上行', '自动闭塞', '双线', '成都', '19.000');

-- ----------------------------
-- Table structure for `dic_station`
-- ----------------------------
DROP TABLE IF EXISTS `dic_station`;
CREATE TABLE `dic_station` (
  `CZ_ID` int(11) NOT NULL,
  `CZ_NAME` varchar(30) DEFAULT NULL,
  `CZ_XZ` varchar(20) DEFAULT NULL,
  `FJZ_FLAG` smallint(6) DEFAULT NULL,
  `LJM` varchar(20) DEFAULT NULL,
  `CZ_SXDFX` smallint(6) DEFAULT NULL,
  `CZ_XXDFX` smallint(6) DEFAULT NULL,
  PRIMARY KEY (`CZ_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=gbk;

-- ----------------------------
-- Records of dic_station
-- ----------------------------
INSERT INTO `dic_station` VALUES ('1', '成都东', '客运站', '0', '成都', '12', '12');
INSERT INTO `dic_station` VALUES ('2', '简阳南', '客运站', '0', '成都', '5', '5');
INSERT INTO `dic_station` VALUES ('3', '资阳北', '客运站', null, '成都', '5', '5');
INSERT INTO `dic_station` VALUES ('4', '资中北', '客运站', null, '成都', '5', '5');
INSERT INTO `dic_station` VALUES ('5', '内江北', '客运站', null, '成都', '5', '5');
INSERT INTO `dic_station` VALUES ('6', '隆昌北', '客运站', null, '成都', '5', '5');
INSERT INTO `dic_station` VALUES ('7', '大足南', '客运站', null, '成都', '5', '5');
INSERT INTO `dic_station` VALUES ('8', '永川东', '客运站', null, '成都', '5', '5');
INSERT INTO `dic_station` VALUES ('9', '璧山', '客运站', null, '成都', '5', '5');
INSERT INTO `dic_station` VALUES ('10', '沙坪坝', '客运站', null, '成都', '5', '5');
INSERT INTO `dic_station` VALUES ('11', '歌乐山', '客运站', '0', '成都', '5', '5');
INSERT INTO `dic_station` VALUES ('12', '重庆北', '客运站', '0', '成都', '20', '20');

-- ----------------------------
-- Table structure for `dic_tzcc`
-- ----------------------------
DROP TABLE IF EXISTS `dic_tzcc`;
CREATE TABLE `dic_tzcc` (
  `TZH` int(11) NOT NULL,
  `QSCC` varchar(30) DEFAULT NULL,
  `ZZCC` varchar(30) DEFAULT NULL,
  `TZM` varchar(30) DEFAULT NULL,
  `JC` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`TZH`)
) ENGINE=InnoDB DEFAULT CHARSET=gbk;

-- ----------------------------
-- Records of dic_tzcc
-- ----------------------------

-- ----------------------------
-- Table structure for `dic_yxzl`
-- ----------------------------
DROP TABLE IF EXISTS `dic_yxzl`;
CREATE TABLE `dic_yxzl` (
  `RUN_TYPE_ID` int(11) NOT NULL,
  `RUN_TYPE` varchar(60) DEFAULT NULL,
  `LINE_ID` int(11) NOT NULL,
  `LINE_NAME` varchar(20) DEFAULT NULL,
  `LOCO_TYPE` varchar(40) DEFAULT NULL,
  `WEIGHT` varchar(40) DEFAULT NULL,
  PRIMARY KEY (`RUN_TYPE_ID`,`LINE_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=gbk;

-- ----------------------------
-- Records of dic_yxzl
-- ----------------------------

-- ----------------------------
-- Table structure for `dic_yxzlsf`
-- ----------------------------
DROP TABLE IF EXISTS `dic_yxzlsf`;
CREATE TABLE `dic_yxzlsf` (
  `XH` int(11) NOT NULL,
  `RUN_TYPE_ID` int(11) NOT NULL,
  `LINE_ID` int(11) NOT NULL,
  `LINE_NAME` varchar(20) DEFAULT NULL,
  `QJ_ID` int(11) NOT NULL,
  `CZ1_NAME` varchar(30) DEFAULT NULL,
  `CZ2_NAME` varchar(30) DEFAULT NULL,
  `SXYXSJ` varchar(20) DEFAULT NULL,
  `XXYXSJ` varchar(20) DEFAULT NULL,
  `SXQDSJ` varchar(20) DEFAULT NULL,
  `XXQDSJ` varchar(20) DEFAULT NULL,
  `SXTZSJ` varchar(20) DEFAULT NULL,
  `XXTZSJ` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`XH`,`RUN_TYPE_ID`,`LINE_ID`,`QJ_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=gbk;

-- ----------------------------
-- Records of dic_yxzlsf
-- ----------------------------
