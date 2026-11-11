package com.egitim.demo.controller;

import com.egitim.demo.model.Product;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    // Veritabanı henüz yok, ürünler bellekte duruyor
    private final List<Product> productList = new ArrayList<>(List.of(
        new Product(1L, "Laptop",   15000.0, "Elektronik"),
        new Product(2L, "Fare",       250.0, "Elektronik"),
        new Product(3L, "Kalem",       10.0, "Kırtasiye"),
        new Product(4L, "Defter",      25.0, "Kırtasiye"),
        new Product(5L, "Kulaklık",  1200.0, "Elektronik")
    ));

    // GET /api/products
    @GetMapping
    public List<Product> getAllProducts() {
        return productList;
    }

    // GET /api/products/count
    @GetMapping("/count")
    public int getCount() {
        return productList.size();
    }

    // GET /api/products/categories
    // TreeSet: hem tekrarsız hem alfabetik sıralı
    @GetMapping("/categories")
    public Set<String> getCategories() {
        Set<String> categories = new TreeSet<>();
        for (Product p : productList) {
            categories.add(p.getCategory());
        }
        return categories;
    }
}
