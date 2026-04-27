# PZ-26-TURKUSOWI

Monorepo projektu schroniska. Repo zawiera:
- `backend` - Spring Boot API
- `frontend` - aplikacje mobilna Android
- `docker` - konfiguracje pomocnicze dla bazy i uruchomienia backendu

## Struktura

```text
PZ-26-TURKUSOWI/
|- backend/
|- frontend/
|- docker/
|- docker-compose.yml
```

## Wymagania

### Backend

- Docker Desktop
- uruchomiony Docker Engine

### Frontend

- Android Studio
- Android SDK
- telefon w tej samej sieci Wi-Fi co komputer albo emulator Androida

## Uruchomienie backendu

Backend uruchamiamy z glownego folderu projektu:

```bash
docker-compose up --build
```

Po poprawnym starcie backend powinien byc dostepny pod:

```text
http://localhost:8080
```

Uwaga:
- `http://localhost:8080` moze pokazac `Whitelabel Error Page` i to jest OK
- to nie znaczy, ze backend nie dziala
- poprawny test API to np.:

```text
http://localhost:8080/api/zwierzeta
```

Jesli ten adres zwraca JSON, backend dziala poprawnie.

## Uruchomienie frontendu

W Android Studio otwieraj folder:

```text
frontend
```

Nie otwieraj glownego folderu monorepo jako projektu Android, tylko sam modul `frontend`.

## Konfiguracja adresu API

Frontend czyta adres backendu z pliku:

```text
frontend/local.properties
```

Uzywana jest wlasciwosc:

```properties
api.base.url=http://ADRES:8080/
```

Ten plik jest lokalny i nie powinien byc commitowany do repo.

Jesli `api.base.url` nie zostanie ustawione, aplikacja uzyje fallbacku:

```text
http://10.0.2.2:8080/
```

To dziala dla emulatora Androida.

## Jak ustawic frontend na emulatorze

W `frontend/local.properties` mozesz ustawic:

```properties
api.base.url=http://10.0.2.2:8080/
```

albo usunac wpis `api.base.url` i wtedy zadziala fallback.

## Jak ustawic frontend na fizycznym telefonie

Telefon i komputer musza byc w tej samej sieci Wi-Fi.

1. Sprawdz IP komputera w tej sieci, np. przez:

```powershell
ipconfig
```

2. Znajdz aktywne IPv4 dla Wi-Fi, np.:

```text
192.168.0.203
```

3. Wpisz je do `frontend/local.properties`:

```properties
api.base.url=http://192.168.0.203:8080/
```

4. Uruchom backend.
5. Zrob w Android Studio:
- `Sync Project with Gradle Files`
- `Clean Project`
- `Rebuild Project`
6. Wgraj aplikacje ponownie na telefon.

## Test polaczenia telefonu z backendem

Przed odpaleniem aplikacji warto sprawdzic w przegladarce na telefonie:

```text
http://TWOJE_IP_KOMPUTERA:8080/api/zwierzeta
```

Przyklad:

```text
http://192.168.0.203:8080/api/zwierzeta
```

Jesli telefon widzi JSON, aplikacja tez powinna sie polaczyc.

## Typowe problemy

### 1. `CLEARTEXT communication not permitted`

To znaczy, ze Android blokuje ruch HTTP. Projekt ma juz skonfigurowany `network_security_config`, wiec po rebuildzie problem nie powinien wracac.

### 2. `failed to connect to /10.0.2.2`

To zwykle znaczy, ze:
- aplikacja ma stary build
- odpalona jest stara wersja APK
- wpis w `local.properties` nie zostal jeszcze zaczytany

Naprawa:
- odinstaluj aplikacje z telefonu
- zrob `Clean Project`
- zrob `Rebuild Project`
- zainstaluj apke ponownie

### 3. `failed to connect to 192.168.x.x:8080`

To zwykle znaczy, ze:
- backend nie dziala
- telefon i komputer nie sa w tej samej sieci
- IP komputera sie zmienilo
- firewall blokuje port `8080`

Sprawdz:
- czy backend odpowiada pod `http://localhost:8080/api/zwierzeta`
- czy telefon widzi `http://TWOJE_IP:8080/api/zwierzeta`
- czy Windows Firewall ma odblokowany port `8080`

### 4. `Whitelabel Error Page` na `localhost:8080`

To jest normalne. Backend nie ma strony glownej pod `/`.

Testuj API przez endpointy, np.:

```text
http://localhost:8080/api/zwierzeta
```

## Workflow dla zespolu

### Osoba pracujaca na emulatorze

- uruchamia backend przez Docker
- otwiera `frontend` w Android Studio
- moze uzywac fallbacku `10.0.2.2`

### Osoba pracujaca na fizycznym telefonie

- uruchamia backend przez Docker
- sprawdza swoje aktualne IP komputera
- wpisuje je lokalnie do `frontend/local.properties`
- robi sync, clean, rebuild i reinstalacje APK

## Przykladowy `local.properties`

```properties
sdk.dir=C\:\\Users\\NAZWA\\AppData\\Local\\Android\\Sdk
api.base.url=http://192.168.0.203:8080/
```
