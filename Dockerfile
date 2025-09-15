# -------------------------
# Build stage
# -------------------------
FROM maven:3.9.9-eclipse-temurin-21 AS builder
WORKDIR /app

# Cache dependencia do pom.xml
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copia do codigo fonte
COPY src ./src

# Compila
RUN mvn clean package -DskipTests

# -------------------------
# Runtime stage
# -------------------------
FROM eclipse-temurin:21-jre AS runtime
WORKDIR /app

# Copia apenas o JAR (imagem final fica bem menor)
COPY --from=builder /app/target/garage-service-0.0.1-SNAPSHOT.jar app.jar

# Variáveis de ambiente padrão
ENV SPRING_PROFILES_ACTIVE=default \
    JAVA_OPTS="-Xms256m -Xmx512m"

EXPOSE 3003

# Usa JAVA_OPTS configurável
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
