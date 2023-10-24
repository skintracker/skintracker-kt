# Use a base image with GraalVM Enterprise Edition and the JDK 21
FROM ghcr.io/graalvm/graalvm-community:21 as build

# Set the working directory
WORKDIR /app

# Copy only the Gradle files needed for dependency resolution
COPY build.gradle.kts settings.gradle.kts /app/

# Switch to root user
USER root

RUN native-image --version || echo 'Native-image not available'

# Copy the rest of the project files into the container
COPY . /app

# Build the Kotlin project with Gradle
RUN ./gradlew clean build

# Build your Kotlin application using the GraalVM compiler
RUN native-image -jar build/libs/skintracker-kt-1.0-SNAPSHOT.jar

# Start with a minimal runtime image
FROM debian:stable-slim

# Copy the compiled application from the previous build stage
COPY --from=build /app/skintracker-kt-1.0-SNAPSHOT /app/skintracker-kt-1.0-SNAPSHOT

# Expose the port your application will listen on
EXPOSE 8080

# Set the entry point for your application
CMD ["/app/skintracker-kt-1.0-SNAPSHOT"]
