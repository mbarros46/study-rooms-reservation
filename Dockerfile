# Build
FROM gradle:8.10.0-jdk17 AS build
WORKDIR /app
COPY . .
RUN gradle -q bootJar

# Run
FROM eclipse-temurin:17-jre
WORKDIR /app
ENV JAVA_OPTS=""
COPY --from=build /app/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["sh","-c","java $JAVA_OPTS -jar app.jar"]