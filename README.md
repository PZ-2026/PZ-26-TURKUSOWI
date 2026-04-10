# PZ-26-TURKUSOWI: Backend API & Frontend Monorepo

To repozytorium to monorepo. Oznacza to, że trzymamy tu obok siebie zarówno kod aplikacji mobilnej (frontend), jak i serwera (backend). 

## Jak odpalić Backend

Nasz backend (Java 17 + Spring Boot 4.0.5) jest w 100% zintegrowany z Dockerem. Nie musisz instalować Javy, Gradle'a ani niczego konfigurować ręcznie. Wystarczy jeden przycisk.

**Wymagania wstępne:**
Musisz mieć zainstalowanego i włączonego **Docker Desktop**. Upewnij się, że na dole aplikacji świeci się zielony status "Engine running". Jeśli Docker jest wyłączony, konsola wywali Ci błąd.

**Kroki uruchomienia:**
1. Otwórz terminal (lub wiersz poleceń).
2. Przejdź do **głównego folderu projektu** (tam, gdzie leży plik `docker-compose.yml`).
3. Wpisz i zatwierdź komendę:
   ```bash
   docker-compose up --build

4. Docker sam pobierze odpowiednią Javę, zbuduje kod przy pomocy narzędzia Gradle i odpali serwer.
5. Kiedy w logach zobaczysz linijkę: Started BackendApiApplication in X seconds – gotowe!