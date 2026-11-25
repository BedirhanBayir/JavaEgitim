# Day 24 – Veritabanı İşlemleri II: Update & Delete, @Transactional

Day 23'teki projeyi kullandım. Update ve Delete endpoint'leri Day 22'den beri veritabanıyla çalışıyordu; bugün onları test edip `@Transactional` ekledim ve kategoriye göre toplu silme yazdım.

```
repository/ProductRepository.java   ← long deleteByCategory(String category)
service/ProductService.java         ← update(), delete(), deleteByCategory() @Transactional
controller/ProductController.java   ← DELETE /api/products/category/{category}
```

Programdan farklı yaptığım bir şey var. Programda `deleteByCategory` `void` ve service önce `findByCategory()` ile ürünleri çekip sayıyor, sonra siliyor. Spring Data derived delete metotlarında dönüş tipi `long` olunca silinen kayıt sayısını kendisi döndürüyor. Bu yüzden ayrıca `findByCategory()` çağırmadım, aynı ürünler iki kez okunmuyor. Ayrıca `delete()`'i de `@Transactional` yaptım; `existsById` ile `deleteById` aynı transaction'da çalışıyor.

## Test Senaryoları

İstekler `Day24.postman_collection.json` dosyasında, bu sırayla:

| Test | İstek | Sonuç |
|---|---|---|
| Var olan ID güncelle | `PUT /api/products/1` + body | **200** + güncel nesne |
| Olmayan ID güncelle | `PUT /api/products/999` + body | **404** |
| Var olan ID sil | `DELETE /api/products/2` | **204**, gövde yok |
| Olmayan ID sil | `DELETE /api/products/999` | **404** |
| Silinen ID'yi getir | `GET /api/products/2` | **404** |
| Toplu silme (ek) | `DELETE /api/products/category/Kırtasiye` | **200**, `2 ürün silindi.` |
| Olmayan kategori (ek) | `DELETE /api/products/category/Yok` | **200**, `0 ürün silindi.` |

Kategori silmede yol Türkçe karakter içeriyor; Postman `Kırtasiye`'yi URL'de otomatik olarak `K%C4%B1rtasiye` şeklinde kodluyor, Spring de geri çeviriyor.

## Sorular

**1. `@Transactional` nedir? Olmadan ne sorun çıkabilir?**
Transaction, birden fazla veritabanı işlemini tek bir bütün yapmak: hepsi başarılı olursa **commit** ediliyor, biri hata verirse o ana kadar yapılanlar da **rollback** ile geri alınıyor ("ya hep ya hiç"). `@Transactional` bunu Spring'e bırakıyor: metot başlarken transaction açılıyor, metot normal biterse commit, `RuntimeException` fırlatırsa rollback yapılıyor.
Olmadan ne oluyor: repository'nin her metodu kendi küçük transaction'ında çalışıyor. Day 22'deki `update()`'te `findById()` ve `save()` iki ayrı transaction'dı; arada başka bir istek ürünü silse veya değiştirse fark edilmezdi. Birden fazla tabloya yazan bir işlemde (örn. sipariş + stok düşme) ilk yazma başarılı, ikincisi hatalı olursa veri yarım kalırdı. Ayrıca `deleteByCategory` gibi derived delete metotları transaction olmadan hiç çalışmıyor, `TransactionRequiredException` veriyor. Bir şey daha: `@Transactional` Spring'in araya koyduğu proxy üzerinden çalışıyor. Bu yüzden aynı sınıfın içinden `this.update()` diye çağrılırsa devreye girmiyor ve sadece `public` metotlarda çalışıyor.

**2. Güncelleme yaparken `save()` neden yeni kayıt oluşturmuyor?**
`save()` önce nesnenin yeni olup olmadığına bakıyor; bizde bunu `id`'ye göre yapıyor. `update()`'te nesneyi `findById()` ile veritabanından aldığım için `id`'si dolu, yani "yeni değil" → INSERT yerine güncelleme yapılıyor.
`@Transactional` ile bir detay daha var: `findById()`'den gelen nesne transaction boyunca Hibernate'in takibinde (**managed**) kalıyor. `setName()`, `setPrice()` ile değiştirdiğim alanları Hibernate kendisi fark ediyor (**dirty checking**) ve commit anında `update products set category=?,name=?,price=? where id=?` çalıştırıyor. Yani bu durumda `save()` yazmasam da güncelleme olurdu. Programdaki gibi okunabilirlik için yine de yazdım. `@Transactional` olmadan (Day 22) nesne `findById()`'den sonra takipten çıkıyordu. O zaman `save()` onu tekrar `SELECT` ile okuyup sonra güncelliyordu, yani fazladan bir sorgu vardı.

**3. `deleteByCategory()` metodu Spring tarafından nasıl çözümleniyor?**
`findByCategory` ile aynı mantık: metot adı parçalanıyor; `deleteBy` → silme işlemi, `Category` → `category` alanı, koşul yok → eşitlik. Ama arka planda tek bir `DELETE ... WHERE` çalışmıyor. Spring Data önce `select ... from products where category=?` ile ürünleri **çekiyor**, sonra her biri için ayrı `delete from products where id=?` çalıştırıyor. Kırtasiye için konsolda 1 SELECT + 2 DELETE görünmeli. Bunu yapmasının sebebi her entity'nin kendi silme kurallarının (cascade, `@PreRemove`) çalışması. Dönüş tipi `long` olunca bulunan ürün sayısını döndürüyor.
Programdaki soru, yani `findByCategory` + döngü + `delete` mi daha verimli: ikisi aslında aynı şeyi yapıyor, derived delete sadece daha kısa yazılıyor. Binlerce satır silinecekse tek sorguluk toplu silme daha hızlı:
```java
@Modifying
@Query("delete from Product p where p.category = :category")
int bulkDeleteByCategory(String category);
```
Bu tek bir `DELETE` çalıştırıyor ama entity'leri okumadığı için cascade ve `@PreRemove` çalışmıyor.

**4. `204 No Content` neden `200 OK` yerine kullanılır?**
İkisi de "başarılı" demek. Farkı şu: 204, "işlem tamam ama geri gönderecek bir şey yok" anlamına geliyor. Silinen ürünü geri döndürmenin anlamı yok, bu yüzden tek ürün silmede 204 dönüyorum ve gövde tamamen boş. İstemci gövdeyi okumaya çalışmıyor. Toplu silmede ise `"2 ürün silindi."` gibi bir bilgi döndürdüğüm için 200 kullanılıyor; gövdesi olan bir cevaba 204 demek yanlış olurdu.

**5. H2 Console'da silme sonrası tabloyu kontrol ettin mi? Ne gördün?**
`DELETE /api/products/2`'den sonra `SELECT * FROM PRODUCTS;` → Fare (id 2) tabloda olmamalı. 1, 3, 4, 5 kalmalı; id'ler yeniden numaralanmıyor, 2 boş kalıyor. `PUT /api/products/1`'den sonra 1. satırda yeni isim ve fiyat görünmeli. Toplu silmeden sonra Kırtasiye satırları (Kalem, Defter) gitmeli, sadece Elektronik ürünleri kalmalı. `SELECT COUNT(*) FROM PRODUCTS WHERE CATEGORY = 'Kırtasiye';` → 0.

**6. Aynı anda iki kullanıcı aynı ürünü silmeye çalışırsa ne olur? (Concurrency problemi)**
İki istek A ve B aynı anda `DELETE /api/products/3` gönderiyor:
- **Biri diğerinden biraz sonra gelirse:** A siliyor ve 204 alıyor. B'nin `existsById()`'si `false` dönüyor, B 404 alıyor. Sorun yok.
- **Tam aynı anda gelirse:** İkisinin `existsById()`'si de ürün henüz silinmediği için `true` dönüyor. İkisi de silmeye çalışıyor. Önce commit eden 204 alıyor. Diğerinin `DELETE`'i 0 satırı etkiliyor. Hibernate "1 satır silinmeliydi, 0 silindi" diye `StaleStateException` fırlatıyor, Spring bunu `ObjectOptimisticLockingFailureException`'a çeviriyor ve şu an **500** dönüyor.

Bu bir **race condition**: "kontrol et, sonra yap" arasında başka biri araya giriyor. Silmede sonuç zararsız, çünkü ürün zaten silinmiş. Asıl tehlike güncellemede: iki kişi aynı ürünü aynı anda güncellerse sonra yazan, öncekinin değişikliğini fark etmeden ezer (**lost update**). Çözümler:
- **Optimistic locking:** entity'ye `@Version private Long version;` eklenir. Her güncellemede sayı artar. Güncelleme sırasında sayı değişmişse işlem reddedilir, kullanıcıya "kayıt değişti, yeniden dene" (genelde 409) denir. Çakışmanın nadir olduğu durumlar için uygun.
- **Pessimistic locking:** `@Lock(LockModeType.PESSIMISTIC_WRITE)` ile satır okunurken kilitlenir, diğer işlem bekler. Kesin ama yavaş, kilit beklemeleri oluşabilir.

## Notlar
- `@Transactional`'ı `org.springframework.transaction.annotation` paketinden aldım (`jakarta.transaction.Transactional` da var ama Spring'inkinde `readOnly`, `rollbackFor` gibi ayarlar var).
- `@Transactional` sadece veri değiştiren metotlarda var; `getAll()`, `getById()` gibi okumalar repository'nin kendi transaction'ıyla çalışıyor.
- Checked exception'larda (`Exception`'dan türeyip `RuntimeException`'dan türemeyenler) varsayılan olarak rollback yapılmıyor; gerekiyorsa `@Transactional(rollbackFor = Exception.class)` yazılıyor.
