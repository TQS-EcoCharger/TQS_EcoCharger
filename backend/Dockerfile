FROM openjdk:21-jdk-slim

WORKDIR /app

# Install curl (and clean cache to keep image small)
RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*

COPY target/*.jar app.jar

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]
