package com.egitim.demo.controller;

import com.egitim.demo.model.Product;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

@RestController
@RequestMapping("/api/products")
public class ProductController {

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
    @GetMapping("/categories")
    public Set<String> getCategories() {
        Set<String> categories = new TreeSet<>();
        for (Product p : productList) {
            categories.add(p.getCategory());
        }
        return categories;
    }

    // GET /api/products/search?name=lap
    @GetMapping("/search")
    public List<Product> searchByName(@RequestParam String name) {
        List<Product> result = new ArrayList<>();
        for (Product p : productList) {
            if (p.getName().toLowerCase().contains(name.toLowerCase())) {
                result.add(p);
            }
        }
        return result;
    }

    // GET /api/products/filter?category=Elektronik
    // GET /api/products/filter?category=Elektronik&maxPrice=1000
    // GET /api/products/filter?category=Elektronik&minPrice=500&maxPrice=2000
    @GetMapping("/filter")
    public List<Product> filterProducts(
            @RequestParam String category,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice) {

        List<Product> result = new ArrayList<>();
        for (Product p : productList) {
            boolean categoryMatch = p.getCategory().equalsIgnoreCase(category);
            boolean minMatch = (minPrice == null || p.getPrice() >= minPrice);
            boolean maxMatch = (maxPrice == null || p.getPrice() <= maxPrice);
            if (categoryMatch && minMatch && maxMatch) {
                result.add(p);
            }
        }
        return result;
    }

    // GET /api/products/1
    // /search, /filter, /count gibi sabit yollar {id}'den önce eşleşir,
    // yani "search" kelimesi id sanılmaz.
    @GetMapping("/{id}")
    public Object getById(@PathVariable Long id) {
        for (Product p : productList) {
            if (p.getId().equals(id)) {
                return p;
            }
        }
        return Map.of("error", "Ürün bulunamadı. ID: " + id);
    }
}
