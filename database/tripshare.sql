
CREATE DATABASE IF NOT EXISTS viajes_compartidos
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE viajes_compartidos;

CREATE TABLE IF NOT EXISTS usuario (
    id_usuario INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    contrasena VARCHAR(255) NOT NULL
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS viaje (
    id_viaje INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    destino VARCHAR(150) NOT NULL,
    fecha_inicio DATE NOT NULL,
    fecha_fin DATE NOT NULL,
    presupuesto DECIMAL(10,2) NOT NULL,
    id_creador INT NOT NULL,

    CONSTRAINT fk_viaje_creador
        FOREIGN KEY (id_creador) REFERENCES usuario(id_usuario),

    CONSTRAINT chk_viaje_fechas
        CHECK (fecha_fin >= fecha_inicio),

    CONSTRAINT chk_viaje_presupuesto
        CHECK (presupuesto >= 0)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS actividad (
    id_actividad INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    descripcion TEXT,
    fecha DATE,
    precio DECIMAL(10,2) NOT NULL DEFAULT 0,

    CONSTRAINT chk_actividad_precio
        CHECK (precio >= 0)
) ENGINE=InnoDB;

-- Relaciona usuarios con viajes.
-- activo=0 conserva el historial cuando alguien abandona el viaje.
CREATE TABLE IF NOT EXISTS usuario_viaje (
    id_usuario INT NOT NULL,
    id_viaje INT NOT NULL,
    fecha_inscripcion DATE NOT NULL,
    activo BOOLEAN NOT NULL DEFAULT TRUE,

    PRIMARY KEY (id_usuario, id_viaje),

    CONSTRAINT fk_uv_usuario
        FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario),

    CONSTRAINT fk_uv_viaje
        FOREIGN KEY (id_viaje) REFERENCES viaje(id_viaje)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS viaje_actividad (
    id_viaje INT NOT NULL,
    id_actividad INT NOT NULL,

    PRIMARY KEY (id_viaje, id_actividad),

    CONSTRAINT fk_va_viaje
        FOREIGN KEY (id_viaje) REFERENCES viaje(id_viaje),

    CONSTRAINT fk_va_actividad
        FOREIGN KEY (id_actividad) REFERENCES actividad(id_actividad)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS gasto (
    id_gasto INT AUTO_INCREMENT PRIMARY KEY,
    concepto VARCHAR(150) NOT NULL,
    importe DECIMAL(10,2) NOT NULL,
    fecha DATE NOT NULL,
    id_viaje INT NOT NULL,
    id_pagador INT NOT NULL,

    -- Necesaria para poder comprobar que el gasto y el participante
    -- pertenecen al mismo viaje mediante una clave ajena compuesta.
    UNIQUE KEY uq_gasto_viaje (id_gasto, id_viaje),

    CONSTRAINT fk_gasto_viaje
        FOREIGN KEY (id_viaje) REFERENCES viaje(id_viaje),

    CONSTRAINT fk_gasto_pagador_viaje
        FOREIGN KEY (id_pagador, id_viaje)
        REFERENCES usuario_viaje(id_usuario, id_viaje),

    CONSTRAINT chk_gasto_importe
        CHECK (importe > 0)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS gasto_participante (
    id_usuario INT NOT NULL,
    id_gasto INT NOT NULL,
    id_viaje INT NOT NULL,
    importe_asignado DECIMAL(10,2) NOT NULL,

    PRIMARY KEY (id_usuario, id_gasto),

    CONSTRAINT fk_gp_usuario_viaje
        FOREIGN KEY (id_usuario, id_viaje)
        REFERENCES usuario_viaje(id_usuario, id_viaje),

    CONSTRAINT fk_gp_gasto_viaje
        FOREIGN KEY (id_gasto, id_viaje)
        REFERENCES gasto(id_gasto, id_viaje),

    CONSTRAINT chk_gp_importe
        CHECK (importe_asignado >= 0)
) ENGINE=InnoDB;

-- Una aportación es una entrega de dinero de un participante
-- (id_usuario) a otro participante (id_receptor) del mismo viaje.
CREATE TABLE IF NOT EXISTS aportacion (
    id_aportacion INT AUTO_INCREMENT PRIMARY KEY,
    id_usuario INT NOT NULL,
    id_receptor INT NOT NULL,
    id_viaje INT NOT NULL,
    importe DECIMAL(10,2) NOT NULL,
    fecha DATE NOT NULL,

    CONSTRAINT fk_aportacion_emisor_viaje
        FOREIGN KEY (id_usuario, id_viaje)
        REFERENCES usuario_viaje(id_usuario, id_viaje),

    CONSTRAINT fk_aportacion_receptor_viaje
        FOREIGN KEY (id_receptor, id_viaje)
        REFERENCES usuario_viaje(id_usuario, id_viaje),

    CONSTRAINT chk_aportacion_importe
        CHECK (importe > 0),

    CONSTRAINT chk_aportacion_usuarios
        CHECK (id_usuario <> id_receptor)
) ENGINE=InnoDB;

-- Procedimiento almacenado: devuelve el total de gastos de un viaje.
DROP PROCEDURE IF EXISTS calcular_total_gastos;

DELIMITER //
CREATE PROCEDURE calcular_total_gastos(IN p_id_viaje INT)
BEGIN
    SELECT COALESCE(SUM(importe), 0) AS total_gastos
    FROM gasto
    WHERE id_viaje = p_id_viaje;
END //
DELIMITER ;
