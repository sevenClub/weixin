CREATE TABLE `order_item` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `project_id` varchar(8) NOT NULL COMMENT '项目id',
  `curr_Num` int(2) DEFAULT NULL COMMENT '已经有人数',
  `total_Num` int(2) DEFAULT NULL COMMENT '总人数',
  `is_full` varchar(1) DEFAULT NULL COMMENT '是否满员,0满员 1 未满员',
  `game_status` varchar(1) DEFAULT NULL COMMENT '是否终止，0开始游戏 1 招募中',
  `end_price` decimal(15,2) DEFAULT NULL COMMENT '最终价格',
  `game_location` varchar(50) DEFAULT NULL COMMENT '场地位置',
  `order_status` varchar(2) DEFAULT NULL COMMENT '订单状态，0 组队中 1 待参加 2 已结束',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` varchar(20) DEFAULT NULL COMMENT '更新时间',
  `acture_start_tm` varchar(20) DEFAULT NULL COMMENT '订单实际开始时间',
  `end_time` varchar(20) DEFAULT NULL COMMENT '订单终止时间',
  `sport_title` varchar(20) DEFAULT NULL COMMENT '活动标题',
  `project_cost` decimal(15,2) DEFAULT NULL COMMENT '订单原价格',
  `fee_tags` varchar(3) DEFAULT NULL COMMENT '费用类型,OO 免费 AA',
  `activity_hour` decimal(3,1) DEFAULT NULL COMMENT '订单活动时间,计时',
  `description` varchar(50) DEFAULT NULL COMMENT '订单描述',
  `contact_dir` varchar(50) DEFAULT NULL COMMENT '是否全支持',
  `start_wechat_openid` varchar(50) DEFAULT NULL COMMENT '发起人openid',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=88 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='订单表';
CREATE TABLE `order_details` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `wechat_openid` varchar(50) NOT NULL COMMENT '参赛人员openid',
  `is_active` varchar(2) DEFAULT NULL COMMENT '是否退出',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` varchar(20) DEFAULT NULL COMMENT '更新时间',
  `project_fee` decimal(15,2) DEFAULT NULL COMMENT '费用',
  `order_id` int(11) DEFAULT NULL,
  `contact_dir` varchar(50) DEFAULT NULL COMMENT '参与人的联系方式',
  `is_captain` varchar(1) DEFAULT NULL COMMENT '是否队长',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=85 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='订单详情表';
CREATE TABLE `project_item` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `project_id` varchar(8) NOT NULL DEFAULT '' COMMENT '项目id',
  `project_name` varchar(20) DEFAULT NULL COMMENT '项目名称',
  `total_Num` int(11) DEFAULT NULL COMMENT '参赛人数',
  `project_cost` decimal(15,2) DEFAULT NULL COMMENT '价格费用',
  `discount` decimal(15,2) DEFAULT NULL COMMENT '优惠力度',
  `end_price` decimal(15,2) DEFAULT NULL COMMENT '最终价格',
  `game_location` varchar(50) DEFAULT NULL COMMENT '场地位置',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` varchar(20) DEFAULT NULL COMMENT '更新时间',
  `open_time` varchar(50) DEFAULT NULL COMMENT '场地开放时间',
  `sport_active_tm` varchar(3) DEFAULT NULL COMMENT '运动活动时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=85 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='运动项目表';

CREATE TABLE `consumer` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `username` varchar(20) DEFAULT NULL,
  `wechat_openid` varchar(50) DEFAULT NULL,
  `phone` varchar(20) DEFAULT NULL,
  `nickname` varchar(50) DEFAULT NULL,
  `avatar_url` varchar(1000) DEFAULT NULL,
  `gender` int(2) DEFAULT NULL COMMENT '0:unknow 1:male 2:female',
  `email` varchar(100) DEFAULT NULL,
  `deleted` tinyint(1) DEFAULT '0' COMMENT '0:normal 1:deleted',
  `created_by` bigint(20) DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `updated_by` bigint(20) DEFAULT NULL,
  `updated_at` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4;