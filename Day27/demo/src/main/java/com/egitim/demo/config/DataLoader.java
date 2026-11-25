package com.egitim.demo.config;

import com.egitim.demo.model.Product;
import com.egitim.demo.repository.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

// data.sql'in yerine: uygulama açılınca bir kez çalışıyor.
// MySQL'de veri kalıcı olduğu için, ürünleri sadece tablo BOŞSA ekliyor;
// yoksa her yeniden başlatmada aynı 5 ürün tekrar eklenirdi.
@Component
public class DataLoader implements CommandLineRunner {

    private final ProductRepository productRepository;

    public DataLoader(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public void run(String... args) {
        if (productRepository.count() > 0) {
            return;
        }
        productRepository.saveAll(List.of(
                new Product("Laptop",   15000, "Elektronik"),
                new Product("Fare",       250, "Elektronik"),
                new Product("Kalem",       10, "Kırtasiye"),
                new Product("Defter",      25, "Kırtasiye"),
                new Product("Kulaklık",  1200, "Elektronik")
        ));
    }
}
