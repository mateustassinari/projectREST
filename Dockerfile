# ---- build stage ----
FROM eclipse-temurin:25-jdk AS build
WORKDIR /workspace

# Copy the wrapper and build files first so the Gradle download and dependencies are cached
COPY gradlew settings.gradle.kts build.gradle.kts gradle.properties ./
COPY gradle gradle
RUN sed -i 's/\r$//' gradlew && chmod +x gradlew && ./gradlew --no-daemon --version

COPY src src
RUN ./gradlew --no-daemon bootJar -x test \
    && cp "$(ls build/libs/*.jar | grep -v -- -plain | head -1)" /app.jar

# ---- runtime stage ----
FROM eclipse-temurin:25-jre
RUN useradd --system --no-create-home appuser
COPY --from=build /app.jar /app.jar
USER appuser
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]
