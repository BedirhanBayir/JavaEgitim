# Day 1 – Workspace Setup, Variables & Data Types

Bir vaka kaydının bilgilerini (vaka no, şüpheli adı, yaş, kilo, tutuklu mu, cinsiyet) uygun veri tipleriyle değişkenlerde tutup ekrana yazdıran ilk Java programı.

## Sorular

**1. `int` ile `double` farkı nedir?**
`int` tam sayı tutar (34, 1001). `double` ondalıklı sayı tutar (78.5). Yaş, adet, ID gibi kesirli olamayacak değerler için `int`, kilo, fiyat, ortalama gibi kesirli olabilecek değerler için `double` kullanırım.

**2. `boolean` hangi değerleri alır, nerede kullanılır?**
Sadece `true` veya `false`. Evet/hayır durumlarında kullanılır: şüpheli tutuklu mu (`isArrested`), kullanıcı aktif mi, ödeme yapıldı mı gibi.

**3. `char` ile `String` farkı nedir?**
`char` tek bir karakter tutar ve tek tırnakla yazılır (`'M'`). Primitive bir tiptir. `String` karakter dizisidir, çift tırnakla yazılır (`"Ali Demir"`) ve bir nesnedir (referans tipi). Bu yüzden `String` büyük harfle başlar.

**4. camelCase nedir, neden önemli?**
İlk kelime küçük harfle başlar, sonraki her kelimenin baş harfi büyük yazılır: `suspectName`, `isArrested`. Java'da standart isimlendirme kuralı budur. Kodu okunur yapar, herkes aynı stilde yazınca kodu anlamak kolaylaşır.

**5. `println()` ile `print()` farkı nedir?**
`println()` yazdıktan sonra alt satıra geçer. `print()` aynı satırda kalır, bir sonraki çıktı yanına yazılır.

**6. Programı IntelliJ'de nasıl çalıştırdım?**
1. IntelliJ'de yeni bir Java projesi açtım, JDK 17'yi seçtim.
2. `Day1.java` dosyasını oluşturdum, sınıf adını dosya adıyla aynı (`Day1`) yaptım.
3. `main` metodunun yanındaki ▶️ ikonuna tıkladım (veya `Shift + F10`).
4. Çıktı alttaki Run penceresinde göründü.

## Çıktı
```
Case ID    : 1001
Suspect    : Ali Demir
Age        : 34
Weight     : 78.5 kg
Arrested   : false
Gender     : M
```
