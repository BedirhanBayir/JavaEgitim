package com.egitim.demo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private double price;
    private String category;

    // JPA için zorunlu boş constructor (Jackson da bunu kullanıyor)
    public Product() {}

    // Veritabanına kaydederken: id'yi veritabanı veriyor
    public Product(String name, double price, String category) {
        this.name = name;
        this.price = price;
        this.category = category;
    }

    // ProductService hâlâ bellekteki listeyle çalıştığı için id'yi elle veren constructor.
    // Day 22'de service repository'ye geçince buna gerek kalmayacak.
    public Product(Long id, String name, double price, String category) {
        this(name, price, category);
        this.id = id;
    }

    public Long getId()         { return id; }
    public String getName()     { return name; }
    public double getPrice()    { return price; }
    public String getCategory() { return category; }

    public void setName(String name)         { this.name = name; }
    public void setPrice(double price)       { this.price = price; }
    public void setCategory(String category) { this.category = category; }
}
