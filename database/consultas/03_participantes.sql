use viajes_compartidos;

INSERT INTO usuario_viaje
(id_usuario, id_viaje, fecha_inscripcion, activo)
VALUES
(2, 2, CURDATE(), TRUE),
(3, 2, CURDATE(), TRUE);

SELECT
    u.id_usuario,
    u.nombre,
    u.email,
    uv.fecha_inscripcion,
    uv.activo
FROM usuario_viaje uv
INNER JOIN usuario u
    ON uv.id_usuario = u.id_usuario
WHERE uv.id_viaje = 2
AND uv.activo = TRUE;

SELECT v.*
FROM viaje v
INNER JOIN usuario_viaje uv
    ON v.id_viaje = uv.id_viaje
WHERE uv.id_usuario = 1;

SELECT *
FROM usuario_viaje
WHERE id_usuario = 2
AND id_viaje = 2
AND activo = TRUE;

UPDATE usuario_viaje
SET activo = FALSE
WHERE id_usuario = 2
AND id_viaje = 2;