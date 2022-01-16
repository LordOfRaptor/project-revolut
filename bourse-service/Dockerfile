FROM maven:3.8.4-openjdk-17-slim AS BUILDER
WORKDIR /app
COPY pom.xml .
COPY /src ./src
RUN mvn -e -B package -DskipTests

FROM gcr.io/distroless/java17-debian11
COPY --from=BUILDER /app/target/bourse-service-1.1.jar /app/target/bourse-service-1.1.jar
EXPOSE 8082
CMD ["/app/target/bourse-service-1.1.jar"]