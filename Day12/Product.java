public class Product {

    private String name;
    private double price;
    private int stock;

    public Product(String name, double price, int stock) {
        this.name = name;
        this.price = price;
        this.stock = stock;
    }

    public String getName()  { return name; }
    public double getPrice() { return price; }
    public int getStock()    { return stock; }

    @Override
    public String toString() {
        return String.format("[%s | %.2f TL | Stok: %d]", name, price, stock);
    }
}
