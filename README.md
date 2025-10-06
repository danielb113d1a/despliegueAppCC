# despliegueAppCC
Repositorio para la asignatura Cloud Copmputing en el curso académico 25/26

# 📚 CloudLibrary – Comunidad de Lectores y Escritores

**CloudLibrary** es una aplicación web que permite a los usuarios compartir libros que han leído o escrito, dejar opiniones, responder a otros lectores y construir una comunidad literaria activa.  
El proyecto está diseñado para desplegarse en la nube, aprovechando servicios escalables y modernos.

## 🌟 Objetivo del Proyecto

El objetivo de **CloudLibrary** es ofrecer una plataforma colaborativa donde lectores y autores puedan:
- Publicar libros propios o recomendar los que han leído.
- Comentar y valorar las publicaciones de otros usuarios.
- Responder y debatir en hilos de conversación.
- Descubrir nuevas lecturas según intereses comunes.

## 🧩 Arquitectura y Tecnologías

CloudLibrary se estructura como una aplicación **Full Stack**:

### 🖥️ Frontend
- **Framework:** React  
- **Lenguaje:** JavaScript / TypeScript  
- **Objetivo:** Crear una interfaz web interactiva, responsiva y dinámica que permita a los usuarios gestionar sus libros, leer reseñas y comentar en tiempo real.  

### ⚙️ Backend
- **Framework:** Spring Boot (Java)  
- **API REST:** Gestionará los recursos principales (usuarios, libros, comentarios, valoraciones).  
- **Autenticación y seguridad:** Spring Security + JWT  
- **Controladores y servicios:** Diseño modular siguiendo buenas prácticas de arquitectura (MVC).  

### 🗄️ Base de datos
La BBDD que va a ser usada es PostgreSQL 