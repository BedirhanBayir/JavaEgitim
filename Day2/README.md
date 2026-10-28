# Day 2 – Control Flow (If-Else, Switch-Case) & Scanner

Kullanıcıdan öğrenci adı, not ve bölüm alıp `if-else if` ile harf notunu, `switch-case` ile bölüme göre mesajı yazdıran program. 0–100 dışındaki puanlarda "Geçersiz puan girdiniz." uyarısı verir.

## Sorular

**1. `if-else` ile `switch-case` farkı nedir?**
`if-else` her türlü koşulu değerlendirebilir: aralıklar (`grade >= 90`), birden fazla şart (`&&`, `||`). `switch` ise bir değişkeni belirli sabit değerlerle karşılaştırır. Not aralıkları için `if-else`, bölüm adı gibi belli seçenekler için `switch` kullandım.

**2. `&&` ve `||` farkı nedir?**
`&&` (VE): iki koşul da doğruysa sonuç doğrudur. `||` (VEYA): en az biri doğruysa sonuç doğrudur.
Örnek: `grade >= 0 && grade <= 100` → not geçerli aralıkta. `grade < 0 || grade > 100` → not geçersiz.

**3. `nextInt()`'ten sonra neden `nextLine()` çağrılır?**
`nextInt()` sadece sayıyı okur, Enter'a basınca oluşan satır sonu karakterini (`\n`) buffer'da bırakır. Sonraki `nextLine()` bu boş satırı okur ve bölüm adını hiç sormadan geçer. Araya bir `nextLine()` koyarak o karakteri temizliyorum.

**4. Puan -5 girilince ne oldu?**
"Geçersiz puan girdiniz." yazdı ve program bitti. Harf notunu hesaplamadan önce `grade < 0 || grade > 100` kontrolü yapıyorum. Bu koşul doğru olduğu için uyarı verip `return` ile çıkıyor. Böylece 150 gibi değerler de yakalanıyor.

**5. `break` kaldırılırsa ne olur?**
"Fall-through" olur: eşleşen `case` çalıştıktan sonra alttaki `case`'ler de çalışır. Mesela "Bilgisayar" girilirse hem Bilgisayar, hem Elektrik, hem Makine mesajları, hatta `default` mesajı da yazılır.

**6. `default` ne zaman çalışır?**
Hiçbir `case` değeri eşleşmediğinde. Örneğin "Kimya" yazılırsa "Bölüm bilgisi tanımlı değil." mesajı çıkar.

## Örnek Çalıştırma
```
Öğrenci adı: Ali
Not (0-100): 85
Bölüm: Bilgisayar
Ali - Harf Notu: BA
Yazılım & donanım odaklı müfredat.
```
