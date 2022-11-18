# Problem 2
## Zbiór Mandelbrota - Epizod II

---
### Opis problemu

Celem ćwiczenia jest wykorzystanie napisanego w poprzednim odcinku kodu do generowania zbioru Mandelbrota
i przerobienie go na wersję wielowątkową.

Należy to zrobić na dwa sposoby:

- Pierwszy program ma tworzyć tyle wątków, ile jest rdzeni w maszynie. Obrazek do wygenerowania
należy podzielić na tyle bloków, ile tworzymy wątków, a każdy wątek ma przeliczać jeden blok.
- Drugi program ma wykorzystywać pulę wątków (ma w niej być tyle wątków, ile rdzeni jest w maszynie).
Do puli należy wrzucać joby polegające na przeliczeniu części obrazka (szczegóły poniżej). 

Program ma zapisać plik zawierający średni czas generacji obrazka w funkcji liczby pikseli obrazka.
Czas ten należy wyznaczyć dla obrazków o bokach 16, 32, 64, 128, 256, 512, 1024, 2048, 4096 i 8192 piksele
(tu pamiętajcie o tym, żeby wyniki odpowiednio uśrednić). Należy też narysować wykres (nie w Java
i nie w ROOT). Przetestujcie oba powyższe podejścia, przy czym w przypadku pierwszego (czyste wątki)
czas tworzenia wątków ma być uwzględniony w czasie wykonania programu (funkcja generująca obrazek
przy każdym wywołaniu tworzy wątki i jest to uwzględniane w uśrednianiu). Natomiast testując drugie
podejście, pulę wątków, przetestujcie dwie opcje. Na początku twórzcie pulę tylko raz i podczas generowania obrazków
(niezbędnego do stworzenia wykresów i porównania czasów) wykorzystujecie ciągle tę samą pulę, której czas tworzenia
nie jest uwzględniany podczas uśredniania. Potem powtórzcie testy, ale tym razem tworząc podczas
uśredniania nową pulę za każdym razem i wliczajcie jej czas tworzenia do średniej.

Zbadajcie do tego, jak w przypadku puli będzie zmieniał się czas wykonania w zależności od tego, jak
podzielicie obrazek na joby do wykonania. Sprawdźcie, jak będzie wyglądał ten czas (za każdym razem w
funkcji wielkości obrazka i nanieście te krzywe na wykres) przy podziale obrazka na liczbę bloków
równą liczbie rdzeni w maszynie, oraz na liczbę bloków większą/mniejszą (dla bloków składających się z
max 4, 8, 16, 32, 64, 128 pikseli).

