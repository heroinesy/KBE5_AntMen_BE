FROM eclipse-temurin:17-jre
WORKDIR /app
COPY m-admin/build/libs/m-admin.jar app.jar
CMD ["java", "-Xmx512m", "-Xms256m", "-jar", "app.jar"]