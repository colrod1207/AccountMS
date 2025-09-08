# Dockerfile multi-stage para optimizar el tamaño de la imagen
FROM openjdk:17-jdk-slim as builder

WORKDIR /app
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src src

# Construir la aplicación
RUN ./mvnw clean package -DskipTests

# Etapa de runtime
FROM openjdk:17-jdk-slim

WORKDIR /app

# Crear usuario no-root para seguridad
RUN addgroup --system spring && adduser --system spring --ingroup spring
USER spring:spring

# Copiar el JAR construido
COPY --from=builder /app/target/*.jar app.jar

# Exponer el puerto
EXPOSE 8081

# Variables de entorno
ENV SPRING_PROFILES_ACTIVE=prod

# Comando de inicio
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
