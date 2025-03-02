-- Active: 1740272039236@@127.0.0.1@3306



DROP DATABASE IF EXISTS tarea2;
CREATE DATABASE tarea2 CHARACTER SET utf8 COLLATE utf8_general_ci;
USE tarea2;

CREATE TABLE usuarios (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(64) NOT NULL,
    email VARCHAR(64) NOT NULL UNIQUE,
    password VARCHAR(128) NOT NULL
);
CREATE TABLE roles (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(64) NOT NULL UNIQUE
);

CREATE TABLE usuario_roles (
    usuario_id BIGINT,
    rol_id BIGINT,
    PRIMARY KEY (usuario_id, rol_id),
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id),
    FOREIGN KEY (rol_id) REFERENCES roles(id)
);

INSERT INTO roles (nombre) VALUES ('ROLE_ADMIN'), ('ROLE_USER');
REPAIR TABLE mysql.db;

DROP USER IF EXISTS 'admin'@'localhost';
FLUSH PRIVILEGES;

CREATE USER 'admin'@'localhost' IDENTIFIED BY 'admin';

GRANT ALL PRIVILEGES ON tarea2.* TO 'admin'@'localhost';
--CREATE USER 'admin'@'172.18.0.1' IDENTIFIED BY 'admin';

--GRANT ALL PRIVILEGES ON tarea2.* TO 'admin'@'172.18.0.1';
CHECK TABLE db;
DROP TABLE db;
FLUSH PRIVILEGES;

select * FROM roles

select * FROM usuarios

select * FROM usuario_roles

SELECT id FROM usuarios WHERE email = 'admin@gmail.com';

SELECT id FROM roles WHERE nombre = 'ROLE_ADMIN';

INSERT INTO usuario_roles (usuario_id, rol_id) 
VALUES ((SELECT id FROM usuarios WHERE email = 'admin@gmail.com'), 
        (SELECT id FROM roles WHERE nombre = 'ROLE_ADMIN'));
