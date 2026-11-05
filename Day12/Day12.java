import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Day12 {
    public static void main(String[] args) {

        // ===== Görev 1: String ArrayList =====
        System.out.println("===== Görev 1: Ürün Listesi =====");
        List<String> products = new ArrayList<>();
        products.add("Elma");
        products.add("Muz");
        products.add("Portakal");
        products.add("Çilek");

        System.out.println("Ürünler : " + products);
        System.out.println("Boyut   : " + products.size());

        products.remove("Muz");          // değere göre sil
        products.set(0, "Karpuz");       // 0. indeksi güncelle
        products.add(1, "Kiraz");        // 1. indekse araya ekle
        System.out.println("Güncel  : " + products);
        System.out.println("İlk ürün: " + products.get(0));
        System.out.println("Çilek var mı? " + products.contains("Çilek"));

        Collections.reverse(products);
        System.out.println("Ters    : " + products);
        Collections.shuffle(products);
        System.out.println("Karışık : " + products);

        // ===== Görev 2: Integer ArrayList =====
        System.out.println("\n===== Görev 2: Fiyatlar =====");
        List<Integer> prices = new ArrayList<>();
        prices.add(25);
        prices.add(10);
        prices.add(80);
        prices.add(45);
        prices.add(5);

        int total = 0;
        for (int p : prices) {           // auto-unboxing: Integer → int
            total += p;
        }
        System.out.println("Fiyatlar : " + prices);
        System.out.println("Toplam   : " + total);
        System.out.println("Min/Max  : " + Collections.min(prices) + " / " + Collections.max(prices));

        Collections.sort(prices);
        System.out.println("Sıralı   : " + prices);
        System.out.println("Min: " + prices.get(0) + " | Max: " + prices.get(prices.size() - 1));

        // remove(int index) vs remove(Object o) farkı
        prices.remove(0);                       // 0. indeksteki elemanı siler (5)
        prices.remove(Integer.valueOf(80));     // değeri 80 olanı siler
        System.out.println("Silme sonrası: " + prices);

        // ===== Görev 3: Nesne Listesi =====
        System.out.println("\n===== Görev 3: Katalog =====");
        List<Product> catalog = new ArrayList<>();
        catalog.add(new Product("Laptop", 15000, 5));
        catalog.add(new Product("Fare", 250, 0));
        catalog.add(new Product("Klavye", 800, 12));
        catalog.add(new Product("Monitör", 8500, 0));
        catalog.add(new Product("Kulaklık", 1200, 3));

        System.out.println("Tüm ürünler:");
        for (Product p : catalog) {
            System.out.println("  " + p);
        }

        System.out.println("\nStokta olmayan ürünler:");
        for (Product p : catalog) {
            if (p.getStock() == 0) {
                System.out.println("  " + p.getName());
            }
        }

        double stockValue = 0;
        for (Product p : catalog) {
            stockValue += p.getPrice() * p.getStock();
        }
        System.out.printf("%nToplam stok değeri: %.2f TL%n", stockValue);
    }
}
