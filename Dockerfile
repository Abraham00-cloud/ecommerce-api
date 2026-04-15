FROM openjdk:25-ea-slim-bookworm

WORKDIR /app

COPY target/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]