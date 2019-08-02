CREATE TABLE `consumer` (
  `id` INT(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `username` VARCHAR(20) DEFAULT NULL,
  `wechat_openid` VARCHAR(50) DEFAULT NULL,
  `phone` VARCHAR(20) DEFAULT NULL,
  `nickname` VARCHAR(50) DEFAULT NULL,
  `avatar_url` VARCHAR(1000) DEFAULT NULL,
  `gender` INT(2) DEFAULT NULL COMMENT '0:unknow 1:male 2:female',
  `email` VARCHAR(100) DEFAULT NULL,
  `deleted` TINYINT(1) DEFAULT '0' COMMENT '0:normal 1:deleted',
  `created_by` BIGINT(20) DEFAULT NULL,
  `created_at` BIGINT(20) DEFAULT NULL,
  `updated_by` BIGINT(20) DEFAULT NULL,
  `updated_at` BIGINT(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;