# --- Etapa 1: Construcción con Maven ---
FROM maven:3.9.6-eclipse-temurin-17 AS builder

# Crear directorio de trabajo
WORKDIR /app

# Copiar pom.xml y resolver dependencias primero
COPY pom.xml .
RUN mvn -q dependency:go-offline

# Copiar el resto del código fuente
COPY . .

# Construir el JAR (sin ejecutar tests para acelerar)
RUN mvn -q -DskipTests package


# --- Etapa 2: Ejecutar la app con Java ---
FROM eclipse-temurin:17-jre

WORKDIR /app

# Copiar JAR construido desde la fase anterior
COPY --from=builder /app/target/*.jar app.jar

# Exponer el puerto de mi backend
EXPOSE 8080

# Comando de inicio
ENTRYPOINT ["java", "-jar", "app.jar"]
