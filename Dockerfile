FROM eclipse-temurin:17-jdk

WORKDIR /app

COPY target/library-books-management-api-0.0.1-SNAPSHOT.jar app.jar

EXPMSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]