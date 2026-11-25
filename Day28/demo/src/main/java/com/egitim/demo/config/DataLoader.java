package com.egitim.demo.config;

import com.egitim.demo.model.Category;
import com.egitim.demo.model.Product;
import com.egitim.demo.repository.CategoryRepository;
import com.egitim.demo.repository.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

// Uygulama açılınca bir kez çalışıyor. Veritabanı BOŞSA 2 kategori ve 5 ürün ekliyor;
// MySQL'de veri kalıcı olduğu için sonraki açılışlarda hiçbir şey yapmıyor.
@Component
public class DataLoader implements CommandLineRunner {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    public DataLoader(CategoryRepository categoryRepository, ProductRepository productRepository) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
    }

    @Override
    public void run(String... args) {
        if (categoryRepository.count() > 0 || productRepository.count() > 0) {
            return;
        }

        // Önce kategoriler: ürünlerin category_id'si için kategorilerin id'si olmalı
        Category elektronik = categoryRepository.save(new Category("Elektronik", "Bilgisayar, telefon ve aksesuarlar"));
        Category kirtasiye  = categoryRepository.save(new Category("Kırtasiye", "Okul ve ofis malzemeleri"));

        productRepository.saveAll(List.of(
                new Product("Laptop",   15000, elektronik),
                new Product("Fare",       250, elektronik),
                new Product("Kalem",       10, kirtasiye),
                new Product("Defter",      25, kirtasiye),
                new Product("Kulaklık",  1200, elektronik)
        ));
    }
}
