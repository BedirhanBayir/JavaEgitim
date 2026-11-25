package com.egitim.demo.service;

import com.egitim.demo.exception.ResourceNotFoundException;
import com.egitim.demo.model.Product;
import com.egitim.demo.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

@Service
public class ProductService {

    // Veri veritabanındaki products tablosunda (Day 27'den beri MySQL).
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // ===================== CRUD =====================

    public List<Product> getAll() {
        return productRepository.findAll();
    }

    // Day 26: Optional yerine ürünün kendisi; yoksa ResourceNotFoundException → 404
    public Product getById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", id));
    }

    public Product create(Product product) {
        // İstemci body'de "id" gönderse bile yeni nesne id'siz oluşturuluyor.
        // Aksi halde save() o id'li ürünü bulup üzerine yazardı (INSERT değil UPDATE).
        Product newProduct = new Product(product.getName(), product.getPrice(), product.getCategory());
        return productRepository.save(newProduct);
    }

    // Okuma + yazma tek transaction'da: ikisi birlikte ya olur ya olmaz.
    // Ürün yoksa getById() exception fırlatıyor, transaction rollback ediliyor.
    @Transactional
    public Product update(Long id, Product updated) {
        Product existing = getById(id);
        existing.setName(updated.getName());
        existing.setPrice(updated.getPrice());
        existing.setCategory(updated.getCategory());
        return productRepository.save(existing);   // id dolu → UPDATE
    }

    @Transactional
    public void delete(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException("Product", id);
        }
        productRepository.deleteById(id);
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
}
