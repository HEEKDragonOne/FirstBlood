
/*创建数据库*/
CREATE DATABASE `tothefor` ;

use tothefor;
/*创建表*/
# 如果有关数据库的报错问题，多半是数据库的编码问题。将其设置为utf8即可


/*物品表*/
CREATE TABLE `s_item` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '物品id',
  `item_id` varchar(100) NOT NULL COMMENT '物品编号',
  `name` varchar(200) NOT NULL COMMENT '物品名',
  `typeID` bigint NOT NULL COMMENT '物品类型编号',
  `is_show` tinyint(1) DEFAULT '1' COMMENT '启用状态',
  `is_delete` tinyint(1) DEFAULT '0' COMMENT '是否删除',
  `item_add_time` datetime DEFAULT NULL COMMENT '物品入库时间',
  `item_update_time` timestamp NULL DEFAULT NULL COMMENT '最后修改时间',
  `size` varchar(155) DEFAULT NULL COMMENT '物品规格',
  `description` varchar(200) DEFAULT NULL COMMENT '物品描述或备注',
  `item_count` bigint DEFAULT '0' COMMENT '物品数量',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


# 物品类型表
CREATE TABLE `s_item_type` (
   `id` bigint NOT NULL AUTO_INCREMENT COMMENT '唯一标识符',
   `type_id` bigint NOT NULL COMMENT '物品编号',
   `typename` varchar(50) NOT NULL COMMENT '物品类型名称',
   `is_delete` tinyint(1) DEFAULT '0' COMMENT '逻辑删除',
   `description` varchar(250) DEFAULT NULL COMMENT '类型描述',
   PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

# 菜单表
CREATE TABLE `s_menu` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '唯一标识符',
  `menu_id` bigint DEFAULT NULL COMMENT '菜单编号',
  `menu_name` varchar(250) DEFAULT NULL COMMENT '菜单名称',
  `menu_path` varchar(250) DEFAULT NULL COMMENT '菜单路径',
  `menu_ico` varchar(200) DEFAULT NULL COMMENT '菜单图表',
  `pid` bigint DEFAULT NULL COMMENT '父级菜单id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

# 借出管理表
CREATE TABLE `s_record` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '记录单唯一表示符',
    `record_name` varchar(200) DEFAULT NULL COMMENT '记录单名称',
    `is_show` tinyint(1) DEFAULT '1' COMMENT '归还状态',
    `is_delete` tinyint(1) DEFAULT '0' COMMENT '是否删除',
    `record_add_time` datetime DEFAULT NULL COMMENT '添加时间',
    `record_update_time` timestamp NULL DEFAULT NULL COMMENT '修改时间',
    `record_man` varchar(200) DEFAULT NULL COMMENT '借用者',
    `description` varchar(200) DEFAULT NULL COMMENT '描述或备注',
    `getCode` varchar(10) DEFAULT NULL COMMENT '领取码',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

# 角色表
CREATE TABLE `s_role_menu` (
   `id` bigint NOT NULL AUTO_INCREMENT COMMENT '唯一标识符',
   `role_id` bigint DEFAULT NULL COMMENT '角色编号',
   `menu_id` bigint DEFAULT NULL COMMENT '菜单编号',
   PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

# 用户表

CREATE TABLE `s_user` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '用户唯一标识符',
  `user_ID` bigint NOT NULL COMMENT '用户编号',
  `username` varchar(30) NOT NULL COMMENT '用户名称',
  `password` varchar(255) DEFAULT NULL COMMENT '用户密码',
  `role_ID` bigint NOT NULL COMMENT '用户角色ID',
  `description` varchar(250) DEFAULT NULL COMMENT '用户描述',
  `is_delete` tinyint(1) DEFAULT '0' COMMENT '逻辑删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

# 用户角色表
CREATE TABLE `s_user_role` (
   `id` bigint NOT NULL AUTO_INCREMENT COMMENT '唯一标识符',
   `role_id` bigint NOT NULL COMMENT '角色编号',
   `rolename` varchar(50) NOT NULL COMMENT '角色名称',
   `is_delete` tinyint(1) DEFAULT '0' COMMENT '逻辑删除',
   `description` varchar(250) DEFAULT NULL COMMENT '角色描述',
   PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;






# 初始数据添加


# 菜单表
insert into s_menu(menu_name, menu_path, menu_ico, pid) VALUES
('物品管理',null,'IconMenu',null),
('权限设置',null,'Lock',null),
('系统设置',null,'Setting',null),
('物品列表','/itemShow','List',1),
('物品类型','/itemType','Tickets',1),
('角色管理','/role','User',2),
('人员管理','/mans','Avatar',2),
('修改密码','/pwd','Key',3),
('个人密码','/perPwd','User',3),
('借出管理','/recordShow','Document',1),
('数据展示','/lookData','Calendar',1);

# 菜单权限表
insert into s_role_menu(role_id, menu_id) VALUES
(2,1),
(2,2),
(2,3),
(2,4),
(2,5),
(2,6),
(2,7),
(2,8),
(2,9),
(2,10),
(2,11);


# 创建用户角色
insert into s_user_role(role_id, rolename,description) VALUES
(2,'超级管理员','拥有最高权限');

# 创建初始超级管理员admin,角色必须是角色表中已经存在的角色id，密码为123456
insert into s_user(user_ID, username, password, role_ID, description) VALUES
(1000,'admin','7ed917eb25c9fc56499a76bf5adbfa50',2,'超级管理员');

# 插入物品类型数据
insert into s_item_type(type_id, typename, description) VALUES
(2022001,'测试类','提供测试所用');

# 插入物品数据，物品的类型编号必须是类型表中已经存在的！
insert into s_item(item_id, name, typeID,size, description, item_count) VALUES
('111111','测试物品名称',2022001,'这里填写规格','填写物品描述',7);


# 注意：在建好数据库表后，自行查看s_user_role表的id和role_id是否相同，则将role_id修改为id，使两者相同。
# 注意：再自行查看s_menu表，看数据的id是不是从1开始的，不是则将其改为从1开始。





