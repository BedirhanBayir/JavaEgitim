import java.util.Scanner;

public class Day3 {
    public static void main(String[] args) {

        // --- Görev 1: For Döngüsü ---
        System.out.println("=== Görev 1: 1'den 20'ye ===");
        for (int i = 1; i <= 20; i++) {
            if (i % 2 == 0) {
                System.out.println(i + " [ÇİFT]");
            } else {
                System.out.println(i + " [TEK]");
            }
        }

        Scanner scanner = new Scanner(System.in);

        // --- Görev 2: While Döngüsü ---
        // 0 girilince döngü biter, 0 toplama eklenmez
        System.out.println("\n=== Görev 2: Sayı Topla (0 = dur) ===");
        int total = 0;
        System.out.print("Sayı gir: ");
        int input = scanner.nextInt();
        while (input != 0) {
            total += input;
            System.out.print("Sayı gir: ");
            input = scanner.nextInt();
        }
        System.out.println("Toplam: " + total);

        // --- Görev 3: Do-While Menü ---
        int choice;
        System.out.println("\n=== Görev 3: Menü ===");
        do {
            System.out.println("1 - Listele | 2 - Ara | 3 - Çıkış");
            System.out.print("Seçim: ");
            choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    System.out.println("Listeleniyor...");
                    break;
                case 2:
                    System.out.println("Aranıyor...");
                    break;
                case 3:
                    System.out.println("Çıkış yapılıyor.");
                    break;
                default:
                    System.out.println("Geçersiz seçim!");
            }
        } while (choice != 3);

        // --- Ek: continue ile 5. ürünü atla ---
        System.out.println("\n=== Ek: continue ile 5. ürünü atla ===");
        for (int productNo = 1; productNo <= 10; productNo++) {
            if (productNo == 5) {
                continue;
            }
            System.out.print("Ürün-" + productNo + " ");
        }
        System.out.println();

        scanner.close();
    }
}
