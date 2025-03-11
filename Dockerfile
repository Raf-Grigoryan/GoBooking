FROM openjdk:17-jdk

WORKDIR /app

COPY GoBooking-rest/target/GoBooking-rest-0.0.1-SNAPSHOT.jar app.jar

COPY GoBooking-rest/src/main/resources/application.yml /app/application.yml

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "app.jar"]