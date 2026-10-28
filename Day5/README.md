# Day 5 – Methods, Parameters & Return Types

Hesap makinesi işlemleri (`add`, `subtract`, `multiply`, `divide`), `isEven`, `findMax`, `printSeparator` ve `greet` metotlarını yazıp hepsini `main` içinden çağıran program.

## Sorular

**1. `void` ne anlama gelir?**
Metodun bir değer döndürmediği anlamına gelir. Sadece bir iş yapar. `printSeparator()` ekrana çizgi basıyor ama geriye bir şey vermiyor, o yüzden `void`.

**2. `static` neden gerekli?**
`static` metot sınıfa aittir, bir nesneye değil. `main` de static olduğu için aynı sınıftaki static metotları nesne oluşturmadan doğrudan adıyla (`add(10, 5)`) çağırabiliyorum. Metotlar static olmasaydı önce `new Day5()` ile nesne oluşturmam gerekirdi.

**3. Method overloading nedir?**
Aynı isimli metodun farklı parametre listeleriyle birden fazla kez yazılmasıdır. Hangisinin çalışacağına Java argümanlara bakarak karar verir.
```java
static int add(int a, int b) { return a + b; }
static double add(double a, double b) { return a + b; }
static int add(int a, int b, int c) { return a + b + c; }
```

**4. Parametre ile argüman farkı nedir?**
Parametre, metot tanımındaki değişkendir (`double a, double b`). Argüman ise çağırırken gönderilen gerçek değerdir (`add(10, 5)` içindeki `10` ve `5`).

**5. Metoda dizi geçirilirse, içerideki değişiklik dışarıyı etkiler mi?**
Evet. Java değerleri kopyalayarak geçirir (pass by value) ama dizi bir nesnedir. Kopyalanan şey dizinin kendisi değil, dizinin adresidir (referans). Metot aynı diziyi gördüğü için `numbers[0] = 99` yaparsa dışarıdaki dizi de değişir. Ama metot içinde `numbers = new int[5]` yapmak dışarıyı etkilemez.

**6. `divide`'a sıfır geçirilince ne oldu?**
`b == 0` kontrolü yapıyorum. Sıfırsa "Hata: Sıfıra bölme!" yazdırıp `0` döndürüyorum, program çökmüyor. Not: `double`'da sıfıra bölme hata vermez, `Infinity` döner. Bu yüzden kontrol olmasa yanlış bir sonuç çıkardı. Daha doğru bir yol, ileride öğreneceğimiz exception fırlatmak olurdu.

## Çıktı
```
Toplama   : 15.0
Çıkarma   : 5.0
Çarpma    : 50.0
Bölme     : 2.5
Hata: Sıfıra bölme!
Bölme     : 0.0
5 çift mi?: false
8 çift mi?: true
Max       : 42
Merhaba, Zeynep!
```
