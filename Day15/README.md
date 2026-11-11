# Day 15 – Web Temelleri (HTTP, JSON) & İlk Spring Boot Projesi

start.spring.io'dan oluşturduğum ilk Spring Boot projesi (`demo`): Maven, Java 17, Spring Boot 3, Group `com.egitim`, tek bağımlılık Spring Web. `HelloController` içinde iki endpoint var: `GET /hello` "Merhaba, Spring Boot!" döndürüyor, `GET /status` sunucunun çalıştığını ve sürümü yazıyor.

Ek olarak JSON'u kendi gözümle görmek için bir de `GET /person` yazdım. Bir `Map` döndürüyor ve Spring bunu otomatik JSON'a çeviriyor.

## Çalıştırma
`DemoApplication.java`'yı IntelliJ'den çalıştır (ya da `demo` klasöründe `mvn spring-boot:run`). Sonra tarayıcıda:
- http://localhost:8080/hello
- http://localhost:8080/status
- http://localhost:8080/person

## HTTP Kavramları

| Kavram | Anlamı |
|---|---|
| GET | Veri okumak |
| POST | Yeni veri oluşturmak |
| PUT | Var olan veriyi güncellemek |
| DELETE | Veri silmek |
| 200 OK | İstek başarılı |
| 201 Created | Yeni kayıt oluşturuldu |
| 400 Bad Request | İstek hatalı (eksik/yanlış veri) |
| 404 Not Found | İstenen kaynak yok |
| 500 Internal Server Error | Sunucu tarafında hata oluştu |

## Sorular

**1. HTTP nedir? Client-server mimarisi nedir?**
HTTP, istemci ile sunucunun web üzerinden konuşmak için kullandığı protokol. İstemci (tarayıcı, Postman, mobil uygulama) bir istek gönderiyor, sunucu bunu işleyip bir cevap dönüyor. Client-server mimarisinde iş bölünmüş: istemci isteği yapan ve sonucu gösteren taraf, sunucu veriyi ve iş mantığını tutan taraf. Bu projede tarayıcım istemci, Spring Boot uygulamam sunucu.

**2. GET ile POST arasındaki temel fark nedir?**
GET veri okumak için. Parametreler URL'de gidiyor, gövdesi (body) yok ve sunucudaki veriyi değiştirmemeli. POST yeni veri oluşturmak için. Veri isteğin gövdesinde gidiyor ve sunucuda bir şey değişiyor. Aynı GET'i tekrar göndermek sonucu değiştirmez, aynı POST'u tekrar göndermek iki kayıt oluşturabilir.

**3. JSON nedir? Bir kişi örneği yaz.**
JSON, veriyi anahtar-değer çiftleriyle yazan, hem insanın hem programın kolayca okuyabildiği bir metin formatı. API'lerde istemci ile sunucu arasında veri böyle taşınıyor.
```json
{
  "ad": "Bedirhan",
  "soyad": "Bayır",
  "yas": 21,
  "ogrenciMi": true,
  "diller": ["Java", "SQL"]
}
```
`/person` endpoint'i de buna benzer bir JSON döndürüyor.

**4. `pom.xml` ne işe yarar?**
Maven'ın proje dosyası. Projenin adını, Java sürümünü ve hangi kütüphanelere (dependency) ihtiyaç duyduğunu yazıyor. `spring-boot-starter-web` yazınca Maven Spring MVC'yi, Jackson'ı ve gömülü Tomcat'i kendisi indiriyor. Ayrıca `spring-boot-starter-parent` sayesinde kütüphane sürümlerini tek tek yazmam gerekmiyor, uyumlu sürümleri parent belirliyor.

**5. `@SpringBootApplication` ne anlama gelir?**
Üç anotasyonun birleşimi:
- `@Configuration`: bu sınıf Spring ayarları içerebilir.
- `@EnableAutoConfiguration`: classpath'teki kütüphanelere bakıp gerekeni otomatik ayarla (Spring Web varsa Tomcat'i başlat gibi).
- `@ComponentScan`: bu paketi ve alt paketlerini tara, `@RestController` gibi sınıfları bul.
`HelloController`'ı hiçbir yere kaydetmeden çalışmasının sebebi bu tarama.

**6. `server.port`'u 9090 yapınca ne değişti?**
Uygulama artık 8080 yerine 9090 portunda dinliyor. Adres `http://localhost:9090/hello` oldu; 8080'e gidince bağlantı kurulamıyor. Port, aynı bilgisayarda çalışan farklı uygulamaları ayırt etmeye yarıyor. Mesela 8080'i başka bir program kullanıyorsa portu değiştirmek gerekiyor.

**7. Tarayıcıdan GET gönderilebiliyor ama POST neden gönderilemiyor?**
Adres çubuğuna URL yazıp Enter'a basınca tarayıcı her zaman GET isteği atıyor. POST için isteğin gövdesine veri koymak gerekiyor ve adres çubuğunun böyle bir alanı yok. POST ancak bir HTML formu, JavaScript kodu ya da Postman/curl gibi bir araçla gönderilebiliyor. Bu yüzden sonraki günlerde Postman kullanacağım.

## Notlar
- `@RestController` = `@Controller` + `@ResponseBody`. Metodun döndürdüğü değer HTML sayfası olarak değil, doğrudan cevabın gövdesi olarak gidiyor.
- `spring-boot-starter-web` içinde gömülü Tomcat var, ayrıca sunucu kurmama gerek kalmadı.
- İlk açılış yavaş olabilir, çünkü Maven bağımlılıkları indiriyor.
