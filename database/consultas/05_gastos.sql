USE viajes_compartidos;

SELECT *
FROM gasto;

SELECT
    gp.id_gasto,
    u.id_usuario,
    u.nombre,
    gp.importe_asignado
FROM gasto_participante gp
INNER JOIN usuario u
    ON gp.id_usuario = u.id_usuario
WHERE gp.id_gasto = 1;

SELECT
    g.id_gasto,
    g.concepto,
    g.importe,
    g.fecha,
    u.nombre AS pagador
FROM gasto g
INNER JOIN usuario u
    ON g.id_pagador = u.id_usuario
WHERE g.id_viaje = 1;

INSERT INTO gasto
(concepto, importe, fecha, id_viaje, id_pagador)
VALUES
('Comida', 60.00, CURDATE(), 1, 2);

SELECT *
FROM gasto
WHERE concepto = 'Comida'
AND id_viaje = 1;

INSERT INTO gasto_participante
(id_usuario, id_gasto, id_viaje, importe_asignado)
VALUES
(1, 2, 1, 20.00),
(2, 2, 1, 20.00),
(3, 2, 1, 20.00);

SELECT
    gp.id_gasto,
    u.nombre,
    gp.importe_asignado
FROM gasto_participante gp
INNER JOIN usuario u
    ON gp.id_usuario = u.id_usuario
WHERE gp.id_gasto = 2;

CALL calcular_total_gastos(1);

SELECT
    u.nombre,
    SUM(g.importe) AS total_pagado
FROM gasto g
INNER JOIN usuario u
    ON g.id_pagador = u.id_usuario
WHERE g.id_viaje = 1
GROUP BY u.id_usuario, u.nombre;

UPDATE gasto
SET concepto = 'Comida compartida'
WHERE id_gasto = 2;

SELECT *
FROM gasto
WHERE id_gasto = 2;

DELETE FROM gasto_participante
WHERE id_gasto = 2
AND id_viaje = 1;

SELECT *
FROM gasto_participante
WHERE id_gasto = 2;

DELETE FROM gasto
WHERE id_gasto = 2
AND id_viaje = 1;

SELECT *
FROM gasto
WHERE id_gasto = 2;