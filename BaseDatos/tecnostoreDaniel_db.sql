DROP DATABASE IF EXISTS tecnostoreDaniel_db;
CREATE DATABASE tecnostoreDaniel_db
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_general_ci;

USE tecnostoreDaniel_db;

CREATE TABLE celular (
    id                INT AUTO_INCREMENT PRIMARY KEY,
    marca             VARCHAR(50)   NOT NULL,
    modelo            VARCHAR(80)   NOT NULL,
    precio            DECIMAL(12,2) NOT NULL,
    stock             INT           NOT NULL DEFAULT 0,
    sistema_operativo VARCHAR(30)   NOT NULL,
    gama              VARCHAR(10)   NOT NULL
) ENGINE=InnoDB;

CREATE TABLE cliente (
    id             INT AUTO_INCREMENT PRIMARY KEY,
    nombre         VARCHAR(100) NOT NULL,
    identificacion VARCHAR(20)  NOT NULL,
    correo         VARCHAR(100) NOT NULL,
    telefono       VARCHAR(20)  NOT NULL,
    CONSTRAINT uq_cliente_identificacion UNIQUE (identificacion)
) ENGINE=InnoDB;

CREATE TABLE venta (
    id         INT AUTO_INCREMENT PRIMARY KEY,
    id_cliente INT           NOT NULL,
    fecha      DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    subtotal   DECIMAL(14,2) NOT NULL DEFAULT 0,
    iva        DECIMAL(14,2) NOT NULL DEFAULT 0,
    total      DECIMAL(14,2) NOT NULL DEFAULT 0,
    CONSTRAINT fk_venta_cliente FOREIGN KEY (id_cliente)
        REFERENCES cliente(id)
) ENGINE=InnoDB;

CREATE TABLE detalle_venta (
    id              INT AUTO_INCREMENT PRIMARY KEY,
    id_venta        INT           NOT NULL,
    id_celular      INT           NOT NULL,
    cantidad        INT           NOT NULL,
    precio_unitario DECIMAL(12,2) NOT NULL,
    subtotal        DECIMAL(14,2) NOT NULL,
    CONSTRAINT fk_detalle_venta FOREIGN KEY (id_venta)
        REFERENCES venta(id) ON DELETE CASCADE,
    CONSTRAINT fk_detalle_celular FOREIGN KEY (id_celular)
        REFERENCES celular(id)
) ENGINE=InnoDB;
