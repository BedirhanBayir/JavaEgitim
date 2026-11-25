package com.egitim.demo.repository;

import com.egitim.demo.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

// JpaRepository<Product, Long>: Product entity tipi, Long id'nin tipi.
// findAll, findById, save, deleteById, existsById, count hazır geliyor;
// implementasyonu Spring Data uygulama açılırken kendisi oluşturuyor.
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    // ===== Programdaki derived query'ler =====

    // WHERE category = ?
    List<Product> findByCategory(String category);

    // WHERE price <= ?
    List<Product> findByPriceLessThanEqual(double maxPrice);

    // ===== Ek: service'teki stream'lerin yerine =====

    // WHERE upper(name) LIKE upper('%?%')   → /api/products/search
    List<Product> findByNameContainingIgnoreCase(String name);

    // WHERE upper(category) = upper(?)      → /api/products/filter
    List<Product> findByCategoryIgnoreCase(String category);
}
