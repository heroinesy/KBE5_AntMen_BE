FROM openjdk:17

RUN microdnf install -y wget

WORKDIR /app
COPY build/libs/app.jar app.jar
CMD ["java", "-jar", "app.jar"]