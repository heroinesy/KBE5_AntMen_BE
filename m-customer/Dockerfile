FROM eclipse-temurin:17-jre
WORKDIR /app
COPY build/libs/m-customer.jar app.jar
CMD ["java", "-Xmx512m", "-Xms256m", "-jar", "app.jar"]