/*
 Navicat Premium Data Transfer

 Source Server         : 127.0.0.1
 Source Server Type    : MySQL
 Source Server Version : 50549
 Source Host           : localhost:3306
 Source Schema         : distributedidgenerator

 Target Server Type    : MySQL
 Target Server Version : 50549
 File Encoding         : 65001

 Date: 18/02/2019 17:53:36
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for idgenerator
-- ----------------------------
DROP TABLE IF EXISTS `idgenerator`;
CREATE TABLE `idgenerator`  (
  `table_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '',
  `current_value` bigint(20) NOT NULL,
  PRIMARY KEY (`table_name`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of idgenerator
-- ----------------------------
INSERT INTO `idgenerator` VALUES ('intention', 20);
INSERT INTO `idgenerator` VALUES ('reservation', 10);

SET FOREIGN_KEY_CHECKS = 1;
