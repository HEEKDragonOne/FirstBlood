
/*创建数据库*/
CREATE DATABASE `tothefor` ;

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
) ENGINE=InnoDB AUTO_INCREMENT=70 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


# 物品类型表
CREATE TABLE `s_item_type` (
   `id` bigint NOT NULL AUTO_INCREMENT COMMENT '唯一标识符',
   `type_id` bigint NOT NULL COMMENT '物品编号',
   `typename` varchar(50) NOT NULL COMMENT '物品类型名称',
   `is_delete` tinyint(1) DEFAULT '0' COMMENT '逻辑删除',
   `description` varchar(250) DEFAULT NULL COMMENT '类型描述',
   PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

# 菜单表
CREATE TABLE `s_menu` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '唯一标识符',
  `menu_id` bigint DEFAULT NULL COMMENT '菜单编号',
  `menu_name` varchar(250) DEFAULT NULL COMMENT '菜单名称',
  `menu_path` varchar(250) DEFAULT NULL COMMENT '菜单路径',
  `menu_ico` varchar(200) DEFAULT NULL COMMENT '菜单图表',
  `pid` bigint DEFAULT NULL COMMENT '父级菜单id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

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
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

# 角色表
CREATE TABLE `s_role_menu` (
   `id` bigint NOT NULL AUTO_INCREMENT COMMENT '唯一标识符',
   `role_id` bigint DEFAULT NULL COMMENT '角色编号',
   `menu_id` bigint DEFAULT NULL COMMENT '菜单编号',
   PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=175 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

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
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

# 用户角色表
CREATE TABLE `s_user_role` (
   `id` bigint NOT NULL AUTO_INCREMENT COMMENT '唯一标识符',
   `role_id` bigint NOT NULL COMMENT '角色编号',
   `rolename` varchar(50) NOT NULL COMMENT '角色名称',
   `is_delete` tinyint(1) DEFAULT '0' COMMENT '逻辑删除',
   `description` varchar(250) DEFAULT NULL COMMENT '角色描述',
   PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
