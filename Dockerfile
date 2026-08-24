# FROM eclipse-temurin:17-jdk

# WORKDIR /app

# COPY target/library-books-management-api-0.0.1-SNAPSHOT.jar app.jar

# EXPOSE 8080

# ENTRYPOINT ["java", "-jar", "app.jar"]


# ---------- Stage 1: Build the JAR ----------
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app

# Copy only the pom first, so dependency downloads get cached
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Now copy the rest of the source and build
COPY src ./src
RUN mvn clean package -DskipTests -B

# ---------- Stage 2: Run the JAR ----------
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Copy only the built JAR from the previous stage
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]