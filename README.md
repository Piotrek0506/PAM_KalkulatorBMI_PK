# Kalkulator BMI z Historią 

Projekt zaliczeniowy z przedmiotu **Programowanie Aplikacji Mobilnych**.
Aplikacja służy do obliczania wskaźnika BMI, interpretacji wyniku oraz archiwizowania pomiarów w lokalnej bazie danych.

## Funkcjonalności

* **Obliczanie BMI:** Precyzyjne wyliczanie wskaźnika na podstawie wagi i wzrostu.
* **Interpretacja wyników:** Automatyczne przypisywanie kategorii (np. Niedowaga, Waga prawidłowa, Otyłość) wraz z kolorystycznym oznaczeniem.
* **Historia pomiarów:** Zapisywanie wyników w trwałej bazie danych (data, waga, wzrost, wynik).
* **Wielojęzyczność:** Pełne wsparcie dla języka **Polskiego** i **Angielskiego**.
* **Nowoczesny UI:** Interfejs oparty na Material Design 3.

---

## Właściwości aplikacji

### Użyte własności
- [x] **Język Kotlin i Jetpack Compose:** Cały interfejs zbudowany deklaratywnie przy użyciu Composable.
- [x] **Plik strings.xml:** Wszystkie teksty wydzielone do zasobów.
- [x] **Wersje językowe:** Obsługa języka PL i EN (katalogi `values` i `values-pl`).
- [x] **Layouty:** Wykorzystanie kontenerów `Column` i `Row` do budowy interfejsu.
- [x] **Toast:** Wyświetlanie komunikatu z wynikiem i kategorią po obliczeniu.
- [x] **Modifier:** Stylowanie komponentów (paddingi, wyrównania, szerokości).
- [x] **Komponenty GUI:** Pola `OutlinedTextField` do wprowadzania danych liczbowych.
- [x] **Obsługa stanu:** Wykorzystanie `remember` i `mutableStateOf` do przechowywania wpisanych wartości.
- [x] **Logika biznesowa:** Algorytm obliczania BMI oraz funkcja `analyzeBmi` określająca kategorię zdrowotną.
- [x] **Nawigacja:** Zastosowanie `Navigation Compose` (NavHost, NavController) do przełączania między ekranem Kalkulatora a Historią.
- [x] **Baza Danych:** Implementacja **Room Database** (Entity, DAO) do zapisu historii pomiarów.

---

## Technologie

* **Język:** Kotlin
* **UI:** Jetpack Compose (Material3)
* **Architektura:** MVVM (uproszczona)
* **Baza danych:** Room Persistence Library
* **Nawigacja:** Jetpack Navigation Compose

---

## Zrzuty ekranu

<img width="414" height="876" alt="image" src="https://github.com/user-attachments/assets/c2d60266-88af-4cb6-a66e-de87d62054f2" />

<img width="414" height="874" alt="image" src="https://github.com/user-attachments/assets/fa682c79-11bd-4d33-a17b-79ce1a4d57a1" />

---

## Jak uruchomić?

1. Sklonuj repozytorium lub pobierz pliki.
2. Otwórz projekt w **Android Studio**.
3. Poczekaj na synchronizację plików Gradle.
4. Uruchom aplikację na emulatorze lub fizycznym urządzeniu z systemem Android.

---

**Autor:** Piotr Kula
