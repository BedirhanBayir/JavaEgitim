import java.util.Scanner;

public class Day14 {

    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {

        StudentService service = new StudentService();
        int choice = 0;

        try {
            do {
                System.out.println("\n1-Ekle  2-Listele  3-Ara  4-Not Güncelle  5-Çıkış");
                choice = readInt("Seçim: ");

                switch (choice) {
                    case 1 -> {
                        System.out.print("İsim: ");
                        String name = scanner.nextLine();
                        int grade = readInt("Not (0-100): ");
                        try {
                            Student s = service.addStudent(name, grade);
                            System.out.println("Öğrenci eklendi: " + s);
                        } catch (InvalidGradeException | IllegalArgumentException e) {
                            System.out.println("HATA: " + e.getMessage());
                        }
                    }
                    case 2 -> service.listAll();
                    case 3 -> {
                        System.out.print("Öğrenci ID: ");
                        String id = scanner.nextLine();
                        try {
                            service.findById(id).printInfo();
                        } catch (StudentNotFoundException e) {
                            System.out.println("HATA: " + e.getMessage());
                        }
                    }
                    case 4 -> {
                        System.out.print("Öğrenci ID: ");
                        String id = scanner.nextLine();
                        int grade = readInt("Yeni not (0-100): ");
                        try {
                            service.updateGrade(id, grade);
                            System.out.println("Not güncellendi.");
                        } catch (StudentNotFoundException | InvalidGradeException e) {
                            System.out.println("HATA: " + e.getMessage());
                        }
                    }
                    case 5 -> System.out.println("Çıkış yapılıyor.");
                    default -> System.out.println("Geçersiz seçim.");
                }
            } while (choice != 5);
        } finally {
            // Hata olsa da olmasa da çalışır → kaynağı kapat
            scanner.close();
            System.out.println("Program sonlandı.");
        }
    }

    /**
     * Kullanıcı sayı yerine harf girerse program çökmesin diye
     * satırı String olarak okuyup int'e çeviriyoruz.
     * Integer.parseInt hatalı girdide NumberFormatException (unchecked) fırlatır.
     */
    private static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            if (!scanner.hasNextLine()) {
                return 5; // girdi bittiyse (Ctrl+D) çıkış seçeneği gibi davran
            }
            String line = scanner.nextLine().trim();
            try {
                return Integer.parseInt(line);
            } catch (NumberFormatException e) {
                System.out.println("HATA: '" + line + "' bir sayı değil. Tekrar deneyin.");
            }
        }
    }
}
