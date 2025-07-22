# Usa una imagen oficial de Maven con JDK 17
FROM maven:3.9.6-eclipse-temurin-17 AS build

# Copia el código fuente al contenedor
COPY . /app

# Establece el directorio de trabajo
WORKDIR /app

# Construye el JAR
RUN mvn clean package -DskipTests

# Segunda fase: usar JDK más ligero para correr el JAR
FROM eclipse-temurin:17-jdk

# Copia el JAR generado
COPY --from=build /app/target/*.jar app.jar

# Puerto expuesto
EXPOSE 8080

# Comando para ejecutar la app
ENTRYPOINT ["java", "-jar", "app.jar"]
