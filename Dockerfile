# -- Stage 1: Build -----------------------------------------------------------
FROM maven:3.9-eclipse-temurin-25-alpine AS builder

WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline -q

COPY .mvn ./.mvn
COPY mvnw .
COPY src ./src
RUN mvn clean package -DskipTests -q

# -- Stage 2: Runtime ---------------------------------------------------------
FROM eclipse-temurin:25-jre-alpine

WORKDIR /app

RUN addgroup -S spring && adduser -S spring -G spring
USER spring

COPY --from=builder /app/target/springboot-template-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

# 9. JVM Docker image is fine, but consider memory tuning (VPS stability)
  #
  #You may add:
  #
  #ENV JAVA_OPTS="-XX:MaxRAMPercentage=75"
  #ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]

ENV JAVA_OPTS="-XX:MaxRAMPercentage=70"
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
