package com.egitim.demo.controller;

import com.egitim.demo.dto.CategoryCreateRequest;
import com.egitim.demo.model.Category;
import com.egitim.demo.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    // POST /api/categories   Body: {"name": "Kitap", "description": "Roman, ders kitabı"}
    @PostMapping
    public ResponseEntity<Category> create(@Valid @RequestBody CategoryCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryService.createCategory(request));
    }

    // GET /api/categories → her kategori ürünleriyle birlikte
    @GetMapping
    public List<Category> getAll() {
        return categoryService.getAllCategories();
    }

    // GET /api/categories/1 → {"id":1, "name":"Elektronik", ..., "products":[...]}
    // Yoksa GlobalExceptionHandler → 404 "Category 1 bulunamadı."
    @GetMapping("/{id}")
    public Category getById(@PathVariable Long id) {
        return categoryService.getCategoryById(id);
    }
}
