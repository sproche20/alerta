# Imagen base con Maven y Java
FROM maven:3.8.6-openjdk-17

# Directorio de trabajo dentro del contenedor
WORKDIR /app

# Copiar pom.xml y código fuente
COPY pom.xml .
COPY src ./src

# Construir el proyecto (sin tests para acelerar)
RUN mvn clean package -DskipTests

# Exponer el puerto que usará la app
EXPOSE 8080

# Comando para ejecutar la app
CMD ["java", "-jar", "target/alerta-0.0.1-SNAPSHOT.jar"]
