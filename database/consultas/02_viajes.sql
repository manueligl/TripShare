use viajes_compartidos;

START TRANSACTION;

INSERT INTO viaje
(nombre, destino, fecha_inicio, fecha_fin, presupuesto, id_creador)
VALUES
('Escapada a Granada', 'Granada, España',
 '2027-08-10', '2027-08-14', 800.00, 1);

SET @id_viaje = LAST_INSERT_ID();

INSERT INTO usuario_viaje
(id_usuario, id_viaje, fecha_inscripcion, activo)
VALUES
(1, @id_viaje, CURDATE(), TRUE);

COMMIT;

SELECT
    v.id_viaje,
    v.nombre,
    v.destino,
    v.fecha_inicio,
    v.fecha_fin,
    v.presupuesto
FROM viaje v
INNER JOIN usuario_viaje uv
    ON v.id_viaje = uv.id_viaje
WHERE uv.id_usuario = 1
AND uv.activo = TRUE;

SELECT
    v.*,
    u.nombre AS creador
FROM viaje v
INNER JOIN usuario u
    ON v.id_creador = u.id_usuario
WHERE v.id_viaje = 1;

UPDATE viaje
SET nombre = 'Viaje a Madrid 2027',
    presupuesto = 1200.00
WHERE id_viaje = 1;

SELECT *
FROM viaje
WHERE id_viaje = 1;

