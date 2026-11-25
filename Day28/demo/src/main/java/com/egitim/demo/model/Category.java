package com.egitim.demo.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "categories")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;

    // Bir kategori → birden fazla ürün.
    // mappedBy = "category": ilişkinin sahibi Product.category alanı; foreign key
    // (category_id) products tablosunda duruyor, categories tablosunda ekstra sütun yok.
    // @JsonManagedReference: JSON'da ürünler yazılıyor, ürünlerin içindeki category yazılmıyor.
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Product> products = new ArrayList<>();   // yeni kategoride null değil [] dönsün

    public Category() {}

    public Category(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public Long getId()                { return id; }
    public String getName()            { return name; }
    public String getDescription()     { return description; }
    public List<Product> getProducts() { return products; }

    public void setName(String name)               { this.name = name; }
    public void setDescription(String description) { this.description = description; }
}
