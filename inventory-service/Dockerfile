#---------------------------
# BUILD STAGE
#---------------------------
FROM gradle:8-jdk21 AS builder

# Set up the working directory
WORKDIR /home/gradle/src

# Copy the source code
COPY --chown=gradle:gradle . /home/gradle/src

# Build the application without running tests
RUN gradle build -x test --no-daemon --stacktrace

#---------------------------
# FINAL STAGE
#---------------------------
FROM openjdk:21-jdk

# Set up the application directory
RUN mkdir /app

# Copy the built JAR from the builder stage
COPY --from=builder /home/gradle/src/build/libs/*.jar /app/product-service.jar

# Set environment variables
ENV MONGO_DB_USERNAME=admin \
    MONGO_DB_PWD=password \
    JAVA_HOME=/usr/lib/jvm/java-21-openjdk

# Expose the application port
EXPOSE 8086

# Define the entry point to run the application
ENTRYPOINT ["java", "-jar", "/app/inventory-service.jar"]
