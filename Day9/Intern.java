public class Intern extends Employee {

    private double dailyAllowance;
    private int internshipDays;

    public Intern(String employeeId, String firstName, String lastName,
                  double dailyAllowance, int internshipDays) {
        super(employeeId, firstName, lastName);
        this.dailyAllowance = dailyAllowance;
        this.internshipDays = internshipDays;
    }

    public double calculateTotalAllowance() {
        return dailyAllowance * internshipDays;
    }

    @Override
    public void printInfo() {
        System.out.println("--- Stajyer ---");
        super.printInfo();
        System.out.printf("Günlük Harcırah : %.2f TL%n", dailyAllowance);
        System.out.printf("Staj Süresi     : %d gün%n", internshipDays);
        System.out.printf("Toplam Harcırah : %.2f TL%n", calculateTotalAllowance());
    }
}
