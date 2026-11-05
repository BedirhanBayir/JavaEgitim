public class Student {

    // Tüm Student nesneleri tarafından paylaşılan sayaç → benzersiz ID üretir
    private static int counter = 1000;

    private String studentId;
    private String name;
    private int grade;

    public Student(String name, int grade) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("İsim boş olamaz.");
        }
        setGrade(grade);                      // önce doğrula, sonra ID ver
        this.name = name.trim();
        this.studentId = "STU" + (++counter);
    }

    public void setGrade(int grade) {
        if (grade < 0 || grade > 100) {
            throw new InvalidGradeException(grade);
        }
        this.grade = grade;
    }

    public String getStudentId() { return studentId; }
    public String getName()      { return name; }
    public int getGrade()        { return grade; }

    public String getLetterGrade() {
        if (grade >= 85) return "AA";
        if (grade >= 70) return "BB";
        if (grade >= 60) return "CC";
        if (grade >= 50) return "DD";
        return "FF";
    }

    public void printInfo() {
        System.out.println("ID   : " + studentId);
        System.out.println("İsim : " + name);
        System.out.println("Not  : " + grade + " (" + getLetterGrade() + ")");
    }

    @Override
    public String toString() {
        return String.format("[%s] %-20s Not: %3d (%s)", studentId, name, grade, getLetterGrade());
    }
}
