# Day 25 – Validasyonlar (@NotBlank, @NotNull, @Min, @Size)

Day 24'teki projeye Bean Validation ekledim. Ürün ekleme ve güncelleme artık `Product` entity'sini değil `ProductCreateRequest` DTO'sunu alıyor; kurallar DTO'nun alanlarında annotation olarak duruyor. `@Valid` sayesinde kural bozulursa controller metodu hiç çalışmıyor ve istemci hangi alanın neden hatalı olduğunu söyleyen bir **400** alıyor.

```
pom.xml                              ← spring-boot-starter-validation
dto/ProductCreateRequest.java        ← YENİ: name, price, category + kurallar
controller/ProductController.java    ← POST/PUT'ta @Valid, @ExceptionHandler (geçici)
service/ProductService.java          ← isValid() silindi
```

| Alan | Kural | Mesaj |
|---|---|---|
| `name` | `@NotBlank`, `@Size(min = 2, max = 100)` | "Ürün adı boş olamaz." / "Ürün adı 2-100 karakter olmalıdır." |
| `price` | `@NotNull`, `@Min(0)` | "Fiyat zorunludur." / "Fiyat negatif olamaz." |
| `category` | `@NotBlank` | "Kategori boş olamaz." |

Programdakine ek olarak:
- **PUT de `@Valid` kullanıyor.** Day 18'deki `ProductService.isValid()` artık gerekmediği için sildim. PUT da aynı DTO ve aynı kurallarla kontrol ediliyor. Böylece "geçerli ürün nedir" kuralı tek yerde duruyor.
- **DTO'ya setter'lar ve boş constructor ekledim.** Programdaki DTO'da sadece getter var. Jackson getter'ı olan private alanı yine de doldurabiliyor, ama Day 20'deki `UserCreateRequest` gibi açıkça yazmak daha anlaşılır.
- **Hata map'inde `put` yerine `merge` kullandım.** Boş isim (`""`) hem `@NotBlank` hem `@Size(min = 2)` kuralını bozuyor. `put` ile ikinci mesaj birincinin üzerine yazılıyordu ve hangisinin kalacağı belli değildi (validator kuralları sabit bir sırayla kontrol etmiyor). `merge` ile iki mesaj birleştiriliyor.
- **`HashMap` yerine `LinkedHashMap` kullandım,** alanlar hatanın geldiği sırayla dönüyor.
- `UserController` Day 20'deki elle yazılmış kontrollerle (`isValid`) kaldı. Bugünkü görev ürün içindi.

## Test Senaryoları

İstekler `Day25.postman_collection.json` dosyasında.

| Test | Gönderilen JSON | Sonuç |
|---|---|---|
| Geçerli | `{"name":"Monitör","price":8500,"category":"Elektronik"}` | **201** + ürün |
| Boş name | `{"name":"","price":8500,"category":"Elektronik"}` | **400** `{"name": "Ürün adı boş olamaz. Ürün adı 2-100 karakter olmalıdır."}` (iki mesajın sırası değişebilir) |
| Negatif fiyat | `{"name":"Test","price":-100,"category":"Test"}` | **400** `{"price": "Fiyat negatif olamaz."}` |
| Eksik kategori | `{"name":"Test","price":100}` | **400** `{"category": "Kategori boş olamaz."}` |
| Tek harfli isim (ek) | `{"name":"A", ...}` | **400** `{"name": "Ürün adı 2-100 karakter olmalıdır."}` |
| Sadece boşluk (ek) | `{"name":"   ", ...}` | **400** `{"name": "Ürün adı boş olamaz."}` |
| Fiyat yok (ek) | `{"name":"Test","category":"Test"}` | **400** `{"price": "Fiyat zorunludur."}` |
| Fiyat 0 (ek) | `{"name":"Bedava","price":0, ...}` | **201** (`@Min(0)` 0'ı kabul ediyor) |
| Boş body (ek) | `{}` | **400**, üç alanın hepsi için hata |

## Sorular

**1. `@NotBlank` ile `@NotNull` arasındaki fark nedir?**
- `@NotNull`: sadece `null` olmasın. `""` ve `"   "` geçiyor. Her tipte kullanılabiliyor (`Double`, `Long`, nesneler).
- `@NotBlank`: `null` olmasın **ve** boşluklar atıldıktan sonra en az bir karakter kalsın. `null`, `""` ve `"   "`'ün üçünü de reddediyor. Sadece `String` (CharSequence) için.
- Arada bir de `@NotEmpty` var: `null` ve `""`'i reddediyor ama `"   "`'ü kabul ediyor. String'in yanında liste ve map'lerde de kullanılabiliyor.

Bu yüzden isim ve kategoride `@NotBlank`, fiyatta (sayı olduğu için) `@NotNull` kullandım.

**2. `@Valid` olmadan validasyon anotasyonları çalışır mı? Dene ve açıkla.**
Çalışmıyor. `@NotBlank`, `@Min` gibi anotasyonlar tek başına sadece "bu alanın kuralı bu" diyen bir etiket; kendileri hiçbir şeyi kontrol etmiyor. Kontrolü Spring yapıyor, o da sadece `@Valid` (veya `@Validated`) gördüğü parametrede validator'ı çalıştırıyor. `@Valid`'i silince:
- `{"name":"Test","price":-100,"category":"Test"}` → **201**, negatif fiyatlı ürün veritabanına kaydediliyor (artık `isValid()` de olmadığı için hiçbir engel yok).
- `{"name":"Test","category":"Test"}` (fiyat yok) → `toProduct()` içinde `null` `Double`'ı `double`'a çevirmeye çalışırken `NullPointerException` fırlıyor → **500**.

**3. `MethodArgumentNotValidException` ne zaman fırlatılır?**
Spring, `@Valid @RequestBody` parametresi için gelen JSON'ı Jackson ile `ProductCreateRequest` nesnesine çeviriyor, sonra validator'ı çalıştırıyor. En az bir kural bozulursa controller metodu **hiç çağrılmadan** `MethodArgumentNotValidException` fırlatılıyor. Bu exception'ın içindeki `BindingResult` bozulan her kural için bir `FieldError` tutuyor: alan adı (`getField()`), mesaj (`getDefaultMessage()`), reddedilen değer (`getRejectedValue()`). Benim `@ExceptionHandler`'ım bunları `alan → mesaj` map'ine çeviriyor. İki benzer durum farklı exception veriyor:
- JSON bozuksa ya da `"price": "abc"` gibi yanlış tipte bir değer gelirse, nesneye çevirme aşamasında `HttpMessageNotReadableException` fırlıyor (Spring bunun için de kendiliğinden 400 dönüyor).
- `@PathVariable` / `@RequestParam` üzerine doğrudan `@Min(1)` gibi bir kural yazılır ve bozulursa `HandlerMethodValidationException` fırlıyor (Spring 6.1+).

**4. Validasyon hatası için neden `400 Bad Request` döndürüldü?**
4xx kodları "hata istemcide" demek, 5xx kodları "hata sunucuda" demek. Boş isim ya da negatif fiyat göndermek istemcinin hatası; aynı isteği tekrar göndermek hiçbir şeyi değiştirmez, istemcinin isteği düzeltmesi gerekiyor. 400 tam olarak bunu söylüyor: "isteğin geçersiz, düzelt ve tekrar gönder". Hangi alanı nasıl düzelteceği de gövdedeki mesajlarda. 500 dönseydim istemci sunucunun çöktüğünü sanardı. (Bazı API'ler "JSON doğru ama kurala uymuyor" durumu için `422 Unprocessable Entity` kullanıyor; programda ve yaygın kullanımda 400.)

**5. `@Size` ile `@Length` arasındaki fark nedir? (Araştır)**
- `@Size` (`jakarta.validation.constraints`) standart Bean Validation anotasyonu. String'in yanında `List`, `Set`, `Map` ve dizilerde de çalışıyor (eleman sayısını kontrol ediyor).
- `@Length` (`org.hibernate.validator.constraints`) Hibernate Validator'a özel ve sadece String için. String'lerde yaptığı iş `@Size` ile aynı.

Standart olduğu ve her yerde çalıştığı için `@Size` tercih ediliyor. İkisinde de `null` geçerli sayılıyor; `null`'ı yakalamak için yanına `@NotBlank`/`@NotNull` yazmak gerekiyor. Bu da `@Column(length = 100)` ile aynı şey değil: `@Column` veritabanındaki sütunun boyutunu belirliyor, istek veritabanına gelmeden kontrol etmiyor.

**6. Validasyonları Entity'de mi yoksa DTO'da mı tanımlamak daha doğru? Neden?**
İstekten gelen veri için **DTO'da**:
- Kurallar isteğe özel olabiliyor. Örneğin kayıt olurken şifre zorunlu, profil güncellerken zorunlu değil. Tek entity'de bu iki kuralı bir arada yazamam, ama iki ayrı DTO'ya yazabilirim.
- Hatalı veri, entity oluşturulmadan ve veritabanına hiç gitmeden controller'ın girişinde reddediliyor.
- Entity'de `id` gibi istemcinin hiç göndermemesi gereken alanlar da var. DTO'da sadece istemcinin göndereceği alanlar var.

Entity'de de anotasyonlar faydalı olabiliyor: JPA kaydetmeden önce onları da kontrol ediyor ve "veritabanına asla kötü veri gitmesin" diye son bir savunma oluyor. Ama oradaki hata `ConstraintViolationException` olarak veritabanı aşamasında, yani çok geç geliyor. Asıl kontrol DTO'da olmalı.

## Notlar
- `@Valid` → `jakarta.validation.Valid`. Validasyon anotasyonları da `jakarta.validation.constraints` paketinden geliyor (Spring Boot 3).
- Fiyat `Double` (wrapper) olmalı, `double` değil: primitive `double` fiyat gönderilmezse 0.0 olur ve `@NotNull` bunu hiç yakalayamaz.
- `@Min` tam sayı (`long`) alıyor, `-0.5` gibi ondalık negatifler de reddediliyor. Ondalıklı sınırlar için `@DecimalMin("0.01")` gibi bir anotasyon da var.
- Bu `@ExceptionHandler` sadece `ProductController` içindeki hataları yakalıyor. Day 26'da tüm controller'lar için ortak hale getirilecek.
