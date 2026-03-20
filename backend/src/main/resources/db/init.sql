CREATE TABLE `sys_role` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '角色ID',
  `role_code` varchar(50) NOT NULL COMMENT '角色编码(ADMIN, TEACHER, STUDENT)',
  `role_name` varchar(50) NOT NULL COMMENT '角色名称',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_role_code` (`role_code`)
);

CREATE TABLE `sys_user` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `username` varchar(50) NOT NULL COMMENT '学号/工号/用户名',
  `password` varchar(100) NOT NULL COMMENT '密码',
  `real_name` varchar(50) NOT NULL COMMENT '真实姓名',
  `role_id` bigint NOT NULL COMMENT '关联角色ID',
  `status` tinyint(1) DEFAULT '1' COMMENT '状态(1:正常, 0:禁用)',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`)
);

INSERT INTO `sys_role` (`role_code`, `role_name`) VALUES 
('ADMIN', '管理员'), ('TEACHER', '指导老师'), ('STUDENT', '学生');

INSERT INTO `sys_user` (`username`, `password`, `real_name`, `role_id`, `status`) 
VALUES ('admin', '123456', '超级管理员', 1, 1);

CREATE TABLE `sys_topic` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '课题ID',
  `title` varchar(100) NOT NULL COMMENT '课题名称',
  `summary` varchar(500) DEFAULT NULL COMMENT '课题简介/目标',
  `require_desc` text COMMENT '任务与要求',
  `teacher_id` bigint NOT NULL COMMENT '出题教师ID',
  `source` varchar(50) DEFAULT NULL COMMENT '课题来源',
  `type` varchar(50) DEFAULT NULL COMMENT '课题类型',
  `max_students` int DEFAULT '1' COMMENT '限选人数',
  `status` tinyint DEFAULT '0' COMMENT '审核状态(0:待审核, 1:已通过, 2:已驳回)',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
);

-- 插入一条测试数据
INSERT INTO `sys_topic` (`title`, `summary`, `require_desc`, `teacher_id`, `source`, `type`, `max_students`, `status`)
VALUES ('基于大语言模型的智能客服系统', '设计并实现一个支持上下文的智能客服', '熟练掌握Vue3和SpringBoot', 1, '自拟', '软件开发', 1, 1);
