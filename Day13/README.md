# Day 13 – Collections: Set & Map

Tek dosyalık `Day13`: `HashSet` ile tekrarsız ürün kategorileri, `HashMap<String, Integer>` ile sepet (ürün → adet) ve `HashMap<String, Double>` ile fiyat kataloğu (ürün → fiyat). Sepette ekleme, güncelleme, silme yapıp en çok adetli ürünü buluyor.

Aynı kategoriyi ikinci kez eklerken `add()`'in `false` döndüğünü yazdırdım. Kategorileri bir de `TreeSet` ile alfabetik sıraladım, sepet güncellemesinde `getOrDefault()`'un yanında `merge()`'ü de denedim. Sonda sepeti katalogla birleştirip toplam tutarı hesapladım.

## Sorular

**1. `List`, `Set` ve `Map` farkları nedir, hangisi nerede kullanılır?**
`List` sıralı ve tekrara izin veriyor, indeksle erişiliyor (sipariş geçmişi). `Set` tekrarsız (kategoriler, benzersiz e-postalar). `Map` anahtar-değer çiftleri tutuyor, key tekrarsız (sepet: ürün → adet). Tekrar olmamalıysa `Set`, sıra önemliyse `List`, bir şeyi anahtarla hızlı bulmam gerekiyorsa `Map`.

**2. `HashSet`'e aynı elemanı iki kez ekleyince ne oldu?**
İkinci "Elektronik" eklenmedi, `add()` `false` döndü ve boyut 3 kaldı. `HashSet` eklemeden önce `hashCode()` ve `equals()` ile elemanın zaten olup olmadığına bakıyor.

**3. `HashMap`'te sıra garanti mi?**
Hayır. Elemanlar hash değerine göre yerleşiyor, yazdırma sırası ekleme sırasından farklı olabiliyor. Ekleme sırası için `LinkedHashMap`, key'e göre sıralı tutmak için `TreeMap` kullanılıyor.

**4. `getOrDefault()` ne işe yarar?**
Key varsa değerini, yoksa verdiğim varsayılanı döndürüyor. `get()` olmayan key için `null` döndürür, onu `int`'e çevirmeye çalışınca `NullPointerException` alırım. Sayaç güncellerken çok işe yarıyor: `cart.getOrDefault("Kablo", 0) + 1`.

**5. `entrySet()`, `keySet()` ve `values()` farkı?**
`keySet()` sadece key'leri, `values()` sadece değerleri, `entrySet()` key-value çiftlerini veriyor. İkisine de ihtiyacım varsa `entrySet()` en iyisi, çünkü `keySet()` ile dönüp her seferinde `get()` çağırmama gerek kalmıyor.

**6. `HashMap`'te key olarak neden her nesne kullanılabiliyor?**
Her nesne `Object`'ten `hashCode()` ve `equals()` miras alıyor. `HashMap` key'in `hashCode()`'u ile hangi kovaya gideceğini buluyor, `equals()` ile doğru key'i seçiyor. Kendi sınıfımı key yapacaksam bu ikisini birlikte override etmeliyim; yoksa içeriği aynı iki nesne farklı key sayılır. Key map'e girdikten sonra da değişmemeli, o yüzden String ve Integer gibi değişmeyen tipler key için ideal.
