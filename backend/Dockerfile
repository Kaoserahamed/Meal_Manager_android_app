FROM eclipse-temurin:17-jdk-jammy AS builder
WORKDIR /workspace/backend
COPY backend/gradlew backend/gradle.properties backend/settings.gradle.kts backend/build.gradle.kts ./
COPY backend/gradle gradle
COPY backend/src src
RUN ./gradlew bootJar -x test

FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
COPY --from=builder /workspace/backend/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
