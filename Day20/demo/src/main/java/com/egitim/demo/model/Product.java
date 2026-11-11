package com.egitim.demo.model;

public class Product {

    private Long id;
    private String name;
    private double price;
    private String category;

    // Jackson, @RequestBody ile gelen JSON'dan nesne oluştururken
    // önce boş constructor'ı çağırıp sonra setter'larla alanları dolduruyor
    public Product() {}

    public Product(Long id, String name, double price, String category) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.category = category;
    }

    public Long getId()         { return id; }
    public String getName()     { return name; }
    public double getPrice()    { return price; }
    public String getCategory() { return category; }

    public void setName(String name)         { this.name = name; }
    public void setPrice(double price)       { this.price = price; }
    public void setCategory(String category) { this.category = category; }
}
