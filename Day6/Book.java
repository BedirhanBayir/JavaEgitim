public class Book {

    String title;
    String author;
    int year;
    int pageCount;

    void printInfo() {
        System.out.println("-----------------------------");
        System.out.println("Kitap  : " + title);
        System.out.println("Yazar  : " + author);
        System.out.println("Yıl    : " + year);
        System.out.println("Sayfa  : " + pageCount);
    }

    boolean isBestSeller() {
        return pageCount > 300;
    }
}
