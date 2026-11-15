# TripShare

**Aplicación de escritorio para la gestión y organización de viajes compartidos.**

## Descripción

TripShare es una aplicación para facilitar la planificación de viajes entre varias personas. Permite centralizar la información de los viajes, gestionar participantes, organizar actividades y controlar los gastos.

La aplicación cuenta con una interfaz gráfica intuitiva y una base de datos MySQL para almacenar la información de forma persistente.

## Tecnologías utilizadas

- **Java 21:** lenguaje de programación principal.
- **JavaFX 21:** desarrollo de la interfaz gráfica.
- **FXML:** definición de las vistas.
- **MySQL:** sistema de gestión de bases de datos.
- **JDBC:** conexión y comunicación entre Java y MySQL.
- **Maven:** gestión de dependencias y construcción del proyecto.
- **Git y GitHub:** control de versiones y trabajo con ramas.

## Funcionalidades principales

- Registro de nuevos usuarios.
- Inicio de sesión.
- Consulta de los viajes asociados a un usuario.
- Creación y gestión de viajes.
- Consulta de los detalles de cada viaje.
- Gestión de participantes.
- Organización y consulta de actividades.
- Gestión de gastos compartidos.
- Registro y consulta de aportaciones económicas.

## Tipos de usuario

La aplicación contempla cuatro modelos de usuario:

1. **Usuario no registrado:** puede acceder al formulario de registro para crear una cuenta.
2. **Usuario registrado:** puede iniciar sesión, consultar sus viajes y crear nuevos viajes.
3. **Creador del viaje:** usuario que organiza un viaje y gestiona su información y participantes.
4. **Invitado al viaje:** usuario que participa en un viaje compartido y puede consultar su información.

## Estructura del proyecto

El proyecto sigue una arquitectura basada en el patrón Modelo-Vista-Controlador (MVC).

```text
TripShare/
│
├── database/
│   ├── tripshare.sql
│   └── consultas/
│       ├── 01_usuarios.sql
│       ├── 02_viajes.sql
│       ├── 03_participantes.sql
│       ├── 04_actividades.sql
│       ├── 05_gastos.sql
│       └── 06_aportaciones.sql
│
├── src/
│   └── main/
│       ├── java/
│       │   └── com/tripshare/
│       │       ├── controlador/
│       │       │   ├── PrimaryController.java
│       │       │   ├── SecondaryController.java
│       │       │   ├── MisViajesController.java
│       │       │   ├── CrearViajeController.java
│       │       │   └── DetallesViajeController.java
│       │       │
│       │       ├── modelo/
│       │       │   ├── Actividad.java
│       │       │   ├── Aportacion.java
│       │       │   ├── Conexion.java
│       │       │   ├── Gasto.java
│       │       │   ├── Usuario.java
│       │       │   └── Viaje.java
│       │       │
│       │       ├── App.java
│       │       └── module-info.java
│       │
│       └── resources/
│           └── com/tripshare/vista/
│               ├── primary.fxml
│               ├── secondary.fxml
│               ├── mis-viajes.fxml
│               ├── crear-viaje.fxml
│               └── detalles-viaje.fxml
│
├── pom.xml
└── README.md
```

### Organización MVC

- **Modelo:** contiene las clases que representan los datos y gestionan las operaciones con la base de datos.
- **Vista:** contiene los archivos FXML que definen las interfaces gráficas.
- **Controlador:** gestiona los eventos de la interfaz y conecta las vistas con el modelo.

## Base de datos

TripShare utiliza MySQL para almacenar la información de usuarios, viajes, participantes, actividades, gastos y aportaciones.

El modelo de datos incluye las siguientes entidades principales:

- `usuario`
- `viaje`
- `actividad`
- `gasto`
- `aportacion`

También utiliza tablas para representar las relaciones entre usuarios y viajes, viajes y actividades, y la distribución de los gastos entre participantes.

Los scripts SQL se encuentran en la carpeta `database/`:

- `tripshare.sql`: creación de la base de datos y definición de sus elementos.
- `consultas/`: consultas y operaciones SQL organizadas por funcionalidad.

La comunicación con MySQL se realiza mediante JDBC.

## Consultas SQL

Dentro de `database/consultas/` se incluyen scripts organizados por áreas funcionales:

| Archivo | Contenido |
|---|---|
| `01_usuarios.sql` | Operaciones relacionadas con usuarios. |
| `02_viajes.sql` | Consultas y operaciones sobre viajes. |
| `03_participantes.sql` | Gestión de participantes. |
| `04_actividades.sql` | Operaciones sobre actividades. |
| `05_gastos.sql` | Consultas y operaciones sobre gastos. |
| `06_aportaciones.sql` | Consultas y operaciones sobre aportaciones. |

## Control de versiones

El desarrollo del proyecto se ha gestionado mediante Git y GitHub, utilizando ramas diferenciadas:

- `Modelo`
- `Vista`
- `Controlador`
- `develop`
- `main`

Las integraciones entre ramas se han realizado mediante Pull Requests de GitHub. Las ramas funcionales se integraron en `develop` y, tras comprobar el funcionamiento del proyecto, se integró `develop` en `main`.
