package com.egitim.demo.controller;

import com.egitim.demo.model.Product;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
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

    // ===================== GET =====================

    @GetMapping
    public List<Product> getAllProducts() {
        return productList;
    }

    @GetMapping("/count")
    public int getCount() {
        return productList.size();
    }

    @GetMapping("/categories")
    public Set<String> getCategories() {
        Set<String> categories = new TreeSet<>();
        for (Product p : productList) {
            categories.add(p.getCategory());
        }
        return categories;
    }

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

    // Day 17'deki Map.of("error") yerine artık gerçek 404 dönüyor
    @GetMapping("/{id}")
    public ResponseEntity<Product> getById(@PathVariable Long id) {
        for (Product p : productList) {
            if (p.getId().equals(id)) {
                return ResponseEntity.ok(p);                 // 200
            }
        }
        return ResponseEntity.notFound().build();            // 404
    }

    // ===================== POST =====================
    // POST /api/products   Body: {"name":"Monitör","price":8500.00,"category":"Elektronik"}
    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        if (!isValid(product)) {
            return ResponseEntity.badRequest().build();      // 400
        }

        long newId = productList.isEmpty() ? 1L : productList.get(productList.size() - 1).getId() + 1;
        Product newProduct = new Product(newId, product.getName(), product.getPrice(), product.getCategory());
        productList.add(newProduct);

        return ResponseEntity.status(HttpStatus.CREATED).body(newProduct);  // 201
    }

    // ===================== PUT =====================
    // PUT /api/products/2   Body: {"name":"Kablosuz Fare","price":450.0,"category":"Elektronik"}
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product updated) {
        if (!isValid(updated)) {
            return ResponseEntity.badRequest().build();      // 400
        }

        for (Product p : productList) {
            if (p.getId().equals(id)) {
                p.setName(updated.getName());
                p.setPrice(updated.getPrice());
                p.setCategory(updated.getCategory());
                return ResponseEntity.ok(p);                 // 200
            }
        }
        return ResponseEntity.notFound().build();            // 404
    }

    // ===================== DELETE =====================
    // DELETE /api/products/3
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        boolean removed = productList.removeIf(p -> p.getId().equals(id));
        if (removed) {
            return ResponseEntity.noContent().build();       // 204
        }
        return ResponseEntity.notFound().build();            // 404
    }

    // İsim ve kategori boş olamaz, fiyat negatif olamaz
    private boolean isValid(Product p) {
        return p.getName() != null && !p.getName().isBlank()
                && p.getCategory() != null && !p.getCategory().isBlank()
                && p.getPrice() >= 0;
    }
}
