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

CREATE TABLE `sys_student_topic` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `student_id` bigint NOT NULL COMMENT '学生ID(关联sys_user)',
  `topic_id` bigint NOT NULL COMMENT '课题ID(关联sys_topic)',
  `status` tinyint DEFAULT '0' COMMENT '申请状态(0:待审核, 1:已同意, 2:已拒绝)',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '申请时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_student_topic` (`student_id`, `topic_id`)
);

-- 插入一条测试数据: admin(其实是教师角色) 拥有 topic(id=1), 这里模拟一个学生账号去申请
INSERT INTO `sys_user` (`username`, `password`, `real_name`, `role_id`, `status`) 
VALUES ('student01', '123456', '张三同学', 3, 1);

CREATE TABLE `sys_proposal` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '开题报告ID',
  `student_id` bigint NOT NULL COMMENT '学生ID',
  `topic_id` bigint NOT NULL COMMENT '课题ID',
  `content` text COMMENT '开题报告正文',
  `status` tinyint DEFAULT '0' COMMENT '审核状态(0:待审核, 1:已通过, 2:已驳回)',
  `teacher_comment` varchar(500) DEFAULT NULL COMMENT '指导老师意见',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '提交时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_student_topic_proposal` (`student_id`, `topic_id`)
);

CREATE TABLE `sys_weekly_report` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `student_id` bigint NOT NULL,
  `topic_id` bigint NOT NULL,
  `week_num` int NOT NULL COMMENT '周次',
  `content` text COMMENT '周志内容',
  `status` tinyint DEFAULT '0' COMMENT '0:待审核, 1:已审阅, 2:需修改',
  `teacher_comment` varchar(500),
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
);

CREATE TABLE `sys_thesis` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `student_id` bigint NOT NULL,
  `topic_id` bigint NOT NULL,
  `file_name` varchar(200) COMMENT '文件名',
  `file_url` varchar(500) COMMENT '文件路径/网盘链接',
  `type` tinyint COMMENT '0:初稿, 1:定稿',
  `status` tinyint DEFAULT '0' COMMENT '0:待审核, 1:通过, 2:打回',
  `score` decimal(5,2) COMMENT '成绩',
  `teacher_comment` text,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
);
