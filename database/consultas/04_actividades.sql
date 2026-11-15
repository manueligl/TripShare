USE viajes_compartidos;

-- 1. Insertar una actividad
INSERT INTO actividad
(nombre, descripcion, fecha, precio)
VALUES
('Visita a la Alhambra',
 'Visita turística a la Alhambra de Granada',
 '2027-08-11', 25.00);

-- 2. Asociar la actividad a un viaje
INSERT INTO viaje_actividad
(id_viaje, id_actividad)
VALUES
(2, LAST_INSERT_ID());

-- 3. Consultar actividades de un viaje
SELECT
    a.id_actividad,
    a.nombre,
    a.descripcion,
    a.fecha,
    a.precio
FROM actividad a
INNER JOIN viaje_actividad va
    ON a.id_actividad = va.id_actividad
WHERE va.id_viaje = 2;

-- 4. Modificar el precio de una actividad
UPDATE actividad
SET precio = 30.00
WHERE id_actividad = 1;

-- 5. Consultar una actividad
SELECT *
FROM actividad
WHERE nombre = 'Visita a la Alhambra';

-- 6. Eliminar la asociación con el viaje
DELETE FROM viaje_actividad
WHERE id_viaje = 2
AND id_actividad = (
    SELECT id_actividad
    FROM actividad
    WHERE nombre = 'Visita a la Alhambra'
);

-- 7. Eliminar la actividad
DELETE FROM actividad
WHERE nombre = 'Visita a la Alhambra';

-- 8. Comprobar el borrado
SELECT *
FROM viaje_actividad
WHERE id_viaje = 2;

SELECT *
FROM actividad
WHERE nombre = 'Visita a la Alhambra';
