# Day 3 – Loops (For, While, Do-While) & Debugging

- **Görev 1:** `for` ile 1–20 arası sayılar, yanlarında `[ÇİFT]` / `[TEK]` etiketi
- **Görev 2:** `while` ile 0 girilene kadar sayı alıp toplama (0 toplama dahil edilmez)
- **Görev 3:** `do-while` ile menü (1-Listele, 2-Ara, 3-Çıkış), geçersiz seçimde tekrar sorar
- **Ek:** `continue` ile 5. ürünü atlayan döngü

## Debugging Gözlemi (Görev 4)
`if (i % 2 == 0)` satırına breakpoint koyup `Shift + F9` ile debug modunda başlattım. Her `F8` (Step Over) basışında Variables penceresinde `i` değerinin 1, 2, 3… diye arttığını gördüm. `i` çiftken program `if` bloğuna, tekken `else` bloğuna giriyor. `i` 21 olunca `i <= 20` koşulu yanlış oldu ve döngüden çıkıldı.

## Sorular

**1. `for`, `while`, `do-while` farkı nedir?**
`for`: kaç kez döneceği belli olduğunda (1'den 20'ye). `while`: tekrar sayısı belli değilse ve koşul baştan kontrol edilecekse (0 girilene kadar). `do-while`: gövde en az bir kez çalışmalıysa (menü önce gösterilir, sonra koşula bakılır).

**2. `break` ve `continue` farkı nedir?**
`break` döngüyü tamamen bitirir. `continue` sadece o turu atlayıp bir sonraki tura geçer. Örnek: `if (productNo == 5) continue;` → 5. ürün yazılmaz, 6'dan devam eder. Orada `break` olsaydı 5'te döngü dururdu.

**3. Sonsuz döngü nedir, nasıl önlenir?**
Çıkış koşulu hiçbir zaman sağlanmayan döngüdür. Örneğin `while (i <= 20)` içinde `i`'yi artırmayı unutmak. Önlemek için döngüyü yazarken önce çıkış koşulunu düşünürüm ve koşuldaki değişkenin döngü içinde değiştiğinden emin olurum.

**4. Step Over ile Step Into farkı nedir?**
Step Over (`F8`) satırı tek adımda çalıştırır. Satırda bir metot çağrısı varsa metodun içine girmez. Step Into (`F7`) ise çağrılan metodun içine girip orayı da satır satır gösterir.

**5. 0 neden toplama dahil edilmemeli?**
0 bir sayı değil, "dur" sinyali. Toplama eklemek sonucu değiştirmez ama mantık olarak yanlış. Örnekteki iskelet `do-while` kullanıyordu ve 0'ı da ekliyordu. Ben `while` kullanarak önce sayıyı okudum, 0 değilse topladım. Böylece 0 hiç toplanmıyor.

**6. do-while ile while farkı nedir?**
`while` koşulu baştan kontrol eder, koşul ilk anda yanlışsa gövde hiç çalışmaz. `do-while` koşulu sonda kontrol eder, gövde en az bir kez mutlaka çalışır.
