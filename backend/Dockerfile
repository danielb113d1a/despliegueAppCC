# --- Etapa 1: Compilación (Builder) con Java 21 ---
# Usamos la imagen que cargamos manualmente
FROM maven:3.9.9-eclipse-temurin-21 AS builder

WORKDIR /app

COPY pom.xml .
COPY src ./src

# Compilamos saltando tests
RUN mvn clean package -DskipTests

# --- Etapa 2: Ejecución (Runtime) con Java 21 ---
# Usamos la versión ligera JRE 21 que cargamos manualmente
FROM eclipse-temurin:21-jre

WORKDIR /app

# Copiamos el jar
COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]