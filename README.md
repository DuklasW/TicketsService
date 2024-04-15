## Aplikacja Obsługi Biletów - Backend

Aplikacja Obsługi Biletów to skalowalny i wydajny backend dla strony internetowej do sprzedaży biletów, zbudowany przy użyciu technologii Spring Boot i MongoDB. Aplikacja zapewnia kompleksowe API do zarządzania wydarzeniami artystycznymi, obsługi zakupów biletów oraz interakcji użytkowników.
Główne funkcje aplikacji obejmują rejestrację i uwierzytelnianie użytkowników, zarządzanie wydarzeniami i artystami, integrację z systemem płatności PayPal oraz możliwość dodawania komentarzy i ocen. Dzięki warstwowej architekturze i wykorzystaniu najlepszych praktyk, aplikacja jest łatwa w utrzymaniu i rozbudowie.

## Dostęp do Dokumentacji API przez Swagger

Aby ułatwić testowanie i eksplorację API, udostępniam dokumentację Swagger, która jest dostępna online. Można przetestować dostępne endpointy i zapoznać się z funkcjonalnościami API, odwiedzając [node.codedream.eu](https://node.codedream.eu:18080/swagger-ui/index.html).

### Spis Treści
1. [Funkcje](#funkcje)
2. [Technologie](#technologie)
3. [Architektura](#architektura)
4. [Jak Zacząć](#jak-zacząć)
5. [Dokumentacja API](#dokumentacja-api)
6. [Testowanie](#testowanie)
7. [Autor](#autor)
8. [Licencja](#licencja)

### Funkcje

Aplikacja Obsługi Biletów oferuje szereg funkcji, które usprawniają zarządzanie wydarzeniami artystycznymi i zapewniają użytkownikom wygodne korzystanie z systemu:

1. **Rejestracja i logowanie użytkowników:**
   - Aplikacja obsługuje rejestrację i logowanie dla różnych typów użytkowników: administratorów, menedżerów oraz konsumentów.
   - Każdy typ użytkownika ma dostęp do odpowiednich funkcji i uprawnień zgodnie z przypisaną rolą.
   - Logowanie odbywa się za pomocą adresu e-mail i hasła, zapewniając bezpieczny dostęp do systemu.

2. **Zarządzanie wydarzeniami:**
   - Menedżerowie mają możliwość tworzenia nowych wydarzeń, podając wszystkie niezbędne informacje, między innymi takie jak nazwa, data, miejsce, opis i cena biletów.
   - Menedżerowie mogą również usuwać wydarzenia, jeśli zajdzie taka potrzeba.
   - Konsumenci mają dostęp do przeglądania listy dostępnych wydarzeń, wraz z szczegółowymi informacjami na ich temat.

3. **Zarządzanie artystami:**
   - Menedżerowie mogą dodawać informacje o artystach, między innymi takie jak nazwa, opis oraz rodzaj ich działalności artystycznej.
   - Aplikacja przechowuje kompleksową listę artystów, którą można przeglądać i przeszukiwać.

4. **Zakup biletów:**
   - Konsumenci mogą przeglądać dostępne wydarzenia i wybierać interesujące ich opcje.
   - W takcie zakupów, bilety są rezerwowane na określony czas aby na spokojnie dokonać płatnośći.
   - Integracja z systemem płatności PayPal zapewnia bezpieczne i wygodne przetwarzanie transakcji.
   - Po zakupie, bilety są przypisywane do konta użytkownika.

5. **Komentarze i oceny:**
   - Konsumenci mogą wyrazić swoją opinię na temat artystów poprzez dodawanie komentarzy i ocen.
   - Wszystkie komentarze i oceny są widoczne dla innych użytkowników, co pomaga w budowaniu społeczności i dzieleniu się opiniami.

6. **Autentykacja i Autoryzacja:**
   - Aplikacja implementuje mechanizm autentykacji oparty na tokenach bearer.
   - Użytkownicy uwierzytelniają się, podając swoje dane logowania, a w odpowiedzi otrzymują token dostępu.
   - Token dostępu jest używany w kolejnych żądaniach, aby zweryfikować tożsamość użytkownika i autoryzować dostęp do chronionych zasobów.
   - Aplikacja obsługuje również tokeny odświeżania, które umożliwiają uzyskanie nowego tokenu dostępu bez konieczności ponownego logowania, zapewniając płynne korzystanie z systemu.

### Technologie
Projekt wykorzystuje następujące technologie i narzędzia:
   - **Spring Boot 3.1.4**: Uproszczenie konfiguracji i uruchamiania aplikacji Springowych.
   - **Java 17**
   - **MongoDB**: Elastyczna baza danych NoSQL do efektywnego przechowywania i zarządzania danymi.
   - **Spring Security**: Rozbudowane wsparcie dla autentykacji i autoryzacji.
   - **Spring Data MongoDB**: Integracja z MongoDB, ułatwiająca operacje na bazie danych.
   - **JWT (JSON Web Tokens)**: Bezstanowy mechanizm uwierzytelniania dla aplikacji webowych.
   - **Spring Web**: Tworzenie RESTful web services.
   - **Spring Doc OpenAPI**: Automatyczne generowanie dokumentacji API.
   - **PayPal API**: Integracja z systemem płatności PayPal, umożliwiająca bezpieczne transakcje finansowe.
   - **Mockito**: Framework do tworzenia testów jednostkowych w Javie, pozwalający na mockowanie zależności w testowanych klasach.

### Architektura

Aplikacja Obsługi Biletów została zbudowana zgodnie z wzorcem architektury warstwowej, który zapewnia separację odpowiedzialności i ułatwia utrzymanie oraz rozbudowę systemu. Główne komponenty architektury to:

1. **Warstwa prezentacji (Kontrolery):**
   - Kontrolery są odpowiedzialne za obsługę żądań HTTP i mapowanie ich na odpowiednie akcje.
   - Zajmują się walidacją danych wejściowych, wywoływaniem usług oraz zwracaniem odpowiedzi do klienta.
   - Kontrolery wykorzystują obiekty DTO do komunikacji z warstwą usług.

2. **Warstwa usług (Serwisy):**
   - Serwisy zawierają logikę biznesową aplikacji i koordynują działania między różnymi komponentami.
   - Odpowiadają za przetwarzanie danych, wykonywanie operacji biznesowych oraz zarządzanie transakcjami.
   - Serwisy komunikują się z warstwą dostępu do danych za pomocą repozytoriów.

3. **Warstwa dostępu do danych (Repozytoria):**
   - Repozytoria są odpowiedzialne za interakcję z bazą danych MongoDB.
   - Zapewniają metody do pobierania, zapisywania, aktualizacji i usuwania danych.
   - Repozytoria zwracają obiekty encji, które reprezentują dane przechowywane w bazie danych.

4. **Mappery:**
   - Mappery są używane do konwersji między obiektami DTO a obiektami encji.
   - Ułatwiają przekształcanie danych między różnymi reprezentacjami, co pozwala na oddzielenie warstwy prezentacji od warstwy dostępu do danych.

5. **Fabryki:**
   - Fabryki są odpowiedzialne za tworzenie obiektów i zarządzanie ich zależnościami.
   - Ułatwiają tworzenie instancji obiektów z odpowiednimi zależnościami, co zwiększa modularność i testowalność aplikacji.

6. **Obiekty DTO (Data Transfer Object):**
   - Obiekty DTO są używane do przenoszenia danych między warstwami aplikacji.
   - Reprezentują dane w formacie dostosowanym do potrzeb warstwy prezentacji i są niezależne od wewnętrznej struktury encji.

7. **Encje:**
   - Encje reprezentują dane przechowywane w bazie danych MongoDB.
   - Odzwierciedlają strukturę dokumentów w kolekcjach MongoDB i zawierają adnotacje mapowania obiektowo-dokumentowego.

Dzięki zastosowaniu architektury warstwowej i wykorzystaniu odpowiednich wzorców projektowych, aplikacja Obsługi Biletów osiąga wysoką modularność, skalowalność i łatwość utrzymania. Poszczególne warstwy są luźno powiązane, co umożliwia niezależny rozwój i testowanie każdej z nich. Takie podejście ułatwia również wprowadzanie zmian i dodawanie nowych funkcjonalności w przyszłości.
Ponadto, aplikacja przestrzega zasad SOLID, co przyczynia się do tworzenia czystego i łatwo rozszerzalnego kodu

### Jak Zacząć

Aby uruchomić aplikację backendową Obsługi Biletów lokalnie:
1. Sklonuj repozytorium.
   -git clone https://github.com/DuklasW/TicketsService.git
2. Zainstaluj wymagane zależności.
   -mvn install
4. Skonfiguruj plik `.env` w katalogu resources projektu, podając potrzebne dane, takie jak dane dostępowe do MongoDB Atlas i konfiguracja PayPal.
5. Uruchom aplikację.
6. Korzystaj z punktów końcowych API za pomocą narzędzi takich jak Postman lub cURL.

### Dokumentacja API

Dokumentacja API jest dostępna pod adresem http://localhost:18080/swagger-ui/index.html, gdy aplikacja działa lokalnie. Zapewnia ona szczegółowe informacje na temat dostępnych punktów końcowych, formatów żądań/odpowiedzi oraz wymagań dotyczących uwierzytelnienia.

### Testowanie

Aplikacja zawiera testy jednostkowe i integracyjne napisane z użyciem JUnit i Mockito. Aby uruchomić testy, użyj polecenia:
	-mvn test

### Autor
Autor: Wojciech Duklas

### Licencja

Aplikacja backendowa Obsługi Biletów jest oprogramowaniem open-source licencjonowanym na zasadach licencji MIT.
