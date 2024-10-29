FROM openjdk:17-jdk

COPY ./build/libs/*SNAPSHOT.jar /usr/src/app/app.jar

ENTRYPOINT ["java", "-jar", "/usr/src/app/app.jar"]