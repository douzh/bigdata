
-- ----------------------------
-- Table structure for `sys_menu`
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu` (
  `id` varchar(32) NOT NULL COMMENT '主键id',
  `parent_id` varchar(32) DEFAULT NULL COMMENT '父菜单id',
  `parent_ids` varchar(2000) DEFAULT NULL COMMENT '所有父id',
  `name` varchar(100) DEFAULT NULL COMMENT '菜单名称',
  `url` varchar(1000) DEFAULT NULL COMMENT '菜单链接',
  `icon` varchar(255) DEFAULT NULL COMMENT '菜单图标',
  `sort` int(11) DEFAULT NULL COMMENT '排序',
  `status` varchar(6) DEFAULT NULL COMMENT '状态（0显示，1隐藏)',
  `permission` varchar(255) DEFAULT NULL COMMENT '权限标识',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL,
  `create_id` varchar(32) DEFAULT NULL,
  `update_id` varchar(32) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  `open` varchar(5) DEFAULT NULL COMMENT '是否展开 true 是 false 否',
  `bapid` varchar(32) DEFAULT NULL COMMENT '机构',
  `baid` varchar(32) DEFAULT NULL COMMENT '部门',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='菜单表';

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO `sys_menu` VALUES ('0', null, null, '根目录', null, '', null, '1', null, null, null, null, '026a564bbfd84861ac4b65393644beef', '2017-11-14 17:09:12', '0', 'true', null, null);
INSERT INTO `sys_menu` VALUES ('1', '0', '0', '系统管理', '23', 'fa fa-cog', '1', '1', '32', null, null, null, '026a564bbfd84861ac4b65393644beef', '2017-10-12 10:20:01', '0', 'false', null, null);
INSERT INTO `sys_menu` VALUES ('14c58e44d6754c92806e1b97614c49b9', 'b7ab517a15b74dea812ee7ef32847a48', '0', '所有权限', '', null, '1', '1', 'sys:organ:all', null, '2017-08-29 22:11:25', '026a564bbfd84861ac4b65393644beef', '026a564bbfd84861ac4b65393644beef', '2017-08-16 11:30:16', '2', 'false', null, null);
INSERT INTO `sys_menu` VALUES ('15d2074a502f443cb760f613a40df598', '8b1f46f8ba6e455790a515c32e0329c5', null, '保存/更新', null, null, '0', '1', 'sys:user:update', null, '2017-05-18 19:31:05', '026a564bbfd84861ac4b65393644beef', '026a564bbfd84861ac4b65393644beef', '2017-08-29 22:08:39', '2', 'false', null, null);
INSERT INTO `sys_menu` VALUES ('2', '1', '1', '菜单', 'sys/menu.html', 'fa fa-bullhorn', '12', '1', 'sys:menu:list', null, null, null, '026a564bbfd84861ac4b65393644beef', '2017-09-26 10:40:07', '1', 'false', null, null);
INSERT INTO `sys_menu` VALUES ('239474f2afbf4196a6fc755c88be008d', '1', '0', 'sql监控', 'druid/sql.html', 'fa fa-tachometer', '40', '1', '', null, '2017-10-11 21:07:45', '026a564bbfd84861ac4b65393644beef', '026a564bbfd84861ac4b65393644beef', '2017-11-16 21:45:54', '1', 'false', null, null);
INSERT INTO `sys_menu` VALUES ('39cb0308a7fc4f7fb6c49440224ac2a4', '9e8dc02fe5614580bdf5f6ca9a852b70', null, '启用', '', null, '0', '1', 'sys:role:enabled', null, '2017-09-15 14:11:24', '026a564bbfd84861ac4b65393644beef', '026a564bbfd84861ac4b65393644beef', '2017-06-29 17:32:59', '2', 'false', null, null);
INSERT INTO `sys_menu` VALUES ('4', '2', '2', '保存/更新', null, null, null, '1', 'sys:menu:update', null, null, null, '026a564bbfd84861ac4b65393644beef', '2017-08-29 22:04:39', '2', 'false', null, null);
INSERT INTO `sys_menu` VALUES ('6', '2', '2', '删除', null, null, null, '1', 'sys:menu:delete', null, null, null, '026a564bbfd84861ac4b65393644beef', '2017-08-29 22:05:01', '2', 'false', null, null);
INSERT INTO `sys_menu` VALUES ('62bdcab5fe5f4e5fa7ab79fcd8cb47c4', '8b1f46f8ba6e455790a515c32e0329c5', null, '禁用', null, null, '0', '1', 'sys:user:delete', null, '2017-05-18 19:31:29', '026a564bbfd84861ac4b65393644beef', '026a564bbfd84861ac4b65393644beef', '2017-09-15 14:29:22', '2', 'false', null, null);
INSERT INTO `sys_menu` VALUES ('7', '2', '2', '查看信息', null, null, null, null, 'sys:menu:info', null, null, null, '026a564bbfd84861ac4b65393644beef', '2017-08-29 22:05:10', '2', 'false', null, null);
INSERT INTO `sys_menu` VALUES ('7cec2d46083c47bba323436cb409693f', '8b1f46f8ba6e455790a515c32e0329c5', null, '启用', '', null, '1', '1', 'sys:user:enabled', null, '2017-09-15 12:02:40', '026a564bbfd84861ac4b65393644beef', '026a564bbfd84861ac4b65393644beef', '2017-09-15 14:29:55', '2', 'false', null, null);
INSERT INTO `sys_menu` VALUES ('80030434d10548968beaaeed79c3408b', '8b1f46f8ba6e455790a515c32e0329c5', null, '查看信息', '', null, '1', '1', 'sys:user:info', null, '2017-08-29 22:09:38', '026a564bbfd84861ac4b65393644beef', '026a564bbfd84861ac4b65393644beef', '2017-06-29 17:33:35', '2', 'false', null, null);
INSERT INTO `sys_menu` VALUES ('8b1f46f8ba6e455790a515c32e0329c5', 'feb235067fd7400090b0aa5451e4a5a4', null, '系统用户', 'sys/user.html', 'fa fa-bar-chart-o', '1', '1', 'sys:user:list', null, '2017-05-10 14:01:31', '026a564bbfd84861ac4b65393644beef', '026a564bbfd84861ac4b65393644beef', '2017-09-26 11:52:05', '1', null, null, null);
INSERT INTO `sys_menu` VALUES ('9d22510a1b1a40459619c5b2fdddfdc2', '9e8dc02fe5614580bdf5f6ca9a852b70', null, '禁用', null, null, '0', '1', 'sys:role:delete', null, '2017-05-18 21:35:21', '026a564bbfd84861ac4b65393644beef', '026a564bbfd84861ac4b65393644beef', '2017-09-15 14:29:28', '2', 'false', null, null);
INSERT INTO `sys_menu` VALUES ('9e8dc02fe5614580bdf5f6ca9a852b70', 'feb235067fd7400090b0aa5451e4a5a4', null, '系统角色', 'sys/role.html', 'fa fa-rouble', '0', '1', 'sys:role:list', null, '2017-05-12 09:31:31', '026a564bbfd84861ac4b65393644beef', '026a564bbfd84861ac4b65393644beef', '2017-09-26 11:51:52', '1', 'false', null, null);
INSERT INTO `sys_menu` VALUES ('b7ab517a15b74dea812ee7ef32847a48', 'feb235067fd7400090b0aa5451e4a5a4', '0', '组织机构', 'sys/organ.html', 'fa fa-cog', '1', '1', 'sys:organ:all', null, '2017-07-19 19:42:23', '026a564bbfd84861ac4b65393644beef', '026a564bbfd84861ac4b65393644beef', '2017-09-26 11:52:28', '1', null, null, null);
INSERT INTO `sys_menu` VALUES ('bb5bc94a71a546aba5d9f813b6a352bb', '8b1f46f8ba6e455790a515c32e0329c5', null, '重置密码', '', null, '1', '1', 'sys:user:reset', null, '2017-09-15 12:23:29', '026a564bbfd84861ac4b65393644beef', '026a564bbfd84861ac4b65393644beef', '2017-09-15 14:31:32', '2', 'false', null, null);
INSERT INTO `sys_menu` VALUES ('c898d73ff1fb49dc89909ed13452103a', '9e8dc02fe5614580bdf5f6ca9a852b70', null, '保存/更新', null, null, '0', '1', 'sys:role:update', null, '2017-05-18 20:37:18', '026a564bbfd84861ac4b65393644beef', '026a564bbfd84861ac4b65393644beef', '2017-08-29 22:10:19', '2', 'false', null, null);
INSERT INTO `sys_menu` VALUES ('ce4a2b7afad24616abd283f4741fe3e0', '9e8dc02fe5614580bdf5f6ca9a852b70', null, '查看信息', '', null, null, '1', 'sys:role:info', null, '2017-05-18 20:34:07', '026a564bbfd84861ac4b65393644beef', '026a564bbfd84861ac4b65393644beef', '2017-08-29 22:10:47', '2', 'false', null, null);
INSERT INTO `sys_menu` VALUES ('feb235067fd7400090b0aa5451e4a5a4', '0', null, '权限管理', null, 'fa fa-certificate', '2', '1', null, null, '2017-06-29 17:31:31', '026a564bbfd84861ac4b65393644beef', '026a564bbfd84861ac4b65393644beef', '2017-09-27 11:04:21', '0', 'false', null, null);

-- ----------------------------
-- Table structure for `sys_organ`
-- ----------------------------
DROP TABLE IF EXISTS `sys_organ`;
CREATE TABLE `sys_organ` (
  `id` varchar(32) NOT NULL,
  `bapid` varchar(32) DEFAULT NULL COMMENT '该部门的归属机构ID ，只有当是部门的时候才生效',
  `type` varchar(1) DEFAULT NULL COMMENT '结点类型：0=根节点 ，1=机构，2=部门',
  `code` varchar(64) DEFAULT NULL COMMENT '编号',
  `name` varchar(64) DEFAULT NULL COMMENT '节点的名字',
  `parent_id` varchar(32) DEFAULT NULL COMMENT '节点的父节点',
  `is_del` varchar(1) DEFAULT NULL COMMENT '是否删除 0 是 1 否',
  `sysmark` varchar(1024) DEFAULT NULL COMMENT '系统标识，32*10+9 最多支持10级节点。用户具体一批数据的控制',
  `sort` varchar(4) DEFAULT NULL COMMENT '在同一级节点中的序号',
  `open` varchar(5) DEFAULT NULL COMMENT '是否展开 true 是 false 否',
  `remark` varchar(255) DEFAULT NULL,
  `create_id` varchar(32) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_id` varchar(32) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='组织表';

-- ----------------------------
-- Records of sys_organ
-- ----------------------------
INSERT INTO `sys_organ` VALUES ('03af227dc962445583c77289c503862a', null, '1', 'test001', 'test', 'ff6d5335b1c7456e9a2e7734062820b2', '0', 'ff6d5335b1c7456e9a2e7734062820b2', '4', 'true', '', 'f68afc12a1924deaa2051154fe0efecc', '2017-11-16 21:49:23', '026a564bbfd84861ac4b65393644beef', '2017-08-22 18:41:27');
INSERT INTO `sys_organ` VALUES ('0eec919538bd4cd7bf3b1aebd54808f5', '7180f5a0c3624f4bb6fb758ab2c3bda6', '2', '101', 'IT信息中心', '7180f5a0c3624f4bb6fb758ab2c3bda6', '1', 'ff6d5335b1c7456e9a2e7734062820b2', '1', 'true', 'IT信息中心', '026a564bbfd84861ac4b65393644beef', '2017-08-16 11:27:39', '98df0e92788c416a9fb983e89a8d01ce', '2017-09-26 22:35:56');
INSERT INTO `sys_organ` VALUES ('40ffec9b300c431e890c17beccf8e65c', '7180f5a0c3624f4bb6fb758ab2c3bda6', '2', '102', '人事中心', '7180f5a0c3624f4bb6fb758ab2c3bda6', '1', 'ff6d5335b1c7456e9a2e7734062820b2', '2', 'true', '人事中心', '026a564bbfd84861ac4b65393644beef', '2017-08-16 11:28:03', '026a564bbfd84861ac4b65393644beef', '2017-08-16 15:19:01');
INSERT INTO `sys_organ` VALUES ('4ec329625a1047ea87a8dfe8dd0750d1', 'a694d140b8e44eb2baa5f26435c6a7f8', '2', '201', '风控部门', 'a694d140b8e44eb2baa5f26435c6a7f8', '1', 'ff6d5335b1c7456e9a2e7734062820b2', '1', 'true', '风控部门', '026a564bbfd84861ac4b65393644beef', '2017-08-16 11:28:49', null, null);
INSERT INTO `sys_organ` VALUES ('7180f5a0c3624f4bb6fb758ab2c3bda6', '0', '1', '001', '成都公司', 'ff6d5335b1c7456e9a2e7734062820b2', '1', 'ff6d5335b1c7456e9a2e7734062820b2', '1', 'true', '成都总部', '026a564bbfd84861ac4b65393644beef', '2017-08-16 11:25:43', '026a564bbfd84861ac4b65393644beef', '2017-09-15 14:43:47');
INSERT INTO `sys_organ` VALUES ('7dd6175254824c15aed1f99bce4b5f06', null, '1', 'xx', 'xx', 'ff6d5335b1c7456e9a2e7734062820b2', '0', 'ff6d5335b1c7456e9a2e7734062820b2', '', 'true', 'xx', '026a564bbfd84861ac4b65393644beef', '2017-11-14 17:59:00', '026a564bbfd84861ac4b65393644beef', '2017-08-22 18:41:27');
INSERT INTO `sys_organ` VALUES ('8a726eabdc8d4bff880b7d6f6ed59d52', 'a694d140b8e44eb2baa5f26435c6a7f8', '2', '2', '2', 'ff6d5335b1c7456e9a2e7734062820b2', '0', 'ff6d5335b1c7456e9a2e7734062820b2', '2', 'true', '', '026a564bbfd84861ac4b65393644beef', '2017-09-04 10:30:37', '026a564bbfd84861ac4b65393644beef', '2017-08-22 18:41:27');
INSERT INTO `sys_organ` VALUES ('988fed2319e14ebbb1ecf6ddc74e594c', 'a694d140b8e44eb2baa5f26435c6a7f8', '1', '1', '1', 'ff6d5335b1c7456e9a2e7734062820b2', '0', 'ff6d5335b1c7456e9a2e7734062820b2', '1', 'true', '1', '026a564bbfd84861ac4b65393644beef', '2017-09-04 10:30:27', '026a564bbfd84861ac4b65393644beef', '2017-08-22 18:41:27');
INSERT INTO `sys_organ` VALUES ('9e6a3c71bcd84d45807aa6641ab1a642', 'a694d140b8e44eb2baa5f26435c6a7f8', '2', '202', '测试部门', 'a694d140b8e44eb2baa5f26435c6a7f8', '1', 'ff6d5335b1c7456e9a2e7734062820b2', '2', 'true', '测试部门', '026a564bbfd84861ac4b65393644beef', '2017-08-16 11:29:10', null, null);
INSERT INTO `sys_organ` VALUES ('a694d140b8e44eb2baa5f26435c6a7f8', '0', '1', '002', '北京分公司', 'ff6d5335b1c7456e9a2e7734062820b2', '1', 'ff6d5335b1c7456e9a2e7734062820b2', '2', 'true', '宜宾分公司', '026a564bbfd84861ac4b65393644beef', '2017-08-16 11:26:16', '026a564bbfd84861ac4b65393644beef', '2017-10-12 10:20:29');
INSERT INTO `sys_organ` VALUES ('b416c33a10674690b2fbf77c639aeb5d', 'a694d140b8e44eb2baa5f26435c6a7f8', '1', '320', 'test', 'ff6d5335b1c7456e9a2e7734062820b2', '0', 'ff6d5335b1c7456e9a2e7734062820b2', '3', 'true', '', '026a564bbfd84861ac4b65393644beef', '2017-09-04 10:08:01', '026a564bbfd84861ac4b65393644beef', '2017-08-22 18:41:27');
INSERT INTO `sys_organ` VALUES ('cfb4ac7bcec2424b85a599c479315aba', null, '2', 'test002', '1', '03af227dc962445583c77289c503862a', '0', 'ff6d5335b1c7456e9a2e7734062820b2', '2', 'true', '12', 'f68afc12a1924deaa2051154fe0efecc', '2017-11-16 21:55:29', '026a564bbfd84861ac4b65393644beef', '2017-08-22 18:41:27');
INSERT INTO `sys_organ` VALUES ('d3a2d739512040a6a7608de76baeef97', '0', '1', '23', '23', 'ff6d5335b1c7456e9a2e7734062820b2', '0', 'ff6d5335b1c7456e9a2e7734062820b2', '23', 'true', '32', '026a564bbfd84861ac4b65393644beef', '2017-08-22 18:41:19', '026a564bbfd84861ac4b65393644beef', '2017-08-22 18:36:12');
INSERT INTO `sys_organ` VALUES ('d46c505eb1194e12bd32eda8fd638e60', 'a694d140b8e44eb2baa5f26435c6a7f8', '1', 'test', 'xx', 'ff6d5335b1c7456e9a2e7734062820b2', '0', 'ff6d5335b1c7456e9a2e7734062820b2', '1', 'true', 'xxx', '026a564bbfd84861ac4b65393644beef', '2017-11-14 15:38:39', '026a564bbfd84861ac4b65393644beef', '2017-08-22 18:41:27');
INSERT INTO `sys_organ` VALUES ('ed67b908a3454079b009b64aa95a6147', null, '1', '234', '234', 'ff6d5335b1c7456e9a2e7734062820b2', '0', 'ff6d5335b1c7456e9a2e7734062820b2', '234', 'true', '234', '026a564bbfd84861ac4b65393644beef', '2017-10-18 09:50:29', '026a564bbfd84861ac4b65393644beef', '2017-08-22 18:41:27');
INSERT INTO `sys_organ` VALUES ('ff6d5335b1c7456e9a2e7734062820b2', '0', '0', '0', '组织机构树', '0', '1', 'ff6d5335b1c7456e9a2e7734062820b2', '1', 'true', '根节点', '026a564bbfd84861ac4b65393644beef', '2017-08-16 11:24:51', '026a564bbfd84861ac4b65393644beef', '2017-08-22 18:41:27');

-- ----------------------------
-- Table structure for `sys_organ_role`
-- ----------------------------
DROP TABLE IF EXISTS `sys_organ_role`;
CREATE TABLE `sys_organ_role` (
  `organ_id` varchar(32) NOT NULL COMMENT '组织id',
  `role_id` varchar(32) NOT NULL COMMENT '角色id',
  PRIMARY KEY (`organ_id`,`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='组织角色关系表';

-- ----------------------------
-- Records of sys_organ_role
-- ----------------------------
INSERT INTO `sys_organ_role` VALUES ('0eec919538bd4cd7bf3b1aebd54808f5', '3f4f819d494c469295d197afea323233');
INSERT INTO `sys_organ_role` VALUES ('0eec919538bd4cd7bf3b1aebd54808f5', '68fd1413d3a148f7aa1fdfb0c2798544');
INSERT INTO `sys_organ_role` VALUES ('0eec919538bd4cd7bf3b1aebd54808f5', '695a97ae34b94d57899833a3ebcd656a');
INSERT INTO `sys_organ_role` VALUES ('0eec919538bd4cd7bf3b1aebd54808f5', 'f6e67f1c5d9d4ac29fd8d6aab81c3aab');
INSERT INTO `sys_organ_role` VALUES ('40ffec9b300c431e890c17beccf8e65c', '3f4f819d494c469295d197afea323233');
INSERT INTO `sys_organ_role` VALUES ('40ffec9b300c431e890c17beccf8e65c', '68fd1413d3a148f7aa1fdfb0c2798544');
INSERT INTO `sys_organ_role` VALUES ('40ffec9b300c431e890c17beccf8e65c', '695a97ae34b94d57899833a3ebcd656a');
INSERT INTO `sys_organ_role` VALUES ('40ffec9b300c431e890c17beccf8e65c', 'f6e67f1c5d9d4ac29fd8d6aab81c3aab');
INSERT INTO `sys_organ_role` VALUES ('4ec329625a1047ea87a8dfe8dd0750d1', '3f4f819d494c469295d197afea323233');
INSERT INTO `sys_organ_role` VALUES ('4ec329625a1047ea87a8dfe8dd0750d1', '68fd1413d3a148f7aa1fdfb0c2798544');
INSERT INTO `sys_organ_role` VALUES ('4ec329625a1047ea87a8dfe8dd0750d1', 'f6e67f1c5d9d4ac29fd8d6aab81c3aab');
INSERT INTO `sys_organ_role` VALUES ('7180f5a0c3624f4bb6fb758ab2c3bda6', '3f4f819d494c469295d197afea323233');
INSERT INTO `sys_organ_role` VALUES ('7180f5a0c3624f4bb6fb758ab2c3bda6', '68fd1413d3a148f7aa1fdfb0c2798544');
INSERT INTO `sys_organ_role` VALUES ('7180f5a0c3624f4bb6fb758ab2c3bda6', '695a97ae34b94d57899833a3ebcd656a');
INSERT INTO `sys_organ_role` VALUES ('7180f5a0c3624f4bb6fb758ab2c3bda6', 'f6e67f1c5d9d4ac29fd8d6aab81c3aab');
INSERT INTO `sys_organ_role` VALUES ('7dd6175254824c15aed1f99bce4b5f06', '68fd1413d3a148f7aa1fdfb0c2798544');
INSERT INTO `sys_organ_role` VALUES ('9e6a3c71bcd84d45807aa6641ab1a642', '3f4f819d494c469295d197afea323233');
INSERT INTO `sys_organ_role` VALUES ('9e6a3c71bcd84d45807aa6641ab1a642', '68fd1413d3a148f7aa1fdfb0c2798544');
INSERT INTO `sys_organ_role` VALUES ('9e6a3c71bcd84d45807aa6641ab1a642', 'f6e67f1c5d9d4ac29fd8d6aab81c3aab');
INSERT INTO `sys_organ_role` VALUES ('a694d140b8e44eb2baa5f26435c6a7f8', '3f4f819d494c469295d197afea323233');
INSERT INTO `sys_organ_role` VALUES ('a694d140b8e44eb2baa5f26435c6a7f8', '68fd1413d3a148f7aa1fdfb0c2798544');
INSERT INTO `sys_organ_role` VALUES ('a694d140b8e44eb2baa5f26435c6a7f8', 'f6e67f1c5d9d4ac29fd8d6aab81c3aab');
INSERT INTO `sys_organ_role` VALUES ('ff6d5335b1c7456e9a2e7734062820b2', '3f4f819d494c469295d197afea323233');
INSERT INTO `sys_organ_role` VALUES ('ff6d5335b1c7456e9a2e7734062820b2', '68fd1413d3a148f7aa1fdfb0c2798544');
INSERT INTO `sys_organ_role` VALUES ('ff6d5335b1c7456e9a2e7734062820b2', 'f6e67f1c5d9d4ac29fd8d6aab81c3aab');

-- ----------------------------
-- Table structure for `sys_role`
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
  `id` varchar(32) NOT NULL COMMENT '角色id',
  `bapid` varchar(32) DEFAULT NULL COMMENT '机构id',
  `name` varchar(64) DEFAULT NULL COMMENT '角色名称',
  `code` varchar(64) DEFAULT NULL COMMENT '角色代码',
  `status` varchar(6) DEFAULT NULL COMMENT '角色状态(0正常 1禁用）',
  `role_type` varchar(6) DEFAULT NULL COMMENT '角色类型',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `create_time` datetime DEFAULT NULL COMMENT '新增时间',
  `update_id` varchar(32) DEFAULT NULL COMMENT '更新人',
  `create_id` varchar(32) DEFAULT NULL COMMENT '新增人',
  `baid` varchar(255) DEFAULT NULL COMMENT '部门id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色表';

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES ('0c3245b565c84f639ab9e05a6d1eedfd', null, '北京分部', null, '0', null, '北京分部', '2017-11-16 21:35:29', '2017-08-16 13:51:21', null, '026a564bbfd84861ac4b65393644beef', null);
INSERT INTO `sys_role` VALUES ('68fd1413d3a148f7aa1fdfb0c2798544', 'a694d140b8e44eb2baa5f26435c6a7f8', 'xx', null, '-1', null, 'xxx', '2017-11-14 20:18:43', '2017-11-14 20:18:38', null, '026a564bbfd84861ac4b65393644beef', '4ec329625a1047ea87a8dfe8dd0750d1');
INSERT INTO `sys_role` VALUES ('695a97ae34b94d57899833a3ebcd656a', null, '成都总部', null, '0', null, '成都总部', '2017-11-17 14:40:12', '2017-07-20 11:49:42', null, '026a564bbfd84861ac4b65393644beef', null);

-- ----------------------------
-- Table structure for `sys_role_menu`
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu` (
  `role_id` varchar(32) NOT NULL COMMENT '角色id',
  `menu_id` varchar(32) NOT NULL COMMENT '权限id',
  PRIMARY KEY (`menu_id`,`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='权限角色表';

-- ----------------------------
-- Records of sys_role_menu
-- ----------------------------
INSERT INTO `sys_role_menu` VALUES ('0c3245b565c84f639ab9e05a6d1eedfd', '0');
INSERT INTO `sys_role_menu` VALUES ('695a97ae34b94d57899833a3ebcd656a', '0');
INSERT INTO `sys_role_menu` VALUES ('0c3245b565c84f639ab9e05a6d1eedfd', '0c410538dff54d35a1e19c7524c20e47');
INSERT INTO `sys_role_menu` VALUES ('695a97ae34b94d57899833a3ebcd656a', '0c410538dff54d35a1e19c7524c20e47');
INSERT INTO `sys_role_menu` VALUES ('0c3245b565c84f639ab9e05a6d1eedfd', '0c7df2638a464cd5b165547e07dd5409');
INSERT INTO `sys_role_menu` VALUES ('695a97ae34b94d57899833a3ebcd656a', '0c7df2638a464cd5b165547e07dd5409');
INSERT INTO `sys_role_menu` VALUES ('0c3245b565c84f639ab9e05a6d1eedfd', '0e6213b719bd424a9c794ee87f64333e');
INSERT INTO `sys_role_menu` VALUES ('695a97ae34b94d57899833a3ebcd656a', '0e6213b719bd424a9c794ee87f64333e');
INSERT INTO `sys_role_menu` VALUES ('0c3245b565c84f639ab9e05a6d1eedfd', '1');
INSERT INTO `sys_role_menu` VALUES ('695a97ae34b94d57899833a3ebcd656a', '1');
INSERT INTO `sys_role_menu` VALUES ('0c3245b565c84f639ab9e05a6d1eedfd', '14c58e44d6754c92806e1b97614c49b9');
INSERT INTO `sys_role_menu` VALUES ('695a97ae34b94d57899833a3ebcd656a', '14c58e44d6754c92806e1b97614c49b9');
INSERT INTO `sys_role_menu` VALUES ('0c3245b565c84f639ab9e05a6d1eedfd', '1515daa136ba41f783e82318f851d343');
INSERT INTO `sys_role_menu` VALUES ('695a97ae34b94d57899833a3ebcd656a', '1515daa136ba41f783e82318f851d343');
INSERT INTO `sys_role_menu` VALUES ('0c3245b565c84f639ab9e05a6d1eedfd', '15d2074a502f443cb760f613a40df598');
INSERT INTO `sys_role_menu` VALUES ('695a97ae34b94d57899833a3ebcd656a', '15d2074a502f443cb760f613a40df598');
INSERT INTO `sys_role_menu` VALUES ('0c3245b565c84f639ab9e05a6d1eedfd', '1cabf71492834ffa9eb5fb85aeca9f14');
INSERT INTO `sys_role_menu` VALUES ('695a97ae34b94d57899833a3ebcd656a', '1cabf71492834ffa9eb5fb85aeca9f14');
INSERT INTO `sys_role_menu` VALUES ('0c3245b565c84f639ab9e05a6d1eedfd', '1e7867f5ef3748bbaa2060b0192b8ff0');
INSERT INTO `sys_role_menu` VALUES ('695a97ae34b94d57899833a3ebcd656a', '1e7867f5ef3748bbaa2060b0192b8ff0');
INSERT INTO `sys_role_menu` VALUES ('0c3245b565c84f639ab9e05a6d1eedfd', '1eabb55ae2fa4f5e890c7739c1678e44');
INSERT INTO `sys_role_menu` VALUES ('695a97ae34b94d57899833a3ebcd656a', '1eabb55ae2fa4f5e890c7739c1678e44');
INSERT INTO `sys_role_menu` VALUES ('0c3245b565c84f639ab9e05a6d1eedfd', '1f936af47d064ab4989aadf6373e6502');
INSERT INTO `sys_role_menu` VALUES ('695a97ae34b94d57899833a3ebcd656a', '1f936af47d064ab4989aadf6373e6502');
INSERT INTO `sys_role_menu` VALUES ('0c3245b565c84f639ab9e05a6d1eedfd', '2');
INSERT INTO `sys_role_menu` VALUES ('695a97ae34b94d57899833a3ebcd656a', '2');
INSERT INTO `sys_role_menu` VALUES ('0c3245b565c84f639ab9e05a6d1eedfd', '239474f2afbf4196a6fc755c88be008d');
INSERT INTO `sys_role_menu` VALUES ('695a97ae34b94d57899833a3ebcd656a', '239474f2afbf4196a6fc755c88be008d');
INSERT INTO `sys_role_menu` VALUES ('0c3245b565c84f639ab9e05a6d1eedfd', '39cb0308a7fc4f7fb6c49440224ac2a4');
INSERT INTO `sys_role_menu` VALUES ('695a97ae34b94d57899833a3ebcd656a', '39cb0308a7fc4f7fb6c49440224ac2a4');
INSERT INTO `sys_role_menu` VALUES ('0c3245b565c84f639ab9e05a6d1eedfd', '39d9dd83a1cf4ea59a5571c7aa15927e');
INSERT INTO `sys_role_menu` VALUES ('695a97ae34b94d57899833a3ebcd656a', '39d9dd83a1cf4ea59a5571c7aa15927e');
INSERT INTO `sys_role_menu` VALUES ('0c3245b565c84f639ab9e05a6d1eedfd', '4');
INSERT INTO `sys_role_menu` VALUES ('695a97ae34b94d57899833a3ebcd656a', '4');
INSERT INTO `sys_role_menu` VALUES ('0c3245b565c84f639ab9e05a6d1eedfd', '4565ee01959c478d96c4071d3bb2a36f');
INSERT INTO `sys_role_menu` VALUES ('695a97ae34b94d57899833a3ebcd656a', '4565ee01959c478d96c4071d3bb2a36f');
INSERT INTO `sys_role_menu` VALUES ('0c3245b565c84f639ab9e05a6d1eedfd', '4e053e35bfed492fa6248e4888addd67');
INSERT INTO `sys_role_menu` VALUES ('695a97ae34b94d57899833a3ebcd656a', '4e053e35bfed492fa6248e4888addd67');
INSERT INTO `sys_role_menu` VALUES ('0c3245b565c84f639ab9e05a6d1eedfd', '6');
INSERT INTO `sys_role_menu` VALUES ('695a97ae34b94d57899833a3ebcd656a', '6');
INSERT INTO `sys_role_menu` VALUES ('0c3245b565c84f639ab9e05a6d1eedfd', '62bdcab5fe5f4e5fa7ab79fcd8cb47c4');
INSERT INTO `sys_role_menu` VALUES ('695a97ae34b94d57899833a3ebcd656a', '62bdcab5fe5f4e5fa7ab79fcd8cb47c4');
INSERT INTO `sys_role_menu` VALUES ('0c3245b565c84f639ab9e05a6d1eedfd', '690b7470fe394a6a8d9d0fa1340f18dc');
INSERT INTO `sys_role_menu` VALUES ('695a97ae34b94d57899833a3ebcd656a', '690b7470fe394a6a8d9d0fa1340f18dc');
INSERT INTO `sys_role_menu` VALUES ('0c3245b565c84f639ab9e05a6d1eedfd', '695388220b704cdaa72539c5f82fb254');
INSERT INTO `sys_role_menu` VALUES ('695a97ae34b94d57899833a3ebcd656a', '695388220b704cdaa72539c5f82fb254');
INSERT INTO `sys_role_menu` VALUES ('0c3245b565c84f639ab9e05a6d1eedfd', '7');
INSERT INTO `sys_role_menu` VALUES ('695a97ae34b94d57899833a3ebcd656a', '7');
INSERT INTO `sys_role_menu` VALUES ('0c3245b565c84f639ab9e05a6d1eedfd', '7cec2d46083c47bba323436cb409693f');
INSERT INTO `sys_role_menu` VALUES ('695a97ae34b94d57899833a3ebcd656a', '7cec2d46083c47bba323436cb409693f');
INSERT INTO `sys_role_menu` VALUES ('0c3245b565c84f639ab9e05a6d1eedfd', '80030434d10548968beaaeed79c3408b');
INSERT INTO `sys_role_menu` VALUES ('695a97ae34b94d57899833a3ebcd656a', '80030434d10548968beaaeed79c3408b');
INSERT INTO `sys_role_menu` VALUES ('0c3245b565c84f639ab9e05a6d1eedfd', '8b1f46f8ba6e455790a515c32e0329c5');
INSERT INTO `sys_role_menu` VALUES ('695a97ae34b94d57899833a3ebcd656a', '8b1f46f8ba6e455790a515c32e0329c5');
INSERT INTO `sys_role_menu` VALUES ('0c3245b565c84f639ab9e05a6d1eedfd', '90ca98befe9f4a67a07d5ab5fb2f3de3');
INSERT INTO `sys_role_menu` VALUES ('695a97ae34b94d57899833a3ebcd656a', '90ca98befe9f4a67a07d5ab5fb2f3de3');
INSERT INTO `sys_role_menu` VALUES ('0c3245b565c84f639ab9e05a6d1eedfd', '9d22510a1b1a40459619c5b2fdddfdc2');
INSERT INTO `sys_role_menu` VALUES ('695a97ae34b94d57899833a3ebcd656a', '9d22510a1b1a40459619c5b2fdddfdc2');
INSERT INTO `sys_role_menu` VALUES ('0c3245b565c84f639ab9e05a6d1eedfd', '9e8dc02fe5614580bdf5f6ca9a852b70');
INSERT INTO `sys_role_menu` VALUES ('695a97ae34b94d57899833a3ebcd656a', '9e8dc02fe5614580bdf5f6ca9a852b70');
INSERT INTO `sys_role_menu` VALUES ('0c3245b565c84f639ab9e05a6d1eedfd', 'ada02522ef7a44cea692541b358e8d1f');
INSERT INTO `sys_role_menu` VALUES ('695a97ae34b94d57899833a3ebcd656a', 'ada02522ef7a44cea692541b358e8d1f');
INSERT INTO `sys_role_menu` VALUES ('0c3245b565c84f639ab9e05a6d1eedfd', 'b7ab517a15b74dea812ee7ef32847a48');
INSERT INTO `sys_role_menu` VALUES ('695a97ae34b94d57899833a3ebcd656a', 'b7ab517a15b74dea812ee7ef32847a48');
INSERT INTO `sys_role_menu` VALUES ('0c3245b565c84f639ab9e05a6d1eedfd', 'bb5bc94a71a546aba5d9f813b6a352bb');
INSERT INTO `sys_role_menu` VALUES ('695a97ae34b94d57899833a3ebcd656a', 'bb5bc94a71a546aba5d9f813b6a352bb');
INSERT INTO `sys_role_menu` VALUES ('0c3245b565c84f639ab9e05a6d1eedfd', 'c898d73ff1fb49dc89909ed13452103a');
INSERT INTO `sys_role_menu` VALUES ('695a97ae34b94d57899833a3ebcd656a', 'c898d73ff1fb49dc89909ed13452103a');
INSERT INTO `sys_role_menu` VALUES ('0c3245b565c84f639ab9e05a6d1eedfd', 'ce4a2b7afad24616abd283f4741fe3e0');
INSERT INTO `sys_role_menu` VALUES ('695a97ae34b94d57899833a3ebcd656a', 'ce4a2b7afad24616abd283f4741fe3e0');
INSERT INTO `sys_role_menu` VALUES ('0c3245b565c84f639ab9e05a6d1eedfd', 'cefab951f1474646b9419828db47c362');
INSERT INTO `sys_role_menu` VALUES ('695a97ae34b94d57899833a3ebcd656a', 'cefab951f1474646b9419828db47c362');
INSERT INTO `sys_role_menu` VALUES ('0c3245b565c84f639ab9e05a6d1eedfd', 'f161e717976340e89c68b93abb87419d');
INSERT INTO `sys_role_menu` VALUES ('695a97ae34b94d57899833a3ebcd656a', 'f161e717976340e89c68b93abb87419d');
INSERT INTO `sys_role_menu` VALUES ('0c3245b565c84f639ab9e05a6d1eedfd', 'f4d418f7f9174338acc595151daa020f');
INSERT INTO `sys_role_menu` VALUES ('695a97ae34b94d57899833a3ebcd656a', 'f4d418f7f9174338acc595151daa020f');
INSERT INTO `sys_role_menu` VALUES ('0c3245b565c84f639ab9e05a6d1eedfd', 'f4f6af97585d401f918afedd3ca223e9');
INSERT INTO `sys_role_menu` VALUES ('695a97ae34b94d57899833a3ebcd656a', 'f4f6af97585d401f918afedd3ca223e9');
INSERT INTO `sys_role_menu` VALUES ('0c3245b565c84f639ab9e05a6d1eedfd', 'f7a850cb503c4b2cb5dc27081aca3325');
INSERT INTO `sys_role_menu` VALUES ('695a97ae34b94d57899833a3ebcd656a', 'f7a850cb503c4b2cb5dc27081aca3325');
INSERT INTO `sys_role_menu` VALUES ('0c3245b565c84f639ab9e05a6d1eedfd', 'f8524c36e2584ff5a2e374c3727c39c5');
INSERT INTO `sys_role_menu` VALUES ('695a97ae34b94d57899833a3ebcd656a', 'f8524c36e2584ff5a2e374c3727c39c5');
INSERT INTO `sys_role_menu` VALUES ('0c3245b565c84f639ab9e05a6d1eedfd', 'fdd70e08ae994de18009bc95f4f51fff');
INSERT INTO `sys_role_menu` VALUES ('695a97ae34b94d57899833a3ebcd656a', 'fdd70e08ae994de18009bc95f4f51fff');
INSERT INTO `sys_role_menu` VALUES ('0c3245b565c84f639ab9e05a6d1eedfd', 'feb235067fd7400090b0aa5451e4a5a4');
INSERT INTO `sys_role_menu` VALUES ('695a97ae34b94d57899833a3ebcd656a', 'feb235067fd7400090b0aa5451e4a5a4');

-- ----------------------------
-- Table structure for `sys_user`
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `id` varchar(32) NOT NULL COMMENT 'id主键',
  `bapid` varchar(32) DEFAULT NULL COMMENT '机构id',
  `user_name` varchar(64) DEFAULT NULL COMMENT '用户名',
  `login_name` varchar(64) NOT NULL COMMENT '登陆帐户',
  `pass_word` varchar(128) DEFAULT NULL COMMENT '密码',
  `create_time` datetime DEFAULT NULL COMMENT '新建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `status` varchar(6) DEFAULT NULL COMMENT '状态(1正常 -1禁用)',
  `phone` varchar(64) DEFAULT NULL COMMENT '电话',
  `photo` varchar(255) DEFAULT NULL COMMENT '头像',
  `email` varchar(128) DEFAULT NULL COMMENT '邮箱',
  `create_id` varchar(32) DEFAULT NULL COMMENT '创建者',
  `update_id` varchar(32) DEFAULT NULL COMMENT '更新者',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `baid` varchar(32) NOT NULL COMMENT '部门id',
  `salt` varchar(64) DEFAULT NULL COMMENT '盐',
  `password` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='系统用户表';

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES ('026a564bbfd84861ac4b65393644beef', 'a694d140b8e44eb2baa5f26435c6a7f8', '超级管理员', 'admin', 'f0bb6dd6de54011017e76100c2a4c098ecbaf1d0e2b0c8c418f2f91e95375ad3', '2017-04-27 21:51:49', '2019-05-22 12:23:43', '', null, null, null, null, '026a564bbfd84861ac4b65393644beef', null, '4ec329625a1047ea87a8dfe8dd0750d1', 'dQCXHbIgU6lhmEeqh7G3', '123456');
INSERT INTO `sys_user` VALUES ('7d3984d45aee4dbd9b85f4ef26f62bb8', '7180f5a0c3624f4bb6fb758ab2c3bda6', '测试数据权限', 'test', '8858d2de410ce66515b39b0f8edd68fcf79b040dd245b6508adedc8808c51f44', '2017-11-16 20:08:28', null, '', null, null, null, 'f68afc12a1924deaa2051154fe0efecc', null, null, '40ffec9b300c431e890c17beccf8e65c', 'iko0qMw7KJHdyYTbEQUg', 'test123');

-- ----------------------------
-- Table structure for `sys_user_role`
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role` (
  `user_id` varchar(32) NOT NULL COMMENT '用户id',
  `role_id` varchar(32) NOT NULL COMMENT '角色id',
  PRIMARY KEY (`role_id`,`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户角色关系表';

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
INSERT INTO `sys_user_role` VALUES ('f68afc12a1924deaa2051154fe0efecc', '695a97ae34b94d57899833a3ebcd656a');
