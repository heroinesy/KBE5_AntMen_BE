FROM eclipse-temurin:17-jre
WORKDIR /app
COPY m-common/build/libs/m-common.jar app.jar
CMD ["java", "-Xmx512m", "-Xms256m", "-jar", "app.jar"]