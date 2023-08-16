FROM maven:3.8.7-openjdk-18-slim
WORKDIR /jrmjacp
RUN chmod 755 /jrmjacp
COPY . .
RUN mvn -B dependency:resolve dependency:resolve-plugins
RUN mvn verify --fail-never
