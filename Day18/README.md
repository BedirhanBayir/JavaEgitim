# Day 18 – POST, PUT, DELETE & @RequestBody

API artık sadece okumuyor, yazıyor da. `Product`'a setter'lar ve boş bir constructor ekledim. `ProductController`'a üç yeni endpoint geldi:

| Endpoint | Body | Cevap |
|---|---|---|
| `POST /api/products` | Ürün JSON'u | **201 Created** + yeni ürün |
| `PUT /api/products/{id}` | Güncel ürün JSON'u | **200 OK** + güncellenmiş ürün, yoksa **404** |
| `DELETE /api/products/{id}` | — | **204 No Content**, yoksa **404** |

Ekler:
- Day 17'deki `GET /{id}` artık `Map.of("error")` yerine gerçek **404** dönüyor.
- POST ve PUT'ta basit bir kontrol var: isim ya da kategori boşsa veya fiyat negatifse **400 Bad Request**.
- Tüm istekleri `Day18.postman_collection.json` dosyasına kaydettim. Postman'de **Import** ile açılıyor.

POST örneği:
```json
{
  "name": "Monitör",
  "price": 8500.00,
  "category": "Elektronik"
}
```

## Sorular

**1. POST isteğinde `@RequestBody` olmasaydı ne olurdu?**
Spring isteğin gövdesindeki JSON'u okumazdı. `Product` parametresini URL'deki query parametrelerinden doldurmaya çalışırdı; orada da bir şey olmadığı için alanları boş (`name = null`, `price = 0`) bir nesne gelirdi. Yani hata vermeden boş bir ürün eklenirdi, bu daha da kötü. Bende ayrıca `isValid` kontrolü olduğu için 400 dönerdi. `@RequestBody`, "bu parametreyi gövdedeki JSON'dan oluştur" demek.

**2. `ResponseEntity` neden kullanılır? Nesneyi doğrudan döndürmekten farkı ne?**
Nesneyi doğrudan döndürünce Spring her zaman **200 OK** gönderiyor. `ResponseEntity` ile cevabın tamamını ben belirliyorum: durum kodu, gövde ve gerekirse header'lar. Böylece oluşturmada 201, silmede 204, bulunamayınca 404 dönebiliyorum. İstemci gövdeyi okumadan sadece koda bakıp ne olduğunu anlayabiliyor.

**3. HTTP 200, 201, 204 ve 404 ne anlama gelir?**
- **200 OK**: istek başarılı, cevapta veri var (GET, PUT).
- **201 Created**: yeni bir kaynak oluşturuldu (POST). Cevapta oluşan kayıt var.
- **204 No Content**: başarılı ama döndürülecek bir şey yok (DELETE).
- **404 Not Found**: istenen kaynak yok (olmayan ID).

**4. Postman'de POST isteği için Body → raw → JSON nasıl ayarlanır?**
Metodu **POST** seçip adrese `http://localhost:8080/api/products` yazdım. Adresin altındaki **Body** sekmesine geçip **raw**'ı seçtim, sağdaki açılır menüden **Text** yerine **JSON** seçtim. JSON seçince Postman `Content-Type: application/json` header'ını kendisi ekliyor; bu olmazsa Spring **415 Unsupported Media Type** döndürüyor. Sonra ürün JSON'unu yazıp **Send**'e bastım, cevap 201 ile geldi.

**5. `removeIf()` nasıl çalışır? Lambda nedir?**
`removeIf` listedeki her elemanı verdiğim koşulla deniyor ve koşulu sağlayanların hepsini siliyor. En az bir eleman silindiyse `true`, hiç silinmediyse `false` döndürüyor. Ben bunu 204 mü 404 mü döneceğime karar vermek için kullandım.
`p -> p.getId().equals(id)` bir lambda: isimsiz, kısa bir fonksiyon. Solda parametre (`p`), okun sağında ne döndürdüğü var. Uzun hali şöyle olurdu:
```java
for (Iterator<Product> it = productList.iterator(); it.hasNext(); ) {
    if (it.next().getId().equals(id)) it.remove();
}
```
Ayrıca `for-each` içinde `list.remove()` çağırmak `ConcurrentModificationException` verir; `removeIf` bunu güvenli şekilde yapıyor.

**6. PUT ile PATCH farkı nedir?**
PUT kaynağın **tamamını** gönderilen yeni haliyle değiştiriyor. Bir alanı göndermezsem o alan boşalmış sayılır. Bu yüzden benim PUT'umda isim, fiyat ve kategoriyi hep birlikte istiyorum. PATCH sadece **değişen alanları** gönderip kısmi güncelleme yapmak için: `{"price": 400}` sadece fiyatı değiştirir, diğerleri aynı kalır. Ayrıca PUT idempotent: aynı isteği kaç kez gönderirsem göndereyim sonuç aynı.

## Notlar
- Veriler bellekteki listede olduğu için sunucu yeniden başlayınca eklediklerim ve sildiklerim kayboluyor.
- `@RequestBody` JSON → Java (deserialization), controller'dan nesne döndürmek Java → JSON (serialization). İkisini de Jackson yapıyor.
- `Product`'a boş constructor ekledim çünkü Jackson önce boş nesne oluşturup alanları setter'larla dolduruyor.
