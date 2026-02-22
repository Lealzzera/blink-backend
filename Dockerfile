FROM amazoncorretto:21-alpine3.22
ARG JAR_FILE=build/libs/*.jar
ENV TZ=Brazil/East
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]