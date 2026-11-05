# Day 8 – Encapsulation & Access Modifiers

`BankAccount` sınıfı (accountNumber, owner, balance – hepsi `private`), her alan için getter, `deposit()`, `withdraw()` ve `printStatement()`. `Day8` hesaba para yatırıp çekiyor, sonra yetersiz bakiye, negatif tutar ve sıfır tutar gibi geçersiz işlemleri deniyor.

`balance` için bilerek setter yazmadım. Bakiye sadece `deposit()` ve `withdraw()` ile değişiyor, böylece kurallar tek yerde kalıyor. Constructor'da negatif başlangıç bakiyesi gelirse 0 atanıyor, bunu da ikinci bir hesapla denedim.

## Sorular

**1. Encapsulation nedir, neden gerekli?**
Sınıfın verisini dışarıya kapatıp erişimi sadece sınıfın kendi metotlarına bırakmaktır. Bu sayede nesne hep geçerli bir durumda kalır. Dışarıdan biri bakiyeyi `-9999` yapamaz, her değişiklik kurallardan geçmek zorunda.

**2. `private` alanlar sınıf dışından neden erişilemez?**
`private` "sadece bu sınıfın içinden görülebilir" demektir ve bunu derleyici denetler. `Day8` içinde `account.balance = -9999` yazınca derleme hatası alıyorum. Veriyi değiştirme yetkisi, onu doğru yönetmeyi bilen sınıfta kalıyor.

**3. Getter ile setter neden ayrı yazılır?**
Okumak ve değiştirmek farklı yetkiler. Bir alanı okunabilir ama değiştirilemez yapmak isteyebilirim. Ayrı yazınca hangi alanın dışarıdan değişebileceğine tek tek karar veriyorum. `accountNumber` ve `owner` için sadece getter var.

**4. Setter yerine `deposit()` / `withdraw()` yazmanın avantajı nedir?**
`setBalance(x)` bakiyeyi herhangi bir değere ayarlar, kuralı dışarıdaki koda bırakır. `deposit()` ve `withdraw()` ise gerçek işlemleri temsil ediyor ve kuralları (negatif tutar olmaz, bakiyeden fazla çekilmez) kendi içinde uyguluyor. Nesneye bakiyeyi sorup hesabı dışarıda yapmıyorum, işlemi nesneye yaptırıyorum ("Tell, don't ask").

**5. `balance` `public` olsaydı ne sorunlar çıkardı?**
Herkes bakiyeyi negatif ya da saçma bir değere çekebilirdi. Doğrulama atlanırdı. Bakiyeyi hangi kodun değiştirdiğini takip etmek zorlaşırdı. İleride tipini (mesela `BigDecimal`) değiştirmek, onu kullanan her yeri bozardı.

**6. `protected` ne zaman kullanılır?**
`protected` üyeler aynı paketteki sınıflardan ve farklı pakette olsa bile alt sınıflardan erişilebilir. Kalıtımda alt sınıfların kullanması gereken ama herkese açılmaması gereken alan ve metotlar için kullanılır. Sıralama: `private` (sadece sınıf) < default (aynı paket) < `protected` (paket + alt sınıflar) < `public` (her yer).
