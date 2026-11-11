# Day 20 – DTO (Data Transfer Object) Pattern

Day 19'daki projeye bir kullanıcı kayıt sistemi ekledim. `User` sınıfı şifre dahil her şeyi tutuyor, ama API dışarıya `User`'ı hiç göstermiyor. İçeri gelen ve dışarı giden veri için iki ayrı DTO var:

```
model/User.java               ← id, username, email, password, role
dto/UserCreateRequest.java    ← YENİ: username, email, password (istemci gönderir)
dto/UserResponse.java         ← YENİ: id, username, email, role (istemciye döner, password YOK)
service/UserService.java      ← liste, id sayacı, toResponse()
controller/UserController.java
```

| Endpoint | Body | Cevap |
|---|---|---|
| `POST /api/users` | `UserCreateRequest` | **201** + `UserResponse` |
| `GET /api/users` | — | `UserResponse` listesi |
| `GET /api/users/{id}` | — | **200** + `UserResponse`, yoksa **404** (ek) |

Programdakine ek olarak:
- Kullanıcı adı boşsa, e-postada `@` yoksa ya da şifre 6 karakterden kısaysa **400** dönüyor.
- Aynı kullanıcı adı veya e-posta zaten kayıtlıysa **409 Conflict** dönüyor.
- Ürün API'si (`/api/products`) Day 19'daki gibi duruyor.
- Ürün ve kullanıcı istekleri `Day20.postman_collection.json` dosyasında.

Örnek istek ve cevap:
```json
// POST /api/users
{ "username": "bedirhan", "email": "bedirhan@ornek.com", "password": "gizli123" }

// 201 Created
{ "id": 1, "username": "bedirhan", "email": "bedirhan@ornek.com", "role": "USER" }
```

## Sorular

**1. DTO nedir? Entity'yi doğrudan döndürmek yerine neden kullanılır?**
DTO, katmanlar arasında, özellikle API'nin içine ve dışına, veri taşımak için yazılan sade bir sınıf. Entity ise sistemin içindeki gerçek veri modeli. Entity'yi doğrudan döndürmenin sorunları:
- Şifre gibi gizli alanlar dışarı sızar.
- Entity'ye eklediğim her yeni alan otomatik olarak API'ye de eklenir, kontrol bende olmaz.
- Veritabanı yapısını değiştirince API'yi kullanan herkes etkilenir.
DTO ile API'nin "sözleşmesini" ayrı tutuyorum. İçeride ne değişirse değişsin, dışarıya neyi göstereceğime ben karar veriyorum.

**2. Request ve Response için neden ayrı DTO'lar var?**
İçeri gelen ile dışarı giden veri aynı değil. Kayıt olurken istemci şifre gönderiyor ama `id` ve `role` göndermemeli; bunları sunucu belirliyor. Tek bir DTO kullansaydım istemci JSON'a `"role": "ADMIN"` yazıp kendini yönetici yapmaya çalışabilirdi. Cevapta ise `id` ve `role` var ama şifre yok. Ayrı DTO'larla her yönde sadece doğru alanlar geçebiliyor.

**3. `password` neden Response DTO'da yok?**
Şifre, sadece sunucunun bilmesi gereken bir bilgi. API cevabında olsaydı tarayıcıda, Postman'de, loglarda ve aradaki her sistemde görünürdü. `UserResponse`'ta `password` alanı hiç olmadığı için yanlışlıkla bile JSON'a giremiyor. (Gerçek bir sistemde şifre düz metin olarak da saklanmaz, BCrypt gibi bir algoritmayla hash'lenir. Bu sonraki konular.)

**4. `toResponse()` service'te mi yoksa ayrı bir mapper sınıfında mı olmalı? MapStruct.**
Küçük bir projede service içinde private bir metot yeterli. Ama DTO ve entity sayısı artınca dönüşümler service'i kalabalıklaştırıyor ve aynı dönüşüm birden fazla service'te gerekebiliyor. O yüzden büyük projelerde ayrı bir `UserMapper` sınıfı yazılıyor. MapStruct bu mapper'ı derleme sırasında otomatik üreten bir kütüphane: bir interface'e `UserResponse toResponse(User user);` yazıyorum, aynı isimli alanları eşleyen kodu kendisi oluşturuyor. Reflection kullanmadığı için hızlı ve hataları derleme anında gösteriyor. ModelMapper da benzer iş yapıyor ama çalışma zamanında reflection ile.

**5. `stream().map(this::toResponse).collect(...)` satırını açıkla.**
```java
users.stream()                     // listeyi bir akışa çevir
     .map(this::toResponse)        // her User'ı UserResponse'a dönüştür
     .collect(Collectors.toList()); // sonuçları yeni bir listeye topla
```
`this::toResponse` bir method reference: `u -> this.toResponse(u)` lambdasının kısa hali. Sonuçta `List<User>`'dan `List<UserResponse>` elde ediliyor ve orijinal liste değişmiyor.

**6. Jackson'ın DTO'ları JSON'a çevirmek için neye ihtiyacı var?**
- **Serialization (Java → JSON, `UserResponse`)**: public getter'lar. Jackson `getUsername()`'i görüp JSON'a `"username"` yazıyor. Getter yoksa alan private olduğu için görünmüyor.
- **Deserialization (JSON → Java, `UserCreateRequest`)**: boş (parametresiz) bir constructor ve setter'lar. Jackson önce boş nesneyi oluşturuyor, sonra JSON'daki her alan için `setUsername(...)` gibi setter'ları çağırıyor. Bu yüzden `UserCreateRequest`'e boş constructor ve setter ekledim.
JSON'daki alan adları getter/setter adlarıyla eşleşmeli (`username` ↔ `getUsername`/`setUsername`).

## Notlar
- Kural: entity'leri API'den doğrudan döndürme. DTO katmanı güvenlik sağlıyor ve API'yi içeriden bağımsız geliştirmeye izin veriyor.
- Büyük projelerde DTO'lar `dto/request/` ve `dto/response/` diye ayrılıyor.
- Ürün API'si hâlâ `Product`'ı doğrudan döndürüyor; ileride ona da `ProductRequest` ve `ProductResponse` yazılabilir.
