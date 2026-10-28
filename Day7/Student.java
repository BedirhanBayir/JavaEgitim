public class Student {

    private String name;
    private String studentId;
    private double gpa;
    private boolean isActive;

    // Constructor 1: Parametresiz
    public Student() {
        this.isActive = true;
    }

    // Constructor 2: Tam parametreli
    public Student(String name, String studentId, double gpa, boolean isActive) {
        this.name = name;
        this.studentId = studentId;
        this.gpa = gpa;
        this.isActive = isActive;
    }

    // Constructor 3: Kısmi, tam parametreliye varsayılanlarla yönlendirir
    public Student(String name, String studentId) {
        this(name, studentId, 0.0, true);
    }

    public void printInfo() {
        System.out.println("-----------------------------");
        System.out.println("İsim      : " + name);
        System.out.println("Numara    : " + studentId);
        System.out.printf("GPA       : %.2f%n", gpa);
        System.out.println("Aktif     : " + isActive);
    }

    public boolean isHonorStudent() {
        return gpa >= 3.5;
    }
}
