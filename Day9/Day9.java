public class Day9 {
    public static void main(String[] args) {

        Employee employee = new Employee("E000", "Ali", "Kaya");
        FullTimeEmployee fullTime = new FullTimeEmployee("E001", "Ahmet", "Çelik", 15000);
        Intern intern = new Intern("E002", "Bedirhan", "Bayır", 200, 29);

        System.out.println("--- Genel Çalışan ---");
        employee.printInfo();
        System.out.println();

        fullTime.printInfo();
        System.out.println();

        intern.printInfo();
        System.out.println();

        // Alt sınıf, üst sınıfın public metotlarını miras alır
        System.out.println("Stajyerin tam adı (Employee'dan miras): " + intern.getFullName());
        System.out.println("Tam zamanlının ID'si (Employee'dan miras): " + fullTime.getEmployeeId());
    }
}
