package com.egitim.demo.service;

import com.egitim.demo.dto.CategoryCreateRequest;
import com.egitim.demo.exception.ResourceNotFoundException;
import com.egitim.demo.model.Category;
import com.egitim.demo.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Category createCategory(CategoryCreateRequest request) {
        Category category = new Category(request.getName(), request.getDescription());
        return categoryRepository.save(category);
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    // Ürünler (products) LAZY: bu sorgu sadece categories tablosunu okuyor.
    // Ürünler, JSON'a yazılırken getProducts() çağrılınca ayrı bir SELECT ile geliyor.
    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", id));
    }
}
