import java.util.Arrays;
import java.util.Scanner;

public class Day4 {
    public static void main(String[] args) {

        // --- Görev 1: Dizi tanımla & yazdır ---
        int[] grades = {72, 85, 91, 60, 45, 88, 73, 95, 55, 68};

        System.out.println("=== Tüm Puanlar ===");
        for (int grade : grades) {
            System.out.print(grade + " ");
        }
        System.out.println();

        // --- Görev 2: Max, min, ortalama ---
        int max = grades[0];
        int min = grades[0];
        int sum = 0;

        for (int grade : grades) {
            if (grade > max) max = grade;
            if (grade < min) min = grade;
            sum += grade;
        }

        System.out.println("En Yüksek : " + max);
        System.out.println("En Düşük  : " + min);
        System.out.printf("Ortalama  : %.2f%n", (double) sum / grades.length);

        // --- Görev 3: String dizisi & filtre ---
        String[] names = {"Ali", "Mehmet", "Zeynep", "Can", "Fatmagül"};
        System.out.println("\n=== Uzun İsimler (>5 karakter) ===");
        for (String name : names) {
            if (name.length() > 5) {
                System.out.println(name);
            }
        }

        // --- Görev 4: Kullanıcı girişi & sıralama ---
        Scanner scanner = new Scanner(System.in);
        int[] userNumbers = new int[5];
        System.out.println("\n5 sayı giriniz:");
        for (int i = 0; i < userNumbers.length; i++) {
            System.out.print((i + 1) + ". sayı: ");
            userNumbers[i] = scanner.nextInt();
        }
        Arrays.sort(userNumbers);
        System.out.println("Sıralı: " + Arrays.toString(userNumbers));

        scanner.close();
    }
}
