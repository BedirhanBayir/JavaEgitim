import java.util.*;

public class Day13 {
    public static void main(String[] args) {

        // ===== Görev 1: HashSet =====
        System.out.println("===== Görev 1: Kategoriler (Set) =====");
        Set<String> categories = new HashSet<>();
        categories.add("Elektronik");
        categories.add("Giyim");
        categories.add("Gıda");
        boolean added = categories.add("Elektronik"); // tekrar → eklenmez

        System.out.println("Kategoriler      : " + categories);
        System.out.println("Boyut            : " + categories.size()); // 3
        System.out.println("Tekrar eklendi mi? " + added);              // false
        System.out.println("Elektronik var mı? " + categories.contains("Elektronik"));
        System.out.println("Kitap var mı?      " + categories.contains("Kitap"));

        // Sıralı set karşılaştırması
        Set<String> sortedCategories = new TreeSet<>(categories);
        System.out.println("Alfabetik (TreeSet): " + sortedCategories);

        // ===== Görev 2: HashMap – Sepet =====
        System.out.println("\n===== Görev 2: Sepet (Map) =====");
        Map<String, Integer> cart = new HashMap<>();
        cart.put("Laptop", 1);
        cart.put("Kablo", 3);
        cart.put("Çanta", 2);
        cart.put("Mouse", 2);

        // Güncelle
        cart.put("Kablo", cart.getOrDefault("Kablo", 0) + 1); // 4
        cart.merge("Mouse", 1, Integer::sum);                  // 3 (alternatif yol)
        // Sil
        cart.remove("Çanta");

        System.out.println("Sepet:");
        for (Map.Entry<String, Integer> entry : cart.entrySet()) {
            System.out.println("  " + entry.getKey() + " → " + entry.getValue() + " adet");
        }

        // En fazla miktarlı ürün
        String maxItem = null;
        int maxCount = 0;
        for (Map.Entry<String, Integer> entry : cart.entrySet()) {
            if (entry.getValue() > maxCount) {
                maxCount = entry.getValue();
                maxItem = entry.getKey();
            }
        }
        System.out.println("En fazla: " + maxItem + " (" + maxCount + " adet)");

        // ===== Görev 3: Fiyat Kataloğu =====
        System.out.println("\n===== Görev 3: Fiyat Kataloğu =====");
        Map<String, Double> priceCatalog = new HashMap<>();
        priceCatalog.put("Laptop", 15000.0);
        priceCatalog.put("Mouse", 250.0);
        priceCatalog.put("Kablo", 80.0);
        priceCatalog.put("Klavye", 800.0);

        for (Map.Entry<String, Double> entry : priceCatalog.entrySet()) {
            System.out.printf("  %-10s → %.2f TL%n", entry.getKey(), entry.getValue());
        }

        double monitorPrice = priceCatalog.getOrDefault("Monitör", -1.0);
        System.out.println("Monitör fiyatı: " + (monitorPrice == -1 ? "Bulunamadı" : monitorPrice + " TL"));

        // keySet / values farkı
        System.out.println("Ürün adları (keySet): " + priceCatalog.keySet());
        System.out.println("Fiyatlar   (values) : " + priceCatalog.values());

        // ===== Bonus: Sepet + Katalog = Toplam Tutar =====
        System.out.println("\n===== Sepet Toplamı =====");
        double total = 0;
        for (Map.Entry<String, Integer> item : cart.entrySet()) {
            double unitPrice = priceCatalog.getOrDefault(item.getKey(), 0.0);
            double lineTotal = unitPrice * item.getValue();
            total += lineTotal;
            System.out.printf("  %-8s %d x %.2f = %.2f TL%n", item.getKey(), item.getValue(), unitPrice, lineTotal);
        }
        System.out.printf("TOPLAM: %.2f TL%n", total);
    }
}
