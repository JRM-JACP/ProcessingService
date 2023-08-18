FROM openjdk:11
RUN apt-get -y update && apt-get install -y maven
WORKDIR /jrmjacp
COPY pom.xml .
RUN mvn -B dependency:resolve dependency:resolve-plugins
#RUN chmod 777 /jrmjacp
COPY runtests.sh .
RUN chmod u+x runtests.sh
COPY . .
