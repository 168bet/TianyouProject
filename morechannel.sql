/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50505
Source Host           : localhost:3306
Source Database       : tianyou

Target Server Type    : MYSQL
Target Server Version : 50505
File Encoding         : 65001

Date: 2017-02-08 11:08:25
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for morechannel
-- ----------------------------
DROP TABLE IF EXISTS `morechannel`;
CREATE TABLE `morechannel` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `name` varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT '名称',
  `type` varchar(20) CHARACTER SET utf8 DEFAULT NULL COMMENT '标示码',
  `status` smallint(2) DEFAULT NULL COMMENT '使用状态',
  `create_time` int(11) DEFAULT NULL COMMENT '添加时间',
  `update_time` int(11) DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=46 DEFAULT CHARSET=sjis;

-- ----------------------------
-- Records of morechannel
-- ----------------------------
INSERT INTO `morechannel` VALUES ('1', '小米', 'ty001', '1', '1477295929', '1477296052');
INSERT INTO `morechannel` VALUES ('2', '华为', 'ty002', '1', '1477295929', '1477295929');
INSERT INTO `morechannel` VALUES ('3', '360', 'ty003', '1', '1477295929', '1477295929');
INSERT INTO `morechannel` VALUES ('4', 'VIVO', 'ty004', '1', '1477295929', '1477295929');
INSERT INTO `morechannel` VALUES ('5', 'UC', 'ty005', '1', '1477295929', '1477295929');
INSERT INTO `morechannel` VALUES ('6', '魅族', 'ty006', '1', '1477295929', '1477295929');
INSERT INTO `morechannel` VALUES ('7', '当乐', 'ty007', '1', '1477295929', '1477295929');
INSERT INTO `morechannel` VALUES ('8', 'OPPO', 'ty008', '1', '1477295929', '1477295929');
INSERT INTO `morechannel` VALUES ('9', '金立', 'ty009', '1', '1477295929', '1477295929');
INSERT INTO `morechannel` VALUES ('10', '安智', 'ty010', '1', '1477295929', '1477295929');
INSERT INTO `morechannel` VALUES ('11', '乐视', 'ty011', '1', '1477295929', '1477295929');
INSERT INTO `morechannel` VALUES ('12', '联想', 'ty012', '1', '1477295929', '1477295929');
INSERT INTO `morechannel` VALUES ('13', '百度', 'ty013', '1', '1477295929', '1477295929');
INSERT INTO `morechannel` VALUES ('14', '豌豆荚', 'ty014', '1', '1477295929', '1477295929');
INSERT INTO `morechannel` VALUES ('15', '海马玩', 'ty015', '1', '1477295929', '1477295929');
INSERT INTO `morechannel` VALUES ('16', 'TT', 'ty016', '1', '1477295929', '1477295929');
INSERT INTO `morechannel` VALUES ('17', '酷派', 'ty017', '1', '1477295929', '1477295929');
INSERT INTO `morechannel` VALUES ('18', '虫虫', 'ty018', '1', '1477295929', '1477295929');
INSERT INTO `morechannel` VALUES ('19', '闲趣', 'ty019', '1', '1477295929', '1477295929');
INSERT INTO `morechannel` VALUES ('20', '朋友玩', 'ty020', '1', '1477295929', '1477295929');
INSERT INTO `morechannel` VALUES ('21', '250', 'ty021', '1', '1477295929', '1477295929');
INSERT INTO `morechannel` VALUES ('22', '爱奇艺', 'ty022', '1', '1477295929', '1477295929');
INSERT INTO `morechannel` VALUES ('23', '爱游戏', 'ty023', '1', '1477295929', '1477295929');
INSERT INTO `morechannel` VALUES ('24', '搜狗', 'ty024', '1', '1477295929', '1477295929');
INSERT INTO `morechannel` VALUES ('25', '4399', 'ty025', '1', '1477295929', '1477295929');
INSERT INTO `morechannel` VALUES ('26', '应用宝', 'ty026', '1', '1477295929', '1477295929');
INSERT INTO `morechannel` VALUES ('27', '红手指', 'ty027', '1', '1477295929', '1477295929');
INSERT INTO `morechannel` VALUES ('28', '神起', 'ty028', '1', '1477295929', '1477295929');
INSERT INTO `morechannel` VALUES ('29', '爱谱', 'ty029', '1', '1477295929', '1477295929');
INSERT INTO `morechannel` VALUES ('30', '无忧IOS', 'ty030', '1', '1477295929', '1477295929');
INSERT INTO `morechannel` VALUES ('31', '点智', 'ty031', '1', '1477295929', '1477295929');
INSERT INTO `morechannel` VALUES ('32', '天天', 'ty032', '1', '1477295929', '1477295929');
INSERT INTO `morechannel` VALUES ('33', '蘑菇', 'ty033', '1', '1477295929', '1477295929');
INSERT INTO `morechannel` VALUES ('34', '无忧Android', 'ty034', '1', '1477295929', '1477295929');
INSERT INTO `morechannel` VALUES ('35', '闪游', 'ty035', '1', '1477295929', '1477295929');
INSERT INTO `morechannel` VALUES ('36', '银狐', 'ty036', '1', '1477295929', '1477295929');
INSERT INTO `morechannel` VALUES ('37', '游码', 'ty037', '1', '1477295929', '1477295929');
INSERT INTO `morechannel` VALUES ('38', '闲趣英文', 'bm101', '1', '1477295929', '1477295929');
INSERT INTO `morechannel` VALUES ('39', '韩国pang-onestore', 'bm102', '1', '1477295929', '1477295929');
INSERT INTO `morechannel` VALUES ('40', '泛为', 'ty038', '1', '1477295929', '1477295929');
INSERT INTO `morechannel` VALUES ('41', '韩国pang-google', 'bm103', '1', '1477295929', '1477295929');
INSERT INTO `morechannel` VALUES ('42', '闲趣Android', 'ty039', '1', '1477295929', '1477295929');
INSERT INTO `morechannel` VALUES ('43', '闲趣Android', 'ty040', '1', '1477295929', '1477295929');
INSERT INTO `morechannel` VALUES ('44', '即拓', 'ty041', '1', '1477295929', '1477295929');
INSERT INTO `morechannel` VALUES ('45', '秒乐IOS', 'ty042', '1', '1477295929', '1477295929');
