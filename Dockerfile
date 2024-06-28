FROM amazoncorretto:21-alpine

COPY target/ResolveMate-0.0.1-SNAPSHOT.jar resolve-mate.jar

ENTRYPOINT ["java", "-jar", "resolve-mate.jar", "--spring.profiles.active=prod"]