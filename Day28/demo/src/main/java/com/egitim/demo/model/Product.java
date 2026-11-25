package com.egitim.demo.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private double price;

    // Day 28: String category yerine gerçek bir ilişki.
    // Birden fazla ürün → bir kategori. products tablosunda category_id foreign key sütunu.
    // @JsonBackReference: ürün JSON'a yazılırken category nesnesi yazılmıyor
    // (yoksa category → products → category → ... sonsuz döngü olurdu).
    @ManyToOne
    @JoinColumn(name = "category_id")
    @JsonBackReference
    private Category category;

    // JPA için zorunlu boş constructor
    public Product() {}

    public Product(String name, double price, Category category) {
        this.name = name;
        this.price = price;
        this.category = category;
    }

    public Long getId()           { return id; }
    public String getName()       { return name; }
    public double getPrice()      { return price; }
    public Category getCategory() { return category; }

    // category JSON'a yazılmadığı için ürünün hangi kategoride olduğu kaybolmasın:
    // JSON'da "categoryName": "Elektronik" olarak görünüyor. Veritabanında sütunu yok.
    public String getCategoryName() {
        return category != null ? category.getName() : null;
    }

    public void setName(String name)           { this.name = name; }
    public void setPrice(double price)         { this.price = price; }
    public void setCategory(Category category) { this.category = category; }
}
