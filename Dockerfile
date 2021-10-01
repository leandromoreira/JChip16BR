FROM maven:3.6.0-jdk-11-slim
RUN mkdir -p /home/app
WORKDIR /home/app/
COPY pom.xml .
RUN mvn install
#RUN mvn -f /home/app/pom.xml clean package
