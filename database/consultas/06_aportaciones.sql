USE viajes_compartidos;

SELECT *
FROM aportacion;

SELECT
    a.id_aportacion,
    emisor.nombre AS emisor,
    receptor.nombre AS receptor,
    a.importe,
    a.fecha
FROM aportacion a
INNER JOIN usuario emisor
    ON a.id_usuario = emisor.id_usuario
INNER JOIN usuario receptor
    ON a.id_receptor = receptor.id_usuario
WHERE a.id_viaje = 1;

SELECT
    u.nombre AS receptor,
    SUM(a.importe) AS total_recibido
FROM aportacion a
INNER JOIN usuario u
    ON a.id_receptor = u.id_usuario
WHERE a.id_viaje = 1
GROUP BY u.id_usuario, u.nombre;

SELECT
    u.nombre AS emisor,
    SUM(a.importe) AS total_aportado
FROM aportacion a
INNER JOIN usuario u
    ON a.id_usuario = u.id_usuario
WHERE a.id_viaje = 1
GROUP BY u.id_usuario, u.nombre;

INSERT INTO aportacion
(id_usuario, id_receptor, id_viaje, importe, fecha)
VALUES
(3, 1, 1, 10.00, CURDATE());

SELECT *
FROM aportacion
WHERE id_viaje = 1
ORDER BY id_aportacion;

UPDATE aportacion
SET importe = 12.00
WHERE id_aportacion = 3;

SELECT *
FROM aportacion
WHERE id_aportacion = 3;

DELETE FROM aportacion
WHERE id_aportacion = 3;

SELECT *
FROM aportacion
WHERE id_aportacion = 3;