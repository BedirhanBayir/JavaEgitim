# Day 26 – Global Exception Handling (@ControllerAdvice)

Day 25'te validasyon hatalarını `ProductController`'ın içinde yakalıyordum; `UserController` için aynısını tekrar yazmam gerekecekti. Bugün bütün hata yönetimini tek bir `GlobalExceptionHandler` sınıfına topladım. Service'ler artık `Optional` döndürmek yerine kayıt yoksa `ResourceNotFoundException` fırlatıyor. Controller'lardaki `.map(ResponseEntity::ok).orElse(notFound())` satırları kalktı.

```
exception/ResourceNotFoundException.java   ← YENİ: "Product 1 bulunamadı."
exception/GlobalExceptionHandler.java      ← YENİ: @ControllerAdvice
dto/ErrorResponse.java                     ← YENİ: timestamp, status, message, path
service/ProductService.java                ← getById/update/delete exception fırlatıyor
service/UserService.java                   ← getById exception fırlatıyor ("User 5 bulunamadı.")
controller/ProductController.java          ← @ExceptionHandler silindi, if/else'ler gitti
controller/UserController.java             ← getById sadeleşti
```

| Exception | Durum | Gövde |
|---|---|---|
| `ResourceNotFoundException` | **404** | `ErrorResponse` |
| `MethodArgumentNotValidException` | **400** | `{"alan": "mesaj"}` map'i |
| `HttpMessageNotReadableException` (ek) | **400** | `ErrorResponse` – bozuk JSON / yanlış tip |
| `MethodArgumentTypeMismatchException` (ek) | **400** | `ErrorResponse` – `/api/products/abc` |
| Spring'in kendi HTTP hataları (ek) | kendi kodu (404, 405, 400) | `ErrorResponse` |
| Diğer her `Exception` | **500** | `ErrorResponse`, genel mesaj |

Programdakine ek olarak:
- **`Exception.class` handler'ı Spring'in kendi hatalarını bozmasın diye** iki handler ve bir kontrol ekledim. Spring normalde bozuk JSON'ı, `/api/products/abc`'yi, olmayan adresi ve yanlış HTTP metodunu kendisi 400/404/405'e çeviriyor. Ama `Exception.class` için genel handler yazınca bu hataları da o yakalıyor ve hepsi **500** oluyordu. Bozuk JSON ve yanlış tip için ayrı handler yazdım. Genel handler'da da exception Spring'in `ErrorResponse` arayüzünü uyguluyorsa (`NoResourceFoundException`, `HttpRequestMethodNotSupportedException`, `MissingServletRequestParameterException` ...) onun durum kodunu kullanıyorum.
- **500 cevabına `ex.getMessage()` koymadım.** Programın kodunda `"Sunucu hatası: " + ex.getMessage()` var, ama aynı sayfadaki senior notu bunu yapmamamı söylüyor: exception mesajında SQL, tablo adı, sınıf adı gibi iç detaylar olabiliyor. İstemciye sadece `"Beklenmeyen bir sunucu hatası oluştu."` gidiyor; asıl hata ve stack trace `log.error(...)` ile konsola yazılıyor.
- **`update()` ve `delete()` de exception fırlatıyor,** sadece `getById()` değil. Böylece üç endpoint de aynı 404 cevabını veriyor.
- **`UserService.getById()` da aynı exception'ı kullanıyor.** `GET /api/users/99` → `"User 99 bulunamadı."`. Global handler'ın gerçekten her controller'a uygulandığını gösteriyor.
- İstekler `Day26.postman_collection.json` dosyasında.

## Sorular

**1. `@ControllerAdvice` ile `@ExceptionHandler` beraber nasıl çalışır?**
`@ExceptionHandler(X.class)`, "X tipinde bir exception fırlarsa bu metodu çalıştır" diyor. Tek başına bir controller'ın içine yazılınca sadece o controller'da geçerli (Day 25'teki gibi). `@ControllerAdvice` ise sınıfı bütün controller'lar için ortak bir "yardımcı" yapıyor; içindeki `@ExceptionHandler`'lar her controller'dan fırlayan exception'lar için çalışıyor.
Akış şöyle: `ProductService.getById(99)` `ResourceNotFoundException` fırlatıyor → exception controller'dan da dışarı çıkıyor → Spring'in `DispatcherServlet`'i yakalıyor → bu exception tipine uyan handler'ı arıyor (önce controller'ın kendi içinde, sonra `@ControllerAdvice` sınıflarında) → `handleNotFound()` çalışıyor ve döndürdüğü `ResponseEntity` istemciye gidiyor. Birden fazla handler uyarsa en **özel** olan seçiliyor: `ResourceNotFoundException` hem kendi handler'ına hem `Exception.class`'a uyuyor, ama kendi tipine daha yakın olduğu için `handleNotFound()` seçiliyor.
Programdaki gibi `@ControllerAdvice` + `ResponseEntity` kullandım. `@RestControllerAdvice` = `@ControllerAdvice` + `@ResponseBody`; `ResponseEntity` döndürünce ikisi aynı sonucu veriyor.

**2. Neden her controller'da ayrı hata yönetimi yazmak kötüdür?**
- **Kod tekrarı:** Day 25'teki validasyon handler'ını `UserController`, ileride `CategoryController` için tekrar kopyalamam gerekirdi. Bir değişiklikte (mesaj formatı, yeni bir alan) hepsini tek tek düzeltmem gerekirdi.
- **Tutarsızlık:** Bir yerde 404 gövdesi boş, bir yerde `{"error": ...}`, başka yerde düz metin olurdu. API'yi kullanan istemci her endpoint için ayrı hata okuma kodu yazmak zorunda kalırdı. Şimdi bütün hatalar aynı `timestamp/status/message/path` şeklinde.
- **Controller'ın işi karışıyor:** Controller'ın görevi isteği alıp service'e iletmek. Day 25'e kadar her metotta "bulunamazsa 404" if/else'i vardı. Şimdi `getById()` tek satır: `return productService.getById(id);`.

**3. `ResourceNotFoundException` neden `RuntimeException` extend ediyor?**
Java'da iki tür exception var:
- **Checked** (`Exception`'dan türeyip `RuntimeException`'dan türemeyenler): derleyici yakalanmasını ya da her metoda `throws` yazılmasını zorunlu kılıyor. `ResourceNotFoundException` checked olsaydı `ProductRepository` hariç her katmanda, `getById()`, `update()`, `ProductController.getById()`... hepsine `throws ResourceNotFoundException` yazmam gerekirdi.
- **Unchecked** (`RuntimeException`): böyle bir zorunluluk yok. Exception katmanları kendiliğinden geçip `GlobalExceptionHandler`'a ulaşıyor.

İkinci sebep transaction: `@Transactional` varsayılan olarak sadece `RuntimeException`'da rollback yapıyor. `update()` içinde ürün bulunamazsa transaction geri alınıyor. Checked exception'da yapılmazdı.

**4. `HttpServletRequest` neden parametreye eklendi?**
Hatanın hangi istekte oluştuğunu `ErrorResponse`'un `path` alanına yazmak için: `request.getRequestURI()` → `/api/products/99`. Exception'ın kendisi hangi adresten geldiğini bilmiyor. `@ExceptionHandler` metotlarına Spring, controller metotlarına olduğu gibi bazı parametreleri kendisi veriyor: exception, `HttpServletRequest`, `HttpServletResponse`, `WebRequest`... Ben de `log.error(...)` satırında HTTP metodunu (`request.getMethod()`) yazmak için kullandım.

**5. Postman'de 404 alan bir isteğin yanıt gövdesini gözlemle ve açıkla.**
`GET /api/products/99`'un cevabı:
```json
{
  "timestamp": "2026-11-25T21:30:12.345678",
  "status": 404,
  "message": "Product 99 bulunamadı.",
  "path": "/api/products/99"
}
```
- `timestamp`: hatanın oluştuğu an. Spring Boot `LocalDateTime`'ı ISO-8601 formatında yazıyor. Bir kullanıcı "şu saatte hata aldım" dediğinde log'larda aynı anı bulmak için kullanılıyor.
- `status`: HTTP durum kodunun gövdedeki kopyası. İstemci sadece gövdeyi log'lasa bile kod kaybolmuyor.
- `message`: `ResourceNotFoundException`'ın constructor'ında oluşturulan mesaj.
- `path`: hangi adreste olduğu.

Day 23'te aynı istek gövdesiz bir 404 dönüyordu; şimdi istemci tam olarak neyin bulunamadığını görüyor. `GET /api/users/99` aynı şekilde `"User 99 bulunamadı."` dönüyor. Validasyon hataları ise farklı şekilde (alan → mesaj map'i) dönüyor, çünkü program öyle istiyor ve birden fazla alan aynı anda hatalı olabiliyor.

**6. `500 Internal Server Error` ne zaman döner? Kasıtlı olarak tetikle.**
Uygulamada hiçbir handler'ın özel olarak tanımadığı, beklenmeyen bir hata olunca: `NullPointerException`, veritabanı bağlantısının kopması, bir kısıtlamanın ihlali... `Exception.class` handler'ı yakalayıp 500 dönüyor.
Kasıtlı tetiklemek için geçici bir `throw new RuntimeException("test")` yazmama gerek kalmadı, uygulamada gerçek bir boşluk var: `category` alanında uzunluk sınırı yok, ama veritabanındaki `category` sütunu `varchar(255)`. Postman'deki "Çok uzun kategori" isteği 300 karakterlik bir kategori gönderiyor:
- Validasyon geçiyor (`@NotBlank` sadece boş olmasına bakıyor).
- H2 `Value too long for column "CATEGORY CHARACTER VARYING(255)"` hatası veriyor, Spring bunu `DataIntegrityViolationException`'a çeviriyor.
- Bu exception için özel bir handler yok → `handleGeneral()` → **500**, `"Beklenmeyen bir sunucu hatası oluştu."`. Asıl SQL hatası ve stack trace sadece konsolda.

Bu aynı zamanda bir hatayı ortaya çıkarıyor: istemci hatası olduğu halde 500 dönüyor. Çözümü `category`'ye `@Size(max = 255)` eklemek, yani hatayı veritabanına gitmeden 400 olarak yakalamak. Day 28'de istekteki `category` yerine `categoryId` geldiği için bu alan değişiyor.

## Notlar
- `@ControllerAdvice(basePackages = "com.egitim.demo.controller")` ile sadece belirli paketlere uygulanabiliyor; bizde tek advice olduğu için gerek yok.
- Exception'ın `throw` edildiği yerde `ResponseEntity` oluşturulmuyor: service HTTP'yi bilmiyor, sadece "bulunamadı" diyor; HTTP koduna çevirmek handler'ın işi.
- Production'da `ex.getMessage()` istemciye gönderilmemeli, sadece log'lanmalı.
