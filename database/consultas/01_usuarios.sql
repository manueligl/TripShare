use viajes_compartidos;

INSERT INTO usuario (nombre, email, contrasena)
VALUES ('Carlos', 'carlos@gmail.com', 'clave123');

SELECT * FROM usuario;

SELECT *
FROM usuario
WHERE email = 'carlos@gmail.com';

UPDATE usuario
SET nombre = 'Carlos García',
    email = 'carlos.garcia@gmail.com'
WHERE id_usuario = 4;

SELECT *
FROM usuario
WHERE id_usuario = 4;

SELECT id_usuario, nombre, email
FROM usuario
WHERE email = 'carlos.garcia@gmail.com'
AND contrasena = 'clave123';

DELETE FROM usuario
WHERE id_usuario = 4;

SELECT * FROM usuario;