FROM gradle:7.4.1-jdk11 AS builder

WORKDIR /home/gradle/backend

RUN mkdir data_collector domain metrics_extractor persistence server \
    && chown gradle:gradle . -R

USER gradle

COPY --chown=gradle:gradle build.gradle.kts settings.gradle.kts ./
COPY --chown=gradle:gradle data_collector/build.gradle.kts ./data_collector/
COPY --chown=gradle:gradle domain/build.gradle.kts ./domain/
COPY --chown=gradle:gradle metrics_extractor/build.gradle.kts ./metrics_extractor/
COPY --chown=gradle:gradle persistence/build.gradle.kts ./persistence/
COPY --chown=gradle:gradle server/build.gradle.kts ./server

RUN gradle clean build

COPY --chown=gradle:gradle data_collector/src/ ./data_collector/src/
COPY --chown=gradle:gradle domain/src ./domain/src/
COPY --chown=gradle:gradle metrics_extractor/src/ ./metrics_extractor/src/
COPY --chown=gradle:gradle persistence/src/ ./persistence/src/
COPY --chown=gradle:gradle server/src/ ./server/src/

RUN gradle :server:shadowJar


FROM eclipse-temurin:11-jdk-alpine AS app

COPY --from=builder /home/gradle/backend/server/build/libs/server*-all.jar app.jar

CMD ["java", "-jar", "app.jar"]
