import java.util.ArrayList;
import java.util.List;

public class StudentService {

    private final List<Student> students = new ArrayList<>();

    public Student addStudent(String name, int grade) {
        Student s = new Student(name, grade);   // geçersizse exception fırlatır
        students.add(s);
        return s;
    }

    public Student findById(String id) {
        for (Student s : students) {
            if (s.getStudentId().equalsIgnoreCase(id.trim())) {
                return s;
            }
        }
        throw new StudentNotFoundException(id);
    }

    public void updateGrade(String id, int newGrade) {
        Student s = findById(id);   // bulunamazsa StudentNotFoundException
        s.setGrade(newGrade);       // geçersizse InvalidGradeException
    }

    public void listAll() {
        if (students.isEmpty()) {
            System.out.println("Kayıtlı öğrenci bulunmamaktadır.");
            return;
        }
        System.out.println("=== Öğrenci Listesi ===");
        for (Student s : students) {
            System.out.println(s);
        }
        System.out.printf("Toplam: %d öğrenci | Ortalama: %.2f%n", students.size(), average());
    }

    public double average() {
        if (students.isEmpty()) return 0;
        int sum = 0;
        for (Student s : students) {
            sum += s.getGrade();
        }
        return (double) sum / students.size();
    }
}
