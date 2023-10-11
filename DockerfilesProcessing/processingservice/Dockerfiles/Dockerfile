FROM openjdk:11

RUN apt-get -y update && apt-get install -y maven

RUN mkdir -p /src/main/java/org/jacp
RUN mkdir -p /src/test/java/org/jacp

COPY pom.xml /src/main/java/org/jacp/
COPY runtests.sh /src/main/java/org/jacp/

RUN chmod u+x /src/main/java/org/jacp/runtests.sh

WORKDIR /src/main/java/org/jacp

RUN mvn -B dependency:resolve dependency:resolve-plugins