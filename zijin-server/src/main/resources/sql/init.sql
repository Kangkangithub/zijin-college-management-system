-- 紫金学院管理系统数据库初始化脚本
CREATE DATABASE IF NOT EXISTS zijin DEFAULT CHARSET utf8mb4;
USE zijin;

-- 角色表
DROP TABLE IF EXISTS t_role;
CREATE TABLE t_role (
    id INT AUTO_INCREMENT PRIMARY KEY,
    role_name VARCHAR(50) NOT NULL,
    role_code VARCHAR(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO t_role VALUES (1, '管理员', 'admin'), (2, '教师', 'teacher'), (3, '学生', 'student');

-- 部门表
DROP TABLE IF EXISTS t_department;
CREATE TABLE t_department (
    id INT AUTO_INCREMENT PRIMARY KEY,
    dept_name VARCHAR(100) NOT NULL,
    dept_code VARCHAR(50),
    description VARCHAR(255),
    create_time VARCHAR(20)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO t_department VALUES
(1, '计算机学院', 'CS', '计算机科学与技术相关专业', '2024-01-01 00:00:00'),
(2, '电子工程学院', 'EE', '电子信息工程相关专业', '2024-01-01 00:00:00'),
(3, '经济管理学院', 'EM', '经济管理相关专业', '2024-01-01 00:00:00'),
(4, '外国语学院', 'FL', '外语相关专业', '2024-01-01 00:00:00'),
(5, '人文与社会科学学院', 'HSS', '人文社科相关专业', '2024-01-01 00:00:00');

-- 专业表
DROP TABLE IF EXISTS t_major;
CREATE TABLE t_major (
    id INT AUTO_INCREMENT PRIMARY KEY,
    major_name VARCHAR(100) NOT NULL,
    major_code VARCHAR(50),
    dept_id INT,
    description VARCHAR(255)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO t_major VALUES
(1, '计算机科学与技术', 'CST', 1, '计算机科学与技术专业'),
(2, '软件工程', 'SE', 1, '软件工程专业'),
(3, '数据科学与大数据技术', 'DS', 1, '大数据专业'),
(4, '电子信息工程', 'EIE', 2, '电子信息工程专业'),
(5, '通信工程', 'CE', 2, '通信工程专业'),
(6, '工商管理', 'BA', 3, '工商管理专业'),
(7, '会计学', 'ACC', 3, '会计学专业'),
(8, '英语', 'ENG', 4, '英语专业'),
(9, '法学', 'LAW', 5, '法学专业');

-- 班级表
DROP TABLE IF EXISTS t_class;
CREATE TABLE t_class (
    id INT AUTO_INCREMENT PRIMARY KEY,
    class_name VARCHAR(100) NOT NULL,
    class_code VARCHAR(50),
    major_id INT,
    grade VARCHAR(20),
    description VARCHAR(255)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO t_class VALUES
(1, '计算机2201班', 'CST2201', 1, '2022级', '计算机科学与技术2022级1班'),
(2, '计算机2202班', 'CST2202', 1, '2022级', '计算机科学与技术2022级2班'),
(3, '软件2201班', 'SE2201', 2, '2022级', '软件工程2022级1班'),
(4, '大数据2201班', 'DS2201', 3, '2022级', '大数据2022级1班'),
(5, '电信2201班', 'EIE2201', 4, '2022级', '电子信息工程2022级1班'),
(6, '通信2201班', 'CE2201', 5, '2022级', '通信工程2022级1班'),
(7, '工商2201班', 'BA2201', 6, '2022级', '工商管理2022级1班'),
(8, '会计2201班', 'ACC2201', 7, '2022级', '会计学2022级1班'),
(9, '英语2201班', 'ENG2201', 8, '2022级', '英语2022级1班'),
(10, '法学2201班', 'LAW2201', 9, '2022级', '法学2022级1班');

-- 用户表
DROP TABLE IF EXISTS t_user;
CREATE TABLE t_user (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    pwd VARCHAR(100) NOT NULL,
    real_name VARCHAR(50),
    role_id INT DEFAULT 3,
    grade VARCHAR(20),
    major_id INT,
    class_id INT,
    phone VARCHAR(20),
    email VARCHAR(100),
    status INT DEFAULT 1,
    create_time VARCHAR(20)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 测试账号
INSERT INTO t_user VALUES
(1, 'admin', '123456', '系统管理员', 1, NULL, NULL, NULL, '13800000001', 'admin@zijin.edu.cn', 1, '2024-01-01 00:00:00'),
(2, 'teacher1', '123456', '张教授', 2, NULL, 2, NULL, '13800000002', 'zhang@zijin.edu.cn', 1, '2024-01-01 00:00:00'),
(3, 'teacher2', '123456', '李老师', 2, NULL, 3, NULL, '13800000003', 'li@zijin.edu.cn', 1, '2024-01-01 00:00:00'),
(4, 'student1', '123456', '王同学', 3, '2022级', 1, 1, '13800000004', 'wang@zijin.edu.cn', 1, '2024-01-01 00:00:00'),
(5, 'student2', '123456', '赵同学', 3, '2022级', 2, 3, '13800000005', 'zhao@zijin.edu.cn', 1, '2024-01-01 00:00:00'),
(6, 'student3', '123456', '陈同学', 3, '2022级', 1, 2, '13800000006', 'chen@zijin.edu.cn', 1, '2024-01-01 00:00:00');
