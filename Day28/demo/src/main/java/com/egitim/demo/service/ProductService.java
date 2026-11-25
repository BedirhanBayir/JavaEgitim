package com.egitim.demo.service;

import com.egitim.demo.exception.ResourceNotFoundException;
import com.egitim.demo.model.Category;
import com.egitim.demo.model.Product;
import com.egitim.demo.repository.CategoryRepository;
import com.egitim.demo.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;   // Day 28: ürünü kategoriye bağlamak için

    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    // ===================== CRUD =====================

    public List<Product> getAll() {
        return productRepository.findAll();
    }

    public Product getById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", id));
    }

    // Day 28: categoryId ile gelen kategoriyi bul, ürünü ona bağla.
    // Kategori yoksa → "Category 99 bulunamadı." (404)
    public Product create(Long categoryId, String name, double price) {
        Category category = findCategory(categoryId);
        Product product = new Product(name, price, category);
        return productRepository.save(product);   // INSERT ... category_id = ?
    }

    @Transactional
    public Product update(Long id, Long categoryId, String name, double price) {
        Product existing = getById(id);
        existing.setName(name);
        existing.setPrice(price);
        existing.setCategory(findCategory(categoryId));   // ürün başka kategoriye taşınabiliyor
        return productRepository.save(existing);
    }

    @Transactional
    public void delete(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException("Product", id);
        }
        productRepository.deleteById(id);
    }

    // Kategorinin kendisi silinmiyor, sadece içindeki ürünler
    @Transactional
    public int deleteByCategory(String categoryName) {
        return (int) productRepository.deleteByCategory_Name(categoryName);
    }

    private Category findCategory(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", categoryId));
    }

    // ===================== Sorgular =====================

    public List<Product> getByCategory(String categoryName) {
        return productRepository.findByCategory_Name(categoryName);
    }

    public List<Product> getByMaxPrice(double maxPrice) {
        return productRepository.findByPriceLessThanEqual(maxPrice);
    }

    public int count() {
        return (int) productRepository.count();
    }

    // İçinde en az bir ürün olan kategorilerin adları (tüm kategoriler için: GET /api/categories)
    public Set<String> getCategories() {
        return productRepository.findAll().stream()
                .map(Product::getCategoryName)
                .filter(Objects::nonNull)
                .collect(Collectors.toCollection(TreeSet::new));
    }

    public List<Product> searchByName(String name) {
        return productRepository.findByNameContainingIgnoreCase(name);
    }

    public List<Product> filter(String categoryName, Double minPrice, Double maxPrice) {
        return productRepository.findByCategory_NameIgnoreCase(categoryName).stream()
                .filter(p -> minPrice == null || p.getPrice() >= minPrice)
                .filter(p -> maxPrice == null || p.getPrice() <= maxPrice)
                .collect(Collectors.toList());
    }
}
