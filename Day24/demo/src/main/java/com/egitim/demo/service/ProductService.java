package com.egitim.demo.service;

import com.egitim.demo.model.Product;
import com.egitim.demo.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

@Service
public class ProductService {

    // Bellekteki List<Product> gitti, veri artık H2'deki products tablosunda.
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // ===================== CRUD =====================

    public List<Product> getAll() {
        return productRepository.findAll();
    }

    public Optional<Product> getById(Long id) {
        return productRepository.findById(id);
    }

    public Product create(Product product) {
        // İstemci body'de "id" gönderse bile yeni nesne id'siz oluşturuluyor.
        // Aksi halde save() o id'li ürünü bulup üzerine yazardı (INSERT değil UPDATE).
        Product newProduct = new Product(product.getName(), product.getPrice(), product.getCategory());
        return productRepository.save(newProduct);
    }

    // Okuma + yazma tek transaction'da: ikisi birlikte ya olur ya olmaz
    @Transactional
    public Optional<Product> update(Long id, Product updated) {
        return productRepository.findById(id).map(existing -> {
            existing.setName(updated.getName());
            existing.setPrice(updated.getPrice());
            existing.setCategory(updated.getCategory());
            return productRepository.save(existing);   // id dolu → UPDATE
        });
    }

    @Transactional
    public boolean delete(Long id) {
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // Day 24: kategorideki tüm ürünleri sil, kaç tane silindiğini döndür.
    // Derived delete metotları transaction olmadan çalışmıyor
    // (TransactionRequiredException), bu yüzden @Transactional zorunlu.
    @Transactional
    public int deleteByCategory(String category) {
        return (int) productRepository.deleteByCategory(category);
    }

    // ===================== Derived query'ler =====================

    public List<Product> getByCategory(String category) {
        return productRepository.findByCategory(category);
    }

    public List<Product> getByMaxPrice(double maxPrice) {
        return productRepository.findByPriceLessThanEqual(maxPrice);
    }

    // ===================== Day 16–17'den taşınanlar =====================
    // Controller değişmesin diye metot imzaları aynı kaldı, içleri repository'ye geçti.

    public int count() {
        // JpaRepository.count() long döndürüyor, controller int bekliyor
        return (int) productRepository.count();
    }

    public Set<String> getCategories() {
        return productRepository.findAll().stream()
                .map(Product::getCategory)
                .collect(Collectors.toCollection(TreeSet::new));
    }

    public List<Product> searchByName(String name) {
        return productRepository.findByNameContainingIgnoreCase(name);
    }

    public List<Product> filter(String category, Double minPrice, Double maxPrice) {
        // Kategori veritabanında filtreleniyor, isteğe bağlı fiyat aralığı burada
        return productRepository.findByCategoryIgnoreCase(category).stream()
                .filter(p -> minPrice == null || p.getPrice() >= minPrice)
                .filter(p -> maxPrice == null || p.getPrice() <= maxPrice)
                .collect(Collectors.toList());
    }

    // ===================== İş kuralı =====================

    public boolean isValid(Product p) {
        return p.getName() != null && !p.getName().isBlank()
                && p.getCategory() != null && !p.getCategory().isBlank()
                && p.getPrice() >= 0;
    }
}
