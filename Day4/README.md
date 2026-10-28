# Day 4 – Arrays & Foreach Loop

- **Görev 1:** 10 puanlık `int[]` dizisi, `for-each` ile yazdırma
- **Görev 2:** En yüksek (95), en düşük (45), ortalama (73.20)
- **Görev 3:** `String[]` isim dizisinden 5 karakterden uzun olanlar (Mehmet, Zeynep, Fatmagül)
- **Görev 4:** Kullanıcıdan 5 sayı alıp `Arrays.sort()` ile sıralama

## Sorular

**1. Dizi indeksleri neden 0'dan başlar?**
İndeks, dizinin başlangıç adresinden kaç eleman ileride olduğunu gösterir. İlk eleman başlangıçtan 0 adım uzaktadır. Bu gelenek C dilinden geliyor, Java da aynısını kullanıyor.

**2. `for` ile `for-each` farkı nedir? Sınırlaması var mı?**
`for` indeksle çalışır (`grades[i]`). Hangi elemanda olduğunu bilirsin, istediğin yerden başlayıp tersten gidebilirsin. `for-each` sadece elemanları sırayla verir, daha kısa ve hata riski daha az. Sınırlamaları: indeksi bilemezsin, diziye yeni değer atayamazsın (döngü değişkenini değiştirmek diziyi değiştirmez) ve sadece baştan sona gider.

**3. `ArrayIndexOutOfBoundsException` nedir?**
Dizide olmayan bir indekse erişince fırlatılan hatadır. 10 elemanlı dizide geçerli indeksler 0–9'dur.
```java
int[] grades = new int[10];
System.out.println(grades[10]); // hata: indeks 10 yok
```

**4. `Arrays.sort()` hangi algoritmayı kullanır?**
`int` gibi primitive dizilerde Dual-Pivot Quicksort kullanır. Nesne dizilerinde (`String[]` gibi) TimSort kullanır. TimSort, Merge Sort ile Insertion Sort karışımıdır ve stabildir (eşit elemanların sırasını korur).

**5. `grades.length` ile `grades.length - 1` farkı ne zaman önemli?**
`length` eleman sayısıdır (10). Son elemanın indeksi ise `length - 1`'dir (9). Son elemana erişirken `grades[grades.length - 1]` yazmak gerekir. Döngüde `i <= grades.length` yazarsam son turda hata alırım, doğrusu `i < grades.length`.

**6. Dizi ile `ArrayList` farkı nedir?**
Dizinin boyutu oluşturulurken sabitlenir, sonradan değişmez. `ArrayList` ise eleman eklendikçe büyür, silindikçe küçülür ve `add`, `remove`, `contains` gibi hazır metotları vardır. `ArrayList` primitive tutamaz (`int` yerine `Integer` kullanılır). Diziler biraz daha hafif ve hızlıdır.
