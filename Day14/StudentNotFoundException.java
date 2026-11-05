public class StudentNotFoundException extends RuntimeException {
    public StudentNotFoundException(String id) {
        super("Öğrenci bulunamadı. ID: " + id);
    }
}
