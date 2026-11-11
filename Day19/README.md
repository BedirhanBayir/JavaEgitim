# Day 19 – Katmanlı Mimari: Controller & Service

Day 18'deki projeyi katmanlı olarak yeniden yazdım. Ürün listesi ve tüm iş mantığı yeni `ProductService` sınıfına taşındı. `ProductController` artık sadece HTTP ile ilgileniyor: isteği alıyor, service'i çağırıyor, uygun `ResponseEntity`'yi dönüyor. Service, controller'a constructor injection ile veriliyor.

```
model/Product.java
service/ProductService.java       ← YENİ: liste + CRUD + arama/filtre
controller/ProductController.java ← sadeleşti, iş mantığı yok
```

Programda sadece CRUD metotları taşınıyordu. Ben Day 16–17'den gelen `count`, `categories`, `search` ve `filter`'ı da service'e taşıdım ve `for` döngülerini stream ile yazdım. "Geçerli ürün" kontrolü (`isValid`) de bir iş kuralı olduğu için service'e geçti. Endpoint'ler ve cevaplar Day 18 ile aynı. Postman koleksiyonu `Day19.postman_collection.json`.

## Sorular

**1. Neden Controller içinde iş mantığı yazılmamalı?**
Controller'ın işi HTTP: URL'yi, parametreleri, body'yi ve durum kodlarını yönetmek. İş mantığı da oraya yazılınca sınıf her şeyi yapan büyük bir yığına dönüşüyor. Daha önemlisi, aynı mantığı başka bir yerde (mesela bir e-posta servisi ya da zamanlanmış bir görev ürünleri listelemek isterse) kullanmak için kodu kopyalamam gerekirdi. Service'te olunca herkes aynı yeri çağırıyor, kural tek yerde kalıyor. Tek Sorumluluk İlkesi (SRP): her sınıfın değişmek için tek bir sebebi olmalı.

**2. `@Service` ne işe yarar? Onsuz ne olurdu?**
Spring'e "bu sınıfı bir bean olarak oluştur ve yönet" diyor. Uygulama açılırken `@ComponentScan` bu sınıfı buluyor, bir tane nesnesini oluşturuyor ve ihtiyacı olan yerlere veriyor. `@Service` aslında `@Component`'in özel bir hali; adı sınıfın iş mantığı katmanı olduğunu belli ediyor. Onsuz Spring `ProductService`'i tanımazdı. Controller'ın constructor'ı bir `ProductService` istediği için uygulama açılırken "*required a bean of type ProductService that could not be found*" hatası verip hiç başlamazdı.

**3. Dependency Injection nedir? Constructor injection neden field injection'a tercih edilir?**
DI, bir sınıfın ihtiyaç duyduğu nesneleri (bağımlılıklarını) kendisinin `new` ile oluşturması yerine dışarıdan alması. Controller `new ProductService()` demiyor; Spring hazır nesneyi constructor'a veriyor. Constructor injection'ın avantajları:
- Alan `final` olabiliyor, sonradan değiştirilemiyor.
- Nesne bağımlılığı olmadan hiç oluşturulamıyor, `null` kalma riski yok.
- Testte Spring'e gerek kalmadan `new ProductController(sahteService)` diyebiliyorum.
- Bağımlılıklar constructor'da açıkça görünüyor; çok fazla olursa sınıfın fazla iş yaptığı belli oluyor.
Field injection (`@Autowired private ProductService service;`) ise alanı gizlice reflection ile dolduruyor; testte doldurmak zor ve `final` olamıyor. Tek constructor varsa Spring onu otomatik kullandığı için `@Autowired` yazmadım.

**4. `Optional<T>` nedir? Neden `null` yerine kullanılır?**
"Bir değer olabilir de olmayabilir de" diyen bir kutu. `getById` ürünü bulursa `Optional.of(urun)`, bulamazsa `Optional.empty()` döndürüyor. `null` döndürseydim çağıran taraf kontrol etmeyi unutabilir ve `NullPointerException` alabilirdi. `Optional` dönüş tipinde "sonuç boş olabilir" bilgisini açıkça yazıyor ve kullanan tarafı boş durumu düşünmeye zorluyor.

**5. `.map(...).orElse(...)` ne yapıyor?**
```java
return productService.getById(id)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
```
- `getById(id)` bir `Optional<Product>` döndürüyor.
- `.map(ResponseEntity::ok)`: Optional doluysa içindeki ürünü `ResponseEntity.ok(urun)`'a çeviriyor (200 + ürün). Boşsa hiçbir şey yapmadan boş geçiyor. `ResponseEntity::ok` bir method reference, `p -> ResponseEntity.ok(p)` ile aynı.
- `.orElse(...)`: sonuç boşsa verdiğim değeri, yani 404'ü döndürüyor.
Day 18'deki `for` + `if` + iki `return`'lük kod tek ifadeye indi.

**6. Katmanlı mimarinin test edilebilirliğe katkısı nedir?**
Her katmanı ayrı ayrı test edebiliyorum. `ProductService` düz bir Java sınıfı; sunucu açmadan `new ProductService()` deyip `create`, `filter`, `isValid` metotlarını test edebilirim. Controller'ı test ederken de gerçek service yerine sahte (mock) bir service verebilirim (constructor injection sayesinde). Böylece sadece "doğru durum kodunu dönüyor mu?" diye bakarım. İleride service veritabanına bağlandığında da controller testleri değişmez.

## Notlar
- Spring bir bean'den varsayılan olarak tek bir nesne (singleton) oluşturuyor. Bu yüzden listeye eklenen ürünü bütün istekler görüyor.
- `Product::getCategory` ve `TreeSet::new` da method reference örnekleri.
