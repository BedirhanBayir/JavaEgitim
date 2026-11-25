package com.egitim.demo.controller;

import com.egitim.demo.dto.ProductCreateRequest;
import com.egitim.demo.model.Product;
import com.egitim.demo.service.ProductService;
import jakarta.validation.Valid;
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

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    // Constructor injection: Spring, ProductService bean'ini buraya kendisi veriyor.
    // Tek constructor olduğu için @Autowired yazmak zorunlu değil.
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public List<Product> getAll() {
        return productService.getAll();
    }

    @GetMapping("/count")
    public int getCount() {
        return productService.count();
    }

    @GetMapping("/categories")
    public Set<String> getCategories() {
        return productService.getCategories();
    }

    @GetMapping("/search")
    public List<Product> search(@RequestParam String name) {
        return productService.searchByName(name);
    }

    @GetMapping("/filter")
    public List<Product> filter(@RequestParam String category,
                                @RequestParam(required = false) Double minPrice,
                                @RequestParam(required = false) Double maxPrice) {
        return productService.filter(category, minPrice, maxPrice);
    }

    @GetMapping("/category/{category}")
    public List<Product> getByCategory(@PathVariable String category) {
        return productService.getByCategory(category);
    }

    // Ürün yoksa service ResourceNotFoundException fırlatıyor,
    // GlobalExceptionHandler 404 + ErrorResponse dönüyor. Controller'da if/else kalmadı.
    @GetMapping("/{id}")
    public Product getById(@PathVariable Long id) {
        return productService.getById(id);
    }

    // Day 25: body artık Product değil ProductCreateRequest.
    // @Valid, metoda girmeden önce DTO'daki kuralları kontrol ediyor;
    // kural bozulursa metot hiç çalışmıyor, MethodArgumentNotValidException fırlıyor.
    @PostMapping
    public ResponseEntity<Product> create(@Valid @RequestBody ProductCreateRequest request) {
        Product product = toProduct(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.create(product));
    }

    // PUT da aynı kurallarla kontrol ediliyor (Day 18'deki isValid'in yerine)
    @PutMapping("/{id}")
    public Product update(@PathVariable Long id, @Valid @RequestBody ProductCreateRequest request) {
        return productService.update(id, toProduct(request));
    }

    @DeleteMapping("/category/{category}")
    public ResponseEntity<String> deleteByCategory(@PathVariable String category) {
        int count = productService.deleteByCategory(category);
        return ResponseEntity.ok(count + " ürün silindi.");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // DTO → Entity. Validasyondan geçtiği için price null olamaz.
    private Product toProduct(ProductCreateRequest request) {
        return new Product(request.getName(), request.getPrice(), request.getCategory());
    }
}
