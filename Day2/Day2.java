import java.util.Scanner;

public class Day2 {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        System.out.print("Öğrenci adı: ");
        String studentName = scanner.nextLine();

        System.out.print("Not (0-100): ");
        int grade = scanner.nextInt();
        scanner.nextLine(); // nextInt'in bıraktığı satır sonunu temizle

        System.out.print("Bölüm: ");
        String department = scanner.nextLine().trim();

        // Önce geçersiz aralığı ele al
        if (grade < 0 || grade > 100) {
            System.out.println("Geçersiz puan girdiniz.");
            scanner.close();
            return;
        }

        // --- Harf Notu ---
        String letterGrade;
        if (grade >= 90) {
            letterGrade = "AA";
        } else if (grade >= 80) {
            letterGrade = "BA";
        } else if (grade >= 70) {
            letterGrade = "BB";
        } else if (grade >= 60) {
            letterGrade = "CB";
        } else if (grade >= 50) {
            letterGrade = "CC";
        } else {
            letterGrade = "FF (Kaldı)";
        }

        System.out.println(studentName + " - Harf Notu: " + letterGrade);

        // --- Bölüm Mesajı ---
        switch (department) {
            case "Bilgisayar":
                System.out.println("Yazılım & donanım odaklı müfredat.");
                break;
            case "Elektrik":
                System.out.println("Devre & sistem odaklı müfredat.");
                break;
            case "Makine":
                System.out.println("Termodinamik & üretim odaklı müfredat.");
                break;
            default:
                System.out.println("Bölüm bilgisi tanımlı değil.");
        }

        scanner.close();
    }
}
