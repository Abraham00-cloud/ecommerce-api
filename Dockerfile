# Stage 1: JDK 25 build environment with Maven
FROM openjdk:25-ea-slim-bookworm AS build

# Copy Maven binaries from the official Maven image into JDK 25
COPY --from=maven:3.9.9-eclipse-temurin-21 /usr/share/maven /usr/share/maven
ENV MAVEN_HOME=/usr/share/maven
ENV PATH=$MAVEN_HOME/bin:$PATH

WORKDIR /app

# Download dependencies first for caching
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source and compile using Java 25
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Java 25 Runtime container
FROM openjdk:25-ea-slim-bookworm
WORKDIR /app

# Copy the generated JAR from the build stage
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]