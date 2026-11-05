# Day 10 – Polymorphism & Method Overriding

Day 9'daki `Employee`, `FullTimeEmployee` ve `Intern` sınıflarına yeni bir `PartTimeEmployee` (saat ücreti × çalışılan saat) ekledim. `Day10` farklı türdeki çalışanları tek bir `Employee[]` dizisinde tutup döngüyle `printInfo()` çağırıyor, `instanceof` ile tipe özel metotları kullanıyor.

Ek olarak döngüde tüm çalışanların bu ayki toplam maliyetini hesapladım ve overloading farkını görmek için iki tane `describe()` metodu yazdım (biri sadece isim, diğeri isim + ID).

## Sorular

**1. Polimorfizm nedir? Gerçek hayattan bir analoji.**
Aynı metot çağrısının nesnenin türüne göre farklı davranmasıdır. Orkestra şefi "çal!" dediğinde keman keman sesi, davul davul sesi çıkarır. Şef her enstrümanı tek tek tanımak zorunda değil, hepsine aynı komutu veriyor. Benim döngüm de her çalışana sadece `printInfo()` diyor.

**2. `Employee emp = new FullTimeEmployee(...)` geçerli mi?**
Geçerli, çünkü `FullTimeEmployee` bir `Employee`'dır. Alt sınıf nesnesi üst sınıf tipindeki referansa atanabilir. Ama `emp` üzerinden sadece `Employee`'da olan metotları çağırabiliyorum, `calculateAnnualSalary()` için tip dönüşümü gerekiyor.

**3. `emp.printInfo()` çağrılınca hangi metot çalışır?**
Referansın tipine değil, nesnenin gerçek tipine bakılır. Nesne `PartTimeEmployee` ise onun `printInfo()`'su çalışır. Bu karar çalışma zamanında JVM tarafından veriliyor, buna dynamic dispatch deniyor.

**4. Overriding ile overloading farkı nedir?**
Overriding: alt sınıfta, üst sınıftaki metodu aynı isim ve aynı parametrelerle yeniden yazmak. Hangisinin çalışacağına çalışma zamanında karar verilir (`printInfo()`). Overloading: aynı sınıfta aynı isimli ama farklı parametreli metotlar yazmak. Hangisinin çalışacağına derleme zamanında karar verilir (`describe(Employee)` ve `describe(Employee, boolean)`).

**5. `instanceof` neden gerekli oldu?**
`Employee` referansıyla sadece `Employee`'nın metotlarını görebiliyorum. `calculateAnnualSalary()` gibi alt sınıfa özel metotlar için önce gerçek tipi kontrol etmem gerekiyor. Kontrol etmeden `(FullTimeEmployee) emp` yazsam ve nesne aslında `Intern` olsa `ClassCastException` alırdım. `if (emp instanceof FullTimeEmployee fte)` hem kontrol ediyor hem dönüştürüyor.

**6. Polimorfizmin tasarıma faydası ne?**
Yeni bir çalışan tipi eklediğimde rapor döngüsünü değiştirmem gerekmiyor, sadece yeni sınıfı yazıyorum (Open/Closed Principle). Kod daha esnek ve bakımı kolay oluyor. Ama çok fazla `instanceof` yazmak da iyi değil; bunun yerine `Employee`'ya ortak bir `calculateMonthlyCost()` koyup her alt sınıfta override etmek daha temiz olurdu.
