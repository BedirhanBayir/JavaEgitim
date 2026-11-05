public class InvalidGradeException extends RuntimeException {
    public InvalidGradeException(int grade) {
        super("Geçersiz not: " + grade + ". Not 0 ile 100 arasında olmalıdır.");
    }
}
