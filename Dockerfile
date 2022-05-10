FROM openjdk:17-alpine3.14

COPY target/portfoliosh-0.0.1-SNAPSHOT.jar /app.jar

CMD ["java", "-Dserver.port=8086", "-jar", "/app.jar"]