# ETAP 1: Budowanie aplikacji przy użyciu JDK i skryptu Gradle Wrapper
FROM eclipse-temurin:17-jdk-alpine AS builder
WORKDIR /app

# Kopiujemy folder wrappera i sam skrypt
COPY gradle/ ./gradle/
COPY gradlew build.gradle.kts settings.gradle.kts ./

# Nadajemy skryptowi uprawnienia do uruchamiania (Linuxowe "chmod")
RUN chmod +x gradlew

# Kopiujemy kod źródłowy
COPY src ./src

# Odpalamy budowanie pliku .jar używając wrappera (kropka i ukośnik przed gradlew to podstawa w Linuksie!)
RUN ./gradlew clean bootJar --no-daemon -x test

# ETAP 2: Docelowe środowisko uruchomieniowe (lekki obraz)
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Kopiujemy zbudowany plik
COPY --from=builder /app/build/libs/*.jar app.jar

# Informujemy, że aplikacja działa na porcie 8080
EXPOSE 8080

# Komenda uruchamiająca backend
ENTRYPOINT ["java", "-jar", "app.jar"]