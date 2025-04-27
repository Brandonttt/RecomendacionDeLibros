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
CREATE TABLE libros_favoritos (
     id BIGINT PRIMARY KEY AUTO_INCREMENT,
     usuario_id BIGINT NOT NULL,
     libro_id VARCHAR(255) NOT NULL,
     titulo VARCHAR(255) NOT NULL,
     autor VARCHAR(255),
     imagen_url VARCHAR(1024),
     fecha_agregado TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
     FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE,
     UNIQUE KEY uk_usuario_libro (usuario_id, libro_id)
 );


INSERT INTO roles (nombre) VALUES ('ROLE_ADMIN'), ('ROLE_USER');
INSERT INTO usuarios (nombre, email, password) 
VALUES ('admin', 'admin@gmail.com', '123');
INSERT INTO usuario_roles (usuario_id, rol_id) 
VALUES ((SELECT id FROM usuarios WHERE email = 'admin@gmail.com'), 
        (SELECT id FROM roles WHERE nombre = 'ROLE_ADMIN'));

INSERT INTO usuarios (nombre, email, password) 
 VALUES ('admin2', 'admin@sistema.com', '$2a$10$TRQFRRFbVMQGUvEZ.gE07OXnCHcr0nFbWO6CXk4QTg7A8QyGb3RMO')

 SET @admin_id = LAST_INSERT_ID();

 INSERT INTO usuario_roles (usuario_id, rol_id) 
 SELECT @admin_id, id FROM roles WHERE nombre = 'ROLE_ADMIN';

--REPAIR TABLE mysql.db;

DROP USER IF EXISTS 'admin'@'localhost';
FLUSH PRIVILEGES;

CREATE USER 'admin'@'localhost' IDENTIFIED BY 'admin';
GRANT ALL PRIVILEGES ON tarea2.* TO 'admin'@'localhost';
FLUSH PRIVILEGES;





--CREATE USER 'admin'@'172.18.0.1' IDENTIFIED BY 'admin';

--GRANT ALL PRIVILEGES ON tarea2.* TO 'admin'@'172.18.0.1';
--CHECK TABLE db;
--DROP TABLE db;
--FLUSH PRIVILEGES;

select * FROM roles

select * FROM usuarios

select * FROM usuario_roles

select * FROM libros_favoritos

SELECT id FROM usuarios WHERE email = 'admin@gmail.com';

SELECT id FROM roles WHERE nombre = 'ROLE_ADMIN';


