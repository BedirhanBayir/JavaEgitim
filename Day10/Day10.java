public class Day10 {
    public static void main(String[] args) {

        // Üst sınıf referansı, alt sınıf nesnesi
        Employee[] employees = {
            new FullTimeEmployee("E001", "Ahmet", "Çelik", 15000),
            new PartTimeEmployee("E002", "Selin", "Yurt", 120, 80),
            new Intern("E003", "Can", "Arslan", 200, 45),
            new FullTimeEmployee("E004", "Merve", "Şahin", 22000)
        };

        System.out.println("========== ÇALIŞAN RAPORU ==========");

        double totalCost = 0;

        for (Employee emp : employees) {
            emp.printInfo();             // Polimorfik çağrı → nesnenin gerçek tipinin metodu çalışır

            // Alt sınıfa özel metotlar için tip kontrolü (Java 16+ pattern matching)
            if (emp instanceof FullTimeEmployee fte) {
                System.out.printf("(Yıllık: %.2f TL)%n", fte.calculateAnnualSalary());
                totalCost += fte.calculateAnnualSalary() / 12;
            } else if (emp instanceof PartTimeEmployee pte) {
                totalCost += pte.calculateMonthlyPay();
            } else if (emp instanceof Intern intern) {
                System.out.printf("(Toplam Harcırah: %.2f TL)%n", intern.calculateTotalAllowance());
                totalCost += intern.calculateTotalAllowance();
            }

            System.out.println();
        }

        System.out.printf("Toplam personel maliyeti (bu ay): %.2f TL%n", totalCost);

        // Overloading örneği (aynı isim, farklı parametre) → derleme zamanında seçilir
        System.out.println();
        System.out.println(describe(employees[0]));
        System.out.println(describe(employees[1], true));
    }

    static String describe(Employee e) {
        return "Çalışan: " + e.getFullName();
    }

    static String describe(Employee e, boolean withId) {
        return withId ? "Çalışan: " + e.getFullName() + " (" + e.getEmployeeId() + ")" : describe(e);
    }
}
