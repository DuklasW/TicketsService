## Aplikacja Obsługi Biletów - Backend

Aplikacja Obsługi Biletów to skalowalny i wydajny backend dla strony internetowej do sprzedaży biletów, zbudowany przy użyciu technologii Spring Boot i MongoDB. Aplikacja zapewnia kompleksowe API do zarządzania wydarzeniami artystycznymi, obsługi zakupów biletów oraz interakcji użytkowników.
Główne funkcje aplikacji obejmują rejestrację i uwierzytelnianie użytkowników, zarządzanie wydarzeniami i artystami, integrację z systemem płatności PayPal oraz możliwość dodawania komentarzy i ocen. Dzięki warstwowej architekturze i wykorzystaniu najlepszych praktyk, aplikacja jest łatwa w utrzymaniu i rozbudowie.
Aplikacja Obsługi Biletów demonstruje solidne zrozumienie tworzenia aplikacji backendowych przy użyciu popularnych frameworków i technologii. Może służyć jako przykład umiejętności programistycznych i doświadczenia w budowaniu skalowalnych i funkcjonalnych aplikacji webowych.



### Spis Treści
1. [Funkcje](#funkcje)
2. [Architektura](#architektura)
3. [Jak Zacząć](#jak-zacząć)
4. [Dokumentacja API](#dokumentacja-api)
5. [Licencja](#licencja)

### Funkcje

Aplikacja Obsługi Biletów oferuje szereg funkcji, które usprawniają zarządzanie wydarzeniami artystycznymi i zapewniają użytkownikom wygodne korzystanie z systemu:

1. **Rejestracja i logowanie użytkowników:**
   - Aplikacja obsługuje rejestrację i logowanie dla różnych typów użytkowników: administratorów, menedżerów oraz konsumentów.
   - Każdy typ użytkownika ma dostęp do odpowiednich funkcji i uprawnień zgodnie z przypisaną rolą.
   - Proces rejestracji jest prosty i intuicyjny, umożliwiając użytkownikom szybkie utworzenie konta.
   - Logowanie odbywa się za pomocą adresu e-mail i hasła, zapewniając bezpieczny dostęp do systemu.

2. **Zarządzanie wydarzeniami:**
   - Menedżerowie mają możliwość tworzenia nowych wydarzeń, podając wszystkie niezbędne informacje, między innymi takie jak nazwa, data, miejsce, opis i cena biletów.
   - Menedżerowie mogą również usuwać wydarzenia, jeśli zajdzie taka potrzeba.
   - Konsumenci mają dostęp do przeglądania listy dostępnych wydarzeń, wraz z szczegółowymi informacjami na ich temat.

3. **Zarządzanie artystami:**
   - Menedżerowie mogą dodawać informacje o artystach, takie jak nazwa, opis oraz rodzaj ich działalności artystycznej.
   - Aplikacja przechowuje kompleksową listę artystów, którą można przeglądać i przeszukiwać.

4. **Zakup biletów:**
   - Konsumenci mogą przeglądać dostępne wydarzenia i wybierać interesujące ich opcje.
   - W takcie zakupów, bilety są rezerwowane na określony czas aby na spokojnie doko
   - Integracja z systemem płatności PayPal zapewnia bezpieczne i wygodne przetwarzanie transakcji.
   - Po zakupie, bilety są przypisywane do konta użytkownika i dostępne do pobrania.

5. **Komentarze i oceny:**
   - Konsumenci mogą wyrazić swoją opinię na temat artystów poprzez dodawanie komentarzy i ocen.
   - Aplikacja udostępnia formularz, za pomocą którego użytkownicy mogą wpisać swój komentarz i wybrać odpowiednią ocenę.
   - Wszystkie komentarze i oceny są widoczne dla innych użytkowników, co pomaga w budowaniu społeczności i dzieleniu się opiniami.

6. **Autentykacja i Autoryzacja:**
   - Aplikacja implementuje mechanizm autentykacji oparty na tokenach bearer.
   - Użytkownicy uwierzytelniają się, podając swoje dane logowania, a w odpowiedzi otrzymują token dostępu.
   - Token dostępu jest używany w kolejnych żądaniach, aby zweryfikować tożsamość użytkownika i autoryzować dostęp do chronionych zasobów.
   - Aplikacja obsługuje również tokeny odświeżania, które umożliwiają uzyskanie nowego tokenu dostępu bez konieczności ponownego logowania, zapewniając płynne korzystanie z systemu.


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

4. **Warstwa mapowania (Mappery):**
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
Ponadto, aplikacja przestrzega zasad SOLID, co przyczynia się do tworzenia czystego i łatwo rozszerzalnego kod

### Jak Zacząć

Aby uruchomić aplikację backendową Obsługi Biletów lokalnie:
1. Sklonuj repozytorium.
2. Zainstaluj wymagane zależności.
3. Skonfiguruj połączenie z MongoDB.
4. Skonfiguruj plik .env podając tam potrzebne dane
5. Uruchom aplikację.
6. Korzystaj z punktów końcowych API za pomocą narzędzi takich jak Postman lub cURL.

### Dokumentacja API

Dokumentacja API jest dostępna pod adresem http://localhost:8080/swagger-ui.html, gdy aplikacja działa lokalnie. Zapewnia ona szczegółowe informacje na temat dostępnych punktów końcowych, formatów żądań/odpowiedzi oraz wymagań dotyczących uwierzytelnienia.

### Autor
Autor: Wojciech Duklas

### Licencja

Aplikacja backendowa Obsługi Biletów jest oprogramowaniem open-source licencjonowanym na zasadach licencji MIT.
