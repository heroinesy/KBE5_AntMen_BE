FROM eclipse-temurin:17-jre
WORKDIR /app
COPY m-manager/build/libs/m-manager.jar app.jar
CMD ["java", "-Xmx512m", "-Xms256m", "-jar", "app.jar"]