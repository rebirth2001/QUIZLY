FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

# Persist changes inside the docker maintained filesystem
VOLUME /tmp

# Expose the port 9010(eureka server) to other docker containers.
EXPOSE 9010

# Create a user to run the app as(root is used by default and that isn't secure)
RUN addgroup -S app && adduser -S app -G app

# Switch to the newly created user
USER app

# Copy the jar file into the docker image
COPY target/*.jar app.jar

# Run the jar file as an entry command.
ENTRYPOINT ["java","-jar","/app.jar"]