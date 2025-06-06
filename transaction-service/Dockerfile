FROM openjdk:21-slim

WORKDIR /app

COPY target/transaction-service-1.0.0.jar app.jar

EXPOSE 8976

CMD ["java", "-jar", "app.jar"]