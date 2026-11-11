package com.egitim.demo.service;

import com.egitim.demo.model.Product;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final List<Product> products = new ArrayList<>(List.of(
        new Product(1L, "Laptop",   15000.0, "Elektronik"),
        new Product(2L, "Fare",       250.0, "Elektronik"),
        new Product(3L, "Kalem",       10.0, "Kırtasiye"),
        new Product(4L, "Defter",      25.0, "Kırtasiye"),
        new Product(5L, "Kulaklık",  1200.0, "Elektronik")
    ));

    // ===================== CRUD =====================

    public List<Product> getAll() {
        return products;
    }

    public Optional<Product> getById(Long id) {
        return products.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst();
    }

    public Product create(Product product) {
        long newId = products.isEmpty() ? 1L : products.get(products.size() - 1).getId() + 1;
        Product newProduct = new Product(newId, product.getName(), product.getPrice(), product.getCategory());
        products.add(newProduct);
        return newProduct;
    }

    public Optional<Product> update(Long id, Product updated) {
        for (Product p : products) {
            if (p.getId().equals(id)) {
                p.setName(updated.getName());
                p.setPrice(updated.getPrice());
                p.setCategory(updated.getCategory());
                return Optional.of(p);
            }
        }
        return Optional.empty();
    }

    public boolean delete(Long id) {
        return products.removeIf(p -> p.getId().equals(id));
    }

    // ===================== Day 16–17'den taşınanlar =====================
    // Controller'daki for döngüleri burada stream ile kısaldı

    public int count() {
        return products.size();
    }

    public Set<String> getCategories() {
        return products.stream()
                .map(Product::getCategory)
                .collect(Collectors.toCollection(TreeSet::new));
    }

    public List<Product> searchByName(String name) {
        String query = name.toLowerCase();
        return products.stream()
                .filter(p -> p.getName().toLowerCase().contains(query))
                .collect(Collectors.toList());
    }

    public List<Product> filter(String category, Double minPrice, Double maxPrice) {
        return products.stream()
                .filter(p -> p.getCategory().equalsIgnoreCase(category))
                .filter(p -> minPrice == null || p.getPrice() >= minPrice)
                .filter(p -> maxPrice == null || p.getPrice() <= maxPrice)
                .collect(Collectors.toList());
    }

    // ===================== İş kuralı =====================
    // "Geçerli ürün nedir?" bir iş kuralı, bu yüzden controller'dan buraya taşındı

    public boolean isValid(Product p) {
        return p.getName() != null && !p.getName().isBlank()
                && p.getCategory() != null && !p.getCategory().isBlank()
                && p.getPrice() >= 0;
    }
}
