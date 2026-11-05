public class FullTimeEmployee extends Employee {

    private double monthlySalary;

    public FullTimeEmployee(String employeeId, String firstName, String lastName, double monthlySalary) {
        super(employeeId, firstName, lastName);
        this.monthlySalary = monthlySalary;
    }

    public double calculateAnnualSalary() {
        return monthlySalary * 12;
    }

    @Override
    public void printInfo() {
        System.out.println("--- Tam Zamanlı Çalışan ---");
        super.printInfo();
        System.out.printf("Aylık Maaş : %.2f TL%n", monthlySalary);
        System.out.printf("Yıllık     : %.2f TL%n", calculateAnnualSalary());
    }
}
