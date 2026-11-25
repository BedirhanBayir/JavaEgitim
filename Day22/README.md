# Day 22 – Repository Katmanı & JpaRepository

Day 21'de `Product`'ı entity yapmıştım ama `ProductService` hâlâ bellekteki listeyle çalışıyordu. Bugün araya `ProductRepository` ekledim ve service'i tamamen veritabanına bağladım. Artık `/api/products` doğrudan H2'deki `products` tablosunu gösteriyor; POST/PUT/DELETE ile yaptığım değişiklikleri H2 Console'da da görüyorum.

```
model/Product.java                  ← id'yi elle veren constructor silindi
repository/ProductRepository.java   ← YENİ: JpaRepository<Product, Long>
service/ProductService.java         ← List<Product> yerine repository
controller/ProductController.java   ← AYNI, hiç değişmedi
```

`ProductRepository`'deki metotlar:

| Metot | Ürettiği SQL (özet) | Kim kullanıyor |
|---|---|---|
| `findAll()`, `findById()`, `save()`, `deleteById()`, `existsById()`, `count()` | hazır | CRUD, `/count`, `/categories` |
| `findByCategory(String)` | `WHERE category = ?` | `getByCategory()` |
| `findByPriceLessThanEqual(double)` | `WHERE price <= ?` | `getByMaxPrice()` |
| `findByNameContainingIgnoreCase(String)` (ek) | `WHERE upper(name) LIKE upper('%?%')` | `/search` |
| `findByCategoryIgnoreCase(String)` (ek) | `WHERE upper(category) = upper(?)` | `/filter` |

Programdakine ek olarak:
- Controller'ı değiştirmemek için Day 16–19'dan gelen `/count`, `/categories`, `/search`, `/filter` endpoint'lerini de repository'ye taşıdım. `search` ve `filter`'daki stream'lerin yerini derived query'ler aldı. `filter`'da kategori veritabanında, isteğe bağlı fiyat aralığı ise Java'da filtreleniyor.
- `create()` içinde istemcinin gönderdiği nesneyi doğrudan `save()`'e vermiyorum, id'siz yeni bir `Product` oluşturuyorum. Body'de `"id": 1` gelirse `save()` INSERT yerine 1 numaralı ürünün üzerine yazardı (2. soruya bakın).
- `JpaRepository.count()` `long` döndürüyor; controller `int` beklediği için service'te `(int)` ile çevirdim.
- İstekler `Day22.postman_collection.json` dosyasında.

## Denemek için
1. Uygulamayı başlat, `GET /api/products` → `data.sql`'deki 5 ürün (artık veritabanından geliyor).
2. `POST /api/products` ile ürün ekle, konsolda Hibernate'in yazdığı `insert into products ...` satırını gör.
3. http://localhost:8080/h2-console → `SELECT * FROM PRODUCTS;` → yeni ürün tabloda.
4. `GET /api/products/search?name=la` → `select ... where upper(p1_0.name) like upper(?) escape '\'` sorgusu konsolda.

## Sorular

**1. `JpaRepository` neden interface? Implementasyonu kim yazıyor?**
Ben sadece "hangi işlemleri istiyorum" diyorum, "nasıl yapılacağını" Spring Data hallediyor. Uygulama açılırken Spring Data, `JpaRepository`'yi extend eden interface'leri buluyor ve her biri için çalışma anında bir proxy sınıf üretiyor. Hazır metotların asıl kodu `SimpleJpaRepository` sınıfında; o da Hibernate'in `EntityManager`'ını kullanıyor. Bu proxy bean olarak kaydediliyor, `ProductService`'in constructor'ına da o veriliyor. Yani `ProductRepository`'nin bir `class`'ını hiç yazmadım. Interface olmasının bir faydası da şu: test yazarken bunun yerine sahte (mock) bir repository kolayca verilebiliyor.

**2. `save()` metodu hem create hem update için nasıl kullanılabilir?**
`save()` nesnenin yeni olup olmadığına bakıyor; bunu da `id` alanından anlıyor:
- `id` **null** ise yeni kayıt → `INSERT`, veritabanının verdiği id nesneye yazılıp geri dönüyor.
- `id` **doluysa** var olan kayıt sayılıyor → Hibernate o satırı okuyup alanları güncelliyor → `UPDATE`.

`update()`'te önce `findById()` ile ürünü bulup alanlarını değiştiriyorum, sonra `save()` çağırıyorum; id dolu olduğu için UPDATE oluyor. `create()`'te ise id'siz yeni nesne verdiğim için INSERT oluyor. Bu yüzden istemcinin body'de gönderdiği id'yi kullanmıyorum, yoksa POST istemeden başka bir ürünü güncelleyebilirdi.

**3. `findByCategory()` metodunu nasıl yazdın → Spring SQL'i nasıl üretiyor? (Derived Query)**
Sadece interface'e `List<Product> findByCategory(String category);` satırını yazdım, gövdesi yok. Spring Data açılışta metot adını parçalara ayırıyor:
- `find...By` → bu bir SELECT sorgusu
- `Category` → `Product`'taki `category` alanı (alan adı yanlış olsaydı uygulama açılmazdı)
- koşul yazılmadığı için eşitlik → `WHERE category = ?`

Sonra bundan JPQL (`select p from Product p where p.category = ?1`), Hibernate de bundan SQL üretiyor. Koşullar eklenebiliyor: `LessThanEqual` → `<=`, `Containing` → `LIKE %...%`, `IgnoreCase` → `upper(...)`, `And`/`Or` ile birden fazla alan. Dikkat ettiğim bir şey: `findByCategory("elektronik")` büyük/küçük harfe duyarlı, "Elektronik"i bulmuyor. `/filter` önceden `equalsIgnoreCase` ile çalıştığı için orada `findByCategoryIgnoreCase` kullandım. Ad çok uzayan veya karmaşık sorgular için `@Query` ile JPQL/SQL elle yazılıyor.

**4. `@Repository` anotasyonu zorunlu mu? Neden var?**
`JpaRepository`'yi extend eden bir interface için zorunlu değil; Spring Data onu zaten bulup bean yapıyor, anotasyonu silsem de çalışır. Yine de iki sebeple var:
- Kodu okuyan kişiye "bu sınıf veri erişim katmanı" diye anlatıyor (`@Service`, `@Controller` gibi bir katman etiketi).
- Kendi yazdığım normal bir repository **class**'ında zorunlu; bean olmasını sağlıyor ve veritabanının kendi hatalarını (SQL hataları gibi) Spring'in ortak `DataAccessException` hatalarına çeviriyor.

**5. `existsById()` neden `findById() != null` yerine tercih edilir?**
Önce bir hata: `findById()` hiçbir zaman `null` döndürmüyor, `Optional<Product>` döndürüyor. Ürün yoksa bile boş bir `Optional` geliyor, yani `findById(id) != null` her zaman `true` olur ve kontrol işe yaramaz. Doğrusu `findById(id).isPresent()`. Ama sadece "var mı?" diye soruyorsam `existsById()` daha iyi: niyetimi açıkça söylüyor ve tüm ürünü okuyup nesneye çevirmek yerine sadece varlığını kontrol eden bir sorgu çalıştırıyor. `delete()`'te ürünün kendisine ihtiyacım olmadığı için bunu kullandım. `update()`'te ise ürünü değiştireceğim için `findById()` kullandım.

**6. Katman sırası nedir? Controller → Service → Repository → DB**
```
İstemci (Postman)
   ↓  HTTP + JSON
ProductController    → isteği alır, HTTP durum kodunu (200/201/400/404) seçer
   ↓
ProductService       → iş kuralları (isValid, id'siz create), repository'yi çağırır
   ↓
ProductRepository    → veritabanı işlemleri (Spring Data'nın ürettiği proxy)
   ↓  Hibernate SQL üretir
H2 (products tablosu)
```
Her katman sadece bir altındakini tanıyor. Bugünkü değişiklik bunun faydasını gösterdi: listeden veritabanına geçtim ama `ProductController`'da tek satır değişmedi, çünkü controller verinin nerede tutulduğunu bilmiyor, sadece service'i çağırıyor.

## Notlar
- `ddl-auto=create-drop` olduğu için uygulama her açılışta tablo sıfırlanıyor ve `data.sql` tekrar çalışıyor; eklediğim ürünler kapanınca gidiyor.
- `UserService` hâlâ bellekteki listeyle çalışıyor; bugünkü görev sadece `Product` içindi.
- Derived query'de alan adını yanlış yazınca (örn. `findByCategori`) hata çalışma anında değil, uygulama açılırken alınıyor: `No property 'categori' found for type 'Product'`.
