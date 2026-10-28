# Day 7 – Constructors & this Keyword

`Student` sınıfı (name, studentId, gpa, isActive), üç constructor (parametresiz, tam parametreli, kısmi), `printInfo()` ve `isHonorStudent()` (gpa ≥ 3.5). `Day7` her constructor ile birer öğrenci oluşturur.

Kısmi constructor'da alanları tek tek atamak yerine `this(name, studentId, 0.0, true)` ile tam parametreli constructor'ı çağırdım. Böylece atama kodu tek yerde kalıyor.

## Sorular

**1. Constructor nedir, normal metottan farkı ne?**
Nesne oluşturulurken (`new` ile) otomatik çalışan, nesneye ilk değerlerini veren özel bir metottur. Farkları: adı sınıf adıyla aynıdır, dönüş tipi yoktur (`void` bile yazılmaz) ve elle çağrılmaz, `new` ile çalışır.

**2. `this.name = name` satırında `this` neden gerekli?**
Parametrenin adı alanın adıyla aynı. `this` olmadan `name = name` yazarsam parametre kendine atanır, alan boş (`null`) kalır. `this.name` "bu nesnenin name alanı" demektir. Sağdaki `name` ise parametredir.

**3. Constructor yazmazsam ne olur?**
Java otomatik olarak parametresiz, boş bir varsayılan constructor ekler. Ama ben herhangi bir constructor yazarsam bu otomatik constructor eklenmez. Parametresizine de ihtiyacım varsa onu da kendim yazmalıyım.

**4. Constructor overloading nedir, avantajı nedir?**
Aynı sınıfta parametre listesi farklı birden fazla constructor tanımlamaktır. Avantajı esnekliktir: elimde tüm bilgiler varsa tam parametreliyi, sadece isim ve numara varsa kısmiyi kullanırım. Gerisi varsayılan değerlerle dolar.

**5. `private` alanlar neden constructor içinden atanabiliyor?**
`private` "sınıf dışından erişilemez" demektir. Constructor ise sınıfın kendi içinde olduğu için tüm `private` alanlara erişebilir. `Day7` içinden `s2.name = "..."` yazsaydım hata alırdım.

**6. `new Student()` çalışınca bellekte ne olur?**
1. Heap'te yeni bir `Student` nesnesi için yer ayrılır.
2. Alanlar varsayılan değerleri alır: `name = null`, `studentId = null`, `gpa = 0.0`, `isActive = false`.
3. Constructor çalışır ve `isActive = true` yapar.
4. Nesnenin adresi stack'teki `s1` değişkenine atanır.

Bu yüzden `s1.printInfo()` isim ve numarayı `null` olarak gösteriyor.
