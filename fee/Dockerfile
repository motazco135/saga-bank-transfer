FROM openjdk:22
WORKDIR /app
COPY target/*.jar app.jar
# Expose port Based on our configuration for each microservice
EXPOSE 8085
ENTRYPOINT ["java", "-jar", "app.jar"]