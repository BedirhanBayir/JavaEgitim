# Day 11 – Abstract Classes vs Interfaces

Abstract `Animal` sınıfı (name, abstract `makeSound()`, herkeste aynı olan `breathe()`), `Flyable` ve `Swimmable` interface'leri ve üç hayvan: `Dog` (yüzer), `Eagle` (uçar), `Duck` (hem uçar hem yüzer). `Day11` hepsini bir `Animal[]` dizisinde tutup seslerini çıkarıyor, sonra `instanceof` ile yeteneklerine göre `fly()` ve `swim()` çağırıyor.

`Swimmable`'a Java 8'deki `default` metodu denemek için `dive()` ekledim; `Dog` ve `Duck` bunu override etmeden kullanabiliyor. Sonda bir de sadece `Flyable[]` tipinde bir dizi yaptım, interface'in de referans tipi olabildiğini görmek için.

## Sorular

**1. Abstract class ile normal class farkı nedir?**
Abstract class'tan doğrudan nesne oluşturulamaz ve gövdesi olmayan abstract metotlar içerebilir. Bu metotları alt sınıflar doldurmak zorunda. Normal sınıfın tüm metotlarının gövdesi var ve `new` ile oluşturulabilir.

**2. Interface ile abstract class farkları nelerdir?**
- Bir sınıf sadece bir sınıfı `extends` edebilir ama birden fazla interface'i `implements` edebilir.
- Abstract class'ın constructor'ı ve alanları olabilir. Interface'te constructor yok, alanlar otomatik `public static final` (sabit).
- Abstract class "ne olduğunu" (Animal), interface "ne yapabildiğini" (Flyable) anlatıyor.

**3. Neden `new Animal()` yazılamaz?**
`Animal` eksik bir şablon, `makeSound()`'un gövdesi yok. Nesne oluşturulabilseydi `makeSound()` çağrılınca çalışacak kod olmazdı. Derleyici bunu engelliyor. Zaten "sadece hayvan" diye bir şey yok, her zaman belirli bir hayvan var.

**4. Neden birden fazla interface uygulanabiliyor ama tek sınıf extend ediliyor?**
Sınıflar alan ve metot gövdesi taşıyor; iki üst sınıftan aynı isimli şey gelirse hangisinin kullanılacağı belirsiz olur. Interface ise sadece sözleşme veriyor, metodun içini sınıf kendisi yazıyor, o yüzden çakışma çıkmıyor. `Duck` hem `Flyable` hem `Swimmable` olabiliyor.

**5. Interface metotları neden varsayılan olarak `public abstract`?**
Interface bir sözleşme: "beni uygulayan herkes bu metotları sunar" diyor. Dışarıdan kullanılabilmesi için `public`, içini sınıf yazdığı için `abstract`. Java bunları ben yazmasam da otomatik ekliyor.

**6. `default` metot nedir? Java 8 ne getirdi?**
Java 8 ile interface'e gövdeli metot yazılabiliyor: `default void dive() {...}`. Böylece bir interface'e yeni metot eklenince onu uygulayan bütün sınıflar bozulmuyor, varsayılan davranışı kullanıyorlar. Java'nın kendisi `List.sort()` ve `forEach()`'i bu şekilde ekledi. Java 8 ayrıca interface'lere `static` metot da getirdi.
