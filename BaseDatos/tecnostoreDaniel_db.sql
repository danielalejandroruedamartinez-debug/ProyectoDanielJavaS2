DROP DATABASE IF EXISTS tecnostoreDaniel_db;
CREATE DATABASE tecnostoreDaniel_db
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_general_ci;

USE tecnostoreDaniel_db;

CREATE TABLE marca (
    id     INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL,
    CONSTRAINT uq_marca_nombre UNIQUE (nombre)
) ENGINE=InnoDB;

CREATE TABLE sistema_operativo (
    id     INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(30) NOT NULL,
    CONSTRAINT uq_sistema_operativo_nombre UNIQUE (nombre)
) ENGINE=InnoDB;

CREATE TABLE gama (
    id     INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(10) NOT NULL,
    CONSTRAINT uq_gama_nombre UNIQUE (nombre)
) ENGINE=InnoDB;

CREATE TABLE celular (
    id                   INT AUTO_INCREMENT PRIMARY KEY,
    id_marca             INT           NOT NULL,
    modelo               VARCHAR(80)   NOT NULL,
    precio               DECIMAL(12,2) NOT NULL,
    stock                INT           NOT NULL DEFAULT 0,
    id_sistema_operativo INT           NOT NULL,
    id_gama              INT           NOT NULL,
    CONSTRAINT fk_celular_marca FOREIGN KEY (id_marca)
        REFERENCES marca(id),
    CONSTRAINT fk_celular_so FOREIGN KEY (id_sistema_operativo)
        REFERENCES sistema_operativo(id),
    CONSTRAINT fk_celular_gama FOREIGN KEY (id_gama)
        REFERENCES gama(id)
) ENGINE=InnoDB;

CREATE TABLE tipo_persona (
    id     INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(30) NOT NULL,
    CONSTRAINT uq_tipo_persona_nombre UNIQUE (nombre)
) ENGINE=InnoDB;

CREATE TABLE persona (
    id              INT AUTO_INCREMENT PRIMARY KEY,
    id_tipo_persona INT          NOT NULL,
    nombre          VARCHAR(100) NOT NULL,
    identificacion  VARCHAR(20)  NOT NULL,
    correo          VARCHAR(100) NOT NULL,
    telefono        VARCHAR(20)  NOT NULL,
    contrasena      VARCHAR(255) NOT NULL,
    CONSTRAINT uq_persona_identificacion UNIQUE (identificacion),
    CONSTRAINT fk_persona_tipo FOREIGN KEY (id_tipo_persona)
        REFERENCES tipo_persona(id)
) ENGINE=InnoDB;

CREATE TABLE cliente (
    id             INT AUTO_INCREMENT PRIMARY KEY,
    id_persona     INT      NOT NULL,
    fecha_registro DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uq_cliente_persona UNIQUE (id_persona),
    CONSTRAINT fk_cliente_persona FOREIGN KEY (id_persona)
        REFERENCES persona(id) ON DELETE CASCADE
) ENGINE=InnoDB;

CREATE TABLE vendedor (
    id            INT AUTO_INCREMENT PRIMARY KEY,
    id_persona    INT         NOT NULL,
    cargo         VARCHAR(50) NOT NULL,
    fecha_ingreso DATE        NOT NULL,
    CONSTRAINT uq_vendedor_persona UNIQUE (id_persona),
    CONSTRAINT fk_vendedor_persona FOREIGN KEY (id_persona)
        REFERENCES persona(id) ON DELETE CASCADE
) ENGINE=InnoDB;

CREATE TABLE administrador (
    id                INT AUTO_INCREMENT PRIMARY KEY,
    id_persona        INT        NOT NULL,
    es_principal      TINYINT(1) NOT NULL DEFAULT 0,
    id_admin_creador  INT        NULL,
    fecha_registro    DATETIME   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uq_administrador_persona UNIQUE (id_persona),
    CONSTRAINT fk_administrador_persona FOREIGN KEY (id_persona)
        REFERENCES persona(id) ON DELETE CASCADE,
    CONSTRAINT fk_administrador_creador FOREIGN KEY (id_admin_creador)
        REFERENCES administrador(id) ON DELETE SET NULL
) ENGINE=InnoDB;

CREATE TABLE venta (
    id             INT AUTO_INCREMENT PRIMARY KEY,
    id_cliente     INT          NOT NULL,
    id_vendedor    INT          NOT NULL,
    fecha          DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    porcentaje_iva DECIMAL(5,2) NOT NULL DEFAULT 19.00,
    CONSTRAINT fk_venta_cliente FOREIGN KEY (id_cliente)
        REFERENCES cliente(id),
    CONSTRAINT fk_venta_vendedor FOREIGN KEY (id_vendedor)
        REFERENCES vendedor(id)
) ENGINE=InnoDB;

CREATE TABLE detalle_venta (
    id              INT AUTO_INCREMENT PRIMARY KEY,
    id_venta        INT           NOT NULL,
    id_celular      INT           NOT NULL,
    cantidad        INT           NOT NULL,
    precio_unitario DECIMAL(12,2) NOT NULL,
    CONSTRAINT fk_detalle_venta FOREIGN KEY (id_venta)
        REFERENCES venta(id) ON DELETE CASCADE,
    CONSTRAINT fk_detalle_celular FOREIGN KEY (id_celular)
        REFERENCES celular(id)
) ENGINE=InnoDB;

CREATE VIEW vista_celular AS
SELECT c.id, m.nombre AS marca, c.modelo, c.precio, c.stock,
       so.nombre AS sistema_operativo, g.nombre AS gama
FROM celular c
JOIN marca m ON m.id = c.id_marca
JOIN sistema_operativo so ON so.id = c.id_sistema_operativo
JOIN gama g ON g.id = c.id_gama;

CREATE VIEW vista_cliente AS
SELECT c.id, p.nombre, p.identificacion, p.correo, p.telefono, c.fecha_registro
FROM cliente c
JOIN persona p ON p.id = c.id_persona;

CREATE VIEW vista_vendedor AS
SELECT v.id, p.nombre, p.identificacion, p.correo, p.telefono, v.cargo, v.fecha_ingreso
FROM vendedor v
JOIN persona p ON p.id = v.id_persona;

CREATE VIEW vista_administrador AS
SELECT a.id, p.nombre, p.identificacion, p.correo, p.telefono, a.es_principal, a.fecha_registro
FROM administrador a
JOIN persona p ON p.id = a.id_persona;

CREATE VIEW vista_venta AS
SELECT v.id, v.fecha, v.id_cliente, v.id_vendedor, v.porcentaje_iva,
       COALESCE(SUM(d.cantidad * d.precio_unitario), 0) AS subtotal,
       ROUND(COALESCE(SUM(d.cantidad * d.precio_unitario), 0) * v.porcentaje_iva / 100, 2) AS iva,
       ROUND(COALESCE(SUM(d.cantidad * d.precio_unitario), 0) * (1 + v.porcentaje_iva / 100), 2) AS total
FROM venta v
LEFT JOIN detalle_venta d ON d.id_venta = v.id
GROUP BY v.id, v.fecha, v.id_cliente, v.id_vendedor, v.porcentaje_iva;

INSERT INTO tipo_persona (nombre) VALUES ('Cliente'), ('Vendedor'), ('Administrador');
INSERT INTO gama (nombre) VALUES ('Alta'), ('Media'), ('Baja');

INSERT INTO marca (nombre) VALUES
('Samsung'), ('Apple'), ('Xiaomi'), ('Motorola');

INSERT INTO sistema_operativo (nombre) VALUES
('Android'), ('iOS');

INSERT INTO celular (id_marca, modelo, precio, stock, id_sistema_operativo, id_gama) VALUES
(1, 'Galaxy S24',     3500000, 10, 1, 1),
(2, 'iPhone 15',      4800000,  8, 2, 1),
(3, 'Redmi Note 13',   900000, 25, 1, 2),
(4, 'Moto G14',        600000, 30, 1, 3),
(1, 'Galaxy A15',      750000, 20, 1, 2),
(3, 'Poco C65',        500000, 15, 1, 3);

INSERT INTO persona (id, id_tipo_persona, nombre, identificacion, correo, telefono, contrasena) VALUES
(1, 3, 'Administrador Principal', '1000000001', 'admin@tecnostore.com',  '3000000001', '$2a$10$k3M40yTuJ4vV9a33er/kS..Gur.Nnysxd7dSzC.3/NSvEjbgYBLO.'),
(2, 2, 'Carlos Ramírez',          '1002345678', 'carlos@tecnostore.com', '3012223344', '$2a$10$RLwXneqI/WnkURcWk6lbuuXYqQ4LVuGzqrt17lKgu.6tNEPjWyf0W'),
(3, 2, 'Laura Gómez',             '1003456789', 'laura@tecnostore.com',  '3023334455', '$2a$10$f/UraBnT4PJxDkXAAk3KKev4gyswszGKPs5EwsYUTnp74tX4ys.wy'),
(4, 1, 'Juan Pérez',              '1001234567', 'juan@correo.com',       '3001112233', '$2a$10$4cVQIoNJeITPUykGlOrJpOCBcMYRPCK87mqkzHrhx2QpVRKaQI/7i'),
(5, 1, 'María López',             '1009876543', 'maria@correo.com',      '3104445566', '$2a$10$wlq0wzuplpx4kDLIR2Ev0uRJomBp7pdtjXP5J2DqW.5RW0YfBelx2'),
(6, 1, 'Pedro Martínez',          '1005678901', 'pedro@correo.com',      '3205556677', '$2a$10$HHuYyMdDvx66zK7O7PUGOOf7rvpmFLXlZWpJZRgMkfNxRih4XdfNi');

INSERT INTO administrador (id_persona, es_principal, id_admin_creador) VALUES
(1, 1, NULL);

INSERT INTO vendedor (id_persona, cargo, fecha_ingreso) VALUES
(2, 'Asesor de ventas', '2025-03-10'),
(3, 'Asesora de ventas', '2025-06-02');

INSERT INTO cliente (id_persona) VALUES (4), (5), (6);

INSERT INTO venta (id_cliente, id_vendedor, porcentaje_iva) VALUES
(1, 1, 19.00),
(2, 2, 19.00);

INSERT INTO detalle_venta (id_venta, id_celular, cantidad, precio_unitario) VALUES
(1, 1, 1, 3500000),
(1, 4, 2,  600000),
(2, 2, 1, 4800000);
