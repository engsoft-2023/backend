FROM openjdk:11.0-jdk

ENV PORT=8080

WORKDIR /usr/src/app

COPY . .

RUN ./mvnw clean install -Dmaven.test.skip

EXPOSE ${PORT}

CMD ./mvnw spring-boot:run