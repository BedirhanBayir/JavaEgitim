# Day 9 – Inheritance & super

`Employee` ana sınıfı (employeeId, firstName, lastName + `getFullName()` ve `printInfo()`) ve ondan türeyen iki sınıf: `FullTimeEmployee` (aylık maaş, `calculateAnnualSalary()`) ve `Intern` (günlük harcırah × gün, `calculateTotalAllowance()`). `Day9` her tipten birer nesne oluşturup bilgilerini yazdırıyor.

Alt sınıflardaki `printInfo()` önce `super.printInfo()` ile ID ve ad soyadı yazdırıyor, sonra kendi bilgilerini ekliyor. Böylece ortak kısım tek yerde kalıyor. Main'in sonunda `intern.getFullName()` ile miras alınan metodu da çağırdım.

## Sorular

**1. Kalıtım nedir, ne problemi çözer?**
Bir sınıfın başka bir sınıfın alan ve metotlarını devralmasıdır. Ortak özellikleri (id, ad, soyad, `getFullName()`) bir kez `Employee`'da yazıp tüm çalışan türlerinde kullanıyorum. Kod tekrarı azalıyor, ortak bir değişikliği tek yerden yapıyorum.

**2. `extends` ne işe yarar?**
Sınıfın hangi sınıftan türediğini belirtir. `Intern extends Employee` "stajyer bir çalışandır" demek. `Intern`, `Employee`'nın public metotlarını miras alıyor.

**3. `super(...)` neden constructor'ın ilk satırında olmak zorunda?**
Alt sınıf nesnesi oluşurken önce üst sınıf kısmının hazırlanması gerekiyor. Alt sınıf, `employeeId` gibi üst sınıftan gelen alanların zaten atanmış olduğunu varsayabilmeli. Bu yüzden Java üst sınıf constructor'ının her şeyden önce çalışmasını zorunlu tutuyor.

**4. `@Override` ne demek, zorunlu mu?**
Metodun üst sınıftaki bir metodu ezdiğini belirtir. Zorunlu değil ama yazmak iyi. Metot adını yanlış yazarsam (`printinfo()` gibi) derleyici hata veriyor. Yazmasaydım yeni bir metot tanımlanmış olurdu ve hata sessizce kaçardı.

**5. Alt sınıf üst sınıfın `private` alanlarına erişebilir mi?**
Hayır. `private` sadece tanımlandığı sınıfa açık, kalıtım bunu değiştirmiyor. Alan alt sınıf nesnesinin içinde var ama `Intern` ona sadece `getEmployeeId()` veya `super.printInfo()` gibi public metotlarla ulaşabiliyor. Kapsülleme kalıtımda da korunuyor.

**6. Java'da çoklu kalıtım neden yok?**
"Elmas problemi" yüzünden. Bir sınıf hem A'dan hem B'den türeseydi ve ikisinde de aynı isimli metot olsaydı, hangisinin çalışacağı belirsiz olurdu. Java bunu önlemek için sınıflarda tek kalıtıma izin veriyor. Birden fazla yetenek kazandırmak için interface kullanılıyor.
