-- V1__init_tables.sql
-- 突发事件应急上报与指挥系统 数据库初始化脚本

-- 1. 用户信息表 (user)
CREATE TABLE `user` (
    `id` INT AUTO_INCREMENT COMMENT '主键ID',
    `userid` VARCHAR(64) NOT NULL UNIQUE COMMENT '用户微信唯一标识',
    `uname` VARCHAR(50) DEFAULT '新用户' COMMENT '用户名',
    `avatar` VARCHAR(255) DEFAULT NULL COMMENT '头像URL',
    `gender` INT DEFAULT 0 COMMENT '性别（0:未知, 1:男, 2:女）',
    `phone` VARCHAR(20) DEFAULT NULL COMMENT '手机号',
    `location` VARCHAR(500) DEFAULT NULL COMMENT '地区',
    `deptid` INT DEFAULT NULL COMMENT '机构ID',
    `status` INT DEFAULT 0 COMMENT '状态（0:正在使用, 1:已注销）',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户信息表';

-- 2. 管理员信息表 (admins)
CREATE TABLE `admins` (
    `id` INT AUTO_INCREMENT COMMENT '主键ID',
    `admin_name` VARCHAR(20) NOT NULL COMMENT '管理员姓名',
    `admin_password` VARCHAR(255) NOT NULL COMMENT '加密后的密码',
    `admin_avatar` VARCHAR(255) DEFAULT NULL COMMENT '头像URL',
    `admin_gender` INT DEFAULT 0 COMMENT '性别',
    `admin_phone` VARCHAR(20) DEFAULT NULL UNIQUE COMMENT '手机号(唯一标识)',
    `admin_email` VARCHAR(64) DEFAULT NULL COMMENT '邮箱',
    `admin_location` VARCHAR(500) DEFAULT NULL COMMENT '地区',
    `dept_id` INT DEFAULT NULL COMMENT '所属机构ID',
    `status` INT DEFAULT 1 COMMENT '状态（0:停用, 1:启用）',
    `remark` VARCHAR(256) DEFAULT NULL COMMENT '备注',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    INDEX `idx_admin_phone` (`admin_phone`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='管理员信息表';

-- 3. 机构信息表 (depts) 
CREATE TABLE `depts` (
    `id` INT AUTO_INCREMENT COMMENT '主键ID',
    `dept_name` VARCHAR(50) NOT NULL COMMENT '机构名称',
    `dept_code` VARCHAR(18) NOT NULL UNIQUE COMMENT '机构编码(18位)',
    `dept_status` INT NOT NULL DEFAULT 1 COMMENT '状态（0:停用, 1:启用）',
    `dept_person` VARCHAR(20) DEFAULT NULL COMMENT '联系人',
    `dept_phone` VARCHAR(20) DEFAULT NULL COMMENT '联系电话',
    `longitude` DOUBLE DEFAULT NULL COMMENT '经度',
    `latitude` DOUBLE DEFAULT NULL COMMENT '纬度',
    `dept_address` VARCHAR(500) DEFAULT NULL COMMENT '位置描述',
    `parent_dept` INT NOT NULL DEFAULT 0 COMMENT '父机构ID',
    `ancestor` VARCHAR(255) DEFAULT NULL COMMENT '祖级列表',
    `dept_responsibility` VARCHAR(256) DEFAULT NULL COMMENT '职责描述',
    PRIMARY KEY (`id`),
    INDEX `idx_dept_code` (`dept_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='机构信息表';

-- 4. 突发事件分类表 (incidents_category) 
CREATE TABLE `incidents_category` (
    `category_code` VARCHAR(20) NOT NULL COMMENT '分类代码',
    `category_name` VARCHAR(50) NOT NULL COMMENT '类别名称',
    `remark` VARCHAR(255) DEFAULT NULL COMMENT '说明',
    `parent_code` VARCHAR(20) DEFAULT NULL COMMENT '父代码',
    PRIMARY KEY (`category_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='事件分类表';

-- 5. 上报事件信息表 (reported_incidents)
CREATE TABLE `reported_incidents` (
    `id` INT AUTO_INCREMENT COMMENT '主键ID',
    `incident_title` VARCHAR(50) NOT NULL COMMENT '事件标题',
    `incident_type` INT DEFAULT 0 COMMENT '类型(1:自然灾害等)',
    `incident_content` TEXT COMMENT '事件详情',
    `incident_location` VARCHAR(500) NOT NULL COMMENT '发生位置',
    `incident_range` INT DEFAULT NULL COMMENT '影响范围(km)',
    `occurrence_time` DATETIME NOT NULL COMMENT '发生时间',
    `longitude` DOUBLE DEFAULT NULL,
    `latitude` DOUBLE DEFAULT NULL,
    `incident_status` INT NOT NULL COMMENT '状态(2:待审核, 3:通过等)',
    `admin_id` INT DEFAULT NULL COMMENT '审核管理员ID',
    `review` VARCHAR(256) DEFAULT NULL COMMENT '审核意见',
    `user_id` INT NOT NULL COMMENT '上报人ID',
    `deleted` INT DEFAULT 0 COMMENT '逻辑删除',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    INDEX `idx_reported_user` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='公众上报事件表';

-- 6. 发布事件信息表 (published_incidents)
CREATE TABLE `published_incidents` (
    `id` INT AUTO_INCREMENT COMMENT '主键ID',
    `incident_title` VARCHAR(50) NOT NULL UNIQUE COMMENT '事件标题',
    `incident_type` INT DEFAULT NULL,
    `incident_level` INT DEFAULT NULL COMMENT '严重程度(1-4)',
    `incident_content` TEXT,
    `incident_location` VARCHAR(500),
    `longitude` DOUBLE,
    `latitude` DOUBLE,
    `incident_range` INT,
    `occurrence_time` DATETIME,
    `incident_status` INT NOT NULL COMMENT '状态(2:待发布, 3:已发布)',
    `remark` VARCHAR(255),
    `admin_id` INT NOT NULL COMMENT '创建者ID',
    `publish_id` INT DEFAULT NULL COMMENT '发布者ID',
    `deleted` INT DEFAULT 0,
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='官方发布事件表';

-- 7. 仓库信息表 (warehouses)
CREATE TABLE `warehouses` (
    `id` INT AUTO_INCREMENT PRIMARY KEY,
    `warehouse_code` VARCHAR(20) NOT NULL UNIQUE COMMENT '仓库编码',
    `warehouse_name` VARCHAR(50) NOT NULL COMMENT '仓库名称',
    `warehouse_address` VARCHAR(500),
    `longitude` DOUBLE,
    `latitude` DOUBLE,
    `warehouse_person` VARCHAR(20),
    `warehouse_phone` VARCHAR(20),
    `warehouse_status` INT NOT NULL DEFAULT 1,
    `remark` VARCHAR(256),
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='仓库信息表';

-- 8. 物资信息表 (materials)
CREATE TABLE `materials` (
    `id` INT AUTO_INCREMENT UNIQUE,
    `material_code` VARCHAR(20) NOT NULL COMMENT '物资编码',
    `material_name` VARCHAR(50) NOT NULL COMMENT '物资名称',
    `material_category` VARCHAR(20) COMMENT '物资类型编码',
    `material_warehouse` VARCHAR(20) COMMENT '仓库编码',
    `material_batch_no` VARCHAR(50) COMMENT '批次号',
    `produced_time` DATETIME COMMENT '生产日期',
    `effective_time` DATETIME COMMENT '有效日期',
    `stock_quantity` INT DEFAULT 0 COMMENT '当前库存',
    `security_quantity` INT DEFAULT 0 COMMENT '安全库存',
    `specification` VARCHAR(50) COMMENT '规格型号',
    `unit` VARCHAR(20) COMMENT '计量单位',
    `manufacturer` VARCHAR(50) COMMENT '生产厂家',
    `material_status` INT DEFAULT 1,
    `remark` VARCHAR(255),
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`material_code`),
    INDEX `idx_mat_warehouse` (`material_warehouse`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='应急物资表';

-- 9. 物资出入库记录表 (material_inout)
CREATE TABLE `material_inout` (
    `id` INT AUTO_INCREMENT PRIMARY KEY,
    `material_id` INT NOT NULL COMMENT '物资ID',
    `warehouse_id` INT NOT NULL COMMENT '仓库ID',
    `in_out` INT NOT NULL DEFAULT 0 COMMENT '0:出库, 1:入库',
    `num` INT NOT NULL COMMENT '数量',
    `remark` VARCHAR(255),
    `admin_id` INT NOT NULL COMMENT '操作员ID',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='出入库明细表';

-- 物资分类表
CREATE TABLE IF NOT EXISTS material_category (
    CATEGORY_CODE VARCHAR(20) NOT NULL COMMENT '代码',
    CATEGORY_NAME VARCHAR(255) DEFAULT NULL COMMENT '类别名称',
    REMARK VARCHAR(1000) DEFAULT NULL COMMENT '说明',
    PARENT_CODE VARCHAR(20) DEFAULT NULL COMMENT '父代码',
    PRIMARY KEY (CATEGORY_CODE)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='应急物资分类表(GB/T 38565-2020)';

-- 10. 应急预案表 (plans)
CREATE TABLE `plans` (
    `id` INT AUTO_INCREMENT PRIMARY KEY,
    `plan_code` VARCHAR(20) NOT NULL COMMENT '预案编码',
    `plan_title` VARCHAR(50) NOT NULL COMMENT '预案名称',
    `plan_type` INT COMMENT '预案类型',
    `category_code` VARCHAR(20) COMMENT '适用事件类型',
    `plan_level` INT COMMENT '事件等级',
    `dept_id` INT COMMENT '发布机构',
    `publish_time` DATETIME,
    `plan_status` INT DEFAULT 1,
    `plan_url` VARCHAR(255) COMMENT '访问地址',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='应急预案表';

-- 11. 附件关联表
CREATE TABLE `reported_attachments` (
    `id` INT AUTO_INCREMENT PRIMARY KEY,
    `attachment_name` VARCHAR(128) COMMENT '文件名',
    `attachment_url` VARCHAR(255) COMMENT '文件路径',
    `attachment_type` INT COMMENT '1:图片, 2:视频, 3:文档',
    `attachment_size` INT COMMENT '大小(KB)',
    `target_id` INT NOT NULL COMMENT '关联的业务ID(上报/发布/预案ID)',
    `target_type` VARCHAR(20) NOT NULL COMMENT '业务类型(REPORT/PUBLISH/PLAN)',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统附件表';

CREATE TABLE `published_attachments` (
    `id` INT AUTO_INCREMENT PRIMARY KEY,
    `attachment_name` VARCHAR(128) COMMENT '文件名',
    `attachment_url` VARCHAR(255) COMMENT '文件路径',
    `attachment_type` INT COMMENT '1:图片, 2:视频, 3:文档',
    `attachment_size` INT COMMENT '大小(KB)',
    `target_id` INT NOT NULL COMMENT '关联的业务ID(上报/发布/预案ID)',
    `target_type` VARCHAR(20) NOT NULL COMMENT '业务类型(REPORT/PUBLISH/PLAN)',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统附件表';

CREATE TABLE `plan_attachments` (
    `id` INT AUTO_INCREMENT PRIMARY KEY,
    `attachment_name` VARCHAR(128) COMMENT '文件名',
    `attachment_url` VARCHAR(255) COMMENT '文件路径',
    `attachment_type` INT COMMENT '1:图片, 2:视频, 3:文档',
    `attachment_size` INT COMMENT '大小(KB)',
    `target_id` INT NOT NULL COMMENT '关联的业务ID(上报/发布/预案ID)',
    `target_type` VARCHAR(20) NOT NULL COMMENT '业务类型(REPORT/PUBLISH/PLAN)',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统附件表';

CREATE TABLE `news_attachments` (
    `id` INT AUTO_INCREMENT PRIMARY KEY,
    `attachment_name` VARCHAR(128) COMMENT '文件名',
    `attachment_url` VARCHAR(255) COMMENT '文件路径',
    `attachment_type` INT COMMENT '1:图片, 2:视频, 3:文档',
    `attachment_size` INT COMMENT '大小(KB)',
    `target_id` INT NOT NULL COMMENT '关联的业务ID(上报/发布/预案ID)',
    `target_type` VARCHAR(20) NOT NULL COMMENT '业务类型(REPORT/PUBLISH/PLAN)',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统附件表';

-- 12. 新闻信息表 (news)
CREATE TABLE `news` (
    `id` INT AUTO_INCREMENT PRIMARY KEY,
    `news_name` VARCHAR(50) NOT NULL COMMENT '新闻名称',
    `publish_time` DATETIME,
    `publish_company` VARCHAR(50) COMMENT '发布机构',
    `news_photo` VARCHAR(255) COMMENT '配图URL',
    `news_url` VARCHAR(255) NOT NULL COMMENT '链接',
    `news_status` INT DEFAULT 1 COMMENT '1:待发布, 2:已发布',
    `deleted` INT DEFAULT 0,
    `admin_id` INT COMMENT '操作管理员',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='新闻信息表';