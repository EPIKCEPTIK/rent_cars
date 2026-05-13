# Stage 1: Build the app using Maven
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src

# Build the jar
RUN mvn clean package -DskipTests

# Stage 2: Run the app using a lighter JRE
FROM eclipse-temurin:21-jre
WORKDIR /app

# Copy the jar from the build stage
COPY --from=build /app/target/rent_cars.jar app.jar
CMD ["java", "-jar", "app.jar"]