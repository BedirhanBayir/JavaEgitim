public interface Swimmable {
    void swim();

    // Java 8+ default metot: uygulayan sınıflar override etmek zorunda değil
    default void dive() {
        System.out.println("Suya dalıyor...");
    }
}
