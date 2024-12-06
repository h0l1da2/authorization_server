FROM gradle:7.6-jdk as builder

WORKDIR /builder
COPY /src/main /builder/src/main
COPY build.gradle settings.gradle /builder/

RUN gradle clean build

FROM eclipse-temurin:17.0.12_7-jre-noble AS runner

WORKDIR /app

COPY --from=builder /builder/build/libs/*.jar /app/auth.jar

EXPOSE 8000

CMD ["java", "-Duser.timezone=Asia/Seoul", "-jar", "/app/auth.jar"]