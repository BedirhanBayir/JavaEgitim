public class PartTimeEmployee extends Employee {

    private double hourlyRate;
    private int hoursWorked;

    public PartTimeEmployee(String employeeId, String firstName, String lastName,
                            double hourlyRate, int hoursWorked) {
        super(employeeId, firstName, lastName);
        this.hourlyRate = hourlyRate;
        this.hoursWorked = hoursWorked;
    }

    public double calculateMonthlyPay() {
        return hourlyRate * hoursWorked;
    }

    @Override
    public void printInfo() {
        System.out.println("--- Yarı Zamanlı Çalışan ---");
        super.printInfo();
        System.out.printf("Saat Ücreti   : %.2f TL%n", hourlyRate);
        System.out.printf("Çalışılan Saat: %d saat%n", hoursWorked);
        System.out.printf("Aylık Ücret   : %.2f TL%n", calculateMonthlyPay());
    }
}
