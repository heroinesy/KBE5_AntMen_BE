FROM eclipse-temurin:17-jre

RUN microdnf install -y wget

WORKDIR /app
COPY build/libs/app.jar app.jar
CMD ["java", "-jar", "app.jar"]