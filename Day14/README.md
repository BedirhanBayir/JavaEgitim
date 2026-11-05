# Day 14 – Exception Handling & OOP Mini Proje

Konsoldan çalışan bir öğrenci kayıt sistemi. `Student` (otomatik ID, doğrulamalı `setGrade()`, harf notu), öğrencileri bir `ArrayList`'te tutan `StudentService` ve iki özel hata: `InvalidGradeException` (not 0–100 dışında) ve `StudentNotFoundException` (ID bulunamadı). `Day14` `do-while` + `Scanner` ile menü gösteriyor: Ekle, Listele, Ara, Not Güncelle, Çıkış.

Programdaki örnek kodda menüye sayı yerine harf girince `nextInt()` hata verip program çöküyordu. Bunun yerine satırı String olarak okuyup `Integer.parseInt()` ile çeviren bir `readInt()` metodu yazdım; hatalı girişte mesaj verip tekrar soruyor. Boş isim için `IllegalArgumentException` fırlatıyorum, `Scanner`'ı da `finally` içinde kapatıyorum. Listede ortalama notu da gösteriyorum.

## Sorular

**1. `try-catch-finally` çalışma sırası nedir, `finally` ne zaman çalışır?**
Önce `try` çalışıyor. Hata olursa `try`'ın kalanı atlanıp uygun `catch` çalışıyor, hata yoksa `catch` atlanıyor. İki durumda da en son `finally` çalışıyor, `try` içinde `return` olsa bile. Bu yüzden Scanner, dosya, bağlantı gibi kaynakları kapatmak için ideal.

**2. `RuntimeException` ile `Exception` farkı nedir?**
`Exception` (RuntimeException hariç) checked: derleyici ya `try-catch` ile yakalamamı ya da `throws` ile bildirmemi zorunlu tutuyor. Genelde programın dışındaki hatalar için (dosya yok gibi). `RuntimeException` unchecked: derleyici zorlamıyor. Geçersiz veri ve programlama hataları için (`NullPointerException`, `NumberFormatException`, benim `InvalidGradeException`'ım).

**3. Custom exception neden kullanılır?**
Hatanın ne olduğunu isminden anlatıyor; `StudentNotFoundException` görünce sorunu hemen anlıyorum. Farklı hataları ayrı `catch` bloklarıyla farklı şekilde ele alabiliyorum ve mesaj tek yerde tanımlı. Spring Boot'ta da bu hatalar yakalanıp 404 gibi HTTP kodlarına çevriliyor.

**4. `static int counter` ne işe yarıyor, `static` neden kritik?**
Her öğrenciye benzersiz ID (`STU1001`, `STU1002`...) vermek için sayaç. `static` olduğu için nesneye değil sınıfa ait, bütün `Student` nesneleri aynı değişkeni paylaşıyor. `static` olmasaydı her nesnenin kendi sayacı olurdu ve hepsi `STU1001` alırdı. Notu geçersiz olan öğrenciye ID boşa gitmesin diye constructor'da önce `setGrade()`'i çağırıp sonra ID veriyorum.

**5. `throws` ne zaman kullanılır?**
Metodun içinde oluşabilecek checked bir hatayı orada yakalamayıp çağıran metoda bırakmak istediğimde imzaya yazıyorum: `void readFile(String path) throws IOException`. `throw` hata fırlatmak, `throws` "bu metot bunu fırlatabilir" demek.

**6. 2 haftada öğrendiğim hangi OOP kavramlarını kullandım?**
- Sınıf ve nesne: `Student`, `StudentService`
- Constructor ve `this`: `Student(String name, int grade)`
- Encapsulation: tüm alanlar `private`, `setGrade()` içinde doğrulama, ID için setter yok
- Inheritance ve `super`: özel hatalar `RuntimeException`'dan türüyor, `super(mesaj)` çağırıyor
- Polymorphism: `toString()` override, multi-catch ile farklı hata türleri
- Collections: `List<Student>`
- `static`: ID sayacı
- Önceki günler: `Scanner`, `switch`, `do-while`, `readInt()` gibi yardımcı metotlar
