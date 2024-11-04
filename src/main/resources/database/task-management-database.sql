DROP SCHEMA IF EXISTS `task-management-database`;

CREATE SCHEMA `task-management-database`;

use `task-management-database`;

SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS `users`;
DROP TABLE IF EXISTS `tasks`;
DROP TABLE IF EXISTS `admins`;


CREATE TABLE users (
  `id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(45) DEFAULT NULL,
  `password` char(68) NOT NULL,
  `email` varchar(45) DEFAULT NULL,
  `role` varchar(45) DEFAULT 'ROLE_USER',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;



INSERT INTO `users` (username, password, email, role) 
VALUES 
('matt', '$2a$10$BpWABldCaTIE3FcpqCzVxOAk.915XZnT485DXQfbgiOzI61HVZMi6', 'matt@gmail.com', 'ROLE_USER'),
('admin', '$2a$10$Lmo6DDOSPezP2e0BV0KK8ebD.Z2qzjRy8gvIizW5G/CsWZz253fIS', 'admin@gmail.com', 'ROLE_ADMIN');

CREATE TABLE tasks (
  `id` int NOT NULL AUTO_INCREMENT,
  `title` varchar(128) DEFAULT NULL,
  `description` varchar(256) DEFAULT NULL,
  `priority` int DEFAULT 1,
  `user_id` int,
  PRIMARY KEY (`id`),

  CONSTRAINT `FK_USER`
  FOREIGN KEY (`user_id`) 
  REFERENCES `users` (`id`) 
  ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=latin1;

INSERT INTO tasks (title, description, priority, user_id)
VALUES ('workout', '30 push-ups and 20 squats and 15 burpees', 3, 1);


SET FOREIGN_KEY_CHECKS = 1;
