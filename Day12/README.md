# Day 12 – Collections: List & ArrayList

`Product` sınıfı (name, price, stock + `toString()`) ve 3 görevli `Day12`: String ürün listesinde ekleme/silme/güncelleme, Integer fiyat listesinde toplam/min/max ve sıralama, `Product` listesinde stokta olmayan ürünleri bulma.

Ek olarak `add(1, "Kiraz")` ile araya ekleme, `Collections.reverse()` ve `shuffle()` denedim. `List<Integer>`'da `remove(0)` ile `remove(Integer.valueOf(80))` farkını gösterdim ve son görevde toplam stok değerini (fiyat × stok) hesapladım.

## Sorular

**1. `ArrayList` ile `int[]` arasındaki 3 fark nedir?**
1. Dizinin boyutu baştan sabit, `ArrayList` eleman ekledikçe büyüyor.
2. Dizi `int` gibi primitive tutabiliyor, `ArrayList` sadece nesne tutuyor (`Integer`).
3. `ArrayList`'te `add`, `remove`, `contains`, `size()` gibi hazır metotlar var; dizide bunları elle yazmam gerekiyor.

**2. Neden `ArrayList` yerine `List` referansı kullanıyoruz?**
Kodum `ArrayList`'e değil `List` sözleşmesine bağlı oluyor. İleride `LinkedList`'e geçmek istersem sadece `new` kısmını değiştiriyorum. Buna "programming to interface" deniyor.

**3. `add(index, element)` ile `add(element)` farkı?**
`add(element)` sona ekliyor. `add(index, element)` verilen konuma ekliyor ve oradaki elemanları bir sağa kaydırıyor. `products.add(1, "Kiraz")` ile Kiraz ikinci sıraya girdi.

**4. `remove(0)` ile `remove("Elma")` aynı mı?**
Değil. `remove(0)` 0. indeksteki elemanı siliyor. `remove("Elma")` değeri "Elma" olan ilk elemanı siliyor. `List<Integer>`'da bu tuzak oluyor: `prices.remove(80)` 80. indeksi silmeye çalışır ve hata verir. Değeri silmek için `prices.remove(Integer.valueOf(80))` yazdım.

**5. `toString()`'i neden ekledik?**
`System.out.println(p)` yazınca Java otomatik `toString()` çağırıyor. Override etmezsem `Product@1b6d3586` gibi anlamsız bir şey çıkıyor. Override edince `[Laptop | 15000.00 TL | Stok: 5]` görünüyor.

**6. `Collections.sort()` için ne gerekir?**
Elemanların nasıl karşılaştırılacağının bilinmesi gerekiyor. Ya sınıf `Comparable`'ı uygulayıp `compareTo()` yazmalı (String ve Integer zaten uyguluyor), ya da ayrı bir `Comparator` verilmeli. `Product` `Comparable` olmadığı için `Collections.sort(catalog)` derlenmiyor; fiyata göre sıralamak için `catalog.sort(Comparator.comparingDouble(Product::getPrice))` yazılabilir.
