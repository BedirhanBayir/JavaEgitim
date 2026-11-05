public class Employee {

    private String employeeId;
    private String firstName;
    private String lastName;

    public Employee(String employeeId, String firstName, String lastName) {
        this.employeeId = employeeId;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public String getEmployeeId() { return employeeId; }

    public void printInfo() {
        System.out.println("ID       : " + employeeId);
        System.out.println("Ad Soyad : " + getFullName());
    }
}
