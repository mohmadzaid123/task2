# syntax=docker/dockerfile:1
# ── Stage 1: build the fat JAR ──
FROM eclipse-temurin:17-jdk-alpine AS builder
WORKDIR /workspace

# Cache layer — Gradle wrapper + build config first (rarely changes).
COPY gradlew settings.gradle build.gradle ./
COPY gradle ./gradle
RUN chmod +x ./gradlew && \
    ./gradlew dependencies --no-daemon -q || true

# Source — only this layer rebuilds on code changes.
COPY src ./src
RUN ./gradlew bootJar --no-daemon -x test

# ── Stage 2: minimal runtime, non-root ──
FROM eclipse-temurin:17-jre-alpine
RUN apk add --no-cache curl && \
    addgroup -S appgroup && adduser -S appuser -G appgroup
WORKDIR /app
USER appuser
COPY --from=builder /workspace/build/libs/*.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
