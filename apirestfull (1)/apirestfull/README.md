# API RESTful - Proyecto de Evaluación Final

Este proyecto consiste en el desarrollo de una API RESTful construida con **Java** y **Spring Boot**, diseñada bajo estándares de la industria y buenas prácticas de desarrollo de software como DRY, KISS y principios SOLID.

## IMPORTANTE
- El proyecto debe abordar una solucion simple y clara a problemas cotidianos, por ejemplo gestion de tareas, prestamos, pedidos, etc.
 

## 🚀 Requerimientos Técnicos
- **Lenguaje:** Java 17+
- **Framework:** Spring Boot 3.x
- **Gestor de Dependencias:** Maven
- **Base de Datos:** H2 (En memoria)
- **Persistencia:** Spring Data JPA

## 🏗️ Estructura del Proyecto
El proyecto sigue el patrón **MVC** (Modelo-Vista-Controlador):
- `controller`: Manejo de rutas y solicitudes HTTP.
- `service`: Lógica de negocio e interfaces.
- `repository`: Interacción con la base de datos (JpaRepository).
- `model/entity`: Definición de las entidades de datos.
- `exception`: Manejo personalizado de errores.


## Acceso a la Base de Datos H2
La consola de H2 está habilitada en: http://localhost:8080/h2-console
- JDBC URL: jdbc:h2:mem:testdb
- User: sa
- Password: (vacío)

## Endpoints Principales
| Método | Endpoint | Descripción |
| :--- | :--- | :--- |
| GET | /api/entidad | Listar todos los recursos. |
| GET | /api/entidad/{id} | Obtener un recurso por ID. |
| POST | /api/entidad | Crear un nuevo recurso. |
| PUT | /api/entidad/{id} | Actualizar un recurso existente.
| DELETE | /api/entidad/{id} | Eliminar un recurso.

## 📝 Buenas prácticas
- Codigo limpio y legible.
- Separación de responsabilidades.
- Manejo de errores.    
- Comentarios en el código.

## 🧪 Pruebas
- Se recomienda utilizar Postman o Insomnia para probar los endpoints. 

## Opcionales
- El archivo src/main/resources/data.sql carga datos iniciales automáticamente al iniciar la aplicación.

