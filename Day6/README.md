# Day 6 – Classes & Objects

`Book` sınıfı (title, author, year, pageCount + `printInfo()` ve `isBestSeller()`) ve ondan 3 nesne oluşturan `Day6` programı: Suç ve Ceza (671 sayfa → bestseller), Küçük Prens ve Kürk Mantolu Madonna.

## Sorular

**1. Sınıf ile nesne farkı nedir?**
Sınıf bir kalıptır, nesne o kalıptan dökülen üründür. Kurabiye kalıbı (sınıf) tektir, onunla istediğin kadar kurabiye (nesne) basarsın. Her kurabiyenin şekli aynıdır ama süslemesi farklı olabilir. `Book` sınıfı hangi alanların olacağını söyler, `book1`, `book2`, `book3` ise kendi değerleri olan gerçek kitaplardır.

**2. `new` ne yapar?**
Bellekte (heap) yeni bir nesne için yer ayırır, alanlara varsayılan değerleri verir (sayılar 0, String'ler null), constructor'ı çalıştırır ve nesnenin adresini döndürür. Bu adres değişkene atanır.

**3. İki `Book` nesnesi aynı alanları paylaşır mı?**
Hayır. Her nesnenin alanları kendine aittir. `book1.title` değişince `book2.title` etkilenmez. Alanlar `static` olsaydı sınıfa ait olurdu ve tüm nesneler aynı değeri paylaşırdı.

**4. `book1` ve `book2` bellekte nerede tutulur?**
`book1` ve `book2` değişkenleri `main` metodunun stack'inde tutulur ve sadece adres içerir. Asıl `Book` nesneleri (title, author vb.) heap'te durur. Stack'teki değişken heap'teki nesneyi işaret eder.

**5. `printInfo()` static olsaydı ne olurdu?**
Derleme hatası verirdi. Static metot belirli bir nesneye ait olmadığı için `title`, `author` gibi nesne alanlarına erişemez. Hangi kitabın başlığını yazacağını bilemez.

**6. `Book` neden ayrı dosyada?**
Java'da `public` bir sınıf, kendi adıyla aynı isimdeki dosyada olmak zorundadır (`Book.java`). Ayrıca her sınıfın kendi dosyasında olması kodu düzenli tutar, sınıf başka programlarda da tekrar kullanılabilir.
