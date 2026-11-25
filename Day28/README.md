# Day 28 – Entity İlişkileri: @OneToMany & @ManyToOne

Day 27'deki projeye `Category` entity'sini ekledim ve `Product` ile bire-çok ilişki kurdum. Bir kategoride birden fazla ürün olabiliyor, her ürün tek bir kategoriye ait. Ürünün kategorisi artık bir `String` değil, `categories` tablosundaki bir satıra işaret eden `category_id` foreign key'i.

```
model/Category.java                 ← YENİ: id, name, description, products (@OneToMany)
model/Product.java                  ← String category → Category category (@ManyToOne)
dto/CategoryCreateRequest.java      ← YENİ: name, description + validasyon
dto/ProductCreateRequest.java       ← category → categoryId
repository/CategoryRepository.java  ← YENİ
repository/ProductRepository.java   ← sorgular category.name üzerinden
service/CategoryService.java        ← YENİ: createCategory, getAllCategories, getCategoryById
service/ProductService.java         ← create/update categoryId ile
controller/CategoryController.java  ← YENİ: /api/categories
controller/ProductController.java   ← POST/PUT categoryId alıyor
config/DataLoader.java              ← önce 2 kategori, sonra 5 ürün
```

| Endpoint | Body | Cevap |
|---|---|---|
| `POST /api/categories` | `{"name": "Kitap", "description": "..."}` | **201** + kategori |
| `GET /api/categories` | — | kategoriler, her biri ürünleriyle |
| `GET /api/categories/{id}` | — | **200** kategori + ürünleri, yoksa **404** |
| `POST /api/products` | `{"name": "Laptop", "price": 15000, "categoryId": 1}` | **201**, kategori yoksa **404** `"Category 1 bulunamadı."` |
| `PUT /api/products/{id}` | aynı | **200**, ürün başka kategoriye de taşınabiliyor |

Programdakinden farklı yaptıklarım:
- **`Product`'ta iki tane `category` alanı olamaz.** Programdaki kod `Product`'a `Category category` alanını "mevcut alanlara ekle" diyor, ama `Product`'ta zaten `String category` var. Java'da aynı isimde iki alan olamayacağı için kod derlenmez. Programdaki `new Product(name, price, category.getName())` satırı da kategori adını ayrıca bir String'de tutmaya devam ediyor; bu da aynı bilgiyi iki yerde tutmak demek (kategori adı değişirse ürünlerdeki eski ad kalır). `String category`'yi tamamen kaldırdım. Kategori adı artık sadece `categories` tablosunda.
- **Ürün JSON'ında kategori adı kaybolmasın diye `getCategoryName()` getter'ı ekledim.** `@JsonBackReference` yüzünden ürünün `category` alanı JSON'a yazılmıyor. Bu getter ürünü `{"id": 1, "name": "Laptop", "price": 15000.0, "categoryName": "Elektronik"}` olarak gösteriyor. Veritabanında karşılığı yok.
- **Eski endpoint'ler ilişki üzerinden çalışıyor.** `/category/{category}`, `/filter`, `/search`, `/categories` ve toplu silme kategori **adıyla** çalışmaya devam ediyor. Bunun için sorgular `findByCategory_Name` (`category.name`) şeklinde yazıldı (3. soruya bakın).
- **Kategori oluştururken de validasyon var** (`CategoryCreateRequest`): isim boş olamaz, açıklama 255 karakteri geçemez. Day 26'daki 500 hatasının bir benzeri burada oluşmasın diye.
- **`products` listesi `new ArrayList<>()` ile başlıyor.** Yeni oluşturulan kategoride JSON'da `"products": null` yerine `"products": []` görünüyor.
- İstekler `Day28.postman_collection.json` dosyasında.

## Day 27'den geçerken: tabloyu sil

Day 27 `egitimdb`'de `products` tablosunu `category` (varchar) sütunuyla oluşturdu. `ddl-auto=update` var olan tabloyu silmiyor; sadece eksik olanı ekliyor. Yani `categories` tablosunu ve `category_id` sütununu ekleyip eski `category` sütununu olduğu gibi bırakıyor. Eski ürünlerin de kategorisi olmuyor (`category_id = NULL`). Bu yüzden Day 28'i ilk kez çalıştırmadan önce MySQL Workbench'te:
```sql
USE egitimdb;
DROP TABLE products;
```
Uygulama açılınca Hibernate iki tabloyu da sıfırdan oluşturuyor, `DataLoader` da 2 kategori ve 5 ürün ekliyor. (`update`'in sütun silmediğini burada görüyorum. Gerçek projelerde bu tür değişiklikler Flyway/Liquibase ile, elle yazılmış SQL ile yapılıyor.)

## Sorular

**1. @OneToMany ve @ManyToOne nedir? Bire-çok ilişkiyi açıkla.**
Bire-çok (one-to-many): bir tarafın bir kaydı, diğer tarafın birden çok kaydıyla eşleşiyor. Bir kategoride (Elektronik) birden fazla ürün (Laptop, Fare, Kulaklık) var, ama her ürün sadece bir kategoride. Aynı ilişkiye iki taraftan bakılıyor:
- `Category` tarafından: bir kategori → çok ürün → `@OneToMany List<Product> products`
- `Product` tarafından: çok ürün → bir kategori → `@ManyToOne Category category`

Veritabanında bu ilişki tek bir sütunla tutuluyor: "çok" tarafındaki `products` tablosunda, hangi kategoriye ait olduğunu gösteren `category_id`. Bir hücreye birden fazla değer yazılamadığı için ürün listesini categories tablosunda tutmak mümkün değil. `categories` tablosunda ürünlerle ilgili hiçbir sütun yok. Java'da ise iki taraf da birbirini görüyor: `product.getCategory()` ve `category.getProducts()`. Buna **çift yönlü (bidirectional)** ilişki deniyor.

**2. `mappedBy` ne anlama gelir? Hangi tarafta yazılır?**
Çift yönlü ilişkide foreign key tek yerde durduğu için bir tarafın ilişkinin **sahibi (owner)** olması gerekiyor. `mappedBy` "bu ilişkinin sahibi ben değilim, karşı taraftaki şu alan" demek. `@OneToMany(mappedBy = "category")` → "ilişki `Product` sınıfındaki `category` alanında tanımlı, foreign key orada". `mappedBy`, sahibi olmayan, yani foreign key'i tutmayan tarafa yazılıyor. Bire-çokta bu her zaman "bir" tarafı (`Category`).
Yazmasaydım Hibernate iki ayrı ilişki olduğunu düşünürdü: `Product`'taki için `category_id`, `Category`'deki için de ayrı bir ara tablo (`categories_products`) oluştururdu. Önemli bir sonucu: veritabanına sadece sahip tarafın değeri yazılıyor. `category.getProducts().add(product)` yazmak tek başına hiçbir şey kaydetmiyor; `product.setCategory(category)` kaydediyor. Bu yüzden `ProductService.create()` içinde ürünün kategorisini set ediyorum.

**3. `@JoinColumn` neden `Product` tarafında? Neden `Category`'de değil?**
`@JoinColumn` foreign key sütununu tanımlıyor ve bu sütun fiziksel olarak `products` tablosunda duruyor. Her ürün satırında tek bir `category_id` var. `Category`'ye koyamazdım, çünkü bir kategori satırında birden fazla ürünün id'sini tutacak bir sütun olamaz. `@JoinColumn(name = "category_id")` sütunun adını belirliyor; yazılmasaydı Hibernate `alan_adı + _ + karşı tablonun id'si` kuralıyla yine `category_id` üretirdi. Hibernate şu SQL'i oluşturuyor:
```sql
-- (kısaltılmış)
create table products (id bigint not null auto_increment, name varchar(255), price float(53) not null,
                       category_id bigint, primary key (id))
alter table products add constraint FK... foreign key (category_id) references categories (id)
```
Foreign key kısıtı sayesinde veritabanı olmayan bir kategoriye işaret eden ürün kabul etmiyor.
Sorgularda da ilişki üzerinden gidiliyor: `findByCategory_Name("Elektronik")`, `select ... from products p left join categories c on c.id = p.category_id where c.name = ?` üretiyor. Alt çizgiyi bilerek koydum. Spring metot adını parçalarken önce `categoryName` adında bir alan arıyor, `Product`'taki `getCategoryName()` getter'ı yüzünden onu bulabilir ve var olmayan bir sütun için sorgu üretmeye çalışırdı. `_` "burada böl: `category` → `name`" demek.

**4. `@JsonManagedReference` ve `@JsonBackReference` neden gerekli? (Sonsuz döngü problemi)**
Jackson bir nesneyi JSON'a çevirirken bütün getter'larını takip ediyor. Çift yönlü ilişkide: `Category` → `getProducts()` → her `Product` → `getCategory()` → aynı `Category` → `getProducts()` → ... Bu döngü hiç bitmiyor. Sonunda `StackOverflowError` oluşuyor ya da Jackson `Infinite recursion` hatası veriyor, istemci de 500 alıyor.
- `@JsonManagedReference` (`Category.products`): "ileri" taraf, normal şekilde yazılıyor.
- `@JsonBackReference` (`Product.category`): "geri" taraf, JSON'a **hiç yazılmıyor**.

`GET /api/categories/1`:
```json
{
  "id": 1, "name": "Elektronik", "description": "Bilgisayar, telefon ve aksesuarlar",
  "products": [
    { "id": 1, "name": "Laptop",   "price": 15000.0, "categoryName": "Elektronik" },
    { "id": 2, "name": "Fare",     "price": 250.0,   "categoryName": "Elektronik" },
    { "id": 5, "name": "Kulaklık", "price": 1200.0,  "categoryName": "Elektronik" }
  ]
}
```
Bunun bir yan etkisi var: `GET /api/products/1`'de ürünün `category`'si de yazılmıyor. Ben bunu `getCategoryName()` ile çözdüm. Daha temiz çözüm entity'yi hiç döndürmeyip Day 20'deki gibi DTO döndürmek (`ProductResponse`, `CategoryResponse`). DTO'da döngü olmaz, JSON'da ne görüneceğini de ben seçerim. Bu iki anotasyon entity'yi doğrudan döndürdüğümüz için gerekiyor.

**5. `cascade = CascadeType.ALL` ne yapar? Riskleri nelerdir?**
Cascade, kategoriye yapılan işlemin ürünlerine de **otomatik olarak uygulanması**. `ALL` = `PERSIST` + `MERGE` + `REMOVE` + `REFRESH` + `DETACH`:
- `PERSIST`: içinde yeni ürünler olan yeni bir kategori `save()` edilince ürünler de kaydediliyor.
- `REMOVE`: kategori silinince **içindeki bütün ürünler de siliniyor**.

Riskleri:
- **Toplu ve geri dönüşsüz silme:** "Elektronik" kategorisini silmek tek bir isteğe bağlı olarak tüm elektronik ürünleri siliyor. Gerçek bir mağazada bu ürünlere bağlı siparişler, faturalar da olur. Daha güvenli seçenekler: içinde ürün olan kategoriyi silmeyi reddetmek (409) ya da ürünleri "kategorisiz" bırakmak.
- **Performans:** Silmeden önce Hibernate bütün ürünleri yüklüyor ve her biri için ayrı `DELETE` çalıştırıyor.
- **Yanlış tarafa koymak:** `@ManyToOne` tarafına `cascade = REMOVE` konursa tek bir ürünü silmek kategorisini ve dolayısıyla diğer bütün ürünleri silebilir. Cascade sadece "sahibi olan" (parent → child) yönde düşünülmeli.

Bu projede kategori silme endpoint'i yok, yani `REMOVE` şu an hiç tetiklenmiyor. Programdaki gibi bıraktım; silme eklenirse önce `ALL` yerine `{CascadeType.PERSIST, CascadeType.MERGE}` düşünülmeli.

**6. Veritabanında `category_id` sütununu MySQL Workbench ile kontrol edebildin mi?**
`egitimdb` → Tables altında artık iki tablo var: `categories` (`id`, `name`, `description`) ve `products` (`id`, `name`, `price`, `category_id`). `products` → Foreign Keys sekmesinde `category_id` → `categories.id` ilişkisi görünüyor. Kontrol sorgusu:
```sql
SELECT p.id, p.name, p.price, p.category_id, c.name AS category
FROM products p JOIN categories c ON c.id = p.category_id;
```
Kalem ve Defter'in `category_id`'si 2 (Kırtasiye), diğerlerinin 1 (Elektronik) olmalı. Var olmayan bir kategoriye bağlamayı denersem foreign key buna izin vermiyor: `INSERT INTO products (name, price, category_id) VALUES ('Test', 1, 99);` → `Cannot add or update a child row: a foreign key constraint fails`.

<!-- TODO: Workbench ekran görüntüsünü Day28 klasörüne mysql-category-id.png adıyla koy -->
![MySQL Workbench – category_id](mysql-category-id.png)

## Notlar
- **Lazy / Eager:** `@OneToMany` varsayılan olarak **LAZY**: `categoryRepository.findById(1)` sadece `categories` tablosunu okuyor, ürünler `getProducts()` ilk çağrıldığında ayrı bir SELECT ile geliyor. `@ManyToOne` ise varsayılan olarak **EAGER**: ürün okunurken kategorisi de geliyor.
- **N+1 problemi:** `GET /api/categories` önce 1 sorguyla bütün kategorileri, sonra JSON'a yazarken **her kategori için** ayrı bir ürün sorgusunu çalıştırıyor: N kategori için 1 + N sorgu. `show-sql=true` ile konsolda görülüyor. Çözümleri: `@Query("select c from Category c left join fetch c.products")` ya da `@EntityGraph(attributePaths = "products")` ile tek sorguda çekmek.
- Lazy ürünler JSON'a yazılırken repository metodu çoktan bitmiş oluyor. Yine de çalışıyor, çünkü Spring Boot'ta `spring.jpa.open-in-view` varsayılan olarak açık ve veritabanı oturumunu istek bitene kadar açık tutuyor. Açılışta konsolda bununla ilgili bir uyarı (`spring.jpa.open-in-view is enabled by default`) görünüyor. Kapatılırsa `LazyInitializationException` alınıyor. Bu da DTO kullanmanın bir sebebi daha.
- `POST /api/products`'ta olmayan bir `categoryId` için 404 dönüyor (programdaki `ResourceNotFoundException`). Adres (`/api/products`) bulunduğu, sorun gövdedeki bir değerde olduğu için bazı API'ler bu durumda 400 veya 422 dönüyor.
