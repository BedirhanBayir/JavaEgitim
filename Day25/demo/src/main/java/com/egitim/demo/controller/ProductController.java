package com.egitim.demo.controller;

import com.egitim.demo.dto.ProductCreateRequest;
import com.egitim.demo.model.Product;
import com.egitim.demo.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
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

    @GetMapping("/{id}")
    public ResponseEntity<Product> getById(@PathVariable Long id) {
        return productService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
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
    public ResponseEntity<Product> update(@PathVariable Long id,
                                          @Valid @RequestBody ProductCreateRequest request) {
        return productService.update(id, toProduct(request))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/category/{category}")
    public ResponseEntity<String> deleteByCategory(@PathVariable String category) {
        int count = productService.deleteByCategory(category);
        return ResponseEntity.ok(count + " ürün silindi.");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return productService.delete(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }

    // DTO → Entity. Validasyondan geçtiği için price null olamaz.
    private Product toProduct(ProductCreateRequest request) {
        return new Product(request.getName(), request.getPrice(), request.getCategory());
    }

    // ===================== Validasyon hataları (geçici) =====================
    // Sadece bu controller'daki hataları yakalıyor. Day 26'da @ControllerAdvice'a taşınacak.
    // Cevap: {"name": "Ürün adı boş olamaz.", "price": "Fiyat negatif olamaz."}
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationErrors(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new LinkedHashMap<>();
        // Bir alan birden fazla kuralı bozabiliyor ("" hem @NotBlank hem @Size).
        // put() kullansaydım mesajlardan biri kaybolurdu; merge ile ikisi birleşiyor.
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.merge(error.getField(), error.getDefaultMessage(), (a, b) -> a + " " + b));
        return ResponseEntity.badRequest().body(errors);
    }
}
