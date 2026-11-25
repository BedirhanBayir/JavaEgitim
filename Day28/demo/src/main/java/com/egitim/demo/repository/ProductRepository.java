package com.egitim.demo.repository;

import com.egitim.demo.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

// JpaRepository<Product, Long>: Product entity tipi, Long id'nin tipi.
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    // Day 28: Product'ta artık String category yok, Category nesnesi var.
    // findByCategory_Name → category.name: Spring ilişkiyi takip edip JOIN'li SQL üretiyor.
    // ... from products p join categories c on c.id = p.category_id where c.name = ?
    // Alt çizgi (_) "category ile name'i ayır" demek. Yazmasam Spring, Product'taki
    // getCategoryName() getter'ını "categoryName" diye ayrı bir alan sanabilirdi.
    List<Product> findByCategory_Name(String categoryName);

    List<Product> findByCategory_NameIgnoreCase(String categoryName);

    // WHERE price <= ?
    List<Product> findByPriceLessThanEqual(double maxPrice);

    // WHERE upper(name) LIKE upper('%?%')   → /api/products/search
    List<Product> findByNameContainingIgnoreCase(String name);

    // Kategorideki ürünleri bulup tek tek siliyor, silinen sayısını döndürüyor
    long deleteByCategory_Name(String categoryName);
}
