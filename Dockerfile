# ── Stage 1: Build ────────────────────────────────────────────────────────────
FROM eclipse-temurin:17-jdk-alpine AS builder

WORKDIR /app

# Copy Gradle wrapper and dependency manifests first for better layer caching
COPY gradlew .
COPY gradle/ gradle/
COPY build.gradle .
COPY settings.gradle .

# Pre-fetch dependencies (layer is cached as long as build files don't change)
RUN chmod +x gradlew && ./gradlew dependencies --no-daemon

# Copy the full source and build the fat JAR, skipping tests
COPY src/ src/
RUN ./gradlew bootJar --no-daemon -x test

# ── Stage 2: Runtime ──────────────────────────────────────────────────────────
FROM eclipse-temurin:21-jre-alpine AS runtime

WORKDIR /app

# Copy only the executable JAR produced by the build stage
COPY --from=builder /app/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
