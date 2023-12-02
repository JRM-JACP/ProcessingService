FROM openjdk:11

RUN apt-get -y update && apt-get install -y maven

RUN mkdir -p ProcessingService/src/main/java/org/jacp
RUN mkdir -p ProcessingService/src/test/java/org/jacp

WORKDIR /ProcessingService

COPY pom.xml .
COPY runtests.sh .

RUN chmod u+x ./runtests.sh

RUN mvn -B dependency:resolve dependency:resolve-plugins

