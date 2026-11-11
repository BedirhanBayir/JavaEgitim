# Day 17 – @PathVariable & @RequestParam

Day 16'daki `ProductController`'a dinamik istekler ekledim. `Product` aynı kaldı.

| Endpoint | Ne yapar |
|---|---|
| `GET /api/products/{id}` | ID'ye göre ürün; yoksa `{"error": "Ürün bulunamadı. ID: 99"}` |
| `GET /api/products/search?name=lap` | İsminde aranan metin geçen ürünler (büyük/küçük harf fark etmez) |
| `GET /api/products/filter?category=Elektronik` | Kategoriye göre filtre |
| `GET /api/products/filter?category=Elektronik&maxPrice=1000` | Kategori + en yüksek fiyat |

Programdakine ek olarak filtreye opsiyonel bir `minPrice` da koydum; `?category=Elektronik&minPrice=500&maxPrice=2000` gibi fiyat aralığı verilebiliyor.

## Test (Postman)
- `GET http://localhost:8080/api/products/2` → Fare
- `GET http://localhost:8080/api/products/99` → hata mesajı
- `GET http://localhost:8080/api/products/search?name=LAP` → Laptop
- `GET http://localhost:8080/api/products/filter?category=elektronik&maxPrice=1000` → Fare

## Sorular

**1. `@PathVariable` ile `@RequestParam` farkı nedir? URL yapısı nasıl farklılaşıyor?**
`@PathVariable` değeri URL'nin yolundan alıyor: `/api/products/3` → `id = 3`. Değer adresin bir parçası. `@RequestParam` değeri `?`'den sonraki sorgu kısmından alıyor: `/api/products/search?name=lap` → `name = "lap"`. Birden fazla parametre `&` ile ekleniyor. Path belirli bir kaynağı gösteriyor, query parametreleri ise bir listeyi daraltıyor ya da ayarlıyor.

**2. `/api/products/{id}` ile `/api/products?id=1` farkı nedir, hangisini tercih edersin?**
İkisi de çalışır ama `/api/products/1` tercih ederim. REST'te tek bir kaynağın kendi adresi olur: "1 numaralı ürün" bir kaynak, adresi `/products/1`. `?id=1` ise "ürün listesini id'ye göre filtrele" anlamına geliyor ve sonuç tek ürün değil bir liste gibi düşünülür. Kural: belirli bir kaynak → path, arama/filtreleme/sıralama → query parametresi.

**3. `required = false` ne demek? Parametre gönderilmezse ne olur?**
O parametrenin zorunlu olmadığı anlamına geliyor. Gönderilmezse değeri `null` oluyor. Bu yüzden tipini `double` değil `Double` yaptım; primitive `double` `null` olamaz. Kodda `maxPrice == null` ise fiyat kontrolünü atlıyorum. `required = false` yazmasaydım ve parametre gelmeseydi Spring **400 Bad Request** döndürürdü. Alternatif olarak `defaultValue = "100000"` verilebilir; o zaman gönderilmeyince bu değer kullanılıyor.

**4. Postman'de query parametresi nasıl eklenir?**
İki yolu var: adrese doğrudan `?category=Elektronik&maxPrice=1000` yazmak, ya da adres çubuğunun altındaki **Params** sekmesinde Key/Value olarak `category` = `Elektronik`, `maxPrice` = `1000` girmek. Params'a yazınca Postman adresi kendisi güncelliyor. Bir satırın tikini kaldırınca o parametre gönderilmiyor, `required = false` denemek için pratik oldu.

**5. Büyük/küçük harf duyarsız arama için `toLowerCase()` kullandık. Alternatif var mı?**
Var:
- Tam eşleşmede `equalsIgnoreCase()`: kategori filtresinde bunu kullandım.
- "İçeriyor mu" için `Pattern.compile(Pattern.quote(name), Pattern.CASE_INSENSITIVE).matcher(p.getName()).find()`.
- Veritabanına geçince Spring Data JPA'da `findByNameContainingIgnoreCase(String name)` gibi bir metot ya da SQL'de `LOWER(name) LIKE ...`.
Not: Türkçe'de `toLowerCase()` cihazın diline göre farklı davranabiliyor (I/ı, İ/i), bu yüzden `toLowerCase(Locale.ROOT)` ya da `new Locale("tr")` vermek daha güvenli olabilir.

**6. Bulunamayan ID için neden `Map.of("error", ...)` döndük? Daha iyi bir yol var mı?**
Metodun dönüş tipi `Object` olduğu için ya `Product` ya da bir hata `Map`'i döndürebildim; JSON'da `{"error": "..."}` olarak görünüyor. Ama sorun şu: durum kodu yine **200 OK**. İstemci isteğin başarılı olduğunu sanıyor ve gövdeyi okumadan hatayı anlayamıyor. Daha iyi yol `ResponseEntity` ile **404 Not Found** döndürmek: `ResponseEntity.notFound().build()`. Ayrıca dönüş tipi `Object` yerine `ResponseEntity<Product>` olunca metodun ne döndürdüğü de belli oluyor. Bunu yarın (Day 18) yapacağım.

## Notlar
- `/{id}` metodunu en alta koydum. Spring sabit yolları (`/search`, `/filter`, `/count`) değişkenli yoldan önce eşleştiriyor, yani `/search` `id` sanılmıyor. Ama `/api/products/abc` gibi sayı olmayan bir değer gelirse `Long`'a çevrilemediği için 400 dönüyor.
