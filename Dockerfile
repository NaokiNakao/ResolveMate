FROM amazoncorretto:21-alpine

WORKDIR /app

COPY target/ResolveMate-0.0.1-SNAPSHOT.jar resolve-mate.jar

ENTRYPOINT ["java", "-Xms100m", "-Xmx512m", "-XX:+UseG1GC", "-jar", "resolve-mate.jar", "--spring.profiles.active=prod"]