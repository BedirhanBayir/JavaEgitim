public class Day6 {
    public static void main(String[] args) {

        Book book1 = new Book();
        book1.title = "Suç ve Ceza";
        book1.author = "Fyodor Dostoyevski";
        book1.year = 1866;
        book1.pageCount = 671;

        Book book2 = new Book();
        book2.title = "Küçük Prens";
        book2.author = "Antoine de Saint-Exupéry";
        book2.year = 1943;
        book2.pageCount = 96;

        Book book3 = new Book();
        book3.title = "Kürk Mantolu Madonna";
        book3.author = "Sabahattin Ali";
        book3.year = 1943;
        book3.pageCount = 160;

        book1.printInfo();
        System.out.println("Bestseller: " + book1.isBestSeller());

        book2.printInfo();
        System.out.println("Bestseller: " + book2.isBestSeller());

        book3.printInfo();
        System.out.println("Bestseller: " + book3.isBestSeller());
    }
}
