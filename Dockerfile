# Compilación (Build)
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
# Copia pom y descarga dependencias
COPY pom.xml .
RUN mvn dependency:go-offline

# Copia código fuente y compila
COPY src ./src
RUN mvn clean package -DskipTests

# Ejecución
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
# Copiamos solo el JAR resultante de la etapa anterior
COPY --from=build /app/target/*.jar app.jar

ENTRYPOINT ["java", "-XX:+UseZGC", "-jar", "app.jar"]