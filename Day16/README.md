# Day 16 – İlk REST API: @RestController & @GetMapping

Ürün yönetimi API'sinin ilk hali. Veritabanı henüz olmadığı için 5 ürün `ProductController` içinde sabit bir listede duruyor. `Product` modeli id, name, price ve category alanlarından oluşuyor. Üç endpoint var:

| Endpoint | Ne döndürür |
|---|---|
| `GET /api/products` | Tüm ürünler (JSON liste) |
| `GET /api/products/count` | Ürün sayısı (`5`) |
| `GET /api/products/categories` | Tekrarsız kategoriler (`["Elektronik","Kırtasiye"]`) |

Kategorilerde `HashSet` yerine `TreeSet` kullandım. Hem tekrarsız hem alfabetik sıralı geliyor, böylece cevap her seferinde aynı sırada.

## Test
Uygulamayı çalıştırıp Postman'de her endpoint'e GET isteği attım. Tarayıcıdan da http://localhost:8080/api/products açılabiliyor.

## Sorular

**1. `@RestController` ile `@Controller` farkı nedir?**
`@Controller` metodun döndürdüğü String'i bir sayfa (view) adı sanıyor ve o HTML sayfasını arıyor. `@RestController` ise `@Controller` + `@ResponseBody`: döndürülen değer doğrudan cevabın gövdesine yazılıyor, nesneyse JSON'a çevriliyor. API yazarken `@RestController` kullanılıyor, sunucu tarafında HTML üretirken `@Controller`.

**2. `@RequestMapping("/api/products")` ne işe yarar?**
Sınıf seviyesinde ortak bir yol (prefix) tanımlıyor. İçindeki `@GetMapping("/count")` bu yolun devamı oluyor, yani tam adres `/api/products/count`. Her metotta `/api/products` yazmama gerek kalmıyor; yolu değiştirmek istersem tek yerden değiştiriyorum.

**3. Spring Boot `Product`'ı nasıl JSON'a çeviriyor? (Jackson)**
`spring-boot-starter-web` içinde Jackson kütüphanesi geliyor. Controller bir nesne döndürünce Spring bunu Jackson'a veriyor. Jackson sınıfın getter'larına bakıyor: `getName()` → `"name"`, `getPrice()` → `"price"`. Bunlardan JSON oluşturuyor. Getter yazmasaydım alanlar private olduğu için JSON'da görünmezdi. Liste döndürünce JSON dizi (`[...]`) oluyor.

**4. Postman'de GET isteği nasıl atılır, hangi bilgileri girdin?**
Yeni bir request açtım, metodu **GET** seçtim, adrese `http://localhost:8080/api/products` yazıp **Send**'e bastım. Alt tarafta cevap JSON olarak ve **200 OK** durum koduyla geldi. Aynısını `/count` ve `/categories` için tekrarladım. GET'te body ya da header girmem gerekmedi.

**5. Neden `List.of(...)` ile `new ArrayList<>(...)` birlikte kullanıldı?**
`List.of(...)` listeyi tek satırda oluşturmak için pratik ama değiştirilemez (immutable) bir liste döndürüyor; `add()` ya da `remove()` çağırınca `UnsupportedOperationException` fırlatıyor. Onu `new ArrayList<>(...)` içine verince elemanlar değiştirilebilir bir listeye kopyalanıyor. İleride POST ve DELETE ile listeye ürün ekleyip sileceğim için değiştirilebilir olması gerekiyor.

**6. API tasarımında neden `/api/products` öneki kullanılıyor?**
- `/api` kısmı bu adreslerin JSON dönen API olduğunu, web sayfası olmadığını belli ediyor. Aynı uygulama ileride HTML sayfa da sunarsa yollar karışmıyor.
- `/products` kaynağın adı. REST'te URL bir eylemi değil kaynağı anlatıyor: `/getProducts` değil `/products`; eylemi HTTP metodu (GET, POST...) söylüyor.
- Çoğul isim kullanmak koleksiyonu anlatıyor; tek bir ürün için `/products/1` gibi devam ediyor.

## Notlar
- Liste bellekte olduğu için sunucu kapanınca her şey baştaki haline dönüyor.
- `/count` sade bir sayı döndürüyor; JSON'da da bu `5` olarak görünüyor.
