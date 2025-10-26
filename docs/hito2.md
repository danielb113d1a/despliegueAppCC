# Hito 2: Integración continua

## 🧱 Elección del gestor de tareas

Para este proyecto, que combina un backend desarrollado con **Java Spring Boot** y un frontend con **React**, se han seleccionado distintos gestores de tareas específicos para cada entorno, junto con una herramienta de orquestación general que permite ejecutar todas las pruebas de forma unificada.

---

### 🔹 Backend — Maven (con `mvnw`)

En el lado del **backend**, se ha elegido **Maven** como gestor de tareas y construcción del proyecto.

#### Justificación técnica:
- **Ecosistema estándar en Spring Boot**: Maven es el sistema más usado y documentado dentro del ecosistema Spring, lo que garantiza compatibilidad con plugins, dependencias y guías oficiales.
- **Gestión declarativa y reproducible**: el archivo `pom.xml` centraliza todas las dependencias y versiones, facilitando la reproducibilidad de los entornos de desarrollo y de integración continua.
- **Integración con testing**: incluye plugins maduros como `maven-surefire-plugin` (para tests unitarios) y `maven-failsafe-plugin` (para tests de integración).  
  Además, puede integrarse fácilmente con herramientas de cobertura de código como `JaCoCo`.
- **Facilidad de uso en CI**: mediante el *wrapper* (`mvnw`), no es necesario tener Maven instalado en el entorno de integración continua.  
  Esto permite que GitHub Actions u otros sistemas CI utilicen siempre la misma versión de Maven.
- **Compatibilidad con PostgreSQL**: Maven no depende del tipo de base de datos, por lo que ejecutar los tests contra PostgreSQL solo requiere configurar el perfil de test o las variables de entorno correspondientes.

### 🔹 Frontend — npm scripts

En el **frontend**, desarrollado con **React**, se ha optado por usar **npm** como gestor de tareas.

#### Justificación técnica
- **Integración nativa**: React (especialmente con *Create React App* o *Vite*) usa npm como herramienta de gestión de dependencias y scripts.
- **Reproducibilidad en CI**: el uso de `npm ci` junto con `package-lock.json` garantiza instalaciones deterministas e idénticas en todos los entornos.
- **Simplicidad**: los scripts de prueba se definen directamente en el `package.json`, permitiendo ejecutar tests con un solo comando.
- **Independencia de la base de datos**: el entorno de frontend no interactúa directamente con PostgreSQL, por lo que el gestor de tareas se centra exclusivamente en la parte de testing de interfaz y lógica de componentes.

### 🔹 Orquestación general — Makefile

Para unificar la ejecución de pruebas de ambos entornos, se ha añadido un **Makefile** en la raíz del proyecto.  
Este permite lanzar todos los tests (backend y frontend) con un único comando, tanto en local como en CI.

#### Justificación técnica
- **Estandariza comandos**: evita tener que recordar rutas o banderas específicas.
- **Facilita integración continua**: GitHub Actions puede ejecutar directamente `make test` como paso principal.
- **Aísla la configuración del entorno**: permite pasar variables (por ejemplo, conexión a PostgreSQL) sin modificar los comandos base.

## Elección de la biblioteca de aserciones

### 1️⃣ Backend — Java (Spring Boot)

**Opciones principales:**

- JUnit Assertions (estándar de Java)  
- AssertJ  
- Hamcrest  

**Recomendación:** AssertJ

### Justificación técnica

- **Sintaxis fluida y expresiva (BDD-style):** permite escribir pruebas más legibles, cercanas al lenguaje natural. Por ejemplo:  

  ```java
  assertThat(user.getName()).isEqualTo("Ana");

- **Mayor poder que las aserciones estándar de JUnit:** facilita comprobaciones complejas, colecciones, mapas, excepciones, fechas, etc.

- **Compatibilidad total con Spring Boot y JUnit 5:** se integra sin problemas con `maven-surefire-plugin` y `@SpringBootTest`.

- **Facilita BDD (Behavior-Driven Development):** aunque seguimos tests unitarios, el estilo `assertThat(...).isEqualTo(...)` permite describir claramente el comportamiento esperado de la aplicación.

### Estilo elegido: BDD

**Razonamiento:**  
Aunque JUnit puro está orientado a TDD, AssertJ permite un estilo más declarativo que describe el comportamiento esperado del sistema, lo que hace más fácil la lectura y el mantenimiento de tests colaborativos, especialmente en un proyecto full-stack.

## Elección del test runner

### 1️⃣ Backend — Java (Spring Boot)

**Opciones principales:**

- JUnit 5 (Jupiter)  
- TestNG  
- Spock (Groovy)  

**Recomendación:** JUnit 5 (Jupiter)

### Justificación técnica

- **Estándar de facto en Java/Spring Boot:** la documentación oficial de Spring Boot y la mayoría de starters usan JUnit 5. Esto asegura compatibilidad con plugins de Maven y CI.

- **Test runner robusto y flexible:** encuentra y ejecuta automáticamente tests anotados con `@Test`, `@ParameterizedTest`, etc., y genera informes de ejecución claros.

- **Integración con bibliotecas de aserciones externas:** se integra sin problemas con AssertJ, que hemos elegido como biblioteca de aserciones.

- **Herramientas de línea de comandos:** mediante Maven (`mvn test` o `mvn verify`) permite ejecutar todos los tests en local o CI de manera reproducible.

- **Extensible:** soporta extensiones y configuraciones avanzadas (por ejemplo, `@SpringBootTest`, `@DataJpaTest`, pruebas con perfiles de Spring).


### Integración con CI

- Compatible con GitHub Actions, Jenkins o CircleCI.  
- Los resultados de tests se pueden exportar a XML/HTML para reportes automáticos.

---

### 2️⃣ Frontend — React (JavaScript)

**Opciones principales:**

- Jest (con CLI)  
- Mocha + Chai  
- Vitest (si se usa Vite)  

**Recomendación:** Jest

### Justificación técnica

- **Integración directa con Create React App y Vite:** viene preconfigurado y listo para ejecutar tests unitarios de componentes y lógica JS.

- **Test runner + assertions:** aunque Jest incluye `expect`, se puede combinar con Testing Library para aserciones más específicas de DOM.

- **Herramienta de línea de comandos:** `npm test` o `npm run ci:test` permite ejecutar todos los tests, generar cobertura y exportar resultados para CI.

- **Flexibilidad:** permite mocks, tests asincrónicos, pruebas parametrizadas, snapshots, y es ampliamente adoptado en la comunidad React.

- **Estilo BDD:** con `describe` y `it`, permite estructurar los tests de manera legible y orientada al comportamiento esperado.


## Integración de pruebas en la herramienta de construcción

Para asegurar que las pruebas se ejecuten de manera consistente tanto en local como en el entorno de integración continua, se han integrado los tests dentro de las herramientas de construcción de cada parte del proyecto, siguiendo las convenciones estándar del lenguaje y el ecosistema.

### Backend (Spring Boot / Maven)

Todos los tests se ejecutan mediante Maven usando el plugin `maven-surefire` para tests unitarios y `maven-failsafe` para tests de integración.

Esto permite que cualquier desarrollador o sistema CI ejecute los tests exactamente de la misma manera, sin depender de configuraciones externas.

### Frontend (React / npm)

Los tests se ejecutan mediante los npm scripts definidos en `package.json`

Esto asegura que la instalación de dependencias y la ejecución de tests sea reproducible y uniforme en todos los entornos.

### Orquestación general — Makefile

Se ha añadido un objetivo `make test` en la raíz del proyecto para unificar la ejecución de tests de backend y frontend:

```makefile
.PHONY: backend-test frontend-test test

backend-test:
	cd backend && ./mvnw -B -Dspring.profiles.active=test clean verify

frontend-test:
	cd frontend && npm ci && npm run ci:test

test: backend-test frontend-test
```

Con este objetivo, ejecutar todos los tests se reduce a:

```bash
make test
```

## Elección y configuración de un sistema de integración continua (CI)

Para este proyecto se ha seleccionado un sistema de integración continua (CI) gratuito que permita ejecutar automáticamente los tests al realizar cambios en el repositorio de GitHub. Entre las opciones estándar se encuentran CircleCI, Jenkins, Travis CI y GitHub Actions.

Se ha decidido utilizar **GitHub Actions**, por las siguientes razones:

- **Integración nativa con GitHub:** se activa automáticamente al hacer un push o abrir un pull request, sin necesidad de configuraciones externas complejas.

- **Gratuito para repositorios públicos:** permite configurar pipelines completos sin coste adicional.

- **Flexibilidad y compatibilidad:** permite ejecutar comandos de backend (Maven) y frontend (npm), levantar servicios como PostgreSQL para pruebas de integración, y generar informes de tests y cobertura.

- **Automatización completa:** garantiza que todos los cambios en el repositorio sean probados automáticamente, asegurando calidad de código antes de cualquier despliegue.

### Flujo de CI configurado

1. Instalación de dependencias de backend y frontend.  
2. Levantamiento de un contenedor de PostgreSQL para pruebas de integración.  
3. Ejecución de tests de backend con Maven y tests de frontend con npm.  
4. Generación de reportes y resultados de tests para revisión automática.

De esta manera, cada push al repositorio activa el pipeline de GitHub Actions y ejecuta todos los tests de manera consistente y reproducible.

## Resumen rápido de herramientas y configuraciones de testing

### Backend (Java / Spring Boot)
- **Test runner:** JUnit 5 (Jupiter)  
- **Biblioteca de aserciones:** AssertJ (estilo BDD)  
- **Ejecución de tests:** Maven (`mvn test` / `mvn verify`)  
- **Plugins:** `maven-surefire` para unit tests, `maven-failsafe` para integration tests  
- **Integración CI:** GitHub Actions  
- **Base de datos de prueba:** PostgreSQL (contenedor durante tests de integración)

### Frontend (React / JavaScript)
- **Test runner:** Jest  
- **Aserciones / testing DOM:** Jest + Testing Library  
- **Ejecución de tests:** npm scripts (`npm ci`, `npm run ci:test`)  
- **Estilo de tests:** BDD (`describe` / `it`)  
- **Integración CI:** GitHub Actions

### Orquestación general
- **Makefile:** objetivo `make test` para ejecutar backend y frontend en un solo comando  

### Integración continua (CI)
- **Plataforma:** GitHub Actions  
- **Flujo CI:** instalación de dependencias, levantamiento de PostgreSQL, ejecución de tests backend y frontend, generación de reportes automáticos  



