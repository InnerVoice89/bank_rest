FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/processingBankCards-0.0.1-SNAPSHOT.jar bank_rest.jar
ENTRYPOINT ["java","-jar","bank_rest.jar"]