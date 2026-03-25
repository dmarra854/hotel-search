# ─────────────────────────────────────────────
# Stage 1: Build the application with Maven
# ─────────────────────────────────────────────
FROM maven:3.9.9-eclipse-temurin-21-alpine AS builder

WORKDIR /build

# Copy dependency descriptors first to exploit Docker layer cache
COPY pom.xml .
RUN mvn dependency:go-offline -q

# Copy source code and build (skip tests — tests run in CI, not at image build time)
COPY src ./src
RUN mvn package -DskipTests -q

# ─────────────────────────────────────────────
# Stage 2: Minimal JRE runtime image
# ─────────────────────────────────────────────
FROM eclipse-temurin:21-jre-alpine AS runtime

# Non-root user for security
RUN addgroup -S appgroup && adduser -S appuser -G appgroup
USER appuser

WORKDIR /app

COPY --from=builder /build/target/hotel-search-1.0.0.jar app.jar

EXPOSE 8080

# Virtual threads are enabled via spring.threads.virtual.enabled=true in application.yml
# -XX:+UseContainerSupport ensures JVM respects Docker CPU/memory limits
ENTRYPOINT ["java", "-XX:+UseContainerSupport", "-jar", "app.jar"]
