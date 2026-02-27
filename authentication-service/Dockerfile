FROM eclipse-temurin:21-jdk
WORKDIR /app

# Copy the Spring Boot fat JAR explicitly
COPY build/libs/*-SNAPSHOT.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java","-Xms128m","-Xmx384m","-jar","/app/app.jar"]