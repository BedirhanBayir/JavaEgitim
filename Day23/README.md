# Day 23 – Veritabanı İşlemleri I: Create & Read

Day 22'deki projeyi kullandım. Bugün yeni kod çok az; asıl iş, Create ve Read senaryolarını Postman'den çalıştırıp sonucu H2 Console'dan doğrulamak.

```
controller/ProductController.java   ← GET /api/products/category/{category} eklendi
resources/application.properties    ← spring.sql.init.mode=always eklendi
```

`defer-datasource-initialization=true` ve `data.sql` Day 21'den beri projede vardı, bugün sadece `spring.sql.init.mode=always` eklendi. Bütün istekler `Day23.postman_collection.json` dosyasında, sırayla çalıştırılacak şekilde dizili.

## Test Senaryoları

Uygulama açılınca `data.sql` 5 ürün ekliyor (id 1–5). Senaryolar bu sırayla çalıştırılınca:

**Create**

| Test | Gönderilen JSON | Program | Bu projede |
|---|---|---|---|
| Geçerli ürün | `{"name":"Monitör","price":8500,"category":"Elektronik"}` | 201 + id'li nesne | **201**, `"id": 6` |
| Boş name | `{"name":"","price":100,"category":"Test"}` | Şimdilik 201 | **400** (aşağıya bakın) |
| Fiyat 0 | `{"name":"Test","price":0,"category":"Test"}` | 201 | **201**, `"id": 7` |

Program boş isim için "şimdilik 201 döner, yarın validasyon" diyor, ama bu projede Day 18'den beri `ProductService.isValid()` var: isim veya kategori boşsa ya da fiyat negatifse **400** dönüyor ve kayıt veritabanına hiç gitmiyor. Fiyat 0 geçerli sayılıyor (`price >= 0`). Annotation'larla yapılan asıl validasyon Day 25'te geliyor.

**Read**

| Test | Endpoint | Sonuç |
|---|---|---|
| Tüm ürünler | `GET /api/products` | **200**, JSON dizi (5 + eklenenler) |
| Var olan ID | `GET /api/products/1` | **200**, Laptop |
| Olmayan ID | `GET /api/products/999` | **404**, gövde boş |
| Kategoriye göre | `GET /api/products/category/Elektronik` | **200**, Laptop, Fare, Kulaklık (+ Monitör) |
| Küçük harfle (ek) | `GET /api/products/category/elektronik` | **200**, boş dizi `[]` |

Son satırı ben ekledim: `findByCategory` büyük/küçük harfe duyarlı, `elektronik` yazınca hiçbir şey bulmuyor. Büyük/küçük harf fark etmeden aramak için Day 22'den beri `/api/products/filter?category=elektronik` var (`findByCategoryIgnoreCase`).

## Sorular

**1. `data.sql` dosyası ne zaman çalışır? Neden `defer-datasource-initialization=true` ekledik?**
Spring Boot, `resources` altındaki `schema.sql` ve `data.sql` dosyalarını uygulama açılırken, veritabanı bağlantısı hazır olunca çalıştırıyor. Varsayılan sıra şöyle: **önce** SQL dosyaları, **sonra** Hibernate (`ddl-auto`). Bizde tabloyu Hibernate oluşturduğu için `data.sql` çalıştığında `products` tablosu henüz yok ve `Table "PRODUCTS" not found` hatası çıkıyordu (Day 21'de bunu yaşadım). `defer-datasource-initialization=true` sırayı tersine çeviriyor: önce Hibernate tabloyu oluşturuyor, sonra `data.sql` çalışıyor.
`spring.sql.init.mode=always` ise `data.sql`'in hangi veritabanlarında çalışacağını belirliyor. Varsayılan `embedded`: sadece H2 gibi gömülü veritabanlarında çalışıyor. H2 kullandığımız için şu an bir şey değiştirmiyor; MySQL'e geçince (Day 27) bu ayar olmadan `data.sql` hiç çalışmazdı.

**2. Postman'de POST yaptıktan sonra H2 Console'da `SELECT * FROM products` çalıştırdın mı? Sonuç ne oldu?**
Monitör'ü POST ettikten sonra konsolda Hibernate'in `insert into products (category,name,price,id) values (?,?,?,default)` satırı görünmeli. H2 Console'da (`jdbc:h2:mem:testdb`) `SELECT * FROM PRODUCTS;` çalıştırınca `data.sql`'deki 5 ürün ve 6. satırda Monitör görünmeli; fiyat 0 ile eklenen ürün de 7. satırda. Boş isimli istek 400 döndüğü için tabloda yok. Bu, Day 21'deki durumdan farklı: o zaman API listeyi, H2 Console veritabanını gösteriyordu; artık ikisi aynı veriyi gösteriyor.

**3. `GET /api/products/999` için neden 404 döndü? Kodu takip et ve açıkla.**
1. `ProductController.getById(999)` çağrılıyor, `productService.getById(999)`'u çağırıyor.
2. `ProductService.getById()` → `productRepository.findById(999)`.
3. Hibernate `select ... from products p1_0 where p1_0.id=?` çalıştırıyor, satır gelmiyor.
4. `findById()` null değil, **boş bir `Optional`** döndürüyor.
5. Controller'da `.map(ResponseEntity::ok)` boş Optional'da çalışmıyor, `.orElse(ResponseEntity.notFound().build())` devreye giriyor → **404**, gövde boş.

Gövde boş olduğu için istemci neden 404 aldığını bilmiyor. Day 26'da `ResourceNotFoundException` ve global hata yönetimi ile bunu açıklayıcı bir JSON'a çevireceğim.

**4. `findByCategory()` için Spring arka planda hangi SQL'i üretiyor? `show-sql=true` ile göster.**
`GET /api/products/category/Elektronik` çağrılınca konsolda şu görünüyor:
```
Hibernate: select p1_0.id,p1_0.category,p1_0.name,p1_0.price from products p1_0 where p1_0.category=?
```
`p1_0` Hibernate'in tabloya verdiği takma ad. `?` bir parametre: "Elektronik" değeri SQL metnine yapıştırılmıyor, ayrı gönderiliyor (prepared statement). Bu yüzden SQL injection yapılamıyor. Değeri de görmek için `logging.level.org.hibernate.orm.jdbc.bind=TRACE` eklenebiliyor.

**5. Her `POST` sonrası ID otomatik artıyor mu? Neden?**
Evet. `Product.id` alanında `@GeneratedValue(strategy = GenerationType.IDENTITY)` var, Hibernate de sütunu `id bigint generated by default as identity` olarak oluşturuyor. INSERT'te id gönderilmiyor (SQL'deki `default`), veritabanı bir sonraki sayıyı kendisi veriyor: `data.sql` 1–5'i aldığı için Monitör 6, sonraki ürün 7 oluyor. Sayaç geri gitmiyor: 7'yi silip yeni ürün eklersem 8 alıyor, boşluk kalıyor. Bu normal; id'nin görevi benzersiz olmak, sıralı olmak değil.

**6. Uygulama yeniden başlatıldığında `data.sql` verileri tekrar yükleniyor mu? Neden?**
Evet, ve POST ile eklediğim ürünler kayboluyor. Sebebi `ddl-auto=create-drop`: uygulama kapanınca tablo siliniyor, açılınca sıfırdan oluşturuluyor, sonra `data.sql` tekrar çalışıp aynı 5 ürünü ekliyor. Zaten H2 bellekte (`mem:`) çalıştığı için uygulama kapanınca veritabanının kendisi de gidiyor. Bu yüzden id sayacı da 1'den başlıyor. Program görevin girişinde "işlem veritabanında kayıtlı kalsın" diyor, ama H2 in-memory ile veri sadece uygulama açıkken duruyor. Kalıcı veri Day 27'de MySQL ile geliyor.

## Notlar
- Hata senaryoları (boş isim, olmayan id, küçük harfle kategori) mutlu yol kadar önemli; küçük harf testi olmasa `findByCategory`'nin harfe duyarlı olduğunu fark etmezdim.
- `/category/{category}` ile `/{id}` çakışmıyor, çünkü biri iki parçalı (`/category/x`), diğeri tek parçalı yol.
