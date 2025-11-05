FROM gradle:8.14-jdk21-alpine AS build
WORKDIR /app

COPY gradle.properties ./

COPY build.gradle.kts settings.gradle.kts ./
COPY buildSrc ./buildSrc

RUN gradle --parallel --build-cache build --no-daemon -x test --dry-run || true

COPY src ./src

RUN gradle --parallel --build-cache build --no-daemon -x test && \
    find / -type s \( -path "*/.kotlin/*" -o -path "*/.gradle/*" \) -delete 2>/dev/null || true && \
    rm -rf /root/.kotlin/daemon* /root/.gradle/daemon* /root/.cache /app/.gradle /app/.kotlin /tmp/* 2>/dev/null || true

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

ENV JAVA_OPTS="-XX:+UseG1GC -XX:MaxRAMPercentage=75.0 -XX:+ParallelRefProcEnabled -XX:+UseStringDeduplication -Xss256k"

COPY --from=build /app/build/libs/*.jar app.jar

EXPOSE 8080
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
  CMD wget -q -O- http://localhost:8080/actuator/health > /dev/null || exit 1

ENTRYPOINT ["sh", "-c", "exec java $JAVA_OPTS -jar app.jar"]